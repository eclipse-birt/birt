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

package org.eclipse.birt.report.designer.ui.internal.samples.rcp.adapters;

import org.eclipse.birt.report.designer.ui.samples.rcp.sampleslocator.RCPSampleReportsEntry;
import org.eclipse.birt.report.designer.ui.samplesview.sampleslocator.ISampleReportEntry;
import org.eclipse.core.runtime.IAdapterFactory;

public class SampleReportsEntryAdapterFactory implements IAdapterFactory {

	public Object getAdapter(Object adaptableObject, Class adapterType) {
		return new RCPSampleReportsEntry();
	}

	public Class[] getAdapterList() {
		return new Class[] { ISampleReportEntry.class };
	}
}
