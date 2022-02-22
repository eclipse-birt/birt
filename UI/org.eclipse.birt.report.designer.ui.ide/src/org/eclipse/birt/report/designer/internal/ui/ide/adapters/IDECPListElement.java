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

package org.eclipse.birt.report.designer.internal.ui.ide.adapters;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathAttribute;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;

/**
 *
 */
public class IDECPListElement {

	private int fEntryKind;
	private IPath fPath, fOrginalPath;
	private IResource fResource;
	private boolean fIsExported;
	private boolean fIsMissing;

	private Object fParentContainer;

	private IClasspathEntry fCachedEntry;

	public IDECPListElement(int entryKind, IPath path, IResource res) {
		this(null, entryKind, path, res);
	}

	public IDECPListElement(int entryKind) {
		this(null, entryKind, null, null);
	}

	public IDECPListElement(Object parent, int entryKind, IPath path, IResource res) {
		this(parent, entryKind, path, false, res);
	}

	public IDECPListElement(Object parent, int entryKind, IPath path, boolean newElement, IResource res) {

		fEntryKind = entryKind;
		fPath = path;
		fOrginalPath = newElement ? null : path;

		fResource = res;
		fIsExported = false;

		fIsMissing = false;
		fCachedEntry = null;
		fParentContainer = parent;

	}

	public IClasspathEntry getClasspathEntry() {
		if (fCachedEntry == null) {
			fCachedEntry = newClasspathEntry();
		}
		return fCachedEntry;
	}

	private IClasspathEntry newClasspathEntry() {

		IClasspathAttribute[] extraAttributes = {};
		switch (fEntryKind) {
		case IClasspathEntry.CPE_SOURCE:
			return JavaCore.newSourceEntry(fPath, null, null, null, extraAttributes);
		case IClasspathEntry.CPE_LIBRARY: {
			return JavaCore.newLibraryEntry(fPath, null, null, null, extraAttributes, isExported());
		}
		case IClasspathEntry.CPE_PROJECT: {
			return JavaCore.newProjectEntry(fPath, null, false, extraAttributes, isExported());
		}
		case IClasspathEntry.CPE_CONTAINER: {
			return JavaCore.newContainerEntry(fPath, null, extraAttributes, isExported());
		}
		case IClasspathEntry.CPE_VARIABLE: {
			return JavaCore.newVariableEntry(fPath, null, null, null, extraAttributes, isExported());
		}
		default:
			return null;
		}
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

	/**
	 * Gets the class path entry kind.
	 *
	 * @return the entry kind
	 * @see IClasspathEntry#getEntryKind()
	 */
	public int getEntryKind() {
		return fEntryKind;
	}

	/**
	 * Entries without resource are either non existing or a variable entry External
	 * jars do not have a resource
	 *
	 * @return returns the resource
	 */
	public IResource getResource() {
		return fResource;
	}

	public Object[] getChildren(boolean hideOutputFolder) {
		// no children
		return new Object[0];
	}

	public Object getParentContainer() {
		return fParentContainer;
	}

	/**
	 * Notifies that an attribute has changed
	 *
	 * @param key the changed key
	 */
	protected void attributeChanged(String key) {
		fCachedEntry = null;
	}

	public boolean isDeprecated() {
		if (fEntryKind != IClasspathEntry.CPE_VARIABLE) {
			return false;
		}
		if (fPath.segmentCount() > 0) {
			return JavaCore.getClasspathVariableDeprecationMessage(fPath.segment(0)) != null;
		}
		return false;
	}

	/*
	 * @see Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other) {
		if (other != null && other.getClass().equals(getClass())) {
			IDECPListElement elem = (IDECPListElement) other;
			return getClasspathEntry().equals(elem.getClasspathEntry());
		}
		return false;
	}

	/*
	 * @see Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return fPath.hashCode() + fEntryKind;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getClasspathEntry().toString();
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

			attributeChanged(null);
		}
	}

	public void setPath(IPath path) {
		fCachedEntry = null;
		fPath = path;
	}

	public IPath getOrginalPath() {
		return fOrginalPath;
	}

}
