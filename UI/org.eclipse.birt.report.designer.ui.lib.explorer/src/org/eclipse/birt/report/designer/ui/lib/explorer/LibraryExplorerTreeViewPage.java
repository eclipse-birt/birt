/*************************************************************************************
 * Copyright (c) 2004-2008 Actuate Corporation and others.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.ui.lib.explorer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.core.preference.IPreferenceChangeListener;
import org.eclipse.birt.core.preference.PreferenceChangeEvent;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.resourcelocator.FragmentResourceEntry;
import org.eclipse.birt.report.designer.internal.ui.resourcelocator.PathResourceEntry;
import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceEntry;
import org.eclipse.birt.report.designer.internal.ui.views.ViewsTreeProvider;
import org.eclipse.birt.report.designer.internal.ui.views.outline.ItemSorter;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.lib.explorer.action.ResourceAction;
import org.eclipse.birt.report.designer.ui.lib.explorer.dnd.LibraryDragListener;
import org.eclipse.birt.report.designer.ui.lib.explorer.resource.ReportElementEntry;
import org.eclipse.birt.report.designer.ui.lib.explorer.resource.ResourceEntryWrapper;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.designer.ui.views.IReportResourceChangeEvent;
import org.eclipse.birt.report.designer.ui.views.IReportResourceChangeListener;
import org.eclipse.birt.report.designer.ui.views.IReportResourceSynchronizer;
import org.eclipse.birt.report.designer.ui.widget.TreeViewerBackup;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.EmbeddedImageHandle;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ParameterGroupHandle;
import org.eclipse.birt.report.model.api.ParameterHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.birt.report.model.api.command.ResourceChangeEvent;
import org.eclipse.birt.report.model.api.core.IResourceChangeListener;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.birt.report.model.api.validators.IValidationListener;
import org.eclipse.birt.report.model.api.validators.ValidationEvent;
import org.eclipse.core.runtime.Platform;
import org.eclipse.gef.dnd.TemplateTransfer;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;

/**
 * This class represents the tree view page of the data view
 * 
 */
