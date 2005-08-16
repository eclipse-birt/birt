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

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.activity.AbstractElementCommand;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.IncludeLibrary;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.CachedMemberRef;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.ReportDesign;
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
		assert module instanceof ReportDesign;
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
		ReportDesign design = (ReportDesign) module;
		Library library = design.loadLibrary( libraryFileName, namespace );
		assert library != null;

		getActivityStack( ).startTrans( );

		LibraryRecord record = new LibraryRecord( (ReportDesign) module,
				library, true );
		getActivityStack( ).execute( record );

		// Add includedLibraries

		IncludeLibrary includeLibrary = StructureFactory.createIncludeLibrary( );
		includeLibrary.setFileName( libraryFileName );
		if ( StringUtil.isBlank( namespace ) )
		{
			includeLibrary.setNamespace( StringUtil
					.extractFileName( libraryFileName ) );
		}
		else
		{
			includeLibrary.setNamespace( namespace );
		}

		ElementPropertyDefn propDefn = design
				.getPropertyDefn( Module.INCLUDE_LIBRARIES_PROP );
		PropertyCommand propCommand = new PropertyCommand( design, design );
		propCommand.addItem( new CachedMemberRef( propDefn ), includeLibrary );

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
		ReportDesign design = (ReportDesign) module;

		getActivityStack( ).startTrans( );

		LibraryRecord record = new LibraryRecord( (ReportDesign) module,
				library, false );
		getActivityStack( ).execute( record );

		String libraryFileName = library.getFileName( );
		assert libraryFileName != null;

		List includeLibraries = design.getIncludeLibraries( );
		Iterator iter = includeLibraries.iterator( );
		while ( iter.hasNext( ) )
		{
			IncludeLibrary includeLibrary = (IncludeLibrary) iter.next( );

			if ( libraryFileName.endsWith( includeLibrary.getFileName( ) ) )
			{
				ElementPropertyDefn propDefn = design
						.getPropertyDefn( Module.INCLUDE_LIBRARIES_PROP );
				PropertyCommand propCommand = new PropertyCommand( design,
						design );
				propCommand.removeItem( new CachedMemberRef( propDefn ),
						includeLibrary );
				break;
			}
		}

		getActivityStack( ).commit( );

	}

}
