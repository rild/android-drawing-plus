package rimp.rild.com.android.android_drawing;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * http://jsdo.it/kikuchy/zsWz?order=pv
 * js + html + css
 *
 */
public class MainActivity extends AppCompatActivity {
    private final String TAG = "Drawing";

    private ImageView mImageView; // main
    private Canvas mCanvas;
    private Bitmap mBitmap;
    private Paint mPaint;
    private Path mPath;

    private ArrayList<Path> mPaths = new ArrayList<Path>();

    private int width, height;

    private List<Point> mStrokeHistoryStack;
    private int mMeanNumber = 5;

    private class Point {
        float x, y;

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView = (ImageView) findViewById(R.id.main_canvas);
        mStrokeHistoryStack = new ArrayList<>();

        mPaint = createPaint();

        mPaint.setColor(Color.RED);

        mImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                MainActivity.this.onTouch(view, motionEvent);
                return true;
//                return false;
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        width = mImageView.getWidth();
        height = mImageView.getHeight();
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        mCanvas = new Canvas(mBitmap);
        mCanvas.drawColor(Color.WHITE);

        mImageView.setImageBitmap(mBitmap);
    }

    private void onTouch(View view, MotionEvent motionEvent) {
        float currentX = motionEvent.getX();
        float currentY = motionEvent.getY();

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "down");

                mStrokeHistoryStack.add(new Point(currentX, currentY));
                mPath = new Path();
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "move");
                Point lastHistoryUnit = mStrokeHistoryStack.get(
                        mStrokeHistoryStack.size() - 1);
                mPath.moveTo(lastHistoryUnit.x, lastHistoryUnit.y);
                mPath.lineTo(currentX, currentY);
                mCanvas.drawPath(mPath, mPaint);

                mStrokeHistoryStack.add(new Point(currentX, currentY));
                break;
            case MotionEvent.ACTION_UP:

                Log.d(TAG, "up");
//                mPath.reset();

                mPath = new Path();
                if (mStrokeHistoryStack.size() > mMeanNumber) {
                    mPaint.setColor(Color.GREEN); // change color
                    mPath.moveTo(mStrokeHistoryStack.get(0).x,
                            mStrokeHistoryStack.get(0).y);
                    for (int i = mMeanNumber - 1, j = mStrokeHistoryStack.size(); i < j; i++) {
                        Point avg = movingAverage(
                                mStrokeHistoryStack.subList(i - mMeanNumber + 1, i + 1));
                        mPath.lineTo(avg.x, avg.y);
//                        mCanvas.drawPath(mPath, mPaint);
                    }

                    mPath.lineTo(
                            mStrokeHistoryStack.get(mStrokeHistoryStack.size() - 1).x,
                            mStrokeHistoryStack.get(mStrokeHistoryStack.size() - 1).y);
//                    mCanvas.drawPath(mPath, mPaint);

                }
                mPaths.add(mPath);

                mCanvas.drawColor(Color.WHITE);
                for (Path p: mPaths) {
                    mCanvas.drawPath(p, mPaint);
                }

                mPaint.setColor(Color.RED); // change color
                mStrokeHistoryStack.clear();
                break;
        }

//        firstX = currentX;
//        firstY = currentY;

        mImageView.setImageBitmap(mBitmap);
    }

    private Point movingAverage(List<Point> list) {
        Point ret = new Point(0, 0);
        for (int i = 0; i < mMeanNumber; i++) {
            ret.x += list.get(i).x;
            ret.y += list.get(i).y;
        }
        ret.x = ret.x / mMeanNumber;
        ret.y = ret.y / mMeanNumber;
        return ret;
    }

    private Paint createPaint() {
        Paint paint = new Paint();
        paint.setStrokeWidth(10.0f);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);

        return paint;
    }
}
