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
package net.runelite.mixins;

import net.runelite.api.Actor;
import net.runelite.api.GameObject;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.WallObject;
import net.runelite.api.events.GameObjectChanged;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.WallObjectChanged;
import net.runelite.api.events.WallObjectDespawned;
import net.runelite.api.events.WallObjectSpawned;
import net.runelite.api.mixins.FieldHook;
import net.runelite.api.mixins.Inject;
import net.runelite.api.mixins.Mixin;
import net.runelite.api.mixins.Shadow;
import static net.runelite.client.callback.Hooks.eventBus;
import net.runelite.rs.api.RSClient;
import net.runelite.rs.api.RSGameObject;
import net.runelite.rs.api.RSTile;

@Mixin(RSTile.class)
public abstract class RSTileMixin implements RSTile
{
	@Shadow("clientInstance")
	private static RSClient client;

	@Inject
	private WallObject previousWallObject;

	@Inject
	private GameObject[] previousGameObjects;

	@Inject
	@Override
	public Point getWorldLocation()
	{
		Point regionLocation = getRegionLocation();
		return Perspective.regionToWorld(client, regionLocation);
	}

	@Inject
	@Override
	public Point getRegionLocation()
	{
		return new Point(getX(), getY());
	}

	@Inject
	@Override
	public Point getLocalLocation()
	{
		Point regionLocation = getRegionLocation();
		return Perspective.regionToLocal(client, regionLocation);
	}

	@FieldHook("wallObject")
	@Inject
	public void wallObjectChanged(int idx)
	{
		WallObject previous = previousWallObject;
		WallObject current = getWallObject();

		previousWallObject = current;

		if (current == null && previous != null)
		{
			WallObjectDespawned wallObjectDespawned = new WallObjectDespawned();
			wallObjectDespawned.setTile(this);
			wallObjectDespawned.setWallObject(previous);
			eventBus.post(wallObjectDespawned);
		}
		else if (current != null && previous == null)
		{
			WallObjectSpawned wallObjectSpawned = new WallObjectSpawned();
			wallObjectSpawned.setTile(this);
			wallObjectSpawned.setWallObject(current);
			eventBus.post(wallObjectSpawned);
		}
		else if (current != null && previous != null)
		{
			WallObjectChanged wallObjectChanged = new WallObjectChanged();
			wallObjectChanged.setTile(this);
			wallObjectChanged.setPrevious(previous);
			wallObjectChanged.setWallObject(current);
			eventBus.post(wallObjectChanged);
		}
	}

	@FieldHook("objects")
	@Inject
	public void gameObjectsChanged(int idx)
	{
		if (idx == -1) // this happens from the field assignment
		{
			return;
		}

		if (previousGameObjects == null)
		{
			previousGameObjects = new GameObject[5];
		}

		// Previous game object
		GameObject previous = previousGameObjects[idx];
		// GameObject that was changed.
		RSGameObject current = (RSGameObject) getGameObjects()[idx];

		// Update previous object to current
		previousGameObjects[idx] = current;

		// Characters seem to generate a constant stream of new GameObjects
		if (current == null || !(current.getRenderable() instanceof Actor))
		{
			if (current == null && previous != null)
			{
				GameObjectDespawned gameObjectDespawned = new GameObjectDespawned();
				gameObjectDespawned.setTile(this);
				gameObjectDespawned.setGameObject(previous);
				eventBus.post(gameObjectDespawned);
			}
			else if (current != null && previous == null)
			{
				GameObjectSpawned gameObjectSpawned = new GameObjectSpawned();
				gameObjectSpawned.setTile(this);
				gameObjectSpawned.setGameObject(current);
				eventBus.post(gameObjectSpawned);
			}
			else if (current != null && previous != null)
			{
				GameObjectChanged gameObjectsChanged = new GameObjectChanged();
				gameObjectsChanged.setTile(this);
				gameObjectsChanged.setPrevious(previous);
				gameObjectsChanged.setGameObject(current);
				eventBus.post(gameObjectsChanged);
			}
		}
	}
}
