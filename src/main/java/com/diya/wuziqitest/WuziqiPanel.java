package com.diya.wuziqitest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class WuziqiPanel extends View {

    private Paint mPaint = new Paint();
    private Bitmap white;
    private Bitmap black;
    private int mPanelWidth;
    private float mLineHeight;
    private boolean iswhite = false;
    private final int MaxiLine = 15;
    private List<Point> ptWhite = new ArrayList<>();
    private List<Point> ptBlack = new ArrayList<>();
    private boolean isWin = false;
    private Context context;

    public WuziqiPanel(Context context) {

        super(context);
        this.context = context;
        initView(context);
    }


    public void reStart() {
        isWin = false;
        ptWhite.clear();
        ptBlack.clear();
        invalidate();
    }

    public WuziqiPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView(context);
    }

    public WuziqiPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width = Math.min(widthSize, heightSize);
        if (widthMode == MeasureSpec.UNSPECIFIED) {

            width = heightSize;
        } else if (heightMode == MeasureSpec.UNSPECIFIED) {

            width = widthSize;
        }
        setMeasuredDimension(width, width);
    }

    private void initView(Context context) {
        setBackgroundColor(0x44ff0000);
        mPaint.setColor(0x88000000);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        black = BitmapFactory.decodeResource(getResources(), R.drawable.stone_b1);
        white = BitmapFactory.decodeResource(getResources(), R.drawable.stone_w2);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (isWin) {
                    return true;
                }
                int cx = (int) event.getX();
                int cy = (int) event.getY();
                Point point = getValidPoint(cx, cy);
                if (ptWhite.contains(point) || ptBlack.contains(point)) {

                    return false;
                }
                if (iswhite) {
                    ptWhite.add(point);
                } else {
                    ptBlack.add(point);
                }
                invalidate();
                iswhite = !iswhite;
                break;
        }
        return true;
    }

    private Point getValidPoint(int x, int y) {

        return new Point((int) (x / mLineHeight), (int) (y / mLineHeight));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPanelWidth = w;
        mLineHeight = w * 1.0f / MaxiLine;
        int bwwidth = (int) (mLineHeight / 2);
        black = Bitmap.createScaledBitmap(black, bwwidth, bwwidth, false);
        white = Bitmap.createScaledBitmap(white, bwwidth, bwwidth, false);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBoard(canvas);
        drawQz(canvas);
        boolean iswhiteWin = false;
        boolean isBlackWin = false;
        for (Point p : ptWhite) {
            iswhiteWin = checkIsWin(p, ptWhite);
            if (iswhiteWin) {
                break;
            }
        }
        if (iswhiteWin) {

            isWin = iswhiteWin;
            Toast.makeText(context, "白棋赢", Toast.LENGTH_LONG).show();
            return;
        }
        for (Point p : ptBlack) {
            isBlackWin = checkIsWin(p, ptBlack);
            if (isBlackWin) {
                break;
            }
        }

        if (isBlackWin) {

            isWin = isBlackWin;
            Toast.makeText(context, "黑棋赢", Toast.LENGTH_LONG).show();
        }
    }

    private boolean checkIsWin(Point point, List<Point> ptlist) {

        boolean iswinnow = false;
        iswinnow = checkVertical(point, ptlist);
        if (iswinnow) {
            return true;
        }
        iswinnow = checkHori(point, ptlist);
        if (iswinnow) {
            return true;
        }
        iswinnow = checkLift(point, ptlist);
        if (iswinnow) {
            return true;
        }
        iswinnow = checkRight(point, ptlist);
        if (iswinnow) {
            return true;
        }

        return false;
    }

    private boolean checkRight(Point point, List<Point> ptlist) {
        int count = 1;
        for (int i = 1; i < 5; i++) {

            if (ptlist.contains(new Point(point.x - i, point.y + i))) {

                count++;
            } else {
                break;
            }
            if (count >= 5) {
                return true;
            }


        }
        for (int i = 1; i < 5; i++) {
            if (ptlist.contains(new Point(point.x + i, point.y - i))) {

                count++;
            } else {
                break;
            }
            if (count >= 5) {
                return true;
            }
        }
        return false;
    }

    private boolean checkLift(Point point, List<Point> ptlist) {
        int count = 1;
        for (int i = 1; i < 5; i++) {

            if (ptlist.contains(new Point(point.x - i, point.y - i))) {

                count++;
            } else {
                break;
            }
            if (count >= 5) {
                return true;
            }


        }
        for (int i = 1; i < 5; i++) {
            if (ptlist.contains(new Point(point.x + i, point.y + i))) {

                count++;
            } else {
                break;
            }
            if (count >= 5) {
                return true;
            }
        }
        return false;
    }

    private boolean checkHori(Point point, List<Point> ptlist) {

        int count = 1;
        for (int i = 1; i < 5; i++) {

            if (ptlist.contains(new Point(point.x, point.y - i))) {

                count++;
            } else {
                break;
            }
            if (count >= 5) {
                return true;
            }

        }
        for (int i = 1; i < 5; i++) {
            if (ptlist.contains(new Point(point.x, point.y + i))) {

                count++;
            } else {
                break;
            }
            if (count >= 5) {
                return true;
            }
        }

        return false;
    }

    private boolean checkVertical(Point point, List<Point> ptlist) {
        int count = 1;
        for (int i = 1; i < 5; i++) {

            if (ptlist.contains(new Point(point.x - i, point.y))) {

                count++;
            } else {
                break;
            }
            if (count >= 5) {
                return true;
            }

        }
        for (int i = 1; i < 5; i++) {
            if (ptlist.contains(new Point(point.x + i, point.y))) {

                count++;
            } else {
                break;
            }
            if (count >= 5) {
                return true;
            }
        }

        return false;
    }

    private void drawQz(Canvas canvas) {
        for (int i = 0, n = ptWhite.size(); i < n; i++) {

            Point pw = ptWhite.get(i);
            canvas.drawBitmap(white, (pw.x) * mLineHeight + mLineHeight / 2 - mLineHeight / 2 / 2, (pw.y) * mLineHeight + mLineHeight / 2 - mLineHeight / 2 / 2, null);

        }
        for (int i = 0, n = ptBlack.size(); i < n; i++) {

            Point pw = ptBlack.get(i);
            canvas.drawBitmap(black, (pw.x) * mLineHeight + mLineHeight / 2 - mLineHeight / 2 / 2, (pw.y) * mLineHeight + mLineHeight / 2 - mLineHeight / 2 / 2, null);

        }
    }

    private void drawBoard(Canvas canvas) {
        int w = mPanelWidth;
        final float lineHeight = mLineHeight;
        for (int i = 0; i < MaxiLine; i++) {
            int start = (int) (lineHeight / 2);
            int stop = (int) (w - lineHeight / 2);
            int y = (int) ((i + 0.5) * lineHeight);
            canvas.drawLine(start, y, stop, y, mPaint);
            canvas.drawLine(y, start, y, stop, mPaint);
        }
    }
}
