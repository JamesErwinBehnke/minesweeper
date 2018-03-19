import java.io.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
public class MineSweeper extends Application {
	static resetButton resetButton;
	static CoverButton buttons[][];
	static int gridWidth = 8;
	static int gridHeight = 8;
	static int cellCount = gridWidth * gridHeight;
	static int bombCount = 0;
	static int flagsSet = 10;
	static int level = 1;
	static int firstClickX = 0;
	static int firstClickY = 0;
	static boolean isFirstClick = true;
	static boolean gameOver = false;
	static boolean boardIsConfigured = false;
	public static void main(String[] args) {
		launch(args);
	}
	class CoverButton extends Button {
		int state;
			int value;
			int originalValue;
			int y, x;
			boolean isFlagged, isOpened, isChecked;
			ImageView cover, revealed, zeroImage, oneImage, twoImage, threeImage, fourImage, fiveImage, sixImage, sevenImage,
					eightImage, theBombImage, theFlag, greyBomb;
			public CoverButton() {
				isFlagged = false;
				isOpened = false;
				isChecked = false;
				x = 0;
				y = 0;
				value = 0;
				originalValue = 0;
				state = 0;
				double size = 45;
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
				fourImage = new ImageView(new Image("file:res/4.png"));
				fourImage.setFitWidth(size);
				fourImage.setFitHeight(size);
				fiveImage = new ImageView(new Image("file:res/5.png"));
				fiveImage.setFitWidth(size);
				fiveImage.setFitHeight(size);
				sixImage = new ImageView(new Image("file:res/6.png"));
				sixImage.setFitWidth(size);
				sixImage.setFitHeight(size);
				sevenImage = new ImageView(new Image("file:res/7.png"));
				sevenImage.setFitWidth(size);
				sevenImage.setFitHeight(size);
				eightImage = new ImageView(new Image("file:res/4.png"));
				eightImage.setFitWidth(size);
				eightImage.setFitHeight(size);
				theBombImage = new ImageView(new Image("file:res/mine-red.png"));
				theBombImage.setFitWidth(size);
				theBombImage.setFitHeight(size);
				theFlag = new ImageView(new Image("file:res/flag.png"));
				theFlag.setFitWidth(size);
				theFlag.setFitHeight(size);
				greyBomb = new ImageView(new Image("file:res/bombrevealed-bicubic.png"));
				greyBomb.setFitWidth(size);
				greyBomb.setFitHeight(size);
				setGraphic(cover);
			}
			public void setAsRevealed() {
				this.revealed = this.zeroImage;
				this.isOpened = true;
				if (this.value == 0) {
					this.revealed = this.zeroImage;
					this.isOpened = true;
				} else if (this.value == 1) {
					this.revealed = this.oneImage;
					this.isOpened = true;
				} else if (this.value == 2) {
					this.revealed = this.twoImage;
					this.isOpened = true;
				} else if (this.value == 3) {
					this.revealed = this.threeImage;
					this.isOpened = true;
				} else if (this.value == 4) {
					this.revealed = this.fourImage;
					this.isOpened = true;
				} else if (this.value == 5) {
					this.revealed = this.fiveImage;
					this.isOpened = true;
				} else if (this.value == 6) {
					this.revealed = this.sixImage;
					this.isOpened = true;
				} else if (this.value == 7) {
					this.revealed = this.sevenImage;
					this.isOpened = true;
				} else if (this.value == 8) {
					this.revealed = this.eightImage;
					this.isOpened = true;
				} else if (this.value == 9) {
					this.revealed = this.theBombImage;
					MineSweeper.resetButton.setGraphic(resetButton.deadFace);
					this.isOpened = true;
				} else if (this.value == 10) {
					this.revealed = this.theFlag;
				}
				this.setGraphic(this.revealed);
			}
		}
	public void start(Stage theStage) {
		BorderPane bp = new BorderPane();
		HBox hbox = new HBox();
		GridPane sweep = new GridPane();
		MenuBar menuBar = new MenuBar();
		Menu difficulty = new Menu("Difficulty select");
		menuBar.getMenus().add(difficulty);
		MenuItem beginner = new MenuItem("Beginner");
		beginner.setOnAction(click -> {
			level = 1;
			createLevel(theStage);
		});
		MenuItem intermediate = new MenuItem("Intermediate");
		intermediate.setOnAction(click -> {
			level = 2;
			createLevel(theStage);
		});
		MenuItem expert = new MenuItem("Expert");
		expert.setOnAction(click -> {
			level = 3;
			createLevel(theStage);
		});
		difficulty.getItems().addAll(beginner, intermediate, expert);
		bp.setTop(menuBar);
		hbox.setStyle(
				"-fx-background-color: #bfbfbf; -fx-border-color: #787878 #fafafa #fafafa #787878; -fx-border-width:5; -fx-border-radius: 0.001;");
		bp.setStyle(
				"-fx-background-color: #bfbfbf; -fx-border-color: #787878 #fafafa #fafafa #787878; -fx-border-width:5; -fx-border-radius: 0.001;");
		bp.setBottom(sweep);
		hbox.setAlignment(Pos.CENTER);
		bp.setCenter(hbox);
		resetButton = new resetButton();
		resetButton.setOnAction(click -> {
			reset();
			isFirstClick = true;
			boardIsConfigured = false;
			createLevel(theStage);
		});
		updateBanner(hbox);
		buttons = new CoverButton[gridWidth][gridHeight];
		for (int x = 0; x < gridWidth; x++) {
			for (int y = 0; y < gridHeight; y++) {
				buttons[x][y] = new CoverButton();
				buttons[x][y].y = y;
				buttons[x][y].x = x;
				CoverButton button = buttons[x][y];
				button.setGraphic(button.cover);
				button.setOnAction(click -> {
					button.state = 1;
					if (!gameOver) {
						revealTile(button);
						button.setGraphic(button.revealed);
					}
					if(isFirstClick && !boardIsConfigured) {
						firstClickX = button.x;
						firstClickY = button.y;
						placeBombs(buttons);
						placeNumbers();
						boardIsConfigured = true;
						isFirstClick = false;
					}
					if(button.value == 0 && !isFirstClick) {
						checkBlank(buttons, button.x, button.y);
					}
					checkBoard(buttons, button.x, button.y);
				});
				
				button.setOnMouseClicked(click -> {
					MouseButton mouseButton = click.getButton();
					if (mouseButton == MouseButton.SECONDARY) {
						if (!gameOver && !button.isOpened) {
							if (button.isFlagged && flagsSet < 99) {
								button.setGraphic(button.cover);
								button.value = button.originalValue;
								button.isFlagged = false;
								flagsSet++;
							} else if (!button.isFlagged && flagsSet > 0) {
								button.setGraphic(button.theFlag);
								button.originalValue = button.value;
								button.value = 10;
								button.isFlagged = true;
								flagsSet--;
							}
						}
					}
					updateBanner(hbox);
				});
				
				sweep.add(buttons[x][y], x, y);
				
			}
		}
		theStage.setScene(new Scene(bp));
		theStage.show();
	}
	private void reset() {
		for (int x = 0; x < gridWidth; x++) {
			for (int y = 0; y < gridHeight; y++) {
				buttons[x][y] = new CoverButton();
			}
		}
	}
	public boolean inRangeOfFirstClick(int x, int y) {
		System.out.println("Checking if " + x + "is near " + firstClickX);
		System.out.println("Checking if " + y + "is near " + firstClickY);
		for(int i = -1; i <= 1; i++) {
			for(int j = -1; j <= 1; j++ ) {
				if(firstClickX == x + (i) && firstClickY == y + (j)) {
					return true;
				}
			}
		}
		return false;
	}
	public void placeNumbers() {
		for (int x = 0; x < gridWidth; x++) {
			for (int y = 0; y < gridHeight; y++) {
				if (buttons[x][y].value != 9) {
					buttons[x][y].value = checkDanger(buttons, x, y);
				}
			}
		}
	}
	public void revealTile(CoverButton button) {
		if (button.state == 1 && !gameOver) {
			button.setAsRevealed();
			cellCount--;
			if(button.revealed == button.theBombImage) {
				gameOver = true;
			}
			if(button.revealed == button.theFlag) {
				bombCount--;
			}
		}
	}
	public void placeBombs(CoverButton[][] button) {
		for (int i = 0; i < flagsSet; i++) {
			tryBombPlacement(button);
		}
	}
	public boolean isValid(int x, int y) {
		return (x >= 0) && (x <= gridWidth - 1) && (y >= 0) && (y <= gridHeight - 1);
	}
	public void checkBlank(CoverButton[][] buttons, int x, int y) {
		if (!buttons[x][y].isChecked) {
			buttons[x][y].isChecked = true;
			buttons[x][y].setAsRevealed();
			if(buttons[x][y].value == 0) {
				for (int i = -1; i <= 1; i++) {
					for (int j = -1; j <= 1; j++) {
						int newX = x + (j);
						int newY = y + (i);
						if(isValid(newX, newY)) {
	
							checkBlank(buttons, newX, newY);
						}
					}
				}
			}
		}
	}
	public void tryBombPlacement(CoverButton[][] button) {
		int x = (int) (Math.random() * gridWidth - 1);
		int y = (int) (Math.random() * gridHeight - 1);
		if (button[x][y].value == 9 || inRangeOfFirstClick(x, y)) {
			tryBombPlacement(button);
		} else {
			System.out.println("placing bomb at " + x + " " + y);
			System.out.println(" first vlick at " + firstClickY + " " + firstClickX);
			button[x][y].value = 9;
		}
	}
	public void createLevel(Stage theStage) {
		if (level == 1) {
			cellCount = (gridWidth * gridHeight);
			flagsSet = 10;
			gridWidth = 8;
			gridHeight = 8;
			MineSweeper gameLoad = new MineSweeper();
			gameLoad.start(theStage);
			gameOver = false;
		} else if (level == 2) {
			cellCount = (gridWidth * gridHeight);
			flagsSet = 40;
			gridWidth = 16;
			gridHeight = 16;
			MineSweeper gameLoad = new MineSweeper();
			gameLoad.start(theStage);
			gameOver = false;
		} else if (level == 3) {
			cellCount = (gridWidth * gridHeight);
			flagsSet = 99;
			gridWidth = 32;
			gridHeight = 16;
			MineSweeper gameLoad = new MineSweeper();
			gameLoad.start(theStage);
			gameOver = false;
		} 

	}
	public void updateBanner(HBox hbox) {
		int hundreds = 0;
		int tens = 0;
		int ones = 0;
		if (flagsSet >= 100) {
			hundreds = flagsSet / 100;
			tens = (flagsSet % 100) / 10;
			ones = (flagsSet % 100) % 10;
		} else if (flagsSet >= 10 && flagsSet < 100) {
			tens = flagsSet / 10;
			ones = (flagsSet % 10);
		} else {
			ones = flagsSet;
		}
		hbox.getChildren().clear();
		ImageView minesLeftBox1 = new ImageView(new Image("file:res/digits/" + hundreds + ".png"));
		ImageView minesLeftBox2 = new ImageView(new Image("file:res/digits/" + tens + ".png"));
		ImageView minesLeftBox3 = new ImageView(new Image("file:res/digits/" + ones + ".png"));
		ImageView timerBox1 = new ImageView(new Image("file:res/digits/0.png"));
		ImageView timerBox2 = new ImageView(new Image("file:res/digits/0.png"));
		ImageView timerBox3 = new ImageView(new Image("file:res/digits/0.png"));
		hbox.getChildren().addAll(minesLeftBox1, minesLeftBox2, minesLeftBox3, resetButton, timerBox1, timerBox2,
				timerBox3);
	}
	public void checkWin() {
		int tilesOpened = 0;
		for (int x = 0; x < gridWidth; x++) {
			for (int y = 0; y < gridHeight; y++) {
				if (buttons[x][y].isOpened && buttons[x][y].value != 9) {
					tilesOpened++;
				}
			}
		}
		if (tilesOpened == ((gridWidth * gridHeight) - flagsSet)) {
			MineSweeper.resetButton.setGraphic(resetButton.winFace);
			gameOver = true;
		}
		System.out.println("tilesOpened " + tilesOpened);
	}
	public void checkLose(CoverButton[][] buttons) {
		if (gameOver) {
			for (int x = 0; x < gridWidth; x++) {
				for (int y = 0; y < gridHeight; y++) {
					if (buttons[x][y].value == 9) {
						if (!buttons[x][y].isOpened) {
							buttons[x][y].revealed = buttons[x][y].greyBomb;
							buttons[x][y].setGraphic(buttons[x][y].revealed);
						}
					}
				}
			}
		}
	}
	public void checkBoard(CoverButton[][] buttons, int x, int y) {
		checkWin();
		checkLose(buttons);
	}
	public int checkDanger(CoverButton[][] grid, int x, int y) {
		int dangerCount = 0;
		if (x > 0) {
			if (isDanger(grid, x - 1, y)) {
				dangerCount++;
			}
		}
		if (y > 0) {
			if (isDanger(grid, x, y - 1)) {
				dangerCount++;
			}
		}
		if (x > 0 && y > 0) {
			if (isDanger(grid, x - 1, y - 1)) {
				dangerCount++;
			}
		}
		if (x != gridWidth - 1 && y > 0) {
			if (isDanger(grid, x + 1, y - 1)) {
				dangerCount++;
			}
		}
		if (x != gridWidth - 1) {
			if (isDanger(grid, x + 1, y)) {
				dangerCount++;
			}
		}
		if (y != gridHeight - 1) {
			if (isDanger(grid, x, y + 1)) {
				dangerCount++;
			}
		}
		if (x != gridWidth - 1 && y != gridHeight - 1) {
			if (isDanger(grid, x + 1, y + 1)) {
				dangerCount++;
			}
		}
		if (x > 0 && y != gridHeight - 1) {
			if (isDanger(grid, x - 1, y + 1)) {
				dangerCount++;
			}
		}
		return dangerCount;
	}
	public boolean isDanger(CoverButton[][] grid, int x, int y) {
		if (grid[x][y].value == 9) {
			return true;
		}
		return false;
	}
}
class resetButton extends Button {
	static ImageView smileyFace;
	static ImageView deadFace;
	static ImageView winFace;
	static ImageView oFace;
	public resetButton() {
		double resetButtonSize = 45;
		setMinWidth(resetButtonSize);
		setMinHeight(resetButtonSize);
		setMaxWidth(resetButtonSize);
		setMaxHeight(resetButtonSize);
		smileyFace = new ImageView(new Image("file:res/face-smile.png"));
		smileyFace.setFitWidth(resetButtonSize);
		smileyFace.setFitHeight(resetButtonSize);
		deadFace = new ImageView(new Image("file:res/face-dead.png"));
		deadFace.setFitWidth(resetButtonSize);
		deadFace.setFitHeight(resetButtonSize);
		winFace = new ImageView(new Image("file:res/face-win.png"));
		winFace.setFitWidth(resetButtonSize);
		winFace.setFitHeight(resetButtonSize);
		oFace = new ImageView(new Image("file:res/face-O.png"));
		oFace.setFitWidth(resetButtonSize);
		oFace.setFitHeight(resetButtonSize);
		setGraphic(smileyFace);
	}
	
}
