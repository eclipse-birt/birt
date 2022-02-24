/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.report.model.adapter.oda.impl;

import org.eclipse.birt.report.model.api.ColumnHintHandle;
import org.eclipse.birt.report.model.api.OdaResultSetColumnHandle;
import org.eclipse.datatools.connectivity.oda.design.ColumnDefinition;

public class ROMResultColumnHelper {
	private OdaResultSetColumnHandle oldColumn;
	private ColumnHintHandle oldColumnHint;
	private ColumnDefinition newColumnDefn;
	private boolean setup;

	public ROMResultColumnHelper(ColumnDefinition newColumnDefn) {
		this.newColumnDefn = newColumnDefn;
		this.oldColumn = null;
		this.oldColumnHint = null;
		this.setup = false;
	}

	public OdaResultSetColumnHandle getOldColumn() {
		return oldColumn;
	}

	public ColumnHintHandle getOldColumnHint() {
		return oldColumnHint;
	}

	public ColumnDefinition getNewColumnDefn() {
		return newColumnDefn;
	}

	public void setOldColumn(OdaResultSetColumnHandle oldColumn) {
		this.oldColumn = oldColumn;
	}

	public void setOldColumnHint(ColumnHintHandle oldColumnHint) {
		this.oldColumnHint = oldColumnHint;
	}

	public boolean isSetup() {
		return setup;
	}

	public void setup() {
		this.setup = true;
	}
}
