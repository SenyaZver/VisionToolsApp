package com.example.visiontoolsapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.util.Size;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

public class CameraActivity extends FragmentActivity  {

    private static final int PERMISSION_REQUEST_CAMERA = 10;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private ImageView cameraView1;
    private ImageView cameraView2;
    private final YUVtoRGB translator = new YUVtoRGB();


    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        getWindow(). addFlags (WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        Bundle bundle = getIntent().getExtras();
        boolean viewFlip_Choice = bundle.getBoolean("viewFlip_Choice");
        int colourCorrection_Choice = bundle.getInt("colourCorrection_Choice");


        HideNavigationBar();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;


        cameraView1 = findViewById(R.id.CameraView);
        cameraView2 = findViewById(R.id.CameraView2);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA},
                    PERMISSION_REQUEST_CAMERA);
        } else {
            

            initializeCamera(height, width, viewFlip_Choice, colourCorrection_Choice);

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        HideNavigationBar();
    }


    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void initializeCamera(int height, int width, boolean viewFlip_Choice, int colourCorrection_Choice) {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {


                ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                        .setTargetResolution(new Size(width, height))
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build();

                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();


                imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(CameraActivity.this),
                        image -> {
                            @SuppressLint("UnsafeExperimentalUsageError") Image img = image.getImage();


                            Bitmap bitmap = translator.translateYUV(img, CameraActivity.this);



                            bitmap = applyEffects(viewFlip_Choice, colourCorrection_Choice, bitmap);


                            cameraView1.setRotation(image.getImageInfo().getRotationDegrees());
                            cameraView1.setImageBitmap(bitmap);
                            cameraView2.setRotation(image.getImageInfo().getRotationDegrees());
                            cameraView2.setImageBitmap(bitmap);
                            image.close();
                        });



                cameraProvider.bindToLifecycle(CameraActivity.this, cameraSelector, imageAnalysis);


            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }


    private Bitmap applyEffects(boolean ViewFlip_Choice, int ColourCorrection_Choice, Bitmap bitmap) {
        //remove bitmap manipulations
        if (ViewFlip_Choice) {
            bitmap = RotateBitmap(bitmap, 180);

        }
        if (ColourCorrection_Choice != -1) {
            this.cameraView1.setColorFilter(new ColorMatrixColorFilter(new ColorMatrix(getColorMatrix(ColourCorrection_Choice))));
            this.cameraView2.setColorFilter(new ColorMatrixColorFilter(new ColorMatrix(getColorMatrix(ColourCorrection_Choice))));
        }

        return bitmap;
    }





    public static Bitmap RotateBitmap(Bitmap bitmap, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }



    private static ColorMatrix getColorMatrix(int ColourCorrection_Choice) {
        float[] matrix = new float[0];


            if (ColourCorrection_Choice == 0) {
                matrix = new float[] {
                        0, 1, 0, 0, 0,
                        0, 0, 1, 0, 0,
                        1, 0, 0, 0, 0,
                        0, 0, 0, 1, 0 };
            }
            else if (ColourCorrection_Choice == 1) {
                matrix = new float[] {

                        0, 0, 1, 0, 0,
                        1, 0, 0, 0, 0,
                        0, 1, 0, 0, 0,
                        0, 0, 0, 1, 0 };
            }
            else if (ColourCorrection_Choice == 2) {
                matrix = new float[] {
                        0, 1, 0, 0, 0,
                        1, 0, 0, 0, 0,
                        0, 0, 1, 0, 0,
                        0, 0, 0, 1, 0 };
            }

            else if (ColourCorrection_Choice == 3) {
                matrix = new float[] {
                        0, 0, 1, 0, 0,
                        0, 1, 0, 0, 0,
                        1, 0, 0, 0, 0,
                        0, 0, 0, 1, 0 };
            }
            else if (ColourCorrection_Choice == 4) {
                matrix = new float[] {
                        1, 0, 0, 0, 0,
                        0, 0, 1, 0, 0,
                        0, 1, 0, 0, 0,
                        0, 0, 0, 1, 0 };
            }
            return new ColorMatrix(matrix);


    }






    private void HideNavigationBar() {
        this.getWindow().getDecorView()
                .setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                );
    }
}



