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
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.internal.ui.util.CrosstabUIHelper;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.provider.SubTotalProvider.SubTotalInfo;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.elements.interfaces.IMeasureModel;
import org.eclipse.jface.resource.JFaceColors;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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

public class CrosstabSubTotalDialog extends BaseDialog {

	protected Combo dataFieldCombo, levelCombo;
	protected CrosstabReportItemHandle reportItemHandle;
	private List measures = new ArrayList();
	protected int axis;
	protected String[] FUNCTION_LIST_ARRAY;
	protected String[] measureNames;
	protected LevelViewHandle lastLevelView;

	protected SubTotalInfo input;

	public final static String TITLE = Messages.getString("SubTotalDialog.Title"); //$NON-NLS-1$

	public void setInput(SubTotalInfo subTotalInfo) {
		this.input = subTotalInfo;
	}

	protected String[] getAllLevelNames(CrosstabReportItemHandle crosstab) {
		List list = new ArrayList();
		CrosstabViewHandle crosstabView = crosstab.getCrosstabView(axis);
		if (crosstabView == null)
			return new String[0];
		int dimCount = crosstabView.getDimensionCount();
		for (int i = 0; i < dimCount; i++) {
			DimensionViewHandle dimensionView = crosstabView.getDimension(i);
			int levelCount = dimensionView.getLevelCount();
			for (int j = 0; j < levelCount; j++) {
				list.add(dimensionView.getLevel(j).getCubeLevelName());
			}
		}

		if (lastLevelView == null) {
			DimensionViewHandle lastDimensionView = crosstabView.getDimension(dimCount - 1);
			lastLevelView = lastDimensionView.getLevel(lastDimensionView.getLevelCount() - 1);
		}

		list.remove(lastLevelView.getCubeLevelName());

		return (String[]) list.toArray(new String[list.size()]);
	}

	protected LevelViewHandle getLevelFromName(String crosstabName) {
		if (crosstabName == null || crosstabName.length() <= 0) {
			return null;
		}
		CrosstabViewHandle crosstabView = reportItemHandle.getCrosstabView(axis);
		int dimCount = crosstabView.getDimensionCount();
		for (int i = 0; i < dimCount; i++) {
			DimensionViewHandle dimensionView = crosstabView.getDimension(i);
			int levelCount = dimensionView.getLevelCount();
			for (int j = 0; j < levelCount; j++) {
				if (dimensionView.getLevel(j).getCubeLevelName().equals(crosstabName)) {
					return dimensionView.getLevel(j);
				}
			}
		}
		return null;
	}

	protected boolean isConditionOK() {
		LevelViewHandle level = getLevelFromName(levelCombo.getText());
		if (level == null || level == lastLevelView) {
			return false;
		}

		if (dataFieldCombo.getText().length() <= 0) {
			return false;
		}

		//
		// SubTotalInfo newSubtTotalInfo = new SubTotalInfo( );
		// newSubtTotalInfo.setMeasure( getMeasure( ) );
		// newSubtTotalInfo.setLevel( getLevel( ) );
		// newSubtTotalInfo.setFunction( getFunction( ) );
		// if ( input == null
		// || ( input.getMeasure( ) != newSubtTotalInfo.getMeasure( ) ||
		// input.getLevel( ) != newSubtTotalInfo.getLevel( ) ) )
		// {
		// if ( inSubtotalList( reportItemHandle, newSubtTotalInfo ) )
		// {
		// return false;
		// }
		// }

		return true;
	}

	public LevelViewHandle getLevel() {
		return getLevelFromName(levelCombo.getText());
	}

