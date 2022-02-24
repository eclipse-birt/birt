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

package org.eclipse.birt.report.model.metadata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.MetaDataConstants;

/**
 * Represents the extension element definition for ODA.
 */

public final class ODAExtensionElementDefn extends ExtensionElementDefn {

	private List<IElementPropertyDefn> hidePrivateProps = null;

	/**
	 * Constructs the add-on extension element definition with element definition
	 * name and base element definition.
	 * 
	 * @param baseElementDefn definition of the base element, from which this
	 *                        extension element definition extends.
	 */

	public ODAExtensionElementDefn(IElementDefn baseElementDefn) {
		assert baseElementDefn != null;

		this.name = baseElementDefn.getName();
		this.displayNameKey = (String) baseElementDefn.getDisplayNameKey();
		this.nameConfig.nameOption = MetaDataConstants.REQUIRED_NAME;
		this.allowExtend = baseElementDefn.canExtend();
		this.extendsFrom = baseElementDefn.getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.ElementDefn#build()
	 */

	protected void build() throws MetaDataException {
		if (isBuilt)
			return;

		buildDefn();

		// Cache data for properties defined here. Note, done here so
		// we don't repeat the work for any style properties copied below.

		buildProperties();

		buildPrivateDriverProperties();

		buildPropertiesVisibility();

		buildContainerProperties();

		// set the xml-name

		buildXmlName();

		// build validation trigger
		buildTriggerDefnSet();

		isBuilt = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.ExtensionElementDefn#buildXmlName
	 * ()
	 */
	protected void buildXmlName() {
		String tmpXmlName = null;

		ElementDefn tmpDefn = (ElementDefn) MetaDataDictionary.getInstance()
				.getElement(ReportDesignConstants.ODA_DATA_SET);
		if (isKindOf(tmpDefn)) {
			tmpXmlName = tmpDefn.getXmlName();
		} else {
			tmpDefn = (ElementDefn) MetaDataDictionary.getInstance().getElement(ReportDesignConstants.ODA_DATA_SOURCE);
			if (isKindOf(tmpDefn)) {
				tmpXmlName = tmpDefn.getXmlName();
			}
		}

		setXmlName(tmpXmlName);
	}

	/**
	 * If the visibility of an ODA property is "hidden", it is treated as private
	 * driver property.
	 * 
	 */

	private void buildPrivateDriverProperties() {
		if (propVisibilites == null)
			return;

		Iterator<String> propNames = propVisibilites.keySet().iterator();
		while (propNames.hasNext()) {
			String propName = propNames.next();
			IElementPropertyDefn propDefn = cachedProperties.get(propName);

			if (propDefn.getValueType() != IPropertyDefn.ODA_PROPERTY)
				continue;

			Integer visibility = propVisibilites.get(propName);

			// if not hide visibility is set for this property, do nothing
			if ((visibility.intValue() & HIDDEN_IN_PROPERTY_SHEET_KEY) == 0)
				continue;

			if (hidePrivateProps == null)
				hidePrivateProps = new ArrayList<IElementPropertyDefn>();

			hidePrivateProps.add(cachedProperties.get(propName));
			cachedProperties.remove(propName);
			properties.remove(propName);

		}

		// need to remove the hide private property from the visibility list,
		// otherwise there will be some warning message.
		if (hidePrivateProps != null) {
			for (int i = 0; i < hidePrivateProps.size(); i++) {
				String propName = hidePrivateProps.get(i).getName();
				propVisibilites.remove(propName);
			}

		}
	}

	/**
	 * Returns names of properties that are ODA defined private driver properties in
	 * ODA plug.xml. If the visibility of an ODA property is "hidden", it is treated
	 * as private driver property.
	 * 
	 * 
	 * @return a list containing private driver property names
	 */

	public List<String> getODAPrivateDriverPropertyNames() {
		if (hidePrivateProps == null)
			return Collections.emptyList();

		List<String> retList = new ArrayList<String>();
		for (int i = 0; i < hidePrivateProps.size(); i++) {
			IElementPropertyDefn propDefn = hidePrivateProps.get(i);
			retList.add(propDefn.getName());
		}
		return Collections.unmodifiableList(retList);
	}

	public List<IElementPropertyDefn> getHidePrivateProps() {
		return hidePrivateProps;
	}
}
