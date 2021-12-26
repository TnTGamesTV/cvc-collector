package de.throwstnt.developing.cvc_collector.manager.data;

public interface IAmIdentifiable<IdentificationType> {

	IdentificationType getIdentification();
	
	boolean identify(IdentificationType identification);
}
