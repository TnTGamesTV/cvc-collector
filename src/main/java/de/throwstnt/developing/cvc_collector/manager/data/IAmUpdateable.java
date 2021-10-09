package de.throwstnt.developing.cvc_collector.manager.data;

public interface IAmUpdateable {

	public static enum UpdateResult {
		SUCCESS, FAILURE;
	}
	
	UpdateResult update();
}
