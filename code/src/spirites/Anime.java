/**
 * 
 */
package spirites;

import java.io.IOException;

/**
 * @author 11914
 *
 */
public class Anime extends Spirite {
	private int animeTimer;

	@Override
	public void load(String dir, int id) throws IOException {
		//write when used
		
	}
	
	public void setTimer(int time) {
		animeTimer = time;
	}

	@Override
	public void save(String dir) throws IOException {
		
	}
}
