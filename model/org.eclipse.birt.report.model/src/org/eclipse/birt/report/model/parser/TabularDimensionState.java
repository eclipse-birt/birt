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

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.NameSpace;
import org.eclipse.birt.report.model.elements.olap.TabularDimension;
import org.eclipse.birt.report.model.elements.strategy.TabularDimensionPropSearchStrategy;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This class parses a Dimension element within a cube.
 *
 */

public class TabularDimensionState extends ReportElementState {

	/**
	 * The dimension being created.
	 */

	protected TabularDimension element = null;

	/**
	 * Constructs dimension state with the design parser handler, the container
	 * element and the container property name of the report element.
	 *
	 * @param handler      the design file parser handler
	 * @param theContainer the element that contains this one
	 * @param prop         the slot in which this element appears
	 */

	public TabularDimensionState(ModuleParserHandler handler, DesignElement theContainer, String prop) {
		super(handler, theContainer, prop);
	}

	/**
	 * Constructs the data source state with the design parser handler, the
	 * container element and the container slot of the data source.
	 *
	 * @param handler the design file parser handler
	 */

	public TabularDimensionState(ModuleParserHandler handler, int slot) {
		super(handler, handler.getModule(), slot);
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
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.
	 * xml.sax.Attributes)
	 */

	@Override
	public void parseAttrs(Attributes attrs) throws XMLParserException {
		element = new TabularDimension();
		initElement(attrs, true);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.parser.ReportElementState#end()
	 */
	@Override
	public void end() throws SAXException {

		super.end();

		// if shared dimension is not null, then remove the element from name
		// space
		NameSpace ns = handler.module.getNameHelper().getNameSpace(Module.DIMENSION_NAME_SPACE);
		DesignElement foundElement = ns.getElement(element.getName());

		if (element.isManagedByNameSpace()) {
			if (container instanceof Module) {
				assert foundElement == element;
			} else {
				assert foundElement == null;
				ns.insert(element);
			}
		} else {
			assert foundElement == null
					|| foundElement == TabularDimensionPropSearchStrategy.getSharedDimension(handler.module, element);
		}

		// update layout to do localization
		if (element.hasSharedDimension(handler.module)) {
			// update the layout properties and handle the id for children
			handler.unhandleCubeDimensions.add(element);

			if (!handler.unhandleIDElements.contains(element)) {
				handler.unhandleIDElements.add(element);
			}
		}
	}

}
