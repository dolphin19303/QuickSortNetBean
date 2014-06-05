/*
 * Copyright (c) 2010, Oracle. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * * Neither the name of Oracle nor the names of its contributors
 *   may be used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
package quicksort;

import java.util.Random;

import quicksort.Ball.MoveCallback;
import quicksort.QuickSort.QuickSortCallback;

public class Main extends javax.swing.JFrame {

	// code xử lý chính
	private int[] mNumberArray;

	// thời gian trễ cho mỗi lần di chuyển
	private int iDelayTime = 7000;

	// đối tượng quản lý tính địa điểm
	private MatrixManager mMatrixManager;

	// mảng các đối tượng bóng
	private Ball[] mBalls;

	// đối tượng quản lý giải thuật quick sort
	private QuickSort mQuickSort;

	// đối tượng vẽ hình chữ nhật
	private MRectangle rect;

	// biến cờ đánh dấu trạng thái luồng của 2 quả bóng
	private boolean statusOfThread1, statusOfThread2;

	// Tạo random giá trị
	private void onGenerateRandomClick() {
		int currentNumber = 0;
		if (txtNumberArray != null && txtNumberArray.getText().length() > 0) {
			currentNumber = Integer.parseInt(txtNumberArray.getText());
		}
		iDelayTime = currentNumber * 1000 + 4000;
		mNumberArray = new int[currentNumber];
		for (int i = 0; i < mNumberArray.length; i++) {
			mNumberArray[i] = getRandom();
		}
		lblArray.setText(convertToString(mNumberArray));
	}

	// hàm lấy giá trị random
	private int getRandom() {
		Random randomGenerator = new Random();
		return randomGenerator.nextInt(100);
	}

	// hàm chính, bắt đầu chương trình
	public Main() {
		initComponents();
	}

	// Hàm đổi từ mảng sang chuỗi để hiển thị lên label
	private String convertToString(int[] intArray) {
		String displayToString = "";

		for (int i = 0; i < intArray.length; i++) {
			displayToString += String.valueOf(intArray[i]);
			if (i < intArray.length - 1) {
				displayToString += "  -  ";
			}
		}
		return displayToString;
	}

	// hàm cập nhật giá trị mảng lên label
	private void updateCurrentArrayStatus() {
		if (txtCurrentArrayStatus != null && mNumberArray != null) {
			txtCurrentArrayStatus.setText(convertToString(mNumberArray));
		}
	}

	// Hàm bắt đầu chạy mô phỏng
	public void startEmulate() {
		mPanelEmulator.removeAll();
		txtCurrentArrayStatus.setText("Không có gì");
		txtStatusEmulator.setText("Không có gì");
		rect = new MRectangle();
		onGenerateRandomClick();
		mQuickSort = new QuickSort();
		mQuickSort.setListener(new QuickSortCallback() {

			@Override
			public void onSwap(int posA, int posB) {

				statusOfThread1 = false;
				statusOfThread2 = false;
				txtStatusEmulator.setText("Hoán đổi     "
						+ mBalls[posA].getNumber() + " và "
						+ mBalls[posB].getNumber());
				swapBall(posA, posB);

			}

			@Override
			public void onShowFence(int posA, int posB) {
				iDelayTime = (posB - posA) * 1000 + 2000;
				int x1 = (int) mMatrixManager.getPositionTopLeftRectangleOf(
						posA).getX();
				int y1 = (int) mMatrixManager.getPositionTopLeftRectangleOf(
						posA).getY();
				int x2 = (int) mMatrixManager
						.getPositionBottomRightRectangleOf(posB).getX();
				int y2 = (int) mMatrixManager
						.getPositionBottomRightRectangleOf(posB).getY();
				rect.addSquare(x1, y1, x2 - x1, y2 - y1);
			}
		});

		mPanelEmulator.setLayout(null);
		mMatrixManager = new MatrixManager(mNumberArray.length, 30, 10,
				mPanelEmulator.getWidth(), mPanelEmulator.getHeight() - 20);
		mBalls = new Ball[mNumberArray.length];
		for (int i = 0; i < mNumberArray.length; i++) {
			mBalls[i] = new Ball("red", mMatrixManager.getPositionOfItem(i),
					mNumberArray[i]);
			mPanelEmulator.add(mBalls[i]);
		}
		mPanelEmulator.add(rect);
		mQuickSort.sort(mNumberArray);

	}

	// hàm kiểm tra xem 2 quả bóng đã di chuyển và đổi chỗ xong chưa
	private boolean isSwapComplete() {
		if (statusOfThread1 && statusOfThread2) {
			statusOfThread1 = false;
			statusOfThread2 = false;
			return true;
		} else {
			return false;
		}
	}

	// hàm thông báo lại khi đã di chuyển xong
	protected void notifyToContinue(int pos1, int pos2) {
		if (isSwapComplete()) {
			updateCurrentArrayStatus();
			mBalls[pos1].setColor("red");
			mBalls[pos2].setColor("red");
			if (pos1 != pos2) {
				Ball tgBall = mBalls[pos1];
				mBalls[pos1] = mBalls[pos2];
				mBalls[pos2] = tgBall;
			}
			sleep(1000);
			mQuickSort.notifyToContinue();
		}
	}

	// hàm delay chương trình
	private void sleep(int time) {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}

	// hàm đổi chỗ 2 quả bóng b1 và b2
	private void swapBall(final int b1, final int b2) {
		mBalls[b1].setColor("blue");
		mBalls[b2].setColor("blue");
		sleep(1000);
		if (mBalls[b1].equals(mBalls[b2])) {
			mBalls[b1].moveTo(mMatrixManager.getPositionAboveOfItem(b1),
					mMatrixManager.getPositionAboveOfItem(b2),
					mMatrixManager.getPositionOfItem(b2), new MoveCallback() {

						@Override
						public void onMoveFailed() {

						}

						@Override
						public void onMoveComplete() {
							statusOfThread1 = true;
							statusOfThread2 = true;
							notifyToContinue(b1, b2);
						}
					});
		} else {
			mBalls[b1].moveTo(mMatrixManager.getPositionAboveOfItem(b1),
					mMatrixManager.getPositionAboveOfItem(b2),
					mMatrixManager.getPositionOfItem(b2), new MoveCallback() {

						@Override
						public void onMoveFailed() {

						}

						@Override
						public void onMoveComplete() {
							statusOfThread1 = true;
							notifyToContinue(b1, b2);
						}
					});

			mBalls[b2].moveTo(mMatrixManager.getPositionBelowOfItem(b2),
					mMatrixManager.getPositionBelowOfItem(b1),
					mMatrixManager.getPositionOfItem(b1), new MoveCallback() {

						@Override
						public void onMoveFailed() {

						}

						@Override
						public void onMoveComplete() {
							statusOfThread2 = true;
							notifyToContinue(b1, b2);
						}
					});
		}
		sleep(iDelayTime);
	}

	/**
	 * thiết lập giao diện người dùng
	 */
	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed"
	// <editor-fold defaultstate="collapsed"
	// <editor-fold defaultstate="collapsed"
	// <editor-fold defaultstate="collapsed"
	// desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		jPanel1 = new javax.swing.JPanel();
		jLabel1 = new javax.swing.JLabel();
		txtNumberArray = new javax.swing.JTextField();
		btnArrayNumber = new javax.swing.JButton();
		jPanel2 = new javax.swing.JPanel();
		lblDescribeArray = new javax.swing.JLabel();
		lblArray = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();
		txtCurrentArrayStatus = new javax.swing.JLabel();
		btnStart = new javax.swing.JButton();
		jLabel3 = new javax.swing.JLabel();
		txtStatusEmulator = new javax.swing.JLabel();
		mPanelEmulator = new javax.swing.JPanel();
		txtStatus = new javax.swing.JLabel();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("QuickSort");

		jPanel1.setBorder(javax.swing.BorderFactory
				.createTitledBorder("Tạo mảng"));

		jLabel1.setText("Nhập số phần tử:");

		txtNumberArray.setText("5");
		txtNumberArray.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				txtNumberArrayActionPerformed(evt);
			}
		});

		btnArrayNumber.setText("Tạo mảng");
		btnArrayNumber.setName("btnAddArrayNumber"); // NOI18N
		btnArrayNumber.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnArrayNumberActionPerformed(evt);
			}
		});

		org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(
				jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(
				org.jdesktop.layout.GroupLayout.LEADING).add(
				jPanel1Layout
						.createSequentialGroup()
						.addContainerGap()
						.add(jLabel1)
						.addPreferredGap(
								org.jdesktop.layout.LayoutStyle.RELATED)
						.add(txtNumberArray,
								org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
								69,
								org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(
								org.jdesktop.layout.LayoutStyle.RELATED)
						.add(btnArrayNumber)
						.addContainerGap(222, Short.MAX_VALUE)));
		jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(
				org.jdesktop.layout.GroupLayout.LEADING).add(
				jPanel1Layout
						.createParallelGroup(
								org.jdesktop.layout.GroupLayout.BASELINE)
						.add(jLabel1)
						.add(txtNumberArray,
								org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
								org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
								org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
						.add(btnArrayNumber)));

		jPanel2.setBorder(javax.swing.BorderFactory
				.createTitledBorder(" Mô phỏng "));

		lblDescribeArray.setText("Mảng tự sinh        :");

		lblArray.setText("không có gì");

		jLabel2.setText("Trạng thái hiện tại:");

		txtCurrentArrayStatus.setText("không có gì");

		btnStart.setText("Mô phỏng");
		btnStart.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnStartActionPerformed(evt);
			}
		});

		jLabel3.setText("Đang thực hiện    : ");

		txtStatusEmulator.setText("không có gì");

		org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(
				jPanel2);
		jPanel2.setLayout(jPanel2Layout);
		jPanel2Layout
				.setHorizontalGroup(jPanel2Layout
						.createParallelGroup(
								org.jdesktop.layout.GroupLayout.LEADING)
						.add(jPanel2Layout
								.createSequentialGroup()
								.add(jPanel2Layout
										.createParallelGroup(
												org.jdesktop.layout.GroupLayout.LEADING)
										.add(jPanel2Layout
												.createSequentialGroup()
												.addContainerGap()
												.add(jPanel2Layout
														.createParallelGroup(
																org.jdesktop.layout.GroupLayout.LEADING)
														.add(jLabel2)
														.add(lblDescribeArray)
														.add(jLabel3))
												.add(18, 18, 18)
												.add(jPanel2Layout
														.createParallelGroup(
																org.jdesktop.layout.GroupLayout.LEADING)
														.add(txtStatusEmulator)
														.add(lblArray)
														.add(txtCurrentArrayStatus)))
										.add(jPanel2Layout
												.createSequentialGroup()
												.add(175, 175, 175)
												.add(btnStart)))
								.addContainerGap(
										org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)));
		jPanel2Layout
				.setVerticalGroup(jPanel2Layout
						.createParallelGroup(
								org.jdesktop.layout.GroupLayout.LEADING)
						.add(jPanel2Layout
								.createSequentialGroup()
								.add(jPanel2Layout
										.createParallelGroup(
												org.jdesktop.layout.GroupLayout.BASELINE)
										.add(lblDescribeArray).add(lblArray))
								.addPreferredGap(
										org.jdesktop.layout.LayoutStyle.RELATED)
								.add(jPanel2Layout
										.createParallelGroup(
												org.jdesktop.layout.GroupLayout.BASELINE)
										.add(jLabel2)
										.add(txtCurrentArrayStatus))
								.addPreferredGap(
										org.jdesktop.layout.LayoutStyle.RELATED)
								.add(jPanel2Layout
										.createParallelGroup(
												org.jdesktop.layout.GroupLayout.BASELINE)
										.add(jLabel3).add(txtStatusEmulator))
								.addPreferredGap(
										org.jdesktop.layout.LayoutStyle.RELATED)
								.add(btnStart)));

		mPanelEmulator.setBorder(javax.swing.BorderFactory
				.createTitledBorder("Mô phỏng"));

		org.jdesktop.layout.GroupLayout mPanelEmulatorLayout = new org.jdesktop.layout.GroupLayout(
				mPanelEmulator);
		mPanelEmulator.setLayout(mPanelEmulatorLayout);
		mPanelEmulatorLayout.setHorizontalGroup(mPanelEmulatorLayout
				.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
				.add(mPanelEmulatorLayout.createSequentialGroup()
						.add(161, 161, 161).add(txtStatus)
						.addContainerGap(313, Short.MAX_VALUE)));
		mPanelEmulatorLayout.setVerticalGroup(mPanelEmulatorLayout
				.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
				.add(mPanelEmulatorLayout.createSequentialGroup()
						.add(txtStatus).add(0, 129, Short.MAX_VALUE)));

		org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
				.add(layout
						.createSequentialGroup()
						.addContainerGap()
						.add(layout
								.createParallelGroup(
										org.jdesktop.layout.GroupLayout.LEADING)
								.add(jPanel2,
										org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
										org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.add(jPanel1,
										org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
										org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.add(org.jdesktop.layout.GroupLayout.TRAILING,
										mPanelEmulator,
										org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
										org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)).addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(
				org.jdesktop.layout.GroupLayout.LEADING).add(
				layout.createSequentialGroup()
						.addContainerGap()
						.add(jPanel1,
								org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
								org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
								org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(
								org.jdesktop.layout.LayoutStyle.RELATED)
						.add(jPanel2,
								org.jdesktop.layout.GroupLayout.PREFERRED_SIZE,
								org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
								org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(
								org.jdesktop.layout.LayoutStyle.RELATED)
						.add(mPanelEmulator,
								org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
								org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE).addContainerGap()));

		jPanel1.getAccessibleContext().setAccessibleName("Nhập phần tử");
		jPanel1.getAccessibleContext().setAccessibleDescription("");

		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void txtNumberArrayActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtNumberArrayActionPerformed
		// TODO add your handling code here:
	}// GEN-LAST:event_txtNumberArrayActionPerformed

	private void btnArrayNumberActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnArrayNumberActionPerformed
		onGenerateRandomClick();
	}// GEN-LAST:event_btnArrayNumberActionPerformed

	private void btnStartActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnStartActionPerformed
		startEmulate();

	}// GEN-LAST:event_btnStartActionPerformed

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		/* Set the Nimbus look and feel */
		// <editor-fold defaultstate="collapsed"
		// desc=" Look and feel setting code (optional) ">
		/*
		 * If Nimbus (introduced in Java SE 6) is not available, stay with the
		 * default look and feel. For details see
		 * http://download.oracle.com/javase
		 * /tutorial/uiswing/lookandfeel/plaf.html
		 */
		try {
			javax.swing.UIManager.LookAndFeelInfo[] installedLookAndFeels = javax.swing.UIManager
					.getInstalledLookAndFeels();
			for (int idx = 0; idx < installedLookAndFeels.length; idx++) {
				if ("Nimbus".equals(installedLookAndFeels[idx].getName())) {
					javax.swing.UIManager
							.setLookAndFeel(installedLookAndFeels[idx]
									.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(Main.class.getName()).log(
					java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(Main.class.getName()).log(
					java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(Main.class.getName()).log(
					java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(Main.class.getName()).log(
					java.util.logging.Level.SEVERE, null, ex);
		}
		// </editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new Main().setVisible(true);
			}
		});
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton btnArrayNumber;
	private javax.swing.JButton btnStart;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JLabel lblArray;
	private javax.swing.JLabel lblDescribeArray;
	private javax.swing.JPanel mPanelEmulator;
	private javax.swing.JLabel txtCurrentArrayStatus;
	private javax.swing.JTextField txtNumberArray;
	private javax.swing.JLabel txtStatus;
	private javax.swing.JLabel txtStatusEmulator;
	// End of variables declaration//GEN-END:variables

}
