/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.item.crosstab.internal.ui;

import java.util.List;

import org.eclipse.birt.report.designer.core.DesignerConstants;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.dialogs.DataColumnBindingDialog;
import org.eclipse.birt.report.designer.internal.ui.dnd.DNDLocation;
import org.eclipse.birt.report.designer.internal.ui.dnd.DNDService;
import org.eclipse.birt.report.designer.internal.ui.dnd.IDropAdapter;
import org.eclipse.birt.report.designer.internal.ui.palette.DesignerPaletteFactory;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.ComputedMeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.editparts.CrosstabCellEditPart;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.editparts.VirtualCellEditPart;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabAdaptUtil;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabCellAdapter;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.ICrosstabCellAdapterFactory;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.VirtualCrosstabCellAdapter;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.jface.window.Window;

/**
 * AggregationDropAdapter
 */
public class AggregationDropAdapter implements IDropAdapter {

	public int canDrop(Object transfer, Object target, int operation, DNDLocation location) {
		if ((transfer.equals(DesignerPaletteFactory.AGG_TEMPLATE) // $NON-NLS-1$
				|| transfer.equals(DesignerPaletteFactory.TIMEPERIOD_TEMPLATE)) // $NON-NLS-1$
				&& target instanceof CrosstabCellEditPart) {
			CrosstabCellAdapter adapter = (CrosstabCellAdapter) ((CrosstabCellEditPart) target).getModel();
			if (adapter.getCrosstabCellHandle() != null
					&& DEUtil.isReferenceElement(adapter.getCrosstabCellHandle().getCrosstabHandle()))
				return DNDService.LOGIC_FALSE;

			String posType = adapter.getPositionType();

			if (ICrosstabCellAdapterFactory.CELL_MEASURE_AGGREGATION.equals(posType)
					|| ICrosstabCellAdapterFactory.CELL_MEASURE.equals(posType)
					|| (ICrosstabCellAdapterFactory.CELL_MEASURE_VIRTUAL.equals(posType)
							&& transfer.equals(DesignerPaletteFactory.TIMEPERIOD_TEMPLATE))) {
				if (transfer.equals(DesignerPaletteFactory.TIMEPERIOD_TEMPLATE)) {
					CrosstabReportItemHandle reportHandle = (CrosstabReportItemHandle) CrosstabUtil.getReportItem(
							(DesignElementHandle) ((CrosstabCellEditPart) target).getParent().getModel());

					if (DEUtil.isReferenceElement(reportHandle.getCrosstabHandle())) {
						return DNDService.LOGIC_FALSE;
					}
					CubeHandle cube = reportHandle.getCube();
					if (cube == null) {
						return DNDService.LOGIC_FALSE;
					}
					List list = cube.getContents(CubeHandle.DIMENSIONS_PROP);
					if (list == null) {
						return DNDService.LOGIC_FALSE;
					}
					for (int i = 0; i < list.size(); i++) {
						DimensionHandle dimension = (DimensionHandle) list.get(i);
						if (CrosstabAdaptUtil.isTimeDimension(dimension)) {
							DimensionViewHandle viewHandle = reportHandle.getDimension(dimension.getName());
							if (viewHandle == null) {
								int count = dimension.getDefaultHierarchy().getLevelCount();
								if (count == 0) {
									continue;
								}
								LevelHandle levelHandle = dimension.getDefaultHierarchy().getLevel(0);
								if (DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_YEAR
										.equals(levelHandle.getDateTimeLevelType())) {
									return DNDService.LOGIC_TRUE;
								}
							} else {
								int count = viewHandle.getLevelCount();
								if (count == 0) {
									continue;
								}
								LevelViewHandle levelViewHandle = viewHandle.getLevel(0);
								if (DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_YEAR
										.equals(levelViewHandle.getCubeLevel().getDateTimeLevelType())) {
									return DNDService.LOGIC_TRUE;
								}
							}
						}
					}

					return DNDService.LOGIC_FALSE;
				} else {
					return DNDService.LOGIC_TRUE;
				}
				// return DNDService.LOGIC_TRUE;
			}
		}
		return DNDService.LOGIC_UNKNOW;
	}

