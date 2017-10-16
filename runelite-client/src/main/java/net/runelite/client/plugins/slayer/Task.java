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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

enum Task
{
	//<editor-fold desc="Enums">
	ABERRANT_SPECTRE("aberrant spectre", new HashSet<>(Arrays.asList("deviant spectre"))),
	ABYSSAL_DEMON("abyssal demon", new HashSet<>(Arrays.asList("abyssal sire"))),
	ANKOU("ankou", null),
	BANSHEE("banshee", null),
	BASILISK("basilisk", null),
	BAT("bat", new HashSet<>(Arrays.asList("giant bat"))),
	BEAR("bear", new HashSet<>(Arrays.asList("black bear","grizzly bear","callisto"))),
	BIRD("bird", new HashSet<>(Arrays.asList("bird","chicken","mounted terrorbird gnome","rooster","terrorbird","vulture","undead chicken"))),
	BLACK_DEMON("black demon", new HashSet<>(Arrays.asList("demonic gorilla","balfrug kreeyath","skotizo"))),
	BLACK_DRAGON("black dragon", new HashSet<>(Arrays.asList("baby black dragon","king black dragon","brutal black dragon"))),
	BLOODVELD("bloodveld", new HashSet<>(Arrays.asList("mutated bloodveld"))),
	BLUE_DRAGON("blue dragon", new HashSet<>(Arrays.asList("baby blue dragon","brutal blue dragon"))),
	BRINE_RAT("brine rat", null),
	BRONZE_DRAGON("bronze dragon", null),
	CATABLEPON("catablepon", null),
	CAVE_BUG("cave bug", null),
	CAVE_CRAWLER("cave crawler", null),
	CAVE_HORROR("cave horror", null),
	CAVE_SLIME("cave slime", null),
	COCKATRICE("cockatrice", null),
	COW("cow", null),
	CRAWLING_HAND("crawling hand", null),
	CROCODILE("crocodile", null),
	DAGANNOTH("dagannoth", new HashSet<>(Arrays.asList("dagannoth fledgeling","dagannoth spawn","dagannoth prime","dagannoth rex","dagannoth supreme"))),
	DARK_BEAST("dark beast", null),
	DESERT_LIZARD("desert lizard", new HashSet<>(Arrays.asList("lizard","small lizard"))),
	DOG("dog", new HashSet<>(Arrays.asList("guard dog","wild dog","jackal"))),
	DUST_DEVIL("dust devil", null),
	DWARF("dwarf", new HashSet<>(Arrays.asList("black guard","chaos dwarf"))),
	EARTH_WARRIOR("earth warrior", null),
	ELF("elf", new HashSet<>(Arrays.asList("mourner (lvl 108 variant only)"))),
	FEVER_SPIDER("fever spider", null),
	FIRE_GIANT("fire giant", null),
	FLESH_CRAWLER("flesh crawler", null),
	GARGOYLE("gargoyle", null),
	GHOST("ghost", new HashSet<>(Arrays.asList("tortured soul"))),
	GHOUL("ghoul", new HashSet<>(Arrays.asList("ghoul champion"))),
	GOBLIN("goblin", new HashSet<>(Arrays.asList("goblin champion","sergeant strongstack","sergeant grimspike","sergeant steelwill"))),
	GORAK("gorak", null),
	GREATER_DEMON("greater demon", new HashSet<>(Arrays.asList("kril tsutsaroth","tstanon karlak","skotizo"))),
	GREEN_DRAGON("green dragon", new HashSet<>(Arrays.asList("baby green dragon","brutal green dragon"))),
	HARPIE_BUG_SWARM("harpie bug swarm", null),
	HELLHOUND("hellhound", new HashSet<>(Arrays.asList("cerberu"))),
	HILL_GIANT("hill giant", new HashSet<>(Arrays.asList("giant champion"))),
	HOBGOBLIN("hobgoblin", new HashSet<>(Arrays.asList("hobgoblin champion"))),
	ICEFIEND("icefiend", null),
	ICE_GIANT("ice giant", null),
	ICE_WARRIOR("ice warrior", null),
	INFERNAL_MAGE("infernal mage", null),
	IRON_DRAGON("iron dragon", null),
	JELLY("jelly", new HashSet<>(Arrays.asList("warped jelly"))),
	JUNGLE_HORROR("jungle horror", null),
	KALPHITE("kalphite", new HashSet<>(Arrays.asList("kalphite queen","kalphite worker","kalphite soldier","kalphite guardian"))),
	KILLERWATT("killerwatt", null),
	KURASK("kurask", null),
	LESSER_DEMON("lesser demon", null),
	LIZARDMAN("lizardman", new HashSet<>(Arrays.asList("lizardman brute","lizardman shaman"))),
	MITHRIL_DRAGON("mithril dragon", null),
	MINOTAUR("minotaur", null),
	MOGRE("mogre", null),
	MOLANISK("molanisk", null),
	MONKEY("monkey", new HashSet<>(Arrays.asList("monkey guard","monkey archer","zombie monkey and more (like padulah)"))),
	MOSS_GIANT("moss giant", null),
	NECHRYAEL("nechryael", null),
	OGRE("ogre", new HashSet<>(Arrays.asList("ogre chieftain","enclave guard"))),
	OTHERWORLDLY_BEING("otherworldly being", null),
	PYREFIEND("pyrefiend", null),
	RED_DRAGON("red dragon", new HashSet<>(Arrays.asList("baby red dragon","brutal red dragon"))),
	ROCKSLUG("rockslug", null),
	SCABARITE("scabarite", new HashSet<>(Arrays.asList("locust rider","scarab mage","scarab swarm"))),
	SCORPION("scorpion", new HashSet<>(Arrays.asList("king scorpion","poison scorpion","pit scorpion","scorpia"))),
	SEA_SNAKE("sea snake", new HashSet<>(Arrays.asList("sea snake hatchling","sea snake young","giant sea snake"))),
	SHADE("shade", new HashSet<>(Arrays.asList("loar","phryn","riyl","asyn and fiyr shade"))),
	SHADOW_WARRIOR("shadow warrior", null),
	SKELETAL_WYVERN("skeletal wyvern", null),
	SKELETON("skeleton", new HashSet<>(Arrays.asList("skeleton champion","vetion"))),
	SMOKE_DEVIL("smoke devil", new HashSet<>(Arrays.asList("thermonuclear smoke devil","nuclear smoke devil"))),
	SPIDER("spider", new HashSet<>(Arrays.asList("giant spider","shadow spider","jungle spider","deadly red spider","poison spider","blessed spider","kalrag","crypt spider"))),
	STEEL_DRAGON("steel dragon", null),
	SUQAH("suqah", null),
	TERROR_DOG("terror dog", null),
	TROLL("troll", new HashSet<>(Arrays.asList("mountain troll","ice troll grunt","runt","male and female","ice troll","river troll"))),
	TUROTH("turoth", null),
	VAMPIRE("vampire", null),
	WALL_BEAST("wall beast", null),
	WATERFIEND("waterfiend", null),
	WEREWOLF("werewolf", null),
	WOLF("wolf", new HashSet<>(Arrays.asList("big wolf","dire wolf","jungle wolf","desert wolf","ice wolf"))),
	ZOMBIE("zombie", new HashSet<>(Arrays.asList("any zombie-class except zogres and zombie monkey"))),
	ZYGOMITE("zygomite", new HashSet<>(Arrays.asList("ancient zygomite")));
	//</editor-fold>

	private static final Logger logger = LoggerFactory.getLogger(Task.class);

	private static final Map<String, Task> tasks = new HashMap<>();

	private final String name;
	private final HashSet<String> alternatives;

	private BufferedImage image;

	static
	{
		for (Task task : values())
		{
			tasks.put(task.getName(), task);
		}
	}

	Task(String name, HashSet<String> alternatives)
	{
		this.name = name;
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

	public BufferedImage getImage()
	{
		if (image != null)
		{
			return image;
		}

		InputStream in = Task.class.getResourceAsStream("Enchanted_gem.png");
		try
		{
			image = ImageIO.read(in);
		}
		catch (IOException ex)
		{
			logger.warn("unable to load image", ex);
		}

		return image;
	}
}
