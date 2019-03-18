package common.datatypes.map;

import java.io.Serializable;
import java.util.ArrayList;
import common.datatypes.Waypoint;
import common.datatypes.map.griddedMap.GridDesign;
import common.datatypes.map.griddedMap.GriddedMap;

/**
 * Over arching data structure for all forms of mapping data. Contains the collection of LiDAR
 * returns to date (layers) along with the Gridded / Amalgamated Map abstract structure
 * 
 * @author David Avery 15823926
 * 
 * @version 1.0
 * @since 2018-04-07
 * 
 * @see common.datatypes.map.griddedMap.GriddedMap
 * @see common.datatypes.map.MapLayer
 * @see common.objects.Member#lSense()
 * @see common.datatypes.map.griddedMap.GridDesign
 * 
 */
public class Map implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -2190687313705793008L;
  private ArrayList<MapLayer> layers = new ArrayList<MapLayer>();
  private GriddedMap amalgamatedMap;
  private int gridSize;

  /**
   * Default Constructor
   * 
   * @param layer The initial LiDAR return (never created empty)
   * 
   */
  public Map(MapLayer layer) {
    this(64, layer); //if no gridsize is defines, assume default of 64
  } 
  
  /**
   * Constructor
   * 
   * @param gridSize The number of subitem (Vertices) to store in both x and y dimensions
   * @param layer The initial LiDAR return (never created empty)
   * 
   */
  public Map(int gridSize, MapLayer layer) {
    this.gridSize = gridSize;
    this.addLayer(layer);
  }

  /**
   * Access method for retrieving a single LiDAR return from the collection
   * 
   * @see common.objects.Member#lSense()
   *
   * @param index The numerical index of the layer requested
   * 
   * @return The MapLayer from the collection
   * 
   */
  public MapLayer getLayer(int index) {
    return layers.get(index);// FIXME possible IOOB
  }

  /**
   * Adds the supplied LiDAR return to the collection and calls amalgamateLayer to produce / update
   * the griddedMap
   * 
   * @see common.datatypes.map.griddedMap.GriddedMap
   *
   * @param layer The LiDAR return date to be added to the collection and combined map
   * 
   */
  public void addLayer(MapLayer layer) {
    // transform layer??
    layers.add(layer);
    amalgamateLayer(layer);
  }

  /**
   * Header function for adding data to the griddedMap
   * 
   * @see common.objects.Member#lSense()
   * @see common.datatypes.map.griddedMap.GriddedMap
   * @see common.datatypes.map.griddedMap.Vertex
   *
   * @param layer The date (LiDAR return) to be added
   * 
   */
  private void amalgamateLayer(MapLayer layer) {
    if (amalgamatedMap == null) {
      amalgamatedMap = new GriddedMap(GridDesign.TETRA, gridSize, this);
    }
    for (Waypoint w : layer.addOpens()) {
      // add cell info to amalgamatedMap in correct loc
      amalgamatedMap.add(w);
    }
  }

  /**
   * Access method for retrieving the griddedMap
   * 
   * @return The entire GriddedMap data structure
   * 
   */
  public GriddedMap getAmalgamatedMap() {
    return amalgamatedMap;
  }

}
