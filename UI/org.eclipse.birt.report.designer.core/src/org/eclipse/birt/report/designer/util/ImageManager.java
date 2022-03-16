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

package org.eclipse.birt.report.designer.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.eclipse.birt.report.designer.core.CorePlugin;
import org.eclipse.birt.report.designer.core.DesignerConstants;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.model.api.util.URIUtil;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;

/**
 * Manages all image resources.
 */

public class ImageManager {

	private static final String EMBEDDED_SUFFIX = ".Embedded."; //$NON-NLS-1$

	private static final ImageManager instance = new ImageManager();

	private List invalidUrlList = new ArrayList();

	private String resourcesRootPath = "";

	private ImageManager() {
	}

	/**
	 * Gets the instance of the image manager
	 *
	 * @return Returns the instanceof the image manager
	 */
	public static ImageManager getInstance() {
		return instance;
	}

	/**
	 * Gets the image by the given ModuleHandle and URI
	 *
	 * @param handle
	 * @param uri
	 * @return
	 */
	public Image getImage(ModuleHandle handle, String uri) {
		return getImage(handle, uri, false);
	}

	/**
	 * Gets the image by the given ModuleHandle and URI
	 *
	 * @param handle
	 * @param uri
	 * @param refresh
	 * @return
	 */
	public Image getImage(ModuleHandle handle, String uri, boolean refresh) {
		Image image = null;
		URL url = null;

		try {
			url = generateURL(handle, uri);
			image = getImageFromURL(url, refresh);
		} catch (Exception e) {
			if (url != null && !invalidUrlList.contains(url.toString())) {
				invalidUrlList.add(url.toString());
			}
		}
		return image;
	}

	private Image getImageFromURL(URL url, boolean refresh) throws IOException {
		if ((url == null) || (!refresh && invalidUrlList.contains(url.toString()))) {
			return null;
		}
		String key = url.toString();
		Image image = getImageRegistry().get(key);
		if (image == null) {
			image = loadImage(url);
		}
		if (image == null) {
			if (!invalidUrlList.contains(url.toString())) {
				invalidUrlList.add(url.toString());
			}
		} else {
			invalidUrlList.remove(url.toString());
		}
		return image;
	}

	/**
	 * Gets the image by the given URI
	 *
	 * @param uri the url of the image file
	 *
	 * @return Returns the image,or null if the url is invalid or the file format is
	 *         unsupported.
	 */
	public Image getImage(String uri, boolean refresh) {
		return getImage(null, uri, refresh);
	}

	/**
	 * Get image from URI
	 *
	 * @param uri URI
	 * @return The image gotten
	 */
	public Image getImage(String uri) {
		return getImage(null, uri, false);
	}

