/***********************************************************************
 * Copyright (c) 2008 IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.util;

import org.eclipse.birt.report.engine.css.engine.value.css.CSSConstants;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

/**
 * Provides convenience methods for text alignment resolution
 * 
 */
public class BidiAlignmentResolver
{
	public static String resolveAlignment( ReportDesignHandle model )
	{
		if ( model != null )
		{
			return getDefaultAlignment( model.getBidiOrientation( ) );
		}
		return CSSConstants.CSS_LEFT_VALUE;
	}

	public static String getDefaultAlignment( boolean rtl )
	{
		if ( rtl )
			return CSSConstants.CSS_RIGHT_VALUE;

		return CSSConstants.CSS_LEFT_VALUE;
	}

	public static String getDefaultAlignment( String orientation )
	{
		return getDefaultAlignment( DesignChoiceConstants.BIDI_DIRECTION_RTL
				.equals( orientation ) );
	}

	public static String resolveAlignment( String alignment, boolean mirrored )
	{
		if ( !mirrored )
		{
			return alignment;
		}
		if ( CSSConstants.CSS_RIGHT_VALUE.equals( alignment ) )
		{
			return CSSConstants.CSS_LEFT_VALUE;
		}
		if ( CSSConstants.CSS_LEFT_VALUE.equals( alignment ) )
		{
			return CSSConstants.CSS_RIGHT_VALUE;
		}
		return alignment;
	}

}