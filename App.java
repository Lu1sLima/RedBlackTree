import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.Scanner;

import javafx.event.ActionEvent; 
import javafx.event.EventHandler; 
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList; 
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.Screen;
import javafx.scene.control.TextField;
/**
 * App
 */
public class App extends Application{
    private TextField searchField;
    private TextField traductionField;
    private Pesquisa p1;
    // public static RedBlackBST<Character, RedBlackBST> arvoreMacro = new RedBlackBST<>();
    // public static RedBlackBST<String, String> arvoreMicro;
    // String alfabeto = "abcdefghijklmnopqrstuvwxyz";

    public App(){

    }

    @Override
    public void start(Stage primaryStage){
        GridPane tab = new GridPane();
        primaryStage.setTitle("Dicionario - Ingles - Portugues");
        tab.setAlignment(Pos.CENTER);
        tab.setVgap(10);
        tab.setHgap(10);
        tab.setPadding(new Insets(10, 10, 10, 10));
        // tab.setGridLinesVisible(true);

        
        Text sceneTitle = new Text("Dicionario");
        sceneTitle.setTextAlignment(TextAlignment.CENTER);
        sceneTitle.setId("dicionario-text");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.SEMI_BOLD, 20));
        
        searchField = new TextField();
        searchField.setPrefHeight(40);
        searchField.setPrefWidth(500);
        tab.add(searchField, 1, 1);

        Pesquisa.carregar();

        Button pesquisar = new Button("Pesquisar");
        pesquisar.setAlignment(Pos.CENTER);
        pesquisar.setMinWidth(150);
        pesquisar.setOnAction( p -> {
            trataBotaoPesquisa(searchField);
            searchField.setText("");
        });


        Button random = new Button ("Aleatorio");
        random.setAlignment(Pos.CENTER);
        random.setMinWidth(150);
        random.setOnAction(e -> aleatorio());

        HBox hb = new HBox(10);
        hb.setAlignment(Pos.CENTER);
        hb.setPadding(new Insets(25, 25, 25, 25));
        hb.getChildren().add(pesquisar);
        hb.getChildren().add(random);
        
        VBox vb = new VBox(10);
        vb.setAlignment(Pos.CENTER);
        vb.setPadding(new Insets(25, 25, 25, 25));
        vb.getChildren().add(sceneTitle);
        vb.getChildren().add(tab);
        vb.getChildren().add(hb);

        Scene scene = new Scene(vb, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();

    }

    public void trataBotaoPesquisa(TextField searchField){
        
        try{
            try{
                String text = searchField.getText();
                String traducao = Pesquisa.pesquisa(text);
                if(!(traducao.equals("null"))){
                    Alert msgBox = new Alert(AlertType.INFORMATION);
                    msgBox.setHeaderText("Palavra ["+text.toUpperCase()+"]");
                    msgBox.setContentText("Traducao: "+traducao.toUpperCase());
                    msgBox.showAndWait();
                }else{
                    Alert confirmationBox = new Alert(AlertType.CONFIRMATION);
                    confirmationBox.setHeaderText("OPS!");
                    confirmationBox.setContentText("Essa palavra ainda nao tem uma traducao.\n"+
                    "Deseja adicionar uma traducao para ela? Se sim, pressione o botao OK.");
                    confirmationBox.showAndWait()
                                   .filter(r -> r == ButtonType.OK)
                                   .ifPresent(r -> trataAdiciona(new Stage(), text));
                }
            }
            catch(NullPointerException e){
                Alert msgBox = new Alert(AlertType.INFORMATION);
                msgBox.setHeaderText("INVALIDO!");
                msgBox.setContentText("PALAVRA INVALIDA!");
                msgBox.showAndWait();
            }
        }catch(StringIndexOutOfBoundsException e){
            Alert msgBox = new Alert(AlertType.INFORMATION);
            msgBox.setHeaderText("VAZIO!");
            msgBox.setContentText("DIGITE UMA PALAVRA NO CAMPO DE PESQUISA!");
            msgBox.showAndWait();
        }
    }

    public void trataAdiciona(Stage primaryStage, String palavra){

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text sceneTitle = new Text("Traducao para a palavra: "+palavra);
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.SEMI_BOLD, 20));
        

        traductionField = new TextField(null);
        traductionField.setPrefHeight(40);
        traductionField.setPrefWidth(500);
        grid.add(traductionField, 1, 1);


        Button salvar = new Button("Salvar");
        salvar.setAlignment(Pos.CENTER);
        salvar.setMinWidth(100);
        salvar.setOnAction(f -> {
            Pesquisa.AdicionaPalavra(palavra, traductionField.getText());
            if(traductionField.getText() != null){
                Alert msgBox = new Alert(AlertType.INFORMATION);
                Pesquisa.salvar();
                msgBox.setHeaderText("SALVO!");
                msgBox.setContentText("Traducao salva por sucesso!");
                msgBox.showAndWait();
                primaryStage.close();
            }
            else{
                Alert msgBox = new Alert(AlertType.INFORMATION);
                msgBox.setHeaderText("VAZIO!");
                msgBox.setContentText("Por favor, insira uma traducao antes de salvar.");
                msgBox.showAndWait();
            }
        }); 
        
        
        Button limpar = new Button("Limpar");
        limpar.setAlignment(Pos.CENTER);
        limpar.setMinWidth(100);
        limpar.setOnAction(
            f ->{
                traductionField.setText("");
            }
            );


            HBox hb = new HBox(10);
            hb.setAlignment(Pos.CENTER);
            hb.setPadding(new Insets(25, 25, 25, 25));
            hb.getChildren().add(salvar);
            hb.getChildren().add(limpar);

            VBox vb = new VBox(10);
            vb.setAlignment(Pos.CENTER);
            vb.setPadding(new Insets(25, 25, 25, 25));
            vb.getChildren().add(sceneTitle);
            vb.getChildren().add(grid);
            vb.getChildren().add(hb);

            Scene scene = new Scene(vb, 600, 400);
            primaryStage.setScene(scene);
            primaryStage.show();

        }

        public void aleatorio(){
            String[]words = Pesquisa.pesquisaAleatoria();
            Alert msgBox = new Alert(AlertType.INFORMATION);
            msgBox.setHeaderText("Palavra ["+words[0].toUpperCase()+"]");
            msgBox.setContentText("Traducao: "+words[1].toUpperCase());
            msgBox.showAndWait();
        }
        
    public static void main(String[] args) {
        launch(args);
    }
}