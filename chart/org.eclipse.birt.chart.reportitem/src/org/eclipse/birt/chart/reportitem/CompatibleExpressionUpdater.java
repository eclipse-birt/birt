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

package org.eclipse.birt.chart.reportitem;

import java.util.Iterator;
import java.util.Map;

import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.TooltipValue;
import org.eclipse.birt.chart.model.attribute.URLValue;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.Action;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.birt.report.model.api.ActionHandle;
import org.eclipse.birt.report.model.api.ModuleUtil;
import org.eclipse.birt.report.model.api.ParamBindingHandle;
import org.eclipse.birt.report.model.api.SearchKeyHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.emf.common.util.EList;

/**
 * This class is used to maintain compatibility for data expression within chart
 * internally.
 */
class CompatibleExpressionUpdater {

	private static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.reportitem/trace"); //$NON-NLS-1$

	/**
	 * Update the old data expression in chart to compatible new expressions.
	 * 
	 * @param model
	 * @param newExpressions
	 */
	static final void update(Chart model, Map newExpressions) {
		if (newExpressions == null) {
			return;
		}

		if (model instanceof ChartWithAxes) {
			updateRowExpressions((ChartWithAxes) model, newExpressions);
		} else if (model instanceof ChartWithoutAxes) {
			updateRowExpressions((ChartWithoutAxes) model, newExpressions);
		}
	}

	private static void updateRowExpressions(ChartWithoutAxes cwoa, Map newExpressions) {
		EList elSD = cwoa.getSeriesDefinitions();

		if (elSD.size() == 0) {
			return;
		}

		SeriesDefinition sd = (SeriesDefinition) elSD.get(0);
		String sExpression;
		String newExp;

		// PROJECT THE EXPRESSION ASSOCIATED WITH THE BASE SERIES EXPRESSION
		final Series seBase = sd.getDesignTimeSeries();
		EList elBaseSeries = seBase.getDataDefinition();

		for (Iterator itr = elBaseSeries.iterator(); itr.hasNext();) {
			final Query qBaseSeries = (Query) itr.next();
			if (qBaseSeries == null) {
				continue;
			}
			sExpression = qBaseSeries.getDefinition();
			newExp = (String) newExpressions.get(sExpression);
			if (newExp != null) {
				qBaseSeries.setDefinition(newExp);
			}
		}

		// PROJECT ALL DATA DEFINITIONS ASSOCIATED WITH THE ORTHOGONAL
		// SERIES
		Query qOrthogonalSeriesDefinition, qOrthogonalSeries;
		Series seOrthogonal;
		EList elOrthogonalSeries;
		elSD = sd.getSeriesDefinitions();

		for (int k = 0; k < elSD.size(); k++) {
			sd = (SeriesDefinition) elSD.get(k);
			qOrthogonalSeriesDefinition = sd.getQuery();
			if (qOrthogonalSeriesDefinition == null) {
				continue;
			}
			sExpression = qOrthogonalSeriesDefinition.getDefinition();
			newExp = (String) newExpressions.get(sExpression);
			if (newExp != null) {
				qOrthogonalSeriesDefinition.setDefinition(newExp);
			}

			seOrthogonal = sd.getDesignTimeSeries();
			elOrthogonalSeries = seOrthogonal.getDataDefinition();
			for (int i = 0; i < elOrthogonalSeries.size(); i++) {
				qOrthogonalSeries = (Query) elOrthogonalSeries.get(i);
				if (qOrthogonalSeries == null) // NPE PROTECTION
				{
					continue;
				}
				sExpression = qOrthogonalSeries.getDefinition();
				newExp = (String) newExpressions.get(sExpression);
				if (newExp != null) {
					qOrthogonalSeries.setDefinition(newExp);
				}
			}

			// Update orthogonal series trigger expressions.
			updateSeriesTriggerExpressions(seOrthogonal, newExpressions);
		}
	}

