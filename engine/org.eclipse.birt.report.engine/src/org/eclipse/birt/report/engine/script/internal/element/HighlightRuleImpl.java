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
import org.eclipse.birt.report.model.api.elements.structures.DateTimeFormatValue;
import org.eclipse.birt.report.model.api.elements.structures.FormatValue;
import org.eclipse.birt.report.model.api.elements.structures.HighlightRule;
import org.eclipse.birt.report.model.api.elements.structures.StringFormatValue;
import org.eclipse.birt.report.model.api.util.StringUtil;

/**
 * Implements of HighLightRule.
 * 
 */

public class HighlightRuleImpl implements IHighlightRule
{

	private HighlightRule rule;

	/**
	 * Constructor
	 * 
	 * @param ruleHandle
	 */

	public HighlightRuleImpl( )
	{
		rule = createHighlightRule( );
	}

	/**
	 * Constructor
	 * 
	 * @param ruleHandle
	 */

	public HighlightRuleImpl( HighlightRuleHandle ruleHandle )
	{
		if ( ruleHandle == null )
		{
			rule = createHighlightRule( );
		}
		else
		{

			this.rule = (HighlightRule) ruleHandle.getStructure( );
		}
	}

	/**
	 * Constructor
	 * 
	 * @param rule
	 * @param handle
	 */

	public HighlightRuleImpl( HighlightRule rule )
	{
		if ( rule == null )
		{
			rule = createHighlightRule( );
		}
		else
		{

			this.rule = rule;
		}
	}

	private HighlightRule createHighlightRule( )
	{
		HighlightRule r = new HighlightRule( );
		return r;
	}

	public String getColor( )
	{
		Object obj = rule.getProperty( null, HighlightRule.COLOR_MEMBER );
		
		if ( obj == null )
			return null;

		if ( obj instanceof Integer )
		{
			return StringUtil.toRgbText( ( (Integer) obj ).intValue( ) )
					.toUpperCase( );
		}
		else
		{
			return obj.toString( );
		}
	}

	public String getDateTimeFormat( )
	{
		Object value = rule.getProperty( null,
				HighlightRule.DATE_TIME_FORMAT_MEMBER );
		if ( value == null )
			return null;
		
		assert value instanceof DateTimeFormatValue;

		return ( (DateTimeFormatValue) value ).getPattern( );
	}

	public String getFontStyle( )
	{
		return (String) rule
				.getProperty( null, HighlightRule.FONT_STYLE_MEMBER );
	}

	public String getFontWeight( )
	{
		return (String) rule.getProperty( null,
				HighlightRule.FONT_WEIGHT_MEMBER );
	}

	public String getStringFormat( )
	{
		Object value = rule.getProperty( null,
				HighlightRule.STRING_FORMAT_MEMBER );
		if ( value == null )
			return null;
		
		assert value instanceof StringFormatValue;
		
		return ( (StringFormatValue) value ).getPattern( );
	}

	public String getTestExpression( )
	{
		return rule.getTestExpression( );
	}

	public void setColor( String color )
	{
		rule.setProperty( HighlightRule.COLOR_MEMBER, color );
	}

	public void setDateTimeFormat( String format )
	{

		Object value = rule.getProperty( null,
				HighlightRule.DATE_TIME_FORMAT_MEMBER );
		if ( value == null )
		{
			FormatValue formatValueToSet = new DateTimeFormatValue( );
			formatValueToSet.setPattern( format );
			rule.setProperty( HighlightRule.DATE_TIME_FORMAT_MEMBER,
					formatValueToSet );
		}
		else
		{
			assert value instanceof DateTimeFormatValue;

			( (DateTimeFormatValue) value ).setPattern( format );
		}

	}

	public void setFontStyle( String style )
	{
		rule.setProperty( HighlightRule.FONT_STYLE_MEMBER, style );
	}

	public void setFontWeight( String weight )
	{
		rule.setProperty( HighlightRule.FONT_WEIGHT_MEMBER, weight );
	}

	public void setStringFormat( String format )
	{
		Object value = rule.getProperty( null,
				HighlightRule.STRING_FORMAT_MEMBER );
		if ( value == null )
		{
			FormatValue formatValueToSet = new StringFormatValue( );
			formatValueToSet.setPattern( format );
			rule.setProperty( HighlightRule.STRING_FORMAT_MEMBER,
					formatValueToSet );
		}
		else
		{
			assert value instanceof StringFormatValue;

			( (StringFormatValue) value ).setPattern( format );
		}
	}

	public void setTestExpression( String expression )
	{
		rule.setTestExpression( expression );
	}

	public void setValue1( String value1 )
	{
		rule.setValue1( value1 );
	}

	public void setValue2( String value2 )
	{
		rule.setValue2( value2 );
	}

	public void setOperator( String operator )
	{
		rule.setOperator( operator );
	}

	public void setBackGroudnColor( String color )
	{
		rule.setProperty( HighlightRule.BACKGROUND_COLOR_MEMBER, color );
	}

	public IStructure getStructure( )
	{
		return rule;
	}

	public String getBackGroudnColor( )
	{
		Object obj = rule.getProperty( null,
				HighlightRule.BACKGROUND_COLOR_MEMBER );

		if ( obj == null )
			return null;

		if ( obj instanceof Integer )
		{
			return StringUtil.toRgbText( ( (Integer) obj ).intValue( ) )
					.toUpperCase( );
		}
		else
		{
			return obj.toString( );
		}
	}

	public String getOperator( )
	{
		return rule.getOperator( );
	}

	public String getValue1( )
	{
		return rule.getValue1( );
	}

	public String getValue2( )
	{
		return rule.getValue2( );
	}

}
