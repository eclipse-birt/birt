/*******************************************************************************
 * Copyright (c) 2004, 2014 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.rcp;

import org.eclipse.birt.report.designer.ui.internal.rcp.DesignerWorkbenchAdvisor;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

/**
 * 
 * BIRT RCP main application
 * 
 */
public class DesignerApplication implements IApplication {

	@Override
	public Object start(IApplicationContext context) throws Exception {
		Display display = PlatformUI.createDisplay();
		TrayDialog.setDialogHelpAvailable(true);
		try {
			int code = PlatformUI.createAndRunWorkbench(display, new DesignerWorkbenchAdvisor());
			// exit the application with an appropriate return code
			return code == PlatformUI.RETURN_RESTART ? EXIT_RESTART : EXIT_OK;
		} finally {
			if (display != null)
				display.dispose();
		}
	}

	@Override
	public void stop() {
	}
}
