package quicksort;

import java.awt.Point;
import java.util.ArrayList;

import sun.security.util.DisabledAlgorithmConstraints;

public class MatrixManager {
	// khởi tạo biến cho đối tượng tính tọa độ
	// số lượng phần tử bóng
	private int iNumItem;
	// vị trí tương ứng với số lượng phần tử bóng
	private ArrayList<Point> ptPositions;
	// chiều dài, rộng của màn hình mô phỏng
	private int iMaxWidth, iMaxHeight;
	// kích thước của mỗi quả bóng
	private int iItemWidth;
	// khoảng cách giữa 2 quả bóng
	private int iDistance;

	// constructor
	public MatrixManager(int numItem, int itemWidth, int distance,
			int maxWidth, int maxHeight) {
		this.iNumItem = numItem;
		ptPositions = new ArrayList<Point>();
		iMaxHeight = maxHeight;
		iMaxWidth = maxWidth;
		iItemWidth = itemWidth;
		iDistance = distance;
		calculatePosition();
	}

	// hàm khởi tạo tính vị trí
	private void calculatePosition() {
		int y = iMaxHeight / 2;
		int preFix = (iMaxWidth - (iNumItem * iItemWidth + iNumItem * iDistance)) / 2;
		for (int i = 0; i < iNumItem; i++) {
			Point mPoint = new Point();
			mPoint.x = preFix + i * iDistance + i * iItemWidth;
			mPoint.y = y;
			ptPositions.add(mPoint);
		}
	}

	// hàm lấy tọa độ hiện tại của quả bóng ở vị trí itemPosition
	public Point getPositionOfItem(int itemPosition) {
		if (itemPosition < ptPositions.size()) {
			return ptPositions.get(itemPosition);
		}
		return null;
	}

	// hàm lấy tọa độ bên trên của quả bóng ở vị trí itemPosition
	public Point getPositionAboveOfItem(int position) {
		if (position < ptPositions.size()) {
			Point pt = new Point(ptPositions.get(position));
			pt.y -= (iDistance + iItemWidth);
			return pt;
		}
		return null;
	}

	// hàm lấy tọa độ bên dưới của quả bóng ở vị trí itemPosition
	public Point getPositionBelowOfItem(int position) {
		if (position < ptPositions.size()) {
			Point pt = new Point(ptPositions.get(position));
			pt.y += (iDistance + iItemWidth);
			return pt;
		}
		return null;
	}

	// hàm lấy tọa độ bên trái phía trên của quả bóng ( phục vụ cho tọa độ vẽ
	// hình chữ nhật )
	public Point getPositionTopLeftRectangleOf(int position) {
		if (position < ptPositions.size()) {
			Point pt = new Point(getPositionAboveOfItem(position));
			pt.x -= iDistance / 2;
			pt.y -= iDistance / 2;
			return pt;
		}
		return null;
	}

	// hàm lấy tọa độ bên phải phía dưới của quả bóng ( phục vụ cho tọa độ vẽ
	// hình chữ nhật )
	public Point getPositionBottomRightRectangleOf(int position) {
		if (position < ptPositions.size()) {
			Point pt = new Point(getPositionBelowOfItem(position));
			pt.x += iDistance / 2 + iItemWidth;
			pt.y += iDistance / 2 + iItemWidth;
			return pt;
		}
		return null;
	}
}
