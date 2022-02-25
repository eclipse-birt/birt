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

import org.eclipse.birt.report.designer.ui.samples.rcp.action.RCPOpenSampleReportAction;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.jface.action.IAction;

/**
 * Add IOpenSampleReport adaptable to ReportExamplesView
 *
 */
public class OpenSampleReportActionAdapterFactory implements IAdapterFactory {

	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		return new RCPOpenSampleReportAction();
	}

	@Override
	public Class[] getAdapterList() {
		return new Class[] { IAction.class };
	}
}
