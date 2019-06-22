package BrickBreaker;

import java.awt.*;
import java.util.*;

public class Block
{
    private int x;
    private int y;
    private Color color;
    private int colorcode;
    private int ident;
    private int score;
    private boolean alive;
    private boolean anim;
    private boolean animon;
    private Timer delay;

    static final int DIMENSION_X = 30;
    static final int DIMENSION_Y = 15;
    static int blockcounter = 0;
    static int blockstohit = 0;

    public Block(int i, int j, int c, int id, int sc)
    {
        x = i;
        y = j;
        ident = id;
        colorcode = c;
        alive = true;
        anim = true;
        score = sc;
        animon = false;
        blockcounter++;
        blockstohit++;
        switch (colorcode)
        {
            case 0: color = Color.BLACK; break;
            case 1: color = Color.RED; break;
            case 2: color = Color.BLUE; break;
            case 3: color = Color.GREEN; break;
            case 4: color = Color.MAGENTA; break;
            case 5: color = Color.ORANGE; break;
            case 6: color = Color.YELLOW; break;
            case 7: color = Color.PINK; break;
            case 8: color = Color.CYAN; break;
            case 9: color = Color.LIGHT_GRAY; break;
            case 10: color = Color.GRAY; break;
            case 11: color = Color.DARK_GRAY; break;
            case 12: color = Color.WHITE; break;
        }
    }

    public void setPosition(int i, int j)
    {
        x = i;
        y = j;
    }

    public void draw(Graphics g)
    {
        if (alive)
        {
            g.setColor(Color.GRAY);
            g.fillRoundRect(x + 4, y + 4, DIMENSION_X, DIMENSION_Y, 7, 7);
            g.setColor(color);
            g.fillRoundRect(x, y, DIMENSION_X, DIMENSION_Y, 5, 5);
            g.setColor(Color.BLACK);
            g.drawRoundRect(x, y, DIMENSION_X, DIMENSION_Y, 5, 5);
        }
        else
        {
            if (!animon)
            {
                delay = new Timer();
                delay.schedule(new ClearScore(), 1000);
                animon = true;
            }
            if (anim)
            {
                g.setColor(Color.BLACK);
                g.setFont(new Font("Arial", Font.PLAIN, 10));
                g.drawString("" + score, x+5, y+11);
            }
        }
    }

    public void insertIntoMap(int[][] map)
    {
        for (int cx = this.getXMin(); cx <= this.getXMax(); cx++)
        {
            for (int cy = this.getYMin(); cy <= this.getYMax(); cy++)
                map[cx][cy] = this.getID();
        }
    }

    public void kill(int[][] map)
    {
        for (int cx = this.getXMin(); cx <= this.getXMax(); cx++)
        {
            for (int cy = this.getYMin(); cy <= this.getYMax(); cy++)
                map[cx][cy] = 0;
        }
        alive = false;
        blockstohit--;
    }

    public int getXMin() { return x; }
    public int getYMin() { return y; }
    public int getXMax() { return x + DIMENSION_X; }
    public int getYMax() { return y + DIMENSION_Y; }
    public int getID() { return ident; }
    public int getColor() { return colorcode; }
    public int getScore() { return score; }
    public boolean isAlive() { return alive; }

    class ClearScore extends TimerTask
    {
        @Override
        public void run()
        {
            anim = false;
        }
    }

}