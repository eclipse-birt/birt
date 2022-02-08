/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.model.tests.matrix;

import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.extension.MultiRowItem;

public class Matrix extends MultiRowItem implements IMatrix {

	public Matrix(ExtendedItemHandle handle) {
		super(handle);
	}

	public String getMethod1() {
		return "matrix"; //$NON-NLS-1$
	}
}
