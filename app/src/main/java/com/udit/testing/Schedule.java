package com.udit.testing;

public class Schedule {
    private String start_day;
    private String end_day;
    private String morning1;
    public String morning2;
    public String evening1;
    public String evening2;
    public int hours1;
    public int min1;
    public int hours2;
    public int min2;

    public int hours3;
    public int min3;
    public int hours4;
    public int min4;

    public int getHours3() {
        return hours3;
    }

    public void setHours3(int hours3) {
        this.hours3 = hours3;
    }

    public int getMin3() {
        return min3;
    }

    public void setMin3(int min3) {
        this.min3 = min3;
    }

    public int getHours4() {
        return hours4;
    }

    public void setHours4(int hours4) {
        this.hours4 = hours4;
    }

    public int getMin4() {
        return min4;
    }

    public void setMin4(int min4) {
        this.min4 = min4;
    }

    public String timespin;

    public String getTimespin() {
        return timespin;
    }

    public void setTimespin(String timespin) {
        this.timespin = timespin;
    }

    public int getHours1() {
        return hours1;
    }

    public void setHours1(int hours1) {
        this.hours1 = hours1;
    }

    public int getMin1() {
        return min1;
    }

    public void setMin1(int min1) {
        this.min1 = min1;
    }

    public int getHours2() {
        return hours2;
    }

    public void setHours2(int hours2) {
        this.hours2 = hours2;
    }

    public int getMin2() {
        return min2;
    }

    public void setMin2(int min2) {
        this.min2 = min2;
    }




    public Schedule() {

    }
    public String getStart_day () {
        return start_day;
    }

    public void setStart_day (String start_day){
        this.start_day = start_day;
    }
    public String getEnd_day () {

        return end_day;
    }

    public void setEnd_day (String end_day){

        this.end_day = end_day;
    }
    public String getMorningtime1 () {

        return morning1;
    }

    public void setMorningtime1 (String morning1){

        this.morning1 = morning1;
    }
    public String getMorningtime2 (String morning2)
    {
        return morning2;
    }
    public void setMorningtime2 (String morning2)
    {
        this.morning2 = morning2;
    }

    public String getEveningtime1 (String evening1)
    {
        return evening1;
    }
    public void setEveningtime1 (String evening1)
    {
        this.evening1 = evening1;
    }
    public String getEveningtime2 (String evening2)
    {
        return evening2;
    }
    public void setEveningtime2 (String evening2)
    {
        this.evening2 = evening2;
    }
}

