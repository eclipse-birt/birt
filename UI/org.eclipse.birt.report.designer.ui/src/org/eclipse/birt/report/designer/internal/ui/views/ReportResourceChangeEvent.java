/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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

package org.eclipse.birt.report.designer.internal.ui.views;

import org.eclipse.birt.report.designer.ui.views.IReportResourceChangeEvent;

/**
 * ReportResourceChangeEvent
 */
public class ReportResourceChangeEvent implements IReportResourceChangeEvent {

	private Object source, data;
	private int type;

	public ReportResourceChangeEvent(Object source, Object data, int type) {
		this.source = source;
		this.data = data;
		this.type = type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.ui.views.IReportResourceChangeEvent#getData(
	 * )
	 */
	public Object getData() {
		return data;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.views.IReportResourceChangeEvent#
	 * getSource()
	 */
	public Object getSource() {
		return source;
	}

	@Override
	public String toString() {
		return "Source: " + source + ", Data: " + data; //$NON-NLS-1$ //$NON-NLS-2$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.ui.views.IReportResourceChangeEvent#getType(
	 * )
	 */
	public int getType() {
		return type;
	}

}
