/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.ui.swt.series;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Marker;
import org.eclipse.birt.chart.model.attribute.MarkerType;
import org.eclipse.birt.chart.model.attribute.impl.MarkerImpl;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.type.GanttSeries;
import org.eclipse.birt.chart.model.util.ChartElementUtil;
import org.eclipse.birt.chart.model.util.DefaultValueProvider;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.plugin.ChartUIExtensionPlugin;
import org.eclipse.birt.chart.ui.swt.ChartCheckbox;
import org.eclipse.birt.chart.ui.swt.composites.GanttLineAttributesComposite;
import org.eclipse.birt.chart.ui.swt.composites.LineAttributesComposite;
import org.eclipse.birt.chart.ui.swt.composites.MarkerEditorComposite;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIExtensionUtil;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

public class GanttSeriesAttributeComposite extends Composite implements SelectionListener, Listener {
	private transient ChartCheckbox btnPalette = null;

	private transient Group grpLine = null;

	private transient GanttLineAttributesComposite gliacGantt = null;

	private transient Group grpOutline = null;

	private transient LineAttributesComposite oliacGantt = null;

	private transient GanttSeries series = null;

	private GanttSeries defSeries = DefaultValueProvider.defGanttSeries();

	private transient ChartWizardContext context;

	private static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.ui.extension/swt.series"); //$NON-NLS-1$

	/**
	 * @param parent
	 * @param style
	 */
	public GanttSeriesAttributeComposite(Composite parent, int style, ChartWizardContext context, Series series) {
		super(parent, style);
		if (!(series instanceof GanttSeries)) {
			try {
				throw new ChartException(ChartUIExtensionPlugin.ID, ChartException.VALIDATION,
						"GanttSeriesAttributeComposite.Exception.IllegalArgument", //$NON-NLS-1$
						new Object[] { series.getClass().getName() }, Messages.getResourceBundle());
			} catch (ChartException e) {
				logger.log(e);
				e.printStackTrace();
			}
		}
		this.series = (GanttSeries) series;
		this.context = context;
		init();
		placeComponents();
		ChartUIUtil.bindHelp(parent, ChartHelpContextIds.SUBTASK_YSERIES_GANTT);
	}

	private void init() {
		this.setSize(getParent().getClientArea().width, getParent().getClientArea().height);
	}

	private void placeComponents() {
		// Layout for content composite
		GridLayout glContent = new GridLayout(2, false);
		glContent.marginHeight = 2;
		glContent.marginWidth = 2;
		glContent.horizontalSpacing = 0;

		// Main content composite
		this.setLayout(glContent);

		// Layout for the Marker group

		Composite cmpMarker = new Composite(this, SWT.NONE);
		{
			cmpMarker.setLayout(new GridLayout());
			cmpMarker.setLayoutData(new GridData(GridData.FILL_BOTH));
		}

		Group grpMarker = new Group(cmpMarker, SWT.NONE);
		grpMarker.setText(Messages.getString("GanttSeriesAttributeComposite.Lbl.Marker")); //$NON-NLS-1$
		grpMarker.setLayout(new GridLayout(2, false));
		grpMarker.setLayoutData(new GridData(GridData.FILL_BOTH));

		// Layout for the Start Marker
		Label lblStart = new Label(grpMarker, SWT.NONE);
		lblStart.setText(Messages.getString("GanttSeriesAttributeComposite.Lbl.Start")); //$NON-NLS-1$

		new MarkerEditorComposite(grpMarker, createMarker(series.getStartMarker()), context,
				defSeries.getStartMarker());

		// Layout for the End Marker
		Label lblEnd = new Label(grpMarker, SWT.NONE);
		lblEnd.setText(Messages.getString("GanttSeriesAttributeComposite.Lbl.End")); //$NON-NLS-1$

		new MarkerEditorComposite(grpMarker, createMarker(series.getEndMarker()), context, defSeries.getEndMarker());

		Composite cmpGroup = new Composite(this, SWT.NONE);
		{
			GridLayout glGroup = new GridLayout(2, true);
			glGroup.marginWidth = 0;
			glGroup.horizontalSpacing = 6;
			cmpGroup.setLayout(glGroup);
			cmpGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
		}
		// Layout for Connection Line
		grpLine = new Group(cmpGroup, SWT.NONE);
		GridData gdGRPLine = new GridData(GridData.FILL_BOTH);
		grpLine.setLayout(new GridLayout(1, false));
		grpLine.setLayoutData(gdGRPLine);
		grpLine.setText(Messages.getString("GanttSeriesAttributeComposite.Lbl.Bars")); //$NON-NLS-1$

		gliacGantt = new GanttLineAttributesComposite(grpLine, context, SWT.NONE, series.getConnectionLine(),
				getLineOptionalStyles(), DefaultValueProvider.defGanttSeries().getConnectionLine());
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		gliacGantt.setLayoutData(gd);
		gliacGantt.addListener(this);

		btnPalette = context.getUIFactory().createChartCheckbox(grpLine, SWT.NONE, defSeries.isPaletteLineColor());
		{
			btnPalette.setText(Messages.getString("GanttSeriesAttributeComposite.Lbl.BarPalette")); //$NON-NLS-1$
			gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.horizontalIndent = 4;
			btnPalette.setLayoutData(gd);
			btnPalette.setSelectionState(series.isSetPaletteLineColor()
					? (series.isPaletteLineColor() ? ChartCheckbox.STATE_SELECTED : ChartCheckbox.STATE_UNSELECTED)
					: ChartCheckbox.STATE_GRAYED);
			btnPalette.addSelectionListener(this);
		}

		// Layout for Outline
		grpOutline = new Group(cmpGroup, SWT.NONE);
		GridData gdGRPOutline = new GridData(GridData.FILL_BOTH);
		grpOutline.setLayout(new FillLayout());
		grpOutline.setLayoutData(gdGRPOutline);
		grpOutline.setText(Messages.getString("GanttSeriesAttributeComposite.Lbl.Outline")); //$NON-NLS-1$

		oliacGantt = new LineAttributesComposite(grpOutline, SWT.NONE, getOutlineOptionalStyles(), context,
				series.getOutline(), defSeries.getOutline());
		oliacGantt.addListener(this);
	}

