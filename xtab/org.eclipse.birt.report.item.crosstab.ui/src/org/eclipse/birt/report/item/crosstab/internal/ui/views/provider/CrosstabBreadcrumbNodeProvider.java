/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.item.crosstab.internal.ui.views.provider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.editors.breadcrumb.providers.DefaultBreadcrumbNodeProvider;
import org.eclipse.birt.report.designer.internal.ui.editors.breadcrumb.providers.IBreadcrumbNodeProvider;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.views.ProviderFactory;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabReportItemConstants;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.editparts.VirtualCellEditPart;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.VirtualCrosstabCellAdapter;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.gef.EditPart;

/**
 * 
 */

public class CrosstabBreadcrumbNodeProvider extends
		DefaultBreadcrumbNodeProvider
{

	public Object getRealModel( Object element )
	{
		if ( element instanceof EditPart )
		{
			EditPart editpart = (EditPart) element;
			return editpart.getModel( );
		}
		return element;
	}

	public Object getParent( Object element )
	{
		Object parent = ProviderFactory.createProvider( element )
				.getParent( element );
		if ( parent instanceof ExtendedItemHandle
				&& ICrosstabConstants.CROSSTAB_EXTENSION_NAME.equals( ( (ExtendedItemHandle) parent ).getExtensionName( ) ) )
		{
			return parent;
		}
		return super.getParent( element );
	}

	public Object[] getChildren( Object object )
	{
		Object element = getRealModel( object );
		Object parent = ProviderFactory.createProvider( element )
				.getParent( element );
		if ( parent instanceof ExtendedItemHandle
				&& ICrosstabConstants.CROSSTAB_EXTENSION_NAME.equals( ( (ExtendedItemHandle) parent ).getExtensionName( ) ) )
		{
			List children = getEditPart( parent ).getChildren( );
			List elements = new ArrayList( );
			for ( int i = 0; i < children.size( ); i++ )
			{
				EditPart child = ( (EditPart) children.get( i ) );
				Object adapter = child.getAdapter( IBreadcrumbNodeProvider.class );
				if ( adapter instanceof CrosstabCellBreadcrumbNodeProvider )
				{
					( (CrosstabCellBreadcrumbNodeProvider) adapter ).setContext( viewer );
					if ( element == ( (CrosstabCellBreadcrumbNodeProvider) adapter ).getParent( child ) )
					{
						elements.add( child );
					}
				}
			}
			return elements.toArray( );
		}
		else
		{
			List children = new ArrayList( );
			children.addAll( Arrays.asList( ProviderFactory.createProvider( element )
					.getChildren( element ) ) );
			List list = getEditPart( element ).getChildren( );

			ExtendedItemHandle crossTabHandle = (ExtendedItemHandle) element;
			try
			{
				CrosstabReportItemHandle crossTab = (CrosstabReportItemHandle) crossTabHandle.getReportItem( );

				for ( int i = 0; i < list.size( ); i++ )
				{
					EditPart editPart = (EditPart) list.get( i );
					if ( editPart instanceof VirtualCellEditPart )
					{
						int type = ( (VirtualCrosstabCellAdapter) ( (VirtualCellEditPart) editPart ).getModel( ) ).getType( );
						Object handle = null;
						switch ( type )
						{
							case VirtualCrosstabCellAdapter.COLUMN_TYPE :
							{
								if ( crossTab.getCrosstabView( ICrosstabConstants.COLUMN_AXIS_TYPE ) != null )
								{
									handle = crossTab.getCrosstabView( ICrosstabConstants.COLUMN_AXIS_TYPE )
											.getModelHandle( );

								}
								else
								{
									handle = crossTabHandle.getPropertyHandle( ICrosstabReportItemConstants.COLUMNS_PROP );
								}
							}
								break;
							case VirtualCrosstabCellAdapter.ROW_TYPE :
							{
								if ( crossTab.getCrosstabView( ICrosstabConstants.ROW_AXIS_TYPE ) != null )
								{
									handle = crossTab.getCrosstabView( ICrosstabConstants.ROW_AXIS_TYPE )
											.getModelHandle( );
								}
								else
								{
									handle = crossTabHandle.getPropertyHandle( ICrosstabReportItemConstants.ROWS_PROP );
								}
							}
								break;
							case VirtualCrosstabCellAdapter.MEASURE_TYPE :
							{
								handle = crossTabHandle.getPropertyHandle( ICrosstabReportItemConstants.MEASURES_PROP );
							}
								break;
						}
						if ( handle != null )
						{
							int index = children.indexOf( handle );
							if ( index > -1 )
							{
								children.remove( index );
								children.add( index, editPart );
							}
						}
					}
				}
			}
			catch ( ExtendedElementException e )
			{
				ExceptionUtil.handle( e );
			}
			return children.toArray( );
		}
	}
}
