package ir.coleo.varam.activities.tabs.report_activites;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ShareCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import ir.coleo.varam.R;
import ir.coleo.varam.activities.DateSelectionActivity;
import ir.coleo.varam.activities.FarmSelectionActivity;
import ir.coleo.varam.constants.Constants;
import ir.coleo.varam.database.DataBase;
import ir.coleo.varam.database.dao.InjuryDao;
import ir.coleo.varam.database.dao.MyDao;
import ir.coleo.varam.database.models.Farm;
import ir.coleo.varam.database.models.InjureyReport;
import ir.coleo.varam.database.utils.AppExecutors;
import ir.coleo.varam.models.DateContainer;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InjuriesFragment extends Fragment {

    private ConstraintLayout farmLayout;
    private TextView farmText;

    private ConstraintLayout dateLayout;
    private TextView dateText;

    private int farmId = -1;
    private DateContainer date = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_injuries, container, false);
        farmLayout = view.findViewById(R.id.livestock_container);
        dateLayout = view.findViewById(R.id.date_container);
        farmText = view.findViewById(R.id.livestock_name_text);
        dateText = view.findViewById(R.id.date_text);

        dateLayout.setOnClickListener(view12 -> {
            Intent intent = new Intent(requireContext(), DateSelectionActivity.class);
            intent.setAction(Constants.DateSelectionMode.RANG);
            requireActivity().startActivityForResult(intent, Constants.DATE_SELECTION_REPORT_INJURY);
        });


        farmLayout.setOnClickListener(view1 -> {
            Intent intent = new Intent(requireContext(), FarmSelectionActivity.class);
            requireActivity().startActivityForResult(intent, Constants.FARM_SELECTION_REPORT_INJURY);
        });

        Button submit = view.findViewById(R.id.submit);
        submit.setOnClickListener(view13 -> {
            if (Constants.checkPermission(requireContext()))
                return;
            if (date == null) {
                return;
            }
            if (farmId == -1) {
                return;
            }

            export();
        });

        return view;
    }

    public void setDate(DateContainer date) {
        this.date = date;
        dateText.setText(date.toStringSmall(requireContext()));

        if (dateText.getText().toString().isEmpty()) {
            dateLayout.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.login_input_background));
        } else {
            dateLayout.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.black_border_background));
        }

    }

    public void setFarm(int id) {
        this.farmId = id;
        MyDao dao = DataBase.getInstance(requireContext()).dao();
        AppExecutors.getInstance().diskIO().execute(() -> {
            Farm farm = dao.getFarm(id);
            if (farm != null)
                requireActivity().runOnUiThread(() -> {
                    farmText.setText(farm.name);
                    if (farmText.getText().toString().isEmpty()) {
                        farmLayout.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.login_input_background));
                    } else {
                        farmLayout.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.black_border_background));
                    }
                });
        });
    }


    public void export() {

        InjuryDao dao = DataBase.getInstance(requireContext()).injuryDao();

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("one");

        String[] headers = {"name", "count"};
        Integer[] rowsName = {
                R.string.injury_row_0,
                R.string.injury_row_1,
                R.string.injury_row_2,
                R.string.injury_row_3,
                R.string.injury_row_4,
                R.string.injury_row_5,
                R.string.injury_row_6,
                R.string.injury_row_7
        };
        Integer[] rowsNameTwo = {
                R.string.column_name_1,
                R.string.column_name_2,
                R.string.column_name_3,
                R.string.column_name_4,
                R.string.column_name_5,
                R.string.column_name_6,
                R.string.column_name_7,
                R.string.column_name_8,
                R.string.column_name_9,
                R.string.column_name_10,
                R.string.column_name_11,
                R.string.column_name_12,
                R.string.column_name_13
        };

        AppExecutors.getInstance().diskIO().execute(() -> {
            ArrayList<Integer> arrayList = new ArrayList<>();
            arrayList.add(dao.felemons(farmId, date.exportStart(), date.exportEnd()));
            arrayList.add(dao.deramatit(farmId, date.exportStart(), date.exportEnd()));
            List<InjureyReport> bottom = dao.woundHoofBottom(farmId, date.exportStart(), date.exportEnd());
            arrayList.add(bottom.size());
            int count = 0;
            List<InjureyReport> white = dao.whiteLineWound(farmId, date.exportStart(), date.exportEnd());
            for (InjureyReport temp : white) {
                for (InjureyReport bot : bottom) {
                    if (temp.cowId.equals(bot.cowId) && temp.date.equals(bot.date) && temp.fingerNumber.equals(bot.fingerNumber)) {
                        count++;
                    }
                }
            }
            arrayList.add(white.size() - count);
            count = 0;
            List<InjureyReport> pange = dao.pangeWound(farmId, date.exportStart(), date.exportEnd());
            for (InjureyReport temp : pange) {
                for (InjureyReport bot : bottom) {
                    if (temp.cowId.equals(bot.cowId) && temp.date.equals(bot.date) && temp.fingerNumber.equals(bot.fingerNumber)) {
                        count++;
                    }
                }
            }
            arrayList.add(pange.size() - count);
            count = 0;
            List<InjureyReport> pashne = dao.pashneWound(farmId, date.exportStart(), date.exportEnd());
            for (InjureyReport temp : pashne) {
                for (InjureyReport bot : bottom) {
                    if (temp.cowId.equals(bot.cowId) && temp.date.equals(bot.date) && temp.fingerNumber.equals(bot.fingerNumber)) {
                        count++;
                    }
                }
            }
            arrayList.add(pashne.size() - count);
            arrayList.add(dao.wallWound(farmId, date.exportStart(), date.exportEnd()));
            arrayList.add(dao.reigenNine(farmId, date.exportStart(), date.exportEnd()));

            ArrayList<Double> secondPart = new ArrayList<>();
            double dayCount = date.getRange();
            secondPart.add(dao.box(farmId, date.exportStart(), date.exportEnd()).size() / dayCount);
            secondPart.add(dao.visit(farmId, date.exportStart(), date.exportEnd()).size() / dayCount);
            secondPart.add(dao.newLimp(farmId, date.exportStart(), date.exportEnd()).size() / dayCount);
            secondPart.add(dao.sadRoze(farmId, date.exportStart(), date.exportEnd()).size() / dayCount);
            secondPart.add(dao.dryness(farmId, date.exportStart(), date.exportEnd()).size() / dayCount);
            secondPart.add(dao.delayed(farmId, date.exportStart(), date.exportEnd()).size() / dayCount);
            secondPart.add(dao.group(farmId, date.exportStart(), date.exportEnd()).size() / dayCount);
            secondPart.add(dao.high(farmId, date.exportStart(), date.exportEnd()).size() / dayCount);
            secondPart.add(dao.refrence(farmId, date.exportStart(), date.exportEnd()).size() / dayCount);
            secondPart.add(dao.heifer(farmId, date.exportStart(), date.exportEnd()).size() / dayCount);
            secondPart.add(dao.longHoof(farmId, date.exportStart(), date.exportEnd()).size() / dayCount);
            secondPart.add(dao.somChini(farmId, date.exportStart(), date.exportEnd()).size() / dayCount);
            secondPart.add(dao.boarding(farmId, date.exportStart(), date.exportEnd()).size() / dayCount);

            requireActivity().runOnUiThread(() -> {
                //add headers
                Row row = sheet.createRow(0);
                for (int i = 0; i < headers.length; i++) {
                    Cell cell = row.createCell(i);
                    cell.setCellValue(headers[i]);
                }
                //add reports
                for (int i = 0; i < 8; i++) {
                    row = sheet.createRow(i + 1);
                    Cell cell = row.createCell(0);
                    cell.setCellValue(getString(rowsName[i]));
                    cell = row.createCell(1);
                    if (arrayList.get(i) == null) {
                        Log.i("report", "export: " + i);
                        cell.setCellValue(0);
                    } else {
                        cell.setCellValue(arrayList.get(i));

                    }
                }
                for (int i = 0; i < 13; i++) {
                    row = sheet.createRow(i + 9);
                    Cell cell = row.createCell(0);
                    cell.setCellValue(getString(rowsNameTwo[i]));
                    cell = row.createCell(1);
                    if (secondPart.get(i) == null) {
                        Log.i("report", "export 2 : " + i);
                        cell.setCellValue(0);
                    } else {
                        cell.setCellValue(secondPart.get(i));
                    }
                }

                try {

                    String storage = Environment.getExternalStorageDirectory().toString() + String.format("/%s.xls", "report");
                    File file = new File(storage);
                    if (file.exists()) {
                        if (file.delete()) {
                            Log.i("TAG", "export: deleted ok");
                        } else {
                            Log.i("TAG", "export: deleted fuck");
                        }
                    }
                    if (file.createNewFile()) {
                        FileOutputStream out = new FileOutputStream(file);
                        workbook.write(out);
                        out.close();
                        Log.i("TAG", "export: Excel written successfully..");
                    } else {
                        Log.i("TAG", "export: Excel written fuck..");
                    }

                    Uri uri;
                    if (Build.VERSION.SDK_INT < 24) {
                        uri = Uri.fromFile(file);
                    } else {
                        uri = FileProvider.getUriForFile(requireContext(),
                                requireContext().getApplicationContext().getPackageName() + ".provider", file);
                    }

                    Intent intent = ShareCompat.IntentBuilder.from(requireActivity())
                            .setType("*/*")
                            .setStream(uri)
                            .setChooserTitle("Choose bar")
                            .createChooserIntent()
                            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    startActivity(intent);


                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });


    }

}