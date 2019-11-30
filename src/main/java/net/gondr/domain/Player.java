package net.gondr.domain;

import java.util.Random;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import net.gondr.tetris.App;
import net.gondr.utill.AppUtil;

public class Player {
	//테트리스에는 총 7개의 블럭이 있어.
	//각각의 블럭은 최대 4개의 변형을 가져
	//각각의 블럭은 모두 4개의 블럭조각으로 구성되어 있어.
	private Point2D[][][] shape = new Point2D[7][][];
	
	private int current = 0; //현재 블럭
	private int rotate = 0; //현재 회전상태
	private int nowColor = 0;
	private Color[] colorSet = new Color[7];
	
	public Random rnd;
	
	private int x = 5;
	private int y = 2;
	
	private int preX;
	private int preY;
	
	private Block[][] board;
	
	public int nextBlock;
	public int nextColor;
	
	private int holdBlock;
	private int holdColor;
	
	private int holdBlock2;
	private int holdColor2;
	
	private int holdBlock3;
	private int holdColor3;
	
	public boolean stop = false;
	public boolean check = false;
	
	public boolean notNull = false;//홀드 캔버스에 블록이 있는지 판단
	public boolean twoC = false;//홀드 버튼을 눌렀는지 판단
	public boolean getHold = false;//홀드된 블록을 가져올지 판단
		
	public Player(Block[][] board) {
		this.board = board;
		//작대기
		shape[0] = new Point2D[2][];
		shape[0][0] = getPointArray("0,-1:0,0:0,1:0,2");
		shape[0][1] = getPointArray("-1,0:0,0:1,0:2,0");
		//네모
		shape[1] = new Point2D[1][];
		shape[1][0] = getPointArray("0,0:1,0:0,1:1,1");
		//ㄴ모양
		shape[2] = new Point2D[4][];
		shape[2][0] = getPointArray("0,-2:0,-1:0,0:1,0");
		shape[2][1] = getPointArray("0,1:0,0:1,0:2,0");
		shape[2][2] = getPointArray("-1,0:0,0:0,1:0,2");
		shape[2][3] = getPointArray("-2,0:-1,0:0,0:0,-1");
		
		//역 ㄴ모양
		shape[3] = new Point2D[4][];
		shape[3][0] = getPointArray("0,-2:0,-1:0,0:-1,0");
		shape[3][1] = getPointArray("0,-1:0,0:1,0:2,0");
		shape[3][2] = getPointArray("0,0:1,0:0,1:0,2");
		shape[3][3] = getPointArray("-2,0:-1,0:0,0:0,1");
		
		shape[4] = new Point2D[2][];
		shape[4][0] = getPointArray("0,0:-1,0:0,-1:1,-1");
		shape[4][1] = getPointArray("0,0:0,-1:1,0:1,1");
		// 왼쪽무릎
		shape[5] = new Point2D[2][];
		shape[5][0] = getPointArray("0,0:0,-1:-1,-1:1,0");
		shape[5][1] = getPointArray("0,0:1,0:1,-1:0,1");
		// ㅗ 모양
		shape[6] = new Point2D[4][];
		shape[6][0] = getPointArray("0,0:0,-1:-1,0:1,0");
		shape[6][1] = getPointArray("0,0:0,-1:1,0:0,1");
		shape[6][2] = getPointArray("0,0:0,1:-1,0:1,0");
		shape[6][3] = getPointArray("0,0:-1,0:0,-1:0,1");
		
		//색상 넣기
		colorSet[0] = Color.ALICEBLUE;
		colorSet[1] = Color.AQUAMARINE;
		colorSet[2] = Color.BEIGE;
		colorSet[3] = Color.BLUEVIOLET;
		colorSet[4] = Color.CORAL;
		colorSet[5] = Color.CRIMSON;
		colorSet[6] = Color.DODGERBLUE;
		
		rnd = new Random();
		current = 0;
		nowColor = 0;
		nextBlock = rnd.nextInt(shape.length);
		nextColor = rnd.nextInt(colorSet.length);
		
		getPrePosition();
		draw(false);
	}
	
	private void getPrePosition() {
		preX = x;
		preY = y;
		
		while(true) {
			preY += 1;
			if(!checkPossible(preX, preY)) {
				preY -= 1;
				break;
			}
		}
		System.out.println(preX + ", " + preY);
		
	}
	
	private void draw(boolean remove) {
			for(int i = 0; i < shape[current][rotate].length; i++) {
				Point2D point = shape[current][rotate][i];
				//예상 포지션 먼저 그려주고
				int px = (int)point.getX() + preX;
				int py = (int)point.getY() + preY;
				try {
					board[py][px].setPreData(!remove, colorSet[nowColor]);
				} catch (Exception e) {
					System.out.println(py + ", " + px  + ", " + preX + ", " + preY);
				}
				
				
				int bx = (int)point.getX() + x;
				int by = (int)point.getY() + y;
				board[by][bx].setData(!remove, colorSet[nowColor]);
				
			}
	}
	
	public Point2D[] getPointArray(String pointStr) {
		// 0,-1:0,0:0,1:0,2 형식의 데이터 스트링이 넘어오면 이를 포인트 배열로 변환
		Point2D[] arr = new Point2D[4];
		String[] pointList = pointStr.split(":");
		for(int i = 0; i < pointList.length; i++) {
			String[] point = pointList[i].split(",");
			double x = Double.parseDouble(point[0]);
			double y = Double.parseDouble(point[1]);
			arr[i] = new Point2D(x, y);
		}
		return arr;
	}
	
