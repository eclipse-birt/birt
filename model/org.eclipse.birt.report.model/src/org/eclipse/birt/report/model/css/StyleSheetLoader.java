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

package org.eclipse.birt.report.model.css;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.css.StyleSheetException;
import org.eclipse.birt.report.model.api.css.StyleSheetParserException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.css.property.ParseException;
import org.eclipse.birt.report.model.css.property.PropertyParser;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.util.CssPropertyConstants;
import org.eclipse.birt.report.model.util.CssPropertyUtil;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.Condition;
import org.w3c.css.sac.ConditionalSelector;
import org.w3c.css.sac.ElementSelector;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SelectorList;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.flute.parser.selectors.ClassConditionImpl;

/**
 * Loads an external style sheet to the BIRT.
 */

public final class StyleSheetLoader
{

	/**
	 * The parser for the CSS2.
	 */

	private CssParser parser;

	/**
	 * The module that loads the style sheet.
	 */

	private Module module;

	/**
	 * The source that read from an external style sheet.
	 */

	private Reader source;

	/**
	 * The definition of Style in ROM.
	 */

	private final IElementDefn style = MetaDataDictionary.getInstance( )
			.getStyle( );

	/**
	 * The warning list during the loading.
	 */

	private List warnings = null;

	/**
	 * The logger to record the warning.
	 */

	private static Logger logger = Logger.getLogger( StyleSheetLoader.class
			.getName( ) );

	/**
	 * Private constructor to do some reset work.
	 * 
	 */

	public StyleSheetLoader( )
	{
		parser = new CssParser( );
		this.module = null;
		this.source = null;
		warnings = new ArrayList( );
	}

	/**
	 * Re-inits the loader. This method is called when the users want to use the
	 * instance to load more than one external style sheet. Call this every
	 * loading operation.
	 * 
	 */

	public void Reinit( )
	{
		parser = new CssParser( );
		this.module = null;
		this.source = null;
		warnings = new ArrayList( );
	}

	/**
	 * Loads styles from an external style sheet resource. A resource can be
	 * something as simple as a file or a directory, or it can be a reference to
	 * a more complicated object, such as a query to a database or to a search
	 * engine. This method will try to create a URL object from the
	 * <code>String</code> representation.
	 * 
	 * @param module
	 *            the module to load the style sheet
	 * @param spec
	 *            spec the <code>String</code> to parse as a URL, it can be a
	 *            file or directory, or other types or formats of URLs to locate
	 *            an external style sheet
	 * @return the <code>CssStyleSheet</code> containing all the styles loaded
	 *         from an external style sheet, otherwise null
	 * @throws StyleSheetException
	 *             if the resource is not found, or the given
	 *             <code>String</code> is malformed to URL specification, or
	 *             the style sheet resource has some syntax errors colliding
	 *             with CSS2 grammar
	 */

	public CssStyleSheet load( Module module, String spec )
			throws StyleSheetException
	{
		assert module != null;
		this.module = module;

		URL url = module.findResource( spec,
				IResourceLocator.CASCADING_STYLE_SHEET );
		if ( url == null )
		{
			throw new StyleSheetException(
					StyleSheetException.DESIGN_EXCEPTION_STYLE_SHEET_NOT_FOUND );
		}

		InputStream is = null;
		try
		{
			is = url.openStream( );
		}
		catch ( IOException e )
		{
			throw new StyleSheetException(
					StyleSheetException.DESIGN_EXCEPTION_STYLE_SHEET_NOT_FOUND,
					e );
		}
		CssStyleSheet sheet = load( module, is );
		
		//set the path to css style sheet.
		
		sheet.setFileName( spec );
		return sheet;
	}

	/**
	 * Loads styles from an external style sheet resource.
	 * 
	 * @param module
	 *            the module to load the style sheet
	 * @param is
	 *            spec the <code>String</code> to parse as a URL, it can be a
	 *            file or directory, or other types or formats of URLs to locate
	 *            an external style sheet
	 * @return the <code>CssStyleSheet</code> containing all the styles loaded
	 *         from an external style sheet, otherwise null
	 * @throws StyleSheetException
	 *             the style sheet resource has some syntax errors colliding
	 *             with CSS2 grammar
	 */

	public CssStyleSheet load( Module module, InputStream is )
			throws StyleSheetException
	{
		assert module != null;
		this.module = module;

		if ( is == null )
		{
			throw new StyleSheetException(
					StyleSheetException.DESIGN_EXCEPTION_STYLE_SHEET_NOT_FOUND );
		}
		source = new InputStreamReader( is );
		return load( source );
	}

