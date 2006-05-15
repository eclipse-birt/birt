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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.data.IColumnBinding;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.DataItem;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.elements.GridItem;
import org.eclipse.birt.report.model.elements.ImageItem;
import org.eclipse.birt.report.model.elements.Label;
import org.eclipse.birt.report.model.elements.ListItem;
import org.eclipse.birt.report.model.elements.ScalarParameter;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.TemplateReportItem;
import org.eclipse.birt.report.model.elements.TextDataItem;
import org.eclipse.birt.report.model.elements.TextItem;
import org.eclipse.birt.report.model.util.BoundColumnsMgr;
import org.eclipse.birt.report.model.util.DataBoundColumnUtil;

/**
 * The utility to provide backward compatibility of bound columns during writing
 * the design file.
 */

final class BoundColumnsWriterMgr extends BoundColumnsMgr
{

	/**
	 * Elements on which bound columns have been created.
	 */

	private Set processedElement = new HashSet( );

	/**
	 * Creates bound columns for the given element.
	 * 
	 * @param element
	 *            the element
	 * @param module
	 *            the root of the element
	 * @param propValue
	 *            the value from which to create bound columns
	 */

	protected void handleBoundsForValue( DesignElement element, Module module,
			String propValue )
	{
		if ( propValue == null )
			return;

		List newExprs = null;

		try
		{
			newExprs = ExpressionUtil.extractColumnExpressions( propValue );
		}
		catch ( BirtException e )
		{
			newExprs = null;
		}

		if ( newExprs != null && newExprs.size( ) >= 1 )
		{
			for ( int i = 0; i < newExprs.size( ); i++ )
			{
				IColumnBinding boundColumn = (IColumnBinding) newExprs.get( i );
				String newExpression = boundColumn.getBoundExpression( );
				if ( newExpression == null )
					continue;

				DataBoundColumnUtil.setupBoundDataColumn( element, boundColumn
						.getResultSetColumnName( ), newExpression, module );
			}
		}
	}

	/**
	 * Creates bound columns for the given value of the given element.
	 * 
	 * @param element
	 *            the element
	 * @param module
	 *            the root of the element
	 * @param propValue
	 *            the value from which to create bound columns
	 */

	protected void handleBoundsForParamBinding( DesignElement element,
			Module module, String propValue )
	{
		if ( propValue == null )
			return;

		List newExprs = null;

		try
		{
			newExprs = ExpressionUtil.extractColumnExpressions( propValue );
		}
		catch ( BirtException e )
		{
			newExprs = null;
		}

		if ( newExprs != null && newExprs.size( ) >= 1 )
		{
			DesignElement target = DataBoundColumnUtil
					.findTargetElementOfParamBinding( element, module );

			for ( int i = 0; i < newExprs.size( ); i++ )
			{
				IColumnBinding boundColumn = (IColumnBinding) newExprs.get( i );
				String newExpression = boundColumn.getBoundExpression( );
				if ( newExpression == null )
					continue;

				DataBoundColumnUtil.createBoundDataColumn( target, boundColumn
						.getResultSetColumnName( ), newExpression, module );
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.BoundColumnsMgr#dealCompatibleValueExpr(java.util.List,
	 *      org.eclipse.birt.report.model.core.Module)
	 */

	protected void dealCompatibleValueExpr( List dataItems, Module module )
	{
		super.dealCompatibleValueExpr( dataItems, module );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.BoundColumnsMgr#dealData(org.eclipse.birt.report.model.elements.DataItem,
	 *      org.eclipse.birt.report.model.core.Module)
	 */
	protected void dealData( DataItem element, Module module )
	{
		if ( processedElement.contains( element ) )
			return;

		processedElement.add( element );

		super.dealData( element, module );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.BoundColumnsMgr#dealExtendedItem(org.eclipse.birt.report.model.elements.ExtendedItem,
	 *      org.eclipse.birt.report.model.core.Module)
	 */
	protected void dealExtendedItem( ExtendedItem element, Module module )
	{
		if ( processedElement.contains( element ) )
			return;

		processedElement.add( element );

		super.dealExtendedItem( element, module );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.BoundColumnsMgr#dealGrid(org.eclipse.birt.report.model.elements.GridItem,
	 *      org.eclipse.birt.report.model.core.Module)
	 */
	protected void dealGrid( GridItem element, Module module )
	{
		if ( processedElement.contains( element ) )
			return;

		processedElement.add( element );

		super.dealGrid( element, module );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.BoundColumnsMgr#dealImage(org.eclipse.birt.report.model.elements.ImageItem,
	 *      org.eclipse.birt.report.model.core.Module)
	 */
	protected void dealImage( ImageItem element, Module module )
	{
		if ( processedElement.contains( element ) )
			return;

		processedElement.add( element );

		super.dealImage( element, module );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.BoundColumnsMgr#dealLabel(org.eclipse.birt.report.model.elements.Label,
	 *      org.eclipse.birt.report.model.core.Module)
	 */
	protected void dealLabel( Label element, Module module )
	{
		if ( processedElement.contains( element ) )
			return;

		processedElement.add( element );

		super.dealLabel( element, module );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.BoundColumnsMgr#dealList(org.eclipse.birt.report.model.elements.ListItem,
	 *      org.eclipse.birt.report.model.core.Module)
	 */
	protected void dealList( ListItem element, Module module )
	{
		if ( processedElement.contains( element ) )
			return;

		processedElement.add( element );

		super.dealList( element, module );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.BoundColumnsMgr#dealScalarParameter(org.eclipse.birt.report.model.elements.ScalarParameter,
	 *      org.eclipse.birt.report.model.core.Module)
	 */
	protected void dealScalarParameter( ScalarParameter element, Module module )
	{
		if ( processedElement.contains( element ) )
			return;

		processedElement.add( element );

		super.dealScalarParameter( element, module );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.BoundColumnsMgr#dealTable(org.eclipse.birt.report.model.elements.TableItem,
	 *      org.eclipse.birt.report.model.core.Module)
	 */
	protected void dealTable( TableItem element, Module module )
	{
		if ( processedElement.contains( element ) )
			return;

		processedElement.add( element );

		super.dealTable( element, module );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.BoundColumnsMgr#dealTemplateReportItem(org.eclipse.birt.report.model.elements.TemplateReportItem,
	 *      org.eclipse.birt.report.model.core.Module)
	 */
	protected void dealTemplateReportItem( TemplateReportItem element,
			Module module )
	{
		if ( processedElement.contains( element ) )
			return;

		processedElement.add( element );

		super.dealTemplateReportItem( element, module );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.BoundColumnsMgr#dealText(org.eclipse.birt.report.model.elements.TextItem,
	 *      org.eclipse.birt.report.model.core.Module)
	 */
	protected void dealText( TextItem element, Module module )
	{
		if ( processedElement.contains( element ) )
			return;

		processedElement.add( element );

		super.dealText( element, module );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.BoundColumnsMgr#dealTextData(org.eclipse.birt.report.model.elements.TextDataItem,
	 *      org.eclipse.birt.report.model.core.Module)
	 */
	protected void dealTextData( TextDataItem element, Module module )
	{
		if ( processedElement.contains( element ) )
			return;

		processedElement.add( element );

		super.dealTextData( element, module );
	}

}
