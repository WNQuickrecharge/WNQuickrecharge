package com.optimumnano.quickcharge.adapter.city;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.optimumnano.quickcharge.R;
import com.optimumnano.quickcharge.bean.CityModel;
import com.optimumnano.quickcharge.utils.PinyinUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * 作者：邓传亮 on 2017/4/26 10:33
 * <p>
 * 邮箱：dengchuanliang@optimumchina.com
 */


public class CityResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<CityModel> mCities;
    /**
     * 存放拼音首字母和下标的Map集合
     */
    private HashMap<String, Integer> letterIndexes;
    private OnCityClickListener mOnCityClickListener;
    private OnLocateClickListener mOnLocateClickListener;

    private int localState = CurrentCityState.LOCATING;
    private String localCity;
    private static final int VIEW_TYPE_FIRST = 111;
    private static final int VIEW_TYPE_SECONED = 222;
    private static final int VIEW_TYPE_THREAD = 333;

    public CityResultAdapter(Context context, @NonNull Collection<CityModel> cities) {
        mContext = context;
        mCities = (List<CityModel>) cities;
        mInflater = LayoutInflater.from(mContext);
        letterIndexes = new HashMap<>();

        Collections.sort(mCities, new PinyinComparator());

        //为集合添加
        for (int i = 0; i < mCities.size(); i++) {
            //当前城市拼音首字母
            String currentLetter = PinyinUtils.getFirstLetter(mCities.get(i).pinyin);
            //上个首字母，如果不存在设为""
            String previousLetter = i >= 1 ? PinyinUtils.getFirstLetter(mCities.get(i - 1).pinyin) : " ";
            if (!TextUtils.equals(currentLetter, previousLetter)) {
                letterIndexes.put(currentLetter, i);
            }
        }
    }

    /**
     * 更新定位状态
     *
     * @param state
     */
    public void updateLocateState(int state, String city) {
        this.localState = state;
        this.localCity = city;
        notifyDataSetChanged();
    }

    /**
     * 获取字母索引的位置
     *
     * @param letter
     * @return
     */
    public int getLetterPosition(String letter) {
        Integer integer = letterIndexes.get(letter);
        return integer == null ? -1 : integer;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
//        if (viewType == VIEW_TYPE_FIRST) {
//            view = mInflater.inflate(R.layout.rv_item_locate_city, parent, false);//view_locate_city
//            return new LocateCityHolder(view);
//        } else {
        view = mInflater.inflate(R.layout.rv_item_city, parent, false);//item_city_listview
        return new CityShowHolder(view);
        //     }
//        else if(viewType==VIEW_TYPE_SECONED){
//            view=mInflater.inflate(R.layout.view_hot_city,parent,false);
//            return new HotCityHolder(view);
//        }


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

         final CityModel  cityModel = mCities.get(position);
        ((CityShowHolder) holder).tv_item_city_listview_name.setText(cityModel.cityName);
        String currentLetter = PinyinUtils.getFirstLetter(mCities.get(position).pinyin);
        String previousLetter = position >= 1 ? PinyinUtils.getFirstLetter(mCities.get(position - 1).pinyin) : "";
        if (!TextUtils.equals(currentLetter, previousLetter)) {
            ((CityShowHolder) holder).tv_item_city_listview_letter.setVisibility(View.VISIBLE);
            ((CityShowHolder) holder).tv_item_city_listview_letter.setText(currentLetter);
        } else {
            ((CityShowHolder) holder).tv_item_city_listview_letter.setVisibility(View.GONE);
        }
        ((CityShowHolder) holder).tv_item_city_listview_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnCityClickListener != null) {
                    mOnCityClickListener.onCityClick(cityModel.cityName);
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_FIRST;
        } else if (position == 1) {
            return VIEW_TYPE_SECONED;
        }
        return VIEW_TYPE_THREAD;
    }

    @Override
    public int getItemCount() {
        return mCities.size();
    }

    static class LocateCityHolder extends RecyclerView.ViewHolder {
        LinearLayout layout_locate;
        TextView tv_located_city;

        public LocateCityHolder(View itemView) {
            super(itemView);
            layout_locate = (LinearLayout) itemView.findViewById(R.id.layout_locate);
            tv_located_city = (TextView) itemView.findViewById(R.id.tv_located_city);
        }
    }

    static class CityShowHolder extends RecyclerView.ViewHolder {
        TextView tv_item_city_listview_letter;
        TextView tv_item_city_listview_name;

        public CityShowHolder(View itemView) {
            super(itemView);
            tv_item_city_listview_name = (TextView) itemView.findViewById(R.id.tv_item_city_listview_name);
            tv_item_city_listview_letter = (TextView) itemView.findViewById(R.id.tv_item_city_listview_letter);
        }
    }


    public void setOnLocateClickListener(OnLocateClickListener onLocateClickListener) {
        mOnLocateClickListener = onLocateClickListener;
    }

    public void setOnCityClickListener(OnCityClickListener onCityClickListener) {
        mOnCityClickListener = onCityClickListener;
    }

    /**
     * 用来对ListView中的数据根据A-Z进行排序，前面两个if判断主要是将不是以汉字开头的数据放在后面
     */
    public class PinyinComparator implements Comparator<CityModel> {
        public int compare(CityModel o1, CityModel o2) {
            //这里主要是用来对ListView里面的数据根据ABCDEFG...来排序
            String str1 = PinyinUtils.getFirstLetter(o1.pinyin);
            String str2 = PinyinUtils.getFirstLetter(o2.pinyin);
            if (str2.equals("#")) {
                return -1;
            } else if (str1.equals("#")) {
                return 1;
            } else {
                return str1.compareTo(str2);
            }
        }
    }
}