	/**
	 * Loads the styles from an external style sheet resource.
	 * 
	 * @param charStream
	 *            character stream that shall not include a byte order mark
	 * @return the <code>CssStyleSheet</code> containing all the styles loaded
	 *         from an external style sheet, otherwise null
	 * @throws StyleSheetException
	 *             the style sheet resource has some syntax errors colliding
	 *             with CSS2 grammar
	 */

	CssStyleSheet load( Reader charStream ) throws StyleSheetException
	{
		StyleSheet ss = null;
		try
		{
			InputSource is = new InputSource( source );
			ss = (StyleSheet) parser.parseStyleSheet( is );
		}
		catch ( CSSException e )
		{
			logger.log( Level.SEVERE, e.getMessage( ) );
			throw new StyleSheetException(
					StyleSheetException.DESIGN_EXCEPTION_SYNTAX_ERROR, e );
		}
		catch ( IOException e )
		{
			throw new StyleSheetException(
					StyleSheetException.DESIGN_EXCEPTION_STYLE_SHEET_NOT_FOUND,
					e );
		}
		finally
		{
			try
			{
				source.close( );
			}
			catch ( IOException e )
			{
				// Do nothing.
			}
		}

		if ( ss == null )
			return null;

		CssStyleSheet styleSheet = new CssStyleSheet( );

		List rules = ss.getRules( );
		for ( int i = 0; i < rules.size( ); i++ )
		{
			CSSRule rule = (CSSRule) rules.get( i );
			loadStyle( styleSheet, rule );
		}

		styleSheet.addWarning( warnings );
		styleSheet.setErrorHandler( parser.getErrorHandler( ) );
		return styleSheet;
	}

	/**
	 * Translates a CSS rule into one or more styles with the definition of ROM.
	 * This method only deals with the style rules, while any other rules, such
	 * as import rules, page rules and so on, are ignored. The neglect
	 * operations will be recorded in the log file and added into the warning
	 * list. Then all the created styles by interpretations and translations
	 * will be added into the given <code>CssStyleSheet</code> container.
	 * 
	 * @param styleSheet
	 *            the style sheet, which is a container defined by Model to
	 *            store all the styles loaded from the external resource
	 * @param rule
	 *            the current CSS rule to handle
	 * @throws StyleSheetException
	 *             the CSS rule has some syntax errors colliding with CSS2
	 *             grammar
	 */

