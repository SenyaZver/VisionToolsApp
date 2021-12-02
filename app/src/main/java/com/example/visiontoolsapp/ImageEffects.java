package com.example.visiontoolsapp;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Build;



import androidx.annotation.RequiresApi;

import java.util.Collections;

public class ImageEffects {


    Bitmap image;
    boolean viewFlip;
    boolean[] ColourCorrection = {false, false, false, false, false};

//0    boolean GBR = false;
//1    boolean BRG = false;
//2    boolean GRB = false;
//3    boolean BGR = false;
//4    boolean RBG = false;


    @RequiresApi(api = Build.VERSION_CODES.Q)
    public ImageEffects(boolean ViewFlipChoice, int colourCorrectionChoice, Bitmap imageArg) {
        this.viewFlip = ViewFlipChoice;

        if (ViewFlipChoice) {
            this.FliptheView();
        }

        this.image = imageArg;
        if (colourCorrectionChoice != -1) {
            this.ColourCorrection[colourCorrectionChoice] = true;
            this.ChangeColours();
        }


    }

    public Bitmap getImage() {
        return image;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void ChangeColours() {
        int width = image.getWidth();
        int height = image.getHeight();


        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                int pixelColor = image.getPixel(x, y);

                int R = Color.red(pixelColor);
                int G = Color.green(pixelColor);
                int B = Color.blue(pixelColor);


                if (ColourCorrection[0] = true) {
                    image.setPixel(G,B,R);
                }
                else if (ColourCorrection[1] = true) {
                    image.setPixel(B,R,G);
                }
                else if (ColourCorrection[2] = true) {
                    image.setPixel(G,R,B);
                }
                else if (ColourCorrection[3] = true) {
                    image.setPixel(B,G,R);
                }
                else if (ColourCorrection[4] = true) {
                    image.setPixel(R,B,G);
                }
            }
        }

    }



    public static Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    private void FliptheView() {
        image = RotateBitmap(image, 180);
    }


}


