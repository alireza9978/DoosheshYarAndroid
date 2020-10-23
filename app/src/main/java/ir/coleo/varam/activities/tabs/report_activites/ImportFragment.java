package ir.coleo.varam.activities.tabs.report_activites;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import ir.coleo.varam.R;
import ir.coleo.varam.constants.Constants;
import ir.coleo.varam.database.DataBase;
import ir.coleo.varam.database.dao.MyDao;
import ir.coleo.varam.database.models.Cow;
import ir.coleo.varam.database.models.Farm;
import ir.coleo.varam.database.models.Report;
import ir.coleo.varam.database.utils.AppExecutors;
import ir.coleo.varam.models.MyDate;
import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;
import com.jaiselrahman.filepicker.model.MediaFile;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import static ir.coleo.varam.R.string.eight;
import static ir.coleo.varam.R.string.eleven;
import static ir.coleo.varam.R.string.five;
import static ir.coleo.varam.R.string.four;
import static ir.coleo.varam.R.string.more_info;
import static ir.coleo.varam.R.string.more_info_reason_1;
import static ir.coleo.varam.R.string.more_info_reason_2;
import static ir.coleo.varam.R.string.more_info_reason_3;
import static ir.coleo.varam.R.string.more_info_reason_4;
import static ir.coleo.varam.R.string.more_info_reason_5;
import static ir.coleo.varam.R.string.more_info_reason_6;
import static ir.coleo.varam.R.string.more_info_reason_7;
import static ir.coleo.varam.R.string.next_visit;
import static ir.coleo.varam.R.string.nine;
import static ir.coleo.varam.R.string.one;
import static ir.coleo.varam.R.string.reason_1;
import static ir.coleo.varam.R.string.reason_10;
import static ir.coleo.varam.R.string.reason_2;
import static ir.coleo.varam.R.string.reason_3;
import static ir.coleo.varam.R.string.reason_4;
import static ir.coleo.varam.R.string.reason_5;
import static ir.coleo.varam.R.string.reason_6;
import static ir.coleo.varam.R.string.reason_7;
import static ir.coleo.varam.R.string.reason_8;
import static ir.coleo.varam.R.string.reason_9;
import static ir.coleo.varam.R.string.seven;
import static ir.coleo.varam.R.string.six;
import static ir.coleo.varam.R.string.ten;
import static ir.coleo.varam.R.string.three;
import static ir.coleo.varam.R.string.twelve;
import static ir.coleo.varam.R.string.two;
import static ir.coleo.varam.R.string.zero;

