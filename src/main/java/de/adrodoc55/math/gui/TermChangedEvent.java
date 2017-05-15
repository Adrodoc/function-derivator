package de.adrodoc55.math.gui;

import de.adrodoc55.math.term.Term;

public class TermChangedEvent {
//	private Term oldTerm;
	private Term newTerm;
//	private Variable ableitungsVariable;

//	public Term getOldTerm() {
//		return (oldTerm == null) ? oldTerm : oldTerm.clone();
//	}
//
//	public void setOldTerm(Term oldTerm) {
//		this.oldTerm = oldTerm;
//	}

	public Term getNewTerm() {
		return (newTerm == null) ? newTerm : newTerm.clone();
	}

	public void setNewTerm(Term newTerm) {
		this.newTerm = newTerm;
	}

	// public Variable getAbleitungsVariable() {
	// return ableitungsVariable;
	// }
	//
	// public void setAbleitungsVariable(Variable ableitungsVariable) {
	// this.ableitungsVariable = ableitungsVariable;
	// }
}