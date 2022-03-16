/*******************************************************************************
 * Copyright (c) 2004, 2012 Actuate Corporation.
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

package org.eclipse.birt.report.designer.core.model;

import java.util.List;

import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.swt.graphics.Image;

/**
 * A composition of SlotHandle and PropertyHandle
 */
public interface IMixedHandle {
	SlotHandle getSlotHandle();

	PropertyHandle getPropertyHandle();

	List getChildren();

	String getDisplayName();

	Image getNodeIcon();
}
