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

package org.eclipse.birt.report.designer.internal.ui.script;

/**
 * A js object's meta-data descriptior.
 */

public interface JSObjectMetaData {

	/**
	 *
	 */
	int VISIBILITY_PUBLIC = 0;
	/**
	 *
	 */
	int VISIBILITY_PROTECTED = 1;
	/**
	 *
	 */
	int VISIBILITY_PRIVATE = 2;
	/**
	 *
	 */
	int VISIBILITY_STATIC = 3;

	/**
	 * JSMethod
	 */
	interface JSMethod {

		/**
		 * Get method's name.
		 *
		 * @return the method name
		 */
		String getName();

		/**
		 * Get method's display text.
		 *
		 * @return the method display text
		 */
		String getDisplayText();

		/**
		 * Get method's description.
		 *
		 * @return the method description
		 */
		String getDescription();

		/**
		 * Get method's visibility.
		 *
		 * @return the method visibility
		 */
		int getVisibility();

		/**
		 * Get method's return type.
		 *
		 * @return the method return type
		 */
		JSObjectMetaData getReturn();

		/**
		 * Gets the arguments info.
		 *
		 * @return the argument info
		 */
		JSObjectMetaData[] getArguments();
	}

	/**
	 * JSField
	 */
	interface JSField {

		/**
		 * Get field's name.
		 *
		 * @return the field name
		 */
		String getName();

		/**
		 * Get field's display text.
		 *
		 * @return the field display text
		 */
		String getDisplayText();

		/**
		 * Get field's description.
		 *
		 * @return the field description
		 */
		String getDescription();

		/**
		 * Get field's visibility.
		 *
		 * @return the field visibility
		 */
		int getVisibility();

		/**
		 * Get field's type.
		 *
		 * @return the field type
		 */
		JSObjectMetaData getType();
	}

	/**
	 * Get this object's name.
	 *
	 * @return the object name
	 */
	String getName();

	/**
	 * Get this object's methods.
	 *
	 * @return the object method
	 */
	JSMethod[] getMethods();

	/**
	 * Get this object's fields.
	 *
	 * @return the object fields
	 */
	JSField[] getFields();

	/**
	 * Get description for this object.
	 *
	 * @return the objet description
	 */
	String getDescription();

	/**
	 * Get this object's visibility.
	 *
	 * @return the object visibility
	 */
	int getVisibility();

	/**
	 * Get the object's component type
	 *
	 * @return the object's component type
	 */
	JSObjectMetaData getComponentType();
}
