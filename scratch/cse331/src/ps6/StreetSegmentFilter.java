package ps6;

import ps4.*;

/**
 * Interface for Street Segment filtering
 */
public interface StreetSegmentFilter {

  /**
   *  Determine if the segment is accepted by the filter
   *
   *  @param seg a StreetSegment candidate
   *  @return true iff the filter accepts seg
   */
  public boolean apply(StreetSegment seg);
}
