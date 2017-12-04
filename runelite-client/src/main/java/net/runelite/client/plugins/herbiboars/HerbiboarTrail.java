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

import net.runelite.api.Point;

import java.util.HashMap;
import java.util.Map;

public enum HerbiboarTrail
{
	TRAIL_31303(31303, null, new Point(3697, 3875)),
	TRAIL_31306(31306, null, new Point(3672, 3890)),
	TRAIL_31309(31309, null, null),
	TRAIL_31312(31312, new Point(3699, 3875), new Point(3710, 3877)),
	TRAIL_31315(31315, new Point(3699, 3875), new Point(3728, 3893)),
	TRAIL_31318(31318, new Point(3670, 3889), new Point(3728, 3893)),
	TRAIL_31321(31321, new Point(3670, 3889), new Point(3667, 3862)),
	TRAIL_31324(31324, new Point(3681, 3860), new Point(3680, 3836)),
	TRAIL_31327(31327, new Point(3681, 3860), new Point(3698, 3847)),
	TRAIL_31330(31330, new Point(3708, 3876), new Point(3713, 3850)),
	TRAIL_31333(31333, new Point(3708, 3876), new Point(3694, 3847)),
	TRAIL_31336(31336, new Point(3728, 3893), null),
	TRAIL_31339(31339, new Point(3728, 3893), new Point(3710, 3877)),
	TRAIL_31342(31342, new Point(3668, 3865), new Point(3681, 3860)),
	TRAIL_31345(31345, new Point(3668, 3865), new Point(3680, 3836)),
	TRAIL_31348(31348, new Point(3680, 3838), null),
	TRAIL_31351(31351, new Point(3680, 3838), null),
	TRAIL_31354(31354, new Point(3694, 3847), null),
	TRAIL_31357(31357, new Point(3715, 3851), new Point(3713, 3840)),
	TRAIL_31360(31360, new Point(3715, 3851), null),
	TRAIL_31363(31363, new Point(3713, 3850), null),
	TRAIL_31366(31366, null, null),
	TRAIL_31369(31369, null, null),
	TRAIL_31372(31372, new Point(3713, 3840), null);
	
	private final int trailId;
	private final Point objectLoc1;
	private final Point objectLoc2;

	private static Map<Integer, HerbiboarTrail> trailIds = new HashMap<>();

	HerbiboarTrail(int trailId, Point objectLoc1, Point objectLoc2)
	{
		this.trailId = trailId;
		this.objectLoc1 = objectLoc1;
		this.objectLoc2 = objectLoc2;
	}

	public int getTrailId()
	{
		return this.trailId;
	}

	public Point getObjectLoc(int varbitValue)
	{
		switch (varbitValue)
		{
			case 1:
				return objectLoc1;
			case 2:
				return objectLoc2;
			case 0:
			default:
				return null;
		}
	}
}
