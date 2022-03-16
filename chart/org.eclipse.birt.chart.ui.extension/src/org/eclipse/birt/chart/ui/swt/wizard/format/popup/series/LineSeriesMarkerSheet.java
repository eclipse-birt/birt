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

package org.eclipse.birt.chart.ui.swt.wizard.format.popup.series;

import org.eclipse.birt.chart.api.ChartEngine;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Marker;
import org.eclipse.birt.chart.model.attribute.MarkerType;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.ImageImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.attribute.impl.LocationImpl;
import org.eclipse.birt.chart.model.attribute.impl.MarkerImpl;
import org.eclipse.birt.chart.model.type.DifferenceSeries;
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.model.util.ChartDefaultValueUtil;
import org.eclipse.birt.chart.render.MarkerRenderer;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.composites.MarkerEditorComposite;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizard;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.AbstractPopupSheet;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.birt.chart.util.NameSet;
import org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase;
import org.eclipse.emf.common.util.EList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;

/**
 *
 */

public class LineSeriesMarkerSheet extends AbstractPopupSheet implements SelectionListener {

	private transient LineSeries series;

	private transient IDeviceRenderer idrSWT = null;

	/** Holds the width of each marker UI block */
	private final static int MARKER_BLOCK_WIDTH = 40;

	/** Holds the width of each marker UI block */
	private final static int MARKER_BLOCK_HEIGHT = 25;

	/** Holds the max number of each row */
	private final static int MARKER_ROW_MAX_NUMBER = 4;

	/** Holds the max number of columns */
	private final static int MARKER_COLUMN_MAX_NUMBER = 3;

	private transient Canvas cnvMarkers;

	private transient Button btnAdd;

	private transient Button btnRemove;

	private transient Button btnUp;

	private transient Button btnDown;

	private transient MarkerEditorComposite newMarkerEditor;

	private transient MarkerEditorComposite currentMarkerEditor;

	private transient boolean bPositive = true;

	/** Holds the selected index of marker */
	private transient int iSelectedIndex = 0;

	/** Holds the starting row of marker list */
	private transient int iStartRow = 0;

	private NameSet markerTypeSet = null;

	private String outlineText = null;

	public LineSeriesMarkerSheet(String title, ChartWizardContext context, LineSeries series) {
		super(title, context, false);
		this.series = series;
	}

	public LineSeriesMarkerSheet(String title, ChartWizardContext context, LineSeries series, NameSet markerTypeSet,
			String outlineText) {
		super(title, context, false);
		this.series = series;
		this.markerTypeSet = markerTypeSet;
		this.outlineText = outlineText;
	}

	public LineSeriesMarkerSheet(String title, ChartWizardContext context, LineSeries series, boolean bPositive) {
		super(title, context, false);
		this.series = series;
		this.bPositive = bPositive;
	}

