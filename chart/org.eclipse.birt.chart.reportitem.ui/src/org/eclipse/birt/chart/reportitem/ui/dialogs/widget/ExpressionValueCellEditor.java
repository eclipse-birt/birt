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

package org.eclipse.birt.chart.reportitem.ui.dialogs.widget;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionBuilder;
import org.eclipse.birt.report.designer.ui.dialogs.IExpressionProvider;
import org.eclipse.birt.report.designer.ui.dialogs.SelectValueDialog;
import org.eclipse.birt.report.designer.ui.widget.PopupSelectionList;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ParamBindingHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

/**
 * Expression value cell editor
 * 
 */
public class ExpressionValueCellEditor extends CellEditor {

	private static String[] actions = new String[] { Messages.getString("ExpressionValueCellEditor.selectValueAction"), //$NON-NLS-1$
			Messages.getString("ExpressionValueCellEditor.buildExpressionAction"), //$NON-NLS-1$
	};

	private transient ParamBindingHandle[] bindingParams = null;

	private transient String bindingName;
	private transient Text expressionText;
	private transient Button btnPopup;
	private transient ReportElementHandle currentItem = null;
	private transient String[] popupItems = null;
	private transient boolean refreshItems = true;

	private static String[] EMPTY_ARRAY = new String[] {};

	private IExpressionProvider provider;

	private class ExpressionCellLayout extends Layout {

		public void layout(Composite editor, boolean force) {
			Rectangle bounds = editor.getClientArea();
			Point size = btnPopup.computeSize(SWT.DEFAULT, SWT.DEFAULT, force);
			expressionText.setBounds(0, 0, bounds.width - size.x, bounds.height);
			btnPopup.setBounds(bounds.width - size.x, 0, size.x, bounds.height);
		}

		public Point computeSize(Composite editor, int wHint, int hHint, boolean force) {
			if (wHint != SWT.DEFAULT && hHint != SWT.DEFAULT)
				return new Point(wHint, hHint);
			Point contentsSize = expressionText.computeSize(SWT.DEFAULT, SWT.DEFAULT, force);
			Point buttonSize = btnPopup.computeSize(SWT.DEFAULT, SWT.DEFAULT, force);
			// Just return the button width to ensure the button is not clipped
			// if the label is long.
			// The label will just use whatever extra width there is
			Point result = new Point(buttonSize.x, Math.max(contentsSize.y, buttonSize.y));
			return result;
		}
	}

	/**
	 * @param parent
	 */
	public ExpressionValueCellEditor(Composite parent) {
		super(parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.CellEditor#createControl(org.eclipse.swt.widgets.
	 * Composite)
	 */
	protected Control createControl(Composite parent) {
		Composite editorComposite = new Composite(parent, getStyle());
		editorComposite.setLayout(new ExpressionCellLayout());
		expressionText = new Text(editorComposite, SWT.NONE);
		expressionText.addKeyListener(new KeyAdapter() {

			public void keyReleased(KeyEvent e) {
				keyReleaseOccured(e);
			}
		});
		expressionText.addSelectionListener(new SelectionAdapter() {

			public void widgetDefaultSelected(SelectionEvent e) {
				// fireApplyEditorValue();
				// deactivate();
			}
		});
		expressionText.addTraverseListener(new TraverseListener() {

			public void keyTraversed(TraverseEvent e) {
				if (e.detail == SWT.TRAVERSE_ESCAPE || e.detail == SWT.TRAVERSE_RETURN) {
					e.doit = false;
				}
			}
		});
		expressionText.addFocusListener(new FocusAdapter() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.events.FocusAdapter#focusLost(org.eclipse.swt.events.
			 * FocusEvent)
			 */
			public void focusLost(FocusEvent e) {
				ExpressionValueCellEditor.this.focusLost();
			}

		});
		btnPopup = new Button(editorComposite, SWT.ARROW | SWT.DOWN);
		btnPopup.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				refreshList();
				Rectangle textBounds = expressionText.getBounds();
				Point pt = expressionText.toDisplay(textBounds.x, textBounds.y);
				Rectangle rect = new Rectangle(pt.x, pt.y, expressionText.getParent().getBounds().width,
						textBounds.height);

