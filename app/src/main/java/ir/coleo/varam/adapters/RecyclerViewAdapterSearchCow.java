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

import java.util.List;

import ir.coleo.varam.R;
import ir.coleo.varam.activities.CowProfileActivity;
import ir.coleo.varam.constants.Constants;
import ir.coleo.varam.database.models.CowForMarked;

/**
 * کلاس مدیریت لیست گاو‌ها در صفحه‌ی جستجوی گاو
 */
public class RecyclerViewAdapterSearchCow extends RecyclerView.Adapter<RecyclerViewAdapterSearchCow.Holder> {

    private List<CowForMarked> cows;
    private Context context;

    public RecyclerViewAdapterSearchCow(List<CowForMarked> cows, Context context) {
        this.cows = cows;
        this.context = context;
    }

    public void setCows(List<CowForMarked> cows) {
        this.cows = cows;
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
        CowForMarked cow = cows.get(position);
        holder.cowName.setText(R.string.cow_title);
        holder.cowName.append(" " + cow.cowNumber);
        holder.farmName.setText(cow.farmName);
        holder.date.setText(cow.lastVisit.toStringWithoutYear(context));
        Constants.setImageFront(context, holder.arrow);
        holder.itemView.setOnClickListener((v) -> {
            Intent intent = new Intent(context, CowProfileActivity.class);
            intent.putExtra(Constants.COW_ID, cow.cowId);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return cows.size();
    }

    static class Holder extends RecyclerView.ViewHolder {

        TextView cowName;
        TextView farmName;
        TextView date;
        ImageView arrow;

        public Holder(@NonNull View itemView) {
            super(itemView);
            farmName = itemView.findViewById(R.id.cow_count_text);
            cowName = itemView.findViewById(R.id.cow_name);
            date = itemView.findViewById(R.id.date_text);
            arrow = itemView.findViewById(R.id.arrow);

        }
    }

}
