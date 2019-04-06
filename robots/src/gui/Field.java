package gui;

import java.util.HashSet;
import java.util.Set;

public class Field {

    public static double Width = 1300;
    public static double Height = 850;

    private Set<FieldCell> badCells = new HashSet<>();

    private Bug bug;
    private Target target;

    private  Set<FieldCell> allCells = new HashSet<>();

    public Field(Bug bug, Target target, Wall[] walls, Mine[] mines)
    {
        this.bug = bug;
        this.target = target;
        for(Wall wall: walls){
            FieldCell cell = FieldCell.getCell(wall.X_Position, wall.Y_Position);
            badCells.add(cell);
        }

        for(Mine mine: mines){
            FieldCell cell = FieldCell.getCell(mine.X_Position, mine.Y_Position);
            badCells.add(cell);
        }
    }

    public void setTargetPosition(double x, double y)
    {
        FieldCell cell = FieldCell.getCell(x, y);
        if (!badCells.contains(cell))
            target.setTargetPosition(x, y);
    }

    private boolean isSmash()
    {
        return badCells.contains(FieldCell.getCell(bug.X_Position, bug.Y_Position));
    }

    public void onModelUpdateEvent(double direction){
        bug.onModelUpdateEvent(direction);
        if (isSmash()) {
            System.out.println("Bug is dead...");
            System.exit(0);
        }
    }

    public void draw()
    {
        bug.draw();
        target.draw();
    }
}
