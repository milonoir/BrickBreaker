package BrickBreaker;

import java.awt.*;
import javax.swing.*;

public class Paddle extends JPanel
{
    private int position;
    private int length;
    private int maxX;
    private int posY;
    private int[] mask60 = { 4, 4, 4, 4, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1,
                             5, 5, 5, 5, 5, 5, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 8, 8, 8, 8 };

    public Paddle(int pos, int len, int boundX, int boundY)
    {
        position = pos;
        length = len;
        maxX = boundX-1 - length;
        posY = boundY - 30;
    }

    public void setPosition(int pos)
    {
        if (pos <= 7)
            position = 7;
        else if (pos >= maxX)
            position = maxX;
        else
            position = pos;
    }

    public int getPosY() { return posY; }
    public int getPosX() { return position; }
    public int getLength() { return length; }

    public void setLength(int len)
    {
        length = len;
    }
    
    public void insertIntoMap(int map[][])
    {
        for (int i = 0; i < mask60.length; i++)
        {
            map[position + i][posY] = mask60[i];
            map[position + i][posY + 1] = mask60[i];
        }
    }

    public void draw(Graphics g)
    {
        g.setColor(Color.GRAY);
        g.fillRect(position + 4, posY + 4, length, 10);
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(position, posY, length, 10);
        g.setColor(Color.BLACK);
        g.drawRect(position, posY, length, 10);
    }
}
