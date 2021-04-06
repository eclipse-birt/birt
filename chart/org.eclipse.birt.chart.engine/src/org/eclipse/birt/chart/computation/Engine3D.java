/*******************************************************************************
 * Copyright (c) 2004, 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.computation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.eclipse.birt.chart.event.Arc3DRenderEvent;
import org.eclipse.birt.chart.event.Area3DRenderEvent;
import org.eclipse.birt.chart.event.I3DRenderEvent;
import org.eclipse.birt.chart.event.Image3DRenderEvent;
import org.eclipse.birt.chart.event.Line3DRenderEvent;
import org.eclipse.birt.chart.event.Oval3DRenderEvent;
import org.eclipse.birt.chart.event.Polygon3DRenderEvent;
import org.eclipse.birt.chart.event.PrimitiveRenderEvent;
import org.eclipse.birt.chart.event.Text3DRenderEvent;
import org.eclipse.birt.chart.event.WrappedInstruction;
import org.eclipse.birt.chart.internal.computations.Matrix;
import org.eclipse.birt.chart.model.attribute.Angle3D;
import org.eclipse.birt.chart.model.attribute.AngleType;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Rotation3D;
import org.eclipse.birt.chart.util.FillUtil;

/**
 * Engine3D
 */
public final class Engine3D implements IConstants {

	/**
	 * Indicates the both points are in range in clipping.
	 */
	public static final byte OUT_OF_RANGE_NONE = 0x0;
	/**
	 * Indicates the ending point is out of range in clipping.
	 */
	public static final byte OUT_OF_RANGE_END = 0x1;
	/**
	 * Indicates the starting point is out of range in clipping.
	 */
	public static final byte OUT_OF_RANGE_START = 0x2;
	/**
	 * Indicates the both points are out of range in clipping.
	 */
	public static final byte OUT_OF_RANGE_BOTH = 0x4;

	/**
	 * Viewer window size
	 */
	private double dViewerWidth, dViewerHeight;

	/**
	 * Viewer plane distance
	 */
	private double VIEW_DISTANCE = 200;
	/**
	 * Model plane distance
	 */
	private double MODEL_DISTANCE = 290;

	/**
	 * Front plane distance
	 */
	private double FRONT_DISTANCE = 20;
	/**
	 * Back plane distance
	 */
	private double BACK_DISTANCE = 600;

	/**
	 * Perspective distance
	 */
	private double PERSPECTIVE_VALUE = 100;

	/**
	 * Plane Reference Points
	 */
	private Vector[] PRP;
	/**
	 * Normal Vectors of Plane
	 */
	private Vector[] PNV;

	/**
	 * Center of projection
	 */
	private Vector COP;
	/**
	 * Projection direction unit vector
	 */
	private Vector VDZ;
	/**
	 * Up direction unit vector
	 */
	private Vector VDY;
	/**
	 * Right direction unit vector
	 */
	private Vector VDX;

	/**
	 * Light direction vector
	 */
	private Vector LDR;

	/**
	 * Rotation of the coordinate frame
	 */
	private Rotation3D ROT;

	/**
	 * Matrix to convert viewer coordinates to model coordinates.
	 */
	private Matrix V2M_MATRIX;
	/**
	 * Matrix to convert model coordinates to viewer coordinates.
	 */
	private Matrix M2V_MATRIX;
	/**
	 * Matrix to convert canvas coordinates to viewer coordinates.
	 */
	private Matrix C2V_MATRIX;
	/**
	 * Matrix to convert viewer coordinates to canvas coordinates.
	 */
	private Matrix V2C_MATRIX;

	// private static ILogger logger = Logger.getLogger(
	// "org.eclipse.birt.chart.engine/computation" ); //$NON-NLS-1$

	/**
	 * @param rotation
	 * @param lightDirection
	 * @param viewerWidth
	 * @param viewerHeight
	 * @param viewingDistance
	 * @param hitherDistance
	 * @param yonDistance
	 */
	public Engine3D(Rotation3D rotation, Vector lightDirection, double viewerWidth, double viewerHeight,
			double viewingDistance, double modelingDistance, double hitherDistance, double yonDistance,
			double perspectiveDistance) {
		dViewerWidth = viewerWidth;
		dViewerHeight = viewerHeight;

		ROT = rotation.copyInstance();
		LDR = new Vector(lightDirection);

		VIEW_DISTANCE = viewingDistance;
		MODEL_DISTANCE = modelingDistance;
		FRONT_DISTANCE = hitherDistance;
		BACK_DISTANCE = yonDistance;

		PERSPECTIVE_VALUE = perspectiveDistance;

		reset();
	}

