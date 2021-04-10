/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.item.crosstab.ui.views.attributes.provider;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.AbstractDescriptorProvider;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;

/**
 * @author Administrator
 * 
 */
public class LayoutMeasuresProvider extends AbstractDescriptorProvider {

	protected Object input;
	protected CrosstabReportItemHandle crosstabHandle;
	protected static final Logger logger = Logger.getLogger(LayoutMeasuresProvider.class.getName());

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.
	 * IDescriptorProvider#getDisplayName()
	 */
	public String getDisplayName() {
		return Messages.getString("LayoutMeasuresProvider.DisplayName"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.
	 * IDescriptorProvider#load()
	 */
	public Object load() {
		String vertical = ICrosstabConstants.MEASURE_DIRECTION_VERTICAL;
		if (input == null) {
			return "false"; //$NON-NLS-1$
		} else if (crosstabHandle == null) {
			initializeCrosstab();
		}
		if (crosstabHandle != null) {
			vertical = crosstabHandle.getMeasureDirection();
		}
		return (vertical.equals(ICrosstabConstants.MEASURE_DIRECTION_VERTICAL)) ? "true" //$NON-NLS-1$
				: "false"; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.
	 * IDescriptorProvider#save(java.lang.Object)
	 */
	public void save(Object value) throws SemanticException {
		String stringValue = (String) value;
		if (input == null) {
			return;
		} else if (crosstabHandle == null) {
			initializeCrosstab();
		}
		if (stringValue != null && stringValue.equalsIgnoreCase("true")) { //$NON-NLS-1$
			crosstabHandle.setMeasureDirection(ICrosstabConstants.MEASURE_DIRECTION_VERTICAL);

		} else // false;
		{
			crosstabHandle.setMeasureDirection(ICrosstabConstants.MEASURE_DIRECTION_HORIZONTAL);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.
	 * IDescriptorProvider#setInput(java.lang.Object)
	 */
	public void setInput(Object input) {
		// TODO Auto-generated method stub
		this.input = input;
		initializeCrosstab();
	}

	protected void initializeCrosstab() {
		crosstabHandle = null;
		if ((input == null)) {
			return;
		}

		if ((!(input instanceof List && ((List) input).size() > 0
				&& ((List) input).get(0) instanceof ExtendedItemHandle)) && (!(input instanceof ExtendedItemHandle))) {
			return;
		}

		ExtendedItemHandle handle;
		if (((List) input).size() > 0) {
			handle = (ExtendedItemHandle) (((List) input).get(0));
		} else // input instanceof ExtendedItemHandle
		{
			handle = (ExtendedItemHandle) input;
		}

		try {
			crosstabHandle = (CrosstabReportItemHandle) handle.getReportItem();
			return;
		} catch (ExtendedElementException e) {
			// TODO Auto-generated catch block
			logger.log(Level.SEVERE, e.getMessage(), e);
			return;
		}
	}
}
