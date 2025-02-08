/*******************************************************************************
 * Copyright (c) 2004, 2025 Actuate Corporation and others
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
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.List;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
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

	private static final String URL_IMAGE_TYPE_SVG = "image/svg+xml";

	private static final String URL_PROTOCOL_TYPE_DATA = "data:";

	private static final String URL_PROTOCOL_TYPE_DATA_BASE = ";base64,";

	private static final String URL_PROTOCOL_TYPE_DATA_UTF8 = ";utf8,";

	private static final ImageManager instance = new ImageManager();

	private List<String> invalidUrlList = new ArrayList<String>();

	private String resourcesRootPath = "";

	private ImageManager() {
	}

	/**
	 * Gets the instance of the image manager
	 *
	 * @return Returns the instance of the image manager
	 */
	public static ImageManager getInstance() {
		return instance;
	}

	/**
	 * Gets the image by the given ModuleHandle and URI
	 *
	 * @param handle
	 * @param uri
	 * @return Return the image
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
	 * @return Return the image
	 */
	public Image getImage(ModuleHandle handle, String uri, boolean refresh) {
		Image image = null;
		URL url = null;

		try {
			if (uri.contains(URL_PROTOCOL_TYPE_DATA)) {
				image = getEmbeddedImageDataURL(uri, refresh);
			} else {
				url = generateURL(handle, uri);
				image = getImageFromURL(url, refresh);
			}
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
		Image image = null;

		image = getImageRegistry().get(key);
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
	 * @param uri     the uri of the image file
	 * @param refresh mark if refresh necessary
	 *
	 * @return Returns the image or null if the uri is invalid or the file format is
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
	 * @param handle handel of the design
	 * @param name   name the image
	 *
	 * @return Returns the image or null if the embedded image doesn't exist.
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
			if (key.toLowerCase().endsWith(".svg") //$NON-NLS-1$
					|| embeddedImage.getType(handle.getModule()) != null
							&& embeddedImage.getType(handle.getModule()).equalsIgnoreCase(URL_IMAGE_TYPE_SVG))
			{
				// convert svg image to JPEG image bytes
				JPEGTranscoder jpegTranscoder = new JPEGTranscoder();
				// set the transcoding hints
				jpegTranscoder.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, .8f);
				// create the transcoder input
				ByteArrayInputStream reader = new ByteArrayInputStream(embeddedImage.getData(handle.getModule()));
				TranscoderInput input = new TranscoderInput(reader);
				// For embedded image we have't a file URI, so set handle
				// filename as URI.
				// See Bugzilla Bug 167395
				input.setURI(generateURL(handle, handle.getFileName()).toString());
				// create the transcoder output
				ByteArrayOutputStream ostream = new ByteArrayOutputStream();
				TranscoderOutput output = new TranscoderOutput(ostream);
				try {
					// issue with batik JPEGTranscoder (since version 1.8)
					// JPEGTranscoder is not longer part of apache-xmlgraphics
					jpegTranscoder.transcode(input, output);
				} catch (TranscoderException excJpg) {
					try {
						// fallback of preview image converting from svg to png
						reader = new ByteArrayInputStream(embeddedImage.getData(handle.getModule()));
						input = new TranscoderInput(reader);
						PNGTranscoder pngConverter = new PNGTranscoder();
						pngConverter.transcode(input, output);
					} catch (TranscoderException excPng) {
					}
				}
				// flush the stream
				ostream.flush();
				ostream.close();
				// use the outputstream as Image input stream.
				in = new ByteArrayInputStream(ostream.toByteArray());
			} else {
				in = new ByteArrayInputStream(embeddedImage.getData(handle.getModule()));
			}
			ImageData[] datas = new ImageLoader().load(in);
			if (datas != null && datas.length != 0) {
				ImageData cur;
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
	 * Get the embedded image of the data URL
	 *
	 * @param url     data URL of the image
	 * @param refresh refresh the image data or use image cache
	 * @return Return the embedded image
	 * @throws IOException
	 */
	public Image getEmbeddedImageDataURL(String url, boolean refresh) throws IOException {
		if ((url == null) || (!refresh && invalidUrlList.contains(url))) {
			return null;
		}
		String key = url;
		Image image = null;
		if (!refresh) {
			image = getImageRegistry().get(key);
			if (image != null) {
				return image;
			}
		} else {
			removeCachedImage(key);
		}
		InputStream in = null;

		Decoder decoder = java.util.Base64.getDecoder();

		try {
			if (url.toLowerCase().contains(URL_IMAGE_TYPE_SVG)
					&& key.toLowerCase().contains(URL_PROTOCOL_TYPE_DATA_BASE))
			{
				String[] imageDataArray = key.split(URL_PROTOCOL_TYPE_DATA_BASE);
				String imageDataBase64 = imageDataArray[0];
				if (imageDataArray.length == 2)
					imageDataBase64 = imageDataArray[1];
				in = convertSvgToRasterImage(new String(decoder.decode(imageDataBase64)));

			} else if (url.toLowerCase().contains(URL_IMAGE_TYPE_SVG)
					&& key.toLowerCase().contains(URL_PROTOCOL_TYPE_DATA_UTF8))
			{
				String[] imageDataArray = key.split(URL_PROTOCOL_TYPE_DATA_UTF8);
				String imageDataUtf8 = imageDataArray[0];
				if (imageDataArray.length == 2)
					imageDataUtf8 = imageDataArray[1];
				in = convertSvgToRasterImage(new String(imageDataUtf8));

			} else if (url.toLowerCase().contains(URL_IMAGE_TYPE_SVG)) {
				String decodedKey = "x";
				String[] imageDataArray = url.split("svg\\+xml,");
				String imageDataUtf8 = imageDataArray[0];
				if (imageDataArray.length == 2)
					imageDataUtf8 = imageDataArray[1];

				try {
					decodedKey = java.net.URLDecoder.decode(imageDataUtf8, StandardCharsets.UTF_8.name());
				} catch (UnsupportedEncodingException e) {
				}
				in = convertSvgToRasterImage(new String(decodedKey));

			} else {
				String[] imageDataArray = key.split(URL_PROTOCOL_TYPE_DATA_BASE);
				String imageDataBase64 = imageDataArray[1];
				in = new ByteArrayInputStream(decoder.decode(imageDataBase64));
			}
			ImageData[] datas = new ImageLoader().load(in);
			if (datas != null && datas.length != 0) {
				ImageData cur;
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

		if (image == null) {
			if (!invalidUrlList.contains(url)) {
				invalidUrlList.add(url);
			}
		} else {
			invalidUrlList.remove(url);
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
	 * @param designHandle handle of report design
	 * @param uri          the URI of the image to load
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
	 * @param designHandle handle of report design
	 * @param uri          the URI of the image to load
	 * @return Reload the image
	 * @throws IOException
	 */
	public Image rloadImage(ModuleHandle designHandle, String uri) throws IOException {
		URL url = generateURL(designHandle, uri);
		if (url == null) {
			throw new FileNotFoundException(uri);
		}
		return loadImage(url, true);
	}

	/**
	 * Load the image based on URI
	 *
	 * @param uri the image URI
	 * @return Return the loaded image
	 * @throws IOException
	 */
	public Image loadImage(String uri) throws IOException {
		return loadImage(null, uri);
	}

	/**
	 * Load the image based on URI
	 *
	 * @param url the image URL
	 * @return Return the loaded image
	 * @throws IOException
	 */
	public Image loadImage(URL url) throws IOException {
		return loadImage(url, false);
	}

	/**
	 * @param url    the image URL
	 * @param reload image should be refreshed or used from cache
	 * @return Return the loaded image
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
				transcoder.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, .8f);
				// create the transcoder input
				String svgURI = url.toString();
				TranscoderInput input = new TranscoderInput(svgURI);
				// create the transcoder output
				ByteArrayOutputStream ostream = new ByteArrayOutputStream();
				TranscoderOutput output = new TranscoderOutput(ostream);
				try {
					transcoder.transcode(input, output);
				} catch (TranscoderException e) {

					PNGTranscoder pngTranscoder = new PNGTranscoder();
					input = new TranscoderInput(svgURI);
					// create the transcoder output
					ostream = new ByteArrayOutputStream();
					output = new TranscoderOutput(ostream);
					try {
						pngTranscoder.transcode(input, output);
					} catch (TranscoderException eJpeg) {
					}
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

	/**
	 * Converter to create raster image based on svg image
	 */
	private InputStream convertSvgToRasterImage(String imageSvg) throws IOException {

		// convert svg image to PNG image bytes
		PNGTranscoder pngTranscoder = new PNGTranscoder();
		// create the transcoder input
		StringReader reader = new StringReader(imageSvg);
		TranscoderInput input = new TranscoderInput(reader);
		// create the transcoder output
		ByteArrayOutputStream ostream = new ByteArrayOutputStream();
		TranscoderOutput output = new TranscoderOutput(ostream);
		try {
			pngTranscoder.transcode(input, output);
		} catch (TranscoderException e) {
		}
		// flush the stream
		ostream.flush();
		ostream.close();
		// use the outputstream as Image input stream.
		return new ByteArrayInputStream(ostream.toByteArray());
	}

	private ImageRegistry getImageRegistry() {
		return CorePlugin.getDefault().getImageRegistry();
	}

	/**
	 * Generate the image URL
	 *
	 * @param designHandle
	 * @param uri          of the image
	 * @return Return the URL of the image
	 * @throws MalformedURLException
	 */
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
	 * @param reportDesignHandle Module handle
	 * @param name               Name
	 * @return key string
	 */
	public String generateKey(ModuleHandle reportDesignHandle, String name) {
		return reportDesignHandle.hashCode() + EMBEDDED_SUFFIX + name;
	}

	/**
	 * Reload the URI image, refresh the cache.
	 *
	 * @param moduleHandel Module handle
	 * @param uri          uri of the image
	 * @return Return the reloaded image
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

	/**
	 * Create the URL based on URI
	 *
	 * @param uri uri string
	 * @return Return the URL based on URI
	 */
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
	 * @param moduleHandel Module handle
	 * @param uri          URI of the image
	 * @return Return the image based on URI
	 */
	// bugzilla 245641
	public Image getURIImage(ModuleHandle moduleHandel, String uri) {
		Image image = null;
		URL url = null;
		String uriParts[] = null;
		try {
			// data protocol raster image
			if (uri.startsWith(URL_PROTOCOL_TYPE_DATA) && uri.contains(URL_PROTOCOL_TYPE_DATA_BASE)
					&& !uri.contains(URL_IMAGE_TYPE_SVG)) {
				uriParts = uri.split(URL_PROTOCOL_TYPE_DATA_BASE);
				if (uriParts.length >= 2) {
					String encodedImg = uriParts[1];
					InputStream in = new ByteArrayInputStream(
							Base64.getDecoder().decode(encodedImg.getBytes(StandardCharsets.UTF_8)));
					ImageData[] datas = new ImageLoader().load(in);
					if (datas != null && datas.length != 0) {
						image = new Image(null, datas[0]);
						in.close();
					}
				}
				// data protocol svg image
			} else if (uri.startsWith(URL_PROTOCOL_TYPE_DATA) && uri.contains(URL_IMAGE_TYPE_SVG)) {

				String svgSplitter = "svg\\+xml,";
				if (uri.contains("svg+xml;utf8,")) {
					svgSplitter = "svg\\+xml;utf8,";
				} else if (uri.contains("svg+xml;base64,")) {
					svgSplitter = "svg\\+xml;base64,";
				}
				uriParts = uri.split(svgSplitter);
				if (uriParts.length >= 2) {
					String encodedImg = uriParts[1];
					String decodedImg = encodedImg;
					if (uri.contains(URL_PROTOCOL_TYPE_DATA_BASE)) { // "svg+xml;base64,"
						decodedImg = new String(
								Base64.getDecoder().decode(encodedImg.getBytes(StandardCharsets.UTF_8)));
					}
					decodedImg = java.net.URLDecoder.decode(decodedImg, StandardCharsets.UTF_8);
					InputStream in = convertSvgToRasterImage(decodedImg);
					ImageData[] datas = new ImageLoader().load(in);
					if (datas != null && datas.length != 0) {
						ImageData cur;
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
				}
			} else {
				url = createURIURL(uri);
				image = getImageFromURL(url, false);
			}
		} catch (Exception e) {
			if (url != null && !invalidUrlList.contains(url.toString())) {
				invalidUrlList.add(url.toString());
			}
		}
		return image;
	}

	/**
	 * Set the URI root path
	 *
	 * @param rootPath Root path of the URI
	 */
	public void setURIRootPath(String rootPath) {
		this.resourcesRootPath = rootPath;
	}

}
