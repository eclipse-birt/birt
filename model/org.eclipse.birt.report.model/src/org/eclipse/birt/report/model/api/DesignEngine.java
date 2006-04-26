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

package org.eclipse.birt.report.model.api;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.core.framework.PlatformConfig;
import org.eclipse.birt.report.model.api.metadata.IMetaDataDictionary;
import org.eclipse.birt.report.model.api.metadata.IMetaLogger;
import org.eclipse.birt.report.model.api.metadata.MetaDataReaderException;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.metadata.ExtensionManager;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.MetaDataParserException;
import org.eclipse.birt.report.model.metadata.MetaDataReader;
import org.eclipse.birt.report.model.metadata.MetaLogManager;

import com.ibm.icu.util.ULocale;

/**
 * Represents the BIRT design engine as a whole. Used to create new designs or
 * open existing designs.
 * <p>
 * The design engine uses <em>meta-data</em> defined in an external file. This
 * file is defined by BIRT and should both be available and valid. However, if
 * an application wants to catch and handle errors associated with this file, it
 * can create and register an instance of <code>IMetaLogger</code> before
 * creating or opening the first report design. The logger is most useful for
 * test suites.
 * 
 * @see IMetaLogger
 * @see MetaLogManager
 */

public final class DesignEngine implements IDesignEngine
{

	/**
	 * The file name of ROM.DEF
	 */

	private static final String ROM_DEF_FILE_NAME = "rom.def"; //$NON-NLS-1$

	/**
	 * The logger for errors.
	 */

	protected static Logger errorLogger = Logger.getLogger( DesignEngine.class
			.getName( ) );

	/**
	 * Initializes the meta-data system and loads all extensions which
	 * implements the extension pointers the model defines. The application must
	 * call this method once (and only once) before opening or creating a
	 * design. It is the application's responsibility because the application
	 * will choose the location to store the definition file, and that location
	 * may differ for different applications.
	 * 
	 * @param is
	 *            stream for reading the "rom.def" file that provides the
	 *            meta-data for the system
	 * @throws MetaDataReaderException
	 *             if error occurs during read the meta-data file.
	 */

	private static void initialize( InputStream is )
			throws MetaDataReaderException
	{
		try
		{
			MetaDataReader.read( is );

			ExtensionManager.initialize( );

			MetaLogManager.shutDown( );
		}
		catch ( MetaDataParserException e )
		{
			throw new MetaDataReaderException(
					MetaDataReaderException.DESIGN_EXCEPTION_META_DATA_ERROR, e );
		}
	}

	/**
	 * Creates a new design session handle. The application uses the handle to
	 * open, create and manage designs. The session also represents the user and
	 * maintains the user's locale information.
	 * 
	 * @param locale
	 *            the user's locale. If <code>null</code>, uses the system
	 *            locale.
	 * @return the design session handle
	 * @see SessionHandle
	 */

	public SessionHandle newSession( ULocale locale, PlatformConfig config )
	{
		// meta-data ready.

		if ( !MetaDataDictionary.getInstance( ).isEmpty( ) )
			return new SessionHandle( locale );

		// Initialize the meta-data if this is the first request to get
		// a new handle.

		synchronized ( DesignEngine.class )
		{
			if ( !MetaDataDictionary.getInstance( ).isEmpty( ) )
				return new SessionHandle( locale );

			MetaDataDictionary.reset( );

			try
			{
				Platform.startup( config );
				initialize( ReportDesign.class
						.getResourceAsStream( ROM_DEF_FILE_NAME ) );
			}
			catch ( MetaDataReaderException e )
			{
				// we provide logger, so do not assert.
			}
			catch ( BirtException e )
			{
				errorLogger.log( Level.INFO,
						"Error occurs while start the platform", e ); //$NON-NLS-1$
			}
			finally
			{
				MetaLogManager.shutDown( );
			}

		}

		return new SessionHandle( locale );
	}

	/**
	 * Creates a new design session handle. The application uses the handle to
	 * open, create and manage designs. The session also represents the user and
	 * maintains the user's locale information.
	 * 
	 * @param locale
	 *            the user's locale. If <code>null</code>, uses the system
	 *            locale.
	 * @return the design session handle
	 * @see SessionHandle
	 * 
	 * @deprecated by {@link #newSession(ULocale, PlatformConfig)}
	 */

	public static SessionHandle newSession( ULocale locale )
	{
		PlatformConfig config = new PlatformConfig( );
		return new DesignEngine( ).newSession( locale, config );
	}

	/**
	 * Gets the meta-data of the design engine.
	 * 
	 * @return the meta-data of the design engine.
	 */

	public IMetaDataDictionary getMetaData( )
	{
		// meta-data ready.

		if ( !MetaDataDictionary.getInstance( ).isEmpty( ) )
			return MetaDataDictionary.getInstance( );

		// Initialize the meta-data if this is the first request to get
		// a new handle.

		synchronized ( DesignEngine.class )
		{
			if ( !MetaDataDictionary.getInstance( ).isEmpty( ) )
				return MetaDataDictionary.getInstance( );

			MetaDataDictionary.reset( );

			try
			{
				initialize( ReportDesign.class
						.getResourceAsStream( ROM_DEF_FILE_NAME ) );
			}
			catch ( MetaDataReaderException e )
			{
				// we provide logger, so do not assert.
			}
			finally
			{
				MetaLogManager.shutDown( );
			}

		}

		return MetaDataDictionary.getInstance( );
	}

	/**
	 * Gets the meta-data dictionary of the design engine.
	 * 
	 * @return the meta-data dictionary of the design engine
	 * 
	 * @deprecated by {@link #getMetaData()}
	 */

	public static IMetaDataDictionary getMetaDataDictionary( )
	{
		return new DesignEngine().getMetaData( );
	}

	/**
	 * Registers a <code>IMetaLogger</code> to record initialization errors.
	 * The logger will be notified of the errors during meta-data
	 * initialization. The meta-data system will be initialized once (and only
	 * once). Loggers should be registered before the first time a session is
	 * created so that it can be notified of the logging actions.
	 * 
	 * @param newLogger
	 *            the <code>MetaLogger</code> to be registered.
	 * 
	 * @see #removeMetaLogger(IMetaLogger)
	 */

	public void registerMetaLogger( IMetaLogger newLogger )
	{
		MetaLogManager.registerLogger( newLogger );
	}

	/**
	 * Removes a <code>IMetaLogger</code>. This method will remove the logger
	 * from the list and close the logger if it has already been registered. The
	 * logger will no longer be notified of the errors during metadata
	 * initialization. Returns <code>true</code> if this logger manager
	 * contained the specified logger.
	 * 
	 * @param logger
	 *            the <code>MetaLogger</code> to be removed.
	 * @return <code>true</code> if this logger manager contained the
	 *         specified logger.
	 * 
	 * @see #registerMetaLogger(IMetaLogger)
	 */

	public boolean removeMetaLogger( IMetaLogger logger )
	{
		return MetaLogManager.removeLogger( logger );
	}

}