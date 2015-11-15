package com.iutbmteprow.shootingarchery.dbman;

public class UserAlreadyRegisteredException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7264910451911870967L;

	@Override
	public String getMessage() {
		return "L'utilisateur existe déja.";
	}
	
}
