/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.core.model;

import java.util.List;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.GroupHandle;

/**
 * IGroupStructureProvider
 */
public interface IGroupStructureProvider {

	/**
	 * @return Returns the groups structure associated with given element, if the
	 *         original group info is not <code>GroupHandle</code>, it's desired to
	 *         be converted to the <code>GroupHandle</code> structure without a
	 *         container. Only the info on <code>GroupHandle</code> itself will be
	 *         used.
	 */
	List<GroupHandle> getGroups(DesignElementHandle handle);
}
