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
package net.runelite.client.game;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.runelite.api.Client;
import net.runelite.api.SpritePixels;
import net.runelite.client.RuneLite;
import net.runelite.client.plugins.runepouch.RunepouchOverlay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

public class ItemImageCache
{
	private static final Logger logger = LoggerFactory.getLogger(ItemImageCache.class);
	private static final Client client = RuneLite.getClient();

	private final LoadingCache<Integer, BufferedImage> cache;

	public ItemImageCache()
	{
		cache = CacheBuilder.newBuilder()
			//.maximumSize(RUNE_NAMES.length)
			.build(
				new CacheLoader<Integer, BufferedImage>()
				{
					@Override
					public BufferedImage load(Integer itemId) throws Exception
					{
						SpritePixels sprite = client.createItemSprite(itemId, 1, 1, SpritePixels.DEFAULT_SHADOW_COLOR, 0, false);
						int[] pixels = sprite.getPixels();
						BufferedImage img = new BufferedImage(sprite.getWidth(), sprite.getHeight(), BufferedImage.TYPE_INT_ARGB);

						for (int i = 0; i < pixels.length ; i++)
						{
							if (pixels[i] != 0)
							{
								pixels[i] |= 0xff000000;
							}
						}

						img.setRGB(0, 0, sprite.getWidth(), sprite.getHeight(), pixels, 0, sprite.getWidth());

						return img;
					}
				}
			);
	}

	public BufferedImage getImage(int itemId)
	{
		try
		{
			return cache.get(itemId);
		}
		catch (ExecutionException e)
		{
			logger.warn("unable to load item image", e);
			return null;
		}
	}
}
