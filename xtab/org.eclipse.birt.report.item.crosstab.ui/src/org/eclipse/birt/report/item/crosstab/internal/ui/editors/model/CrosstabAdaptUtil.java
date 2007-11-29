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

package org.eclipse.birt.report.item.crosstab.internal.ui.editors.model;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.data.adapter.api.ICubeQueryUtil;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.designer.ui.preferences.PreferenceFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.item.crosstab.core.de.AbstractCrosstabItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabQueryUtil;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.item.crosstab.plugin.CrosstabPlugin;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.LevelAttributeHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.elements.structures.LevelAttribute;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.MeasureGroupHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.birt.report.model.api.olap.TabularLevelHandle;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;

/**
 * Util class
 */
public class CrosstabAdaptUtil
{

	protected static Logger logger = Logger.getLogger( CrosstabAdaptUtil.class.getName( ) );

	/**
	 * Gets the row or column count after the handel, now include the current
	 * handle
	 * 
	 * @param handle
	 * @return
	 */
	public static int getNumberAfterDimensionViewHandle(
			DimensionViewHandle handle )
	{
		CrosstabReportItemHandle reportItem = (CrosstabReportItemHandle) CrosstabUtil.getReportItem( handle.getCrosstabHandle( ) );
		int index = handle.getIndex( );
		int type = handle.getAxisType( );
		int count = reportItem.getDimensionCount( type );
		int retValue = 0;
		for ( int i = index; i < count; i++ )
		{
			retValue = retValue
					+ reportItem.getDimension( type, index ).getLevelCount( );
		}
		return retValue;
	}

	/**
	 * @param owner
	 * @param levelHandle
	 * @return
	 */
	public static ComputedColumn createComputedColumn( ReportItemHandle owner,
			LevelHandle levelHandle )
	{
		ComputedColumn bindingColumn = StructureFactory.newComputedColumn( owner,
				levelHandle.getName( ) );

		// bindingColumn.setDataType( DesignChoiceConstants.
		// COLUMN_DATA_TYPE_ANY);
		bindingColumn.setDataType( levelHandle.getDataType( ) );
		bindingColumn.setExpression( DEUtil.getExpression( levelHandle ) );

		return bindingColumn;
	}

	public static ComputedColumn createLevelDisplayComputedColumn(
			ReportItemHandle owner, LevelHandle levelHandle )
	{
		ComputedColumn bindingColumn = StructureFactory.newComputedColumn( owner,
				levelHandle.getName( ) );

		if ( levelHandle instanceof TabularLevelHandle
				&& ( (TabularLevelHandle) levelHandle ).getDisplayColumnName( ) != null
				&& ( (TabularLevelHandle) levelHandle ).getDisplayColumnName( )
						.trim( )
						.length( ) > 0 )
		{
			DesignElementHandle temp = levelHandle.getContainer( );
			String dimensionName = "";
			while ( temp != null )
			{
				if ( temp instanceof org.eclipse.birt.report.model.api.olap.DimensionHandle )
				{
					dimensionName = ( (org.eclipse.birt.report.model.api.olap.DimensionHandle) temp ).getName( );
					break;
				}
				temp = temp.getContainer( );
			}

			String expr = ExpressionUtil.createJSDimensionExpression( dimensionName,
					levelHandle.getName( ),
					ICubeQueryUtil.DISPLAY_NAME_ATTR );

			bindingColumn.setDataType( DesignChoiceConstants.COLUMN_DATA_TYPE_STRING );
			bindingColumn.setExpression( expr );
		}
		else
		{
			bindingColumn.setDataType( levelHandle.getDataType( ) );
			bindingColumn.setExpression( DEUtil.getExpression( levelHandle ) );
		}

		return bindingColumn;
	}

	public static DataItemHandle createColumnBindingAndDataItem(
			ReportItemHandle owner, LevelHandle levelHandle )
			throws SemanticException
	{
		ComputedColumn bindingColumn = createLevelDisplayComputedColumn( owner,
				levelHandle );

		ComputedColumnHandle bindingHandle = owner.addColumnBinding( bindingColumn,
				false );

		DataItemHandle dataHandle = DesignElementFactory.getInstance( )
				.newDataItem( levelHandle.getName( ) );
		dataHandle.setResultSetColumn( bindingHandle.getName( ) );

		if ( levelHandle.getDateTimeFormat( ) != null )
		{
			if ( levelHandle.getContainer( ) != null
					&& levelHandle.getContainer( ).getContainer( ) != null )
			{
				Iterator itr = levelHandle.attributesIterator( );

				boolean hasDateAttribute = false;

				while ( itr != null && itr.hasNext( ) )
				{
					LevelAttributeHandle att = (LevelAttributeHandle) itr.next( );

					if ( LevelAttribute.DATE_TIME_ATTRIBUTE_NAME.equals( att.getName( ) ) )
					{
						hasDateAttribute = true;
						break;
					}
				}

				if ( hasDateAttribute )
				{
					String dimensionName = levelHandle.getContainer( )
							.getContainer( )
							.getName( );

					bindingHandle.setDataType( DesignChoiceConstants.COLUMN_DATA_TYPE_DATETIME );
					bindingHandle.setExpression( ExpressionUtil.createJSDimensionExpression( dimensionName,
							levelHandle.getName( ),
							LevelAttribute.DATE_TIME_ATTRIBUTE_NAME ) );

					dataHandle.getPrivateStyle( )
							.setDateTimeFormatCategory( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_CUSTOM );
					dataHandle.getPrivateStyle( )
							.setDateTimeFormat( levelHandle.getDateTimeFormat( ) );
				}
			}
		}

		return dataHandle;
	}

