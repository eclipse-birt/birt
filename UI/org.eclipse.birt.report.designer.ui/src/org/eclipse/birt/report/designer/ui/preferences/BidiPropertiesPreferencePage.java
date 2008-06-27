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

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * @author bidi_hcg
 * 
 * This class represents a preference page that is contributed to the
 * Preferences dialog. This page is used to modify BiDi-specific settings -
 * Enable/Disable BiDi support - Set 'Left To Right' or 'Right To Left' default
 * report orientation
 */

public class BidiPropertiesPreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage
{

	private final static String DEFAULT_DIRECTION = Messages.getString( "report.designer.ui.preferences.bidiproperties.defaultdirection" );
	private final static String RTL_DIRECTION = Messages.getString( "report.designer.ui.preferences.bidiproperties.rtldirection" );
	private final static String LTR_DIRECTION = Messages.getString( "report.designer.ui.preferences.bidiproperties.ltrdirection" );

	public final int LTR_DIRECTION_INDX = 0;
	public final int RTL_DIRECTION_INDX = 1;

	// private BidiFormat externalBiDiFormat;
	private Label defaultDirectionLabel;
	private Combo directionCombo;

	// boolean isWindows;

	public BidiPropertiesPreferencePage( )
	{
		super( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#PreferencePage(java.lang.String)
	 */
	public BidiPropertiesPreferencePage( String title )
	{
		super( title );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#PreferencePage(java.lang.String,org.eclipse.jface.resource.ImageDescriptor)
	 */
	public BidiPropertiesPreferencePage( String title, ImageDescriptor image )
	{
		super( title, image );
	}

	protected Control createContents( Composite parent )
	{
		// isWindows = SWT.getPlatform( ).indexOf( "win32" ) >= 0; //$NON-NLS-1$

		Composite mainComposite = new Composite( parent, SWT.NONE );

		GridLayout twoColLayout = new GridLayout( );
		twoColLayout.numColumns = 2;
		twoColLayout.marginWidth = 10;
		twoColLayout.marginHeight = 10;
		mainComposite.setLayout( twoColLayout );


		defaultDirectionLabel = new Label( mainComposite, SWT.NONE );
		defaultDirectionLabel.setText( DEFAULT_DIRECTION );
		defaultDirectionLabel.setLayoutData( new GridData() );
		directionCombo = new Combo( mainComposite, SWT.DROP_DOWN
				| SWT.READ_ONLY );
		directionCombo.add( LTR_DIRECTION, LTR_DIRECTION_INDX );
		directionCombo.add( RTL_DIRECTION, RTL_DIRECTION_INDX );
		
		GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.widthHint = 200;
		gd.horizontalIndent = 20;
		directionCombo.setLayoutData( gd );
		directionCombo.select( ReportPlugin.getDefault( )
				.getLTRReportDirection( ) ? LTR_DIRECTION_INDX
				: RTL_DIRECTION_INDX );

		new Label( mainComposite, SWT.NONE );
		new Label( mainComposite, SWT.NONE );

		return mainComposite;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init( IWorkbench workbench )
	{
		setPreferenceStore( ReportPlugin.getDefault( ).getPreferenceStore( ) );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
	 */
	protected void performDefaults( )
	{
		boolean bidiDirection = true;
		if ( bidiDirection )
			directionCombo.select( LTR_DIRECTION_INDX );
		else
			directionCombo.select( RTL_DIRECTION_INDX );

	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.PreferencePage#performOk()
	 */
	public boolean performOk( )
	{
		if ( directionCombo.getSelectionIndex( ) == LTR_DIRECTION_INDX )
			ReportPlugin.getDefault( ).setLTRReportDirection( true );
		else
			ReportPlugin.getDefault( ).setLTRReportDirection( false );

		return super.performOk( );
	}

}
