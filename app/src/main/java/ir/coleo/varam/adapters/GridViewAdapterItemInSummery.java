package ir.coleo.varam.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ir.coleo.varam.R;

/**
 * کلاس مدیریت لیست ایتم‌ها در صفحه‌ی بررسی گزارش
 */
public class GridViewAdapterItemInSummery extends BaseAdapter {

    private List<String> items;
    private Context context;

    public GridViewAdapterItemInSummery(Context context, List<String> farms) {
        this.items = farms;
        this.context = context;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder;
        String item = items.get(i);
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.report_item, viewGroup, false);
            holder = new Holder();
            holder.view = view;
            holder.text = view.findViewById(R.id.text);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        holder.text.setText(item);
        return view;
    }

    static class Holder {
        View view;
        TextView text;
    }

}
