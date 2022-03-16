/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.chart.ui.swt;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartWizardContext;
import org.eclipse.birt.chart.ui.swt.interfaces.IExpressionButton;
import org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider;
import org.eclipse.birt.chart.ui.util.ChartUIUtil.EAttributeAccessor;
import org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

/**
 *
 */

public class ExprEditComposite extends Composite {

	/**
	 * Text modified event.
	 */
	public static final int TEXT_MODIFIED = 49;

	private final IChartWizardContext fContext;
	private Text text;
	private IExpressionButton btnBuilder;

	public ExprEditComposite(Composite parent, IChartWizardContext fContext) {
		super(parent, SWT.NONE);
		this.fContext = fContext;
		placeComponents();
	}

	public void bindModel(EObject eObj, EAttribute eAttr) {
		if (btnBuilder == null) {
			return;
		}
		if (eObj != null && eAttr != null) {
			btnBuilder.setAccessor(new EAttributeAccessor<String>(eObj, eAttr));
		} else {
			btnBuilder.setAccessor(null);
		}
	}

	private void placeComponents() {
		GridData gd = new GridData(GridData.FILL_BOTH);
		this.setLayoutData(gd);
		GridLayout gl = new GridLayout(2, false);
		gl.marginHeight = 0;
		gl.marginWidth = 0;
		gl.verticalSpacing = 0;
		this.setLayout(gl);

		text = new Text(this, SWT.BORDER);
		{
			gd = new GridData(GridData.FILL_BOTH);
			text.setLayoutData(gd);
		}

		try {
			btnBuilder = (IExpressionButton) fContext.getUIServiceProvider().invoke(
					IUIServiceProvider.Command.EXPRESS_BUTTON_CREATE, this, text, fContext.getExtendedItem(),
					IUIServiceProvider.COMMAND_EXPRESSION_DATA_BINDINGS, new Listener() {

						@Override
						public void handleEvent(Event event) {
							fireEvent();
						}
					});
		} catch (ChartException e) {
			WizardBase.displayException(e);
		}
	}

	private void fireEvent() {
		Event eventNew = new Event();
		eventNew.widget = this;
		eventNew.type = TEXT_MODIFIED;
		this.notifyListeners(TEXT_MODIFIED, eventNew);
	}

	public String getExpression() {
		return btnBuilder.getExpression();
	}

	public void setExpression(String expr) {
		btnBuilder.setExpression(expr);
	}

}
