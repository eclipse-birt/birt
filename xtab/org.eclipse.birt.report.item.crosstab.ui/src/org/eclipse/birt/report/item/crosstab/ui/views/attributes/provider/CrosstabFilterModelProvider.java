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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.ui.dialogs.FilterConditionBuilder;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.FilterModelProvider;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.ILevelViewConstants;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.FilterConditionHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.FilterCondition;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IStructureDefn;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclpse.birt.report.item.crosstab.ui.views.dialogs.CrosstabFilterConditionBuilder;

/**
 * 
 */

public class CrosstabFilterModelProvider extends FilterModelProvider
{

	/**
	 * Constant, represents empty String array.
	 */
	private static final String[] EMPTY = new String[0];

	/**
	 * 
	 */
	public CrosstabFilterModelProvider( )
	{
		// TODO Auto-generated constructor stub
	}

	/**
	 * Edit one item into the given position.
	 * 
	 * @param item
	 *            DesignElement object
	 * @param pos
	 *            The position.
	 * @return True if success, otherwise false.
	 * @throws SemanticException
	 */
	public boolean doEditItem( Object item, int pos )
	{
		 if ( item instanceof ExtendedItemHandle
				&& ( (ExtendedItemHandle) item ).getExtensionName( )
						.equals( "Crosstab" ) )
		{
			List list = new ArrayList( );
			list.add( item );
			Object[] levelArray = getElements( list );
			if ( levelArray == null || levelArray.length <= 0 )
			{
				return true;
			}
			LevelFilterConditionHandle levelfilterHandle = (LevelFilterConditionHandle) Arrays.asList( levelArray )
					.get( pos );
			if ( levelfilterHandle == null )
			{
				return false;
			}
			LevelViewHandle level = levelfilterHandle.getLevelHandle( );
			FilterConditionHandle filterHandle = levelfilterHandle.getfilterConditionHandle( );

			CrosstabFilterConditionBuilder dialog = new CrosstabFilterConditionBuilder( UIUtil.getDefaultShell( ),
					FilterConditionBuilder.DLG_TITLE_EDIT );
			dialog.setDesignHandle( (DesignElementHandle) item );
			dialog.setInput( filterHandle, level );
			if ( dialog.open( ) == Dialog.CANCEL )
			{
				return false;
			}
		}
		else
		{
			return false;
		}

		return true;
	}

	/**
	 * Deletes an item.
	 * 
	 * @param item
	 *            DesignElement object
	 * @param pos
	 *            The item's current position
	 * @return True if success, otherwise false.
	 * @throws PropertyValueException
	 */
	public boolean deleteItem( Object item, int pos )
			throws PropertyValueException
	{
		List list = new ArrayList( );
		list.add( item );
		Object[] levelArray = getElements( list );
		if ( levelArray == null || levelArray.length <= 0 )
		{
			return true;
		}
		LevelFilterConditionHandle levelfilterKeyHandle = (LevelFilterConditionHandle) Arrays.asList( levelArray )
				.get( pos );
		LevelViewHandle level = levelfilterKeyHandle.getLevelHandle( );
		PropertyHandle propertyHandle = level.getModelHandle( )
				.getPropertyHandle( ILevelViewConstants.FILTER_PROP );
		FilterConditionHandle sortKey = levelfilterKeyHandle.getfilterConditionHandle( );
		if ( propertyHandle != null && sortKey != null )
		{
			propertyHandle.removeItem( sortKey );
		}
				
		return true;
	}

	/**
	 * Inserts one item into the given position.
	 * 
	 * @param item
	 *            DesignElement object
	 * @param pos
	 *            The position.
	 * @return True if success, otherwise false.
	 * @throws SemanticException
	 */
	public boolean doAddItem( Object item, int pos ) throws SemanticException
	{
		if ( item instanceof ExtendedItemHandle
				&& ( (ExtendedItemHandle) item ).getExtensionName( )
						.equals( "Crosstab" ) )
		{
			CrosstabFilterConditionBuilder dialog = new CrosstabFilterConditionBuilder( UIUtil.getDefaultShell( ),
					FilterConditionBuilder.DLG_TITLE_NEW );
			dialog.setDesignHandle( (DesignElementHandle) item );
			dialog.setInput( null );
			if ( dialog.open( ) == Dialog.CANCEL )
			{
				return false;
			}

		}
		else
		{
			return super.doAddItem( item, pos );
		}
		return true;
	}


	/**
	 * Gets property display name of a given element.
	 * 
	 * @param element
	 *            Sort object
	 * @param key
	 *            Property key
	 * @return
	 */
	public String getText( Object element, String key )
	{

		if ( !( element instanceof LevelFilterConditionHandle ) )
			return "";//$NON-NLS-1$

		if ( key.equals( ILevelViewConstants.LEVEL_PROP ) )
		{
			return ( (LevelFilterConditionHandle) element ).getLevelHandle( )
					.getCubeLevel( ).getFullName( );
		}

		element = ((LevelFilterConditionHandle) element ).getfilterConditionHandle( );
		String value = ( (StructureHandle) element ).getMember( key )
				.getStringValue( );
		if ( value == null )
		{
			value = "";//$NON-NLS-1$
		}

		if ( key.equals( FilterCondition.OPERATOR_MEMBER ) )
		{
			IChoice choice = choiceSet.findChoice( value );
			if ( choice != null )
			{
				return choice.getDisplayName( );
			}
		}
		else
		{
			return value;
		}

		return "";//$NON-NLS-1$
	}

