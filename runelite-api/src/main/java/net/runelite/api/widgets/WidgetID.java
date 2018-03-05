/*
 * Copyright (c) 2017, Adam <Adam@sigterm.info>
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
package net.runelite.api.widgets;

public class WidgetID
{
	public static final int LOGOUT_PANEL_ID = 182;
	public static final int BANK_GROUP_ID = 12;
	public static final int BANK_INVENTORY_GROUP_ID = 15;
	public static final int DEPOSIT_BOX_GROUP_ID = 192;
	public static final int INVENTORY_GROUP_ID = 149;
	public static final int EQUIPMENT_GROUP_ID = 387;
	public static final int PESTRCONTROL_GROUP_ID = 408;
	public static final int CLAN_CHAT_GROUP_ID = 7;
	public static final int MINIMAP_GROUP_ID = 160;
	public static final int LOGIN_CLICK_TO_PLAY_GROUP_ID = 378;
	public static final int CLUE_SCROLL_GROUP_ID = 203;
	public static final int FIXED_VIEWPORT_GROUP_ID = 548;
	public static final int RESIZABLE_VIEWPORT_OLD_SCHOOL_BOX_GROUP_ID = 161;
	public static final int RESIZABLE_VIEWPORT_BOTTOM_LINE_GROUP_ID = 164;
	public static final int PRAYER_GROUP_ID = 541;
	public static final int SHOP_GROUP_ID = 300;
	public static final int SHOP_INVENTORY_GROUP_ID = 301;
	public static final int COMBAT_GROUP_ID = 593;
	public static final int DIALOG_NPC_GROUP_ID = 231;
	public static final int SLAYER_REWARDS_GROUP_ID = 426;
	public static final int PRIVATE_CHAT = 163;
	public static final int CHATBOX_GROUP_ID = 162;
	public static final int WORLD_MAP_MENU_GROUP_ID = 160;
	public static final int VOLCANIC_MINE_GROUP_ID = 611;
	public static final int BA_ATTACKER_GROUP_ID = 485;
	public static final int BA_COLLECTOR_GROUP_ID = 486;
	public static final int BA_DEFENDER_GROUP_ID = 487;
	public static final int BA_HEALER_GROUP_ID = 488;

	static class WorldMap
	{
		static final int OPTION = 29;
	}

	static class SlayerRewards
	{
		static final int TOP_BAR = 12;
	}

	static class DialogNPC
	{
		static final int HEAD_MODEL = 0;
		static final int NAME = 1;
		static final int CONTINUE = 2;
		static final int TEXT = 3;
	}

	static class LogoutPanel
	{
		static final int LOGOUT_BUTTON = 6;
	}

	static class PestControl
	{
		static final int PURPLE_SHIELD = 21;
		static final int BLUE_SHIELD = 23;
		static final int YELLOW_SHIELD = 25;
		static final int RED_SHIELD = 27;

		static final int PURPLE_HEALTH = 17;
		static final int BLUE_HEALTH = 18;
		static final int YELLOW_HEALTH = 19;
		static final int RED_HEALTH = 20;

		static final int PURPLE_ICON = 13;
		static final int BLUE_ICON = 14;
		static final int YELLOW_ICON = 15;
		static final int RED_ICON = 16;

		static final int ACTIVITY_BAR = 6;
		static final int ACTIVITY_PROGRESS = 8;
	}

	static class ClanChat
	{
		static final int TITLE = 1;
		static final int NAME = 3;
		static final int OWNER = 5;
	}

	static class Bank
	{
		static final int ITEM_CONTAINER = 12;
		static final int INVENTORY_ITEM_CONTAINER = 3;
	}

	static class DepositBox
	{
		static final int INVENTORY_ITEM_CONTAINER = 2;
	}

	static class Shop
	{
		static final int ITEMS_CONTAINER = 2;
		static final int INVENTORY_ITEM_CONTAINER = 0;
	}

	static class Equipment
	{
		static final int HELMET = 6;
		static final int CAPE = 7;
		static final int AMULET = 8;
		static final int WEAPON = 9;
		static final int BODY = 10;
		static final int SHIELD = 11;
		static final int LEGS = 12;
		static final int GLOVES = 13;
		static final int BOOTS = 14;
		static final int RING = 15;
		static final int AMMO = 16;
	}

	static class Cluescroll
	{
		static final int CLUE_TEXT = 2;
	}

	static class Minimap
	{
		static final int XP_ORB = 1;
		static final int PRAYER_ORB = 12;
		static final int QUICK_PRAYER_ORB = 14; // Has the "Quick-prayers" name
		static final int RUN_ORB = 20;
	}

	static class Viewport
	{
		static final int FIXED_VIEWPORT = 20;
		static final int RESIZABLE_VIEWPORT_OLD_SCHOOL_BOX = 12;
		static final int RESIZABLE_VIEWPORT_BOTTOM_LINE = 12;
	}

	static class Chatbox
	{
		static final int CHATBOX_MESSAGES = 29;
		static final int CHATBOX_REPORT_TEXT = 28;
		static final int CHATBOX_BUTTONS = 1;
	}

	static class Prayer
	{
		static final int THICK_SKIN = 4;
		static final int BURST_OF_STRENGTH = 5;
		static final int CLARITY_OF_THOUGHT = 6;
		static final int SHARP_EYE = 22;
		static final int MYSTIC_WILL = 23;
		static final int ROCK_SKIN = 7;
		static final int SUPERHUMAN_STRENGTH = 8;
		static final int IMPROVED_REFLEXES = 9;
		static final int RAPID_RESTORE = 10;
		static final int RAPID_HEAL = 11;
		static final int PROTECT_ITEM = 12;
		static final int HAWK_EYE = 24;
		static final int MYSTIC_LORE = 25;
		static final int STEEL_SKIN = 13;
		static final int ULTIMATE_STRENGTH = 14;
		static final int INCREDIBLE_REFLEXES = 15;
		static final int PROTECT_FROM_MAGIC = 16;
		static final int PROTECT_FROM_MISSILES = 17;
		static final int PROTECT_FROM_MELEE = 18;
		static final int EAGLE_EYE = 26;
		static final int MYSTIC_MIGHT = 27;
		static final int RETRIBUTION = 19;
		static final int REDEMPTION = 20;
		static final int SMITE = 21;
		static final int PRESERVE = 32;
		static final int CHIVALRY = 28;
		static final int PIETY = 29;
		static final int RIGOUR = 30;
		static final int AUGURY = 31;
	}

	static class Combat
	{
		static final int WEAPON_NAME = 1;
		static final int LEVEL = 2;
		static final int STYLE_ONE = 3;
		static final int STYLE_TWO = 7;
		static final int STYLE_THREE = 11;
		static final int STYLE_FOUR = 15;
		static final int SPELLS = 19;
		static final int DEFENSIVE_SPELL_BOX = 20;
		static final int DEFENSIVE_SPELL_ICON = 21;
		static final int DEFENSIVE_SPELL_SHIELD = 22;
		static final int DEFENSIVE_SPELL_TEXT = 23;
		static final int SPELL_BOX = 24;
		static final int SPELL_ICON = 25;
		static final int SPELL_TEXT = 26;
	}

	static class VolcanicMine
	{
		static final int GENERAL_INFOBOX_GROUP_ID = 4;
		static final int TIME_LEFT = 8;
		static final int POINTS = 10;
		static final int STABILITY = 12;
		static final int PLAYER_COUNT = 14;
		static final int VENTS_INFOBOX_GROUP_ID = 15;
		static final int VENT_A_PERCENTAGE = 19;
		static final int VENT_B_PERCENTAGE = 20;
		static final int VENT_C_PERCENTAGE = 21;
		static final int VENT_A_STATUS = 23;
		static final int VENT_B_STATUS = 24;
		static final int VENT_C_STATUS = 25;
	}

	static class BarbarianAssault
	{
		static class ATK
		{
			static final int CORRECT_STYLE2 = 4;
			static final int TO_CALL = 6;
			static final int ROLE = 8;
			static final int ROLE_SPRITE = 7;
		}

		static final int CURRENT_WAVE = 1;
		static final int CORRECT_STYLE = 3;
		static final int TO_CALL = 5;
		static final int ROLE_SPRITE = 6;
		static final int ROLE = 7;
	}
}
