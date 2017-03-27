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
public class HeaderTable implements Comparable<HeaderTable> {

    private Element element;
    private Node link;

    public HeaderTable(Element element) {
        this.element = element;
        this.link = null;
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public Node getLink() {
        return link;
    }

    public void setLink(Node link) {
        this.link = link;
    }

    @Override
    public int compareTo(HeaderTable o) {
        if (0 == this.getElement().getOccurence().compareTo(o.getElement().getOccurence())) {
            return -1;
        } else {
            return this.getElement().getOccurence().compareTo(o.getElement().getOccurence());
        }
    }

}
