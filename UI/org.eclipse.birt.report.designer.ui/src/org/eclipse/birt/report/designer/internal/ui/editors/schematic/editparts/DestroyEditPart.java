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

import java.util.Map;

import org.eclipse.birt.report.designer.internal.ui.editors.ReportColorConstants;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.LabelFigure;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.FontManager;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.EditPart;
import org.eclipse.swt.SWT;

/**
 *
 */

public class DestroyEditPart extends DummyEditpart {

	private static final String MESSAGE = Messages.getString("DestroyEditPart.Message"); //$NON-NLS-1$

	public DestroyEditPart(Object model) {
		super(model);
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
		StyleHandle style = ((DesignElementHandle) getModel()).getPrivateStyle();
		((LabelFigure) getFigure()).setFont(FontManager.getFont("Dialog", 10, SWT.ITALIC)); //$NON-NLS-1$

		// ( (LabelFigure) getFigure( ) ).setImage( getImage( ) );
		((LabelFigure) getFigure()).setAlignment(PositionConstants.WEST);

		// bidi_hcg start
		// Set direction before setting text
		((LabelFigure) getFigure()).setDirection(getTextDirection());
		// bidi_hcg end
		((LabelFigure) getFigure()).setText(MESSAGE);
		((LabelFigure) getFigure()).setTextAlign(DesignChoiceConstants.TEXT_ALIGN_LEFT);
		((LabelFigure) getFigure()).setForegroundColor(ReportColorConstants.RedWarning);
		((LabelFigure) getFigure()).setDisplay(style.getDisplay());

		getFigure().setBorder(new LineBorder(1));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	@Override
	protected IFigure createFigure() {
		LabelFigure label = new LabelFigure();
		return label;
	}

	@Override
	protected void refreshPageClip() {
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.
	 * ReportElementEditPart#isinterest(java.lang.Object)
	 */
	@Override
	public boolean isinterest(Object model) {
		if (!(model instanceof DesignElementHandle)) {
			return false;
		}
		DesignElementHandle handle = (DesignElementHandle) model;
		while (handle != null) {
			if (getModel().equals(handle)) {
				return true;
			}
			handle = handle.getContainer();
		}
		return super.isinterest(model);
	}

	@Override
	protected void contentChange(Map info) {
		reload();
	}

	@Override
	protected void propertyChange(Map info) {
		reload();
	}

	private void reload() {
		EditPart part = getParent();
		((ReportElementEditPart) part).removeChild(this);
		part.refresh();
	}
}
