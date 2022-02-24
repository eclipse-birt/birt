/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
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

package org.eclipse.birt.chart.reportitem.ui;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.eclipse.birt.chart.model.impl.ChartModelHelper;
import org.eclipse.birt.chart.reportitem.ui.ChartExpressionButtonUtil.ChartExpressionHelper;
import org.eclipse.birt.chart.reportitem.ui.ChartExpressionButtonUtil.ExpressionDescriptor;
import org.eclipse.birt.chart.reportitem.ui.ChartExpressionButtonUtil.IExpressionDescriptor;
import org.eclipse.birt.chart.ui.swt.interfaces.IAssistField;
import org.eclipse.birt.chart.ui.swt.interfaces.IExpressionButton;
import org.eclipse.birt.chart.ui.util.ChartUIUtil.EAttributeAccessor;
import org.eclipse.birt.chart.util.ChartExpressionUtil.ExpressionCodec;
import org.eclipse.birt.report.designer.internal.ui.dialogs.expression.ExpressionButton;
import org.eclipse.birt.report.designer.internal.ui.util.ExpressionButtonUtil;
import org.eclipse.birt.report.designer.ui.dialogs.IExpressionProvider;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * ChartExpressionButton
 */

public class ChartExpressionButton implements IExpressionButton {

	// lastExpr is used to cache the expression being set by the last
	// invoking of setExpression ore setBindingName.
	private final ExpressionCodec lastExpr = ChartModelHelper.instance().createExpressionCodec();

	protected ExpressionCodec exprCodec = null;

	protected final ExpressionButton eb;
	protected final ChartExpressionHelper eHelper;
	protected final Vector<Listener> listeners = new Vector<Listener>();
	protected EAttributeAccessor<String> accessor;

	public ChartExpressionButton(Composite parent, Control control, ExtendedItemHandle eih, IExpressionProvider ep,
			ChartExpressionHelper eHelper) {
		this.eHelper = eHelper;
		exprCodec = ChartModelHelper.instance().createExpressionCodec();
		eb = ExpressionButtonUtil.createExpressionButton(parent, control, ep, eih, new Listener() {

			public void handleEvent(Event event) {
				onChange();
			}
		}, false, SWT.PUSH, eHelper);
		ExpressionButtonUtil.initExpressionButtonControl(control, (Expression) null);
		eHelper.initialize();

		ControlListener controlListener = new ControlListener();
		control.addListener(SWT.FocusOut, controlListener);
		control.addListener(SWT.Selection, controlListener);
		control.addListener(SWT.KeyDown, controlListener);
	}

	private class ControlListener implements Listener {

		public void handleEvent(Event event) {
			switch (event.type) {
			case SWT.KeyDown:
				if (event.keyCode == SWT.CR || event.keyCode == SWT.KEYPAD_CR) {
					onChange();
				}
				break;
			case SWT.FocusOut:
			case SWT.Selection:
				onChange();
				break;
			}
		}
	}

	private void save() {
		if (accessor != null) {
			String expr = eHelper.getExpression().length() == 0 ? null : getExpression();
			accessor.save(expr);
		}
	}

	private void load() {
		if (accessor != null) {
			setExpression(accessor.load());
		}
	}

	protected boolean hasChanged() {
		String oldExpr = lastExpr.getExpression();
		String newExpr = eHelper.getExpression();
		String oldType = lastExpr.getType();
		String newType = eHelper.getExpressionType();

		if (oldExpr == null) {
			return newType != null || !oldType.equals(newType);
		}

		return !oldExpr.equals(newExpr) || !oldType.equals(newType);
	}

	private void onChange() {
		if (hasChanged()) {
			notifyChangeEvent();
		}
	}

