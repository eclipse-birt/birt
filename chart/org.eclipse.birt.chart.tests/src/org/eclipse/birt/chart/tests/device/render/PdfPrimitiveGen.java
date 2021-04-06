/***********************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.tests.device.render;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.pdf.PDFRendererImpl;
import org.eclipse.birt.chart.event.ArcRenderEvent;
import org.eclipse.birt.chart.event.AreaRenderEvent;
import org.eclipse.birt.chart.event.ClipRenderEvent;
import org.eclipse.birt.chart.event.ImageRenderEvent;
import org.eclipse.birt.chart.event.LineRenderEvent;
import org.eclipse.birt.chart.event.OvalRenderEvent;
import org.eclipse.birt.chart.event.PolygonRenderEvent;
import org.eclipse.birt.chart.event.RectangleRenderEvent;
import org.eclipse.birt.chart.event.TextRenderEvent;
import org.eclipse.birt.chart.event.TransformationEvent;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.HorizontalAlignment;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Text;
import org.eclipse.birt.chart.model.attribute.TextAlignment;
import org.eclipse.birt.chart.model.attribute.VerticalAlignment;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.FontDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.attribute.impl.LocationImpl;
import org.eclipse.birt.chart.model.attribute.impl.TextAlignmentImpl;
import org.eclipse.birt.chart.model.attribute.impl.TextImpl;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.impl.LabelImpl;

import com.ibm.icu.util.StringTokenizer;

/**
 * Concrete class that is used to parse a file with drawing primitives. Each
 * line is a specific drawing event. This class will parse the file and create
 * the appropriate drawing event and execute the event on the PDF device
 * renderer.
 */
public class PdfPrimitiveGen {
	protected PDFRendererImpl renderer;
	protected InputStream fileName;
	protected String outFile;
	protected Fill fillColor;
	protected LineAttributes lineAttr;
	protected ColorDefinition strokeColor;
	protected ColorDefinition shadowColor;
	protected FontDefinition font;

	public PdfPrimitiveGen(InputStream fileName, String outFile) {
		this.fileName = fileName;
		this.outFile = outFile;
		startup();
	}

	protected void startup() {
		renderer = new PDFRendererImpl();
		renderer.setProperty(IDeviceRenderer.FILE_IDENTIFIER, outFile);
		renderer.setProperty(IDeviceRenderer.EXPECTED_BOUNDS, BoundsImpl.create(0, 0, 500, 500));
	}

