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
package net.runelite.client.plugins.herbiboar;

import com.google.common.eventbus.Subscribe;
import net.runelite.client.events.ChatMessage;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.Overlay;

import java.awt.*;

public class Herbiboar extends Plugin {
    private final HerbiboarOverlay overlay = new HerbiboarOverlay(this);

    @Override
    protected void startUp() throws Exception  {    }
    @Override
    protected void shutDown() throws Exception  {    }
    @Override
    public Overlay getOverlay(){
        return overlay;
    }

    @Subscribe
    public void onChatMessage(ChatMessage event)
    {
        String message = event.getMessage();

        if(message == null)
            return;

        if(message.contains("Closer inspection reveals tracks leading away from you.")) {
            //Start
            overlay.newTrail();
        } else
        if(message.contains("Nothing seems to be out of place here.")) {
            //Failure to find
        } else
        if(message.contains("Something has passed this way.") || message.contains("Something has disturbed this seaweed recently, it smells.")) {
            //Find next
            overlay.nextTrailStep();
        } else
        if(message.contains("You stun the creature") || event.getMessage().contains("The creature has successfully"))
        {
            overlay.endTrail();
            System.out.println("ayyy");
            //color = new Color((int)(Math.random()*0x1000000));
        }
    }
}
