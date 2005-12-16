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
package org.eclipse.birt.report.engine.api.script.element;

import org.eclipse.birt.report.engine.api.script.ScriptException;

public interface IDataSource
{

	String getExtensionID( );
	
	String getPrivateDriverProperty( String name );
	
	void setPrivateDriverProperty( String name, String value ) throws ScriptException;
}
