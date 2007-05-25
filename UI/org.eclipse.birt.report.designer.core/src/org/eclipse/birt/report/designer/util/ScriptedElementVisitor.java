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

package org.eclipse.birt.report.designer.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.views.outline.ScriptObjectNode;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;

public class ScriptedElementVisitor
{

	private List scriptNodes = new ArrayList( );

	public ScriptedElementVisitor( )
	{

	}

	public List getScriptNodes( DesignElementHandle elementHandle )
	{
		List scriptMethods = elementHandle.getMethods( );
		for ( Iterator ite = scriptMethods.iterator( ); ite.hasNext( ); )
		{
			IElementPropertyDefn elementPropDefn = (IElementPropertyDefn) ite.next( );
			String methodName = elementPropDefn.getMethodInfo( ).getName( );
			if ( elementHandle.getStringProperty( methodName ) != null )
			{
				ScriptObjectNode scriptElementNode = new ScriptObjectNode( elementPropDefn );
				scriptNodes.add( scriptElementNode );
			}
		}
		return scriptNodes;
	}
}
