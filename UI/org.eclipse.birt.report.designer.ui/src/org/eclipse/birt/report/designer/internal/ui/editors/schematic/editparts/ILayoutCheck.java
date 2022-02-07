/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts;

/**
 * Check the editor can layout correct.
 */
//Note this is internal interface, maybe add the method such as getLayouInfomation.The use can extend the AbstractLayoutCheck
//Now only check the row and column number is regular.
public interface ILayoutCheck {
	/**
	 * Check the layout infomation
	 * 
	 * @param model
	 * @return
	 */
	boolean layoutCheck(Object model);
}
