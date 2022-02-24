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

import org.eclipse.birt.report.designer.internal.ui.editors.ReportColorConstants;
import org.eclipse.birt.report.designer.internal.ui.editors.parts.event.IModelEventProcessor;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.ReportDesignMarginBorder;
import org.eclipse.birt.report.designer.util.ColorManager;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.gef.EditPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

/**
 * For the report topo edit part to process the model event through the
 * GraphicsViewModelEventProcessor.
 */

public abstract class AbstractReportEditPart extends ReportElementEditPart implements IAdvanceModelEventFactory {

	public final static String MODEL_EVENT_DISPATCH = "model event dipatch";//$NON-NLS-1$
	public final static String START = "start";//$NON-NLS-1$
	public final static String END = "end";//$NON-NLS-1$
	// private boolean isDispatch = false;

	/**
	 * @param model
	 */
	public AbstractReportEditPart(Object model) {
		super(model);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editparts.AbstractGraphicalEditPart#getAdapter(java.lang.
	 * Class)
	 */
	public Object getAdapter(Class key) {
		if (key == IModelEventProcessor.class) {
			return new GraphicsViewModelEventProcessor(this);
		}
		return super.getAdapter(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.
	 * GraphicsViewModelEventProcessor.IModelEventFactory#createModelEventRunnable(
	 * java.lang.Object, int, java.util.Map)
	 */
	public Runnable createModelEventRunnable(Object focus, int type, Map args) {
		switch (type) {
		case NotificationEvent.CONTENT_REPLACE_EVENT:
		case NotificationEvent.TEMPLATE_TRANSFORM_EVENT:
		case NotificationEvent.VIEWS_CONTENT_EVENT:
		case NotificationEvent.CONTENT_EVENT: {
			return new EditpartReportEventRunnable(focus, type, args) {

				public void runModelChange() {
					contentChange(getFocus(), getArgs());
				}
			};
		}
		case NotificationEvent.NAME_EVENT:
		case NotificationEvent.STYLE_EVENT:
		case NotificationEvent.EXTENSION_PROPERTY_DEFINITION_EVENT:
		case NotificationEvent.THEME_EVENT:
		case NotificationEvent.LIBRARY_EVENT:
		case NotificationEvent.ELEMENT_LOCALIZE_EVENT:

		case NotificationEvent.PROPERTY_EVENT: {
			return new EditpartReportEventRunnable(focus, type, args) {

				public void runModelChange() {
					propertyChange(getFocus(), getArgs());
				}
			};
		}

		case NotificationEvent.CSS_RELOADED_EVENT:
		case NotificationEvent.LIBRARY_RELOADED_EVENT:
		case NotificationEvent.DATA_DESIGN_RELOADED_EVENT: {
			return new EditpartReportEventRunnable(focus, type, args) {

				public void runModelChange() {
					reloadTheChildren();
				}
			};

		}

		default:
			break;
		}
		return null;
	}

	/**
	 * @param focus
	 * @param info
	 */
	protected void propertyChange(Object focus, Map info) {
		if (getViewer() == null) {
			return;
		}
		Object obj = getViewer().getEditPartRegistry().get(focus);
		if (obj instanceof ReportElementEditPart && !((ReportElementEditPart) obj).isDelete()) {
			((ReportElementEditPart) obj).propertyChange(info);
			// return;
		}
		List temp = new ArrayList();
		getEditPartsFormModel(this, focus, temp);
		int size = temp.size();

		for (int i = 0; i < size; i++) {
			Object part = temp.get(i);
			if (part instanceof ReportElementEditPart && !((ReportElementEditPart) part).isDelete()) {
				((ReportElementEditPart) part).propertyChange(info);
			}
		}
	}

	/**
	 * @param part
	 * @param model
	 * @param list
	 */
	private void getEditPartsFormModel(ReportElementEditPart part, Object model, List list) {
		if (!list.isEmpty()) {
			return;
		}
		List children = part.getChildren();
		int size = children.size();
		for (int i = 0; i < size; i++) {
			Object chPart = (Object) children.get(i);
			if (chPart instanceof ReportElementEditPart) {
				getEditPartsFormModel((ReportElementEditPart) chPart, model, list);
			}
		}
		if (part.isinterest(model)) {
			list.add(part);
			return;
		}
	}

	/*
	 * 
	 * @see org.eclipse.gef.EditPart#getDragTracker(org.eclipse.gef.Request)
	 */

	protected void reloadTheChildren() {
		List list = new ArrayList(getChildren());
		int size = list.size();

		for (int i = 0; i < size; i++) {
			EditPart part = (EditPart) list.get(i);

			removeChild(part);
		}

		list = getModelChildren();
		size = list.size();
		for (int i = 0; i < size; i++) {
			Object model = list.get(i);
			addChild(createChild(model), i);
		}

		refreshVisuals();
	}

	/**
	 * @param focus
	 * @param info
	 */
	protected void contentChange(Object focus, Map info) {
		if (getViewer() == null) {
			return;
		}
		Object obj = getViewer().getEditPartRegistry().get(focus);
		if (obj instanceof ReportElementEditPart && !((ReportElementEditPart) obj).isDelete()) {
			((ReportElementEditPart) obj).contentChange(info);
			return;
		}

		List temp = new ArrayList();
		getEditPartsFormModel(this, focus, temp);
		int size = temp.size();

		for (int i = 0; i < size; i++) {
			Object part = temp.get(i);
			if (part instanceof ReportElementEditPart && !((ReportElementEditPart) part).isDelete()) {
				((ReportElementEditPart) part).contentChange(info);
			}
		}
	}

	private abstract class EditpartReportEventRunnable extends ReportEventRunnable {
		public EditpartReportEventRunnable(Object focus, int type, Map args) {
			super(focus, type, args);

		}

		public void run() {
			if (isDispose()) {
				return;
			}
			runModelChange();
			// When the model cahnge, the report design must layout one time.Because the
			// table laout is
			// complex, if the element in the table model change,must notify the table.
			notifyModelChange(getFocus());
		}

		protected abstract void runModelChange();

	}

	protected void notifyModelChange(Object focus) {
		Object obj = getViewer().getEditPartRegistry().get(focus);
		if (obj instanceof ReportElementEditPart) {
			((ReportElementEditPart) obj).notifyModelChange();
			return;
		}
		// if the edit part don't find, Maybe the model is the proxy model
		List temp = new ArrayList();
		getEditPartsFormModel(this, focus, temp);
		int size = temp.size();

		for (int i = 0; i < size; i++) {
			Object part = temp.get(i);
			if (part != null && part instanceof ReportElementEditPart) {
				((ReportElementEditPart) part).notifyModelChange();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.
	 * AbstractModelEventProcessor.IModelEventFactory#isDispose()
	 */
	public boolean isDispose() {
		return getParent() == null || getViewer().getControl().isDisposed();
	}

	/**
	 * 
	 */
	public void eventDispathStart() {
		getViewer().setProperty(MODEL_EVENT_DISPATCH, START);
	}

	/**
	 * 
	 */
	public void eventDispathEnd() {
		getViewer().setProperty(MODEL_EVENT_DISPATCH, END);
	}

	/**
	 * @param color
	 * @return
	 */
	protected Color getBackGroundColor(int color) {
		if (color == SWT.COLOR_LIST_BACKGROUND) {
			return ReportColorConstants.ReportBackground;
		}
		if (color == SWT.COLOR_LIST_FOREGROUND) {
			return ReportColorConstants.ReportForeground;
		}

		return ColorManager.getColor(color);
	}

	@Override
	protected void propertyChange(Map info) {
		if (info.get(ReportDesignHandle.LAYOUT_PREFERENCE_PROP) != null) {
			updateChildrenLayoutPreference(this);
			getFigure().invalidateTree();
			getFigure().revalidate();
		}
		super.propertyChange(info);
	}

	private void updateChildrenLayoutPreference(EditPart part) {
		if (part instanceof ReportElementEditPart) {
			((ReportElementEditPart) part).updateLayoutPreference();
		}
		List children = part.getChildren();
		int size = children.size();
		for (int i = 0; i < size; i++) {
			Object chPart = children.get(i);
			updateChildrenLayoutPreference((EditPart) chPart);
		}
	}

	@Override
	public void activate() {
		super.activate();
	}

	public void refreshMarginBorder(ReportDesignMarginBorder border) {
		// do nothing now
	}
}
