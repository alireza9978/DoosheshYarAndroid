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
import ir.coleo.varam.database.models.NextReport;

/**
 * کلاس مدیریت لیست بازدید‌های بعدی در صفحه‌ی بررسی بازدیدها در قسمت گزارش
 */
public class RecyclerViewAdapterNextVisitReport extends RecyclerView.Adapter<RecyclerViewAdapterNextVisitReport.Holder> {

    private List<NextReport> nextReports;
    private Context context;

    public RecyclerViewAdapterNextVisitReport(List<NextReport> nextReports, Context context) {
        this.nextReports = nextReports;
        this.context = context;
    }

    public void setNextReports(List<NextReport> nextReports) {
        this.nextReports = nextReports;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.next_visit_item_report, parent, false);
        Constants.gridRtl(context, view);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        NextReport report = nextReports.get(position);
        holder.cowName.setText(R.string.visit_cow);
        holder.cowName.append(" " + report.cowNumber);
        holder.farmName.setText(report.farmName);
        Constants.setImageFront(context, holder.icon);
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, CowProfileActivity.class);
            intent.putExtra(Constants.COW_ID, report.cowId);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return Math.min(nextReports.size(), 3);
    }

    static class Holder extends RecyclerView.ViewHolder {

        TextView cowName;
        TextView farmName;
        ImageView icon;

        public Holder(@NonNull View itemView) {
            super(itemView);
            farmName = itemView.findViewById(R.id.cow_count_text);
            cowName = itemView.findViewById(R.id.cattel_id);
            icon = itemView.findViewById(R.id.date_string);
        }
    }

}
