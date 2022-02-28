/***********************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.nLayout.area.impl;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.birt.report.engine.content.IHyperlinkAction;
import org.eclipse.birt.report.engine.nLayout.area.IAreaVisitor;
import org.eclipse.birt.report.engine.nLayout.area.IImageArea;

public class ImageArea extends AbstractArea implements IImageArea {

	protected String url;

	protected byte[] data;

	protected String extension;

	protected String helpText;

	protected String mimetype;

	protected HashMap<String, String> params;

	protected ArrayList<IImageMap> imageMapDescription;

	public ImageArea() {
		super();
	}

	public ImageArea(ImageArea area) {
		super(area);
		this.url = area.getImageUrl();
		this.data = area.getImageData();
		this.extension = area.extension;
		this.helpText = area.helpText;
		this.mimetype = area.mimetype;
		this.params = area.params;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	@Override
	public void accept(IAreaVisitor visitor) {
		visitor.visitImage(this);
	}

	@Override
	public byte[] getImageData() {
		return data;
	}

	@Override
	public String getImageUrl() {
		return url;
	}

	@Override
	public AbstractArea cloneArea() {
		return new ImageArea(this);
	}

	@Override
	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	@Override
	public String getHelpText() {
		return helpText;
	}

	public void setHelpText(String helpText) {
		this.helpText = helpText;
	}

	@Override
	public String getMIMEType() {
		return mimetype;
	}

	public void setMIMEType(String mimetype) {
		this.mimetype = mimetype;
	}

	@Override
	public HashMap<String, String> getParameters() {
		return params;
	}

	public void setParameters(HashMap<String, String> params) {
		this.params = params;
	}

	@Override
	public void addImageMap(int[] peak, IHyperlinkAction action) {
		if (imageMapDescription == null) {
			imageMapDescription = new ArrayList<>();
		}
		ImageMap map = new ImageMap(peak, action);
		imageMapDescription.add(map);
	}

	@Override
	public ArrayList<IImageMap> getImageMapDescription() {
		return imageMapDescription;
	}

	static class ImageMap implements IImageMap {
		int[] vertices;
		IHyperlinkAction action;

		public ImageMap(int[] vertices, IHyperlinkAction action) {
			this.vertices = vertices;
			this.action = action;
		}

		@Override
		public int[] getVertices() {
			return vertices;
		}

		@Override
		public IHyperlinkAction getAction() {
			return action;
		}

	}

}
