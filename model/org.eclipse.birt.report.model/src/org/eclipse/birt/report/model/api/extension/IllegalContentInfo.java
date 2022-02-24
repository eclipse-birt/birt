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

package org.eclipse.birt.report.model.api.extension;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.extension.SimplePeerExtensibilityProvider.UndefinedChildInfo;

/**
 * 
 */
public class IllegalContentInfo {

	private UndefinedChildInfo info = null;
	private Module module = null;

	/**
	 * 
	 * @param infor
	 * @param module
	 */
	public IllegalContentInfo(UndefinedChildInfo infor, Module module) {
		this.info = infor;
		this.module = module;
	}

	/**
	 * Gets the child of the <code>UndefinedChildInfo</code>.
	 * 
	 * @return the child of the <code>UndefinedChildInfo</code>.
	 */
	public DesignElementHandle getContent() {
		DesignElement child = info.getChild();
		module.rename(child);
		return child.getHandle(module);
	}

	/**
	 * Gets the index of <code>UndefinedChildInfo<code>.
	 * 
	 * @return the index of <code>UndefinedChildInfo<code>.
	 */
	public int getIndex() {
		return info.getIndex();
	}
}
