/*******************************************************************************
* Copyright (c) 2004 Actuate Corporation.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*  Actuate Corporation  - initial API and implementation
*******************************************************************************/ 

package org.eclipse.birt.report.model.parser;

import java.util.ArrayList;

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.StyledElement;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.structures.HighlightRule;
import org.eclipse.birt.report.model.elements.structures.MapRule;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.StringUtil;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.eclipse.birt.report.model.util.XMLParserHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This class parses a style. This class is used in two distinct ways. First, it
 * is used to parse and create a named shared style stored in the styles slot of
 * the report design. Second, it is used to parse "private style" information
 * for a report item. Since both contexts use exactly the same XML (except for
 * the name and extends attributes), and both use the same property names,
 * having one state handle both contexts makes the parser simpler.
 * 
 */

class StyleState extends ReportElementState
{

	/**
	 * The element being built. Either a shared style or a report item.
	 */

	protected DesignElement element = null;

	/**
	 * Temporary storage of the list of map rules.
	 */

	protected ArrayList mapRules = null;

	/**
	 * Temporary storage of one map rule as it is being built.
	 */

	protected MapRule mapRule = null;

	/**
	 * Temporary storage of the list of highlight rules.
	 */

	protected ArrayList highlightRules = null;

	/**
	 * Temporary storage of one highlight rule as it is being built.
	 */

	protected HighlightRule highlightRule = null;

	/**
	 * Constructs for creating a named shared style with the design file parser
	 * handler.
	 * 
	 * @param handler
	 *            the design file parser handler
	 */

	StyleState( DesignParserHandler handler )
	{
		super( handler, handler.getDesign( ), ReportDesign.STYLE_SLOT );
	}

	/**
	 * Constructs for parsing the "private style" of an existing report item
	 * with the design file parser handler and the design element that has
	 * style.
	 * 
	 * @param handler
	 *            the design file parser handler
	 * @param obj
	 *            the element being built
	 */

	StyleState( DesignParserHandler handler, DesignElement obj )
	{
		super( handler );
		element = obj;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.Attributes)
	 */

	public void parseAttrs( Attributes attrs ) throws XMLParserException
	{
		if ( element == null )
		{
			// Top-level shared style.

			element = new Style( );
			initElement( attrs, true );
		}
		else
		{
			// Style properties on a report item.

			String name = attrs.getValue( DesignElement.NAME_PROP );
			if ( !StringUtil.isBlank( name ) )
				( (StyledElement) element ).setStyleName( name );

			// Warning for the common case of using "extends" in place of
			// "name."

			if ( !StringUtil.isBlank( attrs
					.getValue( DesignElement.EXTENDS_PROP ) ) )
				handler.semanticError( new DesignParserException(
						DesignParserException.ILLEGAL_EXTENDS ) );
		}

		parseStyleAttribute( attrs );
	}

	private void setStyleProperty( String propName, String value )
	{
		if ( element.getPropertyDefn( propName ) != null )
			setProperty( propName, value );
	}