	void loadStyle( CssStyleSheet styleSheet, CSSRule rule )
			throws StyleSheetException
	{
		// now only support the style rule

		if ( rule.getType( ) == CSSRule.STYLE_RULE )
		{
			try
			{
				StyleRule sr = (StyleRule) rule;
				SelectorList selectionList = sr.getSelectorList( );
				CSSStyleDeclaration declaration = sr.getStyle( );
				LinkedHashMap properties = null;
				List errors = new ArrayList( );
				boolean buildProperties = false;
				for ( int i = 0; i < selectionList.getLength( ); i++ )
				{
					Selector selector = selectionList.item( i );
					int type = selector.getSelectorType( );
					String name = null;
					switch ( type )
					{
						case Selector.SAC_ELEMENT_NODE_SELECTOR :

							// such as "H1", "<DIV>" and so on.

							ElementSelector elementSelector = (ElementSelector) selector;
							name = elementSelector.getLocalName( );

							// to the CSS standard, element selectors are always
							// upper case.

							if ( name != null )
								name = name.toUpperCase( );
							else
							{
								// the element is any element selector "*", this
								// is not supported by BIRT.

								StyleSheetParserException exception = new StyleSheetParserException(
										"*", //$NON-NLS-1$
										StyleSheetParserException.DESIGN_EXCEPTION_STYLE_NOT_SUPPORTED );
								semanticWarning( exception );
								styleSheet.addUnsupportedStyle( "*", exception ); //$NON-NLS-1$
								name = null;
							}

							break;

						case Selector.SAC_CONDITIONAL_SELECTOR :

							// such as "p.table", ".table". The deeper
							// conditional selectors, such as '.table.s.t.m' are
							// not supported. Former has an element constraint
							// while the latter has not. For both, we all
							// convert to a style named as "table".

							Condition condition = ( (ConditionalSelector) selector )
									.getCondition( );
							if ( condition.getConditionType( ) == Condition.SAC_CLASS_CONDITION )
							{
								name = ( (ClassConditionImpl) condition )
										.getValue( );
								// to the Model, selectors are always lower
								// case.
								if ( name != null )
									name = name.toLowerCase( );
							}
							else
							{
								StyleSheetParserException exception = new StyleSheetParserException(
										CssUtil.toString( selector ),
										StyleSheetParserException.DESIGN_EXCEPTION_STYLE_NOT_SUPPORTED );
								semanticWarning( exception );
								styleSheet.addUnsupportedStyle( CssUtil
										.toString( selector ).toLowerCase( ),
										exception );
								name = null;
							}

							break;

						default :

							StyleSheetParserException exception = new StyleSheetParserException(
									CssUtil.toString( selector ),
									StyleSheetParserException.DESIGN_EXCEPTION_STYLE_NOT_SUPPORTED );
							semanticWarning( exception );
							styleSheet.addUnsupportedStyle( CssUtil.toString(
									selector ).toLowerCase( ), exception );
							name = null;

					}
					if ( name == null )
						return;
					DesignElement style = styleSheet.findStyle( name );
					if ( style == null )
						style = new CssStyle( name );
					else
						styleSheet.removeStyle( name );
					//set css style sheet
					if ( !buildProperties )
					{
						properties = buildProperties( declaration, errors );
						buildProperties = true;
					}

					addProperties( style, properties );
					assert styleSheet.findStyle( name ) == null;
					List ret = styleSheet.getWarnings( name );
					if ( ret == null )
					{
						List localErrors = new ArrayList( );
						localErrors.addAll( errors );
						styleSheet.addWarnings( name, localErrors );
					}
					else
					{
						ret.addAll( errors );
					}
					styleSheet.addStyle( style );
				}
			}
			catch ( CSSException e )
			{
				logger.log( Level.SEVERE, e.getMessage( ) );
				throw new StyleSheetException(
						StyleSheetException.DESIGN_EXCEPTION_SYNTAX_ERROR, e );
			}
		}
		else
		{
			semanticWarning( new StyleSheetParserException(
					rule.toString( ),
					StyleSheetParserException.DESIGN_EXCEPTION_RULE_NOT_SUPPORTED ) );
		}
	}

	/**
	 * Gets all the name/value pairs from a CSS declaration and puts them into a
	 * <code>LinkedHashMap</code>.
	 * 
	 * @param declaration
	 *            the declaration of the style rule
	 * @param errors
	 *            the error list of the declaration
	 * @return all the supported name/value pairs
	 */

	LinkedHashMap buildProperties( CSSStyleDeclaration declaration, List errors )
	{
		LinkedHashMap properties = new LinkedHashMap( );
		for ( int i = 0; i < declaration.getLength( ); i++ )
		{
			String cssName = declaration.item( i );
			String cssValue = declaration.getPropertyValue( cssName );
			if ( StringUtil.isBlank( cssName ) | StringUtil.isBlank( cssValue ) )
				continue;

			properties.put( cssName, cssValue );
		}

		return buildProperties( properties, errors );
	}

	/**
	 * Converts all the name/value pairs in the CSS2 format to the property
	 * values in BIRT defined format. The name/value pair may be simple, like
	 * "background-color:red" or short-hand, like "background: color
	 * url(images/header)". All the short-hand properties will be separated into
	 * corresponding individuals. Whatever BIRT does not support will be
	 * ignored.
	 * 
	 * @param cssProperties
	 *            the hash map that stores all the property values in CSS format
	 * @param errors
	 *            the error list of the properties
	 * @return the name/value pairs in BIRT defined format
	 */

