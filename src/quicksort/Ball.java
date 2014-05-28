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

    Color color;
    int diameter;
    long delay;
    private int vx;
    private int vy;

    private int iDrection;

    public static final int DIRECTION_UP = 0;
    public static final int DIRECTION_DOWN = 1;
    public static final int DIRECTION_LEFT = 2;
    public static final int DIRECTION_RIGHT = 3;

    private JLabel mNumberLabel;

    Point startLocation;

    public Ball(String ballcolor, Point startLocation) {
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
        diameter = 30;
        delay = 100;

        this.startLocation = startLocation;

    }

    public void goLeft(int distance) {
        iDrection = DIRECTION_LEFT;
        new Thread(this).start();
    }

    public void goRight(int distance) {
        iDrection = DIRECTION_RIGHT;
        new Thread(this).start();
    }

    public void goUp(int distance) {
        iDrection = DIRECTION_UP;
        new Thread(this).start();
    }

    public void goDown(int distance) {
        iDrection = DIRECTION_DOWN;
        new Thread(this).start();
    }

    private void initLabel() {
        setLayout(new FlowLayout());
        mNumberLabel = new JLabel("User Name asdfasdfasdf", JLabel.LEFT);
        add(mNumberLabel);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int x = getX();
        int y = getY();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(color);
        g.fillOval(0, 0, 30, 30); //adds color to circle
        g.setColor(Color.black);
        g2.drawOval(0, 0, 30, 30); //draws circle

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(30, 30);
    }

    public void run() {

        try {
            // set start location
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    setLocation(startLocation.x, startLocation.y);
                }
            });
        } catch (InterruptedException exp) {
            exp.printStackTrace();
        } catch (InvocationTargetException exp) {
            exp.printStackTrace();
        }

        while (isVisible()) {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                System.out.println("interrupted");
            }

            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    @Override
                    public void run() {
                        switch (iDrection) {
                            case DIRECTION_DOWN:
                                moveDown();
                                break;
                            case DIRECTION_LEFT:
                                moveBackward(-1);
                                break;
                            case DIRECTION_RIGHT:
                                moveForward();
                                break;
                            case DIRECTION_UP:
                                moveUp();
                                break;
                            default:
                                move();
                                break;
                        }
                        repaint();
                    }
                });
            } catch (InterruptedException exp) {
                exp.printStackTrace();
            } catch (InvocationTargetException exp) {
                exp.printStackTrace();
            }
        }
    }

    public void move() {

        int x = getX();
        int y = getY();

        if (x + vx < 0 || x + diameter + vx > getParent().getWidth()) {
            vx *= -1;
        }
        if (y + vy < 0 || y + diameter + vy > getParent().getHeight()) {
            vy *= -1;
        }
        x += vx;
        y += vy;

        // Update the size and location...
        setSize(getPreferredSize());
        setLocation(x, y);
    }

    public void moveForward() {

        int x = getX();
        int y = getY();

        if (x + 1 < getParent().getWidth()) {
            x += 1;
        }

        // Update the size and location...
        setSize(getPreferredSize());
        setLocation(x, y);
    }

    public void moveBackward(int position) {
        int x = getX();
        int y = getY();
        if (x - 1 > 0) {
            x -= 1;
        }
        // Update the size and location...
        setSize(getPreferredSize());
        setLocation(x, y);
    }

    public void moveUp() {
        int x = getX();
        int y = getY();
        if (y - 1 > 0) {
            y -= 1;
        }
        // Update the size and location...
        setSize(getPreferredSize());
        setLocation(x, y);
    }

    public void moveDown() {
        int x = getX();
        int y = getY();
        if (y + 1 < getParent().getHeight()) {
            y += 1;
        }
        // Update the size and location...
        setSize(getPreferredSize());
        setLocation(x, y);
    }
}
