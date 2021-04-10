/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.data.DataElement;
import org.eclipse.birt.chart.model.data.DateTimeDataElement;
import org.eclipse.birt.chart.model.data.NumberDataElement;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.TextDataElement;
import org.eclipse.birt.chart.model.impl.ChartModelHelper;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartWizardContext;
import org.eclipse.birt.chart.ui.swt.interfaces.IExpressionButton;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.util.ChartExpressionUtil.ExpressionCodec;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Control;

/**
 * The manager for synchronizing data query, text and background color.
 */

public class DataDefinitionTextManager {

	private static DataDefinitionTextManager instance;
	private HashMap<Control, IQueryExpressionManager> textCollection = null;
	private final ExpressionCodec exprCodec = ChartModelHelper.instance().createExpressionCodec();
	private IChartWizardContext context;

	private DataDefinitionTextManager() {
		textCollection = new HashMap<Control, IQueryExpressionManager>(10);
	}

	public synchronized static DataDefinitionTextManager getInstance() {
		if (instance == null)
			instance = new DataDefinitionTextManager();
		return instance;
	}

	public void setContext(IChartWizardContext context) {
		this.context = context;
	}

	public void addDataDefinitionText(Control text, IQueryExpressionManager queryManager) {
		textCollection.put(text, queryManager);
		// update control color when switching.
		updateControlBackground(text, queryManager.getQuery().getDefinition());
		text.addDisposeListener(new DisposeListener() {

			public void widgetDisposed(DisposeEvent e) {
				if (e.widget instanceof Control) {
					removeDataDefinitionText((Control) e.widget);
				}

			}
		});
	}

	public void removeDataDefinitionText(Control text) {
		textCollection.remove(text);
	}

	public void removeAll() {
		textCollection.clear();
	}

	private Collection<String> getAllUsedBindingNames() {
		Set<String> set = new HashSet<String>(5);

		for (IQueryExpressionManager iqem : textCollection.values()) {
			IExpressionButton eb = iqem.getExpressionButton();
			if (eb != null) {
				String name = exprCodec.getBindingName(eb.getExpression());
				if (name != null) {
					set.add(name);
				}
			}
		}
		if (context != null) {
			set.addAll(((DefaultChartDataSheet) context.getDataSheet()).getAllValueDefinitions());
		}

		return set;
	}

	public void refreshAll() {
		// remove disposed control
		checkAll();

		Set<String> usedColorKeys = new HashSet<String>();
		ColorPalette colorPalette = ColorPalette.getInstance();

		// update all text
		for (Map.Entry<Control, IQueryExpressionManager> entry : textCollection.entrySet()) {
			Control text = entry.getKey();
			IQueryExpressionManager iqem = entry.getValue();
			String expr = iqem.getQuery().getDefinition();
			iqem.updateText(expr);

			String name = exprCodec.getBindingName(iqem.getExpressionButton().getExpression());
			if (name != null) {
				colorPalette.putColor(name);
				usedColorKeys.add(name);
				text.setBackground(colorPalette.getColor(name));
			} else {
				text.setBackground(null);
			}
		}

		// re-organize colors
		ColorPalette.getInstance().updateKeys(getAllUsedBindingNames());
	}

	/**
	 * Checks all texts and removes disposed controls
	 * 
	 */
	private void checkAll() {
		List<Control> listToRemove = new ArrayList<Control>(textCollection.size());
		for (Iterator<Control> iterator = textCollection.keySet().iterator(); iterator.hasNext();) {
			Control text = iterator.next();
			if (text.isDisposed()) {
				listToRemove.add(text);
			}
		}
		for (int i = 0; i < listToRemove.size(); i++) {
			textCollection.remove(listToRemove.get(i));
		}
	}

	public int getNumberOfSameDataDefinition(String expression) {
		checkAll();
		int number = 0;
		for (Iterator<Control> iterator = textCollection.keySet().iterator(); iterator.hasNext();) {
			Control text = iterator.next();
			if (ChartUIUtil.getText(text).equals(expression)) {
				number++;
			}
		}
		return number;
	}

