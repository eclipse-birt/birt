/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Actuate Corporation - initial API and implementation
 *    
 *******************************************************************************/

package org.eclipse.birt.report.data.oda.jdbc.ui.model;

import org.eclipse.birt.report.data.oda.jdbc.ui.model.FilterConfig.Type;

public class TableType {

	public final static TableType ALL = new TableType(Type.ALL, Type.ALL.name(),
			FilterConfig.getTypeDisplayText(Type.ALL));
	public final static TableType TABLE = new TableType(Type.TABLE, Type.TABLE.name(),
			FilterConfig.getTypeDisplayText(Type.TABLE));
	public final static TableType VIEW = new TableType(Type.VIEW, Type.VIEW.name(),
			FilterConfig.getTypeDisplayText(Type.VIEW));
	public final static TableType PROCEDURE = new TableType(Type.PROCEDURE, Type.PROCEDURE.name(),
			FilterConfig.getTypeDisplayText(Type.PROCEDURE));
	public final static TableType NO_LIMIT = new TableType(null, FilterConfig.JDBC_TYPE_NO_LIMIT,
			FilterConfig.getTypeDisplayText(Type.ALL));

	private Type typeID;

	private String typeName;

	private String displayName;

	public TableType(Type typeID, String typeName, String displayName) {
		this.typeID = typeID;
		this.typeName = typeName;
		this.displayName = displayName;
	}

	public Type getTypeID() {
		return typeID;
	}

	public String getTypeName() {
		return typeName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public static TableType valueOf(Type type) {
		assert type != null;
		switch (type) {
		case PROCEDURE:
			return TableType.PROCEDURE;
		case TABLE:
			return TableType.TABLE;
		case VIEW:
			return TableType.VIEW;
		case ALL:
			return TableType.ALL;
		default:
			assert false;
			return TableType.ALL;
		}
	}

}
