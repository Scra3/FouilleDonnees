/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FpTree;

import java.util.ArrayList;

/**
 *
 * @author scra
 */
public class Node {

    private Element element;
    private ArrayList<Node> successeurs;
    private Node link;

    public Node(Element element) {
        this.element = element;
        this.successeurs = new ArrayList<Node>();
        this.link = null;
    }

    public Node getLink() {
        return link;
    }

    public void setLink(Node link) {
        this.link = link;
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
