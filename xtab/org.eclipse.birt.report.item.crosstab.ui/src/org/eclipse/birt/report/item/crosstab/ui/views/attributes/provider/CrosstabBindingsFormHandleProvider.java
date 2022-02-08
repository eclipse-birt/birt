/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.item.crosstab.ui.views.attributes.provider;

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.AggregateOnBindingsFormHandleProvider;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.internal.CrosstabModelUtil;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabAdaptUtil;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.TabularDimensionHandle;

public class CrosstabBindingsFormHandleProvider extends AggregateOnBindingsFormHandleProvider {

	public CrosstabBindingsFormHandleProvider() {
		super();
	}

	public CrosstabBindingsFormHandleProvider(boolean bShowAggregation) {
		super(bShowAggregation);
	}

	private ExtendedItemHandle getExtendedItemHandle() {
		return (ExtendedItemHandle) DEUtil.getInputFirstElement(input);
	}

	public void generateAllBindingColumns() {
		CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
		stack.startTrans(Messages.getString("CrosstabBindingRefresh.action.message")); //$NON-NLS-1$
		try {
			ExtendedItemHandle handle = getExtendedItemHandle();
			CrosstabReportItemHandle crosstab = (CrosstabReportItemHandle) handle.getReportItem();
			if (handle.getCube() != null) {
				CubeHandle cube = getExtendedItemHandle().getCube();
				List dimensions = cube.getContents(CubeHandle.DIMENSIONS_PROP);
				for (Iterator iterator = dimensions.iterator(); iterator.hasNext();) {
					DimensionHandle dimension = (DimensionHandle) iterator.next();
					// only generate used dimension
					if (!isUsedDimension(crosstab, dimension)) {
						continue;
					}
					if (dimension instanceof TabularDimensionHandle && !dimension.isTimeType()) {
						generateDimensionBindings(handle, dimension, ICrosstabConstants.ROW_AXIS_TYPE);
					} else {
						generateDimensionBindings(handle, dimension, ICrosstabConstants.COLUMN_AXIS_TYPE);
					}
				}

				for (int i = 0; i < crosstab.getMeasureCount(); i++) {
					MeasureViewHandle measureView = crosstab.getMeasure(i);

					String function = CrosstabModelUtil.getAggregationFunction(crosstab, measureView.getCell());

					LevelHandle rowLevel = measureView.getCell().getAggregationOnRow();
					LevelHandle colLevel = measureView.getCell().getAggregationOnColumn();

					String aggregateRowName = rowLevel == null ? null : rowLevel.getQualifiedName();
					String aggregateColumnName = colLevel == null ? null : colLevel.getQualifiedName();

					CrosstabModelUtil.generateAggregation(crosstab, measureView.getCell(), measureView, function, null,
							aggregateRowName, null, aggregateColumnName);
				}
			}
			stack.commit();
		} catch (SemanticException e) {
			stack.rollback();
			ExceptionHandler.handle(e);
		}
	}

	private boolean isUsedDimension(CrosstabReportItemHandle crosstab, DimensionHandle dimension) {
		boolean result = true;
		DimensionViewHandle viewHandle = crosstab.getDimension(dimension.getName());
		if (viewHandle == null) {
			result = false;
		}
		return result;
	}

	private void generateDimensionBindings(ExtendedItemHandle handle, DimensionHandle dimensionHandle, int type)
			throws SemanticException {
		if (dimensionHandle.getDefaultHierarchy().getLevelCount() > 0) {
			IReportItem reportItem = handle.getReportItem();
			CrosstabReportItemHandle xtabHandle = (CrosstabReportItemHandle) reportItem;
			LevelHandle[] levels = getLevelHandles(dimensionHandle);
			for (int j = 0; j < levels.length; j++) {
				// only generate used
				if (!isUsedLevelHandle(xtabHandle, levels[j])) {
					continue;
				}
				CrosstabAdaptUtil.createColumnBinding((ExtendedItemHandle) xtabHandle.getModelHandle(), levels[j]);
			}
		}
	}

	private boolean isUsedLevelHandle(CrosstabReportItemHandle xtabHandle, LevelHandle levelHandle) {
		boolean result = true;
		LevelViewHandle viewHandle = xtabHandle.getLevel(levelHandle.getFullName());
		if (viewHandle == null) {
			result = false;
		}
		return result;
	}

	private LevelHandle[] getLevelHandles(DimensionHandle dimensionHandle) {
		LevelHandle[] dimensionLevelHandles = new LevelHandle[dimensionHandle.getDefaultHierarchy().getLevelCount()];
		for (int i = 0; i < dimensionLevelHandles.length; i++) {
			dimensionLevelHandles[i] = dimensionHandle.getDefaultHierarchy().getLevel(i);
		}
		return dimensionLevelHandles;
	}
}
