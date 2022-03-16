/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.pptx.writer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.engine.emitter.pptx.PPTXRender;
import org.eclipse.birt.report.engine.emitter.pptx.SlideWriter;
import org.eclipse.birt.report.engine.nLayout.area.impl.PageArea;
import org.eclipse.birt.report.engine.ooxml.IPart;
import org.eclipse.birt.report.engine.ooxml.ImageManager;
import org.eclipse.birt.report.engine.ooxml.Package;
import org.eclipse.birt.report.engine.ooxml.constants.ContentTypes;
import org.eclipse.birt.report.engine.ooxml.constants.NameSpaces;
import org.eclipse.birt.report.engine.ooxml.constants.RelationshipTypes;
import org.eclipse.birt.report.engine.ooxml.util.OOXmlUtil;

public class Presentation extends Component {
	private static final String TAG_CY = "cy";

	private static final String TAG_CX = "cx";

	private static final String TAG_NOTES_SZ = "p:notesSz";

	private static final String TAG_SLIDE_SZ = "p:sldSz";

	private static final String TAG_SLIDE_ID = "p:sldId";

	private static final String TAG_SLIDE_ID_LIST = "p:sldIdLst";

	private static final String TAG_RELATIONSHIP_ID = "r:id";

	private static final String TAG_ID = "id";

	private static final String TAG_SLIDE_MASTER_ID = "p:sldMasterId";

	private static final String TAG_SLIDE_MASTER_ID_LIST = "p:sldMasterIdLst";

	public static final int MAX_SLIDE_HEIGHT = 51206400;

	private final PPTXBookmarkManager bmkmanager;

	private int slideMasterId = 1;
	private int slideLayoutId = 1;
	private int themeId = 1;

	private long globalId = 2147483648L;

	private int width = 0, height = 0;

	private final Package pkg;

	private final HashMap<String, SlideMaster> slideMasters = new HashMap<>();
	private final List<Slide> slides = new ArrayList<>();
	private String author, title, description, subject;
	private PPTXRender render;
	private int shapeId;

