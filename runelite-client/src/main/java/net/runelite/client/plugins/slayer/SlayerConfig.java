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
package net.runelite.client.plugins.slayer;

import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(
	keyName = "slayer",
	name = "Slayer",
	description = "Configuration for the slayer plugin"
)
public interface SlayerConfig
{
	@ConfigItem(
		keyName = "enabled",
		name = "Enable",
		description = "Configures whether slayer plugin is enabled"
	)
	default boolean enabled()
	{
		return true;
	}

	@ConfigItem(
		keyName = "infobox",
		name = "Task InfoBox",
		description = "Display task information in an InfoBox"
	)
	default boolean showInfobox()
	{
		return true;
	}

	@ConfigItem(
		keyName = "itemoverlay",
		name = "Count on Items",
		description = "Display task count remaining on slayer items"
	)
	default boolean showItemOverlay()
	{
		return true;
	}

	// Stored data
	@ConfigItem(
		keyName = "taskName",
		name = "",
		description = "",
		hidden = true
	)
	default String taskName()
	{
		return "";
	}

	@ConfigItem(
		keyName = "taskName",
		name = "",
		description = ""
	)
	void taskName(String key);

	@ConfigItem(
		keyName = "amount",
		name = "",
		description = "",
		hidden = true
	)
	default int amount()
	{
		return -1;
	}

	@ConfigItem(
		keyName = "amount",
		name = "",
		description = ""
	)
	void amount(int amt);

	@ConfigItem(
		keyName = "streak",
		name = "",
		description = "",
		hidden = true
	)
	default int streak()
	{
		return -1;
	}

	@ConfigItem(
		keyName = "streak",
		name = "",
		description = ""
	)
	void streak(int streak);

	@ConfigItem(
		keyName = "points",
		name = "",
		description = "",
		hidden = true
	)
	default int points()
	{
		return -1;
	}

	@ConfigItem(
		keyName = "points",
		name = "",
		description = ""
	)
	void points(int points);
}
