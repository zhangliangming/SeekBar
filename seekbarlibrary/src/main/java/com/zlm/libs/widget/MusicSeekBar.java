package com.zlm.libs.widget;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zlm.libs.seekbarlibrary.R;


/**
 * @Description: 自定义音乐进度条
 * @Param:
 * @Return:
 * @Author: zhangliangming
 * @Date: 2018-02-14
 * @Throws:
 */

public class MusicSeekBar extends CustomSeekBar {
    /**
     * 滑动事件监听
     */
    private OnChangeListener mOnChangeListener;
    /**
     * 音乐滑动事件监听
     */
    private OnMusicListener mOnMusicListener;

    /**
     * 时间窗口
     */
    private PopupWindow mTimePopupWindow;
    private LinearLayout mTimePopupWindowView;
    private int mTimePopupWindowViewColor = parserColor("#0288d1", 180);

    /**
     * 时间和歌词窗口
     */
    private PopupWindow mTimeAndLrcPopupWindow;
    private LinearLayout mTimeAndLrcPopupWindowView;
    private int mTimeAndLrcPopupWindowViewColor = parserColor("#0288d1", 180);

    /**
     * 时间标签view
     */
    private TextView mTimeTextView;

    /**
     * 歌词标签view
     */
    private TextView mLrcTextView;
    private Context mContext;

    private final int SHOWTIMEANDLRCVIEW = 0;
    private final int SHOWTIMEVIEW = 1;
    private final int HIDEVIEW = 2;
    private final int UPDATEVIEW = 3;

    /**
     *
     */
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case SHOWTIMEANDLRCVIEW:

                    showTimeAndLrcDialog();

                    break;
                case SHOWTIMEVIEW:

                    showTimeDialog();

                    break;
                case HIDEVIEW:

                    hideDialog();

                    break;
                case UPDATEVIEW:

                    upDateDialog();

