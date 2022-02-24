/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.data.ui.dataset;

import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.swt.widgets.Shell;

public class AdvancedDataSetEditor extends DataSetEditor {

	public AdvancedDataSetEditor(Shell parentShell, DataSetHandle ds, boolean needToFocusOnOutput,
			boolean isNewlyCreated) {
		super(parentShell, ds, needToFocusOnOutput, isNewlyCreated);

	}

}
