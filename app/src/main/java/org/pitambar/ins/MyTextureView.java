package org.pitambar.ins;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.SurfaceTexture;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;

public class MyTextureView extends TextureView implements TextureView.SurfaceTextureListener, View.OnTouchListener {

    private Bitmap mImage;
    private float mX;
    private float mY;
    private float mVelocityX;
    private float mVelocityY;
    private Paint mPaint;
    private Surface mSurface;
    private int mWidth, mHeight;
    Canvas canvas;

    public MyTextureView(Context context, int width, int height) {
        super(context);
        // Initialize the image and paint objects

        DisplayMetrics displayMetrics = new DisplayMetrics();

        mWidth = width;
        mHeight = height;

        mImage = BitmapFactory.decodeResource(getResources(), R.drawable.indoor_map);
        mPaint = new Paint();
        mPaint.setColor(Color.RED);

        // Set the x-coordinate of the image's position to the center of the screen
        mX = width / 2 - mImage.getWidth() / 2;

        // Set the y-coordinate of the image's position to the center of the screen
        mY = height / 2 - mImage.getHeight() / 2;

        // Set the velocity of the image's movement to 0
        mVelocityX = 0;
        mVelocityY = 0;
        // Set this view as the touch listener
        setOnTouchListener(this);
        setSurfaceTextureListener(this);

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // Update the velocity of the image's movement based on the touch input
        mVelocityX = event.getX() - mX;
        mVelocityY = event.getY() - mY;
        drawImage();
        return true;
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

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        mUpdateThread.setRunning(false);
        try {
            mUpdateThread.join();
            mSurface.release();
            mSurface = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    public void drawImage(){
        update();
         canvas = mSurface.lockCanvas(null);
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        canvas.drawBitmap(mImage, mX, mY, mPaint);
        mSurface.unlockCanvasAndPost(canvas);
    }
    public void update() {
        // Update the x-coordinate of the image's position based on the velocity
        mX += mVelocityX;
        mY += mVelocityY;

        // If the image has moved off the screen, wrap it around to the other side
        if (mX > mWidth) {
            mX = -mImage.getWidth();
        } else if (mX + mImage.getWidth() < 0) {
            mX = mWidth;
        }

        if (mY > mHeight) {
            mY = -mImage.getHeight();
        } else if (mY + mImage.getHeight() < 0) {
            mY = mHeight;
        }
    }
}