	private static void updateRowExpressions(ChartWithAxes cwa, Map newExpressions) {
		final Axis axPrimaryBase = cwa.getPrimaryBaseAxes()[0];
		EList elSD = axPrimaryBase.getSeriesDefinitions();

		if (elSD.size() == 0) {
			return;
		}

		SeriesDefinition sd = (SeriesDefinition) elSD.get(0);
		String sExpression;
		String newExp;

		// PROJECT THE EXPRESSION ASSOCIATED WITH THE BASE SERIES EXPRESSION
		final Series seBase = sd.getDesignTimeSeries();
		EList elBaseSeries = seBase.getDataDefinition();

		for (Iterator itr = elBaseSeries.iterator(); itr.hasNext();) {
			final Query qBaseSeries = (Query) itr.next();
			if (qBaseSeries == null) {
				continue;
			}
			sExpression = qBaseSeries.getDefinition();
			newExp = (String) newExpressions.get(sExpression);
			if (newExp != null) {
				qBaseSeries.setDefinition(newExp);
			}
		}

		// PROJECT ALL DATA DEFINITIONS ASSOCIATED WITH THE ORTHOGONAL SERIES
		Query qOrthogonalSeriesDefinition, qOrthogonalSeries;
		Series seOrthogonal;
		EList elOrthogonalSeries;
		final Axis[] axaOrthogonal = cwa.getOrthogonalAxes(axPrimaryBase, true);

		for (int j = 0; j < axaOrthogonal.length; j++) {
			elSD = axaOrthogonal[j].getSeriesDefinitions();
			for (int k = 0; k < elSD.size(); k++) {
				sd = (SeriesDefinition) elSD.get(k);
				qOrthogonalSeriesDefinition = sd.getQuery();
				if (qOrthogonalSeriesDefinition == null) {
					continue;
				}
				sExpression = qOrthogonalSeriesDefinition.getDefinition();
				newExp = (String) newExpressions.get(sExpression);
				if (newExp != null) {
					qOrthogonalSeriesDefinition.setDefinition(newExp);
				}

				seOrthogonal = sd.getDesignTimeSeries();
				elOrthogonalSeries = seOrthogonal.getDataDefinition();

				for (int i = 0; i < elOrthogonalSeries.size(); i++) {
					qOrthogonalSeries = (Query) elOrthogonalSeries.get(i);
					if (qOrthogonalSeries == null) // NPE PROTECTION
					{
						continue;
					}
					sExpression = qOrthogonalSeries.getDefinition();
					newExp = (String) newExpressions.get(sExpression);
					if (newExp != null) {
						qOrthogonalSeries.setDefinition(newExp);
					}
				}

				// Update orthogonal series trigger expressions.
				updateSeriesTriggerExpressions(seOrthogonal, newExpressions);
			}
		}
	}

	private static void updateSeriesTriggerExpressions(Series se, Map newExpressions) {
		if (se == null || newExpressions == null) {
			return;
		}

		for (Iterator itr = se.getTriggers().iterator(); itr.hasNext();) {
			Trigger tg = (Trigger) itr.next();

			updateActionExpressions(tg.getAction(), newExpressions);
		}
	}

	private static void updateActionExpressions(Action action, Map newExpressions) {
		if (ActionType.URL_REDIRECT_LITERAL.equals(action.getType())) {
			URLValue uv = (URLValue) action.getValue();

			String sa = uv.getBaseUrl();

			try {
				boolean updated = false;

				ActionHandle handle = ModuleUtil.deserializeAction(sa);

				String exp;
				String newExp;

				if (DesignChoiceConstants.ACTION_LINK_TYPE_HYPERLINK.equals(handle.getLinkType())) {
					exp = handle.getURI();
					newExp = (String) newExpressions.get(exp);
					if (newExp != null) {
						updated = true;
						handle.setURI(newExp);
					}
				} else if (DesignChoiceConstants.ACTION_LINK_TYPE_BOOKMARK_LINK.equals(handle.getLinkType())) {
					exp = handle.getTargetBookmark();
					newExp = (String) newExpressions.get(exp);
					if (newExp != null) {
						updated = true;
						handle.setTargetBookmark(newExp);
					}
				} else if (DesignChoiceConstants.ACTION_LINK_TYPE_DRILL_THROUGH.equals(handle.getLinkType())) {
					exp = handle.getTargetBookmark();
					newExp = (String) newExpressions.get(exp);
					if (newExp != null) {
						updated = true;
						handle.setTargetBookmark(newExp);
					}

					for (Iterator itr = handle.getSearch().iterator(); itr.hasNext();) {
						SearchKeyHandle skh = (SearchKeyHandle) itr.next();
						exp = skh.getExpression();
						newExp = (String) newExpressions.get(exp);
						if (newExp != null) {
							updated = true;
							skh.setExpression(newExp);
						}
					}

					for (Iterator itr = handle.getParamBindings().iterator(); itr.hasNext();) {
						ParamBindingHandle pbh = (ParamBindingHandle) itr.next();
						exp = pbh.getExpression();
						newExp = (String) newExpressions.get(exp);
						if (newExp != null) {
							updated = true;
							pbh.setExpression(newExp);
						}
					}

				}

				if (updated) {
					uv.setBaseUrl(ModuleUtil.serializeAction(handle));
				}
			} catch (Exception e) {
				logger.log(e);
			}
		} else if (ActionType.SHOW_TOOLTIP_LITERAL.equals(action.getType())) {
			TooltipValue tv = (TooltipValue) action.getValue();

			String exp = tv.getText();

			String newExp = (String) newExpressions.get(exp);

			if (newExp != null) {
				tv.setText(newExp);
			}
		}
	}
}
