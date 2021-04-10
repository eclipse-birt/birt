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

package org.eclipse.birt.report.model.api.metadata;

/**
 * Interface of the Meta-data information for an element property or an
 * extension model property. Includes the type, possible choices, display name,
 * etc derived from the {@link IPropertyDefn}. It also includes the group name,
 * method information, is visible to the GUI users and can be inherited, etc.
 */

public interface IElementPropertyDefn extends IPropertyDefn {

	/**
	 * Returns the group name (if any) for the property.
	 * 
	 * @return The (localized) group name, or null if the property is not in a
	 *         group.
	 */

	public String getGroupName();

	/**
	 * Returns the message ID for the group name.
	 * 
	 * @return The group name message ID.
	 */

	public String getGroupNameKey();

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

	public boolean canInherit();

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

	public boolean isStyleProperty();

	/**
	 * Checks whether the property is visible to the property sheet.
	 * 
	 * @return <code>true</code> if the property value is visible,
	 *         <code>false</code> otherwise.
	 * 
	 * @deprecated by the method {@link IElementDefn#isPropertyVisible(String)}
	 */

	public boolean isVisible();

	/**
	 * Checks whether the property value is read-only in the property sheet.
	 * 
	 * @return <code>true</code> if the property value is read-only,
	 *         <code>false</code> otherwise.
	 * 
	 * @deprecated by the method {@link IElementDefn#isPropertyReadOnly(String)}
	 */

	public boolean isReadOnly();

	/**
	 * Returns the method information of this property.
	 * 
	 * @return the method information of this property. Return null, if this
	 *         property is not a method property.
	 */

	public IMethodInfo getMethodInfo();

	/**
	 * Checks whether the property value can be edited by the user in the property
	 * sheet.
	 * 
	 * @return <code>true</code> if the property value is read-only,
	 *         <code>false</code> otherwise.
	 */

	public boolean isEditable();
}