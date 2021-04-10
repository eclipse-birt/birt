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
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
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
import org.w3c.css.sac.SimpleSelector;
import org.w3c.dom.css.CSSRule;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.flute.parser.selectors.ClassConditionImpl;

/**
 * Loads an external style sheet to the BIRT.
 */

public final class StyleSheetLoader {

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

	private final IElementDefn style = MetaDataDictionary.getInstance().getStyle();

	/**
	 * The warning list during the loading.
	 */

	private List<StyleSheetParserException> warnings = null;

	/**
	 * The logger to record the warning.
	 */

	private static Logger logger = Logger.getLogger(StyleSheetLoader.class.getName());

	/**
	 * Private constructor to do some reset work.
	 * 
	 */

	public StyleSheetLoader() {
		parser = new CssParser();
		this.module = null;
		this.source = null;
		warnings = new ArrayList<StyleSheetParserException>();
	}

	/**
	 * Re-inits the loader. This method is called when the users want to use the
	 * instance to load more than one external style sheet. Call this every loading
	 * operation.
	 * 
	 */

	public void reInit() {
		parser = new CssParser();
		this.module = null;
		this.source = null;
		warnings = new ArrayList<StyleSheetParserException>();
	}

	/**
	 * Loads styles from an external style sheet resource. A resource can be
	 * something as simple as a file or a directory, or it can be a reference to a
	 * more complicated object, such as a query to a database or to a search engine.
	 * This method will try to create a URL object from the <code>String</code>
	 * representation.
	 * 
	 * @param module the module to load the style sheet
	 * @param url    the url to the spec
	 * @param spec   spec the <code>String</code> to parse as a URL, it can be a
	 *               file or directory, or other types or formats of URLs to locate
	 *               an external style sheet
	 * @return the <code>CssStyleSheet</code> containing all the styles loaded from
	 *         an external style sheet, otherwise null
	 * @throws StyleSheetException if the resource is not found, or the given
	 *                             <code>String</code> is malformed to URL
	 *                             specification, or the style sheet resource has
	 *                             some syntax errors colliding with CSS2 grammar
	 */

	public CssStyleSheet load(Module module, URL url, String spec) throws StyleSheetException

	{
		if (url == null) {
			throw new StyleSheetException(StyleSheetException.DESIGN_EXCEPTION_STYLE_SHEET_NOT_FOUND);
		}

		InputStream is = null;
		try {
			is = url.openStream();
		} catch (IOException e) {
			throw new StyleSheetException(StyleSheetException.DESIGN_EXCEPTION_STYLE_SHEET_NOT_FOUND, e);
		}
		CssStyleSheet sheet = load(module, is);

		// set the path to css style sheet.

		sheet.setFileName(spec);
		return sheet;
	}

	/**
	 * Loads styles from an external style sheet resource. A resource can be
	 * something as simple as a file or a directory, or it can be a reference to a
	 * more complicated object, such as a query to a database or to a search engine.
	 * This method will try to create a URL object from the <code>String</code>
	 * representation.
	 * 
	 * @param module the module to load the style sheet
	 * @param spec   spec the <code>String</code> to parse as a URL, it can be a
	 *               file or directory, or other types or formats of URLs to locate
	 *               an external style sheet
	 * @return the <code>CssStyleSheet</code> containing all the styles loaded from
	 *         an external style sheet, otherwise null
	 * @throws StyleSheetException if the resource is not found, or the given
	 *                             <code>String</code> is malformed to URL
	 *                             specification, or the style sheet resource has
	 *                             some syntax errors colliding with CSS2 grammar
	 */

	public CssStyleSheet load(Module module, String spec) throws StyleSheetException {
		assert module != null;
		this.module = module;

		URL url = module.findResource(spec, IResourceLocator.CASCADING_STYLE_SHEET);
		CssStyleSheet retSheet = load(module, url, spec);

		return retSheet;
	}

