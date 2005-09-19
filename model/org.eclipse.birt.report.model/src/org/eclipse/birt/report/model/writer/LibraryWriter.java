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

package org.eclipse.birt.report.model.writer;

import org.eclipse.birt.report.model.api.elements.structures.IncludeScript;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.parser.DesignSchemaConstants;

/**
 * Represents the writer for writing library file.
 */

public class LibraryWriter extends ModuleWriter
{

	private Library library = null;

	/**
	 * Contructs one library writer with the library instance.
	 * 
	 * @param library
	 *            the library to write
	 */

	public LibraryWriter( Library library )
	{
		this.library = library;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.writer.ModuleWriter#getModule()
	 */
	protected Module getModule( )
	{
		return library;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitLibrary(org.eclipse.birt.report.model.elements.Library)
	 */
	public void visitLibrary( Library obj )
	{
		writer.startElement( DesignSchemaConstants.LIBRARY_TAG );
		super.visitLibrary( obj );
		property( obj, Module.INITIALIZE_METHOD );
		
		// include libraries and scripts

		// Library including library is not supported.
		//
		// writeStructureList( obj, Library.INCLUDE_LIBRARIES_PROP );
		writeSimpleStructureList( obj, Library.INCLUDE_SCRIPTS_PROP,
				IncludeScript.FILE_NAME_MEMBER );

		// config variables

		writeStructureList( obj, Library.CONFIG_VARS_PROP );

		writeArrangedContents( obj, Library.PARAMETER_SLOT,
				DesignSchemaConstants.PARAMETERS_TAG );
		writeArrangedContents( obj, Library.DATA_SOURCE_SLOT,
				DesignSchemaConstants.DATA_SOURCES_TAG );
		writeArrangedContents( obj, Library.DATA_SET_SLOT,
				DesignSchemaConstants.DATA_SETS_TAG );

		// ColorPalette tag

		writeCustomColors( obj );

		// Translations. ( Custom-defined messages )

		writeTranslations( obj );

		writeContents( obj, Library.STYLE_SLOT,
				DesignSchemaConstants.STYLES_TAG );
		writeArrangedContents( obj, Library.COMPONENT_SLOT,
				DesignSchemaConstants.COMPONENTS_TAG );
		writeArrangedContents( obj, Library.PAGE_SLOT,
				DesignSchemaConstants.PAGE_SETUP_TAG );

		// Embedded images

		writeEmbeddedImages( obj );
		
		writer.endElement( );
	}
}
