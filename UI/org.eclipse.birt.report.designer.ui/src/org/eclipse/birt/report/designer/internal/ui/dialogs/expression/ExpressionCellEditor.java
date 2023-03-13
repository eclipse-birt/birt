/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

package org.eclipse.birt.report.designer.internal.ui.dialogs.expression;

import java.text.MessageFormat;

import org.eclipse.birt.report.designer.internal.ui.expressions.ExpressionContextFactoryImpl;
import org.eclipse.birt.report.designer.internal.ui.expressions.IExpressionBuilder;
import org.eclipse.birt.report.designer.internal.ui.expressions.IExpressionContextFactory;
import org.eclipse.birt.report.designer.internal.ui.util.ExpressionButtonUtil;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.ui.dialogs.IExpressionProvider;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExpressionHandle;
import org.eclipse.birt.report.model.api.ExpressionType;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

/**
 *
 */

public class ExpressionCellEditor extends CellEditor {

	/**
	 * The current contents.
	 */
	private Control contents;

	/**
	 * The button.
	 */
	protected Button button;

	/**
	 * Listens for 'focusLost' events and fires the 'apply' event as long as the
	 * focus wasn't lost because the dialog was opened.
	 */
	private FocusListener buttonFocusListener;

	private IExpressionCellEditorProvider provider;

	/**
	 * Internal class for laying out the dialog.
	 */
	private class DialogCellLayout extends Layout {

		@Override
		public void layout(Composite editor, boolean force) {
			Rectangle bounds = editor.getClientArea();
			Point size = button.computeSize(SWT.DEFAULT, SWT.DEFAULT, force);
			if (contents != null) {
				contents.setBounds(0, 0, bounds.width - size.x, bounds.height);
			}
			button.setBounds(bounds.width - size.x, 0, size.x, bounds.height);
		}

		@Override
		public Point computeSize(Composite editor, int wHint, int hHint, boolean force) {
			if (wHint != SWT.DEFAULT && hHint != SWT.DEFAULT) {
				return new Point(wHint, hHint);
			}
			Point contentsSize = contents.computeSize(SWT.DEFAULT, SWT.DEFAULT, force);
			Point buttonSize = button.computeSize(SWT.DEFAULT, SWT.DEFAULT, force);
			Point result = new Point(buttonSize.x, Math.max(contentsSize.y, buttonSize.y));
			return result;
		}
	}

	protected Menu menu;

	private ModifyListener modifyListener;

	public ExpressionCellEditor(Composite parent, int style) {
		this(parent, style, false);
	}

	public ExpressionCellEditor(Composite parent, int style, boolean allowConstant) {
		super(parent, style);
		setExpressionCellEditorProvider(new ExpressionCellEditorProvider(allowConstant));
	}

	protected Button createButton(Composite parent) {
		Button result = new Button(parent, SWT.DOWN);
		result.setText("..."); //$NON-NLS-1$
		return result;
	}

