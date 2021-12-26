package de.throwstnt.developing.cvc_collector.manager.data;

public interface IAmCreatable<DataType, IdentificationType> {

	DataType create(IdentificationType identification);
}
