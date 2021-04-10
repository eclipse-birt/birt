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

import org.eclipse.birt.report.designer.ui.samples.rcp.action.RCPOpenSampleReportAction;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.jface.action.IAction;

/**
 * Add IOpenSampleReport adaptable to ReportExamplesView
 * 
 */
public class OpenSampleReportActionAdapterFactory implements IAdapterFactory {

	public Object getAdapter(Object adaptableObject, Class adapterType) {
		return new RCPOpenSampleReportAction();
	}

	public Class[] getAdapterList() {
		return new Class[] { IAction.class };
	}
}
