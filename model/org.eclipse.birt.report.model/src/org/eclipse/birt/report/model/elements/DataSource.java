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

	protected void broadcastToClients(NotificationEvent ev, Module module) {
	}
}
