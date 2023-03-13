/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
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
package org.eclipse.birt.report.designer.internal.ui.editors.breadcrumb;

import org.eclipse.birt.report.designer.internal.ui.editors.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.actions.ActionGroup;

/**
 * The editor breadcrumb shows the parent chain of the active editor item inside
 * a {@link BreadcrumbViewer}.
 *
 * <p>
 * Clients must implement the abstract methods.
 * </p>
 *
 * @since 2.6.2
 */
public abstract class EditorBreadcrumb implements IBreadcrumb {

//	private static final String ACTIVE_TAB_BG_END = "org.eclipse.ui.workbench.ACTIVE_TAB_BG_END"; //$NON-NLS-1$

	private GraphicalEditorWithFlyoutPalette fEditor;
	// private ITextViewer fTextViewer;

	protected BreadcrumbViewer fBreadcrumbViewer;

//	private boolean fHasFocus;
	private boolean fIsActive;

	private Composite fComposite;

//	private Listener fDisplayFocusListener;
//	private Listener fDisplayKeyListener;

//	private IPropertyChangeListener fPropertyChangeListener;
//
//	private IPartListener fPartListener;

	private MenuManager menuManager;

	/**
	 * The editor inside which this breadcrumb is shown.
	 *
	 * @param editor the editor
	 */
	public EditorBreadcrumb(GraphicalEditorWithFlyoutPalette editor) {
		setEditor(editor);
	}

	/**
	 * The active element of the editor.
	 *
	 * @return the active element of the editor, or <b>null</b> if none
	 */
	protected abstract Object getCurrentInput();

	/**
	 * Create and configure the viewer used to display the parent chain.
	 *
	 * @param parent the parent composite
	 * @return the viewer
	 */
	protected abstract BreadcrumbViewer createViewer(Composite parent);

	/**
	 * Reveal the given element in the editor if possible.
	 *
	 * @param element the element to reveal
	 * @return true if the element could be revealed
	 */
	protected abstract boolean reveal(Object element);

	/**
	 * Open the element in a new editor if possible.
	 *
	 * @param element the element to open
	 * @return true if the element could be opened
	 */
	protected abstract boolean open(Object element);

	/**
	 * Create an action group for the context menu shown for the selection of the
	 * given selection provider or <code>null</code> if no context menu should be
	 * shown.
	 *
	 * @param selectionProvider the provider of the context selection
	 * @return action group to use to fill the context menu or <code>null</code>
	 */
	protected abstract ActionGroup createContextMenuActionGroup(ISelectionProvider selectionProvider);

	/**
	 * The breadcrumb has been activated. Implementors must retarget the editor
	 * actions to the breadcrumb aware actions.
	 */
	protected abstract void activateBreadcrumb();

	/**
	 * The breadcrumb has been deactivated. Implementors must retarget the
	 * breadcrumb actions to the editor actions.
	 */
	protected abstract void deactivateBreadcrumb();

	@Override
	public ISelectionProvider getSelectionProvider() {
		return fBreadcrumbViewer;
	}

	/*
	 * @see org.eclipse.jdt.internal.ui.javaeditor.IBreadcrumb#setInput(java.lang
	 * .Object)
	 */
	@Override
	public void setInput(Object element) {
		if (element == null) {
			return;
		}

		Object input = fBreadcrumbViewer.getInput();
		if (input == element || element.equals(input) || fBreadcrumbViewer.isDropDownOpen()) {
			return;
		}

		fBreadcrumbViewer.setInput(element);
	}

	/*
	 * @see org.eclipse.jdt.internal.ui.javaeditor.IBreadcrumb#setFocus()
	 */
	@Override
	public void activate() {
		if (fBreadcrumbViewer.getSelection().isEmpty()) {
			fBreadcrumbViewer.setSelection(new StructuredSelection(fBreadcrumbViewer.getInput()));
//		fBreadcrumbViewer.setFocus( );
		}
	}

	/*
	 * @see org.eclipse.jdt.internal.ui.javaeditor.breadcrumb.IBreadcrumb#isActive()
	 */
	@Override
	public boolean isActive() {
		return fIsActive;
	}

