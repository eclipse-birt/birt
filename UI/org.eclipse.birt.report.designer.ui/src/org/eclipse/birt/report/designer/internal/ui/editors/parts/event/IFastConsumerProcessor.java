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

package org.eclipse.birt.report.designer.internal.ui.editors.parts.event;

/**
 * Because the outline processot is create frequently, and now hasn't a good way
 * to remove the processor from the manager, so the interface use to manage this
 * kind of processor. When the processod is overdued, Event manager remove the
 * processor.
 */

public interface IFastConsumerProcessor extends IModelEventProcessor {
	/**
	 * If the processor is overdued.
	 * 
	 * @return
	 */
	boolean isOverdued();
}
