/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp1Bis;

/**
 *
 * @author scra
 */
public class Element implements Comparable<Element> {

    private String item;
    private Integer occurence;

    public Element(String element, Integer occurence) {
        this.item = element;
        this.occurence = occurence;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Integer getOccurence() {
        return occurence;
    }

    public void setOccurence(Integer occurence) {
        this.occurence = occurence;
    }

    @Override
    public int compareTo(Element o) {
        return this.getOccurence().compareTo(o.getOccurence());
    }
}
