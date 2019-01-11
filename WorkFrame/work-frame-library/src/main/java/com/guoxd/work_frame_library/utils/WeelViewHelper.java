package com.guoxd.work_frame_library.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.bigkoo.pickerview.adapter.NumericWheelAdapter;
import com.bigkoo.pickerview.configure.PickerOptions;
import com.bigkoo.pickerview.listener.ISelectTimeCallback;
import com.bigkoo.pickerview.utils.ChinaDate;
import com.bigkoo.pickerview.utils.LunarCalendar;
import com.contrarywind.listener.OnItemSelectedListener;
import com.contrarywind.view.WheelView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by guoxd on 2019/1/10.
 */
@SuppressLint("WrongConstant")
public class WeelViewHelper implements ISelectTimeCallback {
    final String TAG = "WeelViewHelper";
    //输出的格式化
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //组件
    private WheelView wv_year;
    private WheelView wv_month;
    private WheelView wv_day;
    private WheelView wv_hours;
    private WheelView wv_minutes;
    private WheelView wv_seconds;
    //默认定义
    private static final int DEFAULT_START_YEAR = 1900;
    private static final int DEFAULT_END_YEAR = 2100;
    private static final int DEFAULT_START_MONTH = 1;
    private static final int DEFAULT_END_MONTH = 12;
    private static final int DEFAULT_START_DAY = 1;
    private static final int DEFAULT_END_DAY = 31;
    //全局变量
    Calendar mCalendar;
    private PickerOptions mPickerOptions;//全局配置项
    private int currentYear;//公历用
    private boolean isStartFromToday = false;

    private int startMonth = DEFAULT_START_MONTH;
    private int endMonth = DEFAULT_END_MONTH;
    private int startDay = DEFAULT_START_DAY;
    private int endDay = DEFAULT_END_DAY; //表示31天的

    //是否农历，true=农历
    private ISelectTimeCallback mSelectChangeCallback;

    public WeelViewHelper(Context context, WheelView... views) {
        super();
        if (views.length == 6) {
            wv_year = views[0];
            wv_month = views[1];
            wv_day = views[2];
            wv_hours = views[3];
            wv_minutes = views[4];
            wv_seconds = views[5];
        }
        mPickerOptions = new PickerOptions(PickerOptions.TYPE_PICKER_TIME);
        mPickerOptions.context = context;
        mPickerOptions.type = new boolean[]{true, true, true, true, true, true};
        mPickerOptions.startYear = DEFAULT_START_YEAR;
        mPickerOptions.endYear = DEFAULT_END_YEAR;
        setLabels("年","月","日","时","分","秒");
    }

    /**设置6个weelView的显示和隐藏
     * new boolean[]{true, true, true, false, false, false}
     * control the "year","month","day","hours","minutes","seconds " display or hide.
     * 分别控制“年”“月”“日”“时”“分”“秒”的显示或隐藏。
     *
     * @param type 布尔型数组，长度需要设置为6。
     * @return TimePickerBuilder
     */
    public void setType(boolean[] type) {
        mPickerOptions.type = type;
    }

    /**设置字体大小（dp）
     * @param textSizeContent
     */
    public void setContentTextSize(int textSizeContent) {
        mPickerOptions.textSizeContent = textSizeContent;
        setContentTextSize();
    }

