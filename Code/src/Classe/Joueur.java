package Classe;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

public class Joueur {
    private final int idJoueur;
    private String nom;
    private int orientation;
    private boolean endormi;
    private ArrayList<Carte> laMain;

    // Constructeur par défaut

    public Joueur(int idJoueur, String nom) {
        this.idJoueur = idJoueur;
        this.nom = nom;
        this.orientation = 0;
        this.endormi = false;
        this.laMain = new ArrayList<Carte>();
    }

    // Getters & Setters

    public int getOrientation() {
        return orientation;
    }
    public ArrayList<Carte> getLaMain() {
        return laMain;
    }
    public int getIdJoueur() {
        return idJoueur;
    }
    public String getNom() {
        return nom;
    }

    public void setLaMain(ArrayList<Carte> laMain) {
        this.laMain = laMain;
    }




    // Méthodes liées à la BDD

    public static Optional<Joueur> load(Connection con, int idJoueur) throws SQLException {
        try (PreparedStatement psmt = con.prepareStatement(
                "SELECT * FROM Joueur WHERE idJoueur = ?")) {
            psmt.setInt(1, idJoueur);
            try (ResultSet rs = psmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("idJoueur");
                    String nom = rs.getString("nom");
                    return Optional.of(new Joueur(id, nom));
                } else {
                    return Optional.empty();
                }
            }
        }
    }
    public static Joueur creerJoueur(Connection con, String nom) throws SQLException {
        try (PreparedStatement psmt = con.prepareStatement(
                "INSERT INTO Joueur VALUES (DEFAULT,?)", Statement.RETURN_GENERATED_KEYS)) {
            psmt.setString(1,nom);
            psmt.executeUpdate();
            try (ResultSet rs = psmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt("idJoueur");
                    String n = rs.getString("nom");
                    return new Joueur(id, n);
                } else {
                    throw new IllegalStateException("Création impossible !");
                }
            }
        }
    }

    // Overrides

    public String toString(){
        String desc = "";
        desc += "ID Joueur : "+idJoueur+"\n";
        desc += "Nom : "+nom+"\n";
        desc += "Jeu du joueur : "+"\n";
        for (Carte c:laMain){
            desc += c.toString();
        }
        return desc;
    }

}