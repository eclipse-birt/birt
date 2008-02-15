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

package org.eclipse.birt.report.item.crosstab.ui.views.attributes.provider;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.PropertyDescriptorProvider;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;


/**
 * @author Administrator
 *
 */
public class HideMeasureHeaderProvider extends PropertyDescriptorProvider
{

	/**
	 * @param property
	 * @param element
	 */
	public HideMeasureHeaderProvider( String property, String element )
	{
		super( property, element );
		// TODO Auto-generated constructor stub
	}
	
	public String getDisplayName( )
	{
		String displayName = super.getDisplayName( );
		if ( displayName != null && displayName.length( ) > 0 )
		{
			return displayName;
		}
		return Messages.getString( "CrosstabGeneralPage.HideMeasureHeader" ); //$NON-NLS-1$
	}

}
