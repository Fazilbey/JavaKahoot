package com.example.finalproject;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends Application {
    private int clientNo = 0;
    private ArrayList<String> arr = new ArrayList<>();


    private  static int randomPin(){
        return 12345;
    }




    @Override
    public void start(Stage stage) throws Exception {


        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: #46178f");

        BorderPane borderPane = new BorderPane();

        Label lbl = new Label("Game PIN: \n" + randomPin());
        lbl.setTextFill(Color.WHITE);
        lbl.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.ITALIC, 20));
        lbl.setAlignment(Pos.CENTER);
        lbl.setMinWidth(600);

        borderPane.setTop(lbl);
        root.getChildren().addAll(borderPane);

        new Thread(()->{
            try {
                ServerSocket serverSocket = new ServerSocket(8000);
                int clientNo = 0;
                while (true) {
                    try{
                        clientNo++;
                        System.out.println("Waiting for players");
                        Socket socket = serverSocket.accept();
                        System.out.println(clientNo  +" Client Connected");
                        int finalClientNo = clientNo;
                        
                        new Thread(() ->{
                            try {
                                DataOutputStream toClient = new DataOutputStream(socket.getOutputStream());
                                DataInputStream fromClient = new DataInputStream(socket.getInputStream());
                                toClient.writeInt(randomPin());

                                while (true) {
                                    try {
                                        int clientPin = fromClient.readInt();
                                        System.out.println(clientPin);
                                        if (clientPin == randomPin()) {
                                            toClient.writeUTF("Succsess");

                                        } else {
                                            toClient.writeUTF("Wrong Pin");
                                        };
                                        String nickname = fromClient.readUTF();
                                        Label lbl1 = new Label(nickname);
                                        lbl1.setTextFill(Color.WHITE);
                                        lbl1.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.ITALIC, 20));
                                        HBox hBox = new HBox();
                                        arr.add(nickname + " ");
                                        Platform.runLater(() -> {
                                            for (int i = 0; i <arr.size() ; i++) {
                                                hBox.getChildren().add(new Label(arr.get(i)));
                                            }

                                            borderPane.setCenter(hBox);

                                        });
                                        String choice = fromClient.readUTF();
                                        System.out.println(choice);
                                        String b = fromClient.readUTF();
                                    }catch (IOException e){
                                        throw new RuntimeException();
                                    }
                                }
                            } catch (IOException e) {
                                throw new RuntimeException();
                            }
                        }).start();
                    }catch (IOException e){
                    }
                }
            }catch (IOException e){
            }
        }).start();
        stage.setTitle("SERVER");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();

    }
}
