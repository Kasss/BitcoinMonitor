package com.devkitchen.kas.datetimepicker.popwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.PopupWindow;

import com.devkitchen.kas.datetimepicker.LoopScrollListener;
import com.devkitchen.kas.datetimepicker.LoopView;
import com.devkitchen.kas.datetimepicker.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * PopWindow for Date Pick
 */
public class DatePickerPopWin extends PopupWindow implements OnClickListener {

    private static final int DEFAULT_MIN_YEAR = 1900;
    public Button cancelBtn;
    public Button confirmBtn;
    public LoopView yearLoopView;
    public LoopView monthLoopView;
    public LoopView dayLoopView;
    public View pickerContainerV;
    public View contentView;//root view

    private int minYear; // min year
    private int maxYear; // max year
    private Calendar minDate;
    private Calendar maxDate;
    private int yearPos = 0;
    private int monthPos = 0;
    private int dayPos = 0;
    private Context mContext;
    private String textCancel;
    private String textConfirm;
    private int colorCancel;
    private int colorConfirm;
    private int btnTextsize;//text btnTextsize of cancel and confirm button
    private int viewTextSize;
    private boolean showDayMonthYear;

    List<String> yearList = new ArrayList();
    List<String> monthList = new ArrayList();
    List<String> monthListStr = new ArrayList();
    List<String> dayList = new ArrayList();

    public static class Builder{

        //Required
        private Context context;
        private OnDatePickedListener listener;
        public Builder(Context context,OnDatePickedListener listener){
            this.context = context;
            this.listener = listener;
        }

        //Option
        private boolean showDayMonthYear = false;
        private int minYear = DEFAULT_MIN_YEAR;
        private int maxYear = Calendar.getInstance().get(Calendar.YEAR);
        private Calendar minDate = Calendar.getInstance();
        private Calendar maxDate = Calendar.getInstance();
        private String textCancel = "Cancel";
        private String textConfirm = "Confirm";
        private Calendar dateChose = Calendar.getInstance();
        private int colorCancel = Color.parseColor("#999999");
        private int colorConfirm = Color.parseColor("#6a296d");
        private int btnTextSize = 16;//text btnTextsize of cancel and confirm button
        private int viewTextSize = 25;

        public Builder minYear(int minYear){
            this.minYear = minYear;
            return this;
        }

        public Builder maxYear(int maxYear){
            this.maxYear = maxYear;
            return this;
        }

        public Builder minDate(Calendar minDate){
            this.minDate = (Calendar) minDate.clone();
            return this;
        }

        public Builder maxDate(Calendar maxDate){
            this.maxDate = (Calendar) maxDate.clone();
            return this;
        }

        public Builder textCancel(String textCancel){
            this.textCancel = textCancel;
            return this;
        }

        public Builder textConfirm(String textConfirm){
            this.textConfirm = textConfirm;
            return this;
        }

        public Builder dateChose(Calendar dateChose){
            this.dateChose = dateChose;
            return this;
        }

        public Builder colorCancel(int colorCancel){
            this.colorCancel = colorCancel;
            return this;
        }

        public Builder colorConfirm(int colorConfirm){
            this.colorConfirm = colorConfirm;
            return this;
        }

        /**
         * set btn text btnTextSize
         * @param textSize dp
         */
        public Builder btnTextSize(int textSize){
            this.btnTextSize = textSize;
            return this;
        }

        public Builder viewTextSize(int textSize){
            this.viewTextSize = textSize;
            return this;
        }

        public DatePickerPopWin build(){
            if(minYear > maxYear){
                throw new IllegalArgumentException();
            }
            return new DatePickerPopWin(this);
        }

        public Builder showDayMonthYear(boolean useDayMonthYear) {
            this.showDayMonthYear = useDayMonthYear;
            return this;
        }
    }

