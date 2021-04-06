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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.dnd.InsertInLayoutUtil;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.AddStyleAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.AddThemeStyleAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.ApplyLayoutPreferenceAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.ChangeDataColumnPartAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.CopyCellContentsContextAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.CreateChartAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.CreatePlaceHolderPartAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.DeleteColumnAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.DeleteGroupAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.DeleteRowAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.EditBindingAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.EditGroupAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.ExportElementToLibraryPartAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.IncludeDetailAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.IncludeFooterAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.IncludeHeaderAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.InsertColumnLeftAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.InsertColumnRightAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.InsertGroupActionFactory;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.InsertGroupHeaderFooterAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.InsertRowAboveAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.InsertRowBelowAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.MergeAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.ResetImageOriginalSizeAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.RevertToReportItemPartAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.SplitAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.DataEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.DummyEditpart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.GridEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ImageEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.LabelEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ListBandEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ListEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.MultipleEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableCellEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart;
import org.eclipse.birt.report.designer.internal.ui.extension.ExtendedElementUIPoint;
import org.eclipse.birt.report.designer.internal.ui.extension.ExtensionPointManager;
import org.eclipse.birt.report.designer.internal.ui.extension.experimental.EditpartExtensionManager;
import org.eclipse.birt.report.designer.internal.ui.extension.experimental.PaletteEntryExtension;
import org.eclipse.birt.report.designer.internal.ui.util.CategorizedElementSorter;
import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.internal.ui.views.actions.CopyAction;
import org.eclipse.birt.report.designer.internal.ui.views.actions.CopyFormatAction;
import org.eclipse.birt.report.designer.internal.ui.views.actions.CutAction;
import org.eclipse.birt.report.designer.internal.ui.views.actions.DeleteAction;
import org.eclipse.birt.report.designer.internal.ui.views.actions.ExportToLibraryAction;
import org.eclipse.birt.report.designer.internal.ui.views.actions.ExtendElementAction;
import org.eclipse.birt.report.designer.internal.ui.views.actions.IExtendElementActionFactory;
import org.eclipse.birt.report.designer.internal.ui.views.actions.ImportCSSStyleAction;
import org.eclipse.birt.report.designer.internal.ui.views.actions.PasteAction;
import org.eclipse.birt.report.designer.internal.ui.views.actions.PasteFormatAction;
import org.eclipse.birt.report.designer.internal.ui.views.actions.RefreshModuleHandleAction;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.actions.ApplyStyleMenuAction;
import org.eclipse.birt.report.designer.ui.actions.ApplyThemeMenuAction;
import org.eclipse.birt.report.designer.ui.actions.DeleteStyleMenuAction;
import org.eclipse.birt.report.designer.ui.actions.EditStyleMenuAction;
import org.eclipse.birt.report.designer.ui.actions.GeneralInsertMenuAction;
import org.eclipse.birt.report.designer.ui.actions.InsertAggregationAction;
import org.eclipse.birt.report.designer.ui.actions.InsertPasteColumnAction;
import org.eclipse.birt.report.designer.ui.actions.InsertRelativeTimePeriodAction;
import org.eclipse.birt.report.designer.ui.actions.MenuUpdateAction;
import org.eclipse.birt.report.designer.ui.actions.NoneAction;
import org.eclipse.birt.report.designer.ui.extensions.IExtensionConstants;
import org.eclipse.birt.report.designer.ui.extensions.IMenuBuilder;
import org.eclipse.birt.report.designer.ui.extensions.IReportItemViewProvider;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.designer.ui.views.ProviderFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.AbstractThemeHandle;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ListGroupHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.TemplateReportItemHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.UpdateAction;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.ui.actions.ActionFactory;

import com.ibm.icu.text.Collator;

/**
 * Schematic context menu provider
 */
public class SchematicContextMenuProvider extends ContextMenuProvider {

	private static final String INSERT_ROW_MENU_ITEM_TEXT = Messages
			.getString("SchematicContextMenuProvider.Menu.insertRow"); //$NON-NLS-1$

	private static final String EDIT_GROUP_MENU_ITEM_TEXT = Messages
			.getString("SchematicContextMenuProvider.Menu.EditGroup"); //$NON-NLS-1$

	private static final String INSERT_GROUP_HEADER_FOOTER_ITEM_TEXT = Messages
			.getString("SchematicContextMenuProvider.Menu.InsertGroupHeaderFooter"); //$NON-NLS-1$

	private static final String DELETE_GROUP_MENU_ITEM_TEXT = Messages
			.getString("SchematicContextMenuProvider.Menu.DeleteGroup"); //$NON-NLS-1$

	private static final String STYLE_MENU_ITEM_TEXT = Messages.getString("SchematicContextMenuProvider.Menu.Style"); //$NON-NLS-1$

	private static final String APPLY_STYLE_MENU_ITEM_TEXT = Messages
			.getString("SchematicContextMenuProvider.Menu.Style.Apply"); //$NON-NLS-1$

	private static final String THEME_MENU_ITEM_TEXT = Messages.getString("SchematicContextMenuProvider.Menu.Theme"); //$NON-NLS-1$

	private static final String APPLY_THEME_MENU_ITEM_TEXT = Messages
			.getString("SchematicContextMenuProvider.Menu.Theme.Apply"); //$NON-NLS-1$

	private static final String INSERT_MENU_ITEM_TEXT = Messages.getString("SchematicContextMenuProvider.Menu.Insert"); //$NON-NLS-1$

	private static final String ELEMENT_MENU_ITEM_TEXT = Messages
			.getString("SchematicContextMenuProvider.Menu.insertElement"); //$NON-NLS-1$

	private static final String EDIT_STYLE_MENU_ITEM_TEXT = Messages
			.getString("SchematicContextMenuProvider.Menu.EditStyle"); //$NON-NLS-1$

	private static final String DELETE_STYLE_MENU_ITEM_TEXT = Messages
			.getString("SchematicContextMenuProvider.Menu.DeleteStyle"); //$NON-NLS-1$

	private static final String NEW_STYLE_MENU_ITEM_TEXT = Messages
			.getString("SchematicContextMenuProvider.Menu.NewStyle"); //$NON-NLS-1$

