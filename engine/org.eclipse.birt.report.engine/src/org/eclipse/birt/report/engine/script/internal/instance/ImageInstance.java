/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

package org.eclipse.birt.report.engine.script.internal.instance;

import org.eclipse.birt.report.engine.api.script.instance.IActionInstance;
import org.eclipse.birt.report.engine.api.script.instance.IImageInstance;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IHyperlinkAction;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.impl.ActionContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;

/**
 * A class representing the runtime state of an image
 */
public class ImageInstance extends ReportItemInstance implements IImageInstance {

	public ImageInstance(IContent image, ExecutionContext context, RunningState runningState) {
		super(image, context, runningState);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api.script.instance.IImageInstance#getAltText(
	 * )
	 */
	public String getAltText() {
		return ((IImageContent) content).getAltText();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.IImageInstance#setAltText(java.lang.String)
	 */
	public void setAltText(String altText) {
		((IImageContent) content).setAltText(altText);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.IImageInstance#
	 * getAltTextKey()
	 */
	public String getAltTextKey() {
		return ((IImageContent) content).getAltTextKey();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.IImageInstance#
	 * setAltTextKey(java.lang.String)
	 */
	public void setAltTextKey(String altTextKey) {
		((IImageContent) content).setAltTextKey(altTextKey);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api.script.instance.IImageInstance#getURI()
	 */
	public String getURI() {
		return ((IImageContent) content).getURI();
	}

	/**
	 * @deprecated (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.IImageInstance#setURI(java.lang.String)
	 */
	public void setURI(String uri) {
		((IImageContent) content).setURI(uri);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.IImageInstance#
	 * getImageSource()
	 */
	public int getImageSource() {
		return ((IImageContent) content).getImageSource();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.IImageInstance#
	 * getImageName()
	 */
	public String getImageName() {
		return ((IImageContent) content).getURI();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.IImageInstance#
	 * setImageName(java.lang.String)
	 */
	public void setImageName(String imageName) {
		((IImageContent) content).setImageSource(IImageContent.IMAGE_NAME);
		((IImageContent) content).setURI(imageName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api.script.instance.IImageInstance#getData( )
	 */
	public byte[] getData() {
		return ((IImageContent) content).getData();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api.script.instance.IImageInstance#setData(
	 * byte[])
	 */
	public void setData(byte[] data) {
		((IImageContent) content).setData(data);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api.script.instance.IImageInstance#getMimeType
	 * ()
	 */
	public String getMimeType() {
		return ((IImageContent) content).getMIMEType();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api.script.instance.IImageInstance#setMimeType
	 * (java.lang.String)
	 */
	public void setMimeType(String type) {
		((IImageContent) content).setMIMEType(type);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#setFile()
	 */
	public void setFile(String file) {
		((IImageContent) content).setImageSource(IImageContent.IMAGE_FILE);
		((IImageContent) content).setURI(file);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#getFile()
	 */
	public String getFile() {
		if (((IImageContent) content).getImageSource() == IImageContent.IMAGE_FILE) {
			return ((IImageContent) content).getURI();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#setURL()
	 */
	public void setURL(String url) {
		((IImageContent) content).setImageSource(IImageContent.IMAGE_URL);
		((IImageContent) content).setURI(url);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IImage#getURL()
	 */
	public String getURL() {
		if (((IImageContent) content).getImageSource() == IImageContent.IMAGE_URL) {
			return ((IImageContent) content).getURI();
		}
		return null;
	}

	private IActionInstance actionInstance;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.IReportInstance#
	 * createHyperlinkActionInstance( )
	 */
	public IActionInstance createAction() {
		IHyperlinkAction hyperlink = new ActionContent();
		return new ActionInstance(hyperlink);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.IReportInstance#
	 * getHyperlinkInstance( )
	 */
	public IActionInstance getAction() {
		IHyperlinkAction hyperlink = content.getHyperlinkAction();
		if (hyperlink != null) {
			if (actionInstance == null) {
				actionInstance = new ActionInstance(hyperlink);
			}
		}
		return actionInstance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.instance.IReportInstance#
	 * setActionInstance(org.eclipse.birt.report.engine.api.script.instance.
	 * IActionInstance )
	 */
	public void setAction(IActionInstance actionInstance) {
		if (actionInstance == null) {
			content.setHyperlinkAction(null);
		} else if (actionInstance instanceof ActionInstance) {
			content.setHyperlinkAction(((ActionInstance) actionInstance).getHyperlinkAction());
		}
		this.actionInstance = actionInstance;
	}
}
