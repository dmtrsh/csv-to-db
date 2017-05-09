package dmtrsh.csv2db.mapping;

public class ColumnMapping {
    private String csvName;
    private String dbName;

    public ColumnMapping(String csvName, String dbName) {
        this.csvName = csvName;
        this.dbName = dbName;
    }

    public String getCsvName() {
        return csvName;
    }

    public void setCsvName(String csvName) {
        this.csvName = csvName;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }
}
