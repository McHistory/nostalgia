/*
 * This file is free for everyone to use under the Creative Commons Zero license.
 */

package net.minecraft.world.level.tile.meta;

/**
 * See {@link net.minecraft.world.level.tile.RailTile$RailState#updateConnections} and {@link net.minecraft.world.level.tile.RailTile$RailState#connectTo}
 */
public final class RailShape {
	public static final int NORTH_SOUTH = 0;
	public static final int EAST_WEST = 1;
	public static final int ASCENDING_EAST = 2;
	public static final int ASCENDING_WEST = 3;
	public static final int ASCENDING_NORTH = 4;
	public static final int ASCENDING_SOUTH = 5;
	public static final int SOUTH_EAST = 6;
	public static final int SOUTH_WEST = 7;
	public static final int NORTH_WEST = 8;
	public static final int NORTH_EAST = 9;
}
