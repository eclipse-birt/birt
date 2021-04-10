/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
