package gui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Field {
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

    private Set<FieldCell> getBugsFieldCells(){
        Set<FieldCell> result = new HashSet<>();
        for(int i = 0; i <= 1; i++)
            for(int j = 0; j <= 1; j++)
            {
                double x = bug.X_Position + i * FieldCell.translateFactor;
                double y = bug.Y_Position + j * FieldCell.translateFactor;

                result.add(FieldCell.getCell(x, y));
            }
        return result;
    }

    private boolean isSmash(Set<FieldCell> bugsCells)
    {
        boolean result = false;
        for (FieldCell cell: bugsCells)
        {
            if (badCells.contains(cell))
            {
                result = true;
                break;
            }
        }
        return result;
    }

    public void onModelUpdateEvent(){
        bug.onModelUpdateEvent(target.X_Position, target.Y_Position);
        Set<FieldCell> bugsCells = getBugsFieldCells();
        if (isSmash(bugsCells)) {
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
