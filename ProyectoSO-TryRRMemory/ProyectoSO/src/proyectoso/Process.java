package proyectoso;
import java.util.Random;
public class Process {
    private String name;
    private int TL;
    private int TC;
    private int TP;
    private String estado;
    private boolean mem;
    private String inicio;
    private String fin;
    private int memoryspace;

    private int generateRand(){
        Random random = new Random();
        int numeroAleatorio = random.nextInt(10) + 1;
        return numeroAleatorio;
    }
    
    public Process(String name) {
        this.name = name;
        this.TC = generateRand();
        this.TL = generateRand();
        this.TP = 0;
        this.mem=false;
        this.estado = "-";
    }
    
    public Process() {
        this.name = name;
        this.TC = generateRand();
        this.TL = generateRand();
        this.TP = 0;
        this.estado = "-";
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

    public boolean isMem() {
        return mem;
    }

    public void setMem(boolean mem) {
        this.mem = mem;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getTP() {
        return TP;
    }

    public void setTP(int TP) {
        this.TP = TP;
    }

    public String getInicio() {
        return inicio;
    }

    public String getFin() {
        return fin;
    }

    public void setInicio(String inicio) {
        this.inicio = inicio;
    }

    public void setFin(String fin) {
        this.fin = fin;
    }
    
    public int getMemoryspace() {
        return memoryspace;
    }
    public void setMemoryspace(int memoryspace) {
        this.memoryspace = memoryspace;
    }
    
}