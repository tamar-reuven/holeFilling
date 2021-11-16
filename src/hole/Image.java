package hole;

import main.WeightFunction;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Set;



public class Image {
    protected Point[][] pixelMat;
    protected Set<Point> hole;
    protected Set<Point> boundary;
    protected WeightFunction weightFunc;
    protected int connectivityType;


    public Image(String imagePath, String maskPath, int z, float e, int connectivityType) {
        this.pixelMat = ImageProcess.buildPixelsMat(imagePath, maskPath);
        this.hole = ImageProcess.findHoleSet(pixelMat);
        this.connectivityType = connectivityType;
        this.boundary = ImageProcess.findBoundarySet(this.pixelMat,this.hole,connectivityType);
        this.weightFunc = (Point u, Point v)->(float)(1 / (Math.pow(HoleFillingCalc.euclideanDistance(u, v), z) + e));
    }

    public void saveFilledImage(String pathName) throws IOException {
        String path = pathName.substring(0,pathName.lastIndexOf("."));
        String format = pathName.substring(pathName.lastIndexOf(".")+1);
        BufferedImage filledImage = new BufferedImage(this.pixelMat[0].length, this.pixelMat.length, BufferedImage.TYPE_INT_RGB);
        setRgbOfEachPixelInFilledImage(filledImage);
        File output = new File( path + "_FILLED." + format);
        ImageIO.write(filledImage, format, output);
        System.out.println("Saved image in " +path+"_FILLED directory.");
    }

    private void setRgbOfEachPixelInFilledImage(BufferedImage filledImage) {
        for (int i = 0; i < this.pixelMat.length; i++) {
            for (int j = 0; j < this.pixelMat[0].length; j++) {
                float pixelColor = this.pixelMat[i][j].getValue();
                Color c = new Color(pixelColor, pixelColor, pixelColor);
                filledImage.setRGB(j, i, c.getRGB());
            }
        }
    }

    public Point[][] getPixelMat() {
        return pixelMat;
    }

    public void setPixelMat(Point[][] pixelMat) {
        this.pixelMat = pixelMat;
    }

    public Set<Point> getHole() {
        return hole;
    }

    public void setHole(Set<Point> hole) {
        this.hole = hole;
    }

    public Set<Point> getBoundary() {
        return boundary;
    }

    public void setBoundary(Set<Point> boundary) {
        this.boundary = boundary;
    }



    public int getcType() {
        return connectivityType;
    }

    public void setcType(int cType) {
        this.connectivityType = cType;
    }

    



}
