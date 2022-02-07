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

package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import java.util.List;

import org.eclipse.birt.report.designer.ui.views.attributes.providers.FilterModelProvider;
import org.eclipse.birt.report.model.api.ParamBindingHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.PropertyEvent;

/**
 * The class extends <code>AbstractFormHandleProvider</code> and declares two
 * new methods for UI to rebuilt filter page when the reference data set or item
 * handle are changed.
 * 
 * @since 2.3
 */
public abstract class AbstractFilterHandleProvider extends AbstractFormHandleProvider {

	/**
	 * The current selections in outline or Editor.
	 */
	protected List<Object> contentInput;

	/**
	 * Model processor, provide data process of Filter model.
	 */
	protected FilterModelProvider modelAdapter;

	protected ParamBindingHandle[] bindingParams = null;

	/**
	 * Returns a concrete filter provider for current data set or binding reference.
	 * 
	 * @return
	 */
	public abstract IFormProvider getConcreteFilterProvider();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.
	 * AbstractFormHandleProvider#needRebuilded(org.eclipse.birt.report.model.api.
	 * activity.NotificationEvent)
	 */
	public boolean needRebuilded(NotificationEvent event) {
		if (event instanceof PropertyEvent) {
			String propertyName = ((PropertyEvent) event).getPropertyName();
			if (ReportItemHandle.DATA_SET_PROP.equals(propertyName)
					|| ReportItemHandle.DATA_BINDING_REF_PROP.equals(propertyName)
					|| ReportItemHandle.CUBE_PROP.equals(propertyName)) {
				return true;
			}
		}

		return false;
	}

	public List<Object> getContentInput() {
		return contentInput;
	}

	public void setContentInput(List<Object> contentInput) {
		this.contentInput = contentInput;
	}

	public FilterModelProvider getModelAdapter() {
		return modelAdapter;
	}

	public void setModelAdapter(FilterModelProvider modelAdapter) {
		this.modelAdapter = modelAdapter;
	}

	public ParamBindingHandle[] getBindingParams() {
		return bindingParams;
	}

	public void setBindingParams(ParamBindingHandle[] bindingParams) {
		this.bindingParams = bindingParams;
	}
}
