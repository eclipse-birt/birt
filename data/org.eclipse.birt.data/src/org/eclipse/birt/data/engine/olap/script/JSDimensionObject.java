
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

import java.util.HashMap;
import java.util.List;

import javax.olap.OLAPException;
import javax.olap.cursor.DimensionCursor;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 *
 */
public class JSDimensionObject extends ScriptableObject {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private HashMap levels;

	JSDimensionObject(List levelNames, List dimensionCursor) throws OLAPException {
		assert levelNames.size() == dimensionCursor.size();

		this.levels = new HashMap();
		for (int i = 0; i < levelNames.size(); i++) {
			this.levels.put(levelNames.get(i),
					new JSLevelObject((DimensionCursor) dimensionCursor.get(i), levelNames.get(i).toString()));
		}
	}

	@Override
	public String getClassName() {
		return "JSDimensionObject";
	}

	/*
	 * @see org.mozilla.javascript.ScriptableObject#get(java.lang.String,
	 * org.mozilla.javascript.Scriptable)
	 */
	@Override
	public Object get(String name, Scriptable start) {
		if (!this.levels.containsKey(name)) {
			throw new RuntimeException(new DataException(ResourceConstants.LEVEL_NAME_NOT_FOUND, name));
		}
		return this.levels.get(name);
	}
}
