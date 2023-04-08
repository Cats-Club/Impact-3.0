package me.zero.clarinet.alt;

public class AccountManagementException extends Exception {
	
	public AccountManagementException() {
		super("AccountManagementException");
	}
	
	public AccountManagementException(String message) {
		super(message);
	}
	
	public AccountManagementException(String message, Throwable tObj) {
		super(message, tObj);
	}
}
