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

package org.eclipse.birt.report.model.elements;

import java.util.List;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.birt.report.model.api.command.ExtendsException;
import org.eclipse.birt.report.model.api.command.WrongTypeException;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.IOdaDataSourceModel;
import org.eclipse.birt.report.model.elements.interfaces.IOdaExtendableElementModel;
import org.eclipse.birt.report.model.extension.IExtendableElement;
import org.eclipse.birt.report.model.extension.oda.ODAProvider;
import org.eclipse.birt.report.model.extension.oda.ODAProviderFactory;
import org.eclipse.birt.report.model.extension.oda.OdaDummyProvider;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ExtensionElementDefn;

/**
 * Represents an extended data source.
 */

public class OdaDataSource extends DataSource
		implements IExtendableElement, IOdaDataSourceModel, IOdaExtendableElementModel {

	/**
	 * ID of the extension which extends this ODA data source.
	 */

	protected String extensionID = null;

	/**
	 * The extensibility provider which provides the functionality of this
	 * extendable element.
	 */

	private ODAProvider provider = null;

	/**
	 * Default constructor.
	 */

	public OdaDataSource() {
		super();
	}

	/**
	 * Constructs an extended data source with name.
	 *
	 * @param theName the name of this extended data source
	 */

	public OdaDataSource(String theName) {
		super(theName);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt
	 * .report.model.elements.ElementVisitor)
	 */
	@Override
	public void apply(ElementVisitor visitor) {
		visitor.visitOdaDataSource(this);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */
	@Override
	public String getElementName() {
		return ReportDesignConstants.ODA_DATA_SOURCE;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.DesignElement#getHandle(org.eclipse
	 * .birt.report.model.elements.ReportDesign)
	 */
	@Override
	public DesignElementHandle getHandle(Module module) {
		return handle(module);
	}

	/**
	 * Returns an API handle for this element.
	 *
	 * @param module the report design
	 * @return an API handle for this element
	 */

	public OdaDataSourceHandle handle(Module module) {
		if (handle == null) {
			handle = new OdaDataSourceHandle(module, this);
		}
		return (OdaDataSourceHandle) handle;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.extension.IExtendable#getExtDefn()
	 */

	@Override
	public ExtensionElementDefn getExtDefn() {
		if (provider != null) {
			return provider.getExtDefn();
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.DesignElement#getPropertyDefns()
	 */

	@Override
	public List<IElementPropertyDefn> getPropertyDefns() {
		if (provider != null && !(provider instanceof OdaDummyProvider)) {
			return provider.getPropertyDefns();
		}

		return super.getPropertyDefns();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.DesignElement#getPropertyDefn(java
	 * .lang.String)
	 */

	@Override
	public ElementPropertyDefn getPropertyDefn(String propName) {
		assert propName != null;

		ElementPropertyDefn propDefn = super.getPropertyDefn(propName);
		if (propDefn != null) {
			return propDefn;
		}

		if (provider != null && !(provider instanceof OdaDummyProvider)) {
			return (ElementPropertyDefn) provider.getPropertyDefn(propName);
		}

		return propDefn;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.DesignElement#getIntrinsicProperty
	 * (java.lang.String)
	 */

	@Override
	protected Object getIntrinsicProperty(String propName) {
		if (EXTENSION_ID_PROP.equals(propName)) {
			return extensionID;
		}
		return super.getIntrinsicProperty(propName);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.DesignElement#setIntrinsicProperty
	 * (java.lang.String, java.lang.Object)
	 */

	@Override
	protected void setIntrinsicProperty(String propName, Object value) {
		if (EXTENSION_ID_PROP.equals(propName)) {
			extensionID = (String) value;
			if (extensionID != null) {
				provider = ODAProviderFactory.getInstance().createODAProvider(this, extensionID);

				// ModelPlugin is not loaded properly

				if (provider == null) {
					return;
				}

				if (!provider.isValidExtensionID()) {
					provider = new OdaDummyProvider(extensionID);
				}
			} else {
				provider = null;
			}

			if (provider != null && provider.getExtDefn() != null) {
				this.cachedDefn = provider.getExtDefn();
			}
		} else {
			super.setIntrinsicProperty(propName, value);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.core.DesignElement#checkExtends(org.eclipse
	 * .birt.report.model.core.DesignElement)
	 */

	@Override
	public void checkExtends(DesignElement parent) throws ExtendsException {
		super.checkExtends(parent);

		if (provider != null && !(provider instanceof OdaDummyProvider)) {
			provider.checkExtends(parent);
		} else {
			OdaDataSource odaParent = (OdaDataSource) parent;

			if ((odaParent.extensionID != null && !odaParent.extensionID.equals(extensionID)) || (extensionID != null && !extensionID.equals(odaParent.extensionID))) {
				throw new WrongTypeException(this, parent, WrongTypeException.DESIGN_EXCEPTION_WRONG_EXTENSION_TYPE);
			}
		}
	}

	/**
	 * Returns the extension provider of the data source.
	 *
	 * @return the extension provider
	 */

	public ODAProvider getProvider() {
		return provider;
	}

}
