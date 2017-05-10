package dmtrsh.csv2db.csv;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CSVReader {

    public List<CsvRow> read(File file) {
        String path = file.getPath();
        return read(path);
    }

    public List<CsvRow> read(String path) {
        String line = "";
        String cvsSplitBy = ",";
        List<CsvRow> csvRows = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            while ((line = br.readLine()) != null) {
                csvRows.add(new CsvRow(line.replace("\"", ""), cvsSplitBy));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return csvRows;
    }

}