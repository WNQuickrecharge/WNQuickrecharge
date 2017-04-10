package com.optimumnano.quickcharge.dialog;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.base.BaseDialog;

/**
 * Created by ds on 2017/4/8.
 */

public class CommentDialog extends BaseDialog {

    public CommentDialog(Activity mAty) {
        super(mAty);
        dialog.getViewHolder().getView(R.id.comment_tvCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });
    }

    @Override
    protected int getContentViewID() {
        return R.layout.dialog_comment;
    }
    public void setConfirmListener(View.OnClickListener i){
        dialog.getViewHolder().getView(R.id.dialog_comment_tvComment).setOnClickListener(i);
    }
    public String getComment(){
        EditText editText = dialog.getViewHolder().getView(R.id.dialog_comment_edtComment);
        return editText.getText().toString();
    }

    public int getRatingCorn(){
        RatingBar ratingBar = dialog.getViewHolder().getView(R.id.comment_rb);
        return (int) ratingBar.getRating();
    }
}
