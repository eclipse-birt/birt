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

package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import org.eclipse.birt.report.model.api.activity.SemanticException;

public abstract class AbstractDescriptorProvider implements IDescriptorProvider {

	private boolean canReset = false;

	public boolean canReset() {
		return canReset;
	}

	public void enableReset(boolean canReset) {
		this.canReset = canReset;
	}

	public void reset() throws SemanticException {
		if (canReset())
			save(null);
	}

}
