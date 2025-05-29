/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
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

package org.eclipse.birt.report.designer.internal.ui.views.outline;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.designer.core.mediator.IMediator;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.util.mediator.request.IRequestConverter;
import org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest;
import org.eclipse.birt.report.designer.internal.ui.editors.EditorUtil;
import org.eclipse.birt.report.designer.internal.ui.editors.ReportColorConstants;
import org.eclipse.birt.report.designer.internal.ui.editors.parts.event.IModelEventProcessor;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.IReportPageBookViewPage;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.AbstractModelEventProcessor.IModelEventFactory;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportEventRunnable;
import org.eclipse.birt.report.designer.internal.ui.extension.ExtendedElementUIPoint;
import org.eclipse.birt.report.designer.internal.ui.extension.ExtensionPointManager;
import org.eclipse.birt.report.designer.internal.ui.extension.experimental.EditpartExtensionManager;
import org.eclipse.birt.report.designer.internal.ui.extension.experimental.PaletteEntryExtension;
import org.eclipse.birt.report.designer.internal.ui.views.DesignerOutlineEventProcessor;
import org.eclipse.birt.report.designer.internal.ui.views.NonGEFSynchronizerWithTreeView;
import org.eclipse.birt.report.designer.internal.ui.views.RenameListener;
import org.eclipse.birt.report.designer.internal.ui.views.ViewContextMenuProvider;
import org.eclipse.birt.report.designer.internal.ui.views.ViewsTreeProvider;
import org.eclipse.birt.report.designer.internal.ui.views.actions.GlobalActionFactory;
import org.eclipse.birt.report.designer.internal.ui.views.actions.ImportLibraryAction;
import org.eclipse.birt.report.designer.internal.ui.views.actions.SearchAction;
import org.eclipse.birt.report.designer.internal.ui.views.outline.dnd.DesignerDragListener;
import org.eclipse.birt.report.designer.internal.ui.views.outline.dnd.DesignerDropListener;
import org.eclipse.birt.report.designer.internal.ui.views.outline.dnd.IDropConstraint;
import org.eclipse.birt.report.designer.ui.editors.IReportEditorContants;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.views.INodeProvider;
import org.eclipse.birt.report.designer.ui.views.ProviderFactory;
import org.eclipse.birt.report.designer.ui.widget.ITreeViewerBackup;
import org.eclipse.birt.report.model.api.CascadingParameterGroupHandle;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.CssSharedStyleHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ParameterGroupHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.core.IDesignElement;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.birt.report.model.api.validators.IValidationListener;
import org.eclipse.birt.report.model.api.validators.ValidationEvent;
import org.eclipse.gef.dnd.TemplateTransfer;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.RetargetAction;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;

/**
 * Outline page to show the tree structure of IR Model
 */
