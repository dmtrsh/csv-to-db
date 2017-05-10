package dmtrsh.csv2db.validator;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ValidatorTest {

    private Validator validator = new Validator();

    @Test
    public void tableNameIsTooBig_test(){
        String tableName = "";
        Assert.assertFalse(validator.isTooBig(tableName));
    }

    @Test
    public void tableNameIsEmpty_test(){
        String tableName = "";
        Assert.assertTrue(validator.isEmpty(tableName));
    }

    @Test
    public void tableNameHasInappropriateSymbols(){
        String tableName = "kmdsklncds-";
        Assert.assertTrue(validator.hasInappropriateSymbols(tableName));
    }

    @Test
    public void tableNameIsTooBig_inList(){
        List<String> list = new ArrayList<>();
        list.add("table");
        Assert.assertFalse(validator.isTooBig(list));
    }

    @Test
    public void tableNameIsEmpty_inList(){
        List<String> list = new ArrayList<>();
        list.add("table");
        Assert.assertFalse(validator.isEmpty(list));
    }

    @Test
    public void tableNameHasInappropriateSymbols_inList(){
        List<String> list = new ArrayList<>();
        list.add("t!able");
        Assert.assertTrue(validator.hasInappropriateSymbols(list));
    }



}
