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

import net.runelite.api.Client;
import net.runelite.api.ItemID;
import net.runelite.api.SpritePixels;
import net.runelite.client.RuneLite;
import net.runelite.client.game.ItemManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

enum Task
{
	//<editor-fold desc="Enums">
	ABERRANT_SPECTRE("aberrant spectre", ItemID.ABERRANT_SPECTRE, new HashSet<>(Arrays.asList("deviant spectre"))),
	ABYSSAL_DEMON("abyssal demon", ItemID.ABYSSAL_DEMON, new HashSet<>(Arrays.asList("abyssal sire"))),
	ANKOU("ankou", ItemID.ANKOU_MASK, null),
	BANSHEE("banshee", ItemID.BANSHEE, null),
	BASILISK("basilisk", ItemID.BASILISK, null),
	BAT("bat", ItemID.GIRAL_BAT_2, new HashSet<>(Arrays.asList("giant bat"))),
	BEAR("bear", ItemID.ENSOULED_BEAR_HEAD, new HashSet<>(Arrays.asList("black bear","grizzly bear","callisto"))),
	BIRD("bird", ItemID.FEATHER, new HashSet<>(Arrays.asList("bird","chicken","mounted terrorbird gnome","rooster","terrorbird","vulture","undead chicken"))),
	BLACK_DEMON("black demon", ItemID.BLACK_DEMON_MASK, new HashSet<>(Arrays.asList("demonic gorilla","balfrug kreeyath","skotizo"))),
	BLACK_DRAGON("black dragon", ItemID.BLACK_DRAGON_MASK, new HashSet<>(Arrays.asList("baby black dragon","king black dragon","brutal black dragon"))),
	BLOODVELD("bloodveld", ItemID.BLOODVELD, new HashSet<>(Arrays.asList("mutated bloodveld"))),
	BLUE_DRAGON("blue dragon", ItemID.BLUE_DRAGON_MASK, new HashSet<>(Arrays.asList("baby blue dragon","brutal blue dragon"))),
	BRINE_RAT("brine rat", ItemID.BRINE_RAT, null),
	BRONZE_DRAGON("bronze dragon", ItemID.BRONZE_DRAGON_MASK, null),
	CATABLEPON("catablepon", ItemID.LEFT_SKULL_HALF, null),
	CAVE_BUG("cave bug", ItemID.SWAMP_CAVE_BUG, null),
	CAVE_CRAWLER("cave crawler", ItemID.CAVE_CRAWLER, null),
	CAVE_HORROR("cave horror", ItemID.CAVE_HORROR, null),
	CAVE_SLIME("cave slime", ItemID.SWAMP_CAVE_SLIME, null),
	COCKATRICE("cockatrice", ItemID.COCKATRICE, null),
	COW("cow", ItemID.COW_MASK, null),
	CRAWLING_HAND("crawling hand", ItemID.CRAWLING_HAND, null),
	CROCODILE("crocodile", -1, null),
	DAGANNOTH("dagannoth", ItemID.DAGANNOTH, new HashSet<>(Arrays.asList("dagannoth fledgeling","dagannoth spawn","dagannoth prime","dagannoth rex","dagannoth supreme"))),
	DARK_BEAST("dark beast", ItemID.DARK_BEAST, null),
	DESERT_LIZARD("desert lizard", ItemID.DESERT_LIZARD, new HashSet<>(Arrays.asList("lizard","small lizard"))),
	DOG("dog", ItemID.GUARD_DOG, new HashSet<>(Arrays.asList("guard dog","wild dog","jackal"))),
	DUST_DEVIL("dust devil", ItemID.DUST_DEVIL, null),
	DWARF("dwarf", ItemID.DWARVEN_HELMET, new HashSet<>(Arrays.asList("black guard","chaos dwarf"))),
	EARTH_WARRIOR("earth warrior", ItemID.BRONZE_FULL_HELM_T, null),
	ELF("elf", ItemID.ELF, new HashSet<>(Arrays.asList("mourner (lvl 108 variant only)"))),
	FEVER_SPIDER("fever spider", ItemID.FEVER_SPIDER, null),
	FIRE_GIANT("fire giant", -1, null),
	FLESH_CRAWLER("flesh crawler", -1, null),
	FOSSIL_ISLAND_WYVERN("fossil island wyvern", -1, new HashSet<>(Arrays.asList("ancient wyvern","spitting wyvern","taloned wyvern","long-tailed wyvern"))),
	GARGOYLE("gargoyle", ItemID.GARGOYLE, null),
	GHOST("ghost", -1, new HashSet<>(Arrays.asList("tortured soul"))),
	GHOUL("ghoul", -1, new HashSet<>(Arrays.asList("ghoul champion"))),
	GOBLIN("goblin", ItemID.ENSOULED_GOBLIN_HEAD, new HashSet<>(Arrays.asList("goblin champion","sergeant strongstack","sergeant grimspike","sergeant steelwill"))),
	GORAK("gorak", ItemID.GORAK_CLAWS, null),
	GREATER_DEMON("greater demon", ItemID.GREATER_DEMON_MASK, new HashSet<>(Arrays.asList("kril tsutsaroth","tstanon karlak","skotizo"))),
	GREEN_DRAGON("green dragon", ItemID.GREEN_DRAGON_MASK, new HashSet<>(Arrays.asList("baby green dragon","brutal green dragon"))),
	HARPIE_BUG_SWARM("harpie bug swarm", ItemID.SWARM, null),
	HELLHOUND("hellhound", ItemID.HELLHOUND, new HashSet<>(Arrays.asList("cerberu"))),
	HILL_GIANT("hill giant", ItemID.ENSOULED_GIANT_HEAD, new HashSet<>(Arrays.asList("giant champion"))),
	HOBGOBLIN("hobgoblin", ItemID.HOBGOBLIN_GUARD, new HashSet<>(Arrays.asList("hobgoblin champion"))),
	ICEFIEND("icefiend", -1, null),
	ICE_GIANT("ice giant", -1, null),
	ICE_WARRIOR("ice warrior", ItemID.MITHRIL_FULL_HELM_T, null),
	INFERNAL_MAGE("infernal mage", ItemID.INFERNAL_MAGE, null),
	IRON_DRAGON("iron dragon", ItemID.IRON_DRAGON_MASK, null),
	JELLY("jelly", ItemID.JELLY, new HashSet<>(Arrays.asList("warped jelly"))),
	JUNGLE_HORROR("jungle horror", -1, null),
	LIZARDMAN("lizardman", -1, new HashSet<>(Arrays.asList("lizardman brute","lizardman shaman"))),
	KALPHITE("kalphite", ItemID.KALPHITE_SOLDIER, new HashSet<>(Arrays.asList("kalphite queen","kalphite worker","kalphite soldier","kalphite guardian"))),
	KILLERWATT("killerwatt", ItemID.KILLERWATT, null),
	KURASK("kurask", ItemID.KURASK, null),
	LESSER_DEMON("lesser demon", ItemID.LESSER_DEMON_MASK, null),
	MITHRIL_DRAGON("mithril dragon", ItemID.MITHRIL_DRAGON_MASK, null),
	MINOTAUR("minotaur", ItemID.ENSOULED_MINOTAUR_HEAD, null),
	MOGRE("mogre", ItemID.MOGRE, null),
	MOLANISK("molanisk", ItemID.MOLANISK, null),
	MONKEY("monkey", ItemID.ENSOULED_MONKEY_HEAD, new HashSet<>(Arrays.asList("monkey guard","monkey archer","zombie monkey and more (like padulah)"))),
	MOSS_GIANT("moss giant", -1, null),
	NECHRYAEL("nechryael", ItemID.NECHRYAEL, null),
	OGRE("ogre", ItemID.ENSOULED_OGRE_HEAD, new HashSet<>(Arrays.asList("ogre chieftain","enclave guard"))),
	OTHERWORLDLY_BEING("otherworldly being", ItemID.GHOSTLY_HOOD, null),
	PYREFIEND("pyrefiend", ItemID.PYREFIEND, null),
	RED_DRAGON("red dragon", ItemID.BABY_RED_DRAGON, new HashSet<>(Arrays.asList("baby red dragon","brutal red dragon"))),
	ROCKSLUG("rockslug", ItemID.ROCKSLUG, null),
	SCABARITE("scabarite", ItemID.GOLDEN_SCARAB, new HashSet<>(Arrays.asList("locust rider","scarab mage","scarab swarm"))),
	SCORPION("scorpion", -1, new HashSet<>(Arrays.asList("king scorpion","poison scorpion","pit scorpion","scorpia"))),
	SEA_SNAKE("sea snake", ItemID.SNAKE_CORPSE, new HashSet<>(Arrays.asList("sea snake hatchling","sea snake young","giant sea snake"))),
	SHADE("shade", ItemID.SHADE_ROBE_TOP, new HashSet<>(Arrays.asList("loar","phryn","riyl","asyn and fiyr shade"))),
	SHADOW_WARRIOR("shadow warrior", -1, null),
	SKELETAL_WYVERN("skeletal wyvern", ItemID.SKELETAL_WYVERN, null),
	SKELETON("skeleton", ItemID.SKELETON_GUARD, new HashSet<>(Arrays.asList("skeleton champion","vetion"))),
	SMOKE_DEVIL("smoke devil", ItemID.SMOKE_DEVIL, new HashSet<>(Arrays.asList("thermonuclear smoke devil","nuclear smoke devil"))),
	SPIDER("spider", ItemID.HUGE_SPIDER, new HashSet<>(Arrays.asList("giant spider","shadow spider","jungle spider","deadly red spider","poison spider","blessed spider","kalrag","crypt spider"))),
	STEEL_DRAGON("steel dragon", ItemID.STEEL_DRAGON, null),
	SUQAH("suqah", ItemID.SUQAH_TOOTH, null),
	TERROR_DOG("terror dog", ItemID.TERROR_DOG, null),
	TROLL("troll", ItemID.TROLL_GUARD, new HashSet<>(Arrays.asList("mountain troll","ice troll grunt","runt","male and female","ice troll","river troll"))),
	TUROTH("turoth", ItemID.TUROTH, null),
	VAMPIRE("vampire", ItemID.STAKE, null),
	WALL_BEAST("wall beast", ItemID.SWAMP_WALLBEAST, null),
	WATERFIEND("waterfiend", -1, null),
	WEREWOLF("werewolf", ItemID.WOLFBANE, null),
	WOLF("wolf", ItemID.GREY_WOLF_FUR, new HashSet<>(Arrays.asList("big wolf","dire wolf","jungle wolf","desert wolf","ice wolf"))),
	ZOMBIE("zombie", ItemID.ZOMBIE_HEAD, new HashSet<>(Arrays.asList("any zombie-class except zogres and zombie monkey"))),
	ZYGOMITE("zygomite", ItemID.MUTATED_ZYGOMITE, new HashSet<>(Arrays.asList("ancient zygomite"))), ;
	//</editor-fold>

	private static final Logger logger = LoggerFactory.getLogger(Task.class);

	private Client client = RuneLite.getClient();

	private static final Map<String, Task> tasks = new HashMap<>();

	private final String name;
	private final HashSet<String> alternatives;

	private final int itemSpriteId;

	static
	{
		for (Task task : values())
		{
			tasks.put(task.getName(), task);
		}
	}

	Task(String name, int itemSpriteId, HashSet<String> alternatives)
	{
		this.name = name;
		this.itemSpriteId = itemSpriteId;
		this.alternatives = alternatives;
	}

	public static Task getTask(String taskName)
	{
		return tasks.get(taskName);
	}

	public String getName()
	{
		return this.name;
	}

	public static boolean isNpcFromTask(String npcName, String taskName)
	{
		System.out.println(npcName);
		System.out.println(taskName);

		Task task = tasks.get(taskName);
		if (task == null)
			return false;

		return task.alternatives.contains(npcName);
	}

	public SpritePixels getSprite()
	{
		return client.createItemSprite(itemSpriteId, 1, 0, SpritePixels.DEFAULT_SHADOW_COLOR, 0, false);
	}

	public BufferedImage getImage()
	{
		return ItemManager.getImage(itemSpriteId);
	}
}
