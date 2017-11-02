package edu.unf.cnt3404.sicxe.syntax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Contains Program State, like location counter, references, definitions,
//Symtab, etc.
public class Program {
	private String name;
	private int start;
	private int first;
	//The contents of the base register, or, an integer less than 0 if
	//base is disabled
	private int base;
	
	private int locctr;
	private List<String> definitions = new ArrayList<>();
	private List<String> references = new ArrayList<>();
	
	private Map<String, Symbol> symtab = new HashMap<>();
	//Start directive methods
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getStart() {
		return start;
	}
	public void setFirst(int first) {
		this.first = first;
	}
	public int getFirst() {
		return first;
	}
	//Location counter methods
	public void setLocationCounter(int locctr) {
		this.locctr = locctr;
	}
	public int getLocationCounter() {
		return locctr;
	}
	public void incrementLocationCounter(int step) {
		locctr += step;
	}
	//Base related methods
	public void setBase(int base) {
		this.base = base;
	}
	public void disableBase() {
		base = -1;
	}
	public int getBase() {
		return base;
	}
	public boolean isBaseEnabled() {
		return base >= 0;
	}
	//External related methods
	public void addExternalDefintion(String def) {
		definitions.add(def);
	}
	public void addExternalReference(String ref) {
		references.add(ref);
	}
	//Symtab methods
	public void putLocal(String symbol, int value, boolean absolute) {
		symtab.put(symbol, new Symbol(symbol, value, absolute));
	}
	public void putExternal(String symbol) {
		symtab.put(symbol, new Symbol(symbol));
	}
	public Symbol getSymbol(String symbol) {
		return symtab.get(symbol);
	}
}
