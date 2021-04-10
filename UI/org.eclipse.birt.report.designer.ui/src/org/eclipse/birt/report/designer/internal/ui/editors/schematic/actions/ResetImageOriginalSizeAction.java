/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.eclipse.birt.report.designer.core.model.schematic.HandleAdapterFactory;
import org.eclipse.birt.report.designer.core.model.schematic.ImageHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.ImageManager;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.util.DimensionUtil;
import org.eclipse.birt.report.model.api.util.URIUtil;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.graphics.Image;

/**
 * 
 */

public class ResetImageOriginalSizeAction extends Action {

	public static final int BYORIGINAL = 0;
	public static final int BYIMAGEDPI = 1;
	public static final int BYREPORTDPI = 2;
	public static final int BYSCREENDPI = 3;

	private ImageHandle imageHandle;
	private String displayName;
	private int type;

	public ResetImageOriginalSizeAction(ImageHandle imageHandle, String displayName, int type) {
		super();
		this.imageHandle = imageHandle;
		this.displayName = displayName;
		this.type = type;
		setText(displayName);
	}

	@Override
	public boolean isEnabled() {
		if (getImage() == null) {
			return false;
		}

		if (type == BYSCREENDPI || type == BYORIGINAL) {
			return true;
		} else if (type == BYREPORTDPI) {
			ModuleHandle handle = imageHandle.getModuleHandle();
			if (handle instanceof ReportDesignHandle) {
				return ((ReportDesignHandle) handle).getImageDPI() != 0;
			} else {
				return false;
			}
		} else if (type == BYIMAGEDPI) {
			return getImageDPI() != 0;
		}
		return false;
	}

	private int getImageDPI() {
		InputStream in = null;
		URL temp = null;
		String imageSource = imageHandle.getSource();
		String url = imageHandle.getURI();
		if (DesignChoiceConstants.IMAGE_REF_TYPE_EXPR.equalsIgnoreCase(imageSource)) {
			// No image now
			return 0;
		} else if (DesignChoiceConstants.IMAGE_REF_TYPE_EMBED.equalsIgnoreCase(imageSource)) {
			EmbeddedImage embeddedImage = imageHandle.getModuleHandle().findImage(imageHandle.getImageName());
			if (embeddedImage == null) {
				return 0;
			}
			in = new ByteArrayInputStream(embeddedImage.getData(imageHandle.getModule()));
		} else if (DesignChoiceConstants.IMAGE_REF_TYPE_URL.equalsIgnoreCase(imageSource)) {
			temp = ImageManager.getInstance().createURIURL(url);
			try {
				in = temp.openStream();
			} catch (IOException e) {
				in = null;
			}
		} else if (DesignChoiceConstants.IMAGE_REF_TYPE_FILE.equalsIgnoreCase(imageSource)) {
			try {
				if (URIUtil.isValidResourcePath(url)) {
					temp = ImageManager.getInstance().generateURL(imageHandle.getModuleHandle(),
							URIUtil.getLocalPath(url));

				} else {
					temp = ImageManager.getInstance().generateURL(imageHandle.getModuleHandle(), url);
				}

				in = temp.openStream();
			} catch (IOException e) {
				in = null;
			}
		}

		int dpi = UIUtil.getImageResolution(in)[0];
		if (in != null) {
			try {
				in.close();
			} catch (IOException e) {
				ExceptionHandler.handle(e);
			}
		}
		return dpi;
	}

	private Image getImage() {
		ImageHandleAdapter adapter = HandleAdapterFactory.getInstance().getImageHandleAdapter(imageHandle);
		return adapter.getImage();
	}

	@Override
	public void run() {
		CommandStack stack = imageHandle.getModuleHandle().getCommandStack();

		stack.startTrans(Messages.getString("ResetImageOriginalSizeAction.trans.label")); //$NON-NLS-1$
		String defaultUnit = imageHandle.getModuleHandle().getDefaultUnits();
		Image image = getImage();
		int width = image.getBounds().width;

		int height = image.getBounds().height;
		// String url = imageHandle.getURI( );
		try {
			if (type == BYORIGINAL) {

				imageHandle.setWidth(width + DesignChoiceConstants.UNITS_PX);

				imageHandle.setHeight(height + DesignChoiceConstants.UNITS_PX);
			} else {
				int dpi = 0;
				if (type == BYSCREENDPI) {
					dpi = UIUtil.getScreenResolution()[0];
				} else if (type == BYREPORTDPI) {
					dpi = ((ReportDesignHandle) imageHandle.getModuleHandle()).getImageDPI();
				} else if (type == BYIMAGEDPI) {
					dpi = getImageDPI();
				}
				double inch = ((double) width) / dpi;

				DimensionValue value = DimensionUtil.convertTo(inch, DesignChoiceConstants.UNITS_IN, defaultUnit);
				imageHandle.getWidth().setValue(value);

				inch = ((double) height) / dpi;
				value = DimensionUtil.convertTo(inch, DesignChoiceConstants.UNITS_IN, defaultUnit);
				imageHandle.getHeight().setValue(value);
			}
		} catch (SemanticException e) {
			stack.rollbackAll();
			ExceptionHandler.handle(e);
			return;
		}

		stack.commit();
	}
}
