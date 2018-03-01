import java.io.*;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class MineSweeper extends Application{
	

	static FaceButton smiley;

	static CoverButton buttons[][];

	static int cellCount = 25;
	static boolean gameOver = false;
	public static void main(String[] args) {

		launch(args);

	}
	public int checkDanger(CoverButton[][] grid, int x, int y ){
		int dangerCount = 0;
		if(x > 0){
			//check x - 1 LEFT
			if(isDanger(grid, x - 1, y)){
				dangerCount++;
			}
		}
		if(y > 0){
			//check y - 1 TOP
			if(isDanger(grid, x, y - 1)){
				dangerCount++;
			}
		}
		if(x > 0 && y > 0){
			//check x-1 y-1 TOP LEFT
			if(isDanger(grid, x-1, y-1)){
				dangerCount++;
			}
		}
		if(x != 4 && y > 0){
			//check x+1 y-1 TOP RIGHT
			if(isDanger(grid, x + 1, y-1)){
				dangerCount++;
			}
		}
		if(x != 4){
			//check x + 1 RIGHT
//			System.out.println(x + " " + y);
			if(isDanger(grid, x + 1, y)){
				dangerCount++;
			}
		}
		if(y != 4){
			//check y + 1 DOWN
//			System.out.println(x + " " + y);
			if(isDanger(grid, x, y+1)){
				dangerCount++;
			}
		}
		if(x != 4 && y != 4){
			//check x + 1 && y + 1 RIGHT BOTTOM
			if(isDanger(grid, x + 1, y + 1)){
				dangerCount++;
			}
		}
		if(x > 0 && y != 4){
			//check X - 1 Y + 1 LEFT BOTTOM
			if(isDanger(grid, x - 1, y + 1)){
				dangerCount++;
			}
		}
		return dangerCount;
	}
	
	public boolean isDanger(CoverButton[][] grid, int x, int y){
		if( grid[y][x].value == 4){
			return true;
		}
		return false;
	}


	public void start(Stage theStage){
	

		BorderPane bp = new BorderPane();

		HBox topSection = new HBox();

		GridPane sweep = new GridPane();

		bp.setBottom(sweep);

		topSection.setAlignment(Pos.CENTER);

		bp.setTop(topSection);

		Label minesLeft = new Label("010");

		minesLeft.setPadding(new Insets(10, 10, 10, 10));	

		smiley = new FaceButton();

		Label timeLeft = new Label("000");

		timeLeft.setPadding(new Insets(10, 10, 10, 10));

		topSection.getChildren().addAll(minesLeft, smiley, timeLeft);		

		buttons = new CoverButton[5][5];
		//hardcode the bombs
		
		///todo figure out code for randomly placing the bombs
		for(int y = 0; y < 5; y++) {

			for(int x = 0; x < 5; x++) {
				buttons[y][x] = new CoverButton();

				CoverButton b = buttons[y][x]; 
				b.setGraphic(b.cover);
				
				b.setOnAction(click ->{
					b.state = 1;
					checkWin();
					System.out.println("b state is " + b.state + " b value is " + b.value);
					if(!gameOver){
						if(b.value == 1) {
							b.revealed = b.oneImage;
							cellCount--;
						}else if(b.value == 2) {
							b.revealed = b.twoImage;
							cellCount--;
						}else if(b.value == 3) {
							b.revealed = b.threeImage;
							cellCount--;
						}else if(b.value == 4) {
							b.revealed = b.theBombImage;
							MineSweeper.smiley.setGraphic(FaceButton.deadFace);
							gameOver = true;
						}else if(b.value == 5) {
							b.revealed = b.zeroImage;	
							cellCount--;
						}	
						b.setGraphic(b.revealed);
					}
				});

				sweep.add(buttons[y][x], y, x);

			}

		}
		
		buttons[1][1].value = 4;
		buttons[3][2].value = 4;
		buttons[3][3].value = 4;
//		System.out.println(buttons[1][1].toString());
		for(int y = 0; y < 5; y++) {
			for(int x = 0; x < 5; x++) {
				
				if(buttons[y][x].value != 4){
					buttons[y][x].value = checkDanger(buttons, x, y);
				}
				System.out.println("buttn val " + buttons[y][x].value);
			}
		}
		

		theStage.setScene(new Scene(bp));

		theStage.show();

	}

	public void checkWin() {
//		System.out.println("Hello from check Win");

		int bombCount = 3;	
		int opened = 0;

		for(int i = 0; i < 5; i++) {
			for(int j = 0; j < 5; j++) {
				if (buttons[i][j].state == 1 && buttons[i][j].value != 4 ) {
					opened++;
				}
			}
		}

		if(opened == 22) {
			MineSweeper.smiley.setGraphic(FaceButton.winFace);
			gameOver = true;
		}

	}

}



class FaceButton extends Button implements EventHandler<ActionEvent>{

	int faceState;

	int faceValue;

	static ImageView smileyFace;

	static ImageView deadFace;

	static ImageView winFace;

	ImageView astonishedFace;

	public FaceButton() {

		faceState = 0;

		faceValue = 0;

		double faceSize = 45;

		setMinWidth(faceSize);

		setMinHeight(faceSize);

		setMaxWidth(faceSize);

		setMaxHeight(faceSize);

		smileyFace = new ImageView(new Image("file:res/face-smile.png"));

		smileyFace.setFitWidth(faceSize);

		smileyFace.setFitHeight(faceSize);

		deadFace = new ImageView(new Image("file:res/face-dead.png"));

		deadFace.setFitWidth(faceSize);

		deadFace.setFitHeight(faceSize);

		winFace = new ImageView(new Image("file:res/face-win.png"));

		winFace.setFitWidth(faceSize);

		winFace.setFitHeight(faceSize);

		astonishedFace = new ImageView(new Image("file:res/face-O.png"));

		astonishedFace.setFitWidth(faceSize);

		astonishedFace.setFitHeight(faceSize);

		setGraphic(smileyFace);

	}

	@Override
	public void handle(ActionEvent event) {



	}

}

class CoverButton extends Button implements EventHandler<ActionEvent>{

	int state;

	int value; 

	ImageView cover, revealed, zeroImage, oneImage, twoImage, threeImage, theBombImage;

	public CoverButton() {

		value = 0;

		state = 0;

		double size = 80;

		setMinWidth(size);

		setMinHeight(size);

		setMaxWidth(size);

		setMaxHeight(size);

		cover = new ImageView(new Image("file:res/cover.png"));

		cover.setFitWidth(size);

		cover.setFitHeight(size);

		zeroImage = new ImageView(new Image("file:res/0.png"));

		zeroImage.setFitWidth(size);

		zeroImage.setFitHeight(size);

		oneImage = new ImageView(new Image("file:res/1.png"));

		oneImage.setFitWidth(size);

		oneImage.setFitHeight(size);

		twoImage = new ImageView(new Image("file:res/2.png"));

		twoImage.setFitWidth(size);

		twoImage.setFitHeight(size);

		threeImage = new ImageView(new Image("file:res/3.png"));

		threeImage.setFitWidth(size);

		threeImage.setFitHeight(size);

		theBombImage = new ImageView(new Image("file:res/bombrevealed-bicubic.png"));

		theBombImage.setFitWidth(size);

		theBombImage.setFitHeight(size);

		setGraphic(cover);

	}

	@Override
	public void handle(ActionEvent clicked) {


	}

}
