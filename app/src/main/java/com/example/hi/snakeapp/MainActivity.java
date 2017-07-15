package com.example.hi.snakeapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.inputmethodservice.Keyboard;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btnRight, btnStart, btnPause, btnLeft, btnUp, btnDown;
    TextView txtScore, txtHighScore, txtName;
    SnakeView snakeView;
    SnakeOpenHelper snakeOpenHelper;
    int highScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtScore = (TextView) findViewById(R.id.txtScore);
        txtHighScore = (TextView) findViewById(R.id.txtHighScore);
        snakeView = (SnakeView) findViewById(R.id.view);

        btnDown = (Button) findViewById(R.id.btnDown);
        btnUp = (Button) findViewById(R.id.btnUp);
        btnLeft = (Button) findViewById(R.id.btnLeft);
        btnRight = (Button) findViewById(R.id.btnRight);

        btnStart = (Button) findViewById(R.id.btnStart);
        btnPause = (Button) findViewById(R.id.btnPause);

        snakeOpenHelper = new SnakeOpenHelper(MainActivity.this, "snake.db", null, 1);
        SQLiteDatabase  database = snakeOpenHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from tbl_snake_score order by score desc limit 1;", null);

        if(cursor != null && cursor.getCount() >= 1){
            cursor.moveToFirst();
            highScore = cursor.getInt(cursor.getColumnIndex("score"));
        }
        txtHighScore.append(String.valueOf(highScore));
        //txtHighScore.setText("Point: 0 Topmost: " + highScore);

        snakeView.setSnakeEatFoodListener(new SnakeView.OnSnakeEatFoodListener() {
            @Override
            public void OnSnakeEatFood(int foodCount) {
                txtScore.append(String.valueOf(foodCount));
            }
        });
        snakeView.setOnSnakeDeadListener(new SnakeView.OnSnakeDeadListener() {
            @Override
            public void OnSnakeDead(final int foodCount) {

                LayoutInflater inflater = (LayoutInflater)MainActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.snake_dialog, null);
                txtName = (EditText)dialogView.findViewById(R.id.txtName);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle("Game over");
                builder.setView(dialogView);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = txtName.getText().toString();
                        SQLiteDatabase database = snakeOpenHelper.getWritableDatabase();
                        database.execSQL("insert into tbl_snake_score (name, score) values(?, ?)", new String[] {name, Integer.toString(foodCount)});
                        database.close();
                    }
                });
                builder.show();
                //txtScore.setText("Score: " + foodCount);
                //Toast.makeText(MainActivity.this, "Yo, you dead brother, dead you hear! Your lazy score is " + foodCount, Toast.LENGTH_SHORT).show();
            }
        });
        ButtonOnClickListener buttonOnClickListener = new ButtonOnClickListener();

        btnStart.setOnClickListener(buttonOnClickListener);
        btnPause.setOnClickListener(buttonOnClickListener);
        btnUp.setOnClickListener(buttonOnClickListener);
        btnDown.setOnClickListener(buttonOnClickListener);
        btnLeft.setOnClickListener(buttonOnClickListener);
        btnRight.setOnClickListener(buttonOnClickListener);
    }

    public class ButtonOnClickListener  implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnPause:
                    snakeView.PauseGame();
                    break;
                case R.id.btnStart:
                    snakeView.StartGame();
                    break;
                case R.id.btnUp:
                    snakeView.ControlGame(SnakeView.DIR_UP);
                    break;
                case R.id.btnDown:
                    snakeView.ControlGame(SnakeView.DIR_DOWN);
                    break;
                case R.id.btnLeft:
                    snakeView.ControlGame(SnakeView.DIR_LEFT);
                    break;
                case R.id.btnRight:
                    snakeView.ControlGame(SnakeView.DIR_RIGHT);
                    break;
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.view_rank){
            Intent intent = new Intent();
            intent.setClassName(MainActivity.this, "com.example.hi.snakeapp.ScoreActivity");
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
}