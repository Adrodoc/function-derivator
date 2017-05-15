package de.adrodoc55.math.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import de.adrodoc55.common.util.Coordinate;
import de.adrodoc55.math.term.Term;
import de.adrodoc55.math.term.Variable;

public class Plotter extends JComponent {

	private static final long serialVersionUID = 1L;
	private static final int MIN_SCALE = 15;
	private int scale = 30;
	private final List<Term> functions = new ArrayList<Term>();

	public Plotter() {
		addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				int zoom = e.getWheelRotation();
				setScale(scale + zoom * 5);
				repaint();
			}
		});
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		if (scale < MIN_SCALE) {
			this.scale = MIN_SCALE;
		} else {
			this.scale = scale;
		}
	}

	public List<Term> getFunctions() {
		return functions;
	}

	private Coordinate getCenter() {
		Coordinate center = new Coordinate(getWidth() / 2, getHeight() / 2);
		return center;
	}

	@Override
	public void paint(Graphics g) {
		Coordinate center = getCenter();
		g.drawLine(center.getX(), 0, center.getX(), getHeight());
		g.drawLine(0, center.getY(), getWidth(), center.getY());
		int thickness = 2;
		for (int x = center.getX(); x < getWidth(); x += scale) {
			g.drawLine(x, center.getY() + thickness, x, center.getY()
					- thickness);

			int coordinateX = (int) convertToCoordinateSystemX(x);
			char[] zahl = String.valueOf(coordinateX).toCharArray();
			drawChars(g, x, center.getY(), zahl);
		}
		for (int x = center.getX() - scale; x > 0; x -= scale) {
			g.drawLine(x, center.getY() + thickness, x, center.getY()
					- thickness);

			int coordinateX = (int) convertToCoordinateSystemX(x);
			char[] zahl = String.valueOf(coordinateX).toCharArray();
			drawChars(g, x, center.getY(), zahl);
		}
		for (int y = center.getY(); y < getHeight(); y += scale) {
			g.drawLine(center.getX() + thickness, y, center.getX() - thickness,
					y);

			int coordinateY = (int) convertToCoordinateSystemY(y);
			char[] zahl = String.valueOf(coordinateY).toCharArray();
			drawChars(g, center.getX(), y, zahl);
		}
		for (int y = center.getY(); y > 0; y -= scale) {
			g.drawLine(center.getX() + thickness, y, center.getX() - thickness,
					y);

			int coordinateY = (int) convertToCoordinateSystemY(y);
			char[] zahl = String.valueOf(coordinateY).toCharArray();
			drawChars(g, center.getX(), y, zahl);
		}
		for (Term function : functions) {
			paintFunction(g, function);
		}
		getBorder().paintBorder(this, g, 0, 0, getWidth(), getHeight());
	}

	private void drawChars(Graphics g, int x, int y, char[] zahl) {
		g.drawChars(zahl, 0, zahl.length, x + 2, y + 11);
	}

	private void paintFunction(Graphics g, Term function) {
		Coordinate lastCoordinate = null;
		for (int x = -1; x < getWidth(); x++) {
			// Berechnung
			double param = convertToCoordinateSystemX(x);
			function.setValue(new Variable("x"), param);
			double value = function.toDouble();
			if (Double.isFinite(value)) {
				int y = convertToActualY(value);
				// Zeichnen
				if (lastCoordinate == null) {
					lastCoordinate = new Coordinate(x, y);
				}
				g.drawLine(lastCoordinate.getX(), lastCoordinate.getY(), x, y);
				lastCoordinate = new Coordinate(x, y);
			} else {
				lastCoordinate = null;
			}
		}

	}

	private double convertToCoordinateSystemX(int actualX) {
		Coordinate center = getCenter();
		double x = ((double) (actualX - center.getX())) / (double) scale;
		return x;
	}

	private double convertToCoordinateSystemY(int actualY) {
		Coordinate center = getCenter();
		double y = ((double) (center.getY() - actualY)) / (double) scale;
		return y;
	}

	// private int convertToActualX(double x) {
	// Coordinate center = getCenter();
	// int actualX = (int) (x * scale) - center.getX();
	// return actualX;
	// }

	private int convertToActualY(double y) {
		Coordinate center = getCenter();
		int actualY = center.getY() - (int) (y * scale);
		return actualY;
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(scale * 10, scale * 10);
	}
}
