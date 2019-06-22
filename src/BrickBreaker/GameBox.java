package BrickBreaker;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.StringTokenizer;
import javax.swing.*;

public class GameBox extends JPanel
{
    static final int SIZE_X = 500;
    static final int SIZE_Y = 550;
    
    private int[][] gameField = new int[SIZE_X+10][SIZE_Y+10];
    ScorePanel newScorePanel = new ScorePanel();
    private Ball newBall = new Ball(245, 360, 0, 4, SIZE_X-5, SIZE_Y-5);
    private Paddle newPaddle = new Paddle(SIZE_X / 2 - 30, 60, SIZE_X-9, SIZE_Y);
    private Block[] newBlocks = new Block[200];
    private int interval = 15;
    private Timer newTimer;
    private boolean[] keys = new boolean[32767];
    private boolean pressed = false;
    private boolean animationrun = false;
    private boolean game = true;
    private int lasthit = 0;

    public GameBox()
    {
        
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        newTimer = new Timer(interval, new TimerAction());

        // Pályaadatok beolvasása a "level.dat"-ból
        try
        {
            FileReader fr = new FileReader("level.dat");
            LineNumberReader lr = new LineNumberReader(fr);
            
            for (int i = 0; i < 200; i++) // max. 200 blocksor kiolvasása
            {
                String line = lr.readLine();
                if (line==null) break;
                StringTokenizer st = new StringTokenizer(line);
                String s1 = st.nextToken(";");
                String s2 = st.nextToken(";");
                String s3 = st.nextToken(";");
                String s4 = st.nextToken(";");
                newBlocks[i] = new Block(Integer.parseInt(s1.trim()), Integer.parseInt(s2.trim()), Integer.parseInt(s3.trim()), 11+i, Integer.parseInt(s4.trim()));
                newBlocks[i].insertIntoMap(gameField);
            }
            fr.close();
        }
        catch (FileNotFoundException e) { System.out.print("File not found\n"); }
        catch (IOException e) { }

        setPreferredSize(new Dimension(SIZE_X, SIZE_Y));
        enableEvents(AWTEvent.KEY_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK);
        setFocusable(true);
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.drawRect(5, 55, SIZE_X - 11, SIZE_Y - 60);
        newScorePanel.draw(g);
        
        g.setFont(new Font("Arial", Font.ITALIC + Font.BOLD, 20));
        g.setColor(Color.BLACK);
        g.drawString("Programmed by Milán Boleradszki", 84, 419);
        g.setColor(Color.WHITE);
        g.drawString("Programmed by Milán Boleradszki", 86, 421);
        g.setColor(Color.GRAY);
        g.drawString("Programmed by Milán Boleradszki", 85, 420);

        if (game==false)
        {
            newTimer.stop();
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.setColor(Color.GRAY);
            g.drawString("GAME OVER", 144, 264);
            g.setColor(Color.red);
            g.drawString("GAME OVER", 140, 260);
        }
        else
        {
            for (int i=0; i<Block.blockcounter; i++)
                newBlocks[i].draw(g);
            newBall.draw(g);
            newPaddle.draw(g);
        }
    }

    @Override
    public void processEvent(AWTEvent e)
    {
        switch (e.getID())
        {
            case KeyEvent.KEY_PRESSED:
                pressed = true;
            case KeyEvent.KEY_RELEASED:
                keys[((KeyEvent) e).getKeyCode()] = pressed;
                pressed = false;
                break;
            // KeyEvent eseteket ki lehet törölni, ha nincs bill. használat
            case MouseEvent.MOUSE_CLICKED:
                if (animationrun)
                {
                    newTimer.stop();
                    animationrun = false;
                }
                else
                {
                    newTimer.start();
                    animationrun = true;
                }
                break;
            case MouseEvent.MOUSE_DRAGGED:
            case MouseEvent.MOUSE_MOVED:
                // Paddle régi pozíció törlés map-ből
                for (int a = 0; a <= SIZE_X; a++)
                {
                    gameField[a][newPaddle.getPosY()] = 0;
                    gameField[a][newPaddle.getPosY()+1] = 0;
                }
                // Paddle új pozíció mentése map-be (60 pixel hosszú paddle)
                newPaddle.insertIntoMap(gameField);
                
                newPaddle.setPosition(((MouseEvent) e).getX());
                break;
        }
        if (keys[KeyEvent.VK_SPACE])
        {
            if (animationrun)
            {
                newTimer.stop();
                animationrun = false;
            }
            else
            {
                newTimer.start();
                animationrun = true;
            }
        }
        this.repaint();
    }

