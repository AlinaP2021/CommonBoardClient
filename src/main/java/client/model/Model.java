package client.model;

import java.awt.*;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class Model {

    private CopyOnWriteArrayList<CopyOnWriteArrayList<Point>> allPointLists = new CopyOnWriteArrayList<>();

    private int maxX;
    private int maxY;

    public void setMaxX(int maxX) {
        this.maxX = maxX;
    }

    public void setMaxY(int maxY) {
        this.maxY = maxY;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMaxY() {
        return maxY;
    }

    public CopyOnWriteArrayList<Point> getLastPointList() {
        int size = allPointLists.size();
        if (size > 0) {
            return allPointLists.get(size - 1);
        }
        CopyOnWriteArrayList<Point> newPointList = new CopyOnWriteArrayList<>();
        allPointLists.add(newPointList);
        return newPointList;
    }

    public CopyOnWriteArrayList<CopyOnWriteArrayList<Point>> getAllPointLists() {
        return allPointLists;
    }

    public void drawLines(Graphics2D graphics2D) {
        Path2D.Double path = new Path2D.Double();

        for (CopyOnWriteArrayList<Point> pointList : allPointLists) {
            ArrayList<Point> newPointList = createNewPointList(pointList);

            int size = newPointList.size();
            double a = 2;

            if (size >= 2) {
                ArrayList<Point> derivativeList = createDerivativeList(newPointList, a);
                path.moveTo(newPointList.get(0).getX(), newPointList.get(0).getY());

                for (int i = 0; i < size; i++) {

                    if (i + 1 < size) {
                        double controlPoint1X = newPointList.get(i).getX() + derivativeList.get(i).getX() / 3;
                        double controlPoint1Y = newPointList.get(i).getY() + derivativeList.get(i).getY() / 3;

                        double controlPoint2X = newPointList.get(i + 1).getX() - derivativeList.get(i + 1).getX() / 3;
                        double controlPoint2Y = newPointList.get(i + 1).getY() - derivativeList.get(i + 1).getY() / 3;

                        path.curveTo(controlPoint1X, controlPoint1Y,
                                controlPoint2X, controlPoint2Y,
                                newPointList.get(i + 1).getX(), newPointList.get(i + 1).getY());
                        graphics2D.draw(path);
                    }
                }
            }
        }
    }

    private ArrayList<Point> createDerivativeList(ArrayList<Point> newPointList, double a) {
        int size = newPointList.size();
        ArrayList<Point> derivativeList = new ArrayList<>();
        derivativeList.add(new Point((newPointList.get(1).getX() - newPointList.get(0).getX()) / a,
                (newPointList.get(1).getY() - newPointList.get(0).getY()) / a));
        for (int i = 1; i < size - 1; i++) {
            derivativeList.add(new Point((newPointList.get(i + 1).getX() - newPointList.get(i - 1).getX()) / a,
                    (newPointList.get(i + 1).getY() - newPointList.get(i - 1).getY()) / a));
        }
        derivativeList.add(new Point((newPointList.get(size - 1).getX() - newPointList.get(size - 2).getX()) / a,
                (newPointList.get(size - 1).getY() - newPointList.get(size - 2).getY()) / a));
        return derivativeList;
    }

    private ArrayList<Point> createNewPointList(CopyOnWriteArrayList<Point> pointList) {
        ArrayList<Point> newPointList = new ArrayList<>();
        newPointList.add(pointList.get(0));
        for (int i = 3; i < pointList.size() - 1; i = i + 3) {
            newPointList.add(pointList.get(i));
        }
        newPointList.add(pointList.get(pointList.size() - 1));
        return newPointList;
    }
}
