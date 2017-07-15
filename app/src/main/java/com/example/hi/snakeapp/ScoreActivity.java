package com.example.hi.snakeapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class ScoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        SnakeOpenHelper snakeOpenHelper = new SnakeOpenHelper(ScoreActivity.this, "snake.db", null, 1);
        SQLiteDatabase database =snakeOpenHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("select * from tbl_snake_score order by score desc limit 10;", null);

        if(cursor == null)
            return;
        ArrayList<HashMap<String, String>> maps = new ArrayList<HashMap<String, String>>();
        cursor.moveToFirst();


        for(int i = 0; i < cursor.getCount(); i++ ){
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("rank", Integer.toString(i + 1));
            map.put("name", cursor.getString(1));
            map.put("score", cursor.getString(2));
            maps.add(map);
            cursor.moveToNext();
        }

        SimpleAdapter adapter = new SimpleAdapter(ScoreActivity.this, maps, R.layout.list_item_layout, new String[] { "rank", "name", "score" }, new int[] { R.id.txtViewItemRank, R.id.txtViewItemName, R.id.txtViewItemScore });
        ListView listView = (ListView)findViewById(R.id.list_view);
        listView.setAdapter(adapter);
    }
}
