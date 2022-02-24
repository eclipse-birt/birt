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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.designer.core.model.schematic.ListBandProxy;
import org.eclipse.birt.report.designer.internal.ui.command.CommandUtils;
import org.eclipse.birt.report.designer.internal.ui.command.ICommandParameterNameContants;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.DummyEditpart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.GridEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ListBandEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ListEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.MultipleEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableCellEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.jface.action.Action;

/**
 * The factory for creating actions to insert table or list group in the
 * position
 */

public class InsertGroupActionFactory {

	protected Logger logger = Logger.getLogger(InsertGroupActionFactory.class.getName());

	private static InsertPositionGroupAction[] instances = new InsertPositionGroupAction[] {
			new InsertAboveGroupAction(null, Messages.getString("InsertPositionGroupAction.Label.Above")), //$NON-NLS-1$
			new InsertBelowGroupAction(null, Messages.getString("InsertPositionGroupAction.Label.Below")), //$NON-NLS-1$
			// new InsertIntoGroupAction( null,
			// Messages.getString( "InsertPositionGroupAction.Label.Into" ) )
			// //$NON-NLS-1$
	};

	/**
	 * Creates a insert group action, given slotid and selection list.
	 * 
	 * @param slotID    slotid
	 * @param selection selected editparts
	 * @return action
	 */
	public static Action createInsertGroupAction(int slotID, List selection) {
		if (slotID == TableHandle.HEADER_SLOT || slotID == ListHandle.HEADER_SLOT) {
			return new InsertAboveGroupAction(selection,
					Messages.getString("InsertGroupActionFactory.label.insertGroup")); //$NON-NLS-1$
		} else if (slotID == TableHandle.DETAIL_SLOT || slotID == ListHandle.DETAIL_SLOT) {
			return new InsertBelowGroupAction(selection,
					Messages.getString("InsertGroupActionFactory.label.insertGroup")); //$NON-NLS-1$
		} else {
			return new InsertBelowGroupAction(selection,
					Messages.getString("InsertGroupActionFactory.label.insertGroup")); //$NON-NLS-1$
		}
	}

	/**
	 * Gets actions array
	 * 
	 * @param selection selected editparts
	 * @return actions array
	 */
	public static Action[] getInsertGroupActions(List selection) {
		initInstances(selection);
		return instances;
	}

	private static void initInstances(List selection) {
		for (int i = 0; i < instances.length; i++) {
			instances[i].setSelection(selection);
		}
	}
}

abstract class InsertPositionGroupAction extends Action {

	protected Logger logger = Logger.getLogger(InsertPositionGroupAction.class.getName());

	private Object currentModel;

	private List selection;

	protected static final int POSITION_TOP_LEVEL = 0;
	protected static final int POSITION_INNERMOST = -1;

	protected InsertPositionGroupAction(List selection, String text) {
		super();
		this.selection = selection;
		setText(text);
	}

