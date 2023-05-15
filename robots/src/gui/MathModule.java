package gui;

public class MathModule {

    public static int round(double number) {
        return (int) (number + 0.5);
    }

    public static double calculateDistance(double firstPointXCoordinate, double firstPointYCoordinate, double secondPointXCoordinate, double secondPointYCoordinate) {
        double xCoordinateDifference = firstPointXCoordinate - secondPointXCoordinate;
        double yCoordinateDifference = firstPointYCoordinate - secondPointYCoordinate;
        return Math.sqrt(xCoordinateDifference * xCoordinateDifference + yCoordinateDifference * yCoordinateDifference);
    }

    public static double calculateAngle(double startXCoordinate, double startYCoordinate, double endXCoordinate, double endYCoordinate) {
        double xCoordinateDifference = endXCoordinate - startXCoordinate;
        double yCoordinateDifference = endYCoordinate - startYCoordinate;
        return asNormalizedRadians(Math.atan2(yCoordinateDifference, xCoordinateDifference));
    }

    public static double asNormalizedRadians(double angle) {
        while (angle < 0) {
            angle += 2 * Math.PI;
        }
        while (angle >= 2 * Math.PI) {
            angle -= 2 * Math.PI;
        }
        return angle;
    }
}
