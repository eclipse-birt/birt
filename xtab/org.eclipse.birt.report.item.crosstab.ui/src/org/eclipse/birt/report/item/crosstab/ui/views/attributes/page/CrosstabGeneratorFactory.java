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

package org.eclipse.birt.report.item.crosstab.ui.views.attributes.page;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.designer.ui.views.IPageGenerator;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.CrosstabCellPageGenerator;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.CrosstabPageGenerator;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.core.runtime.IAdapterFactory;

/**
 * 
 */

public class CrosstabGeneratorFactory implements IAdapterFactory {
	protected static final Logger logger = Logger.getLogger(CrosstabGeneratorFactory.class.getName());

	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (!(adaptableObject instanceof ExtendedItemHandle)) {
			return null;
		}
		ExtendedItemHandle item = (ExtendedItemHandle) adaptableObject;
		IReportItem reportItem = null;
		try {
			reportItem = item.getReportItem();
		} catch (ExtendedElementException e) {
			// TODO Auto-generated catch block
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
		if (reportItem == null) {
			return null;
		}
		if (reportItem instanceof CrosstabReportItemHandle) {
			return new CrosstabPageGenerator();
		} else if (reportItem instanceof CrosstabCellHandle) {
			return new CrosstabCellPageGenerator();
		}
		return null;
	}

	public Class[] getAdapterList() {
		return new Class[] { IPageGenerator.class };
	}

}
