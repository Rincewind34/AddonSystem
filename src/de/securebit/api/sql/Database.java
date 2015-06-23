package de.securebit.api.sql;

import java.util.List;

public interface Database {

	public abstract void openConnection();
	
	public abstract void closeConnection();
	
	public abstract void getTables(Consumer<List<Table>> action);
	
	public abstract void getTable(String tableID, Consumer<Table> action);
	
	public abstract void addTable(Table table);
	
	public abstract void removeTable(String tableID);
	
	public abstract void insert(String tableID, Object... args);
	
	public abstract void update(String tableID, String[] cols, Object[] values, WhereClausel clausel);
	
	public abstract void update(String tableID, List<String> cols, List<Object> values, WhereClausel clausel);
	
	public abstract void delete(String tableID, WhereClausel clausel);
	
	public abstract void select(String tableID, Consumer<TableRow> action);
	
	public abstract void select(String tableID, WhereClausel clausel, Consumer<TableRow> action);
	
	public abstract void select(String tableID, WhereClausel clausel, int limit, Consumer<TableRow> action);
		
	public abstract boolean isConnected();
	
}
