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
package org.eclipse.birt.chart.examples.api.preference;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;

import javax.imageio.ImageIO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.util.PluginSettings;

public class PreferenceServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private transient Chart cm = null;

	private String fontName = null;

	private float size = (float) 0.0;

	private boolean bBold = true;

	private boolean bItalic = false;

	private transient ColorDefinition cd = null;

	private transient IDeviceRenderer idr = null;

	/**
	 * Deploy the render device.
	 */
	public PreferenceServlet() {
		final PluginSettings ps = PluginSettings.instance();
		try {
			idr = ps.getDevice("dv.SWING");//$NON-NLS-1$
		} catch (ChartException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if (session == null) {
			response.sendRedirect("http://localhost:8080/error.html"); //$NON-NLS-1$
		}

		cm = ChartModels.createBarChart();

		Enumeration en = request.getParameterNames();
		while (en.hasMoreElements()) {
			String name = (String) en.nextElement();
			String value = request.getParameterValues(name)[0];

			if (name.equals("fonts"))//$NON-NLS-1$
			{
				fontName = value;
			} else if (name.equals("style"))//$NON-NLS-1$
			{
				if (value.equals("Bold"))//$NON-NLS-1$
				{
					bBold = true;
					bItalic = false;
				} else if (value.equals("Italic"))//$NON-NLS-1$
				{
					bBold = false;
					bItalic = true;
				}
			} else if (name.equals("size"))//$NON-NLS-1$
			{
				size = Float.parseFloat(value);
			} else if (name.equals("color"))//$NON-NLS-1$
			{
				if (value.equals("Black")) //$NON-NLS-1$
				{
					cd = ColorDefinitionImpl.BLACK();
				} else if (value.equals("Red")) //$NON-NLS-1$
				{
					cd = ColorDefinitionImpl.RED();
				} else if (value.equals("Blue")) //$NON-NLS-1$
				{
					cd = ColorDefinitionImpl.BLUE();
				}
			}

		}

		response.setHeader("Cache-Control", "no-store"); //$NON-NLS-1$//$NON-NLS-2$
		response.setDateHeader("Expires", 0); //$NON-NLS-1$

		// Set the Content-Type header for the image output
		response.setContentType("image/jpeg"); //$NON-NLS-1$
		createImage((OutputStream) response.getOutputStream());
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * Create the chart image
	 *
	 * @param out OutputStream
	 */
	private void createImage(OutputStream out) {
		// Create a buffered image
		BufferedImage image = new BufferedImage(600, 400, BufferedImage.TYPE_INT_RGB);

		// Draw the chart in the buffered image
		Graphics2D g2d = (Graphics2D) image.getGraphics();
		idr.setProperty(IDeviceRenderer.GRAPHICS_CONTEXT, g2d);

		Bounds bo = BoundsImpl.create(0, 0, 600, 400);
		bo.scale(72d / idr.getDisplayServer().getDpiResolution());

		Generator gr = Generator.instance();

		try {
			gr.render(idr, gr.build(idr.getDisplayServer(), cm, bo, null, null,
					new LabelStyleProcessor(fontName, size, bBold, bItalic, cd)));
		} catch (ChartException ex) {
			ex.printStackTrace();
		}

		g2d.dispose();

		// Output the image
		try {
			ImageIO.write(image, "JPEG", out); //$NON-NLS-1$
		} catch (IOException io) {
			io.printStackTrace();
		}

	}
}
