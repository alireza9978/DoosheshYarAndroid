package ir.coleo.varam.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.coleo.varam.R;
import ir.coleo.varam.activities.CowProfileActivity;
import ir.coleo.varam.activities.reports.AddReportActivity;
import ir.coleo.varam.constants.Constants;
import ir.coleo.varam.database.models.CowWithLastVisit;

/**
 * کلاس مدیریت لیست گاو‌ها در صفحه‌ی پروفایل گاوداری
 */
public class GridViewAdapterCowInFarmProfile extends RecyclerView.Adapter<GridViewAdapterCowInFarmProfile.Holder> {

    private List<CowWithLastVisit> cows;
    private Context context;
    private int farmId;

    public GridViewAdapterCowInFarmProfile(Context context, List<CowWithLastVisit> cows, int farmId) {
        this.cows = cows;
        this.context = context;
        this.farmId = farmId;
    }

    public Object getItem(int i) {
        if (cows.size() <= i) {
            return null;
        }
        return cows.get(i);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) {
            view = LayoutInflater.from(context)
                    .inflate(R.layout.add_grid_item, parent, false);
        } else {
            view = LayoutInflater.from(context)
                    .inflate(R.layout.livestock_grid_item, parent, false);
        }
        Constants.gridRtl(context, view);
        return new Holder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        if (0 == position) {
            holder.view.setOnClickListener(view1 -> {
                Intent intent = new Intent(context, AddReportActivity.class);
                intent.putExtra(Constants.REPORT_MODE, Constants.REPORT_CREATE);
                intent.putExtra(Constants.COW_ID, -1);
                intent.putExtra(Constants.FARM_ID, farmId);
                context.startActivity(intent);
            });
        } else {
            CowWithLastVisit cow = cows.get(position - 1);
            holder.view.setOnClickListener((v) -> {
                Intent intent = new Intent(context, CowProfileActivity.class);
                intent.putExtra(Constants.COW_ID, cow.getId());
                context.startActivity(intent);
            });
            holder.icon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_calendar));
            holder.farmTitle.setText(cow.getNumber());
            if (cow.getLastVisit() == null) {
                holder.cowCount.setText(R.string.no_visit_short);
            } else {
                holder.cowCount.setText(cow.getLastVisit().toStringWithoutYear(context));
            }
            holder.cowCount.setTextColor(ContextCompat.getColor(context, R.color.persian_green));
            Constants.setImageFront(context, holder.arrow);
            holder.arrow.setColorFilter(ContextCompat.getColor(context, R.color.persian_green), android.graphics.PorterDuff.Mode.SRC_IN);
            holder.icon.setColorFilter(ContextCompat.getColor(context, R.color.persian_green), android.graphics.PorterDuff.Mode.SRC_IN);
        }
    }

    @Override
    public long getItemId(int i) {
        if (cows.size() <= i) {
            return i;
        }
        return cows.get(i).getId();
    }

    @Override
    public int getItemCount() {
        return cows.size() + 1;
    }

    static class Holder extends RecyclerView.ViewHolder {
        View view;
        TextView cowCount;
        TextView farmTitle;
        ImageView icon;
        ImageView arrow;

        public Holder(@NonNull View itemView, int viewType) {
            super(itemView);
            this.view = itemView;
            if (viewType == 1) {
                this.cowCount = view.findViewById(R.id.cow_count);
                this.farmTitle = view.findViewById(R.id.farm_text);
                this.icon = view.findViewById(R.id.cow_icon);
                this.arrow = view.findViewById(R.id.arrow);
            }
        }

    }

}
