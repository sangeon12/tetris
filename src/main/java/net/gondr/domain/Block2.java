package net.gondr.domain;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Block2 {
	private Color color2;
	private boolean fill2;
	public boolean pre2; //미리보기
	private double x2;
	private double y2;
	private double size2;
	private double borderSize2;
	
	public Block2(double x2, double y2, double size2) {
		color2 = Color.WHITE;
		fill2 = false;
		pre2 = false;
		this.x2 = x2;
		this.y2 = y2;
		this.size2 = size2;
		this.borderSize2 = 2;
	}
	
	public void setPreData2(boolean pre2, Color color2) {
		this.pre2 = pre2;
		this.color2 = color2;
	}
	
	public void render2(GraphicsContext gc2) {
		if(fill2) {
			gc2.setFill(color2.darker());
			gc2.fillRoundRect(x2, y2, size2, size2, 4, 4);
			
			gc2.setFill(color2);
			gc2.fillRoundRect(
				x2 + borderSize2, y2 + borderSize2,
				size2 - 2* borderSize2, size2 - 2*borderSize2,
				4, 4);
		}else if(pre2) {
			gc2.setFill(color2.brighter());
			gc2.fillRoundRect(x2, y2, size2, size2, 4, 4);
			
			gc2.setFill(color2.brighter().brighter());
			gc2.fillRoundRect(
				x2 + borderSize2, y2 + borderSize2,
				size2 - 2* borderSize2, size2 - 2*borderSize2,
				4, 4);
		}
	}
	
	public void setData2(boolean fill2, Color color2) {
		this.fill2 = fill2;
		this.color2 = color2;
	}
	
	public boolean getFill2() {
		return fill2;
	}
	
	public Color getColor2() {
		return color2;
	}
	
	public boolean getPre2() {
		return pre2;
	}

	public void getPre2(boolean pre2) {
		this.pre2 = pre2;
	}

	public void copyData2(Block2 block2) {
		this.pre2 = block2.getPre2();
		this.fill2 = block2.getFill2();
		this.color2 = block2.getColor2();
	}
}





