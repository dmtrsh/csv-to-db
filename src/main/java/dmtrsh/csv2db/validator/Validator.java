package dmtrsh.csv2db.validator;


import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class Validator {

    public boolean tableNameIsTooBig(String tableName){
        return tableName.length() > 30;
    }

    public boolean tableNameIsEmpty(String tableName){
        return tableName.length() < 1;
    }

    public boolean tableNameHasInappropriateSymbols(String tableName, String[] inappropriateSymbols){
        return Arrays.stream(inappropriateSymbols)
                .anyMatch(tableName::contains);
    }

    public boolean isTableNameReserved(String tableName, List inappropriateWords) {
        String actualTableName = tableName.toUpperCase();
        return inappropriateWords.stream().anyMatch(actualTableName::equals);
    }

    public boolean isReserved(List<String> dbColumns, List inappropriateWords) {
        return dbColumns.stream()
                .peek(String::toUpperCase)
                .anyMatch(inappropriateWords::contains);
    }

    public boolean columnNameHasInappropriateSymbols(List<String> dbColumns, String[] inappropriateSymbols){
        return Arrays.stream(inappropriateSymbols)
                .anyMatch(s -> dbColumns.stream()
                        .anyMatch(dbColumn -> dbColumn.contains(s)));
    }
}