	protected int getOutlineOptionalStyles() {
		return LineAttributesComposite.ENABLE_WIDTH | LineAttributesComposite.ENABLE_STYLES
				| LineAttributesComposite.ENABLE_VISIBILITY | LineAttributesComposite.ENABLE_COLOR
				| LineAttributesComposite.ENABLE_AUTO_COLOR;
	}

	protected int getLineOptionalStyles() {
		return GanttLineAttributesComposite.ENABLE_STYLES | GanttLineAttributesComposite.ENABLE_VISIBILITY
				| GanttLineAttributesComposite.ENABLE_WIDTH;
	}

	public Point getPreferredSize() {
		return new Point(400, 200);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.
	 * events.SelectionEvent)
	 */
	@Override
	public void widgetSelected(SelectionEvent e) {
		if (e.getSource().equals(btnPalette)) {
			ChartElementUtil.setEObjectAttribute(series, "paletteLineColor", //$NON-NLS-1$
					btnPalette.getSelectionState() == ChartCheckbox.STATE_SELECTED,
					btnPalette.getSelectionState() == ChartCheckbox.STATE_GRAYED);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.
	 * swt.events.SelectionEvent)
	 */
	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	@Override
	public void handleEvent(Event event) {
		boolean isUnset = (event.detail == ChartUIExtensionUtil.PROPERTY_UNSET);
		if (event.widget.equals(gliacGantt)) {
			if (event.type == GanttLineAttributesComposite.VISIBILITY_CHANGED_EVENT) {
				ChartElementUtil.setEObjectAttribute(series.getConnectionLine(), "visible", //$NON-NLS-1$
						((Boolean) event.data).booleanValue(), isUnset);
				btnPalette.setEnabled(!(!isUnset && !((Boolean) event.data).booleanValue()));
			} else if (event.type == GanttLineAttributesComposite.STYLE_CHANGED_EVENT) {
				ChartElementUtil.setEObjectAttribute(series.getConnectionLine(), "style", //$NON-NLS-1$
						event.data, isUnset);
			} else if (event.type == GanttLineAttributesComposite.WIDTH_CHANGED_EVENT) {
				ChartElementUtil.setEObjectAttribute(series.getConnectionLine(), "thickness", //$NON-NLS-1$
						((Integer) event.data).intValue(), isUnset);
			} else if (event.type == GanttLineAttributesComposite.COLOR_CHANGED_EVENT) {
				series.getConnectionLine().setColor((ColorDefinition) event.data);
			}
		} else if (event.widget.equals(oliacGantt)) {
			if (event.type == LineAttributesComposite.VISIBILITY_CHANGED_EVENT) {
				ChartElementUtil.setEObjectAttribute(series.getOutline(), "visible", //$NON-NLS-1$
						((Boolean) event.data).booleanValue(), isUnset);
			} else if (event.type == LineAttributesComposite.STYLE_CHANGED_EVENT) {
				ChartElementUtil.setEObjectAttribute(series.getOutline(), "style", //$NON-NLS-1$
						event.data, isUnset);
			} else if (event.type == LineAttributesComposite.WIDTH_CHANGED_EVENT) {
				ChartElementUtil.setEObjectAttribute(series.getOutline(), "thickness", //$NON-NLS-1$
						((Integer) event.data).intValue(), isUnset);
			} else if (event.type == LineAttributesComposite.COLOR_CHANGED_EVENT) {
				series.getOutline().setColor((ColorDefinition) event.data);
			}
		}
	}

	private Marker createMarker(Marker marker) {
		if (marker == null) {
			marker = MarkerImpl.create(MarkerType.NABLA_LITERAL, 4);
			marker.eAdapters().addAll(series.eAdapters());
		}
		return marker;
	}
}
