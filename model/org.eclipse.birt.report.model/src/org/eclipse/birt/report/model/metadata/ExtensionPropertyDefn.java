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

import org.eclipse.birt.report.model.api.extension.IMessages;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.i18n.ThreadResources;

/**
 * Represents the definition of extension property.
 */

public class ExtensionPropertyDefn extends SystemPropertyDefn {

	/**
	 * <code>ture</code> if the xml property value represents the extesion-defined
	 * model. Otherwise <code>false</code>.
	 */

	protected boolean hasOwnModel;

	protected IMessages messages = null;

	protected String groupName = null;

	/**
	 * The default display name, which is used when the localized string is not
	 * found with I18N feature.
	 */

	protected String defaultDisplayName = null;

	/**
	 * The default display name for property group, which is used when the localized
	 * string is not found with I18N feature.
	 */

	protected String groupDefauleDisplayName = null;

	/**
	 * Sets the group name of this property definition.
	 * 
	 * @param groupName the group name to set
	 */

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	/**
	 * Constructs the property definition with <code>IMessages</code> for extension
	 * property.
	 * 
	 * @param messages the messages which can return localized message for resource
	 *                 key and locale
	 */

	public ExtensionPropertyDefn(IMessages messages) {
		this.messages = messages;
	}

	/*
	 * Returns the localized group name, if non-empty string can be found with
	 * resource key and <code> IMessages </code> . Otherwise, return <code> null
	 * </code> .
	 * 
	 * @see
	 * org.eclipse.birt.report.model.metadata.ElementPropertyDefn#getGroupName()
	 */

	public String getGroupName() {
		if (groupNameKey != null) {
			if (messages != null) {
				String displayName = messages.getMessage(groupNameKey, ThreadResources.getLocale());
				if (!StringUtil.isBlank(displayName))
					return displayName;
			}
		}

		if (groupDefauleDisplayName != null)
			return groupDefauleDisplayName;

		return groupName;
	}

	/*
	 * Returns the localized display name, if non-empty string can be found with
	 * resource key and <code> IMessages </code> . Otherwise, return name of this
	 * property definition.
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyDefn#getDisplayName()
	 */
	public String getDisplayName() {
		if (displayNameID != null && messages != null) {
			String displayName = messages.getMessage(displayNameID, ThreadResources.getLocale());
			if (!StringUtil.isBlank(displayName))
				return displayName;
		}

		if (defaultDisplayName != null)
			return defaultDisplayName;

		return getName();
	}

	/**
	 * Sets the default display name.
	 * 
	 * @param defaultDisplayName the default display name to set
	 */

	public void setDefaultDisplayName(String defaultDisplayName) {
		this.defaultDisplayName = defaultDisplayName;
	}

	/**
	 * Sets the default display name for property group
	 * 
	 * @param groupDefauleDisplayName the default display name for property group to
	 *                                set
	 */

	public void setGroupDefauleDisplayName(String groupDefauleDisplayName) {
		this.groupDefauleDisplayName = groupDefauleDisplayName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyDefn#getValueType()
	 */
	public int getValueType() {
		return EXTENSION_PROPERTY;
	}

	/**
	 * Sets the flag indicating if the xml property value represents the
	 * extesion-defined model.
	 * 
	 * @param hasOwnModel <code>true</code> if the xml property value represents the
	 *                    extesion-defined model.
	 */

	void setHasOwnModel(boolean hasOwnModel) {
		this.hasOwnModel = hasOwnModel;
	}

	/**
	 * Returns <code>true</code> indicating if the xml property value represents the
	 * extesion-defined model.
	 * 
	 * @return <code>true</code> if the xml property value represents the
	 *         extesion-defined model.
	 */

	public boolean hasOwnModel() {
		return hasOwnModel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.ElementPropertyDefn#build()
	 */

	public void build() throws MetaDataException {
		super.buildDefn();

		// add extension validator on extension xml property

		// if ( getValueType( ) == EXTENSION_PROPERTY
		// && getTypeCode( ) == IPropertyType.XML_TYPE && hasOwnModel )
		// {
		// SemanticTriggerDefnSet tmpTriggerSet = getTriggerDefnSet( );
		// String tmpTriggerDefnName = ExtensionValidator.NAME;
		// if ( !tmpTriggerSet.contain( tmpTriggerDefnName ) )
		// {
		// SemanticTriggerDefn triggerDefn = new SemanticTriggerDefn(
		// tmpTriggerDefnName );
		// triggerDefn.setPropertyName( getName( ) );
		// triggerDefn.setValidator( ExtensionValidator.getInstance( ) );
		//
		// tmpTriggerSet.add( triggerDefn );
		// }
		// }

		super.buildTriggerDefnSet();
	}
}
