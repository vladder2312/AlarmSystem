package alarmsystem.ui;

import alarmsystem.events.Event;
import alarmsystem.events.FireEvent;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.*;

public class MainController {

    @FXML private GridPane systemGrid;
    @FXML private Label stateLabel;
    private final MainModel model = new MainModel();

    private final Timer createTimer = new Timer();
    private final TimerTask createEvents = new TimerTask() {
        @Override
        public void run() {
            if(Math.random()<0.3){ //С шансом 30%
                runEvent((int)(Math.random()*9+1)); //Запускается событие на случайном датчике
            }
        }
    };
    private final Timer handlerTimer = new Timer();
    private final TimerTask handleEvents = new TimerTask() {
        @Override
        public void run() {
            Event event;
            while (!model.getEvents().isEmpty()){
                event=model.getEvents().get(0);
                handleFireEvent((FireEvent)event);
                model.getEvents().remove(event);
            }
        }
    };
    private final Timer eventCounter = new Timer();
    private final TimerTask countEvents = new TimerTask() {
        @Override
        public void run() {
            int counter=0;
            String message;
            for (boolean sensor : model.getSensors()) {
                if (sensor) {
                    counter++;
                }
            }
            switch (counter){
                case 0 -> message = "Датчики не сообщают о наличии дыма";
                case 1 -> message = "Один из датчиков сообщает о наличии дыма";
                case 2 -> message = "Два датчика сообщает о наличии дыма. Вызов оператора...";
                default -> message = "В здании пожар. Запуск системы тушения...";
            }
            Platform.runLater(() -> stateLabel.setText(message));
        }
    };

    @FXML
    public void initialize(){
        createTimer.schedule(createEvents,0,1000); //Запуск создания событий
        handlerTimer.schedule(handleEvents,0,1); //Запуск обработки событий
        eventCounter.schedule(countEvents,0,1); //Запуск счётчика активированных датчиков
        Arrays.fill(model.getSensors(), false);
    }

    private void runEvent(int position){
        System.out.println("Creating FireEvent on "+position+" position");
        Event event = new FireEvent(System.currentTimeMillis(), position, 10);
        model.getEvents().add(event);
        event.start();
    }

    private void handleFireEvent(FireEvent event){
        Circle sensor = null;
        int position = event.getPosition();
        for(Node node: systemGrid.getChildren()){ // Поиск датчика по номеру
            if(node.getId().equals("sensor"+position)){
                sensor = (Circle) node;
            }
        }
        model.getSensors()[position]=true;
        turnLight(sensor, true);
    }

    private void turnLight(Circle sensor, boolean isFire){
        sensor.setFill(isFire?Color.RED: Color.WHITE);
    }
}
