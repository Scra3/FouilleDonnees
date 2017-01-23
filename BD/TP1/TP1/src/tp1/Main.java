/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp1;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author scra
 */
public class Main {

    public static TreeMap<String, Integer> treilli = new TreeMap<String, Integer>();
    public final static int min = 2; // 2 occurences est le min
    public static ArrayList<ArrayList<Apriori>> donneesTp;
    /*METHODES*/
    /*Convertit un treilli au format string en un arrayList*/

    public void afficherTreeMap() {
        for (String mapKey : treilli.keySet()) {
            System.out.println("ITEM : " + mapKey + " -- OCCURENRENCES " + treilli.get(mapKey));
        }
    }

    public ArrayList<ArrayList<Apriori>> stringToCorrectStructure(String modele) {
        ArrayList<ArrayList<Apriori>> liste = new ArrayList<ArrayList<Apriori>>();
        /*Décomposition*/
        /*Ligne par ligne*/
        String[] lignes = modele.split("\n");
        /*On supprime les deux premieres lignes en commencant à i = 2*/
        int nbrsLigne = lignes.length;

        for (int i = 2; i < nbrsLigne; i++) {
            String[] vals = lignes[i].split(" ");
            // on supprime les TIDS : i commence à 1
            int taille = vals.length;
            ArrayList<Apriori> collections = new ArrayList<Apriori>();
            liste.add(collections);
            for (int j = 1; j < taille; j++) {
                liste.get(i - 2).add(new Apriori(vals[j])); // chaque élément est un item, un item est un string
                /*Affichage check*/
                //System.out.println(liste.get(i-2).get(j-1).getItem());
            }
        }

        return liste;
    }

    /*Compte le nombre d'item dans un treilli*/
    public void compterItems(ArrayList<Apriori> liste) {
        treilli.clear();
        int taille = liste.size();
        int tailleCollection = donneesTp.size();
        int count = 0;
        for (int i = 0; i < taille; i++) {
            String mot = liste.get(i).getItem();
            System.out.println("mot " + mot);
            char lettres[] = mot.toCharArray();
            int cmpt = 0;
            int tailleMot = lettres.length;
            for (int j = 0; j < tailleCollection; j++) {
                for (int k = 0; k < donneesTp.get(j).size(); k++) {
                    String ite = donneesTp.get(j).get(k).getItem();
                    if (lettres[cmpt] == ite.charAt(0)) {
                        cmpt++;
                        if (cmpt == tailleMot) {
                            if (treilli.containsKey(mot)) {
                                count = treilli.get(mot) + 1;
                                treilli.put(mot, count);
                            } else {
                                treilli.put(mot, 1);
                            }
                            cmpt = 0;
                        }
                    }
                }
                cmpt = 0;
                //System.out.println("de");
            }

        }
    }

    /*afficher arrayList*/
    public void afficherListe(ArrayList<Apriori> liste) {
        int tailleListe = liste.size();
        for (int i = 0; i < tailleListe; i++) {
            System.out.print("ITEM : " + liste.get(i).getItem() + "\n");
        }
    }


    /*Générer combinaison*/
    public ArrayList<Apriori> combinaison(int taille) {
        /*On check si la taille des itemspour les combinaisons*/
        ArrayList<Apriori> donneesLn_1 = new ArrayList<Apriori>();

        if (taille == 1) {
            donneesLn_1 = combinaisonTailleUne(treilli);
        } else {
            donneesLn_1 = combinaisonTailleN(treilli);
        }
        return donneesLn_1;

    }

    /*Combinaison de taille une*/
    private static ArrayList<Apriori> combinaisonTailleUne(TreeMap<String, Integer> donnees) {

        ArrayList<Apriori> donneesLn = new ArrayList<Apriori>();

        /*On change le format*/
        ArrayList<Apriori> newDonnees = new ArrayList<Apriori>();

        for (String mapKey : donnees.keySet()) {
            newDonnees.add(new Apriori(mapKey));
        }
        /**
         * ****************************************************
         */
        for (int i = 0; i < newDonnees.size() - 1; i++) {
            String firstItem = newDonnees.get(i).getItem();
            for (int j = i + 1; j < donnees.size(); j++) {
                String nextItem = newDonnees.get(j).getItem();
                /*Génération de la nouvelle données*/
                String result = firstItem.concat(nextItem);
                Apriori obj = new Apriori(result);
                //add
                donneesLn.add(obj);
            }

        }

        return donneesLn;
    }

