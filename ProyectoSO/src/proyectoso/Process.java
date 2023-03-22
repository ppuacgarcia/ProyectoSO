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
    //TL TIEMPO LLEGADA
    private int TL;
    //TC TIEMPO CONSUMO
    private int TC;
    //TP TIEMPO PROCESADO
    private int TP;
    //bool para ver si esta en memoria
    private boolean mem;
    // int listo=0 bloqueado=1 ejecucion=3
    private int  estado;
    private int generateRand(){
        Random random = new Random();
        int numeroAleatorio = random.nextInt(10) + 1;
        return numeroAleatorio;
    }
    
    public Process(String name) {
        this.name = name;
        this.TC = generateRand();
        this.TL = generateRand();
        this.TP=0;
        this.mem=false;
    }
    
    public Process() {
        this.name = name;
        this.TC = generateRand();
        this.TL = generateRand();
    }
    
    public Process(String name, int TC, int TL){
        this.name = name;
        this.TC = TC;
        this.TL = TL;
    }
    
    public int getEstado(){
        return this.estado;
    }
    
    public void setEstado(int estado){
        this.estado=estado;
    }
    public int getTP(){
        return this.TP;
    }
    
    public void setTP(int tp){
        this.TP=tp;
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