	private void parseStyleAttribute( Attributes attrs )
	{
		setStyleProperty( Style.BACKGROUND_ATTACHMENT_PROP, attrs
				.getValue( DesignSchemaConstants.BACKGROUND_ATTACHMENT_ATTRIB ) );
		setStyleProperty( Style.BACKGROUND_COLOR_PROP, attrs
				.getValue( DesignSchemaConstants.BACKGROUND_COLOR_ATTRIB ) );
		setStyleProperty( Style.BACKGROUND_IMAGE_PROP, attrs
				.getValue( DesignSchemaConstants.BACKGROUND_IMAGE_ATTRIB ) );
		setStyleProperty( Style.BACKGROUND_POSITION_X_PROP, attrs
				.getValue( DesignSchemaConstants.BACKGROUND_POSITION_X_ATTRIB ) );
		setStyleProperty( Style.BACKGROUND_POSITION_Y_PROP, attrs
				.getValue( DesignSchemaConstants.BACKGROUND_POSITION_Y_ATTRIB ) );
		setStyleProperty( Style.BACKGROUND_REPEAT_PROP, attrs
				.getValue( DesignSchemaConstants.BACKGROUND_REPEAT_ATTRIB ) );

		setStyleProperty( Style.TEXT_ALIGN_PROP, attrs
				.getValue( DesignSchemaConstants.TEXT_ALIGN_ATTRIB ) );
		setStyleProperty( Style.TEXT_INDENT_PROP, attrs
				.getValue( DesignSchemaConstants.TEXT_INDENT_ATTRIB ) );
		setStyleProperty( Style.LETTER_SPACING_PROP, attrs
				.getValue( DesignSchemaConstants.TEXT_LETTER_SPACING_ATTRIB ) );
		setStyleProperty( Style.LINE_HEIGHT_PROP, attrs
				.getValue( DesignSchemaConstants.TEXT_LINE_HEIGHT_ATTRIB ) );
		setStyleProperty( Style.ORPHANS_PROP, attrs
				.getValue( DesignSchemaConstants.TEXT_ORPHANS_ATTRIB ) );
		setStyleProperty( Style.TEXT_TRANSFORM_PROP, attrs
				.getValue( DesignSchemaConstants.TEXT_TRANSFORM_ATTRIB ) );
		setStyleProperty( Style.VERTICAL_ALIGN_PROP, attrs
				.getValue( DesignSchemaConstants.TEXT_VERTICAL_ALIGN_ATTRIB ) );
		setStyleProperty( Style.WHITE_SPACE_PROP, attrs
				.getValue( DesignSchemaConstants.TEXT_WHITE_SPACE_ATTRIB ) );
		setStyleProperty( Style.WIDOWS_PROP, attrs
				.getValue( DesignSchemaConstants.TEXT_WIDOWS_ATTRIB ) );
		setStyleProperty( Style.WORD_SPACING_PROP, attrs
				.getValue( DesignSchemaConstants.TEXT_WORD_SPACING_ATTRIB ) );

		setStyleProperty( Style.DISPLAY_PROP, attrs
				.getValue( DesignSchemaConstants.SECTION_DISPLAY_ATTRIB ) );
		setStyleProperty( Style.MASTER_PAGE_PROP, attrs
				.getValue( DesignSchemaConstants.SECTION_MASTER_PAGE_ATTRIB ) );
		setStyleProperty(
				Style.PAGE_BREAK_AFTER_PROP,
				attrs
						.getValue( DesignSchemaConstants.SECTION_PAGE_BREAK_AFTER_ATTRIB ) );
		setStyleProperty(
				Style.PAGE_BREAK_BEFORE_PROP,
				attrs
						.getValue( DesignSchemaConstants.SECTION_PAGE_BREAK_BEFORE_ATTRIB ) );
		setStyleProperty(
				Style.PAGE_BREAK_INSIDE_PROP,
				attrs
						.getValue( DesignSchemaConstants.SECTION_PAGE_BREAK_INSIDE_ATTRIB ) );
		setStyleProperty( Style.SHOW_IF_BLANK_PROP, attrs
				.getValue( DesignSchemaConstants.SECTION_SHOW_LF_BLANK_ATTRIB ) );
		setStyleProperty( Style.CAN_SHRINK_PROP, attrs
				.getValue( DesignSchemaConstants.CAN_SHRINK_ATTRIB ) );

		setStyleProperty( Style.DATE_TIME_FORMAT_PROP, attrs
				.getValue( DesignSchemaConstants.DATE_TIME_FORMAT_ATTRIB ) );
		setStyleProperty( Style.NUMBER_FORMAT_PROP, attrs
				.getValue( DesignSchemaConstants.NUMBER_FORMAT_ATTRIB ) );
		setStyleProperty( Style.NUMBER_ALIGN_PROP, attrs
				.getValue( DesignSchemaConstants.NUMBER_ALIGN_ATTRIB ) );
		setStyleProperty( Style.STRING_FORMAT_PROP, attrs
				.getValue( DesignSchemaConstants.STRING_FORMAT_ATTRIB ) );

		setStyleProperty( Style.FONT_FAMILY_PROP, attrs
				.getValue( DesignSchemaConstants.FONT_FAMILY_ATTRIB ) );
		setStyleProperty( Style.COLOR_PROP, attrs
				.getValue( DesignSchemaConstants.COLOR_ATTRIB ) );
		setStyleProperty( Style.FONT_SIZE_PROP, attrs
				.getValue( DesignSchemaConstants.FONT_SIZE_ATTRIB ) );
		setStyleProperty( Style.FONT_STYLE_PROP, attrs
				.getValue( DesignSchemaConstants.FONT_STYLE_ATTRIB ) );
		setStyleProperty( Style.FONT_WEIGHT_PROP, attrs
				.getValue( DesignSchemaConstants.FONT_WEIGHT_ATTRIB ) );
		setStyleProperty( Style.FONT_VARIANT_PROP, attrs
				.getValue( DesignSchemaConstants.FONT_VARIANT_ATTRIB ) );
		setStyleProperty( Style.TEXT_LINE_THROUGH_PROP, attrs
				.getValue( DesignSchemaConstants.TEXT_LINE_THROUGH_ATTRIB ) );
		setStyleProperty( Style.TEXT_OVERLINE_PROP, attrs
				.getValue( DesignSchemaConstants.TEXT_OVERLINE_ATTRIB ) );
		setStyleProperty( Style.TEXT_UNDERLINE_PROP, attrs
				.getValue( DesignSchemaConstants.TEXT_UNDERLINE_ATTRIB ) );

		setStyleProperty( Style.BORDER_BOTTOM_COLOR_PROP, attrs
				.getValue( DesignSchemaConstants.BORDER_BOTTOM_COLOR_ATTRIB ) );
		setStyleProperty( Style.BORDER_BOTTOM_STYLE_PROP, attrs
				.getValue( DesignSchemaConstants.BORDER_BOTTOM_STYLE_ATTRIB ) );
		setStyleProperty( Style.BORDER_BOTTOM_WIDTH_PROP, attrs
				.getValue( DesignSchemaConstants.BORDER_BOTTOM_WIDTH_ATTRIB ) );
		setStyleProperty( Style.BORDER_LEFT_COLOR_PROP, attrs
				.getValue( DesignSchemaConstants.BORDER_LEFT_COLOR_ATTRIB ) );
		setStyleProperty( Style.BORDER_LEFT_STYLE_PROP, attrs
				.getValue( DesignSchemaConstants.BORDER_LEFT_STYLE_ATTRIB ) );
		setStyleProperty( Style.BORDER_LEFT_WIDTH_PROP, attrs
				.getValue( DesignSchemaConstants.BORDER_LEFT_WIDTH_ATTRIB ) );
		setStyleProperty( Style.BORDER_RIGHT_COLOR_PROP, attrs
				.getValue( DesignSchemaConstants.BORDER_RIGHT_COLOR_ATTRIB ) );
		setStyleProperty( Style.BORDER_RIGHT_STYLE_PROP, attrs
				.getValue( DesignSchemaConstants.BORDER_RIGHT_STYLE_ATTRIB ) );
		setStyleProperty( Style.BORDER_RIGHT_WIDTH_PROP, attrs
				.getValue( DesignSchemaConstants.BORDER_RIGHT_WIDTH_ATTRIB ) );
		setStyleProperty( Style.BORDER_TOP_COLOR_PROP, attrs
				.getValue( DesignSchemaConstants.BORDER_TOP_COLOR_ATTRIB ) );
		setStyleProperty( Style.BORDER_TOP_STYLE_PROP, attrs
				.getValue( DesignSchemaConstants.BORDER_TOP_STYLE_ATTRIB ) );
		setStyleProperty( Style.BORDER_TOP_WIDTH_PROP, attrs
				.getValue( DesignSchemaConstants.BORDER_TOP_WIDTH_ATTRIB ) );

		setStyleProperty( Style.MARGIN_TOP_PROP, attrs
				.getValue( DesignSchemaConstants.MARGIN_TOP_ATTRIB ) );
		setStyleProperty( Style.MARGIN_LEFT_PROP, attrs
				.getValue( DesignSchemaConstants.MARGIN_LEFT_ATTRIB ) );
		setStyleProperty( Style.MARGIN_BOTTOM_PROP, attrs
				.getValue( DesignSchemaConstants.MARGIN_BOTTOM_ATTRIB ) );
		setStyleProperty( Style.MARGIN_RIGHT_PROP, attrs
				.getValue( DesignSchemaConstants.MARGIN_RIGHT_ATTRIB ) );

		setStyleProperty( Style.PADDING_TOP_PROP, attrs
				.getValue( DesignSchemaConstants.PADDING_TOP_ATTRIB ) );
		setStyleProperty( Style.PADDING_LEFT_PROP, attrs
				.getValue( DesignSchemaConstants.PADDING_LEFT_ATTRIB ) );
		setStyleProperty( Style.PADDING_BOTTOM_PROP, attrs
				.getValue( DesignSchemaConstants.PADDING_BOTTOM_ATTRIB ) );
		setStyleProperty( Style.PADDING_RIGHT_PROP, attrs
				.getValue( DesignSchemaConstants.PADDING_RIGHT_ATTRIB ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.DesignParseState#getElement()
	 */

	public DesignElement getElement( )
	{
		return element;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
	 */

	public AbstractParseState startElement( String tagName )
	{
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.HIGHLIGHT_TAG ) )
			return new HighlightState( );

		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.MAP_TAG ) )
			return new MapState( );
		return super.startElement( tagName );
	}

