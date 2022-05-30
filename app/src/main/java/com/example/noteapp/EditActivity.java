package com.example.noteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.noteapp.bean.Note;
import com.example.noteapp.databinding.ActivityEditBinding;
import com.example.noteapp.util.ToastUtil;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EditActivity extends AppCompatActivity {
    private ActivityEditBinding binding;

    private Note note;
    private EditText etTitle, etContent;

    private NoteDbOpenHelper noteDbOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        etTitle = binding.etTitle;
        etContent = binding.etContent;
        
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        note = (Note) intent.getSerializableExtra("note");
        if(note != null) {
            etTitle.setText(note.getTitle());
            etContent.setText(note.getContent());
        }
        //初始化
        noteDbOpenHelper = new NoteDbOpenHelper(this);
    }

    public void save(View view) {
        String title = etTitle.getText().toString();
        String content = etContent.getText().toString();

        if(TextUtils.isEmpty(title)) {
            Snackbar.make(view,"標題不得為空！",1).show();
        }

        note.setTitle(title);
        note.setContent(content);
        note.setCreatedTime(getCurrentTimeFormat());

        long rowId = noteDbOpenHelper.updateData(note);

        //-1 說明添加成功
        if(rowId != -1) {
            ToastUtil.toastShort(this,"添加成功");
            this.finish();
        } else {
            ToastUtil.toastShort(this,"添加失敗");
        }
    }

    private String getCurrentTimeFormat() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.TAIWAN);
        Date date = new Date();
        return simpleDateFormat.format(date);
    }
}