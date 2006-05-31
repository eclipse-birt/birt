/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.ui.preview.editors;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.dialogs.InputParameterHtmlDialog;
import org.eclipse.birt.report.designer.ui.editors.IReportEditorPage;
import org.eclipse.birt.report.designer.ui.editors.IReportProvider;
import org.eclipse.birt.report.model.api.ConfigVariableHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;

/**
 * Preview page.
 */
public class ReportPreviewFormPage extends ReportPreviewEditor
		implements
			IReportEditorPage
{

	public static final String ID = "org.eclipse.birt.report.designer.ui.editors.preview"; //$NON-NLS-1$

	private Control control;

	private int staleType;

	private FormEditor editor;

	// suffix of design file
	public static final String SUFFIX_DESIGN_FILE = "rptdesign"; //$NON-NLS-1$

	// suffix of template file
	public static final String SUFFIX_TEMPLATE_FILE = "rpttemplate"; //$NON-NLS-1$

	// suffix of design config file
	public static final String SUFFIX_DESIGN_CONFIG = "rptconfig"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.editors.IReportEditorPage#onBroughtToTop(org.eclipse.birt.report.designer.ui.editors.IReportEditorPage)
	 */
	public boolean onBroughtToTop( IReportEditorPage prePage )
	{
		boolean isDisplay = false;

		if ( getEditorInput( ) != prePage.getEditorInput( ) )
		{
			setInput( prePage.getEditorInput( ) );
		}

		// if the model is dirty, save it at first.
		if( isDirtyModel())
		{
			doSave( null );
		}
		
		//save the last changes.
		if ( prePage.isDirty( ))
		{
			prePage.doSave( null );
		}

		// if miss parameter, pop up parameter dialog
		if ( isMissingParameter( ) )
		{
			if ( parameterDialog != null )
			{
				parameterDialog.open( );

				// if parameter dialog closed successfully, then preview the
				// current report
				if ( parameterDialog.getReturnCode( ) == InputParameterHtmlDialog.RETURN_CODE_BROWSER_CLOSED )
				{
					isDisplay = true;
					// if miss parameter yet, can't preview report and scroll to
					// the previous page.
					if ( isMissingParameter( ) )
					{
						editor.setActivePage( prePage.getId( ) );
						return false;
					}

				}
			}
		}
		else
		{
			isDisplay = true;
		}

		if ( isDisplay )
		{
			display( );
		}

		return true;
	}

	private boolean isDirtyModel( )
	{
		if( getModel( )!= null && getModel() instanceof ModuleHandle)
		{
			return ((ModuleHandle) getModel()).needsSave( );
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.editor.IFormPage#getPartControl()
	 */
	public Control getPartControl( )
	{
		return control;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.editor.IFormPage#getId()
	 */
	public String getId( )
	{
		return ID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createPartControl( Composite parent )
	{
		super.createPartControl( parent );
		Control[] children = parent.getChildren( );
		control = children[children.length - 1];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.editors.IReportEditorPage#markPageStale(int)
	 */
	public void markPageStale( int type )
	{
		staleType = type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.editors.IReportEditorPage#getStaleType()
	 */
	public int getStaleType( )
	{
		return staleType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#isDirty()
	 */
	public boolean isDirty( )
	{
		return false;
	}

	protected IReportProvider getProvider( )
	{
		IReportProvider provider = (IReportProvider) editor
				.getAdapter( IReportProvider.class );

		if ( provider == null )
		{
			provider = super.getProvider( );
		}

		return provider;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#getAdapter(java.lang.Class)
	 */
	public Object getAdapter( Class adapter )
	{
		if ( adapter.equals( ActionRegistry.class ) )
		{
			return new ActionRegistry( );
		}
		return super.getAdapter( adapter );
	}

	public void initialize( FormEditor editor )
	{
		this.editor = editor;

	}

	public FormEditor getEditor( )
	{
		return editor;
	}

	public IManagedForm getManagedForm( )
	{
		return null;
	}

	public void setActive( boolean active )
	{

	}

	public boolean isActive( )
	{
		return false;
	}

	public boolean canLeaveThePage( )
	{
		return true;
	}

	public int getIndex( )
	{
		return 0;
	}

	public void setIndex( int index )
	{

	}

	public boolean isEditor( )
	{
		return true;
	}

	public boolean selectReveal( Object object )
	{
		return false;
	}

	public void setInput( IEditorInput input )
	{
		super.setInput( input );
	}

	/**
	 * Get parameter values from config file.
	 * 
	 * @return Map
	 */
	private Map getConfigVars( )
	{
		HashMap configVars = new HashMap( );

		// get design config file name
		String configFileName = getConfigFileName( this.getFileUri( ) );
		if ( configFileName == null )
			return configVars;

		ReportDesignHandle handle = null;

		try
		{
			// Generate the session handle
			SessionHandle sessionHandle = SessionHandleAdapter.getInstance( )
					.getSessionHandle( );

			File configFile = new File( configFileName );

			// if config file existed, then delete it
			if ( configFile != null && configFile.exists( )
					&& configFile.isFile( ) )
			{
				handle = sessionHandle.openDesign( configFileName );

				if ( handle != null )
				{
					// get parameter values from config file
					Iterator it = handle.configVariablesIterator( );
					while ( it != null && it.hasNext( ) )
					{
						ConfigVariableHandle configVar = (ConfigVariableHandle) it
								.next( );
						if ( configVar != null && configVar.getName( ) != null )
						{
							// check the parameter whether exist or not
							String paramName = getParameterName( configVar
									.getName( ) );
							if ( paramName != null && paramName.length( ) > 0 )
							{
								configVars
										.put( paramName, configVar.getValue( ) );
							}
						}
					}
					handle.close( );
				}
			}
		}
		catch ( DesignFileException e )
		{
			e.printStackTrace( );
			// close handle
			try
			{
				if ( handle != null )
				{
					handle.close( );
				}
			}
			catch ( Exception e1 )
			{
				e1.printStackTrace( );
			}
		}

		return configVars;
	}

	/**
	 * if parameter existed in config file, return the correct parameter name
	 * 
	 * @param configVarName
	 * @return String
	 */
	private String getParameterName( String configVarName )
	{
		String paramName = null;
		List parameters = null;

		// get parameter list from design handle
		ModuleHandle model = SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( );
		if ( model != null )
		{
			parameters = model.getFlattenParameters( );
		}

		if ( parameters != null )
		{
			for ( int i = 0; i < parameters.size( ); i++ )
			{
				ScalarParameterHandle parameter = null;

				if ( parameters.get( i ) instanceof ScalarParameterHandle )
				{
					parameter = ( (ScalarParameterHandle) parameters.get( i ) );
				}

				// get current name
				String curName = null;
				if ( parameter != null && parameter.getName( ) != null )
				{
					curName = parameter.getName( ) + "_" + parameter.getID( ); //$NON-NLS-1$
				}

				// if find the parameter exist, return true
				if ( curName != null
						&& curName.equalsIgnoreCase( configVarName ) )
				{
					paramName = parameter.getName( );
					break;
				}
			}
		}

		return paramName;
	}

	/**
	 * If miss parameter.
	 * 
	 * @return boolean
	 */
	public boolean isMissingParameter( )
	{
		boolean missingParameter = false;
		ModuleHandle model = SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( );

		HashMap params = (HashMap) this.getConfigVars( );

		List parameters = model.getFlattenParameters( );
		if ( parameters != null )
		{
			for ( int i = 0; i < parameters.size( ); i++ )
			{
				if ( parameters.get( i ) instanceof ScalarParameterHandle )
				{
					ScalarParameterHandle parameter = ( (ScalarParameterHandle) parameters
							.get( i ) );

					if ( parameter.isHidden( ) )
					{
						continue;
					}

					String paramValue = null;

					if ( params != null
							&& params.containsKey( parameter.getName( ) ) )
					{
						Object curVal = params.get( parameter.getName( ) );
						if ( curVal != null )
							paramValue = curVal.toString( );
					}
					else
					{
						paramValue = parameter.getDefaultValue( );
					}

					if ( paramValue == null && !parameter.allowNull( ) )
					{
						missingParameter = true;
						break;
					}

					if ( paramValue != null
							&& paramValue.trim( ).length( ) <= 0
							&& !parameter.allowBlank( )
							&& parameter.getDataType( ).equalsIgnoreCase(
									DesignChoiceConstants.PARAM_TYPE_STRING ) )
					{
						missingParameter = true;
						break;
					}

				}
			}
		}

		return missingParameter;
	}

	/**
	 * Parse config file name from report design filename.
	 * 
	 * @param reportDesignName
	 *            String
	 * @return String
	 */

	private String getConfigFileName( String reportDesignName )
	{
		if ( reportDesignName == null )
			return null;

		String configFileName = null;

		if ( reportDesignName.endsWith( SUFFIX_DESIGN_FILE ) )
		{
			configFileName = reportDesignName.replaceFirst( SUFFIX_DESIGN_FILE,
					SUFFIX_DESIGN_CONFIG );
		}
		else if ( reportDesignName.endsWith( SUFFIX_TEMPLATE_FILE ) )
		{
			configFileName = reportDesignName.replaceFirst(
					SUFFIX_TEMPLATE_FILE, SUFFIX_DESIGN_CONFIG );
		}

		return configFileName;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.texteditor.AbstractTextEditor#firePropertyChange(int)
	 */
	protected void firePropertyChange( int type )
	{
		if ( type == PROP_DIRTY )
		{
			editor.editorDirtyStateChanged( );
		}
		else
		{
			super.firePropertyChange( type );
		}
	}	
}
