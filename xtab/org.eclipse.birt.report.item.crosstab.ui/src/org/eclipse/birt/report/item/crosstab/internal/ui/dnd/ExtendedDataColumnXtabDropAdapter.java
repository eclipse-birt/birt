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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.core.DesignerConstants;
import org.eclipse.birt.report.designer.internal.ui.dnd.DNDLocation;
import org.eclipse.birt.report.designer.internal.ui.dnd.DNDService;
import org.eclipse.birt.report.designer.internal.ui.dnd.IDropAdapter;
import org.eclipse.birt.report.designer.internal.ui.extension.ExtendedDataModelUIAdapterHelper;
import org.eclipse.birt.report.designer.internal.ui.extension.IExtendedDataModelUIAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.DataUtil;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.util.IVirtualValidator;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.item.crosstab.internal.ui.AggregationCellProviderWrapper;
import org.eclipse.birt.report.item.crosstab.internal.ui.dialogs.LevelViewDialog;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabCellAdapter;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.VirtualCrosstabCellAdapter;
import org.eclipse.birt.report.item.crosstab.ui.extension.AggregationCellViewAdapter;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.model.api.ColumnHintHandle;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.jface.window.Window;

public class ExtendedDataColumnXtabDropAdapter implements IDropAdapter {
	private IExtendedDataModelUIAdapter adapter = ExtendedDataModelUIAdapterHelper.getInstance().getAdapter();

	public int canDrop(Object transfer, Object target, int operation, DNDLocation location) {
		if (!(transfer instanceof ReportElementHandle) || adapter == null
				|| !adapter.isExtendedDataItem((ReportElementHandle) transfer)) {
			return DNDService.LOGIC_UNKNOW;
		}
		if (target instanceof EditPart) {
			EditPart editPart = (EditPart) target;
			if (editPart.getModel() instanceof IVirtualValidator) {
				if (handleValidate(editPart, transfer).size() > 0)
					return DNDService.LOGIC_TRUE;
				else
					return DNDService.LOGIC_FALSE;
			}
		}
		return DNDService.LOGIC_UNKNOW;
	}

