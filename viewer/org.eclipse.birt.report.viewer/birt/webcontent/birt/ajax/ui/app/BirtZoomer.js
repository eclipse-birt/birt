/*******************************************************************************
 * Copyright (c) 2025 Thomas Gutmann
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Thomas Gutmann  - initial implementation
 *******************************************************************************/

/**
 *	BirtZoomer, Zoom handling for the report viewer
 */
 BirtZoomer = Class.create( );

 BirtZoomer.prototype = 
 {
	  zoomerObjectId		: "__BIRT_ROOT"
	, zoomerScaleLabel		: "zoomScale"
	, zoomerDocument		: "Document"
	, zoom					: 1
	, zoomStep				: 0.2
	, zoomerObject			: null
	, docReport				: null
	, docBackground			: null
	, bodyInitWidth			: null
	, backgroundInitWidth	: null
	,
 	/**
 	 *	Initialization routine required by "ProtoType" lib.
 	 *	@return, void
 	 */
	initialize : function( id )
	{
	},
	/**
	 * Initialization of zoomer objects
	 */
	initZoomer : function() {
		// object to be zoomed
		if (this.zoomerObject === null ) {
			this.zoomerObject = document.getElementById(this.zoomerObjectId);
		}
		// document of the report
		if (this.docReport === null) {
			this.docReport = document.getElementById(this.zoomerDocument);
		}
		// document background to be increased at zooming
		if (this.docBackground === null && document.getElementById(this.zoomerObjectId) !== null) {
			this.docBackground = document.getElementById(this.zoomerObjectId).parentElement;
		}
		// default width of zoom object
		if (this.bodyInitWidth === null && this.zoomerObject) {
			this.bodyInitWidth = this.zoomerObject.getWidth();
		}
		// default width of document background
		if (this.backgroundInitWidth === null && this.docBackground) {
			this.backgroundInitWidth = this.docBackground.getWidth();
		}
	}
	,
	/**
	 *	Refresh the zoomed preview of the report preview
	 */
	zoomRefresh : function() {
		this.zoomerObject			= null;
		this.docReport				= null;
		this.docBackground			= null;
		this.bodyInitWidth			= null;
		this.backgroundInitWidth	= null;

		this.initZoomer();
		if (this.zoomerObject) {
			this.setZoomScale();
		}
	}
	,
	/**
	 *	Handle the zoom in of the report preview
	 */
	zoomIn : function() {
		this.initZoomer();
		if (this.zoomerObject) {
			this.zoom += this.zoomStep;
			this.setZoomScale();
		}
	}
	,
	/**
	 *	Handle the zoom out of the report preview
	 */
	zoomOut : function() {
		this.initZoomer();
		if (this.zoomerObject && this.zoom > this.zoomStep && this.zoom >= 0.4) {
			this.zoom -= this.zoomStep;
			this.setZoomScale();
		}
	}
	,
	/**
	 *	Handle the reset of the zommed preview to the original size
	 */
	resetZoom : function() {
		this.initZoomer();
		if (this.zoomerObject) {
			this.zoom = 1;
			this.zoomerObject.style.width = this.bodyInitWidth;
			this.setZoomScale();
		}
	}
	,	
	/**
	 *	Calculation of the zoom, handle the norizontal scorllbar position and refresh the zoom scale label
	 */
	setZoomScale : function() {
		
		/* set the zoom and preview background */
		if (this.zoomerObject) {
			this.zoomerObject.style.zoom = this.zoom;
			if(this.zoom > this.zoomStep && this.zoom >= 1.0) {
				this.docBackground.style.width = Math.round(this.backgroundInitWidth * this.zoom) + "px";
			}
		}
		
		/* set the zoom label */
		var nodeZoomScaler = document.getElementById(this.zoomerScaleLabel);
		if (nodeZoomScaler) {
			nodeZoomScaler.innerHTML = Math.round(100 * this.zoom) + "%"
		}
		
		/* set the horizontal scrollbar */
		if (this.docReport) {
			this.docReport.scrollLeft = (this.docReport.scrollLeftMax / 2)
		}
	}
}