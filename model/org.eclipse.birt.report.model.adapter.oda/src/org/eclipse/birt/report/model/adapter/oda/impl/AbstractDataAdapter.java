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

package org.eclipse.birt.report.model.adapter.oda.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.adapter.oda.IODADesignFactory;
import org.eclipse.birt.report.model.adapter.oda.ODADesignFactory;
import org.eclipse.birt.report.model.api.ExtendedPropertyHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.ExtendedProperty;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.api.util.PropertyValueValidationUtil;
import org.eclipse.datatools.connectivity.oda.design.DataSourceDesign;
import org.eclipse.datatools.connectivity.oda.design.Properties;
import org.eclipse.datatools.connectivity.oda.design.Property;
import org.eclipse.emf.common.util.EList;

/**
 *
 */

abstract class AbstractDataAdapter {

	/**
	 * 
	 */

	private static final String RESOURCE_FILE_SUFFIX = ".properties"; //$NON-NLS-1$

	protected final IODADesignFactory designFactory;

	/**
	 * 
	 */

	AbstractDataAdapter() {
		designFactory = ODADesignFactory.getFactory();
	}

	/**
	 * Converts <code>props</code> from Iterator to ODA <code>Properties</code> .
	 * 
	 * @param props the iterator for extended property
	 * @return a new <code>Properties</code> object.
	 */

	protected Properties newOdaPrivateProperties(Iterator props) {
		if (props == null || !props.hasNext())
			return null;

		Properties retProps = designFactory.createProperties();
		for (; props.hasNext();) {
			ExtendedPropertyHandle propHandle = (ExtendedPropertyHandle) props.next();
			retProps.setProperty(propHandle.getName(), propHandle.getValue());
		}

		return retProps;
	}

	/**
	 * Converts ROM public properties to ODA <code>Properties</code> instance.
	 * 
	 * @param sourceHandle the data source handle
	 * @return <code>Properties</code> containing ROM public property values.
	 */

	protected Properties newOdaPublicProperties(List propDefns, ReportElementHandle element) {
		if (propDefns == null)
			return null;

		Properties retProps = null;

		for (int i = 0; i < propDefns.size(); i++) {
			if (retProps == null)
				retProps = designFactory.createProperties();
			IPropertyDefn propDefn = (IPropertyDefn) propDefns.get(i);
			String propName = propDefn.getName();
			String propValue = element.getStringProperty(propName);
			retProps.setProperty(propName, propValue);
		}

		return retProps;
	}

	/**
	 * Converts <code>props</code> from ODA <code>Properties</code> to List.
	 * 
	 * @param props ODA property values.
	 * @return a new <code>List</code> object.
	 */

	protected List newROMPrivateProperties(Properties props) {
		if (props == null)
			return null;

		List list = new ArrayList();
		EList designProps = props.getProperties();
		for (int i = 0; i < designProps.size(); i++) {
			Property prop = (Property) designProps.get(i);
			ExtendedProperty extendedProperty = StructureFactory.createExtendedProperty();
			extendedProperty.setName(prop.getName());
			extendedProperty.setValue(prop.getValue());

			list.add(extendedProperty);
		}

		return list;
	}

	/**
	 * Converts ODA <code>Properties</code> to ROM public properties.
	 * 
	 * @param sourceHandle the data source handle
	 */

	protected void updateROMPublicProperties(Properties designProps, ReportElementHandle sourceHandle)
			throws SemanticException {
		if (designProps == null)
			return;

		EList publicProps = designProps.getProperties();
		for (int i = 0; i < publicProps.size(); i++) {
			Property prop = (Property) publicProps.get(i);

			String propName = prop.getName();
			String propValue = prop.getValue();

			propValue = (String) PropertyValueValidationUtil.validateProperty(sourceHandle, propName, propValue);

			sourceHandle.getElement().setProperty(propName, propValue);
		}
	}

	/**
	 * @param dsDesign
	 * @param root
	 * @throws SemanticException
	 */

	protected void updateROMMessageFile(DataSourceDesign dsDesign, ModuleHandle root) throws SemanticException {
		if (dsDesign == null || root == null)
			return;

		String resourceFile = root.getIncludeResource();
		if (resourceFile != null)
			return;

		resourceFile = dsDesign.getResourceFile();
		if (resourceFile == null)
			return;

		if (!resourceFile.endsWith(RESOURCE_FILE_SUFFIX)) {
			throw new IllegalArgumentException("The DTP resource file must end with " //$NON-NLS-1$
					+ RESOURCE_FILE_SUFFIX);
		}

		int index = resourceFile.indexOf(RESOURCE_FILE_SUFFIX);
		resourceFile = resourceFile.substring(0, index);
		root.setIncludeResource(resourceFile);
	}

	/**
	 * @param dsDesign
	 * @param root
	 */

	protected void updateODAMessageFile(DataSourceDesign dsDesign, ModuleHandle root) {
		String resourceFile = root.getIncludeResource();
		if (resourceFile == null)
			return;

		resourceFile = resourceFile + RESOURCE_FILE_SUFFIX;

		dsDesign.setResourceFile(resourceFile);
	}

}
