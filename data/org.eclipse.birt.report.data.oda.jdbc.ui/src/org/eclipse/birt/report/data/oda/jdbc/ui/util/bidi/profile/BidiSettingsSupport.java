/***********************************************************************
 * Copyright (c) 2009 IBM Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 ***********************************************************************/


package org.eclipse.birt.report.data.oda.jdbc.ui.util.bidi.profile;


import java.util.Properties;

import org.eclipse.birt.report.data.bidi.utils.core.BidiConstants;
import org.eclipse.birt.report.data.bidi.utils.core.BidiFormat;
import org.eclipse.birt.report.data.oda.jdbc.ui.JdbcPlugin;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.bidi.preference.JDBCDataSourcePreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;


/**
 * @author bidi_hcg
 *
 */
public class BidiSettingsSupport 
{
	private Button bidiButton;
	private BidiFormat metadataBidiFormat = null;
	private BidiFormat contentBidiFormat = null;
	
   
    public BidiSettingsSupport() {
    	String bidiFormatString = JdbcPlugin.getDefault( ).getPluginPreferences( ).getString(JDBCDataSourcePreferencePage.EXTERNAL_BIDI_FORMAT);
		contentBidiFormat = metadataBidiFormat = new BidiFormat(bidiFormatString);
		
	}
	public void drawBidiSettingsButton( Composite parent, Properties props )
    {
    	initBidiFormats(props);
    	Composite content = (Composite)parent.getChildren()[0];
    	GridLayout layout = (GridLayout)content.getLayout();
    	layout.numColumns = 4;

    	bidiButton = new Button( content, SWT.PUSH );
    	bidiButton.setText( JdbcPlugin.getResourceString( "wizard.label.bidiSettings" ) );//$NON-NLS-1$
    	bidiButton.setLayoutData( new GridData( GridData.END ) );
    	bidiButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				doSetAdvancedBidiSettings( );
			}
		} );

    }
    private void doSetAdvancedBidiSettings()
    	{
    		AdvancedBidiDialog dlg = new AdvancedBidiDialog(this);
    		dlg.open( );
    	}

    public BidiFormat getContentBidiFormat(){
    	return contentBidiFormat;
    }
    
    public BidiFormat getMetadataBidiFormat(){
    	return metadataBidiFormat;
    }

	public void setBidiFormats(BidiFormat metadataBidiFormat,
			BidiFormat contentBidiFormat) {
		this.contentBidiFormat = contentBidiFormat;
		this.metadataBidiFormat = metadataBidiFormat;

		
	}
	
	public Properties getBidiFormats(){
		Properties p = new Properties();
		BidiFormat externalDefaultBDiFormat = new BidiFormat(JdbcPlugin.getDefault( ).getPluginPreferences( ).getString(JDBCDataSourcePreferencePage.EXTERNAL_BIDI_FORMAT));
		if (contentBidiFormat != null)
			p.setProperty(BidiConstants.CONTENT_FORMAT_PROP_NAME, contentBidiFormat.getBiDiFormatString());
		else
			p.setProperty(BidiConstants.CONTENT_FORMAT_PROP_NAME, externalDefaultBDiFormat.getBiDiFormatString());
		if (metadataBidiFormat != null)
			p.setProperty(BidiConstants.METADATA_FORMAT_PROP_NAME, metadataBidiFormat.getBiDiFormatString());
		else
			p.setProperty(BidiConstants.METADATA_FORMAT_PROP_NAME, externalDefaultBDiFormat.getBiDiFormatString());
		return p;
	}
	
	private void initBidiFormats(Properties props){
		BidiFormat contentFormat = null;
		BidiFormat metadataFormat = null;
		if (props == null)
			return;
		String str = props.getProperty(BidiConstants.CONTENT_FORMAT_PROP_NAME);
		if (str != null && !str.equals(""))
			contentFormat = new BidiFormat(str);
		else
			contentFormat = new BidiFormat(BidiConstants.DEFAULT_BIDI_FORMAT_STR);
		str = props.getProperty(BidiConstants.METADATA_FORMAT_PROP_NAME);
		if (str != null && !str.equals(""))
			metadataFormat = new BidiFormat(str);
		else
			metadataFormat = new BidiFormat(BidiConstants.DEFAULT_BIDI_FORMAT_STR);
		setBidiFormats(metadataFormat, contentFormat);
	}
	public Properties addBidiProperties(Properties props) {
		if (props != null){
			if (contentBidiFormat != null)
				props.setProperty(BidiConstants.CONTENT_FORMAT_PROP_NAME, contentBidiFormat.toString());
			if (metadataBidiFormat != null)
				props.setProperty(BidiConstants.METADATA_FORMAT_PROP_NAME, metadataBidiFormat.toString());
		}
		return props;
	}
    
}

