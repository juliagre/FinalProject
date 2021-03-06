/**
 * Created by JuliaG on 01/12/2015.
 */
import javafx.animation.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.util.Duration;
import javafx.scene.input.KeyEvent;
import java.io.*;
import java.util.*;


public class game extends Application {
    Button btnStart;
    Button btnExit;
    /* frame counter*/
    private int i = 0;
    /* counter of successful jumps*/
    private int points = 0;
    public void start (Stage primaryStage){
        /*Stage setup*/
        primaryStage.setTitle("Q");
        VBox vbox = new VBox(15);
        vbox.setAlignment(Pos.CENTER);
        btnStart = new Button("Start");
        btnStart.setStyle("-fx-font: 22 arial");
        btnStart.setMaxWidth(100);
        btnExit = new Button("Exit");
        btnExit.setStyle("-fx-font: 22 arial");
        btnExit.setMaxWidth(100);
        StackPane stack = new StackPane();
        Scene scene = new Scene(stack, 300, 300);
        vbox.getChildren().addAll(btnStart, btnExit);
        stack.getChildren().add(vbox);
        primaryStage.setScene(scene);
        primaryStage.show();

        btnExit.setOnAction((event) -> {
            primaryStage.close();
            });

        btnStart.setOnAction((event1) -> {
            /*Open new window*/
            Image background = new Image("background.png");
            ImageView background1 = new ImageView(background);
            Image heroImage = new Image("Untitled.png");
            ImageView moose = new ImageView(heroImage);
            moose.setTranslateX(-200);
            moose.setTranslateY(0);
            Image enemyImage = new Image("bear2.png");
            ImageView bear = new ImageView(enemyImage);
            bear.setTranslateX(250);
            bear.setTranslateY(35);
            StackPane stack2 = new StackPane();
            stack2.getChildren().addAll(background1, moose, bear);
            Scene scene2 = new Scene(stack2, 600, 300);
            primaryStage.setScene(scene2);
            Timeline bearAttackAnimation = new Timeline();
            bearAttackAnimation.setCycleCount(5);

            AnimationTimer timer = new AnimationTimer() {
                @Override
                public void handle(long l) {
                    /*Frame counter*/
                    i++;
                    scene2.setOnKeyPressed(new EventHandler<KeyEvent>() {
                        @Override
                        public void handle(KeyEvent e) {
                            if (e.getCode() == KeyCode.SPACE) {
                                /*Moose animation*/
                                KeyValue jumpUpKeyValue = new KeyValue(moose.translateYProperty(), -80);
                                KeyFrame jumpUp = new KeyFrame(Duration.millis(1000), jumpUpKeyValue);
                                KeyValue jumpDownKeyValue = new KeyValue(moose.translateYProperty(), 0);
                                KeyFrame jumpDown = new KeyFrame(Duration.millis(2000), jumpDownKeyValue);
                                Timeline mooseAnimation = new Timeline(jumpUp, jumpDown);
                                mooseAnimation.play();
                                if (i >= 67 && i < 78 && mooseAnimation.getStatus() == Animation.Status.RUNNING) {
                                    points++;

                                }

                            }
                        }
                    });
                }
            };

            /*Bear animation*/
            KeyValue kv2 = new KeyValue(bear.translateXProperty(), -500);
            Duration duration = Duration.millis(3500);

            EventHandler onFinished = new EventHandler<ActionEvent>() {
                public void handle(ActionEvent t) {
                    i = 0;
                }
            };
            KeyFrame kf2 = new KeyFrame(duration, onFinished, kv2);
            bearAttackAnimation.getKeyFrames().add(kf2);
            bearAttackAnimation.play();
            timer.start();
            bearAttackAnimation.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    timer.stop();
                    File file = new File("score.txt");
                    List top10 = new ArrayList();

                    try {
                        if (file.exists()) {
                            BufferedReader br = new BufferedReader(new FileReader(file));
                            String line = br.readLine();
                            while (line != null) {
                                int lineTointeger = Integer.parseInt(line);
                                top10.add(lineTointeger);
                                line = br.readLine();
                            }
                            br.close();
                            Collections.sort(top10);
                            Collections.reverse(top10);
                        } else {
                            file.createNewFile();
                            System.out.println("File not found!");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    


                    int place = 0;
                    if (points>(int)top10.get(0)){
                        place = 1;
                        top10.add(points);
                    }
                    int lastElement = top10.size()-1;
                    if (points<(int)top10.get(lastElement)){
                        place = top10.size()+1;
                        top10.add(points);}
                    for (int listindex = 0; listindex < top10.size(); listindex++) {
                        if (points == (int) top10.get(listindex) || points > (int) top10.get(listindex)) {
                            top10.add(points);
                            place = listindex + 1;
                            System.out.print(listindex);
                            break;
                        }
                    }
                    try (FileWriter writer = new FileWriter(file)) {
                        for (int lindx2 = 0; lindx2 < top10.size(); lindx2++) {
                            writer.write(top10.get(lindx2 )+ "\n");

                        }
                        writer.close();



                    } catch (IOException ie) {
                        ie.printStackTrace();
                    }
                    Text text = new Text(300, 300, "Your score is " + Integer.toString(points)+"\n" + "Your place is " + Integer.toString(place));
                    text.setFont(new Font(40));
                    VBox vbox2 = new VBox();
                    vbox2.getChildren().addAll(text);
                    vbox2.setAlignment(Pos.CENTER);
                    Scene scene3 = new Scene(vbox2);
                    primaryStage.setScene(scene3);
                }
            });
            Timeline mytime = new Timeline();
            final KeyValue kv = new KeyValue(background1.translateXProperty(), -200);
            final KeyFrame kf = new KeyFrame(Duration.millis(5000), kv);
            mytime.getKeyFrames().add(kf);
            mytime.setCycleCount(Timeline.INDEFINITE);
            mytime.play();


        });

    }

    }