	public boolean performDrop(Object transfer, Object target, int operation, DNDLocation location) {

		if (target instanceof EditPart) {
			EditPart editPart = (EditPart) target;

			CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
			if (DesignerPaletteFactory.TIMEPERIOD_TEMPLATE.equals(transfer)) {
				stack.startTrans("Add TimePeriod"); //$NON-NLS-1$
			} else {
				stack.startTrans("Add Aggregation"); //$NON-NLS-1$
			}

			DataItemHandle dataHandle = DesignElementFactory.getInstance().newDataItem(null);

			CrosstabCellHandle cellHandle = null;
			CrosstabReportItemHandle itemHandle = null;
			if (target instanceof VirtualCellEditPart) {
				itemHandle = ((VirtualCrosstabCellAdapter) ((VirtualCellEditPart) target).getModel())
						.getCrosstabReportItemHandle();
			} else if (target instanceof CrosstabCellEditPart) {
				cellHandle = ((CrosstabCellAdapter) ((CrosstabCellEditPart) target).getModel()).getCrosstabCellHandle();
			}

			try {
				DataColumnBindingDialog dialog = new DataColumnBindingDialog(true);

				if (target instanceof VirtualCellEditPart) {
					dialog.setInput((ReportItemHandle) itemHandle.getModelHandle());
				} else if (target instanceof CrosstabCellEditPart) {
					cellHandle.addContent(dataHandle, CellHandle.CONTENT_SLOT);
					dialog.setInput(dataHandle, null, cellHandle);
				}

				dialog.setAggreate(true);
				if (DesignerPaletteFactory.TIMEPERIOD_TEMPLATE.equals(transfer)) {
					dialog.setTimePeriod(true);
				}
				if (dialog.open() == Window.OK) {
					if (target instanceof VirtualCellEditPart) {
						ComputedColumnHandle bindingHandle = dialog.getBindingColumn();
						ComputedMeasureViewHandle computedMeasure = itemHandle.insertComputedMeasure(
								bindingHandle.getName(), itemHandle.getComputedMeasures().size());
						computedMeasure.addHeader();

						ExtendedItemHandle crosstabModelHandle = (ExtendedItemHandle) itemHandle.getModelHandle();

						if (bindingHandle == null) {
							stack.rollback();
						}

						DataItemHandle dataItemHandle = DesignElementFactory.getInstance()
								.newDataItem(bindingHandle.getName());
						CrosstabAdaptUtil.formatDataItem(computedMeasure.getCubeMeasure(), dataItemHandle);
						dataItemHandle.setResultSetColumn(bindingHandle.getName());

						AggregationCellHandle cell = computedMeasure.getCell();

						// There must a set a value to the column
						if (ICrosstabConstants.MEASURE_DIRECTION_HORIZONTAL.equals(itemHandle.getMeasureDirection())) {
							CrosstabCellHandle crosstabCellHandle = computedMeasure.getHeader();
							if (crosstabCellHandle == null) {
								crosstabCellHandle = cell;
							}
							String defaultUnit = itemHandle.getModelHandle().getModuleHandle().getDefaultUnits();
						}
						cell.addContent(dataItemHandle);

						stack.commit();
					} else if (target instanceof CrosstabCellEditPart) {
						CreateRequest request = new CreateRequest();

						request.getExtendedData().put(DesignerConstants.KEY_NEWOBJECT, dataHandle);
						request.setLocation(location.getPoint());

						Command command = editPart.getCommand(request);
						if (command != null && command.canExecute()) {
							dataHandle.setResultSetColumn(dialog.getBindingColumn().getName());

							editPart.getViewer().getEditDomain().getCommandStack().execute(command);

							stack.commit();
						} else {
							stack.rollback();
						}
					}
				} else {
					stack.rollback();
				}
			} catch (Exception e) {
				stack.rollback();
				ExceptionUtil.handle(e);
			}
		}
		return true;
	}

	// public boolean performDrop( Object transfer, Object target, int
	// operation,
	// DNDLocation location )
	// {
	//
	// if(target instanceof EditPart)
	// {
	// EditPart editPart = (EditPart)target;
	//
	// CommandStack stack = SessionHandleAdapter.getInstance( )
	// .getCommandStack( );
	// stack.startTrans( "Add Aggregation" ); //$NON-NLS-1$
	//
	// DataItemHandle dataHandle = DesignElementFactory.getInstance( )
	// .newDataItem( null );
	//
	// CrosstabCellHandle cellHandle = ( (CrosstabCellAdapter) (
	// (CrosstabCellEditPart) target ).getModel( ) ).getCrosstabCellHandle( );
	// try
	// {
	// cellHandle.addContent( dataHandle, CellHandle.CONTENT_SLOT );
	//
	// DataColumnBindingDialog dialog = new DataColumnBindingDialog( true );
	// dialog.setInput( dataHandle, null, cellHandle );
	// dialog.setAggreate( true );
	//
	// if ( dialog.open( ) == Window.OK )
	// {
	// cellHandle.getModelHandle( ).getPropertyHandle(
	// ICrosstabCellConstants.CONTENT_PROP ).removeItem( dataHandle );
	// CreateRequest request = new CreateRequest( );
	//
	// request.getExtendedData( )
	// .put( DesignerConstants.KEY_NEWOBJECT, dataHandle );
	// request.setLocation( location.getPoint( ) );
	//
	// Command command = editPart.getCommand( request );
	// if ( command != null && command.canExecute( ) )
	// {
	// dataHandle.setResultSetColumn( dialog.getBindingColumn( )
	// .getName( ) );
	//
	// editPart.getViewer( )
	// .getEditDomain( )
	// .getCommandStack( )
	// .execute( command );
	//
	// stack.commit( );
	// }else
	// {
	// stack.rollback( );
	// }
	//
	// }
	// else
	// {
	// stack.rollback( );
	// }
	// }
	// catch ( Exception e )
	// {
	// stack.rollback( );
	// ExceptionHandler.handle( e );
	// }
	// }
	// return true;
	// }

}