	/**
	 * Convenience class for the inner classes used to parse parts of the Report
	 * tag.
	 */

	class InnerParseState extends AbstractParseState
	{

		public XMLParserHandler getHandler( )
		{
			return handler;
		}
	}

	/**
	 * Parses the set of highlight rules.
	 */

	class HighlightState extends InnerParseState
	{

		HighlightState( )
		{
			highlightRules = (ArrayList) element.getLocalProperty(
					handler.design, Style.HIGHLIGHT_RULES_PROP );
			if ( highlightRules == null )
			{
				highlightRules = new ArrayList( );
			}

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
		 */
		
		public AbstractParseState startElement( String tagName )
		{
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.TEST_EXPR_TAG ) )
				return new TextState( handler, element,
						Style.HIGHLIGHT_TEST_EXPR_PROP );
			if ( tagName
					.equalsIgnoreCase( DesignSchemaConstants.HIGHLIGHT_RULE_TAG ) )
				return new HighlightRuleState( );
			return super.startElement( tagName );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
		 */
		
		public void end( ) throws SAXException
		{
			element.setProperty( Style.HIGHLIGHT_RULES_PROP, highlightRules );
			highlightRules = null;
			super.end( );
		}
	}

	/**
	 * Parses the set of mapping rules.
	 */

