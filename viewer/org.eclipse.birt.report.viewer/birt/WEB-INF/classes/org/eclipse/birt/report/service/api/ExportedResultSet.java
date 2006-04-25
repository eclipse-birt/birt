/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
