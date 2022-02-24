/***********************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.ui.swt.wizard.format.chart;

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.chart.model.attribute.LegendBehaviorType;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.component.impl.LabelImpl;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.util.ChartDefaultValueUtil;
import org.eclipse.birt.chart.model.util.ChartElementUtil;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.ChartCheckbox;
import org.eclipse.birt.chart.ui.swt.ChartCombo;
import org.eclipse.birt.chart.ui.swt.composites.ExternalizedTextEditorComposite;
import org.eclipse.birt.chart.ui.swt.composites.TriggerDataComposite;
import org.eclipse.birt.chart.ui.swt.interfaces.ITaskPopupSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.SubtaskSheetImpl;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.InteractivitySheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.chart.LegendLayoutSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.chart.LegendTextSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.chart.LegendTitleSheet;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.util.LiteralHelper;
import org.eclipse.birt.chart.util.NameSet;
import org.eclipse.birt.chart.util.TriggerSupportMatrix;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

/**
 * Legend subtask
 * 
 */
public class ChartLegendSheetImpl extends SubtaskSheetImpl implements Listener, SelectionListener {

	protected ChartCheckbox btnVisible;

	protected ExternalizedTextEditorComposite txtTitle;

	protected ChartCheckbox btnTitleVisible;

	protected ChartCheckbox btnShowValue;

	protected Label lblTitle;

	protected Label lblShowValue;

	protected Label lblLegendBehavior;

	protected ChartCombo cmbLegendBehavior;

	public void createControl(Composite parent) {
		ChartUIUtil.bindHelp(parent, ChartHelpContextIds.SUBTASK_LEGEND);

		init();

		cmpContent = new Composite(parent, SWT.NONE);
		{
			GridLayout glContent = new GridLayout(2, false);
			cmpContent.setLayout(glContent);
			GridData gd = new GridData(GridData.FILL_BOTH);
			cmpContent.setLayoutData(gd);
		}

		Group cmpBasic = new Group(cmpContent, SWT.NONE);
		{
			GridLayout layout = new GridLayout(4, false);
			layout.marginWidth = 10;
			layout.marginHeight = 10;
			cmpBasic.setLayout(layout);
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			cmpBasic.setLayoutData(gd);
			cmpBasic.setText(Messages.getString("ChartLegendSheetImpl.Label.Legend")); //$NON-NLS-1$
		}

		Legend defLegend = ChartDefaultValueUtil.getDefaultValueChart(getChart()).getLegend();
		btnVisible = getContext().getUIFactory().createChartCheckbox(cmpBasic, SWT.NONE, defLegend.isVisible());
		{
			GridData gdBTNVisible = new GridData();
			gdBTNVisible.horizontalSpan = 4;
			btnVisible.setLayoutData(gdBTNVisible);
			btnVisible.setText(Messages.getString("Shared.mne.Visibile_v"));//$NON-NLS-1$
		}

		createTitleComposite(cmpBasic, defLegend);

		lblLegendBehavior = new Label(cmpBasic, SWT.NONE);
		{
			lblLegendBehavior.setText(Messages.getString("ChartLegendSheetImpl.Label.LegendBehaviorType")); //$NON-NLS-1$
		}

		cmbLegendBehavior = getContext().getUIFactory().createChartCombo(cmpBasic, SWT.DROP_DOWN | SWT.READ_ONLY,
				getChart().getInteractivity(), "legendBehavior", //$NON-NLS-1$
				ChartDefaultValueUtil.getDefaultValueChart(getChart()).getInteractivity().getLegendBehavior()
						.getName());
		{
			GridData gridData = new GridData();
			gridData.widthHint = 180;
			gridData.horizontalSpan = 2;
			cmbLegendBehavior.setLayoutData(gridData);
			cmbLegendBehavior.addSelectionListener(this);
			cmbLegendBehavior.setEnabled(getChart().getInteractivity().isEnable());
		}

		new Label(cmpBasic, SWT.NONE);

		if (isShowValueEnabled()) {
			lblShowValue = new Label(cmpBasic, SWT.NONE);
			lblShowValue.setText(Messages.getString("ChartLegendSheetImpl.Label.Value")); //$NON-NLS-1$

			btnShowValue = getContext().getUIFactory().createChartCheckbox(cmpBasic, SWT.NONE, defLegend.isShowValue());
			{
				GridData gdBTNVisible = new GridData();
				gdBTNVisible.horizontalSpan = 2;
				btnShowValue.setLayoutData(gdBTNVisible);
				btnShowValue.setText(Messages.getString("ChartLegendSheetImpl.Label.ShowValue")); //$NON-NLS-1$
				btnShowValue.setToolTipText(Messages.getString("ChartLegendSheetImpl.Tooltip.ShowDataPointValue")); //$NON-NLS-1$
			}
		}

		populateLists();
		initDataNListeners();
		createButtonGroup(cmpContent);
		setState(!getContext().getUIFactory().isSetInvisible(getChart().getLegend()));
	}

