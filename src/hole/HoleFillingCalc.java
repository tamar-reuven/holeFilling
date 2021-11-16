package hole;


import main.WeightFunction;

import java.util.Iterator;
import java.util.Set;

public class HoleFillingCalc {

    public static void fillHole(Image image) {
        for (Point h : image.hole) {
            h.setValue(calcColor(h, image.boundary, image.weightFunc));
        }
    }
    public static float calcColor(Point h, Set<Point> B, WeightFunction w) {

        float numerator = 0;
        float denominator = 0;

        for (Point b : B) {
            float weight = w.weight(h, b);
            numerator += weight * b.getValue();
            denominator += weight;
        }

        return numerator / denominator;
    }


    public static float euclideanDistance(Point p1, Point p2) {
        return (float) (Math.sqrt(Math.pow((p1.getX() - p2.getX()),2) + Math.pow((p1.getY() - p2.getY()),2)));
    }
}
