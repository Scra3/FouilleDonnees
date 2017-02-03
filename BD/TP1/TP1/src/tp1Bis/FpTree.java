package tp1Bis;

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
    private static ArrayList<HeaderTable> orderL = new ArrayList<HeaderTable>();

    private Node racine;
    private static int support = 2;

    public FpTree() {
        this.racine = new Node(null);
    }

    public Node getRacine() {
        return racine;
    }

    public void setRacine(Node racine) {
        this.racine = racine;
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

    public static void treeMapToArrayList() {
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
        treeMapToArrayList();
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
                for (int k = 0; k < orderL.size(); k++) {
                    if (orderL.get(k).getElement().getItem().equals(order.get(i))) {
                        ligne = orderL.get(k);
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

    public static ArrayList<Element> frequentsMax(Node noeud, ArrayList<Element> frequents) {
        boolean nodeMax = true;
        //Pour tous les succeceurs 
        for (Node succeceurs : noeud.getSuccesseurs()) {
            //on check le supp
            if (support <= succeceurs.getElement().getOccurence()) {
                if (succeceurs.getSuccesseurs().size() == 0) {
                    frequents.add(succeceurs.getElement());
                } else {
                    frequents = frequentsMax(succeceurs, frequents);
                }
                nodeMax = false;
            }
        }
        if (nodeMax == true) {
            frequents.add(noeud.getElement());
        }

        return frequents;
    }

    private static ArrayList<ItemSets> generationItemsets(Node node, String item, ArrayList<ItemSets> items) {
        // a chaque noeud
        for (Node nextNode : node.getSuccesseurs()) {
            if (nextNode.getElement().getOccurence() >= support) {
                //System.out.println("feezferfpirfreoggerojerfoezjfosakt re ha");
                //On ajoute le noeud
                ArrayList<String> chemin = new ArrayList<>();
                chemin.add(nextNode.getElement().getItem());
                chemin.add(item);
                items.add(new ItemSets(nextNode.getElement().getOccurence(), chemin));

                if (nextNode.getElement().getChemin().size() > 0) {
                    ArrayList<String> way = nextNode.getElement().getChemin();
                    way.add(nextNode.getElement().getItem());
                    way.add(item);
                    items.add(new ItemSets(nextNode.getElement().getOccurence(), way));
                }
                if (nextNode.getSuccesseurs().size() > 0) {
                    items = generationItemsets(nextNode, item, items);
                }
            }
        }

        return items;
    }

    private static void phaseExploration() {
        ArrayList<String[]> frequentMax = new ArrayList<String[]>();

        for (int i = orderL.size() - 1; i >= 1; i--) {
            System.out.println("Base conditionnellle : ");
            ArrayList<String[]> baseCond = getBaseConditionnelle(orderL.get(i));
            //On construir l'arbre conditionnelle
            if (baseCond.size() > 0) {
                System.out.println("Construction du fpTree conditionnelle");

                FpTree treeCond = new FpTree();
                //On construit le fpTree conditionnelle
                buildTree(baseCond, treeCond, false);

                System.out.println("Génération Itemsets");
                // On génère les itemssets
                ArrayList<ItemSets> items = generationItemsets(treeCond.getRacine(), orderL.get(i).getElement().getItem(), new ArrayList<ItemSets>());

                int maxTailleItems = 0;
                ArrayList<ItemSets> itemsMax = new ArrayList<>();
                //On cherche la taille du plus longs chemin
                for (ItemSets item : items) {
                    System.out.println("Items : " + item.getNoeuds());
                    System.out.println("Occurence : " + item.getOccurence());
                    if (maxTailleItems <= item.getNoeuds().size()) {
                        maxTailleItems = item.getNoeuds().size();
                    }
                }
                //On chercher les chemins de longueurs "plus long chemin" => maxTailleItems
                for (ItemSets item : items) {
                    if (maxTailleItems <= item.getNoeuds().size()) {
                        itemsMax.add(item);
                    }
                }

                //On vérifie si il est un sous ensemble d'un fréquent, si oui alors on ne l'ajoute pas car il n'est pas fréquent max
                for (ItemSets itMax : itemsMax) {
                    String[] s = new String[itemsMax.size()];
                    frequentMax.add(itMax.getNoeuds().toArray(s));
                }
            }
        }
        //On construit l'arbre et on prend seulement les feuilles.
        FpTree treeFeuille = new FpTree();
        buildTree(frequentMax, treeFeuille, false);
        System.out.println("Fréquent Maximum");
        ArrayList<Node> feuilles = new ArrayList<Node>();
        feuilles = getFeuilles(treeFeuille.getRacine(), feuilles);
        for (Node feuille : feuilles) {
            System.out.println(" FREQUENT " + feuille.getElement().getChemin());
        }
    }

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
        final String chemin = "src/tp1Bis/donnees";
        // On get le contenu
        String jeuDonnees = getFile(chemin);
        ArrayList<String[]> donnees = split(jeuDonnees);
        countElements(donnees);
        displayTreeMap(L);
        getOrderList(orderL);
        removeBySupport(orderL);

        System.out.println("HEADER TABLE");
        displayArrayList(orderL);
        System.out.println("");
        buildTree(donnees, tree, true);
        phaseExploration();
    }
}
