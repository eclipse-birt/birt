/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.data.providers;

import java.util.Map;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.dialogs.BaseTitleAreaDialog;
import org.eclipse.birt.report.designer.internal.ui.processor.ElementProcessorFactory;
import org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider;
import org.eclipse.birt.report.designer.internal.ui.views.actions.AbstractElementAction;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.dialogs.CascadingParametersDialog;
import org.eclipse.birt.report.designer.ui.dialogs.ParameterDialog;
import org.eclipse.birt.report.designer.ui.dialogs.ParameterGroupDialog;
import org.eclipse.birt.report.designer.ui.dialogs.VariableDialog;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.VariableElementHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

/**
 * 
 */

public class VariablesNodeProvider extends DefaultNodeProvider
{

	private static class AddVariableAction extends AbstractElementAction
	{

		public AddVariableAction( Object selectedObject )
		{
			super( selectedObject,
					Messages.getString( "VariablesNodeProvider.NewActionName" ) ); //$NON-NLS-1$
		}

		@Override
		protected boolean doAction( ) throws Exception
		{
			ReportDesignHandle designHandle = (ReportDesignHandle) SessionHandleAdapter.getInstance( )
					.getReportDesignHandle( );
			VariableElementHandle variable = (VariableElementHandle) ElementProcessorFactory.createProcessor( ReportDesignConstants.VARIABLE_ELEMENT )
					.createElement( null );
			VariableDialog dialog = new VariableDialog( Messages.getString( "VariablesNodeProvider.NewActionName" ), //$NON-NLS-1$
					designHandle,
					variable );
			if ( dialog.open( ) == Dialog.OK )
				designHandle.getPropertyHandle( IReportDesignModel.PAGE_VARIABLES_PROP )
						.add( variable );
			return true;
		}

	}

	public Object[] getChildren( Object model )
	{
		if ( model instanceof PropertyHandle )
			return ( (PropertyHandle) model ).getListValue( ).toArray( );
		return super.getChildren( model );
	}

	/**
	 * Creates the context menu for the given object. Gets the action from the
	 * actionRegistry for the given object and adds them to the menu
	 * 
	 * @param menu
	 *            the menu
	 * @param object
	 *            the object
	 */
	public void createContextMenu( TreeViewer sourceViewer, Object object,
			IMenuManager menu )
	{
		menu.add( new AddVariableAction( object ) );

		super.createContextMenu( sourceViewer, object, menu );
	}

	public String getNodeDisplayName( Object object )
	{
		return Messages.getString( "VariablesNodeProvider.NodeName" ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider
	 * #createElement(java.lang.String)
	 */
	protected DesignElementHandle createElement( String type ) throws Exception
	{
		DesignElementHandle handle = super.createElement( type );
		BaseTitleAreaDialog dialog = null;
		if ( ReportDesignConstants.PARAMETER_GROUP_ELEMENT.equals( type ) )
		{
			dialog = new ParameterGroupDialog( Display.getCurrent( )
					.getActiveShell( ),
					Messages.getString( "ParametersNodeProvider.dialogue.title.group" ) ); //$NON-NLS-1$
			( (ParameterGroupDialog) dialog ).setInput( handle );

		}
		else if ( ReportDesignConstants.SCALAR_PARAMETER_ELEMENT.equals( type ) )
		{
			dialog = new ParameterDialog( PlatformUI.getWorkbench( )
					.getDisplay( )
					.getActiveShell( ),
					Messages.getString( "ParametersNodeProvider.dialogue.title.parameter" ) );//$NON-NLS-1$

			// required default value
			( (ParameterDialog) dialog ).setInput( handle );
		}
		if ( dialog == null )
			return null;
		if ( dialog.open( ) == Dialog.CANCEL )
		{
			return null;
		}

		return (DesignElementHandle) dialog.getResult( );
	}

	protected boolean performInsert( Object model, SlotHandle slotHandle,
			String type, String position, Map extendData ) throws Exception
	{
		if ( ReportDesignConstants.CASCADING_PARAMETER_GROUP_ELEMENT.equals( type ) )
		{
			DesignElementHandle handle = super.createElement( type );
			slotHandle.add( handle );
			CascadingParametersDialog dialog = new CascadingParametersDialog( PlatformUI.getWorkbench( )
					.getDisplay( )
					.getActiveShell( ),
					Messages.getString( "ParameterNodeProvider.dial.title.newCascading" ) ); //$NON-NLS-1$
			dialog.setInput( handle );
			return ( dialog.open( ) == Dialog.OK );
		}
		return super.performInsert( model,
				slotHandle,
				type,
				position,
				extendData );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.views.INodeProvider#getIconName
	 * (java.lang.Object)
	 */
	public String getIconName( Object model )
	{
		return IReportGraphicConstants.ICON_NODE_VARIABLES;
	}

}