    public DatePickerPopWin(Builder builder){
        this.minYear = builder.minYear;
        this.maxYear = builder.maxYear;
        this.minDate = builder.minDate;
        this.maxDate = builder.maxDate;
        this.textCancel = builder.textCancel;
        this.textConfirm = builder.textConfirm;
        this.mContext = builder.context;
        this.mListener = builder.listener;
        this.colorCancel = builder.colorCancel;
        this.colorConfirm = builder.colorConfirm;
        this.btnTextsize = builder.btnTextSize;
        this.viewTextSize = builder.viewTextSize;
        this.showDayMonthYear = builder.showDayMonthYear;
        setSelectedDate(builder.dateChose);
        initView();
    }

    private OnDatePickedListener mListener;

    private void initView() {

        contentView = LayoutInflater.from(mContext).inflate(showDayMonthYear ? R.layout.layout_date_picker_inverted : R.layout.layout_date_picker, null);
        cancelBtn = (Button) contentView.findViewById(R.id.btn_cancel);
        cancelBtn.setTextColor(colorCancel);
        cancelBtn.setTextSize(btnTextsize);
        confirmBtn = (Button) contentView.findViewById(R.id.btn_confirm);
        confirmBtn.setTextColor(colorConfirm);
        confirmBtn.setTextSize(btnTextsize);
        yearLoopView = (LoopView) contentView.findViewById(R.id.picker_year);
        yearLoopView.setTextSize(viewTextSize);
        monthLoopView = (LoopView) contentView.findViewById(R.id.picker_month);
        monthLoopView.setTextSize(viewTextSize);
        dayLoopView = (LoopView) contentView.findViewById(R.id.picker_day);
        dayLoopView.setTextSize(viewTextSize);

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
        yearLoopView.setLoopListener(new LoopScrollListener() {
            @Override
            public void onItemSelect(int item) {
                Calendar wheelDate = (Calendar) minDate.clone();
                wheelDate.set(minYear + item, monthPos, dayPos + 1);

                if(minDate.compareTo(wheelDate) <= 0) {
                    if(maxDate.compareTo(wheelDate) >= 0) {
                        yearPos = item;
                        //initPickerViews();
                        initDayPickerView();
                        monthLoopView.setInitPosition(monthPos);
                        monthLoopView.scrollToInitialPosition(true);
                        dayLoopView.scrollToInitialPosition(true);
                    } else {
                        setSelectedDate(maxDate);
                        yearLoopView.setInitPosition(yearPos);
                        initPickerViews();
                        initDayPickerView();
                        yearLoopView.scrollToInitialPosition(true);
                        monthLoopView.scrollToInitialPosition(true);
                        dayLoopView.scrollToInitialPosition(true);
                    }
                } else {
                    setSelectedDate(minDate);
                    yearLoopView.setInitPosition(yearPos);
                    initPickerViews();
                    initDayPickerView();
                    yearLoopView.scrollToInitialPosition(true);
                    monthLoopView.scrollToInitialPosition(true);
                    dayLoopView.scrollToInitialPosition(true);
                }
            }
        });

        monthLoopView.setLoopListener(new LoopScrollListener() {
            @Override
            public void onItemSelect(int item) {
                Calendar wheelDate = (Calendar) minDate.clone();
                wheelDate.set(minYear + yearPos, item, dayPos + 1);

                if(minDate.compareTo(wheelDate) <= 0) {
                    if(maxDate.compareTo(wheelDate) >= 0) {
                        monthPos = item;
                        initDayPickerView();
                        dayLoopView.setInitPosition(dayPos);
                        dayLoopView.scrollToInitialPosition(true);
                    } else {
                        setSelectedDate(maxDate);
                        monthLoopView.setInitPosition(monthPos);
                        initPickerViews();
                        initDayPickerView();
                        monthLoopView.scrollToInitialPosition(true);
                        dayLoopView.scrollToInitialPosition(true);
                    }
                } else {
                    setSelectedDate(minDate);
                    monthLoopView.setInitPosition(monthPos);
                    initPickerViews();
                    initDayPickerView();
                    monthLoopView.scrollToInitialPosition(true);
                    dayLoopView.scrollToInitialPosition(true);
                }
            }
        });

        dayLoopView.setLoopListener(new LoopScrollListener() {
            @Override
            public void onItemSelect(int item) {
                Calendar wheelDate = (Calendar) minDate.clone();
                wheelDate.set(minYear + yearPos, monthPos, item + 1);

                if(minDate.compareTo(wheelDate) <= 0) {
                    if(maxDate.compareTo(wheelDate) >= 0) {
                        dayPos = item;
                    } else {
                        setSelectedDate(maxDate);
                        dayLoopView.setInitPosition(dayPos);
                        initDayPickerView();
                        dayLoopView.scrollToInitialPosition(true);
                    }
                } else {
                    setSelectedDate(minDate);
                    dayLoopView.setInitPosition(dayPos);
                    initDayPickerView();
                    dayLoopView.scrollToInitialPosition(true);
                }
            }
        });

        initPickerViews(); // init year and month loop view
        initDayPickerView(); //init day loop view

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
    private void initPickerViews() {

        int yearCount = (maxYear - minYear) + 1;

        for (int i = 0; i < yearCount; i++) {
            yearList.add(format2LenStr(minYear + i));
        }

        for (int j = 0; j < 12; j++) {
            monthList.add(format2LenStr(j + 1));
        }

        monthListStr.add("Января");
        monthListStr.add("Февраля");
        monthListStr.add("Марта");
        monthListStr.add("Апреля");
        monthListStr.add("Мая");
        monthListStr.add("Июня");
        monthListStr.add("Июля");
        monthListStr.add("Августа");
        monthListStr.add("Сентября");
        monthListStr.add("Октября");
        monthListStr.add("Ноября");
        monthListStr.add("Декабря");

        yearLoopView.setDataList((ArrayList) yearList);
        yearLoopView.setInitPosition(yearPos);

        monthLoopView.setDataList((ArrayList) monthListStr);
        monthLoopView.setInitPosition(monthPos);
    }

    /**
     * Init day item
     */
    private void initDayPickerView() {

        int dayMaxInMonth;
        Calendar calendar = Calendar.getInstance();
        dayList = new ArrayList<String>();

        calendar.set(Calendar.YEAR, minYear + yearPos);
        calendar.set(Calendar.MONTH, monthPos);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        //get max day in month
        dayMaxInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = 0; i < dayMaxInMonth; i++) {
            dayList.add(format2LenStr(i + 1));
        }

        dayLoopView.setDataList((ArrayList) dayList);
        dayLoopView.setInitPosition(dayPos);
    }

