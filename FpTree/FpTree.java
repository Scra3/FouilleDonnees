package FpTree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author scra
 *
 */
public class FpTree extends Readfile {

    private static TreeMap<String, Integer> L = new TreeMap<String, Integer>();
    private ArrayList<HeaderTable> orderL;
    private Node racine;
    private static int support = 2;

    public FpTree() {
        this.racine = new Node(null);
        this.orderL = new ArrayList<HeaderTable>();
    }

    public Node getRacine() {
        return racine;
    }

    public void setRacine(Node racine) {
        this.racine = racine;
    }

    public ArrayList<HeaderTable> getOrderL() {
        return orderL;
    }

    public void setOrderL(ArrayList<HeaderTable> orderL) {
        this.orderL = orderL;
    }

    protected static ArrayList<String[]> split(String donnees) {
        String[] donneesLignes = donnees.split("\n");

        ArrayList<String[]> lignes = new ArrayList<String[]>();

        for (int i = 2; i < donneesLignes.length; i++) {
            String[] table = donneesLignes[i].split(" ");
            lignes.add(Arrays.copyOfRange(table, 1, table.length));
        }
        return lignes;
    }

    protected static void countElements(ArrayList<String[]> elements) {
        for (int i = 0; i < elements.size(); i++) {
            for (int j = 0; j < elements.get(i).length; j++) {
                if (L.containsKey(elements.get(i)[j])) {
                    L.put(elements.get(i)[j], L.get(elements.get(i)[j]) + 1);
                } else {
                    L.put(elements.get(i)[j], 1);
                }
            }
        }
    }

    public static void treeMapToArrayList(ArrayList<HeaderTable> orderL) {
        // Afficher le contenu du MAP
        Set listKeys = L.keySet();  // Obtenir la liste des clés
        Iterator iterateur = listKeys.iterator();
        // Parcourir les clés et afficher les entrées de chaque clé;
        while (iterateur.hasNext()) {
            String key = (String) iterateur.next();
            orderL.add(new HeaderTable(new Element(key, L.get(key))));
        }
    }

    public static void displayTreeMap(TreeMap<String, Integer> elements) {
        // Afficher le contenu du MAP
        Set listKeys = elements.keySet();  // Obtenir la liste des clés
        Iterator iterateur = listKeys.iterator();
        // Parcourir les clés et afficher les entrées de chaque clé;
        while (iterateur.hasNext()) {
            Object key = iterateur.next();
            System.out.println(key + "=>" + elements.get(key));
        }
    }

    public static void displayArrayList(ArrayList<HeaderTable> array) {
        for (int i = 0; i < array.size(); i++) {
            System.out.println(array.get(i).getElement().getItem() + " => " + array.get(i).getElement().getOccurence());
            for (int j = 0; j < array.get(i).getElement().getChemin().size(); j++) {
                System.out.println(" chemin => " + array.get(i).getElement().getChemin().get(j));
            }
        }
    }

    public static void displayArrayListNode(ArrayList<Node> array) {
        for (int i = 0; i < array.size(); i++) {
            System.out.println(array.get(i).getElement().getChemin() + " : " + array.get(i).getElement().getOccurence());
        }
    }

    public static void displayTable(String[] table) {
        for (int i = 0; i < table.length; i++) {
            System.out.println("Value " + table[i]);
        }
    }

    public static void getOrderList(ArrayList<HeaderTable> orderL) {
        treeMapToArrayList(orderL);
        Collections.sort(orderL);
        Collections.reverse(orderL);
    }