	/*
	 * @see org.eclipse.jdt.internal.ui.javaeditor.IBreadcrumb#createContent(org.
	 * eclipse.swt.widgets.Composite)
	 */
	@Override
	public Control createContent(Composite parent) {
		Assert.isTrue(fComposite == null, "Content must only be created once."); //$NON-NLS-1$

		boolean rtl = (getEditor().getSite().getShell().getStyle() & SWT.RIGHT_TO_LEFT) != 0;

		fComposite = new Composite(parent, rtl ? SWT.RIGHT_TO_LEFT : SWT.NONE);
		GridData data = new GridData(SWT.FILL, SWT.TOP, true, false);
		fComposite.setLayoutData(data);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.horizontalSpacing = 0;
		fComposite.setLayout(gridLayout);

//		fDisplayFocusListener = new Listener( ) {
//
//			public void handleEvent( Event event )
//			{
//				if ( isBreadcrumbEvent( event ) )
//				{
//					if ( fHasFocus )
//						return;
//
//					fIsActive = true;
//
//					focusGained( );
//				}
//				else
//				{
//					if ( !fIsActive )
//						return;
//
//					boolean hasTextFocus = fEditor.getGraphicalViewer( )
//							.getControl( )
//							.isFocusControl( );
//					if ( hasTextFocus )
//					{
//						fIsActive = false;
//					}
//
//					if ( !fHasFocus )
//						return;
//
//					focusLost( );
//				}
//			}
//		};
//		Display.getCurrent( ).addFilter( SWT.FocusIn, fDisplayFocusListener );

		fBreadcrumbViewer = createViewer(fComposite);
		fBreadcrumbViewer.getControl().setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));

		fBreadcrumbViewer.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {
				doOpen(event.getSelection());
			}
		});

		fBreadcrumbViewer.addOpenListener(new IOpenListener() {

			@Override
			public void open(OpenEvent event) {
				doRevealOrOpen(event.getSelection());
			}
		});

		fBreadcrumbViewer.addMenuDetectListener(new MenuDetectListener() {

			@Override
			public void menuDetected(MenuDetectEvent event) {
				ISelectionProvider selectionProvider;
				if (fBreadcrumbViewer.isDropDownOpen()) {
					selectionProvider = fBreadcrumbViewer.getDropDownSelectionProvider();
				} else {
					selectionProvider = fBreadcrumbViewer;
				}

				if (getMenuManager() == null) {
					menuManager = new MenuManager();

					Object element = selectionProvider.getSelection();
					if (selectionProvider.getSelection() instanceof StructuredSelection) {
						element = ((StructuredSelection) selectionProvider.getSelection()).getFirstElement();
					}

					createContextMenu(element, menuManager);

					if (menuManager.isEmpty()) {
						return;
					}

					getEditor().getEditorSite().registerContextMenu(menuManager, selectionProvider, false);
				}

				Menu menu = menuManager.createContextMenu(fBreadcrumbViewer.getControl());
				menu.setLocation(event.x + 10, event.y + 10);
				menu.setVisible(true);
				while (!menu.isDisposed() && menu.isVisible()) {
					if (!menu.getDisplay().readAndDispatch()) {
						menu.getDisplay().sleep();
					}
				}

			}
		});

//		fPropertyChangeListener = new IPropertyChangeListener( ) {
//
//			public void propertyChange( PropertyChangeEvent event )
//			{
//				if ( ACTIVE_TAB_BG_END.equals( event.getProperty( ) ) )
//				{
//					if ( fComposite.isFocusControl( ) )
//					{
//						fComposite.setBackground( JFaceResources.getColorRegistry( )
//								.get( ACTIVE_TAB_BG_END ) );
//					}
//				}
//			}
//		};
//		JFaceResources.getColorRegistry( )
//				.addListener( fPropertyChangeListener );

		return fComposite;
	}

	protected MenuManager getMenuManager() {
		return menuManager;
	}

	public void setMenuManager(MenuManager menuManager) {
		this.menuManager = menuManager;
	}

	abstract protected void createContextMenu(Object element, MenuManager manager);

	/*
	 * @see org.eclipse.jdt.internal.ui.javaeditor.IEditorViewPart#dispose()
	 */
	@Override
	public void dispose() {
//		if ( fPropertyChangeListener != null )
//		{
//			JFaceResources.getColorRegistry( )
//					.removeListener( fPropertyChangeListener );
//		}
//		if ( fDisplayFocusListener != null )
//		{
//			Display.getDefault( ).removeFilter( SWT.FocusIn,
//					fDisplayFocusListener );
//		}
//		deinstallDisplayListeners( );
//		if ( fPartListener != null )
//		{
//			getEditor( ).getSite( )
//					.getPage( )
//					.removePartListener( fPartListener );
//		}

		setEditor(null);
	}

	/**
	 * Either reveal the selection in the editor or open the selection in a new
	 * editor. If both fail open the child pop up of the selected element.
	 *
	 * @param selection the selection to open
	 */
	private void doRevealOrOpen(ISelection selection) {
		if (doReveal(selection)) {
			fEditor.getGraphicalViewer().getControl().setFocus();
		} else if (doOpen(selection)) {
			fIsActive = false;
//			focusLost( );
			fBreadcrumbViewer.setInput(getCurrentInput());
		}
	}

	private boolean doOpen(ISelection selection) {
		if (!(selection instanceof StructuredSelection)) {
			return false;
		}

		StructuredSelection structuredSelection = (StructuredSelection) selection;
		if (structuredSelection.isEmpty()) {
			return false;
		}

		return open(structuredSelection.getFirstElement());
	}

	private boolean doReveal(ISelection selection) {
		if (!(selection instanceof StructuredSelection)) {
			return false;
		}

		StructuredSelection structuredSelection = (StructuredSelection) selection;
		if (structuredSelection.isEmpty()) {
			return false;
		}

		return reveal(structuredSelection.getFirstElement());

	}

	/**
	 * Focus has been transfered into the breadcrumb.
	 */
