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

import java.util.List;

import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.Interactivity;
import org.eclipse.birt.chart.model.attribute.impl.InteractivityImpl;
import org.eclipse.birt.chart.model.layout.TitleBlock;
import org.eclipse.birt.chart.model.util.ChartDefaultValueUtil;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.ChartCheckbox;
import org.eclipse.birt.chart.ui.swt.composites.ExternalizedTextEditorComposite;
import org.eclipse.birt.chart.ui.swt.composites.FontDefinitionComposite;
import org.eclipse.birt.chart.ui.swt.composites.TriggerDataComposite;
import org.eclipse.birt.chart.ui.swt.interfaces.ITaskPopupSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.SubtaskSheetImpl;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.InteractivitySheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.chart.TitleBlockSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.chart.TitleTextSheet;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.chart.util.TriggerSupportMatrix;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

/**
 * Title subtask
 *
 */
public class ChartTitleSheetImpl extends SubtaskSheetImpl implements SelectionListener, Listener {

	private ExternalizedTextEditorComposite txtTitle = null;
	private FontDefinitionComposite fdcFont;
	private ChartCheckbox btnVisible;
	private ChartCheckbox btnAutoTitle;

	@Override
	public void createControl(Composite parent) {
		ChartUIUtil.bindHelp(parent, ChartHelpContextIds.SUBTASK_TITLE);

		TitleBlock defTitle = ChartDefaultValueUtil.getDefaultValueChart(getChart()).getTitle();

		init();

		cmpContent = new Composite(parent, SWT.NONE);
		{
			cmpContent.setLayout(new GridLayout());
			GridData gd = new GridData(GridData.FILL_BOTH);
			cmpContent.setLayoutData(gd);
		}

		Composite cmpBasic = new Composite(cmpContent, SWT.NONE);
		{
			cmpBasic.setLayout(new GridLayout(4, false));
			GridData gd = new GridData(GridData.FILL_BOTH);
			cmpBasic.setLayoutData(gd);
		}

		Label lblTitle = new Label(cmpBasic, SWT.NONE);
		{
			lblTitle.setText(Messages.getString("ChartTitleSheetImpl.Label.ChartTitle")); //$NON-NLS-1$
		}

		List<String> keys = null;
		if (getContext().getUIServiceProvider() != null) {
			keys = getContext().getUIServiceProvider().getRegisteredKeys();
		}
		txtTitle = new ExternalizedTextEditorComposite(cmpBasic, SWT.BORDER, -1, -1, keys,
				getContext().getUIServiceProvider(), getTitleText());
		{
			GridData gdTXTTitle = new GridData();
			gdTXTTitle.widthHint = 200;
			txtTitle.setLayoutData(gdTXTTitle);
			txtTitle.setEnabled(isTitleEnabled());
			txtTitle.addListener(this);
		}

		btnVisible = getContext().getUIFactory().createChartCheckbox(cmpBasic, SWT.NONE, defTitle.isVisible());
		btnVisible.setText(Messages.getString("ChartSheetImpl.Label.Visible")); //$NON-NLS-1$
		GridData gd = new GridData();
		btnVisible.setLayoutData(gd);
		btnVisible.setSelectionState(getChart().getTitle().isSetVisible()
				? (getChart().getTitle().isVisible() ? ChartCheckbox.STATE_SELECTED : ChartCheckbox.STATE_UNSELECTED)
				: ChartCheckbox.STATE_GRAYED);
		btnVisible.addSelectionListener(this);

		boolean dynamicTitleVisible = getContext().getUIFactory().createUIHelper().isDefaultTitleSupported();

		btnAutoTitle = new ChartCheckbox(cmpBasic, SWT.NONE, defTitle.isAuto());
		btnAutoTitle.setText(Messages.getString("ChartTitleSheetImpl.Text.Auto")); //$NON-NLS-1$
		gd = new GridData();
		btnAutoTitle.setLayoutData(gd);
		btnAutoTitle.setSelectionState(getChart().getTitle().isSetAuto()
				? (getChart().getTitle().isAuto() ? ChartCheckbox.STATE_SELECTED : ChartCheckbox.STATE_UNSELECTED)
				: ChartCheckbox.STATE_GRAYED);
		btnAutoTitle.setEnabled(isAutoEnabled());

		btnAutoTitle.setVisible(dynamicTitleVisible);
		btnAutoTitle.addSelectionListener(this);

		Label lblFont = new Label(cmpBasic, SWT.NONE);
		lblFont.setText(Messages.getString("LabelAttributesComposite.Lbl.Font")); //$NON-NLS-1$

		fdcFont = new FontDefinitionComposite(cmpBasic, SWT.NONE, getContext(),
				getChart().getTitle().getLabel().getCaption().getFont(),
				getChart().getTitle().getLabel().getCaption().getColor(), getFontDialogStyles());
		GridData gdFDCFont = new GridData();
		// gdFDCFont.heightHint = fdcFont.getPreferredSize( ).y;
		gdFDCFont.widthHint = 220;
		gdFDCFont.horizontalSpan = 3;
		gdFDCFont.grabExcessVerticalSpace = false;
		fdcFont.setLayoutData(gdFDCFont);
		fdcFont.addListener(this);

		createButtonGroup(cmpContent);
		updateUIState(getContext().getUIFactory().canEnableUI(btnVisible));
	}

