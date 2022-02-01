import java.util.ArrayList;
import java.util.Random;

public class Jeu {
    private ArrayList<Carte> lesCartes;


    public Jeu() {
        this.lesCartes = new ArrayList<Carte>();
        for (int i = 0; i < 4 ; i++) {
            for (int j = 1; j <= 10; j++) {
                if (i == 0) {
                    Carte c = new Carte(j, "Coeur", "C:\\Users\\antoi\\OneDrive\\Bureau\\Iut\\Semestre 2 - Cours\\Projet Semestre 2\\Code_Finale\\src\\Carte\\"+j+"co.png");
                    this.lesCartes.add(c);
                } else if (i == 1) {
                    Carte c = new Carte(j, "Carreau", "C:\\Users\\antoi\\OneDrive\\Bureau\\Iut\\Semestre 2 - Cours\\Projet Semestre 2\\Code_Finale\\src\\Carte\\"+j+"ca.png");
                    this.lesCartes.add(c);
                } else if (i == 2) {
                    Carte c = new Carte(j, "Pique", "C:\\Users\\antoi\\OneDrive\\Bureau\\Iut\\Semestre 2 - Cours\\Projet Semestre 2\\Code_Finale\\src\\Carte\\"+j+"P.png");
                    this.lesCartes.add(c);
                } else if (i == 3) {
                    Carte c = new Carte(j, "Trèfle", "C:\\Users\\antoi\\OneDrive\\Bureau\\Iut\\Semestre 2 - Cours\\Projet Semestre 2\\Code_Finale\\src\\Carte\\"+j+"T.png");
                    this.lesCartes.add(c);
                }
            }
        }
    }

    // Getters & Setters

    public ArrayList<Carte> getLesCartes() {
        return lesCartes;
    }

    public void setLesCartes(ArrayList<Carte> lesCartes) {
        this.lesCartes = lesCartes;
    }

    // Méthodes

    public void melanger(){
        // Déclarations des variables
        Random r = new Random();
        Carte Cartetire;
        int n;
        ArrayList<Carte> apresMelange = new ArrayList<Carte>();

        for(int i = 0;i<40;i++) {
            n = r.nextInt(lesCartes.size());
            Cartetire = lesCartes.get(n);
            apresMelange.add(Cartetire);
            lesCartes.remove(Cartetire);
        }

        this.setLesCartes(apresMelange); // Remplacement du paquet
    }

    public void donner(Joueur N,Joueur S,Joueur E,Joueur O){
        for(int i = 0; i < 4; i++){

            ArrayList<Carte> leJeu = new ArrayList<Carte>();

            for(int j = 0; j < 10; j++){

                leJeu.add(lesCartes.get(0));
                lesCartes.remove(0);
            }
            if (i == 0 ){
                N.setLaMain(leJeu);
            }
            else if (i == 1 ){
                E.setLaMain(leJeu);
            }
            else if (i == 2 ){
                S.setLaMain(leJeu);
            }
            else {
                O.setLaMain(leJeu);
            }

        }

    }

    // Overrides

    public String toString(){
        String desc = "";
        for (Carte c:lesCartes){
            desc += c.toString();
        }
        return desc;
    }
}