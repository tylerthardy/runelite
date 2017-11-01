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

import net.runelite.api.ItemID;
import net.runelite.client.game.ItemManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

enum Task
{
	//<editor-fold desc="Enums">
	ABERRANT_SPECTRES("aberrant spectres", ItemID.ABERRANT_SPECTRE),
	ABYSSAL_DEMONS("abyssal demons", ItemID.ABYSSAL_DEMON),
	ANKOU("ankou", ItemID.ANKOU_MASK),
	AVIANSIES("aviansies", ItemID.ENSOULED_AVIANSIE_HEAD),
	BANSHEES("banshees", ItemID.BANSHEE),
	BASILISKS("basilisks", ItemID.BASILISK),
	BATS("bats", ItemID.GIRAL_BAT_2),
	BEARS("bears", ItemID.ENSOULED_BEAR_HEAD),
	BIRDS("birds", ItemID.FEATHER),
	BLACK_DEMONS("black demons", ItemID.BLACK_DEMON_MASK),
	BLACK_DRAGONS("black dragons", ItemID.BLACK_DRAGON_MASK),
	BLOODVELD("bloodveld", ItemID.BLOODVELD),
	BLUE_DRAGONS("blue dragons", ItemID.BLUE_DRAGON_MASK),
	BOSSES("bosses", -1),
	BRINE_RATS("brine rats", ItemID.BRINE_RAT),
	BRONZE_DRAGONS("bronze dragons", ItemID.BRONZE_DRAGON_MASK),
	CATABLEPON("catablepon", ItemID.LEFT_SKULL_HALF),
	CAVE_BUGS("cave bugs", ItemID.SWAMP_CAVE_BUG),
	CAVE_CRAWLERS("cave crawlers", ItemID.CAVE_CRAWLER),
	CAVE_HORRORS("cave horrors", ItemID.CAVE_HORROR),
	CAVE_KRAKEN("cave kraken", ItemID.CAVE_KRAKEN),
	CAVE_SLIMES("cave slimes", ItemID.SWAMP_CAVE_SLIME),
	COCKATRICE("cockatrice", ItemID.COCKATRICE),
	COWS("cows", ItemID.COW_MASK),
	CRAWLING_HANDS("crawling hands", ItemID.CRAWLING_HAND),
	CROCODILES("crocodiles", ItemID.SWAMP_LIZARD),
	DAGANNOTH("dagannoth", ItemID.DAGANNOTH),
	DARK_BEASTS("dark beasts", ItemID.DARK_BEAST),
	DESERT_LIZARDS("desert lizards", ItemID.DESERT_LIZARD),
	DOGS("dogs", ItemID.GUARD_DOG),
	DUST_DEVILS("dust devils", ItemID.DUST_DEVIL),
	DWARVES("dwarves", ItemID.DWARVEN_HELMET),
	EARTH_WARRIORS("earth warriors", ItemID.BRONZE_FULL_HELM_T),
	ELVES("elves", ItemID.ELF),
	FEVER_SPIDERS("fever spiders", ItemID.FEVER_SPIDER),
	FIRE_GIANTS("fire giants", -1),
	FLESHCRAWLERS("fleshcrawlers", -1),
	GARGOYLES("gargoyles", ItemID.GARGOYLE),
	GHOSTS("ghosts", -1),
	GHOULS("ghouls", -1),
	GOBLINS("goblins", ItemID.ENSOULED_GOBLIN_HEAD),
	GREATER_DEMONS("greater demons", ItemID.GREATER_DEMON_MASK),
	GREEN_DRAGONS("green dragons", ItemID.GREEN_DRAGON_MASK),
	HARPIE_BUG_SWARMS("harpie bug swarms", ItemID.SWARM),
	HELLHOUNDS("hellhounds", ItemID.HELLHOUND),
	HILL_GIANTS("hill giants", ItemID.ENSOULED_GIANT_HEAD),
	HOBGOBLINS("hobgoblins", ItemID.HOBGOBLIN_GUARD),
	ICE_GIANTS("ice giants", -1),
	ICE_WARRIORS("ice warriors", ItemID.MITHRIL_FULL_HELM_T),
	ICEFIENDS("icefiends", -1),
	INFERNAL_MAGES("infernal mages", ItemID.INFERNAL_MAGE),
	IRON_DRAGONS("iron dragons", ItemID.IRON_DRAGON_MASK),
	JELLIES("jellies", ItemID.JELLY),
	JUNGLE_HORRORS("jungle horrors", -1),
	KALPHITE("kalphite", ItemID.KALPHITE_SOLDIER),
	KILLERWATTS("killerwatts", ItemID.KILLERWATT),
	KURASK("kurask", ItemID.KURASK),
	LESSER_DEMONS("lesser demons", ItemID.LESSER_DEMON_MASK),
	LIZARDMEN("lizardmen", ItemID.LIZARDMAN_FANG),
	MINIONS_OF_SCABARAS("minions of scabaras", ItemID.GOLDEN_SCARAB),
	MINOTAURS("minotaurs", ItemID.ENSOULED_MINOTAUR_HEAD),
	MITHRIL_DRAGONS("mithril dragons", ItemID.MITHRIL_DRAGON_MASK),
	MOGRES("mogres", ItemID.MOGRE),
	MOLANISKS("molanisks", ItemID.MOLANISK),
	MONKEYS("monkeys", ItemID.ENSOULED_MONKEY_HEAD),
	MOSS_GIANTS("moss giants", -1),
	MUTATED_ZYGOMITES("mutated zygomites", ItemID.MUTATED_ZYGOMITE),
	NECHRYAEL("nechryael", ItemID.NECHRYAEL),
	OGRES("ogres", ItemID.ENSOULED_OGRE_HEAD),
	OTHERWORLDLY_BEING("otherworldly being", ItemID.GHOSTLY_HOOD),
	PYREFIENDS("pyrefiends", ItemID.PYREFIEND),
	RATS("rats", ItemID.RATS_TAIL),
	RED_DRAGONS("red dragons", ItemID.BABY_RED_DRAGON),
	ROCKSLUGS("rockslugs", ItemID.ROCKSLUG),
	SCORPIONS("scorpions", -1),
	SEA_SNAKES("sea snakes", ItemID.SNAKE_CORPSE),
	SHADES("shades", ItemID.SHADE_ROBE_TOP),
	SHADOW_WARRIORS("shadow warriors", -1),
	SKELETAL_WYVERNS("skeletal wyverns", ItemID.SKELETAL_WYVERN),
	SKELETONS("skeletons", ItemID.SKELETON_GUARD),
	SMOKE_DEVILS("smoke devils", ItemID.SMOKE_DEVIL),
	SPIDERS("spiders", ItemID.HUGE_SPIDER),
	SPIRITUAL_CREATURES("spiritual creatures", -1),
	STEEL_DRAGONS("steel dragons", ItemID.STEEL_DRAGON),
	SUQAHS("suqahs", ItemID.SUQAH_TOOTH),
	TERROR_DOGS("terror dogs", ItemID.TERROR_DOG),
	TROLLS("trolls", ItemID.TROLL_GUARD),
	TUROTH("turoth", ItemID.TUROTH),
	TZHAAR("tzhaar", ItemID.ENSOULED_TZHAAR_HEAD),
	VAMPIRES("vampires", ItemID.STAKE),
	WALL_BEASTS("wall beasts", ItemID.SWAMP_WALLBEAST),
	WATERFIENDS("waterfiends", -1),
	WEREWOLVES("werewolves", ItemID.WOLFBANE),
	WOLVES("wolves", ItemID.GREY_WOLF_FUR),
	ZOMBIES("zombies", ItemID.ZOMBIE_HEAD);
	//</editor-fold>

	private static final Logger logger = LoggerFactory.getLogger(Task.class);

	private static final Map<String, Task> tasks = new HashMap<>();

	private final String name;

	private final int itemSpriteId;

	static
	{
		for (Task task : values())
		{
			tasks.put(task.getName(), task);
		}
	}

	Task(String name, int itemSpriteId)
	{
		this.name = name;
		this.itemSpriteId = itemSpriteId;
	}

	public static Task getTask(String taskName)
	{
		return tasks.get(taskName);
	}

	public String getName()
	{
		return this.name;
	}

	public BufferedImage getImage()
	{
		if (itemSpriteId == -1)
		{
			return ItemManager.getImage(ItemID.ENCHANTED_GEM);
		}
		return ItemManager.getImage(itemSpriteId);
	}
}