	@Override
	protected Composite getComponent(Composite parent) {
		ChartUIUtil.bindHelp(parent, ChartHelpContextIds.POPUP_SERIES_LINE_MARKER);

		Composite cmpContent = new Composite(parent, SWT.NO_FOCUS);
		{
			GridLayout layout = new GridLayout();
			cmpContent.setLayout(layout);
		}

		Group grpTop = new Group(cmpContent, SWT.NO_FOCUS);
		{
			GridLayout layout = new GridLayout(5, false);
			grpTop.setLayout(layout);
			grpTop.setLayoutData(new GridData());
			grpTop.setText(Messages.getString("LineSeriesMarkerSheet.Label.Markers")); //$NON-NLS-1$
		}

		cnvMarkers = new Canvas(grpTop, SWT.V_SCROLL);
		{
			GridData gd = new GridData(GridData.FILL_BOTH);
			gd.horizontalSpan = 5;
			gd.widthHint = MARKER_ROW_MAX_NUMBER * MARKER_BLOCK_WIDTH + 10;
			gd.heightHint = MARKER_COLUMN_MAX_NUMBER * MARKER_BLOCK_HEIGHT + 5;
			cnvMarkers.setLayoutData(gd);

			Listener canvasMarkerslistener = new Listener() {

				@Override
				public void handleEvent(Event event) {
					handleEventCanvasMarkers(event);
				}
			};

			int[] canvasMarkersEvents = { SWT.KeyDown, SWT.MouseDown, SWT.Traverse, SWT.Paint };
			for (int i = 0; i < canvasMarkersEvents.length; i++) {
				cnvMarkers.addListener(canvasMarkersEvents[i], canvasMarkerslistener);
			}

			updateScrollBar();
			cnvMarkers.getVerticalBar().addSelectionListener(this);
		}

		createButtonGroup(grpTop);

		// This control needs to be repainted by gc
		if (markerTypeSet != null) {
			String name = getMarkers().get(0).getType().getName();
			if (markerTypeSet.getNameIndex(name) < 0) {
				Marker m = MarkerImpl.create(MarkerType.getByName(markerTypeSet.getNames()[0]), 4);
				m.setVisible(true);
				LineAttributes la = AttributeFactory.eINSTANCE.createLineAttributes();
				la.setVisible(true);
				m.setOutline(la);
				getMarkers().remove(0);
				getMarkers().add(0, m);
				m.eAdapters().addAll(series.eAdapters());
			}
		}
		currentMarkerEditor = new MarkerEditorComposite(cnvMarkers, getMarkers().get(0), getContext(),
				getDefaultMarker());
		{
			currentMarkerEditor.setBounds(0, 0, MARKER_BLOCK_WIDTH, MARKER_BLOCK_HEIGHT);
		}
		if (markerTypeSet != null) {
			currentMarkerEditor.setSupportedMarkerTypes(markerTypeSet);

		}
		if (outlineText != null) {
			currentMarkerEditor.setOutlineText(outlineText);
		}

		setEnabledState();

		try {
			idrSWT = ChartEngine.instance().getRenderer("dv.SWT"); //$NON-NLS-1$
			idrSWT.getDisplayServer();
		} catch (ChartException pex) {
			WizardBase.displayException(pex);
		}

		cmpContent.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent e) {
				if (idrSWT != null) {
					idrSWT.dispose();
				}
			}
		});

		return cmpContent;
	}

	void handleEventCanvasMarkers(Event event) {
		switch (event.type) {
		case SWT.KeyDown: {
			if (event.keyCode == SWT.ARROW_LEFT) {
				if (iSelectedIndex - 1 >= 0) {
					iSelectedIndex--;
					setEnabledState();
				}
			} else if (event.keyCode == SWT.ARROW_RIGHT) {
				if (iSelectedIndex + 1 < getMarkers().size()) {
					iSelectedIndex++;
					setEnabledState();
				}
			} else if (event.keyCode == SWT.ARROW_UP) {
				if (iSelectedIndex - MARKER_ROW_MAX_NUMBER >= 0) {
					iSelectedIndex -= MARKER_ROW_MAX_NUMBER;
					setEnabledState();
				}
			} else if (event.keyCode == SWT.ARROW_DOWN) {
				if (iSelectedIndex + MARKER_ROW_MAX_NUMBER < getMarkers().size()) {
					iSelectedIndex += MARKER_ROW_MAX_NUMBER;
					setEnabledState();
				}
			}

			else if (event.keyCode == SWT.CR || event.keyCode == SWT.KEYPAD_CR) {
				currentMarkerEditor.setFocus();
			} else if (event.keyCode == SWT.ESC) {
				cnvMarkers.getShell().close();
			}
			break;
		}
		case SWT.Traverse: {
			switch (event.detail) {
			case SWT.TRAVERSE_RETURN:
			case SWT.TRAVERSE_TAB_NEXT:
			case SWT.TRAVERSE_TAB_PREVIOUS:
			case SWT.TRAVERSE_ARROW_PREVIOUS:
			case SWT.TRAVERSE_ARROW_NEXT:
				event.doit = true;
				cnvMarkers.redraw();
			}
			break;
		}
		case SWT.Paint:
			paintControl(new PaintEvent(event));
			break;
		case SWT.MouseDown:
			mouseDown(new MouseEvent(event));
			break;
		}
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		if (e.widget.equals(btnAdd)) {
			// Select the new marker
			iSelectedIndex = getMarkers().size();

			// If the selected is under the bottom, move to new row
			if ((iStartRow + MARKER_COLUMN_MAX_NUMBER) * MARKER_ROW_MAX_NUMBER == iSelectedIndex) {
				iStartRow++;
			}

			getMarkers().add(newMarkerEditor.getMarker());
			newMarkerEditor.setMarker(createMarker());

			cnvMarkers.redraw();
			updateScrollBar();
			setEnabledState();
		} else if (e.widget.equals(btnRemove)) {
			// If the selected is the first of the bottom row, move to the
			// previous
			if (iStartRow > 0 && (iStartRow + MARKER_COLUMN_MAX_NUMBER - 1) * MARKER_ROW_MAX_NUMBER == iSelectedIndex) {
				iStartRow--;
			}

			// Return to the previous if it's the last
			if (this.iSelectedIndex == getMarkers().size() - 1) {
				iSelectedIndex--;
			}

			getMarkers().remove(currentMarkerEditor.getMarker());
			currentMarkerEditor.setMarker(getMarkers().get(iSelectedIndex));

			cnvMarkers.redraw();
			updateScrollBar();
			setEnabledState();
		} else if (e.widget.equals(btnUp)) {
			if (iSelectedIndex > 0) {
				iSelectedIndex--;
				getMarkers().move(iSelectedIndex, currentMarkerEditor.getMarker());
				cnvMarkers.redraw();
				setEnabledState();
			}
		} else if (e.widget.equals(btnDown)) {
			if (iSelectedIndex < getMarkers().size() - 1) {
				iSelectedIndex++;
				getMarkers().move(iSelectedIndex, currentMarkerEditor.getMarker());
				cnvMarkers.redraw();
				setEnabledState();
			}
		} else if (e.widget.equals(cnvMarkers.getVerticalBar())) {
			iStartRow = cnvMarkers.getVerticalBar().getSelection();
			cnvMarkers.redraw();
		}
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub

	}

	protected void createButtonGroup(Group grpTop) {
		btnAdd = new Button(grpTop, SWT.NONE);
		{
			btnAdd.setText(Messages.getString("LineSeriesMarkerSheet.Label.Add")); //$NON-NLS-1$
			btnAdd.addSelectionListener(this);
		}

		newMarkerEditor = new MarkerEditorComposite(grpTop, createMarker(), getContext(), getDefaultMarker());
		if (markerTypeSet != null) {
			newMarkerEditor.setSupportedMarkerTypes(markerTypeSet);
		}
		if (outlineText != null) {
			newMarkerEditor.setOutlineText(outlineText);
		}

		btnRemove = new Button(grpTop, SWT.NONE);
		{
			btnRemove.setText(Messages.getString("LineSeriesMarkerSheet.Label.Remove")); //$NON-NLS-1$
			btnRemove.addSelectionListener(this);
		}

		btnUp = new Button(grpTop, SWT.ARROW | SWT.UP);
		{
			btnUp.setToolTipText(Messages.getString("PaletteEditorComposite.Lbl.Up")); //$NON-NLS-1$
			btnUp.addSelectionListener(this);
		}

		btnDown = new Button(grpTop, SWT.ARROW | SWT.DOWN);
		{
			btnDown.setToolTipText(Messages.getString("PaletteEditorComposite.Lbl.Down")); //$NON-NLS-1$
			btnDown.addSelectionListener(this);
		}
	}

	private Marker createMarker() {
		Marker marker;
		if (markerTypeSet != null) {
			marker = MarkerImpl.createDefault(MarkerType.getByName(markerTypeSet.getNames()[0]), 4, false);
		} else {
			marker = MarkerImpl.createDefault(MarkerType.BOX_LITERAL, 4, false);
		}
		marker.eAdapters().addAll(series.eAdapters());
		return marker;
	}

	void paintControl(PaintEvent e) {
		GC gc = e.gc;

		int markerSize = getMarkers().size();
		int x = 0;
		int y = 0;
		for (int i = 0; i < markerSize;) {
			if (i < iStartRow * MARKER_ROW_MAX_NUMBER) {
				i += MARKER_ROW_MAX_NUMBER;
				continue;
			}

			paintMarker(gc, getMarkers().get(i),
					LocationImpl.create(x + MARKER_BLOCK_WIDTH / 2, y + MARKER_BLOCK_HEIGHT / 2));

			if (i == iSelectedIndex) {
				currentMarkerEditor.setMarker(getMarkers().get(i));
				currentMarkerEditor.setLocation(x + 8, y);
			}

			i++;
			if (i % MARKER_ROW_MAX_NUMBER == 0) {
				y += MARKER_BLOCK_HEIGHT;
				x = 0;
			} else {
				x += MARKER_BLOCK_WIDTH;
			}
		}
	}

	private void paintMarker(GC gc, Marker currentMarker, Location location) {
		// Paint an icon sample, not a real icon in the Fill
		Marker renderMarker = currentMarker;
		if (currentMarker.getType() == MarkerType.ICON_LITERAL) {
			renderMarker = currentMarker.copyInstance();
			renderMarker.setFill(ImageImpl.create(UIHelper.getURL("icons/obj16/marker_icon.gif").toString())); //$NON-NLS-1$
		}

		idrSWT.setProperty(IDeviceRenderer.GRAPHICS_CONTEXT, gc);
		final MarkerRenderer mr = new MarkerRenderer(idrSWT, StructureSource.createUnknown(null), location,
				LineAttributesImpl.create(
						renderMarker.isVisible() ? ColorDefinitionImpl.BLUE() : ColorDefinitionImpl.GREY(),
						LineStyle.SOLID_LITERAL, 1),
				renderMarker.isVisible() ? ColorDefinitionImpl.create(80, 168, 218) : ColorDefinitionImpl.GREY(),
				renderMarker, 4, null, false, false);
		try {
			mr.draw(idrSWT);
			ChartWizard.removeException(ChartWizard.LineSMarkerSh_ID);
		} catch (ChartException ex) {
			ChartWizard.showException(ChartWizard.LineSMarkerSh_ID, ex.getLocalizedMessage());
		}
	}

	private EList<Marker> getMarkers() {
		if (bPositive) {
			return series.getMarkers();
		}
		return ((DifferenceSeries) series).getNegativeMarkers();
	}

	private Marker getDefaultMarker() {
		if (bPositive) {
			return ((LineSeries) ChartDefaultValueUtil.getDefaultSeries(series)).getMarkers().get(0);
		}
		return ((DifferenceSeries) ChartDefaultValueUtil.getDefaultSeries(series)).getNegativeMarkers().get(0);
	}

	private void updateScrollBar() {
		ScrollBar vsb = cnvMarkers.getVerticalBar();
		vsb.setValues(iStartRow, 0,
				Math.max(0, (getMarkers().size() - 1) / MARKER_ROW_MAX_NUMBER + 2 - MARKER_COLUMN_MAX_NUMBER), 1, 1, 1);
	}

	void mouseDown(MouseEvent e) {
		if (e.widget.equals(cnvMarkers)) {
			int ix = e.x / MARKER_BLOCK_WIDTH;
			int iy = e.y / MARKER_BLOCK_HEIGHT + iStartRow;
			int clickIndex = iy * MARKER_ROW_MAX_NUMBER + ix;
			if (ix >= MARKER_ROW_MAX_NUMBER || clickIndex >= getMarkers().size()) {
				// Keep the previous selection if the current is out of bound
				return;
			}
			iSelectedIndex = clickIndex;
			this.cnvMarkers.redraw();

			setEnabledState();
		}
	}

	protected void setEnabledState() {
		if (iSelectedIndex < 0) {
			btnUp.setEnabled(false);
			btnDown.setEnabled(false);
			btnRemove.setEnabled(false);
			currentMarkerEditor.setVisible(false);
		} else {
			btnUp.setEnabled(iSelectedIndex > 0);
			btnDown.setEnabled(iSelectedIndex < getMarkers().size() - 1);
			btnRemove.setEnabled(getMarkers().size() > 1);
			currentMarkerEditor.setVisible(true);
		}
	}
}