	private void init() {
		// Make it compatible with old model
		if (getChart().getInteractivity() == null) {
			Interactivity interactivity = InteractivityImpl.create();
			interactivity.eAdapters().addAll(getChart().eAdapters());
			getChart().setInteractivity(interactivity);
		}
	}

	protected void createButtonGroup(Composite parent) {
		Composite cmp = new Composite(parent, SWT.NONE);
		{
			cmp.setLayout(new GridLayout(5, false));
			GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
			gridData.grabExcessVerticalSpace = true;
			gridData.verticalAlignment = SWT.END;
			cmp.setLayoutData(gridData);
		}

		ITaskPopupSheet popup;
		// Title Text
		popup = new TitleTextSheet(Messages.getString("ChartTitleSheetImpl.Text.TitleText"), //$NON-NLS-1$
				getContext());
		Button btnTitleProp = createToggleButton(cmp, BUTTON_TEXT,
				Messages.getString("ChartTitleSheetImpl.Text.TitleText&"), //$NON-NLS-1$
				popup, getChart().getTitle().isVisible());
		btnTitleProp.addSelectionListener(this);

		// Title Layout
		popup = new TitleBlockSheet(Messages.getString("ChartTitleSheetImpl.Text.TitleLayout"), //$NON-NLS-1$
				getContext());
		Button btnBlockProp = createToggleButton(cmp, BUTTON_LAYOUT,
				Messages.getString("ChartTitleSheetImpl.Text.Layout&"), //$NON-NLS-1$
				popup, getChart().getTitle().isVisible());
		btnBlockProp.addSelectionListener(this);

		// Interactivity
		if (getContext().isInteractivityEnabled()) {
			popup = new InteractivitySheet(Messages.getString("ChartTitleSheetImpl.Label.Interactivity"), //$NON-NLS-1$
					getContext(), getChart().getTitle().getTriggers(), getChart().getTitle(),
					TriggerSupportMatrix.TYPE_CHARTTITLE, TriggerDataComposite.ENABLE_SHOW_TOOLTIP_VALUE);
			Button btnInteractivity = createToggleButton(cmp, BUTTON_INTERACTIVITY,
					Messages.getString("SeriesYSheetImpl.Label.Interactivity&"), //$NON-NLS-1$
					popup, getChart().getInteractivity().isEnable());
			btnInteractivity.addSelectionListener(this);
		}
	}

