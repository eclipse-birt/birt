
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

import javax.olap.OLAPException;

import org.eclipse.birt.data.engine.olap.api.ICubeCursor;
import org.eclipse.birt.data.engine.script.ScriptConstants;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * The scriptable object which bound with key word "row" in cube query.
 */

public class JSCubeBindingObject extends ScriptableObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8830069667117258594L;
	private ICubeCursor cursor;

	public JSCubeBindingObject(ICubeCursor cursor) {
		this.cursor = cursor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mozilla.javascript.ScriptableObject#get(java.lang.String,
	 * org.mozilla.javascript.Scriptable)
	 */
	public Object get(String arg0, Scriptable scope) {
		try {
			if (ScriptConstants.OUTER_RESULT_KEYWORD.equals(arg0))
				return cursor.getObject(ScriptConstants.OUTER_RESULT_KEYWORD);
			return cursor.getObject(arg0);
		} catch (OLAPException e) {
			throw Context.reportRuntimeError(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mozilla.javascript.ScriptableObject#get(java.lang.String,
	 * org.mozilla.javascript.Scriptable)
	 */
	public Object get(int arg0, Scriptable scope) {
		try {
			return cursor.getObject(String.valueOf(arg0));
		} catch (OLAPException e) {
			throw Context.reportRuntimeError(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mozilla.javascript.ScriptableObject#getClassName()
	 */
	public String getClassName() {
		return "JSCubeBindingObject";
	}

}