	/**
	 * @param rotation
	 * @param lightDirection
	 * @param viewerWidth
	 * @param viewerHeight
	 */
	public Engine3D(Rotation3D rotation, Vector lightDirection, double viewerWidth, double viewerHeight) {
		dViewerWidth = viewerWidth;
		dViewerHeight = viewerHeight;

		ROT = rotation.copyInstance();
		LDR = new Vector(lightDirection);

		reset();
	}

	/**
	 * Resets the engine to default state.
	 */
	public void reset() {
		COP = new Vector(0, 0, -MODEL_DISTANCE);

		VDX = new Vector((dViewerWidth / 2) / 100, 0, 0, false);
		VDY = new Vector(0, (dViewerHeight / 2) / 100, 0, false);
		VDZ = new Vector(0, 0, VIEW_DISTANCE / 100, false);

		PNV = new Vector[6];
		PNV[0] = new Vector(0, 0, 1, false); // Hither Plane
		PNV[1] = new Vector(0, 0, -1, false); // Yon Plane
		PNV[2] = new Vector(0, -1, 1, false); // Top Plane
		PNV[3] = new Vector(0, 1, 1, false); // Bottom Plane
		PNV[4] = new Vector(1, 0, 1, false); // Left Plane
		PNV[5] = new Vector(-1, 0, 1, false); // Right Plane

		PRP = new Vector[6];
		PRP[0] = new Vector(0, 0, FRONT_DISTANCE); // Hither Plane
		PRP[1] = new Vector(0, 0, BACK_DISTANCE); // Yon Plane
		PRP[2] = new Vector(0, 0, 0); // Top Plane
		PRP[3] = new Vector(0, 0, 0); // Bottom Plane
		PRP[4] = new Vector(0, 0, 0); // Left Plane
		PRP[5] = new Vector(0, 0, 0); // Right Plane

		V2M_MATRIX = Matrix.identity(4, 4);
		M2V_MATRIX = Matrix.identity(4, 4);
		initViewModelMatrix();

		V2C_MATRIX = Matrix.identity(4, 4);
		C2V_MATRIX = Matrix.identity(4, 4);
		initViewCanvasMatrix();

	}

	private void initViewModelMatrix() {
		for (int i = 0; i < 4; i++) {
			V2M_MATRIX.set(0, i, VDX.get(i));
			V2M_MATRIX.set(1, i, VDY.get(i));
			V2M_MATRIX.set(2, i, VDZ.get(i));
			V2M_MATRIX.set(3, i, COP.get(i));
		}

		// M2V_MATRIX = inverse( V2M_MATRIX.copy( ) );
		M2V_MATRIX = V2M_MATRIX.copy().inverse();
	}

	private void initViewCanvasMatrix() {
		V2C_MATRIX.set(0, 0, (dViewerWidth / 2) / 100);
		V2C_MATRIX.set(1, 1, -(dViewerHeight / 2) / 100);
		V2C_MATRIX.set(3, 0, dViewerWidth / 2);
		V2C_MATRIX.set(3, 1, dViewerHeight / 2);

		C2V_MATRIX = V2C_MATRIX.copy().inverse();
	}

	/**
	 * @param v
	 * @return
	 */
	Vector model2View(Vector v) {
		return v.getMultiply(M2V_MATRIX);
	}

	/**
	 * @param v
	 * @return
	 */
	Vector view2model(Vector v) {
		return v.getMultiply(V2M_MATRIX);
	}

	/**
	 * @param v
	 * @return
	 */
	Vector canvas2View(Vector v) {
		return v.getMultiply(C2V_MATRIX);
	}

	/**
	 * @param v
	 * @return
	 */
	Vector view2Canvas(Vector v) {
		return v.getMultiply(V2C_MATRIX);
	}

	/**
	 * @param v
	 * @return
	 */
	Vector perspective(Vector v) {
		Vector nv = new Vector(v);
		nv.perspective(PERSPECTIVE_VALUE);
		return nv;
	}