    /*Combinaison de taille N différent de 1*/
    private static ArrayList<Apriori> combinaisonTailleN(TreeMap<String, Integer> donnees) {
        ArrayList<Apriori> donneesLn = new ArrayList<Apriori>();

        /*On change le format*/
        ArrayList<Apriori> newDonnees = new ArrayList<Apriori>();

        for (String mapKey : donnees.keySet()) {
            newDonnees.add(new Apriori(mapKey));
        }

        boolean find = false;
        // Si les données sont de taille "1"
        for (int i = 0; i < newDonnees.size() - 1; i++) {
            String firstItem = newDonnees.get(i).getItem();
            /*Récupérer la deniere lettre*/
            char lastLetter = firstItem.toCharArray()[firstItem.length() - 1];

            for (int j = i + 1; j < newDonnees.size(); j++) {
                String nextItem = newDonnees.get(j).getItem();
                /*On prend la premier lettre de la chaine*/
                char compare = nextItem.toCharArray()[0];
                if (compare == lastLetter) {
                    find = true;
                    /*On récupére les lettres suivantes*/
                    String lastLetters = nextItem.substring(1, nextItem.length());
                    /*Génération de la nouvelle données*/
                    String result = firstItem.concat(lastLetters);
                    Apriori obj = new Apriori(result);
                    //add
                    donneesLn.add(obj);
                } //Si on ne trouve pas de composition apres en avoir trouvé au moins une on s'arrete
                else if (find == true) {
                    find = false;
                    break;
                }
            }
        }
        return donneesLn;
    }

    /*On vérifie si il y a des infréquents, retourne seulement les fréquents*/
    public static void checkMins() {
        ArrayList<String> delete = new ArrayList<String>();
        for (String mapKey : treilli.keySet()) {
            int nbs = treilli.get(mapKey); //on get le nombre d'occurences
            if (nbs < min) {
                //ArrayList<Apriori> ite = ensemble.get(i);
                // ite.remove(j); // on supprime l'item
                delete.add(mapKey);
            }
        }
        for (int i = 0; i < delete.size(); i++) {
            treilli.remove(delete.get(i));
        }
    }

    private void genererPremierTreilli() {
        int taille = donneesTp.size();
        int tailleCollection = 0;
        int count = 0;
        for (int i = 0; i < taille; i++) {
            tailleCollection = donneesTp.get(i).size();
            for (int j = 0; j < tailleCollection; j++) {
                String ite = donneesTp.get(i).get(j).getItem();
                if (treilli.containsKey(ite)) {
                    count = treilli.get(ite) + 1;
                    treilli.put(ite, count);
                } else {
                    treilli.put(ite, 1);
                }
            }
        }
    }

    public void aprioriAlgo(String jeuDonnees) {
        Main algo = new Main();

        donneesTp = algo.stringToCorrectStructure(jeuDonnees);
        algo.genererPremierTreilli();
        algo.checkMins();
        algo.afficherTreeMap();
        int i = 1;
        while (treilli.size() !=0) {
            System.out.println("NEXT");
            ArrayList<Apriori> liste = algo.combinaison(i);
            algo.afficherListe(liste);
            algo.compterItems(liste);
            System.out.println("TREILLI");
            algo.afficherTreeMap();
            algo.checkMins();
            System.out.println("ELAGAGE");
            algo.afficherTreeMap();
            i++;
        }
    }

    public static void main(String[] args) {
        //Emplacement fichier
        final String donnees = "src/tp1/donnees";
        //Lecture du fichier
        String jeuDonnees = null;
        try {
            jeuDonnees = Readfile.readFileQuick(donnees, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println(e);
        }

        /**
         * ****************************************************************
         */
        Main algo = new Main();
        algo.aprioriAlgo(jeuDonnees);
        
    }

}
