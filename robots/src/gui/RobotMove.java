package gui;

import javafx.beans.Observable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Observer;
import java.util.Stack;

public class RobotMove extends java.util.Observable {

    ArrayList<Observer> observable = new ArrayList<>();
    volatile int m_robotPositionX;
    volatile int m_robotPositionY;
    volatile int m_robotDirection;

    volatile int m_targetPositionX;
    volatile int m_targetPositionY;

    volatile Point start;
    volatile Point finish;

    private static final double maxVelocity = 0.1;
    private static final double maxAngularVelocity = 0.001;


    public RobotMove(){
        m_robotPositionX=100;
        m_robotPositionY=100;
        m_robotDirection=0;
        m_targetPositionX=150;
        m_targetPositionY=100;
        start = new Point(m_robotPositionX/10,m_robotPositionY/10);
        finish = new Point(m_targetPositionX/10,m_targetPositionY/10);
    }

    protected void setTargetPosition(Point p)
    {
        m_targetPositionX = p.x;
        m_targetPositionY = p.y;
    }

    protected Point getTargetPosition(){
        return new Point(m_targetPositionX,m_targetPositionY);
    }

    protected void setRobotPosition(Point p){
        m_robotPositionX = p.x;
        m_robotPositionY = p.y;
    }

    int[][] setMatrix(int x, int y){
        int[][] matrix = new int[x/10][y/10];
        for (int i=0;i<x/10;i++){
            for (int j=0;j<y/10;j++){
                matrix[i][j]=1;
            }
        }
        return matrix;
    }

    public Stack<Point> algo(int[][] matrix) {
        Stack<Point> stack=new Stack<>();
        ArrayList<Point> F = new ArrayList<>();//множество вершин
        for (int i=-1;i<=1;i++){
            for (int j=-1;j<=1;j++) {
                if (i!=0 || j!=0) {
                    F.add(new Point(start.x+i,start.y+j));//добавляю все, кроме стартовой
                }
            }
        }
        int[][] distance = new int[matrix[0].length][matrix.length];//массив расстояний от начала
        Point[][] previous = new Point[matrix[0].length][matrix.length];//массив предшествующих вершин
        distance[start.x][start.y] = 0;
        previous[start.x][start.y] = start;
        for (Point i : F) {//первый проход по всем доступным вершинам из начала
            if (i != start) {
                distance[i.x][i.y] = 1;
                previous[i.x][i.y] = start;
            }
        }
        for (int k = 1; k < matrix[0].length*matrix.length - 1; k++) {//количество итераций
            Point w = minV(F,distance);//беру вершину с наименьшим расстоянием от начала
            F.remove(F.indexOf(w));//удаляю из множества вершин
            for (Point v : F) {
                if (distance[w.x][w.y] + 1 < distance[v.x][v.y]&&1!=Integer.MAX_VALUE) {
                    distance[v.x][v.y] = distance[w.x][w.y] + 1;
                    previous[v.x][v.y] = w;
                }
            }
        }
        Point t = finish;
        while(previous[t.x][t.y]!=start){
            t = previous[t.x][t.y];
            stack.push(t);
        }
        return stack;
    }

    private static Point minV(ArrayList<Point> F, int[][] distance){//поиск вершины с наименьшим расстоянием
        Point min = F.get(0);
        for (Point i:F){
            if (distance[i.x][i.y]<distance[min.x][min.y])
                min=i;
        }
        return min;
    }

    protected void onModelUpdateEvent()
    {
        Stack stack = algo(setMatrix(300,300));
        while(!stack.empty()){
            Point current = (Point) stack.pop();
            m_robotPositionX=current.x;
            m_robotPositionY=current.y;
        }
        setChanged();
        notifyObservers();
    }

    private void moveRobot()
    {
        setChanged();
        notifyObservers();//уведомление о перемещении
    }

    public void notifyObservers(){//обновление данных наблюдателей
        for(Observer o: observable){
            o.update(this,null);
        }
    }

}