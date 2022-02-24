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

package org.eclipse.birt.report.designer.internal.ui.views.actions;

import java.util.logging.Level;

import org.eclipse.birt.report.designer.core.model.schematic.HandleAdapterFactory;
import org.eclipse.birt.report.designer.internal.ui.command.CommandUtils;
import org.eclipse.birt.report.designer.internal.ui.extension.ExtendedDataModelUIAdapterHelper;
import org.eclipse.birt.report.designer.internal.ui.extension.IExtendedDataModelUIAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.DNDUtil;
import org.eclipse.birt.report.model.api.AbstractScalarParameterHandle;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.EmbeddedImageHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ListGroupHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.ParameterGroupHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.ReportItemThemeHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.TemplateElementHandle;
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.birt.report.model.api.VariableElementHandle;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.MeasureGroupHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * Copy action
 */
public class CopyAction extends AbstractViewAction {

	private static final String DEFAULT_TEXT = Messages.getString("CopyAction.text"); //$NON-NLS-1$

	/**
	 * Create a new copy action with given selection and default text
	 * 
	 * @param selectedObject the selected object,which cannot be null
	 * 
	 */
	public CopyAction(Object selectedObject) {
		this(selectedObject, DEFAULT_TEXT);
	}

	/**
	 * Create a new copy action with given selection and text
	 * 
	 * @param selectedObject the selected object,which cannot be null
	 * @param text           the text of the action
	 */
	public CopyAction(Object selectedObject, String text) {
		super(selectedObject, text);
		ISharedImages shareImages = PlatformUI.getWorkbench().getSharedImages();
		setImageDescriptor(shareImages.getImageDescriptor(ISharedImages.IMG_TOOL_COPY));
		setDisabledImageDescriptor(shareImages.getImageDescriptor(ISharedImages.IMG_TOOL_COPY_DISABLED));
		setAccelerator(SWT.CTRL | 'C');
	}

	/**
	 * Runs this action. Copies the content. Each action implementation must define
	 * the steps needed to carry out this action. The default implementation of this
	 * method in <code>Action</code> does nothing.
	 */
	public void run() {
		if (Policy.TRACING_ACTIONS) {
			System.out.println("Copy action >> Copy " + getSelection()); //$NON-NLS-1$
		}
//		Object cloneElements = DNDUtil.cloneSource( getSelection( ) );
//		Clipboard.getDefault( ).setContents( cloneElements );
		try {
			CommandUtils.executeCommand("org.eclipse.birt.report.designer.ui.command.copyAction"); //$NON-NLS-1$
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#isEnabled()
	 */
	public boolean isEnabled() {
		if (canCopy(getSelection()))
			return super.isEnabled();
		return false;
	}

	// this function is from DNDUtil.handleValidateDragInOutline( Object
	// selection )
	public boolean canCopy(Object selection) {
		if (selection instanceof StructuredSelection) {
			return canCopy(((StructuredSelection) selection).toArray());
		}
		if (selection instanceof Object[]) {
			Object[] array = (Object[]) selection;
			if (array.length == 0) {
				return false;
			}

			if (array[0] instanceof ColumnHandle && ((ColumnHandle) array[0]).getRoot() != null) {
				boolean bool = false;
				int columnNumber = HandleAdapterFactory.getInstance().getColumnHandleAdapter(array[0])
						.getColumnNumber();
				Object parent = ((ColumnHandle) array[0]).getContainer();
				if (parent instanceof TableHandle) {
					bool = ((TableHandle) parent).canCopyColumn(columnNumber);
				} else if (parent instanceof GridHandle) {
					bool = ((GridHandle) parent).canCopyColumn(columnNumber);
				}
				if (bool && array.length == 1) {
					return true;
				}
				if (bool && array[1] instanceof CellHandle) {
					return true;
				}
				return false;
			}

			for (int i = 0; i < array.length; i++) {
				if (DNDUtil.checkContainerExists(array[i], array))
					continue;
				if (!canCopy(array[i]))
					return false;
			}
			return true;
		}
//		if ( selection instanceof ReportElementModel )
//		{
//			return canCopy( ( (ReportElementModel) selection ).getSlotHandle( ) );
//		}
		if (selection instanceof SlotHandle) {
			SlotHandle slot = (SlotHandle) selection;
			DesignElementHandle handle = slot.getElementHandle();
			return slot.getContents().size() > 0 && (handle instanceof ListHandle || handle instanceof ListGroupHandle);
		}
		if (selection instanceof ColumnHandle && ((ColumnHandle) selection).getRoot() != null) {
			int columnNumber = HandleAdapterFactory.getInstance().getColumnHandleAdapter(selection).getColumnNumber();
			Object parent = ((ColumnHandle) selection).getContainer();
			if (parent instanceof TableHandle) {
				return ((TableHandle) parent).canCopyColumn(columnNumber);
			} else if (parent instanceof GridHandle) {
				return ((GridHandle) parent).canCopyColumn(columnNumber);
			}
		}
		if (selection instanceof DesignElementHandle) {
			IExtendedDataModelUIAdapter adapter = ExtendedDataModelUIAdapterHelper.getInstance().getAdapter();
			if (adapter != null && adapter.resolveExtendedData((DesignElementHandle) selection) != null) {
				return true;
			}
		}
		return selection instanceof ReportItemHandle || selection instanceof DataSetHandle
				|| selection instanceof DataSourceHandle || selection instanceof AbstractScalarParameterHandle
				|| selection instanceof ParameterGroupHandle || selection instanceof GroupHandle
				|| selection instanceof StyleHandle || selection instanceof ThemeHandle
				|| selection instanceof ReportItemThemeHandle || selection instanceof EmbeddedImageHandle
				|| selection instanceof TemplateElementHandle || selection instanceof CubeHandle
				|| selection instanceof LevelHandle || selection instanceof MeasureHandle
				|| selection instanceof DimensionHandle || selection instanceof MeasureGroupHandle
				|| selection instanceof VariableElementHandle;
	}
}