	public Control findText(Query query) {
		Iterator<Map.Entry<Control, IQueryExpressionManager>> iterator = textCollection.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<Control, IQueryExpressionManager> entry = iterator.next();
			if (entry.getValue().getQuery() == query) {
				return entry.getKey();
			}
		}
		return null;
	}

	/**
	 * Update query data by specified expression, if current is sharing-binding
	 * case, the expression will be converted and set to query, else directly set
	 * query with the expression.
	 * 
	 * @param query
	 * @param expression
	 * @since 2.3
	 */
	public void updateQuery(Query query, String expression) {
		Control control = findText(query);
		if (control != null) {
			IQueryExpressionManager queryManager = textCollection.get(control);
			queryManager.updateQuery(expression);
		}
	}

	public void updateQuery(Control control) {
		if (textCollection.containsKey(control)) {
			IQueryExpressionManager queryManager = textCollection.get(control);
			queryManager.updateQuery(ChartUIUtil.getText(control));

			adjustScaleData(queryManager.getQuery());

			// control may be disposed when updating query
			if (control.isDisposed()) {
				control = findText(queryManager.getQuery());
			}

			// Bind color to this data definition
			if (control != null) {
				updateControlBackground(control, queryManager.getQuery().getDefinition());
			}
		}
	}

	/**
	 * Binding color to specified control.
	 * 
	 * @param control
	 * @param expression
	 * @since 2.5
	 */
	private void updateControlBackground(Control control, String expression) {
		String bindingName = exprCodec.getBindingName(expression);
		ColorPalette.getInstance().putColor(bindingName);
		control.setBackground(ColorPalette.getInstance().getColor(bindingName));
	}

	/**
	 * Adjust min/max data element of scale when current expression type is
	 * different with old expression type.
	 * 
	 * @param query
	 * @since BIRT 2.3
	 */
	private void adjustScaleData(Query query) {
		EObject object = query;
		while (!(object instanceof Axis)) {
			if (object != null) {
				object = object.eContainer();
			} else {
				return;
			}
		}

		Axis axis = (Axis) object;
		AxisType axisType = axis.getType();
		DataElement minElement = axis.getScale().getMin();
		DataElement maxElement = axis.getScale().getMax();

		if (axisType == AxisType.DATE_TIME_LITERAL) {
			if (!(minElement instanceof DateTimeDataElement)) {
				axis.getScale().setMin(null);
			}
			if (!(maxElement instanceof DateTimeDataElement)) {
				axis.getScale().setMax(null);
			}
		} else if (axisType == AxisType.TEXT_LITERAL) {
			if (!(minElement instanceof TextDataElement)) {
				axis.getScale().setMin(null);
			}
			if (!(maxElement instanceof TextDataElement)) {
				axis.getScale().setMax(null);
			}
		} else if (axisType == AxisType.LINEAR_LITERAL || axisType == AxisType.LOGARITHMIC_LITERAL) {
			if (!(minElement instanceof NumberDataElement)) {
				axis.getScale().setMin(null);
			}
			if (!(maxElement instanceof NumberDataElement)) {
				axis.getScale().setMax(null);
			}
		}
	}

	/**
	 * Check if expression is valid.
	 * 
	 * @param control
	 * @param expression
	 * @return valid expression or not
	 * @since 2.3
	 */
	public boolean isValidExpression(Control control, String expression) {
		if (textCollection.containsKey(control)) {
			IQueryExpressionManager queryManager = textCollection.get(control);
			return queryManager.isValidExpression(expression);
		}

		return false;
	}

	/**
	 * Check if specified expression is valid to specified query. Now, only for
	 * share binding case, it should check it, other's case still returns true.
	 * 
	 * @param query
	 * @param expr
	 * @param isShareBinding
	 * @since 2.3
	 */
	public boolean isAcceptableExpression(Query query, String expr, boolean isShareBinding) {
		if (!isShareBinding) {
			return true;
		}

		Control control = findText(query);
		if (control != null) {
			return isValidExpression(control, expr);
		}

		return false;
	}

	/**
	 * Update the tooltip for value data definition component after grouping
	 * changed.
	 * 
	 * @since 2.5
	 */
	public void updateTooltip() {
		for (IQueryExpressionManager queryExprM : textCollection.values()) {
			queryExprM.setTooltipForInputControl();
		}
	}

	/**
	 * Returns the ExpressionButton connected with the given query.
	 * 
	 * @param query
	 * @return The ExpressionButton connected with the given query.
	 */
	public IExpressionButton findExpressionButton(Query query) {
		Iterator<Map.Entry<Control, IQueryExpressionManager>> iterator = textCollection.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<Control, IQueryExpressionManager> entry = iterator.next();
			if (entry.getValue().getQuery() == query) {
				return entry.getValue().getExpressionButton();
			}
		}
		return null;
	}
}
