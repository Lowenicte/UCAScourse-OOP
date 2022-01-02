/**
 * 
 */
package spirites;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;

import board.GameBoard.GameResources;
import gaming.Display;

/**
 * @author 11914
 *
 */
public abstract class Spirite implements Display {
	protected int position_x;
	protected int position_y;
	protected int size_height;
	protected int size_width;
	public Image pix;
	public int ID;
	
	@Override
	public void display(Graphics g) {
		if (pix != null) {
			g.drawImage(pix, position_x - GameResources.camera_x, position_y - GameResources.camera_y, null);
			int pix_width = pix.getWidth(null);
			int pix_height = pix.getHeight(null);
			int drawNumber_x = pix_width == -1 ? 1 : size_width / pix_width;
			int drawNumber_y = pix_height == -1 ? 1 : size_height / pix_height;
			if (drawNumber_x <= 0) drawNumber_x = 1;
			if (drawNumber_y <= 0) drawNumber_y = 1;
			
			for (int i = 0; i < drawNumber_x; i++)
				for (int j = 0; j < drawNumber_y; j++)
					g.drawImage(pix, position_x + Math.min(i*pix_width, size_width) - GameResources.camera_x, position_y + Math.min(j*pix_height, size_height) - GameResources.camera_y, null);
		}
	}
	@Override
	public void disappear(Graphics g) {
		//none now
	}
	
	public void setSize(int width, int height) {
		size_height = height;
		size_width = width;
	}
	public void setPos(int _x, int _y) {
		position_x = _x;
		position_y = _y;
	}
	public int getPosition_x() {
		return position_x;
	}
	public int getPosition_y() {
		return position_y;
	}
	public void setID(int ids) {
		ID = ids;
	}
	
	public abstract void load(String dir, int id) throws IOException;
	public abstract void save(String dir) throws IOException;
}
