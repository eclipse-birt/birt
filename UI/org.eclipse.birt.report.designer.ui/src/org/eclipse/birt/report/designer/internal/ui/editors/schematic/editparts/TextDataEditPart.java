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

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.LabelFigure;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.TextFigure;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionBuilder;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider;
import org.eclipse.birt.report.model.api.TextDataHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.draw2d.IFigure;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.ui.PlatformUI;

public class TextDataEditPart extends LabelEditPart {

	private static final String FIGURE_DEFAULT_TEXT = Messages.getString("TextDataEditPart.Figure.Dafault"); //$NON-NLS-1$

	public TextDataEditPart(Object model) {
		super(model);
	}

	/**
	 * Popup the builder for Data element
	 */
	public void performDirectEdit() {
		TextDataHandle handle = (TextDataHandle) getModel();

		ExpressionBuilder dialog = new ExpressionBuilder(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
				handle.getValueExpr());

		dialog.setExpressionProvier(new ExpressionProvider(handle));

		dialog.setEditModal(true);

		if (dialog.open() == Dialog.OK) {
			try {
				((TextDataHandle) getModel()).setValueExpr(dialog.getResult());
			} catch (SemanticException e) {
				ExceptionHandler.handle(e);
				return;
			}
			refreshVisuals();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts
	 * .LabelEditPart#getText()
	 */
	protected String getText() {
		TextDataHandle handle = (TextDataHandle) getModel();
		String text = handle.getValueExpr();
		if (text == null || text.length() == 0) {
			text = FIGURE_DEFAULT_TEXT; // $NON-NLS-1$
		}
		// else
		// {
		// if ( text.length( ) > TRUNCATE_LENGTH
		// && DesignChoiceConstants.TEXT_CONTENT_TYPE_HTML.equals(
		// handle.getContentType( ) ) )
		// {
		// text = text.substring( 0, TRUNCATE_LENGTH - 2 ) + ELLIPSIS;
		// }
		// }
		return text;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts
	 * .LabelEditPart#hasText()
	 */
	protected boolean hasText() {
		if (StringUtil.isBlank(((TextDataHandle) getModel()).getValueExpr())) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure() {
		TextFigure text = new TextFigure();
		return text;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts
	 * .ReportElementEditPart#refreshFigure()
	 */
	public void refreshFigure() {
		super.refreshFigure();

		((LabelFigure) getFigure()).setToolTipText(((TextDataHandle) getModel()).getValueExpr());
	}
}
