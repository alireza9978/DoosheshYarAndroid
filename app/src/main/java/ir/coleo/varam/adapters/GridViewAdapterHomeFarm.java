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

import java.util.List;

import ir.coleo.varam.R;
import ir.coleo.varam.activities.FarmProfileActivity;
import ir.coleo.varam.constants.Constants;
import ir.coleo.varam.database.models.FarmWithCowCount;

/**
 * کلاس مدیریت لیست گاوداری‌ها در صفحه‌ی خانه
 */
public class GridViewAdapterHomeFarm extends BaseAdapter {

    private List<FarmWithCowCount> farms;
    private Context context;

    public GridViewAdapterHomeFarm(Context context, List<FarmWithCowCount> farms) {
        this.farms = farms;
        this.context = context;
    }

    public void setFarms(List<FarmWithCowCount> farms) {
        this.farms = farms;
    }

    @Override
    public int getCount() {
        return farms.size();
    }

    @Override
    public Object getItem(int i) {
        return farms.get(i);
    }

    @Override
    public long getItemId(int i) {
        return farms.get(i).farmId;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder;
        FarmWithCowCount farm = farms.get(i);
        if (view == null) {
            view = LayoutInflater.from(context)
                    .inflate(R.layout.livestock_grid_item, viewGroup, false);

            holder = new Holder();
            holder.view = view;
            holder.arrow = view.findViewById(R.id.arrow);
            holder.cowCount = view.findViewById(R.id.cow_count);
            holder.farmTitle = view.findViewById(R.id.farm_text);
            holder.icon = view.findViewById(R.id.cow_icon);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        holder.view.setOnClickListener((v) -> {
            Intent intent = new Intent(context, FarmProfileActivity.class);
            intent.putExtra(Constants.FARM_ID, farm.farmId);
            context.startActivity(intent);
        });
        holder.farmTitle.setText(farm.farmName);
        holder.cowCount.setText("" + farm.cowCount);
        Constants.setImageFront(context, holder.arrow);
        holder.icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_cow));
        Constants.gridRtl(context, view);

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
