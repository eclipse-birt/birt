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

package org.eclipse.birt.report.model.api.validators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.AbstractThemeHandle;
import org.eclipse.birt.report.model.api.IncludedCssStyleSheetHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.CssException;
import org.eclipse.birt.report.model.api.elements.structures.IncludedCssStyleSheet;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.css.CssStyleSheet;
import org.eclipse.birt.report.model.elements.AbstractTheme;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.validators.AbstractElementValidator;

/**
 * Validates whether the included css style sheet file is existed or not.
 * 
 * 
 */

public class IncludedCssStyleSheetValidator extends AbstractElementValidator
{

	private static IncludedCssStyleSheetValidator instance = new IncludedCssStyleSheetValidator( );

	/**
	 * Returns the singleton validator instance.
	 * 
	 * @return the validator instance
	 */

	public static IncludedCssStyleSheetValidator getInstance( )
	{
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.validators.AbstractElementValidator#validate
	 * (org.eclipse.birt.report.model.core.Module,
	 * org.eclipse.birt.report.model.core.DesignElement)
	 */
	public List<SemanticException> validate( Module module,
			DesignElement element )
	{

		List cssStyle = null;
		Iterator<IncludedCssStyleSheetHandle> iter = null;
		if ( element instanceof AbstractTheme )
		{
			AbstractThemeHandle themeHandle = (AbstractThemeHandle) element
					.getHandle( module );
			iter = themeHandle.includeCssesIterator( );
			cssStyle = ( (AbstractTheme) element ).getCsses( );

		}
		else if ( element instanceof ReportDesign )
		{
			ReportDesignHandle handle = (ReportDesignHandle) element
					.getHandle( module );
			iter = handle.includeCssesIterator( );
			cssStyle = ( (ReportDesign) element ).getCsses( );
		}
		else
		{
			assert false;
			return Collections.emptyList( );
		}

		List<String> cssFileNameList = new ArrayList<String>( );

		if ( cssStyle != null )
		{
			for ( int i = 0; i < cssStyle.size( ); i++ )
			{
				CssStyleSheet css = (CssStyleSheet) cssStyle.get( i );
				cssFileNameList.add( css.getFileName( ) );
			}
		}

		List<SemanticException> errorList = new ArrayList<SemanticException>( );
		while ( iter.hasNext( ) )
		{
			IncludedCssStyleSheetHandle includedCssStyleSheet = (IncludedCssStyleSheetHandle) iter
					.next( );
			String fileName = includedCssStyleSheet.getFileName( );
			String externalCSSURI = includedCssStyleSheet.getExternalCssURI( );
			if ( externalCSSURI != null )
			{
				continue;
			}
			if ( !cssFileNameList.contains( fileName ) )
			{

				CssException ex = new CssException( module,
						(IncludedCssStyleSheet) includedCssStyleSheet
								.getStructure( ), new String[]{fileName},
						CssException.DESIGN_EXCEPTION_CSS_NOT_FOUND );
				errorList.add( ex );
			}

		}
		return errorList;
	}

}
