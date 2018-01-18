/*
 * Copyright (c) 2017, Aria <aria@ar1as.space>
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
package net.runelite.client.plugins.gargaoe;

import net.runelite.api.Client;
import net.runelite.api.GroundObject;
import net.runelite.api.Item;
import net.runelite.api.ItemComposition;
import net.runelite.api.ItemID;
import net.runelite.api.ItemLayer;
import net.runelite.api.Node;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.api.Region;
import net.runelite.api.Tile;
import net.runelite.api.queries.GroundObjectQuery;
import net.runelite.api.queries.TileObjectQuery;
import net.runelite.api.widgets.Widget;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.util.QueryRunner;
import net.runelite.http.api.item.ItemPrice;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GargAoeOverlay extends Overlay
{
	private final Client client;

	@Inject
	private QueryRunner queryRunner;

	@Inject
	ItemManager itemManager;

	@Inject
	public GargAoeOverlay(@Nullable Client client)
	{
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.UNDER_WIDGETS);
		this.client = client;
	}

	@Override
	public Dimension render(Graphics2D graphics, java.awt.Point parent)
	{
		renderShit(graphics);
		return null;
	}

	private void renderShit(Graphics2D graphics)
	{

		GroundObjectQuery groundObjectQuery = new GroundObjectQuery()
				.isWithinDistance(client.getLocalPlayer().getLocalLocation(), 500);

		GroundObject[] grObjs = queryRunner.runQuery(groundObjectQuery);

		for(GroundObject grObj : grObjs)
		{
			//System.out.println(String.format("%s: %s", grObj.getId(), grObj.getHash()));
			OverlayUtil.renderTileOverlay(graphics, grObj, grObj.getHash()+"", Color.GREEN);
		}
	}
}
