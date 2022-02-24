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

package org.eclipse.birt.report.designer.ui.parameters;

import org.eclipse.birt.report.engine.api.IEngineTask;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;

/**
 * Combo Box parameter
 * 
 */

public class ComboBoxParameter extends ListingParameter {

	/**
	 * Constructor
	 * 
	 * @param handle
	 * @param engineTask
	 */

	public ComboBoxParameter(ScalarParameterHandle handle, IEngineTask engineTask) {
		super(handle, engineTask);
	}

}