//    private static Bitmap ChangeColours(Bitmap bitmap, int ColourCorrection_Choice) {
//        int[] bitmapPixels = new int[bitmap.getWidth()*bitmap.getHeight()];
//
//        bitmap.getPixels(bitmapPixels,0,bitmap.getWidth(),0, 0,
//                bitmap.getWidth(),bitmap.getHeight());
//
//        for (int i = 0; i<bitmap.getWidth()*bitmap.getHeight(); ++i) {
//            int pixelColor = bitmapPixels[i];
//
//            int A = pixelColor >> 24 & 0xff;
//            int R = pixelColor >> 16 & 0xff;
//            int G = pixelColor >> 8 & 0xff;
//            int B = pixelColor & 0xff;
//
//            if (ColourCorrection_Choice == 0) {
//                bitmapPixels[i] =
//                        (A & 0xff) << 24 | (G & 0xff) << 16 | (B & 0xff) << 8 | (R & 0xff);
//            }
//            else if (ColourCorrection_Choice == 1) {
//                bitmapPixels[i] =
//                        (A & 0xff) << 24 | (B & 0xff) << 16 | (R & 0xff) << 8 | (G & 0xff);
//            }
//            else if (ColourCorrection_Choice == 2) {
//                bitmapPixels[i] =
//                        (A & 0xff) << 24 | (G & 0xff) << 16 | (R & 0xff) << 8 | (B & 0xff);
//            }
//            else if (ColourCorrection_Choice == 3) {
//                bitmapPixels[i] =
//                        (A & 0xff) << 24 | (B & 0xff) << 16 | (G & 0xff) << 8 | (R & 0xff);
//            }
//            else if (ColourCorrection_Choice == 4) {
//                bitmapPixels[i] =
//                        (A & 0xff) << 24 | (R & 0xff) << 16 | (B & 0xff) << 8 | (G & 0xff);
//            }
//        }
//        bitmap.setPixels(bitmapPixels,0,bitmap.getWidth(),0, 0,
//                bitmap.getWidth(),bitmap.getHeight());
//        return bitmap;
//    }




//    private static Bitmap ChangeColours(Bitmap bitmap, int ColourCorrection_Choice) {
//        int[] bitmapPixels = new int[bitmap.getWidth()*bitmap.getHeight()];
//
//        bitmap.getPixels(bitmapPixels,0,bitmap.getWidth(),0, 0,
//                bitmap.getWidth(),bitmap.getHeight());
//
//        for (int i = 0; i<bitmap.getWidth()*bitmap.getHeight(); ++i) {
//            int pixelColor = bitmapPixels[i];
//
//                int R = pixelColor >> 16 & 0xff;
//                int G = pixelColor >> 8 & 0xff;
//                int B = pixelColor & 0xff;
//
//
////            int R = Color.red(pixelColor);
////            int G = Color.green(pixelColor);
////            int B = Color.blue(pixelColor);
//
//            if (ColourCorrection_Choice == 0) {
//                bitmapPixels[i] = Color.valueOf(G, B, R).toArgb();
//            }
//            else if (ColourCorrection_Choice == 1) {
//                bitmapPixels[i] = Color.valueOf(B,R,G).toArgb();
//            }
//            else if (ColourCorrection_Choice == 2) {
//                bitmapPixels[i] = Color.valueOf(G,R,B).toArgb();
//            }
//            else if (ColourCorrection_Choice == 3) {
//                bitmapPixels[i] = Color.valueOf(B,G,R).toArgb();
//
//            }
//            else if (ColourCorrection_Choice == 4) {
//                bitmapPixels[i] = Color.valueOf(R,B,G).toArgb();
//            }
//        }
//        bitmap.setPixels(bitmapPixels,0,bitmap.getWidth(),0, 0,
//                bitmap.getWidth(),bitmap.getHeight());
//        return bitmap;
//    }