	/**
	 * Translates the view frame.
	 * 
	 * @param v
	 */
	public void translate(Vector v) {
		COP.add(v);

		initViewModelMatrix();
	}

	/**
	 * Rotates the view frame along X axis
	 * 
	 * @param degree
	 */
	/*
	 * public void rotateViewX( double degree ) { Matrix m = Matrix.identity( 4, 4
	 * );
	 * 
	 * double radians = Math.toRadians( degree ); double cos = Math.cos( radians );
	 * double sin = Math.sin( radians );
	 * 
	 * m.set( 1, 1, cos ); m.set( 2, 2, cos ); m.set( 1, 2, -sin ); m.set( 2, 1, sin
	 * );
	 * 
	 * VDX.multiply( m ); VDY.multiply( m ); VDZ.multiply( m );
	 * 
	 * initViewModelMatrix( ); }
	 * 
	 * /** Rotates the view frame along Y axis
	 * 
	 * @param degree
	 */
	/*
	 * public void rotateViewY( double degree ) { Matrix m = Matrix.identity( 4, 4
	 * );
	 * 
	 * double radians = Math.toRadians( degree ); double cos = Math.cos( radians );
	 * double sin = Math.sin( radians );
	 * 
	 * m.set( 0, 0, cos ); m.set( 2, 2, cos ); m.set( 0, 2, sin ); m.set( 2, 0, -sin
	 * );
	 * 
	 * VDX.multiply( m ); VDY.multiply( m ); VDZ.multiply( m );
	 * 
	 * initViewModelMatrix( ); }
	 * 
	 * /** Rotates the view frame along Z axis
	 * 
	 * @param degree
	 */
	/*
	 * public void rotateViewZ( double degree ) { Matrix m = Matrix.identity( 4, 4
	 * );
	 * 
	 * double radians = Math.toRadians( degree ); double cos = Math.cos( radians );
	 * double sin = Math.sin( radians );
	 * 
	 * m.set( 0, 0, cos ); m.set( 1, 1, cos ); m.set( 0, 1, -sin ); m.set( 1, 0, sin
	 * ); // m.set( 1, 1, cos ); // m.set( 2, 2, cos ); // m.set( 1, 2, sin ); //
	 * m.set( 2, 1, -sin );
	 * 
	 * VDX.multiply( m ); VDY.multiply( m ); VDZ.multiply( m );
	 * 
	 * initViewModelMatrix( ); }
	 */
	Matrix rotateMatrixX(Matrix t, double degree) {
		Matrix m = Matrix.identity(4, 4);

		double radians = Math.toRadians(degree);
		double cos = Math.cos(radians);
		double sin = Math.sin(radians);

		m.set(1, 1, cos);
		m.set(2, 2, cos);
		m.set(1, 2, sin);
		m.set(2, 1, -sin);

		return t.times(m);
	}

	Matrix rotateMatrixY(Matrix t, double degree) {
		Matrix m = Matrix.identity(4, 4);

		double radians = Math.toRadians(degree);
		double cos = Math.cos(radians);
		double sin = Math.sin(radians);

		m.set(0, 0, cos);
		m.set(2, 2, cos);
		m.set(0, 2, -sin);
		m.set(2, 0, sin);

		return t.times(m);
	}

	Matrix rotateMatrixZ(Matrix t, double degree) {
		Matrix m = Matrix.identity(4, 4);

		double radians = Math.toRadians(degree);
		double cos = Math.cos(radians);
		double sin = Math.sin(radians);

		m.set(0, 0, cos);
		m.set(1, 1, cos);
		m.set(0, 1, sin);
		m.set(1, 0, -sin);

		return t.times(m);
	}

	Matrix translateMatrix(Matrix t, Vector v) {
		Matrix m = Matrix.identity(4, 4);
		m.set(3, 0, v.get(0));
		m.set(3, 1, v.get(1));
		m.set(3, 2, v.get(2));

		return t.times(m);
	}