	LinkedHashMap buildProperties( LinkedHashMap cssProperties, List errors )
	{
		if ( cssProperties.isEmpty( ) )
			return cssProperties;

		LinkedHashMap properties = new LinkedHashMap( );
		Iterator iter = cssProperties.keySet( ).iterator( );
		while ( iter.hasNext( ) )
		{
			String cssName = (String) iter.next( );
			String cssValue = (String) cssProperties.get( cssName );

			assert !StringUtil.isBlank( cssName );
			assert !StringUtil.isBlank( cssValue );

			cssName = cssName.toLowerCase( );
			try
			{
				if ( cssName
						.equalsIgnoreCase( CssPropertyConstants.ATTR_BACKGROUND_POSITION ) )
				{
					List ret = handleBackgroundPosition( cssValue, properties );
					if ( !ret.isEmpty( ) )
						errors.add( ret );
				}
				else if ( cssName
						.equalsIgnoreCase( CssPropertyConstants.ATTR_TEXT_DECORATION ) )
				{
					handleTextDecoration( cssValue, properties );
				}
				else if ( cssName
						.equalsIgnoreCase( CssPropertyConstants.ATTR_BORDER_BOTTOM ) )
				{
					PropertyParser parser = new PropertyParser( cssValue );

					parser.parseBorderBottom( );
					LinkedHashMap shortHand = parser.getCssProperties( );
					shortHand = trimProperties( shortHand );
					shortHand = buildProperties( shortHand, errors );
					properties.putAll( shortHand );
				}
				else if ( cssName
						.equalsIgnoreCase( CssPropertyConstants.ATTR_BORDER_LEFT ) )
				{
					PropertyParser parser = new PropertyParser( cssValue );

					parser.parseBorderLeft( );
					LinkedHashMap shortHand = parser.getCssProperties( );
					shortHand = trimProperties( shortHand );
					shortHand = buildProperties( shortHand, errors );
					properties.putAll( shortHand );

				}
				else if ( cssName
						.equalsIgnoreCase( CssPropertyConstants.ATTR_BORDER_RIGHT ) )
				{
					PropertyParser parser = new PropertyParser( cssValue );

					parser.parseBorderRight( );
					LinkedHashMap shortHand = parser.getCssProperties( );
					shortHand = trimProperties( shortHand );
					shortHand = buildProperties( shortHand, errors );
					properties.putAll( shortHand );
				}
				else if ( cssName
						.equalsIgnoreCase( CssPropertyConstants.ATTR_BORDER_TOP ) )
				{
					PropertyParser parser = new PropertyParser( cssValue );

					parser.parseBorderTop( );
					LinkedHashMap shortHand = parser.getCssProperties( );
					shortHand = trimProperties( shortHand );
					shortHand = buildProperties( shortHand, errors );
					properties.putAll( shortHand );
				}
				else if ( cssName
						.equalsIgnoreCase( CssPropertyConstants.ATTR_BORDER ) )
				{
					PropertyParser parser = new PropertyParser( cssValue );

					parser.parseBorder( );
					LinkedHashMap shortHand = parser.getCssProperties( );
					shortHand = trimProperties( shortHand );
					shortHand = buildProperties( shortHand, errors );
					properties.putAll( shortHand );
				}
				else if ( cssName
						.equalsIgnoreCase( CssPropertyConstants.ATTR_BORDER_WIDTH ) )
				{
					PropertyParser parser = new PropertyParser( cssValue );

					parser.parseBorderWidth( );
					LinkedHashMap shortHand = parser.getCssProperties( );
					shortHand = trimProperties( shortHand );
					shortHand = buildProperties( shortHand, errors );
					properties.putAll( shortHand );
				}
				else if ( cssName
						.equalsIgnoreCase( CssPropertyConstants.ATTR_BORDER_COLOR ) )
				{
					PropertyParser parser = new PropertyParser( cssValue );

					parser.parseBorderColor( );
					LinkedHashMap shortHand = parser.getCssProperties( );
					shortHand = trimProperties( shortHand );
					shortHand = buildProperties( shortHand, errors );
					properties.putAll( shortHand );
				}
				else if ( cssName
						.equalsIgnoreCase( CssPropertyConstants.ATTR_BORDER_STYLE ) )
				{
					PropertyParser parser = new PropertyParser( cssValue );

					parser.parseBorderStyle( );
					LinkedHashMap shortHand = parser.getCssProperties( );
					shortHand = trimProperties( shortHand );
					shortHand = buildProperties( shortHand, errors );
					properties.putAll( shortHand );
				}
				else if ( cssName
						.equalsIgnoreCase( CssPropertyConstants.ATTR_FONT ) )
				{
					PropertyParser parser = new PropertyParser( cssValue );

					parser.parseFont( );
					LinkedHashMap shortHand = parser.getCssProperties( );
					shortHand = trimProperties( shortHand );
					shortHand = buildProperties( shortHand, errors );
					properties.putAll( shortHand );
				}
				else if ( cssName
						.equalsIgnoreCase( CssPropertyConstants.ATTR_BACKGROUND ) )
				{
					PropertyParser parser = new PropertyParser( cssValue );

					parser.parseBackground( );
					LinkedHashMap shortHand = parser.getCssProperties( );
					shortHand = trimProperties( shortHand );
					shortHand = buildProperties( shortHand, errors );
					properties.putAll( shortHand );
				}
				else if ( cssName
						.equalsIgnoreCase( CssPropertyConstants.ATTR_MARGIN ) )
				{
					PropertyParser parser = new PropertyParser( cssValue );

					parser.parseMargin( );
					LinkedHashMap shortHand = parser.getCssProperties( );
					shortHand = trimProperties( shortHand );
					shortHand = buildProperties( shortHand, errors );
					properties.putAll( shortHand );
				}
				else if ( cssName
						.equalsIgnoreCase( CssPropertyConstants.ATTR_PADDING ) )
				{
					PropertyParser parser = new PropertyParser( cssValue );

					parser.parsePadding( );
					LinkedHashMap shortHand = parser.getCssProperties( );
					shortHand = trimProperties( shortHand );
					shortHand = buildProperties( shortHand, errors );
					properties.putAll( shortHand );
				}
				else
				{
					String name = CssPropertyUtil.getPropertyName( cssName );
					if ( name == null )
					{
						StyleSheetParserException exception = new StyleSheetParserException(
								StyleSheetParserException.DESIGN_EXCEPTION_PROPERTY_NOT_SUPPORTED,
								cssName, cssValue );
						semanticWarning( exception );
						errors.add( exception );
						continue;
					}
					ElementPropertyDefn propDefn = (ElementPropertyDefn) style
							.getProperty( name );
					assert propDefn != null;

					try
					{
						String wrongValue = cssValue;
						if ( name
								.equalsIgnoreCase( IStyleModel.BACKGROUND_IMAGE_PROP ) )
						{
							cssValue = CssPropertyUtil.getURLValue( cssValue );
						}
						if ( cssValue
								.equalsIgnoreCase( CssPropertyUtil.WRONG_URL ) )
						{
							StyleSheetParserException exception = new StyleSheetParserException(
									StyleSheetParserException.DESIGN_EXCEPTION_INVALID_SIMPLE_CSSPROPERTY_VALUE,
									cssName, wrongValue );
							semanticWarning( exception );
							errors.add( exception );
						}
						Object value = propDefn.validateXml( module, cssValue );
						properties.put( name, value );
					}
					catch ( PropertyValueException e )
					{
						StyleSheetParserException exception = new StyleSheetParserException(
								StyleSheetParserException.DESIGN_EXCEPTION_INVALID_SIMPLE_CSSPROPERTY_VALUE,
								cssName, cssValue, e );
						semanticWarning( exception );
						errors.add( exception );
					}
				}
			}
			catch ( ParseException e )
			{
				StyleSheetParserException exception = new StyleSheetParserException(
						StyleSheetParserException.DESIGN_EXCEPTION_INVALID_SHORT_HAND_CSSPROPERTY_VALUE,
						cssName, cssValue, e );
				semanticWarning( exception );
				errors.add( exception );
			}
			catch ( CSSException e )
			{
				StyleSheetParserException exception = new StyleSheetParserException(
						StyleSheetParserException.DESIGN_EXCEPTION_INVALID_SHORT_HAND_CSSPROPERTY_VALUE,
						cssName, cssValue, e );
				semanticWarning( exception );
				errors.add( exception );
			}
		}
		return properties;
	}

