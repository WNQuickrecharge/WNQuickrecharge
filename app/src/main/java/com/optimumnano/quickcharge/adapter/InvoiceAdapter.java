package com.optimumnano.quickcharge.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.bean.InvoiceOrder;
import com.optimumnano.quickcharge.bean.InvoiceOrderGroup;

import java.util.List;

/**
 * Created by ds on 2017/4/22.
 */

public class InvoiceAdapter extends BaseExpandableListAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<InvoiceOrderGroup> parent;
    private List<List<InvoiceOrder>> child;
    public InvoiceAdapter(Context context, List<List<InvoiceOrder>> child, List<InvoiceOrderGroup> parent) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.child = child;
        this.parent = parent;
    }
    private OnCheckListener listener;
    private OnChildCheckListener childCheckListener;
    public void setOnChecked(OnCheckListener listener){
        this.listener = listener;
    }
    public void setOnChildChecked(OnChildCheckListener childCheckListener){
        this.childCheckListener = childCheckListener;
    }

    @Override
    public int getGroupCount() {
        return parent.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return child.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return parent.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return child.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup group) {
        ViewHolderParent holder = null;
        if (convertView == null) {
            holder = new ViewHolderParent();
            convertView = inflater.inflate(R.layout.adapter_invoice_parent, null);
            holder.cb = (ImageView) convertView.findViewById(R.id.invoice_cb);
            holder.tvMoney = (TextView) convertView.findViewById(R.id.invoice_tvMoneyParent);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.invoice_tvTitleParent);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolderParent) convertView.getTag();
        }
        InvoiceOrderGroup orderGroup = (InvoiceOrderGroup) getGroup(groupPosition);
        holder.tvTitle.setText(orderGroup.ConsumeMonth);
        holder.tvMoney.setText("￥"+orderGroup.money);
        if (orderGroup.isChecked){
            holder.cb.setImageResource(R.drawable.invoice_checked);
        }
        else {
            holder.cb.setImageResource(R.drawable.invoice_normal);
        }
        holder.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCheck(groupPosition);
            }
        });
        return convertView;
    }

    //【重要】填充二级列表
    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolderChild holder = null;
        if (convertView == null) {
            holder = new ViewHolderChild();
            convertView = inflater.inflate(R.layout.adapter_invoice_child, null);
            holder.cb = (CheckBox) convertView.findViewById(R.id.invoice_cb_child);
            holder.tvOrderNo = (TextView) convertView.findViewById(R.id.invoice_tvOrdernum);
            holder.TvMoney = (TextView) convertView.findViewById(R.id.invoice_tvMoney);
            holder.tvAddress = (TextView) convertView.findViewById(R.id.invoice_tvCompany);
            holder.tvDate = (TextView) convertView.findViewById(R.id.invoice_tvTime);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolderChild) convertView.getTag();
        }
        final InvoiceOrder order = (InvoiceOrder) getChild(groupPosition,childPosition);
        holder.tvOrderNo.setText(order.C_ChargeOrderNum+"");
        holder.tvAddress.setText(order.Address+"");
        holder.TvMoney.setText("￥"+order.ConsumeCash);
        holder.cb.setChecked(order.isChecked);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ToastUtil.showToast(context,"item"+childPosition);
                childCheckListener.onChildCheck(childPosition,order.Id);
            }
        });
        return convertView;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
    class ViewHolderChild{
        CheckBox cb;
        TextView tvOrderNo,TvMoney,tvAddress,tvDate;
    }
    class ViewHolderParent{
        TextView tvTitle,tvMoney;
        ImageView cb;
    }

    public interface OnCheckListener{
        void onCheck(int position);
    }
    public interface OnChildCheckListener{
        void onChildCheck(int position, int ids);

    }
}
