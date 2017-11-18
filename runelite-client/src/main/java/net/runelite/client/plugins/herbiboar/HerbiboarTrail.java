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
package net.runelite.client.plugins.herbiboar;

import net.runelite.api.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public enum HerbiboarTrail
{
	//<editor-fold desc="Enums">
	TRAIL1(31306, 31307, new Point(3685,3872), new Point(3672,3888)),
	TRAIL2(31307, 31306, new Point(3685,3872), new Point(3672,3888)),
	TRAIL3(31321, 31322, new Point(3669,3890), new Point(3666,3866)),
	TRAIL4(31345, 31346, new Point(3666,3863), new Point(3679,3839)),
	TRAIL5(31351, 31352, new Point(3681,3837), new Point(3695,3799)),
	TRAIL6(31366, 31367, new Point(3697,3800), new Point(3706,3809)),
	TRAIL7(31370, 31369, new Point(3708,3811), new Point(3712,3840)),
	TRAIL8(31372, -9999, new Point(3711,3842), new Point(3710,3875)),
	TRAIL9(31313, 31312, new Point(3708,3879), new Point(3701,3874)),
	TRAIL10(31315, 31316, new Point(3700,3872), new Point(3726,3892)),
	TRAIL11(31318, 31319, new Point(3727,3895), new Point(3672,3892)),
	TRAIL12(31342, 31343, new Point(3667,3863), new Point(3680,3862)),
	TRAIL13(31310, 31309, new Point(3684,3869), new Point(3682,3861)),
	TRAIL14(31324, 31325, new Point(3681,3858), new Point(3681,3840));
	//</editor-fold>

	private static final Logger logger = LoggerFactory.getLogger(HerbiboarTrail.class);
	private static final Map<Integer, HerbiboarTrail> tileIds = new HashMap<>();

	private final int tileId1;
	private final int tileId2;
	private final Point end1;
	private final Point end2;

	static
	{
		for (HerbiboarTrail trail : values())
		{
			tileIds.put(trail.getTileId1(), trail);
			tileIds.put(trail.getTileId2(), trail);
		}
	}

	HerbiboarTrail(int tileId1, int tileId2, Point end1, Point end2)
	{
		this.tileId1 = tileId1;
		this.tileId2 = tileId2;
		this.end1 = end1;
		this.end2 = end2;
	}

	public static HerbiboarTrail getTrail(int tileId)
	{
		return tileIds.get(tileId);
	}

	public int getTileId1()
	{
		return tileId1;
	}
	public int getTileId2()
	{
		return tileId2;
	}
	public Point getEnd1()
	{
		return end1;
	}
	public Point getEnd2()
	{
		return end2;
	}
}
