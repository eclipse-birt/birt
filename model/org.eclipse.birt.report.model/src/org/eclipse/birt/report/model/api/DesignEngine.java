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
import java.util.Locale;

import org.eclipse.birt.report.model.api.metadata.IMetaDataDictionary;
import org.eclipse.birt.report.model.api.metadata.IMetaLogger;
import org.eclipse.birt.report.model.api.metadata.MetaDataReaderException;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.metadata.ExtensionLoader;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.MetaDataParserException;
import org.eclipse.birt.report.model.metadata.MetaDataReader;
import org.eclipse.birt.report.model.metadata.MetaLogManager;

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

public final class DesignEngine
{

	/**
	 * The file name of ROM.DEF
	 */

	private static final String ROM_DEF_FILE_NAME = "rom.def"; //$NON-NLS-1$

	/**
	 * Initializes the meta-data system. The application must call this method
	 * once (and only once) before opening or creating a design. It is the
	 * application's responsibility because the application will choose the
	 * location to store the definition file, and that location may differ for
	 * different applications.
	 * 
	 * @param defnFileName
	 *            name and location of the "rom.def" file that provides the
	 *            meta-data for the system
	 * @throws MetaDataReaderException
	 *             if error occurs during read the meta-data file.
	 */

	public static void initialize( String defnFileName )
			throws MetaDataReaderException
	{
		try
		{
			MetaDataReader.read( defnFileName );

			ExtensionLoader.init( );

			MetaLogManager.shutDown( );
		}
		catch ( MetaDataParserException e )
		{
			throw new MetaDataReaderException(
					MetaDataReaderException.DESIGN_EXCEPTION_META_DATA_ERROR, e );
		}

	}

	/**
	 * Initializes the meta-data system. The application must call this method
	 * once (and only once) before opening or creating a design. It is the
	 * application's responsibility because the application will choose the
	 * location to store the definition file, and that location may differ for
	 * different applications.
	 * 
	 * @param is
	 *            stream for reading the "rom.def" file that provides the
	 *            meta-data for the system
	 * @throws MetaDataReaderException
	 *             if error occurs during read the meta-data file.
	 */

	public static void initialize( InputStream is )
			throws MetaDataReaderException
	{
		try
		{
			MetaDataReader.read( is );

			ExtensionLoader.init( );

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

	public static SessionHandle newSession( Locale locale )
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

		return new SessionHandle( locale );
	}
	
	/**
	 * Gets the meta-data dictionary of the design engine.
	 * @return the meta-data dictionary of the design engine
	 */
	
	public static IMetaDataDictionary getMetaDataDictionary( )
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
	 * Opens a design by the given file name.
	 * 
	 * @param fileName
	 *            the name of the file to open.
	 * @return A handle to the report design.
	 * @throws DesignFileException
	 *             If the file is not found, or the file contains fatal errors.
	 * @deprecated
	 */

	public static ReportDesignHandle openDesign( String fileName )
			throws DesignFileException
	{
		SessionHandle session = newSession( null );
		return session.openDesign( fileName );
	}

	/**
	 * Opens a design by a given stream file name of the design. The file name
	 * is used for error reporting, and when saving the design.
	 * 
	 * @param fileName
	 *            the name of the file to open. If <code>null</code>, the
	 *            design will be treated as a new design, and will be saved to a
	 *            different file.
	 * @param is
	 *            the stream to read the design
	 * @return a handle to the report design.
	 * @throws DesignFileException
	 *             If the file is not found, or the file contains fatal errors.
	 * @deprecated
	 */

	public static ReportDesignHandle openDesign( String fileName, InputStream is )
			throws DesignFileException
	{
		SessionHandle session = newSession( null );
		return session.openDesign( fileName, is );
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

	public static void registerMetaLogger( IMetaLogger newLogger )
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

	public static boolean removeMetaLogger( IMetaLogger logger )
	{
		return MetaLogManager.removeLogger( logger );
	}

}