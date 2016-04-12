package javafx.bookmanager;

import java.util.Locale;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Startup extends Application {

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		String langCode = getParameters().getNamed().get("lang");
		if (langCode != null && !langCode.isEmpty()) {
			Locale.setDefault(Locale.forLanguageTag(langCode));
		}

		Parent root = FXMLLoader.load(getClass().getResource("/javafx/bookmanager/view/book-manager.fxml"),
				ResourceBundle.getBundle("javafx/bookmanager/bundle/base"));

		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/javafx/bookmanager/css/standard.css").toExternalForm());

		primaryStage.setTitle("JavaFX-BookManager");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
