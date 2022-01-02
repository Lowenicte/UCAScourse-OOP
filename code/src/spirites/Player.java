package spirites;
import item.Skill;
import menu.Term;
import spirites.Entity.ActionMessage;
import tools.Counter;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.MenuKeyEvent;

import board.GameBoard.GameResources;
import gaming.*;

import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * @author lowenicte
 *
 */

public class Player extends Entity implements Operate {
	int level;
	private int atk;
	private int def;
	private int sp;
	private int maxSp;
	private int hp;
	private int maxHp;
	private int speed = 10;
	int[] bagArr;
	Skill[] skillList;
	
	private ArrayList<ActionMessage> msg;
	private int playerStatus;
	private int statusTimer; 
	
	public void setLevel(int l) {
		level = l;
	}
	public void setAtk(int atk_s) {
		atk = atk_s;
	}
	public void setDef(int def_s) {
		def = def_s;
	}
	public void setSp(int sp_s) {
		sp = sp_s;
	}
	public Player() {
		super();
		msg = new ArrayList<ActionMessage>();
	}
	
	@Override
	public int interact(ActionMessage actMsg) {
		switch (actMsg.act) {
		case ATTACK:
			hp -= actMsg.scale - def;
			break;
		case HEAL:
			hp += actMsg.scale;
			break;
		case MOVE:
			position_x += (actMsg.area & 8)*actMsg.scale - (actMsg.area & 4)*actMsg.scale;
			position_y += (actMsg.area & 2)*actMsg.scale - (actMsg.area & 1)*actMsg.scale;
			break;
		default:
			break;
		}
		return 0;
	}

	@Override
	public int operate(boolean[] keys) {
		if (keys[KeyEvent.VK_W] || keys[KeyEvent.VK_UP]) {
			msg.add(new ActionMessage(Action.MOVE, 1, speed, position_x, position_y, ID));
		}
		if (keys[KeyEvent.VK_S] || keys[KeyEvent.VK_DOWN]) {
			msg.add(new ActionMessage(Action.MOVE, 2, speed, position_x, position_y, ID));
		}
		if (keys[KeyEvent.VK_A] || keys[KeyEvent.VK_LEFT]) {
			msg.add(new ActionMessage(Action.MOVE, 4, speed, position_x, position_y, ID));
		}
		if (keys[KeyEvent.VK_D] || keys[KeyEvent.VK_RIGHT]) {
			msg.add(new ActionMessage(Action.MOVE, 8, speed, position_x, position_y, ID));
		}
		return 1;
	}

	@Override
	public int operate(int[] keys, int keyLength) {
		for (int i = 0; i < keyLength; i++) {
			switch (keys[i]) {
			case KeyEvent.VK_B:
				msg.add(new ActionMessage(Action.BRANCH, 0, 0, 0, 0, ID));
				break;
			
			case KeyEvent.VK_F:
				msg.add(new ActionMessage(Action.TALK, 20 + (int)Math.sqrt((size_width*size_width + size_height*size_height)/2),
						  atk, position_x + size_width/2, position_y + size_height/2, ID));
				break;
				
			case KeyEvent.VK_J:
				msg.add(new ActionMessage(Action.ATTACK, 20 + (int)Math.sqrt((size_width*size_width + size_height*size_height)/2),
										  atk, position_x + size_width/2, position_y + size_height/2, ID));
				break;
			default:
				if (keys[i] >= '0' && keys[i] <= '9') {
					int skillCast = keys[i] - '0';
				}
				break;
			}
		}
		return 1;
	}
	
	public ArrayList<ActionMessage> generateAction() {
		//detect death
		if (hp <= 0) {
			msg.add(new ActionMessage(Action.DESTROY, 0, 0, 0, 0, ID));
		}
		//send the msg
		if (msg.isEmpty())
			return null;
		ArrayList<ActionMessage> ret = new ArrayList<ActionMessage>();
		msg.forEach((msg) -> { ret.add(msg); });
		msg.clear();
		return ret;
	}
	
	@Override
	public void display(Graphics g) {
		if (pix != null) {
			g.setFont(new Font("仿宋", Font.BOLD, 20));
			g.setColor(Color.ORANGE);
			g.drawString(name, position_x - GameResources.camera_x, position_y - 15 - GameResources.camera_y);
			
			if (maxHp/hp == 1) {
				g.setColor(Color.GREEN);
			} else if (maxHp/hp < 5) {
				g.setColor(Color.ORANGE);
			} else {
				g.setColor(Color.RED);
			}
			g.fillRect(position_x - GameResources.camera_x, position_y - GameResources.camera_y - 10, (int)(size_width*((double)hp)/maxHp), 10);
			g.drawImage(pix, position_x - GameResources.camera_x, position_y - GameResources.camera_y, null);
		}
	}
	
	@Override
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
		//read infos
		//in spirite
		ID = id;
		position_x = Counter.getFirstNumber(inData, cnt);
		position_y = Counter.getFirstNumber(inData, cnt);
		size_width = Counter.getFirstNumber(inData, cnt);
		size_height = Counter.getFirstNumber(inData, cnt);
		String imageDir = Counter.getFirstString(inData, cnt);
		pix = Toolkit.getDefaultToolkit().getImage(imageDir);
		//entity
		name = Counter.getFirstString(inData, cnt);
		//player
		level = Counter.getFirstNumber(inData, cnt);
		maxHp = Counter.getFirstNumber(inData, cnt);
		hp = maxHp;
		maxSp = Counter.getFirstNumber(inData, cnt);
		sp = maxSp;
		atk = Counter.getFirstNumber(inData, cnt);
		def = Counter.getFirstNumber(inData, cnt);
		speed = Counter.getFirstNumber(inData, cnt);
		
		fin.close(); 
	}
	
	@Override
	public void save(String dir) throws IOException {
		
	}
}
