/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.model.parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.NameSpace;
import org.eclipse.birt.report.model.core.namespace.NameExecutor;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.VersionUtil;
import org.xml.sax.SAXException;

/**
 * Parses the contents of the list of styles. This can be called by ReportState
 * and ThemeState.
 */
public class StylesState extends SlotState {

	private static final String STYLE_NAME_START = "([a-z]|[^\0-\177]|((\\[0-9a-f]{1,6}[ \n\r\t\f]?)|\\[ -~\200-\4177777]))"; //$NON-NLS-1$
	public static final Pattern styleNameStartPattern = Pattern.compile(STYLE_NAME_START, Pattern.CASE_INSENSITIVE);

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement
	 * (java.lang.String)
	 */

	public StylesState(ModuleParserHandler handler, DesignElement container, int slot) {
		super(handler, container, slot);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement
	 * (java.lang.String)
	 */

	@Override
	public AbstractParseState startElement(String tagName) {
		int tagValue = tagName.toLowerCase().hashCode();
		if (ParserSchemaConstants.STYLE_TAG == tagValue) {
			return new StyleState(handler, container, slotID);
		}
		return super.startElement(tagName);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
	 */
	@Override
	public void end() throws SAXException {
		// if version < 3.2.19, do some compatibilities about style name; from
		// 3.2.19, all style name is case-insensitive
		if (handler.versionNumber < VersionUtil.VERSION_3_2_19) {
			List<DesignElement> styles = container.getSlot(slotID).getContents();
			checkStyleName(styles);
		}
		super.end();
	}

	/**
	 * Checks the name of the styles in the list. If some style has the duplicate
	 * name except the different cases, this method will rename it to have different
	 * name even ignoring the cases.
	 *
	 * @param styles
	 */
	private void checkStyleName(List<DesignElement> styles) {
		if (styles != null && !styles.isEmpty()) {
			// get the built style name map
			Map<String, DesignElement> styleMap = buildNameMap(styles);

			for (int i = 0; i < styles.size(); i++) {
				DesignElement style = styles.get(i);
				String oldName = style.getName();

				// we need NOT check the validation of the style name, Engine
				// should ensure the validation of the style name in html output
				// checkValidation( style, styleMap );

				NameSpace ns = null;
				if (style.getContainer() instanceof ReportDesign) {
					NameExecutor executor = new NameExecutor((ReportDesign) style.getContainer(), style);
					ns = executor.getNameSpace();
				}

				// check the unique
				String styleName = style.getName();
				String lowerCaseName = styleName.toLowerCase();
				if (styleMap.containsKey(lowerCaseName) && styleMap.get(lowerCaseName) != style) {
					String baseName = styleName;
					String name = styleName;
					int index = 0;
					// style name is case-insensitive
					while (styleMap.containsKey(lowerCaseName) && styleMap.get(lowerCaseName) != style) {
						name = baseName + ++index;
						lowerCaseName = name.toLowerCase();
					}

					// rename the style and then add to the name space
					style.setName(name);
					if (ns != null && !ns.contains(name.toLowerCase())) {
						ns.insert(style);
					}
					// do the cache
					if (!styleMap.containsKey(name.toLowerCase())) {
						styleMap.put(name.toLowerCase(), style);
					}
					// remove the old name
					if (styleMap.get(styleName.toLowerCase()) == style) {
						styleMap.remove(styleName.toLowerCase());
					}

					// set-up the oldName/newName map(rename relationship) to
					// help the resolve the style for report items
					Map<String, String> nameMaps = (Map<String, String>) handler.tempValue.get(container);
					if (nameMaps == null) {
						nameMaps = new HashMap<>();
						handler.tempValue.put(container, nameMaps);
					}
					nameMaps.put(oldName, name);
				} else if (ns != null && !ns.contains(lowerCaseName)) {
					ns.insert(style);
				}
			}
		}

	}

	/**
	 * Builds a map for the style elements. Key is the lower-case name for the
	 * element and value is the first element that has the with the name for the
	 * key. If two elements have the same name except the different cases, we will
	 * store the first element in the map and ignore others.
	 *
	 * @param styles
	 * @return
	 */
	private Map<String, DesignElement> buildNameMap(List<DesignElement> styles) {
		Map<String, DesignElement> styleMap = new HashMap<>();
		for (int i = 0; i < styles.size(); i++) {
			DesignElement style = styles.get(i);
			String styleName = style.getName();
			String lowerName = styleName.toLowerCase();
			if (!styleMap.containsKey(lowerName)) {
				styleMap.put(lowerName, style);
			}
		}

		return styleMap;
	}

//	private void checkValidation( DesignElement style,
//			Map<String, DesignElement> styleMap )
//	{
//		String styleName = style.getName( );
//
//		// first, check the validation of the name
//		if ( !NameCommand.styleNamePattern.matcher( styleName ).matches( ) )
//		{
//			String newName = styleName;
//			newName = newName.replaceAll( STYLE_NAME_FORBIDDEN_PATTERN,
//					MIDDLE_LINE );
//
//			// if the new name is not start with letters, it is illegal too
//			String firstChar = newName.substring( 0, 1 );
//			if ( !styleNameStartPattern.matcher( firstChar ).matches( ) )
//			{
//				if ( newName.length( ) <= 1 )
//					newName = REPLACE_LETTER;
//				else
//					newName = REPLACE_LETTER + newName.substring( 1 );
//			}
//
//			// TODO: ensure the new name is valid for the css2 spec; the
//			// worst way is setting the name to NULL and then calling
//			// makeUnique to generate a valid and unique name
//			assert NameCommand.styleNamePattern.matcher( newName ).matches( );
//			style.setName( newName );
//			// set-up name-map
//			Map<String, String> nameMap = (Map<String, String>) handler.tempValue
//					.get( style.getContainer( ) );
//			if ( nameMap == null )
//			{
//				nameMap = new HashMap<String, String>( );
//				handler.tempValue.put( style.getContainer( ), nameMap );
//			}
//			nameMap.put( styleName, newName );
//
//			if ( !styleMap.containsKey( newName.toLowerCase( ) ) )
//				styleMap.put( newName.toLowerCase( ), style );
//			// remove the old name
//			if ( styleMap.get( styleName.toLowerCase( ) ) == style )
//				styleMap.remove( styleName.toLowerCase( ) );
//		}
//	}
}
