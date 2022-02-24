/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: IBM Corporation - initial API and implementation
 *               Actuate Corporation - Change to fit BIRT requirement
 ******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * FigureDirectEditManager
 */
public class FigureDirectEditManager extends DirectEditManager {

	Font scaledFont;

	String initialText = ""; //$NON-NLS-1$

	public FigureDirectEditManager(GraphicalEditPart source) {
		super(source, TextCellEditor.class, new FigureCellEditorLocator(source.getFigure()));
	}

	/**
	 * @see org.eclipse.gef.tools.DirectEditManager#bringDown()
	 */
	@Override
	protected void bringDown() {
		// This method might be re-entered when super.bringDown() is called.
		Font disposeFont = scaledFont;
		scaledFont = null;
		super.bringDown();
		if (disposeFont != null) {
			disposeFont.dispose();
		}
	}

	@Override
	protected void initCellEditor() {
		Text text = (Text) getCellEditor().getControl();

		IFigure figure = getEditPart().getFigure();
		String initialLabelText = initialText;
		getCellEditor().setValue(initialLabelText);

		scaledFont = figure.getFont();
		FontData data = scaledFont.getFontData()[0];
		Dimension fontSize = new Dimension(0, data.getHeight());
		figure.translateToAbsolute(fontSize);
		data.setHeight(fontSize.height);
		scaledFont = new Font(null, data);

		text.setFont(scaledFont);
		text.selectAll();
	}

	/**
	 * Creates the cell editor on the given composite. The cell editor is created by
	 * instantiating the cell editor type passed into this DirectEditManager's
	 * constuctor.
	 *
	 * @param composite the composite to create the cell editor on
	 * @return the newly created cell editor
	 */
	@Override
	protected CellEditor createCellEditorOn(Composite composite) {
		return new TextCellEditor(composite, SWT.MULTI | SWT.WRAP);
	}

	/**
	 * @return Returns the initialText.
	 */
	public String getInitialText() {
		return initialText;
	}

	/**
	 * @param initialText The initialText to set.
	 */
	public void setInitialText(String initialText) {
		this.initialText = initialText;
	}

}
