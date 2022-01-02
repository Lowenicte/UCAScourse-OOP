/**
 * 
 */
package menu;
import java.awt.*;
import javax.swing.*;

import gaming.*;

import java.io.*;
import java.awt.event.*;

import tools.Counter;


/**
 * @author lowenicte
 *
 */
public class Menu implements Display, Operate {
	//terms
	private Term[] termList;
	//output info
	private String info;
	private int menuSize;
	private int position_x, position_y;
	
	public int mid;
	//main info
	private int choice;
	private int total;
	//arrow image
	private Image icon = Toolkit.getDefaultToolkit().getImage("img/arrow.jpg");
	
	@Override
	public void display(Graphics g) {
		//show info;
		g.setColor(Color.black);
		
		for (int i = 0; i < termList.length; i++) {
			termList[i].display(g);
		}
		if (total > 0) {
			g.drawImage(icon, termList[choice].position_x - 60, termList[choice].position_y - 50, null);
		}
		g.setFont(new Font("仿宋", Font.PLAIN, menuSize));
		g.drawString(info, position_x, position_y);
	}
	
	
	public void load(String dir, int id) throws IOException {
		InputStream fin;
		File f;
		byte[] inData = null;
		f = new File(dir);
		fin = new FileInputStream(f);
		inData = new byte[1000];
		fin.read(inData, 0, 1000); 
		
		Counter cnt = new Counter();
		cnt.set(0);
		//first, get the number of terms
		total = Counter.getFirstNumber(inData, cnt);
		if (total == 0)
			return;
		
		//alloc list and initialize params
		termList = new Term[total];
		mid = id;
		choice = 0;
		//get the text for the hole menu
		position_x = Counter.getFirstNumber(inData, cnt);
		position_y = Counter.getFirstNumber(inData, cnt);
		info = Counter.getFirstString(inData, cnt);
		menuSize = Counter.getFirstNumber(inData, cnt);
		
		for (int i = 0; i < total; i++) {
			termList[i] = new Term();
			//read position and set
			termList[i].setPosition(Counter.getFirstNumber(inData, cnt), Counter.getFirstNumber(inData, cnt));
			//read term information and set
			termList[i].setInfo(Counter.getFirstString(inData, cnt));
			termList[i].setSize(Counter.getFirstNumber(inData, cnt));
			//read branch rets and set
			termList[i].setBranch(Counter.getFirstNumber(inData, cnt));
		}
		fin.close();
	}

	@Override
	public void disappear(Graphics g) {
		//none as now we don't repaint
	}

	@Override
	public int operate(boolean[] keys) {
		//there is no situation using this methord
		return mid;
	}


	@Override
	public int operate(int[] keys, int keyLength) {
		for (int i = 0; i < keyLength; i++) {
			switch (keys[i]) {
			//move up
			case KeyEvent.VK_W: case KeyEvent.VK_UP:
				if (choice > 0)
					choice--;
			break;
			//move down
			case KeyEvent.VK_S: case KeyEvent.VK_DOWN:
				if (choice < total - 1)
					choice++;
				break;
			//confirm choice
			case KeyEvent.VK_ENTER:
				if (choice >= 0 && choice < total)
					return termList[choice].branch;
				break;
			}
		}
		return mid;
	}

}
