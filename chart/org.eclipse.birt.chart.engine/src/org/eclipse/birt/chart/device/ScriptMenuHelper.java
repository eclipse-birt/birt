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
import org.eclipse.birt.chart.render.IActionRenderer;
import org.eclipse.birt.chart.script.ScriptHandler;
import org.eclipse.birt.core.script.JavascriptEvalUtil;

import com.ibm.icu.util.ULocale;

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
	 * @return IScriptMenuHelper instance
	 */
	public static IScriptMenuHelper instance( )
	{
		return factory;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.chart.device.IScriptMenuHelper#getScriptValueJS(int, org.eclipse.birt.chart.model.attribute.ScriptValue)
	 */
	public String getScriptValueJS( int index, ScriptValue sv, ULocale locale )
	{
		StringBuilder sb = new StringBuilder();
		sb.append( "\t mii.text = '" + sv.getLabel( ).getCaption( ).getValue( ) + "';\n" );//$NON-NLS-1$//$NON-NLS-2$
		sb.append( "\t mii.actionType = BirtChartInteractivityActions.INVOKE_SCRIPTS;\n" ); //$NON-NLS-1$
		String script = sv.getScript( );
		sb.append( "\t mii.actionValue = \"" + wrapScriptsAsFunction( script ) + "\"\n" ); //$NON-NLS-1$//$NON-NLS-2$
		sb.append( "\t\t menuInfo.addItemInfo(mii);\n" ); //$NON-NLS-1$

		return sb.toString( );
	}

	/**
	 * Wraps specified script into a function for the calling by using eval
	 * function.
	 * 
	 * @param script
	 * @return string script
	 */
	public static String wrapScriptsAsFunction( String script )
	{
		String f = "var _callScripts=function( evt, " + ScriptHandler.BASE_VALUE + ", " + ScriptHandler.ORTHOGONAL_VALUE + ", " + ScriptHandler.SERIES_VALUE + ", " + IActionRenderer.LEGEND_ITEM_DATA + ", " + IActionRenderer.LEGEND_ITEM_TEXT + ", " + IActionRenderer.LEGEND_ITEM_VALUE + ", " + IActionRenderer.AXIS_LABEL + ", menuInfo) {" + JavascriptEvalUtil.transformToJsConstants( script ) + "}; _callScripts(evt, " + ScriptHandler.BASE_VALUE + ", " + ScriptHandler.ORTHOGONAL_VALUE + ", " + ScriptHandler.SERIES_VALUE + ", " + IActionRenderer.LEGEND_ITEM_DATA + ", " + IActionRenderer.LEGEND_ITEM_TEXT + ", " + IActionRenderer.LEGEND_ITEM_VALUE + ", " + IActionRenderer.AXIS_LABEL + ", menuInfo);"; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$//$NON-NLS-4$ //$NON-NLS-5$//$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$//$NON-NLS-9$ //$NON-NLS-10$//$NON-NLS-11$ //$NON-NLS-12$//$NON-NLS-13$ //$NON-NLS-14$ //$NON-NLS-15$ //$NON-NLS-16$  
		return f;
	}
}
