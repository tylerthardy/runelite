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
package net.runelite.client.ui.overlay.tooltips;

import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.client.RuneLite;
import net.runelite.client.ui.overlay.Renderer;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TooltipRenderer implements Renderer
{
	private final Client client = RuneLite.getClient();

	private final int BORDER_SIZE = 2;
	private final int JSWING_BORDER_RIGHT = 5;
	private static final Color BACKGROUND_COLOR = new Color(Color.gray.getRed(), Color.gray.getGreen(), Color.gray.getBlue(), 127);
	private static final Color BORDER_COLOR = Color.black;
	private static final Color FONT_COLOR = Color.white;

	private static final String colorSplit = "<\\/?col=?([^>]+)?>";

	private Tooltip tooltip;

	private boolean leftSide = false;

	@Override
	public void render(BufferedImage clientBuffer)
	{
		Point mousePos = client.getMouseCanvasPosition();
		Graphics2D graphics = clientBuffer.createGraphics();
		Renderer.setAntiAliasing(graphics);
		drawTooltip(graphics, mousePos.getX(), mousePos.getY());
		graphics.dispose();

		this.tooltip = null;
	}

	public void add(Tooltip tooltip)
	{
		if (this.tooltip != null && tooltip.getPriority().compareTo(this.tooltip.getPriority()) < 0)
		{
			return;
		}

		this.tooltip = tooltip;
	}

	private void drawTooltip(Graphics2D graphics, int x, int y)
	{
		if (tooltip == null || tooltip.getText() == null || tooltip.getText().isEmpty())
		{
			return;
		}

		FontMetrics metrics = graphics.getFontMetrics();
		String tooltipText = tooltip.getText();
		int tooltipWidth = 0;
		int tooltipHeight = 0;
		int textHeight =  metrics.getHeight();

		//Tooltip size
		String[] lines = tooltipText.split("</br>");
		for (String line : lines)
		{
			String lineClean = line.replaceAll(colorSplit, "");
			int textWidth =  metrics.stringWidth(lineClean);
			if (textWidth > tooltipWidth)
			{
				tooltipWidth = textWidth;
			}

			tooltipHeight += textHeight;
		}

		//Position tooltip
		if (leftSide)
		{
			x = x - tooltipWidth;
			if (x < 0)
				x = 0;
		}
		else
		{
			int clientWidth = client.getCanvas().getWidth();
			if (x + tooltipWidth + JSWING_BORDER_RIGHT > clientWidth)
				x = clientWidth - tooltipWidth - JSWING_BORDER_RIGHT;
		}

		y = y - tooltipHeight;
		if (y < 0)
			y = 0;

		//Render tooltip - background
		graphics.setColor(BACKGROUND_COLOR);
		graphics.fillRect(x, y, tooltipWidth + BORDER_SIZE * 2, tooltipHeight + BORDER_SIZE);
		graphics.setColor(BORDER_COLOR);
		graphics.drawRect(x, y, tooltipWidth + BORDER_SIZE * 2, tooltipHeight + BORDER_SIZE);
		graphics.setColor(FONT_COLOR);


		//Render tooltip - text - line by line
		for (int i = 0; i < lines.length; i++)
		{
			String line = lines[i];

			Matcher m = Pattern.compile(colorSplit).matcher(line);

			int begin = 0;
			graphics.setColor(Color.white);
			while (m.find())
			{
				System.out.println(begin);
				String preText = line.substring(begin, m.start());
				graphics.drawString(preText, x, y + (i + 1) * textHeight);
				if (m.group(1) == null)
				{
					System.out.println("no color" + begin);
					//no color tag
					graphics.setColor(Color.white);
				}
				else
				{
					//color tag
					System.out.println("color" + begin);
					graphics.setColor(Color.decode("#" + m.group(1)));
				}
				begin = m.end();
				x += metrics.stringWidth(preText);
			}
			graphics.drawString(line.substring(begin, line.length()), x, y + (i + 1) * textHeight);
		}
	}
}
