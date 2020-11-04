package net.runelite.client.plugins.osleague.osleague;

import net.runelite.client.plugins.osleague.Task;

import java.util.Arrays;

public class OsLeagueTasks
{
	public int version = 3;
	public String[] tasks;

	public OsLeagueTasks(Task[] tasks)
	{
		String[] taskNumbers = Arrays.stream(tasks)
			.filter(task -> task.Completed)
			.mapToInt(task -> task.OsLeagueIndex)
			.mapToObj(Integer::toString)
			.toArray(String[]::new);

		this.tasks = taskNumbers;
	}
}
