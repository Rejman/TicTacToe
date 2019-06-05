package Controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class MainController {


    @FXML
    private VBox mainVBox;

    @FXML
    private ImageView field1;

    @FXML
    private ImageView field2;

    @FXML
    private ImageView field3;

    @FXML
    private ImageView field4;

    @FXML
    private ImageView field5;

    @FXML
    private ImageView field6;

    @FXML
    private ImageView field7;

    @FXML
    private ImageView field8;

    @FXML
    private ImageView field9;

    private boolean isCross = true;
    private Image cross = new Image("/img/cross.png");
    private Image circle = new Image("/img/circle.png");



    @FXML
    void initialize() {

        Field fields[] = new Field[9];
        fields[0]=new Field(field1);
        fields[1]=new Field(field2);
        fields[2]=new Field(field3);
        fields[3]=new Field(field4);
        fields[4]=new Field(field5);
        fields[5]=new Field(field6);
        fields[6]=new Field(field7);
        fields[7]=new Field(field8);
        fields[8]=new Field(field9);

        for (final Field field:fields
             ) {
            field.field.setOnMouseClicked(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent event) {
                    if(field.isEmpty){
                        if(isCross){
                            field.field.setImage(cross);
                        }
                        else
                        {
                            field.field.setImage(circle);
                        }
                        if(isCross) isCross=false;
                        else isCross=true;
                        field.isEmpty=false;
                    }
                }
            });
        }


    }

    void click(MouseEvent event){

    }

    @FXML
    void click1(MouseEvent event) {

    }

    @FXML
    void click2(MouseEvent event) {
        if(isCross){
            field2.setImage(cross);
        }
        else
        {
            field2.setImage(circle);
        }
        if(isCross) isCross=false;
        else isCross=true;
    }

    @FXML
    void click3(MouseEvent event) {
        if(isCross){
            field3.setImage(cross);
        }
        else
        {
            field3.setImage(circle);
        }
        if(isCross) isCross=false;
        else isCross=true;
    }

    @FXML
    void click4(MouseEvent event) {
        if(isCross){
            field4.setImage(cross);
        }
        else
        {
            field4.setImage(circle);
        }
        if(isCross) isCross=false;
        else isCross=true;
    }

    @FXML
    void click5(MouseEvent event) {
        if(isCross){
            field5.setImage(cross);
        }
        else
        {
            field5.setImage(circle);
        }
        if(isCross) isCross=false;
        else isCross=true;
    }

    @FXML
    void click6(MouseEvent event) {
        if(isCross){
            field6.setImage(cross);
        }
        else
        {
            field6.setImage(circle);
        }
        if(isCross) isCross=false;
        else isCross=true;
    }

    @FXML
    void click7(MouseEvent event) {
        if(isCross){
            field7.setImage(cross);
        }
        else
        {
            field7.setImage(circle);
        }
        if(isCross) isCross=false;
        else isCross=true;
    }

    @FXML
    void click8(MouseEvent event) {
        if(isCross){
            field8.setImage(cross);
        }
        else
        {
            field8.setImage(circle);
        }
        if(isCross) isCross=false;
        else isCross=true;
    }

    @FXML
    void click9(MouseEvent event) {
        if(isCross){
            field9.setImage(cross);
        }
        else
        {
            field9.setImage(circle);
        }
        if(isCross) isCross=false;
        else isCross=true;
    }
    class Field{
        public ImageView field;
        public boolean isEmpty = true;

        public Field(ImageView field) {
            this.field = field;
        }
    }
}