	/**
	 * Gets the embedded image
	 *
	 * @param embeddedImage the embedded image data
	 *
	 * @return Returns the image,or null if the embedded image doesn't exist.
	 */
	public Image getEmbeddedImage(ModuleHandle handle, String name) {
		String key = generateKey(handle, name);
		EmbeddedImage embeddedImage = handle.findImage(name);
		if (embeddedImage == null) {
			removeCachedImage(key);
			return null;
		}
		Image image = getImageRegistry().get(key);
		if (image != null) {
			return image;
		}

		InputStream in = null;
		try {
			if (key.toLowerCase().endsWith(".svg")) //$NON-NLS-1$
			{
				// convert svg image to JPEG image bytes
				JPEGTranscoder transcoder = new JPEGTranscoder();
				// set the transcoding hints
				transcoder.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, new Float(.8));
				// create the transcoder input
				TranscoderInput input = new TranscoderInput(
						new ByteArrayInputStream(embeddedImage.getData(handle.getModule())));
				// For embedded image we have't a file URI, so set handle
				// filename as URI.
				// See Bugzilla Bug 167395
				input.setURI(generateURL(handle, handle.getFileName()).toString());
				// create the transcoder output
				ByteArrayOutputStream ostream = new ByteArrayOutputStream();
				TranscoderOutput output = new TranscoderOutput(ostream);
				try {
					transcoder.transcode(input, output);
				} catch (TranscoderException e) {
				}
				// flush the stream
				ostream.flush();
				// use the outputstream as Image input stream.
				in = new ByteArrayInputStream(ostream.toByteArray());
			} else {
				in = new ByteArrayInputStream(embeddedImage.getData(handle.getModule()));
			}
			ImageData[] datas = new ImageLoader().load(in);
			if (datas != null && datas.length != 0) {
				ImageData cur;
				// if (datas.length == 1)
				// {
				// cur = datas[0];
				// }
				int index = 0;
				for (int i = 0; i < datas.length; i++) {
					ImageData temp = datas[i];
					if (temp.width * temp.height > datas[index].width * datas[index].height) {
						index = i;
					}
				}
				cur = datas[index];
				image = new Image(null, cur);
			}
			// image = new Image( null, in );
		} catch (Exception e) {
			// do nothing now
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}
		if (image != null) {
			getImageRegistry().put(key, image);
			if (DesignerConstants.TRACING_IMAGE_MANAGER_IMAGE_ADD) {
				System.out.println("Image Manager >> " + key + " added"); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
		return image;
	}

	/**
	 * Remove cached image from map
	 *
	 * @param key The key of map.
	 */
	public void removeCachedImage(String key) {
		getImageRegistry().remove(key);
		if (DesignerConstants.TRACING_IMAGE_MANAGER_IMAGE_REMOVE) {
			System.out.println("Image Manager >> " + key + " removed"); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * Loads the image into the image registry by the given URI
	 *
	 * @param uri the URI of the image to load
	 * @return Returns the image if it loaded correctly
	 * @throws IOException
	 */
	public Image loadImage(ModuleHandle designHandle, String uri) throws IOException {
		URL url = generateURL(designHandle, uri);
		if (url == null) {
			throw new FileNotFoundException(uri);
		}
		return loadImage(url);
	}

	/**
	 * Reload the image, refresh the cache.
	 *
	 * @param designHandle
	 * @param uri
	 * @return
	 * @throws IOException
	 */
	public Image rloadImage(ModuleHandle designHandle, String uri) throws IOException {
		URL url = generateURL(designHandle, uri);
		if (url == null) {
			throw new FileNotFoundException(uri);
		}
		return loadImage(url, true);
	}

	public Image loadImage(String uri) throws IOException {
		return loadImage(null, uri);
	}

	public Image loadImage(URL url) throws IOException {
		return loadImage(url, false);
	}

	/**
	 * @param url
	 * @param reload
	 * @return
	 * @throws IOException
	 */
	public Image loadImage(URL url, boolean reload) throws IOException {
		String key = url.toString();
		Image image = null;
		if (!reload) {
			image = getImageRegistry().get(key);
			if (image != null) {
				return image;
			}
		} else {
			removeCachedImage(key);
		}
		InputStream in = null;

		try {
			if (url.toString().toLowerCase().endsWith(".svg")) //$NON-NLS-1$
			{
				// convert svg image to JPEG image bytes
				JPEGTranscoder transcoder = new JPEGTranscoder();
				// set the transcoding hints
				transcoder.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, new Float(.8));
				// create the transcoder input
				String svgURI = url.toString();
				TranscoderInput input = new TranscoderInput(svgURI);
				// create the transcoder output
				ByteArrayOutputStream ostream = new ByteArrayOutputStream();
				TranscoderOutput output = new TranscoderOutput(ostream);
				try {
					transcoder.transcode(input, output);
				} catch (TranscoderException e) {
				}
				// flush the stream
				ostream.flush();
				// use the outputstream as Image input stream.
				in = new ByteArrayInputStream(ostream.toByteArray());
			} else {
				in = url.openStream();
			}
			ImageData[] datas = new ImageLoader().load(in);
			if (datas != null && datas.length != 0) {
				ImageData cur;
				// if (datas.length == 1)
				// {
				// cur = datas[0];
				// }
				int index = 0;
				for (int i = 0; i < datas.length; i++) {
					ImageData temp = datas[i];
					if (temp.width * temp.height > datas[index].width * datas[index].height) {
						index = i;
					}
				}
				cur = datas[index];
				image = new Image(null, cur);
			}
		} catch (IOException e) {
			throw e;
		} catch (Exception ee) {
			// do nothing
		} finally {
			if (in != null) {
				in.close();
			}
		}
		if (image != null) {
			getImageRegistry().put(key, image);
		}
		return image;
	}

	private ImageRegistry getImageRegistry() {
		return CorePlugin.getDefault().getImageRegistry();
	}

	public URL generateURL(ModuleHandle designHandle, String uri) throws MalformedURLException {
		try {
			return new URL(uri);
		} catch (MalformedURLException e) {
			String path = URIUtil.getLocalPath(uri);

			if (designHandle == null) {
				designHandle = SessionHandleAdapter.getInstance().getReportDesignHandle();
			}

			if (path != null && designHandle != null) {
				// add by gao for lib
				return designHandle.findResource(path, IResourceLocator.IMAGE);
			}
			return URI.create(uri).toURL();
		}
	}

	/**
	 * Generate hash key.
	 *
	 * @param reportDesignHandle Moudle handle
	 * @param name               Name
	 * @return key string
	 */
	public String generateKey(ModuleHandle reportDesignHandle, String name) {
		return reportDesignHandle.hashCode() + EMBEDDED_SUFFIX + name;
	}

	/**
	 * Reload the URI image, refresh the cache.
	 *
	 * @param moduleHandel
	 * @param uri
	 * @return
	 */
	public Image reloadURIImage(ModuleHandle moduleHandel, String uri) {
		URL url = createURIURL(uri);
		if (url == null) {
			return null;
		}

		Image image = null;
		try {
			image = loadImage(url, true);
		} catch (IOException e) {
			// do nothing
		}

		if (image == null) {
			if (!invalidUrlList.contains(url.toString())) {
				invalidUrlList.add(url.toString());
			}
		} else {
			invalidUrlList.remove(url.toString());
		}
		return image;
	}

	public URL createURIURL(String uri) {
		URL url = null;
		try {
			url = new URL(uri);
		} catch (MalformedURLException e) {
			String path = URIUtil.getLocalPath(uri);
			try {
				url = URI.create(uri).toURL();
			} catch (Exception e1) {
				path = URIUtil.resolveAbsolutePath(this.resourcesRootPath, uri);
				try {
					url = new File(path).toURI().toURL();
				} catch (Exception e2) {
				}
			}
		}

		return url;
	}

	/**
	 * Get image from URI
	 *
	 * @param moduleHandel
	 * @param uri
	 * @return
	 */
	// bugzilla 245641
	public Image getURIImage(ModuleHandle moduleHandel, String uri) {
		URL url = createURIURL(uri);
		Image image = null;
		try {
			image = getImageFromURL(url, false);
		} catch (Exception e) {
			if (url != null && !invalidUrlList.contains(url.toString())) {
				invalidUrlList.add(url.toString());
			}
		}
		return image;
	}

	public void setURIRootPath(String rootPath) {
		this.resourcesRootPath = rootPath;
	}

}
