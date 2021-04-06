/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.simpleapi;

import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.simpleapi.IMasterPage;
import org.eclipse.birt.report.model.elements.interfaces.IMasterPageModel;

/**
 * 
 * Implements of <code>IMasterPage</code>
 * 
 */

public class MasterPage extends ReportElement implements IMasterPage {

	/**
	 * Constructor
	 * 
	 * @param handle
	 */

	public MasterPage(ReportElementHandle handle) {
		super(handle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IMasterPage#getPageType()
	 */

	public String getPageType() {
		return ((MasterPageHandle) handle).getPageType();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.simpleapi.IMasterPage#setPageType(java
	 * .lang.String)
	 */

	public void setPageType(String pageType) throws SemanticException {
		setProperty(IMasterPageModel.TYPE_PROP, pageType);
	}

}
