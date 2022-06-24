package com.example.noteapp.adapter;

import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteapp.EditActivity;
import com.example.noteapp.NoteDbOpenHelper;
import com.example.noteapp.bean.Note;
import com.example.noteapp.databinding.ListItemDialogLayoutBinding;
import com.example.noteapp.databinding.ListItemGridBinding;
import com.example.noteapp.databinding.ListItemLayoutBinding;
import com.example.noteapp.util.ToastUtil;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Note> mNoteList;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ListItemDialogLayoutBinding listItemBinging;
    private NoteDbOpenHelper mNoteDbOpenHelper;
    private Vibrator vibrator;
    private int viewType;

    public static int TYPE_LINEAR_LAYOUT = 0;
    public static int TYPE_GRID_LAYOUT = 1;

    public MyAdapter(List<Note> mNoteList, Context mContext) {
        this.mNoteList = mNoteList;
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        mNoteDbOpenHelper = new NoteDbOpenHelper(mContext);
        vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    @Override
    public int getItemViewType(int position) {
        return viewType;
    }

    public void refreshData(List<Note> note) {
        this.mNoteList = note;
        //通知資料重新刷新
        notifyDataSetChanged();
    }

    //viewType用於綁定多種視圖
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == TYPE_LINEAR_LAYOUT) {
            ListItemLayoutBinding binding = ListItemLayoutBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false);
            return new MyViewHolder(binding);
        } else if(viewType == TYPE_GRID_LAYOUT){
            ListItemGridBinding bindingGrid = ListItemGridBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false);
            return new MyGridViewHolder(bindingGrid);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder == null) {
            return;
        }
        if(holder instanceof MyViewHolder) {
            bindMyViewHolder((MyViewHolder) holder, position);
        } else if(holder instanceof MyGridViewHolder) {
            bindGridViewHolder((MyGridViewHolder) holder, position);
        }
    }

    private void bindMyViewHolder(MyViewHolder holder, int position) {
        Note note = mNoteList.get(position);
        holder.mTvTitle.setText(note.getTitle());
        holder.mTvContent.setText(note.getContent());
        holder.mTvTime.setText(note.getCreatedTime());
        holder.rlContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, EditActivity.class);
                intent.putExtra("note", note);
                mContext.startActivity(intent);
            }
        });

        holder.rlContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (vibrator.hasVibrator()){
                    vibrator.vibrate(VibrationEffect.createOneShot(100,VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    vibrator.vibrate(500);
                }

                //長按彈出視窗
                Dialog dialog = new Dialog(mContext, android.R.style.ThemeOverlay_Material_Dialog_Alert);
                listItemBinging = ListItemDialogLayoutBinding.inflate(LayoutInflater.from(view.getContext()));
                //View v = mLayoutInflater.inflate(R.layout.list_item_dialog_layout, null);
                View v = listItemBinging.getRoot();
                TextView tvDelete = listItemBinging.tvDelete;
                TextView tvEdit = listItemBinging.tvEdit;

                tvDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int row = mNoteDbOpenHelper.deleteFromDbById(note.getId());
                        if (row > 0) {
                            removeData(position);
                            ToastUtil.toastShort(mContext,"刪除成功");
                        } else {
                            ToastUtil.toastShort(mContext,"刪除失敗");
                        }
                        dialog.dismiss();
                    }
                });

                tvEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext,EditActivity.class);
                        intent.putExtra("note", note);
                        mContext.startActivity(intent);
                        dialog.dismiss();
                    }
                });

                dialog.setContentView(v);
                dialog.show();
                return false;
            }
        });
    }

    private void bindGridViewHolder(MyGridViewHolder holder, int position) {
        Note note = mNoteList.get(position);
        holder.mTvTitle.setText(note.getTitle());
        holder.mTvContent.setText(note.getContent());
        holder.mTvTime.setText(note.getCreatedTime());
        holder.rlContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, EditActivity.class);
                intent.putExtra("note", note);
                mContext.startActivity(intent);
            }
        });

        holder.rlContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (vibrator.hasVibrator()){
                    vibrator.vibrate(VibrationEffect.createOneShot(100,VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    vibrator.vibrate(500);
                }

                //長按彈出視窗
                Dialog dialog = new Dialog(mContext, android.R.style.ThemeOverlay_Material_Dialog_Alert);
                listItemBinging = ListItemDialogLayoutBinding.inflate(LayoutInflater.from(view.getContext()));
                //View v = mLayoutInflater.inflate(R.layout.list_item_dialog_layout, null);
                View v = listItemBinging.getRoot();
                TextView tvDelete = listItemBinging.tvDelete;
                TextView tvEdit = listItemBinging.tvEdit;

                tvDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int row = mNoteDbOpenHelper.deleteFromDbById(note.getId());
                        if (row > 0) {
                            removeData(position);
                            ToastUtil.toastShort(mContext,"刪除成功");
                        } else {
                            ToastUtil.toastShort(mContext,"刪除失敗");
                        }
                        dialog.dismiss();
                    }
                });

                tvEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext,EditActivity.class);
                        intent.putExtra("note", note);
                        mContext.startActivity(intent);
                        dialog.dismiss();
                    }
                });

                dialog.setContentView(v);
                dialog.show();
                return false;
            }
        });
    }

    public void removeData(int pos) {
        mNoteList.remove(pos);
        //通知資料重新刷新
        notifyItemRemoved(pos);
    }

    @Override
    public int getItemCount() {
        return mNoteList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView mTvTitle;
        TextView mTvContent;
        TextView mTvTime;
        ViewGroup rlContainer;

        public MyViewHolder(@NonNull ListItemLayoutBinding binding) {
            super(binding.getRoot());
            rlContainer = binding.rlItemContainer;
            mTvTitle = binding.tvTitle;
            mTvContent = binding.tvContent;
            mTvTime = binding.tvTime;
        }
    }

    public class MyGridViewHolder extends RecyclerView.ViewHolder{

        TextView mTvTitle;
        TextView mTvContent;
        TextView mTvTime;
        ViewGroup rlContainer;

        public MyGridViewHolder(@NonNull ListItemGridBinding binding) {
            super(binding.getRoot());
            rlContainer = binding.rlItemContainer;
            mTvTitle = binding.tvTitle;
            mTvContent = binding.tvContent;
            mTvTime = binding.tvTime;
        }
    }
}
