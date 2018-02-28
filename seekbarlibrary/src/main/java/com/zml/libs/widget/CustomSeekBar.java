package com.zml.libs.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;


/**
 * @Description: 自定义普通进度条
 * @Param:
 * @Return:
 * @Author: zhangliangming
 * @Date: 2018-02-14
 * @Throws:
 */
public class CustomSeekBar extends View {
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
     * 进度
     */
    private int mProgress = 0;
    /**
     * 第二进度
     */
    private int mSecondaryProgress = 0;
    /**
     * 最大值
     */
    private int mMax = 0;
    /**
     * 默认
     */
    private final int TRACKTOUCH_NONE = -1;
    /**
     * 开始拖动
     */
    private final int TRACKTOUCH_START = 0;
    /**
     * 进度改变
     */
    private final int TRACKTOUCH_PROGRESSCHANGED = 1;
    private int mTrackTouch = TRACKTOUCH_NONE;

    private OnChangeListener mOnChangeListener;
    /**
     *
     */
    private boolean isEnabled = true;

    private int mTouchSlop;
    /**
     * X轴和Y最后的位置
     */
    private float mLastX = 0, mLastY = 0;
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
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
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


        if (getMax() != 0 && isEnabled) {
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
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled || getMax() == 0)
            return super.onTouchEvent(event);


        float curX = event.getX();
        float curY = event.getY();

        int actionId = event.getAction();
        switch (actionId) {

            case MotionEvent.ACTION_DOWN:
                mLastX = curX;
                mLastY = curY;

                if (mTrackTouch == TRACKTOUCH_NONE) {
                    setTrackTouch(TRACKTOUCH_START);
                    if (mOnChangeListener != null) {
                        mOnChangeListener.onProgressChanged(this);
                    }
                    invalidateProgress(event);
                }
                break;

            case MotionEvent.ACTION_MOVE:
                int deltaX = (int) (mLastX - curX);
                int deltaY = (int) (mLastY - curY);

                if ((mTrackTouch == TRACKTOUCH_PROGRESSCHANGED) || (Math.abs(deltaX) > mTouchSlop
                        && Math.abs(deltaY) < mTouchSlop)) {
                    //左右移动
                    if (mTrackTouch == TRACKTOUCH_START || mTrackTouch == TRACKTOUCH_PROGRESSCHANGED) {
                        setTrackTouch(TRACKTOUCH_PROGRESSCHANGED);
                        if (mOnChangeListener != null) {
                            mOnChangeListener.onProgressChanged(this);
                        }
                        invalidateProgress(event);
                    }
                }
                break;

            default:
                ;
                if (mTrackTouch == TRACKTOUCH_START || mTrackTouch == TRACKTOUCH_PROGRESSCHANGED) {

                    mHandler.removeCallbacks(mRunnable);
                    mHandler.postDelayed(mRunnable, 200);

                    if (mOnChangeListener != null) {
                        mOnChangeListener.onTrackingTouchFinish(this);
                    }

                } else {
                    setTrackTouch(TRACKTOUCH_NONE);
                }
                invalidateProgress(event);

                break;
        }

        return true;
    }

    /**
     *
     */
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            setTrackTouch(TRACKTOUCH_NONE);
            postInvalidate();
        }
    };

    /**
     * 更新进度
     *
     * @param event
     */
    private void invalidateProgress(MotionEvent event) {
        if (getMax() != 0) {
            int curX = (int) event.getX();
            if (curX < 0) {
                curX = 0;
            }
            if (curX > getWidth()) {
                curX = getWidth();
            }

            int progress = curX * getMax() / getWidth();
            mProgress = Math.min(getMax(), progress);
            invalidate();
        }
    }

    public synchronized void setProgress(int progress) {
        if (mTrackTouch == TRACKTOUCH_NONE && getMax() != 0) {
            mProgress = Math.min(getMax(), progress);
            postInvalidate();
        }
    }

    public synchronized void setSecondaryProgress(int secondaryProgress) {
        this.mSecondaryProgress = secondaryProgress;
        postInvalidate();
    }

    public synchronized void setMax(int max) {
        this.mMax = max;
        postInvalidate();
    }

    public int getProgress() {
        return mProgress;
    }

    public int getSecondaryProgress() {
        return mSecondaryProgress;
    }

    public int getMax() {
        return mMax;
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

    @Override
    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
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