    class TimerAction implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            // Ball szélei
            int BallX = newBall.getX();
            int BallXm = newBall.getX() + Ball.DIAMETER;
            int BallY = newBall.getY();
            int BallYm = newBall.getY() + Ball.DIAMETER;

            // Ball irányvektorai
            int VelX = newBall.getVelX();
            int VelY = newBall.getVelY();

            // Block azonosító
            int id1 = 0;
            int id2 = 0;

            // Paddle szélei
            int PaddleX = newPaddle.getPosX();
            int PaddleXm = newPaddle.getPosX() + newPaddle.getLength();
            int PaddleY = newPaddle.getPosY();

            // Block ütközések
            if ((VelX >= 0) && (VelY > 0)) // (+)(+) irány
            {
                if ( (BallXm < SIZE_X) && ( ((id1 = gameField[BallXm+VelX][BallY]) > 10) || ((id2 = gameField[BallXm+VelX][BallYm]) > 10) ) )
                {
                    if (id1 > 10)
                    {
                        newBlocks[id1-11].kill(gameField);
                        if (lasthit == newBlocks[id1-11].getColor())
                            newScorePanel.incBonus();
                        else
                            newScorePanel.resetBonus();
                        lasthit = newBlocks[id1-11].getColor();
                        newScorePanel.incScore(newBlocks[id1-11].getScore());
                    }
                    if ((id2 > 10) && (id2 != id1))
                    {
                        newBlocks[id2-11].kill(gameField);
                        if (lasthit == newBlocks[id2-11].getColor())
                            newScorePanel.incBonus();
                        else
                            newScorePanel.resetBonus();
                        lasthit = newBlocks[id2-11].getColor();
                        newScorePanel.incScore(newBlocks[id2-11].getScore());
                    }
                    newBall.invertX();
                }
                if ( (BallYm < SIZE_Y) && ( ((id1 = gameField[BallX][BallYm+VelY]) > 10) || ((id2 = gameField[BallXm][BallYm+VelY]) > 10) ) )
                {
                    if (id1 > 10)
                    {
                        newBlocks[id1-11].kill(gameField);
                        if (lasthit == newBlocks[id1-11].getColor())
                            newScorePanel.incBonus();
                        else
                            newScorePanel.resetBonus();
                        lasthit = newBlocks[id1-11].getColor();
                        newScorePanel.incScore(newBlocks[id1-11].getScore());
                    }
                    if ((id2 > 10) && (id2 != id1))
                    {
                        newBlocks[id2-11].kill(gameField);
                        if (lasthit == newBlocks[id2-11].getColor())
                            newScorePanel.incBonus();
                        else
                            newScorePanel.resetBonus();
                        lasthit = newBlocks[id2-11].getColor();
                        newScorePanel.incScore(newBlocks[id2-11].getScore());
                    }
                    newBall.invertY();
                }
            }
            else if ((VelX <= 0) && (VelY < 0)) // (-)(-) irány
            {
                if ( (BallX > 0) && ( ((id1 = gameField[BallX+VelX][BallY]) > 10) || ((id2 = gameField[BallX+VelX][BallYm]) > 10) ) )
                {
                    if (id1 > 10)
                    {
                        newBlocks[id1-11].kill(gameField);
                        if (lasthit == newBlocks[id1-11].getColor())
                            newScorePanel.incBonus();
                        else
                            newScorePanel.resetBonus();
                        lasthit = newBlocks[id1-11].getColor();
                        newScorePanel.incScore(newBlocks[id1-11].getScore());
                    }
                    if ((id2 > 10) && (id2 != id1))
                    {
                        newBlocks[id2-11].kill(gameField);
                        if (lasthit == newBlocks[id2-11].getColor())
                            newScorePanel.incBonus();
                        else
                            newScorePanel.resetBonus();
                        lasthit = newBlocks[id2-11].getColor();
                        newScorePanel.incScore(newBlocks[id2-11].getScore());
                    }
                    newBall.invertX();
                }
                if ( (BallY > 0) && ( ((id1 = gameField[BallX][BallY+VelY]) > 10) || ((id2 = gameField[BallXm][BallY+VelY]) > 10) ) )
                {
                    if (id1 > 10)
                    {
                        newBlocks[id1-11].kill(gameField);
                        if (lasthit == newBlocks[id1-11].getColor())
                            newScorePanel.incBonus();
                        else
                            newScorePanel.resetBonus();
                        lasthit = newBlocks[id1-11].getColor();
                        newScorePanel.incScore(newBlocks[id1-11].getScore());
                    }
                    if ((id2 > 10) && (id2 != id1))
                    {
                        newBlocks[id2-11].kill(gameField);
                        if (lasthit == newBlocks[id2-11].getColor())
                            newScorePanel.incBonus();
                        else
                            newScorePanel.resetBonus();
                        lasthit = newBlocks[id2-11].getColor();
                        newScorePanel.incScore(newBlocks[id2-11].getScore());
                    }
                    newBall.invertY();
                }
            }
            else if ((VelX > 0) && (VelY < 0)) // (+)(-) irány
            {
                if ( (BallXm < SIZE_X) && ( ((id1 = gameField[BallXm+VelX][BallY]) > 10) || ((id2 = gameField[BallXm+VelX][BallYm]) > 10) ) )
                {
                    if (id1 > 10)
                    {
                        newBlocks[id1-11].kill(gameField);
                        if (lasthit == newBlocks[id1-11].getColor())
                            newScorePanel.incBonus();
                        else
                            newScorePanel.resetBonus();
                        lasthit = newBlocks[id1-11].getColor();
                        newScorePanel.incScore(newBlocks[id1-11].getScore());
                    }
                    if ((id2 > 10) && (id2 != id1))
                    {
                        newBlocks[id2-11].kill(gameField);
                        if (lasthit == newBlocks[id2-11].getColor())
                            newScorePanel.incBonus();
                        else
                            newScorePanel.resetBonus();
                        lasthit = newBlocks[id2-11].getColor();
                        newScorePanel.incScore(newBlocks[id2-11].getScore());
                    }
                    newBall.invertX();
                }
                if ( (BallY > 0) && ( ((id1 = gameField[BallX][BallY+VelY]) > 10) || ((id2 = gameField[BallXm][BallY+VelY]) > 10) ) )
                {
                    if (id1 > 10)
                    {
                        newBlocks[id1-11].kill(gameField);
                        if (lasthit == newBlocks[id1-11].getColor())
                            newScorePanel.incBonus();
                        else
                            newScorePanel.resetBonus();
                        lasthit = newBlocks[id1-11].getColor();
                        newScorePanel.incScore(newBlocks[id1-11].getScore());
                    }
                    if ((id2 > 10) && (id2 != id1))
                    {
                        newBlocks[id2-11].kill(gameField);
                        if (lasthit == newBlocks[id2-11].getColor())
                            newScorePanel.incBonus();
                        else
                            newScorePanel.resetBonus();
                        lasthit = newBlocks[id2-11].getColor();
                        newScorePanel.incScore(newBlocks[id2-11].getScore());
                    }
                    newBall.invertY();
                }
            }
            else if ((VelX < 0) && (VelY > 0)) // (-)(+) irány
            {
                if ( (BallX > 0) && ( ((id1 = gameField[BallX+VelX][BallY]) > 10) || ((id2 = gameField[BallX+VelX][BallYm]) > 10) ) )
                {
                    if (id1 > 10)
                    {
                        newBlocks[id1-11].kill(gameField);
                        if (lasthit == newBlocks[id1-11].getColor())
                            newScorePanel.incBonus();
                        else
                            newScorePanel.resetBonus();
                        lasthit = newBlocks[id1-11].getColor();
                        newScorePanel.incScore(newBlocks[id1-11].getScore());
                    }
                    if ((id2 > 10) && (id2 != id1))
                    {
                        newBlocks[id2-11].kill(gameField);
                        if (lasthit == newBlocks[id2-11].getColor())
                            newScorePanel.incBonus();
                        else
                            newScorePanel.resetBonus();
                        lasthit = newBlocks[id2-11].getColor();
                        newScorePanel.incScore(newBlocks[id2-11].getScore());
                    }
                    newBall.invertX();
                }
                if ( (BallYm < SIZE_Y) && ( ((id1 = gameField[BallX][BallYm+VelY]) > 10) || ((id2 = gameField[BallXm][BallYm+VelY]) > 10) ) )
                {
                    if (id1 > 10)
                    {
                        newBlocks[id1-11].kill(gameField);
                        if (lasthit == newBlocks[id1-11].getColor())
                            newScorePanel.incBonus();
                        else
                            newScorePanel.resetBonus();
                        lasthit = newBlocks[id1-11].getColor();
                        newScorePanel.incScore(newBlocks[id1-11].getScore());
                    }
                    if ((id2 > 10) && (id2 != id1))
                    {
                        newBlocks[id2-11].kill(gameField);
                        if (lasthit == newBlocks[id2-11].getColor())
                            newScorePanel.incBonus();
                        else
                            newScorePanel.resetBonus();
                        lasthit = newBlocks[id2-11].getColor();
                        newScorePanel.incScore(newBlocks[id2-11].getScore());
                    }
                    newBall.invertY();
                }
            }