	/**
	 * Loads styles from an external style sheet resource.
	 * 
	 * @param module the module to load the style sheet
	 * @param is     spec the <code>String</code> to parse as a URL, it can be a
	 *               file or directory, or other types or formats of URLs to locate
	 *               an external style sheet
	 * @return the <code>CssStyleSheet</code> containing all the styles loaded from
	 *         an external style sheet, otherwise null
	 * @throws StyleSheetException the style sheet resource has some syntax errors
	 *                             colliding with CSS2 grammar
	 */

	public CssStyleSheet load(Module module, InputStream is) throws StyleSheetException {
		assert module != null;
		this.module = module;

		if (is == null) {
			throw new StyleSheetException(StyleSheetException.DESIGN_EXCEPTION_STYLE_SHEET_NOT_FOUND);
		}
		source = new InputStreamReader(is);
		return load(source);
	}

	/**
	 * Loads the styles from an external style sheet resource.
	 * 
	 * @param charStream character stream that shall not include a byte order mark
	 * @return the <code>CssStyleSheet</code> containing all the styles loaded from
	 *         an external style sheet, otherwise null
	 * @throws StyleSheetException the style sheet resource has some syntax errors
	 *                             colliding with CSS2 grammar
	 */

	CssStyleSheet load(Reader charStream) throws StyleSheetException {
		StyleSheet ss = null;
		try {
			InputSource is = new InputSource(source);
			ss = (StyleSheet) parser.parseStyleSheet(is);
		} catch (CSSException e) {
			logger.log(Level.SEVERE, e.getMessage());
			throw new StyleSheetException(StyleSheetException.DESIGN_EXCEPTION_SYNTAX_ERROR, e);
		} catch (IOException e) {
			throw new StyleSheetException(StyleSheetException.DESIGN_EXCEPTION_STYLE_SHEET_NOT_FOUND, e);
		} finally {
			try {
				source.close();
			} catch (IOException e) {
				// Do nothing.
			}
		}

		if (ss == null)
			return null;

		CssStyleSheet styleSheet = new CssStyleSheet();

		List<CSSRule> rules = ss.getRules();
		for (int i = 0; i < rules.size(); i++) {
			CSSRule rule = rules.get(i);
			loadStyle(styleSheet, rule);
		}

		styleSheet.addWarning(warnings);
		styleSheet.setErrorHandler(parser.getErrorHandler());
		return styleSheet;
	}

	/**
	 * Translates a CSS rule into one or more styles with the definition of ROM.
	 * This method only deals with the style rules, while any other rules, such as
	 * import rules, page rules and so on, are ignored. The neglect operations will
	 * be recorded in the log file and added into the warning list. Then all the
	 * created styles by interpretations and translations will be added into the
	 * given <code>CssStyleSheet</code> container.
	 * 
	 * @param styleSheet the style sheet, which is a container defined by Model to
	 *                   store all the styles loaded from the external resource
	 * @param rule       the current CSS rule to handle
	 * @throws StyleSheetException the CSS rule has some syntax errors colliding
	 *                             with CSS2 grammar
	 */

