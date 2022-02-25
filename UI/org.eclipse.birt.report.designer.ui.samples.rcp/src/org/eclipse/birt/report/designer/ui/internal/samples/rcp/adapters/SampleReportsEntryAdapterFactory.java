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

package org.eclipse.birt.report.designer.ui.internal.samples.rcp.adapters;

import org.eclipse.birt.report.designer.ui.samples.rcp.sampleslocator.RCPSampleReportsEntry;
import org.eclipse.birt.report.designer.ui.samplesview.sampleslocator.ISampleReportEntry;
import org.eclipse.core.runtime.IAdapterFactory;

public class SampleReportsEntryAdapterFactory implements IAdapterFactory {

	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		return new RCPSampleReportsEntry();
	}

	@Override
	public Class[] getAdapterList() {
		return new Class[] { ISampleReportEntry.class };
	}
}
