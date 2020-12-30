package mines;

import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Controller {
	private int heightSize;
	private int widthSize;
	private int NumberOfmines;
	private Stage primaryStage;
	private VBox menu;
	private GridPane grid;

	public int getWidthSize() {
		return widthSize;
	}

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	public int getNumberOfmines() {
		return NumberOfmines;
	}

	public int getHeightSize() {
		return heightSize;
	}

	public void setFields(VBox fields) {
		this.menu = fields;
	}

	@FXML
	private Button button;

	@FXML
	private TextField height;

	@FXML
	private TextField width;

	@FXML
	private TextField mines;

	public void startGame() {
		Image errorImage = new Image(getClass().getResourceAsStream("bomb.png"));
		button.setText("RESET");
		primaryStage.close(); // Close that latest stage

		// Creating new root , inside root there is the fxml file and the grid of the
		// game
		HBox root = new HBox();
		root.setStyle("-fx-background-color:YELLOWGREEN");
		root.getChildren().removeAll(grid, menu);
		root.getChildren().add(menu);

		try {
			// When clicking on the button collect the parmater fields of board size and
			// number of mines
			heightSize = Integer.valueOf(height.getText());
			widthSize = Integer.valueOf(width.getText());
			NumberOfmines = Integer.valueOf(mines.getText());

			// Check if the number of mines is bigger the the board size
			if (NumberOfmines >= heightSize * widthSize)
				throw new NumberFormatException();

			// Creating the game grid of buttons
			grid = createGridPane();
			root.getChildren().add(grid);

			// If the parameter given was invalid show pop up window
		} catch (NumberFormatException e) {
			openDialog(errorImage, -1); // open error dialog
		}

		primaryStage.setScene(new Scene(root));
		primaryStage.show();

	}

	// Function that create all of the game behavior
	public GridPane createGridPane() {
		GridPane grid = new GridPane();
		// Loading all of the images
		Image logoImage = new Image(getClass().getResourceAsStream("logo2.png"));
		Image bombImage = new Image(getClass().getResourceAsStream("bomb.png"));
		Image flagImage = new Image(getClass().getResourceAsStream("flag.png"));
		Image loseImage = new Image(getClass().getResourceAsStream("lose.png"));
		Image winImage = new Image(getClass().getResourceAsStream("win.png"));

		ImageView liv = new ImageView();
		liv.setFitHeight(100);
		liv.setFitWidth(200);
		liv.setImage(logoImage);

		VBox col = new VBox();
		// Holds all of the list of buttons
		ArrayList<ArrayList<Button>> allButtons = new ArrayList<>();
		// Creating the engine of the game ,in the size that were given in the menu
		// section
		Mines m = new Mines(getHeightSize(), getWidthSize(), getNumberOfmines());

		// Class for the right click of board buttons
		class RightClick implements EventHandler<MouseEvent> {
			private int i;
			private int j;

			public RightClick(int i, int j) {
				this.i = i;
				this.j = j;
			}

			// If clicking on board button(right click) , its put flag on the board (i,j)
			@Override
			public void handle(MouseEvent event) {
				m.toggleFlag(i, j);
				ImageView fiv = new ImageView();
				fiv.setImage(flagImage);
				fiv.setFitHeight(20);
				fiv.setFitWidth(20);

				if (event.getButton() == MouseButton.SECONDARY) {
					allButtons.get(i).get(j).setGraphic(fiv);
					allButtons.get(i).get(j).setText("");
				}
				if (!m.getBoard(i, j).isFlags()) {
					allButtons.get(i).get(j).setGraphic(null);
					allButtons.get(i).get(j).setText(".");
				}
			}
		}

		// Class for the right click of board buttons
		class LeftClick implements EventHandler<ActionEvent> {
			private int i;
			private int j;

			public LeftClick(int i, int j) {
				this.i = i;
				this.j = j;
			}

			// If clicking on board button(left click) , its reveal the result of this board
			// (i,j):
			// *bomb
			// *number of mines nearby
			// *open all of the neighbors is they are not mines
			// *check for win/lose section
			@Override
			public void handle(ActionEvent clicked) {

				// If its flag do nothing
				if (m.getBoard(i, j).isFlags())
					return;

				// After left clicking check for win or lose , and open board (i,j)
				if (!m.open(i, j)) {
					m.setShowAll(true); // Show all of the board items
					showAll(allButtons);
					ImageView biv = new ImageView();
					biv.setImage(bombImage);
					biv.setFitHeight(20);
					biv.setFitWidth(20);
					allButtons.get(i).get(j).setText("");
					allButtons.get(i).get(j).setGraphic(biv);
					allButtons.get(i).get(j).setStyle("-fx-background-color:RED");
					openDialog(loseImage, 0); // Open lose window
				} else if (m.isDone()) {
					m.setShowAll(true);
					showAll(allButtons);
					openDialog(winImage, 1); // open win window
				} else
					showAll(allButtons);

			}

			// Every win/lose/open refresh all of the buttons
			private void showAll(ArrayList<ArrayList<Button>> allButtons) {
				for (int i = 0; i < getHeightSize(); i++)
					for (int j = 0; j < getWidthSize(); j++) {
						try {
							int val = Integer.valueOf(m.get(i, j)); // Collect from getString the board in (i,j) value
							// if val = number and there is no flag in (i,j) return the number of mine
							// nearyby
							if (val == 1 && !m.getBoard(i, j).isFlags()) {
								allButtons.get(i).get(j).setText("1");
								allButtons.get(i).get(j).setTextFill(Color.BLUE);
							} else if (val == 2 && !m.getBoard(i, j).isFlags()) {
								allButtons.get(i).get(j).setText("2");
								allButtons.get(i).get(j).setTextFill(Color.DARKBLUE);
							} else if (val >= 3 && !m.getBoard(i, j).isFlags()) {
								allButtons.get(i).get(j).setText(m.get(i, j));
								allButtons.get(i).get(j).setTextFill(Color.RED);
							}
							// If val its not a number throw NumberFormatException, catch it and check what
							// the type of board in place (i,j)
						} catch (NumberFormatException e) {
							if (m.get(i, j) == "X") {
								if (m.getBoard(i, j).isFlags()) { // If there is flag on mine and the user reveal it
																	// fill the board in place (i,j) to green
									allButtons.get(i).get(j).setStyle("-fx-background-color:GREEN");
								} else {
									ImageView biv = new ImageView();
									biv.setImage(bombImage);
									biv.setFitHeight(20);
									biv.setFitWidth(20);
									allButtons.get(i).get(j).setText("");
									allButtons.get(i).get(j).setGraphic(biv);
								}
							} else if (m.get(i, j) == "F") { // If its flag do not write from getString (image instead)
								allButtons.get(i).get(j).setText("");
							} else if (m.get(i, j) == "." && !m.getBoard(i, j).isFlags()) { // If its empty and there is
																							// no flag write "." on
																							// button
								allButtons.get(i).get(j).setText(".");
							} else if (m.get(i, j) == " " && !m.getBoard(i, j).isFlags()) {
								allButtons.get(i).get(j).setText(" ");
							}
						}
					}
			}

		}

		// Creating all of the buttons
		ArrayList<HBox> HBoxButtons = new ArrayList<>();

		for (int i = 0; i < getHeightSize(); i++) {
			HBox row = new HBox();
			ArrayList<Button> buttons = new ArrayList<>();
			for (int j = 0; j < getWidthSize(); j++) {
				Button but = new Button(".");
				but.setStyle("-fx-font-weight: bold");
				but.setPrefSize(40, 40);
				buttons.add(but);
				but.setOnAction(new LeftClick(i, j));
				but.setOnMouseClicked(new RightClick(i, j));
			}
			allButtons.add(buttons);
			row.getChildren().addAll(buttons);
			HBoxButtons.add(row);
		}

		// Creating and attached the logo above the buttons
		VBox logoBox = new VBox();
		logoBox.setPadding(new Insets(5));
		logoBox.setAlignment(Pos.CENTER);
		logoBox.getChildren().add(liv);
		col.getChildren().add(logoBox);
		grid.getChildren().add(col);
		col.getChildren().addAll(HBoxButtons);

		return grid;
	}

	// Dialog message if the user enter wrong input , win or lose.
	// Operation: 1 = win , 0 = lose , -1 = error
	private void openDialog(Image image, int operation) {
		VBox end = new VBox();
		Stage endStage = new Stage();
		Scene endScene = new Scene(end, 200, 200);
		ImageView biv = new ImageView();
		Button loseErrorButton = new Button("Try Again");
		Button winButton = new Button("New Game");
		Label errorLabel = new Label("Wrong input");

		biv.setFitHeight(120);
		biv.setFitWidth(120);
		end.setStyle("-fx-background-color:YELLOWGREEN");
		end.setAlignment(Pos.CENTER);
		end.getChildren().add(biv);

		class ButtonClicked implements EventHandler<ActionEvent> {
			Stage loseStage;

			public ButtonClicked(Stage stage) {
				loseStage = stage;
			}

			@Override
			public void handle(ActionEvent event) {
				startGame();
				loseStage.close();
			}
		}

		class ButtonClickedError implements EventHandler<ActionEvent> {
			Stage loseStage;

			public ButtonClickedError(Stage stage) {
				loseStage = stage;
			}

			@Override
			public void handle(ActionEvent event) {
				loseStage.close();
			}

		}
		loseErrorButton.setOnAction(new ButtonClicked(endStage));
		winButton.setOnAction(new ButtonClicked(endStage));
		if (operation == 1) { // Win situation
			biv.setImage(image);
			end.getChildren().add(winButton);
		} else if (operation == 0) { // Lose situation
			biv.setImage(image);
			end.getChildren().add(loseErrorButton);
		} else { // Error situation
			end.getChildren().add(errorLabel);
			biv.setImage(image);
			loseErrorButton.setOnAction(new ButtonClickedError(endStage));
			end.getChildren().add(loseErrorButton);
		}

		endStage.setAlwaysOnTop(true);
		endStage.setScene(endScene);
		endStage.show();
	}

}
