
package org.eclipse.birt.report.designer.ui.views.attributes;

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

public class AttributeView extends PageBookView {

	/**
	 * AttributeView ID
	 */
	public static final String ID = "org.eclipse.birt.report.designer.ui.attributes.AttributeView";//$NON-NLS-1$

	private String defaultText = Messages.getString("AttributeView.defaultText.noAttributeView"); //$NON-NLS-1$

	private String defaultPartName = null;

	public AttributeView() {
		super();
	}

	protected IPage createDefaultPage(PageBook book) {
		MessagePage page = new MessagePage();
		initPage(page);
		page.createControl(book);
		page.setMessage(defaultText);
		if (defaultPartName == null) {
			defaultPartName = getPartName();
		}
		return page;
	}

	public void setPartName(String name) {
		super.setPartName(name);
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
		Object page = part.getAdapter(IAttributeViewPage.class);
		if (page instanceof IPageBookViewPage) {
			initPage((IPageBookViewPage) page);

			((IPageBookViewPage) page).createControl(getPageBook());

			return new PageRec(part, (IPageBookViewPage) page);
		}
		return null;
	}

	/**
	 * Destroys a page in the pagebook for a particular part. This page was returned
	 * as a result from <code>doCreatePage</code>.
	 * 
	 * @param part       the input part
	 * @param pageRecord a page record for the part
	 * @see #doCreatePage
	 */
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
	 * @see org.eclipse.ui.part.PageBookView#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class key) {
		if (key == IContributedContentsView.class) {
			return new IContributedContentsView() {

				public IWorkbenchPart getContributingPart() {
					return getCurrentContributingPart();
				}
			};
		}
		return super.getAdapter(key);
	}

	public void partClosed(IWorkbenchPart part) {
		super.partClosed(part);
		if (defaultPartName != null) {
			setPartName(defaultPartName);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.ui.part.PageBookView#partBroughtToTop(org.eclipse.ui.
	 * IWorkbenchPart)
	 */
	public void partBroughtToTop(IWorkbenchPart part) {
		super.partBroughtToTop(part);
		partActivated(part);
	}

}
