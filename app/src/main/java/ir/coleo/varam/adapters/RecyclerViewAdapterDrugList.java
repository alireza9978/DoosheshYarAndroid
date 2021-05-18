package ir.coleo.varam.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.coleo.varam.R;
import ir.coleo.varam.activities.menu.AddDrugActivity;
import ir.coleo.varam.database.models.main.Drug;

/**
 * کلاس مدیریت لیست دارو‌ها در صفحه‌ی افزود دارو‌ی جدید
 */
public class RecyclerViewAdapterDrugList extends RecyclerView.Adapter<RecyclerViewAdapterDrugList.Holder> {

    private List<Drug> drugs;
    private Context context;

    public RecyclerViewAdapterDrugList(Context context) {
        this.context = context;
    }

    public RecyclerViewAdapterDrugList(List<Drug> drugs, Context context) {
        this.drugs = drugs;
        this.context = context;
    }

    public void setDrugs(List<Drug> drugs) {
        this.drugs = drugs;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.make_drug_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Drug drug = drugs.get(position);
        holder.drugName.setText(drug.name);
        holder.remove.setOnClickListener(view -> {
            ((AddDrugActivity) context).delete(drug);
        });
    }

    @Override
    public int getItemCount() {
        return drugs.size();
    }

    static class Holder extends RecyclerView.ViewHolder {

        TextView drugName;
        ImageView remove;

        public Holder(@NonNull View itemView) {
            super(itemView);
            drugName = itemView.findViewById(R.id.drug_title);
            remove = itemView.findViewById(R.id.remove_button);
        }
    }

}
