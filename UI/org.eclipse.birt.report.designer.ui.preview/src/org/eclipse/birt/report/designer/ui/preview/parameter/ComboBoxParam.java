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

package org.eclipse.birt.report.designer.ui.preview.parameter;

import org.eclipse.birt.report.designer.ui.preview.parameter.ListingParam;
import org.eclipse.birt.report.engine.api.IEngineTask;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;

/**
 * Combo Box parameter
 *
 */

public class ComboBoxParam extends ListingParam {

	/**
	 * Constructor
	 * 
	 * @param handle
	 * @param engineTask
	 */

	public ComboBoxParam(ScalarParameterHandle handle, IEngineTask engineTask) {
		super(handle, engineTask);
	}

}
