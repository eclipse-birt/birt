
/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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

import java.util.Map;

import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.util.filter.IFacttableRow;
import org.eclipse.birt.data.engine.script.ScriptConstants;
import org.mozilla.javascript.Scriptable;

/**
 * 
 */

public class FacttableMeasureJSObjectPopulator implements IJSObjectPopulator {

	private DummyJSFacttableMeasureAccessor measureObj;
	private Scriptable scope;
	private ScriptContext cx;
	private Map computedMeasures;

	public FacttableMeasureJSObjectPopulator(Scriptable scope, Map computedMeasures, ScriptContext cx) {
		this.scope = scope;
		this.computedMeasures = computedMeasures;
		this.cx = cx;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.util.IJSObjectPopulator#doInit()
	 */
	public void doInit() throws DataException {
		this.measureObj = new DummyJSFacttableMeasureAccessor(this.computedMeasures, scope, this.cx);
		this.scope.put(ScriptConstants.MEASURE_SCRIPTABLE, this.scope, this.measureObj);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.olap.util.IJSObjectPopulator#setData(java.lang.
	 * Object)
	 */
	public void setData(Object resultRow) {
		assert resultRow instanceof IFacttableRow;

		this.measureObj.setResultRow((IFacttableRow) resultRow);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.util.IJSObjectPopulator#close()
	 */
	public void cleanUp() {
		this.scope.delete(ScriptConstants.MEASURE_SCRIPTABLE);// $NON-NLS-1$
		this.scope.setParentScope(null);
	}

}
