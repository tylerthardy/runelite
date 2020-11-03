/*
 * Copyright (c) 2018 Abex
 * Copyright (c) 2017, Tyler <https://github.com/tylerthardy>
 * Copyright (c) 2018, Yoav Ram <https://github.com/yoyo421>
 * Copyright (c) 2018, Infinitay <https://github.com/Infinitay>
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

package net.runelite.client.plugins.osleague;

import com.google.gson.Gson;
import com.google.inject.Provides;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.*;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.Text;

import javax.inject.Inject;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.image.BufferedImage;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@PluginDescriptor(
	name = "OsLeague",
	description = "Export Trailblazer League tasks to OsLeague importable format",
	tags = {"task", "league", "trailblazer", "osleague"}
)
public class OsLeaguePlugin extends Plugin
{
	private static final Pattern POINTS_PATTERN = Pattern.compile("Reward: <col=ffffff>(\\d*) points<\\/col>");

	@Inject
	private Client client;

	@Inject
	private ClientToolbar clientToolbar;

	private NavigationButton titleBarButton;

	private Task[] tasks;

	@Data
	private static class Task
	{
		Task(int idx, String label, int points, boolean completed, int spriteId) {
			Index = idx;
			Points = points;
			Label = label;
			Completed = completed;
			taskDifficulty = TaskDifficulty.fromSprite(spriteId);
		}
		public int Index;
		public boolean Completed;
		public String Label;
		public int Points;
		public TaskDifficulty taskDifficulty;
	}

	@Subscribe
	public void onVarbitChanged(VarbitChanged event)
	{
		// maybe do something
	}


	@Override
	protected void startUp() throws Exception
	{

		final BufferedImage icon = ImageUtil.getResourceStreamFromClass(getClass(), "osleague.png");

		titleBarButton = NavigationButton.builder()
				.tab(false)
				.tooltip("Copy Tasks to Clipboard")
				.icon(icon)
				.onClick(this::copyJsonToClipboard)
				.build();

		clientToolbar.addNavigation(titleBarButton);
	}

	private void copyJsonToClipboard()
	{
		Gson gson = new Gson();
		String json = gson.toJson(tasks);
		final StringSelection stringSelection = new StringSelection(json);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
	}

	@Subscribe
	public void onWidgetLoaded(WidgetLoaded widgetLoaded)
	{
		if (widgetLoaded.getGroupId() != WidgetID.TRAILBLAZER_TASKS_GROUP_ID) return;

		Widget taskLabelsWidget = client.getWidget(WidgetInfo.TRAILBLAZER_TASK_LABELS);
		Widget taskPointsWidget = client.getWidget(WidgetInfo.TRAILBLAZER_TASK_POINTS);
		Widget taskDifficultiesWidget = client.getWidget(WidgetInfo.TRAILBLAZER_TASK_DIFFICULTIES);
		if (taskLabelsWidget == null || taskPointsWidget == null || taskDifficultiesWidget == null)
		{
			return;
		}

		Widget[] taskLabels = taskLabelsWidget.getDynamicChildren();
		Widget[] taskPoints = taskPointsWidget.getDynamicChildren();
		Widget[] taskDifficulties = taskDifficultiesWidget.getDynamicChildren();
		if (taskLabels.length != taskPoints.length || taskPoints.length != taskDifficulties.length)
		{
			return;
		}

		tasks = new Task[taskLabels.length];
		for (int i = 0; i < taskLabels.length; i++) {
			String label = taskLabels[i].getText();
			Task task = new Task(i, label, getPoints(taskPoints[i]), isCompleted(taskLabels[i]), taskDifficulties[i].getSpriteId());
			tasks[i] = task;
		}
	}

	private int getPoints(Widget taskPoints)
	{
		Matcher m = POINTS_PATTERN.matcher(taskPoints.getText());
		if (m.find())
		{
			return Integer.parseInt(m.group(1));
		}
		return -1;
	}

	private boolean isCompleted(Widget taskLabel)
	{
		String hexColor = Integer.toString(taskLabel.getTextColor(), 16);
		return !hexColor.equals("9f9f9f");
	}
}
