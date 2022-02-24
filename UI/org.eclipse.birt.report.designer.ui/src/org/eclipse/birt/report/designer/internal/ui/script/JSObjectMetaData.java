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
		 * @return
		 */
		String getName();

		/**
		 * Get method's display text.
		 * 
		 * @return
		 */
		String getDisplayText();

		/**
		 * Get method's description.
		 * 
		 * @return
		 */
		String getDescription();

		/**
		 * Get method's visibility.
		 * 
		 * @return
		 */
		int getVisibility();

		/**
		 * Get method's return type.
		 * 
		 * @return
		 */
		JSObjectMetaData getReturn();

		/**
		 * Gets the arguments info.
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
		 * @return
		 */
		String getName();

		/**
		 * Get field's display text.
		 * 
		 * @return
		 */
		String getDisplayText();

		/**
		 * Get field's description.
		 * 
		 * @return
		 */
		String getDescription();

		/**
		 * Get field's visibility.
		 * 
		 * @return
		 */
		int getVisibility();

		/**
		 * Get field's type.
		 * 
		 * @return
		 */
		JSObjectMetaData getType();
	}

	/**
	 * Get this object's name.
	 * 
	 * @return
	 */
	String getName();

	/**
	 * Get this object's methods.
	 * 
	 * @return
	 */
	JSMethod[] getMethods();

	/**
	 * Get this object's fields.
	 * 
	 * @return
	 */
	JSField[] getFields();

	/**
	 * Get description for this object.
	 * 
	 * @return
	 */
	String getDescription();

	/**
	 * Get this object's visibility.
	 * 
	 * @return
	 */
	int getVisibility();

	JSObjectMetaData getComponentType();
}
