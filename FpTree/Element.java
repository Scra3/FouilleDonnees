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
public class Element {

    private String item;
    private Integer occurence;
    private ArrayList<String> chemin;

    public Element(String element, Integer occurence) {
        this.item = element;
        this.occurence = occurence;
        this.chemin = new ArrayList<String>();
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

    public ArrayList<String> getChemin() {
        return chemin;
    }

    public void setChemin(ArrayList<String> chemin) {
        this.chemin = chemin;
    }
    

}
