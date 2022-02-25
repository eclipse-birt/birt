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

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.birt.report.model.api.extension.IChoiceDefinition;
import org.eclipse.birt.report.model.api.extension.IMessages;
import org.eclipse.birt.report.model.api.extension.IPropertyDefinition;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IMethodInfo;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.metadata.IStructureDefn;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.i18n.ThreadResources;

/**
 * Represents the extension model property definition. It is used for only those
 * property defined by BIRT extension element. The instance of this class is
 * read-only, which means that all the "set" methods is forbidden to call.
 */

public class ExtensionModelPropertyDefn extends ElementPropertyDefn {

	/**
	 * The effective extension property definition.
	 */

	private IPropertyDefinition extProperty = null;

	/**
	 * The messages providing the localized message.
	 */

	private IMessages messages = null;

	/**
	 * Constructs the extension element property definition with an effective
	 * extension property.
	 *
	 * @param extPropertyDefn the effective extension property definition
	 * @param messages        the messages providing the localized messages
	 */

	public ExtensionModelPropertyDefn(IPropertyDefinition extPropertyDefn, IMessages messages) {
		extProperty = extPropertyDefn;
		assert extProperty != null;
		MetaDataDictionary dd = MetaDataDictionary.getInstance();
		type = dd.getPropertyType(extProperty.getType());
		name = extProperty.getName();
		defaultValue = extProperty.getDefaultValue();
		this.messages = messages;
		assert type != null;
		buildTrimOption(type.getTypeCode());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.metadata.PropertyDefn#getValueType()
	 */

	@Override
	public int getValueType() {
		return EXTENSION_MODEL_PROPERTY;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.metadata.ElementPropertyDefn#canInherit()
	 */

	@Override
	public boolean canInherit() {
		return false;
	}

	/*
	 * Returns the localized group name, if non-empty string can be found with
	 * resource key and <code> IMessages </code> . Otherwise, return <code> null
	 * </code> .
	 *
	 * @see
	 * org.eclipse.birt.report.model.metadata.ElementPropertyDefn#getGroupName()
	 */

	@Override
	public String getGroupName() {
		if (extProperty.getGroupNameID() != null && messages != null) {
			return messages.getMessage(extProperty.getGroupNameID(), ThreadResources.getLocale());
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.metadata.ElementPropertyDefn#getGroupNameKey ()
	 */

	@Override
	public String getGroupNameKey() {
		return extProperty.getGroupNameID();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.metadata.ElementPropertyDefn#getMethodInfo
	 * ()
	 */

	@Override
	public IMethodInfo getMethodInfo() {
		if (type.getTypeCode() == IPropertyType.SCRIPT_TYPE) {
			return extProperty.getMethodInfo();
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.metadata.ElementPropertyDefn#isStyleProperty ()
	 */

	@Override
	public boolean isStyleProperty() {
		// TODO: if the extension elements can define their own style properties
		// then the interface IPropertyDefinition should handle this?

		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.metadata.ElementPropertyDefn#setCanInherit
	 * (boolean)
	 */

	@Override
	void setCanInherit(boolean flag) {
		assert false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.metadata.ElementPropertyDefn#setGroupNameKey
	 * (java.lang.String)
	 */

	@Override
	void setGroupNameKey(String id) {
		assert false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.metadata.PropertyDefn#getAllowedChoices()
	 */

	@Override
	public IChoiceSet getAllowedChoices() {
		return getChoices();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.metadata.PropertyDefn#getChoices()
	 */

	@Override
	public IChoiceSet getChoices() {
		Collection<IChoiceDefinition> choices = extProperty.getChoices();
		if ((choices == null) || (choices.size() == 0)) {
			return null;
		}
		Choice[] choiceArray = new Choice[choices.size()];
		Iterator<IChoiceDefinition> iter = choices.iterator();
		int i = 0;
		while (iter.hasNext()) {
			IChoiceDefinition choice = iter.next();
			ExtensionChoice extChoice = new ExtensionChoice(choice, messages);
			choiceArray[i] = extChoice;
			i++;
		}
		assert i == choices.size();

		ChoiceSet set = new ChoiceSet();
		set.setChoices(choiceArray);
		details = set;
		return set;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.metadata.PropertyDefn#getDefault()
	 */

	@Override
	public Object getDefault() {
		return extProperty.getDefaultValue();
	}

	/*
	 * Returns the localized display name, if non-empty string can be found with
	 * resource key and <code> IMessages </code> . Otherwise, return name of this
	 * property.
	 *
	 * @see org.eclipse.birt.report.model.metadata.PropertyDefn#getDisplayName()
	 */

	@Override
	public String getDisplayName() {
		if (extProperty.getDisplayNameID() != null && messages != null) {
			String displayName = messages.getMessage(extProperty.getDisplayNameID(), ThreadResources.getLocale());
			if (!StringUtil.isBlank(displayName)) {
				return displayName;
			}
		}

		return extProperty.getName();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.metadata.PropertyDefn#getDisplayNameID()
	 */

	@Override
	public String getDisplayNameID() {
		return extProperty.getDisplayNameID();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.metadata.PropertyDefn#getName()
	 */

	@Override
	public String getName() {
		return extProperty.getName();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.metadata.PropertyDefn#getStructDefn()
	 */

	@Override
	public IStructureDefn getStructDefn() {
		if (details instanceof StructureDefn) {
			return (StructureDefn) details;
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.metadata.PropertyDefn#getTargetElementType
	 * ()
	 */

	@Override
	public IElementDefn getTargetElementType() {
		// till now, the extension elements do not support
		// element reference type?
		// TODO: if support element reference type, we should handle

		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.metadata.PropertyDefn#getType()
	 */

	@Override
	public PropertyType getType() {
		int typeCode = extProperty.getType();
		MetaDataDictionary dd = MetaDataDictionary.getInstance();
		return dd.getPropertyType(typeCode);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.metadata.PropertyDefn#getTypeCode()
	 */

	@Override
	public int getTypeCode() {
		PropertyType type = getType();
		assert type != null;
		return type.getTypeCode();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.metadata.PropertyDefn#isIntrinsic()
	 */

	@Override
	public boolean isIntrinsic() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.metadata.PropertyDefn#isList()
	 */

	@Override
	public boolean isList() {
		return extProperty.isList();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.metadata.PropertyDefn#setAllowedChoices
	 * (org.eclipse.birt.report.model.metadata.ChoiceSet)
	 */

	@Override
	void setAllowedChoices(ChoiceSet allowedChoices) {
		assert false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.metadata.PropertyDefn#setDefault(java.lang
	 * .Object)
	 */

	@Override
	protected void setDefault(Object value) {
		assert false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.metadata.PropertyDefn#setDisplayNameID(
	 * java.lang.String)
	 */

	@Override
	public void setDisplayNameID(String id) {
		assert false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.metadata.PropertyDefn#setIntrinsic(boolean)
	 */

	@Override
	void setIntrinsic(boolean flag) {
		assert false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.metadata.PropertyDefn#setIsList(boolean)
	 */

	@Override
	protected void setIsList(boolean isList) {
		assert false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.metadata.PropertyDefn#setName(java.lang
	 * .String)
	 */

	@Override
	public void setName(String theName) {
		assert false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.metadata.PropertyDefn#setType(org.eclipse
	 * .birt.report.model.metadata.PropertyType)
	 */

	@Override
	public void setType(PropertyType typeDefn) {
		assert false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.metadata.IPropertyDefn#isReadOnly()
	 */

	@Override
	public boolean isReadOnly() {
		return extProperty.isReadOnly();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.metadata.IPropertyDefn#isVisible()
	 */

	@Override
	public boolean isVisible() {
		return extProperty.isVisible();
	}

}
