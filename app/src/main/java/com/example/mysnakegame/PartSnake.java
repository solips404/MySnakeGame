package com.example.mysnakegame;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class PartSnake {
    private Bitmap bm;
    private int x,y;
    private Rect rBody ,rTop,rBottom,rLeft,rRight;

    public PartSnake(Bitmap bm, int x, int y) {
        this.bm = bm;
        this.x = x;
        this.y = y;
    }

    public Bitmap getBm() {
        return bm;
    }

    public void setBm(Bitmap bm) {
        this.bm = bm;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Rect getrBody() {
        return new Rect(this.x,this.y,this.x+GameView.size,this.y+GameView.size);
    }

    public void setrBody(Rect rBody) {
        this.rBody = rBody;
    }

    public Rect getrBottom() {
        return new Rect(this.x,this.y+GameView.size,this.x+GameView.size,this.y+GameView.size+10*Constants.SCREEN_HEIGHT/1920);
    }

    public void setrBottom(Rect rBottom) {
        this.rBottom = rBottom;
    }

    public Rect getrTop() {
        return new Rect(this.x,this.y-10*Constants.SCREEN_HEIGHT/1920,this.x+GameView.size,this.y);
    }

    public void setrTop(Rect rTop) {
        this.rTop = rTop;
    }

    public Rect getrLeft() {
        return new Rect(this.x-10*Constants.SCREEN_WIDTH/1000,this.y,this.x,this.y+GameView.size);
    }

    public void setrLeft(Rect rLeft) {
        this.rLeft = rLeft;
    }

    public Rect getrRight() {
        return new Rect(this.x+GameView.size,this.y,this.x+GameView.size+10*Constants.SCREEN_WIDTH/1000,this.y+GameView.size);
    }

    public void setrRight(Rect rRight) {
        this.rRight = rRight;
    }
}
