package com.nhancv.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by nhancao on 11/17/16.
 */

public class NHoleView extends View {

    private int anchorColor = 0x00000001;
    private int outsideColor = 0xFFCCCCCC;
    private int insideColor = 0x00000000;
    private Drawable anchorDrawable;
    private Paint paint = new Paint();
    private Rect rect = new Rect();
    private Bitmap background;

    public NHoleView(Context context) {
        this(context, null);
    }

    public NHoleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NHoleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray att = context.getTheme().obtainStyledAttributes(attrs, R.styleable._nHoleView, defStyleAttr, 0);

        outsideColor = att.getColor(R.styleable._nHoleView__outsideColor, 0xFFCCCCCC);
        insideColor = att.getColor(R.styleable._nHoleView__insideColor, 0x00000000);
        anchorDrawable = att.getDrawable(R.styleable._nHoleView__anchorDrawable);
        att.recycle();

    }

    public int getAnchorColor() {
        return anchorColor;
    }

    public void setAnchorColor(int anchorColor) {
        this.anchorColor = anchorColor;
    }

    public int getOutsideColor() {
        return outsideColor;
    }

    public void setOutsideColor(int outsideColor) {
        this.outsideColor = outsideColor;
    }

    public int getInsideColor() {
        return insideColor;
    }

    public void setInsideColor(int insideColor) {
        this.insideColor = insideColor;
    }

    public Drawable getAnchorDrawable() {
        return anchorDrawable;
    }

    public void setAnchorDrawable(Drawable anchorDrawable) {
        this.anchorDrawable = anchorDrawable;
    }

    public Rect getRect() {
        return rect;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (background == null || w != background.getWidth() || h != background.getHeight()) {
            if (background != null) background.recycle();
            background = genBg(w, h);

        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (background != null) {
            canvas.drawBitmap(background, 0, 0, paint);
        }
    }

    private Bitmap genBg(int w, int h) {
        if (anchorDrawable == null) return null;
        Bitmap bgView = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bgView);
        Bitmap b = ((BitmapDrawable) anchorDrawable).getBitmap();

        int left = (w - b.getWidth()) / 2;
        int top = (h - b.getHeight()) / 2;
        if (left < 0 || top < 0) {
            Bitmap scaled = Bitmap.createScaledBitmap(b, w, h, false);
            canvas.drawBitmap(scaled, 0, 0, paint);
            rect.set(0, 0, scaled.getWidth(), scaled.getHeight());
            scaled.recycle();
            b.recycle();
        } else {
            canvas.drawBitmap(b, left, top, paint);
            rect.set(left, top, b.getWidth(), b.getHeight());
            b.recycle();
        }

        int imgHeight = bgView.getHeight();
        int imgWidth = bgView.getWidth();
        if (insideColor != Color.TRANSPARENT) {
            if (outsideColor != Color.TRANSPARENT) {
                fillOutsideColor(bgView, imgHeight, imgWidth, outsideColor);
                fillInsideColor(bgView, imgHeight, imgWidth, insideColor);
            } else {
                fillOutsideColor(bgView, imgHeight, imgWidth, anchorColor);
                fillInsideColor(bgView, imgHeight, imgWidth, insideColor);
                fillWitchColor(bgView, imgHeight, imgWidth, Color.TRANSPARENT, anchorColor);
            }
        } else if (outsideColor != Color.TRANSPARENT) {
            fillOutsideColor(bgView, imgHeight, imgWidth, outsideColor);
        }
        return bgView;
    }

    /**
     * Fill color outside anchor view
     *
     * @param bgView
     * @param imgHeight
     * @param imgWidth
     * @param fillColor
     */
    private void fillOutsideColor(Bitmap bgView, int imgHeight, int imgWidth, int fillColor) {
        for (int i = 0; i < imgWidth; i++) {
            for (int j = 0; j < imgHeight; j++) {
                if (bgView.getPixel(i, j) != Color.TRANSPARENT) break;
                bgView.setPixel(i, j, fillColor);
            }
        }
        for (int i = 0; i < imgWidth; i++) {
            for (int j = imgHeight - 1; j >= 0; j--) {
                if (bgView.getPixel(i, j) != Color.TRANSPARENT) break;
                bgView.setPixel(i, j, fillColor);
            }
        }
    }

    /**
     * Fill color inside anchor view
     *
     * @param bgView
     * @param imgHeight
     * @param imgWidth
     * @param fillColor
     */
    private void fillInsideColor(Bitmap bgView, int imgHeight, int imgWidth, int fillColor) {
        fillWitchColor(bgView, imgHeight, imgWidth, fillColor, Color.TRANSPARENT);
    }

    /**
     * Fill with color
     *
     * @param bgView
     * @param imgHeight
     * @param imgWidth
     * @param fillColor
     * @param compareColor
     */
    private void fillWitchColor(Bitmap bgView, int imgHeight, int imgWidth, int fillColor, int compareColor) {
        for (int i = 0; i < imgWidth; i++) {
            for (int j = 0; j < imgHeight; j++) {
                if (bgView.getPixel(i, j) == compareColor) {
                    bgView.setPixel(i, j, fillColor);
                }
            }
        }
    }
}

