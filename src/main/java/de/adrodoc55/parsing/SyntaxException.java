package de.adrodoc55.parsing;

public class SyntaxException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final int line;

	public int getLine() {
		return line;
	}

	private final int column;

	public int getColumn() {
		return column;
	}

	private final String rawMessage;

	public String getRawMessage() {
		return rawMessage;
	}

	private static String constructMessage(int column, String message) {
		return String.format("In column %d: %s", column, message);
	}

	public SyntaxException(int column, String message) {
		super(constructMessage(column, message));
		this.line = 1;
		this.column = column;
		rawMessage = message;
	}

	public SyntaxException(int column, String message, Throwable cause) {
		super(constructMessage(column, message), cause);
		this.line = 1;
		this.column = column;
		rawMessage = message;
	}

	private static String constructMessage(int line, int column, String message) {
		return String.format("In line %d, in column %d: %s", line, column,
				message);
	}

	public SyntaxException(int line, int column, String message) {
		super(constructMessage(line, column, message));
		this.line = line;
		this.column = column;
		rawMessage = message;
	}

	public SyntaxException(int line, int column, String message, Throwable cause) {
		super(constructMessage(line, column, message), cause);
		this.line = line;
		this.column = column;
		rawMessage = message;
	}

}
