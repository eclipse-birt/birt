/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.olap.script;

import java.util.Map;
import javax.olap.OLAPException;
import javax.olap.cursor.CubeCursor;

import org.eclipse.birt.data.engine.core.DataException;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * 
 */

public class JSMeasureAccessor extends ScriptableObject {

	private static final long serialVersionUID = 1L;
	private CubeCursor cursor;
	private Map measureMapping;

	/**
	 * 
	 * @param cursor
	 * @param measureMapping
	 * @throws OLAPException
	 */
	public JSMeasureAccessor(CubeCursor cursor, Map measureMapping) throws OLAPException {
		this.cursor = cursor;
		this.measureMapping = measureMapping;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mozilla.javascript.ScriptableObject#getClassName()
	 */
	public String getClassName() {
		// TODO Auto-generated method stub
		return "JSMeasureAccessor";
	}

	/*
	 * @see org.mozilla.javascript.ScriptableObject#get(java.lang.String,
	 * org.mozilla.javascript.Scriptable)
	 */
	public Object get(String name, Scriptable start) {
		try {
			return this.cursor.getObject((String) measureMapping.get(name));
		} catch (OLAPException e) {
			throw new RuntimeException(new DataException(e.getLocalizedMessage()));
		}
	}

}
