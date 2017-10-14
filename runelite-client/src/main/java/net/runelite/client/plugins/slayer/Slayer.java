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

import com.google.common.eventbus.Subscribe;
import net.runelite.api.Actor;
import net.runelite.api.NPC;
import net.runelite.client.RuneLite;
import net.runelite.client.events.ActorDeath;
import net.runelite.client.events.ChatMessage;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.Overlay;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Slayer extends Plugin
{
	private final SlayerConfig config = RuneLite.getRunelite().getConfigManager().getConfig(SlayerConfig.class);
	private final SlayerOverlay overlay = new SlayerOverlay(this);
	private final Pattern p = Pattern.compile("You're assigned to kill (.*)s; only (\\d*) more to go\\.");
	private String monster;
	private int amount;

	@Override
	protected void startUp() throws Exception
	{
		if (config.amount() != -1 && config.monster() != "")
		{
			this.amount = config.amount();
			this.monster = config.monster();
		}
	}

	@Override
	protected void shutDown() throws Exception
	{
	}

	private void save()
	{
		config.amount(this.amount);
		config.monster(this.monster);
	}

	@Override
	public Overlay getOverlay()
	{
		return overlay;
	}

	@Subscribe
	public void onChatMessage(ChatMessage event)
	{
		Matcher m = p.matcher(event.getMessage());

		if (!m.find())
			return;

		String monster = m.group(1);
		int amount = Integer.parseInt(m.group(2));

		setTask(monster, amount);
	}

	@Subscribe
	public void onActorDeath(ActorDeath death)
	{
		Actor actor = death.getActor();
		if (actor instanceof NPC)
		{
			NPC npc = (NPC)actor;
			int id = npc.getId();
			String name = npc.getName();
			System.out.println("killed:" + name + "/" + id);
			System.out.println(String.format("%s/%s", monster, name.toLowerCase()));

			//TODO: you need to check plurals
			if (name.toLowerCase().equals(monster) || Task.isFromTask(monster, id)) //look up by name
			{
				killedOne();
			}
			System.out.println(monster + ":" + amount);
		}
	}

	private void killedOne()
	{
		amount--;
		save();
	}

	private void setTask(String monster, int amount)
	{
		this.monster = monster.toLowerCase();
		this.amount = amount;
		save();
		System.out.println("task set:" + this.monster + ":" + this.amount);
	}

	public String getMonster()
	{
		return monster;
	}

	public int getAmount()
	{
		return amount;
	}
}
