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

import java.net.URL;
import java.util.ArrayList;

import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.command.CssException;
import org.eclipse.birt.report.model.api.css.StyleSheetException;
import org.eclipse.birt.report.model.api.elements.structures.IncludedCssStyleSheet;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.css.CssStyleSheet;
import org.eclipse.birt.report.model.css.CssStyleSheetAdapter;
import org.eclipse.birt.report.model.elements.ICssStyleSheetOperation;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.xml.sax.SAXException;

/**
 * Parses the simple structure list for "includedCssStyleSheet" property.
 */

public class IncludedCssStyleSheetListState extends ListPropertyState
{

	private int lineNumber = 1;

	IncludedCssStyleSheetListState( ModuleParserHandler theHandler,
			DesignElement element )
	{
		super( theHandler, element );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
	 */
	public AbstractParseState startElement( String tagName )
	{
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.STRUCTURE_TAG ) )
			return new IncludedCssStructureState( handler, element, propDefn,
					list );

		return super.startElement( tagName );
	}

	class IncludedCssStructureState extends CompatibleStructureState
	{

		IncludedCssStructureState( ModuleParserHandler theHandler,
				DesignElement element, PropertyDefn propDefn, ArrayList theList )
		{
			super( theHandler, element, propDefn, theList );
			lineNumber = handler.getCurrentLineNo( );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
		 */

		public void end( ) throws SAXException
		{
			super.end( );

			IncludedCssStyleSheet includeCss = (IncludedCssStyleSheet) struct;

			if ( handler.markLineNumber )
				handler.module.addLineNo( struct, new Integer( lineNumber ) );

			String fileName = includeCss.getFileName( );
			if ( !( element instanceof ICssStyleSheetOperation ) )
				return;

			URL url = handler.module.findResource( fileName,
					IResourceLocator.CASCADING_STYLE_SHEET );
			if ( url == null )
			{
				CssException ex = new CssException( handler.module,
						new String[]{fileName},
						CssException.DESIGN_EXCEPTION_CSS_NOT_FOUND );
				handler.getErrorHandler( ).semanticWarning( ex );
				return;
			}
			
			ICssStyleSheetOperation sheetOperation = (ICssStyleSheetOperation) element;

			CssStyleSheet sheet = CssStyleSheetAdapter
					.getCssStyleSheetByLocation( handler.module, sheetOperation
							.getCsses( ), url.getFile( ) );

			if ( sheet != null )
			{
				CssException ex = new CssException( handler.module,
						new String[]{fileName},
						CssException.DESIGN_EXCEPTION_DUPLICATE_CSS );
				handler.getErrorHandler( ).semanticWarning( ex );
				return;
			}

			try
			{
				sheet = handler.module.loadCss( element , fileName );
				sheetOperation.addCss( sheet );
			}
			catch ( StyleSheetException e )
			{
				CssException ex = new CssException( handler.module,
						new String[]{fileName}, CssException.DESIGN_EXCEPTION_BADCSSFILE );
				handler.getErrorHandler( ).semanticWarning( ex );
			}
		}
	}
}
