/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt.wizard.format.popup.chart;

import java.util.List;

import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.Text;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.composites.ExternalizedTextEditorComposite;
import org.eclipse.birt.chart.ui.swt.composites.FontDefinitionComposite;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.AbstractPopupSheet;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

/**
 * VisibilitySheet
 */

public class VisibilitySheet extends AbstractPopupSheet implements Listener {

	protected ExternalizedTextEditorComposite txtEmptyMsg;

	protected Label lbTxtEmptyMsg;

	protected Label lbFdcEmptyMsg;

	protected FontDefinitionComposite fdcEmptyMsg;

	protected Button btnAutoHide;

	protected Button btnShowEmptyMsg;

	public VisibilitySheet(String title, ChartWizardContext context) {
		super(title, context, false);
	}

	@Override
	protected Composite getComponent(Composite parent) {
		Composite emptyMsgComp = new Composite(parent, SWT.NONE);
		{
			{
				GridData gd = new GridData(GridData.FILL_HORIZONTAL);
				gd.horizontalSpan = 3;
				emptyMsgComp.setLayoutData(gd);
				emptyMsgComp.setLayout(new GridLayout(1, false));
			}

			org.eclipse.birt.chart.model.component.Label laEmptyMsg = getChart().getEmptyMessage();

			btnAutoHide = new Button(emptyMsgComp, SWT.RADIO);
			{
				btnAutoHide.setText(Messages.getString("ChartSheetImpl.Button.AutoHide")); //$NON-NLS-1$
				GridData gd = new GridData();
				btnAutoHide.setLayoutData(gd);
				btnAutoHide.setSelection(laEmptyMsg.isSetVisible() && !laEmptyMsg.isVisible());
				btnAutoHide.addListener(SWT.Selection, this);
			}

			btnShowEmptyMsg = new Button(emptyMsgComp, SWT.RADIO);
			{
				btnShowEmptyMsg.setText(Messages.getString("ChartSheetImpl.Button.ShowEmptyMsg")); //$NON-NLS-1$
				GridData gd = new GridData();
				btnShowEmptyMsg.setLayoutData(gd);
				btnShowEmptyMsg.setSelection(laEmptyMsg.isSetVisible() && laEmptyMsg.isVisible());
				btnShowEmptyMsg.addListener(SWT.Selection, this);
			}

			Composite cmpEmptyText = new Composite(emptyMsgComp, SWT.NONE);
			{
				GridData gd = new GridData(GridData.FILL_BOTH);
				gd.horizontalIndent = 12;
				cmpEmptyText.setLayoutData(gd);
				cmpEmptyText.setLayout(new GridLayout(2, false));
			}

			lbTxtEmptyMsg = new Label(cmpEmptyText, SWT.NONE);
			lbTxtEmptyMsg.setText(Messages.getString("ChartSheetImpl.Label.Text")); //$NON-NLS-1$

			List<String> keys = null;
			if (getContext().getUIServiceProvider() != null) {
				keys = getContext().getUIServiceProvider().getRegisteredKeys();
			}

			txtEmptyMsg = new ExternalizedTextEditorComposite(cmpEmptyText, SWT.BORDER, -1, -1, keys,
					getContext().getUIServiceProvider(), laEmptyMsg.getCaption().getValue());
			{
				GridData gd = new GridData(GridData.FILL_HORIZONTAL);
				gd.widthHint = 200;
				txtEmptyMsg.setLayoutData(gd);
				txtEmptyMsg.addListener(this);
			}

			lbFdcEmptyMsg = new Label(cmpEmptyText, SWT.NONE);
			lbFdcEmptyMsg.setText(Messages.getString("ChartSheetImpl.Label.Font")); //$NON-NLS-1$

			fdcEmptyMsg = new FontDefinitionComposite(cmpEmptyText, SWT.NONE, getContext(),
					laEmptyMsg.getCaption().getFont(), laEmptyMsg.getCaption().getColor(), true);
			{
				GridData gd = new GridData(GridData.FILL_HORIZONTAL);
				gd.widthHint = 200;
				gd.grabExcessVerticalSpace = false;
				fdcEmptyMsg.setLayoutData(gd);
				fdcEmptyMsg.addListener(this);
			}

			updateEmptyMessageUIStates();
		}
		return emptyMsgComp;
	}

	protected void updateEmptyMessageUIStates() {
		boolean bEnabled = getChart().getEmptyMessage().isVisible();
		txtEmptyMsg.setEnabled(bEnabled);
		fdcEmptyMsg.setEnabled(bEnabled);
		lbTxtEmptyMsg.setEnabled(bEnabled);
		lbFdcEmptyMsg.setEnabled(bEnabled);
	}

	public void handleEvent(Event event) {
		if (event.widget == txtEmptyMsg) {
			getChart().getEmptyMessage().getCaption().setValue(txtEmptyMsg.getText());
		} else if (event.widget == btnAutoHide || event.widget == btnShowEmptyMsg) {
			getChart().getEmptyMessage().setVisible(!btnAutoHide.getSelection());
			updateEmptyMessageUIStates();
		} else if (event.widget == fdcEmptyMsg) {
			Text caption = getChart().getEmptyMessage().getCaption();
			caption.setFont((FontDefinition) ((Object[]) event.data)[0]);
			caption.setColor((ColorDefinition) ((Object[]) event.data)[1]);
		}
	}

}
