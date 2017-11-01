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
package net.runelite.client.ui.overlay;

import net.runelite.api.Client;
import net.runelite.client.RuneLite;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class TooltipRenderer implements Renderer
{
	private final List<Overlay> overlays = new ArrayList<>();
	private final Client client = RuneLite.getClient();

	private final int BORDER_SIZE = 2;
	private final int JSWING_BORDER_RIGHT = 5;
	private static final Color BACKGROUND_COLOR = new Color(Color.gray.getRed(), Color.gray.getGreen(), Color.gray.getBlue(), 127);
	private static final Color BORDER_COLOR = Color.black;
	private static final Color FONT_COLOR = Color.white;

	private boolean leftSide = false;

	public void add(Overlay overlay)
	{
		overlays.add(overlay);
	}

	@Override
	public void render(BufferedImage clientBuffer)
	{
		Graphics2D graphics = clientBuffer.createGraphics();
		Renderer.setAntiAliasing(graphics);
		drawTooltip(graphics, client.getMouseCanvasPosition().getX(), client.getMouseCanvasPosition().getY());
		graphics.dispose();
	}

	private void drawTooltip(Graphics2D graphics, int x, int y)
	{
		FontMetrics metrics = graphics.getFontMetrics();
		String tooltipText = "this is some tooltip text";
		int textWidth =  metrics.stringWidth(tooltipText);
		int textHeight =  metrics.getHeight();

		if (leftSide)
		{
			x = x - textWidth;
			if (x < 0)
				x = 0;
		}
		else
		{
			int clientWidth = client.getCanvas().getWidth();
			if (x + textWidth + JSWING_BORDER_RIGHT > clientWidth)
				x = clientWidth - textWidth - JSWING_BORDER_RIGHT;
		}

		y = y - textHeight;
		if (y < 0)
			y = 0;

		graphics.setColor(BORDER_COLOR);
		graphics.drawRect(x, y, textWidth + BORDER_SIZE * 2, textHeight + BORDER_SIZE);
		graphics.setColor(BACKGROUND_COLOR);
		graphics.fillRect(x, y, textWidth + BORDER_SIZE * 2, textHeight + BORDER_SIZE);
		graphics.setColor(FONT_COLOR);
		graphics.drawString(tooltipText, x + BORDER_SIZE, y + textHeight - BORDER_SIZE);
	}
}
