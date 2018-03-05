/*
 * Copyright (c) 2017, Seth <Sethtroll3@gmail.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.plugins.runecraft;

import static net.runelite.api.ItemID.BINDING_NECKLACE;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Query;
import net.runelite.api.queries.EquipmentItemQuery;
import net.runelite.api.queries.InventoryItemQuery;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.RuneLite;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;

public class BindNeckOverlay extends Overlay
{
	private final RuneLite runelite;
	private final Client client;
	private final RunecraftConfig config;
	private final Font font = FontManager.getRunescapeSmallFont().deriveFont(Font.PLAIN, 16);
	int bindingCharges;

	@Inject
	BindNeckOverlay(RuneLite runelite, RunecraftConfig config)
	{
		setPosition(OverlayPosition.DYNAMIC);
		this.runelite = runelite;
		this.client = runelite.getClient();
		this.config = config;
		this.setDrawOverBankScreen(true);
	}

	@Override
	public Dimension render(Graphics2D graphics, Point parent)
	{
		if (!config.showBindNeck())
		{
			return null;
		}

		graphics.setFont(font);

		for (WidgetItem necklace : getNecklaceWidgetItems())
		{
			Color color = bindingCharges == 1 ? Color.RED : Color.WHITE;
			renderBindNeck(graphics, necklace.getCanvasBounds(), bindingCharges, color);
		}

		return null;
	}

	private Collection<WidgetItem> getNecklaceWidgetItems()
	{
		Query inventoryQuery = new InventoryItemQuery()
			.idEquals(BINDING_NECKLACE);
		WidgetItem[] inventoryWidgetItems = runelite.runQuery(inventoryQuery);

		Query equipmentQuery = new EquipmentItemQuery()
			.slotEquals(WidgetInfo.EQUIPMENT_AMULET)
			.idEquals(BINDING_NECKLACE);
		WidgetItem[] equipmentWidgetItems = runelite.runQuery(equipmentQuery);

		Collection<WidgetItem> necklaces = new ArrayList<>();
		necklaces.addAll(Arrays.asList(inventoryWidgetItems));
		necklaces.addAll(Arrays.asList(equipmentWidgetItems));
		return necklaces;
	}

	private void renderBindNeck(Graphics2D graphics, Rectangle bounds, int charges, Color color)
	{
		String text = charges <= 0 ? "?" : charges + "";
		FontMetrics fm = graphics.getFontMetrics();

		int textX = (int) bounds.getX();
		int textY = (int) bounds.getY() + fm.getHeight();

		//text shadow
		graphics.setColor(Color.BLACK);
		graphics.drawString(text, textX + 1, textY + 1);

		graphics.setColor(color);
		graphics.drawString(text, textX, textY);
	}
}
