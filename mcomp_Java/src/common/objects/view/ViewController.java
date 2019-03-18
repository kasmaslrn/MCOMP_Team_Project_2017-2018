package common.objects.view;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import common.datatypes.Waypoint;
import common.datatypes.map.griddedMap.GriddedMap;
import common.datatypes.map.griddedMap.Vertex;
import common.datatypes.path.Path;
import common.interfaces.RemoteLeader;
import common.interfaces.RemoteView;
import common.objects.Herd;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * 
 * @author Harry Jackson: 14812630.
 * @author David Avery 15823926
 * @author Stephen Pope 15836791
 * 
 * @version 1.0
 * @since 2018-04-11
 * 
 */
public class ViewController {

  /**
   * 
   */
  private static final long serialVersionUID = -8788920841067227780L;

  private static final Logger LOGGER = Logger.getLogger(ViewController.class.getName());

  // private RemoteLeader localLeaderRef = null;
  // private Herd localHerdData;
  // private Boolean checkDest = false;
  // private Label label;
  // private Button killButton, killButton2, killButton3, killButton4;
  // private VBox vbox;
  // private int counter = 0;
  // private Pane pane;
  private Group lidarGroup, blockedGroup, lineGroup, amalgamateGroup, pathGroup, searchedGroup,
      optimisedGroup;

  private Stage stage;
  private static Herd localHerdData = null;

  private String title;

  @FXML
  AnchorPane anchorPane;
//  @FXML
//  Spinner<Integer> liDARSpinner = new Spinner<Integer>();
  
  @FXML
  Spinner<Integer> liDARSpinner;

