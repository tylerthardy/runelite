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
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Skill;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.RuneLite;
import net.runelite.client.events.ChatMessage;
import net.runelite.client.events.ExperienceChanged;
import net.runelite.client.events.GameStateChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.task.Schedule;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.temporal.ChronoUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@PluginDescriptor(
		name = "Slayer plugin"
)
public class Slayer extends Plugin
{
	private static final Logger logger = LoggerFactory.getLogger(Slayer.class);

	private final InfoBoxManager infoBoxManager = RuneLite.getRunelite().getInfoBoxManager();
	private final SlayerConfig config = RuneLite.getRunelite().getConfigManager().getConfig(SlayerConfig.class);
	private final Client client = RuneLite.getClient();

	//Chat messages
	private final Pattern chatGemProgressMsg = Pattern.compile("You're assigned to kill (.*); only (\\d*) more to go\\.");
	private final String chatGemCompleteMsg = "You need something new to hunt.";
	private final Pattern chatCompleteMsg = Pattern.compile("You've completed (.*) tasks?(.*)?; return to a Slayer master\\.");

	//NPC messages
	private final Pattern npcAssignMsg = Pattern.compile(".*Your new task is to kill (\\d*) (.*)\\.");
	private final Pattern npcCurrentMsg = Pattern.compile("You're still hunting (.*), you have (\\d*) to go\\..*");


	private String taskName;
	private int amount;
	private TaskCounter counter;
	private int cachedXp;

	@Override
	protected void startUp() throws Exception
	{

	}

	@Override
	protected void shutDown() throws Exception
	{

	}

	@Subscribe
	public void onGameStateChange(GameStateChanged event)
	{
		if (!config.enabled())
		{
			return;
		}

		if (event.getGameState() == GameState.LOGGED_IN)
		{
			if (config.amount() != -1 && !config.taskName().isEmpty())
			{
				setTask(config.taskName(), config.amount());
			}
		}
	}

	private void save()
	{
		config.amount(this.amount);
		config.taskName(this.taskName);
	}

	@Schedule(
		period = 1,
		unit = ChronoUnit.MILLIS
	)
	public void npcScheduledCheck()
	{
		if (!config.enabled())
		{
			return;
		}

		System.out.println("tick");
		if (client == null)
		{
			return;
		}

		Widget NPCDialog = client.getWidget(WidgetInfo.DIALOG_NPC_TEXT);
		if (NPCDialog == null)
		{
			return;
		}

		Matcher mAssign = npcAssignMsg.matcher(NPCDialog.getText()); //number, name
		Matcher mCurrent = npcCurrentMsg.matcher(NPCDialog.getText()); //name, number
		boolean found1 = mAssign.find();
		boolean found2 = mCurrent.find();
		if (!found1 && !found2)
			return;
		String taskName = pluralToSingular(found1 ? mAssign.group(2) : mCurrent.group(1));
		int amount = Integer.parseInt(found1 ? mAssign.group(1) : mCurrent.group(2));

		System.out.println("task set:" + taskName + ":" + amount);
		setTask(taskName, amount);
	}


	@Subscribe
	public void onChatMessage(ChatMessage event)
	{
		if (!config.enabled())
		{
			return;
		}

		String chatMsg = event.getMessage();

		if (chatCompleteMsg.matcher(chatMsg).find() || chatMsg.equals(chatGemCompleteMsg))
		{
			setTask("", 0);
			return;
		}

		Matcher m = chatGemProgressMsg.matcher(chatMsg);
		if (!m.find())
			return;
		String taskName = pluralToSingular(m.group(1));
		int amount = Integer.parseInt(m.group(2));

		setTask(taskName, amount);
	}

	/*@Subscribe
	public void onActorDeath(ActorDeath death)
	{
		if (!config.enabled())
		{
			return;
		}

		Actor actor = death.getActor();
		if (actor instanceof NPC)
		{
			NPC npc = (NPC)actor;
			String npcName = npc.getName().toLowerCase();

			System.out.println("killed:" + npcName);
			System.out.println(String.format("%s/%s", taskName, npcName));

			if (npcName.equals(taskName) || Task.isNpcFromTask(npcName, taskName))
			{
				killedOne();
			}
			System.out.println(taskName + ":" + amount);
		}
	}*/

	@Subscribe
	public void onExperienceChanged(ExperienceChanged event)
	{
		if (!config.enabled())
		{
			return;
		}

		Skill skill = event.getSkill();
		if (skill == Skill.SLAYER)
		{
			if (cachedXp == 0)
			{
				cachedXp = client.getSkillExperience(skill);
				return;
			}

			killedOne();
		}
	}

	private void killedOne()
	{
		amount--;
		counter.setText(String.valueOf(amount));
		save(); //Inefficient, but RL does not run plugins' shutDown method. Move there once fixed.
	}

	private void setTask(String name, int amt)
	{
		taskName = name.toLowerCase();
		amount = amt;
		save();

		infoBoxManager.removeIf(t -> t instanceof TaskCounter);

		if (taskName.isEmpty())
		{
			return;
		}

		Task task = Task.getTask(taskName);
		if (task == null)
		{
			logger.warn("No slayer task for {} in the Task database", taskName);
			return;
		}
		counter = new TaskCounter(task, amount);
		counter.setTooltip(capsString(taskName));

		infoBoxManager.addInfoBox(counter);

		System.out.println("task set:" + taskName + ":" + amount);
	}


	//Utils
	private String capsString(String str)
	{
		return str.substring(0,1).toUpperCase() + str.substring(1);
	}

	private static String pluralToSingular(String input)
	{
		if (input.endsWith("ies"))
		{
			if (input.equals("zombies") || input.equals("aviansies"))
				return input.replaceAll("s$", "");

			return input.replaceAll("ies$", "y");
		}

		if (input.endsWith("ves"))
		{
			System.out.println(input);
			return input.replaceAll("ves$", "f");
		}

		if (input.endsWith("men"))
		{
			return input.replaceAll("men$", "man");
		}

		return input.replaceAll("s$", "");
	}
}
