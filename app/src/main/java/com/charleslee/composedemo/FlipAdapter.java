package com.charleslee.composedemo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class FlipAdapter extends BaseAdapter implements OnClickListener {
    /**
     * 是否循环滚动`
     */
    private final boolean isLoop;

    public interface Callback {
        public void onPageRequested(int page);
    }

    static class Item {
        long mId;
        String image;

        public Item(String image) {
            this.image = image;
        }

        long getId() {
            return mId;
        }
    }

    private Callback callback;
    private final List<Item> items = new ArrayList<>();

    public FlipAdapter() {
        this(false);
    }

    public FlipAdapter(boolean isLoop) {
        this.isLoop = isLoop;
        items.add(new Item("https://imgservice4.suning.cn/uimg1/b2c/image/uBSV4hqcXWF1U_KcDauOAg.jpg"));
        items.add(new Item("https://imgservice5.suning.cn/uimg1/b2c/image/nJTAo7atux8tBAA5-Q85Hw.jpg"));
        items.add(new Item("https://imgservice4.suning.cn/uimg1/b2c/image/k5JzW4FhYqYsKb0Loi-Yqg.jpg"));
        items.add(new Item("https://imgservice5.suning.cn/uimg1/b2c/image/cYUMUonsaHJ8CHICb2vo2A.jpg"));
        items.add(new Item("https://ww2.sinaimg.cn/mw690/007W3sbXly1hi7jj957khj30qo14010c.jpg"));
        items.add(new Item("https://t10.baidu.com/it/u=2811825913,215717797&fm=30&app=106&f=JPEG?w=640&h=640&s=71B75A7E8AA3E6452E13D9B90200700B"));
        items.add(new Item("https://img2.baidu.com/it/u=282486054,1261716498&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500"));
        items.add(new Item("https://t11.baidu.com/it/u=3009121386,218206775&fm=30&app=106&f=JPEG?w=640&h=839&s=D38041AC56A2C6EE508A000E0300F0D9"));
        items.add(new Item("https://img2.baidu.com/it/u=434325551,60311828&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500"));
        items.add(new Item("https://img1.baidu.com/it/u=1248066566,4260352468&fm=253&fmt=auto&app=120&f=JPEG?w=500&h=625"));
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public int getCount() {
        return isLoop ? 100 : items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(isLoop ? position % items.size() :position);
    }

    @Override
    public long getItemId(int position) {
        return items.get(isLoop ? position % items.size() : position).getId();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        int realPosition = isLoop ? position % items.size() : position;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.page, parent, false);

            holder.text = convertView.findViewById(R.id.text);
            holder.firstPage = convertView.findViewById(R.id.first_page);
            holder.lastPage = convertView.findViewById(R.id.last_page);
            holder.image = convertView.findViewById(R.id.image);

            holder.firstPage.setOnClickListener(this);
            holder.lastPage.setOnClickListener(this);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.text.setText(realPosition + 1 + "/" + items.size());
        // 加载图片
        Glide.with(convertView.getContext()).load(items.get(realPosition).image).into(holder.image);

        return convertView;
    }

    static class ViewHolder {
        TextView text;
        ImageView image;
        Button firstPage;
        Button lastPage;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.first_page) {
            if (callback != null) {
                callback.onPageRequested(0);
            }
        } else if (v.getId() == R.id.last_page) {
            if (callback != null) {
                callback.onPageRequested(getCount() - 1);
            }
        }
    }

    public void showTab(boolean show) {
    }

    public void addItems() {
        List<Item> temp = new ArrayList<>();
        temp.add(new Item("https://imgservice4.suning.cn/uimg1/b2c/image/uBSV4hqcXWF1U_KcDauOAg.jpg"));
        temp.add(new Item("https://imgservice5.suning.cn/uimg1/b2c/image/nJTAo7atux8tBAA5-Q85Hw.jpg"));
        temp.add(new Item("https://imgservice4.suning.cn/uimg1/b2c/image/k5JzW4FhYqYsKb0Loi-Yqg.jpg"));
        temp.add(new Item("https://imgservice5.suning.cn/uimg1/b2c/image/cYUMUonsaHJ8CHICb2vo2A.jpg"));
        temp.add(new Item("https://ww2.sinaimg.cn/mw690/007W3sbXly1hi7jj957khj30qo14010c.jpg"));
        temp.add(new Item("https://t10.baidu.com/it/u=2811825913,215717797&fm=30&app=106&f=JPEG?w=640&h=640&s=71B75A7E8AA3E6452E13D9B90200700B"));
        temp.add(new Item("https://img2.baidu.com/it/u=282486054,1261716498&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500"));
        temp.add(new Item("https://t11.baidu.com/it/u=3009121386,218206775&fm=30&app=106&f=JPEG?w=640&h=839&s=D38041AC56A2C6EE508A000E0300F0D9"));
        temp.add(new Item("https://img2.baidu.com/it/u=434325551,60311828&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500"));
        temp.add(new Item("https://img1.baidu.com/it/u=1248066566,4260352468&fm=253&fmt=auto&app=120&f=JPEG?w=500&h=625"));
        items.addAll(temp);
        notifyDataSetChanged();
    }

    public void addItemsBefore() {
        List<Item> temp = new ArrayList<>();
        temp.add(new Item("https://imgservice4.suning.cn/uimg1/b2c/image/uBSV4hqcXWF1U_KcDauOAg.jpg"));
        temp.add(new Item("https://imgservice5.suning.cn/uimg1/b2c/image/nJTAo7atux8tBAA5-Q85Hw.jpg"));
        temp.add(new Item("https://imgservice4.suning.cn/uimg1/b2c/image/k5JzW4FhYqYsKb0Loi-Yqg.jpg"));
        temp.add(new Item("https://imgservice5.suning.cn/uimg1/b2c/image/cYUMUonsaHJ8CHICb2vo2A.jpg"));
        temp.add(new Item("https://ww2.sinaimg.cn/mw690/007W3sbXly1hi7jj957khj30qo14010c.jpg"));
        temp.add(new Item("https://t10.baidu.com/it/u=2811825913,215717797&fm=30&app=106&f=JPEG?w=640&h=640&s=71B75A7E8AA3E6452E13D9B90200700B"));
        temp.add(new Item("https://img2.baidu.com/it/u=282486054,1261716498&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500"));
        temp.add(new Item("https://t11.baidu.com/it/u=3009121386,218206775&fm=30&app=106&f=JPEG?w=640&h=839&s=D38041AC56A2C6EE508A000E0300F0D9"));
        temp.add(new Item("https://img2.baidu.com/it/u=434325551,60311828&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=500"));
        temp.add(new Item("https://img1.baidu.com/it/u=1248066566,4260352468&fm=253&fmt=auto&app=120&f=JPEG?w=500&h=625"));
        items.addAll(0, temp);
        notifyDataSetChanged();
    }
}
