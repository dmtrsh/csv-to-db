package dmtrsh.csv2db.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {

    public static List<CsvRow> read(File file) {
        String path = file.getPath();
        return read(path);
    }

    public static List<CsvRow> read(String path) {
        String csvFile = path;
        String line = "";
        String cvsSplitBy = ",";
        List<CsvRow> csvRows = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                csvRows.add(new CsvRow(line.replace("\"", ""), cvsSplitBy));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return csvRows;
    }

}