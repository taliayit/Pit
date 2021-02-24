package com.example.pit;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class Pit extends ViewGroup implements IPit {

    private Paint mPaint;
    private Canvas mCanvas;
    private List<PointF> points;
    private PointF originPoint;
    private int indexTouched = -1;
    private int pointRadius = 10;
    private int viewWidth;
    private int viewHeight;

    public Pit(Context context) {
        super(context);
        this.setWillNotDraw(false);
        init();
    }

    // initialize pit objects
    private void init() {
        mPaint = new Paint();
        points = new ArrayList<>();
        originPoint = new PointF();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // store view dimensions
        viewWidth = w;
        viewHeight = h;

        setAxisOrigin();
        addInitPoints();
    }

    // initialize origin point
    private void setAxisOrigin() {
        if (originPoint.x == 0 && originPoint.y == 0) {
            originPoint.x = viewWidth / 2;
            originPoint.y = viewHeight / 2;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.mCanvas = canvas;

        canvas.drawColor(Color.GRAY); // fill background color

        drawAxes();
        drawLines();
        drawPoints();
    }

    private void drawAxes() {
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(1);

        drawXAxis();
        drawYAxis();
    }

    private void drawXAxis() {
        float startX = 0;
        float startY = viewHeight / 2f;
        float stopX = viewWidth;
        float stopY = viewHeight / 2f;

        mCanvas.drawLine(startX, startY, stopX, stopY, mPaint);
    }

    private void drawYAxis() {
        float startX = viewWidth / 2f;
        float startY = 0;
        float stopX = viewWidth / 2f;
        float stopY = viewHeight;

        mCanvas.drawLine(startX, startY, stopX, stopY, mPaint);
    }

    private void drawPoints() {
        for (PointF p : points) {
            drawPoint(p);
        }
    }

    private void drawLines() {
        if (points.size() > 1) {
            for (int i = 0; i < points.size() - 1; i++) {
                drawLine(points.get(i), points.get(i + 1));
            }
        }
    }

    private void drawPoint(PointF point) {
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setColor(Color.YELLOW);
        mPaint.setStrokeWidth(2 * pointRadius);
        mCanvas.drawPoint(point.x, point.y, mPaint);
    }

    private void drawLine(PointF a, PointF b) {
        mPaint.setColor(Color.BLUE);
        mPaint.setStrokeWidth(10);
        mCanvas.drawLine(a.x, a.y, b.x, b.y, mPaint);
    }

    @Override
    public void addPoint() {
        points.add(new PointF((int) originPoint.x, (int) originPoint.y));
        invalidate();
    }

    @Override
    public void addInitPoints() {
        int xUnit = viewWidth / 6;
        int y = viewHeight / 3;

        // add five different initial points
        points.add(new PointF(xUnit * 1, y));
        points.add(new PointF(xUnit * 2, y));
        points.add(new PointF(xUnit * 3, y));
        points.add(new PointF(xUnit * 4, y));
        points.add(new PointF(xUnit * 5, y));
    }

    @Override
    public View getView() {
        return this;
    }

    // search if coordinates are within a point range
    private int searchClosePoint(int x, int y) {
        int delta = pointRadius;
        // search for close p :
        for (int i = 0; i < points.size(); i++) {
            if (points.get(i).x > x - delta
                    && points.get(i).x < x + delta
                    && points.get(i).y > y - delta
                    && points.get(i).y < y + delta)
                return i;
        }
        return -1;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_UP:
                if (indexTouched != -1) {
                    indexTouched = -1;
                }
                break;

            case MotionEvent.ACTION_DOWN:
                int pointIndex = searchClosePoint(x, y);
                if (pointIndex != -1) {  //point touched
                    indexTouched = pointIndex;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (indexTouched != -1 && isPointInsideCanvas(x, y)) {    // change point coords
                    points.get(indexTouched).x = x;
                    points.get(indexTouched).y = y;
                    invalidate();
                }
                break;
        }
        return true;
    }

    // check if a received point is not out of bounds
    private boolean isPointInsideCanvas(int x, int y) {
        return x < viewWidth - pointRadius
                && x > pointRadius
                && y < viewHeight - pointRadius
                && y > pointRadius;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
