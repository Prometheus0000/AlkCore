package net.alkalus.api.objects.misc;

import net.alkalus.core.lib.CORE;

public class GenericException extends Throwable {

	private static final long serialVersionUID = 3601884582161841486L;

	public GenericException(String aError) {
		this(aError, true);
	}
	
	public GenericException(String aError, boolean aIsVerbose) {
		AcLog.ERROR("Throwing "+CORE.NAME+" Exception!");
		AcLog.ERROR("[EXCEPTION] "+aError);
		if (aIsVerbose) {
			printStackTrace();
		}
	}
	
	@Override
	public void printStackTrace() {
		super.printStackTrace();
	}
	
	

}
