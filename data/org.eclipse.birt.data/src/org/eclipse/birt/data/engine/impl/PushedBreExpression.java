/*******************************************************************************
 * Copyright (c) 2012 Actuate Corporation.
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
package org.eclipse.birt.data.engine.impl;

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;

public class PushedBreExpression extends ScriptExpression implements IPushedDownExpression {

	private IBaseExpression originalBreScript;

	public PushedBreExpression(String effectiveJavascriptScript, IBaseExpression originalScript) {
		super(effectiveJavascriptScript);
		this.originalBreScript = originalScript;
	}

	public IBaseExpression getOriginalExpression() {
		return this.originalBreScript;
	}

}