				PopupSelectionList popup = new PopupSelectionList(expressionText.getParent().getShell());
				popup.setItems(popupItems);
				String value = popup.open(rect);
				int selectionIndex = popup.getSelectionIndex();
				if (value != null) {
					String newValue = null;
					// only the column reference can be retrieved from select
					// value list. Use the regular filter get expression like
					// row.xxx or row[xx]
					// that may be retreived the select values. If there is
					// Exception throw when retrieving, the waring message will
					// show.
					if (value.equals((actions[0]))) {
						if (bindingName != null) {
							try {
								List selectValueList = getSelectValueList();
								SelectValueDialog dialog = new SelectValueDialog(
										PlatformUI.getWorkbench().getDisplay().getActiveShell(),
										Messages.getString("ExpressionValueCellEditor.title")); //$NON-NLS-1$
								dialog.setSelectedValueList(selectValueList);
								if (bindingParams != null) {
									dialog.setBindingParams(bindingParams);
								}
								if (dialog.open() == IDialogConstants.OK_ID) {
									newValue = dialog.getSelectedExprValue();
								}
							} catch (Exception ex) {
								MessageDialog.openError(null, Messages.getString("SelectValueDialog.selectValue"), //$NON-NLS-1$
										Messages.getString("SelectValueDialog.messages.error.selectVauleUnavailable") //$NON-NLS-1$
												+ "\n" //$NON-NLS-1$
												+ ex.getMessage());
							}
						} else {
							MessageDialog.openInformation(null, Messages.getString("SelectValueDialog.selectValue"), //$NON-NLS-1$
									Messages.getString("SelectValueDialog.messages.info.selectVauleUnavailable")); //$NON-NLS-1$
						}
					} else if (value.equals(actions[1])) {
						ExpressionBuilder dialog = new ExpressionBuilder(
								PlatformUI.getWorkbench().getDisplay().getActiveShell(), (String) getValue());

						dialog.setExpressionProvier(provider);

						if (dialog.open() == IDialogConstants.OK_ID) {
							newValue = dialog.getResult();
						}
					} else if (selectionIndex > 3) {
						newValue = "params[\"" + value + "\"]"; //$NON-NLS-1$ //$NON-NLS-2$
					}
					if (newValue != null) {
						setValue(newValue);
					}
					expressionText.setFocus();
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}

		});
		setValueValid(true);

		return editorComposite;
	}

	private List getSelectValueList() throws BirtException {
		List selectValueList = new ArrayList();
		ReportItemHandle reportItem = DEUtil.getBindingHolder(currentItem);
		if (bindingName != null && reportItem != null) {

			DataRequestSession session = DataRequestSession.newSession(
					new DataSessionContext(DataSessionContext.MODE_DIRECT_PRESENTATION, reportItem.getModuleHandle()));
			selectValueList.addAll(session.getColumnValueSet(getDataSetHandle(reportItem),
					reportItem.paramBindingsIterator(), reportItem.columnBindingsIterator(), bindingName));
			session.shutdown();
		} else {
			ExceptionHandler.openErrorMessageBox(Messages.getString("SelectValueDialog.errorRetrievinglist"), //$NON-NLS-1$
					Messages.getString("SelectValueDialog.noExpressionSet")); //$NON-NLS-1$
		}
		return selectValueList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.CellEditor#focusLost()
	 */
	protected void focusLost() {
		if (btnPopup != null && !btnPopup.isFocusControl() && Display.getCurrent().getCursorControl() != btnPopup) {
			super.focusLost();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.CellEditor#doGetValue()
	 */
	protected Object doGetValue() {
		if (expressionText != null) {
			return expressionText.getText();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.CellEditor#doSetFocus()
	 */
	protected void doSetFocus() {
		if (expressionText != null && expressionText.isVisible()) {
			expressionText.setFocus();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.CellEditor#doSetValue(java.lang.Object)
	 */
	protected void doSetValue(Object value) {
		if (value != null && expressionText != null) {
			expressionText.setText(value.toString());
		}
	}

	/**
	 * @return Returns the bindingParams.
	 */
	public ParamBindingHandle[] getBindingParams() {
		return bindingParams;
	}

	/**
	 * @param bindingParams The bindingParams to set.
	 */
	public void setBindingParams(ParamBindingHandle[] bindingParams) {
		this.bindingParams = bindingParams;
	}

	/**
	 * @param bindingName The selectValueExpression to set.
	 */
	public void setBindingName(String bindingName) {
		this.bindingName = bindingName;
	}

	public void setReportElement(ReportElementHandle reportItem) {
		currentItem = reportItem;
	}

	private void refreshList() {
		if (refreshItems) {
			ArrayList finalItems = new ArrayList(10);
			for (int n = 0; n < actions.length; n++) {
				finalItems.add(actions[n]);
			}

			if (currentItem != null) {
				// addParamterItems( finalItems );
			}
			popupItems = (String[]) finalItems.toArray(EMPTY_ARRAY);
		}
		refreshItems = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.CellEditor#activate()
	 */
	public void activate() {
		refreshItems = true;
		super.activate();
	}

	public void setExpressionProvider(IExpressionProvider provider) {
		this.provider = provider;

	}

	static DataSetHandle getDataSetHandle(DesignElementHandle handle) {
		while (handle != null) {
			if (handle instanceof ReportItemHandle && ((ReportItemHandle) handle).getDataSet() != null) {
				return ((ReportItemHandle) handle).getDataSet();
			}
			handle = handle.getContainer();
		}
		return null;
	}

}
