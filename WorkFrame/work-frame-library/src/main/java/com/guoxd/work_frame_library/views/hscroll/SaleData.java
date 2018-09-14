package com.guoxd.work_frame_library.views.hscroll;


import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/3/9 0009.
 */
public class SaleData {
    private List<List<String>> cells;
    private HashMap<Integer,Integer> columnWidth;
    private int cellTextSize = 48;
    private int titleTextSize = 50;
    private int cellHeight = 80;
    private int titleHeight = 100;

    private int icon;
    private String[] titleArray;
    private HashMap<Integer,Integer> rowColor;
    private HashMap<Integer,Integer> columnColor;
    private HashMap<Integer,Integer> titleColor;

    public List<List<String>> getCells() {
        return cells;
    }

    public List<String> getCellsRow(int i) {
        return cells.get(i);
    }

    public String getCellsItem(int i,int j) {
        if(cells != null && cells.get(i) != null && cells.get(i).get(j)!= null) {
            return cells.get(i).get(j);
        }else{
            return "";
        }
    }

    public String getCellsItem(List<String> rows,int i) {
        if(rows != null&& rows.get(i) != null) {
            return rows.get(i);
        }else
            return "";
    }

    public int getCellTextSize() {
        return cellTextSize;
    }

    public void setCellTextSize(int cellTextSize,float dip) {
        this.cellTextSize = cellTextSize;
        this.icon = icon >(int)(cellTextSize*dip) ? icon:(int)(cellTextSize*dip);
        cellHeight = (int)(cellTextSize*dip*dip);
    }

    public int getTitleTextSize() {
        return titleTextSize;
    }

    public void setTitleTextSize(int titleTextSize,float dip) {
        this.titleTextSize = titleTextSize;
        this.icon = icon >(int)(titleTextSize*dip) ? icon:(int)(titleTextSize*dip);
        titleHeight = (int)(titleTextSize*dip*dip);
    }

    public void setCells(List<List<String>> cells) {
        this.cells = cells;
    }

    public String[] getTitleArray() {
        return titleArray;
    }

    public String getTitlePosition(int i) {
        if(i>=0 && i<titleArray.length){
            return titleArray[i];
        }else{
            return "";
        }
    }


    public void setTitleArray(String[] titleArray) {
        this.titleArray = titleArray;
        columnWidth = new HashMap<Integer,Integer>();
        for(int i=0;i<titleArray.length;i++){
            columnWidth.put(i,titleArray[i].toString().length()*2);
        }
    }

    public int getTitleColumnWidth(int i){
        if(columnWidth != null && columnWidth.get(i) != null){
            return columnWidth.get(i)*icon;
        }else{
            return icon;
        }
    }

    public int getCellTextClolor(int position){
        if(getRowColor() != null && getRowColor().get(position) != null){
            return getRowColor().get(position);
        }else{
//            return R.color.text_black_light;
            return android.R.color.darker_gray;
        }
    }

    public int getTitleTextClolor(int position){
        if(getTitleColor() != null && getTitleColor().get(position) != null){
            return getTitleColor().get(position);
        }else{
//            return R.color.text_black_light;
            return android.R.color.darker_gray;
        }
    }
    public int RowSize(){
        if(cells != null)
            return cells.size();
        else
            return 0;
    }

    public int leftTitleWidth(){
        if(RowSize() == 0)
            return icon;
        else {
            String s = RowSize()+"";
            return s.length()*icon;
        }

    }

    public int cloumnSize(){
        if(titleArray != null) {
            return titleArray.length;
        }else
            return 0;
    }

    public HashMap<Integer, Integer> getRowColor() {
        return rowColor;
    }

    public void setRowColor(HashMap<Integer, Integer> rowColor) {
        this.rowColor = rowColor;
    }

    public HashMap<Integer, Integer> getColumnColor() {
        return columnColor;
    }

    public void setColumnColor(HashMap<Integer, Integer> columnColor) {
        this.columnColor = columnColor;
    }

    public HashMap<Integer, Integer> getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(HashMap<Integer, Integer> titleColor) {
        this.titleColor = titleColor;
    }

    public HashMap<Integer, Integer> getColumnWidth() {
        return columnWidth;
    }

    public void setColumnWidth(HashMap<Integer, Integer> columnWidth) {
        this.columnWidth = columnWidth;
    }

    public int getCellHeight() {
        return cellHeight;
    }

    public void setCellHeight(int cellHeight) {
        this.cellHeight = cellHeight;
    }

    public int getTitleHeight() {
        return titleHeight;
    }

    public void setTitleHeight(int titleHeight) {
        this.titleHeight = titleHeight;
    }
}
