package gui;

import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class Snake {
    private ArrayList<SnakeBlock> snakeBlocks = new ArrayList<>();
    private final double maxVelocity = 0.2;

    public SnakeBlock Head;
    public SnakeBlock Tail;
    public boolean load;

    public Snake(double x, double y, boolean load)
    {
        this.load = load;
        snakeBlocks.add(new SnakeBlock(x, y, load));
        Head = snakeBlocks.get(0);
        Tail = Head;
    }

    public ArrayList<SnakeBlock> getSnakeBlocks() {
        return snakeBlocks;
    }

    public void onModelUpdateEvent(Double direction)
    {
        SnakeBlockPosition nextPosition = new SnakeBlockPosition();
        nextPosition.X = Head.X_Position + maxVelocity * Math.cos(direction);
        nextPosition.Y = Head.Y_Position + maxVelocity * Math.sin(direction);
        nextPosition.Direction = direction;
        for(int i = 0; i < snakeBlocks.size(); i++)
        {
            SnakeBlockPosition position = new SnakeBlockPosition();
            if (snakeBlocks.get(i).isActiveBlock()) {
                //System.out.println("SnakeBlock " + i + " Active, change 'nextPosition'");
                position = snakeBlocks.get(i).getLastPosition();
            }
            if (i == 0)
                snakeBlocks.get(i).addNextPosition(nextPosition);
            else {
                if (snakeBlocks.get(i - 1).isActiveBlock()) {
                   // System.out.println("SnakeBlock " + (i - 1) + " Active");
                    snakeBlocks.get(i).addNextPosition(nextPosition);
                }
            }
            nextPosition = position;
        }
    }

    public ImageView incrementSnake(){
        SnakeBlock tail = new SnakeBlock(snakeBlocks.get(0).X_Position, snakeBlocks.get(0).Y_Position, load);
        tail.Direction = snakeBlocks.get(0).Direction;
        snakeBlocks.add(tail);
        Tail = tail;
        return tail.Picture;
    }

    public void draw()
    {
        for(SnakeBlock snakeBlock: snakeBlocks){
            snakeBlock.draw();
        }
    }
}
