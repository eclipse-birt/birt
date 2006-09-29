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

package org.eclipse.birt.report.designer.ui.editors;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.model.views.outline.ReportElementModel;
import org.eclipse.birt.report.designer.core.util.mediator.request.IRequestConvert;
import org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest;
import org.eclipse.birt.report.designer.internal.lib.commands.SetCurrentEditModelCommand;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.editors.IDEMultiPageReportEditor;
import org.eclipse.birt.report.designer.ui.editors.LibraryLayoutEditorFormPage;
import org.eclipse.birt.report.designer.ui.editors.LibraryMasterPageEditorFormPage;
import org.eclipse.birt.report.designer.ui.editors.pages.ReportLayoutEditorFormPage;
import org.eclipse.birt.report.designer.ui.editors.pages.ReportMasterPageEditorFormPage;
import org.eclipse.birt.report.designer.ui.editors.pages.ReportXMLSourceEditorFormPage;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.TemplateElementHandle;
import org.eclipse.birt.report.model.api.TemplateParameterDefinitionHandle;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.util.Assert;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.editor.IFormPage;
import org.eclipse.ui.ide.IGotoMarker;
import org.eclipse.ui.texteditor.AbstractMarkerAnnotationModel;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.ui.texteditor.MarkerUtilities;

/**
 * 
 */

class BIRTGotoMarker implements IGotoMarker
{

	protected IDEMultiPageReportEditor editorPart;

