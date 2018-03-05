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

import static net.runelite.client.plugins.xptracker.XpWorldType.NORMAL;
import com.google.common.eventbus.Subscribe;
import java.io.IOException;
import java.util.EnumSet;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.ExperienceChanged;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientUI;
import net.runelite.client.ui.NavigationButton;
import net.runelite.http.api.worlds.World;
import net.runelite.http.api.worlds.WorldClient;
import net.runelite.http.api.worlds.WorldResult;
import net.runelite.http.api.worlds.WorldType;

@PluginDescriptor(
	name = "XP tracker plugin"
)
@Slf4j
public class XpTrackerPlugin extends Plugin
{
	@Inject
	ClientUI ui;

	@Inject
	Client client;

	private NavigationButton navButton;
	private XpPanel xpPanel;
	private WorldResult worlds;
	private XpWorldType lastWorldType;
	private String lastUsername;

	@Override
	protected void startUp() throws Exception
	{
		WorldClient worldClient = new WorldClient();
		try
		{
			worlds = worldClient.lookupWorlds();
			log.debug("Worlds list contains {} worlds", worlds.getWorlds().size());
		}
		catch (IOException e)
		{
			log.warn("Error looking up worlds list", e);
		}

		xpPanel = new XpPanel(client);
		navButton = new NavigationButton(
			"XP Tracker",
			ImageIO.read(getClass().getResourceAsStream("xp.png")),
			() -> xpPanel);

		ui.getPluginToolbar().addNavigation(navButton);
	}

	@Override
	protected void shutDown() throws Exception
	{
		ui.getPluginToolbar().removeNavigation(navButton);
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		if (event.getGameState() == GameState.LOGGED_IN)
		{
			// LOGGED_IN is triggered between region changes too.
			// Check that the username changed or the world type changed.
			World world = worlds.findWorld(client.getWorld());

			if (world == null)
			{
				log.warn("Logged into nonexistent world {}?", client.getWorld());
				return;
			}

			XpWorldType type = worldSetToType(world.getTypes());

			if (!Objects.equals(client.getUsername(), lastUsername) || lastWorldType != type)
			{
				// Reset
				log.debug("World change: {} -> {}, {} -> {}",
					lastUsername, client.getUsername(), lastWorldType, type);

				lastUsername = client.getUsername();
				lastWorldType = type;
				xpPanel.resetAllInfoBoxes();
			}
		}
	}

	private XpWorldType worldSetToType(EnumSet<WorldType> types)
	{
		XpWorldType xpType = NORMAL;
		for (WorldType type : types)
		{
			XpWorldType t = XpWorldType.of(type);
			if (t != NORMAL)
			{
				xpType = t;
			}
		}
		return xpType;
	}

	@Subscribe
	public void onXpChanged(ExperienceChanged event)
	{
		xpPanel.updateSkillExperience(event.getSkill());
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		xpPanel.updateAllInfoBoxes();
	}
}