	void loadStyle(CssStyleSheet styleSheet, CSSRule rule) throws StyleSheetException {
		// now only support the style rule

		if (rule.getType() == CSSRule.STYLE_RULE) {
			try {
				StyleRule sr = (StyleRule) rule;
				SelectorList selectionList = sr.getSelectorList();
				CSSStyleDeclaration declaration = sr.getStyle();
				LinkedHashMap<String, ? extends Object> properties = null;
				List<StyleSheetParserException> errors = new ArrayList<StyleSheetParserException>();
				boolean buildProperties = false;
				for (int i = 0; i < selectionList.getLength(); i++) {
					Selector selector = selectionList.item(i);
					int type = selector.getSelectorType();
					String name = null;

					boolean isValid = false;

					switch (type) {
					case Selector.SAC_CONDITIONAL_SELECTOR:

						// such as "*.table", ".table". The deeper
						// conditional selectors, such as '.table.s.t.m' are
						// not supported. Former has an element constraint
						// while the latter has not. For both, we all
						// convert to a style named as "table".

						SimpleSelector simple = ((ConditionalSelector) selector).getSimpleSelector();
						Condition condition = ((ConditionalSelector) selector).getCondition();

						if (simple instanceof ElementSelector || simple == null) {
							// don't allow p.table to be parsed.

							if ((simple == null || ((ElementSelector) simple).getLocalName() == null
									|| ((ElementSelector) simple).getLocalName().equalsIgnoreCase("*")) //$NON-NLS-1$
									&& condition.getConditionType() == Condition.SAC_CLASS_CONDITION) {
								name = ((ClassConditionImpl) condition).getValue();

								isValid = true;
							}
						}

						if (!isValid) {
							StyleSheetParserException exception = new StyleSheetParserException(
									CssUtil.toString(selector),
									StyleSheetParserException.DESIGN_EXCEPTION_STYLE_NOT_SUPPORTED);
							semanticWarning(exception);
							styleSheet.addUnsupportedStyle(CssUtil.toString(selector).toLowerCase(), exception);
							name = null;
						}

						break;

					default:

						StyleSheetParserException exception = new StyleSheetParserException(CssUtil.toString(selector),
								StyleSheetParserException.DESIGN_EXCEPTION_STYLE_NOT_SUPPORTED);
						semanticWarning(exception);
						styleSheet.addUnsupportedStyle(CssUtil.toString(selector).toLowerCase(), exception);
						name = null;

					}
					if (name == null)
						continue;
					DesignElement style = styleSheet.findStyle(name);
					if (style == null)
						style = new CssStyle(name);
					else
						styleSheet.removeStyle(name);
					// set css style sheet
					if (!buildProperties) {
						properties = buildProperties(declaration, errors);
						buildProperties = true;
					}

					addProperties(style, properties);
					assert styleSheet.findStyle(name) == null;
					List<StyleSheetParserException> ret = styleSheet.getWarnings(name);
					if (ret == null) {
						List<StyleSheetParserException> localErrors = new ArrayList<StyleSheetParserException>();
						localErrors.addAll(errors);
						styleSheet.addWarnings(name, localErrors);
					} else {
						ret.addAll(errors);
					}
					styleSheet.addStyle(style);
				}
			} catch (CSSException e) {
				logger.log(Level.SEVERE, e.getMessage());
				throw new StyleSheetException(StyleSheetException.DESIGN_EXCEPTION_SYNTAX_ERROR, e);
			}
		} else {
			semanticWarning(new StyleSheetParserException(rule.toString(),
					StyleSheetParserException.DESIGN_EXCEPTION_RULE_NOT_SUPPORTED));
		}
	}

	/**
	 * Gets all the name/value pairs from a CSS declaration and puts them into a
	 * <code>LinkedHashMap</code>. All the name and value is of string type.
	 * 
	 * @param declaration the declaration of the style rule
	 * @param errors      the error list of the declaration
	 * @return all the supported name/value pairs
	 */

	LinkedHashMap<String, ? extends Object> buildProperties(CSSStyleDeclaration declaration,
			List<StyleSheetParserException> errors) {
		LinkedHashMap<String, String> properties = new LinkedHashMap<String, String>();
		for (int i = 0; i < declaration.getLength(); i++) {
			String cssName = declaration.item(i);
			String cssValue = declaration.getPropertyValue(cssName);
			if (StringUtil.isBlank(cssName) || StringUtil.isBlank(cssValue))
				continue;

			properties.put(cssName, cssValue);
		}

		return buildProperties(properties, errors);
	}

	/**
	 * Converts all the name/value pairs in the CSS2 format to the property values
	 * in BIRT defined format. The name/value pair may be simple, like
	 * "background-color:red" or short-hand, like "background: color
	 * url(images/header)". All the short-hand properties will be separated into
	 * corresponding individuals. Whatever BIRT does not support will be ignored.
	 * 
	 * @param cssProperties the hash map that stores all the property values in CSS
	 *                      format
	 * @param errors        the error list of the properties
	 * @return the name/value pairs in BIRT defined format
	 */

