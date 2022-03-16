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