    public static ArrayList<String> sortNode(String[] elements) {
        ArrayList<String> order = new ArrayList<String>();

        for (int i = 0; i < elements.length; i++) {
            order.add(elements[i]);
        }

        Collections.sort(order, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                Integer val1 = L.get(o1);
                Integer val2 = L.get(o2);
                return val2.compareTo(val1);
            }
        });
        return order;
    }

    public static void addNode(String[] elements, FpTree fp, boolean link) {
        ArrayList<String> order = sortNode(elements);

        // addNode
        ArrayList<Node> successeurs = fp.getRacine().getSuccesseurs();
        boolean find = false;
        for (int i = 0; i < order.size(); i++) {
            ArrayList<String> chemin = new ArrayList<String>();

            if (successeurs.size() != 0) {
                for (int j = 0; j < successeurs.size(); j++) {
                    Node noeud = successeurs.get(j);
                    if (order.get(i).equals(noeud.getElement().getItem())) {
                        noeud.getElement().setOccurence(noeud.getElement().getOccurence() + 1);
                        successeurs = noeud.getSuccesseurs();
                        find = true;
                        break;
                    }
                }
            }
            if (find == false) {
                HeaderTable ligne = null;
                for (int k = 0; k < fp.getOrderL().size(); k++) {
                    if (fp.getOrderL().get(k).getElement().getItem().equals(order.get(i))) {
                        ligne = fp.getOrderL().get(k);
                        break;
                    }
                }
                if (ligne != null) {
                    successeurs.add(new Node(new Element(order.get(i), 1)));

                    Node noeud = successeurs.get(successeurs.size() - 1);
                    if (link == true) {
                        addLink(noeud, order.get(i), ligne);
                    }
                    for (int j = 0; j < i; j++) {
                        chemin.add(order.get(j));
                    }

                    noeud.getElement().setChemin(chemin);
                    successeurs = noeud.getSuccesseurs();
                }
            } else {
                find = false;
            }
        }
    }

    protected static void addLink(Node noeud, String nom, HeaderTable ligne) {
        Node nextNode = null;

        if (ligne.getLink() != null) {
            nextNode = ligne.getLink();

            while (nextNode.getLink() != null) {
                nextNode = nextNode.getLink();
            }
            nextNode.setLink(noeud);

        } else {
            ligne.setLink(noeud);
        }

    }

    protected static void buildTree(ArrayList<String[]> donnees, FpTree fp, boolean link) {
        for (int i = 0; i < donnees.size(); i++) {
            addNode(donnees.get(i), fp, link);
        }
    }

    protected static ArrayList<String[]> getBaseConditionnelle(HeaderTable ligne) {
        //Base conditionnel
        ArrayList<String[]> noeuds = new ArrayList<String[]>();
        Node nextNode = ligne.getLink();
        System.out.println("Item : " + ligne.getElement().getItem());
        while (nextNode != null) {
            // n'est pas à la racine
            if (nextNode.getElement().getChemin().size() > 0) {
                System.out.print("Chemin : " + nextNode.getElement().getChemin());
                System.out.println(" : Occurences => " + nextNode.getElement().getOccurence());

                ArrayList<String> chemin = nextNode.getElement().getChemin();
                String[] cheminTable = new String[chemin.size()];
                cheminTable = chemin.toArray(cheminTable);

                //Le nombre d'occurence du chemin
                for (int i = 0; i < nextNode.getElement().getOccurence(); i++) {
                    noeuds.add(cheminTable);
                }
            }

            nextNode = nextNode.getLink();
        }
        return noeuds;
    }

    public static void removeBySupport(ArrayList<HeaderTable> orderL) {
        for (int i = orderL.size() - 1; i > 0; i--) {
            if (orderL.get(i).getElement().getOccurence() < support) {
                orderL.remove(i);
            } else {
                break;
            }
        }
    }

    private static ArrayList<ItemSets> generationItemsets(ArrayList<HeaderTable> headerTable, String item, ArrayList<ItemSets> items) {

        //Pour tous les liens
        for (HeaderTable link : headerTable) {
            int occurences = 0;

            if (link.getLink() != null) {
                occurences = link.getLink().getElement().getOccurence();
                Node nextNode = link.getLink().getLink();
                while (nextNode != null) {
                    occurences += nextNode.getElement().getOccurence();
                    nextNode = nextNode.getLink();
                }
            }

            if (link.getLink() != null && occurences >= support) {
                ArrayList<String> save = new ArrayList<>();
                Node nextNode = link.getLink();

                save.add(nextNode.getElement().getItem());
                save.add(item);
                items.add(new ItemSets(occurences, save));

                while (nextNode != null) {
                    if (nextNode.getElement().getChemin().size() > 0 && nextNode.getElement().getOccurence() >= support) {
                        save = nextNode.getElement().getChemin();
                        save.add(nextNode.getElement().getItem());
                        save.add(item);
                        items.add(new ItemSets(nextNode.getElement().getOccurence(), save));
                    }
                    nextNode = nextNode.getLink();
                }
            }
        }
        return items;
    }

    private static ArrayList<String[]> phaseExploration(FpTree tree) {
        ArrayList<String[]> frequentMax = new ArrayList<String[]>();
        ArrayList<BaseConditionnelle> bases = new ArrayList<BaseConditionnelle>();
        for (int i = tree.getOrderL().size() - 1; i >= 1; i--) {
            System.out.println("Base conditionnellle : ");
            ArrayList<String[]> baseCond = getBaseConditionnelle(tree.getOrderL().get(i));
            bases.add(new BaseConditionnelle(baseCond, tree.getOrderL().get(i).getElement().getItem()));
        }

        System.out.println("Itemsets générés : ");
        for (BaseConditionnelle base : bases) {
            FpTree fp = new FpTree();
            //On reforme les liens de l'arbre
            for (HeaderTable link : tree.getOrderL()) {
                link.setLink(null);
            }

            fp.setOrderL(tree.getOrderL());
            buildTree(base.getBaseCond(), fp, true);
            //System.out.println(" TESTTT  " + fp.getOrderL().get(1).getLink().getElement().getOccurence());

            System.out.println(" ITEM " + base.getItem());
            ArrayList<ItemSets> items = generationItemsets(fp.getOrderL(), base.getItem(), new ArrayList<ItemSets>());

            int indice = 0;
            int taille = 0;
            int i = 0;
            //Affichage des items sets
            for (ItemSets item : items) {
                if (item.getNoeuds().size() >= taille) {
                    taille = item.getNoeuds().size();
                    indice = i;
                }
                i++;
                System.out.println(" CHEMIN :" + item.getNoeuds() + " => occurrences : " + item.getOccurence());
            }
            //Prendre le plus long chemin de chaque item
            System.out.println("Le plus long chemin : " + items.get(indice).getNoeuds());
            ArrayList<String> chemin = items.get(indice).getNoeuds();
            String[] cheminTable = new String[chemin.size()];
            cheminTable = chemin.toArray(cheminTable);
            frequentMax.add(cheminTable);
        }
        return frequentMax;
    }

    //get les feuilles de l'abre
    public static ArrayList<Node> getFeuilles(Node noeu, ArrayList<Node> feuilles) {
        for (Node nextNode : noeu.getSuccesseurs()) {
            if (nextNode.getSuccesseurs().size() == 0) {
                nextNode.getElement().getChemin().add(nextNode.getElement().getItem());
                feuilles.add(nextNode);
            } else {
                feuilles = getFeuilles(nextNode, feuilles);
            }
        }
        return feuilles;
    }

    public static void main(String[] args) {
        FpTree tree = new FpTree();
        //Emplacement fichier
        final String chemin = "src/FpTree/donnees";
        // On recupere le contenu du fichier
        String jeuDonnees = getFile(chemin);
        // On le split au format voulu
        ArrayList<String[]> donnees = split(jeuDonnees);
        // On compte les items
        countElements(donnees);
        displayTreeMap(L);
        //On ordonne la liste des items
        getOrderList(tree.getOrderL());
        //On supprime les items qui ne respecte pas le support
        removeBySupport(tree.getOrderL());
        System.out.println("HEADER TABLE");
        displayArrayList(tree.getOrderL());
        //On save l'orderL sans les links
        System.out.println("");
        //On construit le FPTREE avec les liens(TRUE)
        buildTree(donnees, tree, true);
        //On explore l'abre pour trouver les fréquent MAX
        ArrayList<String[]> frequentMax = phaseExploration(tree);
        //On détermine les fréquent max
        tree.setRacine(new Node(null));
        buildTree(frequentMax, tree, true);
        ArrayList<Node> feuilles = getFeuilles(tree.getRacine(), new ArrayList<Node>());
        
        //Affichage des fréquents Max
        System.out.println("Fréquents max :");
        for (Node feuille : feuilles) {
            System.out.println("FrequentMax " + feuille.getElement().getChemin());
        }
    }
}
