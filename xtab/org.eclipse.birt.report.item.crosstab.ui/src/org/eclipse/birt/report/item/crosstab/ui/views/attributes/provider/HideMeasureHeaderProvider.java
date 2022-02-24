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

package org.eclipse.birt.report.item.crosstab.ui.views.attributes.provider;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.PropertyDescriptorProvider;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;

/**
 * @author Administrator
 *
 */
public class HideMeasureHeaderProvider extends PropertyDescriptorProvider {
	protected CrosstabReportItemHandle crosstabHandle;
	protected final String TRANS_NAME = "Change Crosstab Hidemeasure";
	protected static final Logger logger = Logger.getLogger(HideMeasureHeaderProvider.class.getName());

	/**
	 * @param property
	 * @param element
	 */
	public HideMeasureHeaderProvider(String property, String element) {
		super(property, element);
		// TODO Auto-generated constructor stub
	}

	public String getDisplayName() {
		String displayName = super.getDisplayName();
		if (displayName != null && displayName.length() > 0) {
			return displayName;
		}
		return Messages.getString("CrosstabGeneralPage.HideMeasureHeader"); //$NON-NLS-1$
	}

	public void save(Object value) throws SemanticException {

		String stringValue = (String) value;
		if (input == null) {
			return;
		} else if (crosstabHandle == null) {
			initializeCrosstab();
		}
		if (stringValue != null) {
			CommandStack stack = crosstabHandle.getModuleHandle().getCommandStack();
			// start trans
			stack.startTrans(TRANS_NAME);
			crosstabHandle.setHideMeasureHeader(Boolean.valueOf((String) value));
			CrosstabUtil.addAllHeaderLabel(crosstabHandle);
			stack.commit();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.provider
	 * .IDescriptorProvider#setInput(java.lang.Object)
	 */
	public void setInput(Object input) {
		// TODO Auto-generated method stub
		super.setInput(input);
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
		} else
		// input instanceof ExtendedItemHandle
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
