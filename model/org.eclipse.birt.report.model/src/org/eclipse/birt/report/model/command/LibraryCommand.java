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

package org.eclipse.birt.report.model.command;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.activity.SimpleRecord;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.LibraryException;
import org.eclipse.birt.report.model.api.command.LibraryReloadedEvent;
import org.eclipse.birt.report.model.api.core.IAccessControl;
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.api.elements.structures.IncludedLibrary;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.LayoutModule;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.StructureContext;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.util.LibraryUtil;

/**
 * Represents the command for adding and dropping library from report design.
 * For each operation, should start a new command instead of using the existing
 * command.
 */

public class LibraryCommand extends AbstractModuleCommand
{

	/**
	 * Construct the command with the report design.
	 * 
	 * @param module
	 *            the report design
	 */

	public LibraryCommand( Module module )
	{
		super( module );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.command.AbstractModuleCommand#validateCommand
	 * ()
	 */
	protected void validateCommand( )
	{
		if ( !( module instanceof LayoutModule ) )
			throw new IllegalArgumentException(
					"Only report design or library can import libraries!" ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.command.AbstractModuleCommand#checkAddModule
	 * (java.lang.String, java.lang.String, java.net.URL)
	 */
	protected Module checkAddModule( String moduleFileName, String namespace,
			URL fileURL ) throws SemanticException
	{
		Module outermostModule = module.findOutermostModule( );

		Library foundLib = null;
		try
		{
			foundLib = LibraryUtil.checkIncludeLibrary( module, namespace,
					fileURL, outermostModule );
		}
		catch ( LibraryException ex )
		{
			throw ex;
		}
		return foundLib;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.command.AbstractModuleCommand#doAddModule
	 * (java.lang.String, java.lang.String, java.net.URL,
	 * org.eclipse.birt.report.model.core.Module)
	 */
	protected void doAddModule( String moduleFileName, String namespace,
			URL fileURL, Module foundModule ) throws DesignFileException,
			SemanticException
	{
		Library foundLib = (Library) foundModule;
		if ( foundLib == null )
			foundLib = module.loadLibrary( moduleFileName, namespace,
					new HashMap<String, Library>( ), fileURL );
		else
			foundLib = foundLib.contextClone( module );

		doAddLibrary( moduleFileName, foundLib );

	}

	/**
	 * Performs the action to add the library to the module.
	 * 
	 * @param libraryFileName
	 *            the library path
	 * @param namespace
	 *            the library namespace
	 * @param action
	 *            can be RELOAD or SIMPLE.
	 * @param overriddenValues
	 *            the overridden values.
	 * @throws SemanticException
	 *             if the library file is invalid.
	 */

	private void doAddLibrary( String libraryFileName, Library foundLib )
			throws SemanticException, DesignFileException
	{
		Library library = foundLib;

		library.setReadOnly( );
		ActivityStack activityStack = getActivityStack( );

		LibraryRecord record = new LibraryRecord( module, library, true );
		activityStack.startTrans( record.getLabel( ) );
		getActivityStack( ).execute( record );

		// Add includedLibraries

		String namespace = foundLib.getNamespace( );

		if ( module.findIncludedLibrary( namespace ) == null )
			addImportModuleStructure( libraryFileName, namespace, APPEND_POS );

		activityStack.commit( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.command.AbstractModuleCommand#
	 * addImportModuleStructure(java.lang.String, java.lang.String, int)
	 */
	protected void addImportModuleStructure( String moduleFileName,
			String namespace, int removePosn ) throws SemanticException
	{
		// Add includedLibraries

		IncludedLibrary includeLibrary = StructureFactory
				.createIncludeLibrary( );
		includeLibrary.setFileName( moduleFileName );
		includeLibrary.setNamespace( namespace );

		ElementPropertyDefn propDefn = module
				.getPropertyDefn( IModuleModel.LIBRARIES_PROP );
		ComplexPropertyCommand propCommand = new ComplexPropertyCommand(
				module, module );

		if ( removePosn == APPEND_POS )
			propCommand.addItem(
					new StructureContext( module, propDefn, null ),
					includeLibrary );
		else
		{
			propCommand.insertItem( new StructureContext( module, propDefn,
					null ), includeLibrary, removePosn );
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.command.AbstractModuleCommand#checkDropModule
	 * (org.eclipse.birt.report.model.core.Module)
	 */
	protected void checkDropModule( Module dropModule )
			throws SemanticException
	{
		Library library = (Library) dropModule;
		if ( !module.getLibraries( ).contains( library ) )
		{
			throw new LibraryException( library, new String[]{library
					.getNamespace( )},
					LibraryException.DESIGN_EXCEPTION_LIBRARY_NOT_FOUND );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.command.AbstractModuleCommand#createDropRecord
	 * (org.eclipse.birt.report.model.core.Module)
	 */

	protected SimpleRecord createDropRecord( Module dropModule )
	{
		Library library = (Library) dropModule;
		return new LibraryRecord( module, library, false );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.command.AbstractModuleCommand#
	 * removeImportStructure(java.lang.String, java.lang.String)
	 */

	protected void removeImportStructure( String moduleFileName,
			String namespace ) throws PropertyValueException
	{
		assert moduleFileName != null;
		assert namespace != null;

		List<IncludedLibrary> includeLibraries = module.getIncludedLibraries( );
		for ( int i = 0; i < includeLibraries.size( ); i++ )
		{
			IncludedLibrary includeLibrary = includeLibraries.get( i );

			if ( !namespace.equals( includeLibrary.getNamespace( ) ) )
				continue;

			if ( !moduleFileName
					.endsWith( StringUtil
							.extractFileNameWithSuffix( includeLibrary
									.getFileName( ) ) ) )
				continue;

			ElementPropertyDefn propDefn = module
					.getPropertyDefn( IModuleModel.LIBRARIES_PROP );
			ComplexPropertyCommand propCommand = new ComplexPropertyCommand(
					module, module );
			propCommand.removeItem( new StructureContext( module, propDefn,
					null ), includeLibrary );
			break;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.command.AbstractModuleCommand#createReloadEvent
	 * (org.eclipse.birt.report.model.core.Module)
	 */
	protected NotificationEvent createReloadEvent( Module reloadModule )
	{
		Library lib = (Library) reloadModule;
		return new LibraryReloadedEvent( module, lib );
	}

	/**
	 * Reloads the library with the given file path. After reloading, activity
	 * stack is cleared.
	 * 
	 * @param toReloadLibrary
	 *            the URL file path of the library file. The instance must be
	 *            directly/indirectly included in the module.
	 * @param includedLib
	 *            the included library structure
	 * @param reloadLibs
	 *            the map contains reload libraries, the name space is key and
	 *            the library instance is the value
	 * @throws DesignFileException
	 *             if the file does no exist.
	 * @throws SemanticException
	 *             if the library is not included in the current module.
	 */

	public final void reloadLibrary( Library toReloadLibrary,
			IncludedLibrary includedLib, Map<String, Library> reloadLibs )
			throws DesignFileException, SemanticException
	{
		String location = toReloadLibrary.getLocation( );
		if ( location == null )
			location = toReloadLibrary.getFileName( );

		Library library = null;
		List<Library> libs = module.getLibrariesByLocation( location,
				IAccessControl.ARBITARY_LEVEL );
		for ( int i = 0; i < libs.size( ); i++ )
		{
			if ( toReloadLibrary == libs.get( i ) )
			{
				library = toReloadLibrary;
				break;
			}
		}

		if ( library == null )
			library = getLibraryByStruct( includedLib );

		if ( library == null )
			throw new LibraryException( module, new String[]{toReloadLibrary
					.getNamespace( )},
					LibraryException.DESIGN_EXCEPTION_LIBRARY_NOT_FOUND );

		library = findTopLevelLibrary( library );
		assert library != null;

		Module host = library.getHost( );
		IncludedLibrary tmpIncludedLib = host.findIncludedLibrary( library
				.getNamespace( ) );
		int removePosn = host.getIncludedLibraries( ).indexOf( tmpIncludedLib );

		Map<Long, Map<Long, List<Object>>> overriddenValues = null;
		ActivityStack activityStack = getActivityStack( );
		activityStack.startSilentTrans( true );

		try
		{
			// must use content command to remove all virtual elements if
			// required. This can solve unresolving issues like DataSet, Style
			// references, as well as removing names from name space.

			overriddenValues = dealAllElementDecendents( library, RELOAD_ACTION );
			doDropModule( library );

			doReloadLibrary( library, tmpIncludedLib.getFileName( ),
					overriddenValues, reloadLibs, removePosn );
		}
		catch ( SemanticException e )
		{
			activityStack.rollback( );
			throw e;
		}
		catch ( DesignFileException e )
		{
			activityStack.rollback( );
			throw e;
		}

		// send the library reloaded event first, and then commit transaction

		library = module.getLibraryWithNamespace( library.getNamespace( ),
				IAccessControl.DIRECTLY_INCLUDED_LEVEL );

		doPostReloadAction( library );

	}

	/**
	 * Reloads the given library. During this step, the input library has been
	 * removed. It only for reloading operation.
	 * 
	 * @param toReload
	 *            the library to reload
	 * @param overriddenValues
	 *            the overridden values
	 * @param reloadLibs
	 *            the map contains reload libraries, the name space is key and
	 *            the library instance is the value
	 * @param removePosn
	 *            the position at which the library should be inserted
	 * @throws SemanticException
	 * @throws DesignFileException
	 */

	private void doReloadLibrary( Library toReload, String includedLibPath,
			Map<Long, Map<Long, List<Object>>> overriddenValues,
			Map<String, Library> reloadLibs, int removePosn )
			throws SemanticException, DesignFileException
	{
		String namespace = toReload.getNamespace( );
		URL fileURL = module.findResource( includedLibPath,
				IResourceLocator.LIBRARY );

		// if the file cannot be found,add the included library structure only.

		if ( fileURL == null )
		{
			if ( module.findIncludedLibrary( namespace ) == null )
				addImportModuleStructure( includedLibPath, namespace,
						removePosn );

			// add an invalid library instance, same to the parser

			Library invalidLib = new Library( module.getSession( ), module );
			invalidLib.setFileName( includedLibPath );
			invalidLib.setNamespace( namespace );
			invalidLib.setID( invalidLib.getNextID( ) );
			invalidLib.addElementID( invalidLib );
			invalidLib.setValid( false );

			ActivityStack activityStack = getActivityStack( );

			LibraryRecord record = new LibraryRecord( module, invalidLib,
					overriddenValues, removePosn );

			activityStack.startTrans( record.getLabel( ) );
			activityStack.execute( record );
			activityStack.commit( );

			return;
		}

		Library library = null;

		if ( reloadLibs.get( toReload.getNamespace( ) ) == null )
		{
			library = module.loadLibrary( includedLibPath, namespace,
					reloadLibs, fileURL );
			LibraryUtil.insertReloadLibs( reloadLibs, library );
		}
		else
		{
			library = reloadLibs.get( toReload.getNamespace( ) ).contextClone(
					module );
		}

		library.setReadOnly( );

		ActivityStack activityStack = getActivityStack( );

		LibraryRecord record = new LibraryRecord( module, library,
				overriddenValues, removePosn );

		assert record != null;
		activityStack.startTrans( record.getLabel( ) );
		getActivityStack( ).execute( record );

		// Add includedLibraries

		if ( module.findIncludedLibrary( namespace ) == null )
			addImportModuleStructure( includedLibPath, namespace, removePosn );

		activityStack.commit( );
	}

	/**
	 * Reloads libraries according to the given location.
	 * 
	 * @param location
	 *            the library location
	 * @param reloadLibs
	 *            the map contains reload libraries, the name space is key and
	 *            the library instance is the value
	 * @throws DesignFileException
	 * @throws SemanticException
	 */

	public void reloadLibrary( String location, Map<String, Library> reloadLibs )
			throws DesignFileException, SemanticException
	{
		List<Library> libs = module.getLibrariesByLocation( location,
				IAccessControl.ARBITARY_LEVEL );

		for ( int i = 0; i < libs.size( ); i++ )
			reloadLibrary( libs.get( i ), null, reloadLibs );
	}

	/**
	 * Returns the library that is directly included the outermost module.
	 * 
	 * @param lib
	 *            the library file
	 * @return the library that is directly included the outermost module
	 */

	private Library findTopLevelLibrary( Library lib )
	{
		Library tmpLib = lib;

		// find the right library to reload.

		while ( tmpLib != null )
		{
			if ( tmpLib.getHost( ) == module )
				break;

			tmpLib = (Library) tmpLib.getHost( );
		}

		return tmpLib;
	}

	/**
	 * Finds the library that matches the given the included library structure.
	 * 
	 * @param includedLib
	 *            the included library structure
	 * @return the matched library instance
	 */

	private Library getLibraryByStruct( IncludedLibrary includedLib )
	{
		List<Object> includedLibs = module.getListProperty( module,
				IModuleModel.LIBRARIES_PROP );
		if ( includedLibs == null )
			return null;

		int index = includedLibs.indexOf( includedLib );
		if ( index == -1 )
			return null;

		Library retLib = module.getLibraries( ).get( index );
		if ( retLib.getNamespace( ).equalsIgnoreCase(
				includedLib.getNamespace( ) ) )
			return retLib;

		return null;
	}
}
