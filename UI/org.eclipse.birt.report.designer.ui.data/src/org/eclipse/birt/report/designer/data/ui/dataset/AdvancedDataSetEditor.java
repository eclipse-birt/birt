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

package org.eclipse.birt.report.designer.data.ui.dataset;

import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.swt.widgets.Shell;

public class AdvancedDataSetEditor extends DataSetEditor {

	public AdvancedDataSetEditor(Shell parentShell, DataSetHandle ds, boolean needToFocusOnOutput,
			boolean isNewlyCreated) {
		super(parentShell, ds, needToFocusOnOutput, isNewlyCreated);

	}

}