	private static final String LAYOUT_PREF_MENU_TEXT = Messages.getString("SchematicContextMenuProvider.Menu.Layout"); //$NON-NLS-1$

	private IMenuListener proxy;

	/** the action registry */
	private final ActionRegistry actionRegistry;

	/**
	 * Constructs a new WorkflowEditorContextMenuProvider instance.
	 * 
	 * @param viewer         the edit part view
	 * @param actionRegistry the actions registry
	 */
	public SchematicContextMenuProvider(EditPartViewer viewer, ActionRegistry actionRegistry) {
		super(viewer);
		this.actionRegistry = actionRegistry;
	}

	/**
	 * Gets the action registry.
	 * 
	 * @return the action registry
	 */
	public ActionRegistry getActionRegistry() {
		return actionRegistry;
	}

	/**
	 * Retrieves action item( value ) from the action registry with the given action
	 * ID( key ).
	 * 
	 * @param actionID the given atcion ID.
	 * @return The retrieved action item.
	 */
	protected IAction getAction(String actionID) {
		IAction action = getActionRegistry().getAction(actionID);
		if (action instanceof UpdateAction) {
			((UpdateAction) action).update();
		}
		return action;
	}

	/**
	 * Gets the current selection.
	 * 
	 * @return The current selection
	 */
	protected ISelection getSelection() {
		return getViewer().getSelection();
	}

