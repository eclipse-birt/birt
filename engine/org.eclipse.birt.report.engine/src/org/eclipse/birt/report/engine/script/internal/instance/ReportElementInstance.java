/*******************************************************************************
 * Copyright (c) 2005,2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.script.internal.instance;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.script.IRowData;
import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.engine.api.script.instance.IReportElementInstance;
import org.eclipse.birt.report.engine.api.script.instance.IScriptStyle;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.ir.Expression;
import org.eclipse.birt.report.engine.ir.ReportElementDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.script.internal.ElementUtil;
import org.eclipse.birt.report.engine.script.internal.RowData;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;

public class ReportElementInstance implements IReportElementInstance {

	protected IContent content;

	private ExecutionContext context;

	private RowData rowData;

	protected RunningState runningState;

	public ReportElementInstance(IContent content, ExecutionContext context, RunningState runningState) {
		this.content = content;
		this.context = context;
		this.runningState = runningState;
	}

	protected ReportElementInstance(ExecutionContext context, RunningState runningState) {
		this.context = context;
		this.runningState = runningState;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api.script.instance.IReportInstance#getStyle()
	 */
	public IScriptStyle getStyle() {
		return new StyleInstance(content.getStyle(), runningState);
	}

	public Object getNamedExpressionValue(String name) {
		return getUserPropertyValue(name);
	}

	public Object getUserPropertyValue(String name) {
		Map<String, Object> values = content.getUserProperties();
		if (values != null && values.containsKey(name)) {
			return values.get(name);
		}
		// try to search the constant value in the design element
		ReportElementDesign design = (ReportElementDesign) content.getGenerateBy();
		Map<String, Expression> props = design.getUserProperties();
		if (props != null) {
			Expression expr = props.get(name);
			if (expr != null && expr.getType() == Expression.CONSTANT) {
				Expression.Constant cs = (Expression.Constant) expr;
				return cs.getValue();
			}
		}
		return null;
	}

	public void setUserPropertyValue(String name, Object value) throws ScriptException {
		Map<String, Object> values = content.getUserProperties();
		if (values == null) {
			values = new HashMap<String, Object>();
			content.setUserProperties(values);
		}
		values.put(name, value);
	}

	public IReportElementInstance getParent() throws ScriptException {
		try {
			return ElementUtil.getInstance(content.getParent(), context, runningState);
		} catch (BirtException e) {
			ScriptException scriptException = new ScriptException(e.getLocalizedMessage());
			scriptException.initCause(e);
			throw scriptException;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.IReportInstance#
	 * getHorizontalPosition()
	 */
	public String getHorizontalPosition() {
		DimensionType x = content.getX();
		if (x != null) {
			return x.toString();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.IReportInstance#
	 * setHorizontalPosition(java.lang.String)
	 */
	public void setHorizontalPosition(String position) {
		content.setX(DimensionType.parserUnit(position));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.IReportInstance#
	 * getVerticalPosition()
	 */
	public String getVerticalPosition() {
		DimensionType y = content.getY();
		if (y != null) {
			return y.toString();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.IReportInstance#
	 * setVerticalPosition(java.lang.String)
	 */
	public void setVerticalPosition(String position) {
		content.setY(DimensionType.parserUnit(position));
	}

	public String getWidth() {
		DimensionType width = content.getWidth();
		if (width != null) {
			return width.toString();
		}
		return null;
	}

	public void setWidth(String width) {
		content.setWidth(DimensionType.parserUnit(width));
	}

	public String getHeight() {
		DimensionType height = content.getHeight();
		if (height != null) {
			return height.toString();
		}
		return null;
	}

	public void setHeight(String height) {
		content.setHeight(DimensionType.parserUnit(height));
	}

	public IRowData getRowData() throws ScriptException {
		if (rowData != null) {
			return rowData;
		}
		// see if the report element has query
		Object objGen = content.getGenerateBy();
		if (objGen instanceof ReportItemDesign) {
			ReportItemDesign design = (ReportItemDesign) objGen;
			if (design.getQuery() != null) {
				DesignElementHandle handle = design.getHandle();
				if (handle instanceof ReportItemHandle) {
					// get the current data set
					IBaseResultSet rset = context.getResultSet();
					// using the handle and the rste to create the row data.
					rowData = new RowData(rset, (ReportItemHandle) handle);
					return rowData;
				}
			}
		}
		// try to return the parnt's rowData
		IReportElementInstance parent = this.getParent();
		if (parent != null) {
			return parent.getRowData();
		}
		// root element, return null
		return null;
	}
}
