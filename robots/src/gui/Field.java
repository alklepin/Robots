package gui;

import javafx.scene.image.ImageView;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Field {

    public static double Width = 1300;
    public static double Height = 850;

    private Set<FieldCell> badCells = new HashSet<>();
    private Set<FieldCell> snakeCells = new HashSet<>();

    private Snake snake;
    private Target target;
    private Zombie zombie;

    public Field(Snake snake, Target target,Zombie zombie, ArrayList<Wall> walls, ArrayList<Mine> mines)
    {
        this.snake = snake;
        this.target = target;
        this.zombie = zombie;
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

    private void updateBadCells()
    {
        FieldCell newCell = FieldCell.getCell(snake.Head.X_Position, snake.Head.Y_Position);
        FieldCell lastCell = FieldCell.getCell(snake.Tail.X_Position, snake.Tail.Y_Position);
        snakeCells.add(newCell);
        snakeCells.remove(lastCell);
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

    private ArrayList<Double> getZombiePossiblePositions()
    {
        FieldCell zombieCell = FieldCell.getCell(zombie.X_Position, zombie.Y_Position);
        ArrayList<Double> result = new ArrayList<>();
        double[] directions = {Directions.LEFT, Directions.UP, Directions.DOWN, Directions.RIGHT};
        int counter = 0;
        for (int i = -1; i <= 1;i++)
            for(int j = -1; j <= 1; j++)
            {
                if ((i + j) % 2 == 0)
                    continue;
                if (zombieCell.X + i < 0 || zombieCell.X + i > Width / FieldCell.translateFactor)
                    continue;
                if (zombieCell.Y + j < 0 || zombieCell.Y + j > Height / FieldCell.translateFactor)
                    continue;
                FieldCell nextCell = new FieldCell(zombieCell.X + i, zombieCell.Y + j);
                if (!badCells.contains(nextCell))
                    result.add(directions[counter]);
                counter++;
            }
        return result;
    }

    private boolean zombieCatchSnake()
    {
        FieldCell zombiePosition = FieldCell.getCell(zombie.X_Position, zombie.Y_Position);
        return snakeCells.contains(zombiePosition);
    }

    public boolean isSmash()
    {
        return badCells.contains(FieldCell.getCell(snake.Head.X_Position, snake.Head.Y_Position));
    }

    public ImageView onModelUpdateEvent(double direction){
        snake.onModelUpdateEvent(direction);
        if (isSmash()) {
            System.out.println("Snake is dead...");
            System.exit(0);
        }
        updateBadCells();
        if (zombieCatchSnake())
        {
            System.out.println("Zombie is eating snake. Snake is dead...");
            System.exit(0);
        }
        if (isHitTarget())
        {
            generateNextTargetPosition();
            return snake.incrementSnake();
        }
        zombie.move(getZombiePossiblePositions());
        if (zombieCatchSnake())
        {
            System.out.println("Zombie is eating snake. Snake is dead...");
            System.exit(0);
        }
        return null;
    }

    public void draw()
    {
        snake.draw();
        target.draw();
        zombie.draw();
    }
}
