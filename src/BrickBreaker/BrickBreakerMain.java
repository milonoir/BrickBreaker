package BrickBreaker;

import javax.swing.*;

public class BrickBreakerMain extends JApplet
{
/*    @Override
    public void init() { }

    @Override
    public void start() { }
    
    public void run()
    {
        add(new GamePanel());
    } */


    public BrickBreakerMain()
    {
        add(new GamePanel());
    }

    public static void main(String args[])
    {
        JFrame newWindow = new JFrame("Brick Breaker");
        newWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        newWindow.setResizable(false);
        newWindow.setContentPane(new GamePanel());
        newWindow.pack();
        newWindow.setVisible(true);
    } 
}