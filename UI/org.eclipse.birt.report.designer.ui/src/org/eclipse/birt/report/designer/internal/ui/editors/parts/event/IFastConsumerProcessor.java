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
