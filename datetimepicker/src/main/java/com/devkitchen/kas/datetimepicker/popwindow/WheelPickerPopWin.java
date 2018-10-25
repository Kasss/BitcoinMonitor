package com.devkitchen.kas.datetimepicker.popwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.devkitchen.kas.datetimepicker.LoopScrollListener;
import com.devkitchen.kas.datetimepicker.LoopView;
import com.devkitchen.kas.datetimepicker.R;

public class WheelPickerPopWin extends PopupWindow implements View.OnClickListener {
    public TextView centerTextView;
    public Button cancelBtn;
    public Button confirmBtn;
    public LoopView wheelLoopView;
    public View pickerContainerV;
    public View contentView;//root view

    private int wheelPos = 0;
    private Context mContext;
    private String textCenterTextView;
    private String textCancel;
    private String textConfirm;
    private List<String> wheelList;
    private int colorCancel;
    private int colorConfirm;
    private int btnTextsize;//text btnTextsize of cancel and confirm button
    private int viewTextSize;

    public static class Builder{

        //Required
        private Context context;
        private WheelPickerPopWin.OnWheelPickedListener listener;
        public Builder(Context context, WheelPickerPopWin.OnWheelPickedListener listener){
            this.context = context;
            this.listener = listener;
        }

        //Option
        private List<String> wheelList = new ArrayList();
        private String textCenterTextView = "";
        private String textCancel = "Cancel";
        private String textConfirm = "Confirm";
        private int itemChose = 0;
        private int colorCancel = Color.parseColor("#999999");
        private int colorConfirm = Color.parseColor("#6a296d");
        private int btnTextSize = 16;//text btnTextsize of cancel and confirm button
        private int viewTextSize = 25;

        public WheelPickerPopWin.Builder textCenterTextView(String textCenterTextView){
            this.textCenterTextView = textCenterTextView;
            return this;
        }

        public WheelPickerPopWin.Builder textCancel(String textCancel){
            this.textCancel = textCancel;
            return this;
        }

        public WheelPickerPopWin.Builder textConfirm(String textConfirm){
            this.textConfirm = textConfirm;
            return this;
        }

        public WheelPickerPopWin.Builder wheelList(ArrayList<String> wheelList){
            this.wheelList = wheelList;
            return this;
        }

        public WheelPickerPopWin.Builder itemChose(int itemChose){
            this.itemChose = itemChose;
            return this;
        }

        public WheelPickerPopWin.Builder colorCancel(int colorCancel){
            this.colorCancel = colorCancel;
            return this;
        }

        public WheelPickerPopWin.Builder colorConfirm(int colorConfirm){
            this.colorConfirm = colorConfirm;
            return this;
        }

        /**
         * set btn text btnTextSize
         * @param textSize dp
         */
        public WheelPickerPopWin.Builder btnTextSize(int textSize){
            this.btnTextSize = textSize;
            return this;
        }

        public WheelPickerPopWin.Builder viewTextSize(int textSize){
            this.viewTextSize = textSize;
            return this;
        }

        public WheelPickerPopWin build(){
            return new WheelPickerPopWin(this);
        }
    }

    public WheelPickerPopWin(WheelPickerPopWin.Builder builder) {
        this.wheelList = builder.wheelList;
        this.textCenterTextView = builder.textCenterTextView;
        this.textCancel = builder.textCancel;
        this.textConfirm = builder.textConfirm;
        this.mContext = builder.context;
        this.mListener = builder.listener;
        this.colorCancel = builder.colorCancel;
        this.colorConfirm = builder.colorConfirm;
        this.btnTextsize = builder.btnTextSize;
        this.viewTextSize = builder.viewTextSize;
        setSelected(builder.itemChose);
        initView();
    }

    private WheelPickerPopWin.OnWheelPickedListener mListener;

    private void initView() {
        contentView = LayoutInflater.from(mContext).inflate(R.layout.layout_wheel_picker, null);
        centerTextView = contentView.findViewById(R.id.centerTextView);
        centerTextView.setText(textCenterTextView);
        cancelBtn = (Button) contentView.findViewById(R.id.btn_cancel);
        cancelBtn.setTextColor(colorCancel);
        cancelBtn.setTextSize(btnTextsize);
        confirmBtn = (Button) contentView.findViewById(R.id.btn_confirm);
        confirmBtn.setTextColor(colorConfirm);
        confirmBtn.setTextSize(btnTextsize);
        wheelLoopView = (LoopView) contentView.findViewById(R.id.picker_wheel);
        wheelLoopView.setTextSize(viewTextSize);

        pickerContainerV = contentView.findViewById(R.id.container_picker);

//        //do not loop,default can loop
//        yearLoopView.setNotLoop();
//        monthLoopView.setNotLoop();
//        dayLoopView.setNotLoop();
//
//        //set loopview text size
//        yearLoopView.setTextSize(25);
//        monthLoopView.setTextSize(25);
//        dayLoopView.setTextSize(25);

        //set checked listen
        wheelLoopView.setLoopListener(new LoopScrollListener() {
            @Override
            public void onItemSelect(int item) {
                wheelPos = item;

            }
        });

        initPickerView(); // init year and month loop view

        cancelBtn.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);
        contentView.setOnClickListener(this);

        if(!TextUtils.isEmpty(textConfirm)){
            confirmBtn.setText(textConfirm);
        }

        if(!TextUtils.isEmpty(textCancel)){
            cancelBtn.setText(textCancel);
        }

        setTouchable(true);
        setFocusable(true);
        // setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());
        setAnimationStyle(R.style.FadeInPopWin);
        setContentView(contentView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
    }

    /**
     * Init year and month loop view,
     * Let the day loop view be handled separately
     */
    private void initPickerView() {
        wheelLoopView.setDataList((ArrayList) wheelList);
        wheelLoopView.setInitPosition(wheelPos);
    }

    /**
     * set selected item position value when initView.
     *
     * @param itemChose
     */
    public void setSelected(int itemChose) {
        wheelPos = itemChose;
    }

    /**
     * Show date picker popWindow
     *
     * @param activity
     */
    public void showPopWin(Activity activity) {

        if (null != activity) {

            TranslateAnimation trans = new TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF,
                    0, Animation.RELATIVE_TO_SELF, 1,
                    Animation.RELATIVE_TO_SELF, 0);

            showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM,
                    0, 0);
            trans.setDuration(400);
            trans.setInterpolator(new AccelerateDecelerateInterpolator());

            pickerContainerV.startAnimation(trans);
        }
    }

    /**
     * Dismiss date picker popWindow
     */
    public void dismissPopWin() {

        TranslateAnimation trans = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1);

        trans.setDuration(400);
        trans.setInterpolator(new AccelerateInterpolator());
        trans.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                dismiss();
            }
        });

        pickerContainerV.startAnimation(trans);
    }

    @Override
    public void onClick(View v) {

        if (v == contentView || v == cancelBtn) {

            dismissPopWin();
        } else if (v == confirmBtn) {

            if (null != mListener) {
                mListener.onWheelPickCompleted(wheelList.get(wheelPos));
            }

            dismissPopWin();
        }
    }

    /**
     * Transform int to String with prefix "0" if less than 10
     * @param num
     * @return
     */
    public static String format2LenStr(int num) {

        return (num < 10) ? "0" + num : String.valueOf(num);
    }

    public static int spToPx(Context context, int spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    public interface OnWheelPickedListener {

        /**
         * Listener when date has been checked
         *
         * @param value  String
         */
        void onWheelPickCompleted(String value);
    }
}
