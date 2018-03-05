/*
 * Copyright (c) 2018, Lotto <https://github.com/devLotto>
 * Copyright (c) 2018, Henke <https://github.com/henke96>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.plugins.puzzlesolver;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.plugins.puzzlesolver.solver.PuzzleSolver;
import net.runelite.client.plugins.puzzlesolver.solver.PuzzleState;
import net.runelite.client.plugins.puzzlesolver.solver.heuristics.ManhattanDistance;
import net.runelite.client.plugins.puzzlesolver.solver.pathfinding.IDAStar;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.ui.overlay.components.BackgroundComponent;
import net.runelite.client.ui.overlay.components.TextComponent;

@Slf4j
public class PuzzleSolverOverlay extends Overlay
{
	private static final int INFO_BOX_WIDTH = 100;
	private static final int INFO_BOX_OFFSET_Y = 50;
	private static final int INFO_BOX_TOP_BORDER = 2;
	private static final int INFO_BOX_BOTTOM_BORDER = 2;

	private static final int PUZZLE_TILE_SIZE = 39;

	private static final int BLANK_TILE_VALUE = -1;
	private static final int DIMENSION = 5;

	private final Client client;
	private final PuzzleSolverConfig config;
	private final ScheduledExecutorService executorService;

	private PuzzleSolver solver;
	private Future<?> solverFuture;
	private int[] cachedItems;

	private BufferedImage downArrow;
	private BufferedImage upArrow;
	private BufferedImage leftArrow;
	private BufferedImage rightArrow;

	@Inject
	public PuzzleSolverOverlay(Client client, PuzzleSolverConfig config, ScheduledExecutorService executorService)
	{
		setPosition(OverlayPosition.DYNAMIC);
		setPriority(OverlayPriority.HIGH);
		setLayer(OverlayLayer.ABOVE_WIDGETS);
		this.client = client;
		this.config = config;
		this.executorService = executorService;
	}

	@Override
	public Dimension render(Graphics2D graphics, Point parent)
	{
		if ((!config.displaySolution() && !config.displayRemainingMoves())
				|| client.getGameState() != GameState.LOGGED_IN)
		{
			return null;
		}

		ItemContainer container = client.getItemContainer(InventoryID.PUZZLE_BOX);

		if (container == null)
		{
			return null;
		}

		Widget puzzleBox = client.getWidget(WidgetInfo.PUZZLE_BOX);

		if (puzzleBox == null)
		{
			return null;
		}

		net.runelite.api.Point puzzleBoxLocation = puzzleBox.getCanvasLocation();

		String infoString = "Solving..";

		int[] itemIds = getItemIds(container);
		boolean shouldCache = false;

		if (solver != null)
		{
			if (solver.hasFailed())
			{
				infoString = "The puzzle could not be solved";
			}
			else
			{
				if (solver.hasSolution())
				{
					boolean foundPosition = false;

					// Find the current state by looking at the current step and then the next 3 steps
					for (int i = 0; i < 4; i++)
					{
						int j = solver.getPosition() + i;

						if (j == solver.getStepCount())
						{
							break;
						}

						Integer currentState = solver.getStep(j);

						// If this is false, player has moved the empty tile
						if (currentState != null && itemIds[currentState] == BLANK_TILE_VALUE)
						{
							foundPosition = true;
							solver.setPosition(j);
							if (i > 0)
							{
								shouldCache = true;
							}
							break;
						}
					}

					// If looking at the next steps didn't find the current state,
					// see if we can find the current state in the 3 previous steps
					if (!foundPosition)
					{
						for (int i = 1; i < 4; i++)
						{
							int j = solver.getPosition() - i;

							if (j < 0)
							{
								break;
							}

							Integer currentState = solver.getStep(j);

							if (currentState != null && itemIds[currentState] == BLANK_TILE_VALUE)
							{
								foundPosition = true;
								shouldCache = true;
								solver.setPosition(j);
								break;
							}
						}
					}

					if (foundPosition)
					{
						int stepsLeft = solver.getStepCount() - solver.getPosition() - 1;

						if (stepsLeft == 0)
						{
							infoString = "Solved!";
						}
						else if (config.displayRemainingMoves())
						{
							infoString = "Moves left: " + stepsLeft;
						}
						else
						{
							infoString = null;
						}

						if (config.displaySolution())
						{
							// Find the current blank tile position
							Integer currentMove = solver.getStep(solver.getPosition());

							int lastBlankX = currentMove % DIMENSION;
							int lastBlankY = currentMove / DIMENSION;

							// Display the next 3 steps
							for (int j = 1; j < 4; j++)
							{
								Integer futureMove = solver.getStep(solver.getPosition() + j);

								if (futureMove == null)
								{
									break;
								}

								int blankX = futureMove % DIMENSION;
								int blankY = futureMove / DIMENSION;

								int xDelta = blankX - lastBlankX;
								int yDelta = blankY - lastBlankY;

								BufferedImage arrow;
								if (xDelta > 0)
								{
									arrow = getRightArrow();
								}
								else if (xDelta < 0)
								{
									arrow = getLeftArrow();
								}
								else if (yDelta > 0)
								{
									arrow = getDownArrow();
								}
								else
								{
									arrow = getUpArrow();
								}

								int x = puzzleBoxLocation.getX() + blankX * PUZZLE_TILE_SIZE
										+ PUZZLE_TILE_SIZE / 2 - arrow.getWidth() / 2;

								int y = puzzleBoxLocation.getY() + blankY * PUZZLE_TILE_SIZE
										+ PUZZLE_TILE_SIZE / 2 - arrow.getHeight() / 2;

								OverlayUtil.renderImageLocation(graphics, new net.runelite.api.Point(x, y), arrow);

								lastBlankX = blankX;
								lastBlankY = blankY;
							}
						}
					}
				}
			}
		}

		// Draw info box
		if (infoString != null)
		{
			int x = puzzleBoxLocation.getX() + puzzleBox.getWidth() / 2 - INFO_BOX_WIDTH / 2;
			int y = puzzleBoxLocation.getY() - INFO_BOX_OFFSET_Y;

			FontMetrics fm = graphics.getFontMetrics();
			int height = INFO_BOX_TOP_BORDER + fm.getHeight() + INFO_BOX_BOTTOM_BORDER;

			BackgroundComponent backgroundComponent = new BackgroundComponent();
			backgroundComponent.setRectangle(new Rectangle(x, y, INFO_BOX_WIDTH, height));
			backgroundComponent.render(graphics, parent);

			int textOffsetX = (INFO_BOX_WIDTH - fm.stringWidth(infoString)) / 2;
			int textOffsetY = fm.getHeight();

			TextComponent textComponent = new TextComponent();
			textComponent.setPosition(new Point(x + textOffsetX, y + textOffsetY));
			textComponent.setText(infoString);
			textComponent.render(graphics, parent);
		}

		// Solve the puzzle if we don't have an up to date solution
		if (solver == null || cachedItems == null || (!shouldCache && !Arrays.equals(cachedItems, itemIds)))
		{
			solve(itemIds);
			shouldCache = true;
		}

		if (shouldCache)
		{
			cacheItems(itemIds);
		}

		return null;
	}

	private int[] getItemIds(ItemContainer container)
	{
		int[] itemIds = new int[DIMENSION * DIMENSION];

		Item[] items = container.getItems();

		for (int i = 0; i < items.length; i++)
		{
			itemIds[i] = items[i].getId();
		}

		// If blank is in the last position, items doesn't contain it, so let's add it manually
		if (itemIds.length > items.length)
		{
			itemIds[items.length] = BLANK_TILE_VALUE;
		}

		return itemIds;
	}

	private void cacheItems(int[] items)
	{
		cachedItems = new int[items.length];
		System.arraycopy(items, 0, cachedItems, 0, cachedItems.length);
	}

	private void solve(int[] items)
	{
		if (solverFuture != null)
		{
			solverFuture.cancel(true);
		}

		int[] puzzleItems = convertToSolverFormat(items);
		PuzzleState puzzleState = new PuzzleState(puzzleItems);

		solver = new PuzzleSolver(new IDAStar(new ManhattanDistance()), puzzleState);
		solverFuture = executorService.submit(solver);
	}

	/**
	 * This depends on there being no gaps in between item ids in puzzles.
	 */
	private int[] convertToSolverFormat(int[] items)
	{
		int lowestId = Integer.MAX_VALUE;

		int[] convertedItems = new int[items.length];

		for (int id : items)
		{
			if (id == BLANK_TILE_VALUE)
			{
				continue;
			}

			if (lowestId > id)
			{
				lowestId = id;
			}
		}

		for (int i = 0; i < items.length; i++)
		{
			if (items[i] != BLANK_TILE_VALUE)
			{
				convertedItems[i] = items[i] - lowestId;
			}
			else
			{
				convertedItems[i] = BLANK_TILE_VALUE;
			}
		}

		return convertedItems;
	}

	private BufferedImage getDownArrow()
	{
		if (downArrow == null)
		{
			try
			{
				InputStream in = PuzzleSolverOverlay.class.getResourceAsStream("arrow.png");
				downArrow = ImageIO.read(in);
			}
			catch (IOException e)
			{
				log.warn("Error loading image", e);
			}
		}
		return downArrow;
	}

	private BufferedImage getUpArrow()
	{
		if (upArrow == null)
		{
			upArrow = getRotatedImage(getDownArrow(), Math.PI);
		}
		return upArrow;
	}

	private BufferedImage getLeftArrow()
	{
		if (leftArrow == null)
		{
			leftArrow = getRotatedImage(getDownArrow(), Math.PI / 2);
		}
		return leftArrow;
	}

	private BufferedImage getRightArrow()
	{
		if (rightArrow == null)
		{
			rightArrow = getRotatedImage(getDownArrow(), 3 * Math.PI / 2);
		}
		return rightArrow;
	}

	private BufferedImage getRotatedImage(BufferedImage image, double theta)
	{
		AffineTransform transform = new AffineTransform();
		transform.rotate(theta, image.getWidth() / 2, image.getHeight() / 2);
		AffineTransformOp transformOp = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
		return transformOp.filter(image, null);
	}
}
