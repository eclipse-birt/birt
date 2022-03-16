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
import org.eclipse.birt.report.model.elements.interfaces.IDataSourceModel;

/**
 * This class represents a data source: a connection to a database or other
 * source of data. A typical connection is a connection via JDBC to Oracle,
 * SQL-Server or another database.
 *
 */

public abstract class DataSource extends ReferenceableElement implements IDataSourceModel {

	/**
	 * Default constructor.
	 */

	public DataSource() {
	}

	/**
	 * Constructs the data source with a required name.
	 *
	 * @param theName the required name
	 */

	public DataSource(String theName) {
		super(theName);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.core.ReferenceableElement#broadcastToClients(
	 * org.eclipse.birt.report.model.api.activity.NotificationEvent,
	 * org.eclipse.birt.report.model.core.Module)
	 */

	@Override
	protected void broadcastToClients(NotificationEvent ev, Module module) {
	}
}