	protected void createTitleComposite(Group cmpBasic, Legend defLegend) {
		lblTitle = new Label(cmpBasic, SWT.NONE);
		lblTitle.setText(Messages.getString("ChartLegendSheetImpl.Label.Title")); //$NON-NLS-1$

		List<String> keys = null;
		if (getContext().getUIServiceProvider() != null) {
			keys = getContext().getUIServiceProvider().getRegisteredKeys();
		}
		txtTitle = new ExternalizedTextEditorComposite(cmpBasic, SWT.BORDER, -1, -1, keys,
				getContext().getUIServiceProvider(), getLegendTitle());
		{
			GridData gd = new GridData();
			gd.widthHint = 180;
			txtTitle.setLayoutData(gd);
			txtTitle.addListener(this);
		}

		btnTitleVisible = getContext().getUIFactory().createChartCheckbox(cmpBasic, SWT.NONE,
				defLegend.getTitle().isVisible());
		GridData gd = new GridData();
		gd.horizontalSpan = 2;
		btnTitleVisible.setLayoutData(gd);
		btnTitleVisible.setText(Messages.getString("Shared.mne.Visibile_s")); //$NON-NLS-1$
	}

	protected void initDataNListeners() {
		Legend l = getChart().getLegend();
		int state = l.isSetVisible() ? (l.isVisible() ? ChartCheckbox.STATE_SELECTED : ChartCheckbox.STATE_UNSELECTED)
				: ChartCheckbox.STATE_GRAYED;
		btnVisible.setSelectionState(state);
		btnVisible.addSelectionListener(this);

		state = l.getTitle().isSetVisible()
				? (l.getTitle().isVisible() ? ChartCheckbox.STATE_SELECTED : ChartCheckbox.STATE_UNSELECTED)
				: ChartCheckbox.STATE_GRAYED;
		btnTitleVisible.setSelectionState(state);
		btnTitleVisible.addSelectionListener(this);

		if (isShowValueEnabled()) {
			state = l.isSetShowValue()
					? (l.isShowValue() ? ChartCheckbox.STATE_SELECTED : ChartCheckbox.STATE_UNSELECTED)
					: ChartCheckbox.STATE_GRAYED;
			btnShowValue.addSelectionListener(this);
			btnShowValue.setSelectionState(state);
		}
	}

	protected void populateLists() {
		NameSet nameSet = LiteralHelper.legendBehaviorTypeSet;
		if (isBehaviorSupported()) {
			cmbLegendBehavior.setItems(nameSet.getDisplayNames());
			cmbLegendBehavior.setItemData(nameSet.getNames());
			cmbLegendBehavior.setSelection(getChart().getInteractivity().getLegendBehavior().getName());
		} else {
			cmbLegendBehavior.setItems(new String[] { nameSet.getDisplayNames()[0] });
			cmbLegendBehavior.setItemData(new String[] { nameSet.getNames()[0] });
			cmbLegendBehavior.setSelection(getChart().getInteractivity().getLegendBehavior().getName());
		}
	}

