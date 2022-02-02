/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.birt.chart.model.impl.ChartModelHelper;
import org.eclipse.birt.chart.reportitem.api.ChartReportItemHelper;
import org.eclipse.birt.chart.ui.swt.ColumnBindingInfo;
import org.eclipse.birt.chart.ui.swt.DefaultExpressionValidator;
import org.eclipse.birt.chart.ui.swt.interfaces.IAssistField;
import org.eclipse.birt.chart.ui.swt.interfaces.IExpressionButton;
import org.eclipse.birt.chart.ui.swt.interfaces.IExpressionValidator;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.ui.util.ChartUIUtil.ComboProxy;
import org.eclipse.birt.chart.util.ChartExpressionUtil.ExpressionCodec;
import org.eclipse.birt.report.designer.internal.ui.util.ExpressionButtonUtil.ExpressionHelper;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.ui.dialogs.IExpressionProvider;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * ChartExpressionButtonUtil
 */

public class ChartExpressionButtonUtil {

	public static interface IExpressionDescriptor {
		void setExpressionType(String type);

		String getExpressionType();

		String getDisplayText();

		String getExpression();

		String getTooltip();

		boolean isColumnBinding();

		String getBindingName();
	}

	public static class ExpressionDescriptor implements IExpressionDescriptor {

		protected final ExpressionCodec exprCodec = ChartModelHelper.instance().createExpressionCodec();

		protected ExpressionDescriptor() {

		}

		protected ExpressionDescriptor(String encodedExpr) {
			exprCodec.decode(encodedExpr);
		}

		protected ExpressionDescriptor(String sExprText, String type) {
			exprCodec.setExpression(sExprText);
			exprCodec.setType(type);
		}

		public static IExpressionDescriptor getInstance(Object expr, boolean isCube) {
			if (expr instanceof String[]) {
				return new ExpressionDescriptor(((String[]) expr)[0]);
			} else if (expr instanceof String) {
				if (isCube) {
					return new BindingExpressionDescriptor((String) expr, (String) expr, isCube);
				}
				return new ExpressionDescriptor((String) expr);
			} else if (expr instanceof ColumnBindingInfo) {
				return new BindingExpressionDescriptor(((ColumnBindingInfo) expr).getName(),
						((ColumnBindingInfo) expr).getTooltip(), isCube);
			}

			return null;
		}

		public static IExpressionDescriptor getInstance(String exprText, String exprType) {
			return new ExpressionDescriptor(exprText, exprType);
		}

		public String getExpression() {
			return exprCodec.encode();
		}

		public void setExpressionType(String type) {
			// not implemented
		}

		public String getDisplayText() {
			return exprCodec.getExpression();
		}

		public String getExpressionType() {
			return exprCodec.getType();
		}

		public String getTooltip() {
			return exprCodec.getExpression();
		}

		public boolean isColumnBinding() {
			return false;
		}

		public String getBindingName() {
			return exprCodec.getBindingName();
		}
	}

	private static class BindingExpressionDescriptor extends ExpressionDescriptor {

		private final String bindingName;
		private final String tooltip;
		private final boolean isCube;

		public BindingExpressionDescriptor(String bindingName, String tooltip, boolean isCube) {
			this.bindingName = bindingName;
			this.tooltip = tooltip;
			this.isCube = isCube;
			exprCodec.setBindingName(bindingName, isCube, UIUtil.getDefaultScriptType());
		}

		public void setExpressionType(String type) {
			if (!exprCodec.getType().equals(type)) {
				exprCodec.setBindingName(bindingName, isCube, type);
			}
		}

		@Override
		public String getTooltip() {
			return tooltip;
		}

		@Override
		public boolean isColumnBinding() {
			return true;
		}

		@Override
		public String getBindingName() {
			return bindingName;
		}

	}

	public static IExpressionButton createExpressionButton(Composite parent, Control control, ExtendedItemHandle eih,
			IExpressionProvider ep) {
		boolean isCube = ChartReportItemHelper.instance().getBindingCubeHandle(eih) != null;

		boolean isCombo = control instanceof Combo || control instanceof CCombo;

		ChartExpressionHelper eHelper = isCombo ? new ChartExpressionComboHelper(isCube)
				: new ChartExpressionHelper(isCube);

		return new ChartExpressionButton(parent, control, eih, ep, eHelper);
	}

	/**
	 * ChartExpressionHelper
	 */
	public static class ChartExpressionHelper extends ExpressionHelper {

		protected final boolean isCube;
		protected IAssistField assistField;
		protected Set<IExpressionDescriptor> predefinedQuerys = new LinkedHashSet<IExpressionDescriptor>();

		private IExpressionValidator exprValidator = new DefaultExpressionValidator();

		public ChartExpressionHelper(boolean isCube) {
			this.isCube = isCube;
		}

		public boolean isCube() {
			return isCube;
		}

