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

package org.eclipse.birt.report.item.crosstab.core.de.internal;

import java.util.List;
import java.util.logging.Level;

import org.eclipse.birt.report.item.crosstab.core.CrosstabException;
import org.eclipse.birt.report.item.crosstab.core.de.AbstractCrosstabItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.i18n.MessageConstants;
import org.eclipse.birt.report.item.crosstab.core.i18n.Messages;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabExtendedItemFactory;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;

/**
 * CrosstabViewTask
 */
public class CrosstabViewTask extends AbstractCrosstabModelTask
{

	protected CrosstabViewHandle crosstabView = null;

	/**
	 * 
	 * @param focus
	 */
	public CrosstabViewTask( AbstractCrosstabItemHandle focus )
	{
		super( focus );
		crosstabView = (CrosstabViewHandle) focus;
	}

	/**
	 * 
	 * @param measureList
	 * @param functionList
	 * @return
	 * @throws SemanticException
	 */
	public CrosstabCellHandle addGrandTotal( List measureList, List functionList )
			throws SemanticException
	{
		if ( !isValidParameters( functionList, measureList ) )
			return null;

		verifyTotalMeasureFunctions( crosstabView.getAxisType( ),
				functionList,
				measureList );

		PropertyHandle propHandle = crosstabView.getGrandTotalProperty( );

		CommandStack stack = crosstabView.getCommandStack( );
		stack.startTrans( Messages.getString( "CrosstabViewTask.msg.add.grandtotal" ) ); //$NON-NLS-1$

		CrosstabCellHandle totalCell = null;

		try
		{
			ExtendedItemHandle grandTotal = null;
			if ( propHandle.getContentCount( ) <= 0 )
			{
				grandTotal = CrosstabExtendedItemFactory.createCrosstabCell( crosstabView.getModuleHandle( ) );
				propHandle.add( grandTotal );
			}

			// adjust the measure aggregations
			CrosstabReportItemHandle crosstab = crosstabView.getCrosstab( );
			if ( crosstab != null && measureList != null )
			{
				addMeasureAggregations( crosstabView.getAxisType( ),
						measureList,
						functionList,
						false );
			}

			validateCrosstab( );

			totalCell = (CrosstabCellHandle) CrosstabUtil.getReportItem( grandTotal );
		}
		catch ( SemanticException e )
		{
			crosstabView.getLogger( ).log( Level.INFO, e.getMessage( ), e );
			stack.rollback( );
			throw e;
		}

		stack.commit( );

		return totalCell;
	}

	/**
	 * Removes grand total from crosstab if it is not empty, otherwise do
	 * nothing.
	 */
	public void removeGrandTotal( ) throws SemanticException
	{
		PropertyHandle propHandle = crosstabView.getGrandTotalProperty( );

		if ( propHandle.getContentCount( ) > 0 )
		{
			CommandStack stack = crosstabView.getCommandStack( );
			stack.startTrans( Messages.getString( "CrosstabViewTask.msg.remove.grandtotal" ) ); //$NON-NLS-1$

			try
			{
				// adjust the measure aggregations before remove the grand-total
				// cell, for some adjustment action should depend on the
				// grand-total information; if there is no level in this axis,
				// then we need do nothing about the aggregations
				if ( crosstab != null )
				// && CrosstabModelUtil.getAllLevelCount( crosstab,
				// crosstabView.getAxisType( ) ) > 0 )
				{
					removeMeasureAggregations( crosstabView.getAxisType( ) );
				}

				propHandle.drop( 0 );
			}
			catch ( SemanticException e )
			{
				crosstabView.getLogger( ).log( Level.INFO, e.getMessage( ), e );
				stack.rollback( );
				throw e;
			}

			stack.commit( );
		}
	}

	/**
	 * Removes a dimension view that refers a cube dimension name with the given
	 * name from the design tree.
	 * 
	 * @param name
	 *            name of the dimension view to remove
	 * @throws SemanticException
	 */
	public void removeDimension( String name ) throws SemanticException
	{
		DimensionViewHandle dimensionView = crosstabView.getDimension( name );

		if ( dimensionView == null )
		{
			crosstabView.getLogger( ).log( Level.SEVERE,
					MessageConstants.CROSSTAB_EXCEPTION_DIMENSION_NOT_FOUND,
					name );
			throw new CrosstabException( crosstabView.getModelHandle( )
					.getElement( ), new String[]{
					name,
					crosstabView.getModelHandle( )
							.getElement( )
							.getIdentifier( )
			}, MessageConstants.CROSSTAB_EXCEPTION_DIMENSION_NOT_FOUND );
		}

		removeDimension( dimensionView );
	}

	/**
	 * Removes a dimension view in the given position. Index is 0-based integer.
	 * 
	 * @param index
	 *            the position index of the dimension to remove, 0-based integer
	 * @throws SemanticException
	 */
	public void removeDimension( int index ) throws SemanticException
	{
		DimensionViewHandle dimensionView = crosstabView.getDimension( index );
		if ( dimensionView == null )
		{
			crosstabView.getLogger( ).log( Level.SEVERE,
					MessageConstants.CROSSTAB_EXCEPTION_DIMENSION_NOT_FOUND,
					String.valueOf( index ) );
			return;
		}

		removeDimension( dimensionView );
	}

	/**
	 * 
	 * @param dimensionView
	 * @throws SemanticException
	 */
	public void removeDimension( DimensionViewHandle dimensionView )
			throws SemanticException
	{
		if ( dimensionView == null
				|| dimensionView.getContainer( ) != crosstabView )
			return;

		CommandStack stack = crosstabView.getCommandStack( );
		stack.startTrans( Messages.getString( "CrosstabViewTask.msg.remove.dimension" ) ); //$NON-NLS-1$

		int count = dimensionView.getLevelCount( );

		try
		{
			// adjust measure aggregations and then remove dimension view from
			// the design tree, the order can not reversed
			if ( crosstab != null )
			{
				for ( int i = 0; i < count; i++ )
				{
					dimensionView.removeLevel( 0 );
				}
			}

			dimensionView.getModelHandle( ).drop( );

			// check if all dimensions are removed, we need remove grand total
			// on the axis
			if ( crosstabView.getDimensionCount( ) == 0 )
			{
				crosstabView.removeGrandTotal( );
			}
		}
		catch ( SemanticException e )
		{
			stack.rollback( );
			throw e;
		}

		stack.commit( );
	}
}