	public MeasureViewHandle getMeasure() {
		if (dataFieldCombo.getText().length() <= 0) {
			return null;
		}
		return (MeasureViewHandle) measures.get(dataFieldCombo.getSelectionIndex());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.provider
	 * .IFormProvider#getElements(java.lang.Object)
	 */
	public boolean inSubtotalList(CrosstabReportItemHandle reportItem, SubTotalInfo subTotal) {
		CrosstabReportItemHandle crossTab = reportItem;
		CrosstabViewHandle crosstabView = crossTab.getCrosstabView(axis);

		int dimensionCount = crosstabView.getDimensionCount();

		for (int i = 0; i < dimensionCount; i++) {
			DimensionViewHandle dimension = crosstabView.getDimension(i);
			int levelCount = dimension.getLevelCount();
			for (int j = 0; j < levelCount; j++) {
				LevelViewHandle levelHandle = dimension.getLevel(j);
				if (subTotal.getLevel() != levelHandle) {
					continue;
				}
				List aggMeasures = levelHandle.getAggregationMeasures();
				for (int k = 0; k < aggMeasures.size(); k++) {
					MeasureViewHandle measure = (MeasureViewHandle) aggMeasures.get(k);
					if (measure.getCubeMeasureName().equals(subTotal.getMeasureName())) {
						return true;
					}
				}

			}
		}

		return false;
	}

	/**
	 * Refreshes the OK button state.
	 * 
	 */
	protected void updateButtons() {
		getOkButton().setEnabled(isConditionOK());
	}

	protected SelectionListener updateButtonListener = new SelectionListener() {

		public void widgetDefaultSelected(SelectionEvent e) {
		}

		public void widgetSelected(SelectionEvent e) {
			if (e.widget == levelCombo)
				updateMeasures();
			updateButtons();
		}

	};

	protected CrosstabSubTotalDialog(String title) {
		this(UIUtil.getDefaultShell(), title);
	}

	protected void updateMeasures() {
		ExtendedItemHandle extendedItem = (ExtendedItemHandle) reportItemHandle.getModelHandle();
		List tmpMeasures = extendedItem.getPropertyHandle(ICrosstabReportItemConstants.MEASURES_PROP).getContents();
		measures = new ArrayList();
		List measureNames = new ArrayList();
		for (int i = 0; i < tmpMeasures.size(); i++) {
			ExtendedItemHandle extHandle = (ExtendedItemHandle) tmpMeasures.get(i);
			MeasureViewHandle measureViewHandle = null;
			try {
				measureViewHandle = (MeasureViewHandle) extHandle.getReportItem();
			} catch (ExtendedElementException e1) {
				ExceptionUtil.handle(e1);
			}

			if (measureViewHandle instanceof ComputedMeasureViewHandle) {
				continue;
			}
			boolean flag = true;
			// TODO:Edit
			if (input != null) {
				if (!measureViewHandle.getCubeMeasureName().equals(input.getMeasureName())) {
					List aggMeasures = getLevel().getAggregationMeasures();
					for (int k = 0; k < aggMeasures.size(); k++) {
						MeasureViewHandle measure = (MeasureViewHandle) aggMeasures.get(k);
						if (measureViewHandle == measure) {
							flag = false;
							break;
						}
					}
				}
			}
			// TODO:New
			else {
				if (getLevel() != null) {
					List aggMeasures = getLevel().getAggregationMeasures();
					for (int k = 0; k < aggMeasures.size(); k++) {
						MeasureViewHandle measure = (MeasureViewHandle) aggMeasures.get(k);
						if (measureViewHandle == measure) {
							flag = false;
							break;
						}
					}
				}
			}
			if (flag) {
				measures.add(measureViewHandle);
				measureNames.add(measureViewHandle.getCubeMeasureName());
			}
		}

		String[] items = new String[measures.size()];
		measureNames.toArray(items);
		String measure = dataFieldCombo.getText();
		dataFieldCombo.setItems(items);
		if (measure != null && measureNames.contains(measure))
			dataFieldCombo.setText(measure);
		else if (items.length > 0)
			dataFieldCombo.select(0);
	}

	protected CrosstabSubTotalDialog(Shell parentShell, String title) {
		super(parentShell, title);
	}

	public CrosstabSubTotalDialog(CrosstabReportItemHandle reportItem, int axis) {
		this(TITLE);
		this.reportItemHandle = reportItem;
		this.axis = axis;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#createContents(org.eclipse.swt.widgets
	 * .Composite)
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

		createSubTotalContent(innerParent);

		Composite space = new Composite(innerParent, SWT.NONE);
		gdata = new GridData(GridData.FILL_HORIZONTAL);
		gdata.widthHint = 200;
		gdata.heightHint = 10;
		space.setLayoutData(gdata);

		Label lb = new Label(innerParent, SWT.SEPARATOR | SWT.HORIZONTAL);
		lb.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		iniValue();
		updateButtons();
		UIUtil.bindHelp(parent, IHelpContextIds.INSERT_EDIT_SUB_TOTAL_DIALOG_ID);
		return composite;
	}

	protected String[] filterLevels(String levels[]) {
		ExtendedItemHandle extendedItem = (ExtendedItemHandle) reportItemHandle.getModelHandle();
		List tmpMeasures = extendedItem.getPropertyHandle(ICrosstabReportItemConstants.MEASURES_PROP).getContents();
		int measureCount = tmpMeasures.size();
		for (int i = 0; i < tmpMeasures.size(); i++) {
			ExtendedItemHandle extHandle = (ExtendedItemHandle) tmpMeasures.get(i);
			MeasureViewHandle measureViewHandle = null;
			try {
				measureViewHandle = (MeasureViewHandle) extHandle.getReportItem();
			} catch (ExtendedElementException e1) {
				ExceptionUtil.handle(e1);
			}

			if (measureViewHandle instanceof ComputedMeasureViewHandle) {
				measureCount--;
			}
		}
		List<String> levelList = new ArrayList<String>();
		for (int i = 0; i < levels.length; i++) {
			LevelViewHandle level = getLevelFromName(levels[i]);
			if (level == null) {
				continue;
			}
			int count = level.getAggregationMeasures().size();
			if (count < measureCount) {
				levelList.add(levels[i]);
				continue;
			}

			if (input != null && input.getLevel() == level) {
				levelList.add(levels[i]);
			}

		}

		return levelList.toArray(new String[] {});
	}

