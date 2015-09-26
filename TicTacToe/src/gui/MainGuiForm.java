/**
 * @author Mohit Uniyal
 * A javaFx project, Tic-Tac-Toe game which is modified into game called BOF- Battle of queens
 * on alternate mouse click black and white queens will be displayed on the board
 * Completed : 11/9/2015 (11:10PM)
 */
package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainGuiForm extends Application implements
		EventHandler<ActionEvent> {
	// to store total score of black and white queens
	private static int blackWin, whiteWin, ttlDraw;
	// images and image view that will be displayed on mouse click
	private Image blackQueenImg, whiteQueenImg;
	private ImageView blackQueenIv[], whiteQueenIv[];
	//to display prompt message when game ends
	private static Alert infoAlert;
	//used at the bottom to display total score 
	private Label scoreLbl;

	static {
		// creating static alert box which will be used, to display warning and
		// all other messages
		infoAlert = new Alert(AlertType.INFORMATION);
		infoAlert.setTitle("Battle Winner");
		infoAlert.setHeaderText("Congratulation");
	}

	public MainGuiForm() {
	 blackQueenImg	= new Image("./res/bqueen.png");
	 whiteQueenImg = new Image("./res/wqueen.png");
	 blackQueenIv = new ImageView[5];
	 whiteQueenIv = new ImageView[5];
		// Allocating 4 black queen image views that will be uses at runtime
		for (int i = 0; i < 5; i++) {
			blackQueenIv[i] = new ImageView(blackQueenImg);
			blackQueenIv[i].setFitHeight(45);
			blackQueenIv[i].setFitWidth(45);
			whiteQueenIv[i] = new ImageView(whiteQueenImg);
			whiteQueenIv[i].setFitHeight(45);
			whiteQueenIv[i].setFitWidth(45);
		}
	}

	//starting point of game
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage boardStage) throws Exception {
		// TODO Auto-generated method stub
		//boardStage.setTitle("BOQ (Tic-Tac-Toe) : Mohit Uniyal");
		BorderPane bp = new BorderPane();

		Scene sc = new Scene(bp, 370, 370);

		//call method to create a matrix 3*3 of buttons
		createCenterBoard(bp);

		//score label to display label at the bottom
		scoreLbl = new Label("White : " + whiteWin + "  Black : " + blackWin
				+ "  Draw : " + ttlDraw);
		scoreLbl.setStyle("-fx-background-color: white; -fx-font-size: 15px");

		VBox scoreHb = new VBox();
		scoreHb.getChildren().add(scoreLbl);
		scoreHb.setAlignment(Pos.CENTER);
		scoreHb.setPadding(new Insets(10, 10, 5, 10));
		bp.setBottom(scoreHb);

		// setting Game Name
		Label gameNameLbl = new Label("Battle  of  Queens");
		gameNameLbl.setStyle("-fx-text-fill : red; -fx-font-size: 35px;");
		HBox gameNameHb = new HBox(10);
		gameNameHb.setAlignment(Pos.CENTER);
		
		//close and minimize buttons
		Button closeBtn = new Button("X");
		Button minBtn = new Button("_");
		closeBtn.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-size: 15px");
		minBtn.setStyle("-fx-background-color: red; -fx-text-fill: white; -fx-font-size: 15px;");

		gameNameHb.getChildren().addAll(gameNameLbl, minBtn, closeBtn);
		bp.setTop(gameNameHb);

		// handling button events, Game will be closed 
		closeBtn.setOnAction((ae) -> {
			Platform.exit();
		});

		//minimizing the 
		minBtn.setOnAction((ae) -> {
			boardStage.setIconified(true);
		});

		// creating transparent background
		bp.setStyle("-fx-background-color: rgba(0,0,0,0);");
		sc.setFill(null);
		boardStage.initStyle(StageStyle.TRANSPARENT);
		// test code

		boardStage.setScene(sc);
		//boardStage.setResizable(false);
		boardStage.show();
	}

	//these are the buttons that will get displayed at center
	private Button centerBtn[];
	
	//just a block which will get copied in the constructor
	{
		centerBtn = new Button[9];
	}
	
	private void createCenterBoard(BorderPane bp) {
		GridPane centerGp = new GridPane();
		int l = 0;
		// gap between tiles in the board
		centerGp.setHgap(15);
		centerGp.setVgap(15);
		// creating 9 tiles (button) at the center of the board
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				centerBtn[l] = new Button();
				centerBtn[l].setId("btn" + l);
				centerBtn[l].setStyle("-fx-background-color: slateblue;");
				centerBtn[l].setPrefHeight(90);
				centerBtn[l].setPrefWidth(90);
				centerBtn[l].setOnAction(this);
				centerGp.add(centerBtn[l], j, i);
				l++;
			}
		}

		centerGp.setAlignment(Pos.CENTER);
		bp.setCenter(centerGp);
	}

	//symbol counter to count how many queens has been placed on the board
	private int symbolCtr = 0, j = 0, k = 0;
	private boolean invFlag = false;
	private String btnText[];
	
	{
		btnText = new String[]{ "-", "-", "-", "-", "-", "-", "-", "-", "-" };
	}

	private void checkBoardTick(Button pressedBtn) {
		//current symbol used 
		String symUsed = "";
		// checking for empty buttons and then only displaying a symbol on it
		if (pressedBtn.getGraphic()==null) {
			// inverting a flag value so that alternatively differnt symbol can
			// be used
			pressedBtn.setEffect(new InnerShadow(30, Color.YELLOW));
			//alternatively queens will be placed on the chess board
			if (invFlag) {
				invFlag = false;
				symUsed = "x";
				pressedBtn.setGraphic(blackQueenIv[j++]);
			} else {
				invFlag = true;
				symUsed = "o";
				pressedBtn.setGraphic(whiteQueenIv[k++]);
			}
			// getting the button index so that accordingly few values will be
			// set for it
			int indx = Integer.parseInt(pressedBtn.getId().substring(3, 4));
			btnText[indx] = symUsed;
		}
	}

	private boolean draw = true;

	// this method will check for winner after three moves
	private void findWinner() {

		// checking for the middle (horizontal, and vertical) and diagonal rows
		if (btnText[4].equals("o")) {
			if ((btnText[0].equals("o") && btnText[8].equals("o"))
					|| (btnText[1].equals("o") && btnText[7].equals("o"))
					|| (btnText[2].equals("o") && btnText[6].equals("o"))
					|| (btnText[3].equals("o") && btnText[5].equals("o"))) {
				draw = false;
				displayPrompt('o');
				return;
			}

		} else if (btnText[4].equals("x")) {
			if ((btnText[0].equals("x") && btnText[8].equals("x"))
					|| (btnText[1].equals("x") && btnText[7].equals("x"))
					|| (btnText[2].equals("x") && btnText[6].equals("x"))
					|| (btnText[3].equals("x") && btnText[5].equals("x"))) {
				draw = false;
				displayPrompt('x');
				return;
			}
		}

		// checking for top and left row
		if (btnText[0].equals("o")) {
			if ((btnText[1].equals("o") && btnText[2].equals("o"))
					|| (btnText[3].equals("o") && btnText[6].equals("o"))) {
				displayPrompt('o');
				draw = false;
				return;
			}

		} else if (btnText[0].equals("x")) {
			if ((btnText[1].equals("x") && btnText[2].equals("x"))
					|| (btnText[3].equals("x") && btnText[6].equals("x"))) {
				draw = false;
				displayPrompt('x');
				return;
			}
		}

		// checking for right and bottom rows
		if (btnText[8].equals("o")) {
			if ((btnText[2].equals("o") && btnText[5].equals("o"))
					|| (btnText[6].equals("o") && btnText[7].equals("o"))) {
				draw = false;
				displayPrompt('o');
				return;
			}
		} else if (btnText[0].equals("x")) {
			if ((btnText[2].equals("x") && btnText[5].equals("x"))
					|| (btnText[6].equals("x") && btnText[7].equals("x"))) {
				draw = false;
				displayPrompt('x');
				return;
			}
		}
	}

	// displaying clicked symbol on the board and removing old
	public void handle(ActionEvent ae) {
		Button pressedBtn = (Button) ae.getSource();
		// checking weather user is clicking on same button which already had a
		// Queen
		if (pressedBtn.getGraphic() == null) {
			// result only need to be checked when at least 5 tiles has been
			// pressed
			if (symbolCtr < 4) {
				checkBoardTick(pressedBtn);
			} else {
				checkBoardTick(pressedBtn);
				findWinner();
				// draw is only possible when 8 tiles are occupied and still no
				// winner
				if (symbolCtr == 8 && draw == true) {
					displayPrompt('-');
				}
			}
			symbolCtr++;
		}
	}

	// method responsible for handling user notifications
	private void displayPrompt(char winner) {
		// changing dialog button image so that it will be displayed according
		// to the winner
		ImageView iv = new ImageView();
		iv.setFitHeight(50);
		iv.setFitWidth(50);

		if ('o' == winner) {
			iv.setImage(whiteQueenImg);
			infoAlert.setGraphic(iv);
			infoAlert.setContentText("WHITE Queen Wins the Battle");
			whiteWin++;
		} else if ('x' == winner) {
			iv.setImage(blackQueenImg);
			infoAlert.setGraphic(iv);
			infoAlert.setContentText("BLACK Queen Wins the Battle");
			blackWin++;
		} else {
			infoAlert.setHeaderText("No Winner");
			infoAlert.setContentText("Match is a draw");
			ttlDraw++;
		}
		scoreLbl.setText("White : " + whiteWin + "  Black : " + blackWin
				+ "  Draw : " + ttlDraw);
		infoAlert.showAndWait();
		resetGame();
	}

	// method to reset the board by setting null to every graphic
	private void resetGame() {
		//setting everything to initial values
		for (int i = 0; i < 9; i++) {
			centerBtn[i].setGraphic(null);
			centerBtn[i].setEffect(null);
			btnText[i] = "-";
		}
		draw = true;
		invFlag = false;
		symbolCtr = 0;
		j = 0;
		k = 0;
	}
}
