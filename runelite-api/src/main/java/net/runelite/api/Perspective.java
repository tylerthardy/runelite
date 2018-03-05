/*
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
package net.runelite.api;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import net.runelite.api.model.Jarvis;
import net.runelite.api.model.Triangle;
import net.runelite.api.model.Vertex;

public class Perspective
{
	private static final double UNIT = Math.PI / 1024d; // How much of the circle each unit of SINE/COSINE is

	public static final int LOCAL_COORD_BITS = 7;
	public static final int LOCAL_TILE_SIZE = 1 << LOCAL_COORD_BITS; // 128 - size of a tile in local coordinates

	public static final int[] SINE = new int[2048]; // sine angles for each of the 2048 units, * 65536 and stored as an int
	public static final int[] COSINE = new int[2048]; // cosine

	static
	{
		for (int i = 0; i < 2048; ++i)
		{
			SINE[i] = (int) (65536.0D * Math.sin((double) i * UNIT));
			COSINE[i] = (int) (65536.0D * Math.cos((double) i * UNIT));
		}
	}

	/**
	 * Translates two-dimensional ground coordinates within the 3D world to
	 * their corresponding coordinates on the game screen.
	 *
	 * @param client
	 * @param x ground coordinate on the x axis
	 * @param y ground coordinate on the y axis
	 * @param plane ground plane on the z axis
	 * @return a {@link Point} on screen corresponding to the position in
	 * 3D-space
	 */
	public static Point worldToCanvas(Client client, int x, int y, int plane)
	{
		return worldToCanvas(client, x, y, plane, 0);
	}

	/**
	 * Translates two-dimensional ground coordinates within the 3D world to
	 * their corresponding coordinates on the game screen.
	 *
	 * @param client
	 * @param x ground coordinate on the x axis
	 * @param y ground coordinate on the y axis
	 * @param plane ground plane on the z axis
	 * @param zOffset distance from ground on the z axis
	 * @return a {@link Point} on screen corresponding to the position in
	 * 3D-space
	 */
	public static Point worldToCanvas(Client client, int x, int y, int plane, int zOffset)
	{
		return worldToCanvas(client, x, y, plane, x, y, zOffset);
	}

	/**
	 * Translates two-dimensional ground coordinates within the 3D world to
	 * their corresponding coordinates on the game screen. Calculating heights
	 * based on the coordinates of the tile provided, rather than the world coordinates
	 *
	 * Using the position of each vertex, rather than the location of the object, to determine the
	 * height of each vertex causes the mesh to be vertically warped, based on the terrain below
	 *
	 * @param client
	 * @param x ground coordinate on the x axis
	 * @param y ground coordinate on the y axis
	 * @param plane ground plane on the z axis
	 * @param tileX the X coordinate of the tile the object is on
	 * @param tileY the Y coordinate of the tile the object is on
	 * @return a {@link Point} on screen corresponding to the position in
	 * 3D-space
	 */
	public static Point worldToCanvas(Client client, int x, int y, int plane, int tileX, int tileY)
	{
		return worldToCanvas(client, x, y, plane, tileX, tileY, 0);
	}

	/**
	 * Translates two-dimensional ground coordinates within the 3D world to
	 * their corresponding coordinates on the game screen. Calculating heights
	 * based on the coordinates of the tile provided, rather than the world coordinates
	 *
	 * Using the position of each vertex, rather than the location of the object, to determine the
	 * height of each vertex causes the mesh to be vertically warped, based on the terrain below
	 *
	 * @param client
	 * @param x ground coordinate on the x axis
	 * @param y ground coordinate on the y axis
	 * @param plane ground plane on the z axis
	 * @param tileX the X coordinate of the tile the object is on
	 * @param tileY the Y coordinate of the tile the object is on
	 * @param zOffset distance from ground on the z axis
	 * @return a {@link Point} on screen corresponding to the position in
	 * 3D-space
	 */
	public static Point worldToCanvas(Client client, int x, int y, int plane, int tileX, int tileY, int zOffset)
	{
		if (x >= 128 && y >= 128 && x <= 13056 && y <= 13056)
		{
			int z = getTileHeight(client, tileX, tileY, client.getPlane()) - plane;
			x -= client.getCameraX();
			y -= client.getCameraY();
			z -= client.getCameraZ();
			z -= zOffset;

			int cameraPitch = client.getCameraPitch();
			int cameraYaw = client.getCameraYaw();

			int pitchSin = SINE[cameraPitch];
			int pitchCos = COSINE[cameraPitch];
			int yawSin = SINE[cameraYaw];
			int yawCos = COSINE[cameraYaw];

			int var8 = yawCos * x + y * yawSin >> 16;
			y = yawCos * y - yawSin * x >> 16;
			x = var8;
			var8 = pitchCos * z - y * pitchSin >> 16;
			y = z * pitchSin + y * pitchCos >> 16;

			if (y >= 50)
			{
				int pointX = client.getViewportWidth() / 2 + x * client.getScale() / y;
				int pointY = client.getViewportHeight() / 2 + var8 * client.getScale() / y;
				return new Point(pointX, pointY);
			}
		}

		return null;

	}

	/**
	 * Translates two-dimensional ground coordinates within the 3D world to
	 * their corresponding coordinates on the Minimap.
	 *
	 * @param client
	 * @param x ground coordinate on the x axis
	 * @param y ground coordinate on the y axis
	 * @return a {@link Point} on screen corresponding to the position in
	 * 3D-space
	 */
	public static Point worldToMiniMap(Client client, int x, int y)
	{
		return worldToMiniMap(client, x, y, 6400);
	}

	/**
	 * Translates two-dimensional ground coordinates within the 3D world to
	 * their corresponding coordinates on the Minimap.
	 *
	 * @param client
	 * @param x ground coordinate on the x axis
	 * @param y ground coordinate on the y axis
	 * @param distance max distance from local player to minimap point
	 * @return a {@link Point} on screen corresponding to the position in
	 * 3D-space
	 */
	public static Point worldToMiniMap(Client client, int x, int y, int distance)
	{
		int angle = client.getMapAngle() & 0x7FF;

		Point localLocation = client.getLocalPlayer().getLocalLocation();
		x = x / 32 - localLocation.getX() / 32;
		y = y / 32 - localLocation.getY() / 32;

		int dist = x * x + y * y;
		if (dist < distance)
		{
			int sin = SINE[angle];
			int cos = COSINE[angle];

			int xx = y * sin + cos * x >> 16;
			int yy = sin * x - y * cos >> 16;

			int miniMapX = client.getCanvas().getWidth() - (!client.isResized() ? 208 : 167);

			x = (miniMapX + 167 / 2) + xx;
			y = (167 / 2 - 1) + yy;
			return new Point(x, y);
		}

		return new Point(-1, -1);
	}

	/**
	 * Calculates the above ground height of a tile point.
	 *
	 * @param client
	 * @param x the ground coordinate on the x axis
	 * @param y the ground coordinate on the y axis
	 * @param plane the client plane/ground level
	 * @return the offset from the ground of the tile
	 */
	public static int getTileHeight(Client client, int x, int y, int plane)
	{
		int var3 = x >> 7;
		int var4 = y >> 7;
		if (var3 >= 0 && var4 >= 0 && var3 <= 103 && var4 <= 103)
		{
			byte[][][] tileSettings = client.getTileSettings();
			int[][][] tileHeights = client.getTileHeights();

			int var5 = plane;
			if (plane < 3 && (tileSettings[1][var3][var4] & 2) == 2)
			{
				var5 = plane + 1;
			}

			int var6 = x & 127;
			int var7 = y & 127;
			int var8 = var6 * tileHeights[var5][var3 + 1][var4] + (128 - var6) * tileHeights[var5][var3][var4] >> 7;
			int var9 = tileHeights[var5][var3][var4 + 1] * (128 - var6) + var6 * tileHeights[var5][var3 + 1][var4 + 1] >> 7;
			return (128 - var7) * var8 + var7 * var9 >> 7;
		}

		return 0;
	}

	public static Point worldToLocal(Client client, Point point)
	{
		int baseX = client.getBaseX();
		int baseY = client.getBaseY();

		int x = (point.getX() - baseX) << LOCAL_COORD_BITS;
		int y = (point.getY() - baseY) << LOCAL_COORD_BITS;

		return new Point(x, y);
	}

	public static Point localToWorld(Client client, Point point)
	{
		int x = (point.getX() >>> LOCAL_COORD_BITS) + client.getBaseX();
		int y = (point.getY() >>> LOCAL_COORD_BITS) + client.getBaseY();
		return new Point(x, y);
	}

	public static Point regionToWorld(Client client, Point point)
	{
		int baseX = client.getBaseX();
		int baseY = client.getBaseY();
		int x = point.getX() + baseX;
		int y = point.getY() + baseY;
		return new Point(x, y);
	}

	public static Point regionToLocal(Client client, Point point)
	{
		int x = point.getX() << LOCAL_COORD_BITS;
		int y = point.getY() << LOCAL_COORD_BITS;
		return new Point(x, y);
	}

	/**
	 * Calculates a tile polygon from offset worldToScreen() points.
	 *
	 * @param client
	 * @param localLocation local location of the tile
	 * @return a {@link Polygon} on screen corresponding to the given
	 * localLocation.
	 */
	public static Polygon getCanvasTilePoly(Client client, Point localLocation)
	{
		return getCanvasTileAreaPoly(client, localLocation, 1);
	}

	/**
	 * Returns a polygon representing an area.
	 *
	 * @param client
	 * @param localLocation Center location of the AoE
	 * @param size size of the area. Ex. Lizardman Shaman AoE is a 3x3, so
	 * size = 3
	 * @return a polygon representing the tiles in the area
	 */
	public static Polygon getCanvasTileAreaPoly(Client client, Point localLocation, int size)
	{
		int plane = client.getPlane();
		int halfTile = LOCAL_TILE_SIZE / 2;

		// If the size is 5, we need to shift it up and left 2 units, then expand by 5 units to make a 5x5
		int aoeSize = size / 2;

		// Shift over one half tile as localLocation is the center point of the tile, and then shift the area size
		Point topLeft = new Point(localLocation.getX() - (aoeSize * LOCAL_TILE_SIZE) - halfTile,
			localLocation.getY() - (aoeSize * LOCAL_TILE_SIZE) - halfTile);
		// expand by size
		Point bottomRight = new Point(topLeft.getX() + size * LOCAL_TILE_SIZE,
			topLeft.getY() + size * LOCAL_TILE_SIZE);
		// Take the x of top left and the y of bottom right to create bottom left
		Point bottomLeft = new Point(topLeft.getX(), bottomRight.getY());
		// Similarly for top right
		Point topRight = new Point(bottomRight.getX(), topLeft.getY());

		Point p1 = worldToCanvas(client, topLeft.getX(), topLeft.getY(), plane);
		Point p2 = worldToCanvas(client, topRight.getX(), topRight.getY(), plane);
		Point p3 = worldToCanvas(client, bottomRight.getX(), bottomRight.getY(), plane);
		Point p4 = worldToCanvas(client, bottomLeft.getX(), bottomLeft.getY(), plane);

		if (p1 == null || p2 == null || p3 == null || p4 == null)
		{
			return null;
		}

		Polygon poly = new Polygon();
		poly.addPoint(p1.getX(), p1.getY());
		poly.addPoint(p2.getX(), p2.getY());
		poly.addPoint(p3.getX(), p3.getY());
		poly.addPoint(p4.getX(), p4.getY());

		return poly;
	}

	/**
	 * Calculates text position and centers depending on string length.
	 *
	 * @param client
	 * @param graphics
	 * @param localLocation local location of the tile
	 * @param text string for width measurement
	 * @param zOffset offset from ground plane
	 * @return a {@link Point} on screen corresponding to the given
	 * localLocation.
	 */
	public static Point getCanvasTextLocation(Client client, Graphics2D graphics, Point localLocation, String text, int zOffset)
	{
		int plane = client.getPlane();

		Point p = Perspective.worldToCanvas(client, localLocation.getX(), localLocation.getY(), plane, zOffset);

		if (p == null)
		{
			return null;
		}

		FontMetrics fm = graphics.getFontMetrics();
		Rectangle2D bounds = fm.getStringBounds(text, graphics);
		int xOffset = p.getX() - (int) (bounds.getWidth() / 2);

		return new Point(xOffset, p.getY());
	}

	/**
	 * Calculates image position and centers depending on image size.
	 *
	 * @param client
	 * @param graphics
	 * @param localLocation local location of the tile
	 * @param image image for size measurement
	 * @param zOffset offset from ground plane
	 * @return a {@link Point} on screen corresponding to the given
	 * localLocation.
	 */
	public static Point getCanvasImageLocation(Client client, Graphics2D graphics, Point localLocation, BufferedImage image, int zOffset)
	{
		int plane = client.getPlane();

		Point p = Perspective.worldToCanvas(client, localLocation.getX(), localLocation.getY(), plane, zOffset);

		if (p == null)
		{
			return null;
		}

		int xOffset = p.getX() - image.getWidth() / 2;
		int yOffset = p.getY() - image.getHeight() / 2;

		return new Point(xOffset, yOffset);
	}

	/**
	 * Calculates image position and centers depending on image size.
	 *
	 * @param client
	 * @param localLocation local location of the tile
	 * @param image image for size measurement
	 * @return a {@link Point} on screen corresponding to the given
	 * localLocation.
	 */
	public static Point getMiniMapImageLocation(Client client, Point localLocation, BufferedImage image)
	{
		Point p = Perspective.worldToMiniMap(client, localLocation.getX(), localLocation.getY());

		if (p == null)
		{
			return null;
		}

		int xOffset = p.getX() - image.getWidth() / 2;
		int yOffset = p.getY() - image.getHeight() / 2;

		return new Point(xOffset, yOffset);
	}

	/**
	 * Calculates sprite position and centers depending on sprite size.
	 *
	 * @param client
	 * @param graphics
	 * @param localLocation local location of the tile
	 * @param sprite SpritePixel for size measurement
	 * @param zOffset offset from ground plane
	 * @return a {@link Point} on screen corresponding to the given
	 * localLocation.
	 */
	public static Point getCanvasSpriteLocation(Client client, Graphics2D graphics, Point localLocation, SpritePixels sprite, int zOffset)
	{
		int plane = client.getPlane();

		Point p = Perspective.worldToCanvas(client, localLocation.getX(), localLocation.getY(), plane, zOffset);

		if (p == null)
		{
			return null;
		}

		int xOffset = p.getX() - sprite.getWidth() / 2;
		int yOffset = p.getY() - sprite.getHeight() / 2;

		return new Point(xOffset, yOffset);
	}

	/**
	 * You don't want this. Use {@link TileObject#getClickbox()} instead
	 *
	 * Get the on-screen clickable area of {@code model} as though it's for the object on the tile at
	 * 	({@code tileX}, {@code tileY}) and rotated to angle {@code orientation}
	 *
	 * @param client
	 * @param model the model to calculate a clickbox for
	 * @param orientation the orientation of the model (0-2048, where 0 is north)
	 * @param tileX the X coordinate of the tile that the object using the model is on
	 * @param tileY the Y coordinate of the tile that the object using the model is on
	 * @return the clickable area of {@code model}, rotated to angle {@code orientation}, at the height of tile ({@code tileX}, {@code tileY})
	 */
	public static Area getClickbox(Client client, Model model, int orientation, int tileX, int tileY)
	{
		if (model == null)
		{
			return null;
		}

		List<Triangle> triangles = model.getTriangles().stream()
			.map(triangle -> triangle.rotate(orientation))
			.collect(Collectors.toList());

		List<Vertex> vertices = model.getVertices().stream()
				.map(v -> v.rotate(orientation))
				.collect(Collectors.toList());

		Area clickBox = get2DGeometry(client, triangles, orientation, tileX, tileY);
		Area visibleAABB = getAABB(client, vertices, orientation, tileX, tileY);

		if (visibleAABB == null || clickBox == null)
		{
			return null;
		}

		clickBox.intersect(visibleAABB);
		return clickBox;
	}

	private static Area get2DGeometry(Client client, List<Triangle> triangles, int orientation, int tileX, int tileY)
	{
		int radius = 5;
		Area geometry = new Area();

		for (Triangle triangle : triangles)
		{
			Vertex _a = triangle.getA();
			Point a = worldToCanvas(client,
				tileX - _a.getX(),
				tileY - _a.getZ(),
				-_a.getY(), tileX, tileY);
			if (a == null)
			{
				continue;
			}

			Vertex _b = triangle.getB();
			Point b = worldToCanvas(client,
				tileX - _b.getX(),
				tileY - _b.getZ(),
				-_b.getY(), tileX, tileY);
			if (b == null)
			{
				continue;
			}

			Vertex _c = triangle.getC();
			Point c = worldToCanvas(client,
				tileX - _c.getX(),
				tileY - _c.getZ(),
				-_c.getY(), tileX, tileY);
			if (c == null)
			{
				continue;
			}

			int minX = Math.min(Math.min(a.getX(), b.getX()), c.getX());
			int minY = Math.min(Math.min(a.getY(), b.getY()), c.getY());

			// For some reason, this calculation is always 4 pixels short of the actual in-client one
			int maxX = Math.max(Math.max(a.getX(), b.getX()), c.getX()) + 4;
			int maxY = Math.max(Math.max(a.getY(), b.getY()), c.getY()) + 4;

			// ...and the rectangles in the fixed client are shifted 4 pixels right and down
			if (!client.isResized())
			{
				minX += 4;
				minY += 4;
				maxX += 4;
				maxY += 4;
			}

			Rectangle clickableRect = new Rectangle(
				minX - radius, minY - radius,
				maxX - minX + radius, maxY - minY + radius
			);
			geometry.add(new Area(clickableRect));
		}

		return geometry;
	}

	private static Area getAABB(Client client, List<Vertex> vertices, int orientation, int tileX, int tileY)
	{
		int maxX = 0;
		int minX = 0;
		int maxY = 0;
		int minY = 0;
		int maxZ = 0;
		int minZ = 0;

		for (Vertex vertex : vertices)
		{
			int x = vertex.getX();
			int y = vertex.getY();
			int z = vertex.getZ();

			if (x > maxX)
			{
				maxX = x;
			}
			if (x < minX)
			{
				minX = x;
			}

			if (y > maxY)
			{
				maxY = y;
			}
			if (y < minY)
			{
				minY = y;
			}

			if (z > maxZ)
			{
				maxZ = z;
			}
			if (z < minZ)
			{
				minZ = z;
			}
		}

		int centerX = (minX + maxX) / 2;
		int centerY = (minY + maxY) / 2;
		int centerZ = (minZ + maxZ) / 2;

		int extremeX = (maxX - minX + 1) / 2;
		int extremeY = (maxY - minY + 1) / 2;
		int extremeZ = (maxZ - minZ + 1) / 2;

		if (extremeX < 32)
		{
			extremeX = 32;
		}

		if (extremeZ < 32)
		{
			extremeZ = 32;
		}

		int x1 = tileX - (centerX - extremeX);
		int y1 = centerY - extremeY;
		int z1 = tileY - (centerZ - extremeZ);

		int x2 = tileX - (centerX + extremeX);
		int y2 = centerY + extremeY;
		int z2 = tileY - (centerZ + extremeZ);

		Point p1 = worldToCanvas(client, x1, z1, -y1, tileX, tileY);
		Point p2 = worldToCanvas(client, x1, z2, -y1, tileX, tileY);
		Point p3 = worldToCanvas(client, x2, z2, -y1, tileX, tileY);

		Point p4 = worldToCanvas(client, x2, z1, -y1, tileX, tileY);
		Point p5 = worldToCanvas(client, x1, z1, -y2, tileX, tileY);
		Point p6 = worldToCanvas(client, x1, z2, -y2, tileX, tileY);
		Point p7 = worldToCanvas(client, x2, z2, -y2, tileX, tileY);
		Point p8 = worldToCanvas(client, x2, z1, -y2, tileX, tileY);

		List<Point> points = new ArrayList<>(8);
		points.add(p1);
		points.add(p2);
		points.add(p3);
		points.add(p4);
		points.add(p5);
		points.add(p6);
		points.add(p7);
		points.add(p8);

		try
		{
			points = Jarvis.convexHull(points);
		}
		catch (NullPointerException e)
		{
			// No non-null screen points for this AABB e.g. for an way off-screen model
			return null;
		}

		if (points == null)
		{
			return null;
		}

		Polygon hull = new Polygon();
		for (Point p : points)
		{
			if (p != null)
			{
				hull.addPoint(p.getX(), p.getY());
			}
		}

		return new Area(hull);
	}

}
