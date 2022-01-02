package spirites;

import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import gaming.*;
import spirites.*;
import spirites.Entity.*;
import tools.Counter;

public class Blocks extends Spirite implements CrashDetect {
	@Override
	public boolean crashDetect(Spirite e, ActionMessage msg) {
		int f_x = (msg.area & 8)*msg.scale - (msg.area & 4)*msg.scale + e.position_x;
		int f_y = (msg.area & 2)*msg.scale - (msg.area & 1)*msg.scale + e.position_y;
		if (f_x >= position_x + size_width || f_x + e.size_width <= position_x
		 || f_y >= position_y + size_height || f_y + e.size_height <= position_y)
			return false;
		return true;
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
		
		fin.close(); 
	}

	@Override
	public void save(String dir) throws IOException {
		
	}
}
