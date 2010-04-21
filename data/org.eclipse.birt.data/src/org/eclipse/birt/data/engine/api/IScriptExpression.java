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
package org.eclipse.birt.data.engine.api;

/**
 * Describes a Javascript expression used in the report design. 
 */
public interface IScriptExpression extends IBaseExpression
{
    /**
     * @return the Javascript expression text
     */
    public String getText();
    
    /**
     * 
     * @return
     * return true, if this expression is a constant expression;
     */
    public boolean isConstant( );
    
    /**
     * This method return the constant value if this expression is a constant expression
     * @return
     */
    public Object getConstantValue( );
    
    /**
     * Set the constant value for constant expression
     * @param constantValue
     */
    public void setConstantValue( Object constantValue );
}
