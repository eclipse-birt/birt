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
package org.eclipse.birt.report.engine.api.script.instance;

import org.eclipse.birt.report.engine.api.script.ScriptException;

public interface IReportElementInstance
{

	IScriptStyle getStyle( );

	Object getNamedExpressionValue( String name );

	Object getUserPropertyValue( String name );

	void setUserPropertyValue( String name, Object value )
			throws ScriptException;

	IReportElementInstance getParent( );

}
