import javafx.scene.Node;

import java.util.Objects;

public class Carte extends Node {
    private final String couleur;
    private final int valeur;
    private String path;


    // Constructeur par défaut

    public Carte(int valeur, String couleur, String path) {
        this.couleur = couleur;
        this.valeur = valeur;
        this.path = path;
    }

    // Getters & Setters

    public String getCouleur() {
        return couleur;
    }

    public int getValeur() {
        return valeur;
    }

    public String getPath() {
        return path;
    }

    // Overrides

    public String toString() {
        return this.valeur + " de " + this.couleur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Carte carte = (Carte) o;
        return valeur == carte.valeur && Objects.equals(couleur, carte.couleur);
    }

}