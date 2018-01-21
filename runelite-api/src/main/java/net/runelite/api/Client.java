/*
 * Copyright (c) 2016-2017, Adam <Adam@sigterm.info>
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
package net.runelite.api;

import java.awt.Canvas;
import java.util.List;
import java.util.Map;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;

public interface Client extends GameEngine
{
	List<Player> getPlayers();

	List<NPC> getNpcs();

	int getBoostedSkillLevel(Skill skill);

	int getRealSkillLevel(Skill skill);

	void sendGameMessage(ChatMessageType type, String message);

	GameState getGameState();

	String getUsername();

	void setUsername(String name);

	Canvas getCanvas();

	int getFPS();

	int getCameraX();

	int getCameraY();

	int getCameraZ();

	int getCameraPitch();

	int getCameraYaw();

	int getWorld();

	int getViewportHeight();

	int getViewportWidth();

	int getScale();

	Point getMouseCanvasPosition();

	int[][][] getTileHeights();

	byte[][][] getTileSettings();

	int getPlane();

	Region getRegion();

	Player getLocalPlayer();

	ItemComposition getItemDefinition(int id);

	SpritePixels createItemSprite(int itemId, int quantity, int border, int shadowColor, int stackable, boolean noted);

	int getBaseX();

	int getBaseY();

	Widget[] getWidgetRoots();

	Widget getWidget(WidgetInfo widget);

	Widget[] getGroup(int groupId);

	Widget getWidget(int groupId, int childId);

	int[] getWidgetPositionsX();

	int[] getWidgetPositionsY();

	String[] getPlayerOptions();

	boolean[] getPlayerOptionsPriorities();

	int[] getPlayerMenuTypes();

	MenuEntry[] getMenuEntries();

	void setMenuEntries(MenuEntry[] entries);

	boolean isMenuOpen();

	int getMapAngle();

	boolean isResized();

	int getRevision();

	int[] getMapRegions();

	int[][] getXteaKeys();

	int[] getSettings();

	int[] getWidgetSettings();

	int getSetting(Varbits varbit);

	int getClanChatCount();

	ClanMember[] getClanMembers();

	HashTable getComponentTable();

	boolean isPrayerActive(Prayer prayer);

	int getSkillExperience(Skill skill);

	int getGameDrawingMode();

	void setGameDrawingMode(int gameDrawingMode);

	void refreshChat();

	Map<Integer, ChatLineBuffer> getChatLineMap();

	Widget getViewportWidget();

	ObjectComposition getObjectDefinition(int objectId);

	Area[] getMapAreas();

	IndexedSprite[] getMapScene();

	int getGameCycle();

	SpritePixels[] getMapIcons();

	IndexedSprite[] getModIcons();

	void setModIcons(IndexedSprite[] modIcons);

	IndexedSprite createIndexedSprite();

	boolean isFriended(String name, boolean mustBeLoggedIn);

	boolean isIgnored(String name);

	boolean isClanMember(String name);

	Point getSceneDestinationLocation();

	List<Projectile> getProjectiles();


	/**
	 * Play a sound effect at the player's current location.
	 * This is how UI, and player-generated (e.g. mining, woodcutting) sound effects are normally played
	 *
	 * @param id the ID of the sound to play. Any int is allowed, but see {@link SoundEffectID} for some common ones
	 */
	void playSoundEffect(int id);

	/**
	 * Play a sound effect from some point in the world.
	 *
	 * @param id the ID of the sound to play. Any int is allowed, but see {@link SoundEffectID} for some common ones
	 * @param x the ground coordinate on the x axis
	 * @param y the ground coordinate on the y axis
	 * @param range the number of tiles away that the sound can be heard from
	 */
	void playSoundEffect(int id, int x, int y, int range);


	BufferProvider getBufferProvider();

	int getMouseIdleTicks();

	int getKeyboardIdleTicks();
}
