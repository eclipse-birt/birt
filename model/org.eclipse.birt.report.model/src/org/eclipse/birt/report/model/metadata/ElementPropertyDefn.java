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

import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IMethodInfo;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.i18n.ModelMessages;

/**
 * Meta-data information for a property. Includes the type, possible choices,
 * display name, etc.
 */

public abstract class ElementPropertyDefn extends PropertyDefn implements IElementPropertyDefn {

	/**
	 * The message ID for the property group name.
	 */

	protected String groupNameKey = null;

	/**
	 * <code>true</code> if the value of the property can be inherited,
	 * <code>false</code> if not.
	 */

	protected boolean isInheritable = true;

	/**
	 * The proposal is to extend the extension schema to call context related
	 * property search algorithm.
	 */

	protected boolean useOwnSearch = false;

	/**
	 * Default constructor.
	 */

	public ElementPropertyDefn() {
	}

	/**
	 * Returns the group name (if any) for the property.
	 *
	 * @return The (localized) group name, or null if the property is not in a
	 *         group.
	 */

	@Override
	public String getGroupName() {
		if (groupNameKey == null) {
			return null;
		}

		return ModelMessages.getMessage(groupNameKey);
	}

	/**
	 * Indicates if derived elements can inherit the value of this property. Most
	 * properties can inherit values from a parent element. A few system properties,
	 * such as the name or extends, can't be inherited.
	 * <p>
	 * Note: This attribute is used for both style property and non-style property
	 * with two different meanings.
	 * <ul>
	 * <li>For style property, it determines whether this property can be cascaded
	 * from container;
	 * <li>For non-style property, that means whether this property can be inherited
	 * from parent.
	 * </ul>
	 * We take it because of the fact style is not inheritable, and the following
	 * diagram:
	 * <p>
	 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
	 * collapse" bordercolor="#111111">
	 * <th width="20%"></th>
	 * <th width="40%">can inherit</th>
	 * <th width="40%">can cascade</th>
	 * <tr>
	 * <td>Style property</td>
	 * <td align="center"><code>false</code></td>
	 * <td align="center"><code>isInheritable</code></td>
	 * </tr>
	 * <tr>
	 * <td>Non-style property</td>
	 * <td align="center"><code>isInheritable</code></td>
	 * <td align="center"><code>false</code></td>
	 * </tr>
	 * </table>
	 *
	 * @return Whether the property can inherit the parent's value.
	 */

	@Override
	public boolean canInherit() {
		return isInheritable;
	}

	/**
	 * Indicates whether the property is associated with a style or not. If this
	 * property is defined on a style, then isStyleProperty says whether the
	 * property can be "associated" with elements that have a style. If so, then
	 * each element with a style will include the style property as though that
	 * property were defined on the element itself. The meta-data for the style
	 * property is copied onto the meta-data for the element. In this case, the copy
	 * will also return true from isStyleProperty( ), indicating that the element
	 * obtained the property implicitly from the style element.
	 *
	 * @return Whether the property is defined on a style for the purpose of being
	 *         used by elements.
	 */

	@Override
	public boolean isStyleProperty() {
		return false;
	}

	/**
	 * Builds the semantic information for this property. Called once while loading
	 * the meta-data. The build must succeed, or a programming error has occurred.
	 *
	 * @throws MetaDataException if the build is failed
	 */

	@Override
	public void build() throws MetaDataException {
		super.build();

		// Check consistency of options. A style property that is meant
		// to be used by multiple elements cannot also be intrinsic:
		// defined by a member variable.

		if (isIntrinsic() && isStyleProperty()) {
			throw new MetaDataException(new String[] { name },
					MetaDataException.DESIGN_EXCEPTION_INCONSISTENT_PROP_TYPE);
		}

		// all element type property is not inheritable
		if (getTypeCode() == IPropertyType.ELEMENT_TYPE) {
			this.isInheritable = false;
		}

	}

	/**
	 * Returns the message ID for the group name.
	 *
	 * @return The group name message ID.
	 */

	@Override
	public String getGroupNameKey() {
		return groupNameKey;
	}

	/**
	 * Sets whether the property value can be inherited by subclasses.
	 *
	 * @param flag <code>true</code> if the property value can be inherited,
	 *             <code>false</code> otherwise.
	 */

	void setCanInherit(boolean flag) {
		isInheritable = flag;
	}

	/**
	 * Sets the message ID for the group name.
	 *
	 * @param id The group name message ID.
	 */

	void setGroupNameKey(String id) {
		groupNameKey = id;
	}

	/**
	 * Checks whether the property is visible to the property sheet.
	 *
	 * @return <code>true</code>.
	 */

	@Override
	public boolean isVisible() {
		return true;
	}

	/**
	 * Checks whether the property value is read-only in the property sheet.
	 *
	 * @return <code>false</code>.
	 */

	@Override
	public boolean isReadOnly() {
		return false;
	}

	/**
	 * Checks whether the property value can be edited by the user in the property
	 * sheet.
	 *
	 * @return <code>true</code> if the property value is read-only,
	 *         <code>false</code> otherwise.
	 */

	@Override
	public boolean isEditable() {
		return !(getTypeCode() == IPropertyType.CHOICE_TYPE);
	}

	/**
	 * Returns the method information of this property.
	 *
	 * @return the method information of this property. Return null, if this
	 *         property is not a method property.
	 */

	@Override
	public IMethodInfo getMethodInfo() {
		if (getTypeCode() == IPropertyType.SCRIPT_TYPE) {
			return (IMethodInfo) details;
		}

		return null;
	}

	/**
	 * Returns <code>true</code> indicating if the xml property value represents the
	 * extension-defined model.
	 *
	 * @return <code>true</code> if the xml property value represents the
	 *         extension-defined model.
	 */

	public boolean hasOwnModel() {
		return false;
	}

	/**
	 * @return the enableExtraSearch
	 */

	public boolean enableContextSearch() {
		return useOwnSearch;
	}
}
