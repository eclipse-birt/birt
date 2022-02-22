/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation .
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

package org.eclipse.birt.report.designer.ui.internal.rcp;

import org.eclipse.birt.report.designer.ui.ReportRCPPerspective;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

/**
 * Class for configuring the workbench of BIRT RCP designer.
 */
public class DesignerWorkbenchAdvisor extends WorkbenchAdvisor {

	@Override
	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		return new DesignerWorkbenchWindowAdvisor(configurer);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.ui.application.WorkbenchAdvisor#getInitialWindowPerspectiveId()
	 */
	@Override
	public String getInitialWindowPerspectiveId() {
		return ReportRCPPerspective.BIRT_REPORT_RCP_PERSPECTIVE;
	}

	@Override
	public void initialize(IWorkbenchConfigurer configurer) {
		super.initialize(configurer);
		configurer.setSaveAndRestore(true);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.application.WorkbenchAdvisor#preStartup()
	 */
	@Override
	public void preStartup() {
		super.preStartup();
//		WorkbenchAdapterBuilder.registerAdapters( );
	}

}
