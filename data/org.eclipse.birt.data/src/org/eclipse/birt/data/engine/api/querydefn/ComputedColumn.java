/*
 *************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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
package org.eclipse.birt.data.engine.api.querydefn;

import java.util.List;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IComputedColumn;
import org.eclipse.birt.data.engine.api.IScriptExpression;

/**
 * Default implementation of
 * {@link org.eclipse.birt.data.engine.api.IComputedColumn} interface.
 * <p>
 */
public class ComputedColumn implements IComputedColumn {
	protected String name;
	protected IBaseExpression expr;
	protected int dataType;
	private String aggrFuntion;
	private List argument;
	private IScriptExpression filter;

	/**
	 * @param name
	 * @param expr
	 */
	public ComputedColumn(String name, String expr) {
		this.name = name;
		this.expr = new ScriptExpression(expr);
		this.dataType = DataType.ANY_TYPE;
		this.aggrFuntion = null;
	}

	/**
	 * Constructs a new computed column with specified name and expression
	 * 
	 * @param name     Name of computed column
	 * @param expr     Expression of computed column
	 * @param dataType data Type of computed column
	 */
	public ComputedColumn(String name, String expr, int dataType) {
		this.name = name;
		this.expr = new ScriptExpression(expr);
		this.dataType = dataType;
		this.aggrFuntion = null;
	}

	/**
	 * @param name
	 * @param expr
	 * @param dataType
	 * @param aggrFunction
	 */
	public ComputedColumn(String name, String expr, int dataType, String aggrFunction, IScriptExpression filter,
			List argument) {
		this.name = name;
		this.expr = expr != null ? new ScriptExpression(expr) : null;
		this.dataType = dataType;
		this.aggrFuntion = aggrFunction;
		this.argument = argument;
		this.filter = filter;
	}

	public ComputedColumn(String name, IScriptExpression expr, int dataType, String aggrFunction,
			IScriptExpression filter, List argument) {
		this.name = name;
		this.expr = expr;
		this.dataType = dataType;
		this.aggrFuntion = aggrFunction;
		this.argument = argument;
		this.filter = filter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IComputedColumn#getName()
	 */
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IComputedColumn#getExpression()
	 */
	public IBaseExpression getExpression() {
		return expr;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IComputedColumn#getDataType()
	 */
	public int getDataType() {
		return this.dataType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IComputedColumn#getAggregateFunction()
	 */
	public String getAggregateFunction() {
		return this.aggrFuntion;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IComputedColumn#getAggregateArgument()
	 */
	public List getAggregateArgument() {
		return this.argument;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IComputedColumn#getAggregateFilter()
	 */
	public IScriptExpression getAggregateFilter() {
		return this.filter;
	}
}
