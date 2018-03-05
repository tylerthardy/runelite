/*
 * Copyright (c) 2017, Cameron <moberg@tuta.io>
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
package net.runelite.client.plugins.xptracker;

import com.google.common.eventbus.Subscribe;
import javax.imageio.ImageIO;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Skill;
import net.runelite.client.events.ExperienceChanged;
import net.runelite.client.events.GameStateChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.ClientUI;
import net.runelite.client.ui.NavigationButton;
import java.time.temporal.ChronoUnit;
import javax.inject.Inject;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.task.Schedule;

@PluginDescriptor(
	name = "XP tracker plugin"
)
public class XpTrackerPlugin extends Plugin
{
	private static final int NUMBER_OF_SKILLS = Skill.values().length - 1; //ignore overall

	@Inject
	ClientUI ui;

	@Inject
	Client client;

	private NavigationButton navButton;
	private XpPanel xpPanel;
	private final SkillXPInfo[] xpInfos = new SkillXPInfo[NUMBER_OF_SKILLS];

	@Override
	protected void startUp() throws Exception
	{
		xpPanel = injector.getInstance(XpPanel.class);
		navButton = new NavigationButton(
			"XP Tracker",
			ImageIO.read(getClass().getResourceAsStream("xp.png")),
			() -> xpPanel);

		ui.getPluginToolbar().addNavigation(navButton);
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		// reset on world hop or logging in
		switch (event.getGameState())
		{
			case HOPPING:
			case LOGGING_IN:
				xpPanel.resetAllSkillXpHr();
		}
	}

	@Subscribe
	public void onXpChanged(ExperienceChanged event)
	{
		Skill skill = event.getSkill();
		int skillIdx = skill.ordinal();

		//To catch login ExperienceChanged event.
		if (xpInfos[skillIdx] != null)
		{
			xpInfos[skillIdx].update(client.getSkillExperience(skill));
		}
		else
		{
			xpInfos[skillIdx] = new SkillXPInfo(client.getSkillExperience(skill),
				skill);
		}
	}

	@Schedule(
		period = 600,
		unit = ChronoUnit.MILLIS
	)
	public void updateXp()
	{
		xpPanel.updateAllSkillXpHr();
	}

	public SkillXPInfo[] getXpInfos()
	{
		return xpInfos;
	}

}
