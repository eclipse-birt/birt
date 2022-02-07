/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.ui.samplesview;

import org.eclipse.ui.plugin.AbstractUIPlugin;

public class ReportExamplesPlugin extends AbstractUIPlugin {

	private static ReportExamplesPlugin reportExamplesPlugin;

	public ReportExamplesPlugin() {
		super();
		reportExamplesPlugin = this;
	}

	/**
	 * Returns the shared instance of this plugin activator.
	 * 
	 * @return
	 */
	public static ReportExamplesPlugin getDefault() {
		return reportExamplesPlugin;
	}

}
