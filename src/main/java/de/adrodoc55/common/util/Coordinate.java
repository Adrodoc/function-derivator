package de.adrodoc55.common.util;

public class Coordinate {

	public static final Coordinate SELF = new Coordinate(0, 0);
	public static final Coordinate UP = new Coordinate(0, -1);
	public static final Coordinate DOWN = new Coordinate(0, 1);
	public static final Coordinate RIGHT = new Coordinate(1, 0);
	public static final Coordinate LEFT = new Coordinate(-1, 0);

	private int relativeX;
	private int relativeY;
	private Coordinate base;

	public Coordinate() {
		this(0, 0);
	}

	public Coordinate(int x, int y) {
		this(x, y, null);
	}

	public Coordinate(int x, int y, Coordinate base) {
		super();
		this.relativeX = x;
		this.relativeY = y;
		this.base = base;
	}

	public int getX() {
		if (base == null)
			return relativeX;
		return base.getX() + relativeX;
	}

	public int getRelativeX() {
		return relativeX;
	}

	public void setRelativeX(int relativeX) {
		this.relativeX = relativeX;
	}

	public void addX(int relativeX) {
		this.relativeX += relativeX;
	}

	public int getY() {
		if (base == null)
			return relativeY;
		return base.getY() + relativeY;
	}

	public int getRelativeY() {
		return relativeY;
	}

	public void setRelativeY(int relativeY) {
		this.relativeY = relativeY;
	}

	public void addY(int relativeY) {
		this.relativeY += relativeY;
	}

	public Coordinate getBase() {
		return base;
	}

	public void setBase(Coordinate base) {
		this.base = base;
	}

}
