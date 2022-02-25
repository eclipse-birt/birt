/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

package org.eclipse.birt.report.engine.script.internal.element;

import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.engine.api.script.element.IDataBinding;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.simpleapi.SimpleElementFactory;

/**
 * Implements of DataBinding.
 */

public class DataBindingImpl implements IDataBinding {

	private org.eclipse.birt.report.model.api.simpleapi.IDataBinding dataBindingImpl;

	/**
	 * Constructor
	 *
	 * @param columnHandle
	 */

	public DataBindingImpl() {
		dataBindingImpl = SimpleElementFactory.getInstance().createDataBinding();
	}

	/**
	 * Constructor
	 *
	 * @param columnHandle
	 */

	public DataBindingImpl(ComputedColumnHandle columnHandle) {
		dataBindingImpl = SimpleElementFactory.getInstance().createDataBinding(columnHandle);
	}

	/**
	 * Constructor
	 *
	 * @param column
	 */

	public DataBindingImpl(ComputedColumn column) {
		dataBindingImpl = SimpleElementFactory.getInstance().createDataBinding(column);
	}

	public DataBindingImpl(org.eclipse.birt.report.model.api.simpleapi.IDataBinding columnBindingImpl) {
		dataBindingImpl = columnBindingImpl;
	}

	@Override
	public String getAggregateOn() {
		return dataBindingImpl.getAggregateOn();
	}

	@Override
	public String getDataType() {
		return dataBindingImpl.getDataType();
	}

	@Override
	public String getExpression() {
		return dataBindingImpl.getExpression();
	}

	@Override
	public String getExpressionType() {
		return dataBindingImpl.getExpressionType();
	}

	@Override
	public void setExpressionType(String type) throws ScriptException {
		try {
			dataBindingImpl.setExpressionType(type);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	@Override
	public String getName() {
		return dataBindingImpl.getName();
	}

	@Override
	public void setAggregateOn(String on) throws ScriptException {
		try {
			dataBindingImpl.setAggregateOn(on);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}

	}

	@Override
	public void setDataType(String dataType) throws ScriptException {
		try {
			dataBindingImpl.setDataType(dataType);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	@Override
	public void setExpression(String expression) throws ScriptException {

		// expression is required.

		try {
			dataBindingImpl.setExpression(expression);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}

	}

	@Override
	public void setName(String name) throws ScriptException {
		// name is required.

		try {

			dataBindingImpl.setName(name);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}

	}

	@Override
	public IStructure getStructure() {
		return dataBindingImpl.getStructure();
	}

}