public class ImportFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_import, container, false);

        ConstraintLayout button = view.findViewById(R.id.import_button);
        button.setOnClickListener(view1 -> showFileChooser());


        return view;
    }

    public void showFileChooser() {
        Intent intent = new Intent(requireContext(), FilePickerActivity.class);
        intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                .setCheckPermission(true)
                .setShowFiles(true)
                .setShowImages(false)
                .enableImageCapture(false)
                .enableVideoCapture(false)
                .setShowVideos(false)
                .enableImageCapture(true)
                .setSingleClickSelection(true)
                .setSingleChoiceMode(true)
                .setSkipZeroSizeFiles(true)
                .setSuffixes("xls", "xlsx")
                .build());

        startActivityForResult(intent, Constants.CHOOSE_FILE_REQUEST_CODE);
    }

    public void importFile(Intent intent) {
        ArrayList<MediaFile> files = intent.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);
        assert files != null;
        if (files.size() == 1) {
            MediaFile file = files.get(0);
            try {

                FileInputStream excelFile = new FileInputStream(new File(Environment.getExternalStorageDirectory().toString() + "/" + file.getBucketName(), file.getName()));
                // Create a POIFSFileSystem object
                POIFSFileSystem myFileSystem = new POIFSFileSystem(excelFile);

                // Create a workbook using the File System
                HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
                Sheet datatypeSheet = myWorkBook.getSheetAt(0);

                Integer[] headers = {R.string.cow_number, R.string.day, R.string.month, R.string.year,
                        reason_1, reason_2, reason_3,
                        reason_6, reason_7, reason_9, reason_8, reason_4,
                        reason_5, reason_10, zero, one, two, three, four, five, six, seven, eight, nine,
                        ten, eleven, twelve, more_info_reason_1, more_info_reason_2, more_info_reason_7,
                        more_info_reason_5, more_info_reason_6, more_info_reason_4, more_info_reason_3,
                        next_visit, more_info};

                //read headers
                int count = 0;
                for (Cell cell : datatypeSheet.getRow(0)) {
                    if (!cell.getStringCellValue().equals(getString(headers[count]))) {
                        Toast.makeText(requireContext(), "expected : " + getString(headers[count])
                                + " find : " + cell.getStringCellValue(), Toast.LENGTH_LONG).show();
                        return;
                    }
                    count++;
                }
                MyDao dao = DataBase.getInstance(requireContext()).dao();
                AppExecutors.getInstance().diskIO().execute(() -> {
                    Farm farm = new Farm();
                    farm.name = file.getName().substring(0, file.getName().indexOf("."));
                    farm.favorite = false;
                    farm.birthCount = 0;
                    farm.id = (int) dao.insertGetId(farm);

                    HashSet<Integer> cowNumbers = new HashSet<>();
                    ArrayList<Cow> cows = new ArrayList<>();
                    ArrayList<Report> reports = new ArrayList<>();

                    Iterator<Row> rows = datatypeSheet.iterator();
                    rows.next();
                    while (rows.hasNext()) {
                        Row row = rows.next();
                        Report report = new Report();
                        report.cowId = (int) row.getCell(0).getNumericCellValue();
                        cowNumbers.add(report.cowId);
                        report.visit = new MyDate((int) row.getCell(1).getNumericCellValue(),
                                (int) row.getCell(2).getNumericCellValue(),
                                (int) row.getCell(3).getNumericCellValue());
                        for (int i = 4; i < 14; i++) {
                            Cell cell = row.getCell(i);
                            if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                                String star = cell.getStringCellValue();
                                if (star != null && !star.isEmpty() && star.equals("*")) {

                                    switch (i) {
                                        case 4:
                                            report.referenceCauseHundredDays = true;
                                            break;
                                        case 5:
                                            report.referenceCauseDryness = true;
                                            break;
                                        case 6:
                                            report.referenceCauseLagged = true;
                                            break;
                                        case 7:
                                            report.referenceCauseNewLimp = true;
                                            break;
                                        case 8:
                                            report.referenceCauseLimpVisit = true;
                                            break;
                                        case 9:
                                            report.referenceCauseHighScore = true;
                                            break;
                                        case 10:
                                            report.referenceCauseReferential = true;
                                            break;
                                        case 11:
                                            report.referenceCauseLongHoof = true;
                                            break;
                                        case 12:
                                            report.referenceCauseHeifer = true;
                                            break;
                                        case 13:
                                            report.referenceCauseGroupHoofTrim = true;
                                            break;
                                    }
                                }
                            } else {

                                switch (i) {
                                    case 4:
                                        report.referenceCauseHundredDays = false;
                                        break;
                                    case 5:
                                        report.referenceCauseDryness = false;
                                        break;
                                    case 6:
                                        report.referenceCauseLagged = false;
                                        break;
                                    case 7:
                                        report.referenceCauseNewLimp = false;
                                        break;
                                    case 8:
                                        report.referenceCauseLimpVisit = false;
                                        break;
                                    case 9:
                                        report.referenceCauseHighScore = false;
                                        break;
                                    case 10:
                                        report.referenceCauseReferential = false;
                                        break;
                                    case 11:
                                        report.referenceCauseLongHoof = false;
                                        break;
                                    case 12:
                                        report.referenceCauseHeifer = false;
                                        break;
                                    case 13:
                                        report.referenceCauseGroupHoofTrim = false;
                                        break;
                                }
                            }
                        }
                        for (int i = 14; i < 27; i++) {
                            Cell cell = row.getCell(i);
                            if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                                report.fingerNumber = (int) cell.getNumericCellValue();
                                report.legAreaNumber = i - 14;
                                report.rightSide = report.fingerNumber % 2 == 0;
                                break;
                            }
                        }
                        for (int i = 27; i < 34; i++) {
                            Cell cell = row.getCell(i);
                            if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                                String star = cell.getStringCellValue();
                                if (star != null && !star.isEmpty() && star.equals("*")) {
                                    switch (i) {
                                        case 27:
                                            report.otherInfoWound = true;
                                            break;
                                        case 28:
                                            report.otherInfoEcchymosis = true;
                                            break;
                                        case 29:
                                            report.otherInfoHoofTrim = true;
                                            break;
                                        case 30:
                                            report.otherInfoGel = true;
                                            break;
                                        case 31:
                                            report.otherInfoBoarding = true;
                                            break;
                                        case 32:
                                            report.otherInfoNoInjury = true;
                                            break;
                                        case 33:
                                            report.otherInfoRecovered = true;
                                            break;
                                    }
                                }
                            } else {
                                switch (i) {
                                    case 27:
                                        report.otherInfoWound = false;
                                        break;
                                    case 28:
                                        report.otherInfoEcchymosis = false;
                                        break;
                                    case 29:
                                        report.otherInfoHoofTrim = false;
                                        break;
                                    case 30:
                                        report.otherInfoGel = false;
                                        break;
                                    case 31:
                                        report.otherInfoBoarding = false;
                                        break;
                                    case 32:
                                        report.otherInfoNoInjury = false;
                                        break;
                                    case 33:
                                        report.otherInfoRecovered = false;
                                        break;
                                }
                            }
                        }
                        Cell nextVisitCell = row.getCell(34);
                        if (nextVisitCell.getCellType() == Cell.CELL_TYPE_STRING) {
                            String[] date = nextVisitCell.getStringCellValue().split("/");
                            report.nextVisit = new MyDate(Integer.parseInt(date[0]),
                                    Integer.parseInt(date[1]),
                                    Integer.parseInt(date[2]));
                        }
                        Cell moreInfo = row.getCell(35);
                        if (nextVisitCell.getCellType() == Cell.CELL_TYPE_STRING) {
                            report.description = moreInfo.getStringCellValue();
                        }
                        reports.add(report);
                    }
                    for (Integer cowNumber : cowNumbers) {
                        cows.add(new Cow(cowNumber, false, farm.id));
                    }
                    for (Cow cow : cows) {
                        cow.setId((int) dao.insertGetId(cow));
                    }
                    main:
                    for (Report report : reports) {
                        for (Cow cow : cows) {
                            if (report.cowId.equals(cow.getNumber())) {
                                report.cowId = cow.getId();
                                dao.insert(report);
                                continue main;
                            }
                        }
                    }

                    requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(), "imported", Toast.LENGTH_LONG).show());
                });


            } catch (IOException e) {
                e.printStackTrace();
            }


        } else {
            Toast.makeText(requireContext(), "no file", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
    }
}