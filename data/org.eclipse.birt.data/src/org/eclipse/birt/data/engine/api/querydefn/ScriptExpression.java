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
 * Default implementation of the {@link org.eclipse.birt.data.engine.api.IScriptExpression} interface
 */
public class ScriptExpression extends BaseExpression implements IScriptExpression
{
	protected String exprText;
	protected Object constantValue;
	protected boolean isConstant = false;
	
	/**
	 * Constructs a Javascript expression
	 * @param text Javascript expression text
	 */
    public ScriptExpression( String text )
    {
        this.exprText = text;
    }
    
	/**
	 * Constructs a Javascript expression
	 * @param text Javascript expression text
	 * @param dataType Return data type of the expression
	 */
    public ScriptExpression( String text, int dataType )
    {
    	super( dataType );
        this.exprText = text;
    }
	
    /**
     * @see org.eclipse.birt.data.engine.api.IScriptExpression#getText()
     */
	public String getText()
	{
		return exprText;
	}
	
	/**
	 * Sets the Javascript expression text
	 */
	public void setText( String text )
	{
	    exprText = text;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.api.IScriptExpression#getConstantValue()
	 */
	public Object getConstantValue( )
	{
		return constantValue;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.api.IScriptExpression#isConstant()
	 */
	public boolean isConstant( )
	{
		return isConstant;
	}
	
	public void setConstant( boolean isConstant )
	{
		this.isConstant = isConstant;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.api.IScriptExpression#setConstantValue(java.lang.Object)
	 */
	public void setConstantValue( Object constantValue )
	{
		this.constantValue = constantValue;
	}
}
