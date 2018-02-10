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
package net.runelite.client.plugins.seedlings;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

import static net.runelite.api.ItemID.*;

public enum SeedlingItems
{
	OAK(OAK_SEEDLING, OAK_SEEDLING_W),
	WILLOW(WILLOW_SEEDLING, WILLOW_SEEDLING_W),
	MAPLE(MAPLE_SEEDLING, MAPLE_SEEDLING_W),
	YEW(YEW_SEEDLING, YEW_SEEDLING_W),
	MAGIC(MAGIC_SEEDLING, MAGIC_SEEDLING_W),
	APPLE(APPLE_SEEDLING, APPLE_SEEDLING_W),
	BANANA(BANANA_SEEDLING, BANANA_SEEDLING_W),
	ORANGE(ORANGE_SEEDLING, ORANGE_SEEDLING_W),
	CURRY(CURRY_SEEDLING, CURRY_SEEDLING_W),
	PAPAYA(PAPAYA_SEEDLING, PAPAYA_SEEDLING_W),
	PINEAPPLE(PINEAPPLE_SEEDLING, PINEAPPLE_SEEDLING_W),
	PALM(PALM_SEEDLING, PALM_SEEDLING_W),
	SPIRIT(SPIRIT_SEEDLING, SPIRIT_SEEDLING_W),
	CALQUAT(CALQUAT_SEEDLING, CALQUAT_SEEDLING_W);

	@Getter
	private int unwatered;
	@Getter
	private int watered;

	private static final Map<Integer, SeedlingItems> SEEDLINGS = new HashMap<>();

	static
	{
		for (SeedlingItems seedling : values())
		{
			SEEDLINGS.put(seedling.getUnwatered(), seedling);
			SEEDLINGS.put(seedling.getWatered(), seedling);
		}
	}

	SeedlingItems(int unwatered, int watered)
	{
		this.unwatered = unwatered;
		this.watered = watered;
	}

	public boolean isWatered(int itemId)
	{
		return itemId == watered;
	}

	public boolean isUnwatered(int itemId)
	{
		return itemId == unwatered;
	}
	public static int[] getSeedlingIds()
	{
		int[] ids = new int[SEEDLINGS.size()];
		int idx = 0;
		for (Integer key : SEEDLINGS.keySet())
		{
			ids[idx++] = key.intValue();
		}

		return ids;
	}

	public static SeedlingItems getSeedling(int id)
	{
		return SEEDLINGS.get(id);
	}
}
