/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
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
package org.eclipse.birt.report.engine.emitter.ods.layout;

import org.eclipse.birt.report.engine.odf.AbstractOdfEmitterContext;

public class OdsContext extends AbstractOdfEmitterContext {
	private boolean wrappingText = true;

	private Boolean hideGridlines;

	public void setWrappingText(boolean wrappingText) {
		this.wrappingText = wrappingText;
	}

	public boolean getWrappingText() {
		return wrappingText;
	}

	public boolean getHideGridlines() {
		return this.hideGridlines;
	}

	public void setHideGridlines(Boolean hideGridlines) {
		this.hideGridlines = hideGridlines;
	}
}
