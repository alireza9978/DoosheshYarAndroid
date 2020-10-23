package ir.coleo.varam.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import ir.coleo.varam.R;
import ir.coleo.varam.activities.CowProfileActivity;
import ir.coleo.varam.activities.reports.AddReportActivity;
import ir.coleo.varam.constants.Constants;
import ir.coleo.varam.database.models.CowWithLastVisit;

import java.util.List;

public class GridViewAdapterCowInFarmProfile extends BaseAdapter {

    private List<CowWithLastVisit> cows;
    private Context context;
    private int farmId;

    public GridViewAdapterCowInFarmProfile(Context context, List<CowWithLastVisit> cows, int farmId) {
        this.cows = cows;
        this.context = context;
        this.farmId = farmId;
    }

    @Override
    public int getCount() {
        return cows.size() + 1;
    }

    @Override
    public Object getItem(int i) {
        if (cows.size() <= i) {
            return null;
        }
        return cows.get(i);
    }

    @Override
    public long getItemId(int i) {
        if (cows.size() <= i) {
            return i;
        }
        return cows.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder;
        if (cows.size() == i) {
            view = LayoutInflater.from(context)
                    .inflate(R.layout.add_grid_item, viewGroup, false);
            Constants.gridRtl(context, view);
            view.setOnClickListener(view1 -> {
                Intent intent = new Intent(context, AddReportActivity.class);
                intent.putExtra(Constants.REPORT_MODE, Constants.REPORT_CREATE);
                intent.putExtra(Constants.COW_ID, -1);
                intent.putExtra(Constants.FARM_ID, farmId);
                context.startActivity(intent);
            });
        } else {
            CowWithLastVisit cow = cows.get(i);
            if (view == null) {
                view = LayoutInflater.from(context)
                        .inflate(R.layout.livestock_grid_item, viewGroup, false);
                Constants.gridRtl(context, view);
                holder = new Holder();
                holder.view = view;
                holder.cowCount = view.findViewById(R.id.cow_count);
                holder.farmTitle = view.findViewById(R.id.farm_text);
                holder.icon = view.findViewById(R.id.cow_icon);
                holder.arrow = view.findViewById(R.id.arrow);
                view.setTag(holder);
            } else {
                holder = (Holder) view.getTag();
            }

            holder.view.setOnClickListener((v) -> {
                Intent intent = new Intent(context, CowProfileActivity.class);
                intent.putExtra(Constants.COW_ID, cow.getId());
                context.startActivity(intent);
            });
            holder.icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_calendar));
            holder.farmTitle.setText(cow.getNumber(context));
            if (cow.getLastVisit() == null) {
                holder.cowCount.setText(R.string.no_visit_short);
                holder.cowCount.setTextColor(ContextCompat.getColor(context, R.color.persian_green));
            } else {
                holder.cowCount.setText(cow.getLastVisit().toStringWithoutYear(context));
                holder.cowCount.setTextColor(ContextCompat.getColor(context, R.color.persian_green));
            }
            Constants.setImageFront(context, holder.arrow);
            holder.arrow.setColorFilter(ContextCompat.getColor(context, R.color.persian_green), android.graphics.PorterDuff.Mode.SRC_IN);
            holder.icon.setColorFilter(ContextCompat.getColor(context, R.color.persian_green), android.graphics.PorterDuff.Mode.SRC_IN);
        }
        return view;
    }

    static class Holder {
        View view;
        TextView cowCount;
        TextView farmTitle;
        ImageView icon;
        ImageView arrow;
    }

}
