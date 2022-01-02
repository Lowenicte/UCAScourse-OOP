/**
 * 
 */
package tools;

/**
 * @author 11914
 *
 */
public class Counter {
	public int index;
	
	public static int getFirstNumber(byte[] buf, Counter offset) {
		int ptr = offset.index;
		int length = buf.length;
		while (ptr < length && (buf[ptr] < '0' || buf[ptr] > '9') && buf[ptr] != '-')
			ptr++;
		if (ptr == length)
			return 0;
		int caltmp = 0;
		boolean menus = false;
		if (buf[ptr] == '-') {
			menus = true;
			ptr++;
		}
		while (ptr < length && buf[ptr] >= '0' && buf[ptr] <= '9') {
			caltmp = caltmp * 10 + buf[ptr] - '0';
			ptr++;
		}
		offset.index = ptr;
		return menus ? -caltmp : caltmp;
	}
	public static String getFirstString(byte[] buf, Counter offset) {
		int ptr = offset.index;
		int length = buf.length;
		while (ptr < length && (buf[ptr] == ' ' || buf[ptr] == '\n' || buf[ptr] == '\r'))
			ptr++;
		if (ptr == length)
			return new String("");
		
		offset.index = ptr;
		while (offset.index < length && (buf[offset.index] != '\n' || buf[ptr] == '\r')) {
			offset.index++;
		}
		return (new String(buf, ptr, offset.index - ptr - 1));
	}
	
	
	
	public void set(int idx) {
		index = idx;
	}
}
