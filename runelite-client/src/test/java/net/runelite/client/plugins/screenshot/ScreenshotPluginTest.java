/*
 * Copyright (c) 2018, Adam <Adam@sigterm.info>
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
package net.runelite.client.plugins.screenshot;

import com.google.inject.Guice;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import java.util.concurrent.ScheduledExecutorService;
import javax.inject.Inject;
import static net.runelite.api.ChatMessageType.SERVER;
import net.runelite.api.Client;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.Notifier;
import net.runelite.client.config.RuneLiteConfig;
import net.runelite.client.ui.ClientUI;
import net.runelite.client.ui.overlay.OverlayRenderer;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ScreenshotPluginTest
{
	private static final String CLUE_SCROLL = "<col=3300ff>You have completed 28 medium Treasure Trails</col>";
	private static final String BARROWS_CHEST = "Your Barrows chest count is <col=ff0000>310</col>";

	@Mock
	@Bind
	Client client;

	@Inject
	ScreenshotPlugin screenshotPlugin;

	@Mock
	@Bind
	ScreenshotConfig screenshotConfig;

	@Mock
	@Bind
	Notifier notifier;

	@Mock
	@Bind
	ClientUI clientUi;

	@Mock
	@Bind
	OverlayRenderer overlayRenderer;

	@Mock
	@Bind
	RuneLiteConfig config;

	@Mock
	@Bind
	ScheduledExecutorService service;

	@Before
	public void before()
	{
		Guice.createInjector(BoundFieldModule.of(this)).injectMembers(this);
	}

	@Test
	public void testcluescroll()
	{
		ChatMessage chatMessageEvent = new ChatMessage(SERVER, "Seth", CLUE_SCROLL, null);
		screenshotPlugin.onChatMessage(chatMessageEvent);

		assertEquals("medium", screenshotPlugin.getClueType());
		assertEquals(28, screenshotPlugin.getClueNumber());
	}

	@Test
	public void testbarrowschest()
	{
		ChatMessage chatMessageEvent = new ChatMessage(SERVER, "Seth", BARROWS_CHEST, null);
		screenshotPlugin.onChatMessage(chatMessageEvent);

		assertEquals(310, screenshotPlugin.getBarrowsNumber());
	}
}