public class LibraryExplorerTreeViewPage extends LibraryExplorerViewPage implements IValidationListener,
		IPreferenceChangeListener, IResourceChangeListener, IReportResourceChangeListener {

	// private static final String LABEL_DOUBLE_CLICK = Messages.getString(
	// "DataViewTreeViewerPage.tooltip.DoubleClickToEdit" ); //$NON-NLS-1$

	private static final String BUNDLE_PROTOCOL = "bundleresource://"; //$NON-NLS-1$

	private TreeViewerBackup libraryBackup;

	private LibraryExplorerContextMenuProvider menuManager = null;

	/**
	 * this flag is used to filter duplicate resoruce refreshing events
	 */
	private boolean allowRefreshing = true;

	public LibraryExplorerTreeViewPage() {
		super();
		SessionHandleAdapter.getInstance().getSessionHandle().addResourceChangeListener(this);

		IReportResourceSynchronizer synchronizer = ReportPlugin.getDefault().getResourceSynchronizerService();

		if (synchronizer != null) {
			synchronizer.addListener(IReportResourceChangeEvent.NewResource
					| IReportResourceChangeEvent.LibraySaveChange | IReportResourceChangeEvent.DataDesignSaveChange,
					this);
		}
	}

	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		initPage();
		refreshRoot();
	}

	/**
	 * Creates the tree view
	 * 
	 * @param parent the parent
	 */
	protected TreeViewer createTreeViewer(Composite parent) {
		TreeViewer treeViewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);

		treeViewer.setSorter(new ItemSorter() {

			@Override
			public int compare(Viewer viewer, Object e1, Object e2) {
				if (e1 instanceof ReportElementEntry || e2 instanceof ReportElementEntry) {
					if (e1 instanceof ReportElementEntry)
						e1 = ((ReportElementEntry) e1).getReportElement();
					if (e2 instanceof ReportElementEntry)
						e2 = ((ReportElementEntry) e2).getReportElement();
				} else if (e1 instanceof ResourceEntry && e2 instanceof ResourceEntry) {
					ResourceEntry entry1 = (ResourceEntry) e1;
					ResourceEntry entry2 = (ResourceEntry) e2;

					if (entry1 == null || entry2 == null) {
						return entry1 == null ? -1 : 1;
					}

					boolean isEntry1File = entry1.isFile();
					boolean isEntry2File = entry2.isFile();

					if (isEntry1File == isEntry2File) {
						String name1 = entry1.getName();
						String name2 = entry2.getName();

						if (name1 != null && name2 != null) {
							return name1.toLowerCase().compareTo(name2.toLowerCase());
						} else {
							return name1 == null ? -1 : 1;
						}
					}
					return isEntry1File ? 1 : -1;
				}
				return super.compare(viewer, e1, e2);
			}

		});
		configTreeViewer(treeViewer);
		return treeViewer;
	}

	/**
	 * Configures the tree viewer.
	 * 
	 * @param treeViewer the tree viewer to config.
	 */
	protected void configTreeViewer(final TreeViewer treeViewer) {
		ViewsTreeProvider provider = (ViewsTreeProvider) ElementAdapterManager.getAdapter(this,
				ViewsTreeProvider.class);
		if (provider == null)
			provider = new LibraryExplorerProvider();

		treeViewer.setContentProvider(provider);
		treeViewer.setLabelProvider(provider);

		// Adds drag and drop support
		int ops = DND.DROP_COPY | DND.DROP_LINK;
		Transfer[] transfers = new Transfer[] { TemplateTransfer.getInstance() };
		treeViewer.addDragSupport(ops, transfers, new LibraryDragListener(treeViewer));

		treeViewer.getControl().addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent e) {

			}

			public void keyReleased(KeyEvent e) {
				if (e.keyCode == SWT.F5) {
					treeViewer.refresh();
				}
			}
		});

		treeViewer.getTree().addDisposeListener(new DisposeListener() {

			public void widgetDisposed(DisposeEvent e) {
				Object input = treeViewer.getInput();
				if (input instanceof Object[]) {
					Object[] array = (Object[]) input;
					for (int i = 0; i < array.length; i++) {
						if (array[i] instanceof ResourceEntry)
							((ResourceEntry) array[i]).dispose();
					}

				}
			}

		});

		TreeListener libraryTreeListener = new TreeListener() {

			public void treeCollapsed(TreeEvent e) {
				Item item = (Item) e.item;
				if (libraryBackup != null)
					libraryBackup.updateCollapsedStatus(treeViewer, item.getData());

			}

			public void treeExpanded(TreeEvent e) {
				Item item = (Item) e.item;
				if (libraryBackup != null)
					libraryBackup.updateExpandedStatus(treeViewer, item.getData());
			}

		};
		treeViewer.getTree().addTreeListener(libraryTreeListener);
		treeViewer.addDoubleClickListener(new IDoubleClickListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.jface.viewers.IDoubleClickListener#doubleClick(org
			 * .eclipse.jface.viewers.DoubleClickEvent)
			 */
			public void doubleClick(DoubleClickEvent event) {
				handleDoubleClick(event);
			}
		});

		treeViewer.addOpenListener(new IOpenListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.jface.viewers.IDoubleClickListener#doubleClick(org
			 * .eclipse.jface.viewers.DoubleClickEvent)
			 */
			public void open(OpenEvent event) {
				try {
					handleOpen(event);
				} catch (IOException e) {
					ExceptionUtil.handle(e);
				}
			}
		});
	}

	/**
	 * Handles a double-click event from the viewer.
	 * 
	 * @param event the double-click event
	 */
	protected void handleDoubleClick(DoubleClickEvent event) {
		IStructuredSelection selection = (IStructuredSelection) event.getSelection();
		Object element = selection.getFirstElement();
		TreeViewer viewer = getTreeViewer();

		if (element instanceof ResourceEntryWrapper) {
			switch (((ResourceEntryWrapper) element).getType()) {
			case ResourceEntryWrapper.LIBRARY:
				return;

			case ResourceEntryWrapper.CSS_STYLE_SHEET:
			default:
				break;
			}
		}

		if (viewer.isExpandable(element)) {
			viewer.setExpandedState(element, !viewer.getExpandedState(element));
		}
	}

	/**
	 * Handles an open event from the viewer. Opens an editor on the selected
	 * library.
	 * 
	 * @param event the open event
	 * @throws IOException if an I/O error occurs.
	 */
	protected void handleOpen(OpenEvent event) throws IOException {
		IStructuredSelection selection = (IStructuredSelection) event.getSelection();
		Object element = selection.getFirstElement();

		if (element instanceof ResourceEntryWrapper && ((ResourceEntryWrapper) element).isFile()) {
			switch (((ResourceEntryWrapper) element).getType()) {
			case ResourceEntryWrapper.RPTDESIGN:
			case ResourceEntryWrapper.LIBRARY:
				File file = null;
				URL url = ((ResourceEntryWrapper) element).getURL();

				if (((ResourceEntryWrapper) element).getEntry() instanceof FragmentResourceEntry) {
					file = ResourceAction.convertToFile(
							Platform.getBundle(IResourceLocator.FRAGMENT_RESOURCE_HOST).getEntry(url.getPath()));
				} else {
					file = ResourceAction.convertToFile(url);
				}

				if (file != null && file.exists() && file.isFile()) {
					if (((ResourceEntryWrapper) element).getType() == ResourceEntryWrapper.LIBRARY) {
						ResourceAction.openLibrary(this, file, false);
					} else if (((ResourceEntryWrapper) element).getType() == ResourceEntryWrapper.RPTDESIGN) {
						ResourceAction.openDesigner(this, file, false);
					}
				} else {
					if (((ResourceEntryWrapper) element).getType() == ResourceEntryWrapper.LIBRARY) {
						if (MessageDialog.openConfirm(getSite().getShell(),
								Messages.getString("LibraryNotExist.Dialog.Title"), //$NON-NLS-1$
								Messages.getString("LibraryNotExist.Dialog.Message"))) //$NON-NLS-1$
						{
							refreshRoot();
						}
					} else if (((ResourceEntryWrapper) element).getType() == ResourceEntryWrapper.RPTDESIGN) {
						if (MessageDialog.openConfirm(getSite().getShell(),
								Messages.getString("DesignerNotExist.Dialog.Title"), //$NON-NLS-1$
								Messages.getString("DesignerNotExist.Dialog.Message"))) //$NON-NLS-1$
						{
							refreshRoot();
						}
					}
				}
				break;

			case ResourceEntryWrapper.CSS_STYLE_SHEET:
			default:
				break;
			}
		}
	}

	/**
	 * Initializes the data view page.
	 */
	protected void initPage() {
		createContextMenus();

		// !remove sorter to keep same order with outline view
		// treeViewer.setSorter( new ViewerSorter( ) {
		//
		// public int category( Object element )
		// {
		// if ( element instanceof LibraryHandle )
		// {
		// return 1;
		// }
		// return super.category( element );
		// }
		//
		// } );

		final Tree tree = getTreeViewer().getTree();

		tree.addMouseTrackListener(new MouseTrackAdapter() {

			public void mouseHover(MouseEvent event) {
				Widget widget = event.widget;
				if (widget == tree) {
					Point pt = new Point(event.x, event.y);
					TreeItem item = tree.getItem(pt);

					try {
						tree.setToolTipText(getTooltip(item));
					} catch (IOException e) {
						// Does nothing
					}
				}
			}
		});
	}

	/**
	 * Creates the context menu
	 */
	private void createContextMenus() {
		menuManager = new LibraryExplorerContextMenuProvider(this);

		Control control = getTreeViewer().getControl();
		Menu menu = menuManager.createContextMenu(control);

		control.setMenu(menu);

		getSite().registerContextMenu("org.eclipse.birt.report.designer.ui.lib.explorer.view", menuManager, //$NON-NLS-1$
				getSite().getSelectionProvider());
	}

	private String getTooltip(TreeItem item) throws IOException {
		if (item != null) {
			Object object = item.getData();
			if (object instanceof DataSourceHandle || object instanceof DataSetHandle) {
				return Messages.getString("LibraryExplorerTreeViewPage.toolTips.DragAndDropOutline"); //$NON-NLS-1$
			} else if (object instanceof ThemeHandle) {
				return Messages.getString("LibraryExplorerTreeViewPage.toolTips.DragAndDropLayout"); //$NON-NLS-1$
			} else if (object instanceof ParameterHandle || object instanceof ParameterGroupHandle
					|| object instanceof EmbeddedImageHandle || object instanceof ReportItemHandle) {
				return Messages.getString("LibraryExplorerTreeViewPage.toolTips.DragAndDropToOutlineORLayout"); //$NON-NLS-1$
			} else if (object instanceof LibraryHandle) {
				return ((LibraryHandle) object).getFileName();
			} else if (object instanceof CssStyleSheetHandle) {
				CssStyleSheetHandle CssStyleSheetHandle = (CssStyleSheetHandle) object;
				if (CssStyleSheetHandle.getFileName().startsWith(BUNDLE_PROTOCOL)) {
					return CssStyleSheetHandle.getFileName();
				} else {
					ModuleHandle moudleHandle = CssStyleSheetHandle.getModule().getModuleHandle();
					URL url = moudleHandle.findResource(CssStyleSheetHandle.getFileName(),
							IResourceLocator.CASCADING_STYLE_SHEET);

					if (url != null) {
						return ResourceAction.convertToFile(url).getAbsolutePath();
					}
				}
			} else if (object instanceof ResourceEntryWrapper) {
				URL url = ((ResourceEntryWrapper) object).getURL();
				File file = null;

				if (((ResourceEntryWrapper) object).getParent() instanceof FragmentResourceEntry) {
					file = ResourceAction.convertToFile(
							Platform.getBundle(IResourceLocator.FRAGMENT_RESOURCE_HOST).getEntry(url.getPath()));
				} else {
					file = ResourceAction.convertToFile(url);
				}
				return file == null ? null : file.getAbsolutePath();
			} else if (object instanceof ResourceEntryWrapper
					&& ((ResourceEntryWrapper) object).getType() == ResourceEntryWrapper.LIBRARY) {
				LibraryHandle libHandle = (LibraryHandle) ((ResourceEntryWrapper) object)
						.getAdapter(LibraryHandle.class);

				return libHandle.getFileName();
			} else if (object instanceof ResourceEntryWrapper
					&& ((ResourceEntryWrapper) object).getType() == ResourceEntryWrapper.CSS_STYLE_SHEET) {
				CssStyleSheetHandle cssHandle = (CssStyleSheetHandle) ((ResourceEntryWrapper) object)
						.getAdapter(CssStyleSheetHandle.class);

				if (cssHandle.getFileName().startsWith(BUNDLE_PROTOCOL)) {
					return cssHandle.getFileName();
				} else {
					ModuleHandle moudleHandle = cssHandle.getModule().getModuleHandle();
					URL url = moudleHandle.findResource(cssHandle.getFileName(),
							IResourceLocator.CASCADING_STYLE_SHEET);
					if (url != null) {
						return ResourceAction.convertToFile(url).getAbsolutePath();
					}
				}
			} else if (object instanceof PathResourceEntry) {
				URL url = ((PathResourceEntry) object).getURL();

				return ResourceAction.convertToFile(url).getAbsolutePath();
			} else if (object instanceof FragmentResourceEntry) {
				URL url = ((FragmentResourceEntry) object).getURL();

				return ResourceAction
						.convertToFile(
								Platform.getBundle(IResourceLocator.FRAGMENT_RESOURCE_HOST).getEntry(url.getPath()))
						.getAbsolutePath();
			}
		}
		return null;
	}

	/**
	 * The <code>Page</code> implementation of this <code>IPage</code> method
	 * disposes of this page's control (if it has one and it has not already been
	 * disposed). Disposes the visitor of the element
	 */
	public void dispose() {
		SessionHandleAdapter.getInstance().getSessionHandle().removeResourceChangeListener(this);

		IReportResourceSynchronizer synchronizer = ReportPlugin.getDefault().getResourceSynchronizerService();

		if (synchronizer != null) {
			synchronizer.removeListener(IReportResourceChangeEvent.NewResource
					| IReportResourceChangeEvent.LibraySaveChange | IReportResourceChangeEvent.DataDesignSaveChange,
					this);
		}

		libraryBackup.dispose();

		if (menuManager != null) {
			menuManager.dispose();
			menuManager = null;
		}
		super.dispose();
	}

	protected boolean isDisposed() {
		Control ctrl = getControl();
		return (ctrl == null || ctrl.isDisposed());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.api.validators.IValidationListener#
	 * elementValidated(org.eclipse.birt.report.model.api.DesignElementHandle,
	 * org.eclipse.birt.report.model.api.validators.ValidationEvent)
	 */
	public void elementValidated(DesignElementHandle targetElement, ValidationEvent ev) {
		TreeViewer treeViewer = getTreeViewer();

		if (treeViewer != null && !treeViewer.getTree().isDisposed()) {
			treeViewer.refresh();
			treeViewer.setInput(getRootEntries());
			handleTreeViewerRefresh();
		}
	}

	private void handleTreeViewerRefresh() {
		TreeViewer treeViewer = getTreeViewer();

		if (libraryBackup != null) {
			libraryBackup.restoreBackup(treeViewer);
		} else {
			libraryBackup = new TreeViewerBackup();
			treeViewer.expandToLevel(2);
			libraryBackup.updateStatus(treeViewer);
		}
	}

	public void refreshRoot() {
		TreeViewer treeViewer = getTreeViewer();

		if (treeViewer != null && !treeViewer.getTree().isDisposed()) {
			ISelection selection = getSelection();

			treeViewer.setSelection(null);
			treeViewer.setInput(getRootEntries());
			handleTreeViewerRefresh();
			if (selection != null) {
				setSelection(selection);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.core.runtime.preferences.
	 * IEclipsePreferences$IPreferenceChangeListener #preferenceChange(org.eclipse
	 * .core.runtime.preferences.IEclipsePreferences.PreferenceChangeEvent)
	 */
	public void preferenceChange(PreferenceChangeEvent event) {
		if (event.getKey().equals(PreferenceChangeEvent.SPECIALTODEFAULT)
				|| ReportPlugin.RESOURCE_PREFERENCE.equals(event.getKey()))
			Display.getDefault().asyncExec(new Runnable() {

				public void run() {
					refreshRoot();
				}
			});
	}

	public void resourceChanged(IReportResourceChangeEvent event) {
		if (event.getType() != IReportResourceChangeEvent.NewResource
				&& event.getType() != IReportResourceChangeEvent.LibraySaveChange
				&& event.getType() != IReportResourceChangeEvent.DataDesignSaveChange) {
			return;
		}
		if (event.getSource() == this) {
			// filter events by self
			return;
		}

		if (allowRefreshing) {
			Display.getDefault().asyncExec(new Runnable() {

				public void run() {
					if (!isDisposed()) {
						refreshRoot();
					}

					// TODO more accurate refreshing control

					allowRefreshing = true;
				}
			});
		}

		allowRefreshing = false;
	}

	public void resourceChanged(ModuleHandle module, ResourceChangeEvent event) {
		if (isDisposed()) {
			return;
		}

		String path = event.getChangedResourcePath();

		if (path != null) {
			File file = new File(path);
			String resourcePath = ReportPlugin.getDefault().getResourceFolder();

			File resource = new File(resourcePath);

			if (file.exists() && resource.exists()
					&& file.toURI().toString().indexOf(resource.toURI().toString()) > -1) {
				refreshRoot();
			}
		}
	}

	/**
	 * Sets selections for the specified tree viewer and optionally makes it
	 * visible.
	 * 
	 * @param treeViewer the specified tree viewer to select.
	 * @param paths      the specified paths to select.
	 */
	public void selectPath(final String[] paths, final boolean forceRefresh) {
		if (paths == null || paths.length <= 0) {
			return;
		}

		Display display = getSite().getShell().getDisplay();

		display.asyncExec(new Runnable() {

			public void run() {
				TreeViewer treeViewer = getTreeViewer();
				boolean needSelect = false;

				if (forceRefresh) {
					refreshRoot();
				}

				for (String path : paths) {
					File file = new File(path);

					if (!file.exists()) {
						continue;
					}

					needSelect = true;
					String parent = file.getParent();
					List<String> folders = new ArrayList<String>();

					while (parent != null) {
						folders.add(parent);
						parent = new File(parent).getParent();
					}

					for (int i = folders.size() - 1; i >= 0; i--) {
						treeViewer.expandToLevel(folders.get(i), 1);
					}
				}
				if (needSelect) {
					treeViewer.setSelection(new StructuredSelection(paths));
					treeViewer.reveal(paths[0]);
				}
			}
		});
	}

	private ResourceEntry[] getRootEntries() {
		ResourceEntry systemResource = new FragmentResourceEntry();

		ResourceEntry sharedResource = (ResourceEntry) ElementAdapterManager.getAdapter(this, ResourceEntry.class);

		if (sharedResource == null)
			sharedResource = new PathResourceEntry();

		// System Resources node should not be shown if no file is contained in
		// this node.
		if (systemResource.hasChildren()) {
			return new ResourceEntry[] { systemResource, sharedResource };
		} else {
			return new ResourceEntry[] { sharedResource };
		}
	}

}