		public void setAssitField(IAssistField assistField) {
			this.assistField = assistField;
			updateAssistFieldContents();
		}

		public void setPredefinedQuerys(Collection<IExpressionDescriptor> exprs) {
			predefinedQuerys.clear();
			predefinedQuerys.addAll(exprs);
			setPredefinedQueryType(getExpressionType());
			updateAssistFieldContents();
		}

		private void updateAssistFieldContents() {
			if (assistField != null) {
				List<String> list = new ArrayList<String>();
				for (IExpressionDescriptor desc : predefinedQuerys) {
					list.add(desc.getDisplayText());
				}
				assistField.setContent(list.toArray(new String[list.size()]));
			}
		}

		private void setPredefinedQueryType(String type) {
			for (IExpressionDescriptor desc : predefinedQuerys) {
				desc.setExpressionType(type);
			}
		}

		@Override
		public void notifyExpressionChangeEvent(String oldExpression, String newExpression) {
			if (listener != null) {
				Event event = new Event();
				event.widget = button.getControl();
				event.data = new String[] { oldExpression, newExpression };
				event.detail = SWT.Modify;
				listener.handleEvent(event);
			}
		}

		@Override
		public String getExpression() {
			if (control.isDisposed()) {
				return ""; //$NON-NLS-1$
			}
			String expr = ChartUIUtil.getText(control).trim();
			if (exprValidator.isReservedString(expr)) {
				return ""; //$NON-NLS-1$
			}
			return expr;
		}

		@Override
		public void setExpression(String expression) {
			if (control.isDisposed()) {
				return;
			}
			ChartUIUtil.setText(control, DEUtil.resolveNull(expression));
		}

		public void initialize() {
			// do nothing
		}

		@Override
		public String getExpressionType() {
			String type = super.getExpressionType();
			return type != null ? type : UIUtil.getDefaultScriptType();
		}

		@Override
		public void setExpressionType(String exprType) {
			super.setExpressionType(exprType);
			setPredefinedQueryType(getExpressionType());
			updateAssistFieldContents();
		}

		/**
		 * Sets expression validator.
		 * 
		 * @param exprValidator
		 */
		public void setExpressionValidator(IExpressionValidator exprValidator) {
			this.exprValidator = exprValidator;
		}
	}

	public static class ChartExpressionComboHelper extends ChartExpressionHelper {

		protected final boolean bCacheUserInput = true;

		public ChartExpressionComboHelper(boolean isCube) {
			super(isCube);
		}

		@Override
		public void setExpressionType(String exprType) {
			if (getExpressionType().equals(exprType)) {
				return;
			}
			super.setExpressionType(exprType);

			ComboProxy cp = ComboProxy.getInstance(control);
			if (cp != null) {
				String[] itemsOld = cp.getItems();
				String userExpr = cp.getText();
				cp.removeAll();

				for (String oldItem : itemsOld) {
					IExpressionDescriptor desc = (IExpressionDescriptor) cp.getData(oldItem);
					if (desc != null) {
						addComboItem(cp, desc);
					} else {
						// do not contain predefined query but only string
						cp.add(oldItem);
					}
				}

				cp.setText(userExpr);
			}

		}

		private void addComboItem(ComboProxy cp, IExpressionDescriptor desc) {
			String key = desc.getDisplayText();
			cp.add(key);
			cp.setData(key, desc);
		}

		@Override
		public void setExpression(String expression) {
			if (bCacheUserInput && expression != null && expression.length() > 0) {
				ComboProxy cp = ComboProxy.getInstance(control);

				// if ( cp != null && !cp.contains( expression ) )
				// {
				// IExpressionDescriptor desc =
				// ExpressionDescriptor.getInstance( expression,
				// getExpressionType( ) );
				// addComboItem( cp, desc );
				// }

				if (cp.getData(expression) instanceof IExpressionDescriptor) {
					IExpressionDescriptor desc = (IExpressionDescriptor) cp.getData(expression);
					control.setToolTipText(desc.getTooltip());
				}
			}
			super.setExpression(expression);
		}

		@Override
		public void initialize() {
			control.addListener(SWT.Selection, new Listener() {

				public void handleEvent(Event event) {
					ComboProxy cp = ComboProxy.getInstance(control);
					if (cp != null) {
						if (cp.getSelectionIndex() >= 0) {
							IExpressionDescriptor desc = (IExpressionDescriptor) cp.getData(cp.getText());
							if (desc != null) {
								setExpressionType(desc.getExpressionType());
							}
							button.refresh();
						}
					}
				}
			});

		}

		@Override
		public void setPredefinedQuerys(Collection<IExpressionDescriptor> exprs) {
			super.setPredefinedQuerys(exprs);

			ComboProxy cp = ComboProxy.getInstance(control);
			if (cp != null) {
				cp.removeAll();
				for (IExpressionDescriptor desc : predefinedQuerys) {
					addComboItem(cp, desc);
				}
			}
		}

	}
}
