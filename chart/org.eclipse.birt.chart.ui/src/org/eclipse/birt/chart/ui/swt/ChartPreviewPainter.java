/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt;

import org.eclipse.birt.chart.api.ChartEngine;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.IUpdateNotifier;
import org.eclipse.birt.chart.factory.GeneratedChartState;
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizard;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

/**
 * The class is responsible for computing and painting chart in chart builder.
 */
public class ChartPreviewPainter extends ChartPreviewPainterBase implements PaintListener, IUpdateNotifier {

	private boolean bIsPainting = false;

	private Image buffer;

	private GeneratedChartState gcs = null;

	private static int X_OFFSET = 3;
	private static int Y_OFFSET = 3;

	public ChartPreviewPainter(ChartWizardContext wizardContext) {
		super(wizardContext);
	}

	/**
	 * Generate whole chart and paint it.
	 */
	protected void paintChart() {
		if (!isDisposedPreviewCanvas()) {
			// Invoke it later and prevent freezing UI .
			Display.getDefault().asyncExec(new Runnable() {

				public void run() {
					updateBuffer();
					if (!isDisposedPreviewCanvas()) {
						preview.redraw();
					}
				}
			});
		}
	}

	private void updateBuffer() {
		if (bIsPainting) {
			return;
		}
		if (chart == null) {
			return;
		}

		if (isDisposedPreviewCanvas()) {
			return;
		}
		Rectangle re = preview.getClientArea();

		final Rectangle adjustedRe = new Rectangle(0, 0, re.width, re.height);

		if (adjustedRe.width - 2 * X_OFFSET <= 0 || adjustedRe.height - 2 * Y_OFFSET <= 0) {
			if (buffer != null && !buffer.isDisposed()) {
				buffer.dispose();
				buffer = null;
			}
			return;
		}

		bIsPainting = true;

		Image oldBuffer = null;

		if (buffer == null) {
			buffer = new Image(Display.getDefault(), adjustedRe);
		} else {
			Rectangle ore = buffer.getBounds();

			oldBuffer = buffer;

			if (!adjustedRe.equals(ore)) {
				buffer = new Image(Display.getDefault(), adjustedRe);
			}
		}

		GC gc = new GC(buffer);

		// fill default backgournd as white.
		gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		gc.fillRectangle(buffer.getBounds());

		final Bounds bo = BoundsImpl.create(X_OFFSET, Y_OFFSET, adjustedRe.width - 2 * X_OFFSET,
				adjustedRe.height - 2 * Y_OFFSET);

		IDeviceRenderer deviceRenderer = null;

		try {
			deviceRenderer = ChartEngine.instance().getRenderer("dv.SWT"); //$NON-NLS-1$

			// The repaintChart should be improved, not to rebuild the whole
			// chart, for the interactivity to work
			// correctly. repaintchart should just call render - David
			// deviceRenderer.setProperty( IDeviceRenderer.UPDATE_NOTIFIER, this
			// );
			deviceRenderer.setProperty(IDeviceRenderer.GRAPHICS_CONTEXT, gc);
			bo.scale(72d / deviceRenderer.getDisplayServer().getDpiResolution());
			// CONVERT TO POINTS

			// GENERATE AND RENDER THE CHART
			final Generator gr = Generator.instance();
			if (Display.getCurrent().getHighContrast()) {
				Color color = Display.getDefault().getSystemColor(SWT.COLOR_LIST_BACKGROUND);
				ColorDefinition cd = ColorDefinitionImpl.create(color.getRed(), color.getGreen(), color.getBlue());
				gr.setDefaultBackground(cd);
			}

			RunTimeContext rtc = new RunTimeContext();
			rtc.setScriptingEnabled(false);
			rtc.setMessageLookup(new ChartBuilderMessageLookup(this.wizardContext.getUIServiceProvider()));
			rtc.setRightToLeft(wizardContext.isRtL());
			rtc.setRightToLeftText(wizardContext.isTextRtL());
			rtc.setResourceFinder(wizardContext.getResourceFinder());
			rtc.setExternalizer(wizardContext.getExternalizer());

			wizardContext.getUIFactory().createUIHelper().updateDefaultTitle((Chart) chart,
					wizardContext.getExtendedItem());
			gcs = gr.build(deviceRenderer.getDisplayServer(), (Chart) chart.copyInstance(), bo, null, rtc,
					isProcessorEnabled() ? wizardContext.getProcessor() : null);
			gr.render(deviceRenderer, gcs);
			ChartWizard.removeException(ChartWizard.PreviewPainter_ID);
		} catch (Exception ex) {
			buffer = oldBuffer;
			ChartWizard.showException(ChartWizard.PreviewPainter_ID, ex.getLocalizedMessage());
		} finally {
			gc.dispose();
			if (deviceRenderer != null) {
				deviceRenderer.dispose();
			}
		}

		if (oldBuffer != null && oldBuffer != buffer) {
			oldBuffer.dispose();
		}
		bIsPainting = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.PaintListener#paintControl(org.eclipse.swt.events
	 * .PaintEvent)
	 */
	public void paintControl(PaintEvent pev) {
		GC gc = pev.gc;
		if (buffer != null) {
			gc.drawImage(buffer, 0, 0);
		}
	}

	public void dispose() {
		super.dispose();

		if (buffer != null) {
			buffer.dispose();
			buffer = null;
		}
	}

	public Chart getDesignTimeModel() {
		return wizardContext.getModel();
	}

	public Chart getRunTimeModel() {
		if (gcs != null) {
			return gcs.getChartModel();
		}
		return null;
	}

	public Object peerInstance() {
		// Preview canvas is used for receiving interactivity events
		return preview;
	}

	public void regenerateChart() {
		paintChart();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.IUpdateNotifier#repaintChart()
	 */
	public void repaintChart() {
		repaintChartInTimer();
	}

	/**
	 * Clear preview canvas area with white color.
	 */
	protected void clearPreviewCanvas() {
		if (isDisposedPreviewCanvas()) {
			return;
		}
		Rectangle re = preview.getClientArea();

		Rectangle adjustedRe = new Rectangle(0, 0, re.width, re.height);
		Image oldBuffer = null;

		if (buffer == null) {
			if (adjustedRe.width <= 0 || adjustedRe.height <= 0) {
				return;
			}
			buffer = new Image(Display.getDefault(), adjustedRe);
		} else {
			Rectangle ore = buffer.getBounds();

			oldBuffer = buffer;

			if (!adjustedRe.equals(ore)) {
				if (adjustedRe.width <= 0 || adjustedRe.height <= 0) {
					return;
				}
				buffer = new Image(Display.getDefault(), adjustedRe);
			}
		}

		GC gc = new GC(buffer);

		// fill default backgournd as white.
		gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		gc.fillRectangle(buffer.getBounds());

		gc.dispose();

		if (oldBuffer != null && oldBuffer != buffer) {
			oldBuffer.dispose();
		}

		if (isDisposedPreviewCanvas()) {
			return;
		}
		preview.redraw();
	}

	protected boolean isLivePreviewEnabled() {
		return wizardContext.getDataServiceProvider().isLivePreviewEnabled();
	}

}
