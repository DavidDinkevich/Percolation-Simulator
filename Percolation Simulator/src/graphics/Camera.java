package graphics;

import processing.event.MouseEvent;

public interface Camera {
		
	public void mouseDragged();
	
	public void mouseWheel(MouseEvent e);
	
	public void keyPressed();
	
	public void mousePressed();
	
	public Layer getLayer();

}
