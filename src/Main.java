import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.postgresql.ds.PGSimpleDataSource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class Main extends Application {


    @Override
    public void start(Stage stage) throws FileNotFoundException, SQLException {
        PGSimpleDataSource ds = new PGSimpleDataSource();
        ds.setDatabaseName("projet");
        ds.setUser("antoine");
        ds.setPassword("antoine59");
        ds.setServerName("localhost");

        Connection connection = ds.getConnection();


        // Création du Group et de la Scene
        Pane root = new Pane();
        root.setId("pane");
        Scene scene = new Scene(root, 1920, 1080);

        scene.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());


        // Création des hBox et VBox
        HBox hBox = new HBox();
        VBox vBox = new VBox();


        vBox.setLayoutX(850);


        // Création Label pour le Titre Jeu de Bridge
        Label label = new Label("Jeu de Bridge");
        label.setId("label");
        label.setLayoutX(825);
        label.setLayoutY(40);
        root.getChildren().add(label);


        vBox.setSpacing(20);
        vBox.setLayoutY(300);

        // Boutton pour lancer une partie
        Button play = new Button("Multijoueur");
        play.setId("play");
        play.setMaxWidth(500);
        vBox.getChildren().add(play);


        // Boutton pour lancer une résolution de problème
        Button probleme = new Button("Résolution de Problème");
        probleme.setId("probleme");
        probleme.setMaxWidth(500);
        vBox.getChildren().add(probleme);


        // Boutton pour quitter le jeu
        Button quitter = new Button("Quitter");
        quitter.setId("quitter");
        quitter.setMaxWidth(500);
        vBox.getChildren().add(quitter);


        vBox.setAlignment(Pos.CENTER);
        // Ajout du vBox dans le group
        root.getChildren().addAll(vBox);


        // Action Boutton pour lancer une partie
        play.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {


                Stage stage1 = new Stage();


                stage1.setTitle("Partie");

                Pane root1 = new Pane();
                root1.setId("root");
                Scene scene1 = new Scene(root1);

                scene1.getStylesheets().addAll(this.getClass().getResource("style1.css").toExternalForm());

                VBox vBox1 = new VBox();
                HBox hBox1 = new HBox();

                hBox1.setSpacing(10);
                hBox1.setLayoutY(300);


                Text text = new Text("Selectionner les noms des joueurs");
                text.setLayoutX(800);
                text.setLayoutY(50);
                text.setId("label");
                root1.getChildren().add(text);


                Text selectionN = new Text("Saisir le nom du Joueur N : ");
                Text selectionS = new Text("Saisir le nom du Joueur S : ");
                Text selectionE = new Text("Saisir le nom du Joueur E : ");
                Text selectionO = new Text("Saisir le nom du Joueur O : ");

                selectionN.setId("selection");
                selectionE.setId("selection");
                selectionO.setId("selection");
                selectionS.setId("selection");


                TextField nomJoueurN = new TextField();
                TextField nomJoueurS = new TextField();
                TextField nomJoueurE = new TextField();
                TextField nomJoueurO = new TextField();


                hBox1.getChildren().addAll(selectionN, nomJoueurN);
                hBox1.getChildren().addAll(selectionS, nomJoueurS);
                hBox1.getChildren().addAll(selectionE, nomJoueurE);
                hBox1.getChildren().addAll(selectionO, nomJoueurO);


                Button entrer = new Button("Lancer Partie");
                entrer.setId("entrer");
                hBox1.getChildren().add(entrer);


                entrer.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        if (nomJoueurN.getText() != "" && nomJoueurS.getText() != "" && nomJoueurE.getText() != "" && nomJoueurO.getText() != "") {

                            String textNomJoueurN = nomJoueurN.getText();
                            String textNomJoueurS = nomJoueurS.getText();
                            String textNomJoueurE = nomJoueurE.getText();
                            String textNomJoueurO = nomJoueurO.getText();


                            try {
                                Joueur jN = Joueur.creerJoueur(connection, textNomJoueurN);
                                Joueur jE = Joueur.creerJoueur(connection, textNomJoueurE);
                                Joueur jO = Joueur.creerJoueur(connection, textNomJoueurO);
                                Joueur jS = Joueur.creerJoueur(connection, textNomJoueurS);
                                Partie partie = Partie.creerPartie(connection, jN, jS, jE, jO);


                                Pane root2 = new Pane();
                                Stage stage2 = new Stage();
                                Scene scene2 = new Scene(root2, 1920, 1080);
                                scene2.getStylesheets().addAll(Objects.requireNonNull(this.getClass().getResource("style2.css")).toExternalForm());
                                stage2.setTitle("Partie");
                                root2.setId("root");


                                Jeu newJeu = new Jeu();
                                newJeu.melanger();
                                newJeu.donner(jN, jE, jO, jS);

                                ArrayList<Carte> jeuNord = jN.getLaMain();
                                System.out.println(jeuNord.get(0).getPath());
                                ArrayList<Carte> jeuEst = jE.getLaMain();
                                ArrayList<Carte> jeuOuest = jO.getLaMain();
                                ArrayList<Carte> jeuSud = jS.getLaMain();


                                HBox jeuCarteAffichageN = new HBox();
                                HBox jeuCarteAffichageS = new HBox();
                                HBox jeuCarteAffichageE = new HBox();
                                HBox jeuCarteAffichageO = new HBox();

                                VBox vBox2 = new VBox();

                                ArrayList<ImageView> lesImagesViewN = new ArrayList<ImageView>();
                                for (int i = 0; i < jeuNord.size(); i++) {
                                    FileInputStream pathCarte = new FileInputStream(jeuNord.get(i).getPath());
                                    Image carte = new Image(pathCarte);
                                    ImageView carteVue = new ImageView(carte);
                                    carteVue.setId("carte");
                                    lesImagesViewN.add(carteVue);
                                }

                                ArrayList<ImageView> lesImagesViewS = new ArrayList<ImageView>();
                                for (int i = 0; i < jeuNord.size(); i++) {
                                    FileInputStream pathCarte = new FileInputStream(jeuSud.get(i).getPath());
                                    Image carte = new Image(pathCarte);
                                    ImageView carteVue = new ImageView(carte);
                                    carteVue.setId("carte");
                                    lesImagesViewS.add(carteVue);
                                }

                                ArrayList<ImageView> lesImagesViewE = new ArrayList<ImageView>();
                                for (int i = 0; i < jeuNord.size(); i++) {
                                    FileInputStream pathCarte = new FileInputStream(jeuEst.get(i).getPath());
                                    Image carte = new Image(pathCarte);
                                    ImageView carteVue = new ImageView(carte);
                                    carteVue.setId("carte");
                                    lesImagesViewE.add(carteVue);
                                }

                                ArrayList<ImageView> lesImagesViewO = new ArrayList<ImageView>();
                                for (int i = 0; i < jeuNord.size(); i++) {
                                    FileInputStream pathCarte = new FileInputStream(jeuOuest.get(i).getPath());
                                    Image carte = new Image(pathCarte);
                                    ImageView carteVue = new ImageView(carte);
                                    carteVue.setId("carte");
                                    lesImagesViewO.add(carteVue);
                                }


                                TextField choixCarte = new TextField("Saisir Votre Carte");
                                choixCarte.setAlignment(Pos.CENTER);


                                Text jouerAffichageN = new Text("Joueur Nord (" + jN.getNom() + ")");
                                jouerAffichageN.setId("joueraffichage");
                                jouerAffichageN.setLayoutX(150);

                                Text jouerAffichageS = new Text("Joueur Sud (" + jS.getNom() + ")");
                                jouerAffichageS.setId("joueraffichage");
                                jouerAffichageS.setLayoutX(150);

                                Text jouerAffichageE = new Text("Joueur Est (" + jE.getNom() + ")");
                                jouerAffichageE.setId("joueraffichage");
                                jouerAffichageE.setLayoutX(150);

                                Text jouerAffichageO = new Text("Joueur Ouest (" + jO.getNom() + ")");
                                jouerAffichageO.setId("joueraffichage");
                                jouerAffichageO.setLayoutX(150);


                                jeuCarteAffichageN.getChildren().addAll(lesImagesViewN.get(0), lesImagesViewN.get(1), lesImagesViewN.get(2), lesImagesViewN.get(3), lesImagesViewN.get(4), lesImagesViewN.get(5), lesImagesViewN.get(6), lesImagesViewN.get(7), lesImagesViewN.get(8), lesImagesViewN.get(9));
                                jeuCarteAffichageE.getChildren().addAll(lesImagesViewE.get(0), lesImagesViewE.get(1), lesImagesViewE.get(2), lesImagesViewE.get(3), lesImagesViewE.get(4), lesImagesViewE.get(5), lesImagesViewE.get(6), lesImagesViewE.get(7), lesImagesViewE.get(8), lesImagesViewE.get(9));
                                jeuCarteAffichageS.getChildren().addAll(lesImagesViewS.get(0), lesImagesViewS.get(1), lesImagesViewS.get(2), lesImagesViewS.get(3), lesImagesViewS.get(4), lesImagesViewS.get(5), lesImagesViewS.get(6), lesImagesViewS.get(7), lesImagesViewS.get(8), lesImagesViewS.get(9));
                                jeuCarteAffichageO.getChildren().addAll(lesImagesViewO.get(0), lesImagesViewO.get(1), lesImagesViewO.get(2), lesImagesViewO.get(3), lesImagesViewO.get(4), lesImagesViewO.get(5), lesImagesViewO.get(6), lesImagesViewO.get(7), lesImagesViewO.get(8), lesImagesViewO.get(9));
                                Button confirmation = new Button("Jouer Carte");


                                ArrayList<Carte> groupCarteJoue = new ArrayList<Carte>();
                                ArrayList<ImageView> imageViewJoue = new ArrayList<ImageView>();

                                confirmation.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent actionEvent) {

                                        String choixCarteInt = choixCarte.getText();
                                        Carte carteRetiree = jeuNord.get(Integer.parseInt(choixCarteInt) - 1);
                                        groupCarteJoue.add(carteRetiree);
                                        ImageView imgRetiree = lesImagesViewN.get(Integer.parseInt(choixCarteInt) - 1);
                                        imgRetiree.setLayoutX(850);
                                        imgRetiree.setLayoutY(30);
                                        imageViewJoue.add(imgRetiree);
                                        root2.getChildren().add(imgRetiree);
                                        jeuNord.remove(carteRetiree);
                                        lesImagesViewN.remove(imgRetiree);
                                        jeuCarteAffichageN.getChildren().clear();
                                        for (int i = 0; i < lesImagesViewN.size(); i++) {
                                            jeuCarteAffichageN.getChildren().addAll(lesImagesViewN.get(i));
                                        }
                                    }
                                });

                                TextField choixCarte1 = new TextField("Saisir Votre Carte");
                                choixCarte1.setAlignment(Pos.CENTER);

                                Button confirmation1 = new Button("Jouer Carte");

                                confirmation1.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent actionEvent) {
                                        String choixCarteInt = choixCarte1.getText();
                                        Carte carteRetiree = jeuEst.get(Integer.parseInt(choixCarteInt) - 1);
                                        groupCarteJoue.add(carteRetiree);
                                        ImageView imgRetiree = lesImagesViewE.get(Integer.parseInt(choixCarteInt) - 1);
                                        imgRetiree.setLayoutX(850);
                                        imgRetiree.setLayoutY(200);
                                        imageViewJoue.add(imgRetiree);
                                        root2.getChildren().add(imgRetiree);
                                        jeuEst.remove(carteRetiree);
                                        lesImagesViewE.remove(imgRetiree);
                                        jeuCarteAffichageE.getChildren().clear();
                                        for (int i = 0; i < lesImagesViewE.size(); i++) {
                                            jeuCarteAffichageE.getChildren().addAll(lesImagesViewE.get(i));
                                        }
                                    }

                                });

                                TextField choixCarte2 = new TextField("Saisir Votre Carte");
                                choixCarte2.setAlignment(Pos.CENTER);

                                Button confirmation2 = new Button("Jouer Carte");

                                confirmation2.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent actionEvent) {
                                        String choixCarteInt = choixCarte2.getText();
                                        Carte carteRetiree = jeuSud.get(Integer.parseInt(choixCarteInt) - 1);
                                        groupCarteJoue.add(carteRetiree);
                                        ImageView imgRetiree = lesImagesViewS.get(Integer.parseInt(choixCarteInt) - 1);
                                        imgRetiree.setLayoutX(850);
                                        imgRetiree.setLayoutY(370);
                                        imageViewJoue.add(imgRetiree);
                                        root2.getChildren().add(imgRetiree);
                                        jeuSud.remove(carteRetiree);
                                        lesImagesViewS.remove(imgRetiree);
                                        jeuCarteAffichageS.getChildren().clear();
                                        for (int i = 0; i < lesImagesViewS.size(); i++) {
                                            jeuCarteAffichageS.getChildren().addAll(lesImagesViewS.get(i));
                                        }
                                    }
                                });

                                TextField choixCarte3 = new TextField("Saisir votre carte");
                                choixCarte3.setAlignment(Pos.CENTER);
                                Button confirmation3 = new Button("Jouer Carte");

                                confirmation3.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent actionEvent) {

                                        String choixCarteInt = choixCarte3.getText();
                                        Carte carteRetiree = jeuOuest.get(Integer.parseInt(choixCarteInt) - 1);
                                        groupCarteJoue.add(carteRetiree);
                                        ImageView imgRetiree = lesImagesViewO.get(Integer.parseInt(choixCarteInt) - 1);
                                        imgRetiree.setLayoutX(850);
                                        imgRetiree.setLayoutY(540);
                                        imageViewJoue.add(imgRetiree);
                                        root2.getChildren().add(imgRetiree);
                                        jeuOuest.remove(carteRetiree);
                                        lesImagesViewO.remove(imgRetiree);
                                        jeuCarteAffichageO.getChildren().clear();
                                        for (int i = 0; i < lesImagesViewO.size(); i++) {
                                            jeuCarteAffichageO.getChildren().addAll(lesImagesViewO.get(i));
                                        }
                                    }
                                });

                                Button quitter = new Button("Quitter");

                                quitter.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent actionEvent) {
                                        Pane rootQuit = new Pane();
                                        Stage stageQuit = new Stage();
                                        Scene sceneQuit = new Scene(rootQuit, 500, 500);
                                        stageQuit.setTitle("Partie terminée !");
                                        rootQuit.setId("root");
                                        sceneQuit.getStylesheets().addAll(this.getClass().getResource("style1.css").toExternalForm());
                                        if (partie.getScoreEO() < partie.getScoreNS()) {

                                            int idJoueurGagnant1 = jN.getIdJoueur();
                                            int idJoueurGagnant2 = jS.getIdJoueur();
                                            System.out.println("L'id JoueurGagnant 1 : " + idJoueurGagnant1);
                                            System.out.println("L'id JoueurGagnant 2 : " + idJoueurGagnant2);
                                            partie.setGagnant1(idJoueurGagnant1);
                                            partie.setGagnant2(idJoueurGagnant2);


                                        } else {
                                            int idJoueurGagnant1 = jE.getIdJoueur();
                                            int idJoueurGagnant2 = jO.getIdJoueur();
                                            System.out.println("L'id JoueurGagnant 1 : " + idJoueurGagnant1);
                                            System.out.println("L'id JoueurGagnant 2 : " + idJoueurGagnant2);
                                            partie.setGagnant1(idJoueurGagnant1);
                                            partie.setGagnant2(idJoueurGagnant2);
                                        }
                                        try {
                                            partie.terminerPartie(connection, partie.getIdPartie(), Joueur.load(connection, partie.getGagnant1()).get(), Joueur.load(connection, partie.getGagnant2()).get(), partie.getScoreEO(), partie.getScoreNS());
                                        } catch (SQLException throwables) {
                                            throwables.printStackTrace();
                                        }
                                        try {
                                            Text recap = new Text(partie.recapitulatif(connection));
                                            recap.setId("jouerAffichage");
                                            rootQuit.getChildren().add(recap);
                                        } catch (SQLException throwables) {
                                            throwables.printStackTrace();
                                        }

                                        Button fermerTout = new Button("Fermer le logiciel");
                                        fermerTout.setLayoutX(200);
                                        fermerTout.setLayoutY(200);
                                        fermerTout.setOnAction(new EventHandler<ActionEvent>() {
                                            @Override
                                            public void handle(ActionEvent actionEvent) {
                                                stage.close();
                                                stage1.close();
                                                stage2.close();
                                                stageQuit.close();
                                                try {
                                                    connection.close();
                                                } catch (SQLException throwables) {
                                                    throwables.printStackTrace();
                                                }
                                            }
                                        });

                                        rootQuit.getChildren().add(fermerTout);


                                        stageQuit.setScene(sceneQuit);
                                        stageQuit.show();
                                    }
                                });

                                Text compteur = new Text("Tour n° " + partie.getTour() + " : ");
                                compteur.setId("joueraffichage");
                                compteur.setLayoutX(850);
                                compteur.setLayoutY(1000);
                                root2.getChildren().add(compteur);

                                Button lancerTour = new Button("Lancer Tour");
                                lancerTour.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent actionEvent) {
                                        Carte n = groupCarteJoue.get(0);
                                        Carte e = groupCarteJoue.get(1);
                                        Carte s = groupCarteJoue.get(2);
                                        Carte o = groupCarteJoue.get(3);

                                        Carte gagnante = partie.jouerTour(n, s, e, o);
                                        Joueur gagnant = null;
                                        ImageView imageViewGagnant = null;
                                        if (gagnante.equals(n)) {
                                            imageViewGagnant = imageViewJoue.get(0);
                                            try {
                                                gagnant = Joueur.load(connection, jN.getIdJoueur()).get();
                                            } catch (SQLException throwables) {
                                                throwables.printStackTrace();
                                            }
                                        } else if (gagnante.equals(e)) {
                                            imageViewGagnant = imageViewJoue.get(1);
                                            try {
                                                gagnant = Joueur.load(connection, jE.getIdJoueur()).get();
                                            } catch (SQLException throwables) {
                                                throwables.printStackTrace();
                                            }
                                        } else if (gagnante.equals(s)) {
                                            imageViewGagnant = imageViewJoue.get(2);
                                            try {
                                                gagnant = Joueur.load(connection, jS.getIdJoueur()).get();
                                            } catch (SQLException throwables) {
                                                throwables.printStackTrace();
                                            }
                                        } else if (gagnante.equals(o)) {
                                            imageViewGagnant = imageViewJoue.get(3);
                                            try {
                                                gagnant = Joueur.load(connection, jO.getIdJoueur()).get();
                                            } catch (SQLException throwables) {
                                                throwables.printStackTrace();
                                            }
                                        }

                                        if (partie.getTour() == 10) {
                                            compteur.setText("Tour n° " + partie.getTour() + " (Gagnant du tour précedent : " + gagnant.getNom() + ")");
                                            quitter.fire();
                                        } else {
                                            partie.setTour(partie.getTour() + 1);
                                            compteur.setText("Tour n° " + partie.getTour() + " (Gagnant du tour précedent : " + gagnant.getNom() + ")");
                                        }


                                        try {
                                            Thread.sleep(1000);
                                        } catch (InterruptedException interruptedException) {
                                            interruptedException.printStackTrace();
                                        }

                                        root2.getChildren().remove(imageViewJoue.get(0));
                                        root2.getChildren().remove(imageViewJoue.get(1));
                                        root2.getChildren().remove(imageViewJoue.get(2));
                                        root2.getChildren().remove(imageViewJoue.get(3));

                                        imageViewGagnant.setLayoutX(1500);
                                        imageViewGagnant.setLayoutY(30);
                                        root2.getChildren().add(imageViewGagnant);

                                        imageViewJoue.clear();
                                        groupCarteJoue.clear();
                                    }
                                });

                                vBox2.getChildren().addAll(jouerAffichageN, jeuCarteAffichageN, choixCarte, confirmation);
                                vBox2.getChildren().addAll(jouerAffichageE, jeuCarteAffichageE, choixCarte1, confirmation1);
                                vBox2.getChildren().addAll(jouerAffichageS, jeuCarteAffichageS, choixCarte2, confirmation2);
                                vBox2.getChildren().addAll(jouerAffichageO, jeuCarteAffichageO, choixCarte3, confirmation3);
                                vBox2.getChildren().add(lancerTour);
                                vBox2.getChildren().add(quitter);


                                jeuCarteAffichageN.setSpacing(-20);
                                jeuCarteAffichageE.setSpacing(-20);
                                jeuCarteAffichageO.setSpacing(-20);
                                jeuCarteAffichageS.setSpacing(-20);

                                root2.getChildren().add(vBox2);


                                stage2.setScene(scene2);
                                stage2.sizeToScene();
                                stage2.show();


                            } catch (SQLException | FileNotFoundException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Alert isEmpty = new Alert(AlertType.ERROR, "Un des champs est vide !");
                            isEmpty.show();
                        }


                    }
                });

                root1.getChildren().addAll(vBox1, hBox1);
                stage1.setScene(scene1);
                stage1.show();
            }
        });


        probleme.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Résolution Problème");
                alert.setContentText("A venir dans une prochaine MAJ");
                alert.showAndWait();
            }
        });


        // Action sur le boutton quitter pour fermer l'application
        quitter.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stage.close();
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });


        // Titre de la fenetre principale
        stage.setTitle("Jeu de Bridge");
        // Ajout de la scene dans le stage
        stage.setScene(scene);
        stage.sizeToScene();
        // Apparition de la fenetre
        stage.show();


    }

    public static void main(String[] args) {
        Main.launch(args);

    }
}
