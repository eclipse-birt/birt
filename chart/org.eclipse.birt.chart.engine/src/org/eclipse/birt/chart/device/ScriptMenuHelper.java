/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.device;

import org.eclipse.birt.chart.model.attribute.ScriptValue;
import org.eclipse.birt.core.script.JavascriptEvalUtil;


/**
 * The helper class is used to generate scripts for Action Value of chart model.
 * 
 * @since 2.5.2
 */

public class ScriptMenuHelper implements IScriptMenuHelper
{
	private static IScriptMenuHelper factory = new ScriptMenuHelper( );

	/**
	 * Initializes instance of this object.
	 * 
	 * @param tFactory
	 */
	public static void initInstance( IScriptMenuHelper tFactory )
	{
		factory = tFactory;
	}

	/**
	 * Returns instance of this object.
	 * 
	 * @return
	 */
	public static IScriptMenuHelper instance( )
	{
		return factory;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.chart.device.IScriptMenuHelper#getScriptValueJS(int, org.eclipse.birt.chart.model.attribute.ScriptValue)
	 */
	public String getScriptValueJS( int index, ScriptValue sv )
	{
		StringBuilder sb = new StringBuilder();
		sb.append( "\t mii.text = '" + sv.getLabel( ).getCaption( ).getValue( ) + "';\n" );//$NON-NLS-1$//$NON-NLS-2$
		sb.append( "\t mii.actionType = BirtChartInteractivityActions.INVOKE_SCRIPTS;\n" ); //$NON-NLS-1$
		String script = sv.getScript( );
		sb.append( "\t mii.actionValue = " + JavascriptEvalUtil.transformToJsExpression( script ) + ";\n" ); //$NON-NLS-1$//$NON-NLS-2$
		sb.append( "\t\t menuInfo.addItemInfo(mii);\n" ); //$NON-NLS-1$

		return sb.toString( );
	}
}
