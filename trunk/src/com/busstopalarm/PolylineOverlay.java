/**
 * Draws an overlay containing a route on the containing Map.
 * 
 * CAUTION: NOT YET FULLY TESTED.
 * 
 * @author Michael Eng 
 */

package com.busstopalarm;

import java.util.Iterator;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

public class PolylineOverlay extends Overlay {
	private Polyline polyline; // Contains set of points to be connected.
	private Paint pathPaint = null; // Paint tool that is used to draw on the map canvas.

	/**
	 * Constructor for PolylineOverlay sets the given polyline
	 * to be the one that is drawn in the overlay.
	 * @param polyline The Polyline object to be overlayed.
	 */
	public PolylineOverlay(Polyline polyline) {
		super();		
		this.polyline = polyline;
		this.pathPaint = new Paint(); 
        this.pathPaint.setAntiAlias(true);
	}
	
	/**
	 * Draws the polyline route on the map the this overlay belongs to.
	 */
	@Override
	public void draw(Canvas canvas, MapView mView, boolean shadow) {
		super.draw(canvas, mView, shadow);
		
		// Reset our paint. 
        this.pathPaint.setStrokeWidth(4); 
        this.pathPaint.setARGB(100, 113, 105, 252); 
        this.pathPaint.setStyle(Paint.Style.STROKE); 
        
		Projection projection = mView.getProjection();
		Path routePath = new Path();
		
		// Add each point to the routePath.
		Iterator<GeoPoint> it = polyline.iterator();
		
		Point first = null;
		first = projection.toPixels(it.next(), first);
		routePath.moveTo(first.x, first.y);
		
		while (it.hasNext()) {
			Point outPoint = null;
			outPoint = projection.toPixels(it.next(), outPoint);
			//if (isOnScreen(canvas, outPoint)) {
				//Log.d("tag", outPoint.toString());
				routePath.lineTo(outPoint.x, outPoint.y);
			//} else { 
				routePath.moveTo(outPoint.x, outPoint.y);
			//}
		}
		canvas.drawPath(routePath, pathPaint);
	}
	
	/**
	 * Checks if the point is on screen.
	 * @param cv
	 * @param p
	 * @return true if point is on screen, otherwise false.
	 */
	private boolean isOnScreen(Canvas cv, Point p) {
		return p.x >= 0 && p.y >= 0 && p.x < cv.getWidth() && p.y < cv.getHeight();
	}
}
