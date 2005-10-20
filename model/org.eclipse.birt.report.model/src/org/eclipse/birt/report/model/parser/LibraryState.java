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

package org.eclipse.birt.report.model.parser;

import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.util.AbstractParseState;

/**
 * This class provides parser state for the top-level Library element.
 */

public class LibraryState extends ModuleState
{

	/**
	 * Constructs the library state with the library file parser handler.
	 * 
	 * @param theHandler
	 *            The library parser handler.
	 */

	public LibraryState( ModuleParserHandler theHandler )
	{
		super( theHandler );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
	 */

	public AbstractParseState startElement( String tagName )
	{
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.TRANSLATIONS_TAG ) )
			return new TranslationsState( );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.PARAMETERS_TAG ) )
			return new ParametersState( handler );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.DATA_SOURCES_TAG ) )
			return new DataSourcesState( );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.DATA_SETS_TAG ) )
			return new DataSetsState( );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.THEMES_TAG ) )
			return new ThemesState( );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.STYLES_TAG ) )
			return new CompatibleLibraryStylesState( handler, getElement( ),
					Library.THEMES_SLOT );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.PAGE_SETUP_TAG ) )
			return new PageSetupState( );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.COMPONENTS_TAG ) )
			return new SlotState( ReportDesign.COMPONENT_SLOT );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.PROPERTY_TAG ) )
			return new CompatibleReportPropertyState( handler, getElement( ) );
		return super.startElement( tagName );
	}

	/**
	 * Parses the contents of the body tag that contains the list of top-level
	 * sections.
	 */

	class ThemesState extends InnerParseState
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
		 */

		public AbstractParseState startElement( String tagName )
		{
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.THEME_TAG ) )
				return new ThemeState( handler, module, Library.THEMES_SLOT );
			return super.startElement( tagName );
		}
	}

}
