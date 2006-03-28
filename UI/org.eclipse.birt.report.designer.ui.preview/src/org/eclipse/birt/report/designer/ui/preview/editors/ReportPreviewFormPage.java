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

import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.dialogs.InputParameterDialog;
import org.eclipse.birt.report.designer.ui.editors.IReportEditorPage;
import org.eclipse.birt.report.designer.ui.editors.IReportProvider;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.ConfigVariable;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;

/**
 * Preview page.
 */
public class ReportPreviewFormPage extends ReportPreviewEditor implements
		IReportEditorPage
{

	public static final String ID = "BIRT.Preivew"; //$NON-NLS-1$

	private Control control;

	private int staleType;


	private FormEditor editor;


	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.designer.ui.editors.IReportEditorPage#onBroughtToTop(org.eclipse.birt.report.designer.ui.editors.IReportEditorPage)
	 */
	public boolean onBroughtToTop( IReportEditorPage prePage )
	{
		if ( getEditorInput( ) != prePage.getEditorInput( ) )
		{
			setInput( prePage.getEditorInput( ) );
		}
		if ( prePage.isDirty( ) )
		{
			prePage.doSave( null );
		}
		if ( getBrowser( ) != null )
		{
			boolean showParameterDialog = false;
			List parameters = ( (ModuleHandle) getModel( ) ).getFlattenParameters( );
			if ( parameters != null )
			{
				for ( int i = 0; i < parameters.size( ); i++ )
				{
					if ( parameters.get( i ) instanceof ScalarParameterHandle )
					{
						ScalarParameterHandle parameter = ( (ScalarParameterHandle) parameters.get( i ) );

						if ( parameter.isHidden( ) )
						{
							continue;
						}

						String paramValue = null;
						ConfigVariable cfgVar = ( (ModuleHandle) getModel( ) ).findConfigVariable( parameter.getName( ) );

						if ( cfgVar != null )
						{
							paramValue = cfgVar.getValue( );
						}
						else
						{
							paramValue = parameter.getDefaultValue( );
						}

						if ( paramValue == null && !parameter.allowNull( ) )
						{
							showParameterDialog = true;
							break;
						}

						if ( paramValue != null
								&& paramValue.trim( ).length( ) <= 0
								&& !parameter.allowBlank( )
								&& parameter.getDataType( )
										.equalsIgnoreCase( DesignChoiceConstants.PARAM_TYPE_STRING ) )
						{
							showParameterDialog = true;
							break;
						}

					}
				}
			}

			if ( showParameterDialog )
			{
				InputParameterDialog dialog = new InputParameterDialog( Display.getCurrent( )
						.getActiveShell( ),
						InputParameterDialog.TITLE ); //$NON-NLS-1$
				dialog.setInput( (ModuleHandle) getModel( ) );
				if ( dialog.open( ) == Dialog.OK )
				{
					if ( ( (ModuleHandle) getModel( )).needsSave( ) )
					{
						this.doSave( null );
					}
				}
			}

			display( );
		}
		
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.editor.IFormPage#getPartControl()
	 */
	public Control getPartControl( )
	{
		return control;
	}

	/* (non-Javadoc)
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
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.designer.ui.editors.IReportEditorPage#markPageStale(int)
	 */
	public void markPageStale( int type )
	{
		staleType = type;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.designer.ui.editors.IReportEditorPage#getStaleType()
	 */
	public int getStaleType( )
	{
		return staleType;
	}


	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#isDirty()
	 */
	public boolean isDirty( )
	{
		return false;
	}
	
	protected IReportProvider getProvider( )
	{
		IReportProvider provider =  (IReportProvider) editor.getAdapter( IReportProvider.class );
		
		if(provider == null)
		{
			provider = super.getProvider( );
		}
		
		return provider;
	}	

	/* (non-Javadoc)
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
	
	public void setInput(IEditorInput input)
	{
		super.setInput( input );
	}
}