	protected Control createContents(Composite cell) {
		editor = new Text(cell, getStyle());
		editor.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				keyReleaseOccured(e);
			}
		});
		editor.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				fireApplyEditorValue();
				deactivate();
			}
		});
		editor.addTraverseListener(new TraverseListener() {

			@Override
			public void keyTraversed(TraverseEvent e) {
				if (e.detail == SWT.TRAVERSE_ESCAPE || e.detail == SWT.TRAVERSE_RETURN) {
					e.doit = false;
				}
			}
		});
		editor.addFocusListener(new FocusAdapter() {

			/*
			 * (non-Javadoc)
			 *
			 * @see org.eclipse.swt.events.FocusAdapter#focusLost(org.eclipse.swt
			 * .events.FocusEvent)
			 */
			@Override
			public void focusLost(FocusEvent e) {
				ExpressionCellEditor.this.focusLost();
			}

		});

		editor.addModifyListener(getModifyListener());

		setValueValid(true);

		return editor;
	}

	protected ModifyListener getModifyListener() {
		if (modifyListener == null) {
			modifyListener = new ModifyListener() {

				@Override
				public void modifyText(ModifyEvent e) {
					editOccured(e);
				}
			};
		}
		return modifyListener;
	}

	protected void editOccured(ModifyEvent e) {
		String value = editor.getText();
		if (value == null) {
			value = "";//$NON-NLS-1$
		}
		Object typedValue = value;
		boolean oldValidState = isValueValid();
		boolean newValidState = isCorrect(typedValue);
		if (typedValue == null && newValidState) {
			Assert.isTrue(false, "Validator isn't limiting the cell editor's type range");//$NON-NLS-1$
		}
		if (!newValidState) {
			// try to insert the current value into the error message.
			setErrorMessage(MessageFormat.format(getErrorMessage(), new Object[] { value }));
		}
		valueChanged(oldValidState, newValidState);
	}

	/*
	 * (non-Javadoc) Method declared on CellEditor.
	 */
	@Override
	protected Control createControl(Composite parent) {

		Font font = parent.getFont();
		Color bg = parent.getBackground();

		final Composite editorArea = new Composite(parent, getStyle());
		editorArea.setFont(font);
		editorArea.setBackground(bg);
		editorArea.setLayout(new DialogCellLayout());

		contents = createContents(editorArea);

		button = createButton(editorArea);
		button.setFont(font);

		menu = new Menu(parent.getShell(), SWT.POP_UP);

		button.addListener(SWT.MouseUp, new Listener() {

			@Override
			public void handleEvent(Event e) {
				if (!button.isEnabled() || e.button != 1) {
					return;
				}
				showMenu();
			}

		});
		button.addListener(SWT.KeyUp, new Listener() {

			@Override
			public void handleEvent(Event e) {
				if (e.keyCode == SWT.ARROW_DOWN || e.keyCode == SWT.ARROW_UP) {
					showMenu();
				}
			}

		});

//		setExpressionCellEditorProvider( new ExpressionCellEditorProvider( _allowConstant ) );

		button.addKeyListener(new KeyAdapter() {

			/*
			 * (non-Javadoc)
			 *
			 * @see org.eclipse.swt.events.KeyListener#keyReleased(org.eclipse.swt
			 * .events.KeyEvent)
			 */
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.character == '\u001b') { // Escape
					fireCancelEditor();
				}
			}
		});

		button.addFocusListener(getButtonFocusListener());

		setValueValid(true);

		return editorArea;
	}

	/*
	 * (non-Javadoc)
	 *
	 * Override in order to remove the button's focus listener if the celleditor is
	 * deactivating.
	 *
	 * @see org.eclipse.jface.viewers.CellEditor#deactivate()
	 */
	@Override
	public void deactivate() {
		if (button != null && !button.isDisposed()) {
			button.removeFocusListener(getButtonFocusListener());
		}

		super.deactivate();
	}

	/*
	 * (non-Javadoc) Method declared on CellEditor.
	 */
	@Override
	protected Object doGetValue() {
		if (getExpression() == null || getExpression().trim().length() == 0) {
			if (!isConstantExpression()) {
				button.setData(ExpressionButtonUtil.EXPR_TYPE, null);
			}
			return null;
		}
		return new Expression(getExpression(), getExpressionType());
	}

	/*
	 * (non-Javadoc) Method declared on CellEditor. The focus is set to the cell
	 * editor's button.
	 */
	@Override
	protected void doSetFocus() {
		button.setFocus();

		// add a FocusListener to the button
		button.addFocusListener(getButtonFocusListener());
	}

	/**
	 * Return a listener for button focus.
	 *
	 * @return FocusListener
	 */
	private FocusListener getButtonFocusListener() {
		if (buttonFocusListener == null) {
			buttonFocusListener = new FocusListener() {

				@Override
				public void focusGained(FocusEvent e) {
					// Do nothing
				}

				@Override
				public void focusLost(FocusEvent e) {
					ExpressionCellEditor.this.focusLost();
				}
			};
		}

		return buttonFocusListener;
	}

	protected Text editor;

	private IExpressionContextFactory contextFactory;

	private Object contextObject;

	private SelectionAdapter listener;

	@Override
	protected void focusLost() {
		if (button != null && !button.isFocusControl() && Display.getCurrent().getCursorControl() != button) {
			super.focusLost();
		}
	}

	@Override
	protected void doSetValue(Object value) {
		if (editor != null) {
			editor.removeModifyListener(getModifyListener());
			if (value instanceof Expression) {
				editor.setText(DEUtil.resolveNull(((Expression) value).getStringExpression()));
				button.setData(ExpressionButtonUtil.EXPR_TYPE, ((Expression) value).getType());
			} else if (value instanceof ExpressionHandle) {
				editor.setText(DEUtil.resolveNull(((ExpressionHandle) value).getStringExpression()));
				button.setData(ExpressionButtonUtil.EXPR_TYPE, ((ExpressionHandle) value).getType());
			} else {
				editor.setText(value == null ? "" : value.toString());
				button.setData(ExpressionButtonUtil.EXPR_TYPE, UIUtil.getDefaultScriptType());
			}
			if (isConstantExpression()) {
				editor.setEditable(true);
			}
			refresh();
			editor.addModifyListener(getModifyListener());
		}
	}

	protected void setExpressionType(String exprType) {
		button.setData(ExpressionButtonUtil.EXPR_TYPE, exprType);
		refresh();
	}

	protected boolean isConstantExpression() {
		if (ExpressionType.CONSTANT.equals(getExpressionType())) {
			return true;
		}
		return false;
	}

	protected String getExpressionType() {
		String type = (String) button.getData(ExpressionButtonUtil.EXPR_TYPE);
		if (type == null) {
			type = UIUtil.getDefaultScriptType();
			setExpressionType(type);
		}
		return type;
	}

	protected void setExpressionCellEditorProvider(IExpressionCellEditorProvider provider) {
		if (provider != null && provider != this.provider) {
			this.provider = provider;

			provider.setInput(this);

			for (int i = 0; i < menu.getItemCount(); i++) {
				menu.getItem(i).dispose();
				i--;
			}

			String[] types = this.provider.getExpressionTypes();

			listener = new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					Widget widget = e.widget;
					if (widget instanceof MenuItem) {
						String exprType = (String) widget.getData();
						ExpressionCellEditor.this.provider.handleSelectionEvent(exprType);
					} else if (widget instanceof Button) {
						ExpressionCellEditor.this.provider.handleSelectionEvent(getExpressionType());
					}
				}

			};

			for (int i = 0; i < types.length; i++) {
				MenuItem item = new MenuItem(menu, SWT.CHECK);
				item.setText(this.provider.getText(types[i]));
				item.setData(types[i]);
				item.addSelectionListener(listener);
			}

			if (menu.getItemCount() <= 1) {
				menu = null;
				button.addSelectionListener(listener);
			}

			refresh();
		}
	}

	public void refresh() {
		if (!button.isDisposed()) {
			button.setToolTipText(provider.getTooltipText(getExpressionType()));
		}
	}

	public void setExpressionInput(IExpressionProvider provider, Object contextObject) {
		this.contextFactory = new ExpressionContextFactoryImpl(contextObject, provider);
		this.contextObject = contextObject;
	}

	protected void openExpressionBuilder(IExpressionBuilder builder, String expressionType) {
		editor.setEditable(false);
		builder.setExpression(editor.getText());

		builder.setExpressionContext(contextFactory.getContext(expressionType, contextObject));

		if (builder.open() == Window.OK) {

			Object result = builder.getExpression();
			String newExpression = result == null ? "" : result.toString();
			editor.setText(newExpression);
			button.setData(ExpressionButtonUtil.EXPR_TYPE, expressionType);
			markDirty();
			refresh();
		}

		editor.setFocus();
	}

	protected void openConstantEditor(String expressionType) {
		button.setData(ExpressionButtonUtil.EXPR_TYPE, expressionType);
		setEditable();
		refresh();
		editor.setFocus();
	}

	protected void setEditable() {
		editor.setEditable(true);
	}

	public String getExpression() {
		return editor.getText();
	}

	public void notifyExpressionChangeEvent(String oldExpr, String newExpr) {
		if (oldExpr != null) {
			boolean newValidState = isCorrect(newExpr);
			if (newValidState) {
				markDirty();
			} else {
				// try to insert the current value into the error message.
				setErrorMessage(MessageFormat.format(getErrorMessage(), new Object[] { newExpr.toString() }));
			}
			fireApplyEditorValue();
		}
	}

	private void showMenu() {
		if (menu != null) {
			Rectangle size = button.getBounds();
			menu.setLocation(button.toDisplay(new Point(0, size.height - 1)));

			for (int i = 0; i < menu.getItemCount(); i++) {
				MenuItem item = menu.getItem(i);
				if (item.getData().equals(getExpressionType())) {
					item.setSelection(true);
				} else {
					item.setSelection(false);
				}
			}
			menu.setVisible(true);
		}
	}
}
