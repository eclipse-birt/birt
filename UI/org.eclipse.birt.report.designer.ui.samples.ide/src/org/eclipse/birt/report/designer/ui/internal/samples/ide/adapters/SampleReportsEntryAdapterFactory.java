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

package org.eclipse.birt.report.designer.ui.internal.samples.ide.adapters;

import org.eclipse.birt.report.designer.ui.samples.ide.sampleslocator.IDESampleReportsEntry;
import org.eclipse.birt.report.designer.ui.samplesview.sampleslocator.ISampleReportEntry;
import org.eclipse.core.runtime.IAdapterFactory;

/**
 * Add ISampleReportEntry adaptable to ReportExamples
 * 
 */
public class SampleReportsEntryAdapterFactory implements IAdapterFactory {

	public Object getAdapter(Object adaptableObject, Class adapterType) {
		return new IDESampleReportsEntry();
	}

	public Class[] getAdapterList() {
		return new Class[] { ISampleReportEntry.class };
	}
}