	protected void notifyChangeEvent() {
		String newExpr = eHelper.getExpression();
		String newType = eHelper.getExpressionType();
		Event event = new Event();
		event.widget = eb.getControl();
		event.detail = SWT.Modify;
		String[] data = new String[4];
		data[0] = lastExpr.getExpression();
		data[1] = newExpr;
		data[2] = lastExpr.getType();
		data[3] = newType;
		event.data = data;

		for (Listener listener : listeners) {
			listener.handleEvent(event);
		}

		save();
	}

	public void addListener(Listener listener) {
		if (listener != null) {
			listeners.add(listener);
		}
	}

	public String getExpression() {
		exprCodec.setExpression(eHelper.getExpression());
		exprCodec.setType(eHelper.getExpressionType());
		return exprCodec.encode();
	}

	public void setExpression(String expr) {
		setExpression(expr, false);
	}

	public String getDisplayExpression() {
		return eHelper.getExpression();
	}

	public boolean isEnabled() {
		return eb.isEnabled();
	}

	public void setEnabled(boolean bEnabled) {
		eb.setEnabled(bEnabled);
	}

	public void setAccessor(EAttributeAccessor<String> accessor) {
		this.accessor = accessor;
		load();
	}

	public String getExpressionType() {
		return eHelper.getExpressionType();
	}

	public boolean isCube() {
		return eHelper.isCube();
	}

	public void setBindingName(String bindingName, boolean bNotifyEvents) {
		if (bindingName != null && bindingName.length() > 0) {
			exprCodec.setBindingName(bindingName, isCube(), eHelper.getExpressionType());
			eHelper.setExpression(exprCodec.getExpression());
		} else {
			eHelper.setExpression(bindingName);
		}

		eb.refresh();

		if (bNotifyEvents) {
			notifyChangeEvent();
		}

		lastExpr.setExpression(eHelper.getExpression());
		lastExpr.setType(eHelper.getExpressionType());
	}

	public void setExpression(String expr, boolean bNotifyEvents) {
		if (expr != null && expr.length() > 0) {
			exprCodec.decode(expr);
			eHelper.setExpressionType(exprCodec.getType());
			eHelper.setExpression(exprCodec.getExpression());
		} else {
			eHelper.setExpression(expr);
		}

		eb.refresh();

		if (bNotifyEvents) {
			notifyChangeEvent();
		}

		lastExpr.setExpression(eHelper.getExpression());
		lastExpr.setType(eHelper.getExpressionType());
	}

	public void setAssitField(IAssistField assistField) {
		eHelper.setAssitField(assistField);
	}

	public void setPredefinedQuery(Object[] predefinedQuery) {
		if (predefinedQuery == null) {
			return;
		}
		boolean isCube = isCube();

		Set<IExpressionDescriptor> set = new LinkedHashSet<IExpressionDescriptor>();
		for (Object obj : predefinedQuery) {
			set.add(ExpressionDescriptor.getInstance(obj, isCube));
		}

		eHelper.setPredefinedQuerys(filterDuplicate(set));
	}

	private Collection<IExpressionDescriptor> filterDuplicate(Collection<IExpressionDescriptor> exprDescs) {
		Set<IExpressionDescriptor> set = new LinkedHashSet<IExpressionDescriptor>();
		Set<String> bindingNames = new LinkedHashSet<String>();
		List<IExpressionDescriptor> otherDescs = new LinkedList<IExpressionDescriptor>();

		for (IExpressionDescriptor desc : exprDescs) {
			if (desc.isColumnBinding()) {
				set.add(desc);
				bindingNames.add(desc.getBindingName());
			} else {
				otherDescs.add(desc);
			}
		}

		for (IExpressionDescriptor desc : otherDescs) {
			String bindingName = desc.getBindingName();

			if (!bindingNames.contains(bindingName)) {
				set.add(desc);
			}
		}

		return set;
	}

	/**
	 * Returns the handle of expression helper.
	 * 
	 * @return instance of ChartExpressionHelper
	 */
	public ChartExpressionHelper getExpressionHelper() {
		return this.eHelper;
	}
}
