/*
 * Copyright (c) 2017, Adam <Adam@sigterm.info>
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
package net.runelite.client.callback;

import com.google.common.eventbus.EventBus;
import com.google.inject.Injector;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.MainBufferProvider;
import net.runelite.api.MenuAction;
import net.runelite.api.MessageNode;
import net.runelite.api.PacketBuffer;
import net.runelite.api.Point;
import net.runelite.api.Projectile;
import net.runelite.api.Region;
import net.runelite.api.RenderOverview;
import net.runelite.api.TextureProvider;
import net.runelite.api.WorldMapManager;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.ProjectileMoved;
import net.runelite.api.events.SetMessage;
import net.runelite.api.widgets.Widget;
import static net.runelite.api.widgets.WidgetID.WORLD_MAP;
import net.runelite.client.RuneLite;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.game.DeathChecker;
import net.runelite.client.input.KeyManager;
import net.runelite.client.input.MouseManager;
import net.runelite.client.task.Scheduler;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayRenderer;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Hooks
{
	// must be public as the mixins use it
	public static final Logger log = LoggerFactory.getLogger(Hooks.class);

	private static final long CHECK = 600; // ms - how often to run checks

	private static final Injector injector = RuneLite.getInjector();
	private static final Client client = injector.getInstance(Client.class);
	public static final EventBus eventBus = injector.getInstance(EventBus.class);
	private static final Scheduler scheduler = injector.getInstance(Scheduler.class);
	private static final InfoBoxManager infoBoxManager = injector.getInstance(InfoBoxManager.class);
	private static final ChatMessageManager chatMessageManager = injector.getInstance(ChatMessageManager.class);
	private static final OverlayRenderer renderer = injector.getInstance(OverlayRenderer.class);
	private static final MouseManager mouseManager = injector.getInstance(MouseManager.class);
	private static final KeyManager keyManager = injector.getInstance(KeyManager.class);
	private static final DeathChecker death = new DeathChecker(client, eventBus);
	private static final GameTick tick = new GameTick();

	private static long lastCheck;

	public static void clientMainLoop(Client client, boolean arg1)
	{
		long now = System.currentTimeMillis();

		if (now - lastCheck < CHECK)
		{
			return;
		}

		lastCheck = now;

		try
		{
			death.check();

			// tick pending scheduled tasks
			scheduler.tick();

			// cull infoboxes
			infoBoxManager.cull();

			chatMessageManager.process();

			checkWorldMap();
		}
		catch (Exception ex)
		{
			log.warn("error during main loop tasks", ex);
		}
	}

	/**
	 * When the world map opens it loads about ~100mb of data into memory, which
	 * represents about half of the total memory allocated by the client.
	 * This gets cached and never released, which causes GC pressure which can affect
	 * performance. This method reinitailzies the world map cache, which allows the
	 * data to be garbage collecged, and causes the map data from disk each time
	 * is it opened.
	 */
	private static void checkWorldMap()
	{
		Widget widget = client.getWidget(WORLD_MAP, 0);
		if (widget != null)
		{
			return;
		}

		RenderOverview renderOverview = client.getRenderOverview();
		if (renderOverview == null)
		{
			return;
		}

		WorldMapManager manager = renderOverview.getWorldMapManager();

		if (manager != null && manager.isLoaded())
		{
			log.debug("World map was closed, reinitializing");
			renderOverview.initializeWorldMap(renderOverview.getWorldMapData());
		}
	}

	public static MouseEvent mousePressed(MouseEvent mouseEvent)
	{
		return mouseManager.processMousePressed(mouseEvent);
	}

	public static MouseEvent mouseReleased(MouseEvent mouseEvent)
	{
		return mouseManager.processMouseReleased(mouseEvent);
	}

	public static MouseEvent mouseClicked(MouseEvent mouseEvent)
	{
		return mouseManager.processMouseClicked(mouseEvent);
	}

	public static MouseEvent mouseEntered(MouseEvent mouseEvent)
	{
		return mouseManager.processMouseEntered(mouseEvent);
	}

	public static MouseEvent mouseExited(MouseEvent mouseEvent)
	{
		return mouseManager.processMouseExited(mouseEvent);
	}

	public static MouseEvent mouseDragged(MouseEvent mouseEvent)
	{
		return mouseManager.processMouseDragged(mouseEvent);
	}

	public static MouseEvent mouseMoved(MouseEvent mouseEvent)
	{
		return mouseManager.processMouseMoved(mouseEvent);
	}

	public static void mouseWheelMoved(MouseWheelEvent event)
	{
		mouseManager.processMouseWheelMoved(event);
	}

	public static void keyPressed(KeyEvent keyEvent)
	{
		keyManager.processKeyPressed(keyEvent);
	}

	public static void keyReleased(KeyEvent keyEvent)
	{
		keyManager.processKeyReleased(keyEvent);
	}

	public static void keyTyped(KeyEvent keyEvent)
	{
		keyManager.processKeyTyped(keyEvent);
	}

	public static void draw(MainBufferProvider mainBufferProvider, Graphics graphics, int x, int y)
	{
		final BufferedImage image = (BufferedImage) mainBufferProvider.getImage();
		final Graphics2D graphics2d = (Graphics2D) image.getGraphics();

		try
		{
			renderer.render(graphics2d, OverlayLayer.ALWAYS_ON_TOP);
		}
		catch (Exception ex)
		{
			log.warn("Error during overlay rendering", ex);
		}

		renderer.provideScreenshot(image);
	}

	public static void drawRegion(Region region, int var1, int var2, int var3, int var4, int var5, int var6)
	{
		MainBufferProvider bufferProvider = (MainBufferProvider) client.getBufferProvider();
		BufferedImage image = (BufferedImage) bufferProvider.getImage();
		Graphics2D graphics2d = (Graphics2D) image.getGraphics();

		try
		{
			renderer.render(graphics2d, OverlayLayer.ABOVE_SCENE);
		}
		catch (Exception ex)
		{
			log.warn("Error during overlay rendering", ex);
		}
	}

	public static void drawAboveOverheads(TextureProvider textureProvider, int var1)
	{
		MainBufferProvider bufferProvider = (MainBufferProvider) client.getBufferProvider();
		BufferedImage image = (BufferedImage) bufferProvider.getImage();
		Graphics2D graphics2d = (Graphics2D) image.getGraphics();

		try
		{
			renderer.render(graphics2d, OverlayLayer.UNDER_WIDGETS);
		}
		catch (Exception ex)
		{
			log.warn("Error during overlay rendering", ex);
		}
	}

	public static void drawAfterWidgets()
	{
		MainBufferProvider bufferProvider = (MainBufferProvider) client.getBufferProvider();
		BufferedImage image = (BufferedImage) bufferProvider.getImage();
		Graphics2D graphics2d = (Graphics2D) image.getGraphics();

		try
		{
			renderer.render(graphics2d, OverlayLayer.ABOVE_WIDGETS);
		}
		catch (Exception ex)
		{
			log.warn("Error during overlay rendering", ex);
		}
	}

	public static void menuActionHook(int actionParam, int widgetId, int menuAction, int id, String menuOption, String menuTarget, int var6, int var7)
	{
		/* Along the way, the RuneScape client may change a menuAction by incrementing it with 2000.
		 * I have no idea why, but it does. Their code contains the same conditional statement.
		 */
		if (menuAction >= 2000)
		{
			menuAction -= 2000;
		}

		MenuOptionClicked menuOptionClicked = new MenuOptionClicked();
		menuOptionClicked.setActionParam(actionParam);
		menuOptionClicked.setMenuOption(menuOption);
		menuOptionClicked.setMenuTarget(menuTarget);
		menuOptionClicked.setMenuAction(MenuAction.of(menuAction));
		menuOptionClicked.setId(id);
		menuOptionClicked.setWidgetId(widgetId);

		log.debug("Menu action clicked: {}", menuOptionClicked);

		eventBus.post(menuOptionClicked);
	}

	public static void addMenuEntry(String option, String target, int type, int identifier, int param0, int param1)
	{
		if (log.isTraceEnabled())
		{
			log.trace("Menu entry added {} {}", option, target);
		}

		MenuEntryAdded menuEntry = new MenuEntryAdded(option, target, type, identifier, param0, param1);

		eventBus.post(menuEntry);
	}

	public static void addChatMessage(int type, String name, String message, String sender)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Chat message type {}: {}", ChatMessageType.of(type), message);
		}

		ChatMessageType chatMessageType = ChatMessageType.of(type);
		ChatMessage chatMessage = new ChatMessage(chatMessageType, name, message, sender);

		eventBus.post(chatMessage);
	}

	/**
	 * Called when a projectile is set to move towards a point. For
	 * projectiles that target the ground, like AoE projectiles from
	 * Lizardman Shamans, this is only called once
	 *
	 * @param projectile The projectile being moved
	 * @param targetX X position of where the projectile is being moved to
	 * @param targetY Y position of where the projectile is being moved to
	 * @param targetZ Z position of where the projectile is being moved to
	 * @param cycle
	 */
	public static void projectileMoved(Projectile projectile, int targetX, int targetY, int targetZ, int cycle)
	{
		Point position = new Point(targetX, targetY);
		ProjectileMoved projectileMoved = new ProjectileMoved();
		projectileMoved.setProjectile(projectile);
		projectileMoved.setPosition(position);
		projectileMoved.setPlane(targetZ);
		eventBus.post(projectileMoved);
	}

	public static void setMessage(MessageNode messageNode, int type, String name, String sender, String value)
	{
		SetMessage setMessage = new SetMessage();
		setMessage.setMessageNode(messageNode);
		setMessage.setType(ChatMessageType.of(type));
		setMessage.setName(name);
		setMessage.setSender(sender);
		setMessage.setValue(value);

		eventBus.post(setMessage);
	}

	public static void onNpcUpdate(boolean var0, PacketBuffer var1)
	{
		eventBus.post(tick);
	}
}
