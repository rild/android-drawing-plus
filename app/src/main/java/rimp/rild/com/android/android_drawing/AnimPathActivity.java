package rimp.rild.com.android.android_drawing;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by rild on 2017/08/09.
 *
 * https://stackoverflow.com/questions/12037709/how-to-draw-a-path-on-an-android-canvas-with-animation
 */

public class AnimPathActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new HeartbeatView(this));
    }

    public static class HeartbeatView extends View {

        private static Paint paint;
        private int screenW, screenH;
        private float X, Y;
        private Path path;
        private float initialScreenW;
        private float initialX, plusX;
        private float TX;
        private boolean translate;
        private int flash;
        private Context context;


        public HeartbeatView(Context context) {
            super(context);

            this.context=context;

            paint = new Paint();
            paint.setColor(Color.argb(0xff, 0x99, 0x00, 0x00));
            paint.setStrokeWidth(10);
            paint.setAntiAlias(true);
            paint.setStrokeCap(Paint.Cap.ROUND);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStyle(Paint.Style.STROKE);
            paint.setShadowLayer(7, 0, 0, Color.RED);


            path= new Path();
            TX=0;
            translate=false;

            flash=0;

        }

        @Override
        public void onSizeChanged (int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);

            screenW = w;
            screenH = h;
            X = 0;
            Y = (screenH/2)+(screenH/4)+(screenH/10);

            initialScreenW=screenW;
            initialX=((screenW/2)+(screenW/4));
            plusX=(screenW/24);

            path.moveTo(X, Y);

        }



        @Override
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            //canvas.save();


            flash+=1;
            if(flash<10 || (flash>20 && flash<30))
            {
                paint.setStrokeWidth(16);
                paint.setColor(Color.RED);
                paint.setShadowLayer(12, 0, 0, Color.RED);
            }
            else
            {
                paint.setStrokeWidth(10);
                paint.setColor(Color.argb(0xff, 0x99, 0x00, 0x00));
                paint.setShadowLayer(7, 0, 0, Color.RED);
            }

            if(flash==100)
            {
                flash=0;
            }

            path.lineTo(X,Y);
            canvas.translate(-TX, 0);
            if(translate==true)
            {
                TX+=4;
            }

            if(X<initialX)
            {
                X+=8;
            }
            else
            {
                if(X<initialX+plusX)
                {
                    X+=2;
                    Y-=8;
                }
                else
                {
                    if(X<initialX+(plusX*2))
                    {
                        X+=2;
                        Y+=14;
                    }
                    else
                    {
                        if(X<initialX+(plusX*3))
                        {
                            X+=2;
                            Y-=12;
                        }
                        else
                        {
                            if(X<initialX+(plusX*4))
                            {
                                X+=2;
                                Y+=6;
                            }
                            else
                            {
                                if(X<initialScreenW)
                                {
                                    X+=8;
                                }
                                else
                                {
                                    translate=true;
                                    initialX=initialX+initialScreenW;
                                }
                            }
                        }
                    }
                }

            }

            canvas.drawPath(path, paint);


            //canvas.restore();

            invalidate();
        }
    }
}
