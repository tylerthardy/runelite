/*
 * Copyright (c) 2018, SomeoneWithAnInternetConnection
 * Copyright (c) 2018, oplosthee <https://github.com/oplosthee>
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
package net.runelite.client.plugins.metronome;

import net.runelite.api.SoundEffectID;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(
	keyName = "metronome",
	name = "Metronome",
	description = "Plays a sound on the specified tick to aid in efficient skilling"
)
public interface MetronomePluginConfiguration extends Config
{
	@ConfigItem(
		keyName = "enabled",
		name = "Enable metronome",
		description = "Toggles tick metronome",
		position = 1
	)
	default boolean enabled()
	{
		return false;
	}

	@ConfigItem(
		keyName = "tickCount",
		name = "Tick count",
		description = "Configures the tick on which a sound will be played",
		position = 2
	)
	default int tickCount()
	{
		return 1;
	}

	@ConfigItem(
		keyName = "tickSound",
		name = "Tick sound ID",
		description = "Configures which sound to play on the specified tick",
		position = 3
	)
	default int tickSound()
	{
		return SoundEffectID.GE_INCREMENT_PLOP;
	}

	@ConfigItem(
		keyName = "enableTock",
		name = "Enable tock (alternating) sound",
		description = "Toggles whether to play two alternating sounds",
		position = 4
	)
	default boolean enableTock()
	{
		return false;
	}

	@ConfigItem(
		keyName = "tockSound",
		name = "Tock sound ID",
		description = "Configures which sound to alternate between",
		position = 5
	)
	default int tockSound()
	{
		return SoundEffectID.GE_DECREMENT_PLOP;
	}
}
