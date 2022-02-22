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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.designer.internal.ui.editors.parts.event.IFastConsumerProcessor;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.ContentEvent;
import org.eclipse.birt.report.model.api.command.PropertyEvent;
import org.eclipse.birt.report.model.api.command.ViewsContentEvent;
import org.eclipse.birt.report.model.api.core.IDesignElement;

/**
 * GraphicsViewModelEventProcessor
 */
public class GraphicsViewModelEventProcessor extends AbstractModelEventProcessor implements IFastConsumerProcessor {

	public static final String CONTENT_EVENTTYPE = "Content event type"; //$NON-NLS-1$
	public static final String EVENT_CONTENTS = "Event contents"; //$NON-NLS-1$

	/**
	 * @param factory
	 */
	public GraphicsViewModelEventProcessor(IModelEventFactory factory) {
		super(factory);
	}

	/**
	 * Filter the event.
	 *
	 * @param type
	 * @return
	 */
	@Override
	protected boolean includeEventType(int type) {
		return type == NotificationEvent.CONTENT_EVENT || type == NotificationEvent.PROPERTY_EVENT
				|| type == NotificationEvent.NAME_EVENT || type == NotificationEvent.STYLE_EVENT
				|| type == NotificationEvent.EXTENSION_PROPERTY_DEFINITION_EVENT
				|| type == NotificationEvent.LIBRARY_EVENT || type == NotificationEvent.THEME_EVENT
				|| type == NotificationEvent.CONTENT_REPLACE_EVENT || type == NotificationEvent.TEMPLATE_TRANSFORM_EVENT
				|| type == NotificationEvent.ELEMENT_LOCALIZE_EVENT || type == NotificationEvent.LIBRARY_RELOADED_EVENT
				|| type == NotificationEvent.DATA_DESIGN_RELOADED_EVENT || type == NotificationEvent.CSS_EVENT
				|| type == NotificationEvent.VIEWS_CONTENT_EVENT || type == NotificationEvent.CSS_RELOADED_EVENT;
	}

	/**
	 * ContentModelEventInfo
	 */
	protected static class ContentModelEventInfo extends ModelEventInfo {

		// private int contentActionType;
		private ContentModelEventInfo(DesignElementHandle focus, NotificationEvent ev) {
			super(focus, ev);
			// assert ev instanceof ContentEvent;
			setTarget(focus);
			setType(ev.getEventType());
			if (ev instanceof ContentEvent) {
				setContentActionType(((ContentEvent) ev).getAction());

				addChangeContents(((ContentEvent) ev).getContent());
			} else if (ev instanceof ViewsContentEvent) {
				setContentActionType(((ViewsContentEvent) ev).getAction());

				addChangeContents(((ViewsContentEvent) ev).getContent());
			}
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts
		 * . GraphicsViewModelEventProcessor.ModelEventInfo#canAcceptModelEvent(org
		 * .eclipse.birt.report.model.api.DesignElementHandle,
		 * org.eclipse.birt.report.model.api.activity.NotificationEvent)
		 */
		@Override
		public boolean canAcceptModelEvent(IModelEventInfo info) {
			if (getContentActionType() == ContentEvent.REMOVE && getChangeContents().contains(info.getTarget())) {
				return true;
			}
			boolean bool = super.canAcceptModelEvent(info);
			if (!(info instanceof ContentModelEventInfo)) {
				return false;
			}
			return bool && ((ContentModelEventInfo) info).getContentActionType() == ((ContentModelEventInfo) info)
					.getContentActionType();
		}

		/**
		 * @return
		 */
		public int getContentActionType() {
			return ((Integer) getOtherInfo().get(CONTENT_EVENTTYPE)).intValue();
		}

		/**
		 * @param contentActionType
		 */
		public void setContentActionType(int contentActionType) {
			getOtherInfo().put(CONTENT_EVENTTYPE, Integer.valueOf(contentActionType));
		}

		public List getChangeContents() {
			return (List) getOtherInfo().get(EVENT_CONTENTS);
		}

		public void addChangeContents(Object obj) {
			Map map = getOtherInfo();

			if (obj instanceof IDesignElement) {
				obj = ((IDesignElement) obj).getHandle(getTarget().getModule());
			}
			List list = (List) map.get(EVENT_CONTENTS);
			if (list == null) {
				list = new ArrayList();
				map.put(EVENT_CONTENTS, list);
			}
			list.add(obj);
		}
	}

	/**
	 * Creat the factor to ctreat the report runnable.
	 *
	 * @return
	 */
	@Override
	protected IModelEventInfoFactory createModelEventInfoFactory() {
		return new GraphicsModelEventInfoFactory();
	}

	/**
	 * ModelEventInfoFactory
	 */
	protected static class GraphicsModelEventInfoFactory implements IModelEventInfoFactory {

		/**
		 * Creat the report runnable for the ReportEditorWithPalette.
		 *
		 * @param focus
		 * @param ev
		 * @return
		 */
		@Override
		public IModelEventInfo createModelEventInfo(DesignElementHandle focus, NotificationEvent ev) {
			switch (ev.getEventType()) {
			case NotificationEvent.VIEWS_CONTENT_EVENT:
			case NotificationEvent.CONTENT_EVENT: {
				return new ContentModelEventInfo(focus, ev);
			}
			default: {
				return new GraphicsViewModelEventInfo(focus, ev);
			}
			}
		}
	}

	/**
	 * GraphicsViewModelEventInfo
	 */
	private static class GraphicsViewModelEventInfo extends ModelEventInfo {

		public GraphicsViewModelEventInfo(DesignElementHandle focus, NotificationEvent ev) {
			super(focus, ev);
			if (ev instanceof PropertyEvent) {
				PropertyEvent proEvent = (PropertyEvent) ev;
				getOtherInfo().put(proEvent.getPropertyName(), focus);
			}
		}

		@Override
		public void addModelEvent(IModelEventInfo info) {
			getOtherInfo().putAll(info.getOtherInfo());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.parts.event.
	 * IFastConsumerProcessor#isOverdued()
	 */
	@Override
	public boolean isOverdued() {
		return getFactory().isDispose();
	}

	@Override
	public void postElementEvent() {
		try {
			if (getFactory() instanceof IAdvanceModelEventFactory) {
				((IAdvanceModelEventFactory) getFactory()).eventDispathStart();
			}
			super.postElementEvent();
		} finally {
			if (getFactory() instanceof IAdvanceModelEventFactory) {
				((IAdvanceModelEventFactory) getFactory()).eventDispathEnd();
			}
		}
	}
}
