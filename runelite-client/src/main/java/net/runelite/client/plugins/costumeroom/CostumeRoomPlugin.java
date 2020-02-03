/*
 * Copyright (c) 2019, Tyler <https://github.com/tylerthardy>
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

package net.runelite.client.plugins.costumeroom;

import com.google.inject.Provides;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetID;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.chatbox.ChatboxPanelManager;
import net.runelite.client.game.chatbox.ChatboxTextInput;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.*;

@Slf4j
@PluginDescriptor(
	name = "Costume Room",
	description = "Arranges and filters contents of costume room furniture",
	tags = {"poh"}
)
public class CostumeRoomPlugin extends Plugin
{
	private static final String MORE_LABEL = "More...";
	private final int WIDGETS_PER_GROUP = 4;
	private final int BG_LABEL_OFFSET = 2;
	private final int ICON_OFFSET = 16 - BG_LABEL_OFFSET;

	private static final String MENU_OPEN = "Open";
	private static final String MENU_CLOSE = "Close";

	@Inject
	private Client client;

	@Inject
	private CostumeRoomConfig config;

	@Inject
	private ChatboxPanelManager chatboxPanelManager;

	@Inject
	private ClientThread clientThread;

	private ChatboxTextInput searchInput = null;
	private Widget searchBtn;
	private Collection<ItemWidgetGroup> codes = null;

	@Data
	private static class ItemWidgetGroup
	{
		private Widget background;

		private Widget icon;

		private Widget label;

		@Nullable
		private Widget overlay;
	}

	@Provides
    CostumeRoomConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(CostumeRoomConfig.class);
	}

	private void processChestItems(Widget list)
	{
		Widget[] items = list.getChildren();
		if (items == null || items.length == 0)
		{
			waitForWidgetLoad = true;
			return;
		}

		// Split into deposited and missing items
		List<ItemWidgetGroup> deposited = new ArrayList<>();
		List<ItemWidgetGroup> missing = new ArrayList<>();
		for (int i = 0; i < items.length / WIDGETS_PER_GROUP; i++) {
			int itemIdx = i * 4;
			Widget background = items[itemIdx];
			Widget icon = items[itemIdx + 1];
			Widget label = items[itemIdx + 2];
			Widget overlay =  items[itemIdx + 3];

			ItemWidgetGroup itemWidgetGroup = new ItemWidgetGroup();
			itemWidgetGroup.setBackground(background);
			itemWidgetGroup.setIcon(icon);
			itemWidgetGroup.setLabel(label);
			itemWidgetGroup.setOverlay(overlay);

			if (itemWidgetGroup.getOverlay().isHidden())
			{
				if (itemWidgetGroup.getLabel().getText().equals(MORE_LABEL))
				{
					// Force to end by adding to missing
					missing.add(itemWidgetGroup);
					continue;
				}
				deposited.add(itemWidgetGroup);
			} else {
				missing.add(itemWidgetGroup);
			}
		}

		// Reposition deposited to top, followed by missing
		List<ItemWidgetGroup> itemGroups = new ArrayList<>();
		itemGroups.addAll(deposited);
		itemGroups.addAll(missing);

		int y = BG_LABEL_OFFSET;
		for (ItemWidgetGroup itemGroup : itemGroups)
		{
			if (itemGroup.getBackground() != null)
			{
				itemGroup.getBackground().setOriginalY(y);
			}
			if (itemGroup.getIcon() != null)
			{
				itemGroup.getIcon().setOriginalY(y + ICON_OFFSET);
			}
			if (itemGroup.getLabel() != null)
			{
				itemGroup.getLabel().setOriginalY(y);
			}
			if (itemGroup.getOverlay() != null)
			{
				itemGroup.getOverlay().setOriginalY(y);
			}

			y += itemGroup.getBackground().getHeight();
		}

		list.setScrollHeight(y);
		list.revalidateScroll();
		//TODO: This
		/*client.runScript(
			ScriptID.UPDATE_SCROLLBAR,
			WidgetInfo.FAIRY_RING_LIST_SCROLLBAR.getId(),
			WidgetInfo.FAIRY_RING_LIST.getId(),
			newHeight
		);*/
	}

	private boolean waitForWidgetLoad = false;
	@Subscribe
	public void onGameTick(GameTick gameTick)
	{
		if (waitForWidgetLoad)
		{
			Widget list = client.getWidget(592, 2);
			if (list == null)
			{
				waitForWidgetLoad = false;
				return;
			}

			Widget[] items = list.getChildren();
			if (items.length == 0)
			{
				return;
			}

			processChestItems(list);
			waitForWidgetLoad = false;
		}
	}

	@Subscribe
	public void onWidgetLoaded(WidgetLoaded widgetLoaded)
	{
		System.out.println(String.format("%s: %s,", widgetLoaded, widgetLoaded.getGroupId()));

		if (widgetLoaded.getGroupId() == WidgetID.POH_COSTUME_CHEST_GROUP_ID) {
			final Widget list = client.getWidget(592, 2);
			if (list == null)
			{
				return;
			}

			Widget[] items = list.getChildren();
			if (items == null || items.length == 0)
			{
				waitForWidgetLoad = true;
				return;
			}

			processChestItems(list);
		}
	}

	/*private void menuOpen(ScriptEvent e)
	{
		openSearch();
		client.playSoundEffect(SoundEffectID.UI_BOOP);
	}

	private void menuClose(ScriptEvent e)
	{
		updateFilter("");
		chatboxPanelManager.close();
		client.playSoundEffect(SoundEffectID.UI_BOOP);
	}

	private void openSearch()
	{
		updateFilter("");
		searchBtn.setAction(1, MENU_CLOSE);
		searchBtn.setOnOpListener((JavaScriptCallback) this::menuClose);
		searchInput = chatboxPanelManager.openTextInput("Filter fairy rings")
			.onChanged(s -> clientThread.invokeLater(() -> updateFilter(s)))
			.onClose(() ->
			{
				clientThread.invokeLater(() -> updateFilter(""));
				searchBtn.setOnOpListener((JavaScriptCallback) this::menuOpen);
				searchBtn.setAction(1, MENU_OPEN);
			})
			.build();
	}

	@Subscribe
	public void onGameTick(GameTick t)
	{
		// This has to happen because the only widget that gets hidden is the tli one
		Widget fairyRingTeleportButton = client.getWidget(WidgetInfo.FAIRY_RING_TELEPORT_BUTTON);
		boolean fairyRingWidgetOpen = fairyRingTeleportButton != null && !fairyRingTeleportButton.isHidden();
		boolean chatboxOpen = searchInput != null && chatboxPanelManager.getCurrentInput() == searchInput;

		if (!fairyRingWidgetOpen && chatboxOpen)
		{
			chatboxPanelManager.close();
		}
	}*/

	/*private void updateFilter(String filter)
	{
		filter = filter.toLowerCase();
		final Widget list = client.getWidget(WidgetInfo.FAIRY_RING_LIST);
		final Widget favorites = client.getWidget(WidgetInfo.FAIRY_RING_FAVORITES);

		if (list == null)
		{
			return;
		}

		if (codes != null)
		{
			// Check to make sure the list hasn't been rebuild since we were last her
			// Do this by making sure the list's dynamic children are the same as when we last saw them
			if (codes.stream().noneMatch(w ->
			{
				Widget codeWidget = w.getCode();
				if (codeWidget == null)
				{
					return false;
				}
				return list.getChild(codeWidget.getIndex()) == codeWidget;
			}))
			{
				codes = null;
			}
		}

		if (codes == null)
		{
			// Find all of the widgets that we care about, grouping by their Y value
			Map<Integer, CodeWidgets> codeMap = new TreeMap<>();

			for (Widget w : list.getStaticChildren())
			{
				if (w.isSelfHidden())
				{
					continue;
				}

				if (w.getSpriteId() != -1)
				{
					codeMap.computeIfAbsent(w.getRelativeY(), k -> new CodeWidgets()).setFavorite(w);
				}
				else if (!Strings.isNullOrEmpty(w.getText()))
				{
					codeMap.computeIfAbsent(w.getRelativeY(), k -> new CodeWidgets()).setDescription(w);
				}
			}

			for (Widget w : list.getDynamicChildren())
			{
				if (w.isSelfHidden())
				{
					continue;
				}

				CodeWidgets c = codeMap.computeIfAbsent(w.getRelativeY(), k -> new CodeWidgets());
				c.setCode(w);
			}

			codes = codeMap.values();
		}

		// Relayout the panel
		int y = 0;

		if (favorites != null)
		{
			boolean hide = !filter.isEmpty();
			favorites.setHidden(hide);
			if (!hide)
			{
				y += favorites.getOriginalHeight() + ENTRY_PADDING;
			}
		}

		for (CodeWidgets c : codes)
		{
			String code = Text.removeTags(c.getDescription().getName()).replaceAll(" ", "");
			String tags = null;

			if (!code.isEmpty())
			{
				try
				{
					FairyRings ring = FairyRings.valueOf(code);
					tags = ring.getTags();
				}
				catch (IllegalArgumentException e)
				{
					log.warn("Unable to find ring with code '{}'", code, e);
				}
			}

			boolean hidden = !(filter.isEmpty()
				|| Text.removeTags(c.getDescription().getText()).toLowerCase().contains(filter)
				|| code.toLowerCase().contains(filter)
				|| tags != null && tags.contains(filter));

			if (c.getCode() != null)
			{
				c.getCode().setHidden(hidden);
				c.getCode().setOriginalY(y);
			}

			if (c.getFavorite() != null)
			{
				c.getFavorite().setHidden(hidden);
				c.getFavorite().setOriginalY(y);
			}

			c.getDescription().setHidden(hidden);
			c.getDescription().setOriginalY(y);

			if (!hidden)
			{
				y += c.getDescription().getHeight() + ENTRY_PADDING;
			}
		}

		y -= ENTRY_PADDING;

		if (y < 0)
		{
			y = 0;
		}

		int newHeight = 0;
		if (list.getScrollHeight() > 0)
		{
			newHeight = (list.getScrollY() * y) / list.getScrollHeight();
		}

		list.setScrollHeight(y);
		list.revalidateScroll();
		client.runScript(
			ScriptID.UPDATE_SCROLLBAR,
			WidgetInfo.FAIRY_RING_LIST_SCROLLBAR.getId(),
			WidgetInfo.FAIRY_RING_LIST.getId(),
			newHeight
		);
	}*/
}
