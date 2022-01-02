/**
 * 
 */
package board;
import javax.swing.JFrame;

import board.GameBoard.GameResources;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import item.*;
import menu.Menu;
import spirites.*; 
/**
 * @author 11914
 *
 */
public class GameBoard extends JFrame {
	
	public static class GameResources {
		public static ArrayList<Entity> entityList;
		public static ArrayList<Spirite> spiriteList; 
		public static ArrayList<Item> itemList;
		public static ArrayList<Menu> menuList;
		
		public static int camera_x;
		public static int camera_y;
		
		public static void reflushResources() {
			entityList = new ArrayList<Entity>();
			spiriteList = new ArrayList<Spirite>();
			itemList = new ArrayList<Item>();
			menuList = new ArrayList<Menu>();
		}
		public static void clearSceneResources() {
			entityList.clear();
			spiriteList.clear();
			camera_x = 0;
			camera_y = 0;
		}
		public static void clearAllResources() {
			entityList.clear();
			spiriteList.clear();
			itemList.clear();
			menuList.clear();
			camera_x = 0;
			camera_y = 0;
		}
		
		public static void setCamera(int x, int y) {
			camera_x = x - sWidth/2;
			camera_y = y - sHeight/2;
		}
	}
	
	//screen size
	private static int sWidth = 1280;
	private static int sHeight = 720;
	//screen double buffer
	Image buffer = null;

	private static GameBoard gameBoard;
	private GameBoard() {
		super();
		//initialize the screen
		setTitle("a game by lowenicte");
		setSize(sWidth, sHeight);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
		if (buffer == null)
			buffer = this.createImage(sWidth, sHeight);
	}
	
	public static synchronized GameBoard getGameBoard() {
		if (gameBoard == null)
			gameBoard = new GameBoard();
		return gameBoard;
	}
	
	
	@Override
	public void paint(Graphics g) {
		if (buffer != null) {
			g.drawImage(buffer, 0, 0, null);
		}
	}
	
	public void lunchGame() {
		Dispatcher gameDispatcher = Dispatcher.getDispatcher(this);
		Graphics g = buffer.getGraphics();
		
		while (true) {
			//now we choose to clear the screen every time
			g.setColor(Color.white);
			g.fillRect(0, 0, sWidth, sHeight);
			
			if (gameDispatcher.scheduler(g) == -1)
				return;
			
			repaint();
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
}
