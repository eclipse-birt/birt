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

package org.eclipse.birt.report.model.parser;

import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.core.DesignElement;

/**
 * Base class for parsing all kinds of parameter.
 * 
 */

public abstract class ParameterState extends ReportElementState {

	/**
	 * Constructs the parameter state with the design parser handler, the container
	 * element and the container slot of the parameter.
	 * 
	 * @param handler      the design file parser handler
	 * @param theContainer the container of this parameter
	 * @param slot         the slot ID of the slot where the parameter is stored.
	 */

	public ParameterState(ModuleParserHandler handler, DesignElement theContainer, int slot) {
		super(handler, theContainer, slot);
	}

	/**
	 * Constructs the parameter state with design file parser handler.
	 * 
	 * @param handler the design file parser handler
	 */

	ParameterState(ModuleParserHandler handler) {
		super(handler, handler.getModule(), IModuleModel.PARAMETER_SLOT);
	}
}
