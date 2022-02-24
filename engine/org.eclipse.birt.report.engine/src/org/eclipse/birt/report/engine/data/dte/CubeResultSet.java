/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.engine.data.dte;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.olap.OLAPException;
import javax.olap.cursor.CubeCursor;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryResults;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.olap.api.ICubeCursor;
import org.eclipse.birt.data.engine.olap.api.ICubeQueryResults;
import org.eclipse.birt.data.engine.olap.api.query.IBaseCubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ISubCubeQueryDefinition;
import org.eclipse.birt.report.engine.adapter.CubeUtil;
import org.eclipse.birt.report.engine.api.DataSetID;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.data.IDataEngine;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.extension.ICubeResultSet;
import org.eclipse.birt.report.engine.i18n.MessageConstants;

public class CubeResultSet implements ICubeResultSet {

	protected IBaseResultSet parent;

	protected DataSetID id;

	protected String cellId;

	/**
	 * data engine
	 */
	protected IDataEngine dataEngine = null;

	private ExecutionContext context;

	private IBaseCubeQueryDefinition queryDefn;

	private ICubeCursor cube;

	private ICubeQueryResults queryResults;

	/**
	 * DTE's QueryResults's ID.
	 */
	private String queryResultsID;

	protected static Logger logger = Logger.getLogger(CubeResultSet.class.getName());

	// Top level query results
	public CubeResultSet(IDataEngine dataEngine, ExecutionContext context, ICubeQueryDefinition queryDefn,
			ICubeQueryResults rsets) throws BirtException {
		this.parent = null;
		this.context = context;
		this.dataEngine = dataEngine;
		this.queryDefn = queryDefn;
		this.cube = rsets.getCubeCursor();
		if (rsets.getID() != null) {
			this.id = new DataSetID(rsets.getID());
		} else {
			this.id = new DataSetID("cube");
		}
		this.queryResults = rsets;
		this.queryResultsID = rsets.getID();
	}

	// Nest query
	public CubeResultSet(IDataEngine dataEngine, ExecutionContext context, IBaseResultSet parent,
			ICubeQueryDefinition queryDefn, ICubeQueryResults rsets) throws BirtException {
		assert parent != null;
		this.parent = parent;
		this.cube = rsets.getCubeCursor();
		if (rsets.getID() != null) {
			this.id = new DataSetID(rsets.getID());
		} else {
			this.id = new DataSetID("cube");
		}
		this.context = context;
		this.dataEngine = dataEngine;
		this.queryDefn = queryDefn;
		this.queryResults = rsets;
		this.queryResultsID = rsets.getID();
	}

	// Sub cube query
	public CubeResultSet(IDataEngine dataEngine, ExecutionContext context, IBaseResultSet parent,
			ISubCubeQueryDefinition queryDefn, ICubeQueryResults rsets) throws BirtException {
		assert parent != null;
		this.parent = parent;
		this.cube = rsets.getCubeCursor();
		if (rsets.getID() != null) {
			this.id = new DataSetID(rsets.getID());
		} else {
			this.id = new DataSetID("cube");
		}
		this.context = context;
		this.dataEngine = dataEngine;
		this.queryDefn = queryDefn;
		this.queryResults = rsets;
		this.queryResultsID = rsets.getID();
	}

	public String getQueryResultsID() {
		return queryResultsID;
	}

	public CubeCursor getCubeCursor() {
		return cube;
	}

	public String getCellIndex() {
		try {
			cellId = CubeUtil.getPositionID(cube);
		} catch (OLAPException e) {
			context.addException(new EngineException(MessageConstants.CUBE_POSITION_ERROR, e));
		}
		return cellId;
	}

	public void close() {
		// remove the data set from the data set list
		try {
			if (queryResults != null) {
				queryResults.close();
			}
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			// context.addException( ex );
		}
		try {
			if (cube != null) {
				cube.close();
			}
		} catch (Exception ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			// context.addException( ex );
		}
	}

	public Object evaluate(String expr) throws BirtException {
		return context.evaluate(expr);
	}

	public Object evaluate(String language, String expr) throws BirtException {
		return context.evaluateInlineScript(language, expr);
	}

	public Object evaluate(IBaseExpression expr) throws BirtException {
		if (expr instanceof IScriptExpression) {
			IScriptExpression scriptExpression = (IScriptExpression) expr;
			return context.evaluate(scriptExpression.getText());
		}
		if (expr instanceof IConditionalExpression) {
			return context.evaluateCondExpr((IConditionalExpression) expr);
		}
		return null;
	}

	public DataSetID getID() {
		return id;
	}

	public IBaseResultSet getParent() {
		return parent;
	}

	public IBaseQueryResults getQueryResults() {
		return queryResults;
	}

	public String getRawID() {
		return getCellIndex();
	}

	public int getType() {
		return CUBE_RESULTSET;
	}

	public void skipTo(String cellIndex) throws BirtException {
		try {
			CubeUtil.positionCursor(cube, cellIndex);
		} catch (OLAPException e) {
			throw new EngineException(MessageConstants.SKIP_ERROR, e);
		}
	}

	public IBaseCubeQueryDefinition getCubeQuery() {
		return this.queryDefn;
	}

}
