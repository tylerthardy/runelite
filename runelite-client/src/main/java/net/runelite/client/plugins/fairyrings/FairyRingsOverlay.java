/*
 * Copyright (c) 2017, Tyler <https://github.com/tylerthardy>
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
package net.runelite.client.plugins.fairyrings;

import com.google.inject.Inject;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

class FairyRingsOverlay extends Overlay
{
	private FairyRings plugin;
	private FairyRingsConfig config;

	@Inject
	FairyRingsOverlay(FairyRings plugin, FairyRingsConfig config)
	{
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_WIDGETS);
		this.plugin = plugin;
		this.config = config;
	}

	private final int X_START = 0;
	private final int Y_START = 100;
	private int x = X_START;
	private int y = Y_START;
	private List<String> lines = new ArrayList<>();
	@Override
	public Dimension render(Graphics2D graphics, Point parent)
	{
		if (renderMethod(graphics)) return null;
		return null;
	}

	private boolean renderMethod(Graphics2D graphics)
	{
		startText(X_START, Y_START);
		drawLine(graphics, "Fairy Rings Tester", Color.YELLOW);

		if (lines.size() == 0)
		{
			return true;
		}
		for (String line: lines)
		{
			drawLine(graphics, line, Color.WHITE);
		}
		return false;
	}


	//Debug lines
	private void startText(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	private void drawLine(Graphics2D graphics, String str, Color col)
	{
		FontMetrics fm = graphics.getFontMetrics();
		y += fm.getHeight();
		graphics.setColor(Color.BLACK);
		graphics.drawString(str, x+1, y+1);
		graphics.setColor(col);
		graphics.drawString(str, x, y);
	}
	public void addLine(String str)
	{
		lines.add(str);
	}
	public void addLine(int i)
	{
		lines.add(Integer.toString(i));
	}
	public void resetLines()
	{
		lines.clear();
	}
	public int lineCount()
	{
		return lines.size();
	}
}
