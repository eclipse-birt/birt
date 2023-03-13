
package org.eclipse.birt.report.model.adapter.oda.impl;

import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.util.PropertyValueValidationUtil;
import org.eclipse.datatools.connectivity.oda.design.DataSourceDesign;
import org.eclipse.datatools.connectivity.oda.design.Properties;
import org.eclipse.datatools.connectivity.oda.design.Property;
import org.eclipse.datatools.connectivity.oda.design.util.DesignUtil;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.EcoreUtil.EqualityHelper;

/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-2.0.html
 *
 * Contributors: Actuate Corporation - initial API and implementation
 *******************************************************************************/

public class DataSourceAdapter extends AbstractDataAdapter {

	private final boolean updateName;

	/**
	 *
	 */

	DataSourceAdapter() {
		this(true);
	}

	DataSourceAdapter(boolean updateName) {
		super();
		this.updateName = updateName;
	}

	/**
	 * @param sourceHandle
	 * @return
	 */

	public DataSourceDesign createDataSourceDesign(OdaDataSourceHandle sourceHandle) {
		if (sourceHandle == null) {
			return null;
		}

		DataSourceDesign sourceDesign = designFactory.createDataSourceDesign();
		updateDataSourceDesign(sourceHandle, sourceDesign);
		return sourceDesign;
	}

	/**
	 * @param sourceHandle
	 * @param sourceDesign
	 */

	public void updateDataSourceDesign(OdaDataSourceHandle sourceHandle, DataSourceDesign sourceDesign) {
		// properties on ReportElement, like name, displayNames, etc.

		sourceDesign.setName(sourceHandle.getName());

		String displayName = sourceHandle.getDisplayName();
		String displayNameKey = sourceHandle.getDisplayNameKey();

		if (displayName != null || displayNameKey != null) {
			sourceDesign.setDisplayName(displayName);
			sourceDesign.setDisplayNameKey(displayNameKey);
		}

		// properties such as comments, extends, etc are kept in
		// DataSourceHandle, not DataSourceDesign.

		// scripts of DataSource are kept in
		// DataSourceHandle, not DataSourceDesign.

		sourceDesign.setOdaExtensionId(sourceHandle.getExtensionID());

		// override the private and public properties in oda design by those in
		// element handle
		sourceDesign.setPrivateProperties(newOdaPrivateProperties(sourceHandle.privateDriverPropertiesIterator()));

		sourceDesign.setPublicProperties(
				newOdaPublicProperties(sourceHandle.getExtensionPropertyDefinitionList(), sourceHandle));

		updateODAMessageFile(sourceDesign, sourceHandle.getModuleHandle());
	}

	public OdaDataSourceHandle createDataSourceHandle(DataSourceDesign sourceDesign, ModuleHandle module)
			throws SemanticException, IllegalStateException {
		if (sourceDesign == null) {
			return null;
		}

		// validate the source design to make sure it is valid

		DesignUtil.validateObject(sourceDesign);
		OdaDataSourceHandle sourceHandle = module.getElementFactory().newOdaDataSource(sourceDesign.getName(),
				sourceDesign.getOdaExtensionId());

		if (sourceHandle == null) {
			return null;
		}

		adaptDataSourceDesign(sourceDesign, sourceHandle);
		return sourceHandle;
	}

	public void updateDataSourceHandle(DataSourceDesign sourceDesign, OdaDataSourceHandle sourceHandle)
			throws SemanticException {
		if (sourceDesign == null || sourceHandle == null || (sourceHandle.getExtends() != null)
				&& isDataSourceHandleAndDataSourceDesignEqual(sourceHandle, sourceDesign)) {
			return;
		}

		DesignUtil.validateObject(sourceDesign);
		CommandStack stack = sourceHandle.getModuleHandle().getCommandStack();

		stack.startTrans(null);
		try {
			// extension id is set without undo/redo support.

			sourceHandle.setProperty(OdaDataSourceHandle.EXTENSION_ID_PROP, sourceDesign.getOdaExtensionId());

			if (updateName) {
				sourceHandle.setName(sourceDesign.getName());
			}

			sourceHandle.setDisplayName(sourceDesign.getDisplayName());
			sourceHandle.setDisplayNameKey(sourceDesign.getDisplayNameKey());

			// set public properties.

			Properties props = sourceDesign.getPublicProperties();
			if (props != null) {
				EList propList = props.getProperties();
				for (int i = 0; i < propList.size(); i++) {
					Property prop = (Property) propList.get(i);
					sourceHandle.setProperty(prop.getName(), prop.getValue());
				}
			}

			// updateROMPropertyBindings( props, sourceHandle );

			// set private properties.

			props = sourceDesign.getPrivateProperties();
			if (props != null) {
				EList propList = props.getProperties();
				for (int i = 0; i < propList.size(); i++) {
					Property prop = (Property) propList.get(i);
					sourceHandle.setPrivateDriverProperty(prop.getName(), prop.getValue());
				}
			}

			updateROMMessageFile(sourceDesign, sourceHandle.getModuleHandle());
		} catch (SemanticException e) {
			stack.rollback();
			throw e;
		}

		stack.commit();
	}