	/**
	 * reads a line from the primitive drawing file. Syntax: <primitive element>
	 * <primitive parameters>...
	 * 
	 * @throws Exception
	 */
	protected void readFile() throws Exception {
		InputStreamReader isr = new InputStreamReader(fileName);
		BufferedReader br = new BufferedReader(isr);
		String lineStr = null;
		while ((lineStr = br.readLine()) != null) {
			StringTokenizer st = new StringTokenizer(lineStr, " ");//$NON-NLS-1$
			String type = st.nextToken();
			if (type == null) {
				throw new Exception("primitive type string is null");//$NON-NLS-1$
			} else if (type.equals("font")) {//$NON-NLS-1$
				String fontName = st.nextToken();
				float size = Float.parseFloat(st.nextToken());
				boolean bold = Boolean.getBoolean(st.nextToken());
				boolean italic = Boolean.getBoolean(st.nextToken());
				boolean underline = Boolean.getBoolean(st.nextToken());
				boolean strike = Boolean.getBoolean(st.nextToken());
				boolean wordwrap = Boolean.getBoolean(st.nextToken());
				double rotation = Double.parseDouble(st.nextToken());
				TextAlignment ta = TextAlignmentImpl.create();
				try {
					ta.setHorizontalAlignment(HorizontalAlignment.get(Integer.parseInt(st.nextToken())));
				} catch (NumberFormatException e) {
				}
				try {
					ta.setVerticalAlignment(VerticalAlignment.get(Integer.parseInt(st.nextToken())));
				} catch (NumberFormatException e) {
				}
				font = FontDefinitionImpl.create(fontName, size, bold, italic, underline, strike, wordwrap, rotation,
						ta);
			}

			else if (type.equals("size")) {//$NON-NLS-1$
				renderer.setProperty(IDeviceRenderer.EXPECTED_BOUNDS,
						BoundsImpl.create(Double.parseDouble(st.nextToken()), Double.parseDouble(st.nextToken()),
								Double.parseDouble(st.nextToken()), Double.parseDouble(st.nextToken())));
			} else if (type.equals("fill")) {//$NON-NLS-1$
				fillColor = ColorDefinitionImpl.create(Integer.parseInt(st.nextToken()),
						Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()),
						Integer.parseInt(st.nextToken()));
			} else if (type.equals("lineAttr")) {//$NON-NLS-1$
				lineAttr = LineAttributesImpl.create(strokeColor, LineStyle.get(Integer.parseInt(st.nextToken())),
						Integer.parseInt(st.nextToken()));
			} else if (type.equals("stroke")) {//$NON-NLS-1$
				strokeColor = ColorDefinitionImpl.create(Integer.parseInt(st.nextToken()),
						Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()),
						Integer.parseInt(st.nextToken()));
			} else if (type.equals("shadow")) {//$NON-NLS-1$
				shadowColor = ColorDefinitionImpl.create(Integer.parseInt(st.nextToken()),
						Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
			} else if (type.startsWith("arc")) {//$NON-NLS-1$
				ArcRenderEvent are = new ArcRenderEvent(this);
				are.setBackground(fillColor);
				are.setTopLeft(
						LocationImpl.create(Double.parseDouble(st.nextToken()), Double.parseDouble(st.nextToken())));
				are.setStartAngle(Double.parseDouble(st.nextToken()));
				are.setEndAngle(Double.parseDouble(st.nextToken()));
				are.setHeight(Double.parseDouble(st.nextToken()));
				are.setWidth(Double.parseDouble(st.nextToken()));
				are.setOutline(lineAttr);
				are.setDepth(Double.parseDouble(st.nextToken()));
				if (type.endsWith("fill"))//$NON-NLS-1$
					renderer.fillArc(are);
				else
					renderer.drawArc(are);
			} else if (type.startsWith("line")) {//$NON-NLS-1$
				LineRenderEvent line = new LineRenderEvent(this);
				line.setStart(
						LocationImpl.create(Double.parseDouble(st.nextToken()), Double.parseDouble(st.nextToken())));
				line.setEnd(
						LocationImpl.create(Double.parseDouble(st.nextToken()), Double.parseDouble(st.nextToken())));
				line.setDepth(Double.parseDouble(st.nextToken()));
				line.setLineAttributes(lineAttr);
				renderer.drawLine(line);

			} else if (type.startsWith("oval")) {//$NON-NLS-1$
				OvalRenderEvent oval = new OvalRenderEvent(this);
				oval.setBackground(fillColor);
				oval.setBounds(BoundsImpl.create(Double.parseDouble(st.nextToken()), Double.parseDouble(st.nextToken()),
						Double.parseDouble(st.nextToken()), Double.parseDouble(st.nextToken())));
				oval.setOutline(lineAttr);
				oval.setDepth(Double.parseDouble(st.nextToken()));
				if (type.endsWith("fill"))//$NON-NLS-1$
					renderer.fillOval(oval);
				else
					renderer.drawOval(oval);
			} else if (type.startsWith("rect")) {//$NON-NLS-1$
				RectangleRenderEvent rect = new RectangleRenderEvent(this);
				rect.setBackground(fillColor);
				rect.setBounds(BoundsImpl.create(Double.parseDouble(st.nextToken()), Double.parseDouble(st.nextToken()),
						Double.parseDouble(st.nextToken()), Double.parseDouble(st.nextToken())));
				rect.setOutline(lineAttr);
				rect.setDepth(Double.parseDouble(st.nextToken()));
				if (type.endsWith("fill"))//$NON-NLS-1$
					renderer.fillRectangle(rect);
				else
					renderer.drawRectangle(rect);
			} else if (type.startsWith("polygon")) {//$NON-NLS-1$
				PolygonRenderEvent shape = new PolygonRenderEvent(this);
				shape.setBackground(fillColor);
				shape.setOutline(lineAttr);
				shape.setDepth(Double.parseDouble(st.nextToken()));
				int pointLength = Integer.parseInt(st.nextToken());
				Location[] locations = new Location[pointLength];
				for (int x = 0; x < pointLength; x++) {
					locations[x] = LocationImpl.create(Double.parseDouble(st.nextToken()),
							Double.parseDouble(st.nextToken()));
				}
				shape.setPoints(locations);
				if (type.endsWith("fill"))//$NON-NLS-1$
					renderer.fillPolygon(shape);
				else
					renderer.drawPolygon(shape);
			} else if (type.startsWith("text")) {//$NON-NLS-1$
				TextRenderEvent shape = new TextRenderEvent(this);
				shape.setAction(Integer.parseInt(st.nextToken()));
				switch (shape.getAction()) {
				case TextRenderEvent.RENDER_SHADOW_AT_LOCATION:
					shape.setTextPosition(Integer.parseInt(st.nextToken()));
					shape.setLocation(LocationImpl.create(Double.parseDouble(st.nextToken()),
							Double.parseDouble(st.nextToken())));
					break;

				case TextRenderEvent.RENDER_TEXT_AT_LOCATION:
					shape.setTextPosition(Integer.parseInt(st.nextToken()));
					shape.setLocation(LocationImpl.create(Double.parseDouble(st.nextToken()),
							Double.parseDouble(st.nextToken())));
					break;

				case TextRenderEvent.RENDER_TEXT_IN_BLOCK:
					shape.setBlockBounds(
							BoundsImpl.create(Double.parseDouble(st.nextToken()), Double.parseDouble(st.nextToken()),
									Double.parseDouble(st.nextToken()), Double.parseDouble(st.nextToken())));
					TextAlignment ta = TextAlignmentImpl.create();
					try {
						ta.setHorizontalAlignment(HorizontalAlignment.get(Integer.parseInt(st.nextToken())));
					} catch (NumberFormatException e) {
					}
					try {
						ta.setVerticalAlignment(VerticalAlignment.get(Integer.parseInt(st.nextToken())));
					} catch (NumberFormatException e) {
					}
					shape.setBlockAlignment(ta);
					break;
				}

				Label label = LabelImpl.create();
				label.setBackground(fillColor);
				label.setOutline(lineAttr);
				String strLabel = "";//$NON-NLS-1$
				String strDepth = "";//$NON-NLS-1$
				while (st.hasMoreTokens()) {
					strDepth = st.nextToken();
					if (st.hasMoreTokens()) {
						if (strLabel.equals(""))//$NON-NLS-1$
							strLabel = strDepth;
						else
							strLabel += " " + strDepth;//$NON-NLS-1$
					}
				}
				Text text = TextImpl.create(strLabel);
				text.setFont(font);
				text.setColor(ColorDefinitionImpl.BLACK());
				label.setCaption(text);
				if (shadowColor != null)
					label.setShadowColor(shadowColor);
				shape.setLabel(label);
				shape.setDepth(Double.parseDouble(strDepth));
				renderer.drawText(shape);
			} else if (type.equals("transform")) {//$NON-NLS-1$
				TransformationEvent trans = new TransformationEvent(this);
				trans.setTransform(Integer.parseInt(st.nextToken()));
				trans.setDepth(Double.parseDouble(st.nextToken()));
				trans.setRotation(Double.parseDouble(st.nextToken()));
				trans.setScale(Double.parseDouble(st.nextToken()));
				trans.setTranslation(Double.parseDouble(st.nextToken()), Double.parseDouble(st.nextToken()));
				renderer.applyTransformation(trans);
			} else if (type.equals("clip")) {//$NON-NLS-1$
				ClipRenderEvent clip = new ClipRenderEvent(this);
				clip.setDepth(Double.parseDouble(st.nextToken()));
				int pointLength = Integer.parseInt(st.nextToken());
				Location[] locations = new Location[pointLength];
				for (int x = 0; x < pointLength; x++) {
					locations[x] = LocationImpl.create(Double.parseDouble(st.nextToken()),
							Double.parseDouble(st.nextToken()));
				}
				clip.setVertices(locations);
				renderer.setClip(clip);
			} else if (type.startsWith("area")) {//$NON-NLS-1$
				AreaRenderEvent area = new AreaRenderEvent(this);

				area.setDepth(Double.parseDouble(st.nextToken()));
				area.setBackground(fillColor);
				area.setOutline(lineAttr);
				renderer.drawArea(area);
			}

			else if (type.equals("image")) {//$NON-NLS-1$
				ImageRenderEvent image = new ImageRenderEvent(this);

				// TODO:need to test image drawing
				renderer.drawImage(image);
			}
		}
		br.close();

	}

	public void generate() throws Exception {
		readFile();
	}

	public void flush() throws Exception {
		renderer.after();

	}

}
