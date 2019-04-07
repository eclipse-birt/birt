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

import java.util.HashMap;
import java.util.ResourceBundle;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.i18n.EngineResourceHandle;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;

/**
 * Define an engine exception that clients of the engine need to handle. EngineException
 * builds on top of BireException and provides resource bundle support
 */
public class EngineException extends BirtException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3137320793453389473L;

	static protected UResourceBundle dftRb = new EngineResourceHandle( ULocale
			.getDefault( ) ).getUResourceBundle( );

	static protected ThreadLocal threadLocal = new ThreadLocal( );
	
	protected static final String pluginId = "org.eclipse.birt.report.engine"; //$NON-NLS-1$
	
	// report element id
	protected long elementId = -1;
	
	protected BirtException birtException;

	public EngineException( BirtException ex)
	{
		super( pluginId, ex.getErrorCode( ), (ResourceBundle) null, ex );
		birtException = ex;
	}
	
	/**
	 * @param errorCode erroe code for the exception
	 * @param arg0 message argument
	 */
	public EngineException( String errorCode, Object arg0 )
	{
		super(pluginId,  errorCode, arg0, getResourceBundle());
	}
	
	/**
	 * @param errorCode
	 * @param arg0 message argument
	 * @param cause the cause of the exception
	 */
	public EngineException( String errorCode, Object arg0,
			Throwable cause )
	{
		super( pluginId, errorCode, arg0, getResourceBundle(), cause );
	}
	
	/**
	 * @param errorCode error code
	 * @param args message argument
	 */
	public EngineException( String errorCode, Object[] args)
	{
		super( pluginId, errorCode, args, getResourceBundle() );
	}
	
	/**
	 * @param errorCode error code
	 * @param args message arguments
	 * @param cause the cause of the exception
	 */
	public EngineException( String errorCode, Object[] args, Throwable cause )
	{
		super( pluginId, errorCode, args, getResourceBundle( ), cause );
	}
	
	/**
	 * @param errorCode the error code
	 */
	public EngineException( String errorCode)
	{
		super( pluginId, errorCode, getResourceBundle() );
	}
	
	/**
	 * @param errorCode the error code for the exception
	 * @param cause the cause of the exception
	 */
	public EngineException( String errorCode,Throwable cause )
	{
		super( pluginId, errorCode, getResourceBundle(), cause );
	}
	
	/**
	 * Set locale.
	 * 
	 * @param locale
	 */
	static public void setULocale( ULocale locale )
	{
		if ( locale == null )
		{
			return;
		}
		UResourceBundle rb = (UResourceBundle) threadLocal.get( );
		if ( rb != null )
		{
			ULocale rbLocale = rb.getULocale( );
			if ( locale.equals( rbLocale ) )
			{
				return;
			}
		}
		rb = getResourceBundle(locale);
		threadLocal.set( rb );
	}
	
	/**
	 * Get resource bundle.
	 * 
	 * @return resource bundle
	 */
	static UResourceBundle getResourceBundle( )
	{
		UResourceBundle rb = (UResourceBundle) threadLocal.get( );
		if ( rb == null )
		{
			return dftRb;
		}
		return rb;
	}
	
	protected static HashMap resourceBundles = new HashMap( );

	/**
	 * Get resource bundle.
	 * 
	 * @param locale
	 * @return resource bundle
	 */
	protected synchronized static UResourceBundle getResourceBundle(
			ULocale locale )
	{
		/* ulocale has overides the hashcode */
		UResourceBundle rb = (UResourceBundle) resourceBundles.get( locale );
		if ( rb == null )
		{
			rb = new EngineResourceHandle( locale ).getUResourceBundle( );
			if ( rb != null )
			{
				resourceBundles.put( locale, rb );
			}
		}
		return rb;
	}
	
	/**
	 * Set element id.
	 * 
	 * @param id
	 */
	public void setElementID( long id )
	{
		this.elementId = id;
	}
	
	/**
	 * Get element id.
	 * 
	 * @return element id
	 */
	public long getElementID( )
	{
		return elementId;
	}

	@Override
	public String getErrorCode( )
	{
		if ( birtException != null )
		{
			return birtException.getErrorCode( );
		}
		return super.getErrorCode( );
	}

	//suppose that this message is for end user
	public String getLocalizedMessage( )
	{
		StringBuffer message = new StringBuffer( );
		if ( birtException != null )
		{
			message.append( birtException.getLocalizedMessage( ) );
		}
		else
		{
			message.append( super.getLocalizedMessage( ) );
		}

		return message.toString( );
	}

	//suppose that this message is used for log
	public String getMessage( )
	{
		StringBuffer message = new StringBuffer( );
		if ( birtException != null )
		{
			message.append( birtException.getLocalizedMessage( ) );
		}
		else
		{
			message.append( super.getLocalizedMessage( ) );
		}
       // This "comment out" intend to show friendly message to
	   // user client ,other than the details.
	/*	if ( message.length( ) > 0 && elementId >= 0 )
		{
			message.append( " (" );
			message.append( EngineResourceHandle.getInstance( )
					.getMessage( MessageConstants.ELEMENT_ID ) );
			message.append( ":" );
			message.append( elementId );
			message.append( ")" );
		}*/

		return message.toString( );

	}

	@Override
	public String getPluginId( )
	{
		if ( birtException != null )
		{
			return birtException.getPluginId( );
		}
		return super.getPluginId( );
	}

	@Override
	public int getSeverity( )
	{
		if ( birtException != null )
		{
			return birtException.getSeverity( );
		}
		return super.getSeverity( );
	}

	@Override
	public void setSeverity( int severity )
	{
		if ( birtException != null )
		{
			birtException.setSeverity( severity );
		}
		super.setSeverity( severity );
	}

	
}
