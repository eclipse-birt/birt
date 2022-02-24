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

package org.eclipse.birt.report.item.crosstab.ui.views.attributes.widget;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.data.adapter.api.DimensionLevel;
import org.eclipse.birt.report.designer.data.ui.util.CubeValueSelector;
import org.eclipse.birt.report.designer.internal.ui.data.DataService;
import org.eclipse.birt.report.designer.ui.dialogs.IExpressionProvider;
import org.eclipse.birt.report.designer.ui.dialogs.SelectValueDialog;
import org.eclipse.birt.report.designer.ui.preferences.PreferenceFactory;
import org.eclipse.birt.report.designer.ui.util.UIUtil;
import org.eclipse.birt.report.designer.ui.widget.PopupSelectionList;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.plugin.CrosstabPlugin;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.MemberValueHandle;
import org.eclipse.birt.report.model.api.ParamBindingHandle;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
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
 * @version $Revision: 1.24 $ $Date: 2010/08/02 09:22:22 $
 */
public class ExpressionValueCellEditor extends CellEditor {

	protected static final Logger logger = Logger.getLogger(ExpressionValueCellEditor.class.getName());

	private static String[] actions = new String[] { Messages.getString("ExpressionValueCellEditor.selectValueAction") //$NON-NLS-1$
	};

	private transient ParamBindingHandle[] bindingParams = null;
	private MemberValueHandle memberValue;
	private transient Text expressionText;
	private transient Button btnPopup;
	private transient ExtendedItemHandle currentItem = null;
	private transient String[] popupItems = null;
	private transient boolean refreshItems = true;
	private List referencedLevelList;
	private static String[] EMPTY_ARRAY = new String[] {};

	public void setMemberValue(MemberValueHandle memberValue) {
		this.memberValue = memberValue;
	}

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
	 * 
	 */
	public ExpressionValueCellEditor() {
		super();
	}

	/**
	 * @param parent
	 */
	public ExpressionValueCellEditor(Composite parent) {
		super(parent);
	}

	public ExpressionValueCellEditor(Composite parent, boolean useDataSetFilter) {
		super(parent);
	}

	/**
	 * @param parent
	 * @param style
	 */
	public ExpressionValueCellEditor(Composite parent, int style) {
		super(parent, style);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.CellEditor#createControl(org.eclipse.swt.widgets
	 * .Composite)
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
			 * @see org.eclipse.swt.events.FocusAdapter#focusLost(org.eclipse.swt
			 * .events.FocusEvent)
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
						// This action will update later.
						List valueList = getSelectMemberValueList();
						if (valueList == null || valueList.size() == 0) {
							MessageDialog.openInformation(null, Messages.getString("SelectValueDialog.selectValue"), //$NON-NLS-1$
									Messages.getString("SelectValueDialog.messages.info.selectVauleUnavailable")); //$NON-NLS-1$
						} else {
							SelectValueDialog dialog = new SelectValueDialog(
									PlatformUI.getWorkbench().getDisplay().getActiveShell(),
									Messages.getString("ExpressionValueCellEditor.title")); //$NON-NLS-1$
							dialog.setSelectedValueList(valueList);

							if (dialog.open() == IDialogConstants.OK_ID) {
								newValue = DEUtil.removeQuote(dialog.getSelectedExprValue());
							}
						}
					} else if (selectionIndex > 3) {
						// newValue = "params[\"" + value + "\"]"; //$NON-NLS-1$
						// //$NON-NLS-2$
						newValue = ExpressionUtil.createJSParameterValueExpression(value);
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
	}

	public void setReportElement(ExtendedItemHandle reportItem) {
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

	}

	public void setReferencedLevelList(List referencedLevelList) {
		this.referencedLevelList = referencedLevelList;
	}

	private CubeHandle getCubeHandle() {
		CrosstabReportItemHandle crosstab = null;
		if (currentItem != null) {
			try {
				crosstab = (CrosstabReportItemHandle) currentItem.getReportItem();
				return crosstab.getCube();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				logger.log(Level.SEVERE, e.getMessage(), e);
			}
		}
		return null;
	}

	private List getExistValueList() {
		List valueList = new ArrayList();
		MemberValueHandle tmpMemberValue = memberValue;
		while (true) {
			Object container = tmpMemberValue.getContainer();
			if (container == null || (!(container instanceof MemberValueHandle))) {
				break;
			}
			tmpMemberValue = (MemberValueHandle) container;
			valueList.add(0, tmpMemberValue);
		}

		return valueList;
	}

	private List getSelectMemberValueList() {
		// get CubeHandle
		CubeHandle cube = getCubeHandle();

		// getValueList
		List valueList = new ArrayList();
		List extValueList = getExistValueList();
		for (int i = 0; i < extValueList.size(); i++) {
			MemberValueHandle tmpMemberValue = (MemberValueHandle) extValueList.get(i);
			String value = tmpMemberValue.getValue();
			if (value == null || value.length() == 0) {
				// assert all the parent have values.
				return new ArrayList();
			}

			valueList.add(value);
		}
		Object[] values = valueList.toArray(new Object[valueList.size()]);
		if (values.length == 0) {
			values = null;
		}

		// get List of ILevelDefinition
		DimensionLevel levelDens[] = null;
		if (values != null) {
			levelDens = new DimensionLevel[values.length];
			for (int i = 0; i < values.length; i++) {
				Object obj = referencedLevelList.get(i);
				if (obj == null || (!(obj instanceof DimensionLevel))) {
					return new ArrayList();
				}
				levelDens[i] = (DimensionLevel) obj;
			}
		} else {
			levelDens = null;
		}

		// get Level;
		String targetLevel = null;
		int index = 0;
		if (values != null && values.length > 0 && values.length + 1 <= referencedLevelList.size()) {
			index = values.length;
		}

		DimensionLevel levelDefn = (DimensionLevel) referencedLevelList.get(index);

		String levelName = levelDefn.getLevelName();
		String dimensionName = levelDefn.getDimensionName();
		targetLevel = ExpressionUtil.createJSDimensionExpression(dimensionName, levelName);

		// validate value
		if (cube == null || (targetLevel == null || targetLevel.length() == 0)) {
			return new ArrayList();
		}

		// get value iterator
		Iterator iter = null;
		DataRequestSession session = null;
		try {
			session = DataRequestSession
					.newSession(new DataSessionContext(DataSessionContext.MODE_DIRECT_PRESENTATION));
			DataService.getInstance().registerSession(cube, session);
			iter = CubeValueSelector.getMemberValueIterator(session, cube, targetLevel, levelDens, values);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.log(Level.SEVERE, e.getMessage(), e);
		}

		// iterator to list
		List retList = new ArrayList();
		int count = 0;
		int MAX_COUNT = PreferenceFactory.getInstance()
				.getPreferences(CrosstabPlugin.getDefault(), UIUtil.getCurrentProject())
				.getInt(CrosstabPlugin.PREFERENCE_FILTER_LIMIT);
		while (iter != null && iter.hasNext()) {
			Object obj = iter.next();
			if (obj != null) {
				if (retList.indexOf(obj) < 0) {
					retList.add(obj);
					if (++count >= MAX_COUNT) {
						break;
					}
				}

			}

		}
		if (session != null) {
			session.shutdown();
		}
		return retList;
	}

}
