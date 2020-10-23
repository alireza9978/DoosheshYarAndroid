package ir.coleo.varam.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ir.coleo.varam.R;
import ir.coleo.varam.activities.FarmProfileActivity;
import ir.coleo.varam.constants.Constants;
import ir.coleo.varam.database.models.SearchFarm;

import java.util.List;

public class RecyclerViewAdapterSearchFarm extends RecyclerView.Adapter<RecyclerViewAdapterSearchFarm.Holder> {

    private List<SearchFarm> farms;
    private Context context;

    public RecyclerViewAdapterSearchFarm(List<SearchFarm> farms, Context context) {
        this.farms = farms;
        this.context = context;
    }

    public void setCows(List<SearchFarm> farms) {
        this.farms = farms;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cow_list_item, parent, false);
        Constants.gridRtl(context, view);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        SearchFarm farm = farms.get(position);
        holder.cowCount.setText("" + farm.cowCount);
        holder.farmName.setText(farm.farmName);
        holder.date.setText(farm.lastVisit.toStringWithoutYear(context));
        Constants.setImageFront(context, holder.arrow);
        holder.itemView.setOnClickListener((v) -> {
            Intent intent = new Intent(context, FarmProfileActivity.class);
            intent.putExtra(Constants.FARM_ID, farm.farmId);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return farms.size();
    }

    static class Holder extends RecyclerView.ViewHolder {

        TextView cowCount;
        TextView farmName;
        TextView date;
        ImageView arrow;

        public Holder(@NonNull View itemView) {
            super(itemView);
            farmName = itemView.findViewById(R.id.cow_name);
            cowCount = itemView.findViewById(R.id.cow_count_text);
            date = itemView.findViewById(R.id.date_text);
            arrow = itemView.findViewById(R.id.arrow);

        }
    }

}
