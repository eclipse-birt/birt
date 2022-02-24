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

package org.eclipse.birt.report.model.elements;

import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.util.ModelUtil;

/**
 * Creates a new template element from a report item or a data set.
 */

public class TemplateFactory {

	/**
	 * Creates a template element from the given default element.
	 * 
	 * @param module         the module of the template to insert
	 * @param defaultElement the base element
	 * @param name           name of the created template element
	 * @return the created template element, or <code>null</code> if the default
	 *         element is not a report item or a data set
	 */

	public static TemplateElement createTemplate(Module module, DesignElement defaultElement, String name) {
		assert defaultElement != null;
		if (!ModelUtil.isTemplateSupported(defaultElement))
			return null;

		if (defaultElement instanceof ReportItem) {
			if (StringUtil.isBlank(name))
				return new TemplateReportItem();
			return new TemplateReportItem(name);

		} else if (defaultElement instanceof SimpleDataSet) {
			TemplateDataSet template = new TemplateDataSet(name);
			assert module != null;
			module.makeUniqueName(template);
			return template;
		}

		return null;
	}
}
