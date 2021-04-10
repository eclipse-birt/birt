
/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.olap.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.core.script.ScriptExpression;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.data.api.IComputedMeasureHelper;
import org.eclipse.birt.data.engine.olap.data.api.MeasureInfo;
import org.eclipse.birt.data.engine.olap.impl.query.ComputedMeasureDefinition;
import org.eclipse.birt.data.engine.olap.util.filter.IFacttableRow;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;
import org.mozilla.javascript.Scriptable;

/**
 * 
 */

public class ComputedMeasureHelper implements IComputedMeasureHelper {
	//
	private Scriptable scope;
	private FacttableMeasureJSObjectPopulator populator;
	private MeasureInfo[] measureInfos;
	private Map exprMap;
	private ScriptContext cx;

	/**
	 * 
	 * @param scope
	 * @param computedColumns
	 * @throws DataException
	 */
	public ComputedMeasureHelper(Scriptable scope, ScriptContext cx, List computedColumns) throws DataException {
		this.exprMap = new HashMap();
		this.scope = scope;
		this.cx = cx;

		this.measureInfos = new MeasureInfo[computedColumns.size()];
		for (int i = 0; i < measureInfos.length; i++) {
			ComputedMeasureDefinition ccd = ((ComputedMeasureDefinition) computedColumns.get(i));
			this.measureInfos[i] = new MeasureInfo(ccd.getName(), ccd.getDataType());
			this.exprMap.put(ccd.getName(), ccd.getExpression());
		}

		this.populator = new FacttableMeasureJSObjectPopulator(scope, this.exprMap, this.cx);
		this.populator.doInit();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.data.api.IComputedMeasureHelper#
	 * computeMeasureValues(org.eclipse.birt.data.engine.olap.util.filter.
	 * IFacttableRow)
	 */
	public Object[] computeMeasureValues(IFacttableRow factTableRow) throws DataException {
		this.populator.setData(factTableRow);

		Object[] result = new Object[this.measureInfos.length];
		for (int i = 0; i < this.measureInfos.length; i++) {
			try {
				result[i] = ScriptEvalUtil.evalExpr(
						(IBaseExpression) this.exprMap.get(this.measureInfos[i].getMeasureName()), cx.newContext(scope),
						ScriptExpression.defaultID, 0);
			} catch (Exception e) {
				result[i] = null;
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.data.api.IComputedMeasureHelper#
	 * getAllComputedMeasureInfos()
	 */
	public MeasureInfo[] getAllComputedMeasureInfos() {
		return measureInfos;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.api.IComputedMeasureHelper#cleanUp()
	 */
	public void cleanUp() {
		this.populator.cleanUp();
	}
}
