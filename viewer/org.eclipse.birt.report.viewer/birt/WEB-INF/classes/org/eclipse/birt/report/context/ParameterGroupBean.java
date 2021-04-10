/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.context;

import org.eclipse.birt.report.service.api.ParameterGroupDefinition;
import org.eclipse.birt.report.utility.ParameterAccessor;

/**
 * Parameter group bean object used by parameter group related jsp pages. It
 * carries the data shared between front-end jsp page and back-end fragment
 * class. In current implementation, ScalarParameterBean uses request scope.
 * <p>
 */
public class ParameterGroupBean extends ParameterAttributeBean {
	/**
	 * Parameter group definition reference.
	 */
	private ParameterGroupDefinition parameterGroup = null;

	/**
	 * Constructor.
	 * 
	 * @param parameterGroup
	 */
	public ParameterGroupBean(ParameterGroupDefinition parameterGroup) {
		this.parameterGroup = parameterGroup;
	}

	/**
	 * Adapt to ParameterGroupDefinition 's getName( ).
	 * 
	 * @return parameter name.
	 */
	public String getName() {
		if (parameterGroup == null) {
			return null;
		}

		return parameterGroup.getName();
	}

	/**
	 * Adapt to ParameterGroupDefinition's getHelpText( ).
	 * 
	 * @return parameter group help text.
	 */
	public String getToolTip() {
		String toolTip = ""; //$NON-NLS-1$

		if (parameterGroup != null && parameterGroup.getHelpText() != null) {
			toolTip = parameterGroup.getHelpText();
		}

		return ParameterAccessor.htmlEncode(toolTip);
	}
}