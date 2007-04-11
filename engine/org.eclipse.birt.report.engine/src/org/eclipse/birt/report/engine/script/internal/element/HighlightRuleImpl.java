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

package org.eclipse.birt.report.engine.script.internal.element;

import org.eclipse.birt.report.engine.api.script.element.IHighlightRule;
import org.eclipse.birt.report.model.api.HighlightRuleHandle;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.elements.structures.HighlightRule;
import org.eclipse.birt.report.model.api.simpleapi.SimpleElementFactory;

/**
 * Implements of HighLightRule.
 */

public class HighlightRuleImpl implements IHighlightRule
{

    private org.eclipse.birt.report.model.api.simpleapi.IHighlightRule highlightRuleImpl;

    /**
     * Constructor
     * 
     * @param ruleHandle
     */

    public HighlightRuleImpl()
    {
        highlightRuleImpl = SimpleElementFactory.getInstance()
                .createHighlightRule();
    }

    /**
     * Constructor
     * 
     * @param ruleHandle
     */

    public HighlightRuleImpl( HighlightRuleHandle ruleHandle )
    {
        highlightRuleImpl = SimpleElementFactory.getInstance()
                .createHighlightRule( ruleHandle );
    }

    /**
     * Constructor
     * 
     * @param rule
     * @param handle
     */

    public HighlightRuleImpl( HighlightRule rule )
    {
        highlightRuleImpl = SimpleElementFactory.getInstance()
                .createHighlightRule( rule );
    }

    public HighlightRuleImpl(
            org.eclipse.birt.report.model.api.simpleapi.IHighlightRule highlightRule )
    {
        highlightRuleImpl = highlightRule;
    }

    public String getColor()
    {
        return highlightRuleImpl.getColor();
    }

    public String getDateTimeFormat()
    {
        return highlightRuleImpl.getDateTimeFormat();
    }

    public String getFontStyle()
    {
        return highlightRuleImpl.getFontStyle();
    }

    public String getFontWeight()
    {
        return highlightRuleImpl.getFontWeight();
    }

    public String getStringFormat()
    {
        return highlightRuleImpl.getStringFormat();
    }

    public String getTestExpression()
    {
        return highlightRuleImpl.getTestExpression();
    }

    public void setColor( String color )
    {
        highlightRuleImpl.setColor( color );
    }

    public void setDateTimeFormat( String format )
    {
        highlightRuleImpl.setDateTimeFormat( format );
    }

    public void setFontStyle( String style )
    {
        highlightRuleImpl.setFontStyle( style );
    }

    public void setFontWeight( String weight )
    {
        highlightRuleImpl.setFontWeight( weight );
    }

    public void setStringFormat( String format )
    {
        highlightRuleImpl.setStringFormat( format );
    }

    public void setTestExpression( String expression )
    {
        highlightRuleImpl.setTestExpression( expression );
    }

    public void setValue1( String value1 )
    {
        highlightRuleImpl.setValue1( value1 );
    }

    public void setValue2( String value2 )
    {
        highlightRuleImpl.setValue2( value2 );
    }

    public void setOperator( String operator )
    {
        highlightRuleImpl.setOperator( operator );
    }

    public void setBackGroudnColor( String color )
    {
        highlightRuleImpl.setBackGroudnColor( color );
    }

    public IStructure getStructure()
    {
        return highlightRuleImpl.getStructure();
    }

    public String getBackGroudnColor()
    {
        return highlightRuleImpl.getBackGroudnColor();
    }

    public String getOperator()
    {
        return highlightRuleImpl.getOperator();
    }

    public String getValue1()
    {
        return highlightRuleImpl.getValue1();
    }

    public String getValue2()
    {
        return highlightRuleImpl.getValue2();
    }

}