	/**
	 * Returns a <code>List</code> containing the currently selected objects.
	 * 
	 * @return A List containing the currently selected objects
	 */
	protected List getSelectedObjects() {
		if (!(getSelection() instanceof IStructuredSelection))
			return Collections.EMPTY_LIST;
		return ((IStructuredSelection) getSelection()).toList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ContextMenuProvider#buildContextMenu(org.eclipse.jface
	 * .action.IMenuManager)
	 */
	public void buildContextMenu(IMenuManager menuManager) {
		if (proxy != null) {
			proxy.menuAboutToShow(menuManager);
			proxy = null;
			return;
		}
		GEFActionConstants.addStandardActionGroups(menuManager);

		Object firstSelectedElement = getFirstElement();
		Object selectedElements = getSelectedElement();
		Object multiSelection = getMultiSelectedElement();

		boolean isExtended = false;
		if (firstSelectedElement instanceof IAdaptable) {
			if (((IAdaptable) firstSelectedElement).getAdapter(DesignElementHandle.class) instanceof ExtendedItemHandle)
				isExtended = true;
		}

		if (selectedElements instanceof ReportDesignHandle) {
			createLayoutPrefMenu((ReportDesignHandle) selectedElements, menuManager, GEFActionConstants.GROUP_VIEW,
					((ReportDesignHandle) selectedElements).getLayoutPreference());
		}

		// special for dealing with multi selected elements (items).
		if (isMutilSelection(multiSelection)) {
			menuManager.appendToGroup(GEFActionConstants.GROUP_UNDO, getAction(ActionFactory.UNDO.getId()));
			menuManager.appendToGroup(GEFActionConstants.GROUP_UNDO, getAction(ActionFactory.REDO.getId()));
			menuManager.appendToGroup(GEFActionConstants.GROUP_COPY, new CutAction(selectedElements));
			menuManager.appendToGroup(GEFActionConstants.GROUP_COPY, new CopyAction(selectedElements));
			menuManager.appendToGroup(GEFActionConstants.GROUP_COPY, new PasteAction(selectedElements));
			menuManager.appendToGroup(GEFActionConstants.GROUP_COPY, getAction(ActionFactory.DELETE.getId()));

			if (isRootElementHandleClass(multiSelection)) {
				Action action = new RefreshModuleHandleAction(selectedElements);
				menuManager.add(action);
				createInsertElementMenu(menuManager, GEFActionConstants.GROUP_EDIT);
				createThemeMenu(selectedElements, menuManager, GEFActionConstants.GROUP_REST);
				action = new ExportToLibraryAction(selectedElements);
				menuManager.add(action);
			}
			if (isListHandleCalss(multiSelection)) {
				IAction action = getAction(CreatePlaceHolderPartAction.ID);
				menuManager.appendToGroup(GEFActionConstants.GROUP_EDIT, action);

			}
			createStyleMenu(menuManager, GEFActionConstants.GROUP_REST);
			if (Policy.TRACING_MENU_SHOW) {
				System.out.println("Menu(for Editor) >> Shows for multi-selcetion."); //$NON-NLS-1$
			}
		}

		// -----------------------------------------------------------------
		else if (firstSelectedElement instanceof DesignElementHandle || isExtended) {

			menuManager.appendToGroup(GEFActionConstants.GROUP_UNDO, getAction(ActionFactory.UNDO.getId()));
			menuManager.appendToGroup(GEFActionConstants.GROUP_UNDO, getAction(ActionFactory.REDO.getId()));
			menuManager.appendToGroup(GEFActionConstants.GROUP_COPY, new CutAction(selectedElements));
			menuManager.appendToGroup(GEFActionConstants.GROUP_COPY, new CopyAction(selectedElements));
			menuManager.appendToGroup(GEFActionConstants.GROUP_COPY, new PasteAction(selectedElements));

			createStyleMenu(menuManager, GEFActionConstants.GROUP_REST);

			if (((IStructuredSelection) getSelection()).size() == 1) {
				Object element = ((IStructuredSelection) getSelection()).getFirstElement();

				if (element instanceof LabelEditPart || element instanceof ImageEditPart) {
					if (element instanceof DataEditPart) {
						IAction action = getAction(ChangeDataColumnPartAction.ID);
						menuManager.appendToGroup(GEFActionConstants.GROUP_EDIT, action);
					}
					IAction action = getAction(GEFActionConstants.DIRECT_EDIT);
					action.setAccelerator(SWT.F2);
					if (element instanceof DataEditPart) {
						action.setText(Messages.getString("SchematicContextMenuProvider.ActionText.editData")); //$NON-NLS-1$
					} else {
						action.setText(Messages.getString("SchematicContextMenuProvider.ActionText.editLabel")); //$NON-NLS-1$
					}
					menuManager.appendToGroup(GEFActionConstants.GROUP_EDIT, action);
					if (element instanceof ImageEditPart) {
						// action = getAction( ResetImageSizeAction.ID );
						// menuManager.appendToGroup(
						// GEFActionConstants.GROUP_EDIT,
						// action );

						createImageMenu((ImageHandle) ((ImageEditPart) element).getModel(), menuManager,
								GEFActionConstants.GROUP_EDIT);
					}

				}

				if (firstSelectedElement instanceof ReportItemHandle) {
					IAction action = getAction(CreatePlaceHolderPartAction.ID);
					menuManager.appendToGroup(GEFActionConstants.GROUP_EDIT, action);

					// action = getAction( RevertToReportItemPartAction.ID );
					// menuManager.appendToGroup( GEFActionConstants.GROUP_EDIT,
					// action );

					// action = getAction( RevertToTemplatePartAction.ID );
					// menuManager.appendToGroup( GEFActionConstants.GROUP_EDIT,
					// action );
				}

				if (firstSelectedElement instanceof TemplateReportItemHandle) {
					IAction action = getAction(RevertToReportItemPartAction.ID);
					menuManager.appendToGroup(GEFActionConstants.GROUP_EDIT, action);
				}
				// add for support multiple view
				Object[] objs = ElementAdapterManager.getAdapters(firstSelectedElement, IReportItemViewProvider.class);
				if (objs != null && objs.length == 1) {
					IAction action = getAction(CreateChartAction.ID);
					menuManager.appendToGroup(GEFActionConstants.GROUP_VIEW, action);
				}
			}

			if (firstSelectedElement instanceof RowHandle) {
				if (getRowHandles().size() != 0) {
					MenuManager insertMenu = new MenuManager(INSERT_MENU_ITEM_TEXT);
					MenuManager rowMenu = new MenuManager(INSERT_ROW_MENU_ITEM_TEXT);
					rowMenu.add(getAction(InsertRowAboveAction.ID));
					rowMenu.add(getAction(InsertRowBelowAction.ID));

					RowHandle row = (RowHandle) getRowHandles().get(0);
					if (!(row.getContainer() instanceof GridHandle)) {
						insertMenu.add(getAction(IncludeHeaderAction.ID));
						insertMenu.add(getAction(IncludeDetailAction.ID));
						insertMenu.add(getAction(IncludeFooterAction.ID));
					}
					insertMenu.add(rowMenu);
					menuManager.appendToGroup(GEFActionConstants.GROUP_EDIT, insertMenu);
				}
				// delete row action.
				menuManager.appendToGroup(GEFActionConstants.GROUP_COPY, getAction(DeleteRowAction.ID));
				menuManager.appendToGroup(GEFActionConstants.GROUP_EDIT, getAction(MergeAction.ID));
				menuManager.appendToGroup(GEFActionConstants.GROUP_EDIT, getAction(SplitAction.ID));
			} else if (firstSelectedElement instanceof ColumnHandle) {
				menuManager.appendToGroup(GEFActionConstants.GROUP_EDIT, new InsertPasteColumnAction(selectedElements));
				if (getColumnHandles().size() != 0) {
					MenuManager subMenu = new MenuManager(INSERT_MENU_ITEM_TEXT);
					subMenu.add(getAction(InsertColumnRightAction.ID));
					subMenu.add(getAction(InsertColumnLeftAction.ID));
					menuManager.appendToGroup(GEFActionConstants.GROUP_EDIT, subMenu);
				}
				// delete column action.
				menuManager.appendToGroup(GEFActionConstants.GROUP_COPY, getAction(DeleteColumnAction.ID));
				menuManager.appendToGroup(GEFActionConstants.GROUP_EDIT, getAction(MergeAction.ID));
				menuManager.appendToGroup(GEFActionConstants.GROUP_EDIT, getAction(SplitAction.ID));
			} else if (firstSelectedElement instanceof CellHandle) {
				createInsertElementMenu(menuManager, GEFActionConstants.GROUP_EDIT);
				menuManager.appendToGroup(GEFActionConstants.GROUP_EDIT, getAction(CopyCellContentsContextAction.ID));
				menuManager.appendToGroup(GEFActionConstants.GROUP_EDIT, getAction(MergeAction.ID));
				menuManager.appendToGroup(GEFActionConstants.GROUP_EDIT, getAction(SplitAction.ID));
				// delete action in cell
				menuManager.appendToGroup(GEFActionConstants.GROUP_COPY, new DeleteAction(selectedElements));
			} else {
				// common delete action
				menuManager.appendToGroup(GEFActionConstants.GROUP_COPY, getAction(ActionFactory.DELETE.getId()));
			}

			// add export action
			menuManager.appendToGroup(GEFActionConstants.GROUP_EDIT, getAction(ExportElementToLibraryPartAction.ID));

			if (Policy.TRACING_MENU_SHOW) {
				System.out.println("Menu(for Editor) >> Shows for " //$NON-NLS-1$
						+ ((DesignElementHandle) firstSelectedElement).getDefn().getDisplayName());
			}
		} else if (firstSelectedElement instanceof SlotHandle) {
			menuManager.appendToGroup(GEFActionConstants.GROUP_UNDO, getAction(ActionFactory.UNDO.getId()));
			menuManager.appendToGroup(GEFActionConstants.GROUP_UNDO, getAction(ActionFactory.REDO.getId()));
			menuManager.appendToGroup(GEFActionConstants.GROUP_COPY, new CutAction(selectedElements));
			menuManager.appendToGroup(GEFActionConstants.GROUP_COPY, new CopyAction(selectedElements));
			menuManager.appendToGroup(GEFActionConstants.GROUP_COPY, new PasteAction(selectedElements));
			menuManager.appendToGroup(GEFActionConstants.GROUP_COPY, new DeleteAction(selectedElements));

			createInsertElementMenu(menuManager, GEFActionConstants.GROUP_EDIT);
			if (Policy.TRACING_MENU_SHOW) {
				System.out.println("Menu(for Editor) >> Shows for the slot " //$NON-NLS-1$
						+ ((SlotHandle) firstSelectedElement).getSlotID() + " of " //$NON-NLS-1$
						+ ((SlotHandle) firstSelectedElement).getElementHandle().getDefn().getDisplayName());
			}
		} else {
		}

		if (!getTableEditParts().isEmpty() || !getTableMultipleEditParts().isEmpty()) {
			if (firstSelectedElement instanceof TableHandle) {
				MenuManager insertMenu = new MenuManager(Messages.getString("TableBandProvider.action.text.row")); //$NON-NLS-1$
				insertMenu.add(getAction(IncludeHeaderAction.ID));
				insertMenu.add(getAction(IncludeDetailAction.ID));
				insertMenu.add(getAction(IncludeFooterAction.ID));
				menuManager.appendToGroup(GEFActionConstants.GROUP_ADD, insertMenu);
			}
			createInsertGroupMenu(menuManager, GEFActionConstants.GROUP_ADD);
			if (getTableEditParts().size() == 1 || getTableMultipleEditParts().size() == 1) {
				createDeleteGroupMenus(menuManager, GEFActionConstants.GROUP_ADD);
				createEditGroupMenu(menuManager, GEFActionConstants.GROUP_ADD);
				createInsertGroupHeaderFooter(menuManager, GEFActionConstants.GROUP_ADD);
				Separator separator = new Separator(EditBindingAction.ID);
				menuManager.add(separator);
				menuManager.appendToGroup(EditBindingAction.ID, getAction(EditBindingAction.ID));
			}
		}

		if (!getListEditParts().isEmpty()) {
			createInsertGroupMenu(menuManager, GEFActionConstants.GROUP_ADD);
			if (getListEditParts().size() == 1) {
				createDeleteGroupMenus(menuManager, GEFActionConstants.GROUP_ADD);
				createEditGroupMenu(menuManager, GEFActionConstants.GROUP_ADD);
				Separator separator = new Separator(EditBindingAction.ID);
				menuManager.add(separator);
				menuManager.appendToGroup(EditBindingAction.ID, getAction(EditBindingAction.ID));
			}
		}

		if (getElements().size() == 1 || isMutilSelection(multiSelection)) {
			if (firstSelectedElement instanceof DesignElementHandle) {
				String elementName = ((DesignElementHandle) firstSelectedElement).getDefn().getName();
				IMenuBuilder menuBuilder = ExtensionPointManager.getInstance().getMenuBuilder(elementName);
				if (menuBuilder != null) {
					menuBuilder.buildMenu(menuManager, getElements());
				}
			}

			Object[] menuAdapters = ElementAdapterManager.getAdapters(firstSelectedElement, IMenuListener.class);

			if (menuAdapters != null && menuAdapters.length > 0) {
				for (int i = 0; i < menuAdapters.length; i++) {
					if (menuAdapters[i] instanceof ISchematicMenuListener) {
						((ISchematicMenuListener) menuAdapters[i]).setActionRegistry(getActionRegistry());
					}
					((IMenuListener) menuAdapters[i]).menuAboutToShow(menuManager);
				}
			}
		}
	}

