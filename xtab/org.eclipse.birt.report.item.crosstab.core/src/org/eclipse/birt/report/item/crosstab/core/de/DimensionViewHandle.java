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

package org.eclipse.birt.report.item.crosstab.core.de;

import java.util.logging.Level;

import org.eclipse.birt.report.item.crosstab.core.CrosstabException;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.IDimensionViewConstants;
import org.eclipse.birt.report.item.crosstab.core.de.util.CrosstabModelUtil;
import org.eclipse.birt.report.item.crosstab.core.i18n.MessageConstants;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabExtendedItemFactory;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;

/**
 * DimensionViewHandle.
 */
public class DimensionViewHandle extends AbstractCrosstabItemHandle implements
		IDimensionViewConstants,
		ICrosstabConstants
{

	/**
	 * 
	 * @param handle
	 */
	DimensionViewHandle( DesignElementHandle handle )
	{
		super( handle );
	}

	/**
	 * Gets the levels property of this dimension view.
	 * 
	 * @return levels property handle of this dimension view
	 */

	public PropertyHandle getLevelsProperty( )
	{
		return handle.getPropertyHandle( LEVELS_PROP );
	}

	/**
	 * Gets the referred OLAP dimension handle.
	 * 
	 * @return the referrred OLAP dimension handle
	 */
	public DimensionHandle getCubeDimension( )
	{
		return (DimensionHandle) handle.getElementProperty( DIMENSION_PROP );
	}

	/**
	 * Gets the referred OLAP dimension qualified name.
	 * 
	 * @return the referrred OLAP dimension qualified name
	 */
	public String getCubeDimensionName( )
	{
		return handle.getStringProperty( DIMENSION_PROP );
	}

	/**
	 * Gets the count of the level view handle.
	 * 
	 * @return count of the level view
	 */
	public int getLevelCount( )
	{
		return getLevelsProperty( ).getContentCount( );
	}

	/**
	 * Gets the level view handle that refers a cube level element with the
	 * given name.
	 * 
	 * @param name
	 *            name of the cube level to find
	 * @return level view handle if found, otherwise null
	 */
	public LevelViewHandle getLevel( String name )
	{
		for ( int i = 0; i < getLevelCount( ); i++ )
		{
			LevelViewHandle levelView = getLevel( i );
			if ( levelView != null )
			{
				String cubeLevelName = levelView.getCubeLevelName( );
				if ( ( cubeLevelName != null && cubeLevelName.equals( name ) )
						|| ( cubeLevelName == null && name == null ) )
					return levelView;
			}
		}
		return null;
	}

	/**
	 * Gets the level view with the given index. Position index is 0-based
	 * integer.
	 * 
	 * @param index
	 *            a 0-based integer of the level position
	 * @return the level view handle if found, otherwise null
	 */
	public LevelViewHandle getLevel( int index )
	{
		DesignElementHandle element = getLevelsProperty( ).getContent( index );
		return (LevelViewHandle) CrosstabUtil.getReportItem( element,
				LEVEL_VIEW_EXTENSION_NAME );
	}

	/**
	 * Inserts a level view to the given position. The position index is a
	 * 0-based integer.
	 * 
	 * @param levelHandle
	 *            the cube level handle to insert
	 * @param index
	 *            the target position, 0-based integer
	 * @return the level view handle that is generated and inserted to this
	 *         dimension view, null if OLAP level handle is used by another
	 *         level view or insert operation fails
	 * @throws SemanticException
	 */
	public LevelViewHandle insertLevel( LevelHandle levelHandle, int index )
			throws SemanticException
	{
		ExtendedItemHandle extendedItemHandle = CrosstabExtendedItemFactory.createLevelView( moduleHandle,
				levelHandle );
		if ( extendedItemHandle == null )
			return null;

		if ( levelHandle != null )
		{
			// if cube dimension container of this cube level element is not
			// what is referred by this dimension view, then the insertion is
			// forbidden
			if ( !levelHandle.getContainer( )
					.getContainer( )
					.getQualifiedName( )
					.equals( getCubeDimensionName( ) ) )
			{
				// TODO: throw exception
				logger.log( Level.WARNING, "" ); //$NON-NLS-1$
				return null;
			}

			// if this level handle has referred by an existing level view,
			// then log error and do nothing
			if ( getLevel( levelHandle.getQualifiedName( ) ) != null )
			{
				logger.log( Level.SEVERE,
						MessageConstants.CROSSTAB_EXCEPTION_DUPLICATE_LEVEL,
						levelHandle.getQualifiedName( ) );
				throw new CrosstabException( handle.getElement( ),
						new String[]{
								levelHandle.getQualifiedName( ),
								handle.getElement( ).getIdentifier( )
						},
						MessageConstants.CROSSTAB_EXCEPTION_DUPLICATE_LEVEL );
			}
		}

		CommandStack stack = getCommandStack( );
		stack.startTrans( null );

		LevelViewHandle levelView = null;
		try
		{
			getLevelsProperty( ).add( extendedItemHandle, index );

			levelView = (LevelViewHandle) CrosstabUtil.getReportItem( extendedItemHandle,
					LEVEL_VIEW_EXTENSION_NAME );

			// if level handle is specified, then adjust aggregations
			if ( levelHandle != null )
			{
				CrosstabReportItemHandle crosstab = getCrosstab( );
				if ( levelView != null && crosstab != null )
				{
					CrosstabModelUtil.adjustForLevelView( this, levelView, true );
				}
			}
		}
		catch ( SemanticException e )
		{
			stack.rollback( );
			throw e;
		}
		stack.commit( );

		return levelView;
	}

	/**
	 * Removes a level view that refers a cube level element with the given
	 * name.
	 * 
	 * @param name
	 *            name of the cube level element to remove
	 * @throws SemanticException
	 */
	public void removeLevel( String name ) throws SemanticException
	{
		LevelViewHandle levelView = getLevel( name );
		if ( levelView != null )
		{
			CommandStack stack = getCommandStack( );
			stack.startTrans( null );

			try
			{
				// adjust measure aggregations and then remove level view from
				// the design tree, the order can not reversed
				CrosstabReportItemHandle crosstab = getCrosstab( );
				if ( crosstab != null )
				{
					CrosstabModelUtil.adjustForLevelView( this,
							levelView,
							false );
				}

				levelView.handle.drop( );
			}
			catch ( SemanticException e )
			{
				stack.rollback( );
				throw e;
			}

			stack.commit( );
		}
	}

	/**
	 * Removes a level view at the given position. The position index is 0-based
	 * integer.
	 * 
	 * @param index
	 *            the position index of the level view to remove
	 * @throws SemanticException
	 */
	public void removeLevel( int index ) throws SemanticException
	{
		LevelViewHandle levelView = getLevel( index );
		if ( levelView != null )
		{
			CommandStack stack = getCommandStack( );
			stack.startTrans( null );

			try
			{
				// adjust measure aggregations and then remove level view from
				// the design tree, the order can not reversed
				CrosstabReportItemHandle crosstab = getCrosstab( );
				if ( crosstab != null )
				{
					CrosstabModelUtil.adjustForLevelView( this,
							levelView,
							false );
				}

				levelView.handle.drop( );
			}
			catch ( SemanticException e )
			{
				stack.rollback( );
				throw e;
			}

			stack.commit( );
		}
	}

	/**
	 * Gets the position index of this dimension view in the crosstab
	 * row/column.
	 * 
	 * @return the position index of this dimension view in the crosstab
	 *         row/column if this dimension is in the design tree and return
	 *         value is 0-based integer, otherwise -1
	 */
	public int getIndex( )
	{
		return handle.getIndex( );
	}

	/**
	 * Gets the axis type of this dimension view in the crosstab. If this
	 * dimension lies in the design tree, the returned value is either
	 * <code>ICrosstabConstants.ROW_AXIS_TYPE</code> or
	 * <code>ICrosstabConstants.COLUMN_AXIS_TYPE</code>. Otherwise return
	 * <code>ICrosstabConstants.NO_AXIS_TYPE</code>.
	 * 
	 * @return the axis type if this dimension resides in design tree, otherwise
	 *         -1;
	 */
	public int getAxisType( )
	{
		CrosstabViewHandle crosstabView = (CrosstabViewHandle) CrosstabUtil.getReportItem( handle.getContainer( ),
				CROSSTAB_VIEW_EXTENSION_NAME );
		return crosstabView == null ? NO_AXIS_TYPE : crosstabView.getAxisType( );

	}
}
