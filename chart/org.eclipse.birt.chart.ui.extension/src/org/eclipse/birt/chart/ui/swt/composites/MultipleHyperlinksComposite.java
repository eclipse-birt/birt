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

package org.eclipse.birt.chart.ui.swt.composites;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.chart.model.attribute.MultiURLValues;
import org.eclipse.birt.chart.model.attribute.URLValue;
import org.eclipse.birt.chart.model.attribute.impl.MultiURLValuesImpl;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.util.TriggerSupportMatrix;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

/**
 * The class implements UI functions to set multiple hyperlinks for an trigger
 * condition.
 *
 * @since 2.5
 */

public class MultipleHyperlinksComposite extends Composite implements Listener {

	private List fListHyperlinks;
	private Button fBtnAdd;
	private Button fBtnEdit;
	private Button fBtnDelete;
	private Button fBtnProperties;
	Text fTxtTooltip;

	private ChartWizardContext fContext;

	MultiURLValues fMultiURLValues;
	private TriggerSupportMatrix fTriggerMatrix;
	private int fOptionalStyles;

	private Map<String, URLValue> fURLValuesMap = new HashMap<>();
	private Button fBtnUp;
	private Button fBtnDown;

	/**
	 * @param parent
	 * @param style
	 */
	public MultipleHyperlinksComposite(Composite parent, int style, ChartWizardContext context,
			TriggerSupportMatrix triggerMatrix, int optionalStyles) {
		super(parent, style);
		fContext = context;
		fTriggerMatrix = triggerMatrix;
		fOptionalStyles = optionalStyles;

		placeComponents();
		initListeners();
		updateButtonStatus();
	}

	public void populateUIValues(MultiURLValues urlValues) {
		// Clear old items.
		fListHyperlinks.removeAll();
		fURLValuesMap.clear();

		setURLValues(urlValues);
		if (fMultiURLValues == null) {
			fMultiURLValues = MultiURLValuesImpl.create();
		}

		EList<URLValue> urlValuies = fMultiURLValues.getURLValues();
		String[] items = new String[urlValuies.size()];
		int i = 0;
		for (URLValue uv : urlValuies) {
			String text = uv.getLabel().getCaption().getValue();
			if (text == null) {
				text = uv.getBaseUrl();
			}
			items[i] = text;
			fListHyperlinks.add(text);
			fURLValuesMap.put(text, uv);
			i++;
		}

		if (fMultiURLValues.getTooltip() != null) {
			fTxtTooltip.setText(fMultiURLValues.getTooltip());
		}
		updateButtonStatus();
	}

