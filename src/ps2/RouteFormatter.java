package ps2;

import java.util.Iterator;
import java.text.*;


/**
 * <p>
 * A RouteFormatter class knows how to create a textual description of
 * directions from one location to another.  The class is abstract to
 * support different textual descriptions.
 * </p>
 *
 * <p>

 * These classes may be thought of as <i>views</i> on the Route model.
 * (see <a
 * href="http://java.sun.com/blueprints/patterns/MVC-detailed.html">Sun's
 * buzzword-filled explanation</a> of this design pattern)
 * </p>
 **/
public abstract class RouteFormatter {

    /**
     * <p>
     * Give directions for following this Route, starting at its
     * start point and facing in the specified heading.
     * </p>
     *
     * <p>
     * This method should call <tt>computeLine</tt> for each
     * geographical feature in this route and concatenate the results
     * into a single String.
     * </p>
     *
     * @requires 0 &le; heading &lt; 360 && route != null
     * @param route The route for which to print directions.
     * @param heading The initial heading.
     * @return A newline-terminated directions <tt>String</tt> giving
     * human-readable directions from start to end along this route.
     **/
    public String computeDirections(Route route, double heading) {
        String result = "";
    	for(GeoFeature gf : route.getGeoFeatures()){
    		result += computeLine(gf, heading);
        	heading = gf.getEndHeading();
        }
    	return result;
    }


    /**
     * Computes a single line of a multi-line directions String that
     * represents the intructions for traversing a single geograhpical
     * feature.
     *
     * @requires geoFeature != null
     * @param geoFeature The geographical feature to traverse.
     * @param origHeading The initial heading
     * @return A newline-terminated <tt>String</tt> that gives
     * directions on how to traverse this geographical feature.
     **/
    public abstract String computeLine(GeoFeature geoFeature,
                                       double origHeading);


    /**
     * Computes directions to turn based on the heading change.  Let
     * a be the angle from the original heading to the new heading (that
     * is, a is the difference between the two headings).
     * The turn should be annotated as:<p>
     *
     * <pre>
     * Continue             if a &lt; 10
     * Turn slight right    if 10 &le; a &lt; 60
     * Turn right           if 60 &le; a &lt; 120
     * Turn sharp right     if 120 &le; a &lt; 179
     * U-turn               if 179 &le; a
     * </pre>
     *
     * and likewise for left turns.
     *
     * @requires 0 &le; origHeading &lt; 360
     * @requires 0 &le; newHeading &lt; 360
     * @param origHeading the start heading
     * @param newHeading the desired new heading
     * @return English directions to go from the old heading to the
     * new one.
     */
    protected String getTurnString(double origHeading, double newHeading) {
        double diff = newHeading - origHeading;
        if(diff > 180){
        	diff -= 360;
        }else if(diff < -180){
        	diff += 360;
        }
        
        
        String result;
        if(diff < 0){
        	result = "left ";
        }else{
        	result = "right ";
        }
        
        diff = Math.abs(diff);
        if(diff < 10){
        	result = "Continue ";
        }else if(diff < 60){
        	result = "Turn slight " +result;
        }else if(diff < 120){
        	result = "Turn " +result;
        }else if(diff < 179){
        	result = "Turn sharp " +result;
        }else{
        	result = "U-turn ";
        }
        return result;
    }
}