	/**
	 * Converts the background-position property in CSS2 to
	 * background-position-X and background-position-Y in BIRT and adds property
	 * values into the given hash map.
	 * 
	 * @param cssValue
	 *            the value of the background-position
	 * @param properties
	 *            the hash map to store the result property values
	 * @return the error list during the parse
	 */

	List handleBackgroundPosition( String cssValue, LinkedHashMap properties )
	{
		assert cssValue != null;
		List errors = new ArrayList( );

		String[] values = cssValue.split( "[\\s]" ); //$NON-NLS-1$
		String positionX = null;
		String positionY = null;
		switch ( values.length )
		{
			case 0 :

				break;

			case 1 :

				positionX = values[0].trim( );
				break;

			case 2 :

				positionX = values[0].trim( );
				positionY = values[1].trim( );
				break;

			default :

				StyleSheetParserException exception = new StyleSheetParserException(
						StyleSheetParserException.DESIGN_EXCEPTION_INVALID_SHORT_HAND_CSSPROPERTY_VALUE,
						CssPropertyConstants.ATTR_BACKGROUND_POSITION, cssValue );
				semanticWarning( exception );
				errors.add( exception );
				break;
		}
		if ( !StringUtil.isBlank( positionX ) )
		{
			ElementPropertyDefn propDefn = (ElementPropertyDefn) style
					.getProperty( IStyleModel.BACKGROUND_POSITION_X_PROP );
			assert propDefn != null;
			try
			{
				Object value = propDefn.validateXml( module, positionX );
				properties.put( IStyleModel.BACKGROUND_POSITION_X_PROP, value );
			}
			catch ( PropertyValueException e )
			{
				StyleSheetParserException exception = new StyleSheetParserException(
						StyleSheetParserException.DESIGN_EXCEPTION_INVALID_SIMPLE_CSSPROPERTY_VALUE,
						CssPropertyConstants.ATTR_BACKGROUND_POSITION,
						positionX, e );
				semanticWarning( exception );
				errors.add( exception );
			}
		}
		if ( !StringUtil.isBlank( positionY ) )
		{
			ElementPropertyDefn propDefn = (ElementPropertyDefn) style
					.getProperty( IStyleModel.BACKGROUND_POSITION_Y_PROP );
			assert propDefn != null;
			try
			{
				Object value = propDefn.validateXml( module, positionY );
				properties.put( IStyleModel.BACKGROUND_POSITION_Y_PROP, value );
			}
			catch ( PropertyValueException e )
			{
				StyleSheetParserException exception = new StyleSheetParserException(
						StyleSheetParserException.DESIGN_EXCEPTION_INVALID_SIMPLE_CSSPROPERTY_VALUE,
						CssPropertyConstants.ATTR_BACKGROUND_POSITION,
						positionY, e );
				semanticWarning( exception );
				errors.add( exception );
			}
		}

		return errors;
	}