	protected boolean isBehaviorSupported() {
		return "SVG".equalsIgnoreCase(getContext().getOutputFormat()); //$NON-NLS-1$
	}

	@SuppressWarnings("rawtypes")
	protected void setState(boolean enabled) {
		lblTitle.setEnabled(enabled);
		txtTitle.setEnabled(enabled && btnTitleVisible.getSelectionState() != ChartCheckbox.STATE_UNSELECTED);
		btnTitleVisible.setEnabled(enabled);
		if (isShowValueEnabled()) {
			lblShowValue.setEnabled(enabled);
			btnShowValue.setEnabled(enabled);
		}

		// Adjust the button selection according to visibility
		Iterator buttons = getToggleButtons().iterator();
		while (buttons.hasNext()) {
			Button toggle = (Button) buttons.next();
			toggle.setEnabled(enabled && getContext().isEnabled(SUBTASK_LEGEND + toggle.getData()));
		}
		setToggleButtonEnabled(BUTTON_TITLE,
				btnTitleVisible.getSelectionState() != ChartCheckbox.STATE_UNSELECTED && enabled);
		setToggleButtonEnabled(BUTTON_INTERACTIVITY, getChart().getInteractivity().isEnable() && enabled);
	}

	protected boolean isShowValueEnabled() {
		return getChart().getLegend().getItemType() == LegendItemType.SERIES_LITERAL;
	}

	private void init() {
		// Make it compatible with old model
		if (getChart().getLegend().getTitle() == null) {
			org.eclipse.birt.chart.model.component.Label label = LabelImpl.createDefault();
			label.eAdapters().addAll(getChart().getLegend().eAdapters());
			getChart().getLegend().setTitle(label);
		}

	}

	protected void createButtonGroup(Composite parent) {
		Composite cmp = new Composite(parent, SWT.NONE);
		{
			cmp.setLayout(new GridLayout(5, false));
			GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
			gridData.horizontalSpan = 2;
			gridData.grabExcessVerticalSpace = true;
			gridData.verticalAlignment = SWT.END;
			cmp.setLayoutData(gridData);
		}

		// Title
		ITaskPopupSheet popup = new LegendTitleSheet(Messages.getString("ChartLegendSheetImpl.Title.LegendTitle"), //$NON-NLS-1$
				getContext());
		Button btnLegendTitle = createToggleButton(cmp, BUTTON_TITLE,
				Messages.getString("ChartLegendSheetImpl.Label.LegendTitle&"), //$NON-NLS-1$
				popup, getTitleVisibleSelection());
		btnLegendTitle.addSelectionListener(this);

		// Layout
		popup = new LegendLayoutSheet(Messages.getString("ChartLegendSheetImpl.Title.LegendLayout"), getContext()); //$NON-NLS-1$
		Button btnAreaProp = createToggleButton(cmp, BUTTON_LAYOUT,
				Messages.getString("ChartLegendSheetImpl.Label.Layout"), popup); //$NON-NLS-1$
		btnAreaProp.addSelectionListener(this);

		// Entries
		createLegendEntriesUI(cmp);

		// Interactivity
		if (getContext().isInteractivityEnabled()) {
			popup = new InteractivitySheet(Messages.getString("ChartLegendSheetImpl.Label.Interactivity"), //$NON-NLS-1$
					getContext(), getChart().getLegend().getTriggers(), getChart().getLegend(),
					TriggerSupportMatrix.TYPE_LEGEND,
					TriggerDataComposite.ENABLE_URL_PARAMETERS | TriggerDataComposite.DISABLE_CATEGORY_SERIES
							| TriggerDataComposite.DISABLE_VALUE_SERIES
							| TriggerDataComposite.ENABLE_SHOW_TOOLTIP_VALUE);
			Button btnInteractivity = createToggleButton(cmp, BUTTON_INTERACTIVITY,
					Messages.getString("SeriesYSheetImpl.Label.Interactivity&"), //$NON-NLS-1$
					popup, getChart().getInteractivity().isEnable());
			btnInteractivity.addSelectionListener(this);
		}
	}

