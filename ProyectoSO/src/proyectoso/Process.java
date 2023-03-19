/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoso;

import java.util.Random;

/**
 *
 * @author USUARIO
 */
public class Process {
    private String name;
    private int TL;
    private int TC;
    private boolean mem;

    private int generateRand(){
        Random random = new Random();
        int numeroAleatorio = random.nextInt(10) + 1;
        return numeroAleatorio;
    }
    
    public Process(String name) {
        this.name = name;
        this.TC = generateRand();
        this.TL = generateRand();
        this.mem=false;
    }
    
    public Process() {
        this.name = name;
        this.TC = generateRand();
        this.TL = generateRand();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public Boolean getMem() {
        return mem;
    }

    public void setMem(Boolean mem) {
        this.mem = mem;
    }
    public int getTL() {
        return TL;
    }

    public void setTL(int TL) {
        this.TL = TL;
    }

    public int getTC() {
        return TC;
    }

    public void setTC(int TC) {
        this.TC = TC;
    }
    
    
    
}
