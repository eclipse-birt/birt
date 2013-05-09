/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.oda.pojo.ui.impl.dialogs;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.eclipse.datatools.connectivity.IConnection;
import org.eclipse.datatools.connectivity.IConnectionProfile;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.design.ResourceIdentifiers;
import org.eclipse.datatools.connectivity.ui.PingJob;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import org.eclipse.birt.data.oda.pojo.api.Constants;
import org.eclipse.birt.data.oda.pojo.ui.i18n.Messages;
import org.eclipse.birt.data.oda.pojo.ui.impl.contols.POJOClassTabFolderPage;
import org.eclipse.birt.data.oda.pojo.ui.util.HelpUtil;
import org.eclipse.birt.data.oda.pojo.ui.util.Utils;
import org.eclipse.birt.data.oda.pojo.util.URLParser;

/**
 * 
 */

public class ClassPathsPageHelper
{
	public static final String DEFAULT_MSG = Messages.getString( "DataSource.PageMessage" ); //$NON-NLS-1$
	private Properties props = new Properties( );
	private WizardPage wizardPage;
	
	private ResourceIdentifiers ri;
	
	private Composite parent;
	private TabFolder tabFolder;
	
	private POJOClassTabFolderPage runtimePage;
	private POJOClassTabFolderPage designtimePage;

	public ClassPathsPageHelper( ResourceIdentifiers ri )
	{
		this.ri = ri;
	}
	
	public void setWizardPage( WizardPage page )
	{
		wizardPage = page;
	}

	public void setResourceIdentifiers( ResourceIdentifiers ri )
	{
		this.ri = ri;
	}

	public Properties collectCustomProperties( )
	{
		props.put( Constants.POJO_DATA_SET_CLASS_PATH,
				runtimePage.getClassPathString( ) );
		props.put( Constants.POJO_CLASS_PATH, designtimePage.getClassPathString( ) );
		return props;
	}

	public void createPageCustomControl( Composite parent )
	{
		this.parent = parent;
		Composite composite = new Composite( parent, SWT.NONE );
		GridLayout layout = new GridLayout( 1, false );
		layout.horizontalSpacing = 10;
		composite.setLayout( layout );
		
		createTabFolderArea( composite );		

		HelpUtil.setSystemHelp( parent, HelpUtil.CONEXT_ID_DATASOURCE_POJO );
		
	}

	private void createTabFolderArea( Composite composite )
	{
		Composite tabArea = new Composite( composite, SWT.NONE );
		GridLayout layout = new GridLayout( 1, false );
		layout.marginWidth = 10;
		tabArea.setLayout( layout );
		tabArea.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		
		tabFolder = new TabFolder( tabArea, SWT.TOP );
		tabFolder.setLayoutData( new GridData( GridData.FILL_BOTH ) );

		runtimePage = new POJOClassTabFolderPage( this, getApplResourceDir( ) );
		runtimePage.setPrompMessage( Messages.getString( "DataSource.POJOClassTabFolderPage.promptLabel.runtime" ) ); //$NON-NLS-1$
		TabItem runtimeTab = runtimePage.createContents( tabFolder );
		runtimeTab.setText( Messages.getString( "DataSource.POJOClasses.tab.runtime" ) ); //$NON-NLS-1$
		runtimeTab.setImage( Utils.getRunTimeIcon( ) );

		designtimePage = new POJOClassTabFolderPage( this, getApplResourceDir( ) );
		designtimePage.setPrompMessage( Messages.getString( "DataSource.POJOClassTabFolderPage.promptLabel.designtime" ) ); //$NON-NLS-1$
		TabItem designTimeTab = designtimePage.createContents( tabFolder );
		designTimeTab.setText( Messages.getString( "DataSource.POJOClasses.tab.designTime" ) ); //$NON-NLS-1$
		designTimeTab.setImage( Utils.getDesignTimeIcon( ) );
		
		runtimePage.setFriendPage( designtimePage );
		designtimePage.setFriendPage( runtimePage );

		initControlValues( );
	}
	
	public void setInitialProperties( Properties dataSourceProps )
	{
		if ( dataSourceProps != null )
		{
			props = dataSourceProps;
		}
		initControlValues( );
	}
	
	private void initControlValues( )
	{
		String dataSetClassPath = props.getProperty( Constants.POJO_DATA_SET_CLASS_PATH );
		String pojoClassPath = props.getProperty( Constants.POJO_CLASS_PATH );
		
		if ( runtimePage != null )
		{
			//UI controls are already created
			runtimePage.setClassPath( dataSetClassPath );
			designtimePage.setClassPath( pojoClassPath );
		}
	}
	
	private File getApplResourceDir( )
	{
		if ( ri != null )
		{
			if ( ri.getApplResourceBaseURI( ) != null )
			{
				return new File( ri.getApplResourceBaseURI( ) );
			}
		}
		return null;
	}
	
	public void updatePageStatus( )
	{
		if ( wizardPage != null )
			wizardPage.setPageComplete( runtimePage.canFinish( )
					|| designtimePage.canFinish( ) );
	}

	protected Runnable createTestConnectionRunnable( final IConnectionProfile profile )
	{
        return new Runnable() 
        {
			public void run() 
            {
                IConnection conn = PingJob.createTestConnection( profile );

                Throwable exception = PingJob.getTestConnectionException( conn );
                
                if ( exception == null ) //succeed in creating connection
                {
					exception = testConnection( );
                }
                PingJob.PingUIJob.showTestConnectionMessage( parent.getShell(), exception );
                if( conn != null )
                {
                    conn.close();
                }
            }

			private Throwable testConnection(  )
			{
				Throwable exception = null;
				if ( runtimePage.getClassPathString( ).length( ) == 0 )
				{
					exception = new OdaException( Messages.getString( "DataSource.MissDataSetPojoClassPath.runtime" ) ); //$NON-NLS-1$
				}
				else if ( designtimePage.getClassPathString( ).length( ) == 0 )
				{
					exception = new OdaException( Messages.getString( "DataSource.MissDataSetPojoClassPath.designtime" ) ); //$NON-NLS-1$
				}
				else
				{
					exception = validateAllJars( exception);
				}
				return exception;
			}
			
			private Throwable validateAllJars( Throwable exception )
			{
				URLParser up = Utils.createURLParser( ri );
				try
				{
					URL[] urls = up.parse( runtimePage.getClassPathString( ) );
					for ( URL url : urls )
					{
						try
						{
							// check if url exists
							url.openStream( ).close( );
						}
						catch ( IOException e )
						{
							throw new OdaException( Messages.getFormattedString( "DataSource.ClassPathPage.testConnection.failed.runtime", //$NON-NLS-1$
									new Object[]{
										url.getFile( )
									} ) );
						}
					}
				}
				catch ( OdaException e1 )
				{
					exception = e1;
				}
				
				if( exception != null )
					return exception;
				
				try
				{
					URL[] urls = up.parse( designtimePage.getClassPathString( ) );
					for ( URL url : urls )
					{
						try
						{
							// check if url exists
							url.openStream( ).close( );
						}
						catch ( IOException e )
						{
							throw new OdaException( Messages.getFormattedString( "DataSource.ClassPathPage.testConnection.failed.designtime", //$NON-NLS-1$
									new Object[]{
										url.getFile( )
									} ) );
						}
					}
				}
				catch ( OdaException e1 )
				{
					exception = e1;
				}
				return exception;
			}
		};
	}
}
