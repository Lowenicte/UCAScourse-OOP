/**
 * 
 */
package spirites;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import board.GameBoard.GameResources;
import spirites.Entity.ActionMessage;
import tools.Counter;
/**
 * @author lowenicte
 *
 */
public class NPC extends Entity {
	int level;
	private int atk;
	private int def;
	private int hp;
	private int maxHp;
	private int sp;
	private int speed = 10;
	private int belongs;
	//private String dialogue;
	private int dialogueBranch;
	
	private ArrayList<ActionMessage> msg;
	
	public NPC() {
		super();
		msg = new ArrayList<ActionMessage>();
	}
	
	public ArrayList<ActionMessage> generateAction() {
		if (hp <= 0) {
			msg.add(new ActionMessage(Action.DESTROY, 0, 0, 0, 0, ID));
		}
		
		if (msg.isEmpty())
			return null;
		ArrayList<ActionMessage> ret = new ArrayList<ActionMessage>();
		msg.forEach((msg) -> { ret.add(msg); });
		msg.clear();
		return ret;
	}
	
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
	public void setBelong(int belong_s) {
		belongs = belong_s;
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
		case TALK:
			if (dialogueBranch != -1)
				msg.add(new ActionMessage(Action.BRANCH, 1, dialogueBranch, 0, 0, ID));
			break;
		default:
			break;
		}
		return 1;
	}

	@Override
	public void display(Graphics g) {
		if (pix != null) {
			g.drawImage(pix, position_x - GameResources.camera_x, position_y - GameResources.camera_y, null);
			g.setFont(new Font("仿宋", Font.BOLD, 20));
			if (belongs == 1)
				g.setColor(Color.GREEN);
			else g.setColor(Color.RED);
			g.drawString(name, position_x - GameResources.camera_x, position_y - 15 - GameResources.camera_y);
			
			if (maxHp/hp == 1) {
				g.setColor(Color.GREEN);
			} else if (maxHp/hp < 5) {
				g.setColor(Color.ORANGE);
			} else {
				g.setColor(Color.RED);
			}
			g.fillRect(position_x - GameResources.camera_x, position_y - GameResources.camera_y - 10, (int)(size_width*((double)hp)/maxHp), 10);
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
		sp = Counter.getFirstNumber(inData, cnt);
		atk = Counter.getFirstNumber(inData, cnt);
		def = Counter.getFirstNumber(inData, cnt);
		speed = Counter.getFirstNumber(inData, cnt);
		belongs = Counter.getFirstNumber(inData, cnt);
		dialogueBranch = Counter.getFirstNumber(inData, cnt);
		
		fin.close();
	}

	@Override
	public void save(String dir) throws IOException {
		
	}
}