            // Paddle ütközés + Ball X vektor módosítás
            if ((BallYm >= PaddleY) && ((BallXm > PaddleX) && (BallX < PaddleXm)))
            {
                int mod = gameField[BallXm - (Ball.DIAMETER / 2)][PaddleY];
                if (mod==0) mod = gameField[BallX][PaddleY];
                if (mod==0) mod = gameField[BallXm][PaddleY];

                switch(mod)
                {
                    case 1:
                        newBall.setVelY(4);
                        newBall.setVelX(-mod);
                        break;
                    case 2:
                        newBall.setVelY(3);
                        newBall.setVelX(-mod);
                        break;
                    case 3:
                        newBall.setVelY(2);
                        newBall.setVelX(-mod);
                        break;
                    case 4:
                        newBall.setVelY(1);
                        newBall.setVelX(-mod);
                        break;
                    case 5:
                        newBall.setVelY(4);
                        newBall.setVelX(mod-4);
                        break;
                    case 6:
                        newBall.setVelY(3);
                        newBall.setVelX(mod-4);
                        break;
                    case 7:
                        newBall.setVelY(2);
                        newBall.setVelX(mod-4);
                        break;
                    case 8:
                        newBall.setVelY(1);
                        newBall.setVelX(mod-4);
                        break;
                }
                newBall.invertY();
            }

