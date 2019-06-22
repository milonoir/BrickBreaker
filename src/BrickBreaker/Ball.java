package BrickBreaker;

import java.awt.*;

public class Ball
{
    final static int DIAMETER = 13;

    private int posX;
    private int posY;
    private int velX;
    private int velY;
    private int boundX;
    private int boundY;

    public Ball(int x, int y, int vx, int vy, int maxx, int maxy)
    {
        posX = x;
        posY = y;
        velX = vx;
        velY = vy;
        boundX = maxx - DIAMETER;
        boundY = maxy - DIAMETER;
    }

    public int getX() { return posX; }
    public int getY() { return posY; }
    public int getVelX() { return velX; }
    public int getVelY() { return velY; }

    public void setPosition(int x, int y)
    {
        posX = x;
        posY = y;
    }

    public void setVelX(int val)
    {
        velX = val;
    }

    public void setVelY(int val)
    {
        velY = val;
    }

    public void invertX()
    {
        velX = -velX;
    }

    public void invertY()
    {
        velY = -velY;
    }

    public void move()
    {
        posX += velX;
        posY += velY;

        if (posX < 5) // Keret miatt 5
        {
            posX = 5;
            invertX();
        }
        else if (posX > boundX)
        {
            posX = boundX;
            invertX();
        }
        if (posY < 55) // ScorePanel + keret miatt 55
        {
            posY = 55;
            invertY();
        }
        else if (posY > boundY)
        {
            posY = boundY;
            invertY();
        }
    }

    public void draw(Graphics g)
    {
        g.setColor(Color.GRAY);
        g.fillOval(posX + 4, posY + 4, DIAMETER, DIAMETER);
        g.setColor(Color.WHITE);
        g.fillOval(posX , posY, DIAMETER, DIAMETER);
        g.setColor(Color.BLACK);
        g.drawOval(posX , posY, DIAMETER, DIAMETER);
    }
}
