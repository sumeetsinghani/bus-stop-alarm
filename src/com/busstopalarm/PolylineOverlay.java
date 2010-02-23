/**
 * Draws an overlay containing a route on the containing Map.
 * 
 * CAUTION: NOT YET FULLY TESTED.
 * 
 * @author Michael Eng
 */

package com.busstopalarm;

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
		for(GeoPoint inPoint : polyline) {
			Point outPoint = null;
			outPoint = projection.toPixels(inPoint, outPoint);
			routePath.lineTo(outPoint.x, outPoint.y);
		}
		
		canvas.drawPath(routePath, pathPaint);
	}
}