	private boolean isListHandleCalss(Object multiSelection) {
		return multiSelection == ListHandle.class;
	}

	private boolean isMutilSelection(Object multiSelection) {
		return multiSelection != null && (multiSelection == Object.class // report
				// design and slot multi ?
				|| multiSelection == DesignElementHandle.class
				// report design
				|| isRootElementHandleClass(multiSelection)
				// saveral report items
				|| multiSelection == ReportItemHandle.class
		// table and list
		// || multiSelection == ListHandle.class
		);
	}

	private boolean isRootElementHandleClass(Object obj) {
		return obj == ReportDesignHandle.class || obj == LibraryHandle.class;
	}

	/**
	 * @param menuManager
	 */
	private void createInsertGroupMenu(IMenuManager menuManager, String group_name) {
		if (getFirstElement() instanceof CellHandle || getFirstElement() instanceof RowHandle) {
			RowHandle row;
			if (getFirstElement() instanceof CellHandle) {
				row = (RowHandle) ((CellHandle) getFirstElement()).getContainer();
			} else {
				row = (RowHandle) getFirstElement();
			}
			if (!(row.getContainer() instanceof TableGroupHandle)) {
				int slotID = row.getContainerSlotHandle().getSlotID();
				menuManager.appendToGroup(group_name,
						InsertGroupActionFactory.createInsertGroupAction(slotID, getSelectedObjects()));
				return;
			}

		}

		if (getFirstElement() instanceof SlotHandle) {
			DesignElementHandle container = ((SlotHandle) getFirstElement()).getElementHandle();
			if (!(container instanceof ListGroupHandle)) {
				int slotID = ((SlotHandle) getFirstElement()).getSlotID();
				menuManager.appendToGroup(group_name,
						InsertGroupActionFactory.createInsertGroupAction(slotID, getSelectedObjects()));
				return;
			}
		}

		MenuManager subMenu = new MenuManager(Messages.getString("InsertGroupAction.actionMsg.group")); //$NON-NLS-1$
		Action[] actions = InsertGroupActionFactory.getInsertGroupActions(getSelectedObjects());
		for (int i = 0; i < actions.length; i++) {
			subMenu.add(actions[i]);
		}
		menuManager.appendToGroup(group_name, subMenu);
		return;
	}

