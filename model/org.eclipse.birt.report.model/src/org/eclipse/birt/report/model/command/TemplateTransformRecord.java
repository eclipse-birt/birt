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

package org.eclipse.birt.report.model.command;

import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.TemplateTransformEvent;
import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.util.CommandLabelFactory;

/**
 * Records transforming a report item or data set to a template element and
 * transforming a template element to a report item or data set. This is
 * different with the normal replace content record. So different event will be
 * sent to the container to identify this.
 */

public class TemplateTransformRecord extends ContentReplaceRecord {

	/**
	 * Constructs the record with container element, slot id, from element, and to
	 * element.
	 *
	 * @param module        the module in which this record executes
	 * @param containerInfo The container information.
	 * @param from          the element which the record transforms from
	 * @param to            the element which the record transforms to
	 *
	 */

	public TemplateTransformRecord(Module module, ContainerContext containerInfo, DesignElement from,
			DesignElement to) {
		super(module, containerInfo, from, to);
		this.label = CommandLabelFactory.getCommandLabel(MessageConstants.REPLACE_ELEMENT_MESSAGE);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.command.ContentReplaceRecord#getContainerEvent
	 * ()
	 */

	@Override
	protected NotificationEvent getContainerEvent() {
		NotificationEvent event = null;

		// send the template transform event to the container, the container is
		// the "target" element. So the listerners can get the notification to
		// do something, which may be different from what to do after get the
		// normal content replace event.

		if (state != UNDONE_STATE) {
			event = new TemplateTransformEvent(focus, oldElement, newElement);
		} else {
			event = new TemplateTransformEvent(focus, newElement, oldElement);
		}

		if (state == DONE_STATE) {
			event.setSender(sender);
		}

		return event;
	}

}
