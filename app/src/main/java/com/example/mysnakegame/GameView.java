package com.example.mysnakegame;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Random;

public class GameView extends View{

    private Bitmap bmGrass1,bmGrass2,bmSnake1, bmApple;
    public static int size = 75*Constants.SCREEN_WIDTH/1080;
    private ArrayList<Grass> arrayGrass = new ArrayList<>();
    private Snake snake;
    private Apple apple;
    private Handler handler;
    private Runnable r;
    private boolean move = false;
    private float mx, my;
    public static boolean isPlaying = false;
    public static int score = 0, bestScore = 0;

    //建置場地
    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        SharedPreferences sp = context.getSharedPreferences("gamesetting", Context.MODE_PRIVATE);
        if(sp!=null){
            bestScore = sp.getInt("bestscore",0);
        }
        bmGrass1 = BitmapFactory.decodeResource(this.getResources(),R.drawable.grass);
        bmGrass1 = Bitmap.createScaledBitmap(bmGrass1,size,size,true);
        bmGrass2 = BitmapFactory.decodeResource(this.getResources(),R.drawable.grass03);
        bmGrass2 = Bitmap.createScaledBitmap(bmGrass2,size,size,true);
        bmSnake1 = BitmapFactory.decodeResource(this.getResources(), R.drawable.snake1);
        bmSnake1 = Bitmap.createScaledBitmap(bmSnake1, 14*size, size, true);
        bmApple = BitmapFactory.decodeResource(this.getResources(), R.drawable.apple);
        bmApple = Bitmap.createScaledBitmap(bmApple, size, size, true);
        for(int i = 0 ; i < 21;i++){
            for(int j = 0 ; j < 12;j++){
                if((i+j)%2==0){
                    arrayGrass.add(new Grass(bmGrass1,j*size+Constants.SCREEN_WIDTH/2-6*size,
                            i*size+100*Constants.SCREEN_HEIGHT/1920,size,size));
                }else{
                    arrayGrass.add(new Grass(bmGrass2,j*size+Constants.SCREEN_WIDTH/2-6*size,
                            i*size+100*Constants.SCREEN_HEIGHT/1920,size,size));
                }
            }
        }
        snake = new Snake(bmSnake1,arrayGrass.get(126).getX(),arrayGrass.get(126).getY(), 4);
        apple = new Apple(bmApple, arrayGrass.get(randomApple()[0]).getX(), arrayGrass.get(randomApple()[1]).getY());
        handler = new Handler();
        r = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
    }
    private int[] randomApple(){
        int []xy = new int[2];
        Random r = new Random();
        xy[0] = r.nextInt(arrayGrass.size()-1);
        xy[1] = r.nextInt(arrayGrass.size()-1);
        Rect rect = new Rect(arrayGrass.get(xy[0]).getX(), arrayGrass.get(xy[1]).getY(), arrayGrass.get(xy[0]).getX()+size, arrayGrass.get(xy[1]).getY()+size);
        boolean check = true;
        while (check){
            check = false;
            for (int i = 0; i < snake.getArrPartSnake().size(); i++){
                if(rect.intersect(snake.getArrPartSnake().get(i).getrBody())){
                    check = true;
                    xy[0] = r.nextInt(arrayGrass.size()-1);
                    xy[1] = r.nextInt(arrayGrass.size()-1);
                    rect = new Rect(arrayGrass.get(xy[0]).getX(), arrayGrass.get(xy[1]).getY(), arrayGrass.get(xy[0]).getX()+size, arrayGrass.get(xy[1]).getY()+size);
                }
            }
        }
        return xy;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int a = event.getActionMasked();
        switch (a){
            case  MotionEvent.ACTION_MOVE:{
                if(move==false){
                    mx = event.getX();
                    my = event.getY();
                    move = true;
                }else{
                    if(mx - event.getX() > 100 && !snake.isMove_right()){
                        mx = event.getX();
                        my = event.getY();
                        this.snake.setMove_left(true);
                        isPlaying = true;
                    }else if(event.getX() - mx > 100 &&!snake.isMove_left()){
                        mx = event.getX();
                        my = event.getY();
                        this.snake.setMove_right(true);
                        isPlaying = true;
                    }else if(event.getY() - my > 100 && !snake.isMove_up()){
                        mx = event.getX();
                        my = event.getY();
                        this.snake.setMove_down(true);
                        isPlaying = true;
                    }else if(my - event.getY() > 100 && !snake.isMove_down()){
                        mx = event.getX();
                        my = event.getY();
                        this.snake.setMove_up(true);
                        isPlaying = true;
                    }
                }
                break;
            }
            case MotionEvent.ACTION_UP:{
                mx = 0;
                my = 0;
                move = false;
                break;
            }
        }
        return true;
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);
        canvas.drawColor(0xFF065700);
        for(int i = 0; i < arrayGrass.size(); i++){
            canvas.drawBitmap(arrayGrass.get(i).getBm(), arrayGrass.get(i).getX(), arrayGrass.get(i).getY(), null);
        }
        snake.update();
        snake.drawSnake(canvas);
        apple.draw(canvas);
        if(snake.getArrPartSnake().get(0).getrBody().intersect(apple.getR())){
            randomApple();
            apple.reset(arrayGrass.get(randomApple()[0]).getX(),arrayGrass.get(randomApple()[1]).getY());
            snake.addPart();
            score++;
            TextView scoreTextView = ((Activity)getContext()).findViewById(R.id.txt_score);
            scoreTextView.setText(String.valueOf("x "+score));
        }
        handler.postDelayed(r, 100);
    }
    public void reset(){
        for(int i = 0; i < 21; i++){
            for (int j = 0; j < 12; j++){
                if((j+i)%2==0){
                    arrayGrass.add(new Grass(bmGrass1, j*bmGrass1.getWidth() + Constants.SCREEN_WIDTH/2 - (12/2)*bmGrass1.getWidth(), i*bmGrass1.getHeight()+50*Constants.SCREEN_HEIGHT/1920, bmGrass1.getWidth(), bmGrass1.getHeight()));
                }else{
                    arrayGrass.add(new Grass(bmGrass2, j*bmGrass2.getWidth() + Constants.SCREEN_WIDTH/2 - (12/2)*bmGrass2.getWidth(), i*bmGrass2.getHeight()+50*Constants.SCREEN_HEIGHT/1920, bmGrass2.getWidth(), bmGrass2.getHeight()));
                }
            }
        }
        snake = new Snake(bmSnake1,arrayGrass.get(126).getX(),arrayGrass.get(126).getY(), 4);
        apple = new Apple(bmApple, arrayGrass.get(randomApple()[0]).getX(), arrayGrass.get(randomApple()[1]).getY());
        score = 0;
    }
}
