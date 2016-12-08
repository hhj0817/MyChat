package com.hhj.mychat.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.hyphenate.util.DensityUtil;
import com.hhj.mychat.R;

public class SlideBar extends View {
    public static final String TAG = "SlideBar";

    private static final String[] SECTIONS = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L"
            , "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private Paint mPaint;

    private float mTextSize;

    private float mBaseLine;

    private int mCurrentIndex = -1;

    private OnSlideChangeListener mOnSlideChangeListener;

    public SlideBar(Context context) {
        this(context, null);
    }

    public SlideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setTextSize(DensityUtil.sp2px(getContext(), 12));
        mPaint.setAntiAlias(true);
        mPaint.setTextAlign(Paint.Align.CENTER);//设置文本居中
        mPaint.setColor(getResources().getColor(R.color.slide_bar_text_color));
    }

    /**
     * layout -> setFrame -> sizeChange -> onSizeChange
     *
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mTextSize = h * 1.0f / SECTIONS.length;
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        float textHeight = fontMetrics.descent - fontMetrics.ascent;
        mBaseLine = mTextSize / 2 + ( textHeight / 2 - fontMetrics.descent);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float x = getWidth() * 1.0f / 2;
        float y = mBaseLine;
        for (int i = 0; i < SECTIONS.length; i++) {
            canvas.drawText(SECTIONS[i], x, y, mPaint);
            y += mTextSize;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setBackgroundResource(R.drawable.bg_slide_bar);
                notifyPositionChange(event);
                break;
            case MotionEvent.ACTION_MOVE:
                notifyPositionChange(event);
                break;
            case MotionEvent.ACTION_UP:
                setBackgroundColor(Color.TRANSPARENT);
                if (mOnSlideChangeListener != null) {
                    mOnSlideChangeListener.onSlideFinish();
                }
                break;
        }
        return true;
    }


    private void notifyPositionChange(MotionEvent event) {
        //找出手指移动时所在的字符
        int index = (int) (event.getY() / mTextSize);
        if (index < 0 || index > SECTIONS.length -1) {
            return;
        }
        //只有位置发生变化才通知外界
        if (index != mCurrentIndex) {
            String firstLetter = SECTIONS[index];
            if (mOnSlideChangeListener != null) {
                mOnSlideChangeListener.onSlideChange(firstLetter);
            }
        }
        mCurrentIndex = index;
    }

    public interface OnSlideChangeListener {
        void onSlideChange(String firstLetter);

        void onSlideFinish();
    }

    public void setOnSlideChangeListener(OnSlideChangeListener l) {
        mOnSlideChangeListener = l;
    }
}
