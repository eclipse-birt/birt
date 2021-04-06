/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.parts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.preference.IPreferenceChangeListener;
import org.eclipse.birt.core.preference.IPreferences;
import org.eclipse.birt.report.designer.core.commands.DeleteCommand;
import org.eclipse.birt.report.designer.internal.ui.editors.breadcrumb.EditorBreadcrumb;
import org.eclipse.birt.report.designer.internal.ui.editors.breadcrumb.ReportLayoutEditorBreadcrumb;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.DummyEditpart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableUtil;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.providers.SchematicContextMenuProvider;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.ReportCreationTool;
import org.eclipse.birt.report.designer.internal.ui.extension.ExtendedElementUIPoint;
import org.eclipse.birt.report.designer.internal.ui.extension.ExtensionPointManager;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.extensions.IExtensionConstants;
import org.eclipse.birt.report.designer.ui.preferences.PreferenceFactory;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.dnd.TemplateTransferDragSourceListener;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.gef.ui.actions.CopyTemplateAction;
import org.eclipse.gef.ui.actions.DeleteAction;
import org.eclipse.gef.ui.actions.EditorPartAction;
import org.eclipse.gef.ui.actions.RedoAction;
import org.eclipse.gef.ui.actions.SaveAction;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.gef.ui.actions.StackAction;
import org.eclipse.gef.ui.actions.UndoAction;
import org.eclipse.gef.ui.actions.UpdateAction;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite;
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite.FlyoutPreferences;
import org.eclipse.gef.ui.palette.PaletteEditPartFactory;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.gef.ui.views.palette.PalettePage;
import org.eclipse.gef.ui.views.palette.PaletteViewerPage;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.contexts.IContextActivation;
import org.eclipse.ui.contexts.IContextService;

import com.ibm.icu.text.MessageFormat;

/**
 * This class serves as a quick starting point for clients who are new to GEF.
 * It will create an Editor with a flyout palette. The flyout palette will only
 * be visible when the palette view is not open.
 * <p>
 */
