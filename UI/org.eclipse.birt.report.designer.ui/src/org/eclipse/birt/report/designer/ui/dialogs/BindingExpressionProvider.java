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

package org.eclipse.birt.report.designer.ui.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;

/**
 * 
 */

public class BindingExpressionProvider extends ExpressionProvider
{

	public static final String DATASETS = Messages.getString( "ExpressionProvider.Category.DataSets" ); //$NON-NLS-1$

	private DataSetHandle dataSetHandle = null;

	public BindingExpressionProvider( DesignElementHandle handle )
	{
		super( handle );
		if ( handle instanceof ReportItemHandle )
		{
			dataSetHandle = ( (ReportItemHandle) handle ).getDataSet( );
		}
	}

	protected List getCategoryList( )
	{
		List categoryList = super.getCategoryList( );
		if ( dataSetHandle != null )
		{
			categoryList.add( DATASETS );
		}
		return categoryList;
	}

	protected List getChildrenList( Object parent )
	{
		if ( DATASETS.equals( parent ) )
		{
			List dataSeList = new ArrayList( );
			dataSeList.add( dataSetHandle );
			return dataSeList;
		}
		if ( parent instanceof DataSetHandle )
		{
			return UIUtil.getColumnList( (DataSetHandle) parent );
		}
		return super.getChildrenList( parent );
	}

	public String getDisplayText( Object element )
	{
		if ( element instanceof DataSetHandle )
		{
			return ( (DataSetHandle) element ).getName( );
		}
		else if ( element instanceof ResultSetColumnHandle )
		{
			return ( (ResultSetColumnHandle) element ).getColumnName( );
		}
		return super.getDisplayText( element );
	}

	public String getInsertText( Object element )
	{
		if ( element instanceof ResultSetColumnHandle )
		{
			return DEUtil.getExpression( element );
		}
		return super.getInsertText( element );
	}

}
