package main;

import hole.HoleFillingCalc;
import hole.Image;
import hole.ImageConverter;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static final String requiredFormat = "PNG";

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter image path");
            String imagePath = scanner.nextLine();
            System.out.println("Enter mask Path");
            String maskPath = scanner.nextLine();
            System.out.println("Enter z");
            int z = Integer.parseInt(scanner.nextLine());
            System.out.println("Enter e");
            float e = Float.parseFloat(scanner.nextLine());
            System.out.println("Enter connectivity Type");
            int connectivityType = Integer.parseInt(scanner.nextLine());

            if (connectivityType != 4 && connectivityType != 8) {
                throw new InvalidPixelConnectivity();
            }

            if(checkFormatAndConvertIfNeeded(imagePath)) {
                Image image = new Image(imagePath, maskPath, z, e, connectivityType);
                HoleFillingCalc.fillHole(image);
                image.saveFilledImage(imagePath);
            }


        }catch (InvalidPixelConnectivity e){
            System.out.println(e.fillInStackTrace());
            System.exit(1);
        } catch (IOException e) {
            System.out.println(e.fillInStackTrace());
            System.exit(1);
        }

    }


    private static boolean checkFormatAndConvertIfNeeded(String imagePath) {
        String format = imagePath.substring(imagePath.lastIndexOf(".")+1);
        if(!(format.equals(requiredFormat.toLowerCase()))){
            String oldImagePath= imagePath;
            imagePath = imagePath.substring(0,imagePath.lastIndexOf("."))+"."+requiredFormat.toLowerCase();
            try {
                boolean result = ImageConverter.convertFormat(oldImagePath, imagePath, requiredFormat);
                if (result) {
                    System.out.println("Image converted successfully.");
                    return true;
                } else {
                    System.out.println("Could not convert image.");
                    return false;
                }
            } catch (IOException ex) {
                System.out.println("Error during converting image.");
                ex.printStackTrace();
            }
        }
        return true;
    }
}
class InvalidPixelConnectivity extends Exception { }

