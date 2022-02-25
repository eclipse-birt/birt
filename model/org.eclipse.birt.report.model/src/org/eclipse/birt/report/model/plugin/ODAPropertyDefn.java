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

package org.eclipse.birt.report.model.plugin;

import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.metadata.IStructureDefn;
import org.eclipse.birt.report.model.metadata.ChoiceSet;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.MetaDataException;
import org.eclipse.birt.report.model.metadata.PropertyType;
import org.eclipse.birt.report.model.metadata.SystemPropertyDefn;
import org.eclipse.datatools.connectivity.oda.util.manifest.Property;
import org.eclipse.datatools.connectivity.oda.util.manifest.PropertyChoice;

/**
 */

public class ODAPropertyDefn extends SystemPropertyDefn {

	protected Property property = null;

	protected String displayName;

	ODAPropertyDefn(Property property) {
		assert property != null;

		this.property = property;

		name = property.getName();

		// Due to the limitation of ODA design interface, the display name
		// contains mnemonic key hint "&".
		// Strips out the '&' character.
		displayName = property.getDisplayName();
		if (displayName != null && displayName.indexOf('&') != -1) {
			displayName = property.getDisplayName().replace("&", "");
		}

		type = MetaDataDictionary.getInstance().getPropertyType(property.getType());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.metadata.IPropertyDefn#isList()
	 */

	@Override
	public boolean isList() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.metadata.IPropertyDefn#getName()
	 */
	@Override
	public String getName() {
		return property.getName();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.metadata.IPropertyDefn#getDisplayName()
	 */

	@Override
	public String getDisplayName() {
		return displayName;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.metadata.IPropertyDefn#getDisplayNameID ()
	 */

	@Override
	public String getDisplayNameID() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.metadata.IPropertyDefn#getChoices()
	 */

	@Override
	public IChoiceSet getChoices() {
		PropertyChoice[] propertyChoices = property.getChoices();

		ChoiceSet choiceSet = null;
		if (propertyChoices != null && propertyChoices.length > 0) {
			choiceSet = new ChoiceSet();
			IChoice[] choices = new ODAChoice[propertyChoices.length];
			for (int i = 0; i < propertyChoices.length; i++) {
				choices[i] = new ODAChoice(propertyChoices[i]);

			}
			choiceSet.setChoices(choices);
		}

		return choiceSet;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.metadata.IPropertyDefn#hasChoices()
	 */
	@Override
	public boolean hasChoices() {
		return property.getChoices() != null && property.getChoices().length > 0;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.metadata.IPropertyDefn#getStructDefn()
	 */

	@Override
	public IStructureDefn getStructDefn() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.metadata.IPropertyDefn#getDefault()
	 */

	@Override
	public Object getDefault() {
		return property.getDefaultValue();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.metadata.IPropertyDefn#getTargetElementType
	 * ()
	 */
	@Override
	public IElementDefn getTargetElementType() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.metadata.IPropertyDefn#getAllowedChoices ()
	 */

	@Override
	public IChoiceSet getAllowedChoices() {
		return getChoices();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.metadata.IPropertyDefn#isEncryptable()
	 */
	@Override
	public boolean isEncryptable() {
		return property.isEncryptable();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.metadata.PropertyDefn#getValueType()
	 */

	@Override
	public int getValueType() {
		return ODA_PROPERTY;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.metadata.ElementPropertyDefn#build()
	 */

	@Override
	public void build() throws MetaDataException {
		PropertyType tmpType = MetaDataDictionary.getInstance().getPropertyType(property.getType());
		if (tmpType == null) {
			return;
		}

		if (tmpType.getTypeCode() == IPropertyType.STRING_TYPE && !property.allowsEmptyValueAsNull()) {
			setType(MetaDataDictionary.getInstance().getPropertyType(IPropertyType.LITERAL_STRING_TYPE));
		}

		// to set the correct trim option, the super build must be done afterwards.

		super.build();
	}
}
