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

import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.xml.sax.SAXException;

/**
 * Parses the ComputedColumn structure tag for compatibility.
 * <p>
 * Provide back-compatibility for original "ColumnName" member for
 * ComputedColumn, current version use "name" instead.
 * <p>
 * The compatible version is 0 and 1.
 * 
 * <pre>
 *  
 *   Old design file:
 *   
 *   &lt;list-property name=&quot;computedColumns&quot;&gt;
 *     &lt;structure&gt;
 *       &lt;property name=&quot;columnName&quot;&gt;column1&lt;/property&gt;
 *     &lt;/structure&gt;
 *   &lt;/list-property&gt;
 *  
 *   New design file:
 *   
 *   &lt;list-property name=&quot;computedColumns&quot;&gt;
 *     &lt;structure&gt;
 *       &lt;property name=&quot;name&quot;&gt;column1&lt;/property&gt;
 *     &lt;/structure&gt;
 *   &lt;/list-property&gt;
 * </pre>
 */

public class CompatibleComputedColumnStructureState extends CompatibleStructureState {

	CompatibleComputedColumnStructureState(ModuleParserHandler theHandler, DesignElement element,
			PropertyDefn propDefn) {
		super(theHandler, element, propDefn);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.
	 * String)
	 */

	public AbstractParseState startElement(String tagName) {
		if (tagName.equalsIgnoreCase(DesignSchemaConstants.PROPERTY_TAG))
			return new CompatibleComputedColumnPropertyState(handler, element, propDefn, struct);
		return super.startElement(tagName);
	}

	static class CompatibleComputedColumnPropertyState extends CompatiblePropertyState {

		CompatibleComputedColumnPropertyState(ModuleParserHandler theHandler, DesignElement element,
				PropertyDefn propDefn, IStructure struct) {
			super(theHandler, element, propDefn, struct);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
		 */

		public void end() throws SAXException {
			if ("columnName".equals(name)) //$NON-NLS-1$
			{
				String value = text.toString();

				assert (struct != null);
				assert (propDefn != null);

				setMember(struct, propDefn.getName(), ComputedColumn.NAME_MEMBER, value);
				return;
			}

			super.end();
		}
	}
}
