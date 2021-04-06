/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.ui.swt.composites;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.event.PolygonRenderEvent;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.EmbeddedImage;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.Gradient;
import org.eclipse.birt.chart.model.attribute.Image;
import org.eclipse.birt.chart.model.attribute.ImageSourceType;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.MultipleFill;
import org.eclipse.birt.chart.model.attribute.PatternImage;
import org.eclipse.birt.chart.model.attribute.impl.LocationImpl;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.interfaces.IImageServiceProvider;
import org.eclipse.birt.chart.util.PatternImageUtil;
import org.eclipse.birt.chart.util.PatternImageUtil.ByteColorModel;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Pattern;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * @author Administrator
 * 
 */
public class FillCanvas extends Canvas implements PaintListener, DisposeListener {

	private static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.ui.extension/swt.composites"); //$NON-NLS-1$

	protected Fill fCurrent = null;

	private boolean isAutoEnabled = false;

	private IDeviceRenderer idr;

	private int textIndent = 0;

	private IImageServiceProvider imageServiceProvider = null;

	public FillCanvas(Composite parent, int iStyle) {
		super(parent, iStyle);
		this.addPaintListener(this);
		this.addDisposeListener(this);

		try {
			idr = PluginSettings.instance().getDevice("dv.SWT"); //$NON-NLS-1$
		} catch (ChartException pex) {
			idr = null;
			WizardBase.displayException(pex);
		}
	}

	/**
	 * 
	 * @param parent
	 * @param iStyle
	 * @param isAutoEnabled If true, null color means auto, rather than transparent
	 */
	public FillCanvas(Composite parent, int iStyle, boolean isAutoEnabled) {
		this(parent, iStyle);
		this.isAutoEnabled = isAutoEnabled;
	}

	/**
	 * If true, null color means auto, rather than transparent
	 * 
	 * @param parent
	 * @param iStyle
	 * @param isAutoEnabled
	 * @param context
	 */
	public FillCanvas(Composite parent, int iStyle, boolean isAutoEnabled, IImageServiceProvider imageServiceProvider) {
		this(parent, iStyle);
		this.isAutoEnabled = isAutoEnabled;
		this.imageServiceProvider = imageServiceProvider;
	}

	public void setTextIndent(int indent) {
		textIndent = indent;
	}

	public void setFill(Fill fill) {
		this.fCurrent = fill;
	}

