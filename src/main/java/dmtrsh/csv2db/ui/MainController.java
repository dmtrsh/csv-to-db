package dmtrsh.csv2db.ui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dmtrsh.csv2db.mapping.ViewEntity;
import dmtrsh.csv2db.csv.CSVReader;
import dmtrsh.csv2db.csv.CsvRow;
import dmtrsh.csv2db.mapping.ColumnMapping;
import dmtrsh.csv2db.mapping.EntityMapping;
import dmtrsh.csv2db.repository.Repo;
import dmtrsh.csv2db.validator.Validator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MainController {

    private Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private Repo repo;

    @FXML private TableView<ViewEntity> table;
    @FXML private TextField tableName;
    @FXML private MenuItem openFileButton;
    @FXML private Button okButton;
    @FXML private TableColumn<ViewEntity, String> csvName;
    @FXML private TableColumn<ViewEntity, String> dbName;

    @Autowired
    Validator validator;

    private Stage dialogStage;
    private List<CsvRow> csvRows;
    private EntityMapping entityMapping;



    private ObservableList<ViewEntity> data;

    @FXML
    private void initialize() {
    }

    @SuppressWarnings("unchecked")
    @PostConstruct
    public void init() {
        data = FXCollections.observableArrayList(new HashSet<ViewEntity>());
        csvName.setCellValueFactory(new PropertyValueFactory<>("csvName"));
        dbName.setCellValueFactory(new PropertyValueFactory<>("dbName"));
        Callback<TableColumn<ViewEntity, String>, TableCell<ViewEntity, String>>
                cellFactoryForMap = (TableColumn<ViewEntity, String> p) ->
                new TextFieldTableCell(new StringConverter() {
                    @Override
                    public String toString(Object t) {
                        return t.toString();
                    }
                    @Override
                    public Object fromString(String string) {
                        return string;
                    }
                });
        dbName.setEditable(true);
        dbName.setCellFactory(cellFactoryForMap);
        table.setItems(data);
    }

    @FXML
    private void openAction() {
        openFileButton.setOnAction(arg0 -> {
            entityMapping = new EntityMapping();
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
            fileChooser.getExtensionFilters().add(extFilter);
            File file = fileChooser.showOpenDialog(table.getScene().getWindow());
            if(file == null) return;
            List<CsvRow> csvFileRows = CSVReader.read(file);
            List<String> header = csvFileRows.get(0).getRow();
            table.getItems().clear();
            header.forEach(h -> {
                ViewEntity dataRow = new ViewEntity();
                dataRow.setCsvName(h);
                dataRow.setDbName(h);
                data.add(dataRow);
            });
            entityMapping.setFileName(file.getName());
            csvRows = csvFileRows
                    .stream()
                    .skip(1)
                    .collect(Collectors.toList());
        });
    }
    @FXML
    private void processExit(){System.exit(0);}

    @FXML
    private void loadIntoDb(){
        if(!checkTableNameIsOk()) return;
        if(!checkColumnName()) return;
        if (table.getItems().size() == 0) return;
        String columnsWithTypes = table.getItems()
                .stream()
                .map(item -> dbName.getCellObservableValue(item).getValue())
                .map(s -> s + " varchar(255)")
                .collect(Collectors.joining(","));
        String columns = table.getItems()
                .stream()
                .map(item -> dbName.getCellObservableValue(item).getValue())
                .collect(Collectors.joining(","));
        String tableName = this.tableName.getText();
        repo.createTable(tableName, columnsWithTypes);
        repo.populateTable(tableName, columns, csvRows);
        saveToJson();
        this.tableName.clear();
        table.getItems().clear();
        showPopup("You file has been successfully inserted to the database.");
    }

    @FXML
    private void changeCellValue(TableColumn.CellEditEvent<ViewEntity, String> event) {
        event.getTableView()
                .getItems()
                .get(event.getTablePosition().getRow())
                .setDbName(event.getNewValue());
    }

    private void saveToJson(){
        entityMapping.setTableName(tableName.getText());

        ArrayList<String> csvColumns = new ArrayList<>();
        ArrayList<String> dbColumns = new ArrayList<>();
        table.getItems()
                .stream()
                .map(item -> csvName.getCellObservableValue(item).getValue())
                .forEach(csvColumns::add);
        table.getItems()
                .stream()
                .map(item -> dbName.getCellObservableValue(item).getValue())
                .forEach(dbColumns::add);

        IntStream.range(0, csvColumns.size())
                .forEach(i -> entityMapping
                        .getColumns()
                        .add(new ColumnMapping(csvColumns.get(i), dbColumns.get(i))));
        saveJson(entityMapping);
    }

    private void saveJson(EntityMapping entityMapping){
        String fileName = entityMapping.getFileName();
        Path path = Paths.get("mapping");
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String jsonFileName = "mapping\\" + fileName.substring(0, fileName.lastIndexOf(".csv")) + ".json";
        try (Writer writer = new FileWriter(jsonFileName)) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(entityMapping, writer);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void showPopup(String msg){
        dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        okButton.setVisible(true);
        VBox vbox = new VBox(new Text(msg), okButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(15));
        dialogStage.setScene(new Scene(vbox));
        dialogStage.show();
    }

    @FXML
    public void closePopup() {
        dialogStage.close();
        okButton.setVisible(false);
    }

    private boolean checkTableNameIsOk() {
        if (validator.isTooBig(tableName.getText())){
            showPopup("Table name should be less than 30 symbols");
            return false;
        } else if (validator.isEmpty(tableName.getText())){
            showPopup("Table name should not be empty");
            return false;
        } else if (validator.hasInappropriateSymbols(tableName.getText())){
            showPopup("Table name should contain only alphabetical symbols or '_'");
            return false;
        }else if (validator.isReserved(tableName.getText())){
            showPopup("Table name should not be a reserved word!");
            return false;
        }
        return true;
    }

    private boolean checkColumnName() {
        List<String> dbColumns = table.getItems()
                .stream()
                .map(item -> dbName.getCellObservableValue(item).getValue())
                .collect(Collectors.toList());
        if (validator.isTooBig(dbColumns)){
            showPopup("Column name should be less than 30 symbols");
            return false;
        } else if (validator.isEmpty(dbColumns)) {
            showPopup("Column name should not be empty");
            return false;
        } else if (validator.isReserved(dbColumns)){
            showPopup("Column name should not be a reserved word!");
            return false;
        } else if (validator.hasInappropriateSymbols(dbColumns)){
            showPopup("Column name should contain only alphabetical symbols or '_'.");
            return false;
        }
        return true;
    }
}
