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
package net.runelite.client.plugins.world;

import com.google.inject.Provides;
import net.runelite.api.Client;
import net.runelite.api.Experience;
import net.runelite.api.GameState;
import net.runelite.api.Skill;
import net.runelite.api.World;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.combatlevel.CombatLevelConfig;
import net.runelite.client.task.Schedule;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.text.DecimalFormat;
import java.time.temporal.ChronoUnit;

@PluginDescriptor(
		name = "Combat level plugin"
)
public class DefaultWorldPlugin extends Plugin
{
	@Inject
	@Nullable
	Client client;

	@Schedule(
			period = 1,
			unit = ChronoUnit.MILLIS
	)
	public void updateWorld()
	{
		if (client == null || client.getGameState() != GameState.LOGIN_SCREEN)
		{
			return;
		}

		World[] worlds = client.getWorldList();
		if(worlds != null)
		{
			System.out.println("SETTING WORLD");
			world.setId(308);
			client.setWorld(worlds[7]);
		}

		/*for(World w : client.getWorldList())
		{
			System.out.println(w.getId() +  ","+w.getActivity()+","+w.getAddress());
			System.out.println(w.getIndex());
			System.out.println(w.getLocation());
			System.out.println(w.getPlayerCount());
		}*/
	}
}