	public void paintControl(PaintEvent pe) {
		Color cBackground = null;

		try {
			Color clrTransparencyBackground = Display.getCurrent().getSystemColor(SWT.COLOR_LIST_BACKGROUND);
			GC gc = pe.gc;
			if (!this.isEnabled()) {
				// Disabled control
				gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
				Color cFore = Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GRAY);
				gc.setForeground(cFore);
				if (fCurrent == null
						|| fCurrent instanceof ColorDefinition && ((ColorDefinition) fCurrent).getTransparency() == 0) {
					gc.fillRectangle(0, 0, this.getSize().x, this.getSize().y);
					if (!isAutoEnabled || fCurrent != null) {
						gc.drawText(Messages.getString("FillCanvas.Transparent"), 2 + textIndent, 2); //$NON-NLS-1$
					} else {
						gc.drawText(Messages.getString("FillCanvas.Auto"), 2 + textIndent, 2); //$NON-NLS-1$
					}
				} else {
					gc.fillRectangle(0, 0, this.getSize().x, this.getSize().y);
					gc.setBackground(cFore);
					gc.fillRectangle(2, 2, this.getSize().x - 4, this.getSize().y - 4);
				}
			} else {
				// Enabled control
				if (fCurrent == null
						|| fCurrent instanceof ColorDefinition && ((ColorDefinition) fCurrent).getTransparency() == 0) {
					gc.setBackground(clrTransparencyBackground);
					gc.fillRectangle(0, 0, this.getSize().x, this.getSize().y);
					Color cText = Display.getDefault().getSystemColor(SWT.COLOR_LIST_FOREGROUND);
					gc.setForeground(cText);
					if (!isAutoEnabled || fCurrent != null) {
						gc.drawText(Messages.getString("FillCanvas.Transparent"), 2 + textIndent, 2); //$NON-NLS-1$
					} else {
						gc.drawText(Messages.getString("FillCanvas.Auto"), 2 + textIndent, 2); //$NON-NLS-1$
					}
					cText.dispose();
				} else {
					if (fCurrent instanceof ColorDefinition) {
						cBackground = new Color(Display.getDefault(), ((ColorDefinition) fCurrent).getRed(),
								((ColorDefinition) fCurrent).getGreen(), ((ColorDefinition) fCurrent).getBlue());
						gc.setBackground(cBackground);
						gc.fillRectangle(2, 2, this.getSize().x - 4, this.getSize().y - 4);
					} else if (fCurrent instanceof Image) {
						org.eclipse.swt.graphics.Image img = getSWTImage((Image) fCurrent);
						if (fCurrent instanceof PatternImage) {
							Pattern ptn = new Pattern(Display.getCurrent(), img);
							gc.setBackgroundPattern(ptn);
							gc.fillRectangle(2, 2, getSize().x - 4, this.getSize().y - 4);
							ptn.dispose();
						} else {
							gc.fillRectangle(2, 2, getSize().x - 4, this.getSize().y - 4);
							gc.drawImage(img, 2, 2);
						}

						if (img != null) {
							img.dispose();
						}
					} else if (fCurrent instanceof Gradient) {
						fillGradient(gc);
					} else if (fCurrent instanceof MultipleFill) {
						fillMultiFill(gc);
					}
				}

				// Render a boundary line to indicate focus
				if (isFocusControl()) {
					gc.setLineStyle(SWT.LINE_DOT);
					gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
					gc.drawRectangle(1, 1, getSize().x - 3, this.getSize().y - 3);
				}
			}
		} catch (Exception ex) {
			logger.log(ex);
		} finally {
			if (cBackground != null) {
				cBackground.dispose();
			}
		}
	}

	private void fillPolygonWithIdr(GC gc, Fill fill, Location[] la) {
		idr.setProperty(IDeviceRenderer.GRAPHICS_CONTEXT, gc);
		PolygonRenderEvent event = new PolygonRenderEvent(this);
		event.setPoints(la);
		event.setBackground(fill);
		try {
			idr.fillPolygon(event);
		} catch (ChartException e) {

		}
	}

	/**
	 * Fill gradient.
	 * 
	 * @param gc
	 */
	protected void fillGradient(GC gc) {
		Location[] la = LocationImpl.create(new double[] { 2, 2, this.getSize().x - 2, this.getSize().x - 2 },
				new double[] { 2, this.getSize().y - 2, this.getSize().y - 2, 2 });

		fillPolygonWithIdr(gc, fCurrent, la);
	}

	/**
	 * Fill gradient.
	 * 
	 * @param gc
	 */
	private void fillMultiFill(GC gc) {
		MultipleFill mFill = (MultipleFill) fCurrent;

		double width = this.getSize().x - 4;
		double height = this.getSize().y - 4;

		Location[] la = LocationImpl.create(new double[] { 2, 2, width / 2 + 2, width / 2 + 2 },
				new double[] { 2, height + 2, height + 2, 2 });

		fillPolygonWithIdr(gc, mFill.getFills().get(0), la);

		la = LocationImpl.create(new double[] { width / 2 + 2, width / 2 + 2, width + 2, width + 2 },
				new double[] { 2, height + 2, height + 2, 2 });

		fillPolygonWithIdr(gc, mFill.getFills().get(1), la);
	}

	private org.eclipse.swt.graphics.Image getSWTImage(Image modelImage) {
		org.eclipse.swt.graphics.Image img = null;
		try {
			if (modelImage instanceof EmbeddedImage) {
				String imageData = ((EmbeddedImage) modelImage).getData();
				if (imageData != null) {
					ByteArrayInputStream bis = new ByteArrayInputStream(Base64.decodeBase64(imageData.getBytes()));
					img = new org.eclipse.swt.graphics.Image(Display.getDefault(), bis);
				} else {
					// To render a blank image for null embedded data
					img = new org.eclipse.swt.graphics.Image(Display.getDefault(), 10, 10);
				}
			} else if (modelImage instanceof PatternImage) {
				PatternImage patternImage = (PatternImage) modelImage;
				Device device = Display.getCurrent();

				PaletteData paletteData = new PaletteData(0xFF00, 0xFF0000, 0xFF000000);
				byte[] data = PatternImageUtil.createImageData(patternImage, ByteColorModel.BGRA);

				ImageData imageData = new ImageData(8, 8, 32, paletteData, 4, data);
				img = new org.eclipse.swt.graphics.Image(device, imageData);
			} else {
				if (imageServiceProvider == null) {
					try {
						img = new org.eclipse.swt.graphics.Image(Display.getCurrent(),
								new URL(modelImage.getURL()).openStream());
					} catch (MalformedURLException e1) {
						img = new org.eclipse.swt.graphics.Image(Display.getCurrent(),
								new FileInputStream(modelImage.getURL()));
					}
				} else if (modelImage.getSource() == ImageSourceType.REPORT) {
					org.eclipse.swt.graphics.Image embeddedImage = imageServiceProvider
							.getEmbeddedImage(modelImage.getURL());
					if (embeddedImage != null) {
						ImageData imageData = (ImageData) embeddedImage.getImageData().clone();
						img = new org.eclipse.swt.graphics.Image(Display.getCurrent(), imageData);
						return img;
					}
				} else {
					String url = imageServiceProvider.getImageAbsoluteURL(modelImage);
					if (url != null) {
						try {
							img = new org.eclipse.swt.graphics.Image(Display.getCurrent(), new URL(url).openStream());
						} catch (MalformedURLException e1) {
							img = new org.eclipse.swt.graphics.Image(Display.getCurrent(), new FileInputStream(url));
						}
					}
				}
			}
		} catch (FileNotFoundException ex) {
			logger.log(ex);
			ex.printStackTrace();
		} catch (IOException ex) {
			logger.log(ex);
			ex.printStackTrace();
		}
		return img;
	}

	@Override
	public void setEnabled(boolean bState) {
		super.setEnabled(bState);
		redraw();
	}

	public void widgetDisposed(DisposeEvent e) {
		if (idr != null) {
			idr.dispose();
			idr = null;
		}
	}
}