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

import net.runelite.api.NpcID;

import java.util.HashMap;
import java.util.Map;

enum Task
{
	DAGANNOTH("Dagannoth", new int[]{NpcID.DAGANNOTH_REX, NpcID.DAGANNOTH_REX, NpcID.DAGANNOTH_SUPREME}),
	ABYSSAL_DEMON("Abyssal demon", new int[]{NpcID.ABYSSAL_SIRE,NpcID.ABYSSAL_SIRE_5887,NpcID.ABYSSAL_SIRE_5888,NpcID.ABYSSAL_SIRE_5889,NpcID.ABYSSAL_SIRE_5890,NpcID.ABYSSAL_SIRE_5891,NpcID.ABYSSAL_SIRE_5908,});
	private final String name;
	private final int[] possibleIds;
	private static Map<Integer, Task> ids = new HashMap<>();

	static
	{
		for (Task task : values())
		{
			for (int id : task.getIds())
			{
				ids.put(id, task);
			}
		}
	}


	Task(String name, int[] ids)
	{
		this.name = name;
		this.possibleIds = ids;
	}

	public String getName()
	{
		return this.name;
	}

	private int[] getIds()
	{
		return this.possibleIds;
	}

	public static boolean isFromTask(int id)
	{
		return ids.get(id) != null;
	}
}
