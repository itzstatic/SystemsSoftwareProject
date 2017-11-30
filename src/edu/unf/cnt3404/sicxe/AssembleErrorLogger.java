package edu.unf.cnt3404.sicxe;

import java.util.HashMap;
import java.util.Map;

import edu.unf.cnt3404.sicxe.parse.AssembleError;
import edu.unf.cnt3404.sicxe.parse.Locatable;

public class AssembleErrorLogger {
	
	private Map<Integer, AssembleError> errors = new HashMap<>();
	
	public void log(Locatable l, String message) {
		errors.put(l.getRow(), new AssembleError(l, message));
	}
	
	public void log(AssembleError e) {
		errors.put(e.getRow(), e);
	}
	
	public Map<Integer, AssembleError> toMap() {
		return errors;
	}
	
	public boolean hasErrors() {
		return !errors.isEmpty();
	}
}
