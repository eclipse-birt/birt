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
import org.eclipse.birt.report.model.elements.OdaDataSet;
import org.eclipse.birt.report.model.elements.interfaces.IOdaDataSetModel;
import org.eclipse.birt.report.model.elements.interfaces.IOdaExtendableElementModel;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.util.CommandLabelFactory;

/**
 * Represents an extended data set.
 * 
 * @see org.eclipse.birt.report.model.elements.OdaDataSet
 */

public class OdaDataSetHandle extends DataSetHandle implements IOdaDataSetModel, IOdaExtendableElementModel {

	/**
	 * Constructs a handle for extended data set report item. The application
	 * generally does not create handles directly. Instead, it uses one of the
	 * navigation methods available on other element handles.
	 * 
	 * @param module  the module
	 * @param element the model representation of the element
	 */

	public OdaDataSetHandle(Module module, DesignElement element) {
		super(module, element);
	}

	/**
	 * Returns the private driver design state.
	 * 
	 * @return the private driver design state
	 * @deprecated
	 */

	public String getPrivateDriverDesignState() {
		return null;
	}

	/**
	 * Returns the script for query.
	 * 
	 * @deprecated to be removed.
	 * @return the script for query .
	 */

	public String getQueryScript() {
		return null;
	}

	/**
	 * Returns the query text.
	 * 
	 * @return the query text.
	 */

	public String getQueryText() {
		return getStringProperty(IOdaDataSetModel.QUERY_TEXT_PROP);
	}

	/**
	 * Returns the data set type.
	 * 
	 * @deprecated type has been replaced by extension ID
	 * @return the data set type
	 */

	public String getType() {
		return null;
	}

	/**
	 * Returns the result set name.
	 * 
	 * @return the result set name
	 */

	public String getResultSetName() {
		return getStringProperty(IOdaDataSetModel.RESULT_SET_NAME_PROP);
	}

	/**
	 * Sets the private driver design state.
	 * 
	 * @param state the design state to set
	 * @throws SemanticException if this property is locked.
	 * @deprecated
	 */

	public void setPrivateDriverDesignState(String state) throws SemanticException {
	}

	/**
	 * Sets the query script.
	 * 
	 * @deprecated to be removed.
	 * 
	 * @param script the script to set
	 * @throws SemanticException if this property is locked.
	 * @deprecated
	 */

	public void setQueryScript(String script) throws SemanticException {
	}

	/**
	 * Sets the query text.
	 * 
	 * @param text the text to set
	 * @throws SemanticException if this property is locked.
	 */

	public void setQueryText(String text) throws SemanticException {
		setStringProperty(IOdaDataSetModel.QUERY_TEXT_PROP, text);
	}

	/**
	 * Sets the type.
	 * 
	 * @deprecated type has been replaced by extension ID
	 * @param type the type to set
	 * @throws SemanticException if this property is locked.
	 */

	public void setType(String type) throws SemanticException {
	}

	/**
	 * Sets the result set name.
	 * 
	 * @param name the name to set
	 * @throws SemanticException if this property is locked.
	 */

	public void setResultSetName(String name) throws SemanticException {
		setStringProperty(IOdaDataSetModel.RESULT_SET_NAME_PROP, name);
	}

	/**
	 * Returns the extension name defined by the extended item.
	 * 
	 * @return the extension name as a string
	 * @deprecated use <code>getExtensionID()</code>
	 */

	public String getExtensionName() {
		return null;
	}

	/**
	 * Returns ID of the extension which extends this ODA data set.
	 * 
	 * @return the extension ID
	 */

	public String getExtensionID() {
		return getStringProperty(EXTENSION_ID_PROP);
	}

	/**
	 * Returns the iterator for the private driver property list. The item over the
	 * iterator is the instance of <code>ExtendedPropertyHandle</code>.
	 * 
	 * @return the iterator over private driver property list defined on this data
	 *         set.
	 * 
	 * @see org.eclipse.birt.report.model.api.elements.structures.ExtendedProperty
	 */

	public Iterator privateDriverPropertiesIterator() {
		PropertyHandle propertyHandle = getPropertyHandle(IOdaDataSetModel.PRIVATE_DRIVER_PROPERTIES_PROP);
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
		return ExtendedPropertyHelper.getExtendedProperty(this, IOdaDataSetModel.PRIVATE_DRIVER_PROPERTIES_PROP, name);
	}

	/**
	 * Sets a private driver property value with the given name and value. If the
	 * property does not exist, it will be added into the property list. If the
	 * property already exists, the value of the property will be overwritten.
	 * 
	 * @param name  the name of a public driver property
	 * @param value the value of a public driver property
	 * 
	 * @throws SemanticException if <code>name</code> is <code>null</code> or an
	 *                           empty string after trimming.
	 */

	public void setPrivateDriverProperty(String name, String value) throws SemanticException {
		ExtendedPropertyHelper.setExtendedProperty(this, IOdaDataSetModel.PRIVATE_DRIVER_PROPERTIES_PROP, name, value);
	}

	/**
	 * Returns the element definition of the element this handle represents.
	 * 
	 * @return the element definition of the element this handle represents.
	 */

	public IElementDefn getDefn() {
		ElementDefn extDefn = ((OdaDataSet) getElement()).getExtDefn();
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
		if (((OdaDataSet) getElement()).getExtDefn() != null)

			return ((OdaDataSet) getElement()).getExtDefn().getLocalProperties();

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
	 * Sets the designer state for a oda data set.
	 * 
	 * @param designerState new designer state
	 * @return a handle to the designer state
	 * 
	 * @throws SemanticException if member of the designer state is not valid.
	 */

	public OdaDesignerStateHandle setDesignerState(OdaDesignerState designerState) throws SemanticException {
		setProperty(IOdaDataSetModel.DESIGNER_STATE_PROP, designerState);

		if (designerState == null)
			return null;
		return (OdaDesignerStateHandle) designerState
				.getHandle(getPropertyHandle(IOdaDataSetModel.DESIGNER_STATE_PROP));
	}

	/**
	 * Returns an iterator over the list of oda dataset parameter definitions. The
	 * iterator returns instances of <code>OdaDataSetParameterHandle</code> that
	 * represents oda dataset parameter objects.
	 * 
	 * @return iterator over oda dataset parameter definitions.
	 * @see org.eclipse.birt.report.model.api.elements.structures.
	 *      OdaDataSetParameter
	 */

	public Iterator parametersIterator() {
		PropertyHandle propHandle = getPropertyHandle(PARAMETERS_PROP);
		assert propHandle != null;
		return propHandle.iterator();
	}

	/**
	 * Returns persistent ODA designer values stored in the data set.
	 * 
	 * @return designer values in string
	 */

	public String getDesignerValues() {
		return getStringProperty(DESIGNER_VALUES_PROP);
	}

	/**
	 * Stores persistent ODA designer values in the data set.
	 * 
	 * @param values designer values in string
	 * @throws SemanticException if the property is locked.
	 */

	public void setDesignerValues(String values) throws SemanticException {
		setStringProperty(DESIGNER_VALUES_PROP, values);
	}

	/**
	 * Gets the result set number.
	 * 
	 * @return the result set number.
	 */
	public int getResultSetNumber() {
		return getIntProperty(IOdaDataSetModel.RESULT_SET_NUMBER_PROP);
	}

	/**
	 * Sets the result set number.
	 * 
	 * @param number the result set number.
	 * @throws SemanticException
	 */
	public void setResultSetNumber(int number) throws SemanticException {
		setIntProperty(IOdaDataSetModel.RESULT_SET_NUMBER_PROP, number);
	}
}