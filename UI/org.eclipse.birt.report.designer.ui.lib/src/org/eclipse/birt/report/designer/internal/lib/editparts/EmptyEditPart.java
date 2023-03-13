/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation .
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

package org.eclipse.birt.report.designer.internal.lib.editparts;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.DesignElementHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.HandleAdapterFactory;
import org.eclipse.birt.report.designer.internal.lib.editors.figures.EmptyFigure;
import org.eclipse.birt.report.designer.internal.ui.editors.parts.ISelectionFilter;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;

/**
 * The Editpart is create in the library editor when the seleection object form
 * outline is not a visual element.
 *
 */
public class EmptyEditPart extends ReportElementEditPart {

	public final static String EMPTY_LIB = Messages.getString("EmptyEditPart.Msg.EmptyLib"); //$NON-NLS-1$
	public final static String NON_VISUAL_SELECTED = Messages.getString("EmptyEditPart.Msg.NonVisual"); //$NON-NLS-1$

	/**
	 * @param model
	 */
	public EmptyEditPart(Object model) {
		super(model);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.
	 * ReportElementEditPart#elementChanged(org.eclipse.birt.report.model.api.
	 * DesignElementHandle,
	 * org.eclipse.birt.report.model.api.activity.NotificationEvent)
	 */
	public void elementChanged(DesignElementHandle arg0, NotificationEvent arg1) {
		// do nothing
	}

	/*
	 * Doesn't install any police (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.
	 * ReportElementEditPart#createEditPolicies()
	 */
	@Override
	protected void createEditPolicies() {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.
	 * ReportElementEditPart#refreshFigure()
	 */
	@Override
	public void refreshFigure() {
		getFigure().setSize(getFigure().getParent().getClientArea().getSize());
		// if(getModel() instanceof LibRootModel)
		// {
		// if(((LibRootModel)getModel()).isEmpty())
		// {
		// ((EmptyFigure)getFigure()).setText(EMPTY_LIB);
		// }
		// else
		// {
		// ((EmptyFigure)getFigure()).setText(NON_VISUAL_SELECTED);
		// }
		// }
		// else
		// {
		// ((EmptyFigure)getFigure()).setText(NON_VISUAL_SELECTED);
		// }
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	@Override
	protected IFigure createFigure() {
		return new EmptyFigure();
	}

	/*
	 * Doesn't install any police include install by parent (non-Javadoc)
	 *
	 * @see org.eclipse.gef.EditPart#installEditPolicy(java.lang.Object,
	 * org.eclipse.gef.EditPolicy)
	 */
	@Override
	public void installEditPolicy(Object role, EditPolicy editPolicy) {
		// do nother
	}

	@Override
	public DesignElementHandleAdapter creatDesignElementHandleAdapter() {
		return HandleAdapterFactory.getInstance().getDesignElementHandleAdapter(getModel(), this);
	}

	@Override
	public Object getAdapter(Class key) {
		if (key == ISelectionFilter.class) {
			return new ISelectionFilter() {

				@Override
				public List filterEditpart(List editparts) {
					List retValue = new ArrayList(editparts);
					for (int i = 0; i < editparts.size(); i++) {
						if (editparts.get(i) instanceof EmptyEditPart) {
							retValue.remove(editparts.get(i));
						}
					}

					return retValue;
				}

			};
		}
		return super.getAdapter(key);
	}
}
