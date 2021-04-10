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

package org.eclipse.birt.report.designer.internal.ui.views;

import org.eclipse.birt.report.designer.internal.ui.editors.parts.event.IFastConsumerProcessor;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.AbstractModelEventProcessor;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.ContentEvent;

/**
 * Processor the model event for the DesignerOutline
 */

public class DesignerOutlineEventProcessor extends AbstractModelEventProcessor implements IFastConsumerProcessor {

	public final static String EVENT_CONTENT = "Event Content"; //$NON-NLS-1$

	/**
	 * @param factory
	 */
	public DesignerOutlineEventProcessor(IModelEventFactory factory) {
		super(factory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.
	 * GraphicsViewModelEventProcessor#createModelEventInfoFactory()
	 */
	protected IModelEventInfoFactory createModelEventInfoFactory() {
		return new OutlineModelEventInfoFactory();
	}

	/**
	 * OutlineModelEventInfoFactory
	 */
	private static class OutlineModelEventInfoFactory implements IModelEventInfoFactory {

		/*
		 * Creat the report runnable for the DesignerOutline.
		 * 
		 * @see
		 * org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.
		 * GraphicsViewModelEventProcessor.ModelEventInfoFactory#createModelEventInfo(
		 * org.eclipse.birt.report.model.api.DesignElementHandle,
		 * org.eclipse.birt.report.model.api.activity.NotificationEvent)
		 */
		public IModelEventInfo createModelEventInfo(DesignElementHandle focus, NotificationEvent ev) {
			switch (ev.getEventType()) {
			case NotificationEvent.CONTENT_EVENT: {
				if (ev instanceof ContentEvent && ((ContentEvent) ev).getAction() == ContentEvent.ADD)
					return new OutlineContentModelEventInfo(focus, ev);
				else
					return new RefreshModelEventInfo(focus, ev);
			}
			default: {
				return new RefreshModelEventInfo(focus, ev);
			}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.
	 * GraphicsViewModelEventProcessor#includeEventType(int)
	 */
	protected boolean includeEventType(int type) {
		return true;
	}

	/**
	 * OutlineContentModelEventInfo
	 */
	protected static class OutlineContentModelEventInfo extends ModelEventInfo {
		public static final String CONTENT_EVENTTYPE = "Content event type"; //$NON-NLS-1$

		/**
		 * @param focus
		 * @param ev
		 */
		private OutlineContentModelEventInfo(DesignElementHandle focus, NotificationEvent ev) {
			super(focus, ev);
			assert ev instanceof ContentEvent;
			setContentActionType(((ContentEvent) ev).getAction());
			setContent(((ContentEvent) ev).getContent());
		}

		public int getContentActionType() {
			return ((Integer) getOtherInfo().get(CONTENT_EVENTTYPE)).intValue();
		}

		/**
		 * @param contentActionType
		 */
		public void setContentActionType(int contentActionType) {
			getOtherInfo().put(CONTENT_EVENTTYPE, Integer.valueOf(contentActionType));
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.
		 * GraphicsViewModelEventProcessor.ModelEventInfo#canAcceptModelEvent(org.
		 * eclipse.birt.report.model.api.DesignElementHandle,
		 * org.eclipse.birt.report.model.api.activity.NotificationEvent)
		 */
		public boolean canAcceptModelEvent(IModelEventInfo info) {
			if (!(info instanceof OutlineContentModelEventInfo) || getContentActionType() != ContentEvent.ADD) {
				return false;
			}

			OutlineContentModelEventInfo newInfo = (OutlineContentModelEventInfo) info;
			if (newInfo.getContentActionType() != ContentEvent.ADD) {
				return false;
			}

			DesignElementHandle element = newInfo.getTarget();
			while (element != null) {
				if (getContent() == element.getElement()) {
					return true;
				}
				if (element instanceof ModuleHandle) {
					break;
				}
				element = element.getContainer();
			}
			return false;
		}

		/**
		 * @return
		 */
		public Object getContent() {
			return getOtherInfo().get(EVENT_CONTENT);
		}

		/**
		 * @param onj
		 */
		public void setContent(Object obj) {
			getOtherInfo().put(EVENT_CONTENT, obj);
		}
	}

	/**
	 * RefreshModelEventInfo
	 */
	protected static class RefreshModelEventInfo extends ModelEventInfo {

		/**
		 * @param focus
		 * @param ev
		 */
		private RefreshModelEventInfo(DesignElementHandle focus, NotificationEvent ev) {
			super(focus, ev);

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.
		 * GraphicsViewModelEventProcessor.ModelEventInfo#canAcceptModelEvent(org.
		 * eclipse.birt.report.model.api.DesignElementHandle,
		 * org.eclipse.birt.report.model.api.activity.NotificationEvent)
		 */
		public boolean canAcceptModelEvent(IModelEventInfo info) {
			return info.getType() != NotificationEvent.CONTENT_EVENT;
		}
	}

	public boolean isOverdued() {
		return getFactory().isDispose();
	}
}
