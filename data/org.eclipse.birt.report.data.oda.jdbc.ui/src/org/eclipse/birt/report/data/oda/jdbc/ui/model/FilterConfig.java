/*******************************************************************************
 * Copyright (c) 2008, 2013 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.data.oda.jdbc.ui.model;

import java.util.ArrayList;

import org.eclipse.birt.report.data.oda.jdbc.ui.JdbcPlugin;

public class FilterConfig {

	public static enum Type {
		ALL, TABLE, VIEW, PROCEDURE
	};

	public final static String JDBC_TYPE_NO_LIMIT = "NO_LIMIT"; //$NON-NLS-1$
	public final static String JDBC_TYPE_TABLE = "TABLE"; //$NON-NLS-1$
	public final static String JDBC_TYPE_VIEW = "VIEW"; //$NON-NLS-1$
	public final static String JDBC_TYPE_SYSTEM_TABLE = "SYSTEM TABLE"; //$NON-NLS-1$
	public final static String JDBC_TYPE_GLOBAL_TEMPORARY = "GLOBAL TEMPORARY"; //$NON-NLS-1$
	public final static String JDBC_TYPE_LOCAL_TEMPORARY = "LOCAL TEMPORARY"; //$NON-NLS-1$
	public final static String JDBC_TYPE_ALIAS = "ALIAS"; //$NON-NLS-1$
	public final static String JDBC_TYPE_SYNONYM = "SYNONYM"; //$NON-NLS-1$

	private TableType type;
	private boolean isShowSystemTable;
	private boolean isShowAlias;
	private String schemaName; // null if no schema specified
	private String namePattern;

	private int maxSchemaCount;
	private int maxTableCountPerSchema;

	public FilterConfig(String schemaName, Type type, String namePattern, boolean isShowSystemTable,
			boolean isShowAlias, int maxSchemaCount, int maxTableCountPerSchema) {
		assert type != null && maxSchemaCount > 0 && maxTableCountPerSchema > 0;
		this.schemaName = schemaName;
		this.type = TableType.valueOf(type);
		this.isShowSystemTable = isShowSystemTable;
		this.isShowAlias = isShowAlias;
		this.namePattern = generatePattern(namePattern);
		this.maxSchemaCount = maxSchemaCount;
		this.maxTableCountPerSchema = maxTableCountPerSchema;
	}

	public FilterConfig(String schemaName, TableType type, String namePattern, boolean isShowSystemTable,
			boolean isShowAlias, int maxSchemaCount, int maxTableCountPerSchema) {
		assert type != null && maxSchemaCount > 0 && maxTableCountPerSchema > 0;
		this.schemaName = schemaName;
		this.type = type;
		this.isShowSystemTable = isShowSystemTable;
		this.isShowAlias = isShowAlias;
		this.namePattern = generatePattern(namePattern);
		this.maxSchemaCount = maxSchemaCount;
		this.maxTableCountPerSchema = maxTableCountPerSchema;
	}

	public Type getType() {
		return type.getTypeID();
	}

	public TableType getTableType() {
		return type;
	}

	public boolean isShowSystemTable() {
		return isShowSystemTable;
	}

	public boolean isShowAlias() {
		return isShowAlias;
	}

	public String getNamePattern() {
		return namePattern;
	}

	public String getSchemaName() {
		return schemaName;
	}

	public int getMaxSchemaCount() {
		return maxSchemaCount;
	}

	public int getMaxTableCountPerSchema() {
		return maxTableCountPerSchema;
	}

	/**
	 * @return null if no table/view needed to query
	 */
	public String[] getTableTypesForJDBC() {
		ArrayList<String> types = new ArrayList<String>();

		if (type == TableType.PROCEDURE) {

		} else if (type == TableType.TABLE) {
			types.add(JDBC_TYPE_TABLE);
			populateSystemTableOption(types);
			populateAliasOption(types);
		} else if (type == TableType.VIEW) {
			types.add(JDBC_TYPE_VIEW);
			populateAliasOption(types);
		} else if (type == TableType.ALL) {
			types.add(JDBC_TYPE_TABLE);
			types.add(JDBC_TYPE_VIEW);
			populateSystemTableOption(types);
			populateAliasOption(types);
		} else {
			types.add(type.getTypeName());
		}
		return types.isEmpty() ? null : types.toArray(new String[] {});
	}

	private void populateSystemTableOption(final ArrayList<String> types) {
		if (isShowSystemTable()) {
			types.add(JDBC_TYPE_SYSTEM_TABLE);
		}
	}

	private void populateAliasOption(final ArrayList<String> types) {
		if (isShowAlias()) {
			types.add(JDBC_TYPE_ALIAS);
			types.add(JDBC_TYPE_SYNONYM);
		}
	}

	private static String generatePattern(String input) {
		if (input != null) {
			if (input.lastIndexOf('%') == -1) {
				input = input + "%"; //$NON-NLS-1$
			}
		} else {
			input = "%"; //$NON-NLS-1$
		}
		return input;
	}

	public static String getTypeDisplayText(Type type) {
		assert type != null;
		switch (type) {
		case PROCEDURE:
			return JdbcPlugin.getResourceString("tablepage.text.procedure"); //$NON-NLS-1$
		case TABLE:
			return JdbcPlugin.getResourceString("tablepage.text.tabletype"); //$NON-NLS-1$
		case VIEW:
			return JdbcPlugin.getResourceString("tablepage.text.viewtype"); //$NON-NLS-1$
		case ALL:
			return JdbcPlugin.getResourceString("tablepage.text.All"); //$NON-NLS-1$
		default:
			return type.name();
		}
	}
}
