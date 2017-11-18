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
import java.util.HashMap;

import com.google.inject.Inject;
import net.runelite.api.*;
import net.runelite.api.Point;
import net.runelite.client.RuneLite;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

public class HerbiboarOverlay extends Overlay
{
	private final Client client;
	private final RuneLite runelite;

	private static final int REGION_SIZE = 104;
	private static final int MAX_DISTANCE = 2400;

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


		renderTileObjects(graphics);

		return null;
	}

	private int trailStep = 0;
	private HashMap<Point,Integer> trailTiles = new HashMap<Point,Integer>();
	private Color[] colorSteps = {
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

	public void newTrail()
	{

	}

	public void endTrail()
	{
		trailTiles.clear();
		trailStep = 0;
	}

	public void nextTrailStep()
	{
		trailStep++;
	}


	private void renderTileObjects(Graphics2D graphics)
	{
		Region region = client.getRegion();
		Tile[][][] tiles = region.getTiles();

		int z = client.getPlane();
		for (int x = 0; x < REGION_SIZE; ++x)
		{
			for (int y = 0; y < REGION_SIZE; ++y)
			{
				Tile tile = tiles[z][x][y];

				if (tile != null)
				{
					Player player = client.getLocalPlayer();
					if (player != null)
					{
						GroundObject groundObject = tile.getGroundObject();
						if (groundObject != null)
						{
							if (player.getLocalLocation().distanceTo(groundObject.getLocalLocation()) <= MAX_DISTANCE)
							{
								int id = groundObject.getId();
								if (id >= 31300 && id <= 31399)
								{
									Renderable renderable = groundObject.getRenderable();
									if (renderable.getModel() != null)
									{
										Color color = colorSteps[trailStep];
										Point loc = groundObject.getWorldLocation();
										if (trailTiles.containsKey(loc))
										{
											int colorStep = trailTiles.get(loc);
											color = colorSteps[colorStep];
										}
										else
										{
											trailTiles.put(loc, trailStep);
										}

										OverlayUtil.renderTileOverlay(graphics, groundObject, "ID:" + groundObject.getId(), color);
									}
								}
							}
						}
					}
				}
			}
		}
	} //wee
}
