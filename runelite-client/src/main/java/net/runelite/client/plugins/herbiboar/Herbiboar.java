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

import com.google.common.eventbus.Subscribe;
import javax.annotation.Nullable;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Varbits;
import net.runelite.client.events.ChatMessage;
import net.runelite.client.events.ExperienceChanged;
import net.runelite.client.events.GameStateChanged;
import net.runelite.client.events.MenuOptionClicked;
import net.runelite.client.events.VarbitChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.Overlay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.Arrays;

import static net.runelite.api.Skill.HUNTER;

@PluginDescriptor(
		name = "Herbiboar plugin"
)
public class Herbiboar extends Plugin
{
	private static final Logger logger = LoggerFactory.getLogger(Herbiboar.class);

	@Inject
	HerbiboarOverlay overlay;

	@Inject
	@Nullable
	Client client;

	@Override
	protected void startUp() throws Exception
	{
	}
	@Override
	protected void shutDown() throws Exception
	{
	}
	@Override
	public Overlay getOverlay()
	{
		return overlay;
	}

	@Subscribe
	public void onChatMessage(ChatMessage event)
	{
		String message = event.getMessage();

		if (message == null)
			return;

		if (message.contains("Closer inspection reveals tracks leading away from you."))
		{
			//Start
			overlay.newTrail();
		}
		else if (message.contains("Nothing seems to be out of place here."))
		{
			//Failure to find
		}
		else if (message.contains("Something has passed this way.") || message.contains("Something has disturbed this seaweed recently, it smells."))
		{
			//Find next
			overlay.nextTrailStep();
		}
		else if (message.contains("You stun the creature") || event.getMessage().contains("The creature has successfully"))
		{
			overlay.endTrail();
			//color = new Color((int)(Math.random()*0x1000000));
		}
	}

	@Subscribe
	public void onExperienceChanged(ExperienceChanged event)
	{
		if (event.getSkill() != HUNTER) //config
		{
			return;
		}

		/*if (cachedXp == 0)
		{
			// this is the initial xp sent on login
			cachedXp = client.getSkillExperience(SLAYER);
			return;
		}*/

	}

	@Subscribe
	public void onGameStateChange(GameStateChanged event)
	{
		switch (event.getGameState())
		{
			case HOPPING:
			case LOGGING_IN:
				overlay.endTrail();
				System.out.println("CLEARED");
				break;
			default:
				break;
		}
	}

	@Subscribe
	public void menuOptionClick(MenuOptionClicked menuOptionClicked)
	{
		if (menuOptionClicked.getId() > 0 && !menuOptionClicked.getMenuTarget().isEmpty() && !menuOptionClicked.getMenuTarget().equals(""))
			System.out.println(String.format("%s -> %s (%s)", menuOptionClicked.getMenuAction(), menuOptionClicked.getMenuTarget(), menuOptionClicked.getMenuTarget()));
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
}
