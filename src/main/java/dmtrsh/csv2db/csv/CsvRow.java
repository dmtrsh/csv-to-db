package dmtrsh.csv2db.csv;

import java.util.Arrays;
import java.util.List;

public class CsvRow {
    List<String> row;

    public CsvRow(List<String> cells) {
        this.row = cells;
    }
    public CsvRow(String cells, String splitter) {
        this.row = Arrays.asList(cells.split(splitter));
    }

    public List<String> getRow() {
        return row;
    }

    public void setRow(List<String> row) {
        this.row = row;
    }

    public void setRow(String cells, String splitter) {
        this.row = Arrays.asList(cells.split(splitter));
    }
}