	LinkedHashMap<String, ? extends Object> buildProperties(LinkedHashMap<String, String> cssProperties,
			List<StyleSheetParserException> errors) {
		if (cssProperties.isEmpty())
			return cssProperties;

		LinkedHashMap<String, Object> properties = new LinkedHashMap<String, Object>();
		Iterator<Entry<String, String>> iter = cssProperties.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, String> entry = iter.next();
			String cssName = entry.getKey();
			String cssValue = entry.getValue();

			assert !StringUtil.isBlank(cssName);
			assert !StringUtil.isBlank(cssValue);

			cssName = cssName.toLowerCase();
			try {
				if (cssName.equalsIgnoreCase(CssPropertyConstants.ATTR_BACKGROUND_POSITION)) {
					List<StyleSheetParserException> ret = handleBackgroundPosition(cssValue, properties);
					if (!ret.isEmpty())
						errors.addAll(ret);
				} else if (cssName.equalsIgnoreCase(CssPropertyConstants.ATTR_BACKGROUND_SIZE)) {
					List<StyleSheetParserException> ret = handleBackgroundSize(cssValue, properties);
					if (!ret.isEmpty())
						errors.addAll(ret);
				} else if (cssName.equalsIgnoreCase(CssPropertyConstants.ATTR_TEXT_DECORATION)) {
					handleTextDecoration(cssValue, properties);
				} else if (cssName.equalsIgnoreCase(CssPropertyConstants.ATTR_BORDER_BOTTOM)) {
					PropertyParser parser = new PropertyParser(cssValue);

					parser.parseBorderBottom();
					LinkedHashMap<String, String> shortHand = parser.getCssProperties();
					shortHand = trimProperties(shortHand);
					properties.putAll(buildProperties(shortHand, errors));
				} else if (cssName.equalsIgnoreCase(CssPropertyConstants.ATTR_BORDER_LEFT)) {
					PropertyParser parser = new PropertyParser(cssValue);

					parser.parseBorderLeft();
					LinkedHashMap<String, String> shortHand = parser.getCssProperties();
					shortHand = trimProperties(shortHand);
					properties.putAll(buildProperties(shortHand, errors));

				} else if (cssName.equalsIgnoreCase(CssPropertyConstants.ATTR_BORDER_RIGHT)) {
					PropertyParser parser = new PropertyParser(cssValue);

					parser.parseBorderRight();
					LinkedHashMap<String, String> shortHand = parser.getCssProperties();
					shortHand = trimProperties(shortHand);
					properties.putAll(buildProperties(shortHand, errors));
				} else if (cssName.equalsIgnoreCase(CssPropertyConstants.ATTR_BORDER_TOP)) {
					PropertyParser parser = new PropertyParser(cssValue);

					parser.parseBorderTop();
					LinkedHashMap<String, String> shortHand = parser.getCssProperties();
					shortHand = trimProperties(shortHand);
					properties.putAll(buildProperties(shortHand, errors));
				} else if (cssName.equalsIgnoreCase(CssPropertyConstants.ATTR_BORDER)) {
					PropertyParser parser = new PropertyParser(cssValue);

					parser.parseBorder();
					LinkedHashMap<String, String> shortHand = parser.getCssProperties();
					shortHand = trimProperties(shortHand);
					properties.putAll(buildProperties(shortHand, errors));
				} else if (cssName.equalsIgnoreCase(CssPropertyConstants.ATTR_BORDER_WIDTH)) {
					PropertyParser parser = new PropertyParser(cssValue);

					parser.parseBorderWidth();
					LinkedHashMap<String, String> shortHand = parser.getCssProperties();
					shortHand = trimProperties(shortHand);
					properties.putAll(buildProperties(shortHand, errors));
				} else if (cssName.equalsIgnoreCase(CssPropertyConstants.ATTR_BORDER_COLOR)) {
					PropertyParser parser = new PropertyParser(cssValue);

					parser.parseBorderColor();
					LinkedHashMap<String, String> shortHand = parser.getCssProperties();
					shortHand = trimProperties(shortHand);
					properties.putAll(buildProperties(shortHand, errors));
				} else if (cssName.equalsIgnoreCase(CssPropertyConstants.ATTR_BORDER_STYLE)) {
					PropertyParser parser = new PropertyParser(cssValue);

					parser.parseBorderStyle();
					LinkedHashMap<String, String> shortHand = parser.getCssProperties();
					shortHand = trimProperties(shortHand);
					properties.putAll(buildProperties(shortHand, errors));
				} else if (cssName.equalsIgnoreCase(CssPropertyConstants.ATTR_FONT)) {
					PropertyParser parser = new PropertyParser(cssValue);

					parser.parseFont();
					LinkedHashMap<String, String> shortHand = parser.getCssProperties();
					shortHand = trimProperties(shortHand);
					properties.putAll(buildProperties(shortHand, errors));
				} else if (cssName.equalsIgnoreCase(CssPropertyConstants.ATTR_BACKGROUND)) {
					PropertyParser parser = new PropertyParser(cssValue);

					parser.parseBackground();
					LinkedHashMap<String, String> shortHand = parser.getCssProperties();
					shortHand = trimProperties(shortHand);
					properties.putAll(buildProperties(shortHand, errors));
				} else if (cssName.equalsIgnoreCase(CssPropertyConstants.ATTR_MARGIN)) {
					PropertyParser parser = new PropertyParser(cssValue);

					parser.parseMargin();
					LinkedHashMap<String, String> shortHand = parser.getCssProperties();
					shortHand = trimProperties(shortHand);
					properties.putAll(buildProperties(shortHand, errors));
				} else if (cssName.equalsIgnoreCase(CssPropertyConstants.ATTR_PADDING)) {
					PropertyParser parser = new PropertyParser(cssValue);

					parser.parsePadding();
					LinkedHashMap<String, String> shortHand = parser.getCssProperties();
					shortHand = trimProperties(shortHand);
					properties.putAll(buildProperties(shortHand, errors));
				} else {
					String name = CssPropertyUtil.getPropertyName(cssName);
					if (name == null) {
						StyleSheetParserException exception = new StyleSheetParserException(
								StyleSheetParserException.DESIGN_EXCEPTION_PROPERTY_NOT_SUPPORTED, cssName, cssValue);
						semanticWarning(exception);
						errors.add(exception);
						continue;
					}
					ElementPropertyDefn propDefn = (ElementPropertyDefn) style.getProperty(name);
					assert propDefn != null;

					try {
						String wrongValue = cssValue;
						if (name.equalsIgnoreCase(IStyleModel.BACKGROUND_IMAGE_PROP)) {
							cssValue = CssPropertyUtil.getURLValue(cssValue);
						}
						if (cssValue.equalsIgnoreCase(CssPropertyUtil.WRONG_URL)) {
							StyleSheetParserException exception = new StyleSheetParserException(
									StyleSheetParserException.DESIGN_EXCEPTION_INVALID_SIMPLE_CSSPROPERTY_VALUE,
									cssName, wrongValue);
							semanticWarning(exception);
							errors.add(exception);
						}
						Object value = propDefn.validateXml(module, null, cssValue);
						properties.put(name, value);
					} catch (PropertyValueException e) {
						StyleSheetParserException exception = new StyleSheetParserException(
								StyleSheetParserException.DESIGN_EXCEPTION_INVALID_SIMPLE_CSSPROPERTY_VALUE, cssName,
								cssValue, e);
						semanticWarning(exception);
						errors.add(exception);
					}
				}
			} catch (ParseException e) {
				StyleSheetParserException exception = new StyleSheetParserException(
						StyleSheetParserException.DESIGN_EXCEPTION_INVALID_SHORT_HAND_CSSPROPERTY_VALUE, cssName,
						cssValue, e);
				semanticWarning(exception);
				errors.add(exception);
			} catch (CSSException e) {
				StyleSheetParserException exception = new StyleSheetParserException(
						StyleSheetParserException.DESIGN_EXCEPTION_INVALID_SHORT_HAND_CSSPROPERTY_VALUE, cssName,
						cssValue, e);
				semanticWarning(exception);
				errors.add(exception);
			}
		}
		return properties;
	}

	/**
	 * Converts the background-position property in CSS2 to background-position-X
	 * and background-position-Y in BIRT and adds property values into the given
	 * hash map.
	 * 
	 * @param cssValue   the value of the background-position
	 * @param properties the hash map to store the result property values
	 * @return the error list during the parse
	 */

	private List<StyleSheetParserException> handleBackgroundPosition(String cssValue,
			LinkedHashMap<String, Object> properties) {
		assert cssValue != null;
		List<StyleSheetParserException> errors = new ArrayList<StyleSheetParserException>();

		String[] values = cssValue.split("[\\s]"); //$NON-NLS-1$
		String positionX = null;
		String positionY = null;
		switch (values.length) {
		case 0:

			break;

		case 1:

			positionX = values[0].trim();
			break;

		case 2:

			positionX = values[0].trim();
			positionY = values[1].trim();
			break;

		default:

			StyleSheetParserException exception = new StyleSheetParserException(
					StyleSheetParserException.DESIGN_EXCEPTION_INVALID_SHORT_HAND_CSSPROPERTY_VALUE,
					CssPropertyConstants.ATTR_BACKGROUND_POSITION, cssValue);
			semanticWarning(exception);
			errors.add(exception);
			break;
		}
		errors.addAll(handleBackgroundValue(CssPropertyConstants.ATTR_BACKGROUND_POSITION,
				IStyleModel.BACKGROUND_POSITION_X_PROP, positionX, properties));

		errors.addAll(handleBackgroundValue(CssPropertyConstants.ATTR_BACKGROUND_POSITION,
				IStyleModel.BACKGROUND_POSITION_Y_PROP, positionY, properties));

		return errors;
	}

	/**
	 * Converts the background-size property in CSS3 to background-size-width and
	 * background-size-height in BIRT and adds property values into the given hash
	 * map.
	 * 
	 * @param cssValue   the value of the background-size
	 * @param properties the hash map to store the result property values
	 * @return the error list during the parse
	 */
	private List<StyleSheetParserException> handleBackgroundSize(String cssValue,
			LinkedHashMap<String, Object> properties) {
		assert cssValue != null;
		List<StyleSheetParserException> errors = new ArrayList<StyleSheetParserException>();

		String[] values = cssValue.split("[\\s]"); //$NON-NLS-1$
		String sizeWidth = null;
		String sizeHeight = null;
		switch (values.length) {
		case 0:

			break;

		case 1:

			sizeWidth = values[0].trim();
			break;

		case 2:

			sizeWidth = values[0].trim();
			sizeHeight = values[1].trim();
			break;

		default:

			StyleSheetParserException exception = new StyleSheetParserException(
					StyleSheetParserException.DESIGN_EXCEPTION_INVALID_SHORT_HAND_CSSPROPERTY_VALUE,
					CssPropertyConstants.ATTR_BACKGROUND_SIZE, cssValue);
			semanticWarning(exception);
			errors.add(exception);
			break;
		}
		errors.addAll(handleBackgroundValue(CssPropertyConstants.ATTR_BACKGROUND_SIZE,
				IStyleModel.BACKGROUND_SIZE_WIDTH, sizeWidth, properties));

		errors.addAll(handleBackgroundValue(CssPropertyConstants.ATTR_BACKGROUND_SIZE,
				IStyleModel.BACKGROUND_SIZE_HEIGHT, sizeHeight, properties));

		return errors;

	}

	/**
	 * Validates and sets the css background value.
	 * 
	 * @param cssName         the css name
	 * @param backgroundProp  the background property name
	 * @param backgroundValue the background css value
	 * @param properties      the hash map to store the result property values
	 * @return the error list during the parse
	 */
	private List<StyleSheetParserException> handleBackgroundValue(String cssName, String backgroundProp,
			String backgroundValue, LinkedHashMap<String, Object> properties) {
		if (!StringUtil.isBlank(backgroundValue)) {
			ElementPropertyDefn propDefn = (ElementPropertyDefn) style.getProperty(backgroundProp);

			assert propDefn != null;

			try {
				Object value = propDefn.validateXml(module, null, backgroundValue);
				properties.put(backgroundProp, value);
			} catch (PropertyValueException e) {
				StyleSheetParserException exception = new StyleSheetParserException(
						StyleSheetParserException.DESIGN_EXCEPTION_INVALID_SIMPLE_CSSPROPERTY_VALUE, cssName,
						backgroundValue, e);
				semanticWarning(exception);
				List<StyleSheetParserException> errors = new ArrayList<StyleSheetParserException>();
				errors.add(exception);
				return errors;
			}
		}
		return Collections.emptyList();
	}

	private void handleTextDecoration(String cssValue, LinkedHashMap<String, Object> properties) {
		assert cssValue != null;
		cssValue = cssValue.toLowerCase();
		String[] values = cssValue.split("[\\s]"); //$NON-NLS-1$
		for (int i = 0; i < values.length; i++) {
			String value = values[i].trim();
			if (value.equalsIgnoreCase(CssPropertyConstants.TEXT_DECORATION_LINE_THROUGH))
				properties.put(IStyleModel.TEXT_LINE_THROUGH_PROP,
						DesignChoiceConstants.TEXT_LINE_THROUGH_LINE_THROUGH);
			else if (value.equalsIgnoreCase(CssPropertyConstants.TEXT_DECORATION_OVERLINE))
				properties.put(IStyleModel.TEXT_OVERLINE_PROP, DesignChoiceConstants.TEXT_OVERLINE_OVERLINE);
			else if (value.equalsIgnoreCase(CssPropertyConstants.TEXT_DECORATION_UNDERLINE))
				properties.put(IStyleModel.TEXT_UNDERLINE_PROP, DesignChoiceConstants.TEXT_UNDERLINE_UNDERLINE);
		}
	}

	/**
	 * Adds all the valid property values to the style.
	 * 
	 * @param style      the style to add property values
	 * @param properties the values to add
	 */

	void addProperties(DesignElement style, LinkedHashMap<String, ? extends Object> properties) {
		Iterator iter = properties.entrySet().iterator();
		while (iter.hasNext()) {
			Entry entry = (Entry) iter.next();
			String name = (String) entry.getKey();
			Object value = entry.getValue();
			style.setProperty(name, value);
		}
	}

	/**
	 * Trims the property values and filters all the empty values.
	 * 
	 * @param properties the properties to trim
	 * @return the trimmed property values
	 */

	LinkedHashMap<String, String> trimProperties(LinkedHashMap<String, String> properties) {
		assert properties != null;
		LinkedHashMap<String, String> ret = new LinkedHashMap<String, String>();
		Iterator<Entry<String, String>> iter = properties.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, String> entry = iter.next();
			String key = entry.getKey();
			String value = entry.getValue();
			if (!StringUtil.isBlank(value))
				ret.put(key, value);
		}
		return ret;
	}

	/**
	 * Records a style sheet parser exception into the warning list.
	 * 
	 * @param e the exception to record
	 */

	void semanticWarning(StyleSheetParserException e) {
		warnings.add(e);
		logger.log(Level.WARNING, e.getLocalizedMessage());
	}
}