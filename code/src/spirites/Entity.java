/**
 * 
 */
package spirites;

import java.util.ArrayList;

import gaming.*;

/**
 * @author lowenicte
 *
 */
public abstract class Entity extends Spirite implements CrashDetect, Interact {
	
	public enum Action {
		NONE, ATTACK, HEAL, TALK, GET, USE, MOVE, DESTROY, BRANCH
	}
	
	public String name;
	public abstract ArrayList<ActionMessage> generateAction();
	
	
	public boolean crashDetect(Spirite e, ActionMessage msg) {
		int f_x = (msg.area & 8)*msg.scale - (msg.area & 4)*msg.scale + e.position_x;
		int f_y = (msg.area & 2)*msg.scale - (msg.area & 1)*msg.scale + e.position_y;
		if (f_x >= position_x + size_width || f_x + e.size_width <= position_x
		 || f_y >= position_y + size_height || f_y + e.size_height <= position_y)
			return false;
		return true;
	}
	
	public boolean generalCrashDetect(int center_x, int center_y, int area) {
		int d_x = position_x + size_width/2 - center_x;
		int d_y = position_x + size_height/2 - center_y;
		return (d_x*d_x + d_y*d_y <= area*area);
	}
	public boolean generalCrashDetect(ActionMessage msg) {
		int d_x = position_x + size_width/2 - msg.position_x;
		int d_y = position_y + size_height/2 - msg.position_y;
		return (d_x*d_x + d_y*d_y <= msg.area*msg.area);
	}
	
	static public class ActionMessage {
		public Action act;
		public int position_x, position_y;
		public int area;
		public int scale;
		public int id; 
		
		public ActionMessage(Action _a, int _size, int _scale, int _x, int _y, int ids) {
			act = _a;
			area = _size;
			scale = _scale;
			position_x = _x;
			position_y = _y;
			id = ids;
		}

		public ActionMessage() {
			act = Action.NONE;
			area = 0;
			scale = 0;
			position_x = 0;
			position_y = 0;
		}
	}
}