	/**
	 * Creats sub menu in the specified action group of the specified menu manager.
	 * 
	 * @param menuManager The menu manager contains the action group.
	 * @param group_name  The action group contains the sub menu.
	 */
	private void createInsertElementMenu(IMenuManager menuManager, String group_name) {
		MenuManager subMenu = new MenuManager(ELEMENT_MENU_ITEM_TEXT);

		IAction action = getAction(GeneralInsertMenuAction.INSERT_LABEL_ID);
		action.setText(GeneralInsertMenuAction.INSERT_LABEL_DISPLAY_TEXT);
		subMenu.add(action);

		action = getAction(GeneralInsertMenuAction.INSERT_TEXT_ID);
		action.setText(GeneralInsertMenuAction.INSERT_TEXT_DISPLAY_TEXT);
		subMenu.add(action);

		action = getAction(GeneralInsertMenuAction.INSERT_DYNAMIC_TEXT_ID);
		action.setText(GeneralInsertMenuAction.INSERT_DYNAMIC_TEXT_DISPLAY_TEXT);
		subMenu.add(action);

		action = getAction(GeneralInsertMenuAction.INSERT_DATA_ID);
		action.setText(GeneralInsertMenuAction.INSERT_DATA_DISPLAY_TEXT);
		subMenu.add(action);

		action = getAction(GeneralInsertMenuAction.INSERT_IMAGE_ID);
		action.setText(GeneralInsertMenuAction.INSERT_IMAGE_DISPLAY_TEXT);
		subMenu.add(action);

		action = getAction(GeneralInsertMenuAction.INSERT_GRID_ID);
		action.setText(GeneralInsertMenuAction.INSERT_GRID_DISPLAY_TEXT);
		subMenu.add(action);

		action = getAction(GeneralInsertMenuAction.INSERT_LIST_ID);
		action.setText(GeneralInsertMenuAction.INSERT_LIST_DISPLAY_TEXT);
		subMenu.add(action);

		action = getAction(GeneralInsertMenuAction.INSERT_TABLE_ID);
		action.setText(GeneralInsertMenuAction.INSERT_TABLE_DISPLAY_TEXT);
		subMenu.add(action);

		/*
		 * Extended Items insert actions
		 */

		CategorizedElementSorter<IAction> elementSorter = new CategorizedElementSorter<IAction>();

		List<ExtendedElementUIPoint> points = ExtensionPointManager.getInstance().getExtendedElementPoints();
		for (Iterator<ExtendedElementUIPoint> iter = points.iterator(); iter.hasNext();) {
			ExtendedElementUIPoint point = iter.next();

			IElementDefn extension = DEUtil.getMetaDataDictionary().getExtension(point.getExtensionName());

			action = getAction(point.getExtensionName());
			if (action != null) {
				String menuLabel = (String) point.getAttribute(IExtensionConstants.ATTRIBUTE_EDITOR_MENU_LABEL);

				action.setText(menuLabel == null ? extension.getDisplayName() : menuLabel);

				String category = (String) point.getAttribute(IExtensionConstants.ATTRIBUTE_PALETTE_CATEGORY);

				elementSorter.addElement(category, action);
			}
		}

		PaletteEntryExtension[] entries = EditpartExtensionManager.getPaletteEntries();
		for (int i = 0; i < entries.length; i++) {
			action = getAction(entries[i].getItemName());
			if (action != null) {
				action.setText(entries[i].getMenuLabel());

				String category = entries[i].getCategory();

				elementSorter.addElement(category, action);
			}
		}

		List<IAction> actions = elementSorter.getSortedElements();

		Collections.sort(actions, new Comparator<IAction>() {

			public int compare(IAction o1, IAction o2) {
				return Collator.getInstance().compare(o1.getText(), o2.getText());
			}
		});

		for (Iterator<IAction> itr = actions.iterator(); itr.hasNext();) {
			subMenu.add(itr.next());
		}

		subMenu.add(new Separator());
		action = getAction(InsertAggregationAction.ID);
		action.setText(InsertAggregationAction.TEXT);
		subMenu.add(action);

		action = getAction(InsertRelativeTimePeriodAction.ID);
		action.setText(InsertRelativeTimePeriodAction.TEXT);
		subMenu.add(action);

		menuManager.appendToGroup(group_name, subMenu);
	}

	/**
	 * Creats sub menu in the specified action group of the specified menu manager.
	 * 
	 * @param menuManager The menu manager contains the action group.
	 * @param group_name  The action group contains the sub menu.
	 */
	private void createStyleMenu(IMenuManager menuManager, String group_name) {
		MenuManager menu = new MenuManager(STYLE_MENU_ITEM_TEXT);
		populateAddStyleAction(menu);
		menu.add(new Separator());

		// add "Edit Style" menu
		MenuManager subMenu = new MenuManager(EDIT_STYLE_MENU_ITEM_TEXT);
		subMenu.add(NoneAction.getInstance());
		subMenu.addMenuListener(new IMenuListener() {

			public void menuAboutToShow(IMenuManager manager) {
				updateDynamicItems(EditStyleMenuAction.ID, manager);
			}
		});
		menu.add(subMenu);

		subMenu = new MenuManager(APPLY_STYLE_MENU_ITEM_TEXT);
		subMenu.add(NoneAction.getInstance());
		subMenu.addMenuListener(new IMenuListener() {

			public void menuAboutToShow(IMenuManager manager) {
				updateDynamicItems(ApplyStyleMenuAction.ID, manager);
			}
		});

		menu.add(subMenu);

		// add "Delete Style" menu
		subMenu = new MenuManager(DELETE_STYLE_MENU_ITEM_TEXT);
		subMenu.add(NoneAction.getInstance());
		subMenu.addMenuListener(new IMenuListener() {

			public void menuAboutToShow(IMenuManager manager) {
				updateDynamicItems(DeleteStyleMenuAction.ID, manager);
			}
		});
		menu.add(subMenu);
		menu.add(new Separator());

		menu.add(getAction(ImportCSSStyleAction.ID));
		menuManager.appendToGroup(group_name, menu);

		menuManager.appendToGroup(group_name, getAction(CopyFormatAction.ID));
		menuManager.appendToGroup(group_name, getAction(PasteFormatAction.ID));
	}

	private void populateAddStyleAction(MenuManager menu) {
		ModuleHandle module = SessionHandleAdapter.getInstance().getReportDesignHandle();

		if (module instanceof ReportDesignHandle) {
			menu.add(getAction(AddStyleAction.ID));
		} else if (module instanceof LibraryHandle) {
			LibraryHandle libraryHandle = (LibraryHandle) module;
			MenuManager subMenu = new MenuManager(NEW_STYLE_MENU_ITEM_TEXT);

			// AddThemeStyleAction
			SlotHandle themeSlot = libraryHandle.getThemes();
			for (Iterator iter = themeSlot.getContents().iterator(); iter.hasNext();) {
				Object obj = iter.next();
				if (obj instanceof AbstractThemeHandle) {
					subMenu.add(new AddThemeStyleAction((AbstractThemeHandle) obj,
							(AddStyleAction) getAction(AddStyleAction.ID)));
				}
			}
			menu.add(subMenu);
		}
	}

