package com.guoxd.workframe.widge.materialcalendar.Interface;


import com.guoxd.workframe.widge.materialcalendar.DayView;
import com.guoxd.workframe.widge.materialcalendar.Util.CalendarDay;

public interface DayViewDecorator {
    boolean shouldDecorate(CalendarDay day);

    void decorate(DayView view);
}
