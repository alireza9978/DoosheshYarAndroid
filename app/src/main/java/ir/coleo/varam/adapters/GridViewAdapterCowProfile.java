package ir.coleo.varam.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import ir.coleo.varam.R;
import ir.coleo.varam.activities.reports.AddReportActivity;
import ir.coleo.varam.activities.reports.ReportSummery;
import ir.coleo.varam.constants.Constants;
import ir.coleo.varam.database.models.main.Report;

import java.util.List;

public class GridViewAdapterCowProfile extends BaseAdapter {

    private List<Report> reports;
    private Context context;
    private int cowId;

    public GridViewAdapterCowProfile(Context context, List<Report> reports, int cowId) {
        this.reports = reports;
        this.context = context;
        this.cowId = cowId;
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
    }

    @Override
    public int getCount() {
        return reports.size() + 1;
    }

    @Override
    public Object getItem(int i) {
        return reports.get(i);
    }

    @Override
    public long getItemId(int i) {
        return reports.get(i).id;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (reports.size() == i) {
            HolderAdd holder;
            if (view == null) {
                view = LayoutInflater.from(context)
                        .inflate(R.layout.cow_report_item_add, viewGroup, false);

                holder = new HolderAdd();
                holder.view = view;
                view.setTag(holder);
            } else {
                holder = (HolderAdd) view.getTag();
            }
            holder.view.setOnClickListener(view1 -> {
                Intent intent = new Intent(context, AddReportActivity.class);
                intent.putExtra(Constants.REPORT_MODE, Constants.REPORT_CREATE);
                intent.putExtra(Constants.COW_ID, cowId);
                context.startActivity(intent);
            });
        } else {
            Holder holder;
            Report report = reports.get(i);
            if (view == null) {
                view = LayoutInflater.from(context)
                        .inflate(R.layout.cow_report_item, viewGroup, false);
                Constants.gridRtl(context, view);

                holder = new Holder();
                holder.view = view;
                holder.title = view.findViewById(R.id.report_name);
                holder.arrow = view.findViewById(R.id.arrow);
                view.setTag(holder);
            } else {
                holder = (Holder) view.getTag();
            }

            Constants.setImageFront(context, holder.arrow);
            holder.view.setOnClickListener((v) -> {
                Intent intent = new Intent(context, ReportSummery.class);
                intent.putExtra(Constants.REPORT_ID, report.id);
                context.startActivity(intent);
            });
            holder.title.setText(R.string.report);
            holder.title.append(" " + report.id);
        }
        return view;
    }

    static class HolderAdd {
        View view;
    }

    static class Holder {
        View view;
        TextView title;
        ImageView arrow;
    }

}