	/**
	 * Clipping the lines according to viewing volumn.
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public byte checkClipping(Vector start, Vector end) {
		byte retval = OUT_OF_RANGE_NONE;
		Vector v1 = new Vector();
		Vector v2 = new Vector();
		Vector clip_ptr = new Vector();

		// check for each plane, and do canonical view clipping
		for (int i = 0; i < 6; i++) {
			v1.set(start.get(0), start.get(1), start.get(2));
			v1.sub(PRP[i]);

			v2.set(end.get(0), end.get(1), end.get(2));
			v2.sub(PRP[i]);

			double sp1 = v1.scalarProduct(PNV[i]);
			double sp2 = v2.scalarProduct(PNV[i]);

			// both end point of line are out side of this clipping plane
			if (sp1 < 0 && sp2 < 0) {
				return OUT_OF_RANGE_BOTH;
			}

			// one end point is outside of the clipping plane, this point
			// needs to be clipped out
			if (sp1 < 0 || sp2 < 0) {
				double fraction = Math.abs(sp1) / (Math.abs(sp1) + Math.abs(sp2));
				clip_ptr.set(end.get(0), end.get(1), end.get(2));
				clip_ptr.sub(start);
				clip_ptr.scale(fraction);
				clip_ptr.add(start);

				// start point is clipped out, and is replaced by the new point
				if (sp1 < 0) {
					retval = (byte) (retval | OUT_OF_RANGE_START);
					start.set(clip_ptr.get(0), clip_ptr.get(1), clip_ptr.get(2));
				}
				// end point is clipped out, and is replaced by the new point
				else {
					retval = (byte) (retval | OUT_OF_RANGE_END);
					end.set(clip_ptr.get(0), clip_ptr.get(1), clip_ptr.get(2));
				}
			}
		}

		return retval;
	}

	/**
	 * @param va
	 * @param m
	 */
	void transform(Vector[] va, Matrix m) {
		for (int i = 0; i < va.length; i++) {
			va[i].multiply(m);
		}
	}

	/**
	 * @param p3dre
	 * @return true if polygon is behind
	 */
	boolean checkBehindFace(Polygon3DRenderEvent p3dre) {
		if (p3dre.isDoubleSided())
			return false;

		Vector viewDirection = p3dre.getObject3D().getCenter();
		Vector normal = p3dre.getObject3D().getNormal();

		// check if the normal vector of face points to the same direction
		// of the viewing direction
		return (normal.scalarProduct(viewDirection) <= 0);
	}

	Matrix getTransformMatrix() {
		Matrix m = Matrix.identity(4, 4);

		// inverse Z sign.
		m.set(2, 2, -1);

		for (Iterator<?> itr = ROT.getAngles().iterator(); itr.hasNext();) {
			Angle3D agl = (Angle3D) itr.next();
			if (agl.getType() == AngleType.NONE_LITERAL) {
				m = rotateMatrixY(m, agl.getYAngle());
				m = rotateMatrixX(m, agl.getXAngle());
				m = rotateMatrixZ(m, agl.getZAngle());
			} else {
				switch (agl.getType().getValue()) {
				case AngleType.X:
					m = rotateMatrixX(m, agl.getAxisAngle());
					break;
				case AngleType.Y:
					m = rotateMatrixY(m, agl.getAxisAngle());
					break;
				case AngleType.Z:
					m = rotateMatrixZ(m, agl.getAxisAngle());
					break;
				}
			}
		}

		// // TODO test translate matrix
		// Vector v = new Vector( 0, -200, 0, false );
		// // v.multiply( C2V_MATRIX );
		// // v.multiply( V2M_MATRIX );
		// m = translateMatrix( m, v );

		return m;
	}

	private boolean translate3DEvent(Object obj, Matrix transMatrix, double xOffset, double yOffset) {
		return translate3DEvent_clip_opt(obj, transMatrix, xOffset, yOffset, true);
	}

