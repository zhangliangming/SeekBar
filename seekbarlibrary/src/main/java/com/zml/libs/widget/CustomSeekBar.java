package com.zml.libs.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.SeekBar;


/**
 * @Description: 自定义普通进度条
 * @Param:
 * @Return:
 * @Author: zhangliangming
 * @Date: 2018-02-14
 * @Throws:
 */
public class CustomSeekBar extends SeekBar {
    /**
     * 背景画笔
     */
    private Paint mBackgroundPaint;

    /**
     * 进度画笔
     */
    private Paint mProgressPaint;

    /**
     * 第二进度画笔
     */
    private Paint mSecondProgressPaint;

    /**
     * 游标画笔
     */
    private Paint mThumbPaint;

    /**
     * 默认
     */
    private final int TRACKTOUCH_NONE = -1;
    /**
     * 开始拖动
     */
    private final int TRACKTOUCH_START = 0;
    private int mTrackTouch = TRACKTOUCH_NONE;

    private OnChangeListener mOnChangeListener;

    /**
     *
     */
    private Handler mHandler = new Handler();


    public CustomSeekBar(Context context) {
        super(context);
        init(context);
    }

    public CustomSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * 初始化
     */
    private void init(Context context) {
        setBackgroundColor(Color.TRANSPARENT);
        //
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setDither(true);
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setColor(Color.parseColor("#e5e5e5"));

        //
        mProgressPaint = new Paint();
        mProgressPaint.setDither(true);
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setColor(Color.parseColor("#0288d1"));

        //
        mSecondProgressPaint = new Paint();
        mSecondProgressPaint.setDither(true);
        mSecondProgressPaint.setAntiAlias(true);
        mSecondProgressPaint.setColor(Color.parseColor("#b8b8b8"));

        //
        mThumbPaint = new Paint();
        mThumbPaint.setDither(true);
        mThumbPaint.setAntiAlias(true);
        mThumbPaint.setColor(Color.parseColor("#0288d1"));

        //
        setThumb(new BitmapDrawable());
        setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (mTrackTouch == TRACKTOUCH_START) {
                    if (mOnChangeListener != null) {
                        mOnChangeListener.onProgressChanged(CustomSeekBar.this);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (mTrackTouch == TRACKTOUCH_NONE) {
                    setTrackTouch(TRACKTOUCH_START);
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mTrackTouch == TRACKTOUCH_START) {
                    setTrackTouch(TRACKTOUCH_NONE);

                    if (mOnChangeListener != null) {
                        mOnChangeListener.onTrackingTouchFinish(CustomSeekBar.this);
                    }
                }
            }
        });
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        int rSize = getHeight() / 4;
        if (mTrackTouch != TRACKTOUCH_NONE) {
            rSize = getHeight() / 3;
        }
        int height = getHeight() / 4 / 3;
        int leftPadding = rSize;

        if (getProgress() > 0) {
            leftPadding = 0;
        }

        RectF backgroundRect = new RectF(leftPadding, getHeight() / 2 - height, getWidth(),
                getHeight() / 2 + height);
        canvas.drawRoundRect(backgroundRect, rSize, rSize, mBackgroundPaint);


        if (getMax() != 0) {
            RectF secondProgressRect = new RectF(leftPadding, getHeight() / 2 - height,
                    getSecondaryProgress() * getWidth() / getMax(), getHeight()
                    / 2 + height);
            canvas.drawRoundRect(secondProgressRect, rSize, rSize, mSecondProgressPaint);

            RectF progressRect = new RectF(leftPadding, getHeight() / 2 - height,
                    getProgress() * getWidth() / getMax(), getHeight() / 2
                    + height);
            canvas.drawRoundRect(progressRect, rSize, rSize, mProgressPaint);


            int cx = getProgress() * getWidth() / getMax();
            if ((cx + rSize) > getWidth()) {
                cx = getWidth() - rSize;
            } else {
                cx = Math.max(cx, rSize);
            }
            int cy = getHeight() / 2;
            canvas.drawCircle(cx, cy, rSize, mThumbPaint);
        }
    }

    @Override
    public synchronized void setProgress(int progress) {
        if (mTrackTouch == TRACKTOUCH_NONE && getMax() != 0) {
            super.setProgress(progress);
        }
    }

    private void setTrackTouch(int trackTouch) {
        this.mTrackTouch = trackTouch;
    }

    /**
     * 设置背景颜色
     *
     * @param backgroundColor
     */
    public void setBackgroundPaintColor(int backgroundColor) {
        mBackgroundPaint.setColor(backgroundColor);
        postInvalidate();
    }

    /**
     * 设置进度颜色
     *
     * @param progressColor
     */
    public void setProgressColor(int progressColor) {
        mProgressPaint.setColor(progressColor);
        postInvalidate();
    }

    /**
     * 设置第二进度颜色
     *
     * @param secondProgressColor
     */
    public void setSecondProgressColor(int secondProgressColor) {
        mSecondProgressPaint.setColor(secondProgressColor);
        postInvalidate();
    }

    /**
     * 设置游标颜色
     *
     * @param thumbColor
     */
    public void setThumbColor(int thumbColor) {
        mThumbPaint.setColor(thumbColor);
        postInvalidate();
    }

    public void setOnChangeListener(OnChangeListener onChangeListener) {
        this.mOnChangeListener = onChangeListener;
    }

    public interface OnChangeListener {
        /**
         * 进度改变
         *
         * @param seekBar
         */
        void onProgressChanged(CustomSeekBar seekBar);

        /**
         * 拖动结束
         *
         * @param seekBar
         */
        void onTrackingTouchFinish(CustomSeekBar seekBar);

    }
}
