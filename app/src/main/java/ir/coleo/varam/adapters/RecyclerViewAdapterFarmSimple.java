package ir.coleo.varam.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.coleo.varam.R;
import ir.coleo.varam.activities.FarmSelectionActivity;
import ir.coleo.varam.database.models.main.Farm;

/**
 * کلاس مدیریت لیست گاوداری‌ها در صفحه‌ی انتخاب گاوداری
 */
public class RecyclerViewAdapterFarmSimple extends RecyclerView.Adapter<RecyclerViewAdapterFarmSimple.Holder> {

    private List<Farm> farms;
    private Context context;

    public RecyclerViewAdapterFarmSimple(List<Farm> farms, Context context) {
        this.farms = farms;
        this.context = context;
    }

    public void setCows(List<Farm> farms) {
        this.farms = farms;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.livestroke_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Farm farm = farms.get(position);
        holder.farmName.setText(farm.name);
        holder.itemView.setOnClickListener(view -> ((FarmSelectionActivity) context).selectedFarm(farm.id));
    }

    @Override
    public int getItemCount() {
        return farms.size();
    }

    static class Holder extends RecyclerView.ViewHolder {

        TextView farmName;

        public Holder(@NonNull View itemView) {
            super(itemView);
            farmName = itemView.findViewById(R.id.drug_title);
        }
    }

}
