package de.securebit.api.sql;

public interface Consumer<T> {

	void accept(T result);
}
