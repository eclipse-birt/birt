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

package org.eclipse.birt.report.model.api;

import java.util.Iterator;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.TemplateException;
import org.eclipse.birt.report.model.command.TemplateCommand;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.TemplateReportItem;

/**
 * Handle for template report items. A template report item is a place holder to
 * generate a real report item.
 * 
 * @see org.eclipse.birt.report.model.api.TemplateElementHandle
 */

public class TemplateReportItemHandle extends TemplateElementHandle {

	/**
	 * Constructs a handle for the given design and design element. The application
	 * generally does not create handles directly. Instead, it uses one of the
	 * navigation methods available on other element handles.
	 * 
	 * @param module  the module
	 * @param element the model representation of the element
	 */

	public TemplateReportItemHandle(Module module, DesignElement element) {
		super(module, element);
	}

	/**
	 * Transforms the current template report item to the given real report item.
	 * 
	 * @param reportItemHandle the real report item handle to replace this template
	 *                         report item
	 * @throws SemanticException if this template report item has no template
	 *                           definition or some containing contexts don't match
	 */

	public void transformToReportItem(ReportItemHandle reportItemHandle) throws SemanticException {
		if (getRoot() == null)
			throw new TemplateException(getElement(),
					TemplateException.DESIGN_EXCEPTION_CREATE_TEMPLATE_ELEMENT_FORBIDDEN);

		TemplateCommand cmd = new TemplateCommand(getModule(), getElement().getContainerInfo());
		cmd.transformToReportItem((TemplateReportItem) getElement(), (ReportItem) reportItemHandle.getElement());
	}

	/**
	 * Returns visibility rules defined on the template report item. The element in
	 * the iterator is the corresponding <code>StructureHandle</code> that deal with
	 * a <code>HideRule</code> in the list.
	 * 
	 * @return the iterator for visibility rules defined on this template report
	 *         item.
	 * 
	 * @see org.eclipse.birt.report.model.api.elements.structures.HideRule
	 */

	public Iterator visibilityRulesIterator() {
		PropertyHandle propHandle = getPropertyHandle(TemplateReportItem.VISIBILITY_PROP);
		assert propHandle != null;
		return propHandle.iterator();
	}

}