	protected void createLegendEntriesUI(Composite cmp) {
		ITaskPopupSheet popup;
		popup = new LegendTextSheet(Messages.getString("ChartLegendSheetImpl.Title.LegendEntries"), getContext()); //$NON-NLS-1$
		Button btnLegendText = createToggleButton(cmp, BUTTON_ENTRIES,
				Messages.getString("ChartLegendSheetImpl.Label.Entries"), popup); //$NON-NLS-1$
		btnLegendText.addSelectionListener(this);
	}

	public void handleEvent(Event event) {
		if (event.widget.equals(txtTitle)) {
			getChart().getLegend().getTitle().getCaption().setValue(txtTitle.getText());
		}
	}

	public void widgetSelected(SelectionEvent e) {
		// Detach popup dialog if there's selected button.
		if (detachPopup(e.widget)) {
			return;
		}

		if (isRegistered(e.widget)) {
			attachPopup(((Button) e.widget).getData().toString());
		}

		if (e.widget.equals(btnVisible)) {
			ChartElementUtil.setEObjectAttribute(getChart().getLegend(), "visible", //$NON-NLS-1$
					btnVisible.getSelectionState() == ChartCheckbox.STATE_SELECTED,
					btnVisible.getSelectionState() == ChartCheckbox.STATE_GRAYED);
			boolean enabled = (btnVisible.getSelectionState() != ChartCheckbox.STATE_UNSELECTED);
			// If legend is invisible, close popup
			if (!enabled && isButtonSelected()) {
				detachPopup();
			}
			// Adjust the UI according to visibility
			setState(enabled);
		} else if (e.widget.equals(btnTitleVisible)) {
			setToggleButtonEnabled(BUTTON_TITLE, getTitleVisibleSelection());
			int state = btnTitleVisible.getSelectionState();
			boolean enabled = state != ChartCheckbox.STATE_UNSELECTED;
			ChartElementUtil.setEObjectAttribute(getChart().getLegend().getTitle(), "visible", //$NON-NLS-1$
					state == ChartCheckbox.STATE_SELECTED, state == ChartCheckbox.STATE_GRAYED);
			txtTitle.setEnabled(enabled);
			Button btnLegendTitle = getToggleButton(BUTTON_TITLE);
			if (!getTitleVisibleSelection() && btnLegendTitle.getSelection()) {
				btnLegendTitle.setSelection(false);
				detachPopup();
			} else {
				refreshPopupSheet();
			}
		} else if (e.widget.equals(cmbLegendBehavior)) {
			String selectedLg = cmbLegendBehavior.getSelectedItemData();
			if (selectedLg != null) {
				getChart().getInteractivity().setLegendBehavior(LegendBehaviorType.getByName(selectedLg));
			}
		} else if (e.widget.equals(btnShowValue)) {
			int state = btnShowValue.getSelectionState();
			ChartElementUtil.setEObjectAttribute(getChart().getLegend(), "showValue", //$NON-NLS-1$
					state == ChartCheckbox.STATE_SELECTED, state == ChartCheckbox.STATE_GRAYED);
		}
	}

	protected String getLegendTitle() {
		String value = getChart().getLegend().getTitle().getCaption().getValue();
		if (value == null) {
			return ""; //$NON-NLS-1$
		}
		return value;
	}

	public void widgetDefaultSelected(SelectionEvent e) {
		// Do nothing.
	}

	protected boolean getTitleVisibleSelection() {
		return getContext().getUIFactory().canEnableUI(btnTitleVisible);
	}
}
