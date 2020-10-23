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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import static com.microsoft.appcenter.utils.HandlerUtils.runOnUiThread;


public class FactorFragment extends Fragment {

    private ConstraintLayout farmLayout;
    private TextView farmText;

    private ConstraintLayout dateLayout;
    private TextView dateText;

    private ConstraintLayout cowLayout;
    private EditText cowText;
    private ConstraintLayout priceSomLayout;
    private EditText priceSomText;
    private ConstraintLayout priceCureLayout;
    private EditText priceCureText;
    private ConstraintLayout priceBoardLayout;
    private EditText priceBoardText;

    private int farmId = -1;
    private DateContainer date = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_factor, container, false);

        farmLayout = view.findViewById(R.id.livestock_container);
        dateLayout = view.findViewById(R.id.date_container);
        cowLayout = view.findViewById(R.id.cow_number_container);
        farmText = view.findViewById(R.id.livestock_name_text);
        dateText = view.findViewById(R.id.date_text);
        cowText = view.findViewById(R.id.cow_name_text);
        priceSomLayout = view.findViewById(R.id.som_cut_price_container);
        priceCureLayout = view.findViewById(R.id.cure_price_container);
        priceBoardLayout = view.findViewById(R.id.som_bed_price_container);
        priceSomText = view.findViewById(R.id.som_cut_price_text);
        priceCureText = view.findViewById(R.id.cure_price_text);
        priceBoardText = view.findViewById(R.id.som_bed_price_text);


        dateLayout.setOnClickListener(view12 -> {
            Intent intent = new Intent(requireContext(), DateSelectionActivity.class);
            intent.setAction(Constants.DateSelectionMode.RANG);
            requireActivity().startActivityForResult(intent, Constants.DATE_SELECTION_REPORT_FACTOR);
        });


        farmLayout.setOnClickListener(view1 -> {
            Intent intent = new Intent(requireContext(), FarmSelectionActivity.class);
            requireActivity().startActivityForResult(intent, Constants.FARM_SELECTION_REPORT_FACTOR);
        });

        Button submit = view.findViewById(R.id.submit);
        submit.setOnClickListener(view1 -> {
            if (Constants.checkPermission(requireContext())) {
                return;
            }
            if (farmId == -1) {
                Toast.makeText(requireContext(), "enter farm", Toast.LENGTH_SHORT).show();
                return;
            }
            if (date == null) {
                Toast.makeText(requireContext(), "enter date", Toast.LENGTH_SHORT).show();
                return;
            }
            if (priceSomText.getText().toString().isEmpty() ||
                    priceBoardText.getText().toString().isEmpty() ||
                    priceCureText.getText().toString().isEmpty()) {
                Toast.makeText(requireContext(), "enter price", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                int[] price = new int[4];
                price[0] = Integer.parseInt(priceSomText.getText().toString());
                price[1] = Integer.parseInt(priceCureText.getText().toString());
                price[2] = Integer.parseInt(priceBoardText.getText().toString());
                price[3] = 10000;

                ArrayList<Integer> cowNumber = new ArrayList<>();
                if (!cowText.getText().toString().isEmpty()) {
                    String[] cows = cowText.getText().toString().split(",");
                    for (String cow : cows) {
                        cowNumber.add(Integer.parseInt(cow));
                    }
                }


                HSSFWorkbook workbook = new HSSFWorkbook();
                HSSFSheet sheet = workbook.createSheet("Sample sheet");

                Integer[] headers = {R.string.empty, R.string.count, R.string.price, R.string.total_price};
                Integer[] rows = {R.string.more_info_reason_7, R.string.more_info_reason_5,
                        R.string.more_info_reason_6, R.string.column_name_2, R.string.sum};

                InjuryDao dao = DataBase.getInstance(requireContext()).injuryDao();

                AppExecutors.getInstance().diskIO().execute(() -> {
                    int[] counts = {0, 0, 0, 0};
                    if (cowNumber.isEmpty()) {
                        List<Integer> temp = dao.hoofTrimFactorAll(farmId, date.exportStart(), date.exportEnd());
                        List<Integer> temp1 = dao.gelFactorAll(farmId, date.exportStart(), date.exportEnd());
                        List<Integer> temp2 = dao.boardingFactorAll(farmId, date.exportStart(), date.exportEnd());
                        List<Integer> temp3 = dao.visitFactorAll(farmId, date.exportStart(), date.exportEnd());
                        counts[0] = temp.size();
                        counts[1] = temp1.size();
                        counts[2] = temp2.size();
                        counts[3] = temp3.size();
                    } else {
                        for (Integer number : cowNumber) {
                            List<Integer> temp = dao.hoofTrimFactor(farmId, date.exportStart(), date.exportEnd(), number);
                            List<Integer> temp1 = dao.gelFactor(farmId, date.exportStart(), date.exportEnd(), number);
                            List<Integer> temp2 = dao.boardingFactor(farmId, date.exportStart(), date.exportEnd(), number);
                            List<Integer> temp3 = dao.visitFactor(farmId, date.exportStart(), date.exportEnd(), number);
                            counts[0] += temp.size();
                            counts[1] += temp1.size();
                            counts[2] += temp2.size();
                            counts[3] += temp3.size();
                        }
                    }
                    runOnUiThread(() -> {
                        Row row = sheet.createRow(0);
                        for (int i = 0; i < headers.length; i++) {
                            Cell cell = row.createCell(i);
                            cell.setCellValue(getString(headers[i]));
                        }
                        long total = 0;
                        long temp;
                        //add reports
                        for (int i = 0; i < rows.length - 1; i++) {
                            row = sheet.createRow(i + 1);

                            Cell cell = row.createCell(0);
                            cell.setCellValue(getString(rows[i]));

                            cell = row.createCell(1);
                            cell.setCellValue(counts[i]);

                            cell = row.createCell(2);
                            cell.setCellValue(price[i]);

                            cell = row.createCell(3);
                            temp = price[i] * counts[i];
                            total += temp;
                            cell.setCellValue(temp);
                        }
                        row = sheet.createRow(5);
                        Cell cell = row.createCell(0);
                        cell.setCellValue(getString(rows[4]));
                        cell = row.createCell(1);
                        cell.setCellValue(getString(headers[0]));
                        cell = row.createCell(2);
                        cell.setCellValue(getString(headers[0]));
                        cell = row.createCell(3);
                        cell.setCellValue(total);

                        String storage = Environment.getExternalStorageDirectory().toString() + String.format("/%s.xls", "factor");
                        File file = new File(storage);
                        if (file.exists()) {
                            if (file.delete()) {
                                Log.i("TAG", "export: deleted ok");
                            } else {
                                Log.i("TAG", "export: deleted fuck");
                            }
                        }
                        try {
                            if (file.createNewFile()) {
                                FileOutputStream out = new FileOutputStream(file);
                                workbook.write(out);
                                out.close();
                                Log.i("TAG", "export: Excel written successfully..");
                            } else {
                                Log.i("TAG", "export: Excel written fuck..");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Uri uri;
                        if (Build.VERSION.SDK_INT < 24) {
                            uri = Uri.fromFile(file);
                        } else {
                            uri = FileProvider.getUriForFile(requireContext(), requireContext().getApplicationContext().getPackageName() + ".provider", file);
                        }

                        Intent intent = ShareCompat.IntentBuilder.from(requireActivity())
                                .setType("*/*")
                                .setStream(uri)
                                .setChooserTitle("Choose bar")
                                .createChooserIntent()
                                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                        startActivity(intent);

                    });

                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        setBackgrounds();

        return view;
    }

    private void setBackgrounds() {
        setBackgroundChanger(cowText, cowLayout);
        setBackgroundChanger(priceBoardText, priceBoardLayout);
        setBackgroundChanger(priceCureText, priceCureLayout);
        setBackgroundChanger(priceSomText, priceSomLayout);
    }

    private void setBackgroundChanger(EditText editText, ConstraintLayout layout) {
        editText.setOnFocusChangeListener((view13, b) -> {
            if (b) {
                layout.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.black_border_background));
            } else {
                if (editText.getText().toString().isEmpty()) {
                    layout.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.login_input_background));
                } else {
                    layout.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.black_border_background));
                }
            }
        });
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

}