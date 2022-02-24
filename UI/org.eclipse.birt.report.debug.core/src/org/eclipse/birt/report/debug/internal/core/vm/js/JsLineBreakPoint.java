/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.debug.internal.core.vm.js;

import org.eclipse.birt.report.debug.internal.core.vm.VMBreakPoint;

/**
 * JsLineBreakPoint
 */
public class JsLineBreakPoint implements VMBreakPoint {

	private static final long serialVersionUID = 1L;

	protected String name;
	protected int lineNo;

	public JsLineBreakPoint(String name, int lineNo) {
		this.name = name;
		this.lineNo = lineNo;
	}

	public String getName() {
		return name;
	}

	public int getLineNo() {
		return lineNo;
	}

	public int hashCode() {
		if (name != null) {
			return name.hashCode() ^ lineNo;
		}
		return lineNo;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof JsLineBreakPoint)) {
			return false;
		}

		JsLineBreakPoint that = (JsLineBreakPoint) obj;

		return ((name == null && that.name == null) || (name != null && name.equals(that.name)))
				&& (this.lineNo == that.lineNo);

	}
}
