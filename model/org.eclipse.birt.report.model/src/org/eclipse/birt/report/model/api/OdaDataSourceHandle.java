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

package org.eclipse.birt.report.model.api;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.OdaDesignerState;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.OdaDataSource;
import org.eclipse.birt.report.model.elements.interfaces.IOdaDataSetModel;
import org.eclipse.birt.report.model.elements.interfaces.IOdaDataSourceModel;
import org.eclipse.birt.report.model.elements.interfaces.IOdaExtendableElementModel;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.util.CommandLabelFactory;

/**
 * Represents a extended data source.
 * 
 * @see org.eclipse.birt.report.model.elements.OdaDataSource
 */

public class OdaDataSourceHandle extends DataSourceHandle implements IOdaDataSourceModel, IOdaExtendableElementModel {

	/**
	 * Constructs an extended data source handle with the given design and the
	 * element. The application generally does not create handles directly. Instead,
	 * it uses one of the navigation methods available on other element handles.
	 * 
	 * @param module  the module
	 * @param element the model representation of the element
	 */

	public OdaDataSourceHandle(Module module, DesignElement element) {
		super(module, element);
	}

	/**
	 * Returns the extension name defined by the extended item.
	 * 
	 * @return the extension name as a string
	 * @deprecated use <code>getExtensioID()</code>
	 */

	public String getExtensionName() {
		return null;
	}

	/**
	 * Returns ID of the extension which extends this ODA data source.
	 * 
	 * @return the extension ID
	 */

	public String getExtensionID() {
		return getStringProperty(IOdaExtendableElementModel.EXTENSION_ID_PROP);
	}

	/**
	 * Sets the driver name.
	 * 
	 * @param driverName the name to set
	 * @throws SemanticException if this property is locked.
	 * @deprecated This property is removed.
	 */

	public void setDriverName(String driverName) throws SemanticException {
	}

	/**
	 * Returns the driver name.
	 * 
	 * @return the driver name
	 * @deprecated This property is removed.
	 */

	public String getDriverName() {
		return null;
	}

	/**
	 * Returns the iterator for the private driver property list. The item over the
	 * iterator is the instance of <code>ExtendedPropertyHandle</code>.
	 * 
	 * @return the iterator over private driver property list defined on this data
	 *         source.
	 * 
	 * @see org.eclipse.birt.report.model.api.elements.structures.ExtendedProperty
	 */

	public Iterator privateDriverPropertiesIterator() {
		PropertyHandle propertyHandle = getPropertyHandle(IOdaDataSourceModel.PRIVATE_DRIVER_PROPERTIES_PROP);
		assert propertyHandle != null;

		return propertyHandle.iterator();
	}

	/**
	 * Returns a private driver property value with the given property name.
	 * 
	 * @param name the name of a public driver property
	 * 
	 * @return a public driver property value
	 */

	public String getPrivateDriverProperty(String name) {
		return ExtendedPropertyHelper.getExtendedProperty(this, IOdaDataSourceModel.PRIVATE_DRIVER_PROPERTIES_PROP,
				name);
	}

	/**
	 * Sets a private driver property value with the given name and value. If the
	 * property does not exist, it will be added into the property list. If the
	 * property already exists, the value will be overwritten.
	 * 
	 * @param name  the name of a public driver property
	 * @param value the value of a public driver property
	 * 
	 * @throws SemanticException if <code>name</code> is <code>null</code> or an
	 *                           empty string after trimming.
	 */

	public void setPrivateDriverProperty(String name, String value) throws SemanticException {
		ExtendedPropertyHelper.setExtendedProperty(this, IOdaDataSourceModel.PRIVATE_DRIVER_PROPERTIES_PROP, name,
				value);
	}

	/**
	 * Returns the element definition of the element this handle represents.
	 * 
	 * @return the element definition of the element this handle represents.
	 */

	public IElementDefn getDefn() {
		ElementDefn extDefn = ((OdaDataSource) getElement()).getExtDefn();
		if (extDefn != null)
			return extDefn;

		return super.getDefn();
	}

	/**
	 * Returns the list of extension property definition. All these properties are
	 * just those defined in extension plugin.
	 * 
	 * @return the list of extension property definition.
	 */

	public List getExtensionPropertyDefinitionList() {
		if (((OdaDataSource) getElement()).getExtDefn() != null)

			return ((OdaDataSource) getElement()).getExtDefn().getLocalProperties();

		return Collections.EMPTY_LIST;

	}

	/**
	 * Returns the version of designer state.
	 * 
	 * @return the version of designer state.
	 */

	public String getDesigerStateVersion() {
		Object value = getProperty(DESIGNER_STATE_PROP);
		if (value == null)
			return null;

		assert value instanceof OdaDesignerState;

		return ((OdaDesignerState) value).getVersion();
	}

	/**
	 * Returns the version of designer state.
	 * 
	 * @param version the version of designer state.
	 * @throws SemanticException if designer state property is locked.
	 */

	public void setDesigerStateVersion(String version) throws SemanticException {
		setDesignerStateMemberValue(OdaDesignerState.VERSION_MEMBER, version);
	}

	/**
	 * Returns the content of designer state as the string.
	 * 
	 * @return the content of designer state as the string.
	 */

