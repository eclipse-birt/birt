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

package org.eclipse.birt.report.model.api;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.core.Module;

/**
 * Implements an empty group element handle, which actually can do nothing.
 */

public class EmptyGroupElementHandle extends GroupElementHandle {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.GroupElementHandle#
	 * hasVirtualExtendsElements()
	 */

	protected boolean allExtendedElements() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.GroupElementHandle#getElements()
	 */

	public List getElements() {
		return Collections.EMPTY_LIST;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.GroupElementHandle#getModule()
	 */
	public Module getModule() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.GroupElementHandle#getModuleHandle()
	 */

	public ModuleHandle getModuleHandle() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.GroupElementHandle#isSameType()
	 */

	public boolean isSameType() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.GroupElementHandle#getCommonProperties()
	 */

	public List getCommonProperties() {
		return Collections.EMPTY_LIST;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.GroupElementHandle#visiblePropertyIterator(
	 * )
	 */

	public Iterator visiblePropertyIterator() {
		return new GroupPropertyIterator(Collections.EMPTY_LIST);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.GroupElementHandle#isPropertyVisible(java.
	 * lang.String)
	 */

	protected boolean isPropertyVisible(String propName) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.GroupElementHandle#clearLocalProperties()
	 */

	public void clearLocalProperties() throws SemanticException {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.GroupElementHandle#isExtendedElements()
	 */

	public boolean isExtendedElements() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.GroupElementHandle#isPropertyReadOnly(java.
	 * lang.String)
	 */

	protected boolean isPropertyReadOnly(String propName) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.GroupElementHandle#getPropertyHandle(java.
	 * lang.String)
	 */

	public GroupPropertyHandle getPropertyHandle(String propName) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.GroupElementHandle#isInGroup(org.eclipse.
	 * birt.report.model.api.DesignElementHandle)
	 */

	protected boolean isInGroup(DesignElementHandle element) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.GroupElementHandle#clearLocalProperties()
	 */
	public void clearLocalPropertiesIncludeSubElement() throws SemanticException {

	}

}
