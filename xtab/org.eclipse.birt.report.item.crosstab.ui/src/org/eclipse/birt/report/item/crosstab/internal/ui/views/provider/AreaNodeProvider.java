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

package org.eclipse.birt.report.item.crosstab.internal.ui.views.provider;

import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabReportItemConstants;
import org.eclipse.birt.report.item.crosstab.internal.ui.util.CrosstabUIHelper;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.swt.graphics.Image;

public class AreaNodeProvider extends DefaultNodeProvider
{

	public Object[] getChildren( Object model )
	{
		PropertyHandle handle = (PropertyHandle) model;
		String propertyName = handle.getPropertyDefn( ).getName( );
		Object value = handle.getValue( );
		if ( value == null )
			return new Object[0];

		if ( propertyName.equals( ICrosstabReportItemConstants.MEASURES_PROP ) )
		{
			List measures = (List) value;
			return measures.toArray( );
		}
		return new Object[0];
	}

	public Object getParent( Object model )
	{
		PropertyHandle handle = (PropertyHandle) model;
		return handle.getElementHandle( );
	}

	public boolean hasChildren( Object model )
	{
		return getChildren( model ).length!=0;
	}

	public String getNodeDisplayName( Object element )
	{
		PropertyHandle handle = (PropertyHandle) element;
		String propertyName = handle.getPropertyDefn( ).getName( );

		if ( propertyName.equals( ICrosstabReportItemConstants.COLUMNS_PROP ) )
			return Messages.getString("AreaNodeProvider.ColumnArea"); //$NON-NLS-1$
		if ( propertyName.equals( ICrosstabReportItemConstants.ROWS_PROP ) )
			return Messages.getString("AreaNodeProvider.RowArea"); //$NON-NLS-1$
		if ( propertyName.equals( ICrosstabReportItemConstants.MEASURES_PROP ) )
			return Messages.getString("AreaNodeProvider.DetailArea"); //$NON-NLS-1$

		return null;
	}

	public Image getNodeIcon( Object element )
	{
		PropertyHandle handle = (PropertyHandle) element;
		String propertyName = handle.getPropertyDefn( ).getName( );

		if ( propertyName.equals( ICrosstabReportItemConstants.COLUMNS_PROP ) )
			return CrosstabUIHelper.getImage( CrosstabUIHelper.COLUMNS_AREA_IMAGE );
		if ( propertyName.equals( ICrosstabReportItemConstants.ROWS_PROP ) )
			return CrosstabUIHelper.getImage( CrosstabUIHelper.ROWS_AREA_IMAGE );
		if ( propertyName.equals( ICrosstabReportItemConstants.MEASURES_PROP ) )
			return CrosstabUIHelper.getImage( CrosstabUIHelper.DETAIL_AREA_IMAGE );
		return super.getNodeIcon( element );
	}
}
