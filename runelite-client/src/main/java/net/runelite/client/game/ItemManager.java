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
package net.runelite.client.game;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import net.runelite.api.Client;
import net.runelite.api.SpritePixels;
import net.runelite.client.RuneLite;
import net.runelite.http.api.item.ItemClient;
import net.runelite.http.api.item.ItemPrice;

public class ItemManager
{
	/**
	 * not yet looked up
	 */

	static final ItemPrice EMPTY = new ItemPrice();
	/**
	 * has no price
	 */
	static final ItemPrice NONE = new ItemPrice();

	private final ItemClient itemClient = new ItemClient();
	private final LoadingCache<Integer, ItemPrice> itemPrices;
	private final Client client = RuneLite.getClient();

	public ItemManager(RuneLite runelite)
	{
		itemPrices = CacheBuilder.newBuilder()
			.maximumSize(512L)
			.expireAfterAccess(1, TimeUnit.HOURS)
			.build(new ItemPriceLoader(runelite, itemClient));
	}

	/**
	 * Look up an item's price asynchronously.
	 *
	 * @param itemId
	 * @return the price, or null if the price is not yet loaded
	 */
	public ItemPrice get(int itemId)
	{
		ItemPrice itemPrice = itemPrices.getIfPresent(itemId);
		if (itemPrice != null && itemPrice != EMPTY)
		{
			return itemPrice == NONE ? null : itemPrice;
		}

		itemPrices.refresh(itemId);
		return null;
	}

	/**
	 * Look up an item's price synchronously
	 *
	 * @param itemId
	 * @return
	 * @throws IOException
	 */
	public ItemPrice getItemPrice(int itemId) throws IOException
	{
		ItemPrice itemPrice = itemPrices.getIfPresent(itemId);
		if (itemPrice != null && itemPrice != EMPTY)
		{
			return itemPrice == NONE ? null : itemPrice;
		}

		itemPrice = itemClient.lookupItemPrice(itemId);
		itemPrices.put(itemId, itemPrice);
		return itemPrice;
	}

	/**
	 * Convert a quantity to stack size
	 *
	 * @param quantity
	 * @return
	 */
	public static String quantityToStackSize(int quantity)
	{
		if (quantity >= 10_000_000)
		{
			return quantity / 1_000_000 + "M";
		}

		if (quantity >= 100_000)
		{
			return quantity / 1_000 + "K";
		}

		return "" + quantity;
	}

	public Image getImage(int itemId)
	{
		return getImage(itemId, 1, 1, SpritePixels.DEFAULT_SHADOW_COLOR, 0, false);
	}

	public Image getImage(int itemId, int quantity, int border, int bgColor, int stacked, boolean noted)
	{
		SpritePixels sprite = client.createItemSprite(itemId, quantity, border, bgColor, stacked, noted);
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