	private boolean translate3DEvent_clip_opt(Object obj, Matrix transMatrix, double xOffset, double yOffset,
			boolean bClip) {
		if (obj instanceof Polygon3DRenderEvent) {
			Polygon3DRenderEvent p3dre = (Polygon3DRenderEvent) obj;
			Object3D object3D = p3dre.getObject3D();

			object3D.transform(transMatrix);
			object3D.transform(M2V_MATRIX);

			object3D.prepareZSort();

			// The disabled 3d render event is just used to determine the
			// rendering order of polygons, can't be removed.
			if (p3dre.isEnabled()) {
				boolean behind = checkBehindFace(p3dre);
				p3dre.setBehind(behind);

				if (p3dre.isBehind()) {
					// optimize for culling face.
					return false;
				}
			}

			double cosValue = object3D.getNormal().cosineValue(LDR);
			if (p3dre.isDoubleSided()) {
				cosValue = -Math.abs(cosValue);
			}
			double brightnessRatio = (1 - cosValue) / 2d;
			p3dre.setBrightness(brightnessRatio);

			// If this render event is disabled, it means this render event is
			// used for determining the order of 3D polygons, not used for
			// rendering, so here we can't do clipping action for this event,
			// the clipping action might cause this render event is ignored.
			if (bClip && p3dre.isEnabled()) {
				object3D.clip(this);
			}

			if (object3D.getVectors().length < 3) {
				return false;
			}
			object3D.perspective(PERSPECTIVE_VALUE);
			object3D.transform(V2C_MATRIX);

			p3dre.prepare2D(xOffset, yOffset);

			return true;
		} else if (obj instanceof Line3DRenderEvent) {
			Line3DRenderEvent l3dre = (Line3DRenderEvent) obj;

			if (l3dre.getLineAttributes() == null || !l3dre.getLineAttributes().isVisible()) {
				return false;
			}

			Object3D object3D = l3dre.getObject3D();

			object3D.transform(transMatrix);
			object3D.transform(M2V_MATRIX);

			object3D.prepareZSort();

			if (bClip) {
				object3D.clip(this);
			}

			if (object3D.getVectors().length < 2) {
				return false;
			}
			object3D.perspective(PERSPECTIVE_VALUE);

			object3D.transform(V2C_MATRIX);

			l3dre.prepare2D(xOffset, yOffset);

		} else if (obj instanceof Text3DRenderEvent) {
			Text3DRenderEvent t3dre = (Text3DRenderEvent) obj;
			Object3D object3D = t3dre.getObject3D();

			object3D.transform(transMatrix);
			object3D.transform(M2V_MATRIX);

			object3D.prepareZSort();

			if (bClip) {
				object3D.clip(this);
			}

			if (object3D.getVectors().length < 1) {
				return false;
			}
			object3D.perspective(PERSPECTIVE_VALUE);
			object3D.transform(V2C_MATRIX);

			t3dre.prepare2D(xOffset, yOffset);
		} else if (obj instanceof Oval3DRenderEvent) {
			Oval3DRenderEvent o3dre = (Oval3DRenderEvent) obj;
			Object3D object3D = o3dre.getObject3D();

			object3D.transform(transMatrix);
			object3D.transform(M2V_MATRIX);

			object3D.prepareZSort();

			if (bClip) {
				object3D.clip(this);
			}

			if (object3D.getVectors().length < 3) {
				return false;
			}
			object3D.perspective(PERSPECTIVE_VALUE);
			object3D.transform(V2C_MATRIX);

			o3dre.prepare2D(xOffset, yOffset);

		} else if (obj instanceof Image3DRenderEvent) {
			Image3DRenderEvent i3dre = (Image3DRenderEvent) obj;
			Object3D object3D = i3dre.getObject3D();

			object3D.transform(transMatrix);
			object3D.transform(M2V_MATRIX);

			object3D.prepareZSort();

			if (bClip) {
				object3D.clip(this);
			}

			if (object3D.getVectors().length < 1) {
				return false;
			}
			object3D.perspective(PERSPECTIVE_VALUE);
			object3D.transform(V2C_MATRIX);

			i3dre.prepare2D(xOffset, yOffset);
		} else if (obj instanceof Arc3DRenderEvent) {
			Arc3DRenderEvent a3dre = (Arc3DRenderEvent) obj;
			Object3D object3D = a3dre.getObject3D();

			object3D.transform(transMatrix);
			object3D.transform(M2V_MATRIX);

			object3D.prepareZSort();

			if (bClip) {
				object3D.clip(this);
			}

			if (object3D.getVectors().length < 1) {
				return false;
			}
			object3D.perspective(PERSPECTIVE_VALUE);
			object3D.transform(V2C_MATRIX);

			a3dre.prepare2D(xOffset, yOffset);
		} else if (obj instanceof Area3DRenderEvent) {
			Area3DRenderEvent a3dre = (Area3DRenderEvent) obj;

			for (Iterator<PrimitiveRenderEvent> itr = a3dre.iterator(); itr.hasNext();) {
				PrimitiveRenderEvent pre = (PrimitiveRenderEvent) itr.next();

				if (pre instanceof I3DRenderEvent) {

					Object3D object3D = ((I3DRenderEvent) pre).getObject3D();

					object3D.transform(transMatrix);
					object3D.transform(M2V_MATRIX);

					object3D.prepareZSort();

					if (bClip) {
						object3D.clip(this);
					}

					if (object3D.getVectors().length < 1) {
						itr.remove();
						continue;
					}

					object3D.perspective(PERSPECTIVE_VALUE);
					object3D.transform(V2C_MATRIX);

				}
			}

			a3dre.prepare2D(xOffset, yOffset);
		}

		return true;
	}

