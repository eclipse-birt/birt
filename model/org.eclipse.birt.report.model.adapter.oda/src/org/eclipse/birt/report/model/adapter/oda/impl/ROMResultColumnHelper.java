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