public abstract class GraphicalEditorWithFlyoutPalette extends GraphicalEditor
		implements EditorSelectionProvider, IPropertyChangeListener, IPreferenceChangeListener {

	private static final String VIEW_CONTEXT_ID = "org.eclipse.birt.report.designer.internal.ui.editors.parts.graphicaleditorwithflyoutpalette.context"; //$NON-NLS-1$
	private PaletteViewerProvider provider;
	private FlyoutPaletteComposite splitter;
	private CustomPalettePage page;

	// private ButtonPaneComposite bPane;

	// a temporary flag, used for checking if it needs to active the current
	// editor part.
	private static boolean shellActiveFlag = false;

	// record the last activated shell, used for dataset or datasource
	// selection.
	private static Shell lastActiveShell = null;

	// Used for multiple window case. When the shell is activated and it's not
	// the last recored activated shell, then active the current active editor.
	private ShellListener shellActiveListener = new ShellAdapter() {

		public void shellActivated(ShellEvent e) {
			if (!shellActiveFlag) {
				shellActiveFlag = true;
				// Pre-fetch shell from site in case it could be disposed before async execution
				final Shell siteShell = getSite().getShell();
				Display.getCurrent().asyncExec(new Runnable() {

					public void run() {
						if (lastActiveShell == siteShell) {
							// don't active the current active editor
							shellActiveFlag = false;
							return;
						} else {
							lastActiveShell = getSite().getShell();
							IEditorPart editor = UIUtil.getActiveEditor(true);
							if (editor instanceof IPartListener) {
								// update the SessionHandleAdapter's model.
								// If old selection is dataset or datasource,
								// the selection status will lost.
								((IPartListener) editor).partActivated(editor);
							}
							shellActiveFlag = false;
						}
					}
				});
			}
		};
	};

	/**
	 * the list of action ids that are to CommandStack actions
	 */
	private List stackActionIDs = new ArrayList();
	/**
	 * the list of action ids that are editor actions
	 * 
	 */
	private List editorActionIDs = new ArrayList();

	/**
	 * the list of action ids that are to EditPart actions
	 */
	private List editPartActionIDs = new ArrayList();

	/**
	 * @see GraphicalEditor#initializeGraphicalViewer()
	 */
	protected void initializeGraphicalViewer() {
		splitter.hookDropTargetListener(getGraphicalViewer());
		// createActions( );
		updateActions(stackActionIDs);
		// add selection change listener
		getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(getSelectionListener());
	}

	/**
	 * @see org.eclipse.ui.part.WorkbenchPart#firePropertyChange(int)
	 */
	protected void firePropertyChange(int propertyId) {
		super.firePropertyChange(propertyId);
		updateActions(editorActionIDs);
	}

	public void propertyChange(PropertyChangeEvent event) {
		if (getBreadcrumbPreferenceKey().equals(event.getProperty())) {
			fIsBreadcrumbVisible = isBreadcrumbShown();
			if (fIsBreadcrumbVisible) {
				showBreadcrumb();
				List list = getModelList(this.getGraphicalViewer().getSelection());
				if (list != null && list.size() == 1)
					setBreadcrumbInput(list.get(0));
				else
					setBreadcrumbInput(null);
			} else {
				hideBreadcrumb();
			}
		}
	}

	private List getModelList(ISelection selection) {
		List list = new ArrayList();
		if (selection == null)
			return list;
		if (!(selection instanceof StructuredSelection))
			return list;

		StructuredSelection structured = (StructuredSelection) selection;
		if (structured.getFirstElement() instanceof ReportElementEditPart) {
			boolean bool = false;
			for (Iterator it = structured.iterator(); it.hasNext();) {
				ReportElementEditPart object = (ReportElementEditPart) it.next();
				if (object instanceof DummyEditpart) {
					list.add(object.getModel());
					bool = true;
				}
				if (!bool) {
					list.add(object.getModel());
				}
			}
		} else {
			list = structured.toList();
		}
		return list;
	}

	/**
	 * the selection listener
	 */
	private ISelectionListener selectionListener = new ISelectionListener() {

		public void selectionChanged(IWorkbenchPart part, ISelection selection) {
			updateActions(editPartActionIDs);
		}
	};
	private EditorBreadcrumb fBreadcrumb;
	private boolean fIsBreadcrumbVisible;
	private static IContextActivation contextActivation;

	/**
	 * Creates a PaletteViewerProvider that will be used to create palettes for the
	 * view and the flyout.
	 * 
	 * @return the palette provider
	 */
	protected PaletteViewerProvider createPaletteViewerProvider() {
		return new PaletteViewerProvider(getEditDomain()) {

			protected void configurePaletteViewer(final PaletteViewer viewer) {
				super.configurePaletteViewer(viewer);

				viewer.addDragSourceListener(new TemplateTransferDragSourceListener(viewer));

				viewer.getControl().addMouseListener(new MouseListener() {

					public void mouseDoubleClick(MouseEvent e) {
						EditPart editPart = viewer.findObjectAt(new Point(e.x, e.y));
						CombinedTemplateCreationEntry entry = null;
						if (editPart != null && editPart.getModel() instanceof CombinedTemplateCreationEntry) {
							entry = (CombinedTemplateCreationEntry) editPart.getModel();
						}
						if (entry == null)
							return;
						ReportCreationTool tool = (ReportCreationTool) entry.createTool();

						final EditDomain domain = UIUtil.getLayoutEditPartViewer().getEditDomain();
						tool.setEditDomain(domain);
						tool.setViewer(UIUtil.getLayoutEditPartViewer());
						tool.performCreation(UIUtil.getCurrentEditPart());

						Display.getCurrent().asyncExec(new Runnable() {

							public void run() {
								domain.loadDefaultTool();
							}
						});
					}

					public void mouseDown(MouseEvent e) {
					}

					public void mouseUp(MouseEvent e) {
					}
				});
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.gef.ui.palette.PaletteViewerProvider#createPaletteViewer
			 * (org.eclipse.swt.widgets.Composite)
			 */
			public PaletteViewer createPaletteViewer(Composite parent) {
				PaletteViewer pViewer = new PaletteViewer();

				// Replace with new factory
				pViewer.setEditPartFactory(new PaletteEditPartFactory());
				pViewer.createControl(parent);
				configurePaletteViewer(pViewer);
				hookPaletteViewer(pViewer);
				return pViewer;
			}
		};

	}

	/**
	 * @return a newly-created {@link CustomPalettePage}
	 */
	protected CustomPalettePage createPalettePage() {
		return new CustomPalettePage(getPaletteViewerProvider());
	}

	/**
	 * Creates actions and registers them to the ActionRegistry.
	 */
	protected void createActions() {
		// Fix bug 284633
		addStackAction(new UndoAction(this) {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.gef.ui.actions.UndoAction#init()
			 */
			protected void init() {
				super.init();
				setToolTipText(Messages.getString("GraphicalEditorWithFlyoutPalette_Undo.ToolTip0")); //$NON-NLS-1$
				setText(Messages.getString("GraphicalEditorWithFlyoutPalette_Undo.Text0")); //$NON-NLS-1$
				setId(ActionFactory.UNDO.getId());

				ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
				setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_UNDO));
				setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_UNDO_DISABLED));
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.gef.ui.actions.UndoAction#refresh()
			 */
			protected void refresh() {
				super.refresh();
				Command undoCmd = getCommandStack().getUndoCommand();
				if (getLabelForCommand(undoCmd).length() == 0) {
					setToolTipText(Messages.getString("GraphicalEditorWithFlyoutPalette_Undo.ToolTip0")); //$NON-NLS-1$
					setText(Messages.getString("GraphicalEditorWithFlyoutPalette_Undo.Text0")); //$NON-NLS-1$
				} else {
					setToolTipText(
							MessageFormat.format(Messages.getString("GraphicalEditorWithFlyoutPalette_Undo.ToolTip1"), //$NON-NLS-1$
									new Object[] { getLabelForCommand(undoCmd) }).trim());
					setText(MessageFormat.format(Messages.getString("GraphicalEditorWithFlyoutPalette_Undo.Text1"), //$NON-NLS-1$
							new Object[] { getLabelForCommand(undoCmd) }).trim());
				}
			}
		});
		addStackAction(new RedoAction(this) {

			protected void init() {
				super.init();
				setToolTipText(Messages.getString("GraphicalEditorWithFlyoutPalette_Redo.ToolTip0")); //$NON-NLS-1$
				setText(Messages.getString("GraphicalEditorWithFlyoutPalette_Redo.Text0")); //$NON-NLS-1$
				setId(ActionFactory.REDO.getId());

				ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
				setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_REDO));
				setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_REDO_DISABLED));
			}

			/**
			 * Refreshes this action's text to use the last undone command's label.
			 */
			protected void refresh() {
				super.refresh();
				Command redoCmd = getCommandStack().getRedoCommand();
				if (getLabelForCommand(redoCmd).length() == 0) {
					setToolTipText(Messages.getString("GraphicalEditorWithFlyoutPalette_Redo.ToolTip0")); //$NON-NLS-1$
					setText(Messages.getString("GraphicalEditorWithFlyoutPalette_Redo.Text0")); //$NON-NLS-1$
				} else {
					setToolTipText(
							MessageFormat.format(Messages.getString("GraphicalEditorWithFlyoutPalette_Redo.ToolTip1"), //$NON-NLS-1$
									new Object[] { getLabelForCommand(redoCmd) }).trim());
					setText(MessageFormat.format(Messages.getString("GraphicalEditorWithFlyoutPalette_Redo.Text1"), //$NON-NLS-1$
							new Object[] { getLabelForCommand(redoCmd) }).trim());
				}
			}
		});
		addEditPartAction(new DeleteAction((IWorkbenchPart) this) {

			public Command createDeleteCommand(List objects) {
				if (objects.isEmpty())
					return null;
				if (!(objects.get(0) instanceof EditPart))
					return null;

				GroupRequest deleteReq = new GroupRequest(RequestConstants.REQ_DELETE);
				deleteReq.setEditParts(objects);

				List temp = TableUtil.filletCellModel(objects);
				List list = new ArrayList();
				for (int i = 0; i < temp.size(); i++) {
					EditPart object = (EditPart) temp.get(i);
					list.add(object.getModel());
				}
				return new DeleteCommand(list.toArray());
			}

			@Override
			public void run() {
				if (UIUtil.canDelete(getSelectedObjects()))
					super.run();
			}

			@Override
			public String getText() {
				return Messages.getString("DeleteAction.text"); //$NON-NLS-1$
			}
		});

		SaveAction saveAction = new SaveAction(this);
		saveAction.setLazyEnablementCalculation(true);
		addEditorAction(saveAction);

		addAction(new CopyTemplateAction(this));
	}

	/**
	 * @return button pane that provides function to change pages.
	 */
	// public ButtonPaneComposite getButtonPane( )
	// {
	// return bPane;
	// }
	protected boolean hasRuler() {
		return false;
	}

	protected boolean hasButtonPane() {
		return true;
	}

	/**
	 * @see GraphicalEditor#createPartControl(Composite)
	 */
	public void createPartControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(1, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		composite.setLayout(layout);

		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		fBreadcrumbComposite = new Composite(composite, SWT.NONE);
		GridData layoutData = new GridData(SWT.FILL, SWT.TOP, true, false);
		fBreadcrumbComposite.setLayoutData(layoutData);
		layout = new GridLayout(1, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		layoutData.exclude = true;
		fBreadcrumbComposite.setLayout(layout);

		splitter = new FlyoutPaletteComposite(composite, SWT.NONE, getSite().getPage(), getPaletteViewerProvider(),
				getPalettePreferences());
		super.createPartControl(splitter);

		splitter.setLayoutData(new GridData(GridData.FILL_BOTH));

		Control ctrl = getGraphicalControl();

		splitter.setGraphicalControl(ctrl);

		// if ( hasButtonPane( ) )
		// {
		// bPane.setGraphicalControl( splitter );
		// }

		if (page != null) {
			splitter.setExternalViewer(page.getPaletteViewer());
			page = null;
		}

		fBreadcrumb = createBreadcrumb();
		fBreadcrumb.setMenuManager(new SchematicContextMenuProvider(getGraphicalViewer(), getActionRegistry()));

		fIsBreadcrumbVisible = isBreadcrumbShown();
		if (fIsBreadcrumbVisible)
			showBreadcrumb();

		getPreferenceStore().addPropertyChangeListener(this);

		registerExtensionPreference(IExtensionConstants.ATTRIBUTE_EDITOR_SHOW_IN_DESIGNER_BY_PREFERENCE);

		getSite().getShell().addShellListener(shellActiveListener);

		lastActiveShell = getSite().getShell();

		activateDesignerEditPart();

	}

	private void activateDesignerEditPart() {
		IContextService contextService = (IContextService) PlatformUI.getWorkbench().getService(IContextService.class);
		if (contextActivation == null)
			contextActivation = contextService.activateContext(VIEW_CONTEXT_ID);
	}

	/**
	 * @see org.eclipse.ui.IWorkbenchPart#dispose()
	 */
	public void dispose() {

		if (getSite() != null && !getSite().getShell().isDisposed()) {
			getSite().getShell().removeShellListener(shellActiveListener);
		}

		if (fBreadcrumb != null) {
			fBreadcrumb.dispose();
		}

		deregisterExtensionPreference(IExtensionConstants.ATTRIBUTE_EDITOR_SHOW_IN_DESIGNER_BY_PREFERENCE);

		getPreferenceStore().removePropertyChangeListener(this);

		// remove selection listener
		getSite().getWorkbenchWindow().getSelectionService().removeSelectionListener(getSelectionListener());
		// dispose the ActionRegistry (will dispose all actions)
		super.dispose();
		if (splitter != null)
			splitter.setExternalViewer(null);
		splitter = null;
		getSelectionActions().clear();
		getActionRegistry().dispose();
		List list = new ArrayList();
		Iterator actions = getActionRegistry().getActions();
		while (actions.hasNext()) {
			IAction action = (IAction) actions.next();
			list.add(action);
			// getActionRegistry( ).removeAction(action);
		}
		for (int i = 0; i < list.size(); i++) {
			getActionRegistry().removeAction((IAction) list.get(i));
		}
		// ( (ReportMultiPageEditorSite)getSite()).dispose();
		deActivateDesignerEditPart();

	}

	private void deActivateDesignerEditPart() {
		if (!UIUtil.isReportEditorActivated()) {
			IContextService contextService = (IContextService) PlatformUI.getWorkbench()
					.getService(IContextService.class);
			if (contextActivation != null) {
				contextService.deactivateContext(contextActivation);
				contextActivation = null;
			}
		}
	}

	/**
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class type) {
		if (type == PalettePage.class) {
			if (splitter == null) {
				page = createPalettePage();
				return page;
			}
			return createPalettePage();
		}

		if (type == PaletteRoot.class) {
			return getPaletteRoot();
		}

		if (type == ZoomManager.class) {
			return getGraphicalViewer().getProperty(ZoomManager.class.toString());
		}
		return super.getAdapter(type);
	}

	protected abstract IEditorPart getMultiPageEditor();

	/**
	 * @return the graphical viewer's control
	 */
	protected Control getGraphicalControl() {
		return getGraphicalViewer().getControl();
	}

	/**
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#getGraphicalViewer()
	 */
	public GraphicalViewer getGraphicalViewer() {
		return super.getGraphicalViewer();
	}

	/**
	 * @return the FlyoutPreferences object used to save the flyout palette's
	 *         preferences
	 */
	protected abstract FlyoutPreferences getPalettePreferences();

	/**
	 * Returns the PaletteRoot for the palette viewer.
	 * 
	 * @return the palette root
	 */
	protected abstract PaletteRoot getPaletteRoot();

	/**
	 * Returns the palette viewer provider that is used to create palettes for the
	 * view and the flyout. Creates one if it doesn't already exist.
	 * 
	 * @return the PaletteViewerProvider that can be used to create PaletteViewers
	 *         for this editor
	 * @see #createPaletteViewerProvider()
	 */
	protected final PaletteViewerProvider getPaletteViewerProvider() {
		if (provider == null)
			provider = createPaletteViewerProvider();
		return provider;
	}

	/**
	 * Sets the edit domain for this editor.
	 * 
	 * @param ed The new EditDomain
	 */
	protected void setEditDomain(DefaultEditDomain ed) {
		super.setEditDomain(ed);
		getEditDomain().setPaletteRoot(getPaletteRoot());
	}

	/**
	 * Creates the GraphicalViewer on the specified <code>Composite</code>.
	 * 
	 * @param parent the parent composite
	 */
	protected void createGraphicalViewer(Composite parent) {
		DeferredGraphicalViewer viewer = new DeferredGraphicalViewer();
		viewer.createControl(parent);
		setGraphicalViewer(viewer);
		configureGraphicalViewer();
		hookGraphicalViewer();
		initializeGraphicalViewer();
		// addAction ( new ToggleRulerVisibilityAction(
		// this.getGraphicalViewer() ));
	}

	/**
	 * A custom PalettePage that helps GraphicalEditorWithFlyoutPalette keep the two
	 * PaletteViewers (one displayed in the editor and the other displayed in the
	 * PaletteView) in sync when switching from one to the other (i.e., it helps
	 * maintain state across the two viewers).
	 * 
	 * @author Pratik Shah
	 * @since 3.0
	 */
	protected class CustomPalettePage extends PaletteViewerPage {

		/**
		 * Constructor
		 * 
		 * @param provider the provider used to create a PaletteViewer
		 */
		public CustomPalettePage(PaletteViewerProvider provider) {
			super(provider);
		}

		/**
		 * @see org.eclipse.ui.part.IPage#createControl(org.eclipse.swt.widgets.Composite)
		 */
		public void createControl(Composite parent) {
			super.createControl(parent);
			if (splitter != null)
				splitter.setExternalViewer(viewer);
		}

		/**
		 * @see org.eclipse.ui.part.IPage#dispose()
		 */
		public void dispose() {
			if (splitter != null)
				splitter.setExternalViewer(null);
			super.dispose();
		}

		/**
		 * @return the PaletteViewer created and displayed by this page
		 */
		public PaletteViewer getPaletteViewer() {
			return viewer;
		}
	}

	protected void hookZoom(ScalableFreeformRootEditPart root) {
		List zoomLevels = new ArrayList(3);
		zoomLevels.add(ZoomManager.FIT_ALL);
		zoomLevels.add(ZoomManager.FIT_WIDTH);
		zoomLevels.add(ZoomManager.FIT_HEIGHT);
		root.getZoomManager().setZoomLevelContributions(zoomLevels);

		IAction zoomIn = new ZoomInAction(root.getZoomManager());
		IAction zoomOut = new ZoomOutAction(root.getZoomManager());
		getActionRegistry().registerAction(zoomIn);
		getActionRegistry().registerAction(zoomOut);
		getSite().getKeyBindingService().registerAction(zoomIn);
		getSite().getKeyBindingService().registerAction(zoomOut);
	}

	/**
	 * Returns the selection listener.
	 * 
	 * @return the <code>ISelectionListener</code>
	 */
	protected ISelectionListener getSelectionListener() {
		return selectionListener;
	}

	/**
	 * Adds an action to this editor's <code>ActionRegistry</code>. (This is a
	 * helper method.)
	 * 
	 * @param action the action to add.
	 */
	protected void addAction(IAction action) {
		getActionRegistry().registerAction(action);
	}

	/**
	 * Adds an editor action to this editor.
	 * 
	 * <p>
	 * <Editor actions are actions that depend and work on the editor.
	 * 
	 * @param action the editor action
	 */
	protected void addEditorAction(EditorPartAction action) {
		getActionRegistry().registerAction(action);
		editorActionIDs.add(action.getId());
	}

	/**
	 * Adds an <code>EditPart</code> action to this editor.
	 * 
	 * <p>
	 * <code>EditPart</code> actions are actions that depend and work on the
	 * selected <code>EditPart</code>s.
	 * 
	 * @param action the <code>EditPart</code> action
	 */
	protected void addEditPartAction(SelectionAction action) {
		getActionRegistry().registerAction(action);
		editPartActionIDs.add(action.getId());
	}

	protected void removeEditPartAction(SelectionAction action) {
		getActionRegistry().removeAction(action);
		editPartActionIDs.remove(action.getId());
	}

	/**
	 * Adds an <code>CommandStack</code> action to this editor.
	 * 
	 * <p>
	 * <code>CommandStack</code> actions are actions that depend and work on the
	 * <code>CommandStack</code>.
	 * 
	 * @param action the <code>CommandStack</code> action
	 */
	protected void addStackAction(StackAction action) {
		getActionRegistry().registerAction(action);
		stackActionIDs.add(action.getId());
	}

	/**
	 * Updates the specified actions.
	 * 
	 * @param actionIds the list of ids of actions to update
	 */
	protected void updateActions(List actionIds) {
		for (Iterator ids = actionIds.iterator(); ids.hasNext();) {
			IAction action = getActionRegistry().getAction(ids.next());
			if (null != action && action instanceof UpdateAction)
				((UpdateAction) action).update();
		}
	}

	/**
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.parts.EditorSelectionProvider#updateStackActions()
	 */
	public void updateStackActions() {
		updateActions(stackActionIDs);

	}

	public void setFocus() {
		if (getGraphicalViewer() != null && getGraphicalViewer().getControl() != null) {
			super.setFocus();
		}
		return;
	}

	protected EditorBreadcrumb createBreadcrumb() {
		return new ReportLayoutEditorBreadcrumb(this);
	}

	protected boolean isBreadcrumbShown() {
		IPreferenceStore store = getPreferenceStore();
		String key = getBreadcrumbPreferenceKey();
		return store != null && key != null && store.getBoolean(key);
	}

	private IPreferenceStore getPreferenceStore() {
		return ReportPlugin.getDefault().getPreferenceStore();
	}

	public static final String EDITOR_SHOW_BREADCRUMB = "breadcrumb"; //$NON-NLS-1$
	private Composite fBreadcrumbComposite;

	public String getBreadcrumbPreferenceKey() {
		IPerspectiveDescriptor perspective = getSite().getPage().getPerspective();
		if (perspective == null)
			return null;
		return EDITOR_SHOW_BREADCRUMB + "." + perspective.getId(); //$NON-NLS-1$
	}

	private void showBreadcrumb() {
		if (fBreadcrumb == null)
			return;

		if (fBreadcrumbComposite.getChildren().length == 0) {
			fBreadcrumb.createContent(fBreadcrumbComposite);
		}

		((GridData) fBreadcrumbComposite.getLayoutData()).exclude = true;
		fBreadcrumbComposite.setVisible(false);
		fBreadcrumbComposite.getParent().layout(true, true);
	}

	protected void setBreadcrumbInput(Object element) {
		if (!isBreadcrumbShown())
			return;
		if (fBreadcrumb == null)
			return;
		if (element != null)
			fBreadcrumb.setInput(element);
		else {
			fBreadcrumb.setInput(new Object[0]);
		}
		((GridData) fBreadcrumbComposite.getLayoutData()).exclude = false;
		fBreadcrumbComposite.setVisible(true);
		fBreadcrumbComposite.getParent().layout(true, true);
	}

	public EditorBreadcrumb getBreadcrumb() {
		return fBreadcrumb;
	}

	private void hideBreadcrumb() {
		if (fBreadcrumb == null)
			return;

		((GridData) fBreadcrumbComposite.getLayoutData()).exclude = true;
		fBreadcrumbComposite.setVisible(false);
		fBreadcrumbComposite.getParent().layout(true, true);
	}

	protected void registerExtensionPreference(String extension) {
		List exts = ExtensionPointManager.getInstance().getExtendedElementPoints();

		for (Iterator itor = exts.iterator(); itor.hasNext();) {
			ExtendedElementUIPoint point = (ExtendedElementUIPoint) itor.next();
			String preference = (String) point.getAttribute(extension);
			if (preference != null) {
				String[] splits = preference.split("/");
				if (splits.length == 2) {
					IPreferences wrapper = PreferenceFactory.getInstance().getPluginPreferences(splits[0], null);
					if (wrapper != null) {
						wrapper.removePreferenceChangeListener(this);
						wrapper.addPreferenceChangeListener(this);
						ExtensionPointManager.getInstance().addPreference(extension, splits[1]);
					}
				}
			}
		}
	}

	protected void deregisterExtensionPreference(String extension) {
		List exts = ExtensionPointManager.getInstance().getExtendedElementPoints();

		for (Iterator itor = exts.iterator(); itor.hasNext();) {
			ExtendedElementUIPoint point = (ExtendedElementUIPoint) itor.next();
			String preference = (String) point.getAttribute(extension);
			if (preference != null) {
				String[] splits = preference.split("/");
				if (splits.length == 2) {
					IPreferences wrapper = PreferenceFactory.getInstance().getPluginPreferences(splits[0], null);
					if (wrapper != null) {
						wrapper.removePreferenceChangeListener(this);
					}
				}
			}
		}
	}
}
