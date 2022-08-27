package com.example.finalproject;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class Client1 extends Application {
    private final double W = 600;
    private final double H = 400;
    private Pane root;
    private Socket socket;
    private DataOutputStream send2Server;
    private DataInputStream fromServer;
    private String enteredStatus;
    private Stage window;
    private TextField textField;


    public void connectWithServer() throws IOException {
        socket = new Socket("localhost", 8000);
        send2Server = new DataOutputStream(socket.getOutputStream());
        fromServer = new DataInputStream(socket.getInputStream());
    }
    //    public static void main(String[] args) throws IOException {
//        Socket socket = new Socket("localhost", 2000);
//        DataOutputStream send2Server = new DataOutputStream(socket.getOutputStream());
//        Scanner scan = new Scanner(System.in);
//        System.out.print("Enter your name:");
//        String nickName = scan.nextLine();
//        while (true) {
//            send2Server.writeUTF(nickName+ " : " + scan.nextLine());
//        }
    public Button kahootButton(String btnText, String btnColor) {
        Button btn = new Button(btnText);
        btn.setMinWidth(W/2);
        btn.setMinHeight(H/2);
        btn.setStyle("-fx-background-color: " + btnColor);

        btn.setTextFill(Color.WHITE);
        btn.setWrapText(true);
        btn.setPadding(new Insets(10));

        Font font = Font.font("Times New Roman", FontWeight.BOLD, FontPosture.ITALIC, 120);
        btn.setFont(font);
        return btn;
    }
    public StackPane pinPane() throws IOException {
        StackPane stackPane = new StackPane();
        stackPane.setStyle("-fx-background-color: #46178f");
        textField = new TextField();
        textField.setPromptText("GAME PIN");
        textField.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.ITALIC, 16));
        textField.setAlignment(Pos.CENTER);
        textField.setMinHeight(50);
        textField.setMaxWidth(200);





        Button btn = new Button("Enter");
        btn.setMinSize(200, 50);
        btn.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.ITALIC, 16));
        btn.setOnAction(e ->{
            try {
                send2Server.writeInt(Integer.parseInt(textField.getText()));
                enteredStatus  = fromServer.readUTF();
                System.out.println(enteredStatus);
                if(enteredStatus.equals("Succsess")){
                    window.setScene(new Scene(NickNamePane(), W, H));
                    window.setTitle("Nickname");
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        String  s = String.valueOf("Game Pin: " +fromServer.readInt());
        Label lbl = new Label(s);
        lbl.setPadding(new Insets(20, 0,0,0));
        lbl.setTextFill(Color.WHITE);
        lbl.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.ITALIC, 24));
        stackPane.setAlignment(lbl , Pos.TOP_CENTER);







        VBox vBox = new VBox(10);
        vBox.setMaxSize(200, 50);
        vBox.setAlignment(Pos.CENTER);
        vBox.setStyle("-fz-background-color: White ");
        vBox.getChildren().addAll(textField, btn);



        stackPane.getChildren().addAll(vBox,lbl);
        return stackPane;

    }
    public StackPane nicknameAns(String nickname) {
        StackPane stackPane = new StackPane();
        VBox vBox1 = new VBox(10);
        Button btnRed = kahootButton("▴" ,"red");
        Button btnOrange = kahootButton("♦" ,"orange");
        vBox1.getChildren().addAll(btnRed, btnOrange);
        VBox vBox2 = new VBox(10);
        Button btnBlue = kahootButton("●" , "blue");
        Button btnGreen = kahootButton("■" ,"green");
        vBox2.getChildren().addAll(btnBlue, btnGreen);


        HBox hBox = new HBox(10);
        hBox.getChildren().addAll(vBox1, vBox2);
        stackPane.getChildren().addAll(hBox);
        stackPane.setAlignment(hBox, Pos.CENTER);

        btnRed.setOnAction(e -> {
            try {
                send2Server.writeUTF( nickname +": Red");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        btnOrange.setOnAction(e -> {
            try {
                send2Server.writeUTF( nickname +": Orange");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        btnBlue.setOnAction(e -> {
            try {
                send2Server.writeUTF(nickname +": Blue");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        btnGreen.setOnAction(e -> {
            try {
                send2Server.writeUTF(nickname +": Green");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        return stackPane;
    }

    public StackPane NickNamePane(){
        StackPane stackPane = new StackPane();
        stackPane.setStyle("-fx-background-color: #46178f");
        textField = new TextField();
        textField.setPromptText("Nickname");
        textField.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.ITALIC, 16));
        textField.setAlignment(Pos.CENTER);
        textField.setMinHeight(50);
        textField.setMaxWidth(200);




        Button btn = new Button("Enter");
        btn.setMinSize(200, 50);
        btn.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.ITALIC, 16));
        btn.setOnAction(e ->{
            try {

                send2Server.writeUTF(textField.getText());
                window.setScene(new Scene(nicknameAns(textField.getText()), W, H));
                window.setTitle(textField.getText());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });




        VBox vBox = new VBox(10);
        vBox.setMaxSize(200, 50);
        vBox.setAlignment(Pos.CENTER);


        //  vBox.getChildren().addAll(textField, btn, lbl);
        vBox.getChildren().addAll(textField, btn);
        stackPane.getChildren().addAll(vBox);
        return stackPane;

    }


    @Override
    public void start(Stage stage) throws Exception {
        window = stage;
        connectWithServer();
        root = pinPane();
        stage.setScene(new Scene(root, W, H));
        stage.setTitle("Pin");
        stage.show();
        root.requestFocus();
    }
}
