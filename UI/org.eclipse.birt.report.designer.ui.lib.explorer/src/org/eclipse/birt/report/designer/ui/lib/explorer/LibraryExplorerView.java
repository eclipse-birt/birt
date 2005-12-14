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

package org.eclipse.birt.report.designer.ui.lib.explorer;

import org.eclipse.birt.report.designer.internal.ui.editors.parts.AbstractMultiPageLayoutEditor;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.ILibraryProvider;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.IContributedContentsView;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.part.MessagePage;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.part.PageBookView;

/**
 * Attribute view shows the attributes of the selected control. If no control is
 * selected, it will show no attributes and a sentence describing there is
 * nothing to show for the selected object.
 * </p>
 * Multi-selection of control of the same type will normally show the same UI as
 * if only one control was selected. Some of the values may be gray or blank if
 * the selected controls have different attributes. If the controls have
 * different type, nothing will be shown in the attributes view.
 * </P>
 * 
 * 
 */
public class LibraryExplorerView extends PageBookView
{

	/**
	 * the ID
	 */
	public static final String ID = "org.eclipse.birt.report.designer.ui.lib.explorer.view"; //$NON-NLS-1$

	private String defaultText = Messages.getString( "LibraryExplorerView.defaultText.notAvailable" ); //$NON-NLS-1$

	/**
	 * default constructor
	 */
	public LibraryExplorerView( )
	{
		super( );
	}

	/**
	 * Creates and returns the default page for this view.
	 * 
	 * @param book
	 *            the pagebook control
	 * @return the default page
	 */
	protected IPage createDefaultPage( PageBook book )
	{
		MessagePage page = new MessagePage( );
		initPage( page );
		page.createControl( book );
		page.setMessage( defaultText );
		return page;
	}

	/**
	 * Creates a new page in the pagebook for a particular part. This page will
	 * be made visible whenever the part is active, and will be destroyed with a
	 * call to <code>doDestroyPage</code>.
	 * 
	 * @param part
	 *            the input part
	 * @return the record describing a new page for this view
	 * @see #doDestroyPage
	 */
	protected PageRec doCreatePage( IWorkbenchPart part )
	{
		if ( part instanceof AbstractMultiPageLayoutEditor )
		{
			LibraryExplorerTreeViewPage page = new LibraryExplorerTreeViewPage( );
			IEditorPart editor = UIUtil.getActiveEditor( true );
			if(editor !=null)
			{
				ILibraryProvider provider = (ILibraryProvider) editor.getAdapter( ILibraryProvider.class );
				if( provider != null )
				{
					page.setLibraryProvider( provider );
					initPage( page );
					page.createControl( getPageBook( ) );
					return new PageRec( part, page );
				}
			}
		}
		return null;
	}

	/**
	 * Destroys a page in the pagebook for a particular part. This page was
	 * returned as a result from <code>doCreatePage</code>.
	 * 
	 * @param part
	 *            the input part
	 * @param pageRecord
	 *            a page record for the part
	 * @see #doCreatePage
	 */
	protected void doDestroyPage( IWorkbenchPart part, PageRec pageRecord )
	{
		IPage page = pageRecord.page;
		page.dispose( );
		pageRecord.dispose( );
	}

	/**
	 * Returns the active, important workbench part for this view.
	 * <p>
	 * When the page book view is created it has no idea which part within the
	 * workbook should be used to generate the first page. Therefore, it
	 * delegates the choice to subclasses of <code>PageBookView</code>.
	 * </p>
	 * <p>
	 * Implementors of this method should return an active, important part in
	 * the workbench or <code>null</code> if none found.
	 * </p>
	 * 
	 * @return the active important part, or <code>null</code> if none
	 */
	protected IWorkbenchPart getBootstrapPart( )
	{
		IWorkbenchPage page = getSite( ).getPage( );
		if ( page != null )
		{
			return page.getActiveEditor( );
		}
		return null;
	}

	/**
	 * Returns whether the given part should be added to this view.
	 * 
	 * @param part
	 *            the input part
	 * @return <code>true</code> if the part is relevant, and
	 *         <code>false</code> otherwise
	 */
	protected boolean isImportant( IWorkbenchPart part )
	{
		return ( part instanceof IEditorPart );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter( Class key )
	{
		if ( key == IContributedContentsView.class )
			return new IContributedContentsView( ) {

				public IWorkbenchPart getContributingPart( )
				{
					return getCurrentContributingPart( );
				}
			};
		return super.getAdapter( key );
	}
}