	/**
	 * @param renderingEvents
	 * @return
	 */
	public PrimitiveRenderEvent processEvent(PrimitiveRenderEvent event, double xOffset, double yOffset) {
		Matrix transMatrix = getTransformMatrix();

		if (translate3DEvent(event, transMatrix, xOffset, yOffset)) {
			return event;
		}

		return null;
	}

	/**
	 * @param renderingEvents
	 * @return
	 */
	public PrimitiveRenderEvent processEvent_noclip(PrimitiveRenderEvent event, double xOffset, double yOffset) {
		Matrix transMatrix = getTransformMatrix();

		if (translate3DEvent_clip_opt(event, transMatrix, xOffset, yOffset, false)) {
			return event;
		}

		return null;
	}

	/**
	 * Transforms 3D polygons and sort their rendering order, and antialias.
	 * 
	 * @param renderingEvents
	 * @param xOffset
	 * @param yOffset
	 * @return
	 */
	public List processEvent(List renderingEvents, double xOffset, double yOffset) {
		return this.processEvent(renderingEvents, xOffset, yOffset, true);
	}

	/**
	 * Transforms 3D polygons and sort their rendering order, and antialias.
	 * 
	 * @param renderingEvents
	 * @param xOffset
	 * @param yOffset
	 * @param antialiasing
	 * @return
	 */
	public List processEvent(List renderingEvents, double xOffset, double yOffset, boolean antialiasing) {
		Matrix transMatrix = getTransformMatrix();

		List rtList = new ArrayList();
		List labels = new ArrayList();

		WrappedInstruction wi;

		for (Iterator itr = renderingEvents.iterator(); itr.hasNext();) {
			Object obj = itr.next();

			wi = null;

			if (obj instanceof WrappedInstruction) {
				wi = (WrappedInstruction) obj;

				// Never model for 3D events.
				assert !wi.isModel();

				obj = wi.getEvent();

				// The render events in sub-deferred cache should be processed too.
				if (wi.getSubDeferredCache() != null) {
					wi.getSubDeferredCache().process3DEvent(this, xOffset, yOffset);
				}
			}

			if (translate3DEvent(obj, transMatrix, xOffset, yOffset)) {
				if (wi != null) {
					if (obj instanceof Text3DRenderEvent)
						labels.add(wi);
					else
						rtList.add(wi);
				} else {
					if (obj instanceof Text3DRenderEvent)
						labels.add(obj);
					else
						rtList.add(obj);
				}
			}
		}

		zsort(rtList);
		overlapSwap(rtList);
		if (antialiasing) {
			detectSharedEdges(rtList, xOffset, yOffset);
		}
		rtList.addAll(labels);
		return rtList;
	}

	// Draw non-antialiased lines on shared edges
	private void detectSharedEdges(List rtList, double xOffset, double yOffset) {
		SortedMap sharedEdges = new TreeMap();

		for (int i = 0; i < rtList.size(); i++) {
			Object obj = rtList.get(i);
			while (obj instanceof WrappedInstruction) {
				obj = ((WrappedInstruction) obj).getEvent();
			}
			if (obj instanceof Polygon3DRenderEvent) {
				for (int j = 0; j < i; j++) {
					Object comparedEvent = rtList.get(j);
					while (comparedEvent instanceof WrappedInstruction) {
						comparedEvent = ((WrappedInstruction) comparedEvent).getEvent();
					}
					if (comparedEvent instanceof Polygon3DRenderEvent)

					{
						I3DRenderEvent event = (I3DRenderEvent) comparedEvent;

						WrappedInstruction edge = getSharedEdge(event, (I3DRenderEvent) obj, xOffset, yOffset);
						if (edge != null) {
							Integer index = Integer.valueOf(j);
							if (sharedEdges.containsKey(index)) {
								((List) sharedEdges.get(index)).add(edge);
							} else {
								List list = new ArrayList();
								list.add(edge);
								sharedEdges.put(index, list);
							}
						}
					}
				}
			}
		}
		int offset = 0;
		// re-insert edge polygons just before the first polygon with the shared edge
		for (Iterator iter = sharedEdges.entrySet().iterator(); iter.hasNext();) {
			Map.Entry entry = (Map.Entry) iter.next();
			Integer position = (Integer) entry.getKey();
			List lines = (List) entry.getValue();
			for (Iterator iterList = lines.iterator(); iterList.hasNext();) {
				rtList.add(position.intValue() + offset, iterList.next());
				offset++;
			}
		}
	}

