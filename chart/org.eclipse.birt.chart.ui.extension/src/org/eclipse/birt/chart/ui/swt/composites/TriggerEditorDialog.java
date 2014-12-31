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

package org.eclipse.birt.chart.ui.swt.composites;

import java.util.Collection;

import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Actuate Corporation
 * 
 */
public class TriggerEditorDialog extends TrayDialog
{

	private final EList<Trigger> triggers;

	private final EObject cursorContainer;

	private final Collection<Trigger> vOriginalTriggers;

	private final ChartWizardContext wizardContext;

	private final int iInteractivityType;

	private final String sTitle;

	private final int optionalStyle;

	private TriggerDataComposite triggerUI;

	/**
	 * 
	 * @param shellParent
	 * @param triggers
	 * @param wizardContext
	 * @param sTitle
	 * @param iInteractivityType
	 *            see <code>TriggerSupportMatrix</code>
	 * @param optionalStyle
	 * @param conditionFilter
	 *            trigger condition filter. If null, no filter applied.
	 * @param cursorFilter
	 *            cursor filter. If null, no filter applied
	 */
	public TriggerEditorDialog( Shell shellParent, EList<Trigger> triggers,
			EObject cursorContainer, ChartWizardContext wizardContext,
			String sTitle, int iInteractivityType, int optionalStyle )
	{
		super( shellParent );
		setHelpAvailable( false );
		this.triggers = triggers;
		this.cursorContainer = cursorContainer;
		this.wizardContext = wizardContext;
		this.sTitle = sTitle;
		this.iInteractivityType = iInteractivityType;
		this.optionalStyle = optionalStyle;
		vOriginalTriggers = EcoreUtil.copyAll( triggers );
	}

	protected Control createContents( Composite parent )
	{
		ChartUIUtil.bindHelp( parent, ChartHelpContextIds.POPUP_INTERACTIVITY );
		getShell( ).setText( Messages.getFormattedString( "TriggerEditorDialog.Title.TriggerEditor", sTitle ) ); //$NON-NLS-1$
		return super.createContents( parent );
	}

	protected void setShellStyle( int newShellStyle )
	{
		super.setShellStyle( newShellStyle
				| SWT.DIALOG_TRIM
				| SWT.RESIZE
				| SWT.APPLICATION_MODAL );
	}

	protected Control createDialogArea( Composite parent )
	{
		triggerUI = new TriggerDataComposite( parent,
				SWT.NONE,
				triggers,
				cursorContainer,
				wizardContext,
				iInteractivityType,
				optionalStyle );
		GridData gdTriggerEditor = new GridData( GridData.FILL_BOTH );
		triggerUI.setLayoutData( gdTriggerEditor );

		return triggerUI;
	}

	public EList<Trigger> getTriggers( )
	{
		return triggers;
	}

	protected void okPressed( )
	{
		triggerUI.markSaveWhenClosing( );
		super.okPressed( );
	}

	protected void cancelPressed( )
	{
		triggers.clear( );
		triggers.addAll( vOriginalTriggers );
		super.cancelPressed( );
	}

}