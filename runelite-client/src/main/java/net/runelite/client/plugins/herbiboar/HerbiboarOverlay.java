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

import java.awt.*;

import net.runelite.api.*;
import net.runelite.client.RuneLite;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

public class HerbiboarOverlay extends Overlay {
    private final Herbiboar plugin;
    private final Client client = RuneLite.getClient();

    private static final int REGION_SIZE = 104;
    private static final int MAX_DISTANCE = 2400;

    public HerbiboarOverlay(Herbiboar plugin)
    {
        super(OverlayPosition.DYNAMIC);
        this.plugin = plugin;
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        if (client.getGameState() != GameState.LOGGED_IN)
        {
            return null;
        }

        /*Font font = plugin.getFont();
        if (font != null)
        {
            graphics.setFont(font);
        }*/


        renderTileObjects(graphics);

        return null;
    }

    private void renderTileObjects(Graphics2D graphics)
    {
        Region region = client.getRegion();
        Tile[][][] tiles = region.getTiles();

        int z = client.getPlane();

        for (int x = 0; x < REGION_SIZE; ++x)
        {
            for (int y = 0; y < REGION_SIZE; ++y)
            {
                Tile tile = tiles[z][x][y];
                if (tile != null)
                {
                    Player player = client.getLocalPlayer();
                    if (player != null)
                    {
                        renderGroundObject(graphics, tile, player);
                    }
                }
            }
        }
    }
    private void renderGroundObject(Graphics2D graphics, Tile tile, Player player)
    {
        GroundObject groundObject = tile.getGroundObject();
        if (groundObject != null)
        {
            if (player.getLocalLocation().distanceTo(groundObject.getLocalLocation()) <= MAX_DISTANCE)
            {
                int id = groundObject.getId();
                if(id >= 31300 && id <= 31399){
                    Renderable renderable = groundObject.getRenderable();
                    if(renderable.getModelHeight() == 0)
                        OverlayUtil.renderTileOverlay(graphics, groundObject, "track", new Color(255,255,255));
                }
            }
        }
    }
}
