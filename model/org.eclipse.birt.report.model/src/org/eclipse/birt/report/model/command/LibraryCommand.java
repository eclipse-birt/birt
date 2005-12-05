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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.activity.AbstractElementCommand;
import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.IncludedLibrary;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.CachedMemberRef;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;

/**
 * Represents the command for adding and dropping library from report design.
 */

public class LibraryCommand extends AbstractElementCommand
{

	/**
	 * Construct the command with the report design.
	 * 
	 * @param module
	 *            the report design
	 */

	public LibraryCommand( Module module )
	{
		super( module, module );
	}

	/**
	 * Adds new library file to report design.
	 * 
	 * @param libraryFileName
	 *            library file name
	 * @param namespace
	 *            library namespace
	 * @throws DesignFileException
	 *             if the library file is not found or has fatal errors.
	 * @throws SemanticException
	 *             if failed to add <code>IncludeLibrary</code> strcutre
	 */

	public void addLibrary( String libraryFileName, String namespace )
			throws DesignFileException, SemanticException
	{
		if ( StringUtil.isBlank( namespace ) )
			namespace = StringUtil.extractFileName( libraryFileName );

		if ( module.isDuplicateNamespace( namespace ) )
		{
			throw new LibraryException(
					module,
					new String[]{namespace},
					LibraryException.DESIGN_EXCEPTION_DUPLICATE_LIBRARY_NAMESPACE );
		}

		//	the library has already been included.
		
		URL url = module.findResource( libraryFileName, IResourceLocator.LIBRARY );
		if ( url != null && module.getLibraryByLocation( url.toString( ) ) != null )
		{
			throw new LibraryException( module, new String[]{url.toString( )},
					LibraryException.DESIGN_EXCEPTION_LIBRARY_ALREADY_INCLUDED );
		}
		
		
		Library library = module.loadLibrary( libraryFileName, namespace );
		assert library != null;
		library.setReadOnly( );
	
		getActivityStack( ).startTrans( );

		LibraryRecord record = new LibraryRecord( module, library, true );
		getActivityStack( ).execute( record );

		// Add includedLibraries

		IncludedLibrary includeLibrary = StructureFactory.createIncludeLibrary( );
		includeLibrary.setFileName( libraryFileName );
		includeLibrary.setNamespace( namespace );

		ElementPropertyDefn propDefn = module
				.getPropertyDefn( Module.LIBRARIES_PROP );
		PropertyCommand propCommand = new PropertyCommand( module, module );
		propCommand.addItem( new CachedMemberRef( propDefn ), includeLibrary );

		getActivityStack( ).commit( );
	}

	/**
	 * Drop the given libary from the design. And break all the parent/child
	 * relationships. All child element will be localized in the module.
	 * 
	 * @param library
	 *            a given library to be dropped.
	 * @throws SemanticException
	 * 
	 */

	public void dropLibraryAndBreakExtends( Library library )
			throws SemanticException
	{
		// library not found.

		if ( !module.getLibraries( ).contains( library ) )
		{
			throw new LibraryException( library,
					LibraryException.DESIGN_EXCEPTION_LIBRARY_NOT_FOUND );
		}

		ActivityStack stack = getActivityStack( );
		stack.startTrans( );
		try
		{
			for ( Iterator iter = library.getSlot( Library.COMPONENT_SLOT )
					.iterator( ); iter.hasNext( ); )
			{
				DesignElement element = (DesignElement) iter.next( );
				List derived = element.getDerived( );
				for ( int i = 0; i < derived.size( ); i++ )
				{
					DesignElement child = (DesignElement) derived.get( i );
					if ( child.getRoot( ) == getModule( ) )
					{
						ExtendsCommand command = new ExtendsCommand(
								getModule( ), child );
						command.localizeElement( );
					}
				}
			}

			LibraryRecord record = new LibraryRecord( module, library, false );
			getActivityStack( ).execute( record );

			// Remove the include library structure.

			String libraryFileName = library.getFileName( );
			assert libraryFileName != null;
			removeIncludeLibrary( libraryFileName, library.getNamespace() );

		}
		catch ( SemanticException ex )
		{
			stack.rollback( );
			throw ex;
		}
		getActivityStack( ).commit( );
	}

	/**
	 * Drops the given library.
	 * 
	 * @param library
	 *            the library to drop
	 * @throws SemanticException
	 *             if failed to remove <code>IncludeLibrary</code> strcutre
	 */

	public void dropLibrary( Library library ) throws SemanticException
	{
		// library not found.

		if ( !module.getLibraries( ).contains( library ) )
		{
			throw new LibraryException( library,
					LibraryException.DESIGN_EXCEPTION_LIBRARY_NOT_FOUND );
		}

		// library has decendents in the current module.

		for ( Iterator iter = library.getSlot( Library.COMPONENT_SLOT )
				.iterator( ); iter.hasNext( ); )
		{
			DesignElement element = (DesignElement) iter.next( );
			List allDescendents = new ArrayList();
			getAllDescdents( element, allDescendents );
			
			for ( int i = 0; i < allDescendents.size( ); i++ )
			{
				DesignElement child = (DesignElement) allDescendents.get( i );
				do
				{
					if ( child.getRoot( ) == getModule( ) )
					{
						throw new LibraryException(
								library,
								new String[]{child.getHandle( module ).getDisplayLabel() },
								LibraryException.DESIGN_EXCEPTION_LIBRARY_HAS_DESCENDENTS );
					}

				} while ( child.hasDerived( ) );
			}
		}

		// Remove the include library structure.

		ActivityStack stack = getActivityStack( );
		stack.startTrans( );

		// Drop the library and update the client references.

		LibraryRecord record = new LibraryRecord( module, library, false );
		getActivityStack( ).execute( record );

		try
		{
			String libFileName = library.getFileName( );
			assert libFileName != null;
			removeIncludeLibrary( libFileName, library.getNamespace() );
		}
		catch ( SemanticException ex )
		{
			stack.rollback( );
			throw ex;
		}
		getActivityStack( ).commit( );
	}

	/**
	 * Recursively collect all the descendents of the given element.
	 * 
	 * @param element
	 *            a given element.
	 * @param results
	 *            the result list containing all the childs.
	 */

	private void getAllDescdents( DesignElement element, List results )
	{
		List descends = element.getDerived( );
		results.addAll( descends );

		for ( int i = 0; i < descends.size( ); i++ )
		{
			getAllDescdents( (DesignElement) descends.get( i ), results );
		}
	}

	/**
	 * drop the include library structure.
	 * 
	 * @param fileName
	 *            file name of the library.
	 *            
	 * @param namespace
	 *            namespace of the library.
	 *            
	 * @throws PropertyValueException
	 */

	private void removeIncludeLibrary( String fileName, String namespace )
			throws PropertyValueException
	{
		assert fileName != null;
		assert namespace != null;

		List includeLibraries = module.getIncludeLibraries( );
		Iterator iter = includeLibraries.iterator( );
		while ( iter.hasNext( ) )
		{
			IncludedLibrary includeLibrary = (IncludedLibrary) iter.next( );
			
			if ( !namespace.equals( includeLibrary.getNamespace() ) )
				continue;
			
			if ( !fileName.endsWith( includeLibrary.getFileName( ) ) )
				continue;

			ElementPropertyDefn propDefn = module
					.getPropertyDefn( Module.LIBRARIES_PROP );
			PropertyCommand propCommand = new PropertyCommand( module, module );
			propCommand.removeItem( new CachedMemberRef( propDefn ),
					includeLibrary );
			break;
		}
	}

}
