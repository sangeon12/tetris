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
import net.gondr.domain.Block2;
import net.gondr.domain.Player;
import net.gondr.domain.Player2;
import net.gondr.utill.AppUtil;

public class Game2 {
	public GraphicsContext gc2;
	public Block2[][] board2;
	
	//게임판의 너비와 높이를 저장
	private double width2;
	private double height2;
	
	private AnimationTimer mainLoop2; //게임의 메인 루프
	private long before2;
	
	private Player2 player2; //지금 움직이는 블록
	private double blockDownTime2 = 0;
	
	public int score2 = 0;
	
	private Canvas nextBlockCanvas2;
	private GraphicsContext nbgc2; //다음블록 캔버스
	
	private double nbWidth2;  //다음블록 캔버스의 너비
	private double nbHeight2;//다음블록 캔버스의 높이
	
	private Label scoreLabel2;
	
	public boolean gameOver2 = false;
	
	public boolean stop2 = false;
	
	private Canvas holdCanvas2;
	private GraphicsContext hbgc2; 
	
	private double hWidth2;  
	private double hHeight2;
	
	public Game game;
	
	public Game2(Canvas canvas2, Canvas nextBlockCanvas2, Label scoreLabel2, Canvas holdCanvas2) {
		width2 = canvas2.getWidth();
		height2 = canvas2.getHeight();
		this.nextBlockCanvas2 = nextBlockCanvas2;
		this.scoreLabel2 = scoreLabel2;
		this.holdCanvas2 = holdCanvas2;
		
		this.nbgc2 = this.nextBlockCanvas2.getGraphicsContext2D();
		this.nbWidth2 = this.nextBlockCanvas2.getWidth();
		this.nbHeight2 = this.nextBlockCanvas2.getHeight();
		
		this.hbgc2 = this.holdCanvas2.getGraphicsContext2D();
		this.hWidth2 = this.holdCanvas2.getWidth();
		this.hHeight2 = this.holdCanvas2.getHeight();
		
		double size = (width2 - 4) / 10;
		board2 = new Block2[20][10]; //게임판을 만들어주고
		
		for(int i = 0; i < 20; i++) {
			for(int j = 0; j < 10; j++) {
				board2[i][j] = new Block2(j * size + 2, i * size + 2, size);
			}
		}
		gc2  = canvas2.getGraphicsContext2D();
		
		mainLoop2 = new AnimationTimer() {
			@Override
			public void handle(long now) {
				update2( (now - before2) / 1000000000d );
				before2 = now;
				render2();
			}
		};
		
		before2 = System.nanoTime();
		player2 = new Player2(board2);
		gameOver2 = true;
	}
	
	public void gameStart2() {
		gameOver2 = false;
		mainLoop2.start();
		for(int i = 0; i < 20; i++) {
			for(int j = 0; j < 10; j++) {
				board2[i][j].setData2(false, Color.WHITE);
				board2[i][j].setPreData2(false, Color.WHITE);
			}
		}
	}
	
	//매 프레임마다 실행되는 업데이트 매서드
	public void update2(double delta2) {
		if(stop2) return;
		if(gameOver2) return;
		
		blockDownTime2 += delta2;
		
		double limit = 0.5 - App.app.game.score / 10d;
		
		if(limit < 0.1) {  
			limit = 0.1;
		}
		
		if( blockDownTime2 >= limit) {
			player2.down2();
			blockDownTime2 = 0;
		}
	}
	
	//라인이 꽉 찼는지를 검사하는 매서드
	public void checkLineStatus2() {
		for(int i = 19; i >= 0; i--) {
			boolean clear = true;
			for(int j = 0; j < 10; j++) {
				if(!board2[i][j].getFill2()) {
					clear = false;  //한칸이라도 비어있다면 clear를 false로 설정
					break;
				}
			}
			
			if(clear) { //해당 줄이 꽉 찼다면
				score2++;
				for(int j = 0; j < 10; j++) {
					board2[i][j].setData2(false, Color.WHITE);
					board2[i][j].setPreData2(false, Color.WHITE);
				}
				
				for(int k = i - 1; k >= 0; k--) {
					for(int j = 0; j < 10; j++) {
						board2[k+1][j].copyData2(board2[k][j]);
					}
				}
				
				for(int j = 0; j < 10; j++) {
					board2[0][j].setData2(false, Color.WHITE);
					board2[i][j].setPreData2(false, Color.WHITE);
				}
				i++;
			}
		}
		
	}
	
	//매 프레임마다 화면을 그려주는 매서드
	public void render2() {
		gc2.clearRect(0, 0, width2, height2);
		gc2.setStroke(Color.rgb(0,0,0));
		gc2.setLineWidth(2);
		gc2.strokeRect(0, 0, width2, height2);
		
		for(int i = 0; i < 20; i++) {
			for(int j = 0; j < 10; j++) {
				board2[i][j].render2(gc2);
			}
		}
		
		scoreLabel2.setText("Score : \n" + score2);
		
		player2.render2(nbgc2, nbWidth2, nbHeight2);
		
		if(gameOver2&&!App.app.game.gameOver) {
			gc2.setFont(new Font("Arial", 30));
			gc2.setTextAlign(TextAlignment.CENTER);
			gc2.strokeText("Lose", width2 / 2, height2 / 2);
			
		}else {
			return;
		}
	}
	
	public void keyHandler(KeyEvent e2) {
		if(gameOver2) return;
		player2.keyHandler(e2);
	}
	
	public void setGameOver2() {
		
		gameOver2 = true;
		render2();
		mainLoop2.stop();
		
		
	}
	public void holdRender2() {
		player2.holdRender2(hbgc2, hWidth2, hHeight2);
	}
	
}