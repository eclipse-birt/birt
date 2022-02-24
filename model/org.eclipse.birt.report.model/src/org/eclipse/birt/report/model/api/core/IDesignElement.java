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

package org.eclipse.birt.report.model.api.core;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.core.Module;

/**
 * Interface for all the design elements.
 */

public interface IDesignElement extends Cloneable {

	/**
	 * Returns the definition object for this element.
	 * <p>
	 * Part of: Meta data system.
	 * 
	 * @return The element definition. Will always be non-null in a valid build.
	 */

	public IElementDefn getDefn();

	/**
	 * Returns an API handle for this element.
	 * 
	 * @param module the module
	 * @return an API handle for this element.
	 */

	public DesignElementHandle getHandle(Module module);

	/**
	 * Generates a clone copy of this element. When a report element is cloned, the
	 * basic principle is just copying the property value into the clone, the other
	 * things, like container references, child list references, listener references
	 * will not be cloned; that is, the clone is isolated from the design tree until
	 * it is added into a target design tree.
	 * 
	 * <p>
	 * When inserting the cloned element into the design tree, user needs to care
	 * about the element name confliction; that is, the client needs to call the
	 * method <code>{@link ReportDesignHandle#rename( DesignElementHandle )}</code>
	 * to change the element names.
	 * 
	 * @return Object the cloned design element.
	 * @throws CloneNotSupportedException if clone is not supported.
	 * 
	 */

	public Object clone() throws CloneNotSupportedException;
}
