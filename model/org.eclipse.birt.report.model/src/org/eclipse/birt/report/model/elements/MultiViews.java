/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

package org.eclipse.birt.report.model.elements;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.MultiViewsHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.ContentEvent;
import org.eclipse.birt.report.model.api.command.PropertyEvent;
import org.eclipse.birt.report.model.api.command.ViewsContentEvent;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.IMultiViewsModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;

/**
 *
 */

public class MultiViews extends AbstractMultiViews implements IMultiViewsModel {

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt.
	 * report.model.elements.ElementVisitor)
	 */

	@Override
	public void apply(ElementVisitor visitor) {
		visitor.visitMultiView(this);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */

	@Override
	public String getElementName() {
		return ReportDesignConstants.MULTI_VIEWS;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.core.IDesignElement#getHandle(org.eclipse.
	 * birt.report.model.core.Module)
	 */

	@Override
	public DesignElementHandle getHandle(Module module) {
		return handle(module);
	}

	/**
	 * Returns an API handle for this element.
	 *
	 * @param module the module of the dimension
	 *
	 * @return an API handle for this element.
	 */

	private MultiViewsHandle handle(Module module) {
		if (handle == null) {
			handle = new MultiViewsHandle(module, this);
		}
		return (MultiViewsHandle) handle;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.core.DesignElement#broadcast(org.eclipse.birt.
	 * report.model.api.activity.NotificationEvent,
	 * org.eclipse.birt.report.model.core.Module)
	 */

	@Override
	public void broadcast(NotificationEvent ev, Module module) {
		super.broadcast(ev, module);

		DesignElement tmpContainer = getContainer();
		if (tmpContainer == null) {
			return;
		}

		NotificationEvent newEvent = adjustEvent(ev, tmpContainer);
		if (newEvent != null) {
			tmpContainer.broadcast(newEvent, module);
		}
	}

	/**
	 * Changes the content event to the <code>ViewsContentEvent</code>.
	 *
	 * @param ev the given event
	 * @return the return event
	 */

	private static NotificationEvent adjustEvent(NotificationEvent ev, DesignElement tmpContainer) {

		if (ev instanceof PropertyEvent) {
			return new PropertyEvent(tmpContainer, IReportItemModel.MULTI_VIEWS_PROP);
		}

		if (!(ev instanceof ContentEvent)) {
			return null;
		}

		ContentEvent tmpEv = (ContentEvent) ev;

		// actions cannot be same in two events because of UI constraints.

		int action = tmpEv.getAction();

		int newAction = -1;
		switch (action) {
		case ContentEvent.ADD:
			newAction = ViewsContentEvent.ADD;
			break;
		case ContentEvent.REMOVE:
			newAction = ViewsContentEvent.REMOVE;
			break;
		case ContentEvent.SHIFT:
			newAction = ViewsContentEvent.SHIFT;
			break;
		default:
			assert false;
		}
		ContainerContext tmpContext = new ContainerContext(tmpContainer, IReportItemModel.MULTI_VIEWS_PROP);
		ev = new ViewsContentEvent(tmpContext, (DesignElement) tmpEv.getContent(), newAction);

		ev.setDeliveryPath(NotificationEvent.CONTAINER);

		return ev;
	}
}
