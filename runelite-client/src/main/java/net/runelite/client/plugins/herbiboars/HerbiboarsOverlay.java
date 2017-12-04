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
package net.runelite.client.plugins.herbiboars;

import com.google.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.GroundObject;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.api.Region;
import net.runelite.api.Tile;
import net.runelite.api.Varbits;
import net.runelite.client.RuneLite;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HerbiboarsOverlay extends Overlay
{
	private static final Logger logger = LoggerFactory.getLogger(HerbiboarsOverlay.class);

	private final Client client;
	private final RuneLite runelite;

	private static final int REGION_SIZE = 104;
	private static final int MAX_DISTANCE = 2400;

	private final Varbits[] varbits = {
			Varbits.HB_TRAIL_31303,
			Varbits.HB_TRAIL_31306,
			Varbits.HB_TRAIL_31309,
			Varbits.HB_TRAIL_31312,
			Varbits.HB_TRAIL_31315,
			Varbits.HB_TRAIL_31318,
			Varbits.HB_TRAIL_31321,
			Varbits.HB_TRAIL_31324,
			Varbits.HB_TRAIL_31327,
			Varbits.HB_TRAIL_31330,
			Varbits.HB_TRAIL_31333,
			Varbits.HB_TRAIL_31336,
			Varbits.HB_TRAIL_31339,
			Varbits.HB_TRAIL_31342,
			Varbits.HB_TRAIL_31345,
			Varbits.HB_TRAIL_31348,
			Varbits.HB_TRAIL_31351,
			Varbits.HB_TRAIL_31354,
			Varbits.HB_TRAIL_31357,
			Varbits.HB_TRAIL_31360,
			Varbits.HB_TRAIL_31363,
			Varbits.HB_TRAIL_31366,
			Varbits.HB_TRAIL_31369,
			Varbits.HB_TRAIL_31372,
			Varbits.HB_STARTED,
			Varbits.HB_FINISH
	};

	@Inject
	public HerbiboarsOverlay(RuneLite runelite) //, HerbiboarConfig config)
	{
		super(OverlayPosition.DYNAMIC);
		this.runelite = runelite;
		this.client = runelite.getClient();
		//this.config = config;
	}

	private final int X_START = 0;
	private final int Y_START = 100;
	private int x = X_START;
	private int y = Y_START;

	private Color[] colorz = {
			Color.WHITE,
			Color.RED,
			Color.CYAN,
			Color.GREEN,
			Color.YELLOW
	};

	private final ArrayList<Integer> startObjectIds = new ArrayList<Integer>(Arrays.asList(
			30519,
			30520,
			30521,
			30522,
			30523
	));
	private final List<Point> endLocations = new ArrayList<Point>(Arrays.asList(
			new Point(3693, 3798),
			new Point(3702, 3808),
			new Point(3703, 3826),
			new Point(3710, 3881),
			null,
			new Point(3715, 3840),
			new Point(3751, 3849),
			new Point(3685, 3869),
			new Point(3681, 3863)
	));

	private Set<Integer> shownTrails = new HashSet<Integer>();
	private HerbiboarTrail currentTrail;
	private int currentPath;

	@Override
	public Dimension render(Graphics2D graphics)
	{
		startText(X_START, Y_START);
		drawLine(graphics, client.getLocalPlayer().getWorldLocation().toString(),Color.WHITE);
		int finishId = client.getSetting(Varbits.HB_FINISH);
		for (HerbiboarTrail trail : HerbiboarTrail.values())
		{
			int trailId = trail.getTrailId();
			int value =  client.getSetting(Varbits.valueOf("HB_TRAIL_" + trailId));
			if (value > 0)
			{
				shownTrails.add(trail.getTrailId());
				shownTrails.add(trail.getTrailId() + 1);
			}
			if (value == 1 || value == 2)
			{
				currentTrail = trail;
				currentPath = value;
			}
		}
		if (finishId > 0 && currentTrail != null)
		{
			shownTrails.add(currentTrail.getTrailId());
			shownTrails.add(currentTrail.getTrailId() + 1);
			currentTrail = null;
			currentPath = -1;
		}

		if (currentTrail != null || finishId > 0)
		{
			Region region = client.getRegion();
			Tile[][][] tiles = region.getTiles();
			Tile closestTile;

			int z = client.getPlane();
			for (int x = 0; x < REGION_SIZE; ++x)
			{
				for (int y = 0; y < REGION_SIZE; ++y)
				{
					Tile tile = tiles[z][x][y];
					Player player = client.getLocalPlayer();
					if (tile == null || player == null)
					{
						continue;
					}

					//Draw game objects
					GameObject[] gameObjects = tile.getGameObjects();
					for (GameObject gameObject : gameObjects)
					{
						if (gameObject == null)
						{
							continue;
						}
						Point loc = gameObject.getWorldLocation();
						//End object to trigger next trail
						if (currentTrail != null && loc.equals(currentTrail.getObjectLoc(currentPath)))
						{
							OverlayUtil.renderTileOverlay(graphics, gameObject, "", Color.CYAN);
							break;
						}
						//Herbiboar tunnel
						if (finishId > 0)
						{
							if (loc.equals(endLocations.get(finishId - 1)))
							{
								OverlayUtil.renderTileOverlay(graphics, gameObject, "", Color.CYAN);
								break;
							}
						}
					}

					//Draw ground objects
					GroundObject groundObject = tile.getGroundObject();
					if (groundObject == null)
					{
						continue;
					}
					if (player.getLocalLocation().distanceTo(groundObject.getLocalLocation()) > MAX_DISTANCE)
					{
						continue;
					}
					//Trails
					int id = groundObject.getId();
					if (shownTrails.contains(id))
					{
						Color color;
						if (currentTrail != null && (currentTrail.getTrailId() == id || currentTrail.getTrailId() + 1 == id))
							color = Color.RED;
						else
							color = Color.WHITE;
						OverlayUtil.renderTileOverlay(graphics, groundObject, "", color);
					}
					//Herbiboar tunnel
					if (finishId > 0)
					{
						Point loc = groundObject.getWorldLocation();
						if (loc.equals(endLocations.get(finishId - 1)))
						{
							OverlayUtil.renderTileOverlay(graphics, groundObject, "", Color.CYAN);
							break;
						}
					}
				}
			}
		}
		else
		{
			Region region = client.getRegion();
			Tile[][][] tiles = region.getTiles();
			Tile closestTile;

			int z = client.getPlane();
			for (int x = 0; x < REGION_SIZE; ++x)
			{
				for (int y = 0; y < REGION_SIZE; ++y)
				{
					Tile tile = tiles[z][x][y];
					Player player = client.getLocalPlayer();
					if (tile == null || player == null)
					{
						continue;
					}

					GameObject[] gameObjects = tile.getGameObjects();
					for (GameObject gameObject : gameObjects)
					{
						if (gameObject == null)
						{
							continue;
						}
						int id = gameObject.getId();
						if (startObjectIds.contains(id))
						{
							OverlayUtil.renderTileOverlay(graphics, gameObject, "", Color.cyan);
						}
					}
				}
			}
		}

		return null;
	}

	public void endTrail()
	{
		shownTrails.clear();
		currentTrail = null;
		currentPath = -1;
	}

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
}