	void handleTextDecoration( String cssValue, LinkedHashMap properties )
	{
		assert cssValue != null;
		cssValue = cssValue.toLowerCase( );
		String[] values = cssValue.split( "[\\s]" ); //$NON-NLS-1$
		for ( int i = 0; i < values.length; i++ )
		{
			String value = values[i].trim( );
			if ( value
					.equalsIgnoreCase( CssPropertyConstants.TEXT_DECORATION_LINE_THROUGH ) )
				properties.put( IStyleModel.TEXT_LINE_THROUGH_PROP,
						DesignChoiceConstants.TEXT_LINE_THROUGH_LINE_THROUGH );
			else if ( value
					.equalsIgnoreCase( CssPropertyConstants.TEXT_DECORATION_OVERLINE ) )
				properties.put( IStyleModel.TEXT_OVERLINE_PROP,
						DesignChoiceConstants.TEXT_OVERLINE_OVERLINE );
			else if ( value
					.equalsIgnoreCase( CssPropertyConstants.TEXT_DECORATION_UNDERLINE ) )
				properties.put( IStyleModel.TEXT_UNDERLINE_PROP,
						DesignChoiceConstants.TEXT_UNDERLINE_UNDERLINE );
		}
	}

	/**
	 * Adds all the valid property values to the style.
	 * 
	 * @param style
	 *            the style to add property values
	 * @param properties
	 *            the values to add
	 */

	void addProperties( DesignElement style, LinkedHashMap properties )
	{
		Set keys = properties.keySet( );
		Iterator iter = keys.iterator( );
		while ( iter.hasNext( ) )
		{
			String name = (String) iter.next( );
			Object value = properties.get( name );
			style.setProperty( name, value );
		}
	}

	/**
	 * Trims the property values and filters all the empty values.
	 * 
	 * @param properties
	 *            the properties to trim
	 * @return the trimmed property values
	 */

	LinkedHashMap trimProperties( LinkedHashMap properties )
	{
		assert properties != null;
		LinkedHashMap ret = new LinkedHashMap( );
		Iterator keys = properties.keySet( ).iterator( );
		while ( keys.hasNext( ) )
		{
			String key = (String) keys.next( );
			String value = (String) properties.get( key );
			if ( !StringUtil.isBlank( value ) )
				ret.put( key, value );
		}
		return ret;
	}

	/**
	 * Records a style sheet parser exception into the warning list.
	 * 
	 * @param e
	 *            the exception to record
	 */

	void semanticWarning( StyleSheetParserException e )
	{
		warnings.add( e );
		logger.log( Level.WARNING, e.getMessage( ) );
	}
}