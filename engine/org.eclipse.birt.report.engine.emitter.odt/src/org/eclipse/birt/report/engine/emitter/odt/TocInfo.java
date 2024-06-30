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
package org.eclipse.birt.report.engine.emitter.odt;

public class TocInfo {
	public String tocValue;
	public int tocLevel;

	TocInfo(String tocValue, int tocLevel) {
		this.tocValue = tocValue;
		this.tocLevel = tocLevel;
	}
}
