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

package org.eclipse.birt.data.engine.olap.util;

import java.util.List;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.util.filter.IResultRow;
import org.eclipse.birt.data.engine.script.ScriptConstants;
import org.mozilla.javascript.Scriptable;

/**
 * 
 */

public class DimensionJSObjectPopulator implements IJSObjectPopulator {
	//
	private DummyJSLevels dimObj;
	private Scriptable scope;
	private String dimensionName;
	private List levelNames;

	/**
	 * 
	 * @param scope
	 * @param dimensionName
	 * @param levelNames
	 */
	public DimensionJSObjectPopulator(Scriptable scope, String dimensionName, List levelNames) {
		this.scope = scope;
		this.dimensionName = dimensionName;
		this.levelNames = levelNames;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.util.IJSObjectPopulator#doInit()
	 */
	public void doInit() throws DataException {
		this.dimObj = new DummyJSLevels(dimensionName);
		DummyJSDimensionObject dimObj = new DummyJSDimensionObject(this.dimObj, levelNames);

		scope.put(ScriptConstants.DIMENSION_SCRIPTABLE, scope, new DummyJSDimensionAccessor(dimensionName, dimObj));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.olap.util.IJSObjectPopulator#setData(java.lang.
	 * Object)
	 */
	public void setData(Object resultRow) {
		assert resultRow instanceof IResultRow;
		dimObj.setResultRow((IResultRow) resultRow);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.util.IJSObjectPopulator#cleanUp()
	 */
	public void cleanUp() {
		this.scope.delete(ScriptConstants.DIMENSION_SCRIPTABLE);
		this.scope.setParentScope(null);
	}

}
