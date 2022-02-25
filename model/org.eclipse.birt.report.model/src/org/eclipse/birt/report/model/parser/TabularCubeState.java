/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.parser;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.birt.report.model.api.elements.structures.DimensionCondition;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.interfaces.ITabularCubeModel;
import org.eclipse.birt.report.model.elements.olap.TabularCube;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This class parses a cube element within a report design.
 */

public class TabularCubeState extends ReportElementState {

	/**
	 * The cube being created.
	 */

	protected TabularCube element = null;

	/**
	 * Constructs the cube state with the design parser handler, the container
	 * element and the container slot of the cube.
	 *
	 * @param handler      the design file parser handler
	 * @param theContainer the element that contains this one
	 * @param slot         the slot in which this element appears
	 */

	public TabularCubeState(ModuleParserHandler handler, DesignElement theContainer, int slot) {
		super(handler, theContainer, slot);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.parser.ReportElementState#getElement()
	 */

	@Override
	public DesignElement getElement() {
		return element;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.xml.sax.
	 * Attributes)
	 */

	@Override
	public void parseAttrs(Attributes attrs) throws XMLParserException {
		element = new TabularCube();
		initElement(attrs, true);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.parser.ReportElementState#end()
	 */
	@Override
	public void end() throws SAXException {
		List dimensionConditions = (List) element.getLocalProperty(this.handler.module,
				ITabularCubeModel.DIMENSION_CONDITIONS_PROP);
		LinkedHashMap mergedConditions = new LinkedHashMap();
		if (dimensionConditions != null) {
			for (int i = 0; i < dimensionConditions.size(); i++) {
				DimensionCondition condition = (DimensionCondition) dimensionConditions.get(i);
				ElementRefValue hierarchyRef = (ElementRefValue) condition.getLocalProperty(handler.module,
						DimensionCondition.HIERARCHY_MEMBER);
				String hierarchyName = hierarchyRef.getQualifiedReference();
				if (hierarchyName != null) {
					DimensionCondition temp = (DimensionCondition) mergedConditions.get(hierarchyName);
					if (temp == null) {
						mergedConditions.put(hierarchyName, condition);
					} else {
						// get the join condition list from the cached map
						List tempJoinConditions = (List) temp.getLocalProperty(handler.module,
								DimensionCondition.JOIN_CONDITIONS_MEMBER);
						if (tempJoinConditions == null) {
							tempJoinConditions = new ArrayList();
						}

						// get the join condition list set in the current and
						// merge them with the cached ones
						List joinConditions = (List) condition.getLocalProperty(handler.module,
								DimensionCondition.JOIN_CONDITIONS_MEMBER);
						if (joinConditions != null) {
							tempJoinConditions.addAll(joinConditions);
						}

						// put back the join conditions
						if (!tempJoinConditions.isEmpty()) {
							temp.setProperty(DimensionCondition.JOIN_CONDITIONS_MEMBER, tempJoinConditions);
						}
					}
				}
			}
		}

		List conditionList = new ArrayList(mergedConditions.values());
		if (!conditionList.isEmpty()) {
			element.setProperty(ITabularCubeModel.DIMENSION_CONDITIONS_PROP, conditionList);
		} else {
			element.setProperty(ITabularCubeModel.DIMENSION_CONDITIONS_PROP, null);
		}
		super.end();
	}
}
