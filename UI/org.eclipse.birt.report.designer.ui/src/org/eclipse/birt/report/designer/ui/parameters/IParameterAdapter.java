/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.designer.ui.parameters;

import java.util.Map;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.IGetParameterDefinitionTask;
import org.eclipse.birt.report.model.api.AbstractScalarParameterHandle;
import org.eclipse.swt.widgets.Composite;

/**
 *
 */

public interface IParameterAdapter {

	/**
	 * Create UI the adapt parameter handle
	 *
	 * @param parent
	 */
	void createControl(Composite parent);

	/**
	 * Get parameter name
	 *
	 * @return
	 */
	String getName();

	/**
	 * Get parameter value
	 *
	 * @return
	 */
	String getValue();

	/**
	 * Set the parameter handle of this adapter, must call before createControl
	 *
	 * @param handle
	 */
	void setHandle(AbstractScalarParameterHandle handle);

	/**
	 * Return the adapt parameter handle
	 *
	 * @return
	 */
	AbstractScalarParameterHandle getHandle();

	/**
	 * Set the IGetParameterDefinitionTask
	 *
	 * @param task
	 */
	void setParameterDefinitionTask(IGetParameterDefinitionTask task);

	/**
	 * set additional data
	 *
	 * @param data
	 */
	void setData(Map<String, Object> data);

	/**
	 * test the the input parameter value is valid
	 *
	 * @return
	 * @throws BirtException
	 */
	boolean validate() throws BirtException;

	public abstract class ParameterAdapter implements IParameterAdapter {

		protected AbstractScalarParameterHandle handle;
		protected IGetParameterDefinitionTask parameterDefinitionTask;

		public IGetParameterDefinitionTask getParameterDefinitionTask() {
			return parameterDefinitionTask;
		}

		@Override
		public void setParameterDefinitionTask(IGetParameterDefinitionTask parameterDefinitionTask) {
			this.parameterDefinitionTask = parameterDefinitionTask;
		}

		protected Map<String, Object> data;

		@Override
		public AbstractScalarParameterHandle getHandle() {
			return handle;
		}

		@Override
		public void setHandle(AbstractScalarParameterHandle handle) {
			this.handle = handle;
		}

		public Map<String, Object> getData() {
			return data;
		}

		@Override
		public void setData(Map<String, Object> data) {
			this.data = data;
		}

	}
}
