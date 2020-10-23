package ir.coleo.varam.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ir.coleo.varam.R;
import ir.coleo.varam.constants.Constants;
import ir.coleo.varam.database.models.NextVisit;

import java.util.List;

public class RecyclerViewAdapterNextVisitFarmProfile extends RecyclerView.Adapter<RecyclerViewAdapterNextVisitFarmProfile.Holder> {

    private List<NextVisit> nextVisits;
    private Context context;

    public RecyclerViewAdapterNextVisitFarmProfile(List<NextVisit> nextVisits, Context context) {
        this.nextVisits = nextVisits;
        this.context = context;
    }

    public void setNextVisits(List<NextVisit> nextVisits) {
        this.nextVisits = nextVisits;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.next_visit_item_farm_profile, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        NextVisit visit = nextVisits.get(position);
        holder.cowName.setText(R.string.visit_cow);
        holder.cowName.append(" " + visit.cowNumber);
        holder.date.setText(visit.visitDate.toStringWithoutYear(context));
        Constants.setImageFront(context, holder.arrow);
//        holder.itemView.setOnClickListener(view -> {
//            Intent intent = new Intent(context, AddReportActivity.class);
//            intent.putExtra(Constants.COW_ID, visit.cowId);
//            context.startActivity(intent);
//        });
    }

    @Override
    public int getItemCount() {
        return Math.min(nextVisits.size(), 3);
    }

    static class Holder extends RecyclerView.ViewHolder {

        TextView cowName;
        TextView date;
        ImageView arrow;

        public Holder(@NonNull View itemView) {
            super(itemView);
            arrow = itemView.findViewById(R.id.arrow);
            cowName = itemView.findViewById(R.id.cattel_id);
            date = itemView.findViewById(R.id.date_text);
        }
    }

}
