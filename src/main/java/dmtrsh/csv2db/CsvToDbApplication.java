package dmtrsh.csv2db;

import dmtrsh.csv2db.config.ConfigurationControllers;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Lazy;

@SpringBootApplication
public class CsvToDbApplication extends AbstractJavaFxApplicationSupport{

	@Value("${ui.title}")
	private String windowTitle;

	@Qualifier("mainView")
	@Autowired
	private ConfigurationControllers.View view;

	@Override
	public void start(Stage stage) throws Exception {
		stage.setTitle(windowTitle);
		stage.setScene(new Scene(view.getView()));
		stage.setResizable(true);
		stage.centerOnScreen();
		stage.show();
	}

	public static void main(String[] args) {
		launchApp(CsvToDbApplication.class, args);
	}}
