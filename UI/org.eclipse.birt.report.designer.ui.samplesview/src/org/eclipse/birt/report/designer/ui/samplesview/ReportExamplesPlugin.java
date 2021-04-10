/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
