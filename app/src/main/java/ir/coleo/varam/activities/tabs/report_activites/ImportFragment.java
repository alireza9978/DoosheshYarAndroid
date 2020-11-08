package ir.coleo.varam.activities.tabs.report_activites;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import ir.coleo.varam.R;
import ir.coleo.varam.constants.Constants;
import ir.coleo.varam.database.DataBase;
import ir.coleo.varam.database.dao.MyDao;
import ir.coleo.varam.database.models.main.Cow;
import ir.coleo.varam.database.models.main.Drug;
import ir.coleo.varam.database.models.main.Farm;
import ir.coleo.varam.database.models.main.Report;
import ir.coleo.varam.database.utils.AppExecutors;
import ir.coleo.varam.models.MyDate;

import static ir.coleo.varam.R.string.more_info;
import static ir.coleo.varam.R.string.next_visit;

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
        if (Constants.checkPermissionRead(requireContext())) {
            return;
        }

        Intent chooseFile = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
        chooseFile.setType("application/*");
        chooseFile = Intent.createChooser(chooseFile, "Choose a file");
        startActivityForResult(chooseFile, Constants.CHOOSE_FILE_REQUEST_CODE);
    }

    public void importFile(Intent intent) {


        try {
            if (intent == null || intent.getData() == null) {
                return;
            }
            Uri fileUri = intent.getData();
            InputStream stream;
            try {
                if (fileUri == null) {
                    Toast.makeText(requireContext(), "no file selected", Toast.LENGTH_SHORT).show();
                    return;
                }
                stream = requireContext().getContentResolver().openInputStream(fileUri);
            } catch (FileNotFoundException e) {
                Toast.makeText(requireContext(), "file not found", Toast.LENGTH_SHORT).show();
                return;
            }

            POIFSFileSystem myFileSystem = new POIFSFileSystem(stream);

            // Create a workbook using the File System
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
            Sheet dataTypeSheet = myWorkBook.getSheetAt(0);

            Integer[] headers = {R.string.cow_number, R.string.day, R.string.month, R.string.year,
                    R.string.injury_area, R.string.score_type, R.string.score, R.string.score_zero,
                    R.string.score_one, R.string.score_two, R.string.cartie_state, R.string.drug_title_1,
                    R.string.drug_title_2, R.string.drug_title_3, R.string.drug_title_4,
                    R.string.drug_title_5, next_visit, more_info};
            Integer[] threeLevel = {R.string.score_three_one, R.string.score_three_two,
                    R.string.score_three_three, R.string.score_three_four};
            Integer[] fourLevel = {R.string.score_four_one, R.string.score_four_two,
                    R.string.score_four_three, R.string.score_four_four};
            Integer[] cartieState = {R.string.cartie_one, R.string.cartie_two,
                    R.string.cartie_three, R.string.cartie_four};

            //read headers
            int count = 0;
            for (Cell cell : dataTypeSheet.getRow(0)) {
                if (!cell.getStringCellValue().equals(getString(headers[count]))) {
                    Toast.makeText(requireContext(), "expected : " + getString(headers[count])
                            + " find : " + cell.getStringCellValue(), Toast.LENGTH_LONG).show();
                    return;
                }
                count++;
            }
            TreeSet<Drug> importedDrugs = new TreeSet<>();
            {
                Iterator<Row> rows = dataTypeSheet.iterator();
                rows.next();
                while (rows.hasNext()) {
                    Row row = rows.next();
                    for (int i = 11; i < 16; i++) {
                        Cell cell = row.getCell(i);
                        if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                            Drug drug = new Drug();
                            drug.type = i - 11;
                            drug.name = cell.getStringCellValue();
                            importedDrugs.add(drug);
                        }
                    }
                }
            }

            MyDao dao = DataBase.getInstance(requireContext()).dao();
            AppExecutors.getInstance().diskIO().execute(() -> {
                Farm farm = new Farm();
                farm.name = "imported farm";
                farm.favorite = false;
                farm.birthCount = 0;
                farm.showerCount = 0;
                farm.bedType = "";
                farm.dryMethod = null;
                farm.scoreMethod = null;
                farm.showerUnitCount = 0;
                farm.showerPitCount = 0;
                farm.id = (int) dao.insertGetId(farm);

                HashSet<Integer> cowNumbers = new HashSet<>();
                ArrayList<Cow> cows = new ArrayList<>();
                ArrayList<Report> reports = new ArrayList<>();
                List<Drug> drugList = dao.getAllDrug();
                ArrayList<Drug> newDrugs = new ArrayList<>();
                main:
                for (Drug newDrug : importedDrugs) {
                    for (Drug drug : drugList) {
                        if (drug.compareTo(newDrug) == 0) {
                            continue main;
                        }
                    }
                    newDrugs.add(newDrug);
                }
                for (Drug drug : newDrugs) {
                    dao.insert(drug);
                }
                drugList = dao.getAllDrug();


                Iterator<Row> rows = dataTypeSheet.iterator();
                rows.next();
                while (rows.hasNext()) {
                    Row row = rows.next();
                    Report report = new Report();
                    report.cowId = (int) row.getCell(0).getNumericCellValue();
                    cowNumbers.add(report.cowId);
                    report.visit = new MyDate((int) row.getCell(1).getNumericCellValue(),
                            (int) row.getCell(2).getNumericCellValue(),
                            (int) row.getCell(3).getNumericCellValue());
                    report.areaNumber = (int) row.getCell(4).getNumericCellValue();

                    String scoreType = row.getCell(5).getStringCellValue();
                    if (scoreType.equals(getString(R.string.three_level_text))) {
                        report.scoreType = true;
                    } else if (scoreType.equals(getString(R.string.four_level_text))) {
                        report.scoreType = false;
                    } else {
                        requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(), "score type error", Toast.LENGTH_SHORT).show());
                        return;
                    }

                    String score = row.getCell(6).getStringCellValue();
                    if (report.scoreType) {
                        for (int i = 0; i < threeLevel.length; i++) {
                            if (getString(threeLevel[i]).equals(score)) {
                                report.score = i;
                            }
                        }
                    } else {
                        for (int i = 0; i < fourLevel.length; i++) {
                            if (getString(fourLevel[i]).equals(score)) {
                                report.score = i;
                            }
                        }
                    }

                    for (int i = 7; i < 10; i++) {
                        Cell cell = row.getCell(i);
                        if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                            String star = cell.getStringCellValue();
                            if (star != null && !star.isEmpty() && star.equals("*")) {
                                switch (i) {
                                    case 7:
                                        report.sardalme = true;
                                        break;
                                    case 8:
                                        report.khoni = true;
                                        break;
                                    case 9:
                                        report.kor = true;
                                        break;
                                }
                            }
                        } else {
                            switch (i) {
                                case 7:
                                    report.sardalme = false;
                                    break;
                                case 8:
                                    report.khoni = false;
                                    break;
                                case 9:
                                    report.kor = false;
                                    break;
                            }
                        }
                    }

                    String cartieStateString = row.getCell(10).getStringCellValue();
                    for (int i = 0; i < cartieState.length; i++) {
                        if (cartieStateString.equals(getString(cartieState[i]))) {
                            report.cartieState = i;
                            break;
                        }
                    }

                    for (int i = 11; i < 16; i++) {
                        Cell cell = row.getCell(i);
                        if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                            String drugName = cell.getStringCellValue();
                            for (Drug drug : drugList) {
                                if (drug.type == i - 11 && drug.name.equals(drugName)) {
                                    switch (drug.type) {
                                        case 0: {
                                            report.pomadeId = drug.id;
                                            break;
                                        }
                                        case 1: {
                                            report.antibioticId = drug.id;
                                            break;
                                        }
                                        case 2: {
                                            report.serumId = drug.id;
                                            break;
                                        }
                                        case 3: {
                                            report.cureId = drug.id;
                                            break;
                                        }
                                        case 4: {
                                            report.antiInflammatoryId = drug.id;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }


                    Cell nextVisitCell = row.getCell(16);
                    if (nextVisitCell.getCellType() == Cell.CELL_TYPE_STRING) {
                        String[] date = nextVisitCell.getStringCellValue().split("/");
                        report.nextVisit = new MyDate(Integer.parseInt(date[2]),
                                Integer.parseInt(date[1]),
                                Integer.parseInt(date[0]));
                    }
                    Cell moreInfo = row.getCell(17);
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
            Toast.makeText(requireContext(), "reading error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


    }

    @Override
    public void onResume() {
        super.onResume();
    }
}