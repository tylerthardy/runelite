/*
 * Copyright (c) 2017, Seth <Sethtroll3@gmail.com>
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
package net.runelite.client.plugins.slayer;

import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.ItemID;
import net.runelite.api.Query;
import net.runelite.api.queries.EquipmentItemQuery;
import net.runelite.api.queries.InventoryItemQuery;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.RuneLite;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

class SlayerOverlay extends Overlay
{
	private final RuneLite runelite = RuneLite.getRunelite();
	private final Client client = RuneLite.getClient();
	private final SlayerConfig config;
	private final Slayer plugin;
	private final Font font = FontManager.getRunescapeSmallFont().deriveFont(Font.PLAIN, 16);

	SlayerOverlay(Slayer plugin)
	{
		super(OverlayPosition.DYNAMIC);
		this.plugin = plugin;
		this.config = plugin.getConfig();
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (client.getGameState() != GameState.LOGGED_IN
			|| !config.enabled()
			|| client.getWidget(WidgetInfo.LOGIN_CLICK_TO_PLAY_SCREEN) != null)
		{
			return null;
		}

		if (!config.showItemOverlay())
		{
			return null;
		}

		int amount = plugin.getAmount();
		if (amount == 0)
		{
			return null;
		}

		graphics.setFont(font);

		for (WidgetItem item : getSlayerWidgetItems())
		{
			int itemId = item.getId();
			if (!slayerEquipment.contains(itemId) && !slayerJewelry.contains(itemId))
			{
				continue;
			}
			renderWidgetText(graphics, itemId, item.getCanvasBounds(), amount, Color.white);
		}

		return null;
	}

	private Collection<WidgetItem> getSlayerWidgetItems()
	{
		Query inventoryQuery = new InventoryItemQuery();
		WidgetItem[] inventoryWidgetItems = runelite.runQuery(inventoryQuery);

		Query equipmentQuery = new EquipmentItemQuery().slotEquals(WidgetInfo.EQUIPMENT_HELMET, WidgetInfo.EQUIPMENT_RING);
		WidgetItem[] equipmentWidgetItems = runelite.runQuery(equipmentQuery);

		Collection<WidgetItem> items = new ArrayList<>();
		items.addAll(Arrays.asList(inventoryWidgetItems));
		items.addAll(Arrays.asList(equipmentWidgetItems));
		return items;
	}

	private void renderWidgetText(Graphics2D graphics, int itemId, Rectangle bounds, int amount, Color color)
	{
		FontMetrics fm = graphics.getFontMetrics();

		int textX = (int) bounds.getX();
		int textY = (int) bounds.getY() + (slayerJewelry.contains(itemId) ? (int) bounds.getHeight() : fm.getHeight());

		//text shadow
		graphics.setColor(Color.BLACK);
		graphics.drawString(String.valueOf(amount), textX + 1, textY + 1);

		graphics.setColor(color);
		graphics.drawString(String.valueOf(amount), textX, textY);
	}

	private HashSet<Integer> slayerJewelry = new HashSet<>(Arrays.asList(
			ItemID.SLAYER_RING_1,
			ItemID.SLAYER_RING_2,
			ItemID.SLAYER_RING_3,
			ItemID.SLAYER_RING_4,
			ItemID.SLAYER_RING_5,
			ItemID.SLAYER_RING_6,
			ItemID.SLAYER_RING_7,
			ItemID.SLAYER_RING_8
	));
	private HashSet<Integer> slayerEquipment = new HashSet<>(Arrays.asList(
			ItemID.SLAYER_HELMET,
			ItemID.SLAYER_HELMET_I,
			ItemID.BLACK_SLAYER_HELMET,
			ItemID.BLACK_SLAYER_HELMET_I,
			ItemID.GREEN_SLAYER_HELMET,
			ItemID.GREEN_SLAYER_HELMET_I,
			ItemID.PURPLE_SLAYER_HELMET,
			ItemID.PURPLE_SLAYER_HELMET_I,
			ItemID.RED_SLAYER_HELMET,
			ItemID.RED_SLAYER_HELMET_I,
			ItemID.SLAYER_RING_ETERNAL,
			ItemID.ENCHANTED_GEM,
			ItemID.ETERNAL_GEM
	));
}