	/**
	 * Creates report layout preference menu in the specified action group of the
	 * specified menu manager.
	 * 
	 * @param menuManager The menu manager contains the action group.
	 * @param group_name  The action group contains the sub menu.
	 */
	private void createLayoutPrefMenu(ReportDesignHandle handle, IMenuManager menuManager, String group_name,
			String layout) {
		MenuManager menu = new MenuManager(LAYOUT_PREF_MENU_TEXT,
				ReportPlatformUIImages.getImageDescriptor(IReportGraphicConstants.ICON_LAYOUT_PREFERENCE), null);

		menu.add(new ApplyLayoutPreferenceAction(handle, DesignChoiceConstants.REPORT_LAYOUT_PREFERENCE_AUTO_LAYOUT));
		menu.add(new ApplyLayoutPreferenceAction(handle, DesignChoiceConstants.REPORT_LAYOUT_PREFERENCE_FIXED_LAYOUT));
		menuManager.appendToGroup(group_name, menu);
	}

	private void createImageMenu(ImageHandle imageHandle, IMenuManager menuManager, String group_name) {
		MenuManager menu = new MenuManager(Messages.getString("SchematicContextMenuProvider.label.ResetSize")); //$NON-NLS-1$

		IAction action = new ResetImageOriginalSizeAction(imageHandle,
				Messages.getString("SchematicContextMenuProvider.label.RestToOrginalPixels"), //$NON-NLS-1$
				ResetImageOriginalSizeAction.BYORIGINAL);
		if (action.isEnabled()) {
			menu.add(action);
		}
		action = new ResetImageOriginalSizeAction(imageHandle,
				Messages.getString("SchematicContextMenuProvider.label.ResetToImageDPI"), //$NON-NLS-1$
				ResetImageOriginalSizeAction.BYIMAGEDPI);
		if (action.isEnabled()) {
			menu.add(action);
		}
		action = new ResetImageOriginalSizeAction(imageHandle,
				Messages.getString("SchematicContextMenuProvider.label.ResetToReportDPI"), //$NON-NLS-1$
				ResetImageOriginalSizeAction.BYREPORTDPI);
		if (action.isEnabled()) {
			menu.add(action);
		}
		action = new ResetImageOriginalSizeAction(imageHandle,
				Messages.getString("SchematicContextMenuProvider.label.ResetToScreenDPI"), //$NON-NLS-1$
				ResetImageOriginalSizeAction.BYSCREENDPI);
		if (action.isEnabled()) {
			menu.add(action);
		}

		menuManager.appendToGroup(group_name, menu);
	}

	/**
	 * Creats sub menu in the specified action group of the specified menu manager.
	 * 
	 * @param menuManager The menu manager contains the action group.
	 * @param group_name  The action group contains the sub menu.
	 */
	private void createThemeMenu(Object selectedObject, IMenuManager menuManager, String group_name) {
		MenuManager menu = new MenuManager(THEME_MENU_ITEM_TEXT);
		if (selectedObject instanceof LibraryHandle) {
			SlotHandle obj = ((LibraryHandle) selectedObject).getThemes();
			menu.add(new ExtendElementAction(ProviderFactory.createProvider(obj),
					"org.eclipse.birt.report.designer.internal.ui.action.NewThemeAction", //$NON-NLS-1$
					obj, Messages.getString("ThemesNodeProvider.action.New"), ReportDesignConstants.THEME_ITEM)); //$NON-NLS-1$

			Object adapter = ElementAdapterManager.getAdapter(selectedObject, IExtendElementActionFactory.class);

			if (adapter instanceof IExtendElementActionFactory) {
				SlotHandle slot = ((LibraryHandle) selectedObject).getSlot(LibraryHandle.THEMES_SLOT);
				menu.add(((IExtendElementActionFactory) adapter).getAction(slot));
			}
		}

		MenuManager subMenu = new MenuManager(APPLY_THEME_MENU_ITEM_TEXT);
		subMenu.add(NoneAction.getInstance());
		subMenu.addMenuListener(new IMenuListener() {

			public void menuAboutToShow(IMenuManager manager) {
				updateDynamicItems(ApplyThemeMenuAction.ID, manager);
			}
		});

		menu.add(subMenu);
		menu.add(new Separator());
		menuManager.appendToGroup(group_name, menu);
	}

	private void updateDynamicItems(String actionId, IMenuManager menu) {
		IAction action = getAction(actionId);
		if (action != null && action instanceof MenuUpdateAction) {
			((MenuUpdateAction) action).updateMenu((MenuManager) menu);
		}
	}

	/**
	 * Creats sub menu in the specified action group of the specified menu manager.
	 * 
	 * @param menuManager The menu manager contains the action group.
	 * @param group_name  The action group contains the sub menu.
	 */
	private void createEditGroupMenu(IMenuManager menuManager, String group_name) {
		// If select on Group, no need to provide cascade menu
		if (getFirstElement() instanceof RowHandle) {
			DesignElementHandle container = ((RowHandle) getFirstElement()).getContainer();
			if (container instanceof TableGroupHandle) {
				Action action = new EditGroupAction(null, (TableGroupHandle) container);
				action.setText(EDIT_GROUP_MENU_ITEM_TEXT);
				menuManager.appendToGroup(group_name, action);
				return;
			}
		}

		if (getFirstElement() instanceof SlotHandle) {
			DesignElementHandle container = ((SlotHandle) getFirstElement()).getElementHandle();
			if (container instanceof ListGroupHandle) {
				Action action = new EditGroupAction(null, (ListGroupHandle) container);
				action.setText(EDIT_GROUP_MENU_ITEM_TEXT);
				menuManager.appendToGroup(group_name, action);
				return;
			}
		}

		MenuManager subMenu = new MenuManager(EDIT_GROUP_MENU_ITEM_TEXT);
		ListingHandle parentHandle = null;

		if (!getTableEditParts().isEmpty())

		{
			parentHandle = (ListingHandle) ((TableEditPart) getTableEditParts().get(0)).getModel();
		} else if (!getTableMultipleEditParts().isEmpty()) {
			parentHandle = (ListingHandle) ((ReportElementEditPart) getTableMultipleEditParts().get(0)).getModel();

		} else if (!getListEditParts().isEmpty()) {
			parentHandle = (ListingHandle) ((ListEditPart) getListEditParts().get(0)).getModel();
		} else {
			return;
		}
		SlotHandle handle = parentHandle.getGroups();
		Iterator iter = handle.iterator();
		while (iter.hasNext()) {
			GroupHandle groupHandle = (GroupHandle) iter.next();
			subMenu.add(new EditGroupAction(null, groupHandle));
		}
		menuManager.appendToGroup(group_name, subMenu);
	}

