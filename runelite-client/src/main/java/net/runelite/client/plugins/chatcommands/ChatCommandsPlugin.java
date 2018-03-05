/*
 * Copyright (c) 2017. l2-
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
package net.runelite.client.plugins.chatcommands;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Provides;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.ItemComposition;
import net.runelite.api.MessageNode;
import net.runelite.client.chat.ChatColor;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.api.events.ConfigChanged;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.SetMessage;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.http.api.hiscore.HiscoreClient;
import net.runelite.http.api.hiscore.HiscoreSkill;
import net.runelite.http.api.hiscore.SingleHiscoreSkillResult;
import net.runelite.http.api.hiscore.Skill;
import net.runelite.http.api.item.Item;
import net.runelite.http.api.item.ItemPrice;
import net.runelite.http.api.item.SearchResult;

@PluginDescriptor(
	name = "Chat commands plugin"
)
@Slf4j
public class ChatCommandsPlugin extends Plugin
{
	private static final float HIGH_ALCHEMY_CONSTANT = 0.6f;

	private final HiscoreClient hiscoreClient = new HiscoreClient();

	@Inject
	Client client;

	@Inject
	ChatCommandsConfig config;

	@Inject
	ItemManager itemManager;

	@Inject
	ChatMessageManager chatMessageManager;

	@Inject
	ScheduledExecutorService executor;

	@Provides
	ChatCommandsConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ChatCommandsConfig.class);
	}

	@Subscribe
	public void onGameStateChange(GameStateChanged event)
	{
		if (event.getGameState().equals(GameState.LOGIN_SCREEN))
		{
			cacheConfiguredColors();
		}
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (event.getGroup().equals("chatcommands"))
		{
			cacheConfiguredColors();
			chatMessageManager.refreshAll();
		}
	}

	private void cacheConfiguredColors()
	{
		chatMessageManager
			.cacheColor(new ChatColor(ChatColorType.NORMAL, config.getPublicRecolor(), false),
				ChatMessageType.PUBLIC)
			.cacheColor(new ChatColor(ChatColorType.HIGHLIGHT, config.getPublicHRecolor(), false),
				ChatMessageType.PUBLIC)
			.cacheColor(new ChatColor(ChatColorType.NORMAL, config.getPrivateRecolor(), false),
				ChatMessageType.PRIVATE_MESSAGE_SENT, ChatMessageType.PRIVATE_MESSAGE_RECEIVED)
			.cacheColor(new ChatColor(ChatColorType.HIGHLIGHT, config.getPrivateHRecolor(), false),
				ChatMessageType.PRIVATE_MESSAGE_SENT, ChatMessageType.PRIVATE_MESSAGE_RECEIVED)
			.cacheColor(new ChatColor(ChatColorType.NORMAL, config.getCcRecolor(), false),
				ChatMessageType.CLANCHAT)
			.cacheColor(new ChatColor(ChatColorType.HIGHLIGHT, config.getCcHRecolor(), false),
				ChatMessageType.CLANCHAT)
			.cacheColor(new ChatColor(ChatColorType.NORMAL, config.getTransparentPublicRecolor(), true),
				ChatMessageType.PUBLIC)
			.cacheColor(new ChatColor(ChatColorType.HIGHLIGHT, config.getTransparentPublicHRecolor(), true),
				ChatMessageType.PUBLIC)
			.cacheColor(new ChatColor(ChatColorType.NORMAL, config.getTransparentPrivateRecolor(), true),
				ChatMessageType.PRIVATE_MESSAGE_SENT, ChatMessageType.PRIVATE_MESSAGE_RECEIVED)
			.cacheColor(new ChatColor(ChatColorType.HIGHLIGHT, config.getTransparentPrivateHRecolor(), true),
				ChatMessageType.PRIVATE_MESSAGE_SENT, ChatMessageType.PRIVATE_MESSAGE_RECEIVED)
			.cacheColor(new ChatColor(ChatColorType.NORMAL, config.getTransparentCcRecolor(), true),
				ChatMessageType.CLANCHAT)
			.cacheColor(new ChatColor(ChatColorType.HIGHLIGHT, config.getTransparentCcHRecolor(), true),
				ChatMessageType.CLANCHAT);
	}

	/**
	 * Checks if the chat message is a command.
	 *
	 * @param setMessage The chat message
	 */
	@Subscribe
	public void onSetMessage(SetMessage setMessage)
	{
		if (client.getGameState() != GameState.LOGGED_IN)
		{
			return;
		}

		switch (setMessage.getType())
		{
			case PUBLIC:
			case CLANCHAT:
			case PRIVATE_MESSAGE_RECEIVED:
			case PRIVATE_MESSAGE_SENT:
				break;
			default:
				return;
		}

		String message = setMessage.getValue();
		MessageNode messageNode = setMessage.getMessageNode();

		// clear runelite formatted messsage as the message node is
		// being reused
		messageNode.setRuneLiteFormatMessage(null);

		if (config.lvl() && message.toLowerCase().equals("!total"))
		{
			log.debug("Running total level lookup");
			executor.submit(() -> playerSkillLookup(setMessage.getType(), setMessage, "total"));
		}
		else if (config.price() && message.toLowerCase().startsWith("!price") && message.length() > 7)
		{
			String search = message.substring(7);

			log.debug("Running price lookup for {}", search);

			executor.submit(() -> itemPriceLookup(setMessage.getType(), setMessage.getMessageNode(), search));
		}
		else if (config.lvl() && message.toLowerCase().startsWith("!lvl") && message.length() > 5)
		{
			String search = message.substring(5);

			log.debug("Running level lookup for {}", search);
			executor.submit(() -> playerSkillLookup(setMessage.getType(), setMessage, search));
		}
	}

	/**
	 * Looks up the item price and changes the original message to the
	 * reponse.
	 *
	 * @param messageNode The chat message containing the command.
	 * @param search The item given with the command.
	 */
	private void itemPriceLookup(ChatMessageType type, MessageNode messageNode, String search)
	{
		SearchResult result;

		try
		{
			result = itemManager.searchForItem(search);
		}
		catch (ExecutionException ex)
		{
			log.warn("Unable to search for item {}", search, ex);
			return;
		}

		if (result != null && !result.getItems().isEmpty())
		{
			Item item = retrieveFromList(result.getItems(), search);
			if (item == null)
			{
				log.debug("Unable to find item {} in result {}", search, result);
				return;
			}

			int itemId = item.getId();
			ItemPrice itemPrice;

			try
			{
				itemPrice = itemManager.getItemPrice(itemId);
			}
			catch (IOException ex)
			{
				log.warn("Unable to fetch item price for {}", itemId, ex);
				return;
			}

			final ChatMessageBuilder builder = new ChatMessageBuilder()
				.append(ChatColorType.NORMAL)
				.append("Price of ")
				.append(ChatColorType.HIGHLIGHT)
				.append(item.getName())
				.append(ChatColorType.NORMAL)
				.append(": GE average ")
				.append(ChatColorType.HIGHLIGHT)
				.append(String.format("%,d", itemPrice.getPrice()));

			ItemComposition itemComposition = itemManager.getItemComposition(itemId);
			if (itemComposition != null)
			{
				int alchPrice = Math.round(itemComposition.getPrice() * HIGH_ALCHEMY_CONSTANT);
				builder
					.append(ChatColorType.NORMAL)
					.append(" HA value ")
					.append(ChatColorType.HIGHLIGHT)
					.append(String.format("%,d", alchPrice));
			}

			String response = builder.build();

			log.debug("Setting response {}", response);
			chatMessageManager.update(type, response, messageNode);
			client.refreshChat();
		}
	}

	/**
	 * Looks up the player skill and changes the original message to the
	 * reponse.
	 *
	 * @param setMessage The chat message containing the command.
	 * @param search The item given with the command.
	 */
	private void playerSkillLookup(ChatMessageType type, SetMessage setMessage, String search)
	{
		String player;
		if (type.equals(ChatMessageType.PRIVATE_MESSAGE_SENT))
		{
			player = client.getLocalPlayer().getName();
		}
		else
		{
			player = sanitize(setMessage.getName());
		}
		try
		{
			search = SkillAbbreviations.valueOf(search.toUpperCase()).getName();
		}
		catch (IllegalArgumentException i)
		{
		}

		HiscoreSkill skill;
		try
		{
			skill = HiscoreSkill.valueOf(search.toUpperCase());
		}
		catch (IllegalArgumentException i)
		{
			return;
		}

		try
		{
			SingleHiscoreSkillResult result = hiscoreClient.lookup(player, skill);
			Skill hiscoreSkill = result.getSkill();

			String response = new ChatMessageBuilder()
				.append(ChatColorType.NORMAL)
				.append("Level ")
				.append(ChatColorType.HIGHLIGHT)
				.append(skill.getName()).append(": ").append(String.valueOf(hiscoreSkill.getLevel()))
				.append(ChatColorType.NORMAL)
				.append(" Experience: ")
				.append(ChatColorType.HIGHLIGHT)
				.append(String.format("%,d", hiscoreSkill.getExperience()))
				.append(ChatColorType.NORMAL)
				.append(" Rank: ")
				.append(ChatColorType.HIGHLIGHT)
				.append(String.format("%,d", hiscoreSkill.getRank()))
				.build();

			log.debug("Setting response {}", response);
			chatMessageManager.update(type, response, setMessage.getMessageNode());
			client.refreshChat();
		}
		catch (IOException ex)
		{
			log.warn("unable to look up skill {} for {}", skill, search, ex);
		}
	}

	/**
	 * Compares the names of the items in the list with the original input.
	 * returns the item if its name is equal to the original input or null
	 * if it can't find the item.
	 *
	 * @param items List of items.
	 * @param originalInput String with the original input.
	 * @return Item which has a name equal to the original input.
	 */
	private Item retrieveFromList(List<Item> items, String originalInput)
	{
		for (Item item : items)
		{
			if (item.getName().toLowerCase().equals(originalInput.toLowerCase()))
			{
				return item;
			}
		}
		return null;
	}

	/**
	 * Cleans the playername string from ironman status icon if present and
	 * corrects spaces
	 *
	 * @param lookup Playername to lookup
	 * @return Cleaned playername
	 */
	private static String sanitize(String lookup)
	{
		String cleaned = lookup.contains("<img") ? lookup.substring(lookup.lastIndexOf('>') + 1) : lookup;
		return cleaned.replace('\u00A0', ' ');
	}
}
