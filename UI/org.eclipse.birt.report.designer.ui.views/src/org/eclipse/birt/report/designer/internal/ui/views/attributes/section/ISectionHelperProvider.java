/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.attributes.section;

/**
 * IDialogHelperProvider
 */
public interface ISectionHelperProvider {

	/**
	 * Creates helper for given container and specific helper key
	 * 
	 * @param container
	 * @param helperKey
	 * @return
	 */
	ISectionHelper createHelper(Object container, String helperKey);

}
