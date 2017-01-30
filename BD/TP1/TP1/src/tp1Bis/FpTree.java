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

    private static TreeMap<String, Integer> L;
    private static ArrayList<HeaderTable> orderL;
    private static Node racine;
    private static int support;

    public FpTree() {
        this.L = new TreeMap<String, Integer>();
        this.orderL = new ArrayList<HeaderTable>();
        this.racine = new Node(null);
        this.support = 2;
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

    public static void addNode(String[] elements) {
        ArrayList<String> order = sortNode(elements);

        // addNode
        ArrayList<Node> successeurs = racine.getSuccesseurs();
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
                successeurs.add(new Node(new Element(order.get(i), 1)));
                Node noeud = successeurs.get(successeurs.size() - 1);
                addLink(noeud, order.get(i));
                for (int j = 0; j < i; j++) {
                    chemin.add(order.get(j));
                }

                noeud.getElement().setChemin(chemin);
                successeurs = noeud.getSuccesseurs();
            } else {
                find = false;
            }
        }
    }

    protected static void addLink(Node noeud, String nom) {
        HeaderTable ligne = null;
        Node nextNode = null;

        for (int i = 0; i < orderL.size(); i++) {
            if (orderL.get(i).getElement().getItem().equals(nom)) {
                ligne = orderL.get(i);
                break;
            }
        }

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

    protected static void buildTree(ArrayList<String[]> donnees) {
        for (int i = 0; i < donnees.size(); i++) {
            addNode(donnees.get(i));
        }
    }

    protected static ArrayList<BaseConditionnelle> findItems(ArrayList<HeaderTable> list) {
        //Base conditionnel
        list.remove(0);
        ArrayList<BaseConditionnelle> noeuds = new ArrayList<BaseConditionnelle>();
        for (HeaderTable ligne : list) {
            ArrayList<Node> noeud = new ArrayList<Node>();
            Node nextNode = ligne.getLink();
            System.out.println("Item : " + ligne.getElement().getItem());
            while (nextNode != null) {
                // n'est pas à la racine
                if (nextNode.getElement().getChemin().size() > 0) {
                    noeud.add(nextNode);
                    System.out.print("Chemin : " + nextNode.getElement().getChemin());
                    System.out.println(" : Occurences => " + nextNode.getElement().getOccurence());
                }
                nextNode = nextNode.getLink();
            }

            noeuds.add(new BaseConditionnelle(ligne.getElement().getItem(), noeud));
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

        for (Node e : noeud.getSuccesseurs()) {

            //Au dessus du support ? 
            if (e.getElement().getOccurence() >= support) {
                // Feuille ? 
                if (e.getSuccesseurs().size() == 0) {
                    frequents.add(e.getElement());
                } else {
                    if (e.getElement().getChemin().size() > 0) {
                        frequents.add(e.getElement());
                    }
                    frequents = frequentsMax(e, frequents);
                }
            }
        }
        return frequents;
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
        buildTree(donnees);
        ArrayList<BaseConditionnelle> base = findItems(orderL);
        ArrayList<Element> els = frequentsMax(racine, new ArrayList<Element>());
        System.out.println("");
        for (Element el : els) {
            System.out.println(el.getItem() + " => " + el.getChemin());
        }
        // System.out.println("ok => " + racine.getSuccesseurs().get(0).getSuccesseurs().get(0).getSuccesseurs().get(1).getLink().getElement().getItem());
        //System.out.println(" ok => " + orderL.get(1).getLink().getLink().getLink().getElement().getItem());
    }
}
