package dmtrsh.csv2db.repository;

import dmtrsh.csv2db.csv.CsvRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class Repo {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void createTable(String tableName, String columns){
        String query = "CREATE TABLE ${tableName} (${columns})"
                .replace("${tableName}", tableName)
                .replace("${columns}", columns);
        jdbcTemplate.execute(query);
    }

    public void populateTable(String tableName, String columns, List<CsvRow> csvRows){
        csvRows.forEach(csvRow -> jdbcTemplate.execute(createInsertQuery(tableName, columns, csvRow)));
    }

    private String createInsertQuery(String tableName, String columns, CsvRow row){
        String query = "insert into ${tableName} (${columns}) values (${values})";
        String insertValues = row.getRow()
                                    .stream()
                                    .map(s -> "'" + s + "'")
                                    .collect(Collectors.joining(","));
        String q = query.replace("${tableName}", tableName)
                .replace("${columns}", columns)
                .replace("${values}", insertValues);
        return q;
    }

}
