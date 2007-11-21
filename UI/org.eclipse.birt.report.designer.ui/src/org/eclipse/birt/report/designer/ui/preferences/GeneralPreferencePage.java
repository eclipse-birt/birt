/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.preferences;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbenchPropertyPage;

/**
 * TODO: Please document
 * 
 * @version $Revision: 1.2 $ $Date: 2005/10/12 05:05:07 $
 */
public class GeneralPreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage,
		IWorkbenchPropertyPage
{

	/**
	 * 
	 */
	public GeneralPreferencePage( )
	{
		super( );
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param title
	 */
	public GeneralPreferencePage( String title )
	{
		super( title );
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param title
	 * @param image
	 */
	public GeneralPreferencePage( String title, ImageDescriptor image )
	{
		super( title, image );
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createContents( Composite parent )
	{
		// TODO Auto-generated method stub
		return new Composite( parent, SWT.NONE );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init( IWorkbench workbench )
	{
		// TODO Auto-generated method stub

	}

	protected Control createPreferenceContent( Composite composite )
	{
		// TODO Auto-generated method stub
		return null;
	}

	protected String getPreferencePageID( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	protected String getPropertyPageID( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	protected boolean hasProjectSpecificOptions( IProject project )
	{
		// TODO Auto-generated method stub
		return false;
	}

	public IAdaptable getElement( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void setElement( IAdaptable element )
	{
		// TODO Auto-generated method stub

	}

}
