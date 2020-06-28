package edu.unf.cnt3404.sicxe.syntax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Contains Program State, like location counter, references, definitions,
//Symtab, etc.
public class Program {
	private String name;
	//The address of first instruction, or an integer less than 0 if
	//there is no first instruction (not a runnable program)
	private int first;
	//The contents of the base register, or, an integer less than 0 if
	//base is disabled
	private int base;
	
	private int start;
	private int size;
	private int locctr;
	
	private List<String> definitions = new ArrayList<>();
	private List<String> references = new ArrayList<>();
	
	private Map<String, Symbol> symtab = new HashMap<>();
	private Littab littab = new Littab();
	
	//Start directive methods
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	
	public boolean isRelocatable() {
		return start == 0;
	}
	//End directive methods
	public void setFirst(int first) {
		this.first = first;
	}
	public void disableFirst() {
		first = -1;
	}
	public int getFirst() {
		return first;
	}
	
	public int getStart() {
		return start;
	}
	
	public void setStart(int start) {
		this.start = start;
	}
	
	//Whether there's a first...
	public boolean isRunnable() {
		return first >= 0;
	}
	//Locctr methods
	public void setLocationCounter(int locctr) {
		this.locctr = locctr;
		//When the locctr is rewound to something small, then
		//size should not change
		if (locctr - start > size) {
			size = locctr - start;
		}
	}
	
	public int getLocationCounter() {
		return locctr;
	}
	public void incrementLocationCounter(int step) {
		setLocationCounter(locctr + step);
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
	//External definition methods
	public void addExternalDefintion(String def) {
		definitions.add(def);
	}
	public List<Symbol> getExternalDefinitions() {
		List<Symbol> result = new ArrayList<>();
		for (String def : definitions) {
			Symbol symbol = getSymbol(def);
			if (symbol == null) {
				throw new RuntimeException("External Definition symbol " + def
						+ " was never defined");
			}
			result.add(symbol);
		}
		return result;
	}
	//External reference methods
	public void addExternalReference(String ref) {
		symtab.put(ref, new Symbol(ref));
		references.add(ref);
	}
	public List<String> getExternalReferences() {
		return references;
	}
	//Symtab methods
	public void put(String symbol, int value, boolean absolute) {
		symtab.put(symbol, new Symbol(symbol, value, absolute));
	}
	public Symbol getSymbol(String symbol) {
		return symtab.get(symbol);
	}
	//Literals methods
	public void addLiteral(Data literal) {
		littab.addLiteral(literal);
	}
	public void allocateLiterals(List<Command> commands) {
		littab.allocate(this, commands);
	}
	public int getLiteral(Data literal) {
		return littab.getLiteral(literal);
	}
	public int getSize() {
		return size;
	}
}