	/**
	 * Gets the display names of the given property keys.
	 * 
	 * @param keys
	 *            Property keys
	 * @return String array contains display names
	 */
	public String[] getColumnNames( String[] keys )
	{
		assert keys != null;
		String[] columnNames = new String[keys.length];
		columnNames[0] = Messages.getString( "CrosstabSortingModelProvider.ColumnName.GroupLevel" );
		for ( int i = 1; i < keys.length; i++ )
		{
			IStructureDefn structure = DEUtil.getMetaDataDictionary( )
					.getStructure( FilterCondition.FILTER_COND_STRUCT );
			columnNames[i] = structure.getMember( keys[i] ).getDisplayName( );
		}
		return columnNames;
	}

	/**
	 * Gets all elements of the given input.
	 * 
	 * @param input
	 *            The input object.
	 * @return Sorts array.
	 */
	public Object[] getElements( List input )
	{
		List list = new ArrayList( );
		Object obj = input.get( 0 );
		if ( !( obj instanceof ExtendedItemHandle ) )
			return EMPTY;
		ExtendedItemHandle element = (ExtendedItemHandle) obj;
		CrosstabReportItemHandle crossTab = null;
		try
		{
			crossTab = (CrosstabReportItemHandle) element.getReportItem( );
		}
		catch ( ExtendedElementException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace( );
		}
		if ( crossTab == null )
		{
			return list.toArray( );
		}
		if ( crossTab.getCrosstabView( ICrosstabConstants.COLUMN_AXIS_TYPE ) != null )
		{
			DesignElementHandle elementHandle = crossTab.getCrosstabView( ICrosstabConstants.COLUMN_AXIS_TYPE )
					.getModelHandle( );
			list.addAll( getLevel( (ExtendedItemHandle) elementHandle ) );
		}

		if ( crossTab.getCrosstabView( ICrosstabConstants.ROW_AXIS_TYPE ) != null )
		{
			DesignElementHandle elementHandle = crossTab.getCrosstabView( ICrosstabConstants.ROW_AXIS_TYPE )
					.getModelHandle( );
			list.addAll( getLevel( (ExtendedItemHandle) elementHandle ) );
		}

		return list.toArray( );
	}

	private List getLevel( ExtendedItemHandle handle )
	{
		CrosstabViewHandle crossTabViewHandle = null;
		try
		{
			crossTabViewHandle = (CrosstabViewHandle) handle.getReportItem( );
		}
		catch ( ExtendedElementException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace( );
		}
		List list = new ArrayList( );
		if ( crossTabViewHandle == null )
		{
			return list;
		}
		int dimensionCount = crossTabViewHandle.getDimensionCount( );

		for ( int i = 0; i < dimensionCount; i++ )
		{
			DimensionViewHandle dimension = crossTabViewHandle.getDimension( i );
			int levelCount = dimension.getLevelCount( );
			for ( int j = 0; j < levelCount; j++ )
			{
				LevelViewHandle levelHandle = dimension.getLevel( j );
				Iterator iter = levelHandle.filtersIterator( );
				while ( iter.hasNext( ) )
				{
					LevelFilterConditionHandle levelSortKeyHandle = new LevelFilterConditionHandle( levelHandle,
							(FilterConditionHandle) iter.next( ) );
					list.add( levelSortKeyHandle );
				}

			}
		}
		return list;
	}

	/**
	 * Moves one item from a position to another.
	 * 
	 * @param item
	 *            DesignElement object
	 * @param oldPos
	 *            The item's current position
	 * @param newPos
	 *            The item's new position
	 * @return True if success, otherwise false.
	 * @throws PropertyValueException
	 */
	public boolean moveItem( Object item, int oldPos, int newPos )
			throws PropertyValueException
	{
		// can not move for Crosstab sorting.
		return false;
	}

	class LevelFilterConditionHandle
	{

		protected LevelViewHandle levelHandle;
		protected FilterConditionHandle filterHandle;

		public LevelFilterConditionHandle( LevelViewHandle level,
				FilterConditionHandle filterCondition )
		{
			this.levelHandle = level;
			this.filterHandle = filterCondition;
		}

		public LevelViewHandle getLevelHandle( )
		{
			return this.levelHandle;
		}

		public void setLevelHandle( LevelViewHandle level )
		{
			this.levelHandle = level;
		}

		public FilterConditionHandle getfilterConditionHandle( )
		{
			return this.filterHandle;
		}

		public void setFilterConditionHandle(
				FilterConditionHandle filterCondition )
		{
			this.filterHandle = filterCondition;
		}

	}

}