	private void placeComponents() {
		// Set layout.
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		this.setLayout(gl);

		// Add hyperlinks group.
		Group group = new Group(this, SWT.NONE);
		group.setText(Messages.getString("MultipleHyperlinksComposite.Group.Hyperlinks")); //$NON-NLS-1$
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		group.setLayoutData(gd);

		gl = new GridLayout();
		gl.numColumns = 3;
		group.setLayout(gl);

		fListHyperlinks = new List(group, SWT.V_SCROLL | SWT.H_SCROLL | SWT.SINGLE | SWT.BORDER);
		gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 80;
		gd.widthHint = 100;
		gd.horizontalSpan = 3;
		fListHyperlinks.setLayoutData(gd);

		fBtnAdd = new Button(group, SWT.NONE);
		fBtnAdd.setText(Messages.getString("MultipleHyperlinksComposite.Btn.Add")); //$NON-NLS-1$
		gd = new GridData(GridData.FILL_HORIZONTAL);
		fBtnAdd.setLayoutData(gd);

		fBtnEdit = new Button(group, SWT.NONE);
		fBtnEdit.setText(Messages.getString("MultipleHyperlinksComposite.Btn.Edit")); //$NON-NLS-1$
		gd = new GridData(GridData.FILL_HORIZONTAL);
		fBtnEdit.setLayoutData(gd);

		fBtnDelete = new Button(group, SWT.NONE);
		fBtnDelete.setText(Messages.getString("MultipleHyperlinksComposite.Btn.Delete")); //$NON-NLS-1$
		gd = new GridData(GridData.FILL_HORIZONTAL);
		fBtnDelete.setLayoutData(gd);

		fBtnUp = new Button(group, SWT.NONE);
		fBtnUp.setText(Messages.getString("MultipleHyperlinksComposite.Btn.Up")); //$NON-NLS-1$
		gd = new GridData(GridData.FILL_HORIZONTAL);
		fBtnUp.setLayoutData(gd);

		fBtnDown = new Button(group, SWT.NONE);
		fBtnDown.setText(Messages.getString("MultipleHyperlinksComposite.Btn.Down")); //$NON-NLS-1$
		gd = new GridData(GridData.FILL_HORIZONTAL);
		fBtnDown.setLayoutData(gd);

		new Label(group, SWT.NONE);

		// Add Properties button.
		fBtnProperties = new Button(this, SWT.NONE);
		fBtnProperties.setText(Messages.getString("MultipleHyperlinksComposite.Btn.Properties")); //$NON-NLS-1$
		gd = new GridData();
		gd.horizontalSpan = 2;
		fBtnProperties.setLayoutData(gd);

		// Add Tooltip fields.
		Label label = new Label(this, SWT.NONE);
		label.setText(Messages.getString("MultipleHyperlinksComposite.Label.Tooltip")); //$NON-NLS-1$

		fTxtTooltip = new Text(this, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 150;
		fTxtTooltip.setLayoutData(gd);
	}

	private void initListeners() {
		fListHyperlinks.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				doEdit();
			}
		});
		fListHyperlinks.addListener(SWT.Selection, this);
		fBtnAdd.addListener(SWT.Selection, this);
		fBtnEdit.addListener(SWT.Selection, this);
		fBtnDelete.addListener(SWT.Selection, this);
		fBtnUp.addListener(SWT.Selection, this);
		fBtnDown.addListener(SWT.Selection, this);

		fBtnProperties.addListener(SWT.Selection, this);
		fTxtTooltip.addFocusListener(new FocusAdapter() {

			@Override
			public void focusLost(FocusEvent e) {
				fMultiURLValues.setTooltip(fTxtTooltip.getText());
			}
		});
	}

	private void updateButtonStatus() {
		if (fListHyperlinks.isDisposed()) {
			return;
		}

		int index = fListHyperlinks.getSelectionIndex();
		boolean enabled = (index >= 0);
		fBtnEdit.setEnabled(enabled);
		fBtnDelete.setEnabled(enabled);
		fBtnUp.setEnabled(enabled && index > 0);
		fBtnDown.setEnabled(enabled && index < (fListHyperlinks.getItemCount() - 1));
		fBtnProperties.setEnabled(fMultiURLValues != null && fMultiURLValues.getURLValues().size() > 1);
	}

	private void doAdd() {
		HyperlinkEditorDialog dialog = new HyperlinkEditorDialog(getShell(), null, fContext, fTriggerMatrix,
				fOptionalStyles);
		java.util.List<String> labels = Arrays.asList(fListHyperlinks.getItems());
		dialog.setExistingLabels(labels);

		if (dialog.open() == Window.OK) {
			URLValue value = dialog.getURLValue();
			fMultiURLValues.getURLValues().add(value);
			value.eAdapters().addAll(fMultiURLValues.eAdapters());

			String text = value.getLabel().getCaption().getValue();
			fListHyperlinks.add(text);
			fURLValuesMap.put(text, value);
		}

		fListHyperlinks.setSelection(fListHyperlinks.getItemCount() - 1);
	}

	void doEdit() {
		int selectionIndex = fListHyperlinks.getSelectionIndex();
		if (selectionIndex < 0) {
			return;
		}

		URLValue value = fURLValuesMap.get(fListHyperlinks.getItem(selectionIndex));
		String oldText = value.getLabel().getCaption().getValue();
		HyperlinkEditorDialog dialog = new HyperlinkEditorDialog(getShell(), value, fContext, fTriggerMatrix,
				fOptionalStyles);
		java.util.List<String> labels = new ArrayList<>(Arrays.asList(fListHyperlinks.getItems()));
		labels.remove(value.getLabel().getCaption().getValue());
		dialog.setExistingLabels(labels);

		if (dialog.open() == Window.OK) {
			String text = value.getLabel().getCaption().getValue();
			if (!oldText.equals(text)) {
				fListHyperlinks.setItem(selectionIndex, text);
				fURLValuesMap.remove(oldText);
				fURLValuesMap.put(text, value);
			}
		}
	}

	private void doDelete() {
		int index = fListHyperlinks.getSelectionIndex();
		if (index < 0) {
			return;
		}

		URLValue value = fURLValuesMap.remove(fListHyperlinks.getItem(index));
		fMultiURLValues.getURLValues().remove(value);

		fListHyperlinks.remove(index);
		int last = fListHyperlinks.getItemCount() - 1;
		fListHyperlinks.select(index < last ? index : last);
	}

	private void doDown() {
		int index = fListHyperlinks.getSelectionIndex();
		if (index < (fListHyperlinks.getItemCount() - 1)) {
			fMultiURLValues.getURLValues().add(index + 1, fMultiURLValues.getURLValues().remove(index));

			String item = fListHyperlinks.getItem(index);
			fListHyperlinks.remove(index);
			fListHyperlinks.add(item, index + 1);
			fListHyperlinks.setSelection(index + 1);
		}
	}

	private void doUp() {
		int index = fListHyperlinks.getSelectionIndex();
		if (index > 0) {
			fMultiURLValues.getURLValues().add(index - 1, fMultiURLValues.getURLValues().remove(index));

			String item = fListHyperlinks.getItem(index);
			fListHyperlinks.remove(index);
			fListHyperlinks.add(item, index - 1);
			fListHyperlinks.setSelection(index - 1);
		}
	}

	private void editProperties() {
		MenuStylesDialog dialog = new MenuStylesDialog(getShell(), fMultiURLValues.getPropertiesMap());
		dialog.open();
	}

	/**
	 * Returns current URL values.
	 *
	 * @return
	 */
	public MultiURLValues getURLValues() {
		return fMultiURLValues;
	}

	public void setURLValues(MultiURLValues urlValues) {
		fMultiURLValues = urlValues;
	}

	@Override
	public void handleEvent(Event event) {
		Object source = event.widget;
		if (source == fBtnAdd) {
			doAdd();
		} else if (source == fBtnEdit) {
			doEdit();
		} else if (source == fBtnDelete) {
			doDelete();
		} else if (source == fBtnUp) {
			doUp();
		} else if (source == fBtnDown) {
			doDown();
		} else if (source == fBtnProperties) {
			editProperties();
		}
		updateButtonStatus();
	}
}
