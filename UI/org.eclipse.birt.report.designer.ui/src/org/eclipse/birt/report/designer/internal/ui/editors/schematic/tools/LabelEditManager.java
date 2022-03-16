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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.PlaceHolderEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.LabelFigure;
import org.eclipse.birt.report.designer.internal.ui.views.LabelCellEditor;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.SharedCursors;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.CellEditorActionHandler;

/**
 * Manager for label editor.
 *
 */
public class LabelEditManager extends DirectEditManager {

	private IActionBars actionBars;
	private CellEditorActionHandler actionHandler;
	private IAction copy, cut, paste, undo, redo, find, selectAll, delete;
	Font scaledFont;
	private Object model;
	private boolean changed = false;

	/**
	 * Constructor.
	 *
	 * @param source
	 * @param editorType
	 * @param locator
	 */
	public LabelEditManager(GraphicalEditPart source, Class editorType, CellEditorLocator locator) {
		super(source, editorType, locator);
		setModel(source.getModel());
	}

	/**
	 * @param model
	 */
	public void setModel(Object model) {
		this.model = model;
	}

	Object getModel() {
		return model;
	}

	/**
	 * @see org.eclipse.gef.tools.DirectEditManager#bringDown()
	 */
	@Override
	protected void bringDown() {
		if (actionHandler != null) {
			actionHandler.dispose();
			actionHandler = null;
		}
		if (actionBars != null) {
			restoreSavedActions(actionBars);
			actionBars.updateActionBars();
			actionBars = null;
		}
		// This method might be re-entered when super.bringDown() is called.
		Font disposeFont = scaledFont;
		scaledFont = null;
		super.bringDown();
		if (disposeFont != null) {
			disposeFont.dispose();
		}
		if (getEditPart() instanceof PlaceHolderEditPart) {
			((PlaceHolderEditPart) getEditPart()).perfrormLabelEdit(getChanged());
		}
	}

	private void restoreSavedActions(IActionBars actionBars) {
		actionBars.setGlobalActionHandler(ActionFactory.COPY.getId(), copy);
		actionBars.setGlobalActionHandler(ActionFactory.PASTE.getId(), paste);
		actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(), delete);
		actionBars.setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(), selectAll);
		actionBars.setGlobalActionHandler(ActionFactory.CUT.getId(), cut);
		actionBars.setGlobalActionHandler(ActionFactory.FIND.getId(), find);
		actionBars.setGlobalActionHandler(ActionFactory.UNDO.getId(), undo);
		actionBars.setGlobalActionHandler(ActionFactory.REDO.getId(), redo);
	}

	@Override
	protected void initCellEditor() {
		Text text = (Text) getCellEditor().getControl();

		LabelFigure labelFigure = (LabelFigure) getEditPart().getFigure();
		String initialLabelText = ((LabelHandle) getModel()).getText();
		if (initialLabelText == null) {
			initialLabelText = ""; //$NON-NLS-1$
		}
		getCellEditor().setValue(initialLabelText);
		IFigure figure = getEditPart().getFigure();
		scaledFont = figure.getFont();
		FontData data = scaledFont.getFontData()[0];
		Dimension fontSize = new Dimension(0, data.getHeight());
		labelFigure.translateToAbsolute(fontSize);
		data.setHeight(fontSize.height);
		scaledFont = new Font(null, data);

		text.setFont(scaledFont);
		text.selectAll();

//		 Hook the cell editor's copy/paste actions to the actionBars so that they can
		// be invoked via keyboard shortcuts.
		actionBars = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor()
				.getEditorSite().getActionBars();
		saveCurrentActions(actionBars);
		actionHandler = new CellEditorActionHandler(actionBars);
		actionHandler.addCellEditor(getCellEditor());
		actionBars.updateActionBars();
	}

	private void saveCurrentActions(IActionBars actionBars) {
		copy = actionBars.getGlobalActionHandler(ActionFactory.COPY.getId());
		paste = actionBars.getGlobalActionHandler(ActionFactory.PASTE.getId());
		delete = actionBars.getGlobalActionHandler(ActionFactory.DELETE.getId());
		selectAll = actionBars.getGlobalActionHandler(ActionFactory.SELECT_ALL.getId());
		cut = actionBars.getGlobalActionHandler(ActionFactory.CUT.getId());
		find = actionBars.getGlobalActionHandler(ActionFactory.FIND.getId());
		undo = actionBars.getGlobalActionHandler(ActionFactory.UNDO.getId());
		redo = actionBars.getGlobalActionHandler(ActionFactory.REDO.getId());
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
		int style = this.applyBidiStyle(SWT.MULTI | SWT.WRAP); // bidi_hcg

		LabelCellEditor editor = new LabelCellEditor(composite, style);
		// new LabelCellEditor( composite, SWT.MULTI | SWT.WRAP );
		final Control c = editor.getControl();
		c.addMouseTrackListener(new MouseTrackAdapter() {

			@Override
			public void mouseEnter(MouseEvent e) {
				c.setCursor(SharedCursors.IBEAM);
			}

		});
		return editor;
	}

	@Override
	protected void commit() {
		setChanged(true);
		super.commit();
	}

	private void setChanged(boolean b) {
		this.changed = b;
	}

	public boolean getChanged() {
		return changed;
	}

	/**
	 * @param style
	 * @return A new CellEditor style
	 * @author bidi_hcg
	 */
	private int applyBidiStyle(int style) {
		LabelFigure figure = (LabelFigure) getEditPart().getFigure();
		boolean rtl = DesignChoiceConstants.BIDI_DIRECTION_RTL.equals(figure.getDirection());
		style |= (rtl ? SWT.RIGHT_TO_LEFT : SWT.LEFT_TO_RIGHT);

		String align = figure.getTextAlign();

		if (IStyle.CSS_CENTER_VALUE.equals(align)) {
			style |= SWT.CENTER;
		} else if (IStyle.CSS_RIGHT_VALUE.equals(align)) {
			style |= (rtl ? SWT.LEFT : SWT.RIGHT);
		} else if (IStyle.CSS_LEFT_VALUE.equals(align)) {
			style |= (rtl ? SWT.RIGHT : SWT.LEFT);
		} else {
			style |= SWT.LEFT;
		}

		return style;
	}

}
