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
public class BaseConditionnelle {

    private ArrayList<String[]> baseCond;
    private String item;

    public BaseConditionnelle(ArrayList<String[]> baseCond, String item) {
        this.baseCond = baseCond;
        this.item = item;
    }

    public ArrayList<String[]> getBaseCond() {
        return baseCond;
    }

    public void setBaseCond(ArrayList<String[]> baseCond) {
        this.baseCond = baseCond;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

}
