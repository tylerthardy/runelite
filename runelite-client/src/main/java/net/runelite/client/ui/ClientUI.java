/*
 * Copyright (c) 2016-2017, Adam <Adam@sigterm.info>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.ui;

import java.applet.Applet;
import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.FontUIResource;
import com.google.common.eventbus.Subscribe;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.ConfigChanged;
import net.runelite.client.RuneLiteProperties;
import org.pushingpixels.substance.api.skin.SubstanceGraphiteLookAndFeel;
import org.pushingpixels.substance.internal.SubstanceSynapse;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;
import org.pushingpixels.substance.internal.utils.SubstanceTitlePaneUtilities;

@Slf4j
public class ClientUI extends JFrame
{
	private static final int PANEL_EXPANDED_WIDTH = PluginPanel.PANEL_WIDTH + PluginPanel.SCROLLBAR_WIDTH;
	private static final BufferedImage ICON;
	private static final String DISCORD_INVITE = "https://discord.gg/R4BQ8tU";

	@Getter
	private TrayIcon trayIcon;

	private final Applet client;
	private final RuneLiteProperties properties;
	private JPanel navContainer;
	private PluginToolbar pluginToolbar;
	private PluginPanel pluginPanel;

	static
	{
		BufferedImage icon = null;

		try
		{
			icon = ImageIO.read(ClientUI.class.getResourceAsStream("/runelite.png"));
		}
		catch (IOException e)
		{
			log.warn("Client icon failed to load", e);
		}

		ICON = icon;
	}

	public static ClientUI create(RuneLiteProperties properties, Applet client)
	{
		// Force heavy-weight popups/tooltips.
		// Prevents them from being obscured by the game applet.
		ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);
		JPopupMenu.setDefaultLightWeightPopupEnabled(false);

		// Do not render shadows under popups/tooltips.
		// Fixes black boxes under popups that are above the game applet.
		System.setProperty("jgoodies.popupDropShadowEnabled", "false");

		// Do not fill in background on repaint. Reduces flickering when
		// the applet is resized.
		System.setProperty("sun.awt.noerasebackground", "true");

		// Use substance look and feel
		try
		{
			UIManager.setLookAndFeel(new SubstanceGraphiteLookAndFeel());
		}
		catch (UnsupportedLookAndFeelException ex)
		{
			log.warn("unable to set look and feel", ex);
		}

		// Use custom UI font
		setUIFont(new FontUIResource(FontManager.getRunescapeFont()));

		return new ClientUI(properties, client);
	}

	private ClientUI(RuneLiteProperties properties, Applet client)
	{
		this.properties = properties;
		this.client = client;
		this.trayIcon = setupTrayIcon();

		init();
		setTitle(properties.getTitle());
		setIconImage(ICON);
		// Prevent substance from using a resize cursor for pointing
		getLayeredPane().setCursor(Cursor.getDefaultCursor());
		setLocationRelativeTo(getOwner());
		setResizable(true);
	}

	public void showWithChrome(boolean customChrome)
	{
		setUndecorated(customChrome);
		if (customChrome)
		{
			getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
		}
		pack();
		revalidateMinimumSize();
		setLocationRelativeTo(getOwner());
		if (customChrome)
		{
			try
			{
				BufferedImage discordIcon = ImageIO.read(ClientUI.class.getResourceAsStream("discord.png"));
				BufferedImage invertedIcon = ImageIO.read(ClientUI.class.getResourceAsStream("discord_inverted.png"));

				JButton discordButton = new JButton();
				discordButton.setToolTipText("Join Discord");
				discordButton.addMouseListener(new MouseAdapter()
				{
					@Override
					public void mouseClicked(MouseEvent e)
					{
						super.mouseClicked(e);
						try
						{
							Desktop.getDesktop().browse(new URL(DISCORD_INVITE).toURI());
						}
						catch (IOException | URISyntaxException ex)
						{
							log.warn("error opening browser", ex);
						}
					}
				});

				addButtonToTitleBar(discordButton, discordIcon, invertedIcon, 100);
			}
			catch (IOException ex)
			{
				log.warn("unable to load discord button", ex);
			}
		}

		setVisible(true);
		toFront();
		requestFocus();
		giveClientFocus();
	}

	private void giveClientFocus()
	{
		if (client instanceof Client)
		{
			final Canvas c = ((Client) client).getCanvas();
			c.requestFocusInWindow();
		}
		else
		{
			client.requestFocusInWindow();
		}
	}

	public void addButtonToTitleBar(JButton button, Image iconImage, Image invertedIconImage, int xOffset)
	{
		JComponent titleBar = SubstanceCoreUtilities.getTitlePaneComponent(this);

		if (titleBar == null)
		{
			return;
		}

		int size = titleBar.getHeight() - 6;

		ImageIcon icon = new ImageIcon(iconImage.getScaledInstance(size, size, Image.SCALE_SMOOTH));
		ImageIcon invertedIcon = new ImageIcon(invertedIconImage.getScaledInstance(size, size, Image.SCALE_SMOOTH));

		button.setIcon(icon);
		button.setRolloverIcon(invertedIcon);
		button.putClientProperty(SubstanceSynapse.FLAT_LOOK, Boolean.TRUE);
		button.putClientProperty(SubstanceTitlePaneUtilities.EXTRA_COMPONENT_KIND, SubstanceTitlePaneUtilities.ExtraComponentKind.TRAILING);
		button.setFocusable(false);
		button.setBounds(titleBar.getWidth() - xOffset, 2,
				icon.getIconWidth() + 4, icon.getIconHeight() + 2);

		titleBar.addComponentListener(new ComponentAdapter()
		{
			@Override
			public void componentResized(ComponentEvent e)
			{
				super.componentResized(e);
				button.setBounds(titleBar.getWidth() - xOffset, 1, button.getWidth(), button.getHeight());
			}
		});

		titleBar.add(button);

		revalidate();
		repaint();
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (!event.getGroup().equals("runelite"))
		{
			return;
		}

		if (!event.getKey().equals("gameSize"))
		{
			return;
		}

		if (client == null)
		{
			return;
		}

		String[] splitStr = event.getNewValue().split("x");
		int width = Integer.parseInt(splitStr[0]);
		int height = Integer.parseInt(splitStr[1]);

		// The upper bounds are defined by the applet's max size
		// The lower bounds are taken care of by ClientPanel's setMinimumSize

		if (width > 7680)
		{
			width = 7680;
		}

		if (height > 2160)
		{
			height = 2160;
		}

		Dimension size = new Dimension(width, height);

		client.setSize(size);
		client.setPreferredSize(size);

		client.getParent().setPreferredSize(size);
		client.getParent().setSize(size);

		pack();
	}

	private static void setUIFont(FontUIResource f)
	{
		final Enumeration keys = UIManager.getDefaults().keys();

		while (keys.hasMoreElements())
		{
			final Object key = keys.nextElement();
			final Object value = UIManager.get(key);

			if (value instanceof FontUIResource)
			{
				UIManager.put(key, f);
			}
		}
	}

	private TrayIcon setupTrayIcon()
	{
		if (!SystemTray.isSupported())
		{
			return null;
		}

		SystemTray systemTray = SystemTray.getSystemTray();
		TrayIcon trayIcon = new TrayIcon(ICON, properties.getTitle());
		trayIcon.setImageAutoSize(true);

		try
		{
			systemTray.add(trayIcon);
		}
		catch (AWTException ex)
		{
			log.debug("Unable to add system tray icon", ex);
			return trayIcon;
		}

		// bring to front when tray icon is clicked
		trayIcon.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				setVisible(true);
				setState(Frame.NORMAL); // unminimize
			}
		});

		return trayIcon;
	}

	private void init()
	{
		assert SwingUtilities.isEventDispatchThread();

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				checkExit();
			}
		});

		final JPanel container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));
		container.add(new ClientPanel(client));

		navContainer = new JPanel();
		navContainer.setLayout(new BorderLayout(0, 0));
		navContainer.setMinimumSize(new Dimension(0, 0));
		navContainer.setMaximumSize(new Dimension(0, Integer.MAX_VALUE));
		container.add(navContainer);

		pluginToolbar = new PluginToolbar(this);
		container.add(pluginToolbar);

		add(container);
	}

	private void revalidateMinimumSize()
	{
		// The JFrame only respects minimumSize if it was set by setMinimumSize, for some reason. (atleast on windows/native)
		this.setMinimumSize(this.getLayout().minimumLayoutSize(this));
	}

	void expand(PluginPanel panel)
	{
		if (pluginPanel != null)
		{
			navContainer.remove(0);
		}

		pluginPanel = panel;
		navContainer.setMinimumSize(new Dimension(PANEL_EXPANDED_WIDTH, 0));
		navContainer.setMaximumSize(new Dimension(PANEL_EXPANDED_WIDTH, Integer.MAX_VALUE));

		final JPanel wrappedPanel = panel.getWrappedPanel();
		navContainer.add(wrappedPanel);
		navContainer.revalidate();

		// panel.onActivate has to go after giveClientFocus so it can get focus if it needs.
		giveClientFocus();
		panel.onActivate();

		wrappedPanel.repaint();
		revalidateMinimumSize();
	}

	void contract()
	{
		boolean wasMinimumWidth = this.getWidth() == (int) this.getMinimumSize().getWidth();
		pluginPanel.onDeactivate();
		navContainer.remove(0);
		navContainer.setMinimumSize(new Dimension(0, 0));
		navContainer.setMaximumSize(new Dimension(0, Integer.MAX_VALUE));
		navContainer.revalidate();
		giveClientFocus();
		revalidateMinimumSize();
		if (wasMinimumWidth)
		{
			this.setSize((int) this.getMinimumSize().getWidth(), getHeight());
		}
		pluginPanel = null;
	}

	private void checkExit()
	{
		int result = JOptionPane.OK_OPTION;

		// only ask if not logged out
		if (client != null && client instanceof Client && ((Client) client).getGameState() != GameState.LOGIN_SCREEN)
		{
			result = JOptionPane.showConfirmDialog(this, "Are you sure you want to exit?", "Exit", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
		}

		if (result == JOptionPane.OK_OPTION)
		{
			System.exit(0);
		}
	}

	public PluginToolbar getPluginToolbar()
	{
		return pluginToolbar;
	}
}
