package dmtrsh.csv2db.mapping;

import java.util.ArrayList;

public class EntityMapping {
    private String fileName;
    private String tableName;
    private ArrayList<ColumnMapping> columns;

    public EntityMapping() {
        this.columns = new ArrayList<>();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public ArrayList<ColumnMapping> getColumns() {
        return columns;
    }

    public void setColumns(ArrayList<ColumnMapping> columns) {
        this.columns = columns;
    }
}
