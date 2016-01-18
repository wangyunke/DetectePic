package com.i.detectepic.activity;

/**
 * Created by ykw on 2015/11/12.
 */

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.i.detectepic.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainAdapter extends BaseAdapter {
    private List<Map<String,String>> list;
    private Context context;

    public MainAdapter(Context context) {
        this.context = context;
        list = new ArrayList<Map<String,String>>();
    }

    public void setData(List<Map<String,String>> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }

    public void addData(List<Map<String,String>> list) {
        this.list.addAll(list);
        this.notifyDataSetChanged();
    }

    public void clearData() {
        this.list = new ArrayList<Map<String,String>>();
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.main_item, null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Map<String,String> item = list.get(position);
        if (null != item) {
            Set<Map.Entry<String, String>> set=item.entrySet();
            Iterator<Map.Entry<String, String>> iterator=set.iterator();
            while (iterator.hasNext()){
                final Map.Entry<String, String> entry=iterator.next();
                final String value=entry.getValue();

                holder.title.setText(value);
                convertView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        Intent i = new Intent(context, DetailActivity.class);
                        i.putExtra("Type",entry.getKey());
                        i.putExtra("Value",value);
                        context.startActivity(i);
                    }
                });
            }
        }
        return convertView;
    }

    class ViewHolder {
        public TextView title;
    }

}