	public boolean performDrop(Object transfer, Object target, int operation, DNDLocation location) {
		if (target instanceof EditPart) {
			EditPart targetPart = (EditPart) target;

			CrosstabReportItemHandle crosstab = getCrosstab(targetPart);

			if (crosstab == null) {
				return false;
			}

			CommandStack cmdStack = crosstab.getModuleHandle().getCommandStack();
			cmdStack.startTrans(Messages.getFormattedString("ExtendedDataColumnXtabDropAdapter.trans.add", //$NON-NLS-1$
					new String[] { ((ReportElementHandle) transfer).getName() }));

			ReportElementHandle extendedData = adapter
					.getBoundExtendedData((ReportItemHandle) crosstab.getModelHandle());

			if (extendedData == null
					|| !extendedData.equals(adapter.resolveExtendedData((ReportElementHandle) transfer))) {
				if (!adapter.setExtendedData((ReportItemHandle) crosstab.getModelHandle(),
						adapter.resolveExtendedData((ReportElementHandle) transfer))) {
					cmdStack.rollback();
					return false;
				}
			}

			List validElements = handleValidate(targetPart, transfer);
			MeasureHandle measure = null;
			DimensionHandle tabularDimension = null;
			DimensionHandle timeDimension = null;

			ResultSetColumnHandle resultSetColumn = adapter.getResultSetColumn((ReportElementHandle) transfer);
			DataSetHandle dataSetHandle = (DataSetHandle) resultSetColumn.getElementHandle();
			List<ColumnHintHandle> columnHints = DataUtil.getColumnHints(dataSetHandle);
			ColumnHintHandle chh = null;
			for (ColumnHintHandle columnHintHandle : columnHints) {
				if (resultSetColumn.getColumnName().equals(columnHintHandle.getAlias())
						|| resultSetColumn.getColumnName().equals(columnHintHandle.getColumnName())) {
					chh = columnHintHandle;
					break;
				}
			}
			String contentKeyForLabel = null;
			if (chh != null) {
				contentKeyForLabel = chh.getDisplayNameKey();
			}

			for (Object obj : validElements) {
				if (obj instanceof MeasureHandle) {
					measure = (MeasureHandle) obj;
					// Copy the style to measure and then report item in it
					if (chh != null) {
						try {
							measure.setAlignment(chh.getHorizontalAlign());
							measure.setFormat(chh.getValueFormat());
						} catch (SemanticException e) {
							ExceptionHandler.handle(e);
						}
					}
				} else if (obj instanceof DimensionHandle) {
					DimensionHandle dim = (DimensionHandle) obj;

					if (dim.isTimeType()) {
						timeDimension = dim;
					} else {
						tabularDimension = dim;
					}
				}
			}

			if (measure != null) {
				CreateRequest request = new CreateRequest();
				request.getExtendedData().put(DesignerConstants.KEY_NEWOBJECT, measure);
				request.setLocation(location.getPoint());

				Command command = targetPart.getCommand(request);
				if (command != null && command.canExecute()) {
					targetPart.getViewer().getEditDomain().getCommandStack().execute(command);

					// Set content key for label
					MeasureViewHandle measureViewHandle = crosstab.getMeasure(measure.getName());
					PropertyHandle headHandle = measureViewHandle.getHeaderProperty();
					Object object = headHandle.getValue();
					if (object instanceof List && !((List) object).isEmpty()) {
						Object headerCell = ((List) object).get(0);
						if (headerCell instanceof ExtendedItemHandle) {
							Object model = CrosstabUtil.getReportItem((ExtendedItemHandle) headerCell);
							if (model instanceof CrosstabCellHandle) {
								CrosstabCellHandle crosstabCellHandle = (CrosstabCellHandle) model;
								List contentList = crosstabCellHandle.getContents();
								if (!contentList.isEmpty() && contentList.get(0) instanceof LabelHandle) {
									LabelHandle labelHandle = (LabelHandle) contentList.get(0);
									try {
										labelHandle.setTextKey(contentKeyForLabel);
									} catch (Exception e) {
										ExceptionHandler.handle(e);
									}
								}
							}
						}
					}

					AggregationCellProviderWrapper providerWrapper = new AggregationCellProviderWrapper(crosstab);
					providerWrapper.updateAllAggregationCells(AggregationCellViewAdapter.SWITCH_VIEW_TYPE);

					if (crosstab.getDimensionCount(ICrosstabConstants.COLUMN_AXIS_TYPE) != 0) {
						DimensionViewHandle viewHnadle = crosstab.getDimension(ICrosstabConstants.COLUMN_AXIS_TYPE,
								crosstab.getDimensionCount(ICrosstabConstants.COLUMN_AXIS_TYPE) - 1);
						CrosstabUtil.addLabelToHeader(viewHnadle.getLevel(viewHnadle.getLevelCount() - 1));
					}

					cmdStack.commit();

					return true;
				}
			} else if (tabularDimension != null || timeDimension != null) {
				Object element = null;

				if (timeDimension != null) {
					LevelViewDialog dialog = new LevelViewDialog(false);
					dialog.setInput(timeDimension, adapter.getLevelHints(timeDimension));

					if (dialog.open() != Window.OK) {
						cmdStack.rollback();
						return false;
					}

					if (((List) dialog.getResult()).size() > 0) {
						element = ((List) dialog.getResult()).toArray();
					} else {
						element = tabularDimension;
					}
				} else if (tabularDimension != null) {
					element = tabularDimension;
				}

				if (element == null) {
					return false;
				}

				ReportElementHandle reportElementHandle = null;
				if (element instanceof Object[] && ((Object[]) element).length != 0) {
					reportElementHandle = (ReportElementHandle) ((Object[]) element)[0];
				} else if (element instanceof DimensionHandle) {
					reportElementHandle = (ReportElementHandle) element;
				}
				try {
					String displayNameKey = reportElementHandle.getDisplayNameKey();
					if (displayNameKey == null) {
						reportElementHandle.setDisplayNameKey(contentKeyForLabel);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				CreateRequest request = new CreateRequest();
				request.getExtendedData().put(DesignerConstants.KEY_NEWOBJECT, element);
				request.setLocation(location.getPoint());

				Command command = targetPart.getCommand(request);
				if (command != null && command.canExecute()) {
					targetPart.getViewer().getEditDomain().getCommandStack().execute(command);

					AggregationCellProviderWrapper providerWrapper = new AggregationCellProviderWrapper(crosstab);
					providerWrapper.updateAllAggregationCells(AggregationCellViewAdapter.SWITCH_VIEW_TYPE);

					cmdStack.commit();

					return true;
				}
			}
		}
		return false;
	}

	private CrosstabReportItemHandle getCrosstab(EditPart editPart) {
		CrosstabReportItemHandle crosstab = null;
		Object tmp = editPart.getModel();
		if (!(tmp instanceof CrosstabCellAdapter)) {
			return null;
		}
		if (tmp instanceof VirtualCrosstabCellAdapter) {
			return ((VirtualCrosstabCellAdapter) tmp).getCrosstabReportItemHandle();
		}

		CrosstabCellHandle handle = ((CrosstabCellAdapter) tmp).getCrosstabCellHandle();
		if (handle != null) {
			crosstab = handle.getCrosstab();
		}

		return crosstab;

	}

	private List handleValidate(EditPart editPart, Object transfer) {
		if (!(transfer instanceof ReportElementHandle)) {
			return null;
		}

		ReportElementHandle[] supportedTypes = adapter.getSupportedTypes((ReportElementHandle) transfer,
				getCrosstab(editPart).getCube());

		List list = new ArrayList();

		for (ReportElementHandle type : supportedTypes) {
			if (((IVirtualValidator) editPart.getModel()).handleValidate(type)) {
				list.add(type);
			}
		}

		return list;
	}
}