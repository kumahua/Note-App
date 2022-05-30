package com.example.noteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.noteapp.bean.Note;
import com.example.noteapp.databinding.ActivityAddBinding;
import com.example.noteapp.util.ToastUtil;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddActivity extends AppCompatActivity {

    private ActivityAddBinding binding;
    private EditText etTitle, etContent;
    private NoteDbOpenHelper noteDbOpenHelper;
    private SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        etContent = binding.etContent;
        etTitle = binding.etTitle;

        noteDbOpenHelper = new NoteDbOpenHelper(this);
    }

    private String getCurrentTimeFormat() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.TAIWAN);
        Date date = new Date();
        return simpleDateFormat.format(date);
    }

    public void add(View view) {
        String title = etTitle.getText().toString();
        String content = etContent.getText().toString();
        if(TextUtils.isEmpty(title)) {
            Snackbar.make(view,"標題不得為空！",1).show();
            return;
        }

        Note note = new Note();
        note.setTitle(title);
        note.setContent(content);
        note.setCreatedTime(getCurrentTimeFormat());

        long row = noteDbOpenHelper.insertData(note);

        Log.e("row", String.valueOf(row));

        //-1 說明添加成功
        if(row != -1) {
            ToastUtil.toastShort(this,"添加成功");
            this.finish();
        } else {
            ToastUtil.toastShort(this,"添加失敗");
        }

    }


}