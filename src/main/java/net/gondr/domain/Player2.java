package net.gondr.domain;

import java.util.Random;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import net.gondr.tetris.App;
import net.gondr.utill.AppUtil;

public class Player2 {
	//테트리스에는 총 7개의 블럭이 있어.
	//각각의 블럭은 최대 4개의 변형을 가져
	//각각의 블럭은 모두 4개의 블럭조각으로 구성되어 있어.
	private Point2D[][][] shape2 = new Point2D[7][][];
	
	private int current2 = 0; //현재 블럭
	private int rotate2 = 0; //현재 회전상태
	private int nowColor2 = 0;
	private Color[] colorSet2 = new Color[7];
	
	private Random rnd2;
	
	private int x2 = 5;
	private int y2 = 2;
	
	private int preX2;
	private int preY2;
	
	private Block2[][] board2;
	
	private int nextBlock2;
	private int nextColor2;
	
	private int holdBlock;
	private int holdColor;
	
	private int holdBlock2;
	private int holdColor2;
	
	private int holdBlock3;
	private int holdColor3;
	
	public boolean stop2 = false;
	public boolean check2 = false;
	
	public boolean notNull = false;//홀드 캔버스에 블록이 있는지 판단
	public boolean twoC = false;//홀드 버튼을 눌렀는지 판단
	public boolean getHold = false;//홀드된 블록을 가져올지 판단
	 
	
	public Player2(Block2[][] board2) {
		this.board2 = board2;
		//작대기
		shape2[0] = new Point2D[2][];
		shape2[0][0] = getPointArray2("0,-1:0,0:0,1:0,2");
		shape2[0][1] = getPointArray2("-1,0:0,0:1,0:2,0");
		//네모
		shape2[1] = new Point2D[1][];
		shape2[1][0] = getPointArray2("0,0:1,0:0,1:1,1");
		//ㄴ모양
		shape2[2] = new Point2D[4][];
		shape2[2][0] = getPointArray2("0,-2:0,-1:0,0:1,0");
		shape2[2][1] = getPointArray2("0,1:0,0:1,0:2,0");
		shape2[2][2] = getPointArray2("-1,0:0,0:0,1:0,2");
		shape2[2][3] = getPointArray2("-2,0:-1,0:0,0:0,-1");
		
		//역 ㄴ모양
		shape2[3] = new Point2D[4][];
		shape2[3][0] = getPointArray2("0,-2:0,-1:0,0:-1,0");
		shape2[3][1] = getPointArray2("0,-1:0,0:1,0:2,0");
		shape2[3][2] = getPointArray2("0,0:1,0:0,1:0,2");
		shape2[3][3] = getPointArray2("-2,0:-1,0:0,0:0,1");
		
		shape2[4] = new Point2D[2][];
		shape2[4][0] = getPointArray2("0,0:-1,0:0,-1:1,-1");
		shape2[4][1] = getPointArray2("0,0:0,-1:1,0:1,1");
		// 왼쪽무릎
		shape2[5] = new Point2D[2][];
		shape2[5][0] = getPointArray2("0,0:0,-1:-1,-1:1,0");
		shape2[5][1] = getPointArray2("0,0:1,0:1,-1:0,1");
		// ㅗ 모양
		shape2[6] = new Point2D[4][];
		shape2[6][0] = getPointArray2("0,0:0,-1:-1,0:1,0");
		shape2[6][1] = getPointArray2("0,0:0,-1:1,0:0,1");
		shape2[6][2] = getPointArray2("0,0:0,1:-1,0:1,0");
		shape2[6][3] = getPointArray2("0,0:-1,0:0,-1:0,1");
		
		//색상 넣기
		colorSet2[0] = Color.ALICEBLUE;
		colorSet2[1] = Color.AQUAMARINE;
		colorSet2[2] = Color.BEIGE;
		colorSet2[3] = Color.BLUEVIOLET;
		colorSet2[4] = Color.CORAL;
		colorSet2[5] = Color.CRIMSON;
		colorSet2[6] = Color.DODGERBLUE;
		
		rnd2 = new Random();
		current2 = 0;
		nowColor2 = 0;
		nextBlock2 = rnd2.nextInt(shape2.length);
		nextColor2 = rnd2.nextInt(colorSet2.length);
		
		getPrePosition2();
		draw2(false);
	}
	
