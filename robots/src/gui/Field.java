package gui;

import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Field {

    public static double Width = 1300;
    public static double Height = 850;

    private Set<FieldCell> badCells = new HashSet<>();

    private Bug bug;
    private Target target;
    private ArrayList<Body> bodys;

    private  Set<FieldCell> allCells = new HashSet<>();

    public Field(Bug bug, Target target, ArrayList<Body> bodys,  ArrayList<Wall> walls, ArrayList<Mine> mines)
    {
        this.bug = bug;
        this.target = target;
        this.bodys = bodys;
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

    private boolean isEat()
    {
        return (bug.X_Position - 10 <= target.X_Position) & (target.X_Position <= bug.X_Position + 10)
                & (bug.Y_Position - 10 <=  target.Y_Position) & (target.Y_Position <= bug.Y_Position + 10);
    }

    public void onModelUpdateEvent(double direction, Pane pane){
        bug.onModelUpdateEvent(direction);
        if (isSmash()) {
            System.out.println("Bug is dead...");
            System.exit(0);
        }
        if (isEat())
        {
            target.setTargetPosition(0, 0);
            Body body = new Body(bug.lastPoint_X, bug.lastPoint_Y);
            bodys.add(body);
            System.out.println(bodys);
        }
        moveBody(bug.lastPoint_X, bug.lastPoint_Y);
    }

    public ArrayList<Body> getBodys()
    {
        return bodys;
    }

    public void draw()
    {
        bug.draw();
        target.draw();
        for (int i = 0; i < bodys.size(); i++)
            bodys.get(i).draw();
    }


    public void moveBody(double head_x, double head_y)
    {
        double last_X = head_x;
        double last_Y = head_y;
        System.out.println(last_X);
        if(bodys.size() > 0) {
            for (int i = bodys.size() - 1; i + 1 > 0; i--) {
                Body piece = bodys.get(i);
                double x = last_X;
                double y = last_Y;
                last_X = piece.current_X;
                System.out.println(last_X);
                last_Y = piece.current_Y;
                piece.current_X = x;
                piece.current_Y = y;
            }
        }
    }
}