	class MapState extends InnerParseState
	{

		MapState( )
		{
			mapRules = (ArrayList) element.getLocalProperty( handler.design,
					Style.MAP_RULES_PROP );
			if ( mapRules == null )
				mapRules = new ArrayList( );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
		 */
		
		public AbstractParseState startElement( String tagName )
		{
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.TEST_EXPR_TAG ) )
				return new TextState( handler, element,
						Style.MAP_TEST_EXPR_PROP );
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.MAP_RULE_TAG ) )
				return new MapRuleState( );
			return super.startElement( tagName );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
		 */
		
		public void end( ) throws SAXException
		{
			element.setProperty( Style.MAP_RULES_PROP, mapRules );
			mapRules = null;
			super.end( );
		}
	}

	/**
	 * Parses one map rule.
	 */

	class MapRuleState extends InnerParseState
	{

		MapRuleState( )
		{
			mapRule = new MapRule( );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
		 */

		public void end( ) throws SAXException
		{
			if ( mapRule != null )
				mapRules.add( mapRule );
			mapRule = null;
			super.end( );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.Attributes)
		 */
		
		public void parseAttrs( Attributes attrs ) throws XMLParserException
		{
			setMember( mapRule, Style.MAP_RULES_PROP, MapRule.OPERATOR_MEMBER,
					attrs.getValue( DesignSchemaConstants.OPERATOR_ATTRIB ) );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
		 */
		
		public AbstractParseState startElement( String tagName )
		{
			if ( tagName
					.equalsIgnoreCase( DesignSchemaConstants.VALUE1_EXPR_TAG ) )
				return new TextState( handler, mapRule, MapRule.VALUE1_MEMBER );
			if ( tagName
					.equalsIgnoreCase( DesignSchemaConstants.VALUE2_EXPR_TAG ) )
				return new TextState( handler, mapRule, MapRule.VALUE2_MEMBER );
			if ( tagName.equalsIgnoreCase( DesignSchemaConstants.DISPLAY_TAG ) )
				return new ExternalTextState( handler, mapRule,
						MapRule.DISPLAY_MEMBER );
			return super.startElement( tagName );
		}
	}

	/**
	 * Parses one highlight rule
	 */
	
	class HighlightRuleState extends InnerParseState
	{

		HighlightRuleState( )
		{
			highlightRule = new HighlightRule( );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
		 */
		
		public AbstractParseState startElement( String tagName )
		{
			if ( tagName
					.equalsIgnoreCase( DesignSchemaConstants.VALUE1_EXPR_TAG ) )
				return new TextState( handler, highlightRule,
						HighlightRule.VALUE1_MEMBER );
			if ( tagName
					.equalsIgnoreCase( DesignSchemaConstants.VALUE2_EXPR_TAG ) )
				return new TextState( handler, highlightRule,
						HighlightRule.VALUE2_MEMBER );
			return super.startElement( tagName );
		}

		/*
		 *  (non-Javadoc)
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
		 */
		
		public void end( ) throws SAXException
		{
			if ( highlightRule != null )
			{
				highlightRules.add( highlightRule );
			}
			highlightRule = null;
			super.end( );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.Attributes)
		 */
		
		public void parseAttrs( Attributes attrs ) throws XMLParserException
		{
			setMember( highlightRule, Style.HIGHLIGHT_RULES_PROP,
					HighlightRule.OPERATOR_MEMBER, attrs
							.getValue( DesignSchemaConstants.OPERATOR_ATTRIB ) );
			setMember(
					highlightRule,
					Style.HIGHLIGHT_RULES_PROP,
					HighlightRule.BACKGROUND_COLOR_MEMBER,
					attrs
							.getValue( DesignSchemaConstants.BACKGROUND_COLOR_ATTRIB ) );

			setMember(
					highlightRule,
					Style.HIGHLIGHT_RULES_PROP,
					HighlightRule.BORDER_BOTTOM_COLOR_MEMBER,
					attrs
							.getValue( DesignSchemaConstants.BORDER_BOTTOM_COLOR_ATTRIB ) );
			setMember(
					highlightRule,
					Style.HIGHLIGHT_RULES_PROP,
					HighlightRule.BORDER_BOTTOM_STYLE_MEMBER,
					attrs
							.getValue( DesignSchemaConstants.BORDER_BOTTOM_STYLE_ATTRIB ) );
			setMember(
					highlightRule,
					Style.HIGHLIGHT_RULES_PROP,
					HighlightRule.BORDER_BOTTOM_WIDTH_MEMBER,
					attrs
							.getValue( DesignSchemaConstants.BORDER_BOTTOM_WIDTH_ATTRIB ) );

			setMember(
					highlightRule,
					Style.HIGHLIGHT_RULES_PROP,
					HighlightRule.BORDER_LEFT_COLOR_MEMBER,
					attrs
							.getValue( DesignSchemaConstants.BORDER_LEFT_COLOR_ATTRIB ) );
			setMember(
					highlightRule,
					Style.HIGHLIGHT_RULES_PROP,
					HighlightRule.BORDER_LEFT_STYLE_MEMBER,
					attrs
							.getValue( DesignSchemaConstants.BORDER_LEFT_STYLE_ATTRIB ) );
			setMember(
					highlightRule,
					Style.HIGHLIGHT_RULES_PROP,
					HighlightRule.BORDER_LEFT_WIDTH_MEMBER,
					attrs
							.getValue( DesignSchemaConstants.BORDER_LEFT_WIDTH_ATTRIB ) );

			setMember(
					highlightRule,
					Style.HIGHLIGHT_RULES_PROP,
					HighlightRule.BORDER_TOP_COLOR_MEMBER,
					attrs
							.getValue( DesignSchemaConstants.BORDER_TOP_COLOR_ATTRIB ) );
			setMember(
					highlightRule,
					Style.HIGHLIGHT_RULES_PROP,
					HighlightRule.BORDER_TOP_STYLE_MEMBER,
					attrs
							.getValue( DesignSchemaConstants.BORDER_TOP_STYLE_ATTRIB ) );
			setMember(
					highlightRule,
					Style.HIGHLIGHT_RULES_PROP,
					HighlightRule.BORDER_TOP_WIDTH_MEMBER,
					attrs
							.getValue( DesignSchemaConstants.BORDER_TOP_WIDTH_ATTRIB ) );

			setMember(
					highlightRule,
					Style.HIGHLIGHT_RULES_PROP,
					HighlightRule.BORDER_RIGHT_COLOR_MEMBER,
					attrs
							.getValue( DesignSchemaConstants.BORDER_RIGHT_COLOR_ATTRIB ) );
			setMember(
					highlightRule,
					Style.HIGHLIGHT_RULES_PROP,
					HighlightRule.BORDER_RIGHT_STYLE_MEMBER,
					attrs
							.getValue( DesignSchemaConstants.BORDER_RIGHT_STYLE_ATTRIB ) );
			setMember(
					highlightRule,
					Style.HIGHLIGHT_RULES_PROP,
					HighlightRule.BORDER_RIGHT_WIDTH_MEMBER,
					attrs
							.getValue( DesignSchemaConstants.BORDER_RIGHT_WIDTH_ATTRIB ) );

			setMember( highlightRule, Style.HIGHLIGHT_RULES_PROP,
					HighlightRule.COLOR_MEMBER, attrs
							.getValue( DesignSchemaConstants.COLOR_ATTRIB ) );
			setMember(
					highlightRule,
					Style.HIGHLIGHT_RULES_PROP,
					HighlightRule.DATE_TIME_FORMAT_MEMBER,
					attrs
							.getValue( DesignSchemaConstants.DATE_TIME_FORMAT_ATTRIB ) );
			setMember( highlightRule, Style.HIGHLIGHT_RULES_PROP,
					HighlightRule.FONT_FAMILY_MEMBER,
					attrs.getValue( DesignSchemaConstants.FONT_FAMILY_ATTRIB ) );
			setMember( highlightRule, Style.HIGHLIGHT_RULES_PROP,
					HighlightRule.FONT_SIZE_MEMBER, attrs
							.getValue( DesignSchemaConstants.FONT_SIZE_ATTRIB ) );
			setMember( highlightRule, Style.HIGHLIGHT_RULES_PROP,
					HighlightRule.FONT_STYLE_MEMBER, attrs
							.getValue( DesignSchemaConstants.FONT_STYLE_ATTRIB ) );
			setMember( highlightRule, Style.HIGHLIGHT_RULES_PROP,
					HighlightRule.FONT_WEIGHT_MEMBER,
					attrs.getValue( DesignSchemaConstants.FONT_WEIGHT_ATTRIB ) );
			setMember( highlightRule, Style.HIGHLIGHT_RULES_PROP,
					HighlightRule.FONT_VARIANT_MEMBER,
					attrs.getValue( DesignSchemaConstants.FONT_VARIANT_ATTRIB ) );

			setMember( highlightRule, Style.HIGHLIGHT_RULES_PROP,
					HighlightRule.NUMBER_FORMAT_MEMBER,
					attrs.getValue( DesignSchemaConstants.NUMBER_FORMAT_ATTRIB ) );
			setMember( highlightRule, Style.HIGHLIGHT_RULES_PROP,
					HighlightRule.STRING_FORMAT_MEMBER,
					attrs.getValue( DesignSchemaConstants.STRING_FORMAT_ATTRIB ) );
			setMember( highlightRule, Style.HIGHLIGHT_RULES_PROP,
					HighlightRule.TEXT_ALIGN_MEMBER, attrs
							.getValue( DesignSchemaConstants.TEXT_ALIGN_ATTRIB ) );
			setMember(
					highlightRule,
					Style.HIGHLIGHT_RULES_PROP,
					HighlightRule.TEXT_UNDERLINE_MEMBER,
					attrs
							.getValue( DesignSchemaConstants.TEXT_UNDERLINE_ATTRIB ) );
			setMember(
					highlightRule,
					Style.HIGHLIGHT_RULES_PROP,
					HighlightRule.TEXT_LINE_THROUGH_MEMBER,
					attrs
							.getValue( DesignSchemaConstants.TEXT_LINE_THROUGH_ATTRIB ) );
			setMember( highlightRule, Style.HIGHLIGHT_RULES_PROP,
					HighlightRule.TEXT_OVERLINE_MEMBER,
					attrs.getValue( DesignSchemaConstants.TEXT_OVERLINE_ATTRIB ) );
			setMember(
					highlightRule,
					Style.HIGHLIGHT_RULES_PROP,
					HighlightRule.TEXT_TRANSFORM_MEMBER,
					attrs
							.getValue( DesignSchemaConstants.TEXT_TRANSFORM_ATTRIB ) );
			setMember(
					highlightRule,
					Style.HIGHLIGHT_RULES_PROP,
					HighlightRule.TEXT_INDENT_MEMBER,
					attrs
							.getValue( DesignSchemaConstants.TEXT_INDENT_ATTRIB ) );
		}
	}
}