	private void getPrePosition2() {
		preX2 = x2;
		preY2 = y2;
		
		while(true) {
			preY2 += 1;
			if(!checkPossible2(preX2, preY2)) {
				preY2 -= 1;
				break;
			}
		}
		System.out.println(preX2 + ", " + preY2);
		
	}
	
	private void draw2(boolean remove2) {
			for(int i = 0; i < shape2[current2][rotate2].length; i++) {
				Point2D point = shape2[current2][rotate2][i];
				//예상 포지션 먼저 그려주고
				int px = (int)point.getX() + preX2;
				int py = (int)point.getY() + preY2;
				try {
					board2[py][px].setPreData2(!remove2, colorSet2[nowColor2]);
				} catch (Exception e) {
					System.out.println(py + ", " + px  + ", " + preX2 + ", " + preY2);
				}
				
				
				int bx = (int)point.getX() + x2;
				int by = (int)point.getY() + y2;
				board2[by][bx].setData2(!remove2, colorSet2[nowColor2]);
				
			}
	}
	
	public Point2D[] getPointArray2(String pointStr2) {
		// 0,-1:0,0:0,1:0,2 형식의 데이터 스트링이 넘어오면 이를 포인트 배열로 변환
		Point2D[] arr = new Point2D[4];
		String[] pointList = pointStr2.split(":");
		for(int i = 0; i < pointList.length; i++) {
			String[] point = pointList[i].split(",");
			double x = Double.parseDouble(point[0]);
			double y = Double.parseDouble(point[1]);
			arr[i] = new Point2D(x, y);
		}
		return arr;
	}
	
	public void keyHandler(KeyEvent e2) {
		int dx2 = 0, dy2 = 0; //이동한 거리
		boolean rot2 = false; //회전했는지 여부
		if(e2.getCode() == KeyCode.ESCAPE) {
			if(!check2) {
				App.app.game2.stop2 = true;
				stop2 = true;
				check2 = true;
			}else if(check2) {
				App.app.game2.stop2 = false;
				stop2 = false;
				check2 = false;
			}
		}
		if(!stop2) {
			if(e2.getCode() == KeyCode.LEFT) {
				dx2 -= 1;
			}else if(e2.getCode() == KeyCode.RIGHT) {
				dx2 += 1;
			}else if(e2.getCode() == KeyCode.UP) {
				rot2 = true;
			}
			
			move2(dx2, dy2, rot2);
			
			//내려가는 로직
			if(e2.getCode() == KeyCode.DOWN) { //한칸 내리기
				down2();
			}else if(e2.getCode() == KeyCode.ENTER) { //한방에 내리기
				while(!down2()) {
					//do nothing
				}
			}
			if(e2.getCode()==KeyCode.SHIFT) {
				if(twoC){
					return;
				}else if(!twoC) {
					if(!notNull) {
						App.app.game2.holdRender2();
						holdBlock2 = current2;
						holdColor2 = nowColor2;
						draw2(true);
						getNextBlock2();
						notNull = true;
						twoC = true;
					}else if(notNull){
						App.app.game2.holdRender2();
						holdBlock3 = current2;
						holdColor3 = nowColor2;
						draw2(true);
						getHold = true;
						getNextBlock2();
						getHold = false;
						notNull = true;
						twoC = true;
					}
				}
			}
			
		}else if(stop2){
			return;
		}
	}
	
	public void move2(int dx2, int dy2, boolean rot2) {
		draw2(true);
		x2 += dx2;
		y2 += dy2;
		if(rot2) {
			rotate2 = (rotate2 + 1) % shape2[current2].length;
		}
		
		if(!checkPossible2(x2, y2)) {
			x2 -= dx2;
			y2 -= dy2;
			if(rot2) {				
				rotate2 = rotate2 - 1 < 0 ? shape2[current2].length - 1 : rotate2 - 1;
			}
		}
		getPrePosition2();
		draw2(false);
	}
	
