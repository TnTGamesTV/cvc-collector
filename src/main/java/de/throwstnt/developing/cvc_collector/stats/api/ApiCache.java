package de.throwstnt.developing.cvc_collector.stats.api;

public class ApiCache extends AbstractCache<String, ApiRequestAndResponse> {

	public ApiCache() {
		super(100);
	}	
}