    /**
     * 设置格式化,此内容影响输出，
     *
     * @param dateFormat
     */
    public void setDateFormat(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public void initWheelTime(PickerOptions pickerOptions) {
        setLunarMode(pickerOptions.isLunarCalendar);
        this.mSelectChangeCallback = this;
        if (pickerOptions.startYear != 0 && pickerOptions.endYear != 0
                && pickerOptions.startYear <= pickerOptions.endYear) {
            setRange(pickerOptions);
        }
        setCyclic(mPickerOptions.cyclic);
        //若手动设置了时间范围限制
        if (pickerOptions.startDate != null && pickerOptions.endDate != null) {
            if (pickerOptions.startDate.getTimeInMillis() > pickerOptions.endDate.getTimeInMillis()) {
                throw new IllegalArgumentException("startDate can't be later than endDate");
            } else {
                setRangDate(pickerOptions);
            }
        } else if (pickerOptions.startDate != null) {
            if (pickerOptions.startDate.get(Calendar.YEAR) < DEFAULT_START_YEAR) {
                throw new IllegalArgumentException("The startDate can not as early as 1900");
            } else {
                setRangDate(pickerOptions);
            }
        } else if (pickerOptions.endDate != null) {
            if (pickerOptions.endDate.get(Calendar.YEAR) > DEFAULT_END_YEAR) {
                throw new IllegalArgumentException("The endDate should not be later than 2100");
            } else {
                setRangDate(pickerOptions);
            }
        } else {//没有设置时间范围限制，则会使用默认范围。
            setRangDate(pickerOptions);
        }
        setTime();
        setLabels();
        setTextXOffset();
        setCyclic();
        setDividerColor();
        setDividerType();
        setLineSpacingMultiplier();
        setTextColorCenter();//选中部分的文字颜色
        setTextColorOut();//未选中部分文字的颜色
        isCenterLabel(pickerOptions.isCenterLabel);
    }

    /**设置可以选择的时间范围, 要在setTime之前调用才有效果
     */
    private void setRange(PickerOptions pickerOptions) {
        setStartYear(pickerOptions.startYear);
        setEndYear(pickerOptions.endYear);
    }

    /**设置可以选择的时间范围, 要在setTime之前调用才有效果
     */
    private void setRangDate(PickerOptions pickerOptions) {
        setRangDate(pickerOptions.startDate, pickerOptions.endDate);
        //如果手动设置了时间范围
        if (pickerOptions.startDate != null && pickerOptions.endDate != null) {
            //若默认时间未设置，或者设置的默认时间越界了，则设置默认选中时间为开始时间。
            if (pickerOptions.date == null || pickerOptions.date.getTimeInMillis() < pickerOptions.startDate.getTimeInMillis()
                    || pickerOptions.date.getTimeInMillis() > pickerOptions.endDate.getTimeInMillis()) {
                pickerOptions.date = pickerOptions.startDate;
            }
        } else if (pickerOptions.startDate != null) {
            //没有设置默认选中时间,那就拿开始时间当默认时间
            pickerOptions.date = pickerOptions.startDate;
        } else if (pickerOptions.endDate != null) {
            pickerOptions.date = pickerOptions.endDate;
        }
    }


    public void Show() {
        initWheelTime(mPickerOptions);
        setTime();
    }

    /**给组件设置选中时间,默认选中当前时间
     */
    private void setTime() {
        int year, month, day, hours, minute, seconds;
        mCalendar = Calendar.getInstance();
        if(isStartFromToday){//显示当天
            int[] today = new int[6];
            today[0] = mCalendar.get(Calendar.YEAR);
            today[1] = mCalendar.get(Calendar.MONTH)+1;
            today[2] = mCalendar.get(Calendar.DAY_OF_MONTH);
            today[3] = mCalendar.get(Calendar.HOUR);
            today[4] = mCalendar.get(Calendar.MINUTE);
            today[5] = mCalendar.get(Calendar.SECOND);
            mPickerOptions.startYear = today[0];
            startMonth = today[1];
            startDay = today[2];
        }
        if (mPickerOptions.date == null) {//默认当前
            mCalendar.setTimeInMillis(System.currentTimeMillis());
            year = mCalendar.get(Calendar.YEAR);
            month = mCalendar.get(Calendar.MONTH);
            day = mCalendar.get(Calendar.DAY_OF_MONTH);
            hours = mCalendar.get(Calendar.HOUR_OF_DAY);
            minute = mCalendar.get(Calendar.MINUTE);
            seconds = mCalendar.get(Calendar.SECOND);
        } else {
            year = mPickerOptions.date.get(Calendar.YEAR);
            month = mPickerOptions.date.get(Calendar.MONTH);
            day = mPickerOptions.date.get(Calendar.DAY_OF_MONTH);
            hours = mPickerOptions.date.get(Calendar.HOUR_OF_DAY);
            minute = mPickerOptions.date.get(Calendar.MINUTE);
            seconds = mPickerOptions.date.get(Calendar.SECOND);
        }
        setPicker(year, month, day, hours, minute, seconds);
    }

    //设置农历
    public void setLunarMode(boolean isLunarCalendar) {
        mPickerOptions.isLunarCalendar = isLunarCalendar;
    }

    public boolean isLunarMode() {
        return mPickerOptions.isLunarCalendar;
    }

    public void setPicker(int year, int month, int day) {
        this.setPicker(year, month, day, 0, 0, 0);
    }

    //设置页面
    public void setPicker(int year, final int month, int day, int h, int m, int s) {
        if (wv_year == null || wv_month == null || wv_day == null
                || wv_hours == null || wv_minutes == null || wv_seconds == null) {
            Log.e(TAG, "setPicker error:cause of wheelView is null");
            return;
        }
        if (mPickerOptions.isLunarCalendar) {//农历
            int[] lunar = LunarCalendar.solarToLunar(year, month + 1, day);
            setLunar(lunar[0], lunar[1] - 1, lunar[2], lunar[3] == 1, h, m, s);
        } else {//公立
            setSolar(year, month, day, h, m, s);
        }
    }

    /**设置农历
     * @param year
     * @param month
     * @param day
     * @param h
     * @param m
     * @param s
     */
    private void setLunar(int year, final int month, int day, boolean isLeap, int h, int m, int s) {
        // 年
        wv_year.setAdapter(new ArrayWheelAdapter(ChinaDate.getYears(mPickerOptions.startYear, mPickerOptions.endYear)));// 设置"年"的显示数据

        // 月
        wv_month.setAdapter(new ArrayWheelAdapter(ChinaDate.getMonths(year)));
        int leapMonth = ChinaDate.leapMonth(year);
        if (leapMonth != 0 && (month > leapMonth - 1 || isLeap)) { //选中月是闰月或大于闰月
            wv_month.setCurrentItem(month + 1);
        } else {
            wv_month.setCurrentItem(month);
        }

        // 日
        // 判断大小月及是否闰年,用来确定"日"的数据
        if (ChinaDate.leapMonth(year) == 0) {
            wv_day.setAdapter(new ArrayWheelAdapter(ChinaDate.getLunarDays(ChinaDate.monthDays(year, month))));
        } else {
            wv_day.setAdapter(new ArrayWheelAdapter(ChinaDate.getLunarDays(ChinaDate.leapDays(year))));
        }
        wv_day.setCurrentItem(day - 1);
        wv_day.setGravity(mPickerOptions.textGravity);

        // 添加"年"监听
        wv_year.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                int year_num = index + mPickerOptions.startYear;
                // 判断是不是闰年,来确定月和日的选择
                wv_month.setAdapter(new ArrayWheelAdapter(ChinaDate.getMonths(year_num)));
                if (ChinaDate.leapMonth(year_num) != 0 && wv_month.getCurrentItem() > ChinaDate.leapMonth(year_num) - 1) {
                    wv_month.setCurrentItem(wv_month.getCurrentItem() + 1);
                } else {
                    wv_month.setCurrentItem(wv_month.getCurrentItem());
                }
                int maxItem = 29;
                if (ChinaDate.leapMonth(year_num) != 0 && wv_month.getCurrentItem() > ChinaDate.leapMonth(year_num) - 1) {
                    if (wv_month.getCurrentItem() == ChinaDate.leapMonth(year_num) + 1) {
                        wv_day.setAdapter(new ArrayWheelAdapter(ChinaDate.getLunarDays(ChinaDate.leapDays(year_num))));
                        maxItem = ChinaDate.leapDays(year_num);
                    } else {
                        wv_day.setAdapter(new ArrayWheelAdapter(ChinaDate.getLunarDays(ChinaDate.monthDays(year_num, wv_month.getCurrentItem()))));
                        maxItem = ChinaDate.monthDays(year_num, wv_month.getCurrentItem());
                    }
                } else {
                    wv_day.setAdapter(new ArrayWheelAdapter(ChinaDate.getLunarDays(ChinaDate.monthDays(year_num, wv_month.getCurrentItem() + 1))));
                    maxItem = ChinaDate.monthDays(year_num, wv_month.getCurrentItem() + 1);
                }

                if (wv_day.getCurrentItem() > maxItem - 1) {
                    wv_day.setCurrentItem(maxItem - 1);
                }

                if (mSelectChangeCallback != null) {
                    mSelectChangeCallback.onTimeSelectChanged();
                }
            }
        });

        // 添加"月"监听
        wv_month.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                int month_num = index;
                int year_num = wv_year.getCurrentItem() + mPickerOptions.startYear;
                int maxItem = 29;
                if (ChinaDate.leapMonth(year_num) != 0 && month_num > ChinaDate.leapMonth(year_num) - 1) {
                    if (wv_month.getCurrentItem() == ChinaDate.leapMonth(year_num) + 1) {
                        wv_day.setAdapter(new ArrayWheelAdapter(ChinaDate.getLunarDays(ChinaDate.leapDays(year_num))));
                        maxItem = ChinaDate.leapDays(year_num);
                    } else {
                        wv_day.setAdapter(new ArrayWheelAdapter(ChinaDate.getLunarDays(ChinaDate.monthDays(year_num, month_num))));
                        maxItem = ChinaDate.monthDays(year_num, month_num);
                    }
                } else {
                    wv_day.setAdapter(new ArrayWheelAdapter(ChinaDate.getLunarDays(ChinaDate.monthDays(year_num, month_num + 1))));
                    maxItem = ChinaDate.monthDays(year_num, month_num + 1);
                }

                if (wv_day.getCurrentItem() > maxItem - 1) {
                    wv_day.setCurrentItem(maxItem - 1);
                }

                if (mSelectChangeCallback != null) {
                    mSelectChangeCallback.onTimeSelectChanged();
                }
            }
        });
        setContentAfter(year,month+1,day, h, m, s);
    }

    /**设置公历
     * @param year
     * @param month
     * @param day
     * @param h
     * @param m
     * @param s
     */
    private void setSolar(int year, final int month, int day, int h, int m, int s) {
        // 添加大小月月份并将其转换为list,方便之后的判断
        String[] months_big = {"1", "3", "5", "7", "8", "10", "12"};
        String[] months_little = {"4", "6", "9", "11"};

        final List<String> list_big = Arrays.asList(months_big);
        final List<String> list_little = Arrays.asList(months_little);

        currentYear = year;
        // 年
        wv_year.setAdapter(new NumericWheelAdapter(mPickerOptions.startYear, mPickerOptions.endYear));// 设置"年"的显示数据
        // 月
        if (mPickerOptions.startYear == mPickerOptions.endYear) {//开始年等于终止年
            wv_month.setAdapter(new NumericWheelAdapter(startMonth, endMonth));
            wv_month.setCurrentItem(month + 1 - startMonth);
        } else if (year == mPickerOptions.startYear) {//当前年为起始年份
            //起始日期的月份控制
            wv_month.setAdapter(new NumericWheelAdapter(startMonth, 12));
            wv_month.setCurrentItem(month + 1 - startMonth);
        } else if (year == mPickerOptions.endYear) {
            //终止日期的月份控制
            wv_month.setAdapter(new NumericWheelAdapter(1, endMonth));
            wv_month.setCurrentItem(month);
        } else {
            wv_month.setAdapter(new NumericWheelAdapter(1, 12));
            wv_month.setCurrentItem(month);
        }

        // 日
        if (mPickerOptions.startYear == mPickerOptions.endYear && startMonth == endMonth) {
            if (list_big.contains(String.valueOf(month + 1))) {
                if (endDay > 31) {
                    endDay = 31;
                }
                wv_day.setAdapter(new NumericWheelAdapter(startDay, endDay));
            } else if (list_little.contains(String.valueOf(month + 1))) {
                if (endDay > 30) {
                    endDay = 30;
                }
                wv_day.setAdapter(new NumericWheelAdapter(startDay, endDay));
            } else {
                // 闰年
                if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                    if (endDay > 29) {
                        endDay = 29;
                    }
                    wv_day.setAdapter(new NumericWheelAdapter(startDay, endDay));
                } else {
                    if (endDay > 28) {
                        endDay = 28;
                    }
                    wv_day.setAdapter(new NumericWheelAdapter(startDay, endDay));
                }
            }
            wv_day.setCurrentItem(day - startDay);
        } else if (year == mPickerOptions.startYear && month + 1 == startMonth) {
            // 起始日期的天数控制
            if (list_big.contains(String.valueOf(month + 1))) {
                wv_day.setAdapter(new NumericWheelAdapter(startDay, 31));
            } else if (list_little.contains(String.valueOf(month + 1))) {
                wv_day.setAdapter(new NumericWheelAdapter(startDay, 30));
            } else {
                // 闰年
                if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                    wv_day.setAdapter(new NumericWheelAdapter(startDay, 29));
                } else {
                    wv_day.setAdapter(new NumericWheelAdapter(startDay, 28));
                }
            }
            wv_day.setCurrentItem(day - startDay);
        } else if (year == mPickerOptions.endYear && month + 1 == endMonth) {
            // 终止日期的天数控制
            if (list_big.contains(String.valueOf(month + 1))) {
                if (endDay > 31) {
                    endDay = 31;
                }
                wv_day.setAdapter(new NumericWheelAdapter(1, endDay));
            } else if (list_little.contains(String.valueOf(month + 1))) {
                if (endDay > 30) {
                    endDay = 30;
                }
                wv_day.setAdapter(new NumericWheelAdapter(1, endDay));
            } else {// 闰年
                if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                    if (endDay > 29) {
                        endDay = 29;
                    }
                    wv_day.setAdapter(new NumericWheelAdapter(1, endDay));
                } else {
                    if (endDay > 28) {
                        endDay = 28;
                    }
                    wv_day.setAdapter(new NumericWheelAdapter(1, endDay));
                }
            }
            wv_day.setCurrentItem(day - 1);
        } else {
            // 判断大小月及是否闰年,用来确定"日"的数据
            if (list_big.contains(String.valueOf(month + 1))) {
                wv_day.setAdapter(new NumericWheelAdapter(1, 31));
            } else if (list_little.contains(String.valueOf(month + 1))) {
                wv_day.setAdapter(new NumericWheelAdapter(1, 30));
            } else {
                // 闰年
                if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 29));
                } else {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 28));
                }
            }
            wv_day.setCurrentItem(day - 1);
        }
        // 添加"年"监听
        wv_year.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                int year_num = index + mPickerOptions.startYear;
                currentYear = year_num;
                int currentMonthItem = wv_month.getCurrentItem();//记录上一次的item位置
                // 判断大小月及是否闰年,用来确定"日"的数据
                if (mPickerOptions.startYear == mPickerOptions.endYear) {
                    //重新设置月份
                    wv_month.setAdapter(new NumericWheelAdapter(startMonth, endMonth));
                    if (currentMonthItem > wv_month.getAdapter().getItemsCount() - 1) {
                        currentMonthItem = wv_month.getAdapter().getItemsCount() - 1;
                        wv_month.setCurrentItem(currentMonthItem);
                    }
                    int monthNum = currentMonthItem + startMonth;
                    if (startMonth == endMonth) {
                        //重新设置日
                        setReDay(year_num, monthNum, startDay, endDay, list_big, list_little);
                    } else if (monthNum == startMonth) {
                        //重新设置日
                        setReDay(year_num, monthNum, startDay, 31, list_big, list_little);
                    } else if (monthNum == endMonth) {
                        setReDay(year_num, monthNum, 1, endDay, list_big, list_little);
                    } else {//重新设置日
                        setReDay(year_num, monthNum, 1, 31, list_big, list_little);
                    }
                } else if (year_num == mPickerOptions.startYear) {//等于开始的年
                    //重新设置月份
                    wv_month.setAdapter(new NumericWheelAdapter(startMonth, 12));

                    if (currentMonthItem > wv_month.getAdapter().getItemsCount() - 1) {
                        currentMonthItem = wv_month.getAdapter().getItemsCount() - 1;
                        wv_month.setCurrentItem(currentMonthItem);
                    }
                    int month = currentMonthItem + startMonth;
                    if (month == startMonth) {
                        //重新设置日
                        setReDay(year_num, month, startDay, 31, list_big, list_little);
                    } else {
                        //重新设置日
                        setReDay(year_num, month, 1, 31, list_big, list_little);
                    }
                } else if (year_num == mPickerOptions.endYear) {
                    //重新设置月份
                    wv_month.setAdapter(new NumericWheelAdapter(1, endMonth));
                    if (currentMonthItem > wv_month.getAdapter().getItemsCount() - 1) {
                        currentMonthItem = wv_month.getAdapter().getItemsCount() - 1;
                        wv_month.setCurrentItem(currentMonthItem);
                    }
                    int monthNum = currentMonthItem + 1;

                    if (monthNum == endMonth) {
                        //重新设置日
                        setReDay(year_num, monthNum, 1, endDay, list_big, list_little);
                    } else {
                        //重新设置日
                        setReDay(year_num, monthNum, 1, 31, list_big, list_little);
                    }
                } else {
                    //重新设置月份
                    wv_month.setAdapter(new NumericWheelAdapter(1, 12));
                    //重新设置日
                    setReDay(year_num, wv_month.getCurrentItem() + 1, 1, 31, list_big, list_little);
                }
                if (mSelectChangeCallback != null) {
                    mSelectChangeCallback.onTimeSelectChanged();
                }
            }
        });

        // 添加"月"监听
        wv_month.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                int month_num = index + 1;

                if (mPickerOptions.startYear == mPickerOptions.endYear) {
                    month_num = month_num + startMonth - 1;
                    if (startMonth == endMonth) {
                        //重新设置日
                        setReDay(currentYear, month_num, startDay, endDay, list_big, list_little);
                    } else if (startMonth == month_num) {
                        //重新设置日
                        setReDay(currentYear, month_num, startDay, 31, list_big, list_little);
                    } else if (endMonth == month_num) {
                        setReDay(currentYear, month_num, 1, endDay, list_big, list_little);
                    } else {
                        setReDay(currentYear, month_num, 1, 31, list_big, list_little);
                    }
                } else if (currentYear == mPickerOptions.startYear) {
                    month_num = month_num + startMonth - 1;
                    if (month_num == startMonth) {
                        //重新设置日
                        setReDay(currentYear, month_num, startDay, 31, list_big, list_little);
                    } else {
                        //重新设置日
                        setReDay(currentYear, month_num, 1, 31, list_big, list_little);
                    }
                } else if (currentYear == mPickerOptions.endYear) {
                    if (month_num == endMonth) {
                        //重新设置日
                        setReDay(currentYear, wv_month.getCurrentItem() + 1, 1, endDay, list_big, list_little);
                    } else {
                        setReDay(currentYear, wv_month.getCurrentItem() + 1, 1, 31, list_big, list_little);
                    }
                } else {
                    //重新设置日
                    setReDay(currentYear, month_num, 1, 31, list_big, list_little);
                }
                if (mSelectChangeCallback != null) {
                    mSelectChangeCallback.onTimeSelectChanged();
                }
            }
        });
        setContentAfter(year,month+1,day, h, m, s);
    }

    /**农历和公历初始化中相同的部分
     * @param year
     * @param h
     * @param m
     * @param s
     */
    private void setContentAfter(int year,int month,int day, int h, int m, int s) {
        wv_year.setGravity(mPickerOptions.textGravity);
        wv_year.setCurrentItem(year - mPickerOptions.startYear);// 初始化时显示的数据

        wv_month.setGravity(mPickerOptions.textGravity);
        wv_day.setGravity(mPickerOptions.textGravity);

        wv_hours.setAdapter(new NumericWheelAdapter(0, 23));
        wv_hours.setCurrentItem(h);
        //分钟
        wv_minutes.setAdapter(new NumericWheelAdapter(0, 59));
        wv_minutes.setCurrentItem(m);
        //秒
        wv_seconds.setAdapter(new NumericWheelAdapter(0, 59));
        wv_seconds.setCurrentItem(s);

        wv_hours.setGravity(mPickerOptions.textGravity);
        wv_minutes.setGravity(mPickerOptions.textGravity);
        wv_seconds.setGravity(mPickerOptions.textGravity);
        setChangedListener(wv_day);
        setChangedListener(wv_hours);
        setChangedListener(wv_minutes);
        setChangedListener(wv_seconds);

        if (mPickerOptions.type.length != 6) {
            throw new RuntimeException("type[] length is not 6");
        }
        wv_year.setVisibility(mPickerOptions.type[0] ? View.VISIBLE : View.GONE);
        wv_month.setVisibility(mPickerOptions.type[1] ? View.VISIBLE : View.GONE);
        wv_day.setVisibility(mPickerOptions.type[2] ? View.VISIBLE : View.GONE);
        wv_hours.setVisibility(mPickerOptions.type[3] ? View.VISIBLE : View.GONE);
        wv_minutes.setVisibility(mPickerOptions.type[4] ? View.VISIBLE : View.GONE);
        wv_seconds.setVisibility(mPickerOptions.type[5] ? View.VISIBLE : View.GONE);
        setContentTextSize();
    }

    private void setChangedListener(WheelView wheelView) {
        if (mSelectChangeCallback != null) {
            wheelView.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(int index) {
                    mSelectChangeCallback.onTimeSelectChanged();
                }
            });
        }
    }

    private void setReDay(int year_num, int monthNum, int startD, int endD, List<String> list_big, List<String> list_little) {
        int currentItem = wv_day.getCurrentItem();
        if (list_big.contains(String.valueOf(monthNum))) {
            if (endD > 31) {
                endD = 31;
            }
            wv_day.setAdapter(new NumericWheelAdapter(startD, endD));
        } else if (list_little.contains(String.valueOf(monthNum))) {
            if (endD > 30) {
                endD = 30;
            }
            wv_day.setAdapter(new NumericWheelAdapter(startD, endD));
        } else {
            if ((year_num % 4 == 0 && year_num % 100 != 0)
                    || year_num % 400 == 0) {
                if (endD > 29) {
                    endD = 29;
                }
                wv_day.setAdapter(new NumericWheelAdapter(startD, endD));
            } else {
                if (endD > 28) {
                    endD = 28;
                }
                wv_day.setAdapter(new NumericWheelAdapter(startD, endD));
            }
        }
        if (currentItem > wv_day.getAdapter().getItemsCount() - 1) {
            currentItem = wv_day.getAdapter().getItemsCount() - 1;
            wv_day.setCurrentItem(currentItem);
        }
    }

    /*******私有配置项**********/
    private void setContentTextSize() {//textSize
        wv_day.setTextSize(mPickerOptions.textSizeContent);
        wv_month.setTextSize(mPickerOptions.textSizeContent);
        wv_year.setTextSize(mPickerOptions.textSizeContent);
        wv_hours.setTextSize(mPickerOptions.textSizeContent);
        wv_minutes.setTextSize(mPickerOptions.textSizeContent);
        wv_seconds.setTextSize(mPickerOptions.textSizeContent);
    }

    private void setTextColorOut() {
        wv_day.setTextColorOut(mPickerOptions.textColorOut);
        wv_month.setTextColorOut(mPickerOptions.textColorOut);
        wv_year.setTextColorOut(mPickerOptions.textColorOut);
        wv_hours.setTextColorOut(mPickerOptions.textColorOut);
        wv_minutes.setTextColorOut(mPickerOptions.textColorOut);
        wv_seconds.setTextColorOut(mPickerOptions.textColorOut);
    }

    private void setTextColorCenter() {
        wv_day.setTextColorCenter(mPickerOptions.textColorCenter);
        wv_month.setTextColorCenter(mPickerOptions.textColorCenter);
        wv_year.setTextColorCenter(mPickerOptions.textColorCenter);
        wv_hours.setTextColorCenter(mPickerOptions.textColorCenter);
        wv_minutes.setTextColorCenter(mPickerOptions.textColorCenter);
        wv_seconds.setTextColorCenter(mPickerOptions.textColorCenter);
    }

    private void setCyclic(){
        wv_year.setCyclic(mPickerOptions.cyclic);
        wv_month.setCyclic(mPickerOptions.cyclic);
        wv_day.setCyclic(mPickerOptions.cyclic);
        wv_hours.setCyclic(mPickerOptions.cyclic);
        wv_minutes.setCyclic(mPickerOptions.cyclic);
        wv_seconds.setCyclic(mPickerOptions.cyclic);
    }

    private void setDividerColor() {
        wv_day.setDividerColor(mPickerOptions. dividerColor);
        wv_month.setDividerColor(mPickerOptions.dividerColor);
        wv_year.setDividerColor(mPickerOptions.dividerColor);
        wv_hours.setDividerColor(mPickerOptions.dividerColor);
        wv_minutes.setDividerColor(mPickerOptions.dividerColor);
        wv_seconds.setDividerColor(mPickerOptions.dividerColor);
    }

    private void setDividerType() {
        wv_day.setDividerType(mPickerOptions.dividerType);
        wv_month.setDividerType(mPickerOptions.dividerType);
        wv_year.setDividerType(mPickerOptions.dividerType);
        wv_hours.setDividerType(mPickerOptions.dividerType);
        wv_minutes.setDividerType(mPickerOptions.dividerType);
        wv_seconds.setDividerType(mPickerOptions.dividerType);
    }

    private void setLineSpacingMultiplier() {
        wv_day.setLineSpacingMultiplier(mPickerOptions.lineSpacingMultiplier);
        wv_month.setLineSpacingMultiplier(mPickerOptions.lineSpacingMultiplier);
        wv_year.setLineSpacingMultiplier(mPickerOptions.lineSpacingMultiplier);
        wv_hours.setLineSpacingMultiplier(mPickerOptions.lineSpacingMultiplier);
        wv_minutes.setLineSpacingMultiplier(mPickerOptions.lineSpacingMultiplier);
        wv_seconds.setLineSpacingMultiplier(mPickerOptions.lineSpacingMultiplier);
    }

    private void setTextXOffset(){
        wv_day.setTextXOffset(mPickerOptions.x_offset_year);
        wv_month.setTextXOffset(mPickerOptions.x_offset_month);
        wv_year.setTextXOffset(mPickerOptions.x_offset_day);
        wv_hours.setTextXOffset(mPickerOptions.x_offset_hours);
        wv_minutes.setTextXOffset(mPickerOptions.x_offset_minutes);
        wv_seconds.setTextXOffset(mPickerOptions.x_offset_seconds);
    }
    /*******公有配置项**********/
    /**设置单位（随内容显示）
     * 不随内容显示需要自定义TextView
     * @param label_year
     * @param label_month
     * @param label_day
     * @param label_hours
     * @param label_mins
     * @param label_seconds
     */
    public void setLabels(String label_year, String label_month, String label_day, String label_hours, String label_mins, String label_seconds) {
        mPickerOptions.label_year = label_year;
        mPickerOptions.label_month = label_month;
        mPickerOptions.label_day = label_day;
        mPickerOptions.label_hours = label_hours;
        mPickerOptions.label_minutes = label_mins;
        mPickerOptions.label_seconds = label_seconds;
        setLabels();
    }
    private void setLabels(){
        if (!TextUtils.isEmpty(mPickerOptions.label_year)) {
            wv_year.setLabel(mPickerOptions.label_year);
        }
        if (!TextUtils.isEmpty(mPickerOptions.label_month)) {
            wv_month.setLabel(mPickerOptions.label_month);
        }
        if (!TextUtils.isEmpty(mPickerOptions.label_day)) {
            wv_day.setLabel(mPickerOptions.label_day);
        }
        if (!TextUtils.isEmpty(mPickerOptions.label_hours)) {
            wv_hours.setLabel(mPickerOptions.label_hours);
        }
        if (!TextUtils.isEmpty(mPickerOptions.label_minutes)) {
            wv_minutes.setLabel(mPickerOptions.label_minutes);
        }
        if (!TextUtils.isEmpty(mPickerOptions.label_seconds)) {
            wv_seconds.setLabel(mPickerOptions.label_seconds);
        }
    }

    /**取得组件上的当前时间
     * @return
     */
    public String getTime() {
        int year = (wv_year.getCurrentItem() + mPickerOptions.startYear);
        int month, day = 0;
        int h = wv_hours.getCurrentItem();
        int m = wv_minutes.getCurrentItem();
        int s = wv_seconds.getCurrentItem();
        //农历返回对应的公历时间
        if (mPickerOptions.isLunarCalendar) {//如果是农历 返回对应的公历时间
            boolean isLeapMonth = false;
            if (ChinaDate.leapMonth(year) == 0) {//判断是否有闰月，0=不润
                month = wv_month.getCurrentItem() + 1;
            } else {
                if ((wv_month.getCurrentItem() + 1) - ChinaDate.leapMonth(year) <= 0) {
                    month = wv_month.getCurrentItem() + 1;
                } else if ((wv_month.getCurrentItem() + 1) - ChinaDate.leapMonth(year) == 1) {
                    month = wv_month.getCurrentItem();
                    isLeapMonth = true;
                } else {
                    month = wv_month.getCurrentItem();
                }
            }
            day = wv_day.getCurrentItem() + 1;
            int[] solar = LunarCalendar.lunarToSolar(year, month, day, isLeapMonth);
            return dateFormat.format(getTimeData(solar[0], solar[1] - 1, solar[2], h, m, s));
        }
        //公历
        if (currentYear == mPickerOptions.startYear) {
            month = (wv_month.getCurrentItem() + startMonth - 1);
            if ((wv_month.getCurrentItem() + startMonth) == startMonth) {
                day = (wv_day.getCurrentItem() + startDay);
            }
        } else {
            month = (wv_month.getCurrentItem());
        }
        if (day == 0) {
            day = (wv_day.getCurrentItem() + 1);
        }
        return dateFormat.format(getTimeData(year, month, day, h, m, s));
    }
    //格式化getTime的结果为String
    public Date getTimeData(int year, int month, int day, int hour, int minute, int second) {
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, day);
        mCalendar.set(Calendar.HOUR, hour);
        mCalendar.set(Calendar.MINUTE, minute);
        mCalendar.set(Calendar.SECOND, second);
        return mCalendar.getTime();
    }

    public int getStartYear() {
        return mPickerOptions.startYear;
    }

    public void setStartYear(int startYear) {
        mPickerOptions.startYear = startYear;
    }

    public void setStartFromToday(boolean isStartFromToday) {
        this.isStartFromToday = isStartFromToday;
    }

    public int getEndYear() {
        return mPickerOptions.endYear;
    }

    public void setEndYear(int endYear) {
        mPickerOptions.endYear = endYear;
    }

    public void setRangDate(Calendar startDate, Calendar endDate) {
        if (startDate == null && endDate != null) {
            int year = endDate.get(Calendar.YEAR);
            int month = endDate.get(Calendar.MONTH) + 1;
            int day = endDate.get(Calendar.DAY_OF_MONTH);
            if (year > mPickerOptions.startYear) {
                mPickerOptions.endYear = year;
                this.endMonth = month;
                this.endDay = day;
            } else if (year == mPickerOptions.startYear) {
                if (month > startMonth) {
                    mPickerOptions.endYear = year;
                    this.endMonth = month;
                    this.endDay = day;
                } else if (month == startMonth) {
                    if (day > startDay) {
                        mPickerOptions.endYear = year;
                        this.endMonth = month;
                        this.endDay = day;
                    }
                }
            }
        } else if (startDate != null && endDate == null) {
            int year = startDate.get(Calendar.YEAR);
            int month = startDate.get(Calendar.MONTH) + 1;
            int day = startDate.get(Calendar.DAY_OF_MONTH);
            if (year < mPickerOptions.endYear) {
                this.startMonth = month;
                this.startDay = day;
                mPickerOptions.startYear = year;
            } else if (year == mPickerOptions.endYear) {
                if (month < endMonth) {
                    this.startMonth = month;
                    this.startDay = day;
                    mPickerOptions.startYear = year;
                } else if (month == endMonth) {
                    if (day < endDay) {
                        this.startMonth = month;
                        this.startDay = day;
                        mPickerOptions.startYear = year;
                    }
                }
            }
        } else if (startDate != null && endDate != null) {
            mPickerOptions.startYear = startDate.get(Calendar.YEAR);
            mPickerOptions.endYear = endDate.get(Calendar.YEAR);
            this.startMonth = startDate.get(Calendar.MONTH) + 1;
            this.endMonth = endDate.get(Calendar.MONTH) + 1;
            this.startDay = startDate.get(Calendar.DAY_OF_MONTH);
            this.endDay = endDate.get(Calendar.DAY_OF_MONTH);
        }
    }

    /**设置间距倍数,但是只能在1.0-4.0f之间
     * @param lineSpacingMultiplier
     */
    public void setLineSpacingMultiplier(float lineSpacingMultiplier) {
        mPickerOptions.lineSpacingMultiplier = lineSpacingMultiplier;
        setLineSpacingMultiplier();
    }
    /**设置是否循环滚动
     * @param cyclic
     */
    public void setCyclic(boolean cyclic) {
        mPickerOptions.cyclic = cyclic;
        setCyclic();
    }
    /**设置分割线的颜色
     * @param dividerColor
     */
    public void setDividerColor(int dividerColor) {
        mPickerOptions.dividerColor = dividerColor;
        setDividerColor();
    }

    /**设置分割线的类型
     * @param dividerType
     */
    public void setDividerType(WheelView.DividerType dividerType) {
        mPickerOptions.dividerType = dividerType;
        setDividerType();
    }

    /**设置分割线之间的文字的颜色
     * @param textColorCenter
     */
    public void setTextColorCenter(int textColorCenter) {
        mPickerOptions.textColorCenter = textColorCenter;
        setTextColorCenter();
    }

    /**设置分割线以外文字的颜色
     * @param textColorOut
     */
    public void setTextColorOut(int textColorOut) {
        mPickerOptions.textColorOut = textColorOut;
        setTextColorOut();
    }

    /**设置只显示中间选中项
     * @param isCenterLabel 是否只显示中间选中项的
     */
    public void isCenterLabel(boolean isCenterLabel) {
        wv_day.isCenterLabel(isCenterLabel);
        wv_month.isCenterLabel(isCenterLabel);
        wv_year.isCenterLabel(isCenterLabel);
        wv_hours.isCenterLabel(isCenterLabel);
        wv_minutes.isCenterLabel(isCenterLabel);
        wv_seconds.isCenterLabel(isCenterLabel);
    }

    public void setTextXOffset(int x_offset_year, int x_offset_month, int x_offset_day,
                               int x_offset_hours, int x_offset_minutes, int x_offset_seconds) {
        mPickerOptions.x_offset_year = x_offset_year;
        mPickerOptions.x_offset_month = x_offset_month;
        mPickerOptions.x_offset_day = x_offset_day;
        mPickerOptions.x_offset_hours = x_offset_hours;
        mPickerOptions.x_offset_minutes = x_offset_minutes;
        mPickerOptions.x_offset_seconds = x_offset_seconds;
        setTextXOffset();
    }
    //mSelectChangeCallback的回调
    @Override
    public void onTimeSelectChanged() {
        if (mListener != null) {
            mListener.onDataChange(getTime());
        }
    }

    //外界监听
    OnDataChangeListener mListener;

    public void setListener(OnDataChangeListener mListener) {
        this.mListener = mListener;
    }

    public interface OnDataChangeListener {
        void onDataChange(String data);
    }
}
