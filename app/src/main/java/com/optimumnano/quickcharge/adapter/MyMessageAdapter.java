package com.optimumnano.quickcharge.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.bean.MessageBean;

import java.util.List;


/**
 * 作者：邓传亮 on 2017/4/8 18:03
 * <p>
 * 邮箱：dengchuanliang@optimumchina.com
 */
public class MyMessageAdapter extends BaseQuickAdapter<MessageBean,BaseViewHolder> {
    public MyMessageAdapter(int layoutResId, List<MessageBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MessageBean item) {

        int position = helper.getAdapterPosition();
        TextView tvStatus = helper.getView(R.id.item_msg_list_tv);
        if (position%2==0){
            tvStatus.setText("喜欢你好久了喜欢你好久了喜欢你好久了喜欢你好喜欢你好久了喜欢你好久了喜欢你好久了喜欢你好久了喜欢你好久了喜欢你好久了久了喜欢你好久了喜欢你好久了");
        }else {
            tvStatus.setText(item.content);
        }
    }
}
