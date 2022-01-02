/**
 * 
 */
package spirites;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;

import spirites.Entity.ActionMessage;
/**
 * @author lowenicte
 *
 */
public class Effect extends Entity {
	private int atk;
	double[] elements;
	int timer;
	
	public ArrayList<ActionMessage> generateAction() {
		ArrayList<ActionMessage> eMsg = new ArrayList<ActionMessage>();
		if (timer < 0) {
			eMsg.add(new ActionMessage(Action.DESTROY, 0, 0, 0, 0, ID));
		}
		else {
			eMsg.add(null);
		}
		return null;
	}
	
	public void setAtk(int atk_s) {
		atk = atk_s;
	}
	public void setElements(double[] eles) {
		for (int i = 0; i < eles.length; i++)
			elements[i] = eles[i];
	}
	public void setTimer(int time) {
		timer = time;
	}
	
	@Override
	public int interact(ActionMessage actMsg) {
		return 0;
	}

	@Override
	public void load(String dir, int id) throws IOException {
		//skill don't need load
		//skills are set by items instead
	}

	@Override
	public void save(String dir) throws IOException {
		
	}
}
