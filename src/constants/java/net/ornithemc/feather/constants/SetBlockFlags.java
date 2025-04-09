package net.ornithemc.feather.constants;

public class SetBlockFlags {

	/**
	 * dispatch neighborChanged events to neighboring blocks
	 */
	public static final int NOTIFY_NEIGHBORS          = 0b0000001;
	/**
	 * notify WorldEventListeners of this block change
	 */
	public static final int NOTIFY_LISTENERS          = 0b0000010;
	/**
	 * do not cause the render chunk that contains this block to rebuild
	 */
	public static final int NO_REDRAW                 = 0b0000100;
	/**
	 * rebuild the render chunk that contains this block on the main thread
	 */
	public static final int REDRAW_ON_MAIN_THREAD     = 0b0001000;
	/**
	 * skip updating neighboring observer blocks
	 */
	public static final int SUPPRESS_OBSERVER_UPDATES = 0b0010000;
	/**
	 * skip updating shapes of neighboring blocks
	 */
	public static final int SUPPRESS_SHAPE_UPDATES    = 0b0010000;
	/**
	 * skip dropping items for the block that was replaced
	 */
	public static final int SUPPRESS_DROPS            = 0b0100000;
	/**
	 * notify that this block was moved by a piston
	 */
	public static final int MOVED_BY_PISTON           = 0b1000000;

}