                    break;
                default:
                    break;
            }

        }
    };


    public MusicSeekBar(Context context) {
        super(context);
        init(context);
    }

    public MusicSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * 初始化
     */
    private void init(Context context) {
        this.mContext = context;
        mOnChangeListener = new OnChangeListener() {
            @Override
            public void onProgressChanged(CustomSeekBar seekBar) {

                String timeText = null;
                String lrcText = null;
                if (mOnMusicListener != null) {
                    timeText = mOnMusicListener.getTimeText();
                    lrcText = mOnMusicListener.getLrcText();
                }
                if (timeText != null && !timeText.equals("") && lrcText != null && !lrcText.equals("")) {
                    mHandler.sendEmptyMessage(SHOWTIMEANDLRCVIEW);
                } else if (timeText != null && !timeText.equals("")) {
                    mHandler.sendEmptyMessage(SHOWTIMEVIEW);
                }
                mHandler.sendEmptyMessage(UPDATEVIEW);

                if (mOnMusicListener != null) {
                    mOnMusicListener.onProgressChanged(MusicSeekBar.this);
                }
            }

            @Override
            public void onTrackingTouchStart(CustomSeekBar seekBar) {
                if (mOnMusicListener != null) {
                    mOnMusicListener.onTrackingTouchStart(MusicSeekBar.this);
                }
            }

            @Override
            public void onTrackingTouchFinish(CustomSeekBar seekBar) {
                mHandler.sendEmptyMessageDelayed(HIDEVIEW, 200);
                if (mOnMusicListener != null) {
                    mOnMusicListener.onTrackingTouchFinish(MusicSeekBar.this);
                }
            }
        };
        setOnChangeListener(mOnChangeListener);
    }

    /**
     * 显示时间标签窗口
     */
    private void showTimeDialog() {
        if (mTimeAndLrcPopupWindow != null && mTimeAndLrcPopupWindow.isShowing()) {
            mTimeAndLrcPopupWindow.dismiss();
        }

        //
        int popHeight = (int) mContext.getResources().getDimension(R.dimen.pop_height);
        //
        if (mTimePopupWindow == null) {

            mTimeTextView = new TextView(mContext);

            int timeWidth = (int) (mTimeTextView.getTextSize()) * "00:00".length();
            //
            LinearLayout.LayoutParams popLayout = new LinearLayout.LayoutParams(timeWidth, popHeight);
            mTimePopupWindowView = new LinearLayout(mContext);
            mTimePopupWindowView.setLayoutParams(popLayout);


            //

            LinearLayout.LayoutParams timeLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

            mTimeTextView.setLayoutParams(timeLayout);
            mTimeTextView.setTextColor(Color.WHITE);
            mTimeTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER | Gravity.CENTER_HORIZONTAL);
            mTimeTextView.setSingleLine(true);
            mTimeTextView.setEllipsize(TextUtils.TruncateAt.END);
            mTimePopupWindowView.addView(mTimeTextView);

            mTimePopupWindow = new PopupWindow(mTimePopupWindowView, timeWidth,
                    popHeight, true);

        }
        //////////////////////////

        int strokeWidth = 1; // 3dp 边框宽度
        float[] roundRadius = {15, 15, 15, 15, 15, 15, 15, 15}; // 圆角半径
        int strokeColor = Color.TRANSPARENT;

        GradientDrawable gd = new GradientDrawable();// 创建drawable
        gd.setColor(mTimePopupWindowViewColor);
        gd.setCornerRadii(roundRadius);
        gd.setStroke(strokeWidth, strokeColor);

        ///////////////////////////////
        mTimePopupWindowView.setBackgroundDrawable(gd);

        if (mTimePopupWindow != null && !mTimePopupWindow.isShowing()) {
            int[] location = new int[2];
            this.getLocationOnScreen(location);


            int leftX = (int) (location[0] + getWidth() * (float)getProgress() / getMax());
            //判断是否越界
            if ((leftX + mTimePopupWindow.getWidth()) > (location[0] + getWidth())) {
                leftX = (location[0] + getWidth()) - mTimePopupWindow.getWidth();
            } else if (leftX < location[0]) {
                leftX = location[0];
            }

            mTimePopupWindow.showAtLocation(this, Gravity.NO_GRAVITY, leftX, location[1]
                    - mTimePopupWindow.getHeight() * 3 / 2);
        }
    }

    /**
     * 显示时间标签和歌词标签窗口
     */
    private void showTimeAndLrcDialog() {
        if (mTimePopupWindow != null && mTimePopupWindow.isShowing()) {
            mTimePopupWindow.dismiss();
        }
        //
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int screenWidth = display.getWidth();
        //
        int popHeight = (int) mContext.getResources().getDimension(R.dimen.pop_height);
        int popPadding = (int) mContext.getResources().getDimension(R.dimen.pop_Padding);
        //
        if (mTimeAndLrcPopupWindow == null) {

            //
            LinearLayout.LayoutParams popLayout = new LinearLayout.LayoutParams(screenWidth - popPadding * 2, popHeight);
            mTimeAndLrcPopupWindowView = new LinearLayout(mContext);
            mTimeAndLrcPopupWindowView.setLayoutParams(popLayout);


            //
            mTimeTextView = new TextView(mContext);
            LinearLayout.LayoutParams timeLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            timeLayout.leftMargin = popPadding;
            mTimeTextView.setLayoutParams(timeLayout);
            mTimeTextView.setTextColor(Color.WHITE);
            mTimeTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER | Gravity.CENTER_HORIZONTAL);
            mTimeTextView.setSingleLine(true);
            mTimeTextView.setEllipsize(TextUtils.TruncateAt.END);
            mTimeAndLrcPopupWindowView.addView(mTimeTextView);

            //
            mLrcTextView = new TextView(mContext);
            LinearLayout.LayoutParams lrcLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            mLrcTextView.setLayoutParams(lrcLayout);
            mLrcTextView.setTextColor(Color.WHITE);
            mLrcTextView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER | Gravity.CENTER_HORIZONTAL);
            mLrcTextView.setSingleLine(true);
            mLrcTextView.setEllipsize(TextUtils.TruncateAt.END);
            mTimeAndLrcPopupWindowView.addView(mLrcTextView);

            mTimeAndLrcPopupWindow = new PopupWindow(mTimeAndLrcPopupWindowView, screenWidth - popPadding * 2,
                    popHeight, true);

        }
        //////////////////////////

        int strokeWidth = 1; // 3dp 边框宽度
        float[] roundRadius = {15, 15, 15, 15, 15, 15, 15, 15}; // 圆角半径
        int strokeColor = Color.TRANSPARENT;

        GradientDrawable gd = new GradientDrawable();// 创建drawable
        gd.setColor(mTimeAndLrcPopupWindowViewColor);
        gd.setCornerRadii(roundRadius);
        gd.setStroke(strokeWidth, strokeColor);

        ///////////////////////////////
        mTimeAndLrcPopupWindowView.setBackgroundDrawable(gd);

        if (mTimeAndLrcPopupWindow != null && !mTimeAndLrcPopupWindow.isShowing()) {
            int[] location = new int[2];
            this.getLocationOnScreen(location);

            mTimeAndLrcPopupWindow.showAtLocation(this, Gravity.NO_GRAVITY, popPadding, location[1]
                    - mTimeAndLrcPopupWindow.getHeight() * 3 / 2);
        }

    }

    /**
     * 隐藏窗口
     */
    private void hideDialog() {
        if (mTimeAndLrcPopupWindow != null && mTimeAndLrcPopupWindow.isShowing()) {
            mTimeAndLrcPopupWindow.dismiss();
        }

        if (mTimePopupWindow != null && mTimePopupWindow.isShowing()) {
            mTimePopupWindow.dismiss();
        }
    }

    /**
     * 更新窗口
     */
    private void upDateDialog() {

        if ((mTimeAndLrcPopupWindow != null && mTimeAndLrcPopupWindow.isShowing()) || (mTimePopupWindow != null && mTimePopupWindow.isShowing())) {

            String timeText = null;
            String lrcText = null;
            if (mOnMusicListener != null) {
                timeText = mOnMusicListener.getTimeText();
                lrcText = mOnMusicListener.getLrcText();
            }
            if (timeText == null) {
                return;
            }

            mTimeTextView.setText(timeText);


            //如果时间窗口正在显示
            if (mTimePopupWindow != null && mTimePopupWindow.isShowing()) {
                int[] location = new int[2];
                this.getLocationOnScreen(location);

                int leftX = (int) (location[0] + getWidth() * (float)getProgress() / getMax() - mTimePopupWindow.getWidth() / 2);

                //判断是否越界
                if ((leftX + mTimePopupWindow.getWidth()) > (location[0] + getWidth())) {
                    leftX = (location[0] + getWidth()) - mTimePopupWindow.getWidth();
                } else if (leftX < location[0]) {
                    leftX = location[0];
                }

                //更新弹出窗口的位置
                mTimePopupWindow.update(leftX, location[1]
                        - mTimePopupWindow.getHeight() * 3 / 2, -1, -1);
            }

            if (lrcText == null) {

                return;
            }

            mLrcTextView.setText(lrcText);
        }
    }


    public void setOnMusicListener(OnMusicListener onMusicListener) {
        this.mOnMusicListener = onMusicListener;
    }

    /**
     * 解析颜色
     *
     * @param colorStr #ffffff 颜色字符串
     * @param alpha    0-255 透明度
     * @return
     */
    private int parserColor(String colorStr, int alpha) {
        int color = Color.parseColor(colorStr);
        int red = (color & 0xff0000) >> 16;
        int green = (color & 0x00ff00) >> 8;
        int blue = (color & 0x0000ff);
        return Color.argb(alpha, red, green, blue);
    }

    public void setTimePopupWindowViewColor(int mTimePopupWindowViewColor) {
        this.mTimePopupWindowViewColor = mTimePopupWindowViewColor;
    }

    public void setTimeAndLrcPopupWindowViewColor(int mTimeAndLrcPopupWindowViewColor) {
        this.mTimeAndLrcPopupWindowViewColor = mTimeAndLrcPopupWindowViewColor;
    }

    /**
     * 音乐进度条监听事件
     */
    public interface OnMusicListener {
        /**
         * 获取时间标签
         *
         * @return
         */
        public String getTimeText();

        /**
         * 获取歌词标签
         *
         * @return
         */
        public String getLrcText();

        /**
         * 进度改变
         *
         * @param seekBar
         */
        public void onProgressChanged(MusicSeekBar seekBar);

        /**
         * 开始拖动
         *
         * @param seekBar
         */
        public void onTrackingTouchStart(MusicSeekBar seekBar);


        /**
         * 拖动结束
         *
         * @param seekBar
         */
        public void onTrackingTouchFinish(MusicSeekBar seekBar);
    }
}
