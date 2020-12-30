package mines;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MinesFX extends Application {

	protected Scene scene;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		VBox menu = null;
		Controller controller = null;

		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("minesFXML.fxml"));
			menu = loader.load();
			controller = loader.getController();
		} catch (IOException e) {
			e.printStackTrace();
		}

		controller.setPrimaryStage(primaryStage);
		controller.setFields(menu);
		primaryStage.setScene(new Scene(menu));
		primaryStage.setTitle("The Amazing Mines Sweeper");
		primaryStage.show();

	}
}
