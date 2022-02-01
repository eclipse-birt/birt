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
package org.eclipse.birt.report.engine.script.internal;

import org.mozilla.javascript.ScriptableObject;

public class NativeContent extends ScriptableObject {
	private static final long serialVersionUID = -3429963455948196487L;

	public String getClassName() {
		return "ElementState";
	}
}
