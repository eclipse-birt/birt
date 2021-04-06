package org.eclipse.birt.report.service;

import java.io.ByteArrayOutputStream;

import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.service.api.IViewerReportDesignHandle;

public class BirtViewerReportDesignHandle implements IViewerReportDesignHandle {
	private String contentType;

	private String fileName;

	private IReportRunnable runnable; // Report design name not always exists.

	public BirtViewerReportDesignHandle(String contentType, String fileName) {
		this.contentType = contentType;
		this.fileName = fileName;
	}

	public BirtViewerReportDesignHandle(String contentType, IReportRunnable runnable) {
		this.contentType = contentType;
		this.runnable = runnable;
	}

	public String getContentType() {
		return contentType;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String name) {
		this.fileName = name;
	}

	public Object getDesignObject() {
		return runnable;
	}

	public void setDesignObject(Object obj) {
		runnable = (IReportRunnable) obj;
	}

	public ByteArrayOutputStream getObjectStream() {
		// TODO What to do here??
		return null;
	}

	public String getDocumentName() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setDocumentName(String documentName) {
		// TODO Auto-generated method stub

	}

}
