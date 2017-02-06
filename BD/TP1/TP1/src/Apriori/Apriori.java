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

    /*Constructeur*/
    public Apriori(String item, int occurence) {
        this.item = item;
    }

    public Apriori(String item) {
        this.item = item;
    }
    /*GET AND SET*/

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

}
