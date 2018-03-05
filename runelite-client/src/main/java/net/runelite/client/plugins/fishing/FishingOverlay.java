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
package net.runelite.client.plugins.fishing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.time.Duration;
import java.time.Instant;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.client.plugins.xptracker.XpTrackerService;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.PanelComponent;

class FishingOverlay extends Overlay
{
	private static final String FISHING_SPOT = "Fishing spot";

	private final Client client;
	private final FishingPlugin plugin;
	private final FishingConfig config;
	private final XpTrackerService xpTrackerService;

	private final PanelComponent panelComponent = new PanelComponent();

	@Inject
	public FishingOverlay(Client client, FishingPlugin plugin, FishingConfig config, XpTrackerService xpTrackerService)
	{
		setPosition(OverlayPosition.TOP_LEFT);
		this.client = client;
		this.plugin = plugin;
		this.config = config;
		this.xpTrackerService = xpTrackerService;
	}

	@Override
	public Dimension render(Graphics2D graphics, Point parent)
	{
		FishingSession session = plugin.getSession();

		if (session.getLastFishCaught() == null)
		{
			return null;
		}

		Duration statTimeout = Duration.ofMinutes(config.statTimeout());
		Duration sinceCaught = Duration.between(session.getLastFishCaught(), Instant.now());

		if (sinceCaught.compareTo(statTimeout) >= 0)
		{
			return null;
		}

		panelComponent.getLines().clear();
		if (client.getLocalPlayer().getInteracting() != null && client.getLocalPlayer().getInteracting().getName()
			.contains(FISHING_SPOT))
		{
			panelComponent.setTitle("You are fishing");
			panelComponent.setTitleColor(Color.GREEN);
		}
		else
		{
			panelComponent.setTitle("You are NOT fishing");
			panelComponent.setTitleColor(Color.RED);
		}

		int actions = xpTrackerService.getActions(Skill.FISHING);
		if (actions > 0)
		{
			panelComponent.getLines().add(new PanelComponent.Line(
				"Caught fish:",
				Integer.toString(actions)
			));

			if (actions > 2)
			{
				panelComponent.getLines().add(new PanelComponent.Line(
					"Fish/hr:",
					Integer.toString(xpTrackerService.getActionsHr(Skill.FISHING))
				));
			}
		}

		return panelComponent.render(graphics, parent);
	}
}