            // Pálya aljának ütközés (halál)
            if (BallYm >= SIZE_X+45)
            {
                newScorePanel.setBalls(newScorePanel.getBalls()-1);
                newTimer.stop();
                animationrun = false;
                newBall.setPosition(245, 360);
                newBall.setVelX(0);
                newBall.setVelY(4);
            }

            // Game Over
            if (newScorePanel.getBalls() <= 0)
            {
                game = false;
                return;
            }

            // Ball mozgás
            newBall.move();
            repaint();
        }
    }

    public class ScorePanel //extends JPanel
    {
        int score;
        int balls;
        int bonus;

        public ScorePanel()
        {
            score = 0;
            balls = 3;
            bonus = 1;
        }

        public void incScore(int points)
        {
            score += (points * bonus);
        }

        public void setBalls(int lives)
        {
            balls = lives;
        }

        public void incBonus()
        {
            bonus++;
        }

        public void resetBonus()
        {
            bonus = 1;
        }

        public void draw(Graphics g)
        {
            g.setColor(Color.BLACK);
            g.drawRect(5, 5, SIZE_X-11, 45);
            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.drawString("Score: " + score, 20, 35);
            g.drawString(bonus + "× Bonus", SIZE_X/2-35, 35);
            g.drawString("Balls: " + balls, SIZE_X-80, 35);
        }

        public int getScore() { return score; }
        public int getBalls() { return balls; }
    }

}
