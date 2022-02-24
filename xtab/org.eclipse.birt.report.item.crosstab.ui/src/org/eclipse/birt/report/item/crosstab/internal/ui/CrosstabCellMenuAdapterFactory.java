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

package org.eclipse.birt.report.item.crosstab.internal.ui;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.providers.ISchematicMenuListener;
import org.eclipse.birt.report.designer.internal.ui.extension.ExtendedElementUIPoint;
import org.eclipse.birt.report.designer.internal.ui.extension.ExtensionPointManager;
import org.eclipse.birt.report.designer.internal.ui.extension.experimental.EditpartExtensionManager;
import org.eclipse.birt.report.designer.internal.ui.extension.experimental.PaletteEntryExtension;
import org.eclipse.birt.report.designer.internal.ui.util.CategorizedElementSorter;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.actions.GeneralInsertMenuAction;
import org.eclipse.birt.report.designer.ui.actions.InsertAggregationAction;
import org.eclipse.birt.report.designer.ui.actions.InsertRelativeTimePeriodAction;
import org.eclipse.birt.report.designer.ui.extensions.IExtensionConstants;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.item.crosstab.core.de.ComputedMeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.action.AddComputedMeasureAction;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.action.AddLevelHandleAction;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.action.AddMeasureViewHandleAction;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.action.AddRelativeTimePeriodAction;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.action.AddSubTotalAction;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.action.CopyCrosstabCellContentsAction;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.action.DeleteDimensionViewHandleAction;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.action.DeleteMeasureHandleAction;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.action.MergeCrosstabHeaderCellAction;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.action.ShowAsViewMenuAction;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.action.SplitCrosstabHeadCellAction;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabAdaptUtil;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabCellAdapter;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.ICrosstabCellAdapterFactory;
import org.eclipse.birt.report.item.crosstab.ui.extension.IAggregationCellViewProvider;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.UpdateAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;

import com.ibm.icu.text.Collator;

/**
 * 
 */

public class CrosstabCellMenuAdapterFactory implements IAdapterFactory {

	private void createMeasureMenu(IMenuManager menu, Object firstSelectedObj, IContributionItem beforeThis) {
		DesignElementHandle element = null;
		String firstId = beforeThis.getId();
		if (firstSelectedObj instanceof DesignElementHandle) {
			element = (DesignElementHandle) firstSelectedObj;
		} else if (firstSelectedObj instanceof CrosstabCellAdapter) {
			element = ((CrosstabCellAdapter) firstSelectedObj).getDesignElementHandle();
		}
		if (element != null) {

			buildShowMenu(menu, element, firstId);

			IAction action = new AddRelativeTimePeriodAction(element);
			// if (action.isEnabled( ))
			{
				menu.insertBefore(firstId, action);
			}

			action = new AddComputedMeasureAction(element);
			menu.insertBefore(firstId, action);

			action = new AddMeasureViewHandleAction(element);
			menu.insertBefore(firstId, action);

			action = new DeleteMeasureHandleAction(element);
			menu.insertBefore(firstId, action);
		}
	}

	protected void buildShowMenu(IMenuManager menu, DesignElementHandle element, String firstId) {

		ExtendedItemHandle extendedHandle = CrosstabAdaptUtil.getExtendedItemHandle(element);
		MeasureViewHandle measureViewHandle = CrosstabAdaptUtil.getMeasureViewHandle(extendedHandle);
		if (measureViewHandle == null || measureViewHandle instanceof ComputedMeasureViewHandle
				|| (measureViewHandle.getCubeMeasure() != null && measureViewHandle.getCubeMeasure().isCalculated())) {
			return;
		}
		AggregationCellProviderWrapper providerWrapper = new AggregationCellProviderWrapper(
				measureViewHandle.getCrosstab());
		IAggregationCellViewProvider[] providers = providerWrapper.getAllProviders();
		int count = 1;
		for (int i = 0; i < providers.length; i++) {
			IAggregationCellViewProvider provider = providers[i];
			if (provider == null) {
				continue;
			}
			ShowAsViewMenuAction showAsViewAction = new ShowAsViewMenuAction(element, provider.getViewName(), count);
			count++;
			menu.insertBefore(firstId, showAsViewAction);
		}
		menu.insertBefore(firstId, new Separator());

	}

