package com.insa.lifraison.controller;

import com.insa.lifraison.model.CityMap;
import com.insa.lifraison.model.DeliveryRequest;
import com.insa.lifraison.model.Intersection;
import com.insa.lifraison.view.MainController;
import com.insa.lifraison.model.Tour;
import com.insa.lifraison.view.View;
import javafx.stage.Stage;

import java.time.LocalTime;

/**
 * Controller of the application
 */
public class Controller {
    private View view;
    private CityMap map;
    private final ListOfCommands listOfCommands;

    private State currentState;
    protected final InitialState initialState = new InitialState();
    protected final NoDeliveriesMainState noDeliveriesMainState = new NoDeliveriesMainState();
    protected final AddDeliveryState1 addDeliveryState1 = new AddDeliveryState1();
    protected final AddDeliveryState2 addDeliveryState2 = new AddDeliveryState2();
    protected final FilledMapMainState filledMapMainState = new FilledMapMainState();
    protected final DeleteDeliveryState1 deleteDeliveryState1 = new DeleteDeliveryState1();
    protected final ChangeMapState changeMapState = new ChangeMapState();
    protected final EmptyMapMainState emptyMapMainState = new EmptyMapMainState();
    protected final DeleteTourState deleteTourState = new DeleteTourState();

    protected final SaveRoadmapState saveRoadmapState = new SaveRoadmapState();

    /**
     * Create the controller of the application
     * @param stage the stage of the view
     */
    public Controller(Stage stage) {
        this.currentState = initialState;
        this.listOfCommands = new ListOfCommands();
        this.view = new View(stage, this);

        view.loadScene("main", "main.fxml", "style/main.css");
        view.loadScene("home", "home.fxml", "style/home.css");

        stage.setTitle("LIFraison");
        view.navigate("home");
        stage.show();
    }

    /**
     * Set view of the controller
     * @param view the View
     */
    public void setView(View view) {
        this.view = view;
    }

    /**
     * Change the current state
     * @param state new state
     */
    protected void setCurrentState(State state) {
        if(this.currentState != state) {
            this.currentState.exitAction(map, view);
            this.currentState = state;
            this.currentState.entryAction(map, view);
        }
    }

    /**
     * Change the current state
     */
    protected void setCurrentStateToMain() {
        if(this.map.getNumberDeliveries() > 0) {
            this.setCurrentState(this.filledMapMainState);
        } else if(this.map.getTours().size()>1) {
            this.setCurrentState(this.noDeliveriesMainState);
        } else {
            this.setCurrentState(this.emptyMapMainState);
        }
    }

    /**
     * Method called after a click on the "load Map" button
     */
    public void loadMap(){
        currentState.loadMap(this, map, view, listOfCommands);
    }

    /**
     * Method called after a click on the "load Deliveries" button
     */
    public void loadDeliveries(){
        currentState.loadDeliveries(this, map, view, listOfCommands);
    }

    /**
     * Method called after a click on the "Add Delivery" button
     */
    public void addDelivery(){
        currentState.addDelivery(this, map, view);
    }

    /**
     * Method called after a left Click
     * @param i the intersection chosen by the user
     * @param d the delivery request chosen by the user
     * @param t the tour chosen by the user
     */
    public void leftClick(Intersection i, DeliveryRequest d, Tour t){
        currentState.leftClick(this, map, i, d, t, listOfCommands);
    }

    /**
     * Method called after a right Click
     */
    public void rightClick(){
        currentState.rightClick(this, map, view, listOfCommands);
    }

    /**
     * Method called after a click on the "Confirm" button
     */
    public void confirm(){
        currentState.confirm(this, map, view, listOfCommands);
    }

    /**
     * Method called after a click on the "Cancel" button
     */
    public void cancel(){currentState.cancel(this, view);}

    /**
     * Method called after a click on the "Delete Delivery" button
     */
    public void removeDelivery(){
        currentState.removeDelivery(this, map, view);
    }

    /**
     * Method called after a click on the "Compute Plan" button
     */
    public void computePlan(){
        currentState.computePlan(map, listOfCommands);
    }
    /**
     * Method called after a click on the "Undo" button
     */
    public void undo(){
        currentState.undo(this, map, listOfCommands);
    }

    /**
     * Method called after a click on the "Redo" button
     */
    public void redo(){
        currentState.redo(this, map, listOfCommands);
    }

    /**
     * Method called after a click on the "Save" button
     */
    public void saveDeliveries(){ currentState.saveDeliveries( map, view);}

    /**
     * Method called after a click on the "AddTour" button
     */
    public void addTour() { currentState.addTour(this, map, listOfCommands); }
    
    /**
     * Method called after a click on the "RemoveTour" button
     */
    public void removeTour() { currentState.removeTour(this, map); }

    /**
     * Method called after a click on a "Tour" button
     * @param tour the tour clicked
     */
    public void tourButtonClicked(Tour tour) { currentState.tourButtonClicked(this, map, tour, view, listOfCommands); }

    /**
     * Method called when the time window changes
     * @param timeWindowStart start of the time window
     * @param timeWindowEnd end of the time window
     */
    public void timeWindowChanged(LocalTime timeWindowStart, LocalTime timeWindowEnd) {currentState.timeWindowChanged(map, timeWindowStart, timeWindowEnd);}

    /**
     * Method called when the save roadmap button is clicked
     */
    public void saveRoadmap(){ currentState.saveRoadmap(this);}


    /**
     * Set the CityMap of the Controller
     * @param map the CityMap
     */
    public void setMap(CityMap map) {
        this.map = map;
        view.<MainController>getController("main").setMap(this.map);
    }
}
