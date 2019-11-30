package net.gondr.tetris;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import net.gondr.domain.Block;
import net.gondr.domain.Player;
import net.gondr.domain.Player2;
import net.gondr.utill.AppUtil;

public class Game {
	public GraphicsContext gc;
	public Block[][] board;
	
	//게임판의 너비와 높이를 저장
	private double width;
	private double height;
	
	private AnimationTimer mainLoop; //게임의 메인 루프
	private long before;
	
	private Player player; //지금 움직이는 블록
	private double blockDownTime = 0;
	
	public int score = 0;
	
	private Canvas nextBlockCanvas;
	private GraphicsContext nbgc; //다음블록 캔버스
	
	private double nbWidth;  //다음블록 캔버스의 너비
	private double nbHeight;//다음블록 캔버스의 높이
	
	private Label scoreLabel;
	
	public boolean gameOver = false;
	
	public boolean stop = false;
	
	private Canvas holdCanvas;
	private GraphicsContext hbgc; 
	
	private double hWidth;  
	private double hHeight;
	
	public Game2 game2;
	
	public Game(Canvas canvas, Canvas nextBlockCanvas, Label scoreLabel, Canvas holdCanvas) {
		width = canvas.getWidth();
		height = canvas.getHeight();
		this.nextBlockCanvas = nextBlockCanvas;
		this.scoreLabel = scoreLabel;
		this.holdCanvas = holdCanvas;
		
		this.nbgc = this.nextBlockCanvas.getGraphicsContext2D();
		this.nbWidth = this.nextBlockCanvas.getWidth();
		this.nbHeight = this.nextBlockCanvas.getHeight();
		
		this.hbgc = this.holdCanvas.getGraphicsContext2D();
		this.hWidth = this.holdCanvas.getWidth();
		this.hHeight = this.holdCanvas.getHeight();
		
		double size = (width - 4) / 10;
		board = new Block[20][10]; //게임판을 만들어주고
		
		for(int i = 0; i < 20; i++) {
			for(int j = 0; j < 10; j++) {
				board[i][j] = new Block(j * size + 2, i * size + 2, size);
			}
		}
		gc  = canvas.getGraphicsContext2D();
		
		mainLoop = new AnimationTimer() {
			@Override
			public void handle(long now) {
				update( (now - before) / 1000000000d );
				before = now;
				render();
			}
		};
		
		before = System.nanoTime();
		player = new Player(board);
		gameOver = true;
	}
	
	public void gameStart() {
		gameOver = false;
		mainLoop.start();
		for(int i = 0; i < 20; i++) {
			for(int j = 0; j < 10; j++) {
				board[i][j].setData(false, Color.WHITE);
				board[i][j].setPreData(false, Color.WHITE         );
			}
		}
	}
	
	//매 프레임마다 실행되는 업데이트 매서드
	public void update(double delta) {
		if(stop) return;
		if(gameOver) return;
		
		blockDownTime += delta;
		
		double limit = 0.5 - App.app.game2.score2 / 10d;
		
		if(limit < 0.1) {  
			limit = 0.1;
		}
		
		if( blockDownTime >= limit) {
			player.down();
			blockDownTime = 0;
		}
	}
	
	//라인이 꽉 찼는지를 검사하는 매서드
	public void checkLineStatus() {
		for(int i = 19; i >= 0; i--) {
			boolean clear = true;
			for(int j = 0; j < 10; j++) {
				if(!board[i][j].getFill()) {
					clear = false;  //한칸이라도 비어있다면 clear를 false로 설정
					break;
				}
			}
			
			if(clear) { //해당 줄이 꽉 찼다면
				score++;
				for(int j = 0; j < 10; j++) {
					board[i][j].setData(false, Color.WHITE);
					board[i][j].setPreData(false, Color.WHITE);
				}
				
				for(int k = i - 1; k >= 0; k--) {
					for(int j = 0; j < 10; j++) {
						board[k+1][j].copyData(board[k][j]);
					}
				}
				
				for(int j = 0; j < 10; j++) {
					board[0][j].setData(false, Color.WHITE);
					board[i][j].setPreData(false, Color.WHITE);
				}
				i++;
			}
		}
		
	}
	
	//매 프레임마다 화면을 그려주는 매서드
	public void render() {
		gc.clearRect(0, 0, width, height);
		gc.setStroke(Color.rgb(0,0,0));
		gc.setLineWidth(2);
		gc.strokeRect(0, 0, width, height);
		
		for(int i = 0; i < 20; i++) {
			for(int j = 0; j < 10; j++) {
				board[i][j].render(gc);
			}
		}
		
		scoreLabel.setText("Score : \n" + score);
		
		player.render(nbgc, nbWidth, nbHeight);
		
		if(gameOver&&!App.app.game2.gameOver2) {
			gc.setFont(new Font("Arial", 30));
			gc.setTextAlign(TextAlignment.CENTER);
			gc.strokeText("Lose", width / 2, height / 2);
			
		}else {
			return;
		}
		
		
	}
	
	public void keyHandler(KeyEvent e) {
		if(gameOver) return;
		player.keyHandler(e);
	}
	
	public void setGameOver() {
		gameOver = true;
		render();
		mainLoop.stop();
		
		
	}
	public void holdRender() {
		player.holdRender(hbgc, hWidth, hHeight);
	}
	
}