public class DesignerOutlinePage extends ContentOutlinePage
		implements IValidationListener, IModelEventFactory, IReportPageBookViewPage {

	/**
	 * the root report design of outline tree
	 */
	private ModuleHandle model;

	private NonGEFSynchronizerWithTreeView synchronizer;

	private ITreeViewerBackup backup;

	// private ListenerElementVisitor visitor;

	/**
	 * Instantiates DesignerOutlinePage, and sets the IR Model's root report design
	 * object
	 */
	public DesignerOutlinePage(ModuleHandle model) {
		this.model = model;
	}

	/**
	 * Creates the SWT control for this page under the given parent control.
	 * <p>
	 * Clients should not call this method (the workbench calls this method when it
	 * needs to, which may be never).
	 * </p>
	 *
	 * @param parent the parent control
	 */
	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		// added by gao 2004.08.05
		createContextMenu();
		ViewsTreeProvider provider = new ViewsTreeProvider();

		getTreeViewer().setContentProvider(provider);

		getTreeViewer().setLabelProvider(provider);

		getSelectionSynchronizer().setTreeViewer(getTreeViewer());

		// add inline renaming support
		new RenameListener(getTreeViewer()).apply();

		getTreeViewer().setSorter(new ItemSorter());

		addDragAndDropListener();

		init(model);

		getTreeViewer().expandToLevel(2);

		final Tree tree = getTreeViewer().getTree();
		// 184790
		tree.addListener(SWT.PaintItem, new Listener() {

			@Override
			public void handleEvent(Event event) {
				// Fix bug 192094
				TreeItem item = (TreeItem) event.item;
				if (event.item.getData() instanceof CssStyleSheetHandle
						|| event.item.getData() instanceof CssSharedStyleHandle) {
					Color gray = Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GRAY);
					if (!item.getForeground().equals(gray)) {
						item.setForeground(gray);
					}
				} else {
					INodeProvider provider = null;
					if (event.item != null && event.item.getData() != null) {
						provider = ProviderFactory.createProvider(event.item.getData());
					}
					if (provider != null && provider.isReadOnly(event.item.getData())) {
						Color gray = Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GRAY);
						if (!item.getForeground().equals(gray)) {
							item.setForeground(gray);
						}
					} else {
						Color black = ReportColorConstants.ReportForeground;
						if (!item.getForeground().equals(black)) {
							item.setForeground(black);
						}
					}
				}
			}
		});
		// Adds mouse listener to disable Cell multi-selection
		tree.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseDown(MouseEvent e) {
				if ((e.stateMask & SWT.CTRL) != 0 || (e.stateMask & SWT.SHIFT) != 0) {
					setSingleSelection(e);
				}
			}

			private void setSingleSelection(MouseEvent e) {
				IStructuredSelection selection = (IStructuredSelection) getSelection();
				if (!selection.isEmpty() && selection.size() > 1) {
					boolean includeCell = false;
					for (Iterator iterator = selection.iterator(); iterator.hasNext();) {
						Object obj = iterator.next();
						if (obj instanceof CellHandle) {
							if (includeCell) {
								// Set current single selection if include
								// multiple cells
								TreeItem item = getTreeViewer().getTree().getItem(new Point(e.x, e.y));
								if (item != null) {
									getTreeViewer().getTree().setSelection(new TreeItem[] { item });
								}
								break;
							}
							includeCell = true;
						}
					}
				}
			}
		});

		tree.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// Do nothing

			}

			// Handle double click event
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				if (getSelection() instanceof StructuredSelection) {
					List list = new ArrayList();

					Object selectedObject = ((StructuredSelection) getSelection()).getFirstElement();
					list.add(selectedObject);

					ReportRequest r = new ReportRequest();
					r.setType(ReportRequest.OPEN_EDITOR);
					r.setRequestConverter(new IRequestConverter() {

						/*
						 * (non-Javadoc)
						 *
						 * @see org.eclipse.birt.report.designer.core.util.mediator .request
						 * .IRequestConvert#convertSelectionToModelLisr(java .util.List)
						 */
						@Override
						public List convertSelectionToModelLisr(List list) {
							List lst = new ArrayList();

							for (Iterator itr = list.iterator(); itr.hasNext();) {
								Object obj = itr.next();

								lst.add(obj);

							}
							return lst;
						}
					});

					r.setSelectionObject(list);
					SessionHandleAdapter.getInstance().getMediator(model).notifyRequest(r);

					// handle double-click for included library
					if (selectedObject instanceof LibraryHandle && selectedObject != getRoot()) {
						try {
							final File file = EditorUtil
									.convertToFile(new URL(((LibraryHandle) selectedObject).getFileName()));

							if (file != null && file.exists() && file.isFile()) {
								Display.getCurrent().asyncExec(new Runnable() {

									@Override
									public void run() {
										try {
											EditorUtil.openEditor(DesignerOutlinePage.this, file,
													IReportEditorContants.LIBRARY_EDITOR_ID);
										} catch (PartInitException e) {
											ExceptionUtil.handle(e);
										}
									}
								});
							}
						} catch (IOException e1) {
							// ignore
						}
					}

					try {
						Tree tree = (Tree) e.getSource();
						TreeItem[] selectedItems = tree.getSelection();
						if (selectedItems.length > 0) {
							if (selectedItems[0].getExpanded()) {
								selectedItems[0].setExpanded(false);
							} else {
								getTreeViewer().expandToLevel(selectedObject, selectedItems[0].getExpanded() ? 0 : 1);
							}
						}
					} catch (Exception e2) {

					}

				}
			}

		});

		tree.addMouseTrackListener(new MouseTrackAdapter() {

			@Override
			public void mouseHover(MouseEvent event) {
				Widget widget = event.widget;
				if (widget == tree) {
					Point pt = new Point(event.x, event.y);
					TreeItem item = tree.getItem(pt);
					if (item == null || item.getData() == null) {
						tree.setToolTipText(null);
					} else {
						tree.setToolTipText(getTooltip(item.getData()));
					}
				}
			}
		});

		// suport the mediator
		SessionHandleAdapter.getInstance().getMediator(model).addColleague(getSelectionSynchronizer());

		if (backup != null) {
			backup.restoreBackup(getTreeViewer());
			getTreeViewer().getTree().addTreeListener(new TreeListener() {

				@Override
				public void treeCollapsed(TreeEvent e) {
					Item item = (Item) e.item;
					backup.updateCollapsedStatus(getTreeViewer(), item.getData());

				}

				@Override
				public void treeExpanded(TreeEvent e) {
					Item item = (Item) e.item;
					backup.updateExpandedStatus(getTreeViewer(), item.getData());
				}

			});
		}

	}

	protected void addDragAndDropListener() {
		// add drag and drop support
		int ops = DND.DROP_MOVE | DND.DROP_COPY;
		Transfer[] transfers = { TemplateTransfer.getInstance() };
		getTreeViewer().addDragSupport(ops, transfers, new DesignerDragListener(getTreeViewer()));
		transfers = new Transfer[] { TemplateTransfer.getInstance() };

		DesignerDropListener dropListener = new DesignerDropListener(getTreeViewer());

		// dropListener.addDropConstraint( ParameterGroupHandle.class,
		// new DropTypeConstraint( ParameterHandle.class,
		// ParameterGroupHandle.class,
		// true ) );
		//
		// dropListener.addDropConstraint( ParameterGroupHandle.class,
		// new DropTypeConstraint( Object.class,
		// ParameterGroupHandle.class,
		// false ) );

		// nothing can drag into CascadingParameterGroupHandle
		dropListener.addDropConstraint(CascadingParameterGroupHandle.class, new IDropConstraint() {

			@Override
			public int validate(Object transfer, Object target) {
				return RESULT_NO;
			}
		});
		// can't drag into slot as a CascadingParameterGroupHandle children
		// sibling
		dropListener.addDropConstraint(ScalarParameterHandle.class, new IDropConstraint() {

			@Override
			public int validate(Object transfer, Object target) {
				if (target instanceof ScalarParameterHandle) {
					ScalarParameterHandle targetParameter = (ScalarParameterHandle) target;
					if (targetParameter.getContainer() instanceof CascadingParameterGroupHandle) {
						return RESULT_NO;
					}
				}
				return RESULT_UNKNOW;
			}
		});

		// CascadingParameterGroupHandle children can't drag into other slot.
		IDropConstraint cascadingParameterGroupChildrenConstraint = new IDropConstraint() {

			@Override
			public int validate(Object transfer, Object target) {
				if (transfer instanceof Object[] && ((Object[]) transfer).length > 0
						&& ((Object[]) transfer)[0] instanceof ScalarParameterHandle) {
					ScalarParameterHandle transferParameter = (ScalarParameterHandle) ((Object[]) transfer)[0];
					if (transferParameter.getContainer() instanceof CascadingParameterGroupHandle) {
						return RESULT_NO;
					}
				}
				return RESULT_UNKNOW;
			}
		};

		dropListener.addDropConstraint(ScalarParameterHandle.class, cascadingParameterGroupChildrenConstraint);
		dropListener.addDropConstraint(ParameterGroupHandle.class, cascadingParameterGroupChildrenConstraint);
		dropListener.addDropConstraint(SlotHandle.class, cascadingParameterGroupChildrenConstraint);

		getTreeViewer().addDropSupport(ops, transfers, dropListener);
	}

	@Override
	public void dispose() {
		// if ( visitor != null )
		// {
		// visitor.removeListener( getRoot( ) );
		// visitor.dispose( );
		// visitor = null;
		// }
		model.removeValidationListener(this);
		// remove the mediator listener
		IMediator mediator = SessionHandleAdapter.getInstance().getMediator(model, false);
		if (mediator != null) {
			mediator.removeColleague(getSelectionSynchronizer());
		}
//		SessionHandleAdapter.getInstance( )
//				.getMediator( reportHandle )
//				.removeColleague( getSelectionSynchronizer( ) );
	}

	/**
	 * Creates the context menu
	 *
	 */
	protected void createContextMenu() {
		MenuManager menuManager = new ViewContextMenuProvider(getTreeViewer());

		Menu menu = menuManager.createContextMenu(getTreeViewer().getControl());

		getTreeViewer().getControl().setMenu(menu);

		getSite().registerContextMenu("outlinemenu", menuManager, //$NON-NLS-1$
				getSite().getSelectionProvider());
		getSite().setSelectionProvider(getTreeViewer());

		// Add a key listener for opening the search dialog.
		getTreeViewer().getTree().addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.stateMask == SWT.CTRL && e.keyCode == 'f') {
					new SearchAction(getTreeViewer()).run();
				}
			}
		});
	}

	/**
	 * Gets the visitor.
	 *
	 * @return the visitor
	 */
	// private ListenerElementVisitor getListenerElementVisitor( )
	// {
	// if ( visitor == null )
	// {
	// visitor = new ListenerElementVisitor( this );
	// }
	// return visitor;
	// }
	public ModuleHandle getRoot() {
		return model;
	}

	// public void setRoot( ModuleHandle reportHandle )
	// {
	// // getListenerElementVisitor( ).removeListener( getRoot( ) );
	// reportHandle.removeValidationListener( this );
	// this.reportHandle = reportHandle;
	// init( reportHandle );
	// }

	private void init(ModuleHandle reportHandle) {
		setTreeInput(reportHandle);
		// getListenerElementVisitor( ).addListener( reportHandle );
		reportHandle.addValidationListener(this);
		handleGlobalAction();
	}

	private void setTreeInput(ModuleHandle reportHandle) {
		getTreeViewer().setInput(new Object[] { reportHandle });
	}

	/**
	 * @return NonGEFSynchronizerWithTreeView
	 */
	public NonGEFSynchronizerWithTreeView getSelectionSynchronizer() {
		if (synchronizer == null) {
			synchronizer = createNonGEFSynchronizerWithTreeView();
			synchronizer.setSource(this);
		}
		return synchronizer;
	}

	protected NonGEFSynchronizerWithTreeView createNonGEFSynchronizerWithTreeView() {
		return new NonGEFSynchronizerWithTreeView();
	}

	/**
	 * Handles all global actions
	 */
	protected void handleGlobalAction() {
		for (int i = 0; i < GlobalActionFactory.GLOBAL_SELECTION_ACTIONS.length; i++) {
			String id = GlobalActionFactory.GLOBAL_SELECTION_ACTIONS[i];
			getSite().getActionBars().setGlobalActionHandler(id, GlobalActionFactory.createSelectionAction(id, this));
		}
		for (int i = 0; i < GlobalActionFactory.GLOBAL_INSERT_ACTIONS.length; i++) {
			String id = GlobalActionFactory.GLOBAL_INSERT_ACTIONS[i];
			getSite().getActionBars().setGlobalActionHandler(id, GlobalActionFactory.createSelectionAction(id, this));
		}

		for (int i = 0; i < GlobalActionFactory.GLOBAL_ELEMENT_ACTIONS.length; i++) {
			String id = GlobalActionFactory.GLOBAL_ELEMENT_ACTIONS[i];
			getSite().getActionBars().setGlobalActionHandler(id, GlobalActionFactory.createSelectionAction(id, this));
		}

		List list = ExtensionPointManager.getInstance().getExtendedElementPoints();
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			ExtendedElementUIPoint point = (ExtendedElementUIPoint) iter.next();
			String id = point.getExtensionName();
			getSite().getActionBars().setGlobalActionHandler(id, GlobalActionFactory.createSelectionAction(id, this));
		}

		PaletteEntryExtension[] entries = EditpartExtensionManager.getPaletteEntries();
		for (int i = 0; i < entries.length; i++) {
			getSite().getActionBars().setGlobalActionHandler(entries[i].getItemName(),
					GlobalActionFactory.createSelectionAction(entries[i].getItemName(), this));
		}

		getSite().getActionBars().setGlobalActionHandler(ImportLibraryAction.ID,
				new RetargetAction(ImportLibraryAction.ID, ImportLibraryAction.ACTION_TEXT));

		for (int i = 0; i < GlobalActionFactory.GLOBAL_STACK_ACTIONS.length; i++) {
			String id = GlobalActionFactory.GLOBAL_STACK_ACTIONS[i];
			getSite().getActionBars().setGlobalActionHandler(id,
					GlobalActionFactory.createStackAction(id, getRoot().getCommandStack()));
		}

		getSite().getActionBars().updateActionBars();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.model.api.validators.IValidationListener#
	 * elementValidated(org.eclipse.birt.report.model.api.DesignElementHandle,
	 * org.eclipse.birt.report.model.api.validators.ValidationEvent)
	 */
	@Override
	public void elementValidated(DesignElementHandle targetElement, ValidationEvent ev) {
		getTreeViewer().refresh();
	}

	/**
	 * Gets the tooltip of the node
	 *
	 * @param model the model of the node
	 * @return Returns the tooltip name for the node, or null if no tooltip is
	 *         needed.
	 */

	private String getTooltip(Object element) {
		return ProviderFactory.createProvider(element).getNodeTooltip(element);
	}

	public IModelEventProcessor getModelProcessor() {
		return new DesignerOutlineEventProcessor(this) {

			// fix bug 235758
			@Override
			public void clear() {
				super.clear();
				if (isDispose()) {
					return;
				}
				getTreeViewer().refresh();
			}
		};
	}

	@Override
	public Runnable createModelEventRunnable(Object focus, int type, Map args) {
		switch (type) {
		case NotificationEvent.CONTENT_EVENT: {
			return new ReportEventRunnable(focus, type, args) {

				@Override
				public void run() {
					if (isDispose()) {
						return;
					}
					getTreeViewer().refresh();
					Object obj = getArgs().get(DesignerOutlineEventProcessor.EVENT_CONTENT);
					expandNodeAfterCreation(obj);
				}
			};
		}

		default:
			return new ReportEventRunnable(focus, type, args) {

				@Override
				public void run() {
					if (isDispose()) {
						return;
					}
					getTreeViewer().refresh();
				}
			};
		}
	}

	protected void expandNodeAfterCreation(Object obj) {
		if (obj instanceof IDesignElement) {
			IDesignElement element = (IDesignElement) obj;
			getTreeViewer().expandToLevel(element.getHandle(getRoot().getModule()), 0);
			if (backup != null) {
				backup.updateStatus(getTreeViewer());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.designer.internal.ui.views.
	 * DesignerOutlineEventProcessor.IFactConsumerFactory#isDispose()
	 */
	@Override
	public boolean isDispose() {
		if (getTreeViewer() == null || getTreeViewer().getTree() == null) {
			return true;
		} else {
			return getTreeViewer().getTree().isDisposed();
		}
	}

	public void setBackupState(ITreeViewerBackup backup) {
		this.backup = backup;
	}

	@Override
	public ISelectionProvider getSelectionProvider() {
		return getTreeViewer();
	}
}
