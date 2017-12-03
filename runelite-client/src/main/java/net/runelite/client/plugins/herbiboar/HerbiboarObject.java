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

public enum HerbiboarObject
{
	//<editor-fold desc="Enums">
	Object_1(30532, new Point(3681, 3863)),
	Object_2(30542, new Point(3715, 3851)),
	Object_3(30520, new Point(3695, 3800)),
	Object_4(30571, new Point(3718, 3785)),
	Object_5(30522, new Point(3705, 3830)),
	Object_6(30481, new Point(3702, 3837)),
	Object_7(30546, new Point(3672, 3890)),
	Object_8(30547, new Point(3680, 3836)),
	Object_9(30540, new Point(3680, 3838)),
	Object_10(30480, new Point(3708, 3833)),
	Object_11(30572, new Point(3724, 3785)),
	Object_12(30523, new Point(3752, 3850)),
	Object_13(30550, new Point(3713, 3850)),
	Object_14(30537, new Point(3728, 3893)),
	Object_15(30539, new Point(3668, 3865)),
	Object_16(30521, new Point(3704, 3810)),
	Object_17(30541, new Point(3694, 3847)),
	Object_18(30534, new Point(3670, 3889)),
	Object_19(30566, new Point(3679, 3815)),
	Object_20(30533, new Point(3699, 3875)),
	Object_21(30551, new Point(3710, 3877)),
	Object_22(30573, new Point(3714, 3785)),
	Object_23(30482, new Point(3715, 3835)),
	Object_24(30565, new Point(3677, 3882)),
	Object_25(30543, new Point(3698, 3847)),
	Object_26(30532, new Point(3715, 3840)),
	Object_27(30519, new Point(3686, 3870)),
	Object_28(30536, new Point(3708, 3876)),
	Object_29(30544, new Point(3681, 3859)),
	Object_30(30545, new Point(3697, 3875)),
	Object_31(30532, new Point(3751, 3849)),
	Object_32(30535, new Point(3681, 3860)),
	Object_33(30549, new Point(3667, 3862)),
	Object_34(31480, new Point(3713, 3840)),
	Object_35(31479, new Point(3706, 3811));
	//</editor-fold>

	private static final Logger logger = LoggerFactory.getLogger(HerbiboarTrail.class);
	private static final Map<Point, HerbiboarObject> objectLocs = new HashMap<>();

	private final int objectId;
	private final Point location;

	static
	{
		for (HerbiboarObject object : values())
		{
			objectLocs.put(object.getLocation(), object);
		}
	}

	HerbiboarObject(int objectId, Point location)
	{
		this.objectId = objectId;
		this.location = location;
	}
	public Point getLocation()
	{
		return location;
	}

	public String getName()
	{
		return name();
	}

	public static HerbiboarObject getObject(Point location)
	{
		return objectLocs.get(location);
	}
}
