/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.designer.internal.ui.preferences;

import org.eclipse.core.runtime.IPath;

/**
 *
 */
public class CPListElement {

	private IPath fPath;
	private boolean fIsExported;
	private boolean fIsMissing;

	public CPListElement() {
		this(null);
	}

	public CPListElement(IPath path) {
		fPath = path;
	}

	/**
	 * Gets the class path entry path.
	 *
	 * @return returns the path
	 * @see IClasspathEntry#getPath()
	 */
	public IPath getPath() {
		return fPath;
	}

	public Object[] getChildren(boolean hideOutputFolder) {
		// Has no children
		return new Object[0];
	}

	/*
	 * @see Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other) {
		if (other != null && other.getClass().equals(getClass())) {
			CPListElement elem = (CPListElement) other;
			return getPath().equals(elem.getPath());
		}
		return false;
	}

	/*
	 * @see Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return fPath.hashCode();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getPath().toString();
	}

	/**
	 * Returns if a entry is missing.
	 *
	 * @return Returns a boolean
	 */
	public boolean isMissing() {
		return fIsMissing;
	}

	/**
	 * Sets the 'missing' state of the entry.
	 *
	 * @param isMissing the new state
	 */
	public void setIsMissing(boolean isMissing) {
		fIsMissing = isMissing;
	}

	/**
	 * Returns if a entry is exported (only applies to libraries)
	 *
	 * @return Returns a boolean
	 */
	public boolean isExported() {
		return fIsExported;
	}

	/**
	 * Sets the export state of the entry.
	 *
	 * @param isExported the new state
	 */
	public void setExported(boolean isExported) {
		if (isExported != fIsExported) {
			fIsExported = isExported;
		}
	}
}
