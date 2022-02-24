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

package org.eclipse.birt.report.item.crosstab.ui.views.dialogs;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.ui.dialogs.BaseDialog;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.util.UIUtil;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.designer.util.FontManager;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabReportItemConstants;
import org.eclipse.birt.report.item.crosstab.core.de.ComputedMeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.internal.ui.util.CrosstabUIHelper;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.provider.GrandTotalProvider.GrandTotalInfo;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.elements.interfaces.IMeasureModel;
import org.eclipse.jface.resource.JFaceColors;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * 
 */

public class CrosstabGrandTotalDialog extends BaseDialog {

	public final static String TITLE = Messages.getString("CrosstabGrandTotalDialog.Title"); //$NON-NLS-1$

	protected String[] FUNCTION_LIST_ARRAY;

	protected Combo dataFieldCombo;

	protected GrandTotalInfo input;

	protected CrosstabReportItemHandle reportItemHandle;

	private List measures;

	private int axis;

	protected CrosstabGrandTotalDialog(String title) {
		this(UIUtil.getDefaultShell(), title);
		// TODO Auto-generated constructor stub
	}

	protected CrosstabGrandTotalDialog(Shell parentShell, String title) {
		super(parentShell, title);
	}

	public CrosstabGrandTotalDialog(CrosstabReportItemHandle reportItem, int axis) {
		this(TITLE);
		this.reportItemHandle = reportItem;
		this.axis = axis;
	}

	private CommandStack getActionStack() {
		return SessionHandleAdapter.getInstance().getCommandStack();
	}

	protected void okPressed() {
		CommandStack stack = getActionStack();

		if (input == null) {
			stack.startTrans(Messages.getString("FormPage.Menu.ModifyProperty")); //$NON-NLS-1$
			List measureList = new ArrayList();
			List functionList = new ArrayList();
			measureList.add(getMeasureViewHandle(dataFieldCombo.getText()));
			functionList.add(getFunctionNames()[0]);
			try {
				CrosstabCellHandle cellHandle = reportItemHandle.addGrandTotal(axis, measureList, functionList);
				if (cellHandle != null)
					CrosstabUIHelper.createGrandTotalLabel(cellHandle);
				stack.commit();
			} catch (SemanticException e) {
				stack.rollback();
			}

		}
		super.okPressed();
	}

	public void setInput(GrandTotalInfo grandTotalInfo) {
		this.input = grandTotalInfo;
	}

