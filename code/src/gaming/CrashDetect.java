package gaming;

import spirites.Spirite;
import spirites.Entity.ActionMessage;

public interface CrashDetect {
	public abstract boolean crashDetect(Spirite e, ActionMessage msg);
}
