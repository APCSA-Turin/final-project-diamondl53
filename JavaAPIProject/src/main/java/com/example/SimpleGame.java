package com.example;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class SimpleGame extends JPanel implements ActionListener {
    private int[] position = {0, 100};     // [x, y]
    private int[] velocity = {3, -4};       // [vx, vy]
    private Image sprite;
    private Timer timer;
    
    public SimpleGame() {
        sprite = new ImageIcon("star.png").getImage();
        timer = new Timer(20, this); // update every 20 milliseconds
        timer.start();               // start the animation
    }

    // Paint the star
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLUE);
        g.fillOval(position[0], position[1], 30, 30); // Draw a blue ball

    }

    // Called automatically every 20ms by Timer
    @Override
    public void actionPerformed(ActionEvent e) {
        position[0] += velocity[0]; // x
        position[1] += velocity[1]; // y
    
        // Bounce off left/right walls
        if (position[0] < 0 || position[0] > getWidth() - 50) {
            velocity[0] *= -1;
        }
    
        // Bounce off top/bottom walls
        if (position[1] < 0 || position[1] > getHeight() - 50) {
            velocity[1] *= -1;
        }
        
        repaint(); // Refresh the screen
    }

    // Main method to show frame
    public static void main(String[] args) {
        JFrame frame = new JFrame("Moving Ball Game");
        SimpleGame game = new SimpleGame();

        frame.add(game);
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

