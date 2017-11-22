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
package net.runelite.client.plugins.herbiboar;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;

import com.google.inject.Inject;
import net.runelite.api.*;
import net.runelite.api.Point;
import net.runelite.api.queries.GameObjectQuery;
import net.runelite.client.RuneLite;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HerbiboarOverlay extends Overlay
{
	private static final Logger logger = LoggerFactory.getLogger(HerbiboarOverlay.class);

	private final Client client;
	private final RuneLite runelite;

	private static final int REGION_SIZE = 104;
	private static final int MAX_DISTANCE = 2400;
	private final Color[] colorSteps = {
			Color.RED,
			Color.ORANGE,
			Color.YELLOW,
			Color.GREEN,
			Color.BLUE,
			Color.MAGENTA,
			Color.PINK,
			Color.WHITE,
			Color.BLACK
	};
	private final ArrayList<Integer> startObjectIds = new ArrayList<Integer>(Arrays.asList(
			30519,
			30520,
			30521,
			30522,
			30523
	));

	private final int[] varbitIds = {
		1617,
		1618,
		1619,
		1620,
		1621
	};

	private Hashtable<Integer, Integer> pastVarbs = new Hashtable<Integer,Integer>();

	public int trailStep = 0;
	public int printStep = -1;
	public boolean trailStarted = false;
	private Point trailEnd = null;
	private HashMap<Point, Integer> trailTiles = new HashMap<Point, Integer>();
	private HashMap<Point, GameObject> trailObjects = new HashMap<Point, GameObject>();

	@Inject
	public HerbiboarOverlay(RuneLite runelite) //, HerbiboarConfig config)
	{
		super(OverlayPosition.DYNAMIC);
		this.runelite = runelite;
		this.client = runelite.getClient();
		//this.config = config;
	}

/*
	BANNED PLAYERS AKA NOTES
	----------------------

	get ids for all of the starting objects
	final path does not give xp drop


	tunnel is 30532
	tunnel can be game object or ground object

	rocks (start) 30519
	mushroom (start) 30520
	rock (start) 30521
	driftwood (start) 30523

*/

	private int last1617 = 0;
	private int last1620 = 0;

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (client.getGameState() != GameState.LOGGED_IN)
		{
			return null;
		}

		/*Font font = plugin.getFont();
		if (font != null)
		{
			graphics.setFont(font);
		}*/
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

				if (!trailStarted)
				{
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
				else
				{
					/*Point tileWorldLoc = Perspective.localToWorld(client, new Point(x, y));
					if (trailEnd != null && tileWorldLoc.distanceTo(trailEnd) < 3)
					{
						//highlighting objs for next step
						//or highlight the tunnel if it's the end (known by xp drop)
						//OverlayUtil.renderTileOverlay(graphics, tile., tileWorldLoc.toString(), Color.red);
					}*/

					GameObject[] gameObjects = tile.getGameObjects();
					for (GameObject go : gameObjects)
					{
						if (go != null)
						{
							int id = go.getId();
							Point loc = go.getWorldLocation();
							HerbiboarObject ho = HerbiboarObject.getObject(loc);
							if (ho != null)
							{
								OverlayUtil.renderTileOverlay(graphics, go, "" + ho.getName(), Color.pink);
							}
							/*if (id >= 30400 && id <= 30600)
							{
								if(trailObjects.get(loc) == null)
								{
									trailObjects.put(loc, go);
									System.out.println(String.format("Added %s at %s - total: %s", go.getId(), go.getWorldLocation(), trailObjects.size()));
								}
								//OverlayUtil.renderTileOverlay(graphics, go, "" + go.getId(), Color.green);
							}*/
						}
					}

					GroundObject groundObject = tile.getGroundObject();
					if (groundObject == null)
					{
						continue;
					}

					if (player.getLocalLocation().distanceTo(groundObject.getLocalLocation()) > MAX_DISTANCE)
					{
						continue;
					}

					int id = groundObject.getId();
					if (id >= 31300 && id <= 31399)
					{
						Renderable renderable = groundObject.getRenderable();
						Color color = colorSteps[trailStep];
						Point loc = groundObject.getWorldLocation();
						if (renderable.getModel() == null)
						{
							color = Color.white;
							OverlayUtil.renderTileOverlay(graphics, groundObject, "" + groundObject.getId(), color);
							continue;
						}
						if (trailTiles.containsKey(loc))
						{
							int colorStep = trailTiles.get(loc);
							color = colorSteps[colorStep];
						}
						else
						{
							trailTiles.put(loc, trailStep);
							if (printStep != trailStep)
							{
								printStep = trailStep;
								//System.out.println("Path: " + groundObject.getId());

								//int[] widgetSettings = client.getWidgetSettings();
								int[] clientSettings = client.getSettings();
								for (int varbId : varbitIds)
								{
									if (clientSettings.length > varbId)
									{
										pastVarbs.putIfAbsent(varbId, 0);
										int currVarb = clientSettings[varbId];
										if (pastVarbs.get(varbId) != null && pastVarbs.get(varbId) != currVarb)
											System.out.println(String.format("%s,%s,%s,%s", groundObject.getId(), varbId, prettyPrintInt(pastVarbs.get(varbId)), prettyPrintInt(currVarb)));
											//System.out.println(String.format("%s: %s -> %s (%s->%s)", varbId, prettyPrintInt(pastVarbs.get(varbId)), prettyPrintInt(currVarb), pastVarbs.get(varbId), currVarb));
										pastVarbs.put(varbId, currVarb);
									}
								}
							}
							//HerbiboarTrail trail = HerbiboarTrail.getTrail(loc);
							//trailEnd = trail.getOtherEnd(loc);
						}

						OverlayUtil.renderTileOverlay(graphics, groundObject, "" + groundObject.getId(), color);
					}
				}
			}
		}

		return null;
	}
	private static String prettyPrintInt(int value)
	{
		String s = Integer.toBinaryString(value);
		while (s.length() < 32)
		{
			s = "0" + s;
		}

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 32; i += 8)
		{
			String substr = s.substring(i, i + 8);
			if (i > 0)
			{
				sb.append(' ');
			}
			sb.append(substr);
		}
		return sb.toString();
	}

	public void newTrail()
	{
		trailStarted = true;
	}

	public void endTrail()
	{
		trailTiles.clear();
		trailStep = 0;
		printStep = -1;
		pastVarbs.clear();
		trailStarted = false;

		int objectnum = 1;
		for (Point loc : trailObjects.keySet())
		{
			GameObject go = trailObjects.get(loc);
			System.out.println(String.format("Object_%s(%s, new Point(%s, %s))", objectnum, go.getId(), loc.getX(), loc.getY()));
			objectnum++;
		}
		trailObjects.clear();
	}

	public void nextTrailStep()
	{
		trailStep++;
	}
}
