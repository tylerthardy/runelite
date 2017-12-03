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
import net.runelite.api.GameState;
import net.runelite.api.Varbits;
import net.runelite.client.RuneLite;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class HerbiboarsOverlay extends Overlay
{
	private static final Logger logger = LoggerFactory.getLogger(HerbiboarsOverlay.class);

	private final Client client;
	private final RuneLite runelite;

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
	private final int Y_START = 0;
	private int x = X_START;
	private int y = Y_START;

	private Color[] colorz = {
			Color.WHITE,
			Color.RED,
			Color.CYAN,
			Color.GREEN,
			Color.YELLOW
	};

	@Override
	public Dimension render(Graphics2D graphics)
	{
		graphics.setColor(new Color(0,0,0,180));
		graphics.fillRect(0,0,150,client.getViewportWidth());
		graphics.setColor(Color.WHITE);
		startText(X_START,Y_START);
		/*int numVarbs = varbits.length;
		for(int i = 0; i < numVarbs; i++)
		{
			int value =  client.getSetting(varbits[i]);
			Color col = value <= 4 ? colorz[value] : Color.MAGENTA;
			graphics.setColor(col);
			drawLine(graphics, String.format("%s: %s", varbits[i].name(), value));
			graphics.setColor(Color.WHITE);
		}

		drawLine(graphics, "--");*/
		for (HerbiboarTrail trail : HerbiboarTrail.values())
		{
			int trailId = trail.getTrailId();
			int value =  client.getSetting(Varbits.valueOf("HB_TRAIL_" + trailId));

			Color col = Color.WHITE;
			int objectId = trail.getObjectId(value);
			switch (value)
			{
				case 1:
				case 2:
					switch (objectId)
					{
						case -1:
							col = Color.WHITE;
							break;
						case -999:
							col = Color.RED;
							break;
						default:
							col = Color.YELLOW;
							break;
					}
					break;
				case 3:
				case 4:
					col = Color.GREEN;
					break;
				case 0:
				default:
					col = Color.WHITE;
					break;
			}
			graphics.setColor(col);
			drawLine(graphics, String.format("%s: %s (%s)", trailId, objectId, value));
			graphics.setColor(Color.WHITE);
		}
		graphics.setColor(Color.WHITE);
		drawLine(graphics, client.getLocalPlayer().getWorldLocation().toString());

		return null;
	}

	private void startText(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	private void drawLine(Graphics2D graphics, String str)
	{
		FontMetrics fm = graphics.getFontMetrics();
		y += fm.getHeight();
		graphics.drawString(str, x, y);
	}
}
