package hole;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ImageProcess {
    private static final double intensityValue = 0.5;
    private static final float holeValue = -1f;

    public static Point[][] buildPixelsMat(String imagePath, String maskPath){
        Point[][] pixels = null;
        float value;

        try{
            BufferedImage image = ImageIO.read(new File(imagePath));
            BufferedImage mask = ImageIO.read(new File(maskPath));
            int width = mask.getWidth();
            int height = mask.getHeight();
            pixels = new Point[height][width];

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {

                    Color pixelColor = new Color(mask.getRGB(j,i));
                    if(rgbToGrayscale(pixelColor)>intensityValue){
                        Color imagePixelColor = new Color(image.getRGB(j,i));
                        value = rgbToGrayscale(imagePixelColor);
                    }else{
                        value = holeValue;
                    }
                    pixels[i][j] = new Point(i, j, value);
                }
            }
        }catch(IOException exception){
            System.out.println(exception.fillInStackTrace());
            System.exit(1);
        }

        return pixels;
    }

    private static float rgbToGrayscale(Color pixelColor) {
        float avg = (pixelColor.getRed() + pixelColor.getGreen() + pixelColor.getBlue()) / 3;
        return avg / 255;
    }

    public static Set<Point> findHoleSet(Point[][] pixels) {
        Set<Point> H = new HashSet();
        for (int i = 1; i < pixels.length - 1; i++) {
            for (int j = 1; j < pixels[0].length - 1; j++) {
                if (pixels[i][j].getValue() == holeValue) {
                    H.add(pixels[i][j]);
                }
            }
        }
        return H;
    }

    public static Set<Point> findBoundarySet(Point[][] pixels, Set<Point> H, int connectivityType) {
        Set<Point> B = new HashSet<>();
        for (Point p : H) {
            int x = p.getX();
            int y = p.getY();
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (connectivityType == 4 && Math.abs(i) + Math.abs(j) == 2) {
                        // if connectivityType is 4, we want to skip diagonal neighbors
                        // |i| + |j| gives us 2 only if the the current pixel is a diagonal neighbor
                        continue;
                    }
                    if (!isHole(pixels[y + i][x + j])) {
                        B.add(pixels[y + i][x + j]);
                    }
                }
            }
        }
        return B;
    }

    private static boolean isHole(Point point) { return point.getValue()== holeValue;}

}
