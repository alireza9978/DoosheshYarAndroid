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
import ir.coleo.varam.activities.DrugSelectionActivity;
import ir.coleo.varam.database.models.main.Drug;

public class RecyclerViewAdapterDrugSimple extends RecyclerView.Adapter<RecyclerViewAdapterDrugSimple.Holder> {

    private List<Drug> drugs;
    private Context context;

    public RecyclerViewAdapterDrugSimple(List<Drug> drugs, Context context) {
        this.drugs = drugs;
        this.context = context;
    }

    public void setCows(List<Drug> drugs) {
        this.drugs = drugs;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.livestroke_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Drug drug = drugs.get(position);
        holder.farmName.setText(drug.name);
        holder.itemView.setOnClickListener(view -> ((DrugSelectionActivity) context).selectedDrug(drug.id));
    }

    @Override
    public int getItemCount() {
        return drugs.size();
    }

    static class Holder extends RecyclerView.ViewHolder {

        TextView farmName;

        public Holder(@NonNull View itemView) {
            super(itemView);
            farmName = itemView.findViewById(R.id.drug_title);
        }
    }

}
