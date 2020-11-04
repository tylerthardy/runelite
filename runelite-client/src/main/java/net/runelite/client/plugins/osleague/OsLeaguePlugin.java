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
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.*;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.osleague.osleague.OsLeagueImport;
import net.runelite.client.plugins.osleague.osleague.OsLeagueRelics;
import net.runelite.client.plugins.osleague.osleague.OsLeagueTasks;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;

import javax.inject.Inject;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static javax.swing.JOptionPane.INFORMATION_MESSAGE;

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
	private List<Relic> relics;
	private List<Area> areas;

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
		if (this.tasks == null || this.areas == null || this.relics == null)
		{
			showMessageBox(
				"Cannot Export Data",
				"You must open the tasks UI, areas UI, and relics UI before exporting.");
			return;
		}

		Gson gson = new Gson();

		OsLeagueImport osLeagueImport = new OsLeagueImport();
		osLeagueImport.unlockedRegions = gson.toJson(this.areas.stream().map(Area::getName).toArray());
		osLeagueImport.unlockedRelics = gson.toJson(new OsLeagueRelics(relics.toArray(new Relic[relics.size()])));
		osLeagueImport.tasks = gson.toJson(new OsLeagueTasks(tasks));

		String json = gson.toJson(osLeagueImport);
		final StringSelection stringSelection = new StringSelection(json);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);

		showMessageBox(
			"OsLeague Data Exported!",
			"Exported data copied to clipboard! Go to osleague.tools, click Manage Data > Import from Runelite, and paste into the box."
		);
	}

	@Subscribe
	public void onWidgetLoaded(WidgetLoaded widgetLoaded)
	{
		if (widgetLoaded.getGroupId() == WidgetID.TRAILBLAZER_TASKS_GROUP_ID)
		{
			GatherTaskData();
		}
		if (widgetLoaded.getGroupId() == WidgetID.TRAILBLAZER_RELICS_GROUP_ID)
		{
			GatherRelicData();
		}
		if (widgetLoaded.getGroupId() == WidgetID.TRAILBLAZER_AREAS_GROUP_ID)
		{
			GatherAreaData();
		}
	}

	private void GatherAreaData()
	{
		this.areas = new ArrayList<>();
		Widget mapWidget = client.getWidget(WidgetInfo.TRAILBLAZER_AREAS_MAP);
		if (mapWidget == null)
		{
			return;
		}

		Widget[] widgets = mapWidget.getStaticChildren();
		for (Widget widget : widgets)
		{
			Area area = Area.getAreaBySprite(widget.getSpriteId());
			if (area != null)
			{
				this.areas.add(area);
			}
		}
	}

	private void GatherRelicData()
	{
		this.relics = new ArrayList<>();
		Widget relicIconsWidget = client.getWidget(WidgetInfo.TRAILBLAZER_RELIC_OVERLAY_ICONS);
		if (relicIconsWidget == null)
		{
			return;
		}

		Widget[] widgets = relicIconsWidget.getDynamicChildren();
		for (Widget widget : widgets)
		{
			Relic relic = Relic.getRelicBySprite(widget.getSpriteId());
			if (relic != null)
			{
				this.relics.add(relic);
			}
		}
	}

	private void GatherTaskData()
	{
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
		for (int i = 0; i < taskLabels.length; i++)
		{
			String label = taskLabels[i].getText();
			int osLeagueIndex = i + RemappedTaskRange.getOffset(i);
			Task task = new Task(
				i,
				osLeagueIndex,
				label,
				getTaskPoints(taskPoints[i]),
				isTaskCompleted(taskLabels[i]),
				taskDifficulties[i].getSpriteId());
			tasks[i] = task;
		}
	}

	private int getTaskPoints(Widget taskPoints)
	{
		Matcher m = POINTS_PATTERN.matcher(taskPoints.getText());
		if (m.find())
		{
			return Integer.parseInt(m.group(1));
		}
		return -1;
	}

	private boolean isTaskCompleted(Widget taskLabel)
	{
		String hexColor = Integer.toString(taskLabel.getTextColor(), 16);
		return !hexColor.equals("9f9f9f");
	}

	private boolean isRelicEnabled(Widget relic)
	{
		String hexColor = Integer.toString(relic.getTextColor(), 16);
		return !hexColor.equals("aaaaaa");
	}

	/**
	 * Open swing message box with specified message and copy data to clipboard
	 *
	 * @param message message to show
	 */
	private static void showMessageBox(final String title, final String message)
	{
		SwingUtilities.invokeLater(() ->
			JOptionPane.showMessageDialog(
				null,
				message, title,
				INFORMATION_MESSAGE));
	}
}
