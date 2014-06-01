package quicksort;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class MRectangle extends JPanel {
	private Rectangle rect;
	int iWidth;
	int iHeight;
	int x, y;

	public void addSquare(int x, int y, int width, int height) {
		rect = new Rectangle(x, y, width, height);
		this.iWidth = width;
		this.iHeight = height;
		this.x = x;
		this.y = y;
		setSize(getPreferredSize());
		setBounds(x, y, iWidth + 10, iHeight + 1);
		repaint();
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(iWidth, iHeight);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.black);
		g.drawRect(0, 0, iWidth, iHeight);
	}

}