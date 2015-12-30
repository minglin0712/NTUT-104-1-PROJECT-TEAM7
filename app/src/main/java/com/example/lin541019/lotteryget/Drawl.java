package com.example.lin541019.lotteryget;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

/**
 * Created by lin541019 on 12/8/15.
 */
public class Drawl extends View {
    //宣告區
    private Paint mPaint;
    private Paint erasePaint;

    private Bitmap mBitmap;
    private Bitmap MapToFile;

    private int lotteryNumber = -1;
    public void setLotteryNumber(int lotteryNumber){
        this.lotteryNumber = lotteryNumber;
    }
    public int getLotteryNumber(){
        return lotteryNumber;
    }

    private boolean isErasable = true;
    public void setErasable(boolean erasable){
        isErasable = erasable;
    }
    public boolean getErasable(){
        return isErasable;
    }

    private boolean paintTight = true;
    public void setPaintTight(boolean paintTight){
        this.paintTight = paintTight;
    }
    public boolean getPaintTight(){
        return paintTight;
    }

    private int eraseWidth = 50;
    public void setEraseWidth(int eraseWidth){
        this.eraseWidth = eraseWidth;
    }
    public int getEraseWidth(){
        return eraseWidth;
    }
    public Bitmap getMapToFile(){
        return MapToFile;
    }
    private Canvas mCanvas;
    private Path mPath;
    private Paint mBitmapPaint;
    Context context;
    private Paint circlePaint;
    private Path circlePath;
    public Bitmap getmBitmap(){
        return mBitmap;
    }
    public Paint getmPaint(){return mPaint;}

    private int[] lottery = {
            R.drawable.noprice_1, R.drawable.noprice_2, R.drawable.noprice_3,
            R.drawable.noprice_4, R.drawable.noprice_5, R.drawable.noprice_6,
            R.drawable.noprice_7, R.drawable.price100_1, R.drawable.price100_2,
            R.drawable.price_200, R.drawable.price_500, R.drawable.price_1000
    };



    public static String sdcard = Environment.getExternalStorageDirectory().toString();

    public Drawl(Context c){
        super(c);
        context = c;
        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        mBitmapPaint.setColor(Color.BLUE);

        circlePaint = new Paint();
        circlePath = new Path();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.RED);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeJoin(Paint.Join.MITER);
        circlePaint.setStrokeWidth(4f);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(12);

        erasePaint = new Paint();
        erasePaint.setAntiAlias(true);
        erasePaint.setStyle(Paint.Style.STROKE);
        erasePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        erasePaint.setDither(true);
        erasePaint.setStrokeJoin(Paint.Join.ROUND);
        erasePaint.setStrokeWidth(eraseWidth);

        Log.d("DrawingView", "DrawingView=========================");
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //創建畫布
        //mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mBitmap = CreateBitmap(Color.GRAY, w, h).copy(Bitmap.Config.ARGB_8888, true);
        mCanvas = new Canvas(mBitmap);

        Log.d("OnSizeChanged", "OnSizeChanged=========================");

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //布上底圖
        Bitmap bitmapOrg = BitmapFactory.decodeResource(getResources(), lottery[lotteryNumber]);

        //Bitmap bitmapOrg = BitmapFactory.decodeFile(sdcard + "/checking/testPhoto1.jpg");

        canvas.drawBitmap(bitmapOrg, 10, 10, null);

        //布上透明畫布
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);

        if(isErasable){
            erasePaint.setStrokeWidth(eraseWidth);
            mCanvas.drawPath(mPath, erasePaint);
        }else{
            canvas.drawPath(mPath, mPaint);
        }

        //游標跟著的紅色圈圈
        canvas.drawPath(circlePath, circlePaint);

        MapToFile = doodle(bitmapOrg, mBitmap);

        Log.d("onDraw", "onDraw================================");

    }

    //底圖與上層畫布合成
    public Bitmap doodle(Bitmap bitmap0,Bitmap bitmap1)
    {
        Matrix matrix = new Matrix();

        matrix.postRotate(0); //旋轉角度

        int width = bitmap0.getWidth();

        int height = bitmap0.getHeight();

        bitmap0 = Bitmap.createBitmap(bitmap0, 0, 0, width, height, matrix, true);

        Bitmap newb = Bitmap.createBitmap(bitmap0.getWidth(), bitmap0.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(newb);

        canvas.drawBitmap(bitmap0, 0, 0, null);

        canvas.drawBitmap(bitmap1, 0, 0, null);

        canvas.save(Canvas.ALL_SAVE_FLAG);

        canvas.restore();

        return newb;
    }

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private void touch_start(float x, float y) {
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;

            circlePath.reset();
            circlePath.addCircle(mX, mY, 30, Path.Direction.CW);
        }
    }

    private void touch_up() {
        mPath.lineTo(mX, mY);
        circlePath.reset();

        // commit the path to our offscreen
        //如果不是橡皮擦模式，此印出畫筆
        if(!isErasable) mCanvas.drawPath(mPath, mPaint);

        // kill this so we don't double draw
        mPath.reset();
        Log.d("onDraw", "up================================");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                break;
        }
        return true;
    }

    public Bitmap CreateBitmap(int color, int width, int height) {
        int[] rgb = new int[width * height];

        for (int i = 0; i < rgb.length; i++) {
            rgb[i] = color;
        }

        return Bitmap.createBitmap(rgb, width, height, Bitmap.Config.ARGB_8888);
    }
}