	public WrappedInstruction getSharedEdge(I3DRenderEvent event1, I3DRenderEvent event2, double xOffset,
			double yOffset) {
		PrimitiveRenderEvent primEvent = (PrimitiveRenderEvent) event2;
		Fill background = primEvent.getBackground();
		if (!(background instanceof ColorDefinition))
			return null;

		ColorDefinition backgroundColor = (ColorDefinition) background;
		Polygon3DRenderEvent sharedPolygonEdge = null;
		WrappedInstruction wi = null;

		Object3D sharedEdgeObject = event1.getObject3D().getSharedEdge(event2.getObject3D());
		if (sharedEdgeObject != null) {
			sharedPolygonEdge = new Polygon3DRenderEvent(primEvent.getSource());
			ColorDefinition sharedBackgroundColor = (ColorDefinition) FillUtil.copyOf(backgroundColor);

			sharedPolygonEdge.setBackground(sharedBackgroundColor);
			if (backgroundColor.getTransparency() < 255) {
				int t = backgroundColor.getTransparency();
				// Make the background more transparent than the original so it doesn't appear
				// too much.
				sharedBackgroundColor.setTransparency(t * t * t / (255 * 255));
			}

			// sharedPolygonEdge.setOutline( LineAttributesImpl.create(
			// sharedBackgroundColor, LineStyle.SOLID_LITERAL, 1 ) );

			Location[] locations = sharedEdgeObject.getPoints2D(xOffset, yOffset);

			sharedPolygonEdge.setPoints(locations);
			wi = new WrappedInstruction(null, sharedPolygonEdge, PrimitiveRenderEvent.FILL);
		}

		return wi;
	}

