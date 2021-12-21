/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
