/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.integrate;

/**
 * Represents an "action" (hyperlink) attached to an element.
 */

public class SimpleActionHandle {

	private String uri = ""; //$NON-NLS-1$
	private String targetWindow = ""; //$NON-NLS-1$

	SimpleActionHandle() {

	}

	/**
	 * @param uri the uri to set
	 */
	public void setURI(String uri) {
		this.uri = uri;
	}

	/**
	 * @return the uri
	 */
	public String getURI() {
		return uri;
	}

	/**
	 * @param targetWindow the targetWindow to set
	 */
	public void setTargetWindow(String target) {
		this.targetWindow = target;
	}

	/**
	 * @return the targetWindow
	 */
	public String getTargetWindow() {
		return targetWindow;
	}
}
