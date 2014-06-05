/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package quicksort;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Ball extends JPanel implements Runnable {

	// khởi tạo các biến cho đối tượng Ball
	// màu
	private Color color;
	// thời gian trễ (càng ít thì càng tốn CPU & chạy nhanh)
	private long delay = 10;
	// giá trị số trên quả bóng
	private int number;
	// trạng thái di chuyển của bóng
	private boolean stop;
	// tốc độ di chuyển của bóng
	private int speed = 1;
	// kích thước của bóng
	private int ballSize = 30;

	// điểm di chuyển tới của quả bóng
	private Point ptDestinationPoint;
	// điểm bắt đầu quả quả bóng
	private Point startLocation;
	// các điểm trung gian trong khi di chuyển
	private Point path1, path2;

	// các chế độ di chuyển
	private int mode;
	public static final int MODE_MOVE_ABOVE = 0;
	public static final int MODE_NORMAL = 1;

	// Đối tượng label để hiển thị số trên quả bóng
	private JLabel mNumberLabel;

	// cờ đánh dấu thông báo 1 lần
	private boolean oneTimeNotify;

	// Callback thông báo sau khi di chuyển xong
	private MoveCallback mCallback;

	// luồng xử lý
	private Thread mThread;

	// constructor
	public Ball(String ballcolor, Point startLocation, int number) {
		setColor(ballcolor);
		this.number = number;
		this.startLocation = new Point(startLocation);

		setLayout(null);
		initLabel(number);
		setMLocation(startLocation.x, startLocation.y);
	}

	// hàm set màu cho bóng
	public void setColor(String ballcolor) {
		if (ballcolor == "red") {
			color = Color.red;
		} else if (ballcolor == "blue") {
			color = Color.blue;
		} else if (ballcolor == "black") {
			color = Color.black;
		} else if (ballcolor == "cyan") {
			color = Color.cyan;
		} else if (ballcolor == "darkGray") {
			color = Color.darkGray;
		} else if (ballcolor == "gray") {
			color = Color.gray;
		} else if (ballcolor == "green") {
			color = Color.green;
		} else if (ballcolor == "yellow") {
			color = Color.yellow;
		} else if (ballcolor == "lightGray") {
			color = Color.lightGray;
		} else if (ballcolor == "magenta") {
			color = Color.magenta;
		} else if (ballcolor == "orange") {
			color = Color.orange;
		} else if (ballcolor == "pink") {
			color = Color.pink;
		} else if (ballcolor == "white") {
			color = Color.white;
		}
		repaint();
	}

	// hàm lấy màu
	public Color getColor() {
		return color;
	}

	// hàm lấy thời gian trễ
	public long getDelay() {
		return delay;
	}

	// hàm lấy vị trí hiện tại
	public Point getStartLocation() {
		return startLocation;
	}

	// hàm lấy giá trị số của bóng
	public int getNumber() {
		return number;
	}

	// hàm gán tọa độ cho bóng
	public void setMLocation(int x, int y) {
		// setLocation(x, y);
		setBounds(x, y, ballSize + 1, ballSize + 1);
	}

	// hàm khởi tạo giá trị và hiển thị số cho bóng
	private void initLabel(int number) {
		setLayout(new FlowLayout());
		mNumberLabel = new JLabel(String.valueOf(number), JLabel.LEFT);
		add(mNumberLabel);
	}

	// hàm di chuyển bóng thông qua vị trí path1 -> path2 -> pt và thông báo lại
	// qua mCallback
	public void moveTo(Point path1, Point path2, Point pt,
			MoveCallback mCallback) {
		mode = MODE_MOVE_ABOVE;
		oneTimeNotify = false;
		stop = false;
		ptDestinationPoint = new Point(pt);
		this.path1 = new Point(path1);
		this.path2 = new Point(path2);
		this.mCallback = mCallback;
		mThread = new Thread(this);
		mThread.start();
	}

	// hàm vẽ bóng
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(color);
		g.fillOval(0, 0, ballSize, ballSize);
		g.setColor(Color.black);
		g2.drawOval(0, 0, ballSize, ballSize);

	}

	// trả về kích cỡ của bóng
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(ballSize, ballSize);
	}

	// Hàm khởi tạo luồng để di chuyển bóng
	public void run() {

		try {
			// set start location
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					setMLocation(startLocation.x, startLocation.y);
				}
			});
		} catch (InterruptedException exp) {
			assert false;
			exp.printStackTrace();
		} catch (InvocationTargetException exp) {
			exp.printStackTrace();
		}

		while (!stop) {
			try {
				Thread.sleep(delay);

				SwingUtilities.invokeAndWait(new Runnable() {
					@Override
					public void run() {
						switch (mode) {
						case MODE_MOVE_ABOVE:
							moveToMultiple();
							break;
						case MODE_NORMAL:
							moveTo(ptDestinationPoint);
							break;
						default:
							break;
						}
						repaint();
					}
				});
			} catch (InterruptedException exp) {
			} catch (InvocationTargetException exp) {
			}
		}
	}

	// hàm di chuyển bóng tới vị trí ptDestinationPoint
	private void moveTo(Point ptDestinationPoint) {

		int x = getX();
		int y = getY();
		if (ptDestinationPoint != null) {
			if (x != ptDestinationPoint.getX()
					|| y != ptDestinationPoint.getY()) {
				if (ptDestinationPoint.getX() > x) {
					if (ptDestinationPoint.getX() > x + speed) {
						x += speed;
					} else {
						x = (int) ptDestinationPoint.getX();
					}
				} else if (ptDestinationPoint.getX() < x) {
					if (ptDestinationPoint.getX() < x - speed) {
						x -= speed;
					} else {
						x = (int) ptDestinationPoint.getX();
					}
				}
				if (ptDestinationPoint.getY() > y) {
					if (ptDestinationPoint.getY() > y + speed) {
						y += speed;
					} else {
						y = (int) ptDestinationPoint.getY();
					}
				} else if (ptDestinationPoint.getY() < y) {
					if (ptDestinationPoint.getY() < y - speed) {
						y -= speed;
					} else {
						y = (int) ptDestinationPoint.getY();
					}
				}
			} else if (x == ptDestinationPoint.getX()
					&& y == ptDestinationPoint.getY()) {
				this.startLocation = new Point(ptDestinationPoint);
				stop = true;
				if (mThread != null)
					mThread.interrupt();
				if (mCallback != null && mode == MODE_NORMAL)
					mCallback.onMoveComplete();

			} else {
				if (mCallback != null && mode == MODE_NORMAL)
					mCallback.onMoveFailed();
			}
			setSize(getPreferredSize());
			setMLocation(x, y);
		}
	}

	// hàm di chuyển bóng tới nhiều vị trí. path1 -> path2 -> ptDestinationPoint
	private void moveToMultiple() {

		int x = getX();
		int y = getY();
		if (!(new Point(x, y).equals(path1)) && path1 != null) {
			moveTo(path1);
		} else {
			path1 = null;
			if (!(new Point(x, y).equals(path2)) && path2 != null
					&& path1 == null) {
				moveTo(path2);
			} else {
				path2 = null;
				if (!(new Point(x, y).equals(ptDestinationPoint))
						&& path1 == null && path2 == null) {
					moveTo(ptDestinationPoint);
				} else {
					this.startLocation = new Point(ptDestinationPoint);
					stop = true;
					if (!oneTimeNotify) {
						oneTimeNotify = true;
						mCallback.onMoveComplete();
					}
				}
			}
		}
	}

	// interface để callback
	public interface MoveCallback {
		public void onMoveComplete();

		public void onMoveFailed();
	}
}