	public boolean down2() {
		//블럭을 한칸 아래로 내리는 매서드
		draw2(true);
		y2 += 1;
		if(!checkPossible2(x2, y2)) {
			twoC = false;
			y2 -=1;
			draw2(false); //내려놓은 블럭을 원상복구 하기
			App.app.game2.checkLineStatus2();
			getNextBlock2();
			draw2(false); //다음블럭 뽑은것을 그려주는거야
			return true;
		}
		draw2(false);
		return false;
	}
	
	//다음블럭을 생성하는 매서드
	private void getNextBlock2() {
		if(!getHold) {
			current2 = nextBlock2;
			nowColor2 = nextColor2;
			
			holdBlock = nextBlock2;
			holdColor = nextColor2;
			
			nextBlock2 = rnd2.nextInt(shape2.length);
			nextColor2 = rnd2.nextInt(colorSet2.length);
			x2 = 5;
			y2 = 2;
			rotate2 = 0;
			
			if(!checkPossible2(x2, y2)) {
//				draw(true);
				App.app.game2.setGameOver2();
				App.app.game.setGameOver();
			}
			getPrePosition2();
		}else if(getHold) {
			current2 = holdBlock2;
			nowColor2 = holdColor2;
			
			holdBlock = nextBlock2;
			holdColor = nextColor2;
			

			nextBlock2 = rnd2.nextInt(shape2.length);
			nextColor2 = rnd2.nextInt(colorSet2.length);
			x2 = 5;
			y2 = 2;
			rotate2 = 0;
			
			holdBlock2 = holdBlock3;
			holdColor2 = holdColor3;
			if(!checkPossible2(x2, y2)) {
//				draw(true);
				App.app.game2.setGameOver2();
			}
			getPrePosition2();
		}
	}
	
	private boolean checkPossible2(int x2, int y2) {
		//블럭의 이동이 가능한지 체크하는 매서드
		for(int i = 0; i < shape2[current2][rotate2].length; i++) {
			int bx = (int)shape2[current2][rotate2][i].getX() + x2;
			int by = (int)shape2[current2][rotate2][i].getY() + y2;
			
			if(bx < 0 || by < 0 || bx >= 10 || by >= 20) {
				return false;
			}
			
			if(board2[by][bx].getFill2()) {
				return false;
			}
		}
		return true;
	}
	
	public void render2(GraphicsContext gc2, double width2, double height2) {
		Color color = colorSet2[nextColor2];
		Point2D[] block = shape2[nextBlock2][0]; //다음 블럭 모양
		
		gc2.clearRect(0, 0, width2, height2);
		double x = width2 / 2;
		double y = height2 / 2;
		double size = width2 / 4 - 10;
		if(nextBlock2 == 0) {
			y -= size;
		}
		
		for(int i = 0; i < block.length; i++) {
			double dx = x + block[i].getX() * size;
			double dy = y + block[i].getY() * size;
			gc2.setFill(color.darker());
			gc2.fillRoundRect(dx, dy, size, size, 4, 4);
			
			gc2.setFill(color);
			gc2.fillRoundRect(dx + 2, dy + 2, size - 4, size -4, 4, 4);
		}
	}
	public void holdRender2(GraphicsContext gc2, double width2, double height2) {
		Color color = colorSet2[holdColor];
		Point2D[] block = shape2[holdBlock][0];
		
		gc2.clearRect(0, 0, width2, height2);
		double x = width2 / 2;
		double y = height2 / 2;
		double size = width2 / 4 - 10;
		if(nextBlock2 == 0) {
			y -= size;
		}
		
		for(int i = 0; i < block.length; i++) {
			double dx = x + block[i].getX() * size;
			double dy = y + block[i].getY() * size;
			gc2.setFill(color.darker());
			gc2.fillRoundRect(dx, dy, size, size, 4, 4);
			
			gc2.setFill(color);
			gc2.fillRoundRect(dx + 2, dy + 2, size - 4, size -4, 4, 4);
		}
	}
}