/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.preferences;

import org.eclipse.birt.report.designer.internal.ui.expressions.ExpressionSupportManager;
import org.eclipse.birt.report.designer.internal.ui.expressions.IExpressionSupport;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.util.PixelConverter;
import org.eclipse.birt.report.designer.ui.util.UIUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

/**
 */
public class ExpressionBuilderConfigurationBlock extends
		OptionsConfigurationBlock
{

	private final Key PREF_DEFAULT_UNIT = getReportKey( ReportPlugin.DEFAULT_SCRIPT_TYPE );
	private final Key PREF_FISCAL_START = getReportKey( ReportPlugin.FISCAL_YEAR_START );
	private final static String DEFAULT_UNIT = Messages.getString("ExpressionBuilderConfigurationBlock.DefaultSyntax"); //$NON-NLS-1$
	private final static String DEFAULT_FISCAL_START = Messages
			.getString( "ExpressionBuilderConfigurationBlock.FiscalYearStart" ); //$NON-NLS-1$

	public final int LTR_DIRECTION_INDX = 0;
	public final int RTL_DIRECTION_INDX = 1;

	private PixelConverter fPixelConverter;

	public ExpressionBuilderConfigurationBlock( IStatusChangeListener context,
			IProject project )
	{
		super( context, ReportPlugin.getDefault( ), project );
		setKeys( getKeys( ) );
	}

	private Key[] getKeys( )
	{
		Key[] keys = new Key[]{
				PREF_DEFAULT_UNIT, PREF_FISCAL_START
		};
		return keys;
	}

	/*
	 * @see
	 * org.eclipse.jface.preference.PreferencePage#createContents(Composite)
	 */
	protected Control createContents( Composite parent )
	{
		fPixelConverter = new PixelConverter( parent );
		setShell( parent.getShell( ) );

		Composite mainComp = new Composite( parent, SWT.NONE );
		mainComp.setFont( parent.getFont( ) );
		GridLayout layout = new GridLayout( );
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		mainComp.setLayout( layout );

		Composite othersComposite = createBuildPathTabContent( mainComp );
		GridData gridData = new GridData( GridData.FILL,
				GridData.FILL,
				true,
				true );
		gridData.heightHint = fPixelConverter.convertHeightInCharsToPixels( 20 );
		othersComposite.setLayoutData( gridData );

		validateSettings( null, null, null );

		UIUtil.bindHelp( parent, IHelpContextIds.PREF_PAGE_EXPRESSION_SYNTAX );

		return mainComp;
	}

	private Composite createBuildPathTabContent( Composite parent )
	{
		IExpressionSupport[] supports = ExpressionSupportManager.getExpressionSupports( );
		String[] supportDisplayNames = new String[supports.length];
		String[] supportNames = new String[supports.length];
		for ( int i = 0; i < supports.length; i++ )
		{
			supportDisplayNames[i] = supports[i].getDisplayName( );
			supportNames[i] = supports[i].getName( );
		}

		Composite pageContent = new Composite( parent, SWT.NONE );

		GridData data = new GridData( GridData.FILL_HORIZONTAL
				| GridData.FILL_VERTICAL
				| GridData.VERTICAL_ALIGN_BEGINNING );
		data.grabExcessHorizontalSpace = true;
		pageContent.setLayoutData( data );

		GridLayout layout = new GridLayout( );
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.numColumns = 3;
		pageContent.setLayout( layout );

		addComboBox( pageContent,
				DEFAULT_UNIT,
				PREF_DEFAULT_UNIT,
				supportNames,
				supportDisplayNames,
				0 );
		
		new Label( pageContent, SWT.NONE );
		
		this.addLabelledTextField( pageContent,
				DEFAULT_FISCAL_START,
				PREF_FISCAL_START,
				10,
				0,
				false );

		return pageContent;
	}

	/*
	 * (non-javadoc) Update fields and validate. @param changedKey Key that
	 * changed, or null, if all changed.
	 */

	public void performDefaults( )
	{
		super.performDefaults( );
	}

	public void useProjectSpecificSettings( boolean enable )
	{
		super.useProjectSpecificSettings( enable );
	}
}
