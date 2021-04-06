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

package org.eclipse.birt.report.designer.internal.ui.util;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;

/**
 * 
 *
 */
public class ModelEventInfo {

	private DesignElementHandle focus;
	private NotificationEvent ev;

	/**
	 * @param focus
	 * @param ev
	 */
	public ModelEventInfo(DesignElementHandle focus, NotificationEvent ev) {
		this.focus = focus;
		this.ev = ev;
	}

	/**
	 * @return
	 */
	public DesignElementHandle getTarget() {
		return focus;
	}

	/**
	 * @return
	 */
	public NotificationEvent getEvent() {
		return ev;
	}

}