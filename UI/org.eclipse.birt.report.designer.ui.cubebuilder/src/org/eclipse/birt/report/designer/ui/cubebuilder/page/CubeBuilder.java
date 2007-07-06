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

import org.eclipse.birt.report.designer.data.ui.property.AbstractTitlePropertyDialog;
import org.eclipse.birt.report.designer.data.ui.property.PropertyNode;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.ui.cubebuilder.nls.Messages;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.jface.preference.IPreferencePageContainer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class CubeBuilder extends AbstractTitlePropertyDialog implements
		IPreferencePageContainer
{

	// public static final String MEASURESPAGE =
	// "org.eclipse.birt.datasource.editor.cubebuilder.measurespage";
	public static final String GROUPPAGE = "org.eclipse.birt.datasource.editor.cubebuilder.grouppage";
	public static final String DATASETSELECTIONPAGE = "org.eclipse.birt.datasource.editor.cubebuilder.datasetselectionpage";
	public static final String LINKGROUPSPAGE = "org.eclipse.birt.datasource.editor.cubebuilder.linkgroupspage";

	public CubeBuilder( Shell parentShell, TabularCubeHandle input )
	{
		super( parentShell, input );
		addCommonPage( input );
	}

	private DatasetSelectionPage datasetPage = null;
	private GroupsPage groupsPage = null;
	private LinkGroupsPage linkGroupsPage = null;

	private void addCommonPage( TabularCubeHandle model )
	{
		datasetNode = new PropertyNode( DATASETSELECTIONPAGE,
				Messages.getString( "DatasetPage.Title" ),
				null,
				datasetPage = new DatasetSelectionPage( this, model ) );
		groupsNode = new PropertyNode( GROUPPAGE,
				Messages.getString( "GroupsPage.Title" ),
				null,
				groupsPage = new GroupsPage( this, model ) );
		linkGroupNode = new PropertyNode( LINKGROUPSPAGE,
				Messages.getString( "LinkGroupsPage.Title" ),
				null,
				linkGroupsPage = new LinkGroupsPage( this, model ) );
		addNodeTo( "/", datasetNode );
		addNodeTo( "/", groupsNode );
		addNodeTo( "/", linkGroupNode );
	}
	private String showNodeId;

	public void showPage( String nodeId )
	{
		this.showNodeId = nodeId;
	}

	public boolean performCancel( )
	{
		return true;
	}

	public boolean performOk( )
	{
		return true;
	}

	protected Control createContents( Composite parent )
	{
		String title = Messages.getString( "CubeBuilder.Title" );
		getShell( ).setText( title );

		if ( showNodeId != null )
		{
			setDefaultNode( showNodeId );
		}

		Control control = super.createContents( parent );
		return control;
	}

	private boolean okEnable = true;
	private PropertyNode datasetNode;
	private PropertyNode groupsNode;
	private PropertyNode linkGroupNode;

	public void setOKEnable( boolean okEnable )
	{
		this.okEnable = okEnable;
		if ( getOkButton( ) != null )
			getOkButton( ).setEnabled( this.okEnable );
	}

	protected void createButtonsForButtonBar( Composite parent )
	{
		super.createButtonsForButtonBar( parent );
		getOkButton( ).setEnabled( this.okEnable );
	}

	public void elementChanged( DesignElementHandle focus, NotificationEvent ev )
	{
		if ( getOkButton( ) != null )
		{
			if ( ( (CubeHandle) getModel( ) ).getName( ) != null
					&& !( (CubeHandle) getModel( ) ).getName( )
							.trim( )
							.equals( "" ) )
			{
				getOkButton( ).setEnabled( true );
			}
			else
				getOkButton( ).setEnabled( false );
		}
	}

	public IPreferenceStore getPreferenceStore( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void updateButtons( )
	{
		// TODO Auto-generated method stub

	}

	public void updateMessage( )
	{
		// TODO Auto-generated method stub

	}

	public void updateTitle( )
	{
		// TODO Auto-generated method stub

	}

	protected Point getDefaultSize( )
	{
		return new Point( 820, 600 );
	}

	public PropertyNode getLinkGroupNode( )
	{
		return linkGroupNode;
	}

	public PropertyNode getDatasetNode( )
	{
		return datasetNode;
	}

	public PropertyNode getGroupsNode( )
	{
		return groupsNode;
	}

}
