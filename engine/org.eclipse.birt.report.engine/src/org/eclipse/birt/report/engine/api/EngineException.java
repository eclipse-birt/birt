/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.engine.api;

import java.util.Locale;
import java.util.ResourceBundle;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.i18n.EngineResourceHandle;

/**
 * Define an engine exception that clients of the engine need to handle. EngineException
 * builds on top of BireException and provides resource bundle support
 */
public class EngineException extends BirtException {
	
	static protected ResourceBundle rb = new EngineResourceHandle(Locale.getDefault()).getResourceBundle();
	
	/**
	 * @param errorCode erroe code for the exception
	 * @param arg0 message argument
	 */
	public EngineException( String errorCode, Object arg0 )
	{
		super( errorCode, arg0, rb);
	}
	
	/**
	 * @param errorCode
	 * @param arg0 message argument
	 * @param cause the cause of the exception
	 */
	public EngineException( String errorCode, Object arg0,
			Throwable cause )
	{
		super( errorCode, arg0, rb, cause );
	}
	
	/**
	 * @param errorCode error code
	 * @param args message argument
	 */
	public EngineException( String errorCode, Object[] args)
	{
		super( errorCode, args, rb );
	}
	
	/**
	 * @param errorCode error code
	 * @param args message arguments
	 * @param cause the cause of the exception
	 */
	public EngineException( String errorCode, Object[] args,
			Throwable cause )
	{
		super( errorCode, args, rb, cause );
	}
	
	/**
	 * @param errorCode the error code
	 */
	public EngineException( String errorCode)
	{
		super( errorCode, rb );
	}
	
	/**
	 * @param errorCode the error code for the exception
	 * @param cause the cause of the exception
	 */
	public EngineException( String errorCode,Throwable cause )
	{
		super( errorCode, rb, cause );
	}
}
