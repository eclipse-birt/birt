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

package org.eclipse.birt.report.designer.ui.parameters;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.engine.api.IEngineTask;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;

/**
 * Static text parameter.
 * 
 */

public class StaticTextParameter extends ScalarParameter {

	/**
	 * Constructor
	 * 
	 * @param handle
	 * @param engineTask
	 */

	public StaticTextParameter(ScalarParameterHandle handle, IEngineTask engineTask) {
		super(handle, engineTask);
	}

	/**
	 * Gets Text parameter value list. contain and only contain one value.
	 */

	public List getValueList() {
		List values = new ArrayList();
		values.add(getDefaultValue());
		return values;
	}

}
