/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.item.crosstab.internal.ui.views.provider;

import java.util.ArrayList;

import org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabReportItemConstants;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.internal.ui.util.CrosstabUIHelper;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;

/**
 * Group provider class - Populates the Group node menu items and associates to
 * the Action class. - Implements the getChildren method for this node type.
 *
 *
 */
public class CrossTabNodeProvider extends DefaultNodeProvider {

	@Override
	public Object getParent(Object model) {
		return super.getParent(model);
	}

	/**
	 * Gets the children element of the given model using visitor.
	 *
	 * @param model the model
	 */
	@Override
	public Object[] getChildren(Object model) {
		ArrayList list = new ArrayList();
		ExtendedItemHandle crossTabHandle = (ExtendedItemHandle) model;
		try {
			CrosstabReportItemHandle crossTab = (CrosstabReportItemHandle) crossTabHandle.getReportItem();
			CrosstabPropertyHandleWrapper headerWrap = new CrosstabPropertyHandleWrapper(
					crossTabHandle.getPropertyHandle(ICrosstabReportItemConstants.HEADER_PROP));
			headerWrap.setTestType("crosstabHeader");
			list.add(headerWrap);
			if (crossTab.getCrosstabView(ICrosstabConstants.COLUMN_AXIS_TYPE) != null) {
				list.add(crossTab.getCrosstabView(ICrosstabConstants.COLUMN_AXIS_TYPE).getModelHandle());
			} else {
				list.add(new CrosstabPropertyHandleWrapper(
						crossTabHandle.getPropertyHandle(ICrosstabReportItemConstants.COLUMNS_PROP)));
			}
			if (crossTab.getCrosstabView(ICrosstabConstants.ROW_AXIS_TYPE) != null) {
				list.add(crossTab.getCrosstabView(ICrosstabConstants.ROW_AXIS_TYPE).getModelHandle());
			} else {
				list.add(new CrosstabPropertyHandleWrapper(
						crossTabHandle.getPropertyHandle(ICrosstabReportItemConstants.ROWS_PROP)));
			}
		} catch (ExtendedElementException e) {
			ExceptionUtil.handle(e);
		}
		list.add(new CrosstabPropertyHandleWrapper(
				crossTabHandle.getPropertyHandle(ICrosstabReportItemConstants.MEASURES_PROP)));
		return list.toArray();
	}

	@Override
	public Image getNodeIcon(Object model) {
		if (model instanceof DesignElementHandle && ((DesignElementHandle) model).getSemanticErrors().size() > 0) {
			return ReportPlatformUIImages.getImage(ISharedImages.IMG_OBJS_ERROR_TSK);
		}
		// if ( model instanceof DesignElementHandle
		// && DEUtil.isLinkedElement( (DesignElementHandle) model ) )
		// return CrosstabUIHelper.getImage(
		// CrosstabUIHelper.CROSSTAB_LINK_IMAGE );
		return decorateImage(CrosstabUIHelper.getImage(CrosstabUIHelper.CROSSTAB_IMAGE), model);
	}
}
