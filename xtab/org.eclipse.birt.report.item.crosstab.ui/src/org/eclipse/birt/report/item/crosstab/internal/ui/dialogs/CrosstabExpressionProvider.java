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

package org.eclipse.birt.report.item.crosstab.internal.ui.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.ui.cubebuilder.util.BuilderConstants;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.UIHelper;
import org.eclipse.birt.report.designer.ui.dialogs.BindingExpressionProvider;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.swt.graphics.Image;

/**
 *
 */

public class CrosstabExpressionProvider extends BindingExpressionProvider {
	public CrosstabExpressionProvider(DesignElementHandle handle, ComputedColumnHandle computedColumnHandle) {
		super(handle, computedColumnHandle);
		addFilterToProvider();
	}

	protected void addFilterToProvider() {

	}

	@Override
	protected List getChildrenList(Object parent) {
		if (parent instanceof DimensionHandle) {
			List children = new ArrayList();
			try {
				DimensionHandle handle = (DimensionHandle) parent;
				CrosstabReportItemHandle xtabHandle = getCrosstabReportItemHandle();
				for (int i = 0; i < xtabHandle.getDimensionCount(ICrosstabConstants.ROW_AXIS_TYPE); i++) {
					DimensionViewHandle dimensionHandle = xtabHandle.getDimension(ICrosstabConstants.ROW_AXIS_TYPE, i);
					if (dimensionHandle.getCubeDimension().equals(handle)) {
						children.add(dimensionHandle.getLevel(0).getCubeLevel());
					}
				}
				for (int i = 0; i < xtabHandle.getDimensionCount(ICrosstabConstants.COLUMN_AXIS_TYPE); i++) {
					DimensionViewHandle dimensionHandle = xtabHandle.getDimension(ICrosstabConstants.COLUMN_AXIS_TYPE,
							i);
					if (dimensionHandle.getCubeDimension().equals(handle)) {
						children.add(dimensionHandle.getLevel(0).getCubeLevel());
					}
				}
			} catch (ExtendedElementException e) {
			}
			return children;
		} else if (parent instanceof LevelHandle) {
			List children = new ArrayList();
			LevelHandle levelHandle = (LevelHandle) parent;
			try {
				CrosstabReportItemHandle xtabHandle = getCrosstabReportItemHandle();
				for (int i = 0; i < xtabHandle.getDimensionCount(ICrosstabConstants.ROW_AXIS_TYPE); i++) {
					DimensionViewHandle dimensionHandle = xtabHandle.getDimension(ICrosstabConstants.ROW_AXIS_TYPE, i);
					LevelViewHandle levelViewHandle = dimensionHandle.getLevel(levelHandle.getQualifiedName());
					if (levelViewHandle != null) {
						if (dimensionHandle.getLevelCount() > levelViewHandle.getIndex() + 1) {
							children.add(dimensionHandle.getLevel(levelViewHandle.getIndex() + 1).getCubeLevel());
						}
					}
				}
				for (int i = 0; i < xtabHandle.getDimensionCount(ICrosstabConstants.COLUMN_AXIS_TYPE); i++) {
					DimensionViewHandle dimensionHandle = xtabHandle.getDimension(ICrosstabConstants.COLUMN_AXIS_TYPE,
							i);
					LevelViewHandle levelViewHandle = dimensionHandle.getLevel(levelHandle.getQualifiedName());
					if (levelViewHandle != null) {
						if (dimensionHandle.getLevelCount() > levelViewHandle.getIndex() + 1) {
							children.add(dimensionHandle.getLevel(levelViewHandle.getIndex() + 1).getCubeLevel());
						}
					}
				}
			} catch (ExtendedElementException e) {
			}
			return children;
		}
		return super.getChildrenList(parent);
	}

	protected CrosstabReportItemHandle getCrosstabReportItemHandle() throws ExtendedElementException {
		return (CrosstabReportItemHandle) ((ExtendedItemHandle) elementHandle).getReportItem();
	}

	@Override
	public String getDisplayText(Object element) {
		if (element instanceof LevelViewHandle) {
			return ((LevelViewHandle) element).getCubeLevel().getName();
		}
		return super.getDisplayText(element);
	}

	@Override
	public Image getImage(Object element) {
		if (element instanceof LevelViewHandle) {
			return UIHelper.getImage(BuilderConstants.IMAGE_LEVEL);
		}
		return super.getImage(element);
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof LevelHandle) {
			return getChildrenList(element).size() > 0;
		}
		return super.hasChildren(element);
	}

}
