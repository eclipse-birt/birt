
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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
package org.eclipse.birt.data.engine.olap.script;

import jakarta.olap.OLAPException;
import jakarta.olap.cursor.DimensionCursor;
import jakarta.olap.cursor.RowDataMetaData;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.util.OlapExpressionUtil;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 *
 */

public class JSLevelObject extends ScriptableObject {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private DimensionCursor cursor;
	private String levelName;
	private String displayName;

	JSLevelObject(DimensionCursor cursor, String levelName) throws OLAPException {
		this.cursor = cursor;
		this.levelName = levelName;
		RowDataMetaData meta = this.cursor.getMetaData();
		String defaultName = OlapExpressionUtil.getDisplayColumnName(this.levelName);
		for (int i = 0; i < meta.getColumnCount(); i++) {
			if (meta.getColumnName(i).equals(defaultName)) {
				this.displayName = defaultName;
				break;
			}
		}
		if (this.displayName == null) {
			this.displayName = this.levelName;
		}
	}

	@Override
	public String getClassName() {
		return "JSLevelObject";
	}

	@Override
	public Object getDefaultValue(Class hint) {
		return this.getKeyValue();
	}

	private Object getKeyValue() {
		try {
			return this.cursor.getObject(this.levelName);
		} catch (OLAPException e) {
			return null;
		}
	}

	/*
	 * @see org.mozilla.javascript.ScriptableObject#get(java.lang.String,
	 * org.mozilla.javascript.Scriptable)
	 */
	@Override
	public Object get(String name, Scriptable start) {
		try {
			if (this.displayName.equals(name)) {
				return this.cursor.getObject(OlapExpressionUtil.getAttributeColumnName(levelName, this.displayName));
			}
			return this.cursor.getObject(OlapExpressionUtil.getAttributeColumnName(levelName, name));
		} catch (OLAPException e) {
			throw new RuntimeException(new DataException(e.getLocalizedMessage()));
		}
	}
}
