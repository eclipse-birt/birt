/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.palette;

import org.eclipse.birt.report.designer.core.DesignerConstants;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.model.views.data.DataSetItemModel;
import org.eclipse.birt.report.designer.internal.ui.dnd.InsertInLayoutUtil;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.AbstractToolHandleExtends;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.LibraryElementsToolHandleExtends;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.ReportCreationTool;
import org.eclipse.birt.report.designer.internal.ui.palette.BasePaletteFactory.DataSetColumnToolExtends;
import org.eclipse.birt.report.designer.internal.ui.palette.BasePaletteFactory.DataSetToolExtends;
import org.eclipse.birt.report.designer.internal.ui.palette.BasePaletteFactory.ParameterToolExtends;
import org.eclipse.birt.report.designer.internal.ui.views.actions.InsertInLayoutAction;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.DNDUtil;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.dnd.TemplateTransfer;
import org.eclipse.gef.dnd.TemplateTransferDropTargetListener;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.jface.util.Assert;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;

/**
 * Drag&Drop listener
 */
public class ReportTemplateTransferDropTargetListener
		extends
			TemplateTransferDropTargetListener
{

	private static final String TRANS_LABEL_CREATE_ELEMENT = Messages.getString( "ReportTemplateTransferDropTargetListener.transLabel.createElement" ); //$NON-NLS-1$

	/**
	 * Constructor
	 * 
	 * @param viewer
	 */
	public ReportTemplateTransferDropTargetListener( EditPartViewer viewer )
	{
		super( viewer );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.dnd.TemplateTransferDropTargetListener#getFactory(java.lang.Object)
	 */
	protected CreationFactory getFactory( Object template )
	{
		if ( handleValidateDrag( template ) )
		{
			if ( template instanceof String )
			{
				return new ReportElementFactory( template );
			}
			return new ReportElementFactory( getSingleTransferData( template ),
					template );
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.dnd.AbstractTransferDropTargetListener#handleDrop()
	 */
	protected void handleDrop( )
	{
		final Object template = TemplateTransfer.getInstance( ).getTemplate( );
		Assert.isNotNull( template );

		Assert.isTrue( handleValidateDrag( template ) );

		updateTargetRequest( );
		updateTargetEditPart( );

		AbstractToolHandleExtends preHandle = null;
		String transName = null;
		if ( template instanceof String )
		{
			transName = TRANS_LABEL_CREATE_ELEMENT;
			preHandle = BasePaletteFactory.getAbstractToolHandleExtendsFromPaletteName( template );
		}
		else if ( handleValidateInsert( template ) )
		{
			transName = InsertInLayoutAction.DISPLAY_TEXT;
			Object objectType = getFactory( template ).getObjectType( );
			if ( objectType instanceof DataSetHandle )
			{
				preHandle = new DataSetToolExtends( );
			}
			else if ( objectType instanceof DataSetItemModel )
			{
				preHandle = new DataSetColumnToolExtends( );
			}
			else if ( objectType instanceof ScalarParameterHandle )
			{
				preHandle = new ParameterToolExtends( );
			}
		}
		else if ( handleValidateLibrary( template ) )
		{
			preHandle = new LibraryElementsToolHandleExtends( (DesignElementHandle) getSingleTransferData( template ) );
		}

		if ( preHandle != null )
		{
			SessionHandleAdapter.getInstance( )
					.getReportDesignHandle( )
					.getCommandStack( )
					.startTrans( transName );
			preHandle.setRequest( this.getCreateRequest( ) );
			preHandle.setTargetEditPart( getTargetEditPart( ) );

			Command command = this.getCommand( );
			if ( command != null && command.canExecute( ) )
			{
				if ( !( preHandle.preHandleMouseUp( ) ) )
				{
					SessionHandleAdapter.getInstance( )
							.getReportDesignHandle( )
							.getCommandStack( )
							.rollback( );
					return;
				}
			}
			super.handleDrop( );
			SessionHandleAdapter.getInstance( )
					.getReportDesignHandle( )
					.getCommandStack( )
					.commit( );
			selectAddedObject( );
		}

	}

	/**
	 * Validates drag source from palette, layout, or data view
	 * 
	 * @param dragObj
	 * @return validate result
	 */
	private boolean handleValidateDrag( Object dragObj )
	{
		return dragObj != null
				&& ( handleValidatePalette( dragObj )
						|| handleValidateOutline( dragObj )
						|| handleValidateInsert( dragObj ) || handleValidateLibrary( dragObj ) );
	}

	private boolean handleValidatePalette( Object dragObj )
	{
		return dragObj instanceof String
				&& ( getTargetEditPart( ) == null || ReportCreationTool.handleValidatePalette( dragObj,
						getTargetEditPart( ) ) );
	}

	/**
	 * Validates drag from data view to layout
	 * 
	 * @param template
	 * @return validate result
	 */
	private boolean handleValidateInsert( Object template )
	{
		return InsertInLayoutUtil.handleValidateInsert( template )
				&& ( getTargetEditPart( ) == null || InsertInLayoutUtil.handleValidateInsertToLayout( template,
						getTargetEditPart( ) ) );
	}

	/**
	 * Validates drag source of outline view and drop target of layout
	 * 
	 * @return validate result
	 */
	private boolean handleValidateOutline( Object dragSource )
	{
		return false;
	}

	private boolean handleValidateLibrary( Object dragObj )
	{
		EditPart targetEditPart = getTargetEditPart( );
		if ( targetEditPart == null )
		{
			return true;
		}
		if ( dragObj != null )
		{
			Object[] dragObjs;
			if ( dragObj instanceof Object[] )
			{
				dragObjs = (Object[]) dragObj;
			}
			else
			{
				dragObjs = new Object[]{
					dragObj
				};
			}
			if ( dragObjs.length == 0 )
			{
				return false;
			}
			for ( int i = 0; i < dragObjs.length; i++ )
			{
				dragObj = dragObjs[i];
				if ( dragObj instanceof ReportElementHandle )
				{
					if ( ( (ReportElementHandle) dragObj ).getRoot( ) instanceof LibraryHandle )
					{
						if ( !DNDUtil.handleValidateTargetCanContain( targetEditPart.getModel( ),
								dragObj )
								|| !DNDUtil.handleValidateTargetCanContainMore( targetEditPart.getModel( ),
										1 ) )
						{
							return false;
						}
					}
					else
					{
						return false;
					}
				}
				else
				{
					return false;
				}
			}
			return true;
		}
		return false;
	} /*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.gef.dnd.AbstractTransferDropTargetListener#dragOver(org.eclipse.swt.dnd.DropTargetEvent)
		 */

	public void dragOver( DropTargetEvent event )
	{
		super.dragOver( event );
		if ( !handleValidateDrag( TemplateTransfer.getInstance( ).getTemplate( ) ) )
		{
			event.detail = DND.DROP_NONE;
		}
	}

	/*
	 * Add the newly created object to the viewer's selected objects.
	 */
	private void selectAddedObject( )
	{
		final Object model = getCreateRequest( ).getExtendedData( )
				.get( DesignerConstants.KEY_NEWOBJECT );
		final EditPartViewer viewer = getViewer( );
		viewer.getControl( ).setFocus( );
		ReportCreationTool.selectAddedObject( model, viewer );
	}

	/**
	 * Gets single transfer data from TemplateTransfer
	 * 
	 * @param template
	 *            object transfered by TemplateTransfer
	 * @return single transfer data in array or itself
	 */
	private Object getSingleTransferData( Object template )
	{
		if ( template instanceof Object[] )
		{
			return ( (Object[]) template )[0];
		}
		return template;
	}

}
