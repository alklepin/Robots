package gui;

import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Field {

    public static double Width = 1300;
    public static double Height = 850;

    private Set<FieldCell> badCells = new HashSet<>();

    private Snake snake;
    private Target target;

    private  Set<FieldCell> allCells = new HashSet<>();

    public Field(Snake snake, Target target, ArrayList<Wall> walls, ArrayList<Mine> mines)
    {
        this.snake = snake;
        this.target = target;
        if(walls != null) {
            for (Wall wall : walls) {
                FieldCell cell = FieldCell.getCell(wall.X_Position, wall.Y_Position);
                badCells.add(cell);
            }
        }
        if(mines != null) {
            for (Mine mine : mines) {
                FieldCell cell = FieldCell.getCell(mine.X_Position, mine.Y_Position);
                badCells.add(cell);
            }
        }
    }

    public boolean isHitTarget()
    {
        double x = target.X_Position - snake.Head.X_Position;
        double y = target.Y_Position - snake.Head.Y_Position;
        return Math.sqrt(x * x - y * y) < 5;
    }

    private void generateNextTargetPosition()
    {
        FieldCell nextCell = new FieldCell(-1, -1);
        while (nextCell.equals(new FieldCell(-1, -1)) & !badCells.contains(nextCell))
        {
            double x = Math.random() * Field.Width;
            double y = Math.random() * Field.Height;
            nextCell = FieldCell.getCell(x, y);
        }
        target.setTargetPosition(nextCell.X * FieldCell.translateFactor, nextCell.Y * FieldCell.translateFactor);
    }

    public boolean isSmash()
    {
        return badCells.contains(FieldCell.getCell(snake.Head.X_Position, snake.Head.Y_Position));
    }

    public ImageView onModelUpdateEvent(double direction){
        snake.onModelUpdateEvent(direction);
        if (isSmash()) {
            System.out.println("Bug is dead...");
            System.exit(0);
        }
        if (isHitTarget())
        {
            generateNextTargetPosition();
            return snake.incrementSnake();
        }
        return null;
    }

    public void draw()
    {
        snake.draw();
        target.draw();
    }
}
