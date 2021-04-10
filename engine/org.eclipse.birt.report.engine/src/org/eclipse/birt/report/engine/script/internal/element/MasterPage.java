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

package org.eclipse.birt.report.engine.script.internal.element;

import org.eclipse.birt.report.engine.api.script.element.IMasterPage;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;

/**
 * Implements of MasterPage
 */

public class MasterPage extends ReportElement implements IMasterPage {

	public MasterPage(MasterPageHandle handle) {
		super(handle);
	}

	public MasterPage(org.eclipse.birt.report.model.api.simpleapi.IMasterPage reportElementImpl) {
		super(reportElementImpl);
	}

	public String getPageType() {
		return ((org.eclipse.birt.report.model.api.simpleapi.IMasterPage) designElementImpl).getPageType();
	}

	public void setPageType(String pageType) throws SemanticException {
		((org.eclipse.birt.report.model.api.simpleapi.IMasterPage) designElementImpl).setPageType(pageType);
	}

}