	/**
	 * @param owner
	 * @param measureHandle
	 * @return
	 */
	public static ComputedColumn createComputedColumn( ReportItemHandle owner,
			MeasureHandle measureHandle )
	{
		ComputedColumn bindingColumn = StructureFactory.newComputedColumn( owner,
				measureHandle.getName( ) );

		// bindingColumn.setDataType( DesignChoiceConstants.
		// COLUMN_DATA_TYPE_ANY);
		bindingColumn.setDataType( measureHandle.getDataType( ) );
		bindingColumn.setExpression( DEUtil.getExpression( measureHandle ) );

		return bindingColumn;
	}

	/**
	 * Find the position of the element. If the element is null, the position is
	 * last
	 * 
	 * @param parent
	 * @param element
	 * @return position
	 */
	public static int findInsertPosition( DesignElementHandle parent,
			DesignElementHandle element )
	{
		// if after is null, insert at last
		if ( element == null )
		{
			return parent.getContentCount( DEUtil.getDefaultContentName( parent ) );
		}
		// parent.findContentSlot( )

		return element.getIndex( );
	}

	public static ExtendedItemHandle getExtendedItemHandle(
			DesignElementHandle handle )
	{
		while ( handle != null )
		{
			if ( handle instanceof ExtendedItemHandle )
			{
				return (ExtendedItemHandle) handle;
			}
			handle = handle.getContainer( );

		}
		return null;
	}

	/**
	 * @param extendedHandle
	 * @return
	 */
	public static LevelViewHandle getLevelViewHandle(
			ExtendedItemHandle extendedHandle )
	{
		AbstractCrosstabItemHandle handle = (AbstractCrosstabItemHandle) CrosstabUtil.getReportItem( extendedHandle );
		while ( handle != null )
		{
			if ( handle instanceof LevelViewHandle )
			{
				return (LevelViewHandle) handle;
			}
			handle = handle.getContainer( );
		}
		return null;
	}

	/**
	 * @param extendedHandle
	 * @return
	 */
	public static DimensionViewHandle getDimensionViewHandle(
			ExtendedItemHandle extendedHandle )
	{
		AbstractCrosstabItemHandle handle = (AbstractCrosstabItemHandle) CrosstabUtil.getReportItem( extendedHandle );
		while ( handle != null )
		{
			if ( handle instanceof DimensionViewHandle )
			{
				return (DimensionViewHandle) handle;
			}
			handle = handle.getContainer( );
		}
		return null;
	}

	public static DimensionHandle getDimensionHandle( LevelHandle levelHandle )
	{
		DesignElementHandle parent = levelHandle;
		while ( parent != null )
		{
			if ( parent instanceof DimensionHandle )
			{
				return (DimensionHandle) parent;
			}
			parent = parent.getContainer( );
		}
		return null;
	}

	public static CubeHandle getCubeHandle( DesignElementHandle levelHandle )
	{
		DesignElementHandle parent = levelHandle;
		while ( parent != null )
		{
			if ( parent instanceof CubeHandle )
			{
				return (CubeHandle) parent;
			}
			parent = parent.getContainer( );
		}
		return null;
	}

	/**
	 * @param extendedHandle
	 * @return
	 */
	public static MeasureViewHandle getMeasureViewHandle(
			ExtendedItemHandle extendedHandle )
	{
		AbstractCrosstabItemHandle handle = (AbstractCrosstabItemHandle) CrosstabUtil.getReportItem( extendedHandle );
		while ( handle != null )
		{
			if ( handle instanceof MeasureViewHandle )
			{
				return (MeasureViewHandle) handle;
			}
			handle = handle.getContainer( );
		}
		return null;
	}

	// public static void processInvaildBindings( CrosstabReportItemHandle
	// handle )
	// {
	// processInvaildBindings( handle, true );
	// }

