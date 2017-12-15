/*
 * Copyright (c) 2017, Tyler <http://github.com/tylerthardy>
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
package net.runelite.client.plugins.inventorycount;

import com.google.inject.Inject;
import net.runelite.api.*;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.RuneLite;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.Map;

public class InventoryCountOverlay extends Overlay
{
	private static final Logger logger = LoggerFactory.getLogger(InventoryCountOverlay.class);
	private final Client client;
	private final RuneLite runelite;

	@Inject
	public InventoryCountOverlay(RuneLite runelite, @Nullable Client client, InventoryCount config)
	{
		setPosition(OverlayPosition.DYNAMIC);
		this.client = client;
		this.runelite = runelite;
	}

	@Override
	public Dimension render(Graphics2D graphics, java.awt.Point parent)
	{
		if (client.getGameState() != GameState.LOGGED_IN
				|| client.getWidget(WidgetInfo.LOGIN_CLICK_TO_PLAY_SCREEN) != null)
		{
			return null;
		}

		Font font = graphics.getFont();
		if (font.getSize() != 16)
		{
			Map attributes = font.getAttributes();
			attributes.put(TextAttribute.SIZE, 16);
			attributes.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
			font = Font.getFont(attributes);
			graphics.setFont(font);
		}

		Collection<Node> nodes = client.getItemContainers().getNodes();
		ItemContainer inventory  = null;
		for (Node n : nodes)
		{
			if (n.getHash() == 93)
			{
				inventory  = (ItemContainer)n;
				break;
			}
		}

		if (inventory == null)
		{
			return null;
		}

		int[] itemIds = inventory.getItemIds();

		int emptyCount = 0;
		for (int i : itemIds)
		{
			if (i < 0 || i == 1779)
				emptyCount++;
		}

		int openSlots = 28 - itemIds.length + emptyCount;

		Widget inventoryBag = client.getWidget(WidgetInfo.BUTTONS_BAG);
		if (inventoryBag == null)
		{
			return null;
		}

		Rectangle2D bounds = inventoryBag.getBounds().getBounds2D();
		if (bounds.getX() <= 0)
		{
			return null;
		}

		FontMetrics fm = graphics.getFontMetrics();
		int fontHeight = fm.getAscent();

		int x = (int)bounds.getX();
		int y = (int)bounds.getY() + fontHeight;
		graphics.setColor(Color.BLACK);
		graphics.drawString(String.valueOf(openSlots), x, y);
		graphics.setColor(Color.YELLOW);
		graphics.drawString(String.valueOf(openSlots), x - 1, y - 1);

		return null;
	}
}