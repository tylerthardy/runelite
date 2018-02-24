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
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.util.QueryRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class HerbiboarsOverlay extends Overlay
{
	private static final Logger logger = LoggerFactory.getLogger(HerbiboarsOverlay.class);

	private final Client client;
	private final HerbiboarConfig config;

	@Inject
	public HerbiboarsOverlay(Client client, HerbiboarConfig config)
	{
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_WIDGETS);
		this.client = client;
		this.config = config;
	}

	private static final int REGION_SIZE = 104;
	private static final int MAX_DISTANCE = 2400;
	private final ArrayList<Integer> startObjectIds = new ArrayList<>(Arrays.asList(
			30519,
			30520,
			30521,
			30522,
			30523
	));
	private final List<Point> endLocations = new ArrayList<>(Arrays.asList(
			new Point(3693, 3798),
			new Point(3702, 3808),
			new Point(3703, 3826),
			new Point(3710, 3881),
			new Point(3700, 3877),
			new Point(3715, 3840),
			new Point(3751, 3849),
			new Point(3685, 3869),
			new Point(3681, 3863)
	));

	private Set<Integer> shownTrails = new HashSet<>();
	private HerbiboarTrail currentTrail;
	private int currentPath;

	@Override
	public Dimension render(Graphics2D graphics, java.awt.Point parent)
	{
		if (!config.enabled())
		{
			return null;
		}

		//Get trail data
		for (HerbiboarTrail trail : HerbiboarTrail.values())
		{
			int trailId = trail.getTrailId();
			int value = client.getSetting(Varbits.valueOf("HB_TRAIL_" + trailId));
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

		//Get finish data
		int finishId = client.getSetting(Varbits.HB_FINISH);
		if (finishId > 0 && currentTrail != null)
		{
			shownTrails.add(currentTrail.getTrailId());
			shownTrails.add(currentTrail.getTrailId() + 1);
			currentTrail = null;
			currentPath = -1;
		}

		/////Drawing (draw trails if there is a current trail or finished; otherwise, draw start rocks)
		if (currentTrail != null || finishId > 0)
		{
			Region region = client.getRegion();
			Tile[][][] tiles = region.getTiles();

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

					//Draw GameObjects (objects used to trigger next trails, and some tunnels)
					GameObject[] gameObjects = tile.getGameObjects();
					for (GameObject gameObject : gameObjects)
					{
						if (gameObject == null)
						{
							continue;
						}
						Point loc = gameObject.getWorldLocation();
						//GameObject to trigger next trail (mushrooms, mud, seaweed, etc)
						if (config.isObjectShown() && currentTrail != null && Arrays.asList(currentTrail.getObjectLocs(currentPath)).contains(loc))
						{
							OverlayUtil.renderTileOverlay(graphics, gameObject, "", config.getObjectColor());
							break;
						}
						//Herbiboar tunnel
						if (config.isTunnelShown() && finishId > 0)
						{
							if (loc.equals(endLocations.get(finishId - 1)))
							{
								OverlayUtil.renderTileOverlay(graphics, gameObject, "", config.getTunnelColor());
								break;
							}
						}
					}

					//Draw GroundObjects (tracks on trails, and some tunnels)
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
					if (config.isTrailShown() && shownTrails.contains(id) &&
							((currentTrail == null && finishId > 0) ||
							(currentTrail != null && currentTrail.getTrailId() != id && currentTrail.getTrailId() + 1 != id)))
					{
						OverlayUtil.renderTileOverlay(graphics, groundObject, "", config.getTrailColor());
					}
					//Herbiboar tunnel
					if (config.isTunnelShown() && finishId > 0)
					{
						Point loc = groundObject.getWorldLocation();
						if (loc.equals(endLocations.get(finishId - 1)))
						{
							OverlayUtil.renderTileOverlay(graphics, groundObject, "", config.getTunnelColor());
							break;
						}
					}
				}
			}
		}
		else
		{
			if (!config.isStartShown())
			{
				return null;
			}
			Region region = client.getRegion();
			Tile[][][] tiles = region.getTiles();

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
							OverlayUtil.renderTileOverlay(graphics, gameObject, "", config.getStartColor());
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
}