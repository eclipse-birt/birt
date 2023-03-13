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

package org.eclipse.birt.report.designer.ui.samples.ide.sampleslocator;

import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceEntry;
import org.eclipse.birt.report.designer.ui.samplesview.sampleslocator.ISampleReportEntry;
import org.eclipse.birt.report.designer.ui.samplesview.sampleslocator.SampleReportsEntry;

public class IDESampleReportsEntry implements ISampleReportEntry {

	private static final String[] REPORTDESIGN_FILENAME_PATTERN = { "*.rptdesign" //$NON-NLS-1$
	};
	SampleReportsEntry samplesEntries, ideSamplesEntries;

	public IDESampleReportsEntry() {
		samplesEntries = new SampleReportsEntry(REPORTDESIGN_FILENAME_PATTERN, "Report Examples", //$NON-NLS-1$
				"/samplereports", //$NON-NLS-1$
				null, false);
		ideSamplesEntries = new SampleReportsEntry(REPORTDESIGN_FILENAME_PATTERN, "IDE Report Examples", //$NON-NLS-1$
				"/samplereports.ide", //$NON-NLS-1$
				null, false);
	}

	@Override
	public ResourceEntry[] getEntries() {
		int m, n, length;
		length = samplesEntries.getChildren().length + ideSamplesEntries.getChildren().length;
		ResourceEntry[] entries = new ResourceEntry[length];
		for (m = 0; m < samplesEntries.getChildren().length; m++) {
			entries[m] = samplesEntries.getChildren()[m];
		}
		for (n = 0; n < ideSamplesEntries.getChildren().length; n++) {
			entries[m + n] = ideSamplesEntries.getChildren()[n];
		}

		return entries;
	}
}
