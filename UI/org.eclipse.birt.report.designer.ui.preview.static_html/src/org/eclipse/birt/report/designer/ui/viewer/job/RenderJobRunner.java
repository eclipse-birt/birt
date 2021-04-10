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

package org.eclipse.birt.report.designer.ui.viewer.job;

import org.eclipse.core.runtime.jobs.Job;

/**
 * 
 */

public class RenderJobRunner {
	public static void runRenderJob(Job runJob, RenderJobRule jobRule) {
		// boolean showDialog = true;
		// if ( Display.getCurrent( ) == null )
		// showDialog = false;
		//
		// Shell shell = PlatformUI.getWorkbench( )
		// .getActiveWorkbenchWindow( )
		// .getShell( );

		// if ( showDialog )
		// {
		// service.showInDialog( shell, runJob );
		// }

		// runJob.setProperty( IProgressConstants.ICON_PROPERTY, image );
		runJob.setUser(true);
		runJob.setRule(jobRule);
		runJob.schedule();
	}
}
