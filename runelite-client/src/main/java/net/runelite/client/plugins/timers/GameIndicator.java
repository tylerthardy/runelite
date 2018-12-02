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
package net.runelite.client.plugins.timers;

import lombok.Getter;
import java.awt.Color;
import java.awt.image.BufferedImage;
import net.runelite.api.GraphicID;
import net.runelite.api.SpriteID;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.SpriteManager;

enum GameIndicator
{
	VENGEANCE_ACTIVE(SpriteID.SPELL_VENGEANCE_OTHER, GameTimerImageType.SPRITE, GraphicID.VENGEANCE, "Vengeance active");

	@Getter
	private final String description;
	@Getter
	private String text;
	@Getter
	private Color textColor;
	@Getter
	private int graphicId;
	private int imageId = -1;
	private GameTimerImageType imageType;

	private BufferedImage image;

	GameIndicator(int imageId, GameTimerImageType idType, int graphicId, String description, String text, Color textColor)
	{
		this.imageId = imageId;
		this.imageType = idType;
		this.graphicId = graphicId;
		this.description = description;
		this.text = text;
		this.textColor = textColor;
	}
	GameIndicator(int imageId, GameTimerImageType idType, int graphicId, String description) // No text
	{
		this(imageId, idType, graphicId, description, "", null);
	}

	BufferedImage getImage(ItemManager itemManager, SpriteManager spriteManager)
	{
		if (image != null)
		{
			return image;
		}

		switch (imageType)
		{
			case ITEM:
				image = itemManager.getImage(imageId);
				break;
			case SPRITE:
				image = spriteManager.getSprite(imageId, 0);
				break;
		}

		return image;
	}
}