	/**
	 * This method uses
	 * <a href='http://en.wikipedia.org/wiki/Painter%27s_algorithm'>Painter's
	 * Algorithm</a> to determine the orders of the polygons in current 3D space.
	 * <P>
	 * Painter's Algorithm:<br>
	 * Let's say there are two polygons P and Q. Supposing Zmin(P) < Zmin(Q), if
	 * Zmax(P) < Zmax(Q) then P doesn't overlay Q, if Zmax(P) > Zmax(Q) then we must
	 * check five cases below: <br>
	 * 1. The shadow bounding(s) of P and Q on OXY plane haven't intersectant part
	 * on X direction.<br>
	 * 2. The shadow bounding(s) of P and Q on OXY plane haven't intersectant part
	 * on Y direction.<br>
	 * 3. The shadows of P and Q on OXY plane haven't intersection.<br>
	 * 4. All points of P lie in one side of Q and view point lies in opposite side
	 * of Q.<br>
	 * 5. All points of Q and view point lie in the same side of P.<br>
	 * Considering above 5 cases, anyone case is true, P won't overlay Q. Otherwise,
	 * if all 5 cases are false, we must compute the intersection of P and Q's
	 * shadow polygons on XY plane, it doesn't need to compute concrete overlay
	 * part, just do the Z depth comparison on the crosspoint to know the painting
	 * order of P and Q. If polygons have loop intersections, it must first separate
	 * the polygons from intersection and then compute crosspoints.<br>
	 * Note: Painter's algorithm can just be applied to convex polygon, and polygons
	 * in 3D space can't be intersection.
	 * 
	 * @param rtList
	 */
	protected void overlapSwap(List<Object> rtList) {
		if (rtList.size() == 0) {
			return;
		}

		// Since this method uses two embedded 'for' loops to order 3D polygons,
		// here gets 3D event objects and parent objects in advance to improve
		// performance for the 3D object accessing in following 'for' loops.
		int size = rtList.size();
		Object3D[] eventObjs = new Object3D[size];
		Object3D[] eventParents = new Object3D[size];
		for (int i = 0; i < size; i++) {
			Object e = rtList.get(i);
			eventObjs[i] = getObjectFromEvent(e, true);
			eventParents[i] = getParentObject(e);
		}

		// According to the Z-depth order of polygons, check the polygon overlay
		// case. If two polygons are overlay then adjust the correct rendering
		// order for these two polygons, else it should still keep the Z-depth
		// order of these two polygons and their contiguous polygons.
		HashSet<Object> hs = new HashSet<Object>();

		for (int i = 0; i < rtList.size(); i++) {
			long max_loop = rtList.size() - i;
			int n = -1;
			boolean restart = true;
			while (restart && n < max_loop) {
				restart = false;
				n++;

				Object event = rtList.get(i);
				Object3D far = eventObjs[i];
				;

				for (int j = i + 1; j < rtList.size(); j++) {
					Object event2 = rtList.get(j);
					Object3D near = eventObjs[j];
					Object3D nearParent = eventParents[j];

					if (far == near) {
						if (nearParent == null) // near is parent
						{
							rtList.set(i, event2);
							rtList.set(j, event);

							// Adjust the related event 3D object arrays at the same time.
							Object3D tmpObj = eventObjs[j];
							eventObjs[j] = eventObjs[i];
							eventObjs[i] = tmpObj;
							tmpObj = eventParents[j];
							eventParents[j] = eventParents[i];
							eventParents[i] = tmpObj;

							restart = true;
							break;
						} else {
							continue;
						}
					}

					// far != near
					if (far.testZOverlap(near)) {
						boolean bSwap = far.testSwap(near, this);

						if (bSwap) {
							boolean bSwapedFromFront = hs.contains(event);
							bSwap = !bSwapedFromFront;

							if (bSwap) {
								hs.add(event);
							}
						}

						if (bSwap) {
							rtList.set(i, event2);
							rtList.set(j, event);

							// Adjust the related event 3D object arrays at the same time.
							Object3D tmpObj = eventObjs[j];
							eventObjs[j] = eventObjs[i];
							eventObjs[i] = tmpObj;
							tmpObj = eventParents[j];
							eventParents[j] = eventParents[i];
							eventParents[i] = tmpObj;

							restart = true;
							break;
						}
					}
				}
			}
		}
	}

	public Vector getViewReferencePoint() {
		return this.PRP[0];
	}

	public static Object3D getObjectFromEvent(Object event) {
		return getObjectFromEvent(event, false);
	}

	/**
	 * 
	 * @param event
	 * @return
	 */
	public static Object3D getParentObject(Object event) {
		if (event instanceof Line3DRenderEvent) {
			return ((Line3DRenderEvent) event).getObject3DParent();
		}
		return null;
	}

	public static Object3D getObjectFromEvent(Object event, boolean bParent) {
		if (event instanceof WrappedInstruction) {
			event = ((WrappedInstruction) event).getEvent();
		}

		if (event instanceof I3DRenderEvent) {

			if (event instanceof Area3DRenderEvent) {
				return ((I3DRenderEvent) ((Area3DRenderEvent) event).getElement(0)).getObject3D();
			} else if (bParent && event instanceof Line3DRenderEvent
					&& ((Line3DRenderEvent) event).getObject3DParent() != null) {
				return ((Line3DRenderEvent) event).getObject3DParent();
			} else {
				return ((I3DRenderEvent) event).getObject3D();
			}

		} else {
			throw new IllegalArgumentException();
		}
	}

	private int zSortComparator(Object o1, Object o2) {
		Object3D obj1 = getObjectFromEvent(o1);
		Object3D obj2 = getObjectFromEvent(o2);

		if (obj1.getZMax() > obj2.getZMax()) {
			return -1;
		} else if (obj1.getZMax() < obj2.getZMax()) {
			return 1;
		} else {
			return 0;
		}

	}

	// z-sort
	protected void zsort(List<Object> rtList) {
		Collections.sort(rtList, new Comparator<Object>() {

			public int compare(Object o1, Object o2) {
				return zSortComparator(o1, o2);
			}
		});
	}
}
