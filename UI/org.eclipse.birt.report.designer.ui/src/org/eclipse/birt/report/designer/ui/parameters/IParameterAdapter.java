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
	public void createControl(Composite parent);

	/**
	 * Get parameter name
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * Get parameter value
	 * 
	 * @return
	 */
	public String getValue();

	/**
	 * Set the parameter handle of this adapter, must call before createControl
	 * 
	 * @param handle
	 */
	public void setHandle(AbstractScalarParameterHandle handle);

	/**
	 * Return the adapt parameter handle
	 * 
	 * @return
	 */
	public AbstractScalarParameterHandle getHandle();

	/**
	 * Set the IGetParameterDefinitionTask
	 * 
	 * @param task
	 */
	public void setParameterDefinitionTask(IGetParameterDefinitionTask task);

	/**
	 * set additional data
	 * 
	 * @param data
	 */
	public void setData(Map<String, Object> data);

	/**
	 * test the the input parameter value is valid
	 * 
	 * @return
	 * @throws BirtException
	 */
	public boolean validate() throws BirtException;

	public abstract class ParameterAdapter implements IParameterAdapter {

		protected AbstractScalarParameterHandle handle;
		protected IGetParameterDefinitionTask parameterDefinitionTask;

		public IGetParameterDefinitionTask getParameterDefinitionTask() {
			return parameterDefinitionTask;
		}

		public void setParameterDefinitionTask(IGetParameterDefinitionTask parameterDefinitionTask) {
			this.parameterDefinitionTask = parameterDefinitionTask;
		}

		protected Map<String, Object> data;

		public AbstractScalarParameterHandle getHandle() {
			return handle;
		}

		public void setHandle(AbstractScalarParameterHandle handle) {
			this.handle = handle;
		}

		public Map<String, Object> getData() {
			return data;
		}

		public void setData(Map<String, Object> data) {
			this.data = data;
		}

	}
}
