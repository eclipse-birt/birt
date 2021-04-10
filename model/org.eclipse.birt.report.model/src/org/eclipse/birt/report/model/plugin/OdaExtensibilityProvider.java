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

package org.eclipse.birt.report.model.plugin;

import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.model.api.command.ExtendsException;
import org.eclipse.birt.report.model.api.command.WrongTypeException;
import org.eclipse.birt.report.model.api.core.UserPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.OdaDataSet;
import org.eclipse.birt.report.model.elements.OdaDataSource;
import org.eclipse.birt.report.model.elements.interfaces.IOdaExtendableElementModel;
import org.eclipse.birt.report.model.extension.ExtensibilityProvider;
import org.eclipse.birt.report.model.extension.oda.ODAProvider;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ExtensionElementDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.datatools.connectivity.oda.util.manifest.DataSetType;
import org.eclipse.datatools.connectivity.oda.util.manifest.ExtensionManifest;

/**
 * Provides ODA extensibility.
 */

public class OdaExtensibilityProvider extends ExtensibilityProvider implements ODAProvider {

	/**
	 * ID of the extension which is used to extend the extendable element.
	 */

	String extensionID = null;

	/**
	 * Constructs ODA extensibility provider with the element to extend and
	 * extension ID.
	 * 
	 * @param element     the element to extend
	 * @param extensionID the ID of the extension which provides property
	 *                    definition.
	 */

	public OdaExtensibilityProvider(DesignElement element, String extensionID) {
		super(element);
		if (element == null)
			throw new IllegalArgumentException("element can not be null!"); //$NON-NLS-1$

		this.extensionID = extensionID;
		cachedExtDefn = extensionID == null ? null
				: (ExtensionElementDefn) MetaDataDictionary.getInstance().getElement(extensionID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.extension.ExtensibilityProvider#
	 * getPropertyDefns()
	 */

	public List<IElementPropertyDefn> getPropertyDefns() {
		if (getExtDefn() == null)
			return Collections.emptyList();

		List<IElementPropertyDefn> list = getExtDefn().getProperties();
		List<UserPropertyDefn> userProps = element.getUserProperties();
		if (userProps != null)
			list.addAll(userProps);

		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.extension.ExtensibilityProvider#getPropertyDefn
	 * (java.lang.String)
	 */

	public ElementPropertyDefn getPropertyDefn(String propName) {
		if (getExtDefn() == null)
			return null;

		ElementPropertyDefn propDefn = (ElementPropertyDefn) getExtDefn().getProperty(propName);

		if (propDefn == null)
			propDefn = element.getUserPropertyDefn(propName);
		return propDefn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.extension.ExtensibilityProvider#checkExtends
	 * (org.eclipse.birt.report.model.core.DesignElement)
	 */

	public void checkExtends(DesignElement parent) throws ExtendsException {
		String parentExt = (String) parent.getProperty(null, IOdaExtendableElementModel.EXTENSION_ID_PROP);

		assert extensionID != null;
		if (!extensionID.equalsIgnoreCase(parentExt))
			throw new WrongTypeException(element, parent, WrongTypeException.DESIGN_EXCEPTION_WRONG_EXTENSION_TYPE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.IExtendableElement#getExtDefn()
	 */

	public ExtensionElementDefn getExtDefn() {
		return cachedExtDefn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.extension.oda.ODAProvider#convertExtensionID
	 * (java.lang.String)
	 */

	public String convertExtensionID() {
		if (element instanceof OdaDataSource) {
			String id = extensionID;
			ExtensionManifest manifest = ODAManifestUtil.getDataSourceExtension(id);

			if (manifest != null && manifest.isDeprecated()) {
				id = manifest.getRelatedDataSourceId();
			}
			return id;
		} else if (element instanceof OdaDataSet) {
			String id = extensionID;
			DataSetType type = ODAManifestUtil.getDataSetExtension(id);
			if (type != null && type.isDeprecated()) {
				id = type.getRelatedDataSetId();
			}
			return id;
		}
		assert false;
		return null;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.extension.oda.ODAProvider#isValidExtensionID ()
	 */

	public boolean isValidExtensionID() {
		if (element instanceof OdaDataSet && ODAManifestUtil.getDataSetExtension(extensionID) != null)
			return true;
		if (element instanceof OdaDataSource && ODAManifestUtil.getDataSourceExtension(extensionID) != null)
			return true;
		return false;
	}

}