	public static void processInvaildBindings( CrosstabReportItemHandle handle )
	{
		if ( PreferenceFactory.getInstance( )
				.getPreferences( CrosstabPlugin.getDefault( ) )
				.getBoolean( CrosstabPlugin.PREFERENCE_AUTO_DEL_BINDINGS ) )
		{
			MessageDialogWithToggle msgDlg = MessageDialogWithToggle.openYesNoQuestion( UIUtil.getDefaultShell( ),
					Messages.getString( "DeleteBindingDialog.Title" ), //$NON-NLS-1$
					Messages.getString( "DeleteBindingDialog.Message" ), //$NON-NLS-1$
					Messages.getString( "DeleteBindingDialog.ToggleMessage" ), //$NON-NLS-1$
					false,
					null,
					null );
			if ( msgDlg.getReturnCode( ) == IDialogConstants.YES_ID )
			{
				removeInvalidBindings( handle );
			}
			else if ( msgDlg.getReturnCode( ) == IDialogConstants.NO_ID )
			{
				// dothing
			}
			if ( msgDlg.getReturnCode( ) == IDialogConstants.YES_ID
					|| msgDlg.getReturnCode( ) == IDialogConstants.NO_ID )
			{
				PreferenceFactory.getInstance( )
						.getPreferences( CrosstabPlugin.getDefault( ) )
						.setValue( CrosstabPlugin.PREFERENCE_AUTO_DEL_BINDINGS,
								!msgDlg.getToggleState( ) );
			}
		}
		else
		{
			removeInvalidBindings( handle );
		}
		// removeInvalidBindings(handle);
	}

	public static void removeInvalidBindings( CrosstabReportItemHandle handle )
	{
		try
		{
			ICubeQueryDefinition definition = CrosstabQueryUtil.createCubeQuery( handle,
					null,
					true,
					true,
					true,
					true,
					false,
					false );
			DataRequestSession session = DataRequestSession.newSession( new DataSessionContext( DataSessionContext.MODE_DIRECT_PRESENTATION ) );
			List list = session.getCubeQueryUtil( )
					.getInvalidBindings( definition );
			// for (int i=0; i<list.size( ); i++)
			// {
			// Object obj = list.get( i );
			// if (obj instanceof IBinding)
			// {
			// String name = ((IBinding)obj).getBindingName( );
			// ComputedColumnHandle delhandle =
			// ((ReportItemHandle)handle.getModelHandle( )).findColumnBinding(
			// name );
			// if (delhandle != null)
			// {
			// delhandle.drop( );
			// }
			// }
			// }
			( (ReportItemHandle) handle.getModelHandle( ) ).removedColumnBindings( list );
		}
		catch ( BirtException e )
		{
			// donothing
			logger.log( Level.SEVERE, e.getMessage( ), e );
		}
	}

	/**
	 * Adds the measreview handle to the CrosstabReportItemHandle.
	 * 
	 * @param reportHandle
	 * @param measureHandle
	 * @param position
	 * @throws SemanticException
	 */
	public static void addMeasureHandle( CrosstabReportItemHandle reportHandle,
			MeasureHandle measureHandle, int position )
			throws SemanticException
	{
		MeasureViewHandle measureViewHandle = reportHandle.insertMeasure( measureHandle,
				position );
		measureViewHandle.addHeader( );

		ComputedColumn bindingColumn = CrosstabAdaptUtil.createComputedColumn( (ExtendedItemHandle) reportHandle.getModelHandle( ),
				measureHandle );
		ComputedColumnHandle bindingHandle = ( (ExtendedItemHandle) reportHandle.getModelHandle( ) ).addColumnBinding( bindingColumn,
				false );

		CrosstabCellHandle cellHandle = measureViewHandle.getCell( );

		DataItemHandle dataHandle = DesignElementFactory.getInstance( )
				.newDataItem( measureHandle.getName( ) );

		dataHandle.setResultSetColumn( bindingHandle.getName( ) );

		cellHandle.addContent( dataHandle );

		LabelHandle labelHandle = DesignElementFactory.getInstance( )
				.newLabel( null );
		labelHandle.setText( measureHandle.getName( ) );

		measureViewHandle.getHeader( ).addContent( labelHandle );
	}

	/**
	 * @param objects
	 * @return
	 */
	public static boolean canCreateMultipleCommand( Object[] objects )
	{
		if ( objects == null || objects.length <= 1 )
		{
			return false;
		}
		// There are have some other logic, but when allow to drag the ,ignore
		// the logic here.
		for ( int i = 0; i < objects.length; i++ )
		{
			if ( objects[i] instanceof MeasureHandle
					|| objects[i] instanceof MeasureGroupHandle )
			{
				continue;
			}
			return false;
		}
		return true;
	}
}
