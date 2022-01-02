package board;

import javax.swing.JFrame;

import board.GameBoard.GameResources;
import gaming.CrashDetect;
import gaming.Interact;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import item.*;
import menu.*;
import menu.Menu;
import spirites.*;
import spirites.Entity.Action;
import spirites.Entity.ActionMessage;
import tools.Counter;

/**
 * @author lowenicte
 *
 */

public class Dispatcher implements Interact {
	//managing resources
	//now player
	Player player;
	//msg list
	ArrayList<Entity.ActionMessage> msgList;
	//item and menu
	
	
	int map;
	//where we are
	int[] gameStack;
	int   stackTop;
	
	//detected io input
	boolean[] input;
	int[] inputbuffer;
	int bufferLength;
	
	private static Dispatcher dispatcher = null;
	private Dispatcher(GameBoard board) {
		//init all resources
		GameResources.reflushResources();
		msgList = new ArrayList<Entity.ActionMessage>();
		player = new Player();
		gameStack = new int[32];
		stackTop = 0;
		gameStack[0] = 2;
		map = 0;
		
		//load begin page, player
		GameResources.menuList.add(new Menu());
		GameResources.entityList.add(player);
		GameResources.spiriteList.add(player);
		try {
			GameResources.menuList.get(0).load("data/menu/menu0.txt", 0);
			player.load("data/play/player.txt", 0);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		//add key listener and prepare input list
		board.addKeyListener(new Dispatcher.KeyMoniter()); 
		input = new boolean[128];
		inputbuffer = new int[64];
	}
	
	public static synchronized Dispatcher getDispatcher(GameBoard board) {
		if (dispatcher == null)
			dispatcher = new Dispatcher(board);
		else board.addKeyListener(dispatcher.new KeyMoniter());
		return dispatcher;
	} 
	
	
	public int scheduler(Graphics g) {
		if (stackTop < 0)
			return -1;
		//gaming
		switch (gameStack[stackTop]) {
		//game exit or error
		case 0:
			if (stackTop == 0) {
				GameResources.clearAllResources();
				return -1;
			}
			else {
				if (interact(new ActionMessage(Action.BRANCH, 0, 0, 0, 0, 0)) == 0)
					return -1;
			}
			break;
		
		//main game
		case 1:
			GameResources.setCamera(player.getPosition_x(), player.getPosition_y());
			//process input first
			player.operate(input);
			player.operate(inputbuffer, bufferLength);
			bufferLength = 0;
			
			//get acts
			msgList.clear();
			ArrayList<Entity.ActionMessage> eMsg;
			for (Entity entity : GameResources.entityList) {
				eMsg = entity.generateAction();
				if (eMsg != null)
					msgList.addAll(eMsg);
			}
			
			//process action
			msgList.forEach(m -> interact(m));
			
			//display
			g.setColor(Color.gray);
			g.fillRect(-GameResources.camera_x, -GameResources.camera_y, 800, 600);
			GameResources.spiriteList.forEach(e -> e.display(g));
			break;
		
		default:
			if (gameStack[stackTop] - 2 < GameResources.menuList.size() && gameStack[stackTop] > 1) {
				int menu = gameStack[stackTop] - 2;
				GameResources.menuList.get(menu).operate(input);
				int newStatus = GameResources.menuList.get(menu).operate(inputbuffer, bufferLength);
				bufferLength = 0;
				
				if (newStatus != gameStack[stackTop] - 2) {
					if (newStatus == -1)
						gameStack[stackTop] = 0;
					else if (interact(new ActionMessage(Action.BRANCH, (newStatus == 1 ? newStatus : newStatus + 2), (newStatus == 1 ? 0 : newStatus - 2), 0, 0, 0)) == 0)
						return -1;
				}
				GameResources.menuList.get(menu).display(g);
			}
			break;
		}
		return 0;
	}
	
	//process msg at dispatcher side
	public int interact(ActionMessage msg) {
		boolean crash = false;
		switch (msg.act) {
		case MOVE:
			for (Spirite sprites : GameResources.spiriteList) {
				if (msg.id != sprites.ID && sprites instanceof CrashDetect)
					crash |= ((CrashDetect) sprites).crashDetect(player, msg);
			}
			if (!crash) {
				player.interact(msg);
			}
			break;
		case DESTROY:
			for (Entity entity : GameResources.entityList) {
				if (entity.ID == msg.id) {
					GameResources.spiriteList.remove((Spirite)entity);
					GameResources.entityList.remove(entity);
					break;
				}
			}
			break;
		case BRANCH:
			if (msg.area == 0) {
				stackTop--;
				if (stackTop < 0)
					return 0;
				GameResources.clearSceneResources();
				String dir;
				if (gameStack[stackTop] == 1) {
					dir = "data/map/map" + Integer.toString(msg.scale) + ".txt";
					if (load(dir, 1) == 0) {
						stackTop--;
					}
				} else {
					dir = "data/menu/menu" + Integer.toString(msg.scale) + ".txt";
					if (load(dir, msg.scale + 2) == 0) {
						stackTop--;
					}
				}
			} else if (msg.area == 1) {
				GameResources.clearSceneResources();
				stackTop++;
				gameStack[stackTop] = 1;
				String dir = "data/map/map" + Integer.toString(msg.scale) + ".txt";
				if (load(dir, 1) == 0) {
					stackTop--;
				}
			} else {
				stackTop++;
				gameStack[stackTop] = GameResources.menuList.size() + 2;
				String dir = "data/menu/menu" + Integer.toString(msg.scale) + ".txt";
				if (load(dir, msg.scale + 2) == 0) {
					stackTop--;
				}
			}
			break;
		default:
			for (Entity entity : GameResources.entityList) {
				if (msg.id != entity.ID && entity.generalCrashDetect(msg)) {
					entity.interact(msg);
				}
			}
			break;
		}
		return 1;
	}
	
	public int load(String dir, int status) {
		InputStream fin;
		File f;
		byte[] inData = new byte[1000];
		Counter cnt = new Counter();
		
		try {
			f = new File(dir);
			fin = new FileInputStream(f);
			fin.read(inData, 0, 1000);
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
		
		if (status == 1) {
			int npcNumber, itemNumber, blockNumber, num;
			String loaddir;
			
			npcNumber = Counter.getFirstNumber(inData, cnt);
			itemNumber = Counter.getFirstNumber(inData, cnt);
			blockNumber = Counter.getFirstNumber(inData, cnt);
			player.setPos(0, 0);
			GameResources.spiriteList.add(player);
			GameResources.entityList.add(player);
			//add npc
			for (int i = 0; i < npcNumber; i++) {
				num = Counter.getFirstNumber(inData, cnt);
				loaddir = "data/play/npc" + Integer.toString(num) + ".txt";
				NPC newnpc = new NPC();
				try {
					newnpc.load(loaddir, i + 1);
				} catch (IOException e) {
					e.printStackTrace();
					return 0;
				}
				GameResources.entityList.add(newnpc);
				GameResources.spiriteList.add(newnpc);
			}
			//add item
			for (int i = 0; i < itemNumber; i++) {
				num = Counter.getFirstNumber(inData, cnt);
				loaddir = "data/item/item" + Integer.toString(num) + ".txt";
				Item newitem = new Item();
				GameResources.itemList.add(newitem);
			}
			//add blocks
			for (int i = 0; i < blockNumber; i++) {
				num = Counter.getFirstNumber(inData, cnt);
				loaddir = "data/block/block" + Integer.toString(num) + ".txt";
				Blocks newblock = new Blocks();
				try {
					newblock.load(loaddir, i);
				} catch (IOException e) {
					e.printStackTrace();
					return 0;
				}
				GameResources.spiriteList.add(newblock);
			}
			
		} else if (status > 1) {
			String loaddir = "data/menu/menu" + Integer.toString(status - 2) + ".txt";
			Menu newmenu = new Menu();
			try {
				newmenu.load(loaddir, status - 2);
			} catch (IOException e) {
				e.printStackTrace();
				return 0;
			}
			GameResources.menuList.add(newmenu);
		}
		return 1;
	}
	
	public class KeyMoniter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			if (key < 128)
				input[key] = true;
			inputbuffer[bufferLength++] = key;
		}
		
		@Override
		public void keyReleased(KeyEvent e) {
			int key = e.getKeyCode();
			if (key < 128)
				input[e.getKeyCode()] = false;
		}
	}
}
