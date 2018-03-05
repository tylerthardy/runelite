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
package net.runelite.client.plugins.boosts;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;

@Slf4j
class BoostsOverlay extends Overlay
{
	private final BufferedImage[] imgCache = new BufferedImage[Skill.values().length - 1];

	@Getter
	private final BoostIndicator[] indicators = new BoostIndicator[Skill.values().length - 1];

	private final Client client;
	private final BoostsConfig config;
	private final InfoBoxManager infoBoxManager;

	@Inject
	private BoostsPlugin plugin;

	private PanelComponent panelComponent;

	@Inject
	BoostsOverlay(@Nullable Client client, BoostsConfig config, InfoBoxManager infoBoxManager)
	{
		setPosition(OverlayPosition.TOP_LEFT);
		setPriority(OverlayPriority.MED);
		this.client = client;
		this.config = config;
		this.infoBoxManager = infoBoxManager;
	}

	@Override
	public Dimension render(Graphics2D graphics, Point parent)
	{
		if (!config.enabled())
		{
			return null;
		}

		panelComponent = new PanelComponent();

		for (Skill skill : plugin.getShownSkills())
		{
			int boosted = client.getBoostedSkillLevel(skill),
				base = client.getRealSkillLevel(skill);

			if (boosted == base)
			{
				if (indicators[skill.ordinal()] != null)
				{
					infoBoxManager.removeInfoBox(indicators[skill.ordinal()]);
					indicators[skill.ordinal()] = null;
				}

				continue;
			}

			if (config.displayIndicators())
			{
				if (indicators[skill.ordinal()] == null)
				{
					BoostIndicator indicator = new BoostIndicator(skill, getSkillImage(skill), client, config);
					indicators[skill.ordinal()] = indicator;
					infoBoxManager.addInfoBox(indicator);
				}
			}
			else
			{
				String str;
				Color strColor = Color.WHITE;
				if (!config.useRelativeBoost())
				{
					str = boosted + "/" + base;
				}
				else
				{
					int boost = boosted - base;
					str = String.valueOf(boost);
					strColor = getTextColor(boost);
					if (boost > 0)
					{
						str = "+" + str;
					}
				}

				panelComponent.getLines().add(new PanelComponent.Line(
					skill.getName(),
					Color.WHITE,
					str,
					strColor
				));
			}
		}

		return config.displayIndicators() ? null : panelComponent.render(graphics, parent);
	}

	private Color getTextColor(int boost)
	{
		if (boost > 0)
		{
			return Color.GREEN;
		}

		return new Color(238, 51, 51);

	}

	private BufferedImage getSkillImage(Skill skill)
	{
		int skillIdx = skill.ordinal();
		BufferedImage skillImage = null;

		if (imgCache[skillIdx] != null)
		{
			return imgCache[skillIdx];
		}

		try
		{
			String skillIconPath = "/skill_icons/" + skill.getName().toLowerCase() + ".png";
			log.debug("Loading skill icon from {}", skillIconPath);
			skillImage = ImageIO.read(BoostsOverlay.class.getResourceAsStream(skillIconPath));
			imgCache[skillIdx] = skillImage;
		}
		catch (IOException e)
		{
			log.debug("Error Loading skill icons {}", e);
		}

		return skillImage;
	}
}
