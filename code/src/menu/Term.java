/**
 * 
 */
package menu;
import java.awt.*;
import javax.swing.*;
/**
 * @author lowenicte
 *
 */
public class Term {
	String info;
	int position_x, position_y;
	int branch;
	int size;
	
	void display(Graphics g) {
		g.setColor(Color.black);
		g.setFont(new Font("仿宋", Font.PLAIN, size));
		g.drawString(info, position_x, position_y);
	}
	
	public void setSize(int s) {
		size = s;
	}
	public void setInfo(String information) {
		info = information;
	}
	public void setPosition(int _x, int _y) {
		position_x = _x;
		position_y = _y;
	}
	public void setBranch(int br) {
		branch = br;
	}
}
