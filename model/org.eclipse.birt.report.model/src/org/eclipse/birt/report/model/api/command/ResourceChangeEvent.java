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

package org.eclipse.birt.report.model.api.command;

import org.eclipse.birt.report.model.api.activity.NotificationEvent;

/**
 * Notification event that says that the module need refreshing. The listener
 * can find out which module changed by calling
 * {@link org.eclipse.birt.report.model.api.activity.NotificationEvent#getTarget}(
 * ).
 */

public abstract class ResourceChangeEvent extends NotificationEvent {
	private String filePath = null;

	/**
	 * Constructor.
	 * 
	 * @param path the file path of the changed library.
	 */

	public ResourceChangeEvent(String path) {
		filePath = path;
	}

	/**
	 * Returns the file path of the changed library.
	 * 
	 * @return the file path of the changed library.
	 */

	public String getChangedResourcePath() {
		return filePath;
	}

}