	private void createLevelMenu(IMenuManager menu, Object firstSelectedObj, IContributionItem beforeThis) {
		DesignElementHandle element = null;
		if (firstSelectedObj instanceof DesignElementHandle) {
			element = (DesignElementHandle) firstSelectedObj;
		} else if (firstSelectedObj instanceof CrosstabCellAdapter) {
			element = ((CrosstabCellAdapter) firstSelectedObj).getDesignElementHandle();
		}

		String firstId = beforeThis.getId();
		if (element != null) {
			IAction action = new AddLevelHandleAction(element);
			// if (!CrosstabUtil.isBoundToLinkedDataSet( getCrosstab(element) ))
			{
				menu.insertBefore(firstId, action);
			}

			action = new AddSubTotalAction(element);
			menu.insertBefore(firstId, action);

			action = new DeleteDimensionViewHandleAction(element);
			menu.insertBefore(firstId, action);
		}

	}

	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adaptableObject instanceof CrosstabCellAdapter
				&& ((CrosstabCellAdapter) adaptableObject).getCrosstabCellHandle() != null
				&& adapterType == IMenuListener.class) {
			final String position = ((CrosstabCellAdapter) adaptableObject).getPositionType();
			final CrosstabCellAdapter firstSelectedElement = (CrosstabCellAdapter) adaptableObject;

			return new ISchematicMenuListener() {

				private ActionRegistry actionRegistry;

				public void menuAboutToShow(IMenuManager manager) {
					// items.length must be larger than 0
					IContributionItem items[] = manager.getItems();
					IContributionItem firstMemuItem = items[0];

					if (ICrosstabCellAdapterFactory.CELL_FIRST_LEVEL_HANDLE.equals(position)) {
						createLevelMenu(manager, firstSelectedElement, firstMemuItem);
						manager.insertBefore(firstMemuItem.getId(), new Separator());
					} else if (ICrosstabCellAdapterFactory.CELL_MEASURE.equals(position)) {
						createMeasureMenu(manager, firstSelectedElement, firstMemuItem);
						manager.insertBefore(firstMemuItem.getId(), new Separator());
					} else if (ICrosstabCellAdapterFactory.CROSSTAB_HEADER.equals(position)) {
						IAction action = new SplitCrosstabHeadCellAction(
								firstSelectedElement.getCrosstabCellHandle().getModelHandle());
						// if (action.isEnabled( ))
						{
							manager.insertBefore(firstMemuItem.getId(), action);

						}
						action = new MergeCrosstabHeaderCellAction(
								firstSelectedElement.getCrosstabCellHandle().getModelHandle());
						// if (action.isEnabled( ))
						{
							manager.insertBefore(firstMemuItem.getId(), action);
							manager.insertBefore(firstMemuItem.getId(), new Separator());
						}
						manager.insertBefore(firstMemuItem.getId(), new Separator());
					}

					MenuManager subMenu = new MenuManager(
							Messages.getString("SchematicContextMenuProvider.Menu.insertElement")); //$NON-NLS-1$

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

					List<ExtendedElementUIPoint> points = ExtensionPointManager.getInstance()
							.getExtendedElementPoints();
					for (Iterator<ExtendedElementUIPoint> iter = points.iterator(); iter.hasNext();) {
						ExtendedElementUIPoint point = iter.next();

						IElementDefn extension = DEUtil.getMetaDataDictionary().getExtension(point.getExtensionName());

						action = getAction(point.getExtensionName());
						if (action != null) {
							String menuLabel = (String) point
									.getAttribute(IExtensionConstants.ATTRIBUTE_EDITOR_MENU_LABEL);

							action.setText(menuLabel == null ? extension.getDisplayName() : menuLabel);

							String category = (String) point
									.getAttribute(IExtensionConstants.ATTRIBUTE_PALETTE_CATEGORY);

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

					manager.add(new CopyCrosstabCellContentsAction(firstSelectedElement.getCrosstabCellHandle()));

					manager.add(subMenu);
				}

				public void setActionRegistry(ActionRegistry actionRegistry) {
					this.actionRegistry = actionRegistry;
				}

				protected IAction getAction(String actionID) {
					IAction action = getActionRegistry().getAction(actionID);
					if (action instanceof UpdateAction) {
						((UpdateAction) action).update();
					}
					return action;
				}

				private ActionRegistry getActionRegistry() {
					if (actionRegistry == null)
						actionRegistry = new ActionRegistry();
					return actionRegistry;
				}
			};
		}
		return null;
	}

	public Class[] getAdapterList() {
		// TODO Auto-generated method stub
		return null;
	}

	private CrosstabReportItemHandle getCrosstab(DesignElementHandle handle) {
		if (handle == null) {
			return null;
		}

		IReportItem item = null;
		try {
			item = ((ExtendedItemHandle) handle).getReportItem();
		} catch (ExtendedElementException e) {
			return null;
		}

		if (item instanceof CrosstabReportItemHandle) {
			return (CrosstabReportItemHandle) item;
		}

		return getCrosstab(handle.getContainer());
	}

}
