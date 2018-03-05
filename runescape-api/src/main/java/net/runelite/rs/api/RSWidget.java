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
package net.runelite.rs.api;

import net.runelite.api.widgets.Widget;
import net.runelite.mapping.Import;

public interface RSWidget extends Widget
{
	@Import("dynamicValues")
	int[][] getDynamicValues();

	@Import("children")
	RSWidget[] getChildren();

	@Import("id")
	@Override
	int getId();

	@Import("parentId")
	int getRSParentId();

	@Import("boundsIndex")
	int getBoundsIndex();

	@Import("modelId")
	@Override
	int getModelId();

	@Import("itemIds")
	int[] getItemIds();

	@Import("itemQuantities")
	int[] getItemQuantities();

	@Import("modelType")
	int getModelType();

	@Import("actions")
	String[] getActions();

	@Import("text")
	String getRSText();

	@Import("name")
	String getRSName();

	@Import("name")
	void setRSName(String name);

	@Import("text")
	@Override
	void setText(String text);

	@Import("textColor")
	@Override
	int getTextColor();

	@Import("textColor")
	@Override
	void setTextColor(int textColor);

	@Import("opacity")
	int getOpacity();

	@Import("relativeX")
	@Override
	int getRelativeX();

	@Import("relativeX")
	@Override
	void setRelativeX(int x);

	@Import("relativeY")
	@Override
	int getRelativeY();

	@Import("relativeY")
	@Override
	void setRelativeY(int y);

	@Import("width")
	@Override
	int getWidth();

	@Import("width")
	@Override
	void setWidth(int width);

	@Import("height")
	@Override
	int getHeight();

	@Import("height")
	@Override
	void setHeight(int height);

	@Import("isHidden")
	boolean isRSHidden();

	@Import("isHidden")
	void setHidden(boolean hidden);

	@Import("index")
	int getIndex();

	@Import("rotationX")
	int getRotationX();

	@Import("rotationY")
	int getRotationY();

	@Import("rotationZ")
	int getRotationZ();

	@Import("contentType")
	@Override
	int getContentType();

	@Import("contentType")
	@Override
	void setContentType(int contentType);

	@Import("type")
	@Override
	int getType();

	@Import("scrollX")
	int getScrollX();

	@Import("scrollY")
	int getScrollY();

	@Import("spriteId")
	@Override
	int getSpriteId();

	@Import("spriteId")
	@Override
	void setSpriteId(int spriteId);

	@Import("borderThickness")
	int getBorderThickness();

	@Import("itemId")
	@Override
	int getItemId();

	@Import("itemQuantity")
	@Override
	int getItemQuantity();

	@Import("originalX")
	int getOriginalX();

	@Import("originalY")
	int getOriginalY();

	@Import("paddingX")
	int getPaddingX();

	@Import("paddingY")
	int getPaddingY();
}
