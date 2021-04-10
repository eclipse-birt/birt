/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.TemplateException;
import org.eclipse.birt.report.model.command.TemplateCommand;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.SimpleDataSet;
import org.eclipse.birt.report.model.elements.TemplateDataSet;

/**
 * Handle for template data sets. A template data set is a place holder to
 * generate a real data set element.
 * 
 * @see org.eclipse.birt.report.model.api.TemplateElementHandle
 */

public class TemplateDataSetHandle extends TemplateElementHandle {

	/**
	 * Constructs a handle for the given design and design element. The application
	 * generally does not create handles directly. Instead, it uses one of the
	 * navigation methods available on other element handles.
	 * 
	 * @param module  the module
	 * @param element the model representation of the element
	 */

	public TemplateDataSetHandle(Module module, DesignElement element) {
		super(module, element);
	}

	/**
	 * Transforms the current template data set to the given real data set.
	 * 
	 * @param dataSetHandle the real data set handle to replace this template data
	 *                      set
	 * @throws SemanticException if this template data set has no template
	 *                           definition or some containing contexts don't match
	 */

	public void transformToDataSet(DataSetHandle dataSetHandle) throws SemanticException {
		if (getRoot() == null)
			throw new TemplateException(getElement(),
					TemplateException.DESIGN_EXCEPTION_CREATE_TEMPLATE_ELEMENT_FORBIDDEN);
		TemplateCommand cmd = new TemplateCommand(getModule(), getElement().getContainerInfo());
		cmd.transformToDataSet((TemplateDataSet) getElement(), (SimpleDataSet) dataSetHandle.getElement());
	}
}
