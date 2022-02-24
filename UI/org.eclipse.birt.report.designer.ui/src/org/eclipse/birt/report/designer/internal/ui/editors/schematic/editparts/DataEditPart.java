/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts;

import org.eclipse.birt.report.designer.internal.ui.dialogs.DataColumnBindingDialog;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.LabelFigure;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.bidi.BidiUIUtils;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.ColumnHint;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.jface.dialogs.Dialog;

/**
 * Data edit part
 *
 */
public class DataEditPart extends LabelEditPart {

	private static final String FIGURE_DEFAULT_TEXT = Messages.getString("DataEditPart.Figure.Dafault"); //$NON-NLS-1$
	protected static final String AGGREGATE_ON = Messages.getString("DataEditPart.text.AggregateOn"); //$NON-NLS-1$
	protected static final String PREFIX = "\u2211"; //$NON-NLS-1$

	/**
	 * Constructor
	 *
	 * @param model
	 */
	public DataEditPart(Object model) {
		super(model);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	@Override
	protected IFigure createFigure() {
		LabelFigure label = new LabelFigure();
		label.setLayoutManager(new StackLayout());

		return label;
	}

	/**
	 * Popup the builder for Data element
	 */
	@Override
	public void performDirectEdit() {
		DataItemHandle handle = (DataItemHandle) getModel();
		/*
		 * Object dialogAdapter = ElementAdapterManager.getAdapter( handle,
		 * AbstractDataBindingDialog.class ); if ( dialogAdapter != null ) {
		 * AbstractDataBindingDialog dialog = (AbstractDataBindingDialog) dialogAdapter;
		 * dialog.setTitle( "Edit data item binding" ); dialog.setInput( handle );
		 * dialog.setBindingHolder( getBindingHolder(handle) ); dialog.setBindingHandle(
		 * DEUtil.getInputBinding( handle, handle.getResultSetColumn( ) ) );
		 * dialog.setEdit( true ); handle.getModuleHandle( ).getCommandStack(
		 * ).startTrans( null ); if ( dialog.open( ) == Dialog.OK ) { try { if (
		 * dialog.getBindingColumn( ) != null ) { handle.setResultSetColumn(
		 * dialog.getBindingColumn( ) .getName( ) ); } handle.getModuleHandle(
		 * ).getCommandStack( ).commit( ); } catch ( SemanticException e ) {
		 * ExceptionHandler.handle( e ); handle.getModuleHandle( ).getCommandStack(
		 * ).rollbackAll( ); } } else { handle.getModuleHandle( ).getCommandStack(
		 * ).rollbackAll( ); } }
		 */
		handle.getModuleHandle().getCommandStack().startTrans(Messages.getString("DataEditPart.stackMsg.edit")); //$NON-NLS-1$
		DataColumnBindingDialog dialog = new DataColumnBindingDialog(handle.getResultSetColumn() == null);

		dialog.setEditModal(true);

		if (handle.getResultSetColumn() != null) {
			// DataItemBindingAggregateOnProvider provider;
			// ComputedColumnHandle bindingColumn = DEUtil.getInputBinding(
			// handle,
			// handle.getResultSetColumn( ) );
			// if ( bindingColumn == null )
			// {
			// provider = new NODataItemBindingAggregateOnProvider( );
			// dialog.setProvider( provider );
			// }
			// else
			// {
			// Object obj = bindingColumn.getElementHandle( );
			// EditPart part = (EditPart) getViewer( ).getEditPartRegistry( )
			// .get( obj );
			//
			// if ( part == null )
			// {
			// provider = new NODataItemBindingAggregateOnProvider( );
			// }
			// else
			// {
			// provider = (DataItemBindingAggregateOnProvider) part.getAdapter(
			// DataItemBindingAggregateOnProvider.class );
			// }
			// if ( provider == null )
			// {
			// provider = new NODataItemBindingAggregateOnProvider( );
			// }
			// provider.setDataItemHandle( handle );
			//
			// // dialog.setProvider( provider );
			// }
			dialog.setInput(handle, DEUtil.getInputBinding(handle, handle.getResultSetColumn()));
		} else {
			dialog.setInput(handle);
		}
		// ColumnBindingDialog dialog = new ColumnBindingDialog( true );
		// dialog.setInput( handle );
		// dialog.setGroupList( DEUtil.getGroups( handle ) );
		if (dialog.open() == Dialog.OK) {

			try {
				if (dialog.getBindingColumn() != null) {
					handle.setResultSetColumn(dialog.getBindingColumn().getName());
				}
				handle.getModuleHandle().getCommandStack().commit();
			} catch (SemanticException e) {
				ExceptionHandler.handle(e);
				handle.getModuleHandle().getCommandStack().rollbackAll();
			}
			refreshVisuals();
		} else {
			handle.getModuleHandle().getCommandStack().rollbackAll();
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts
	 * .ReportElementEditPart#refreshFigure()
	 */
	@Override
	public void refreshFigure() {
		super.refreshFigure();

		((LabelFigure) getFigure()).setToolTipText(((DataItemHandle) getModel()).getResultSetColumn());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts
	 * .LabelEditPart#getText()
	 */
	@Override
	protected String getText() {
		String text = ((DataItemHandle) getModel()).getResultSetColumn();
		if (text == null || text.length() == 0) {
			text = FIGURE_DEFAULT_TEXT;
		} else {
			String displayName = getDisplayName();
			if (displayName != null && displayName.length() > 0) {
				text = displayName;
			}
			if (text.length() > TRUNCATE_LENGTH) {
				text = text.substring(0, TRUNCATE_LENGTH - 2) + ELLIPSIS;
			}
			// bidi_hcg start
			// Add control characters to avoid the bracket mirroring.
			// Note: Do not use LRMs since org.eclipse.draw2d.text or
			// org.eclipse.swt.graphics.TextLayout (or their native peers) don't
			// handle them correctly.
			if (BidiUIUtils.INSTANCE.isDirectionRTL(getModel())) {
				text = BidiUIUtils.LRE + "[" + BidiUIUtils.RLE + text + //$NON-NLS-1$
						BidiUIUtils.PDF + "]" + BidiUIUtils.PDF; //$NON-NLS-1$
			} else {
				// bidi_hcg end
				text = "[" + text + "]"; //$NON-NLS-1$//$NON-NLS-2$
			}
		}
		if (hasBindingFunction()) {
			((LabelFigure) getFigure()).setSpecialPREFIX(PREFIX);
			text = PREFIX + text;
		}
		return text;
	}

	protected boolean hasBindingFunction() {
		DataItemHandle handle = (DataItemHandle) getModel();
		String name = handle.getResultSetColumn();
		if (name == null) {
			return false;
		}
		ComputedColumnHandle bindingColumn = DEUtil.getInputBinding(handle, name);
		if (bindingColumn == null) {
			return false;
		}
		if (bindingColumn.getAggregateFunction() != null) {
			return true;
		}
		return false;
	}

	protected String getDisplayName() {
		DataItemHandle handle = (DataItemHandle) getModel();
		String name = handle.getResultSetColumn();
		if (name == null) {
			return null;
		}
		ComputedColumnHandle bindingColumn = DEUtil.getInputBinding(handle, name);
		if (bindingColumn == null) {
			return null;
		}
		String displayName = null;
		if (bindingColumn.getDisplayName() == null && bindingColumn.getDisplayNameID() != null) {
			displayName = bindingColumn.getExternalizedValue(ColumnHint.DISPLAY_NAME_ID_MEMBER,
					ColumnHint.DISPLAY_NAME_MEMBER);
		} else {
			displayName = bindingColumn.getDisplayName();
		}
		if (displayName == null) {
			displayName = bindingColumn.getColumnName();
		}
		return displayName;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts
	 * .LabelEditPart#hasText()
	 */
	@Override
	protected boolean hasText() {
		String text = ((DataItemHandle) getModel()).getResultSetColumn();

		return (text != null && text.length() > 0);
	}

}
