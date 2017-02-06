/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp1Bis;

import java.util.ArrayList;

/**
 *
 * @author b16007026
 */
public class ItemSets {

    private Integer occurence;
    private ArrayList<String> noeuds;

    public ItemSets(Integer occurence, ArrayList<String> noeuds) {
        this.occurence = occurence;
        this.noeuds = noeuds;
    }

    
    public Integer getOccurence() {
        return occurence;
    }

    public void setOccurence(Integer occurence) {
        this.occurence = occurence;
    }

    public ArrayList<String> getNoeuds() {
        return noeuds;
    }

    public void setNoeuds(ArrayList<String> noeuds) {
        this.noeuds = noeuds;
    }

    
}