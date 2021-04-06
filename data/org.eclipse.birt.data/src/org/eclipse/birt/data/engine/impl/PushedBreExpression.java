/*******************************************************************************
 * Copyright (c) 2012 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
