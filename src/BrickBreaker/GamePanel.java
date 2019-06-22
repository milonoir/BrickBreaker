package BrickBreaker;

import java.awt.*;
import javax.swing.*;

public class GamePanel extends JPanel
{
    public GamePanel()
    {
        GameBox newGameBox;
        newGameBox = new GameBox();
        this.setLayout(new BorderLayout());
        this.add(newGameBox, BorderLayout.CENTER);
    }
}
