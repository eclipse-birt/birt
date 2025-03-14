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

package org.eclipse.birt.report.item.crosstab.internal.ui.views.provider;

import java.util.logging.Level;

import org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.internal.ui.util.CrosstabUIHelper;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;

public class CrossTabLevelNodeProvider extends DefaultNodeProvider {

	@Override
	public Object[] getChildren(Object model) {
		ExtendedItemHandle element = (ExtendedItemHandle) model;
		try {
			LevelViewHandle levelView = (LevelViewHandle) element.getReportItem();
			if (levelView != null) {
				if (levelView.getAggregationHeader() != null
						&& levelView.getAggregationHeader().getModelHandle() != null) {
					return new Object[] { levelView.getAggregationHeader().getModelHandle(),
							levelView.getCell().getModelHandle() };
				} else {
					return new Object[] { levelView.getCell().getModelHandle() };
				}
			}

		} catch (ExtendedElementException e) {
		}
		return new Object[0];
	}

	@Override
	public void createContextMenu(TreeViewer sourceViewer, Object object, IMenuManager menu) {
		// do nothing

	}

	@Override
	public Object getParent(Object model) {
		ExtendedItemHandle element = (ExtendedItemHandle) model;
		try {
			LevelViewHandle levelView = (LevelViewHandle) element.getReportItem();
			if (levelView.getContainer() != null) {
				DimensionViewHandle dimension = (DimensionViewHandle) levelView.getContainer();
				if (dimension.getContainer() != null) {
					return dimension.getContainer().getModelHandle();
				}
			}
		} catch (ExtendedElementException e) {
		}
		return null;
	}

	@Override
	public boolean hasChildren(Object model) {
		return getChildren(model).length != 0;
	}

	@Override
	public String getNodeDisplayName(Object model) {
		ExtendedItemHandle element = (ExtendedItemHandle) model;
		try {
			LevelHandle level = ((LevelViewHandle) element.getReportItem()).getCubeLevel();
			String levelName = ""; //$NON-NLS-1$
			if (level != null) {
				levelName = level.getName();
			}
			return Messages.getString("CrossTabLevelNodeProvider.Level") + levelName; //$NON-NLS-1$
		} catch (ExtendedElementException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
		return super.getNodeDisplayName(model);
	}

	@Override
	public Image getNodeIcon(Object element) {
		if (element instanceof DesignElementHandle && ((DesignElementHandle) element).getSemanticErrors().size() > 0) {
			return ReportPlatformUIImages.getImage(ISharedImages.IMG_OBJS_ERROR_TSK);
		}
		return CrosstabUIHelper.getImage(CrosstabUIHelper.LEVEL_IMAGE);
	}
}