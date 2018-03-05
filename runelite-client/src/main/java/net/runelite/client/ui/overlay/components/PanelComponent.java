/*
 * Copyright (c) 2017, Tomas Slusny <slusnucky@gmail.com>
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
package net.runelite.client.ui.overlay.components;

import com.google.common.base.Strings;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.runelite.client.ui.overlay.RenderableEntity;

public class PanelComponent implements RenderableEntity
{
	private static final int TOP_BORDER = 3;
	private static final int LEFT_BORDER = 6;
	private static final int RIGHT_BORDER = 6;
	private static final int BOTTOM_BORDER = 6;
	private static final int SEPARATOR = 2;

	@Data
	@AllArgsConstructor
	@RequiredArgsConstructor
	public static class Line
	{
		private final String left;
		private Color leftColor = Color.WHITE;
		private final String right;
		private Color rightColor = Color.WHITE;
	}

	@Setter
	@Nullable
	private String title;

	@Setter
	private Color titleColor = Color.WHITE;

	@Setter
	private Point position = new Point();

	@Getter
	private List<Line> lines = new ArrayList<>();

	@Setter
	private int width = 140;

	@Override
	public Dimension render(Graphics2D graphics, Point parent)
	{
		final Dimension dimension = new Dimension();
		final int elementNumber = (Strings.isNullOrEmpty(title) ? 0 : 1) + lines.size();
		int height = elementNumber == 0 ? 0 :
			TOP_BORDER + (graphics.getFontMetrics().getHeight() * elementNumber)
				+ SEPARATOR * elementNumber + BOTTOM_BORDER;
		dimension.setSize(width, height);

		if (dimension.height == 0)
		{
			return null;
		}

		final FontMetrics metrics = graphics.getFontMetrics();

		// Calculate panel dimensions
		int y = position.y + TOP_BORDER + metrics.getHeight();

		// Render background
		final BackgroundComponent backgroundComponent = new BackgroundComponent();
		backgroundComponent.setRectangle(new Rectangle(position.x, position.y, dimension.width, dimension.height));
		backgroundComponent.render(graphics, parent);

		// Render title
		if (!Strings.isNullOrEmpty(title))
		{
			final TextComponent titleComponent = new TextComponent();
			titleComponent.setText(title);
			titleComponent.setColor(titleColor);
			titleComponent.setPosition(new Point(position.x + (width - metrics.stringWidth(title)) / 2, y));
			titleComponent.render(graphics, parent);
			y += metrics.getHeight() + SEPARATOR;
		}

		// Render all lines
		for (final Line line : lines)
		{
			final TextComponent leftLineComponent = new TextComponent();
			leftLineComponent.setPosition(new Point(position.x + LEFT_BORDER, y));
			leftLineComponent.setText(line.getLeft());
			leftLineComponent.setColor(line.getLeftColor());
			leftLineComponent.render(graphics, parent);

			final TextComponent rightLineComponent = new TextComponent();
			rightLineComponent.setPosition(new Point(position.x +  width - RIGHT_BORDER - metrics.stringWidth(line.getRight()), y));
			rightLineComponent.setText(line.getRight());
			rightLineComponent.setColor(line.getRightColor());
			rightLineComponent.render(graphics, parent);
			y += metrics.getHeight() + SEPARATOR;
		}

		return dimension;
	}
}
