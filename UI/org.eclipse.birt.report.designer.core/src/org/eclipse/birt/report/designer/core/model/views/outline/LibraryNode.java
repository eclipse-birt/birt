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

package org.eclipse.birt.report.designer.core.model.views.outline;

import org.eclipse.birt.report.model.api.ModuleHandle;

/**
 * Model class for embedded image node in the outline view
 */

public class LibraryNode {

	private ModuleHandle reportHandle;

	/**
	 * Constructor
	 * 
	 * @param reportHandle
	 */
	public LibraryNode(ModuleHandle reportHandle) {
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
	 * Get children
	 * 
	 * @return Array of embedded images.
	 */
	public Object[] getChildren() {
		return reportHandle.getLibraries().toArray();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object arg0) {
		if (arg0 == this) {
			return true;
		}
		if (arg0 instanceof LibraryNode) {
			return ((LibraryNode) arg0).reportHandle == reportHandle;
		}
		return false;
	}

	public int hashCode() {
		int hashCode = 13;
		if (reportHandle != null)
			hashCode += reportHandle.hashCode() * 7;
		return hashCode;
	}
}
