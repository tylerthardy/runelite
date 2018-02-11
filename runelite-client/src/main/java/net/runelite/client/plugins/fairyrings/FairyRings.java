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
package net.runelite.client.plugins.fairyrings;

import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Provides;
import net.runelite.api.Client;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.task.Schedule;
import net.runelite.client.ui.overlay.Overlay;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@PluginDescriptor(
		name = "Fairy rings plugin"
)
public class FairyRings extends Plugin
{

	public static final int SEPARATOR_PADDING = 4;
	public static final int ENTRY_PADDING = 3;

	@Inject
	private Client client;
	@Inject
	private FairyRingsConfig config;
	@Inject
	private FairyRingsOverlay overlay;

	@Override
	public void configure(Binder binder)
	{
		binder.bind(FairyRingsOverlay.class);
	}

	@Provides
	FairyRingsConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(FairyRingsConfig.class);
	}

	@Override
	public Overlay getOverlay()
	{
		return overlay;
	}

	@Schedule(
			period = 1,
			unit = ChronoUnit.MILLIS
	)
	public void checkList()
	{
		checkListVoid();
	}
	int lineNums = 0;

	private List<Widget> favAddWidgets = new ArrayList<Widget>();
	private List<Widget> destinationWidgets = new ArrayList<Widget>();
	private void checkListVoid()
	{
		if (!config.enabled())
		{
			return;
		}

		overlay.resetLines();

		if (client.getWidget(WidgetInfo.FAIRY_RING) == null)
		{
			overlay.addLine("No widget");
			favAddWidgets.clear();
			destinationWidgets.clear();
			return;
		}


		Widget list = client.getWidget(WidgetInfo.FAIRY_RING_LIST);

		//Store widgets
		if (destinationWidgets.isEmpty() || favAddWidgets.isEmpty())
		{

			overlay.addLine("List1 ----------");
			overlay.addLine("DynamicChilds:" + list.getDynamicChildren().length);
			overlay.addLine("StaticChilds:" + list.getStaticChildren().length);
			overlay.addLine("Lines:" + lineNums);
			overlay.addLine("----------------");

			for (Widget w :  list.getStaticChildren())
			{
				//overlay.addLine(w.getType()+":"+w.getName()+":"+w.getSpriteId()+":"+w.getText());
				if (w.getSpriteId() == 1341)
				{
					if (!w.isHidden())
						favAddWidgets.add(w);
				}
				else
				{
					String destination = w.getText().replace("<br>", "");
					String code = w.getName().replaceAll("<[^>]*>", "");
					if (destination.isEmpty() || code.isEmpty())
					{
						continue;
					}
					destinationWidgets.add(w);
				}
			}
		}
		overlay.addLine("Fav:" + favAddWidgets.size());
		overlay.addLine("Widg:" + destinationWidgets.size());

		int y = client.getWidget(WidgetInfo.FAIRY_RING_LIST_SEPARATOR).getRelativeY() + SEPARATOR_PADDING;
		for (int i = 0; i < destinationWidgets.size(); i++)
		{
			Widget dw = destinationWidgets.get(i);
			Widget fw = favAddWidgets.get(i);
			Widget cw = list.getDynamicChildren()[i];
			String destination = dw.getText().replace("<br>", "");
			String code = dw.getName().replaceAll("<[^>]*>", "");//35-79

			assert code.equals(cw.getText());

			if (destination.isEmpty() || code.isEmpty())
			{
				continue;
			}
			if (config.getFilter().isEmpty() || destination.toLowerCase().contains(config.getFilter().toLowerCase()))
			{
				cw.setHidden(false);
				dw.setHidden(false);
				fw.setHidden(false);

				cw.setRelativeY(y);
				dw.setRelativeY(y);
				fw.setRelativeY(y);

				overlay.addLine(String.format("%s/%s", code, destination));

				y += dw.getHeight() + ENTRY_PADDING;
			}
			else
			{
				cw.setHidden(true);
				dw.setHidden(true);
				fw.setHidden(true);
			}
		}

		client.getWidget(WidgetInfo.FAIRY_RING_LIST_SCROLLBAR).setHidden(y < list.getHeight());
		overlay.addLine(y + "/" + list.getHeight());

		lineNums = overlay.lineCount();
	}
}