	public void setSelection(List selection) {
		this.selection = selection;
		this.currentModel = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#isEnabled()
	 */
	public boolean isEnabled() {
		boolean canContain = false;

		if (getTableEditPart() != null) {
			TableHandle table = (TableHandle) getTableEditPart().getModel();
			if (table.getDataBindingType() == ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF)
				canContain = false;
			else
				canContain = table.canContain(TableHandle.GROUP_SLOT, ReportDesignConstants.TABLE_GROUP_ELEMENT);
		}

		if (getTableMultipleEditPart() != null) {
			TableHandle table = (TableHandle) getTableMultipleEditPart().getModel();
			if (table.getDataBindingType() == ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF)
				canContain = false;
			else
				canContain = table.canContain(TableHandle.GROUP_SLOT, ReportDesignConstants.TABLE_GROUP_ELEMENT);
		}

		if (getListEditPart() != null) {
			ListHandle list = (ListHandle) getListEditPart().getModel();
			if (list.getDataBindingType() == ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF)
				canContain = false;
			canContain = ((ListHandle) getListEditPart().getModel()).canContain(ListHandle.GROUP_SLOT,
					ReportDesignConstants.LIST_GROUP_ELEMENT);

		}
		return canContain;
	}

	/**
	 * Runs action.
	 * 
	 */
	public void run() {

		CommandUtils.setVariable(ICommandParameterNameContants.INSERT_GROUP_CURRENT_MODEL_NAME, currentModel);

		CommandUtils.setVariable(ICommandParameterNameContants.INSERT_GROUP_POSITION, Integer.valueOf(getPosition()));
		try {
			CommandUtils.executeCommand("org.eclipse.birt.report.designer.ui.command.insertGroupCommand", //$NON-NLS-1$
					null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.log(Level.SEVERE, e.getMessage(), e);
		}

	}

	protected boolean isGroup() {
		if (getRowHandle() != null) {
			return getRowHandle().getContainer() instanceof GroupHandle;
		} else if (getListBandProxy() != null) {
			return getListBandProxy().getElemtHandle() instanceof GroupHandle;
		}
		return false;
	}

	/**
	 * Returns if the order is reverse
	 * 
	 * @return true when slot is not footer
	 */
	protected boolean isNotReverse() {
		return true;
		// if ( currentModel != null )
		// {
		// if ( isGroup( ) )
		// {
		// if ( getRowHandle( ) != null )
		// {
		// return getRowHandle( ).getContainerSlotHandle( )
		// .getSlotID( ) != GroupHandle.FOOTER_SLOT;
		// }
		// else if ( getListBandProxy( ) != null )
		// {
		// return getListBandProxy( ).getSlotId( ) != GroupHandle.FOOTER_SLOT;
		// }
		// }
		// else
		// {
		// if ( getRowHandle( ) != null )
		// {
		// return getRowHandle( ).getContainerSlotHandle( )
		// .getSlotID( ) != TableHandle.FOOTER_SLOT;
		// }
		// else if ( getListBandProxy( ) != null )
		// {
		// return getListBandProxy( ).getSlotId( ) != ListHandle.FOOTER_SLOT;
		// }
		// }
		// }
		// return true;
	}

	/**
	 * Gets table edit part.
	 * 
	 * @return The current selected table edit part, null if no table edit part is
	 *         selected.
	 */
	protected TableEditPart getTableEditPart() {
		if (getSelection() == null || getSelection().isEmpty())
			return null;
		List list = getSelection();
		int size = list.size();
		TableEditPart part = null;
		for (int i = 0; i < size; i++) {
			Object obj = getSelection().get(i);
			if (i == 0 && obj instanceof ReportElementEditPart) {
				currentModel = ((ReportElementEditPart) obj).getModel();
			}

			TableEditPart currentEditPart = null;
			if (obj instanceof TableEditPart) {
				currentEditPart = (TableEditPart) obj;
			} else if (obj instanceof TableCellEditPart) {
				currentEditPart = (TableEditPart) ((TableCellEditPart) obj).getParent();
			} else if (obj instanceof DummyEditpart) {
				continue;
			}
			if (part == null) {
				part = currentEditPart;
			}
			// Check if select only one table
			if (currentEditPart == null || currentEditPart != null && part != currentEditPart) {
				return null;
			}
		}
		// Only table permitted
		if (part instanceof GridEditPart)
			return null;
		return part;
	}

	// fix bug 217589
	private ReportElementEditPart getTableMultipleEditPart() {
		if (getSelection() == null || getSelection().isEmpty())
			return null;
		List list = getSelection();
		int size = list.size();
		ReportElementEditPart part = null;
		for (int i = 0; i < size; i++) {
			Object obj = getSelection().get(i);
			if (i == 0 && obj instanceof ReportElementEditPart) {
				currentModel = ((ReportElementEditPart) obj).getModel();
			}

			ReportElementEditPart currentEditPart = null;
			if (obj instanceof MultipleEditPart && ((MultipleEditPart) obj).getModel() instanceof TableHandle) {
				currentEditPart = (ReportElementEditPart) obj;
			}

			else if (obj instanceof DummyEditpart) {
				continue;
			}
			if (part == null) {
				part = currentEditPart;
			}
			// Check if select only one table
			if (currentEditPart == null || currentEditPart != null && part != currentEditPart) {
				return null;
			}
		}
		// Only table permitted
		if (part instanceof GridEditPart)
			return null;
		return part;
	}

	/**
	 * Gets list edit part.
	 * 
	 * @return The current selected list edit part, null if no list edit part is
	 *         selected.
	 */
	protected ListEditPart getListEditPart() {
		if (getSelection() == null || getSelection().isEmpty())
			return null;
		List list = getSelection();
		int size = list.size();
		ListEditPart part = null;
		for (int i = 0; i < size; i++) {
			Object obj = getSelection().get(i);
			if (i == 0 && obj instanceof ReportElementEditPart) {
				currentModel = ((ReportElementEditPart) obj).getModel();
			}

			ListEditPart currentEditPart = null;
			if (obj instanceof ListEditPart) {
				currentEditPart = (ListEditPart) obj;
			} else if (obj instanceof ListBandEditPart) {
				currentEditPart = (ListEditPart) ((ListBandEditPart) obj).getParent();
			}
			if (part == null) {
				part = currentEditPart;
			}
			// Check if select only one list
			if (currentEditPart == null || currentEditPart != null && part != currentEditPart) {
				return null;
			}
		}
		return part;
	}

	public List getSelection() {
		return selection;
	}

	protected RowHandle getRowHandle() {
		if (currentModel instanceof RowHandle) {
			return (RowHandle) currentModel;
		} else if (currentModel instanceof CellHandle) {
			return (RowHandle) ((CellHandle) currentModel).getContainer();
		}
		return null;
	}

	protected ListBandProxy getListBandProxy() {
		if (currentModel instanceof ListBandProxy) {
			return (ListBandProxy) currentModel;
		}
		return null;
	}

	/**
	 * Returns the current position of the selected part
	 * 
	 * @return the current position of the selected part
	 */
	protected int getCurrentPosition() {
		if (currentModel != null && isGroup()) {
			if (getRowHandle() != null) {
				DesignElementHandle group = getRowHandle().getContainer();
				TableHandle table = (TableHandle) group.getContainer();
				return DEUtil.findInsertPosition(table.getGroups().getElementHandle(), group,
						table.getGroups().getSlotID());
			} else if (getListBandProxy() != null) {
				DesignElementHandle group = getListBandProxy().getElemtHandle();
				ListHandle list = (ListHandle) group.getContainer();
				return DEUtil.findInsertPosition(list.getGroups().getElementHandle(), group,
						list.getGroups().getSlotID());
			}
		}
		return POSITION_INNERMOST;
	}

	/**
	 * Returns the insert position
	 * 
	 * @return the insert position
	 */
	abstract protected int getPosition();
}

class InsertAboveGroupAction extends InsertPositionGroupAction {

	/**
	 * @param editParts
	 */
	public InsertAboveGroupAction(List editParts, String text) {
		super(editParts, text);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions
	 * .InsertPositionGroupAction#getPosition()
	 */
	protected int getPosition() {
		if (isGroup()) {
			if (isNotReverse()) {
				return getCurrentPosition();
			}
			return getCurrentPosition() + 1;
		}
		if (isNotReverse()) {
			return POSITION_TOP_LEVEL;
		}
		return POSITION_INNERMOST;
	}

}

class InsertBelowGroupAction extends InsertPositionGroupAction {

	/**
	 * @param editParts
	 */
	public InsertBelowGroupAction(List editParts, String text) {
		super(editParts, text);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions
	 * .InsertPositionGroupAction#getPosition()
	 */
	protected int getPosition() {
		if (isGroup()) {
			if (isNotReverse()) {
				return getCurrentPosition() + 1;
			}
			return getCurrentPosition();
		}
		if (isNotReverse()) {
			return POSITION_INNERMOST;
		}
		return POSITION_TOP_LEVEL;
	}
}

/**
 * Insert table or list group in the position
 */

class InsertIntoGroupAction extends InsertPositionGroupAction {

	/**
	 * @param editParts
	 */
	public InsertIntoGroupAction(List editParts, String text) {
		super(editParts, text);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions
	 * .InsertPositionGroupAction#isEnabled()
	 */
	public boolean isEnabled() {
		return super.isEnabled() && isGroup();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions
	 * .InsertPositionGroupAction#getPosition()
	 */
	protected int getPosition() {
		return POSITION_INNERMOST;
	}
}
