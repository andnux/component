package top.andnux.libbase.web;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import top.andnux.libbase.R;

public class MoreAdapter extends BaseAdapter {

    private Context context;
    private List<String> list;

    public MoreAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
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
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_more,null);
            holder = new ViewHolder();
            holder.textView = convertView.findViewById(R.id.text1);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textView.setText(list.get(position));
        return convertView;
    }

    private static  class ViewHolder{
        protected TextView textView;
    }
}
