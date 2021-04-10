/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
