package dmtrsh.csv2db.validator;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class Validator {
    private String[] inappropriateSymbols = {" ", "/", "#", "-", "+", "=", ",", "+", "!", "&", "$", "|", "\\", "*",
            "%", "'", "(", ")", "\n", "\t"};

    @Value(value = "${sql.reserved.words}")
    private String inappropriateWordsProperty;

    private List<String> inappropriateWords;

    public boolean isTooBig(String tableName){
        return tableName.length() > 30;
    }

    public boolean isEmpty(String tableName){
        return tableName.length() < 1;
    }

    public boolean hasInappropriateSymbols(String tableName){
        return Arrays.stream(inappropriateSymbols)
                .anyMatch(tableName::contains);
    }

    public boolean isReserved(String tableName) {
        initialize();
        String actualTableName = tableName.toUpperCase();
        return inappropriateWords.stream().anyMatch(actualTableName::equals);
    }

    public boolean isReserved(List<String> dbColumns) {
        initialize();
        return dbColumns.stream()
                .peek(String::toUpperCase)
                .anyMatch(inappropriateWords::contains);
    }

    public boolean hasInappropriateSymbols(List<String> dbColumns){
        return Arrays.stream(inappropriateSymbols)
                .anyMatch(s -> dbColumns.stream()
                        .anyMatch(dbColumn -> dbColumn.contains(s)));
    }

    public boolean isTooBig(List<String> dbColumns){
        return dbColumns.stream().anyMatch(s -> s.length() > 30);
    }

    public boolean isEmpty(List<String> dbColumns){
        return dbColumns.stream().anyMatch(String::isEmpty);
    }

    private void initialize(){
        if (null == inappropriateWords)
            inappropriateWords = Arrays.asList(inappropriateWordsProperty.split(","));
    }
}
