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

package org.eclipse.birt.chart.reportitem.ui.views.attributes.provider;

import org.eclipse.birt.chart.reportitem.ChartReportItemUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.AggregateOnBindingsFormHandleProvider;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ReportItemHandle;

/**
 * The class is used for chart sharing bindings with table/crosstab.
 * <p>
 * This class is deprecated since 2.6, this is not a appropriate implementation.
 * 
 * @since 2.3
 * @deprecated
 */
public class ChartShareBindingsFormHandlerProvider extends AggregateOnBindingsFormHandleProvider {

	/**
	 * @param bShowAggregation
	 */
	public ChartShareBindingsFormHandlerProvider(boolean bShowAggregation) {
		super(bShowAggregation);
	}

	public ChartShareBindingsFormHandlerProvider() {
		super(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.
	 * DataSetColumnBindingsFormHandleProvider#isEditable()
	 */
	public boolean isEditable() {
		if (input == null) {
			return false;
		}

		boolean editable = super.isEditable();

		// Don't allow to edit bindings in chart property page when chart is in
		// multi-views, so return false.
		if (ChartReportItemUtil.isChildOfMultiViewsHandle(((ReportItemHandle) DEUtil.getInputFirstElement(input)))) {
			return false;
		}

		return editable;
	}
}
