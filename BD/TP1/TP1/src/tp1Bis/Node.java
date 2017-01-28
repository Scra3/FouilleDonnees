/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp1Bis;

import java.util.ArrayList;

/**
 *
 * @author scra
 */
public class Node {

    private Element element;
    private ArrayList<Node> successeurs;

    public Node(Element element) {
        this.element = element;
        this.successeurs = new ArrayList<Node>();
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public ArrayList<Node> getSuccesseurs() {
        return successeurs;
    }

    public void setSuccesseurs(ArrayList<Node> successeurs) {
        this.successeurs = successeurs;
    }

}
