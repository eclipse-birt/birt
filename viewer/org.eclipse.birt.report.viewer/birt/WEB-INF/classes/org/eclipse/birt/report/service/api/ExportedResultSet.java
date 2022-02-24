/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.service.api;

import java.util.List;

/**
 * Representation of a result set
 * 
 */
public class ExportedResultSet {

	private String queryName;

	private List columns;

	public ExportedResultSet(String queryName, List columns) {
		this.queryName = queryName;
		this.columns = columns;
	}

	public String getQueryName() {
		return queryName;
	}

	public List getColumns() {
		return columns;
	}

}
