/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.chart.ui.swt;

import org.eclipse.birt.chart.model.impl.ChartModelHelper;
import org.eclipse.birt.chart.ui.swt.interfaces.IExpressionButton;
import org.eclipse.birt.chart.util.ChartExpressionUtil.ExpressionCodec;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

/**
 *
 * This class implements supporting for Drop of selected table column header
 */
public class DataTextDropListener extends DropTargetAdapter {

	private final Control txtDataDefn;
	private final IExpressionButton btnBuilder;

	public DataTextDropListener(Control txtDataDefn, IExpressionButton btnBuilder) {
		super();
		this.txtDataDefn = txtDataDefn;
		this.btnBuilder = btnBuilder;
		assert txtDataDefn instanceof Text || txtDataDefn instanceof Combo || txtDataDefn instanceof CCombo;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.swt.dnd.DropTargetListener#dragEnter(org.eclipse.swt.dnd.
	 * DropTargetEvent)
	 */
	@Override
	public void dragEnter(DropTargetEvent event) {
		// indicate a copy
		event.detail = DND.DROP_COPY;

		// check if valid expression
		// Get Data in a Java format
		// Since in Mac os x / Linux, the event.currentDataType.data is still
		// null, so here ignores validation under Mac os x /Linux.
		if (Platform.OS_WIN32.equals(Platform.getOS())) {
			Object object = null;
			Transfer[] transferAgents = ((DropTarget) event.widget).getTransfer();
			for (int i = 0; i < transferAgents.length; i++) {
				Transfer transfer = transferAgents[i];
				if (transfer instanceof SimpleTextTransfer) {
					object = SimpleTextTransfer.getInstance().nativeToJava(event.currentDataType);
					break;
				}
			}
			if (object != null) {
				// object is a binding name. expression is needed for
				// validation.
				ExpressionCodec expCodec = ChartModelHelper.instance().createExpressionCodec();
				expCodec.setType(btnBuilder.getExpressionType());
				expCodec.setBindingName(object.toString(), btnBuilder.isCube());
				if (!DataDefinitionTextManager.getInstance().isValidExpression(txtDataDefn, expCodec.getExpression())) {
					event.detail = DND.DROP_NONE;
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.swt.dnd.DropTargetListener#dragOperationChanged(org.eclipse.swt.
	 * dnd.DropTargetEvent)
	 */
	@Override
	public void dragOperationChanged(DropTargetEvent event) {
		// always indicate a copy
		event.detail = DND.DROP_COPY;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.swt.dnd.DropTargetListener#dragOver(org.eclipse.swt.dnd.
	 * DropTargetEvent)
	 */
	@Override
	public void dragOver(DropTargetEvent event) {
		// TODO Auto-generated method stub
		event.feedback = DND.FEEDBACK_SELECT | DND.FEEDBACK_SCROLL;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.swt.dnd.DropTargetListener#drop(org.eclipse.swt.dnd.
	 * DropTargetEvent)
	 */
	@Override
	public void drop(DropTargetEvent event) {
		String bindingName = (String) event.data;

		// Since in Mac os X/ Linux, the dragEnter method did not validate
		// expression, so
		// here validate the expression.
		if (!Platform.OS_WIN32.equals(Platform.getOS())) {
			ExpressionCodec expCodec = ChartModelHelper.instance().createExpressionCodec();
			expCodec.setBindingName(bindingName, btnBuilder.isCube());
			if (!DataDefinitionTextManager.getInstance().isValidExpression(txtDataDefn, expCodec.getExpression())) {
				return;
			}
		}

		btnBuilder.setBindingName(bindingName, true);
		// String expression = (String) event.data;
		// // If it's last element, remove color binding
		// if ( !expression.equals( ChartUIUtil.getText( txtDataDefn ) )
		// && DataDefinitionTextManager.getInstance( )
		// .getNumberOfSameDataDefinition( ChartUIUtil.getText( txtDataDefn ) )
		// == 1 )
		// {
		// ColorPalette.getInstance( ).retrieveColor( ChartUIUtil.getText(
		// txtDataDefn ) );
		// }
		//
		// // Check if dragged expression can be put on target series. For
		// sharing
		// // binding case, Y optional only allow grouped binding.
		// if ( !DataDefinitionTextManager.getInstance( )
		// .isValidExpression( txtDataDefn, expression ) )
		// {
		// return;
		// }
		//
		// btnBuilder.setExpression( expression );
		//
		// DataDefinitionTextManager.getInstance( ).updateQuery( txtDataDefn );
		//
		// // Refresh all data definition text
		// DataDefinitionTextManager.getInstance( ).refreshAll( );
	}
}
