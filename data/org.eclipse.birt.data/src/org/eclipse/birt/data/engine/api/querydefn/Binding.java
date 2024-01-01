
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
package org.eclipse.birt.data.engine.api.querydefn;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.aggregation.AggregationManager;
import org.eclipse.birt.data.engine.api.aggregation.IAggrFunction;
import org.eclipse.birt.data.engine.api.aggregation.IParameterDefn;
import org.eclipse.birt.data.engine.api.timefunction.ITimeFunction;
import org.eclipse.birt.data.engine.core.DataException;

/**
 *
 */

public class Binding implements IBinding {
	private List<String> aggregateOn;
	private Map<String, IBaseExpression> argument;
	private List<IBaseExpression> orderedArgument;
	private IBaseExpression expr;
	private IBaseExpression filter;
	private String aggrFunc;
	private String name;
	private String displayName;
	private int dataType;
	private boolean exportable;
	private ITimeFunction timeFunction;

	public Binding(String name) {
		this(name, null);
	}

	public Binding(String name, IBaseExpression expr) {
		this.name = name;
		this.expr = expr;
		this.aggregateOn = new ArrayList<>();
		this.argument = new LinkedHashMap<>();
		if (expr != null) {
			this.dataType = expr.getDataType();
		} else {
			this.dataType = DataType.ANY_TYPE;
		}
		this.exportable = true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.api.IBinding#addAggregateOn(java.lang.String)
	 */
	@Override
	public void addAggregateOn(String levelName) throws DataException {
		if (!this.aggregateOn.contains(levelName)) {
			this.aggregateOn.add(levelName);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.api.IBinding#addArgument(org.eclipse.birt.data.
	 * engine.api.IBaseExpression)
	 */
	@Override
	@Deprecated
	public void addArgument(IBaseExpression expr) {
		if (expr != null) {
			if (orderedArgument == null) {
				orderedArgument = new ArrayList<>();
			}
			this.orderedArgument.add(expr);
		}

	}

	@Override
	public void addArgument(String name, IBaseExpression expr) {
		if ("Data Field".equals(name)) {
			this.argument.put("Expression", expr);
		} else {
			this.argument.put(name, expr);
		}
		// orderedArgument list needs to be regenerated
		orderedArgument = null;
	}

	public void removeArgument(IBaseExpression expr) {
		this.argument.values().remove(expr);
		// orderedArgument list needs to be regenerated
		orderedArgument = null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.IBinding#getAggrFunction()
	 */
	@Override
	public String getAggrFunction() {
		return aggrFunc;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.IBinding#getAggregatOns()
	 */
	@Override
	public List<String> getAggregatOns() {
		return this.aggregateOn;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.IBinding#getArguments()
	 */
	@Override
	public List<IBaseExpression> getArguments() throws DataException {
		if (this.orderedArgument != null) {
			// ordered list already computed;
			return this.orderedArgument;
		}

		orderedArgument = new ArrayList<>();

		if (this.aggrFunc == null) {
			return this.orderedArgument;
		}
		IAggrFunction info = AggregationManager.getInstance().getAggregation(this.aggrFunc);

		if (info == null) {
			return this.orderedArgument;
		}

		IParameterDefn[] parameters = info.getParameterDefn();
		if (parameters != null) {
			for (int i = 0; i < parameters.length; i++) {
				IParameterDefn pInfo = parameters[i];
				if (this.argument.get(pInfo.getName()) != null) {
					orderedArgument.add(this.argument.get(pInfo.getName()));
				}
			}
		}
		return orderedArgument;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.IBinding#getDataType()
	 */
	@Override
	public int getDataType() {
		return this.dataType;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.IBinding#getFilter()
	 */
	@Override
	public IBaseExpression getFilter() {
		return this.filter;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.api.IBinding#setAggrFunction(java.lang.String)
	 */
	@Override
	public void setAggrFunction(String functionName) {
		this.aggrFunc = functionName;

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.IBinding#setDataType(int)
	 */
	@Override
	public void setDataType(int type) {
		this.dataType = type;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.api.IBinding#setExpression(java.lang.String)
	 */
	@Override
	public void setExpression(IBaseExpression expr) {
		this.expr = expr;

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.api.IBinding#setFilter(org.eclipse.birt.data.
	 * engine.api.IBaseExpression)
	 */
	@Override
	public void setFilter(IBaseExpression expr) {
		this.filter = expr;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.IBinding#getBindingName()
	 */
	@Override
	public String getBindingName() {
		return this.name;
	}

	/**
	 * @see org.eclipse.birt.data.engine.api.IBinding#getExpression()
	 */
	@Override
	public IBaseExpression getExpression() {
		return this.expr;
	}

	/**
	 * @see org.eclipse.birt.data.engine.api.IBinding#getDisplayName()
	 */
	@Override
	public String getDisplayName() throws DataException {
		return this.displayName == null ? this.name : this.displayName;
	}

	/**
	 * @see org.eclipse.birt.data.engine.api.IBinding#setDisplayName(String)
	 */
	@Override
	public void setDisplayName(String displayName) throws DataException {
		this.displayName = displayName;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if ((obj == null) || (getClass() != obj.getClass())) {
			return false;
		}
		Binding other = (Binding) obj;
		return name.equals(other.getBindingName());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.IBinding#exportable()
	 */
	@Override
	public boolean exportable() throws DataException {
		return this.exportable;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.IBinding#setExportable(boolean)
	 */
	@Override
	public void setExportable(boolean exportable) throws DataException {
		this.exportable = exportable;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.api.IBinding#setTimeFunction(org.eclipse.birt.
	 * data.engine.api.timefunction.ITimeFunction)
	 */
	@Override
	public void setTimeFunction(ITimeFunction timeFunction) {
		this.timeFunction = timeFunction;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.IBinding#getTimeFunction()
	 */
	@Override
	public ITimeFunction getTimeFunction() {
		return this.timeFunction;
	}

	@Override
	public IBinding clone() {
		Binding n = new Binding(this.name, this.expr);
		n.expr = this.expr;
		n.aggrFunc = this.aggrFunc;
		n.dataType = this.dataType;
		n.displayName = this.displayName;
		n.exportable = this.exportable;
		n.filter = this.filter;
		n.timeFunction = this.timeFunction;
		for (String o : this.aggregateOn) {
			n.aggregateOn.add(o);
		}

		if (orderedArgument != null) {
			n.orderedArgument = new ArrayList<>();
			for (IBaseExpression o : this.orderedArgument) {
				n.orderedArgument.add(o);
			}
		}

		for (Map.Entry<String, IBaseExpression> o : this.argument.entrySet()) {
			n.argument.put(o.getKey(), o.getValue());
		}
		return n;
	}
}
