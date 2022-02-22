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

package org.eclipse.birt.report.model.api.scripts;

import java.lang.reflect.Method;

import org.eclipse.birt.report.model.api.metadata.ITemplateMethodInfo;

/**
 * Represents the method information that can provide code snippet as the
 * template.
 */

public class TemplateMethodInfo extends MethodInfo implements ITemplateMethodInfo {

	/**
	 * Default constructor.
	 *
	 * @param method
	 */

	public TemplateMethodInfo(Method method) {
		super(method);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.metadata.ITemplateMethodInfo#
	 * getCodeTemplate()
	 */

	@Override
	public String getCodeTemplate() {
		return ""; //$NON-NLS-1$
	}

}
