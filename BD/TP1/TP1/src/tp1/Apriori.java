/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tp1;



/**
 *
 * @author scra
 */
public class Apriori {

    private String item;
    private int occurence;

    /*Constructeur*/
    public Apriori(String item, int occurence) {
        this.item = item;
        this.occurence = occurence;
    }

    public Apriori(String item) {
        this.item = item;
        this.occurence = -1; // pas encore compté
    }
    /*GET AND SET*/

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getOccurence() {
        return occurence;
    }

    public void setOccurence(int occurence) {
        this.occurence = occurence;
    }

    /*Methodes*/
    
   

}