	@Override
	public void handleEvent(Event event) {
		if (event.widget.equals(txtTitle)) {
			String title = txtTitle.getText();
			if ((btnAutoTitle.getSelectionState() == ChartCheckbox.STATE_GRAYED
					|| btnAutoTitle.getSelectionState() == ChartCheckbox.STATE_UNSELECTED)
					&& (txtTitle.getText() == null || txtTitle.getText().trim().length() == 0)) {
				title = null;
			}
			getChart().getTitle().getLabel().getCaption().setValue(title);
		} else if (event.widget.equals(fdcFont)) {
			if (event.type == FontDefinitionComposite.FONT_CHANTED_EVENT) {
				getChart().getTitle().getLabel().getCaption().setFont((FontDefinition) ((Object[]) event.data)[0]);
				getChart().getTitle().getLabel().getCaption().setColor((ColorDefinition) ((Object[]) event.data)[1]);
			}
		}

	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		// Detach popup dialog if there's selected popup button.
		if (detachPopup(e.widget)) {
			return;
		}

		if (isRegistered(e.widget)) {
			attachPopup(((Button) e.widget).getData().toString());
		}

		if (e.widget.equals(btnVisible)) {
			boolean visible = btnVisible.getSelectionState() == ChartCheckbox.STATE_SELECTED;
			if (btnVisible.getSelectionState() == ChartCheckbox.STATE_GRAYED) {
				getChart().getTitle().unsetVisible();
			} else {
				getChart().getTitle().setVisible(visible);
			}
			updateUIState(getContext().getUIFactory().canEnableUI(btnVisible));

			if (getToggleButtonSelection(BUTTON_TEXT) || getToggleButtonSelection(BUTTON_LAYOUT)) {
				detachPopup();
			}
		} else if (e.widget.equals(btnAutoTitle)) {
			if (btnAutoTitle.getSelectionState() == ChartCheckbox.STATE_GRAYED) {
				getChart().getTitle().unsetAuto();
				getChart().getTitle().getLabel().getCaption().setValue(null);
			} else {
				getChart().getTitle().setAuto(btnAutoTitle.getSelectionState() == ChartCheckbox.STATE_SELECTED);
				if (btnAutoTitle.getSelectionState() == ChartCheckbox.STATE_UNSELECTED) {
					getChart().getTitle().getLabel().getCaption().setValue(ChartUtil.getDefaultChartTitle(getChart()));
				}
			}
			updateTextTitleState();
		}
	}

	protected void updateTextTitleState() {
		txtTitle.setEnabled(isTitleEnabled());
		txtTitle.setText(getTitleText());
	}

	protected void updateUIState(boolean enabled) {
		txtTitle.setEnabled(isTitleEnabled());
		if (btnAutoTitle != null) {
			btnAutoTitle.setEnabled(isAutoEnabled());
		}
		setToggleButtonEnabled(BUTTON_TEXT, enabled);
		setToggleButtonEnabled(BUTTON_LAYOUT, enabled);
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub

	}

	private boolean isAutoEnabled() {
		return !getContext().getUIFactory().isSetInvisible(getChart().getTitle());
	}

	private boolean isTitleEnabled() {
		return !getContext().getUIFactory().isSetInvisible(getChart().getTitle()) && !isAutoTitle();
	}

	protected boolean isAutoTitle() {
		return getChart().getTitle().isSetAuto() && getChart().getTitle().isAuto();
	}

	protected int getFontDialogStyles() {
		return FontDefinitionComposite.ENABLE_ALIGNMENT | FontDefinitionComposite.ENABLE_ROTATION;
	}

	private String getTitleText() {
		if (getChart().getTitle().isAuto() && getContext().getExtendedItem() != null) {
			return getContext().getUIFactory().createUIHelper().getDefaultTitle(getContext());
		}
		String title = getChart().getTitle().getLabel().getCaption().getValue();
		return (title == null) ? "" : title; //$NON-NLS-1$
	}

}
