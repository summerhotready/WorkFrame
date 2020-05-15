package com.guoxd.workframe.widge.materialcalendar.Interface;


import com.guoxd.workframe.widge.materialcalendar.Util.CalendarDay;

import java.util.ArrayList;
import java.util.Date;

public interface CalendarCallback {
    Date getDateSelected();

    ArrayList<CalendarDay> getEvents();

    boolean getIndicatorsVisible();
}
