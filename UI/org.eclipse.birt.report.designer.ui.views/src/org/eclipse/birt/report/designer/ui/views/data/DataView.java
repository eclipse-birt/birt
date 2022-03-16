/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.views.data;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.IContributedContentsView;
import org.eclipse.ui.part.IPage;
import org.eclipse.ui.part.IPageBookViewPage;
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
public class DataView extends PageBookView {

	/**
	 * the ID
	 */
	public static final String ID = "org.eclipse.birt.report.designer.ui.views.data.DataView"; //$NON-NLS-1$

	private String defaultText = Messages.getString("DataView.defaultText.noDataView"); //$NON-NLS-1$

	/**
	 * default constructor
	 */
	public DataView() {
		super();
	}

	/**
	 * Creates and returns the default page for this view.
	 *
	 * @param book the pagebook control
	 * @return the default page
	 */
	@Override
	protected IPage createDefaultPage(PageBook book) {
		MessagePage page = new MessagePage();
		initPage(page);
		page.createControl(book);
		page.setMessage(defaultText);
		return page;
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
	@Override
	protected PageRec doCreatePage(IWorkbenchPart part) {
		Object page = part.getAdapter(IDataViewPage.class);
		if (page instanceof IPageBookViewPage) {
			initPage((IPageBookViewPage) page);

			((IPageBookViewPage) page).createControl(getPageBook());
			return new PageRec(part, (IPageBookViewPage) page);
		}
		return null;
//		IPageBookViewPage page = new IPageBookViewPage( ) {
//
//			private CommonViewer viewer;
//			private IPageSite site;
//
//			public IPageSite getSite( )
//			{
//				// TODO Auto-generated method stub
//				return null;
//			}
//
//			public void init( IPageSite site ) throws PartInitException
//			{
//				this.site = site;
//			}
//
//			public void createControl( Composite parent )
//			{
//				viewer = new CommonViewer( "org.eclipse.birt.report.designer.ui.view2",
//						parent,
//						SWT.NONE ) {
//
//					protected void initDragAndDrop( )
//					{
////						int operations = DND.DROP_COPY | DND.DROP_MOVE;
////
////						CommonDragAdapter dragAdapter = new CommonDragAdapter( getNavigatorContentService( ),
////								this );
////						addDragSupport( operations, new Transfer[]{
////							TemplateTransfer.getInstance( )
////						}, dragAdapter );
////
////						CommonDropAdapter dropAdapter = new CommonDropAdapter( getNavigatorContentService( ),
////								this );
////						addDropSupport( operations,
////								dropAdapter.getSupportedDropTransfers( ),
////								dropAdapter );
//						super.initDragAndDrop( );
//					}
//
//				};
//				viewer.setInput( SessionHandleAdapter.getInstance( )
//						.getReportDesignHandle( ) );
//				ICommonViewerSite commonSite = CommonViewerSiteFactory.createCommonViewerSite( SessionHandleAdapter.getInstance( )
//						.getReportDesignHandle( )
//						.getFileName( ),
//						this.site );
//				final NavigatorActionService actionService = new NavigatorActionService( commonSite,
//						viewer,
//						viewer.getNavigatorContentService( ) );
//				MenuManager menuMgr = new MenuManager( viewer.getNavigatorContentService( )
//						.getViewerDescriptor( )
//						.getPopupMenuId( ) );
//				menuMgr.setRemoveAllWhenShown( true );
//				menuMgr.addMenuListener( new IMenuListener( ) {
//
//					public void menuAboutToShow( IMenuManager manager )
//					{
//						ISelection selection = viewer.getSelection( );
//						actionService.setContext( new ActionContext( selection ) );
//						actionService.fillContextMenu( manager );
//					}
//				} );
//				Menu menu = menuMgr.createContextMenu( viewer.getTree( ) );
//
//				actionService.prepareMenuForPlatformContributions( menuMgr,
//						viewer,
//						false );
//
//				viewer.getTree( ).setMenu( menu );
//
//				int ops = DND.DROP_MOVE | DND.DROP_COPY;
//				Transfer[] transfers = new Transfer[]{
//					TemplateTransfer.getInstance( )
//				};
//				// viewer.addDragSupport( operations, transferTypes, listener )
//
//				// viewer.addSelectionChangedListener( new
//				// ISelectionChangedListener( ) {
//				//
//				// public void selectionChanged( SelectionChangedEvent event )
//				// {
//				// actionService.getContext( )
//				// .setInput( event.getSelection( ) );
//				// }
//				// } );
//
//			}
//
//			public void dispose( )
//			{
//				// TODO Auto-generated method stub
//
//			}
//
//			public Control getControl( )
//			{
//				return viewer.getControl( );
//			}
//
//			public void setActionBars( IActionBars actionBars )
//			{
//				// TODO Auto-generated method stub
//
//			}
//
//			public void setFocus( )
//			{
//				// TODO Auto-generated method stub
//
//			}
//		};
//		page.createControl( getPageBook( ) );
//		return new PageRec( part, page );
	}

	/**
	 * Destroys a page in the pagebook for a particular part. This page was returned
	 * as a result from <code>doCreatePage</code>.
	 *
	 * @param part       the input part
	 * @param pageRecord a page record for the part
	 * @see #doCreatePage
	 */
	@Override
	protected void doDestroyPage(IWorkbenchPart part, PageRec pageRecord) {
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
	@Override
	protected IWorkbenchPart getBootstrapPart() {
		IWorkbenchPage page = getSite().getPage();
		if (page != null) {
			return page.getActiveEditor();
		} else {
			return null;
		}
	}

	/**
	 * Returns whether the given part should be added to this view.
	 *
	 * @param part the input part
	 * @return <code>true</code> if the part is relevant, and <code>false</code>
	 *         otherwise
	 */
	@Override
	protected boolean isImportant(IWorkbenchPart part) {
		return (part instanceof IEditorPart);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.part.PageBookView#getAdapter(java.lang.Class)
	 */
	@Override
	public Object getAdapter(Class key) {
		if (key == IContributedContentsView.class) {
			return new IContributedContentsView() {

				@Override
				public IWorkbenchPart getContributingPart() {
					return getCurrentContributingPart();
				}
			};
		}
		return super.getAdapter(key);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.part.PageBookView#partBroughtToTop(org.eclipse.ui.
	 * IWorkbenchPart)
	 */
	@Override
	public void partBroughtToTop(IWorkbenchPart part) {
		super.partBroughtToTop(part);
		partActivated(part);
	}
}
