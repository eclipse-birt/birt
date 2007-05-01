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

package org.eclipse.birt.report.designer.ui.cubebuilder.page;

import org.eclipse.birt.report.designer.data.ui.property.AbstractDescriptionPropertyPage;
import org.eclipse.birt.report.designer.ui.cubebuilder.nls.Messages;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class GroupsPage extends AbstractDescriptionPropertyPage
{

	public static final String GROUPPAGE_MESSAGE = Messages.getString( "GroupsPage.Title.Message" );
	private TabularCubeHandle input;
	private CubeGroupContent cubeGroup;
	private CubeBuilder builder;

	public GroupsPage( CubeBuilder builder, TabularCubeHandle model )
	{
		input = model;
		this.builder = builder;
	}

	public Control createContents( Composite parent )
	{
		cubeGroup = new CubeGroupContent( builder, parent, SWT.NONE );
		return cubeGroup;
	}

	public void pageActivated( )
	{
		getContainer( ).setMessage( Messages.getString( "GroupsPage.Container.Title.Message" ),//$NON-NLS-1$
				IMessageProvider.NONE );
		builder.setTitleTitle( Messages.getString( "GroupsPage.Title.Title" ) );
		builder.setErrorMessage( null );
		builder.setTitleMessage( GROUPPAGE_MESSAGE );
		load( );
	}

	private void load( )
	{
		if ( input != null )
		{
			cubeGroup.setInput( input, null );
			cubeGroup.load( );
		};
	}
}
