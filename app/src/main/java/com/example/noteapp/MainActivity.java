package com.example.noteapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.example.noteapp.adapter.MyAdapter;
import com.example.noteapp.bean.Note;
import com.example.noteapp.databinding.ActivityMainBinding;
import com.example.noteapp.util.SpfUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private List<Note> mNote;
    private MyAdapter mMyAdapter;
    private NoteDbOpenHelper mNoteDbOpenHelper;

    private RecyclerView myRV;

    public static int MODE_LINEAR = 0;
    public static int MODE_GRID = 1;

    public static final String KEY_LAYOUT_MODE = "key_layout_mode";
    public int currentListLayoutMode = MODE_LINEAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        initView();
        initData();
        initEvent();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshDataFromDb();
        setListLayout();
    }

    private void setListLayout() {
        currentListLayoutMode = SpfUtil.getIntWithDefault(this, KEY_LAYOUT_MODE, MODE_LINEAR);
        if(currentListLayoutMode == MODE_LINEAR) {
            setToLinearList();
        } else {
            setToGridList();
        }
    }

    private void setToGridList() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        myRV.setLayoutManager(gridLayoutManager);
        mMyAdapter.setViewType(MyAdapter.TYPE_GRID_LAYOUT);
        mMyAdapter.notifyDataSetChanged();
    }

    private void setToLinearList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        myRV.setLayoutManager(linearLayoutManager);
        mMyAdapter.setViewType(MyAdapter.TYPE_LINEAR_LAYOUT);
        mMyAdapter.notifyDataSetChanged();
    }

    private void refreshDataFromDb() {
        mNote = getDataFromDB();
        mMyAdapter.refreshData(mNote);
    }

    private void initEvent() {
        mMyAdapter = new MyAdapter(mNote,this);
        myRV.setAdapter(mMyAdapter);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        myRV.setLayoutManager(linearLayoutManager);
//        mMyAdapter.setViewType(MyAdapter.TYPE_LINEAR_LAYOUT);
        setListLayout();
    }

    private void initData() {
        mNote = new ArrayList<>();
        mNoteDbOpenHelper = new NoteDbOpenHelper(this);

        //mNote = getDataFromDB();
    }

    private List<Note> getDataFromDB() {
        return mNoteDbOpenHelper.queryAllFromDB();
    }

    private String getCurrentTimeFormat() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.TAIWAN);
        Date date = new Date();
        return simpleDateFormat.format(date);
    }

    private void initView() {
        myRV = binding.rlv;
        //myRV.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
    }

    public void add(View view) {
        Intent intent = new Intent(this,AddActivity.class);
        startActivity(intent);
    }

    //此方法用於初始化選單，其中menu參數就是即將要顯示的Menu例項
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // Get SearchView and set the searchable configuration
        // SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();

        // 被查詢時的監聽器
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 當輸入查詢、點擊搜索的時候
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            // 每次打字時，文本改變時
            @Override
            public boolean onQueryTextChange(String newText) {
                mNote = mNoteDbOpenHelper.queryFromDbByTitle(newText);
                mMyAdapter.refreshData(mNote);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    // 選單項被點選時呼叫，也就是選單項的監聽方法
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        switch(item.getItemId()) {
            case R.id.menu_linear:
                setToLinearList();
                currentListLayoutMode = MODE_LINEAR;
                SpfUtil.saveInt(this, KEY_LAYOUT_MODE, MODE_LINEAR);
                return true;

            case R.id.menu_grid:
                setToGridList();
                currentListLayoutMode = MODE_GRID;
                SpfUtil.saveInt(this, KEY_LAYOUT_MODE, MODE_GRID);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //onPrepareOptionsMenu是每次在display menu之前，都會去呼叫，只要按一次menu按鍵，就會呼叫一次。
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(currentListLayoutMode == MODE_LINEAR) {
            MenuItem menuItem = menu.findItem(R.id.menu_linear);
            menuItem.setChecked(true);
        } else {
            menu.findItem(R.id.menu_grid).setChecked(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }
}