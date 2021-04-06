/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.item.crosstab.internal.ui.editors.action;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.dialogs.DataColumnBindingDialog;
import org.eclipse.birt.report.designer.internal.ui.views.actions.AbstractViewAction;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.ComputedMeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabAdaptUtil;
import org.eclipse.birt.report.item.crosstab.internal.ui.util.CrosstabUIHelper;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.elements.interfaces.ICubeModel;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

/**
 * 
 */

public class AddRelativeTimePeriodAction extends AbstractViewAction {

	public static final String ID = "com.actuate.birt.report.designer.internal.ui.croostab.AddRelativeTimePeriodAction"; //$NON-NLS-1$
	private static final double DEFAULT_COLUMN_WIDTH = 1.0;
	private static final String ICON = "/icons/obj16/relativetime.gif"; //$NON-NLS-1$
	private MeasureViewHandle measureViewHandle;
	private CrosstabReportItemHandle reportHandle;

	public AddRelativeTimePeriodAction(Object selectedObject) {
		super(selectedObject);
		setId(ID);
		setText(Messages.getString("AddRelativeTimePeriodAction_action_label")); //$NON-NLS-1$
		Image image = CrosstabUIHelper.getImage(CrosstabUIHelper.ADD_RELATIVETIMEPERIOD);
		setImageDescriptor(ImageDescriptor.createFromImage(image));
	}

	public void run() {
		if (measureViewHandle != null) {
			reportHandle = measureViewHandle.getCrosstab();
		}
		reportHandle.getModuleHandle().getCommandStack()
				.startTrans(Messages.getString("AddRelativeTimePeriodAction_trans_label")); //$NON-NLS-1$
		// AddRelativeTimePeriodDialog computedSummaryDialog = new
		// AddRelativeTimePeriodDialog(UIUtil.getDefaultShell( ),
		// "Add Relative TimeP eriod");
		// computedSummaryDialog.setBindingHolder(
		// (ReportItemHandle)reportHandle.getModelHandle( ) );
		// String measureName = "TempName";
		DataColumnBindingDialog dialog = new DataColumnBindingDialog(true);
		dialog.setInput((ReportItemHandle) reportHandle.getModelHandle());
		dialog.setAggreate(true);
		dialog.setTimePeriod(true);
		if (dialog.open() == Dialog.OK) {
			int index = caleIndex();

			try {
				ComputedColumnHandle bindingHandle = dialog.getBindingColumn();
				ComputedMeasureViewHandle computedMeasure = reportHandle.insertComputedMeasure(bindingHandle.getName(),
						index);
				computedMeasure.addHeader();

				ExtendedItemHandle crosstabModelHandle = (ExtendedItemHandle) reportHandle.getModelHandle();

				if (bindingHandle == null) {
					reportHandle.getModuleHandle().getCommandStack().rollbackAll();
					return;
				}

				DataItemHandle dataHandle = DesignElementFactory.getInstance().newDataItem(bindingHandle.getName());
				CrosstabAdaptUtil.formatDataItem(computedMeasure.getCubeMeasure(), dataHandle);
				dataHandle.setResultSetColumn(bindingHandle.getName());

				AggregationCellHandle cell = computedMeasure.getCell();

				// There must a set a value to the column
				if (ICrosstabConstants.MEASURE_DIRECTION_HORIZONTAL.equals(reportHandle.getMeasureDirection())) {
					CrosstabCellHandle cellHandle = computedMeasure.getHeader();
					if (cellHandle == null) {
						cellHandle = cell;
					}
					String defaultUnit = reportHandle.getModelHandle().getModuleHandle().getDefaultUnits();
					// DimensionValue dimensionValue = DimensionUtil.convertTo(
					// DEFAULT_COLUMN_WIDTH, DesignChoiceConstants.UNITS_IN,
					// defaultUnit );
					// reportHandle.setColumnWidth( cellHandle,
					// dimensionValue );
				}
				cell.addContent(dataHandle);

				reportHandle.getModuleHandle().getCommandStack().commit();
			} catch (SemanticException e) {
				reportHandle.getModuleHandle().getCommandStack().rollbackAll();
				return;
			}
		} else {
			reportHandle.getModuleHandle().getCommandStack().rollbackAll();
		}
	}

	@Override
	public boolean isEnabled() {
		Object selection = getSelection();
		if (selection == null || !(selection instanceof DesignElementHandle)) {
			return false;
		}
		ExtendedItemHandle extendedHandle = CrosstabAdaptUtil.getExtendedItemHandle((DesignElementHandle) selection);
		if (extendedHandle == null) {
			return false;
		}
		if (extendedHandle.getExtensionName().equals("Crosstab")) {
			try {
				reportHandle = (CrosstabReportItemHandle) extendedHandle.getReportItem();
			} catch (ExtendedElementException e) {
				return false;
			}
		} else {
			measureViewHandle = CrosstabAdaptUtil.getMeasureViewHandle(extendedHandle);
			if (measureViewHandle == null) {
				return false;
			}
			reportHandle = measureViewHandle.getCrosstab();
		}
		if (DEUtil.isReferenceElement(reportHandle.getCrosstabHandle())) {
			return false;
		}

		CubeHandle cube = reportHandle.getCube();
		if (cube == null) {
			return false;
		}
		if (cube.getPropertyHandle(ICubeModel.DIMENSIONS_PROP) == null) {
			return false;
		}
		List list = cube.getContents(ICubeModel.DIMENSIONS_PROP);
		if (CrosstabUtil.isBoundToLinkedDataSet(reportHandle)) {
			list = new ArrayList();
			for (int i = 0; i < reportHandle.getDimensionCount(ICrosstabConstants.ROW_AXIS_TYPE); i++) {
				DimensionHandle dimension = reportHandle.getDimension(ICrosstabConstants.ROW_AXIS_TYPE, i)
						.getCubeDimension();
				list.add(dimension);
			}
			for (int i = 0; i < reportHandle.getDimensionCount(ICrosstabConstants.COLUMN_AXIS_TYPE); i++) {
				DimensionHandle dimension = reportHandle.getDimension(ICrosstabConstants.COLUMN_AXIS_TYPE, i)
						.getCubeDimension();
				list.add(dimension);
			}
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
					if (DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_YEAR.equals(levelHandle.getDateTimeLevelType())) {
						return true;
					}
				} else {
					int count = viewHandle.getLevelCount();
					if (count == 0) {
						continue;
					}
					LevelViewHandle levelViewHandle = viewHandle.getLevel(0);
					if (levelViewHandle.getCubeLevel() != null && DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_YEAR
							.equals(levelViewHandle.getCubeLevel().getDateTimeLevelType())) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private int caleIndex() {
		if (measureViewHandle != null) {
			return reportHandle.getAllMeasures().indexOf(measureViewHandle) + 1;
		} else {
			return reportHandle.getAllMeasures().size();
		}
	}

}