//	private void focusGained( )
//	{
//		if ( fHasFocus )
//			focusLost( );
//
//		fComposite.setBackground( JFaceResources.getColorRegistry( )
//				.get( ACTIVE_TAB_BG_END ) );
//		fHasFocus = true;
//
//		installDisplayListeners( );
//
//		activateBreadcrumb( );
//
//		getEditor( ).getEditorSite( ).getActionBars( ).updateActionBars( );
//
//	}

	/**
	 * Focus has been revoked from the breadcrumb.
	 */
//	private void focusLost( )
//	{
//		fComposite.setBackground( null );
//		fHasFocus = false;
//
//		deinstallDisplayListeners( );
//
//		deactivateBreadcrumb( );
//
//		getEditor( ).getEditorSite( ).getActionBars( ).updateActionBars( );
//
//	}

	/**
	 * Installs all display listeners.
	 */
//	private void installDisplayListeners( )
//	{
//		// Sanity check
//		deinstallDisplayListeners( );
//
//		fDisplayKeyListener = new Listener( ) {
//
//			public void handleEvent( Event event )
//			{
//				if ( event.keyCode != SWT.ESC )
//					return;
//
//				if ( !isBreadcrumbEvent( event ) )
//					return;
//
//				fEditor.getGraphicalViewer( ).getControl( ).setFocus( );
//			}
//		};
//		Display.getDefault( ).addFilter( SWT.KeyDown, fDisplayKeyListener );
//	}

	/**
	 * Removes all previously installed display listeners.
	 */
//	private void deinstallDisplayListeners( )
//	{
//		if ( fDisplayKeyListener != null )
//		{
//			Display.getDefault( ).removeFilter( SWT.KeyDown,
//					fDisplayKeyListener );
//			fDisplayKeyListener = null;
//		}
//	}

	/**
	 * Tells whether the given event was issued inside the breadcrumb viewer's
	 * control.
	 *
	 * @param event the event to inspect
	 * @return <code>true</code> if event was generated by a breadcrumb child
	 */
//	private boolean isBreadcrumbEvent( Event event )
//	{
//		if ( fBreadcrumbViewer == null )
//			return false;
//
//		Widget item = event.widget;
//		if ( !( item instanceof Control ) )
//			return false;
//
//		Shell dropDownShell = fBreadcrumbViewer.getDropDownShell( );
//		if ( dropDownShell != null && isChild( (Control) item, dropDownShell ) )
//			return true;
//
//		return isChild( (Control) item, fBreadcrumbViewer.getControl( ) );
//	}

//	private boolean isChild( Control child, Control parent )
//	{
//		if ( child == null )
//			return false;
//
//		if ( child == parent )
//			return true;
//
//		return isChild( child.getParent( ), parent );
//	}

	/**
	 * Sets the text editor for which this breadcrumb is.
	 *
	 * @param editor the editor to be used
	 */
	protected void setEditor(GraphicalEditorWithFlyoutPalette editor) {
		fEditor = editor;

		if (fEditor == null) {
		}

//		fPartListener = new IPartListener( ) {
//
//			public void partActivated( IWorkbenchPart part )
//			{
//				if ( part == fEditor && fHasFocus )
//				{
//					// focus-in event comes before part activation and the
//					// workbench activates the editor -> reactivate the
//					// breadcrumb
//					// if it is the active part.
//					focusGained( );
//				}
//			}
//
//			public void partBroughtToTop( IWorkbenchPart part )
//			{
//			}
//
//			public void partClosed( IWorkbenchPart part )
//			{
//
//			}
//
//			public void partDeactivated( IWorkbenchPart part )
//			{
//				if ( part == fEditor && fHasFocus )
//				{
//					focusLost( );
//				}
//			}
//
//			public void partOpened( IWorkbenchPart part )
//			{
//			}
//
//		};
//		fEditor.getSite( ).getPage( ).addPartListener( fPartListener );
	}

	/**
	 * This breadcrumb's text editor.
	 *
	 * @return the text editor
	 */
	protected GraphicalEditorWithFlyoutPalette getEditor() {
		return fEditor;
	}

}