	public boolean isDataSourceHandleAndDataSourceDesignEqual(OdaDataSourceHandle sourceHandle,
			DataSourceDesign sourceDesign) {

		EqualityHelper equalityHelper = new EcoreUtil.EqualityHelper();
		// compare public properties
		Properties sourceHandlePublicProperties = newOdaPublicProperties(
				sourceHandle.getExtensionPropertyDefinitionList(), sourceHandle);
		if (!equalityHelper.equals(sourceHandlePublicProperties, sourceDesign.getPublicProperties())) {
			return false;
		}

		// compare private properties
		Properties sourceHandlePrivateProperties = newOdaPrivateProperties(
				sourceHandle.privateDriverPropertiesIterator());
		return equalityHelper.equals(sourceHandlePrivateProperties, sourceDesign.getPrivateProperties());
	}

	public boolean isEqualDataSourceDesign(DataSourceDesign designFromHandle, DataSourceDesign design) {
		if (designFromHandle == null && design == null) {
			return true;
		}

		if ((designFromHandle != null && design == null) || (designFromHandle == null && design != null)) {
			return false;
		}

		assert designFromHandle != null;

		// both must be not null

		Properties handleProps = designFromHandle.getPublicProperties();
		Properties props = design.getPublicProperties();

		if (handleProps == null && props == null) {
			return true;
		}

		if (handleProps != null && props == null) {
			return false;
		}

		if (handleProps == null && props != null) {
			return false;
		}

		assert handleProps != null;
		assert props != null;

		EList publicProps = handleProps.getProperties();
		for (int i = 0; i < publicProps.size(); i++) {
			Property prop = (Property) publicProps.get(i);
			String propValue = prop.getValue();
			String propName = prop.getName();
			if (propValue == null) {
				String value = props.getProperty(propName);
				if (value != null && value.trim().equals("")) { // $NON-NLS-1$
					prop.setNameValue(prop.getName(), ""); //$NON-NLS-1$
				}
			}
		}

		DataSourceDesign tmpDesign = (DataSourceDesign) EcoreUtil.copy(design);
		tmpDesign.setHostResourceIdentifiers(null);

		return new EcoreUtil.EqualityHelper().equals(designFromHandle, tmpDesign);
	}

	/**
	 * Copies values of <code>sourceDesign</code> to <code>sourceHandle</code>.
	 * Values in <code>sourceDesign</code> are validated before maps to values in
	 * OdaDataSourceHandle.
	 *
	 * @param sourceDesign the ODA data source design
	 * @param sourceHandle the Model handle
	 * @throws SemanticException if any value is invalid.
	 *
	 */

	private void adaptDataSourceDesign(DataSourceDesign sourceDesign, OdaDataSourceHandle sourceHandle)
			throws SemanticException {

		Object value;

		// properties on ReportElement, like name, displayNames, etc.

		value = sourceDesign.getName();
		PropertyValueValidationUtil.validateProperty(sourceHandle, OdaDataSourceHandle.NAME_PROP, value);
		sourceHandle.getElement().setName(sourceDesign.getName());

		// properties on ReportElement, like name, displayNames, etc.

		value = sourceDesign.getDisplayName();
		PropertyValueValidationUtil.validateProperty(sourceHandle, OdaDataSourceHandle.DISPLAY_NAME_PROP, value);
		sourceHandle.getElement().setProperty(OdaDataSourceHandle.DISPLAY_NAME_PROP, sourceDesign.getDisplayName());

		value = sourceDesign.getDisplayNameKey();
		PropertyValueValidationUtil.validateProperty(sourceHandle, OdaDataSourceHandle.DISPLAY_NAME_ID_PROP, value);
		sourceHandle.getElement().setProperty(OdaDataSourceHandle.DISPLAY_NAME_ID_PROP,
				sourceDesign.getDisplayNameKey());

		// properties such as comments, extends, etc are kept in
		// DataSourceHandle, not DataSourceDesign.

		// scripts of DataSource are kept in
		// DataSourceHandle, not DataSourceDesign.

		// set null or empty list if the return list is empty.

		value = newROMPrivateProperties(sourceDesign.getPrivateProperties());
		PropertyValueValidationUtil.validateProperty(sourceHandle, OdaDataSourceHandle.PRIVATE_DRIVER_PROPERTIES_PROP,
				value);
		sourceHandle.getElement().setProperty(OdaDataSourceHandle.PRIVATE_DRIVER_PROPERTIES_PROP, value);

		updateROMPublicProperties(sourceDesign.getPublicProperties(), sourceHandle);

		// udpate property bindings, report parameters and so on.

		// updateROMPropertyBindings( sourceDesign.getPublicProperties( ),
		// sourceHandle );

		updateROMMessageFile(sourceDesign, sourceHandle.getModuleHandle());
	}

}
