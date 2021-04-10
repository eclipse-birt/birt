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

package org.eclipse.birt.report.designer.ui.samples.rcp.sampleslocator;

import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceEntry;
import org.eclipse.birt.report.designer.ui.samplesview.sampleslocator.ISampleReportEntry;
import org.eclipse.birt.report.designer.ui.samplesview.sampleslocator.SampleReportsEntry;

public class RCPSampleReportsEntry implements ISampleReportEntry {
	private static final String[] REPORTDESIGN_FILENAME_PATTERN = new String[] { "*.rptdesign" //$NON-NLS-1$
	};
	SampleReportsEntry samplesEntries;

	public RCPSampleReportsEntry() {
		samplesEntries = new SampleReportsEntry(REPORTDESIGN_FILENAME_PATTERN, "Report Examples", //$NON-NLS-1$
				"/samplereports", //$NON-NLS-1$
				null, false);
	}

	public ResourceEntry[] getEntries() {
		return samplesEntries.getChildren();
	}
}
