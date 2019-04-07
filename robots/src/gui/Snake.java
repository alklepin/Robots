package gui;

import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class Snake {
    private ArrayList<SnakeBlock> snakeBlocks = new ArrayList<>();
    private final double maxVelocity = 0.2;

    public SnakeBlock Head;
    public SnakeBlock Tail;

    Snake(double x, double y)
    {
        snakeBlocks.add(new SnakeBlock(x, y));
        Head = snakeBlocks.get(0);
        Tail = Head;
    }

    public void onModelUpdateEvent(Double direction)
    {
        for(int i = 0; i < snakeBlocks.size(); i++)
        {
            double x = snakeBlocks.get(i).X_Position;
            double y = snakeBlocks.get(i).Y_Position;
            SnakeBlockPosition nextPosition = null;
            if (i == 0)
            {
                snakeBlocks.get(i).X_Position = x + maxVelocity * Math.cos(direction);
                snakeBlocks.get(i).Y_Position = y + maxVelocity * Math.sin(direction);
                snakeBlocks.get(i).Direction = direction;
            }
            else{
                if (snakeBlocks.get(i - 1).isActiveBlock())
                    snakeBlocks.get(i).addNextPosition(nextPosition);
            }
            if (snakeBlocks.get(i).isActiveBlock())
                nextPosition = snakeBlocks.get(i).getNextPosition();
        }
    }

    public void incrementSnake(){
        SnakeBlock tail = new SnakeBlock(snakeBlocks.get(0).X_Position, snakeBlocks.get(0).Y_Position);
        tail.Direction = snakeBlocks.get(0).Direction;
        snakeBlocks.add(tail);
        Tail = tail;
    }

    public void draw()
    {
        for(SnakeBlock snakeBlock: snakeBlocks){
            snakeBlock.draw();
        }
    }
}
