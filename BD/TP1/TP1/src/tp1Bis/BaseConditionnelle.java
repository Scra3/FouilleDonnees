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
public class BaseConditionnelle {

    private String nomItem;
    private ArrayList<Node> noeuds;

    public BaseConditionnelle(String nomItem, ArrayList<Node> noeuds) {
        this.nomItem = nomItem;
        this.noeuds = noeuds;
    }


    public String getNomItem() {
        return nomItem;
    }

    public void setNomItem(String nomItem) {
        this.nomItem = nomItem;
    }

    public ArrayList<Node> getNoeuds() {
        return noeuds;
    }

    public void setNoeuds(ArrayList<Node> noeuds) {
        this.noeuds = noeuds;
    }

}
