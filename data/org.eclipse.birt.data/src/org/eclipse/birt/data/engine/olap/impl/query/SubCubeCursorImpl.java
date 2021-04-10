/*
 *************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */
package org.eclipse.birt.data.engine.olap.impl.query;

import javax.olap.OLAPException;
import javax.olap.cursor.CubeCursor;

import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.api.IBaseQueryResults;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.query.view.BirtCubeView;
import org.mozilla.javascript.Scriptable;

public class SubCubeCursorImpl extends CubeCursorImpl {

	public SubCubeCursorImpl(IBaseQueryResults outerResults, CubeCursor cursor, Scriptable scope, ScriptContext cx,
			ICubeQueryDefinition queryDefn, BirtCubeView view) throws DataException {
		super(outerResults, cursor, scope, cx, queryDefn, view);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.impl.query.CubeCursorImpl#close()
	 */
	public void close() throws OLAPException {
		// do nothing
	}
}