	protected void iniValue() {
		if (input == null) {
			String levels[] = getAllLevelNames(reportItemHandle);
			levels = filterLevels(levels);
			levelCombo.setItems(levels);
			levelCombo.select(0);
			updateMeasures();
		} else {
			levelCombo.add(input.getLevel().getCubeLevelName());
			levelCombo.select(0);
			dataFieldCombo.add(input.getMeasureName());
			dataFieldCombo.select(0);
		}

//		GridData dataFieldGd = (GridData) dataFieldCombo.getLayoutData( );
//		GridData functionGd = (GridData) functionCombo.getLayoutData( );
//		GridData levelGd = (GridData) levelCombo.getLayoutData( );
//		int width = dataFieldCombo.computeSize( SWT.DEFAULT, SWT.DEFAULT ).x;
//		dataFieldGd.widthHint = width > dataFieldGd.widthHint ? width
//				: dataFieldGd.widthHint;
//		width = levelCombo.computeSize( SWT.DEFAULT, SWT.DEFAULT ).x;
//		levelGd.widthHint = width > dataFieldGd.widthHint ? width
//				: dataFieldGd.widthHint;
//		if ( dataFieldGd.widthHint > functionGd.widthHint
//				&& dataFieldGd.widthHint > levelGd.widthHint )
//		{
//			levelGd.widthHint = functionGd.widthHint = dataFieldGd.widthHint;
//		}
//		else if ( functionGd.widthHint > dataFieldGd.widthHint
//				&& functionGd.widthHint > levelGd.widthHint )
//		{
//			levelGd.widthHint = dataFieldGd.widthHint = functionGd.widthHint;
//		}
//		else
//		{
//			dataFieldGd.widthHint = functionGd.widthHint = levelGd.widthHint;
//		}
//		dataFieldCombo.setLayoutData( dataFieldGd );
//		functionCombo.setLayoutData( functionGd );
//		levelCombo.setLayoutData( functionGd );
//		dataFieldCombo.getParent( ).layout( );

		updateButtons();
	};

	protected void createSubTotalContent(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GridLayout glayout = new GridLayout(2, false);
		container.setLayout(glayout);

		Label lb = new Label(container, SWT.NONE);
		lb.setText(Messages.getString("SubTotalDialog.Text.AggregateOn")); //$NON-NLS-1$
		levelCombo = new Combo(container, SWT.BORDER | SWT.READ_ONLY);
		GridData gdata = new GridData(GridData.FILL_HORIZONTAL);
		gdata.widthHint = 120;
		levelCombo.setLayoutData(gdata);
		levelCombo.setVisibleItemCount(30);
		levelCombo.addSelectionListener(updateButtonListener);

		lb = new Label(container, SWT.NONE);
		lb.setText(Messages.getString("SubTotalDialog.Text.DataField")); //$NON-NLS-1$

		dataFieldCombo = new Combo(container, SWT.BORDER | SWT.READ_ONLY);
		gdata = new GridData(GridData.FILL_HORIZONTAL);
		gdata.minimumWidth = 140;
		dataFieldCombo.setLayoutData(gdata);
		dataFieldCombo.setVisibleItemCount(30);
		dataFieldCombo.addSelectionListener(updateButtonListener);

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

		return titleArea;
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

	private CommandStack getActionStack() {
		return SessionHandleAdapter.getInstance().getCommandStack();
	}

	protected void okPressed() {
		CommandStack stack = getActionStack();

		if (input == null) {
			stack.startTrans(Messages.getString("FormPage.Menu.ModifyProperty")); //$NON-NLS-1$
			List measureList = new ArrayList();
			List functionList = new ArrayList();
			measureList.addAll(getLevel().getAggregationMeasures());
			functionList.add(getFunctionNames()[0]);
			for (int i = 0; i < measureList.size(); i++) {
				functionList.add(getLevel().getAggregationFunction((MeasureViewHandle) measureList.get(i)));
			}
			measureList.add(getMeasure());

			try {
				// remove first, and then add
				getLevel().removeSubTotal();

				CrosstabCellHandle cellHandle = getLevel().addSubTotal(measureList, functionList);
				if (cellHandle != null)
					CrosstabUIHelper.createSubTotalLabel(getLevel(), cellHandle);
				stack.commit();
			} catch (SemanticException e) {
				stack.rollback();
			}

		}

		super.okPressed();
	}

}
