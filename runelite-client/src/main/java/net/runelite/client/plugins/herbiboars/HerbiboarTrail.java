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
package net.runelite.client.plugins.herbiboars;

import java.util.HashMap;
import java.util.Map;

public enum HerbiboarTrail
{
	TRAIL_31303(31303, -999, 30),
	TRAIL_31306(31306, -999, 7),
	TRAIL_31309(31309, -999, -999),
	TRAIL_31312(31312, 20, 21),
	TRAIL_31315(31315, 20, 14),
	TRAIL_31318(31318, 18, 14),
	TRAIL_31321(31321, 18, 33),
	TRAIL_31324(31324, 32, 8),
	TRAIL_31327(31327, 32, 25),
	TRAIL_31330(31330, 28, 13),
	TRAIL_31333(31333, -999, 17),
	TRAIL_31336(31336, 14, -999),
	TRAIL_31339(31339, 14, 21),
	TRAIL_31342(31342, 15, 32),
	TRAIL_31345(31345, 15, 8),
	TRAIL_31348(31348, 9, -999),
	TRAIL_31351(31351, 9, -999),
	TRAIL_31354(31354, 17, -999),
	TRAIL_31357(31357, 2, 34),
	TRAIL_31360(31360, 2, -999),
	TRAIL_31363(31363, 13, -999),
	TRAIL_31366(31366, -999, -999),
	TRAIL_31369(31369, -999, -999),
	TRAIL_31372(31372, 34, -999);
	
	private final int trailId;
	private final int objectId1;
	private final int objectId2;

	HerbiboarTrail(int trailId, int objectId1, int objectId2)
	{
		this.trailId = trailId;
		this.objectId1 = objectId1;
		this.objectId2 = objectId2;
	}

	public int getTrailId()
	{
		return this.trailId;
	}

	public int getObjectId(int varbitValue)
	{
		switch (varbitValue)
		{
			case 1:
				return objectId1;
			case 2:
				return objectId2;
			case 0:
			default:
				return -1;
		}
	}

}
