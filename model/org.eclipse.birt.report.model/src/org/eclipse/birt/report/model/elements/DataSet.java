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

package org.eclipse.birt.report.model.elements;

import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.ReferenceableElement;
import org.eclipse.birt.report.model.elements.interfaces.IDataSetModel;

/**
 * Abastract parent of simple data set and joint data set.
 * 
 */

public abstract class DataSet extends ReferenceableElement implements IDataSetModel {

	/**
	 * Default constructor.
	 */

	public DataSet() {
	}

	/**
	 * Constructs this data set by name.
	 * 
	 * @param name of the data set.
	 */

	public DataSet(String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.ReferenceableElement#broadcastToClients(
	 * org.eclipse.birt.report.model.api.activity.NotificationEvent,
	 * org.eclipse.birt.report.model.core.Module)
	 */

	protected void broadcastToClients(NotificationEvent ev, Module module) {
	}
}
