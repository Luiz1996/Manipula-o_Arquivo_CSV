/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import controller.TelaController;
import view.TelaView;

/**
 *
 * @author luiz.pereira
 */
public class Main {
    public static void main(String[] args) {
        TelaView tV = new TelaView();
        TelaController tC = new TelaController(tV);
    }
}   
