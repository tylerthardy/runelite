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
package net.runelite.cache.region;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.runelite.cache.IndexType;
import net.runelite.cache.fs.Archive;
import net.runelite.cache.fs.Index;
import net.runelite.cache.fs.Storage;
import net.runelite.cache.fs.Store;
import net.runelite.cache.util.XteaKeyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegionLoader
{
	private static final Logger logger = LoggerFactory.getLogger(RegionLoader.class);

	private static final int MAX_REGION = 32768;

	private final Store store;
	private final Index index;
	private final XteaKeyManager keyManager;

	private final List<Region> regions = new ArrayList<>();
	private Region lowestX = null, lowestY = null;
	private Region highestX = null, highestY = null;

	public RegionLoader(Store store)
	{
		this.store = store;
		index = store.getIndex(IndexType.MAPS);
		keyManager = new XteaKeyManager();
		keyManager.loadKeys();
	}

	public void loadRegions() throws IOException
	{
		for (int i = 0; i < MAX_REGION; ++i)
		{
			Region region = this.loadRegionFromArchive(i);
			if (region != null)
				regions.add(region);
		}
	}

	public Region loadRegionFromArchive(int i) throws IOException
	{
		int x = i >> 8;
		int y = i & 0xFF;

		Storage storage = store.getStorage();
		Archive map = index.findArchiveByName("m" + x + "_" + y);
		Archive land = index.findArchiveByName("l" + x + "_" + y);

		assert (map == null) == (land == null);

		if (map == null || land == null)
		{
			return null;
		}

		byte[] data = map.decompress(storage.loadArchive(map));

		Region region = new Region(i);
		region.loadTerrain(data);

		int[] keys = keyManager.getKeys(i);
		if (keys != null)
		{
			try
			{
				data = land.decompress(storage.loadArchive(land), keys);
				region.loadLocations(data);
			}
			catch (IOException ex)
			{
				logger.debug("Can't decrypt region " + i, ex);
			}
		}

		return region;
	}

	public void calculateBounds()
	{
		for (Region region : regions)
		{
			if (lowestX == null || region.getBaseX() < lowestX.getBaseX())
			{
				lowestX = region;
			}

			if (highestX == null || region.getBaseX() > highestX.getBaseX())
			{
				highestX = region;
			}

			if (lowestY == null || region.getBaseY() < lowestY.getBaseY())
			{
				lowestY = region;
			}

			if (highestY == null || region.getBaseY() > highestY.getBaseY())
			{
				highestY = region;
			}
		}
	}

	public List<Region> getRegions()
	{
		return regions;
	}

	public Region getLowestX()
	{
		return lowestX;
	}

	public Region getLowestY()
	{
		return lowestY;
	}

	public Region getHighestX()
	{
		return highestX;
	}

	public Region getHighestY()
	{
		return highestY;
	}
}