	public void keyHandler(KeyEvent e) {
		int dx = 0, dy = 0; //이동한 거리
		boolean rot = false; //회전했는지 여부
		if(e.getCode() == KeyCode.ESCAPE) {
			if(!check) {
				App.app.game.stop = true;
				stop = true;
				check = true;
			}else if(check) {
				App.app.game.stop = false;
				stop = false;
				check = false;
			}
		}
		if(!stop) {
			if(e.getCode() == KeyCode.A) {
				dx -= 1;
			}else if(e.getCode() == KeyCode.D) {
				dx += 1;
			}else if(e.getCode() == KeyCode.W) {
				rot = true;
			}
			
			move(dx, dy, rot);
			
			//내려가는 로직
			if(e.getCode() == KeyCode.S) { //한칸 내리기
				down();
			}else if(e.getCode() == KeyCode.SPACE) { //한방에 내리기
				while(!down()) {
					//do nothing
				}
			}
			if(e.getCode()==KeyCode.C) {
				if(twoC){
					return;
				}else if(!twoC) {
					if(!notNull) {
						App.app.game.holdRender();
						holdBlock2 = current;
						holdColor2 = nowColor;
						draw(true);
						getNextBlock();
						notNull = true;
						twoC = true;
					}else if(notNull){
						App.app.game.holdRender();
						holdBlock3 = current;
						holdColor3 = nowColor;
						draw(true);
						getHold = true;
						getNextBlock();
						getHold = false;
						notNull = true;
						twoC = true;
					}
				}
			}
			
		}else if(stop){
			return;
		}
	}
	
	public void move(int dx, int dy, boolean rot) {
		draw(true);
		x += dx;
		y += dy;
		if(rot) {
			rotate = (rotate + 1) % shape[current].length;
		}
		
		if(!checkPossible(x, y)) {
			x -= dx;
			y -= dy;
			if(rot) {				
				rotate = rotate - 1 < 0 ? shape[current].length - 1 : rotate - 1;
			}
		}
		getPrePosition();
		draw(false);
	}
	
	public boolean down() {
		//블럭을 한칸 아래로 내리는 매서드
		draw(true);
		y += 1;
		if(!checkPossible(x, y)) {
			twoC = false;
			y -=1;
			draw(false); //내려놓은 블럭을 원상복구 하기
			App.app.game.checkLineStatus();
			getNextBlock();
			draw(false); //다음블럭 뽑은것을 그려주는거야
			return true;
		}
		draw(false);
		return false;
	}
	
	//다음블럭을 생성하는 매서드
	private void getNextBlock() {
		if(!getHold) {
			current = nextBlock;
			nowColor = nextColor;
			
			holdBlock = nextBlock;
			holdColor = nextColor;
			
			nextBlock = rnd.nextInt(shape.length);
			nextColor = rnd.nextInt(colorSet.length);
			x = 5;
			y = 2;
			rotate = 0;
			
			if(!checkPossible(x, y)) {
//				draw(true);
				App.app.game.setGameOver();
				App.app.game2.setGameOver2();
			}
			getPrePosition();
		}else if(getHold) {
			current = holdBlock2;
			nowColor = holdColor2;
			
			holdBlock = nextBlock;
			holdColor = nextColor;
			

			nextBlock = rnd.nextInt(shape.length);
			nextColor = rnd.nextInt(colorSet.length);
			x = 5;
			y = 2;
			rotate = 0;
			
			holdBlock2 = holdBlock3;
			holdColor2 = holdColor3;
			if(!checkPossible(x, y)) {
//				draw(true);
				App.app.game.setGameOver();
			}
			getPrePosition();
		}
	}
	
	private boolean checkPossible(int x, int y) {
		//블럭의 이동이 가능한지 체크하는 매서드
		for(int i = 0; i < shape[current][rotate].length; i++) {
			int bx = (int)shape[current][rotate][i].getX() + x;
			int by = (int)shape[current][rotate][i].getY() + y;
			
			if(bx < 0 || by < 0 || bx >= 10 || by >= 20) {
				return false;
			}
			
			if(board[by][bx].getFill()) {
				return false;
			}
		}
		return true;
	}
	
	public void render(GraphicsContext gc, double width, double height) {
		Color color = colorSet[nextColor];
		Point2D[] block = shape[nextBlock][0]; //다음 블럭 모양
		
		gc.clearRect(0, 0, width, height);
		double x = width / 2;
		double y = height / 2;
		double size = width / 4 - 10;
		if(nextBlock == 0) {
			y -= size;
		}
		
		for(int i = 0; i < block.length; i++) {
			double dx = x + block[i].getX() * size;
			double dy = y + block[i].getY() * size;
			gc.setFill(color.darker());
			gc.fillRoundRect(dx, dy, size, size, 4, 4);
			
			gc.setFill(color);
			gc.fillRoundRect(dx + 2, dy + 2, size - 4, size -4, 4, 4);
		}
	}
	public void holdRender(GraphicsContext gc, double width, double height) {
		Color color = colorSet[holdColor];
		Point2D[] block = shape[holdBlock][0];
		
		gc.clearRect(0, 0, width, height);
		double x = width / 2;
		double y = height / 2;
		double size = width / 4 - 10;
		if(nextBlock == 0) {
			y -= size;
		}
		
		for(int i = 0; i < block.length; i++) {
			double dx = x + block[i].getX() * size;
			double dy = y + block[i].getY() * size;
			gc.setFill(color.darker());
			gc.fillRoundRect(dx, dy, size, size, 4, 4);
			
			gc.setFill(color);
			gc.fillRoundRect(dx + 2, dy + 2, size - 4, size -4, 4, 4);
		}
	}
}
















