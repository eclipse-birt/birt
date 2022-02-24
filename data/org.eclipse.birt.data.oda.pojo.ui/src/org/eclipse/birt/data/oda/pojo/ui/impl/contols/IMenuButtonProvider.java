/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
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

package org.eclipse.birt.data.oda.pojo.ui.impl.contols;

import org.eclipse.swt.graphics.Image;

public interface IMenuButtonProvider {

	public void setInput(ClassSelectionButton input);

	public String[] getMenuItems();

	public Image getMenuItemImage(String menuItem);

	public String getMenuItemText(String menuItem);

	public String getTooltipText(String menuItem);

	public void handleSelectionEvent(String type);

	public String getButtonText();

	public String getButtonImage();

}