  private void initSpinner() {
    liDARSpinner.setValueFactory(
          new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10)
      );
  }
  
  public void updateSpinnerValue(Integer newValue) {
    liDARSpinner.getValueFactory().setValue(newValue);
}

  final int initialValue = 3;

  public ViewController() {
    LOGGER.log(Level.SEVERE, "ViewController stub constructor called");
    title = title + "stub";
  } // FIXME change scope?

  public ViewController(Herd h) {
    LOGGER.log(Level.SEVERE, "ViewController main constructor called");
    title = title + "main";
    this.localHerdData = h;
  }

  public void startGUI(Stage s) {
    this.stage = s;
    this.stage.setTitle(title);
    // this.stage.setTitle("CI390 Herd GUI");
    Parent root;
    try {
      root = FXMLLoader.load(getClass().getResource("./gui.fxml"));
      Scene scene = new Scene(root);
      this.stage.setScene(scene);
      this.stage.show();
    } catch (IOException e) {
      e.printStackTrace();
      LOGGER.log(Level.SEVERE, "Can't find the GUI FXML file!");
      anchorPane.getChildren().add(lidarGroup);
    }
  }


  @FXML
  private void toggleLiDARLayer(Event e) {
    lidarGroup = new Group(); // init new empty group
    Circle c;
    for (Waypoint w : localHerdData.getMap().getLayer(0).transform(0, 0, 0, 4)) {// FIXME
                                                                                          // ref
                                                                                          // spinner
                                                                                          // val
      c = new Circle((w.getX()+400), (w.getY()+400), 2, Color.RED);
      lidarGroup.getChildren().add(c);
    }
    LOGGER.log(Level.SEVERE, "Return added");
    anchorPane.getChildren().add(lidarGroup);
  }

  public void notifyOfChange(Herd h) {
    LOGGER.log(Level.SEVERE, "Herd update pushed to " + title);
    localHerdData = h;
  }



  // /**
  // * Takes in all methods that deal with drawing to the GUI and adds them to the new HBox. Adds
  // the
  // * HBox to a new pane. Finally the pane is displayed as a new scene for the current stage.
  // *
  // * @param Stage
  // *
  // */
  // @Override
  // public void start(Stage primaryStage) throws Exception {
  //
  // // rmi connect stuff
  // localLeaderRef = connectRMI();
  // if (localLeaderRef == null) {
  //
  // Alert runtimeDialogue = new Alert(AlertType.CONFIRMATION);
  // runtimeDialogue.setContentText("Unable to connect!");
  // Optional<ButtonType> result = runtimeDialogue.showAndWait();
  // throw new RuntimeException("Unable to connect to Leader");
  // }
  //
  // localHerdData = localLeaderRef.getState();
  //
  //
  // Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
  // primaryStage.setX(screenBounds.getMinX());
  // primaryStage.setY(screenBounds.getMinY());
  // primaryStage.setWidth(screenBounds.getWidth());
  // primaryStage.setHeight(screenBounds.getHeight());
  //
  // HBox hbox = new HBox();
  // hbox.getChildren().addAll(getMapBox(), getVBox());
  // pane = new Pane();
  // hbox.setSpacing(8);
  // hbox.setPadding(new Insets(8, 8, 8, 8));
  // pane.getChildren().addAll(hbox);
  // Scene scene = new Scene(pane);
  // primaryStage.setScene(scene);
  // primaryStage.show();
  //
  // }
  //
  // /**
  // * Creates the main box for displaying all data. Has onClick for setting destination.
  // *
  // * @return main Rectangle node to display all data.
  // *
  // */
  // public Rectangle getMapBox() {
  // Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
  // Rectangle r = new Rectangle();
  // r.setX(screenBounds.getMinX());
  // r.setY(screenBounds.getMinY());
  // r.setWidth((screenBounds.getWidth() - r.getStrokeWidth()) / 1.25);
  // r.setHeight(screenBounds.getHeight() - r.getStrokeWidth() - 40);
  // r.setFill(Color.TRANSPARENT);
  // r.setStroke(Color.BLACK);
  // r.setStrokeWidth(4);
  // r.setOnMouseClicked(new EventHandler<MouseEvent>() {
  // @Override
  // public void handle(MouseEvent event) {
  // if (counter == 0) {
  // System.out.print(event.getSceneX() + "," + event.getSceneY());
  // Waypoint w = new Waypoint(event.getSceneX(), event.getSceneY());
  // try {
  // localLeaderRef.setDestination(w);
  // } catch (RemoteException e) {
  // // TODO Auto-generated catch block
  // e.printStackTrace();
  // }
  // Circle dest = new Circle();
  // Pane pane2 = new Pane();
  // dest.setCenterX(event.getSceneX());
  // dest.setCenterY(event.getSceneY());
  // dest.setRadius(5d);
  // dest.setFill(Color.RED);
  // Label label = new Label("(" + event.getSceneX() + "," + event.getSceneY() + ")");
  // label.setLayoutX(event.getSceneX());
  // label.setLayoutY(event.getSceneY());
  // pane2.getChildren().addAll(dest, label);
  // pane.getChildren().addAll(pane2);
  // checkDest = true;
  // counter++;
  // } else {
  // pane.getChildren().remove(1);
  // Pane pane2 = new Pane();
  // Circle dest = new Circle();
  // dest.setCenterX(event.getSceneX());
  // dest.setCenterY(event.getSceneY());
  // dest.setRadius(5d);
  // dest.setFill(Color.RED);
  // Label label = new Label("(" + event.getSceneX() + "," + event.getSceneY() + ")");
  // label.setLayoutX(event.getSceneX());
  // label.setLayoutY(event.getSceneY());
  // pane2.getChildren().addAll(dest, label);
  // pane.getChildren().addAll(pane2);
  //
  // }
  // }
  // });
  // return r;
  // }
  //
  // /**
  // * Takes in all current VBox's (getVBoxMap, getVBoxPath ad getAbilities). Adds all to a new VBox
  // *
  // * @return List of VBox's
  // *
  // */
  // public VBox getVBox() {
  // vbox = new VBox();
  // vbox.setSpacing(15);
  // Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
  // vbox.setPrefWidth((screenBounds.getWidth() / 5) - 30);
  // vbox.setPrefHeight(screenBounds.getHeight() - 40);
  // ObservableList<Node> list = vbox.getChildren();
  // list.addAll(getVBoxMap(), getVBoxPath(), getMembers(), pathAndMove());
  // return vbox;
  // }
  //
  //
  // /**
  // * Handles all GUI nodes to do with Map data, sets their size, position and CSS. Used for
  // * displaying Map data on GUI.
  // *
  // * @see common.datatypes.map.Map
  // * @see common.datatypes.map.griddedMap.Vertex
  // * @see common.datatypes.map.griddedMap.GriddedMap
  // *
  // *
  // * @return VBox for displaying all nodes associated with Map data.
  // *
  // */
  // public VBox getVBoxMap() {
  // VBox vboxMap = new VBox();
  // HBox hboxBtn = new HBox();
  // HBox hboxBtn2 = new HBox();
  //
  // vboxMap.setStyle("-fx-background-color:#ffffff;" + "-fx-border-width: 4px 4px 4px 4px;"
  // + "-fx-border-color: #000000;");
  //
  // vboxMap.setPrefWidth(vbox.getPrefWidth());
  // vboxMap.setPrefHeight((vbox.getPrefHeight() / 4) - 8);
  // vboxMap.setSpacing(2);
  //
  // label = new Label("Map");
  // label.setMinHeight(vboxMap.getPrefHeight() / 4);
  // label.setMaxWidth(vbox.getPrefWidth());
  // toggleOffStyle(label);
  //
  // Spinner<Integer> spinner = new Spinner<Integer>();
  // SpinnerValueFactory<Integer> value = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5);
  // spinner.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_VERTICAL);
  // spinner
  // .setStyle("-fx-body-color:#00bfff;" + "-fx-font-size: 10px; " + "-fx-font-weight: bold;");
  //
  // spinner.setMaxHeight(vboxMap.getPrefHeight() / 4);
  // spinner.setMaxWidth(vboxMap.getPrefWidth() / 14);
  // spinner.setValueFactory(value);
  //
  // /**
  // * Button for iteratively displaying Waypoints from LiDar return.
  // */
  // ToggleButton lidarBtn = new ToggleButton("LiDar");
  // lidarBtn.setMinWidth((vboxMap.getPrefWidth() / 2) - 46);
  // lidarBtn.setMinHeight(vboxMap.getPrefHeight() / 4);
  // toggleOffStyle(lidarBtn);
  // setTooltip(lidarBtn, "Displays a single LiDar return");
  // lidarBtn.setOnAction(event -> {
  // if (lidarBtn.isSelected()) {
  // toggleOnStyle(lidarBtn);
  // pane.getChildren().add(
  // drawLidarLayer(localHerdData.getMap().getLayer(spinner.getValue()).getWaypoints()));
  // } else {
  // toggleOffStyle(lidarBtn);
  // pane.getChildren().remove(lidarGroup);
  // }
  // });
  //
  // /**
  // * Button for displaying Blocked Vertices.
  // */
  // ToggleButton blockedBtn = new ToggleButton("Blocked");
  // blockedBtn.setMinWidth((vboxMap.getPrefWidth() / 2) - 16);
  // blockedBtn.setMinHeight(vboxMap.getPrefHeight() / 4);
  // setTooltip(blockedBtn, "Displays all Blocked Vertices");
  // toggleOffStyle(blockedBtn);
  // blockedBtn.setOnAction(event -> {
  // if (blockedBtn.isSelected()) {
  // toggleOnStyle(blockedBtn);
  // //
  // pane.getChildren().add(drawBlockedVertices(parent.getLocalHerdData().getMap().getAmalgamatedMap().getGrid()));
  // } else {
  // toggleOffStyle(blockedBtn);
  // // pane.getChildren().remove(blockedGroup);
  // }
  // });
  //
  // /**
  // * Button for displaying Grid overlay.
  // */
  // ToggleButton gridBtn = new ToggleButton("Grid");
  // gridBtn.setMinWidth((vboxMap.getPrefWidth() / 2) - 16);
  // gridBtn.setMinHeight(vboxMap.getPrefHeight() / 4);
  // setTooltip(gridBtn, "Displays Grid Overlay");
  // toggleOffStyle(gridBtn);
  // gridBtn.setOnAction(event -> {
  // if (gridBtn.isSelected()) {
  // toggleOnStyle(gridBtn);
  // // pane.getChildren().add(drawGrid(scale(TestData.getPresentationMaze(), 40)));
  // } else {
  // toggleOffStyle(gridBtn);
  // // pane.getChildren().remove(lineGroup);
  // }
  // });
  //
  // /**
  // * Button for displaying Amalgamated Map.
  // */
  // ToggleButton mapBtn = new ToggleButton("Map");
  // mapBtn.setMinWidth((vboxMap.getPrefWidth() / 2) - 16);
  // mapBtn.setMinHeight(vboxMap.getPrefHeight() / 4);
  // setTooltip(mapBtn, "Displays Amalgamated Map");
  // toggleOffStyle(mapBtn);
  // mapBtn.setOnAction(event -> {
  // if (mapBtn.isSelected()) {
  // toggleOnStyle(mapBtn);
  // pane.getChildren().add(drawAmalgamatedMap(localHerdData.getMap().getAmalgamatedMap()));
  // } else {
  // toggleOffStyle(mapBtn);
  // pane.getChildren().remove(amalgamateGroup);
  // }
  // });
  //
  // hboxBtn.setSpacing(8);
  // hboxBtn2.setSpacing(8);
  // hboxBtn.setPadding(new Insets(8, 8, 8, 8));
  // hboxBtn2.setPadding(new Insets(8, 8, 8, 8));
  // hboxBtn.getChildren().addAll(lidarBtn, spinner, blockedBtn);
  // hboxBtn2.getChildren().addAll(gridBtn, mapBtn);
  //
  // ObservableList<Node> list = vboxMap.getChildren();
  // list.addAll(label, hboxBtn, hboxBtn2);
  // return vboxMap;
  // }
  //
  // /**
  // * Handles all GUI nodes to do with Pathfinding, sets their size, position and CSS. Used for
  // * displaying Pathfinding data on GUI.
  // *
  // * @see pathfinding.AStar
  // * @see pathfinding.Heuristic
  // * @see pathfinding.PathOptimisation
  // *
  // *
  // * @return VBox for displaying all nodes associated with Pathfinding and Heuristics.
  // *
  // */
  // public VBox getVBoxPath() {
  // VBox vboxPath = new VBox();
  // HBox hboxBtn = new HBox();
  // HBox hboxBtn2 = new HBox();
  //
  // vboxPath.setStyle("-fx-background-color:#ffffff;" + "-fx-border-width: 4px 4px 4px 4px;"
  // + "-fx-border-color: #000000;");
  // vboxPath.setPrefWidth(vbox.getPrefWidth());
  // vboxPath.setPrefHeight((vbox.getPrefHeight() / 4));
  // vboxPath.setSpacing(2);
  //
  // label = new Label("Pathfinding");
  // label.setMinHeight(vboxPath.getPrefHeight() / 4);
  // label.setMaxWidth(vbox.getPrefWidth());
  // toggleOffStyle(label);
  //
  // /**
  // * Button for displaying Path retrieved from AStar search.
  // */
  // ToggleButton pathBtn = new ToggleButton("Path");
  // pathBtn.setMinWidth((vboxPath.getPrefWidth() / 2) - 16);
  // pathBtn.setMinHeight(vboxPath.getPrefHeight() / 4);
  // setTooltip(pathBtn, "Displays the found Path");
  // toggleOffStyle(pathBtn);
  // pathBtn.setOnAction(event -> {
  // if (pathBtn.isSelected()) {
  // toggleOnStyle(pathBtn);
  // pane.getChildren().add(drawPath(pathToVerts(localHerdData.unoptimizedPath)));
  // } else {
  // toggleOffStyle(pathBtn);
  // pane.getChildren().remove(pathGroup);
  // }
  // });
  //
  // /**
  // * Button for displaying all searched nodes from AStar search.
  // */
  // ToggleButton searchedBtn = new ToggleButton("Searched");
  // searchedBtn.setMinWidth((vboxPath.getPrefWidth() / 2) - 16);
  // searchedBtn.setMinHeight(vboxPath.getPrefHeight() / 4);
  // setTooltip(searchedBtn, "Displays all Vertices scored");
  // toggleOffStyle(searchedBtn);
  // searchedBtn.setOnAction(event -> {
  // if (searchedBtn.isSelected()) {
  // toggleOnStyle(searchedBtn);
  // pane.getChildren().add(drawSearched(localHerdData.searchedNodes));
  // } else {
  // toggleOffStyle(searchedBtn);
  // pane.getChildren().remove(searchedGroup);
  // }
  // });
  //
  // /**
  // * Button for displaying Optimised Path.
  // */
  // ToggleButton optimisedPathBtn = new ToggleButton("Optimised");
  // optimisedPathBtn.setMinWidth((vboxPath.getPrefWidth() / 2) - 16);
  // optimisedPathBtn.setMinHeight(vboxPath.getPrefHeight() / 4);
  // setTooltip(optimisedPathBtn, "Displays the Optimised path found");
  // toggleOffStyle(optimisedPathBtn);
  // optimisedPathBtn.setOnAction(event -> {
  // if (optimisedPathBtn.isSelected()) {
  // toggleOnStyle(optimisedPathBtn);
  // pane.getChildren().add(drawOptimisedPath(pathToVerts(localHerdData.optimizedPath)));
  //
  // } else {
  // toggleOffStyle(optimisedPathBtn);
  // pane.getChildren().remove(optimisedGroup);
  // }
  // });
  //
  // /**
  // * Button for displaying specialised Heuristic.
  // */
  // ToggleButton heuristicBtn = new ToggleButton("Heuristic");
  // heuristicBtn.setMinWidth((vboxPath.getPrefWidth() / 2) - 16);
  // heuristicBtn.setMinHeight(vboxPath.getPrefHeight() / 4);
  // setTooltip(heuristicBtn, "Displays Straight Line Heuristic");
  // toggleOffStyle(heuristicBtn);
  // heuristicBtn.setOnAction(event -> {
  // if (heuristicBtn.isSelected()) {
  // toggleOnStyle(heuristicBtn);
  // } else {
  // toggleOffStyle(heuristicBtn);
  // }
  // });
  //
  // hboxBtn.setSpacing(8);
  // hboxBtn2.setSpacing(8);
  // hboxBtn.setPadding(new Insets(8, 8, 8, 8));
  // hboxBtn2.setPadding(new Insets(8, 8, 8, 8));
  //
  // hboxBtn.getChildren().addAll(pathBtn, searchedBtn);
  // hboxBtn2.getChildren().addAll(optimisedPathBtn, heuristicBtn);
  //
  // ObservableList<Node> list = vboxPath.getChildren();
  // list.addAll(label, hboxBtn, hboxBtn2);
  // return vboxPath;
  // }
  //
  // /**
  // * Handles all GUI nodes to do with Members abilities within a Herd. Used for displaying herd
  // * Abilities on GUI.
  // *
  // * @see member.MemberMain
  // *
  // * @return VBox Layout.
  // *
  // */
  // public VBox getMembers() {
  // VBox vboxMember = new VBox();
  // HBox hboxBtn = new HBox();
  // HBox hboxBtn2 = new HBox();
  //
  // vboxMember.setPrefWidth((vbox.getPrefWidth()));
  // vboxMember.setPrefHeight(vbox.getPrefHeight() / 3);
  // vboxMember.setStyle("-fx-background-color:#ffffff;" + "-fx-border-width: 4px 4px 4px 4px;"
  // + "-fx-border-color: #000000;");
  // vboxMember.setSpacing(2);
  //
  // label = new Label("Members");
  // label.setMaxWidth(vbox.getPrefWidth());
  // label.setMinHeight(vboxMember.getPrefHeight() / 6.5);
  // toggleOffStyle(label);
  //
  // Rectangle r = new Rectangle();
  // r.setWidth(vboxMember.getPrefWidth() - 30);
  // r.setHeight((vboxMember.getPrefHeight())/2.75);
  // r.setX(11);
  // r.setY(0);
  // r.setStroke(Color.BLACK);
  // r.setStrokeWidth(4);
  // r.setFill(Color.WHITE);
  //
  // Pane p = new Pane();
  // p.getChildren().add(r);
  //
  // ToggleButton memberBtn = new ToggleButton("Member 1");
  // memberBtn.setMinWidth((vboxMember.getPrefWidth() / 3) - 24);
  // memberBtn.setMinHeight(vboxMember.getPrefHeight() / 6.5);
  // toggleOffStyle(memberBtn);
  // memberBtn.setOnAction(event -> {
  // if (memberBtn.isSelected()) {
  // toggleOnStyle(memberBtn);
  // try {
  // Text t = new Text(localHerdData.getMembers().get(0).getAbilities().toString());
  // killButton = new Button("1");
  // killButton.setMaxHeight(vboxMember.getPrefHeight() / 6.5);
  // killButton.setMinWidth(vboxMember.getPrefWidth() / 7);
  // toggleOffStyle(killButton);
  // hboxBtn.getChildren().add(killButton);
  // killMember(killButton, memberBtn, hboxBtn, p, t);
  // } catch (RemoteException e) {
  // // TODO Auto-generated catch block
  // e.printStackTrace();
  // }
  // } else {
  // toggleOffStyle(memberBtn);
  // removeFromHBox(killButton, hboxBtn);
  // }
  // });
  //
  // ToggleButton memberBtn2 = new ToggleButton("Member 2");
  // memberBtn2.setMinWidth((vboxMember.getPrefWidth() / 3) - 24);
  // memberBtn2.setMinHeight(vboxMember.getPrefHeight() / 6.5);
  // toggleOffStyle(memberBtn2);
  // memberBtn2.setOnAction(event -> {
  // if (memberBtn2.isSelected()) {
  // toggleOnStyle(memberBtn2);
  // try {
  // Text t = new Text(localHerdData.getMembers().get(1).getAbilities().toString());
  // t.setX(20);
  // t.setY(50);
  // p.getChildren().add(t);
  // killButton2 = new Button("2");
  // killButton2.setMaxHeight(vboxMember.getPrefHeight() / 6.5);
  // killButton2.setMinWidth(vboxMember.getPrefWidth() / 7);
  // toggleOffStyle(killButton2);
  // hboxBtn.getChildren().add(killButton2);
  // killMember(killButton2, memberBtn2, hboxBtn, p, t);
  // } catch (RemoteException e) {
  // // TODO Auto-generated catch block
  // e.printStackTrace();
  // }
  // } else {
  // toggleOffStyle(memberBtn2);
  // removeFromHBox(killButton2, hboxBtn);
  // }
  // });
  //
  // ToggleButton memberBtn3 = new ToggleButton("Member 3");
  // memberBtn3.setMinWidth((vboxMember.getPrefWidth() / 3) - 24);
  // memberBtn3.setMinHeight(vboxMember.getPrefHeight() / 6.5);
  // toggleOffStyle(memberBtn3);
  // memberBtn3.setOnAction(event -> {
  // if (memberBtn3.isSelected()) {
  // toggleOnStyle(memberBtn3);
  // try {
  // Text t = new Text(localHerdData.getMembers().get(2).getAbilities().toString());
  // t.setX(120);
  // t.setY(30);
  // p.getChildren().add(t);
  // killButton3 = new Button("3");
  // killButton3.setMaxHeight(vboxMember.getPrefHeight() / 6.5);
  // killButton3.setMinWidth(vboxMember.getPrefWidth() / 7);
  // toggleOffStyle(killButton3);
  // hboxBtn2.getChildren().add(killButton3);
  // killMember(killButton3, memberBtn3, hboxBtn2, p, t);
  // } catch (RemoteException e) {
  // // TODO Auto-generated catch block
  // e.printStackTrace();
  // }
  // } else {
  // toggleOffStyle(memberBtn3);
  // removeFromHBox(killButton3, hboxBtn2);
  // }
  // });
  //
  // ToggleButton memberBtn4 = new ToggleButton("Member 4");
  // memberBtn4.setMinWidth((vboxMember.getPrefWidth() / 3) - 24);
  // memberBtn4.setMinHeight(vboxMember.getPrefHeight() / 6.5);
  // toggleOffStyle(memberBtn4);
  // memberBtn4.setOnAction(event -> {
  // if (memberBtn4.isSelected()) {
  // toggleOnStyle(memberBtn4);
  // try {
  // Text t = new Text(localHerdData.getMembers().get(3).getAbilities().toString());
  // t.setX(120);
  // t.setY(50);
  // p.getChildren().add(t);
  // killButton4 = new Button("4");
  // killButton4.setMaxHeight(vboxMember.getPrefHeight() / 6.5);
  // killButton4.setMinWidth(vboxMember.getPrefWidth() / 7);
  // toggleOffStyle(killButton4);
  // hboxBtn2.getChildren().add(killButton4);
  // killMember(killButton4, memberBtn4, hboxBtn2, p, t);
  // } catch (RemoteException e) {
  // // TODO Auto-generated catch block
  // e.printStackTrace();
  // }
  // } else {
  // toggleOffStyle(memberBtn4);
  // removeFromHBox(killButton4, hboxBtn2);
  // }
  // });
  //
  // hboxBtn.getChildren().addAll(memberBtn, memberBtn2);
  // hboxBtn2.getChildren().addAll(memberBtn3, memberBtn4);
  // hboxBtn.setSpacing(8);
  // hboxBtn2.setSpacing(8);
  // hboxBtn.setPadding(new Insets(8, 8, 8, 8));
  // hboxBtn2.setPadding(new Insets(8, 8, 8, 8));
  //
  // ObservableList<Node> list = vboxMember.getChildren();
  // list.addAll(label, hboxBtn, hboxBtn2, p);
  //
  // return vboxMember;
  // }
  //
  //
  // /**
  // * Vbox layout for handling the GO and pathfinding buttons
  // * Pathfinding button deals with pathfinding and returning a path
  // * Go button tell the bots to start moving to the destination.
  // *
  // * @return Vbox
  // */
  // public VBox pathAndMove() {
  // VBox vbox = new VBox();
  // Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
  // vbox.setPrefWidth((screenBounds.getWidth() / 5) - 30);
  // vbox.setMinHeight(screenBounds.getHeight() / 14);
  // vbox.setStyle("-fx-background-color:#ffffff;" + "-fx-border-width: 4px 4px 4px 4px;"
  // + "-fx-border-color: #000000;");
  //
  // Button pathfindBtn = new Button("Pathfind");
  // pathfindBtn.setStyle("-fx-background-color: #e8ff66;" + "-fx-font-size: 2em;" +
  // "-fx-font-weight: bold;" + "-fx-border-width: 4px 4px 4px 4px;" + "-fx-border-color:
  // #000000;");
  // pathfindBtn.setMaxWidth(vbox.getPrefWidth());
  // pathfindBtn.setMinHeight(vbox.getPrefHeight());
  // setTooltip(pathfindBtn, "Once clicked the pathfinding will begin to run");
  // pathfindBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
  //
  // // checks if a destination has been set on the Map. if true the algorithm starts pathfinding.
  // @Override
  // public void handle(MouseEvent event) {
  // if(checkDest == false) {
  // Alert runtimeDialogue = new Alert(AlertType.WARNING);
  // runtimeDialogue.setContentText("No destination has been set! To set a location click on the
  // Map.");
  // Optional<ButtonType> result = runtimeDialogue.showAndWait();
  // }else {
  // try {
  // localHerdData.getLeader().pathfind();
  // } catch (RemoteException e) {
  // // TODO Auto-generated catch block
  // e.printStackTrace();
  // }
  // }
  // }
  // });
  //
  // Button goBtn = new Button("GO");
  // goBtn.setStyle("-fx-background-color: #00ff00;" + "-fx-font-size: 2em;" + "-fx-font-weight:
  // bold;" + "-fx-border-width: 4px 4px 4px 4px;" + "-fx-border-color: #000000;");
  // goBtn.setMaxWidth(vbox.getPrefWidth());
  // goBtn.setMaxHeight(10);
  // setTooltip(goBtn, "Once clicked the robots will start to move to destination");
  // goBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
  // @Override
  // public void handle(MouseEvent event) {
  // // TODO Auto-generated method stub
  // try {
  // if(localHerdData.destReached() == false) { // if pathfinding could not reach destination
  // Alert dialogue = new Alert(AlertType.CONFIRMATION);
  // dialogue.setContentText("Destination was not reached! Would you like to Pathfind again?");
  // Optional<ButtonType> result = dialogue.showAndWait();
  // if (result.get() == ButtonType.OK) {
  // try {
  // localHerdData.getLeader().pathfind();
  // } catch (NumberFormatException | RemoteException e) {
  // // TODO Auto-generated catch block
  // e.printStackTrace();
  // }
  // }
  // else if(result.get() == ButtonType.CANCEL) {
  // dialogue.close();
  // }
  // }
  //
  // else { // if destination is reached start robot
  // Alert dialogue = new Alert(AlertType.CONFIRMATION);
  // dialogue.setContentText("Do you want to start the Robot?");
  // Optional<ButtonType> result = dialogue.showAndWait();
  // if (result.get() == ButtonType.OK) {
  // localHerdData.getLeader().go();
  // }
  // else if(result.get() == ButtonType.CANCEL) {
  // dialogue.close();
  // }
  // }
  // } catch (RemoteException e) {
  // // TODO Auto-generated catch block
  // e.printStackTrace();
  // }
  // }
  //
  // });
  // vbox.setSpacing(10);
  // vbox.setPadding(new Insets(8, 8, 8, 8));
  // vbox.getChildren().addAll(pathfindBtn, goBtn);
  // return vbox;
  // }
  //
  //
  // /**
  // * When called in it allows the user to move nodes around the stage.
  // *
  // * @param event
  // */
  // public void drag(MouseEvent event) {
  // Node n = (Node) event.getSource();
  // n.setTranslateY(n.getTranslateY() + event.getY());
  // }
  //
  //
  // /**
  // *
  // * Method handles the removal of a member from the herd
  // * GUI needs to be updated such that it reflects this change.
  // * Button for controlling member is removed as it no longer handles any functionality.
  // *
  // * @param a
  // * @param b
  // * @param h
  // * @param p
  // * @param t
  // */
  // public void killMember(Button a, ToggleButton b, HBox h, Pane p, Text t) {
  // a.setOnMouseClicked(new EventHandler<MouseEvent>() {
  // @Override
  // public void handle(MouseEvent event) {
  // Alert killMember = new Alert(AlertType.CONFIRMATION);
  // killMember.setTitle("Remove " + b.getText());
  // killMember.setHeaderText(b.getText());
  // killMember.setContentText("Are you sure you want to remove Member?");
  //
  // Optional<ButtonType> result = killMember.showAndWait();
  // if (result.get() == ButtonType.OK) {
  // try {
  // localHerdData.getMembers().get(Integer.parseInt(b.getText()))
  // .kill("Member: " + b.getText() + "removed");
  // } catch (NumberFormatException | RemoteException e) {
  // // TODO Auto-generated catch block
  // e.printStackTrace();
  // }
  // p.getChildren().remove(t);
  // } else {
  // killMember.close();
  // }
  // }
  // });
  // }
  //
  // /**
  // * Method for removing a button from the Horizontal layout pane
  // *
  // * @param a
  // * @param h
  // */
  // public void removeFromHBox(Button a, HBox h) {
  // h.getChildren().remove(a);
  // }
  //
  // /**
  // * Takes in a Node (Button, Label, ToggleButton etc) and applies CSS to it depending on what
  // Node
  // * type it is.
  // *
  // * @param the Node to apply CSS to.
  // *
  // * @return styled Node.
  // *
  // */
  // public Node toggleOffStyle(Node o) {
  // if (o.equals(label)) {
  // o.setStyle("-fx-background-color:#00bfff;" + "-fx-border-color: #000000;"
  // + "-fx-border-width: 0px 0px 4px 0px;" + "-fx-font-size: 25px; "
  // + "-fx-font-weight: bold;" + "-fx-alignment:center");
  // return o;
  // } else if (o.equals(killButton) || o.equals(killButton2) || o.equals(killButton3)
  // || o.equals(killButton4)) {
  // o.setStyle("-fx-background-color:#ff0000;" + "-fx-border-color: #000000;"
  // + "-fx-border-width: 4px;" + "-fx-font-size: 12px; " + "-fx-font-weight: bold;");
  // return o;
  // } else {
  // o.setStyle("-fx-background-color:#fff;" + "-fx-border-color: #000000;"
  // + "-fx-border-width: 4px;" + "-fx-font-size: 12px; " + "-fx-font-weight: bold;");
  // return o;
  // }
  // }
  //
  // /**
  // * Takes in a Node (Button, Label, ToggleButton etc) and applies CSS to it.
  // *
  // * @param the Node to apply CSS to.
  // *
  // * @return styled Node.
  // *
  // */
  // public Node toggleOnStyle(Node o) {
  // o.setStyle("-fx-background-color:#00cc00;" + "-fx-border-color: #000000;"
  // + "-fx-border-width: 4px;" + "-fx-font-size: 12px; " + "-fx-font-weight: bold;");
  // return o;
  // }
  //
  // /**
  // * Sets the tooltip on a Button.
  // *
  // * @param b ToggleButton
  // * @param s String
  // * @return
  // */
  // public Tooltip setTooltip(ToggleButton b, String s) {
  // Tooltip tooltip = new Tooltip();
  // tooltip.setText(s);
  // b.setTooltip(tooltip);
  // return tooltip;
  // }
  //
  // /**
  // * Sets the tooltip on a Button.
  // *
  // * @param b Button
  // * @param s String
  // * @return
  // */
  // public Tooltip setTooltip(Button b, String s) {
  // Tooltip tooltip = new Tooltip();
  // tooltip.setText(s);
  // b.setTooltip(tooltip);
  // return tooltip;
  // }
  //
  // /**
  // * Takes in a ArrayList of Waypoints (LiDar return) and draws Circles at their (x,y) position.
  // *
  // * @see common.datatypes.Waypoint
  // *
  // * @param the Collection of Waypoints to be drawn.
  // *
  // * @return a group of Circle nodes to display all objects from Lidar return.
  // *
  // */
  // public Group drawLidarLayer(ArrayList<Waypoint> l) {
  // lidarGroup = new Group();
  // for (Waypoint w : l) {
  // Circle circle = new Circle();
  // circle.setCenterX(w.getX());
  // circle.setCenterY(w.getY());
  // circle.setRadius(5d);
  // lidarGroup.getChildren().add(circle);
  // }
  //
  // return lidarGroup;
  // }
  //
  // /**
  // * Takes in a single Waypoint and draws a Rectangle at its (x,y) location.
  // *
  // * @see common.datatypes.Waypoint.
  // *
  // * @param current Waypoint.
  // *
  // * @return a single Rectangle node.
  // *
  // */
  // public Rectangle drawRectangle(Vertex v) {
  // Rectangle rectangle = new Rectangle();
  // rectangle.setX(v.getX() - 20);
  // rectangle.setY(v.getY() - 20);
  // rectangle.setWidth(40);
  // rectangle.setStroke(Color.BLACK);
  // rectangle.setStrokeWidth(4);
  // rectangle.setHeight(40);
  // return rectangle;
  // }
  //
  // /**
  // * Takes in a ArrayList of Vertices and draws Rectangles at their (x,y) position.
  // *
  // * @see common.datatypes.Waypoint
  // *
  // * @param the Collection of Waypoints to be drawn.
  // *
  // * @return a group of Rectangle nodes to display all Vertices in the Map that are Blocked.
  // *
  // */
  // public Group drawBlockedVertices(ArrayList<Vertex> l) {
  // blockedGroup = new Group();
  // for (Vertex v : l) {
  // Rectangle rectangle = drawRectangle(v);
  // rectangle.setFill(Color.RED);
  // blockedGroup.getChildren().add(rectangle);
  // }
  //
  // return blockedGroup;
  // }
  //
  // /**
  // * Takes in a ArrayList of Vertices and draws Rectangles at their (x,y) position.
  // *
  // * @see common.datatypes.Waypoint
  // *
  // * @param the Collection of Waypoints to be drawn.
  // *
  // * @return a group of Rectangle nodes to display all Vertices in the Map that are Blocked.
  // *
  // */
  // public Group drawAmalgamatedMap(GriddedMap griddedMap) {
  // Group amalgamateGroup = new Group();
  // for (Vertex v : griddedMap.toArrayList()) {
  // Rectangle rectangle = drawRectangle(v);
  // rectangle.setFill(Color.RED);
  // amalgamateGroup.getChildren().add(rectangle);
  // }
  //
  // return amalgamateGroup;
  // }
  //
  // /**
  // * Takes in a ArrayList (path item) of Waypoints and draws Rectangles at their (x,y) position.
  // *
  // * @see common.datatypes.Waypoint
  // * @see pathfinding.AStar
  // *
  // * @param the Collection of Waypoints to be drawn.
  // *
  // * @return a group of Rectangle nodes to display the initial path the AStar Algorithm found.
  // *
  // */
  // public Group drawPath(ArrayList<Vertex> vs) {
  // pathGroup = new Group();
  // for (Vertex v : vs) {
  // Rectangle rectangle = drawRectangle(v);
  // rectangle.setFill(Color.YELLOW);
  // pathGroup.getChildren().add(rectangle);
  // }
  //
  // return pathGroup;
  // }
  //
  // /**
  // * Takes in a ArrayList (closedList) of Waypoints and draws Rectangles at their (x,y) position.
  // *
  // * @see common.datatypes.Waypoint
  // *
  // * @param the Collection of Waypoints to be drawn.
  // *
  // * @return a group of Rectangle nodes to display all Waypoints the AStar Algorithm searched.
  // *
  // */
  // public Group drawSearched(ArrayList<Vertex> l) {
  // searchedGroup = new Group();
  // for (Vertex v : l) {
  // Rectangle rectangle = drawRectangle(v);
  // rectangle.setFill(Color.PURPLE);
  // searchedGroup.getChildren().add(rectangle);
  // }
  //
  // return searchedGroup;
  // }
  //
  // /**
  // * Takes in a ArrayList (optimised path) of Waypoints and draws Rectangles at their (x,y)
  // * position.
  // *
  // * @see common.datatypes.Waypoint
  // *
  // * @param the Collection of Waypoints to be drawn.
  // *
  // * @return a group of Rectangle nodes to display the optimised path.
  // *
  // */
  // public Group drawOptimisedPath(ArrayList<Vertex> l) {
  // optimisedGroup = new Group();
  // for (Vertex v : l) {
  // Rectangle rectangle = drawRectangle(v);
  // rectangle.setFill(Color.YELLOWGREEN);
  // optimisedGroup.getChildren().add(rectangle);
  // }
  //
  // return optimisedGroup;
  // }
  //
  // /**
  // * Takes in a array of Waypoints and draws vertical and horizontal lines at the Waypoint
  // position.
  // * The lines fill the total map rectangle region.
  // *
  // * @see common.datatypes.Waypoint
  // *
  // * @param the Collection of Waypoints to be drawn.
  // *
  // * @return a group of line Nodes to display the grid overlay.
  // *
  // */
  // public Group drawGrid(ArrayList<Waypoint> l) {
  // Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
  // Rectangle r = new Rectangle();
  // r.setX(screenBounds.getMinX());
  // r.setY(screenBounds.getMinY());
  // r.setWidth(screenBounds.getWidth() / 1.3);
  // r.setHeight(screenBounds.getHeight() - 50);
  // lineGroup = new Group();
  // for (int x = 0; x < r.getWidth(); x = x + 40) {
  // Line line = new Line();
  // line.setStartX(x - 20);
  // line.setEndX(x - 20);
  // line.setStartY(0);
  // line.setEndY(r.getHeight());
  // lineGroup.getChildren().add(line);
  // }
  //
  // for (int y = 0; y < r.getHeight(); y = y + 40) {
  // Line line = new Line();
  // line.setStartX(0);
  // line.setEndX(r.getWidth());
  // line.setStartY(y - 20);
  // line.setEndY(y - 20);
  // lineGroup.getChildren().add(line);
  // }
  //
  // return lineGroup;
  // }
  //
  // /**
  // * Takes in array of Waypoints and multiplies the x and y values by a given integer s.
  // *
  // * @see common.datatypes.Waypoint
  // *
  // * @deprecated use {@link common.datatypes.map.MapLayer#transform(int, int, int, int)} to scale
  // *
  // * @param the Collection of Waypoints to be scaled.
  // * @param s the integer that the x and y values of the collection will be multiplied by.
  // *
  // * @return new Collection of Waypoints scaled by an Integer value.
  // *
  // */
  // public ArrayList<Waypoint> scale(ArrayList<Waypoint> input, int s) {
  // ArrayList<Waypoint> output = new ArrayList<Waypoint>();
  // for (Waypoint w : input) {
  // output.add(new Waypoint(w.getX() * s, w.getY() * s));
  // }
  // return output;
  // }
  //
  //
  // private ArrayList<Vertex> pathToVerts(Path p) {
  // ArrayList<Vertex> res = new ArrayList<Vertex>();
  // for (Waypoint w : p.toArray()) {
  // res.add(new Vertex(w, null));
  // }
  // // TODO Auto-generated method stub
  // return null;
  // }
  //
  //
  // private RemoteLeader connectRMI() {
  // RemoteLeader res = null;
  // try {
  // res = (RemoteLeader) Naming.lookup("rmi://192.168.25.42" + "/HerdLeader");// FIXME lookup IP
  // } catch (MalformedURLException e) {
  // // TODO Auto-generated catch block
  // e.printStackTrace();
  // } catch (RemoteException e) {
  // // TODO Auto-generated catch block
  // e.printStackTrace();
  // } catch (NotBoundException e) {
  // // TODO Auto-generated catch block
  // e.printStackTrace();
  // } // needs to be var vs hardcode
  // // wait
  // // send the RMI leader the herd info
  // try {
  // res.register(this);
  // } catch (RemoteException e) {
  // // TODO Auto-generated catch block
  // e.printStackTrace();
  // }
  // return res;
  // }
  //
  // @Override
  // public boolean kill(String log) throws RemoteException {
  // LOGGER.log(Level.SEVERE, log);
  // Platform.exit();
  // return true;// TODO some logic
  // }
  //
  // @Override
  // public void RMITest() {
  // System.out.println("Member RMITest was called in the GUI");
  // try {
  // localLeaderRef.RMITest();
  // } catch (RemoteException e) {
  // e.printStackTrace();
  // }
  // }
  //
  // @Override
  // public void notifyOfChange() throws RemoteException {
  // try {
  // localHerdData = localLeaderRef.getState();
  // } catch (RemoteException e) {
  // // TODO Auto-generated catch block
  // e.printStackTrace();
  // }
  // }


}