	public BIRTGotoMarker( IDEMultiPageReportEditor editorPart )
	{
		this.editorPart = editorPart;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.ide.IGotoMarker#gotoMarker(org.eclipse.core.resources.IMarker)
	 */
	public void gotoMarker( IMarker marker )
	{
		// TODO Auto-generated method stub
		Assert.isNotNull( editorPart );

		if ( !marker.exists( ) )
		{
			return;
		}

		ModuleHandle moduleHandle = editorPart.getModel( );
		ReportElementHandle reportElementHandle = null;
		reportElementHandle = getReportElementHandle( moduleHandle, marker );
		if ( reportElementHandle == null 
		|| ( reportElementHandle != null && isElementTemplateParameterDefinition(reportElementHandle)))
		{
			gotoXMLSourcePage( moduleHandle, marker );
		}
		else
		{
			if ( moduleHandle instanceof ReportDesignHandle )
			{
				// go to master page
				if ( isElementInMasterPage( reportElementHandle ) )
				{
					gotoLayoutPage( ReportMasterPageEditorFormPage.ID,
							marker,
							reportElementHandle );
				}
				else
				// go to Layout Page
				{
					gotoLayoutPage( ReportLayoutEditorFormPage.ID,
							marker,
							reportElementHandle );
				}
			}
			else if ( moduleHandle instanceof LibraryHandle )
			{
				// go to master page
				if ( isElementInMasterPage( reportElementHandle ) )
				{
					gotoLayoutPage( LibraryMasterPageEditorFormPage.ID,
							marker,
							reportElementHandle );
				}
				else
				// go to Layout Page
				{
					gotoLibraryLayoutPage( marker, reportElementHandle );
				}

			}

		}

	}

	protected void gotoLibraryLayoutPage( IMarker marker,
			ReportElementHandle reportElementHandle )
	{
		String pageId = LibraryLayoutEditorFormPage.ID;
		if ( activatePage( pageId ) == false )
		{
			return;
		}
		ModuleHandle moduleHandle = editorPart.getModel( );
		reportElementHandle = getReportElementHandle( moduleHandle, marker );
		if ( reportElementHandle != null
				&& ( !isElementInMasterPage( reportElementHandle ) ) )
		{
			SetCurrentEditModelCommand command = new SetCurrentEditModelCommand( reportElementHandle );
			command.execute( );
		}
		else
		// can not find it in this editpage
		{
			MessageDialog.openError( UIUtil.getDefaultShell( ),
					Messages.getString( "BIRTGotoMarker.Error.Title" ),
					Messages.getString( "BIRTGotoMarker.Error.Message" ) );
		}

	}

	protected void gotoLayoutPage( String pageId, final IMarker marker,
			final ReportElementHandle reportElementHandle )
	{
		if ( activatePage( pageId ) == false )
		{
			return;
		}

		Display.getCurrent( ).asyncExec( new Runnable( ) {

			public void run( )
			{
				gotoLayoutMarker( marker, reportElementHandle );
			}
		} );
	}

	protected void gotoXMLSourcePage( ModuleHandle moduleHandle,
			final IMarker marker )
	{
		String pageId = ReportXMLSourceEditorFormPage.ID;
		if ( activatePage( pageId ) == false )
		{
			return;
		}

		final ReportXMLSourceEditorFormPage xmlSourceEditorFormPage = (ReportXMLSourceEditorFormPage) editorPart.getActivePageInstance( );
		Display.getCurrent( ).asyncExec( new Runnable( ) {

			public void run( )
			{
				gotoXMLSourceMarker( xmlSourceEditorFormPage, marker );
			}
		} );

	}

	protected boolean activatePage( String pageId )
	{
		String currentId = editorPart.getActivePageInstance( ).getId( );
		if ( pageId.equals( currentId ) )
		{
			return true;
		}

		IFormPage formPage = editorPart.setActivePage( pageId );
		if ( formPage != null )
		{
			return true;
		}

		return false;
	}

	protected ReportElementHandle getReportElementHandle(
			ModuleHandle moduleHandle, IMarker marker )
	{
		Integer elementId = new Integer( 0 );
		try
		{
			elementId = (Integer) marker.getAttribute( IDEMultiPageReportEditor.ELEMENT_ID );
		}
		catch ( CoreException e )
		{
			ExceptionHandler.handle( e );
		}
		DesignElementHandle elementHandle = null;
		if ( elementId != null && elementId.intValue( ) > 0 )
		{
			elementHandle = moduleHandle.getElementByID( elementId.intValue( ) );
			if ( ( elementHandle == null )
					|| ( !( elementHandle instanceof ReportElementHandle ) ) )
			{
				return null;
			}
			if ( elementHandle instanceof CellHandle
					|| elementHandle instanceof ColumnHandle
					|| elementHandle instanceof MasterPageHandle
					|| elementHandle instanceof ReportItemHandle
					|| elementHandle instanceof RowHandle
					|| elementHandle instanceof TemplateElementHandle )
			{
				return (ReportElementHandle) elementHandle;
			}
		}
		return null;
	}

	/**
	 * Select the report element in the layout(including report design and
	 * library)
	 * 
	 * @param marker
	 *            the marker to go to
	 */
	protected void gotoLayoutMarker( IMarker marker,
			ReportElementHandle reportElementHandle )
	{
		ModuleHandle moduleHandle = editorPart.getModel( );
		reportElementHandle = getReportElementHandle( moduleHandle, marker );

		if ( reportElementHandle == null )
		{
			MessageDialog.openError( UIUtil.getDefaultShell( ),
					Messages.getString( "BIRTGotoMarker.Error.Title" ),
					Messages.getString( "BIRTGotoMarker.Error.Message" ) );
			return;
		}

		List list = new ArrayList( );
		list.add( reportElementHandle );
		ReportRequest r = new ReportRequest( );
		r.setType( ReportRequest.SELECTION );
		r.setRequestConvert( new IRequestConvert( ) {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.birt.report.designer.core.util.mediator.request.IRequestConvert#convertSelectionToModelLisr(java.util.List)
			 */
			public List convertSelectionToModelLisr( List list )
			{
				List lst = new ArrayList( );

				for ( Iterator itr = list.iterator( ); itr.hasNext( ); )
				{
					Object obj = itr.next( );

					if ( obj instanceof ReportElementModel )
					{
						lst.add( ( (ReportElementModel) obj ).getSlotHandle( ) );
					}
					else
					{
						lst.add( obj );
					}
				}
				return lst;
			}
		} );

		r.setSelectionObject( list );
		SessionHandleAdapter.getInstance( ).getMediator( ).notifyRequest( r );

	}

	/**
	 * If the editor can be saved all marker ranges have been changed according
	 * to the text manipulations. However, those changes are not yet propagated
	 * to the marker manager. Thus, when opening a marker, the marker's position
	 * in the editor must be determined as it might differ from the position
	 * stated in the marker.
	 * 
	 * @param marker
	 *            the marker to go to
	 */
	protected void gotoXMLSourceMarker( AbstractTextEditor editorFormPage,
			IMarker marker )
	{

		int start = MarkerUtilities.getCharStart( marker );
		int end = MarkerUtilities.getCharEnd( marker );

		boolean selectLine = start < 0 || end < 0;

		// look up the current range of the marker when the document has been
		// edited
		IAnnotationModel model = editorFormPage.getDocumentProvider( )
				.getAnnotationModel( editorFormPage.getEditorInput( ) );
		if ( model instanceof AbstractMarkerAnnotationModel )
		{

			AbstractMarkerAnnotationModel markerModel = (AbstractMarkerAnnotationModel) model;
			Position pos = markerModel.getMarkerPosition( marker );
			if ( pos != null && !pos.isDeleted( ) )
			{
				// use position instead of marker values
				start = pos.getOffset( );
				end = pos.getOffset( ) + pos.getLength( );
			}

			if ( pos != null && pos.isDeleted( ) )
			{
				// do nothing if position has been deleted
				return;
			}
		}

		IDocument document = editorFormPage.getDocumentProvider( )
				.getDocument( editorFormPage.getEditorInput( ) );

		if ( selectLine )
		{
			int line;
			try
			{
				if ( start >= 0 )
					line = document.getLineOfOffset( start );
				else
				{
					line = MarkerUtilities.getLineNumber( marker );
					// Marker line numbers are 1-based
					if ( line >= 1 )
					{
						--line;
					}

					start = document.getLineOffset( line );
				}
				end = start + document.getLineLength( line ) - 1;
			}
			catch ( BadLocationException e )
			{
				return;
			}
		}

		int length = document.getLength( );
		if ( end - 1 < length && start < length )
			editorFormPage.selectAndReveal( start, end - start );
	}

	protected boolean isElementInMasterPage( DesignElementHandle elementHandle )
	{
		ModuleHandle root = elementHandle.getRoot( );
		DesignElementHandle container = elementHandle;
		while ( container != null && container != root )
		{
			if ( container instanceof MasterPageHandle )
			{
				return true;
			}
			container = container.getContainer( );
		}

		return false;
	}
	
	protected boolean isElementTemplateParameterDefinition( DesignElementHandle elementHandle )
	{
		ModuleHandle root = elementHandle.getRoot( );
		DesignElementHandle container = elementHandle;
		while ( container != null && container != root )
		{
			if ( container instanceof TemplateParameterDefinitionHandle )
			{
				return true;
			}
			container = container.getContainer( );
		}

		return false;
	}
}
