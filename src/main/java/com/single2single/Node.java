package com.single2single;

public class Node {
    public String title;
    public String text;
    public int id;
    public String time;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return String.format("【标题：%s，内容：%s，编号：%d，时间戳：%s】",
                title, text, id, time);
    }
}
