package renderer;

import java.awt.Color;

public class ZBuffer {
	
	public Color[][] color;
	public float[][] depth;
	
	public ZBuffer(){}
	
	public void setColor(Color[][] color){
		this.color = color;
	}
	
	public void setDepth(float[][] depth){
		this.depth = depth;
	}

}
