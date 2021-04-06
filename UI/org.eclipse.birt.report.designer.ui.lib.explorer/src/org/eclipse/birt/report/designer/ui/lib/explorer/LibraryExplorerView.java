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

import org.eclipse.birt.core.preference.IPreferences;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.preferences.PreferenceFactory;
import org.eclipse.birt.report.designer.ui.util.UIUtil;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.IContributedContentsView;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.part.MessagePage;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.part.PageBookView;

/**
 * LibraryExplorerView display all library files in BIRT resource folder.
 * 
 */
public class LibraryExplorerView extends PageBookView {

	/**
	 * the ID
	 */
	public static final String ID = "org.eclipse.birt.report.designer.ui.lib.explorer.view"; //$NON-NLS-1$

	private String defaultText = Messages.getString("LibraryExplorerView.defaultText.notAvailable"); //$NON-NLS-1$

	// private Map pageMap = new HashMap( );

	private LibraryExplorerTreeViewPage treeViewPage;

	private String resourceFolder;

	private IPreferences prefs;

	/**
	 * default constructor
	 */
	public LibraryExplorerView() {
		super();
	}

	/**
	 * Creates and returns the default page for this view.
	 * 
	 * @param book the pagebook control
	 * @return the default page
	 */
	protected IPage createDefaultPage(PageBook book) {
		MessagePage page = new MessagePage();
		initPage(page);
		page.createControl(book);
		page.setMessage(defaultText);
		return page;
	}

	protected PageRec getPageRec(IWorkbenchPart part) {
		PageRec rec = super.getPageRec(part);
		if (treeViewPage != null && !treeViewPage.isDisposed()) {
			if (prefs != null)
				prefs.removePreferenceChangeListener(treeViewPage);
			prefs = PreferenceFactory.getInstance().getPreferences(ReportPlugin.getDefault(),
					UIUtil.getCurrentProject());
			prefs.addPreferenceChangeListener(treeViewPage);
			String currentResourceFolder = ReportPlugin.getDefault().getResourceFolder();
			if (currentResourceFolder != null && !currentResourceFolder.equals(this.resourceFolder)) {
				treeViewPage.refreshRoot();
				this.resourceFolder = currentResourceFolder;
			}
		}
		return rec;
	}

	/**
	 * Creates a new page in the pagebook for a particular part. This page will be
	 * made visible whenever the part is active, and will be destroyed with a call
	 * to <code>doDestroyPage</code>.
	 * 
	 * @param part the input part
	 * @return the record describing a new page for this view
	 * @see #doDestroyPage
	 */
	protected PageRec doCreatePage(IWorkbenchPart part) {
		// // if ( part instanceof AbstractMultiPageEditor )
		// // {
		// IEditorPart editor = UIUtil.getActiveEditor( true );
		// if ( editor != null )
		// {
		// Object fileAdapter = editor.getEditorInput( )
		// .getAdapter( IFile.class );
		// LibraryExplorerTreeViewPage page = getPage( fileAdapter, editor );
		// initPage( page );
		// page.createControl( getPageBook( ) );
		// return new PageRec( part, page );
		// }
		// // }
		// return null;
		if (treeViewPage == null || treeViewPage.isDisposed()) {
			treeViewPage = new LibraryExplorerTreeViewPage();
			initPage(treeViewPage);
			treeViewPage.createControl(getPageBook());
			if (prefs != null)
				prefs.removePreferenceChangeListener(treeViewPage);
			prefs = PreferenceFactory.getInstance().getPreferences(ReportPlugin.getDefault(),
					UIUtil.getCurrentProject());
			if (prefs != null)
				prefs.addPreferenceChangeListener(treeViewPage);
		}
		return new PageRec(part, treeViewPage);
	}

	// private LibraryExplorerTreeViewPage getPage( Object fileAdapter,
	// IEditorPart editor )
	// {
	// LibraryExplorerTreeViewPage page;
	// if ( fileAdapter != null
	// && pageMap.containsKey( ( (IFile) fileAdapter ).getProject( ) ) )
	// {
	// page = (LibraryExplorerTreeViewPage) pageMap.get( ( (IFile) fileAdapter
	// ).getProject( ) );
	// }
	// else
	// {
	// page = new LibraryExplorerTreeViewPage( );
	// ILibraryProvider provider = (ILibraryProvider) editor.getAdapter(
	// ILibraryProvider.class );
	// if ( provider != null )
	// {
	// page.setLibraryProvider( provider );
	// if ( fileAdapter != null )
	// {
	// pageMap.put( ( (IFile) fileAdapter ).getProject( ), page );
	// }
	// }
	// }
	// return page;
	// }

	/**
	 * Destroys a page in the pagebook for a particular part. This page was returned
	 * as a result from <code>doCreatePage</code>.
	 * 
	 * @param part       the input part
	 * @param pageRecord a page record for the part
	 * @see #doCreatePage
	 */
	protected void doDestroyPage(IWorkbenchPart part, PageRec pageRecord) {
		if (treeViewPage != null && prefs != null) {
			prefs.removePreferenceChangeListener(treeViewPage);
		}

		this.resourceFolder = null;

		IPage page = pageRecord.page;
		page.dispose();
		pageRecord.dispose();
	}

	/**
	 * Returns the active, important workbench part for this view.
	 * <p>
	 * When the page book view is created it has no idea which part within the
	 * workbook should be used to generate the first page. Therefore, it delegates
	 * the choice to subclasses of <code>PageBookView</code>.
	 * </p>
	 * <p>
	 * Implementors of this method should return an active, important part in the
	 * workbench or <code>null</code> if none found.
	 * </p>
	 * 
	 * @return the active important part, or <code>null</code> if none
	 */
	protected IWorkbenchPart getBootstrapPart() {
		IWorkbenchPage page = getSite().getPage();
		if (page != null) {
			return page.getActiveEditor();
		}
		return null;
	}

	/**
	 * Returns whether the given part should be added to this view.
	 * 
	 * @param part the input part
	 * @return <code>true</code> if the part is relevant, and <code>false</code>
	 *         otherwise
	 */
	protected boolean isImportant(IWorkbenchPart part) {
		return (part instanceof IEditorPart);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class key) {
		if (key == IContributedContentsView.class)
			return new IContributedContentsView() {

				public IWorkbenchPart getContributingPart() {
					return getCurrentContributingPart();
				}
			};
		return super.getAdapter(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.PageBookView#partBroughtToTop(org.eclipse.ui.
	 * IWorkbenchPart)
	 */
	public void partBroughtToTop(IWorkbenchPart part) {
		super.partBroughtToTop(part);
		partActivated(part);
	}
}