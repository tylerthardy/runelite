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
package net.runelite.client.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import lombok.AccessLevel;
import lombok.Getter;

public abstract class PluginPanel extends JPanel
{
	static final int PANEL_WIDTH = 225;
	static final int SCROLLBAR_WIDTH = 17;
	private static final int OFFSET = 6;
	private static final EmptyBorder BORDER_PADDING = new EmptyBorder(OFFSET, OFFSET, OFFSET, OFFSET);

	@Getter(AccessLevel.PROTECTED)
	private final JScrollPane scrollPane;

	@Getter(AccessLevel.PACKAGE)
	private final JPanel wrappedPanel;

	public PluginPanel()
	{
		super();
		setBorder(BORDER_PADDING);
		setLayout(new GridLayout(0, 1, 0, 3));

		final JPanel northPanel = new JPanel();
		northPanel.setLayout(new BorderLayout());
		northPanel.add(this, BorderLayout.NORTH);

		scrollPane = new JScrollPane(northPanel);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16); //Otherwise scrollspeed is really slow
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		wrappedPanel = new JPanel();

		// Adjust the preferred size to expand to width of scrollbar to
		// to preven scrollbar overlapping over contents
		wrappedPanel.setPreferredSize(new Dimension(PluginPanel.PANEL_WIDTH + SCROLLBAR_WIDTH, 0));
		wrappedPanel.setLayout(new BorderLayout());
		wrappedPanel.add(scrollPane, BorderLayout.CENTER);
	}

	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(PANEL_WIDTH, super.getPreferredSize().height);
	}

	public void onActivate()
	{
	}

	public void onDeactivate()
	{
	}
}
