import java.sql.*;
import java.util.Optional;
import java.util.Scanner;

public class Partie {
    private final int idPartie;
    private final int idNord;
    private final int idSud;
    private final int idEst;
    private final int idOuest;
    private int gagnant1;
    private int gagnant2;
    private int scoreNS;
    private int scoreEO;
    private int tour;

    // Constructeur

    public Partie(int idPartie, int idNord, int idSud, int idEst, int idOuest, int gagnant1, int gagnant2, int scoreNS, int scoreEO) {
        this.idPartie = idPartie;
        this.idNord = idNord;
        this.idSud = idSud;
        this.idEst = idEst;
        this.idOuest = idOuest;
        this.gagnant1 = gagnant1;
        this.gagnant2 = gagnant2;
        this.scoreNS = scoreNS;
        this.scoreEO = scoreEO;
        this.tour = 1;
    }

    public Partie(int idPartie, int idNord, int idSud, int idEst, int idOuest) {
        this.idPartie = idPartie;
        this.idNord = idNord;
        this.idSud = idSud;
        this.idEst = idEst;
        this.idOuest = idOuest;
        this.gagnant1 = 0;
        this.gagnant2 = 0;
        this.scoreNS = 0;
        this.scoreEO = 0;
        this.tour = 1;
    }


    // Méthodes liées à la BDD

    public static Optional<Partie> load(Connection con, int idPartie) throws SQLException {
        try (PreparedStatement psmt = con.prepareStatement(
                "SELECT * FROM Partie WHERE idPartie = ?")) {
            psmt.setInt(1, idPartie);
            try (ResultSet rs = psmt.executeQuery()) {
                if (rs.next()) {
                    int idP = rs.getInt("idPartie");
                    int idN = rs.getInt("idNord");
                    int idS = rs.getInt("idSud");
                    int idE = rs.getInt("idEst");
                    int idO = rs.getInt("idOuest");
                    int g1 = rs.getInt("gagnant1");
                    int g2 = rs.getInt("gagnant2");
                    int sNS = rs.getInt("scoreNS");
                    int sEO = rs.getInt("scoreEO");
                    return Optional.of(
                            new Partie(idP, idN, idS, idE, idO, g1, g2, sNS, sEO));
                } else {
                    return Optional.empty();
                }
            }
        }
    }

    public static Partie creerPartie(Connection con, Joueur joueurNord, Joueur joueurSud, Joueur joueurEst, Joueur joueurOuest) throws SQLException {
        try (PreparedStatement psmt = con.prepareStatement(
                "INSERT INTO Partie VALUES (DEFAULT,?,?,?,?,0,0,0,0)", Statement.RETURN_GENERATED_KEYS)) {
            psmt.setInt(1,joueurNord.getIdJoueur());
            psmt.setInt(2,joueurSud.getIdJoueur());
            psmt.setInt(3,joueurEst.getIdJoueur());
            psmt.setInt(4,joueurOuest.getIdJoueur());
            psmt.executeUpdate();
            try (ResultSet rs = psmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int idP = rs.getInt("idPartie");
                    int idN = rs.getInt("idNord");
                    int idS = rs.getInt("idSud");
                    int idE = rs.getInt("idEst");
                    int idO = rs.getInt("idOuest");
                    return new Partie(idP, idN, idS, idE, idO);
                } else {
                    throw new IllegalStateException("Création impossible !");
                }
            }
        }
    }
    public void terminerPartie(Connection con, int idPartie, Joueur g1, Joueur g2, int scoreEO, int scoreNS) throws SQLException{
        try(PreparedStatement psmt = con.prepareStatement(
                "UPDATE Partie SET (gagnant1,gagnant2,scoreeo,scorens) = (?,?,?,?)" +
                        "WHERE idpartie = ?"
        )){
            psmt.setInt(1,g1.getIdJoueur());
            psmt.setInt(2,g2.getIdJoueur());
            psmt.setInt(3,scoreEO);
            psmt.setInt(4,scoreNS);
            psmt.setInt(5,idPartie);
            psmt.executeUpdate();
        }
    }


//    public void lancerPartie(Connection con) throws SQLException {
//        int nord = 1;
//        int est = 2;
//        int sud = 3;
//        int ouest = 4;
//        Joueur un = Joueur.load(con,idNord).get();
//        Joueur deux = Joueur.load(con,idEst).get();
//        Joueur trois = Joueur.load(con,idSud).get();
//        Joueur quatre = Joueur.load(con,idOuest).get();
//
//        Joueur tmp;
//        int temp;
//
//        for(int i = 0; i < 10; i++) {
//            System.out.println("Tour n° " + i); // A faire ?
//            jouerTour(un,trois,quatre,deux);
//        }
//    }

    public Carte jouerTour(Carte cN, Carte cS, Carte cE, Carte cO){
        // On détermine le gagnant
        Carte gagnante = cN;
        if (gagnante.getCouleur().equals(cE.getCouleur())) {
            if (gagnante.getValeur() < cE.getValeur()) {
                gagnante = cE;
            }
        }
        if (gagnante.getCouleur().equals(cS.getCouleur())) {
            if (gagnante.getValeur() < cS.getValeur()) {
                gagnante = cS;
            }
        }
        if (gagnante.getCouleur().equals(cO.getCouleur())) {
            if (gagnante.getValeur() < cO.getValeur()) {
                gagnante = cO;
            }
        }

        // On assigne les points

        if(gagnante.equals(cN) || gagnante.equals(cS)){
            this.scoreNS += 1;
        }else if(gagnante.equals(cE) || gagnante.equals(cO)){
            this.scoreEO += 1;
        }

        return gagnante;
    }

    public int pausePartie(){
        System.out.println("Partie en pause");
        return 1;
    }

    public String recapitulatif(Connection con) throws SQLException {
        String recap = "";
        recap += "Partie terminée !\n";
        recap += "Les gagnants sont : "+Joueur.load(con,this.gagnant1).get().getNom()+ " et "+Joueur.load(con,this.gagnant2).get().getNom()+"\n";
        if (scoreEO < scoreNS){
            recap += "Score des gagnants : "+scoreNS+"\n";
            recap += "Score des seconds : "+scoreEO+"\n";
        }else{
            recap += "Score des gagnants : "+scoreEO+"\n";
            recap += "Score des seconds : "+scoreNS+"\n";
        }
        return recap;
    }

    // Getters & Setters

    public int getTour(){
        return tour;
    }

    public int getIdPartie() {
        return idPartie;
    }

    public int getScoreEO() {
        return scoreEO;
    }

    public int getScoreNS() {
        return scoreNS;
    }

    public int getGagnant1() {
        return gagnant1;
    }

    public int getGagnant2() {
        return gagnant1;
    }

    public void setGagnant1(int gagnant1) {
        this.gagnant1 = gagnant1;
    }

    public void setGagnant2(int gagnant2) {
        this.gagnant2 = gagnant2;
    }

    public void setTour(int tour) {
        this.tour = tour;
    }
}