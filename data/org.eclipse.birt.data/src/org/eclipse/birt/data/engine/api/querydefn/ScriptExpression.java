/*
 *************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */ 
package org.eclipse.birt.data.engine.api.querydefn;

import org.eclipse.birt.data.engine.api.IScriptExpression;

/**
 * Default implementation of the IJSExpression interface
 */
public class ScriptExpression extends BaseExpression implements IScriptExpression
{
	protected String exprText;
	
    public ScriptExpression( String text )
    {
        this.exprText = text;
    }
    
    public ScriptExpression( String text, int dataType )
    {
    	super( dataType );
        this.exprText = text;
    }
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.api.IJSExpression#getText()
	 */
	public String getText()
	{
		return exprText;
	}
	
	/**
	 * Sets the expression text
	 */
	public void setText( String text )
	{
	    exprText = text;
	}
	
}
