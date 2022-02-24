/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.ir;

import java.util.Map;

import org.eclipse.birt.report.model.api.DesignElementHandle;

/**
 * base class of all elements in the report design.
 * 
 */
public abstract class ReportElementDesign {

	/**
	 * handle of this report element
	 */
	protected transient DesignElementHandle handle;

	/**
	 * ID of the object.
	 */
	protected long ID = -1;
	/**
	 * name of this element
	 */
	protected String name;
	/**
	 * parent element's name
	 */
	protected String extend;

	/**
	 * class implement this element.
	 */
	protected String javaClass;

	/**
	 * map a prepared expression to a name
	 */
	protected Map<String, Expression> userProperties;

	/**
	 * return user properties
	 * 
	 * @return
	 */
	public Map<String, Expression> getUserProperties() {
		return userProperties;
	}

	public void setUserProperties(Map<String, Expression> userProperties) {
		this.userProperties = userProperties;
	}

	/**
	 * @return Returns the extend.
	 */
	public String getExtends() {
		return extend;
	}

	/**
	 * @param extend The extend to set.
	 */
	public void setExtends(String extend) {
		this.extend = extend;
	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Returns the iD.
	 */
	public long getID() {
		return ID;
	}

	/**
	 * @param id The iD to set.
	 */
	public void setID(long id) {
		ID = id;
	}

	/**
	 * @return Returns the javaClass.
	 */
	public String getJavaClass() {
		return javaClass;
	}

	/**
	 * @param javaClass The javaClass to set.
	 */
	public void setJavaClass(String javaClass) {
		this.javaClass = javaClass;
	}

	/**
	 * @return Returns the handle.
	 */
	public DesignElementHandle getHandle() {
		return handle;
	}

	/**
	 * @param handle The handle to set.
	 */
	public void setHandle(DesignElementHandle handle) {
		this.handle = handle;
	}
}
