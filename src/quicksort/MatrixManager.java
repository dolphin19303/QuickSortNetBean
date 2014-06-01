package quicksort;

import java.awt.Point;
import java.util.ArrayList;

import sun.security.util.DisabledAlgorithmConstraints;

public class MatrixManager {
	private int iNumItem;
	private ArrayList<Point> ptPositions;
	private int iMaxWidth, iMaxHeight;
	private int iItemWidth;
	private int iDistance;

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

	public Point getPositionOfItem(int itemPosition) {
		if (itemPosition < ptPositions.size()) {
			return ptPositions.get(itemPosition);
		}
		return null;
	}

	public Point getPositionAboveOfItem(int position) {
		if (position < ptPositions.size()) {
			Point pt = new Point(ptPositions.get(position));
			pt.y -= (iDistance + iItemWidth);
			return pt;
		}
		return null;
	}

	public Point getPositionBelowOfItem(int position) {
		if (position < ptPositions.size()) {
			Point pt = new Point(ptPositions.get(position));
			pt.y += (iDistance + iItemWidth);
			return pt;
		}
		return null;
	}

	public Point getPositionTopLeftRectangleOf(int position) {
		if (position < ptPositions.size()) {
			Point pt = new Point(getPositionAboveOfItem(position));
			pt.x -= iDistance/2;
			pt.y -= iDistance/2;
			return pt;
		}
		return null;
	}

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
