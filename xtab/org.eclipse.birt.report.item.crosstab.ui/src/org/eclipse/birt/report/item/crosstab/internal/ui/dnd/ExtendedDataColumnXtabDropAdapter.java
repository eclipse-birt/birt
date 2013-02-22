/*******************************************************************************
 * Copyright (c) 2004, 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.item.crosstab.internal.ui.dnd;

import org.eclipse.birt.report.designer.core.DesignerConstants;
import org.eclipse.birt.report.designer.internal.ui.dnd.DNDLocation;
import org.eclipse.birt.report.designer.internal.ui.dnd.DNDService;
import org.eclipse.birt.report.designer.internal.ui.dnd.IDropAdapter;
import org.eclipse.birt.report.designer.internal.ui.extension.ExtendedDataXtabAdapterHelper;
import org.eclipse.birt.report.designer.internal.ui.extension.IExtendedDataXtabAdapter;
import org.eclipse.birt.report.designer.util.IVirtualValidator;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.item.crosstab.internal.ui.AggregationCellProviderWrapper;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.editparts.CrosstabCellEditPart;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.editparts.CrosstabTableEditPart;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabCellAdapter;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.VirtualCrosstabCellAdapter;
import org.eclipse.birt.report.item.crosstab.ui.extension.AggregationCellViewAdapter;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;

public class ExtendedDataColumnXtabDropAdapter implements IDropAdapter
{
	IExtendedDataXtabAdapter adapter = ExtendedDataXtabAdapterHelper.getInstance( ).getAdapter( );
	Object transfer;

	public int canDrop( Object transfer, Object target, int operation,
			DNDLocation location )
	{
		if (adapter == null)
		{
			return DNDService.LOGIC_UNKNOW;
		}
		
		if ( ( target instanceof CrosstabCellEditPart || target instanceof CrosstabTableEditPart)
				&& adapter.isExtendedDataColumn( transfer )
//				&& InsertInLayoutUtil.handleValidateInsertToLayout( transfer, getTargetEditPart( target ) )
		)
		{
			return DNDService.LOGIC_TRUE;
		}
		
		return DNDService.LOGIC_UNKNOW;
	}
	
	private EditPart getTargetEditPart( Object target )
	{
		if ( target instanceof EditPart )
		{
			return (EditPart) target;
		}
		return null;
	}

	public boolean performDrop( Object transfer, Object target, int operation,
			DNDLocation location )
	{
		this.transfer = transfer;
		if ( adapter != null && adapter.isExtendedDataColumn( transfer) && getTargetEditPart(target) != null)
		{
			EditPart targetPart = getTargetEditPart(target);
			
			CubeHandle cubeHandle = null;
			MeasureHandle measureHandle = null;
			DimensionHandle dimensionHandle = null;
			
			CrosstabReportItemHandle crosstab = getCrosstab( targetPart );
			if ( crosstab != null )
			{
				crosstab.getModuleHandle( ).getCommandStack( ).startTrans( "Insert Column" ); //$NON-NLS-1$

				adapter.setExtendedData( (ReportItemHandle)crosstab.getModelHandle( ), ((ReportElementHandle) transfer).getContainer( ).getContainer( ));

				cubeHandle = ((ReportItemHandle)crosstab.getModelHandle( )).getCube( );
				measureHandle = cubeHandle.getMeasure( ((ReportElementHandle) transfer).getName( ) );
				dimensionHandle = cubeHandle.getDimension( ((ReportElementHandle) transfer).getName( ) );
			}
			
			if ( targetPart.getModel( ) instanceof IVirtualValidator )
			{
				// TODO
				//if ( ( (IVirtualValidator) targetPart.getModel( ) ).handleValidate( measureHandle ) )
				if (handleValidate(targetPart, measureHandle))
				{
					// drop as measure
					
					CreateRequest request = new CreateRequest( );
					request.getExtendedData( ).put( DesignerConstants.KEY_NEWOBJECT, measureHandle );
					request.setLocation( location.getPoint( ) );
					
					Command command = targetPart.getCommand( request );
					if ( command != null && command.canExecute( ) )
					{
						targetPart.getViewer( )
							.getEditDomain( )
							.getCommandStack( )
							.execute( command );
						
						if ( crosstab != null )
						{
							AggregationCellProviderWrapper providerWrapper = new AggregationCellProviderWrapper( crosstab );
							providerWrapper.updateAllAggregationCells( AggregationCellViewAdapter.SWITCH_VIEW_TYPE );
							
							if (crosstab.getDimensionCount( ICrosstabConstants.COLUMN_AXIS_TYPE ) != 0)
							{
								DimensionViewHandle viewHnadle = crosstab.getDimension( ICrosstabConstants.COLUMN_AXIS_TYPE, 
										crosstab.getDimensionCount( ICrosstabConstants.COLUMN_AXIS_TYPE ) - 1 );
								CrosstabUtil.addLabelToHeader( viewHnadle.getLevel( viewHnadle.getLevelCount( ) - 1 ) );
							}
							
							crosstab.getModuleHandle( ).getCommandStack( ).commit( );
						}
						return true;
					}
					else
					{
						return false;
					}
				}
				// TODO
				//else if (( (IVirtualValidator) targetPart.getModel( ) ).handleValidate( dimensionHandle ))
				else if (handleValidate(targetPart, dimensionHandle))
				{
					// drop as dimension
					
					CreateRequest request = new CreateRequest( );
					request.getExtendedData( ).put( DesignerConstants.KEY_NEWOBJECT, dimensionHandle );
					request.setLocation( location.getPoint( ) );
					
					Command command = targetPart.getCommand( request );
					if ( command != null && command.canExecute( ) )
					{
						targetPart.getViewer( )
							.getEditDomain( )
							.getCommandStack( )
							.execute( command );
						
						if (crosstab != null)
						{
							AggregationCellProviderWrapper providerWrapper = new AggregationCellProviderWrapper( crosstab );
							providerWrapper.updateAllAggregationCells( AggregationCellViewAdapter.SWITCH_VIEW_TYPE );
							
							crosstab.getModuleHandle( ).getCommandStack( ).commit( );
						}
						return true;
					}
					else
					{
						return false;
					}
				}
			}
		}

		return false;
	}

	private DesignElementHandle getExtendedItemHandle( Object target )
	{
		if ( target instanceof CrosstabTableEditPart )
			return (DesignElementHandle) ( (CrosstabTableEditPart) target ).getModel( );
		if ( target instanceof EditPart )
		{
			EditPart part = (EditPart) target;
			DesignElementHandle handle = (DesignElementHandle) ( (IAdaptable) target ).getAdapter( DesignElementHandle.class );
			if ( handle == null && part.getParent( ) != null )
				return getExtendedItemHandle( part.getParent( ) );

		}
		return null;
	}

	private CrosstabReportItemHandle getCrosstab( EditPart editPart )
	{
		CrosstabReportItemHandle crosstab = null;
		Object tmp = editPart.getModel( );
		if ( !( tmp instanceof CrosstabCellAdapter ) )
		{
			return null;
		}
		if ( tmp instanceof VirtualCrosstabCellAdapter )
		{
			return ( (VirtualCrosstabCellAdapter) tmp ).getCrosstabReportItemHandle( );
		}

		CrosstabCellHandle handle = ( (CrosstabCellAdapter) tmp ).getCrosstabCellHandle( );
		if ( handle != null )
		{
			crosstab = handle.getCrosstab( );
		}

		return crosstab;

	}
	
	private boolean handleValidate(EditPart target, ReportElementHandle handle)
	{
		if (target.getModel( ) instanceof VirtualCrosstabCellAdapter)
		{
			VirtualCrosstabCellAdapter cellAdapter = (VirtualCrosstabCellAdapter) target.getModel( );
			int type = cellAdapter.getType( );
			
			if ( handle instanceof DimensionHandle 
					&& ((type == cellAdapter.ROW_TYPE || type == cellAdapter.COLUMN_TYPE)))
			{
				return canContain( getCrosstab(target), (DimensionHandle) handle );
			}
			else if (handle instanceof MeasureHandle
					&& (type == cellAdapter.MEASURE_TYPE))
			{
				return canContain( getCrosstab(target), (MeasureHandle) handle );
			}
		}
		
		return false;
	}
	
	private boolean canContain( CrosstabReportItemHandle crosstab,
			DimensionHandle dimension )
	{
		if ( crosstab != null
				&& crosstab.getModelHandle( ).getExtends( ) != null )
			return false;

		if ( crosstab != null && dimension != null )
		{
			CubeHandle currentCube = crosstab.getCube( );

			if ( currentCube == null )
			{
				return true;
			}

			// check containment consistency
			if (adapter != null && adapter.contains( currentCube, transfer ))
			{
				for ( int i = 0; i < crosstab.getDimensionCount( ICrosstabConstants.ROW_AXIS_TYPE ); i++ )
				{
					DimensionViewHandle dv = crosstab.getDimension( ICrosstabConstants.ROW_AXIS_TYPE,
							i );

					if ( dv.getCubeDimension( ) == dimension )
					{
						return false;
					}
				}

				for ( int i = 0; i < crosstab.getDimensionCount( ICrosstabConstants.COLUMN_AXIS_TYPE ); i++ )
				{
					DimensionViewHandle dv = crosstab.getDimension( ICrosstabConstants.COLUMN_AXIS_TYPE,
							i );

					if ( dv.getCubeDimension( ) == dimension )
					{
						return false;
					}
				}

				return true;
			}

		}

		return false;
	}
	
	private boolean canContain( CrosstabReportItemHandle crosstab,
			MeasureHandle measure )
	{
		if ( crosstab != null
				&& crosstab.getModelHandle( ).getExtends( ) != null )
			return false;

		if ( crosstab != null && measure != null )
		{
			CubeHandle currentCube = crosstab.getCube( );

			if ( currentCube == null )
			{
				return true;
			}

			// check containment consistency
			if (adapter != null && adapter.contains( currentCube, transfer ))
			{
				for ( int i = 0; i < crosstab.getMeasureCount( ); i++ )
				{
					MeasureViewHandle mv = crosstab.getMeasure( i );

					if ( mv.getCubeMeasure( ) == measure )
					{
						return false;
					}
				}

				return true;
			}

		}

		return false;
	}
}