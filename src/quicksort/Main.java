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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main extends javax.swing.JFrame {

    //code xử lý chính
    public int[] mNumberArray;

    private void onGenerateRandomClick() {
        int currentNumber = 0;
        if (txtNumberArray != null && txtNumberArray.getText().length() > 0) {
            currentNumber = Integer.parseInt(txtNumberArray.getText());
        }

        mNumberArray = new int[currentNumber];
        for (int i = 0; i < mNumberArray.length; i++) {
            mNumberArray[i] = getRandom();
        }
        lblArray.setText(convertToString(mNumberArray));
    }

    public int getRandom() {
        Random randomGenerator = new Random();
        return randomGenerator.nextInt(100);
    }

    public void drawMovingOjbect() {
        new JFrame("Draw a red box") {
            Point pointStart = new Point(50, 50);
            Point pointEnd = new Point(200, 200);

            public void paint(Graphics g) {
                super.paint(g);
                if (pointStart != null) {
                    g.setColor(Color.RED);
                    g.drawRect(pointStart.x, pointStart.y, pointEnd.x, pointEnd.y);
                }
            }

            {
                setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                setSize(300, 300);
                setLocation(300, 300);
                setVisible(true);
                Thread t = new Thread(new Runnable() {
                    public void run() {
                        while (pointEnd.x > 0 && pointEnd.y > 0) {
                            pointEnd = new Point(--pointEnd.x, --pointEnd.y);
                            repaint();
                            try {
                                Thread.sleep(22);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        pointStart = null;
                        pointEnd = null;
                    }
                });
                t.setDaemon(true);
                t.start();
            }
        };
    }

    public Main() {
        initComponents();
        jPanel1.setVisible(false);
        jPanel2.setVisible(false);
        startEmulate();
//        drawMovingOjbect();

    }

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

    private void updateCurrentArrayStatus() {
        if (txtCurrentArrayStatus != null && mNumberArray != null) {
            txtCurrentArrayStatus.setText(convertToString(mNumberArray));
        }
    }

    public void AnimatedBalls() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException ex) {
                } catch (InstantiationException ex) {
                } catch (IllegalAccessException ex) {
                } catch (UnsupportedLookAndFeelException ex) {
                }

                JFrame frame = new JFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                frame.add(new Balls());
                frame.setSize(400, 400);
                frame.setVisible(true);
            }
        });
    }

    public class Balls extends JPanel {

        public Balls() {
            setLayout(null);
            // Randomize the speed and direction...
            add(new Ball("red", 10 - (int) Math.round((Math.random() * 20)), 10 - (int) Math.round((Math.random() * 20))));
            add(new Ball("blue", 10 - (int) Math.round((Math.random() * 20)), 10 - (int) Math.round((Math.random() * 20))));
        }
    }

    public void startEmulate() {
//        Quicksort mQuicksort = new Quicksort();
//        mQuicksort.sort(mNumberArray);
//        updateCurrentArrayStatus();
        add(new Ball("red", 10 - (int) Math.round((Math.random() * 20)), 10 - (int) Math.round((Math.random() * 20))));
        add(new Ball("blue", 10 - (int) Math.round((Math.random() * 20)), 10 - (int) Math.round((Math.random() * 20))));

    }

    public class Quicksort {

        private int[] numbers;
        private int number;

        public void sort(int[] values) {
            // check for empty or null array
            if (values == null || values.length == 0) {
                return;
            }
            this.numbers = values;
            number = values.length;
            quicksort(0, number - 1);
        }

        private void quicksort(int low, int high) {
            int i = low, j = high;
            // Get the pivot element from the middle of the list
            int pivot = numbers[low + (high - low) / 2];

            // Divide into two lists
            while (i <= j) {
                // If the current value from the left list is smaller then the pivot
                // element then get the next element from the left list
                while (numbers[i] < pivot) {
                    i++;
                }
                // If the current value from the right list is larger then the pivot
                // element then get the next element from the right list
                while (numbers[j] > pivot) {
                    j--;
                }

                // If we have found a values in the left list which is larger then
                // the pivot element and if we have found a value in the right list
                // which is smaller then the pivot element then we exchange the
                // values.
                // As we are done we can increase i and j
                if (i <= j) {
                    exchange(i, j);
                    i++;
                    j--;
                }
            }
            // Recursion
            if (low < j) {
                quicksort(low, j);
            }
            if (i < high) {
                quicksort(i, high);
            }
        }

        private void exchange(int i, int j) {
            int temp = numbers[i];
            numbers[i] = numbers[j];
            numbers[j] = temp;
        }
    }

    /**
     * thiết lập giao diện người dùng
     */
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("QuickSort");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Tạo mảng"));

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

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(txtNumberArray, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 69, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btnArrayNumber)
                .addContainerGap(208, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                .add(jLabel1)
                .add(txtNumberArray, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(btnArrayNumber))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(" Mô phỏng "));

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

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel2)
                            .add(lblDescribeArray))
                        .add(18, 18, 18)
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(lblArray)
                            .add(txtCurrentArrayStatus)))
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(189, 189, 189)
                        .add(btnStart)))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lblDescribeArray)
                    .add(lblArray))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(txtCurrentArrayStatus))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 67, Short.MAX_VALUE)
                .add(btnStart))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(6, 6, 6)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.getAccessibleContext().setAccessibleName("Nhập phần tử");
        jPanel1.getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtNumberArrayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumberArrayActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumberArrayActionPerformed

    private void btnArrayNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnArrayNumberActionPerformed
        onGenerateRandomClick();
    }//GEN-LAST:event_btnArrayNumberActionPerformed

    private void btnStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartActionPerformed
        startEmulate();

    }//GEN-LAST:event_btnStartActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            javax.swing.UIManager.LookAndFeelInfo[] installedLookAndFeels = javax.swing.UIManager.getInstalledLookAndFeels();
            for (int idx = 0; idx < installedLookAndFeels.length; idx++) {
                if ("Nimbus".equals(installedLookAndFeels[idx].getName())) {
                    javax.swing.UIManager.setLookAndFeel(installedLookAndFeels[idx].getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblArray;
    private javax.swing.JLabel lblDescribeArray;
    private javax.swing.JLabel txtCurrentArrayStatus;
    private javax.swing.JTextField txtNumberArray;
    // End of variables declaration//GEN-END:variables

}
