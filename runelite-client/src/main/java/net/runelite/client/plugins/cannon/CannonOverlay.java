/*
 * Copyright (c) 2016-2018, Seth <Sethtroll3@gmail.com>
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
package net.runelite.client.plugins.cannon;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.widgets.Widget;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.components.TextComponent;

class CannonOverlay extends Overlay
{
	private final Client client;
	private final CannonConfig config;
	private final CannonPlugin plugin;
	private final TextComponent textComponent = new TextComponent();

	@Inject
	CannonOverlay(Client client, CannonConfig config, CannonPlugin plugin)
	{
		setPosition(OverlayPosition.DYNAMIC);
		setPriority(OverlayPriority.MED);
		this.client = client;
		this.config = config;
		this.plugin = plugin;
	}

	@Override
	public Dimension render(Graphics2D graphics, Point parent)
	{
		if (!plugin.isCannonPlaced() || plugin.getCannonPosition() == null)
		{
			return null;
		}

		if (!Perspective.isWorldInScene(client, plugin.getCannonPosition()))
		{
			return null;
		}

		net.runelite.api.Point cannonPoint = Perspective.worldToLocal(client, plugin.getCannonPosition());

		if (cannonPoint == null)
		{
			return null;
		}

		net.runelite.api.Point cannonLoc = Perspective.getCanvasTextLocation(client,
			graphics,
			cannonPoint,
			String.valueOf(plugin.getCballsLeft()), 200);

		Widget viewport = client.getViewportWidget();

		if (viewport == null)
		{
			return null;
		}

		if (cannonLoc != null && viewport.contains(cannonLoc))
		{
			textComponent.setText(String.valueOf(plugin.getCballsLeft()));
			textComponent.setPosition(new Point(cannonLoc.getX(), cannonLoc.getY()));
			textComponent.setColor(plugin.getStateColor());
			textComponent.render(graphics, parent);
		}

		return null;
	}
}