	public String getDesigerStateContentAsString() {
		Object value = getProperty(DESIGNER_STATE_PROP);
		if (value == null)
			return null;

		assert value instanceof OdaDesignerState;

		return ((OdaDesignerState) value).getContentAsString();
	}

	/**
	 * Returns the content of designer state as the string.
	 * 
	 * @param content the content of designer state as the string.
	 * 
	 * @throws SemanticException if designer state property is locked.
	 */

	public void setDesigerStateContentAsString(String content) throws SemanticException {
		setDesignerStateMemberValue(OdaDesignerState.CONTENT_AS_STRING_MEMBER, content);
	}

	/**
	 * Returns the content of designer state as the byte array.
	 * 
	 * @return the content of designer state as the byte array.
	 */

	public byte[] getDesigerStateContentAsBlob() {
		Object value = getProperty(DESIGNER_STATE_PROP);
		if (value == null)
			return null;

		assert value instanceof OdaDesignerState;

		return ((OdaDesignerState) value).getContentAsBlob();
	}

	/**
	 * Returns the content of designer state as the byte.
	 * 
	 * @param content the content of designer state as the byte.
	 * 
	 * @throws SemanticException if designer state property is locked.
	 */

	public void setDesigerStateContentAsBlob(byte[] content) throws SemanticException {
		setDesignerStateMemberValue(OdaDesignerState.CONTENT_AS_BLOB_MEMBER, content);
	}

	/**
	 * Sets the version of designer state.
	 * 
	 * @param version the version of designer state.
	 */

	private void setDesignerStateMemberValue(String memberName, Object memberValue) throws SemanticException {
		Object value = element.getLocalProperty(module, DESIGNER_STATE_PROP);

		if (value == null) {
			OdaDesignerState designerState = new OdaDesignerState();
			if (OdaDesignerState.VERSION_MEMBER.equalsIgnoreCase(memberName))
				designerState.setVersion((String) memberValue);
			else if (OdaDesignerState.CONTENT_AS_STRING_MEMBER.equalsIgnoreCase(memberName))
				designerState.setContentAsString((String) memberValue);
			else if (OdaDesignerState.CONTENT_AS_BLOB_MEMBER.equalsIgnoreCase(memberName))
				designerState.setContentAsBlob((byte[]) memberValue);

			setProperty(DESIGNER_STATE_PROP, designerState);
		} else {
			PropertyHandle propHandle = getPropertyHandle(DESIGNER_STATE_PROP);
			OdaDesignerState designerState = (OdaDesignerState) value;
			OdaDesignerStateHandle stateHandle = (OdaDesignerStateHandle) designerState.getHandle(propHandle);

			if (OdaDesignerState.VERSION_MEMBER.equalsIgnoreCase(memberName))
				stateHandle.setVersion((String) memberValue);
			else if (OdaDesignerState.CONTENT_AS_STRING_MEMBER.equalsIgnoreCase(memberName)) {
				getModuleHandle().getCommandStack()
						.startTrans(CommandLabelFactory.getCommandLabel(MessageConstants.CHANGE_ITEM_MESSAGE));
				stateHandle.setContentAsString((String) memberValue);
				stateHandle.setContentAsBlob(null);
				getModuleHandle().getCommandStack().commit();
			} else if (OdaDesignerState.CONTENT_AS_BLOB_MEMBER.equalsIgnoreCase(memberName)) {
				getModuleHandle().getCommandStack()
						.startTrans(CommandLabelFactory.getCommandLabel(MessageConstants.CHANGE_ITEM_MESSAGE));
				stateHandle.setContentAsString(null);
				stateHandle.setContentAsBlob((byte[]) memberValue);
				getModuleHandle().getCommandStack().commit();
			}
		}
	}

	/**
	 * Returns the oda designer state.
	 * 
	 * @return a handle to the designer state property, return <code>null</code> if
	 *         the designer state.
	 * @see OdaDesignerStateHandle
	 */

	public OdaDesignerStateHandle getDesignerState() {
		PropertyHandle propHandle = getPropertyHandle(IOdaDataSetModel.DESIGNER_STATE_PROP);

		OdaDesignerState designerState = (OdaDesignerState) propHandle.getValue();

		if (designerState == null)
			return null;

		return (OdaDesignerStateHandle) designerState.getHandle(propHandle);
	}

	/**
	 * Sets the designer state for a oda data source.
	 * 
	 * @param designerState new designer state
	 * @return a handle to the designer state
	 * 
	 * @throws SemanticException if member of the designer state is not valid.
	 */

	public OdaDesignerStateHandle setDesignerState(OdaDesignerState designerState) throws SemanticException {
		setProperty(IOdaDataSourceModel.DESIGNER_STATE_PROP, designerState);

		if (designerState == null)
			return null;
		return (OdaDesignerStateHandle) designerState
				.getHandle(getPropertyHandle(IOdaDataSourceModel.DESIGNER_STATE_PROP));
	}

	public String getExternalConnectionName() {
		String result = getElement().getStringProperty(module, IOdaDataSourceModel.EXTERNAL_CONNECTION_NAME);
		if (result != null) {
			return result;
		}
		return getName();
	}

	public void setExternalConnectionName(String externalConnectionName) throws SemanticException {
		setProperty(IOdaDataSourceModel.EXTERNAL_CONNECTION_NAME, externalConnectionName);
	}
}