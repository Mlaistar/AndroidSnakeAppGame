package com.example.hi.snakeapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;

public class SnakeView extends View {

    private int blockSize = 20;
    private int width, height;
    private int offsetX, offsetY;
    private int snakeLength;

    private int[] snakeX = new int[100];
    private int[] snakeY = new int[100];

    private int snakeDir;
    private int foodX, foodY, foodCount;

    Paint paintBackground = new Paint();
    Paint paintHead = new Paint();
    Paint paintBody = new Paint();
    Paint paintFood = new Paint();
    Paint paintBorder = new Paint();

    Handler handler = null;
    //private final int SNAKE_MOVE = 99;

    private final int STATUS_RUN = 1;
    private final int STATUS_DEAD = 2;
    private final int STATUS_PAUSE = 3;
    private int gameStatus = STATUS_PAUSE;
    private final int SNAKE_MOVE = 1;
    /*
        private final int SNAKE_READ = 2;
        private final int SNAKE_PAUSE = 3;
    */
    public static final int DIR_UP = 0;
    public static final int DIR_RIGHT = 1;
    public static final int DIR_DOWN = 2;
    public static final int DIR_LEFT = 3;

    public SnakeView(Context context) {
        super(context);
        InitGame();
        //  InitSnake();
    }

    public SnakeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        InitGame();
        //  InitSnake();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        //Get the number of block size in your GameArea.
        width = w / blockSize - 1;
        height = h / blockSize - 1;

        //Compute your GameArea offset
        offsetX = (w - width * blockSize) / 2;
        offsetY = (h - height * blockSize) / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        // Paint paintBlack = new Paint();
        // paintBlack.setColor(Color.argb(255, 0, 0, 0));
        canvas.drawRect(offsetX, offsetY, width * blockSize + offsetX, height * blockSize + offsetY, paintBackground);

        //Paint paintFood = new Paint();
        //paintFood.setColor(Color.argb(255, 0, 0, 255));
        canvas.drawRect(foodX * blockSize + offsetX,
                foodY * blockSize + offsetY,
                (foodX + 1) * blockSize + offsetX,
                (foodY + 1) * blockSize + offsetY,
                paintFood);

        for (int i = 0; i< snakeLength; i++){
            if(i == 0){
                canvas.drawRect(snakeX[i] * blockSize + offsetX, snakeY[i] * blockSize + offsetY, (snakeX[i] + 1) * blockSize + offsetX, (snakeY[i] + 1) * blockSize + offsetY, paintHead);
                //Rect rect = new Rect();
                // canvas.drawRect();
            }else{
                canvas.drawRect(snakeX[i] * blockSize + offsetX, snakeY[i] * blockSize + offsetY, (snakeX[i] + 1) * blockSize + offsetX, (snakeY[i] + 1) * blockSize + offsetY, paintBody);
            }
        }

        super.onDraw(canvas);
    }
    public void InitGame(){

        paintBackground.setColor(Color.argb(255, 0, 0, 0));//Black
        paintFood.setColor(Color.argb(255, 0, 11, 255));//Blue
        paintHead.setColor(Color.argb(255, 255, 0, 0));//Red
        paintBody.setColor(Color.argb(255, 225, 211, 55));//Yellow
        paintBorder.setColor(Color.argb(255, 255, 255, 255));//White
        // gameStatus = STATUS_PAUSE;
        InitSnake();
        handler = new Handler(){

            @Override
            public void handleMessage(Message msg){
                if(msg.what == SNAKE_MOVE)
                    SnakeMove();
            }
        };

        Thread newThread = new Thread(new Runnable() {

            @Override
            public void run() {
                while(true){
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message message = new Message();
                    message.what = SNAKE_MOVE;
                    handler.sendMessage(message);
                }
            }
        });
        newThread.start();
    }
    public void InitSnake(){

        snakeLength = 4;
        snakeX[0] = 3;
        snakeY[0] = 0;

        snakeX[1] = 2;
        snakeY[1] = 0;

        snakeX[2] = 1;
        snakeY[2] = 0;

        snakeX[3] = 0;
        snakeY[3] = 0;

        foodX = 4;
        foodY = 4;
        foodCount = 0;
        snakeDir = DIR_RIGHT;
    }
    public void StartGame(){

        switch (gameStatus){
            case STATUS_DEAD:
                InitSnake();
                gameStatus = STATUS_RUN;
                break;
            case STATUS_PAUSE:
                gameStatus = STATUS_RUN;
                break;
            default:
                break;
        }
    }
    public void PauseGame(){

        if(gameStatus == STATUS_RUN)
            gameStatus = STATUS_PAUSE;
    }

    public void ControlGame(int dir){

        if(gameStatus != STATUS_RUN)
            return;

        switch(dir){
            case DIR_UP:
            case DIR_RIGHT:
            case DIR_DOWN:
            case DIR_LEFT:
                snakeDir = dir;
                break;
            default:
                break;
        }
    }
    private void SnakeMove(){

        if(gameStatus != STATUS_RUN)
            return;
        int newheadx = 0, newheady = 0;

        switch (snakeDir){

            case DIR_UP:
                newheadx = snakeX[0];
                newheady = snakeY[0] - 1;
                break;

            case DIR_RIGHT:
                newheadx = snakeX[0] + 1;
                newheady = snakeY[0];
                break;

            case DIR_DOWN:
                newheadx = snakeX[0];
                newheady = snakeY[0] + 1;
                break;

            case DIR_LEFT:
                newheadx = snakeX[0] - 1;
                newheady = snakeY[0];
                break;

            default:

                break;
        }
        if(newheadx < 0 || newheadx >= width || newheady < 0 || newheady >= height){
            gameStatus = STATUS_DEAD;
            if(onSnakeDeadListener!=null)
                onSnakeDeadListener.OnSnakeDead(foodCount);
            return;

        }
        if(newheadx == foodX && newheady == foodY){
            Random random = new Random();
            foodX = random.nextInt(width - 1);
            foodY = random.nextInt(height - 1);

            snakeLength++;
            foodCount++;

            if(onSnakeEatFoodListener != null)
                onSnakeEatFoodListener.OnSnakeEatFood(foodCount);
        }


        for (int i = snakeLength - 1; i > 0; i--){

            snakeX[i] = snakeX[i - 1];
            snakeY[i] = snakeY[i - 1];
        }
        snakeX[0] = newheadx;
        snakeY[0] = newheady;
        invalidate();
    }

    public interface OnSnakeEatFoodListener{
        void OnSnakeEatFood(int foodCount);
    }

    private  OnSnakeEatFoodListener onSnakeEatFoodListener;

    public void setSnakeEatFoodListener(OnSnakeEatFoodListener listener){
        this.onSnakeEatFoodListener = listener;
    }

    public interface OnSnakeDeadListener{
        void OnSnakeDead(int foodCount);
    }
    private OnSnakeDeadListener onSnakeDeadListener;

    public void setOnSnakeDeadListener(OnSnakeDeadListener listener){
        this.onSnakeDeadListener = listener;
    }
/*
    public void setOnClickListener(View.OnClickListener v){}*/

}
