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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.NameSpace;
import org.eclipse.birt.report.model.core.namespace.INameHelper;
import org.eclipse.birt.report.model.core.namespace.NameExecutor;
import org.eclipse.birt.report.model.elements.Parameter;
import org.eclipse.birt.report.model.elements.ParameterGroup;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.AnyElementState;
import org.eclipse.birt.report.model.util.ContentIterator;
import org.eclipse.birt.report.model.util.VersionUtil;
import org.eclipse.birt.report.model.util.XMLParserHandler;
import org.xml.sax.SAXException;

/**
 * This class parses the contents of the list of parameters.
 * 
 */

public class ParametersState extends SlotState {

	/**
	 * Constructs the parameters state with the design parser handler, the container
	 * element and the container slot of the parameters.
	 * 
	 * @param handler   the design file parser handler.
	 * @param container the container of this the parameter and parameter group.
	 *                  Here, the container can only be either ReportDesign or
	 *                  ParameterGroup.
	 * @param slotID    the slot id of the slot where the parameter/parametergroup
	 *                  is stored.
	 */

	public ParametersState(ModuleParserHandler handler, DesignElement container, int slotID) {
		super(handler, container, slotID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java
	 * .lang.String)
	 */

	public AbstractParseState startElement(String tagName) {
		int tagValue = tagName.toLowerCase().hashCode();

		if (ParserSchemaConstants.PARAMETER_GROUP_TAG == tagValue)
			return new ParameterGroupState(handler, slotID);
		if (ParserSchemaConstants.CASCADING_PARAMETER_GROUP_TAG == tagValue)
			return new CascadingParameterGroupState(handler, slotID);
		if (ParserSchemaConstants.SCALAR_PARAMETER_TAG == tagValue)
			return new ScalarParameterState(handler, container, slotID);
		if (ParserSchemaConstants.DYNAMIC_FILTER_PARAMETER_TAG == tagValue)
			return new DynamicFilterParameterState(handler, container, slotID);
		if (ParserSchemaConstants.FILTER_PARAMETER_TAG == tagValue)
			return new AnyElementState(handler);
		if (ParserSchemaConstants.LIST_PARAMETER_TAG == tagValue)
			return new AnyElementState(handler);
		if (ParserSchemaConstants.TABLE_PARAMETER_TAG == tagValue)
			return new AnyElementState(handler);
		return super.startElement(tagName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#getHandler()
	 */

	public XMLParserHandler getHandler() {
		return handler;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
	 */
	public void end() throws SAXException {
		super.end();

		// if the container is module, then do the backward compatibility,
		// otherwise the container is parameter group, do nothing; for we must
		// do compatibility until all parameter and parameter group is parsed.
		if (handler.versionNumber < VersionUtil.VERSION_3_2_21 && container instanceof Module) {
			checkParameterNames();
		}
	}

	private void checkParameterNames() {
		Module module = (Module) container;
		Iterator<DesignElement> iter = new ContentIterator(module, new ContainerContext(module, Module.PARAMETER_SLOT));
		List<DesignElement> params = new ArrayList<DesignElement>();

		// build parameter list
		while (iter.hasNext()) {
			DesignElement content = iter.next();
			if (content instanceof Parameter || content instanceof ParameterGroup)
				params.add(content);
		}

		if (!params.isEmpty()) {
			// get the built style name map
			Map<String, DesignElement> parameterMap = buildNameMap(params);

			for (int i = 0; i < params.size(); i++) {
				DesignElement param = params.get(i);
				String oldName = param.getName();

				NameExecutor executor = new NameExecutor((Module) container, param);
				INameHelper nameHelper = executor.getNameHelper();
				if (nameHelper == null) {
					continue;
				}
				NameSpace ns = executor.getNameSpace();

				// check the unique
				String paramName = param.getName();
				String lowerCaseName = paramName.toLowerCase();
				if (parameterMap.containsKey(lowerCaseName) && parameterMap.get(lowerCaseName) != param) {
					String baseName = paramName;
					String name = paramName;
					int index = 0;
					// parameter name is case-insensitive
					while (parameterMap.containsKey(lowerCaseName) && parameterMap.get(lowerCaseName) != param) {
						name = baseName + ++index;
						lowerCaseName = name.toLowerCase();
					}

					// rename the parameter and then add to the name space
					param.setName(name);
					if (!ns.contains(name.toLowerCase()))
						ns.insert(param);
					// do the cache
					if (!parameterMap.containsKey(name.toLowerCase()))
						parameterMap.put(name.toLowerCase(), param);
					// remove the old name
					if (parameterMap.get(paramName.toLowerCase()) == param)
						parameterMap.remove(paramName.toLowerCase());

					// set-up the oldName/newName map(rename relationship) to
					// help update the binding in report items
					Map<String, String> nameMaps = (Map<String, String>) handler.tempValue
							.get(ModuleParserHandler.PARAMETER_NAME_CACHE_KEY);
					if (nameMaps == null) {
						nameMaps = new HashMap<String, String>();
						handler.tempValue.put(ModuleParserHandler.PARAMETER_NAME_CACHE_KEY, nameMaps);
					}
					nameMaps.put(oldName, name);
				} else {
					if (!ns.contains(lowerCaseName))
						ns.insert(param);
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
	private Map<String, DesignElement> buildNameMap(List<DesignElement> params) {
		Map<String, DesignElement> paramMap = new HashMap<String, DesignElement>();
		for (int i = 0; i < params.size(); i++) {
			DesignElement param = params.get(i);
			String styleName = param.getName();
			String lowerName = styleName.toLowerCase();
			if (!paramMap.containsKey(lowerName))
				paramMap.put(lowerName, param);
		}

		return paramMap;
	}

}
