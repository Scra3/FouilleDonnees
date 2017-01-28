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
    private static ArrayList<Element> orderL;
    private static Node racine;
    private static int support;

    public FpTree() {
        this.L = new TreeMap<String, Integer>();
        this.orderL = new ArrayList<Element>();
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
            orderL.add(new Element(key, L.get(key)));
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

    public static void displayArrayList(ArrayList<Element> array) {
        for (int i = 0; i < array.size(); i++) {
            System.out.println(array.get(i).getItem() + " => " + array.get(i).getOccurence());
        }
    }

    public static void displayTable(String[] table) {
        for (int i = 0; i < table.length; i++) {
            System.out.println("Value " + table[i]);
        }
    }

    public static void getOrderList() {
        treeMapToArrayList();
        Collections.sort(orderL);
        Collections.reverse(orderL);
    }

    public static void addNode(String[] elements) {

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

        // addNode
        ArrayList<Node> successeurs = racine.getSuccesseurs();
        boolean find = false;
        for (int i = 0; i < order.size(); i++) {
            if (successeurs.size() != 0) {
                for (int j = 0; j < successeurs.size(); j++) {
                    if (order.get(i).equals(successeurs.get(j).getElement().getItem())) {
                        successeurs.get(j).getElement().setOccurence(successeurs.get(j).getElement().getOccurence() + 1);
                        successeurs = successeurs.get(j).getSuccesseurs();
                        find = true;
                        break;
                    }
                }
            }
            if (find == false) {
                successeurs.add(new Node(new Element(order.get(i), 1)));
                successeurs = successeurs.get(successeurs.size() - 1).getSuccesseurs();
            } else {
                find = false;
            }
        }
    }

    protected static void buildTree(ArrayList<String[]> donnees) {
        for (int i = 0; i < donnees.size(); i++) {
            addNode(donnees.get(i));
        }
    }

    protected static ArrayList<Element> genererItems(ArrayList<Node> successeurs, Element element, ArrayList<Element> chemin) {
        //Base conditionnel
        for (int j = 0; j < successeurs.size(); j++) {
            if (successeurs.get(j).getElement().getItem().equals(element.getItem())) {
                chemin.add(successeurs.get(j).getElement());
                //System.out.println(" " + successeurs.get(j).getElement().getItem());
                return chemin;
            } else {
                chemin = genererItems(successeurs.get(j).getSuccesseurs(), element,chemin);
            }
        }
        return chemin;
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
        getOrderList();
        displayArrayList(orderL);
        buildTree(donnees);
        //System.out.println(racine.getSuccesseurs().get(0).getSuccesseurs().get(0).getElement().getOccurence());
        ArrayList<Element> el = genererItems(racine.getSuccesseurs(), new Element("3", 0), new ArrayList<Element>());
        System.out.println("");
        displayArrayList(el);
    }
}
