package item;

import java.awt.*;
import javax.swing.*;

import gaming.Display;
/**
 * @author lowenicte
 *
 */

public class Item implements Display {
	int id;
	private int status;
	//String imgDir;
	int belongs;
	int value;
	
	public void disappear(Graphics gImage) {
		
	}
	public void display(Graphics gImage) {
		
	}
	
	public Item(int ids) {
		id = ids;
	}
	public Item() {
		super();
	}
	
	public void delItem() {
		
	}
	public int sale() {
		delItem();
		return value;
	}
	public static void load() {
		
	}
	public void use() {
		
	}
}
