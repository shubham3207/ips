package org.pitambar.ins;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;

import java.util.ArrayList;

public class MyTextureView extends TextureView implements TextureView.SurfaceTextureListener{

    private Bitmap mImage;
    private Bitmap mMarker;
    private float mX;
    private float mY;
    private float mVelocityX;
    private float mVelocityY;
    private Paint mPaint;
    private Surface mSurface;
    private int mWidth, mHeight;
    private float[] mPosition;
    private float[] mOrientation;
    private ArrayList<float[]> mPositionArray = new ArrayList<>();
    Canvas canvas;

    public MyTextureView(Context context, int width, int height) {
        super(context);
        // Initialize the image and paint objects

        DisplayMetrics displayMetrics = new DisplayMetrics();

        mWidth = width;
        mHeight = height;

        mImage = BitmapFactory.decodeResource(getResources(), R.drawable.floorplan_hero);
        mPaint = new Paint();
        mPaint.setColor(Color.RED);

        mMarker = BitmapFactory.decodeResource(getResources(), R.drawable.compass_icon);

        float imageWidth = mImage.getWidth();
        float imageHeight = mImage.getHeight();

        // Set the x-coordinate of the image's position to the center of the screen
        mX = width / 2 - mImage.getWidth() / 2;

        // Set the y-coordinate of the image's position to the center of the screen
        mY = height / 2 - mImage.getHeight() / 2;

        // Set the velocity of the image's movement to 0
        mVelocityX = 0;
        mVelocityY = 0;
        // Set this view as the touch listener
//        setOnTouchListener(this);
        setSurfaceTextureListener(this);

    }


//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        // Update the velocity of the image's movement based on the touch input
//        mVelocityX = event.getX() - mX;
//        mVelocityY = event.getY() - mY;
//        drawImage();
//        return true;
//    }


    public void setmVelocityX(float mVelocityX) {
        this.mVelocityX = mVelocityX;
    }

    public void setmVelocityY(float mVelocityY) {
        this.mVelocityY = mVelocityY;
    }

    public void setmPositionArray(ArrayList<float[]> mPositionArray) {
        this.mPositionArray = mPositionArray;
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mSurface = new Surface(surface);
//        mUpdateThread = new UpdateThread(this);
        drawImage();
//        mUpdateThread.setRunning(true);
//        mUpdateThread.start();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    public void setmX(float mX) {
        this.mX = mX;
    }

    public void setmY(float mY) {
        this.mY = mY;
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
//        mUpdateThread.setRunning(false);
//        try {
////            mUpdateThread.join();
//            mSurface.release();
//            mSurface = null;
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        mSurface.release();
        mSurface = null;
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    public void drawImage(){
        update();
        canvas = mSurface.lockCanvas(null);
//        Bitmap temp = BitmapFactory.decodeResource(getResources(), R.drawable.indoor_map);
//        Bitmap image = Bitmap.createScaledBitmap(temp, canvas.getWidth(), canvas.getHeight(), true);
//        canvas.drawBitmap(image, 0, 0, null);
//
//        Bitmap over = BitmapFactory.decodeResource(getResources(), R.drawable.transparent_picture);
//        image = Bitmap.createScaledBitmap(over, canvas.getWidth(), canvas.getHeight(), true);
//        canvas.drawBitmap(image, 0, 0, null);
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//        mImage = Bitmap.createScaledBitmap(mImage, canvas.getWidth(), canvas.getHeight(),true);
//        canvas.drawBitmap(mImage, mX, mY, mPaint);
//        canvas.drawBitmap(mImage, 0, 0, mPaint);
           Bitmap bit =  drawCircle(mImage);
           if(mPositionArray.size()>1) {

               float tempX = mPositionArray.get(mPositionArray.size() - 1)[0]-mPositionArray.get(mPositionArray.size() - 2)[0];
               float tempY = mPositionArray.get(mPositionArray.size() - 1)[1]-mPositionArray.get(mPositionArray.size() - 2)[1];

               canvas.drawBitmap(bit, tempX, tempY, mPaint);
           }else   canvas.drawBitmap(bit, mX, mY, mPaint);
//        canvas.drawCircle(mWidth/2, mHeight/2, 20f, mPaint);
//        canvas.drawBitmap(mMarker, mWidth/2-(mMarker.getWidth()/2), mHeight/2- (mMarker.getHeight()/2), mPaint);

//        canvas.rotate(20,mWidth/2, mHeight/2);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        canvas.save();
        // Draw the circle at the current position
//        if (mPositionArray.size() > 0) {
//            for(int i = 0;i<mPositionArray.size(); i++){
//                canvas.drawCircle(mPositionArray.get(i)[0], mPositionArray.get(i)[1], 10f, paint);
//                if(mPositionArray.size()>1 && i<=mPositionArray.size()-2){
//                    canvas.drawLine(mPositionArray.get(i)[0],mPositionArray.get(i)[1], mPositionArray.get(i+1)[0], mPositionArray.get(i+1)[1], paint);
//                }
//            }
//            postInvalidate();
//        }
        canvas.restore();


        mSurface.unlockCanvasAndPost(canvas);
    }
    private  Bitmap drawCircle(Bitmap bm) {

        Bitmap bmOverlay = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), bm.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bm, new Matrix(), null);
//        canvas.drawCircle(bm.getWidth()/2, bm.getHeight()/2, 50, paint);

        //draw arry
        // Draw the circle at the current position
        if (mPositionArray.size() > 0) {
            for(int i = 0;i<mPositionArray.size(); i++){
                canvas.drawCircle(mPositionArray.get(i)[0], mPositionArray.get(i)[1], 10f, paint);
                if(mPositionArray.size()>1 && i<=mPositionArray.size()-2){
                    canvas.drawLine(mPositionArray.get(i)[0],mPositionArray.get(i)[1], mPositionArray.get(i+1)[0], mPositionArray.get(i+1)[1], paint);
                }
            }
            postInvalidate();
        }


        return bmOverlay;
    }



//    private static Bitmap drawCircle(Bitmap bitmap) {
//        int size = Math.min(bitmap.getWidth(), bitmap.getHeight());
//
//        Bitmap localBitmap = Bitmap.createBitmap(size, size,
//                Bitmap.Config.ARGB_8888);
//        Canvas localCanvas = new Canvas(localBitmap);
//        localCanvas.setDrawFilter(new PaintFlagsDrawFilter(0,
//                Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
//        Paint paint = new Paint();
//        paint.setColor(Color.BLUE);
//        paint.setAntiAlias(true);/*  www .  ja  v  a 2 s  . com*/
//
//        int left = bitmap.getWidth() / 2 - size / 2;
//        int top = bitmap.getHeight() / 2 - size / 2;
//
//        RectF rectF = new RectF(0, 0, size, size);
////        localCanvas.drawOval(rectF, paint);
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//        localCanvas.drawBitmap(bitmap, -left, -top, paint);
//        localCanvas.drawOval(rectF, paint);
//
//        paint.setXfermode(null);
//
//        return localBitmap;
//    }
    public void update() {
        // Update the x-coordinate of the image's position based on the velocity
        mX += mVelocityX;
        //update the y-coordinate of the image's position based on the velocity
        mY += mVelocityY;

//        // If the image has moved off the screen, wrap it around to the other side
//        if (mX > mWidth) {
//            mX = -mImage.getWidth();
//        } else if (mX + mImage.getWidth() < 0) {
//            mX = mWidth;
//        }
//
//        if (mY > mHeight) {
//            mY = -mImage.getHeight();
//        } else if (mY + mImage.getHeight() < 0) {
//            mY = mHeight;
//        }
    }
}