	/**
	 * Gets element handles.
	 * 
	 * @return element handles
	 */
	protected List getElements() {
		return InsertInLayoutUtil.editPart2Model(getSelection()).toList();
	}

	/**
	 * Gets the current selected object.
	 * 
	 * @return The current selected object array. If length is one, return the first
	 */
	protected Object getSelectedElement() {
		Object[] array = getElements().toArray();
		if (array.length == 1) {
			return array[0];
		}
		return array;
	}

	/**
	 * Gets the first selected object.
	 * 
	 * @return The first selected object
	 */
	protected Object getFirstElement() {
		Object[] array = getElements().toArray();
		if (array.length > 0) {
			return array[0];
		}
		return null;
	}

	/**
	 * Gets multiple selected elements
	 * 
	 * @return The (base) class type all the multi selected elements
	 */
	private Object getMultiSelectedElement() {
		List list = getElements();
		Object baseHandle = list.get(0);
		if (baseHandle != null) {

			Class base = baseHandle.getClass();

			for (int i = 1; i < list.size(); i++) {
				Object obj = list.get(i);
				if (base.isInstance(obj)) {
					continue;
				}
				// Ensure multi selected elements are instance of the "base"
				// class.
				while (!base.isInstance(obj)) {
					base = base.getSuperclass();
				}
				continue;
			}
			return base;
		}
		return null;
	}

	/**
	 * Gets the current selected row objects.
	 * 
	 * @return The current selected row objects.
	 */

