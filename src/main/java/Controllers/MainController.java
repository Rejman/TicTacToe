package Controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class MainController {

    @FXML
    private GridPane mainGridPane;

    @FXML
    private ImageView field1ImageView;

    @FXML
    private ImageView field2ImageView;

    @FXML
    private ImageView field3ImageView;

    @FXML
    private ImageView field4ImageView;

    @FXML
    private ImageView field5ImageView;

    @FXML
    private ImageView field6ImageView;

    @FXML
    private ImageView field7ImageView;

    @FXML
    private ImageView field8ImageView;

    @FXML
    private ImageView field9ImageView;

    private Image cross = new Image("/img/cross.png");
    private Image circle = new Image("/img/circle.png");
    private Image empty = new Image("/img/empty.png");

    @FXML
    void initialize() {

        buildFields();


    }

    private void buildFields(){
        final int WIDTH = 100;
        final int HEIGHT = 100;
        final int SIZE = 3;


        for(int i=0; i<SIZE; i++){

            for(int j=0;j<SIZE; j++){

                final ImageView imageView = new ImageView();
                imageView.setFitWidth(WIDTH);
                imageView.setFitWidth(HEIGHT);
                imageView.setImage(empty);
                imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent event) {
                        click(imageView);
                    }
                });

                mainGridPane.add(imageView, i, j);
            }
        }
        mainGridPane.setGridLinesVisible(true);
        mainGridPane.setCursor(Cursor.HAND);

    }

    private boolean click(ImageView imageView){
        if(imageView.getImage()==empty){
            imageView.setImage(cross);
            return true;
        }
        else{
            System.out.println("Fields is not empty");
            return false;
        }
    }

}
