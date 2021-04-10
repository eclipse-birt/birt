/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt.wizard.format.popup;

import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.birt.chart.ui.swt.composites.TriggerDataComposite;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 */

public class InteractivitySheet extends AbstractPopupSheet {

	private final EList<Trigger> triggers;
	private final EObject cursorContainer;
	private final int iInteractivityType;
	private final int optionalStyle;

	/**
	 * 
	 * @param title
	 * @param context
	 * @param triggers
	 * @param cursorContainer
	 * @param iInteractivityType see <code>TriggerSupportMatrix</code>
	 * @param optionalStyle
	 */
	public InteractivitySheet(String title, ChartWizardContext context, EList<Trigger> triggers,
			EObject cursorContainer, int iInteractivityType, int optionalStyle) {
		super(title, context, false);
		this.triggers = triggers;
		this.cursorContainer = cursorContainer;
		this.iInteractivityType = iInteractivityType;
		this.optionalStyle = optionalStyle;
	}

	protected Composite getComponent(Composite parent) {
		ChartUIUtil.bindHelp(parent, ChartHelpContextIds.POPUP_INTERACTIVITY);
		final TriggerDataComposite triggerUI = new TriggerDataComposite(parent, SWT.NONE, triggers, cursorContainer,
				getContext(), iInteractivityType, optionalStyle);
		parent.getShell().addDisposeListener(new DisposeListener() {

			public void widgetDisposed(DisposeEvent e) {
				triggerUI.markSaveWhenClosing();
			}
		});
		return triggerUI;
	}

}
