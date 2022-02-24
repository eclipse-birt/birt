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

package org.eclipse.birt.report.item.crosstab.internal.ui.editors.action;

import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.item.crosstab.core.de.ComputedMeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.internal.ui.AggregationCellProviderWrapper;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabAdaptUtil;
import org.eclipse.birt.report.item.crosstab.ui.extension.IAggregationCellViewProvider;
import org.eclipse.birt.report.item.crosstab.ui.extension.SwitchCellInfo;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;

/**
 * 
 */

public class ShowAsViewMenuAction extends AbstractCrosstabAction {

	/** action ID */
	public static final String ID = "org.eclipse.birt.report.item.crosstab.internal.ui.editors.action.ShowAsViewAction"; //$NON-NLS-1$

	private MeasureViewHandle measureViewHandle;
	private AggregationCellProviderWrapper providerWrapper;
	/**
	 * Trans name
	 */
	// private static final String NAME = "Add measure handle";
	public static final String NAME = Messages.getString("ShowAsViewAction.DisplayName");//$NON-NLS-1$
	private static final String ACTION_MSG_MERGE = Messages.getString("ShowAsViewAction.TransName");//$NON-NLS-1$
	private final String expectedView;
	private final String expectedViewDisplayName;

	public ShowAsViewMenuAction(DesignElementHandle handle, String expectedView, int index) {
		super(handle);
		setId(ID);
		ExtendedItemHandle extendedHandle = CrosstabAdaptUtil.getExtendedItemHandle(handle);
		setHandle(extendedHandle);
		measureViewHandle = CrosstabAdaptUtil.getMeasureViewHandle(extendedHandle);
		providerWrapper = new AggregationCellProviderWrapper(measureViewHandle.getCrosstab());
		expectedViewDisplayName = providerWrapper.getViewDisplayName(expectedView);
		setText("&" + index + " " + NAME + " " + expectedViewDisplayName); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		this.expectedView = expectedView;
	}

	/*
	 * (non-Javadoc) Method declared on IAction.
	 */
	public boolean isEnabled() {
		boolean enabled = true;

		if (measureViewHandle instanceof ComputedMeasureViewHandle) {
			enabled = false;
		} else {
			if (DEUtil.isLinkedElement(measureViewHandle.getCrosstabHandle())) {
				return false;
			}

			IAggregationCellViewProvider provider = providerWrapper.getProvider(expectedView);
			SwitchCellInfo info = new SwitchCellInfo(measureViewHandle.getCrosstab(), SwitchCellInfo.MEASURE);
			info.setMeasureInfo(true, measureViewHandle.getCubeMeasureName(), expectedView);
			enabled = provider.canSwitch(info);

			IAggregationCellViewProvider matchProvider = providerWrapper.getMatchProvider(measureViewHandle.getCell());
			if (matchProvider != null && matchProvider.getViewName().equals(expectedView)) {
				enabled = false;
			}
		}

		setEnabled(enabled);
		return enabled;
	}

	public void run() {
		// do nothing
		transStar(ACTION_MSG_MERGE + " " + expectedViewDisplayName);
		// providerWrapper.switchView( viewName, measureViewHandle.getCell( ) );
		SwitchCellInfo info = new SwitchCellInfo(measureViewHandle.getCrosstab(), SwitchCellInfo.MEASURE);
		info.setMeasureInfo(true, measureViewHandle.getCubeMeasureName(), expectedView);
		providerWrapper.switchView(info);
		transEnd();
	}

}
