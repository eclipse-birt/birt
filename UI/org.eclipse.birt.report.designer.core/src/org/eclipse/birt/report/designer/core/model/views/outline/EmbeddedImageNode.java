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

package org.eclipse.birt.report.designer.core.model.views.outline;

import org.eclipse.birt.report.model.api.ModuleHandle;

/**
 * Model class for embedded image node in the outline view
 */

public class EmbeddedImageNode {

	private ModuleHandle reportHandle;

	/**
	 * Constructor
	 *
	 * @param reportHandle
	 */
	public EmbeddedImageNode(ModuleHandle reportHandle) {
		this.reportHandle = reportHandle;
	}

	/**
	 * Get container of embedded images.
	 *
	 * @return report design handle, which contains embedded images.
	 */
	public ModuleHandle getReportDesignHandle() {
		return reportHandle;
	}

	/**
	 * Get array of embedded images.
	 *
	 * @return Array of embedded images.
	 */
	public Object[] getChildren() {
		return reportHandle.getVisibleImages().toArray();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (arg0 == this) {
			return true;
		}
		if (arg0 instanceof EmbeddedImageNode) {
			return ((EmbeddedImageNode) arg0).reportHandle == reportHandle;
		}
		return false;
	}

	@Override
	public int hashCode() {
		int hashCode = 13;
		if (reportHandle != null) {
			hashCode += reportHandle.hashCode() * 7;
		}
		return hashCode;
	}
}
