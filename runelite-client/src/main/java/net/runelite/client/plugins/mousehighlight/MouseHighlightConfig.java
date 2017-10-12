/*
 * Copyright (c) 2017, Seth <Sethtroll3@gmail.com>
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
package net.runelite.client.plugins.mousehighlight;

import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

import java.awt.Color;

@ConfigGroup(
	keyName = "mousehighlight",
	name = "Mouse Highlighting",
	description = "Configuration for the mouse Highlight plugin"
)
public interface MouseHighlightConfig
{
	@ConfigItem(
		keyName = "enabled",
		name = "Enabled",
		description = "Configures whether or not mouse hover info is displayed"
	)
	default boolean enabled()
	{
		return true;
	}

	@ConfigItem(
		keyName = "border",
		name = "Border Color",
		description = "Color of the mouse hover border"
	)
	default Color borderColor()
	{
		return Color.cyan;
	}

	@ConfigItem(
		keyName = "smallfont",
		name = "Small Font",
		description = "Use the small Runescape font for hover info"
	)
	default boolean smallFont()
	{
		return false;
	}

	@ConfigItem(
		keyName = "tooltipleft",
		name = "Left Side",
		description = "Show tooltip on left side of cursor"
	)
	default boolean leftSide()
	{
		return false;
	}
}