	public Presentation(OutputStream out, String tempFileDir, int compressionMode) {
		pkg = Package.createInstance(out, tempFileDir, compressionMode);
		String uri = "ppt/presentation.xml";
		String type = ContentTypes.PRESENTATIONML;
		String relationshipType = RelationshipTypes.DOCUMENT;
		bmkmanager = new PPTXBookmarkManager();
		this.part = pkg.getPart(uri, type, relationshipType);
		pkg.setExtensionData(new ImageManager());
		try {
			writer = part.getCacheWriter();
			initialize();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public SlideMaster getSlideMaster(String name) throws IOException {
		return slideMasters.get(name);
	}

	public SlideMaster createSlideMaster(String name, PageArea area) throws IOException {
		Theme theme = new Theme(part, this);
		SlideMaster slideMaster = new SlideMaster(this, area);
		slideMaster.referTo(theme);
		slideMasters.put(name, slideMaster);
		return slideMaster;
	}

	private void outputSlideMasters() throws IOException {
		writer.openTag(TAG_SLIDE_MASTER_ID_LIST);
		for (Map.Entry<String, SlideMaster> entry : slideMasters.entrySet()) {
			SlideMaster slideMaster = entry.getValue();
			writer.openTag(TAG_SLIDE_MASTER_ID);
			writer.attribute(TAG_ID, String.valueOf(getNextGlobalId()));
			writer.attribute(TAG_RELATIONSHIP_ID, slideMaster.getPart().getRelationshipId());
			writer.closeTag(TAG_SLIDE_MASTER_ID);
			if (render.isEditMode()) {
				new SlideWriter(render).writeSlideMaster(slideMaster);
			}
			slideMaster.close();
		}
		writer.closeTag(TAG_SLIDE_MASTER_ID_LIST);
	}

	public void initialize() {
		writer.startWriter();
		writer.openTag("p:presentation");
		writer.nameSpace("a", NameSpaces.DRAWINGML);
		writer.nameSpace("r", NameSpaces.RELATIONSHIPS);
		writer.nameSpace("p", NameSpaces.PRESENTATIONML);

	}

	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public Slide createSlide(SlideMaster master, int pageWidth, int pageHeight, PageArea area) throws IOException {
		if (pageWidth > width) {
			width = pageWidth;
		}
		if (pageHeight > height) {
			height = pageHeight;
		}
		int slideIndex = slides.size() + 1;
		Slide slide = new Slide(this, slideIndex, master.getSlideLayout());
		slides.add(slide);
		return slide;
	}

	private void outputSlides() {
		writer.openTag(TAG_SLIDE_ID_LIST);
		for (Slide slide : slides) {
			bmkmanager.resolveDisconnectedBookmarks(slide);
			writer.openTag(TAG_SLIDE_ID);
			writer.attribute(TAG_ID, slide.getSlideId());
			writer.attribute(TAG_RELATIONSHIP_ID, slide.getPart().getRelationshipId());
			writer.closeTag(TAG_SLIDE_ID);
		}
		writer.closeTag(TAG_SLIDE_ID_LIST);
	}

	public void close() throws IOException {
		new Core(this, author, title, description, subject);
		outputSlideMasters();
		outputSlides();
		writer.openTag(TAG_SLIDE_SZ);
		// Set default page size to A4.
		if (width == 0) {
			width = 612;
		}
		if (height == 0) {
			height = 792;
		}

		long convertedWidth = OOXmlUtil.convertPointerToEmus(width);
		long convertedHeight = OOXmlUtil.convertPointerToEmus(height);

		if (convertedHeight > MAX_SLIDE_HEIGHT) {
			convertedHeight = MAX_SLIDE_HEIGHT;
		}
		writer.attribute(TAG_CX, convertedWidth);
		writer.attribute(TAG_CY, convertedHeight);
		writer.closeTag(TAG_SLIDE_SZ);

		writer.openTag(TAG_NOTES_SZ);
		writer.attribute(TAG_CX, convertedHeight);
		writer.attribute(TAG_CY, convertedWidth);
		writer.closeTag(TAG_NOTES_SZ);
		writer.closeTag("p:presentation");

		copyPropertyFile("viewProps");
		copyPropertyFile("tableStyles");
		copyPropertyFile("presProps");

		writer.close();
		pkg.close();
	}

	public void copyPropertyFile(String propFile) throws IOException {
		String url = "ppt/" + propFile + ".xml";
		String type = "application/vnd.openxmlformats-officedocument.presentationml." + propFile + "+xml";
		IPart part = pkg.getPart(url, type, null);
		copyPartContent(propFile + ".xml", part);
	}

	private void copyPartContent(String file, IPart part) throws IOException {
		InputStream is = this.getClass().getResourceAsStream(file);
		OutputStream os = part.getOutputStream();
		// Transfer bytes from in to out
		byte[] buf = new byte[1024];
		int len;
		while ((len = is.read(buf)) > 0) {
			os.write(buf, 0, len);
		}
		is.close();
		os.close();
	}

	public Package getPackage() {
		return pkg;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public int getNextShapeId() {
		return shapeId++;
	}

	public int getNextSlideMasterId() {
		return slideMasterId++;
	}

	public int getNextThemeId() {
		return themeId++;
	}

	public int getNextSlideLayoutId() {
		return slideLayoutId++;
	}

	public long getNextGlobalId() {
		return globalId++;
	}

	public int getCurrentSlideIdx() {
		return slides.size();
	}

	private Slide getCurrentSlide() {
		return slides.get(slides.size() - 1);
	}

	public String getBookmarkRelationshipid(String bmk) {
		return bmkmanager.getBookmarkRelationId(bmk, getCurrentSlide());
	}

	public void addBookmark(String key, int slideIdx) {
		bmkmanager.addBookmark(key, slideIdx);
	}

	public void setRender(PPTXRender pptxrender) {
		render = pptxrender;
	}

	public int getTotalSlides() {
		return slides.size();
	}
}
