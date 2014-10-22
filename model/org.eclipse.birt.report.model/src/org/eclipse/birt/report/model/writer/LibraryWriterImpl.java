/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.writer;

import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.Theme;
import org.eclipse.birt.report.model.elements.interfaces.IAbstractThemeModel;
import org.eclipse.birt.report.model.elements.interfaces.ILibraryModel;
import org.eclipse.birt.report.model.parser.DesignSchemaConstants;

/**
 * 
 */

public abstract class LibraryWriterImpl extends ModuleWriter
{

	protected Library library = null;

	public LibraryWriterImpl( )
	{
		super( );
	}

	protected Module getModule( )
	{
		return library;
	}

	public void visitLibrary( Library obj )
	{
		writer.startElement( DesignSchemaConstants.LIBRARY_TAG );
		super.visitLibrary( obj );
		property( obj, IModuleModel.INITIALIZE_METHOD );
	
		if ( markLineNumber )
		{
			getModule( ).addLineNo(
					obj.getPropertyDefn( IModuleModel.THEME_PROP ),
					Integer.valueOf( writer.getLineCounter( ) ) );
		}
		property( obj, IModuleModel.THEME_PROP );
	
		// include libraries and scripts
	
		// Library including library is not supported.
		//
		writeStructureList( obj, IModuleModel.LIBRARIES_PROP );
	
		// config variables
	
		writeStructureList( obj, IModuleModel.CONFIG_VARS_PROP );
	
		writeContents( obj, IModuleModel.PARAMETER_SLOT,
				DesignSchemaConstants.PARAMETERS_TAG );
		writeContents( obj, IModuleModel.DATA_SOURCE_SLOT,
				DesignSchemaConstants.DATA_SOURCES_TAG );
		writeContents( obj, IModuleModel.DATA_SET_SLOT,
				DesignSchemaConstants.DATA_SETS_TAG );
		writeContents( obj, ILibraryModel.CUBE_SLOT,
				DesignSchemaConstants.CUBES_TAG );
	
		// ColorPalette tag
	
		writeCustomColors( obj );
	
		// Translations. ( Custom-defined messages )
	
		writeTranslations( obj );
	
		writeContents( obj, ILibraryModel.THEMES_SLOT,
				DesignSchemaConstants.THEMES_TAG );
		writeArrangedContents( obj, IModuleModel.COMPONENT_SLOT,
				DesignSchemaConstants.COMPONENTS_TAG );
		writeContents( obj, IModuleModel.PAGE_SLOT,
				DesignSchemaConstants.PAGE_SETUP_TAG );
		
		writeContentProperties( obj );
		// Embedded images
	
		writeEmbeddedImages( obj );
	
		writer.endElement( );
	}

	protected void writeContentProperties( Library obj )
	{
		
	}
}