	protected void iniValue() {
		ExtendedItemHandle extendedItem = (ExtendedItemHandle) reportItemHandle.getModelHandle();

		measures = extendedItem.getPropertyHandle(ICrosstabReportItemConstants.MEASURES_PROP).getContents();

		if (input == null) {
			List measureNames = new ArrayList();
			for (int i = 0; i < measures.size(); i++) {
				ExtendedItemHandle extHandle = (ExtendedItemHandle) measures.get(i);
				MeasureViewHandle measureViewHandle = null;
				try {
					measureViewHandle = (MeasureViewHandle) extHandle.getReportItem();
					if (measureViewHandle instanceof ComputedMeasureViewHandle) {
						continue;
					}
					if (!inGrandTotalList(reportItemHandle, measureViewHandle)) {
						measureNames.add(measureViewHandle.getCubeMeasureName());
					}
				} catch (ExtendedElementException e1) {
					ExceptionUtil.handle(e1);
				}
			}
			String[] names = new String[measureNames.size()];
			measureNames.toArray(names);
			dataFieldCombo.setItems(names);
			dataFieldCombo.select(0);
		} else {
			dataFieldCombo.add(input.getMeasureName());
			dataFieldCombo.select(0);
		}
		GridData dataFieldGd = (GridData) dataFieldCombo.getLayoutData();
		int width = dataFieldCombo.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
		dataFieldGd.widthHint = width > dataFieldGd.widthHint ? width : dataFieldGd.widthHint;
		dataFieldCombo.setLayoutData(dataFieldGd);
		dataFieldCombo.getParent().layout();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#createContents(org.eclipse.swt.widgets.
	 * Composite)
	 */
	protected Control createContents(Composite parent) {
		GridData gdata;
		GridLayout glayout;
		Composite contents = new Composite(parent, SWT.NONE);
		contents.setLayout(new GridLayout());
		contents.setLayoutData(new GridData(GridData.FILL_BOTH));

		createTitleArea(contents);

		Composite composite = new Composite(contents, SWT.NONE);
		glayout = new GridLayout();
		glayout.marginHeight = 0;
		glayout.marginWidth = 0;
		glayout.verticalSpacing = 0;
		composite.setLayout(glayout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		applyDialogFont(composite);
		initializeDialogUnits(composite);

		Composite innerParent = (Composite) createDialogArea(composite);
		createButtonBar(composite);

		createGrandTotalContent(innerParent);

		Composite space = new Composite(innerParent, SWT.NONE);
		gdata = new GridData(GridData.FILL_HORIZONTAL);
		gdata.minimumWidth = 200;
		gdata.heightHint = 10;
		space.setLayoutData(gdata);

		iniValue();
		updateButtons();

		return composite;
	}

	protected void createGrandTotalContent(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GridLayout glayout = new GridLayout(2, false);
		container.setLayout(glayout);

		Label lb = new Label(container, SWT.NONE);
		lb.setText(Messages.getString("CrosstabGrandTotalDialog.Text.DataField")); //$NON-NLS-1$

		dataFieldCombo = new Combo(container, SWT.BORDER | SWT.READ_ONLY);
		GridData gdata = new GridData(GridData.FILL_HORIZONTAL);
		gdata.minimumWidth = 140;
		dataFieldCombo.setLayoutData(gdata);
		dataFieldCombo.setVisibleItemCount(30);
		dataFieldCombo.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				updateButtons();
			}
		});

	}

	private Composite createTitleArea(Composite parent) {
		int heightMargins = 3;
		int widthMargins = 8;
		final Composite titleArea = new Composite(parent, SWT.NONE);
		FormLayout layout = new FormLayout();
		layout.marginHeight = heightMargins;
		layout.marginWidth = widthMargins;
		titleArea.setLayout(layout);

		Display display = parent.getDisplay();
		Color background = JFaceColors.getBannerBackground(display);
		GridData layoutData = new GridData(GridData.FILL_HORIZONTAL);
		layoutData.heightHint = 20 + (heightMargins * 2);
		titleArea.setLayoutData(layoutData);
		titleArea.setBackground(background);

		titleArea.addPaintListener(new PaintListener() {

			public void paintControl(PaintEvent e) {
				e.gc.setForeground(titleArea.getDisplay().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));
				Rectangle bounds = titleArea.getClientArea();
				bounds.height = bounds.height - 2;
				bounds.width = bounds.width - 1;
				e.gc.drawRectangle(bounds);
			}
		});

		Label label = new Label(titleArea, SWT.NONE);
		label.setBackground(background);
		label.setFont(FontManager.getFont(label.getFont().toString(), 10, SWT.BOLD));
		label.setText(getTitle());
		UIUtil.bindHelp(parent, IHelpContextIds.INSERT_EDIT_GRAND_TOTAL_DIALOG_ID);
		return titleArea;
	}

	/**
	 * Refreshes the OK button state.
	 * 
	 */
	protected void updateButtons() {
		getOkButton().setEnabled(isConditionOK());
	}

	private boolean isConditionOK() {
		if (dataFieldCombo.getSelectionIndex() == -1) {
			return false;
		}
		return true;
	}

	private MeasureViewHandle getMeasureViewHandle(String measureName) {
		MeasureViewHandle retValue = null;
		for (int i = 0; i < measures.size(); i++) {
			ExtendedItemHandle extHandle = (ExtendedItemHandle) measures.get(i);
			MeasureViewHandle measureViewHandle = null;
			try {
				measureViewHandle = (MeasureViewHandle) extHandle.getReportItem();
			} catch (ExtendedElementException e1) {
				// TODO Auto-generated catch block
				logger.log(Level.SEVERE, e1.getMessage(), e1);
			}

			if (measureViewHandle.getCubeMeasureName().equals(measureName)) {
				retValue = measureViewHandle;
				break;
			}
		}

		return retValue;
	}

	private String[] getFunctionDisplayNames() {
		IChoice[] choices = getFunctions();
		if (choices == null)
			return new String[0];

		String[] displayNames = new String[choices.length];
		for (int i = 0; i < choices.length; i++) {
			displayNames[i] = choices[i].getDisplayName();
		}
		return displayNames;

	}

	private String[] getFunctionNames() {
		IChoice[] choices = getFunctions();
		if (choices == null)
			return new String[0];

		String[] displayNames = new String[choices.length];
		for (int i = 0; i < choices.length; i++) {
			displayNames[i] = choices[i].getName();
		}
		return displayNames;
	}

	private String getFunctionDisplayName(String name) {
		return ChoiceSetFactory.getDisplayNameFromChoiceSet(name,
				DEUtil.getMetaDataDictionary().getElement(ReportDesignConstants.MEASURE_ELEMENT)
						.getProperty(IMeasureModel.FUNCTION_PROP).getAllowedChoices());
	}

	private IChoice[] getFunctions() {
		return DEUtil.getMetaDataDictionary().getElement(ReportDesignConstants.MEASURE_ELEMENT)
				.getProperty(IMeasureModel.FUNCTION_PROP).getAllowedChoices().getChoices();
	}

	private boolean inGrandTotalList(CrosstabReportItemHandle reportHandle, MeasureViewHandle speHandle) {
		List measures = reportHandle.getAggregationMeasures(axis);
		for (int i = 0; i < measures.size(); i++) {
			MeasureViewHandle measureViewHandle = (MeasureViewHandle) measures.get(i);
			if (measureViewHandle.getCubeMeasure() == speHandle.getCubeMeasure()) {
				return true;
			}

		}
		return false;

	}
}
