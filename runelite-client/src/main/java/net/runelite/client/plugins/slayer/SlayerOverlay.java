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
package net.runelite.client.plugins.slayer;

import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.client.RuneLite;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;

import java.awt.*;

public class SlayerOverlay extends Overlay
{
	private static final int WIDTH = 140;
	private static final Color BACKGROUND = new Color(Color.gray.getRed(), Color.gray.getGreen(), Color.gray.getBlue(), 127);
	private static final int LEFT_PADDING = 2;
	private static final int RIGHT_PADDING = 2;
	private static final int TOP_PADDING = 2;
	private static final int BOTTOM_PADDING = 2;

	private static final Client client = RuneLite.getClient();
	private final Slayer plugin;

	public SlayerOverlay(Slayer plugin)
	{
		super(OverlayPosition.TOP_LEFT, OverlayPriority.MED);
		this.plugin = plugin;
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (client.getGameState() != GameState.LOGGED_IN ||
				plugin.getTaskName() == null || plugin.getAmount() == 0)//|| !config.enabled())
		{
			return null;
		}

		//Draw rectangle
		FontMetrics metrics = graphics.getFontMetrics();
		int boxHeight = TOP_PADDING;
		int textHeight = metrics.getHeight();
		boxHeight += textHeight;
		boxHeight += BOTTOM_PADDING;

		graphics.setColor(BACKGROUND);
		graphics.fillRect(0, 0, WIDTH, boxHeight);

		//Draw text
		int y = TOP_PADDING + textHeight;
		String monster = plugin.getTaskName();
		String monsterUpper = monster.substring(0,1).toUpperCase() + monster.substring(1);
		String amount = String.valueOf(plugin.getAmount());

		graphics.setColor(Color.white);
		graphics.drawString(monsterUpper, LEFT_PADDING, y);
		graphics.drawString(amount, WIDTH - RIGHT_PADDING - metrics.stringWidth(amount), y);

		return new Dimension(WIDTH, boxHeight);
	}
}