    /**
     * set selected date position value when initView.
     *
     * @param selectedDate
     */
    public void setSelectedDate(Calendar selectedDate) {
        yearPos = selectedDate.get(Calendar.YEAR) - minYear;
        monthPos = selectedDate.get(Calendar.MONTH);
        dayPos = selectedDate.get(Calendar.DAY_OF_MONTH) - 1;

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
        trans.setAnimationListener(new AnimationListener() {

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

                int year = minYear + yearPos;
                int month = monthPos + 1;
                int day = dayPos + 1;
                StringBuffer sb = new StringBuffer();

                sb.append(String.valueOf(year));
                sb.append("-");
                sb.append(format2LenStr(month));
                sb.append("-");
                sb.append(format2LenStr(day));
                mListener.onDatePickCompleted(year, month, day, sb.toString());
            }

            dismissPopWin();
        }
    }

    /**
     * get long from yyyy-MM-dd
     *
     * @param date
     * @return
     */
    public static long getLongFromyyyyMMdd(String date) {
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date parse = null;
        try {
            parse = mFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (parse != null) {
            return parse.getTime();
        } else {
            return -1;
        }
    }

    public static String getStrDate() {
        SimpleDateFormat dd = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return dd.format(new Date());
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


    public interface OnDatePickedListener {

        /**
         * Listener when date has been checked
         *
         * @param year
         * @param month
         * @param day
         * @param dateDesc  yyyy-MM-dd
         */
        void onDatePickCompleted(int year, int month, int day,
                                 String dateDesc);
    }
}
