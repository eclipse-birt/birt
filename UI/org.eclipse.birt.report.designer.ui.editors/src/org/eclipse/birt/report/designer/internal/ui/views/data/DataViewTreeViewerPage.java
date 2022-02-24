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

package org.eclipse.birt.report.designer.internal.ui.views.data;

import java.util.Map;

import org.eclipse.birt.report.designer.core.model.views.data.DataSetItemModel;
import org.eclipse.birt.report.designer.core.model.views.data.ReportDataHandle;
import org.eclipse.birt.report.designer.internal.ui.dnd.InsertInLayoutUtil;
import org.eclipse.birt.report.designer.internal.ui.editors.parts.event.IModelEventProcessor;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.IReportPageBookViewPage;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.AbstractModelEventProcessor.IModelEventFactory;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportEventRunnable;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.DataViewEventProcessor;
import org.eclipse.birt.report.designer.internal.ui.views.RenameListener;
import org.eclipse.birt.report.designer.internal.ui.views.ViewContextMenuProvider;
import org.eclipse.birt.report.designer.internal.ui.views.ViewsTreeProvider;
import org.eclipse.birt.report.designer.internal.ui.views.actions.GlobalActionFactory;
import org.eclipse.birt.report.designer.internal.ui.views.outline.ItemSorter;
import org.eclipse.birt.report.designer.internal.ui.views.outline.dnd.DesignerDragListener;
import org.eclipse.birt.report.designer.internal.ui.views.outline.dnd.DesignerDropListener;
import org.eclipse.birt.report.designer.internal.ui.views.outline.dnd.IDropConstraint;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.designer.ui.widget.ITreeViewerBackup;
import org.eclipse.birt.report.model.api.CascadingParameterGroupHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ParameterGroupHandle;
import org.eclipse.birt.report.model.api.ParameterHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.VariableElementHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IDesignElement;
import org.eclipse.birt.report.model.api.elements.structures.ConfigVariable;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.validators.IValidationListener;
import org.eclipse.birt.report.model.api.validators.ValidationEvent;
import org.eclipse.gef.dnd.TemplateTransfer;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
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
public class DataViewTreeViewerPage extends DataViewPage
		implements IModelEventFactory, IValidationListener, IReportPageBookViewPage {

	private static final String LABEL_DOUBLE_CLICK = Messages
			.getString("DataViewTreeViewerPage.tooltip.DoubleClickToEdit"); //$NON-NLS-1$

	/**
	 * constructor
	 * 
	 * @param model the handle of the report design
	 */
	public DataViewTreeViewerPage(ModuleHandle model) {
		super();
		setRoot(model);
	}

	/**
	 * Creates the tree view
	 * 
	 * @param parent the parent
	 */
	protected TreeViewer createTreeViewer(Composite parent) {
		TreeViewer treeViewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		return treeViewer;
	}

	/**
	 * Initializes the data view page.
	 */
	protected void initPage() {
		createContextMenus();

		handleGlobalAction();

		// add inline renaming support
		new RenameListener(getTreeViewer()).apply();
		getTreeViewer().setSorter(new ItemSorter());
		getTreeViewer().getTree().addMouseTrackListener(new MouseTrackAdapter() {

			public void mouseHover(MouseEvent event) {
				Widget widget = event.widget;
				if (widget == getTreeViewer().getTree()) {
					Point pt = new Point(event.x, event.y);
					TreeItem item = getTreeViewer().getTree().getItem(pt);
					getTreeViewer().getTree().setToolTipText(getTooltip(item));
				}
			}
		});
		getTreeViewer().getTree().addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				// Do nothing

			}

			// Handle double click event
			public void widgetDefaultSelected(SelectionEvent e) {
				if (getSelection() instanceof StructuredSelection) {

					Object selectedObject = ((StructuredSelection) getSelection()).getFirstElement();

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

		if (backup != null) {
			backup.restoreBackup(getTreeViewer());
			getTreeViewer().getTree().addTreeListener(new TreeListener() {

				public void treeCollapsed(TreeEvent e) {
					Item item = (Item) e.item;
					backup.updateCollapsedStatus(getTreeViewer(), item.getData());

				}

				public void treeExpanded(TreeEvent e) {
					Item item = (Item) e.item;
					backup.updateExpandedStatus(getTreeViewer(), item.getData());
				}

			});
		}

	}

	private String getTooltip(TreeItem item) {
		if (item != null) {
			Object[] tooltipProviders = ElementAdapterManager.getAdapters(this, IDataViewerTooltipProvider.class);
			if (tooltipProviders != null) {
				for (int i = 0; i < tooltipProviders.length; i++) {
					IDataViewerTooltipProvider tooltipProvider = (IDataViewerTooltipProvider) tooltipProviders[i];
					if (tooltipProvider != null) {
						String tooltip = tooltipProvider.getNodeTooltip(item);
						if (tooltip != null)
							return tooltip;
					}
				}
			}
			Object object = item.getData();
			if (object instanceof DataSourceHandle || object instanceof ParameterGroupHandle) {
				return LABEL_DOUBLE_CLICK;
			}
			StringBuffer tooltip = new StringBuffer();
			boolean canInsert = InsertInLayoutUtil.handleValidateInsertToLayout(object, UIUtil.getCurrentEditPart());
			String text = "(" + item.getText() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
			if (object instanceof DataSetHandle) {
				if (canInsert)
					tooltip.append(Messages.getString("DataViewTreeViewerPage.tooltip.DragToInsertDataSetColumns") //$NON-NLS-1$
							+ text + "; "); //$NON-NLS-1$
				tooltip.append(LABEL_DOUBLE_CLICK);
			}
			if (object instanceof DataSetItemModel || object instanceof ResultSetColumnHandle) {
				if (canInsert)
					tooltip.append(Messages.getString("DataViewTreeViewerPage.tooltip.DragToInsertColumn") + text); //$NON-NLS-1$
			} else if (object instanceof ParameterHandle || object instanceof VariableElementHandle
					|| object instanceof CubeHandle) {
				if (canInsert)
					tooltip.append(Messages.getString("DataViewTreeViewerPage.tooltip.DragToInsertParameter")); //$NON-NLS-1$
				tooltip.append(LABEL_DOUBLE_CLICK);
			}
			return tooltip.toString();
		}
		return ""; //$NON-NLS-1$
	}

	/**
	 * Creates the context menu
	 */
	private void createContextMenus() {
		MenuManager menuManager = new ViewContextMenuProvider(getTreeViewer());

		Menu menu = menuManager.createContextMenu(getTreeViewer().getControl());

		getTreeViewer().getControl().setMenu(menu);
		getSite().registerContextMenu("#Pop up", menuManager, //$NON-NLS-1$
				getSite().getSelectionProvider());
	}

	/**
	 * Configures the tree viewer.
	 */
	protected void configTreeViewer() {

		ViewsTreeProvider provider = new ViewsTreeProvider();
		getTreeViewer().setContentProvider(provider);
		getTreeViewer().setLabelProvider(provider);

		initRoot();

		// add inline renaming support

		addDragAndDropListener();
	}

	protected void addDragAndDropListener() {
		// Adds drag and drop support
		int ops = DND.DROP_MOVE | DND.DROP_COPY;
		Transfer[] transfers = new Transfer[] { TemplateTransfer.getInstance() };
		getTreeViewer().addDragSupport(ops, transfers, new DesignerDragListener(getTreeViewer()));

		transfers = new Transfer[] { TemplateTransfer.getInstance() };
		ops = DND.DROP_COPY | DND.DROP_MOVE;// Copy may cause duplicate name
		// exception

		DesignerDropListener dropListener = new DesignerDropListener(getTreeViewer());

		// nothing can drag into CascadingParameterGroupHandle
		dropListener.addDropConstraint(CascadingParameterGroupHandle.class, new IDropConstraint() {

			public int validate(Object transfer, Object target) {
				return RESULT_NO;
			}
		});
		// can't drag into slot as a CascadingParameterGroupHandle children
		// sibling
		dropListener.addDropConstraint(ScalarParameterHandle.class, new IDropConstraint() {

			public int validate(Object transfer, Object target) {
				if (target instanceof ScalarParameterHandle) {
					ScalarParameterHandle targetParameter = (ScalarParameterHandle) target;
					if (targetParameter.getContainer() instanceof CascadingParameterGroupHandle)
						return RESULT_NO;
				}
				return RESULT_UNKNOW;
			}
		});

		// CascadingParameterGroupHandle children can't drag into other slot.
		IDropConstraint cascadingParameterGroupChildrenConstraint = new IDropConstraint() {

			public int validate(Object transfer, Object target) {
				if (transfer instanceof Object[] && ((Object[]) transfer).length > 0
						&& ((Object[]) transfer)[0] instanceof ScalarParameterHandle) {
					ScalarParameterHandle transferParameter = (ScalarParameterHandle) ((Object[]) transfer)[0];
					if (transferParameter.getContainer() instanceof CascadingParameterGroupHandle)
						return RESULT_NO;
				}
				return RESULT_UNKNOW;
			}
		};

		dropListener.addDropConstraint(ScalarParameterHandle.class, cascadingParameterGroupChildrenConstraint);
		dropListener.addDropConstraint(ParameterGroupHandle.class, cascadingParameterGroupChildrenConstraint);
		dropListener.addDropConstraint(SlotHandle.class, cascadingParameterGroupChildrenConstraint);

		getTreeViewer().addDropSupport(ops, transfers, dropListener);
	}

	/**
	 * Initializes the root of the view
	 * 
	 */
	protected void initRoot() {
		getTreeViewer().setInput(new ReportDataHandle(getRoot()));
		getRoot().addValidationListener(this);
	}

	/**
	 * The <code>Page</code> implementation of this <code>IPage</code> method
	 * disposes of this page's control (if it has one and it has not already been
	 * disposed). Disposes the visitor of the element
	 */
	public void dispose() {
		getRoot().removeValidationListener(this);
		super.dispose();
	}

	// /**
	// * Refreshes the focus and the focus container of the tree view. And
	// applies
	// * the visitor to the given focus.
	// *
	// * @param focus
	// * the design element
	// * @param ev
	// * the notification event
	// */
	// public void elementChanged( DesignElementHandle focus, NotificationEvent
	// ev )
	// {
	// if ( getTreeViewer( ) == null
	// || getTreeViewer( ).getControl( ).isDisposed( ) )
	// {
	// return;
	// }
	// getTreeViewer( ).refresh( );
	// expandNodeAfterCreation( ev );
	// deleteConfigVariable( focus, ev );
	// if ( backup != null )
	// backup.updateStatus( getTreeViewer( ) );
	// getListenerElementVisitor( ).addListener( focus );
	// }

	// /**
	// * Gets the visitor.
	// *
	// * @return the visitor
	// */
	// private ListenerElementVisitor getListenerElementVisitor( )
	// {
	// if ( visitor == null )
	// {
	// visitor = new ListenerElementVisitor( this );
	// }
	// return visitor;
	// }

	/**
	 * Deletes config variable when parameter is deleted. Config variable is used to
	 * store default value in the dialogue when preview page
	 * 
	 * @param ev delete event
	 */
	private void deleteConfigVariable(final Map args) {
		Display.getCurrent().asyncExec(new Runnable() {

			public void run() {
				String variableName = null;
				variableName = (String) args.get(DataViewEventProcessor.VARIABLE_NAME);
				if (variableName != null) {
					ConfigVariable cv = getRoot().findConfigVariable(variableName);
					try {
						if (cv != null) {
							getRoot().getPropertyHandle(ReportDesignHandle.CONFIG_VARS_PROP).removeItem(cv);
						}
					} catch (SemanticException e) {
						ExceptionUtil.handle(e);
					}
				}
			}
		});

	}

	protected boolean isDisposed() {
		Control ctrl = getControl();
		return (ctrl == null || ctrl.isDisposed());
	}

	/**
	 * Handles all global actions
	 */
	protected void handleGlobalAction() {
		for (int i = 0; i < GlobalActionFactory.GLOBAL_SELECTION_ACTIONS.length; i++) {
			String id = GlobalActionFactory.GLOBAL_SELECTION_ACTIONS[i];
			getSite().getActionBars().setGlobalActionHandler(id, GlobalActionFactory.createSelectionAction(id, this));
		}

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
	public void elementValidated(DesignElementHandle targetElement, ValidationEvent ev) {
		getTreeViewer().refresh();
	}

	private ITreeViewerBackup backup;

	public void setBackupState(ITreeViewerBackup dataBackup) {
		this.backup = dataBackup;
	}

	public IModelEventProcessor getModelProcessor() {
		return new DataViewEventProcessor(this);
	}

	public Runnable createModelEventRunnable(Object focus, final int type, final Map args) {
		switch (type) {
		case NotificationEvent.PROPERTY_EVENT:
		case NotificationEvent.NAME_EVENT:
		case NotificationEvent.CONTENT_EVENT: {
			return new ReportEventRunnable(focus, type, args) {

				public void run() {
					if (isDispose()) {
						return;
					}
					getTreeViewer().refresh();
					if (type == NotificationEvent.CONTENT_EVENT) {
						Object obj = getArgs().get(DataViewEventProcessor.EVENT_CONTENT);
						expandNodeAfterCreation(obj);
					}
					deleteConfigVariable(args);
				}
			};
		}

		default:
			return new ReportEventRunnable(focus, type, args) {

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
			if (backup != null)
				backup.updateStatus(getTreeViewer());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.designer.internal.ui.views.
	 * DesignerOutlineEventProcessor.IFactConsumerFactory#isDispose()
	 */
	public boolean isDispose() {
		if (getTreeViewer() == null || getTreeViewer().getTree() == null)
			return true;
		else
			return getTreeViewer().getTree().isDisposed();
	}

	public ISelectionProvider getSelectionProvider() {
		return getTreeViewer();
	}
}
