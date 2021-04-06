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

import java.util.List;

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.JointDataSet;
import org.eclipse.birt.report.model.elements.interfaces.IDataSetModel;
import org.eclipse.birt.report.model.util.ModelUtil;
import org.eclipse.birt.report.model.util.VersionUtil;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Parser of the joint data set element.
 * 
 */

public class JointDataSetState extends ReportElementState {

	/**
	 * The joint data set being built.
	 */

	protected JointDataSet element;

	/**
	 * Constructs the joint data set state with design parser handler, container
	 * element and container slot of the data source.
	 * 
	 * @param handler the design file parser handler
	 */

	public JointDataSetState(ModuleParserHandler handler, Module module, int slot) {
		super(handler, module, slot);
		element = new JointDataSet();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.DesignParseState#getElement()
	 */

	public DesignElement getElement() {
		return element;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.
	 * xml.sax.Attributes)
	 */

	public void parseAttrs(Attributes attrs) throws XMLParserException {
		initElement(attrs, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
	 */

	public void end() throws SAXException {
		if (handler.versionNumber < VersionUtil.VERSION_3_2_2) {
			List dataSetColumns = (List) element.getLocalProperty(handler.module, IDataSetModel.RESULT_SET_PROP);
			Object dataSetHints = element.getLocalProperty(handler.module, IDataSetModel.RESULT_SET_HINTS_PROP);
			if (dataSetHints == null && dataSetColumns != null)
				element.setProperty(IDataSetModel.RESULT_SET_HINTS_PROP, ModelUtil
						.copyValue(element.getPropertyDefn(IDataSetModel.RESULT_SET_HINTS_PROP), dataSetColumns));
		}
		super.end();
	}
}