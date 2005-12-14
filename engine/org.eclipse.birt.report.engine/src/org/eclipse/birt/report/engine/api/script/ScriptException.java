/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.engine.api.script;

/** Exception thrown in scripting.
*/
public class ScriptException extends Exception
{

	private static final long serialVersionUID = -8895956245804505077L;
	
	public ScriptException (String message) {
		super(message);
	}

}