	public List getRowHandles() {
		List list = getSelectedObjects();
		if (list.isEmpty())
			return Collections.EMPTY_LIST;

		List rowHandles = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			Object obj = list.get(i);
			if (obj instanceof DummyEditpart) {
				if (((DummyEditpart) obj).getModel() instanceof RowHandle) {
					rowHandles.add(((DummyEditpart) obj).getModel());
				}
			}
		}
		return rowHandles;
	}

	/**
	 * Gets the current selected column objects.
	 * 
	 * @return The current column objects
	 */
	public List getColumnHandles() {
		List list = getSelectedObjects();
		if (list.isEmpty())
			return Collections.EMPTY_LIST;

		List columnHandles = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			Object obj = list.get(i);
			if (obj instanceof DummyEditpart) {
				if (((DummyEditpart) obj).getModel() instanceof ColumnHandle) {
					columnHandles.add(((DummyEditpart) obj).getModel());
				}
			}
		}
		return columnHandles;
	}

	// support the multiple view pop menu

	private List getTableMultipleEditParts() {
		List tableParts = new ArrayList();
		for (Iterator itor = getSelectedObjects().iterator(); itor.hasNext();) {
			Object obj = itor.next();
			if (obj instanceof DummyEditpart) {
				// Column or Row indicators
				// ignore, do nothing.
			} else if (obj instanceof MultipleEditPart && ((MultipleEditPart) obj).getModel() instanceof TableHandle) {
				if (!(tableParts.contains(obj))) {
					tableParts.add(obj);
				}
			} else {
				return Collections.EMPTY_LIST;
			}
		}
		return tableParts;
	}

	/**
	 * Gets table edit part.
	 * 
	 * @return The current selected table edit part, null if no table edit part is
	 *         selected.
	 */
	protected List getTableEditParts() {
		List tableParts = new ArrayList();
		for (Iterator itor = getSelectedObjects().iterator(); itor.hasNext();) {
			Object obj = itor.next();
			if (obj instanceof DummyEditpart) {
				// Column or Row indicators
				// ignore, do nothing.
			} else if (obj instanceof TableEditPart) {
				if (obj instanceof GridEditPart) {
					return Collections.EMPTY_LIST;
				}
				if (!(tableParts.contains(obj))) {
					tableParts.add(obj);
				}
			} else if (obj instanceof TableCellEditPart) {
				TableEditPart parent2 = (TableEditPart) ((TableCellEditPart) obj).getParent();
				Object parent = parent2;
				if (parent instanceof GridEditPart) {
					return Collections.EMPTY_LIST;
				}
				if (!(tableParts.contains(parent))) {
					tableParts.add(parent);
				}
			} else {
				return Collections.EMPTY_LIST;
			}
		}
		return tableParts;
	}

	/**
	 * Gets list edit parts.
	 * 
	 * @return The current selected list edit parts, null if no list edit part is
	 *         selected.
	 */
	protected List getListEditParts() {
		List listParts = new ArrayList();
		for (Iterator iter = getSelectedObjects().iterator(); iter.hasNext();) {
			Object obj = iter.next();
			if (obj instanceof ListEditPart) {
				if (!(listParts.contains(obj))) {
					listParts.add(obj);
				}
			} else if (obj instanceof ListBandEditPart) {
				ListEditPart parent2 = (ListEditPart) ((ListBandEditPart) obj).getParent();
				Object parent = parent2;
				if (!(listParts.contains(parent))) {
					listParts.add(parent);
				}
			} else {
				return Collections.EMPTY_LIST;
			}
		}
		return listParts;
	}

	/**
	 * Creats sub menu in the specified action group of the specified menu manager.
	 * 
	 * @param menuManager The menu manager contains the action group.
	 * @param group_name  The action group contains the sub menu.
	 */
	private void createDeleteGroupMenus(IMenuManager menuManager, String group_name) {
		ReportElementEditPart editPart = null;
		// If select on Group, no need to provide cascade menu
		if (getFirstElement() instanceof RowHandle) {
			DesignElementHandle container = ((RowHandle) getFirstElement()).getContainer();
			if (container instanceof TableGroupHandle) {
				editPart = getTableEditPart();
				Action action = new DeleteGroupAction(editPart, (TableGroupHandle) container);
				action.setText(DELETE_GROUP_MENU_ITEM_TEXT);
				menuManager.appendToGroup(group_name, action);
				return;
			}
		}

		if (getFirstElement() instanceof SlotHandle) {
			DesignElementHandle container = ((SlotHandle) getFirstElement()).getElementHandle();
			if (container instanceof ListGroupHandle) {
				editPart = getListEditPart();
				Action action = new DeleteGroupAction(editPart, (ListGroupHandle) container);
				action.setText(DELETE_GROUP_MENU_ITEM_TEXT);
				menuManager.appendToGroup(group_name, action);
				return;
			}
		}

		MenuManager subMenu = new MenuManager(DELETE_GROUP_MENU_ITEM_TEXT);
		ListingHandle parentHandle = null;

		if (!getTableEditParts().isEmpty())

		{
			parentHandle = (ListingHandle) ((TableEditPart) getTableEditParts().get(0)).getModel();
			editPart = (TableEditPart) getTableEditParts().get(0);
		} else if (!getTableMultipleEditParts().isEmpty()) {
			parentHandle = (ListingHandle) ((ReportElementEditPart) getTableMultipleEditParts().get(0)).getModel();
			editPart = (ReportElementEditPart) getTableMultipleEditParts().get(0);
		} else if (!getListEditParts().isEmpty()) {
			parentHandle = (ListingHandle) ((ListEditPart) getListEditParts().get(0)).getModel();
			editPart = (ListEditPart) getListEditParts().get(0);
		} else {
			return;
		}

		SlotHandle handle = parentHandle.getGroups();
		Iterator iter = handle.iterator();
		while (iter.hasNext()) {
			GroupHandle groupHandle = (GroupHandle) iter.next();
			subMenu.add(new DeleteGroupAction(editPart, groupHandle));
		}

		menuManager.appendToGroup(group_name, subMenu);
	}

	/**
	 * Gets table edit part.
	 * 
	 * @return the table edit part
	 */
	protected TableEditPart getTableEditPart() {
		List list = getSelectedObjects();
		if (list.isEmpty()) {
			return null;
		}
		TableEditPart part = null;
		for (int i = 0; i < list.size(); i++) {
			Object obj = list.get(i);
			if (obj instanceof TableEditPart) {
				part = (TableEditPart) obj;
			} else if (obj instanceof TableCellEditPart) {
				part = (TableEditPart) ((TableCellEditPart) obj).getParent();
			}
		}
		return part;
	}

	/**
	 * Gets list edit part.
	 * 
	 * @return The current selected list edit part, null if no list edit part is
	 *         selected.
	 */
	protected ListEditPart getListEditPart() {
		List list = getSelectedObjects();
		if (list.isEmpty()) {
			return null;
		}
		ListEditPart part = null;
		for (int i = 0; i < list.size(); i++) {
			Object obj = list.get(i);
			if (obj instanceof ListEditPart) {
				part = (ListEditPart) obj;
			} else if (obj instanceof ListBandEditPart) {
				part = (ListEditPart) ((ListBandEditPart) obj).getParent();
			}
		}
		return part;
	}

	/**
	 * Creats sub menu in the specified action group of the specified menu manager.
	 * 
	 * @param menuManager The menu manager contains the action group.
	 * @param group_name  The action group contains the sub menu.
	 */
	private void createInsertGroupHeaderFooter(IMenuManager menuManager, String group_name) {
		// If select on Group, no need to provide cascade menu
		MenuManager subMenu = new MenuManager(INSERT_GROUP_HEADER_FOOTER_ITEM_TEXT);
		if (getFirstElement() instanceof RowHandle) {
			DesignElementHandle container = ((RowHandle) getFirstElement()).getContainer();
			if (container instanceof TableGroupHandle) {
				GroupHandle groupHandle = (TableGroupHandle) container;
				subMenu.add(new InsertGroupHeaderFooterAction(groupHandle, InsertGroupHeaderFooterAction.HEADER));
				subMenu.add(new InsertGroupHeaderFooterAction(groupHandle, InsertGroupHeaderFooterAction.FOOTER));
				menuManager.appendToGroup(group_name, subMenu);
				return;
			}
		}

		if (getFirstElement() instanceof SlotHandle) {
			return;
		}

		ListingHandle parentHandle = null;

		if (!getTableEditParts().isEmpty()) {
			parentHandle = (ListingHandle) ((TableEditPart) getTableEditParts().get(0)).getModel();
		} else if (!getTableMultipleEditParts().isEmpty()) {
			parentHandle = (ListingHandle) ((ReportElementEditPart) getTableMultipleEditParts().get(0)).getModel();
		} else {
			return;
		}

		SlotHandle handle = parentHandle.getGroups();
		Iterator iter = handle.iterator();
		while (iter.hasNext()) {
			GroupHandle groupHandle = (GroupHandle) iter.next();
			MenuManager groupMenu = new MenuManager(DEUtil.getEscapedMenuItemText(groupHandle.getDisplayLabel()));
			groupMenu.add(new InsertGroupHeaderFooterAction(groupHandle, InsertGroupHeaderFooterAction.HEADER));
			groupMenu.add(new InsertGroupHeaderFooterAction(groupHandle, InsertGroupHeaderFooterAction.FOOTER));
			subMenu.add(groupMenu);
		}
		menuManager.appendToGroup(group_name, subMenu);
	}

	public IMenuListener getProxy() {
		return proxy;
	}

	public void setProxy(IMenuListener proxy) {
		this.proxy = proxy;
	}

}
