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

package org.eclipse.birt.report.designer.ui.preview.parameter;

import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.IEngineTask;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;

/**
 * Adaptor class of <code>IParameter</code>
 *
 */

public abstract class ScalarParam implements IParameter {

	/**
	 * Parameter Group
	 */
	protected IParamGroup group = null;

	private String selectionValue = null;

	/**
	 * Gets selection value.
	 *
	 * @return selection value.
	 */
	@Override
	public String getSelectionValue() {
		return selectionValue;
	}

	/**
	 * Sets selection value.
	 *
	 * @param value
	 */
	@Override
	public void setSelectionValue(String value) {
		selectionValue = value;
	}

	/**
	 * Sets parameter group
	 */
	@Override
	public void setParentGroup(IParamGroup group) {
		this.group = group;
	}

	/**
	 * Gets parameter group
	 */
	@Override
	public IParamGroup getParentGroup() {
		return group;
	}

	/**
	 * Scalar parameter handle.
	 */
	protected ScalarParameterHandle handle;

	/**
	 * engine task.
	 */
	protected IEngineTask engineTask;

	/**
	 * Constructor
	 *
	 * @param handle     scalar parameter handle.
	 * @param engineTask engine task.
	 */

	public ScalarParam(ScalarParameterHandle handle, IEngineTask engineTask) {
		this.handle = handle;
		this.engineTask = engineTask;
	}

	/**
	 * Gets values of parameter.
	 *
	 * @return value list.
	 */
	@Override
	public abstract List getValueList();

	/**
	 * Gets default value.
	 *
	 * @return default value
	 */

	@Override
	public String getDefaultValue() {
		return handle.getDefaultValue();
	}

	/**
	 * Get parameter handle.
	 *
	 * @return parameter handle.
	 */

	public ScalarParameterHandle getHandle() {
		return handle;
	}

	@Override
	public String format(String input) throws BirtException {
		return FormatUtil.format(handle, input);
	}

	@Override
	public Object converToDataType(Object value) throws BirtException {
		return DataTypeConvertUtil.convert(value, handle.getDataType());
	}

	/**
	 * Gets isRequired property.
	 *
	 * @return
	 */
	@Override
	public boolean isRequired() {
		return handle.isRequired();
	}

}
