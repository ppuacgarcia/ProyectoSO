package proyectoso;

import java.awt.Point;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class Procesos extends javax.swing.JFrame{
    //Procesos de referencia en consola para verificar el funcionamiento del programa
    //variables generales
    private String[] Procesos = new String[15];
    private String Mem_array[] = {" "," "," "," "," "," "," "," "," "," "," "," "," ","Activador","SO","SO"}; // variable que se usa para representar la matriz
    private String Direc_array[] = {"0xAFFh","0xA5Ah","0x9B5h","0x910h","0x86Bh","0x7C6h","0x721h","0x67Ch","0x5D7h","0x532h","0x48Dh","0x3E8h","0x343h","0x29Eh","0x1F9h","0x154h","0x000h"};
    // variable que se usa para direcciones de memoria por segundo
    private Process[] proc = new Process[10];
    private Procesos.Reloj HoraActual=new Procesos.Reloj();
    private int mC=0; 
    private Process[] proc_enMem = new Process[10];
    private int cont_proc_enMem = 0;
    private Calendar TiempoActual;
    private RoundRobin rr = new RoundRobin(TiempoActual);
    //metodo adicional para random
    private int generateRand(){
        Random random = new Random();
        int numeroAleatorio = random.nextInt(12) + 1;
        return numeroAleatorio;
    }
    //metodo de limpieza inicial
    public void clearElements(){
        TextPlanificador.setText("");
        TextPc.setText("");
        TextH.setText("");
        TextB.setText("");
        //Limpiar tablas e insertar columnas
        createProcess();
        DefaultTableModel modelPL = new DefaultTableModel();
        modelPL.addColumn("ID");
        modelPL.addColumn("State");
        modelPL.addColumn("Init");
        modelPL.addColumn("End");
        ProcList.setModel(modelPL);
        //Limpiar lista al inicio de la ejecucion
        DefaultListModel listmodel = new DefaultListModel();
        PMemoryList.setModel(listmodel);
        DireccionesList.setListData(Direc_array);
    }
    //asignacion de procesos, TL y TC
    public void createProcess(){
        //Crear procesos
        for(int i = 0; i < proc.length; i++){
            String name = (String)(((char)(i+65))+"");
            proc[i] = new Process(name);
        }
        //Modelo de Tabla
        DefaultTableModel modelDT = new DefaultTableModel();
        modelDT.addColumn("P");
        modelDT.addColumn("TL");
        modelDT.addColumn("TC");
        //Ciclo de llenado de tabla
        for(int i = 0; i < proc.length; i++){
            String Name = proc[i].getName();
            int TL = proc[i].getTL();
            int TC = proc[i].getTC();
            modelDT.addRow(new Object[]{Name, TL, TC});
        }
        DescripTable.setModel(modelDT);
    }
    
    //metodo para verificar si la posicion de la memoria esta disponible o no
    public boolean verifypos(int position, int process_len){
        boolean bandera =  false;
        for(int i = position; i < position+process_len; i++){
            if(Mem_array[i] != " "){
                bandera = true;
            }
        }
        return bandera;
    }
    public void processVerify(){
        DefaultTableModel modelDT2 = new DefaultTableModel();
        modelDT2=(DefaultTableModel) DescripTable.getModel();
        int TL=0;
        int TC=0;
        for(int i=0;i<proc.length;i++){
          
           TL=Integer.parseInt(modelDT2.getValueAt(i, 1).toString());
           TC=Integer.parseInt(modelDT2.getValueAt(i, 2).toString());
           this.proc[i].setTL(TL);
           this.proc[i].setTC(TC);
        }
    }
    public void ListFill(){
        DefaultTableModel modelT = new DefaultTableModel();
        modelT.addColumn("ID");
        modelT.addColumn("ESTADO");
        modelT.addColumn("HORA INICIO");
        modelT.addColumn("HORA FINALIZACION");
        Calendar auxCal = TiempoActual;
        int quantumAdd = 0;
        for(int v = 0; v<cont_proc_enMem; v++){
            if (v==0)
            {
                auxCal.add(Calendar.SECOND, proc_enMem[v].getTL());
                modelT.addRow(new Object[]{proc_enMem[v].getName(),"-","-",""});
            }
            else
            {
                auxCal.add(Calendar.SECOND, proc_enMem[v].getTL()-proc_enMem[v-1].getTL());
                modelT.addRow(new Object[]{proc_enMem[v].getName(),"-","-",""});
            }
        }
        this.ProcList.setModel(modelT);
        this.TextPlanificador.setText(proc[0].getName());
        
    }
    //llenado de lista de memoria principal
    public void memory_fill(){
        processVerify();
        boolean bandera = false;
        DefaultListModel listmodel = new DefaultListModel();
        Process[] TLOrder = this.proc.clone();
        //Ordenamiento Burbuja para tiempos de llegada
        for (int i = 0; i < proc.length - 1; i++) {
            for (int j = 0; j < proc.length - i - 1; j++) {
                if (TLOrder[j].getTL() > TLOrder[j + 1].getTL()) {
                    Process temp = TLOrder[j];
                    TLOrder[j] = TLOrder[j + 1];
                    TLOrder[j + 1] = temp;
                }
            }
        }
        String[] auxMem = Mem_array.clone();
        //Escritura en la lista de memoria
        for (int i = 0; i < proc.length; i++) {
            //Variables booleanas solo para verificacion de condiciones
            boolean spacemem = true;
            boolean aux = true;
            //Ciclo para ingresar los procesos en la memoria
            for(int k = 0; k < 10; k++){
                //generar numero random para ponerlo en la memoria
                int rnd = generateRand();
                
                if(rnd + TLOrder[i].getTC() - 1 < 14){
                    
                    if(verifypos(rnd-1, TLOrder[i].getTC()) == false){
                        aux = false;
                        //insercion de los procesos en el vector para luego ponerlos en la lista de memoria
                        TLOrder[i].setMemoryspace(rnd - 1);
                        for (int j = 0; j < TLOrder[i].getTC(); j++) {
                            Mem_array[rnd-1+j] = TLOrder[i].getName();
                        }
                        proc_enMem[cont_proc_enMem] = TLOrder[i];
                        cont_proc_enMem++;
                        break;
                    }  
                }
                if(k == 9) spacemem = false;
            }
            if(spacemem == false){
                    System.out.println("Memoria insuficiente para proceso "+ TLOrder[i].getName());
            }else {
                this.mC++;
                proc[i].setMem(true);
            }
        }
        Mem_array = auxMem.clone();
        ListFill();
    }
    
    
    
    /**
     * Creates new form Procesos
     */
    public Procesos() {
        initComponents();
        HoraActual.start();
        clearElements();
    }
    public void DeleteProcessMemory(Process aux){
        DefaultListModel listmodel = new DefaultListModel();
            for(int j = 0; j < aux.getTP(); j++){
                Mem_array[j + aux.getMemoryspace()] = " ";
            }
        for(int i = 0; i < 16; i++){
            listmodel.addElement(Mem_array[i]);
        }
        PMemoryList.setModel(listmodel);
    }
    
    public void AddProcessMemory(Process aux){
        DefaultListModel listmodel = new DefaultListModel();
        for(int j = 0; j < aux.getTC(); j++){
            Mem_array[j + aux.getMemoryspace()] = aux.getName();
        }
        for(int i = 0; i < 16; i++){
            listmodel.addElement(Mem_array[i]);
        }
        PMemoryList.setModel(listmodel);
    }
    
    public class RoundRobin extends Thread {
        private Calendar Tiempo;
        private int TP;
        private String Pactual;
        private int quantum;
        private String preProceso = "-";
        
        public RoundRobin(Calendar Tiempo) {
            this.Tiempo = Tiempo;
            this.TP=0;
        }

        public Calendar getTiempo() {
            return Tiempo;
        }

        public void setTiempo(Calendar Tiempo) {
            this.Tiempo = Tiempo;
        }
        
        public void ActualizarBH(int contador){
            String aux[] = Direc_array.clone();
            aux[proc_enMem[contador].getMemoryspace()] = "h";
            aux[proc_enMem[contador].getMemoryspace()+proc_enMem[contador].getTC()] = "b";
            DireccionesList.setListData(aux);
        }
        
        public void Activador(String proceso){
            if (!preProceso.equals(proceso)){
                preProceso = proceso;
                TextPlanificador.setText("Activador");
                String aux[] = Direc_array.clone();
                TextH.setText(aux[aux.length-3]);
                TextB.setText(aux[aux.length-4]);
                aux[aux.length-3] = "h";
                aux[aux.length-4] = "b";
                DireccionesList.setListData(aux);
                TextPc.setText(TextH.getText());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
                }
                TextPc.setText(TextB.getText());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        @Override
        public void run(){
            int auxContP = 0;
            try {
                Thread.sleep(proc_enMem[auxContP].getTL()*1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
            }
            AddProcessMemory(proc_enMem[auxContP]);
            TextPlanificador.setText(proc_enMem[auxContP].getName());
            proc_enMem[auxContP].setEstado("Ejecucion");
            int bpos=66+((proc_enMem[auxContP].getMemoryspace()+proc_enMem[auxContP].getTC())*20);
            int hpos=66+((proc_enMem[auxContP].getMemoryspace())*20);
            
            //position(bpos,hpos);
            TextB.setText(Direc_array[proc_enMem[auxContP].getMemoryspace()+proc_enMem[auxContP].getTC()]);
            TextH.setText(Direc_array[proc_enMem[auxContP].getMemoryspace()]);
            ProcList.setValueAt(proc_enMem[auxContP].getEstado(), auxContP, 1);
            proc_enMem[auxContP].setInicio(jLabel7.getText());
            ProcList.setValueAt(proc_enMem[auxContP].getInicio(), auxContP, 2);
            int terminados = 0;
            while (true){
                for (int i = 0; i<cont_proc_enMem; i++){
                    if ((this.TP==proc_enMem[i].getTL())){
                        if (proc_enMem[i].getEstado().equals("-")){
                            proc_enMem[i].setInicio(jLabel7.getText());
                            ProcList.setValueAt(proc_enMem[i].getInicio(), i, 2);
                            TextPc.setText(Direc_array[proc_enMem[i].getMemoryspace()]);
                        }
                        AddProcessMemory(proc_enMem[i]);
                        proc_enMem[i].setEstado("Listo");
                        ProcList.setValueAt(proc_enMem[i].getEstado(), i, 1);
                        TextPc.setText(Direc_array[proc_enMem[i].getMemoryspace()]);
                    }
                    if (proc_enMem[i].getTP()>=proc_enMem[i].getTC()){
                        if (proc_enMem[i].getEstado().equals("Listo")){
                            proc_enMem[i].setFin(jLabel7.getText());
                            ProcList.setValueAt(proc_enMem[i].getFin(), i, 3);
                            TextPc.setText(Direc_array[proc_enMem[i].getMemoryspace()]);
                        }
                        proc_enMem[i].setEstado("Terminado");
                        DeleteProcessMemory(proc_enMem[i]);
                        ProcList.setValueAt(proc_enMem[i].getEstado(), i, 1);
                        terminados++;
                        TextPc.setText(Direc_array[proc_enMem[i].getMemoryspace()]);
                    }
                }
                if (terminados>=cont_proc_enMem){
                    proc_enMem[auxContP].setFin(jLabel7.getText());
                    ProcList.setValueAt(proc_enMem[auxContP].getFin(), auxContP, 3);
                    proc_enMem[auxContP].setEstado("Terminado");
                    DeleteProcessMemory(proc_enMem[auxContP]);
                    ProcList.setValueAt(proc_enMem[auxContP].getEstado(), auxContP, 1);
                    System.out.println("Terminado");
                    DireccionesList.setListData(Direc_array);
                    break;
                }
                else{
                    terminados = 0;
                }
                if ((quantum<3)&&(proc_enMem[auxContP].getTP()<proc_enMem[auxContP].getTC())){
                    TextPlanificador.setText(proc_enMem[auxContP].getName());
                    proc_enMem[auxContP].setTP(proc_enMem[auxContP].getTP()+1);
                    proc_enMem[auxContP].setEstado("Ejecucion");
                    ActualizarBH(auxContP);
                    TextB.setText(Direc_array[proc_enMem[auxContP].getMemoryspace()+proc_enMem[auxContP].getTC()]);
                    TextH.setText(Direc_array[proc_enMem[auxContP].getMemoryspace()]);
                    ProcList.setValueAt(proc_enMem[auxContP].getEstado(), auxContP, 1);
                    quantum++;
                }
                else{
                    if (proc_enMem[auxContP].getTP()>=proc_enMem[auxContP].getTC()){
                        proc_enMem[auxContP].setFin(jLabel7.getText());
                        ProcList.setValueAt(proc_enMem[auxContP].getFin(), auxContP, 3);
                        proc_enMem[auxContP].setEstado("Terminado");
                        DeleteProcessMemory(proc_enMem[auxContP]); 
                        ProcList.setValueAt(proc_enMem[auxContP].getEstado(), auxContP, 1);
                    }
                    else{
                        AddProcessMemory(proc_enMem[auxContP]);
                        proc_enMem[auxContP].setEstado("Listo");
                        ProcList.setValueAt(proc_enMem[auxContP].getEstado(), auxContP, 1);
                        //DireccionesList.set
                    }
                    auxContP++;
                    for(int k = 0; k<cont_proc_enMem; k++){
                        if (auxContP>=cont_proc_enMem){
                            auxContP=0; 
                        }
                        if ((proc_enMem[auxContP].getTP()<proc_enMem[auxContP].getTC())&&(proc_enMem[auxContP].getEstado().equals("Listo"))){
                            Activador(proc_enMem[auxContP].getName());
                            TextPlanificador.setText(proc_enMem[auxContP].getName());
                            proc_enMem[auxContP].setTP(proc_enMem[auxContP].getTP()+1);
                            proc_enMem[auxContP].setEstado("Ejecucion");
                            ActualizarBH(auxContP);
                            bpos=66+(proc_enMem[auxContP].getMemoryspace()+proc_enMem[auxContP].getTC())*20;
                            hpos=66+proc_enMem[auxContP].getMemoryspace()*20;
                            
                            TextB.setText(Direc_array[proc_enMem[auxContP].getMemoryspace()+proc_enMem[auxContP].getTC()]);
                            TextH.setText(Direc_array[proc_enMem[auxContP].getMemoryspace()]);
                            ProcList.setValueAt(proc_enMem[auxContP].getEstado(), auxContP, 1);
                            quantum=1;
                            break;
                        }
                        else{
                            auxContP++;
                        }
                       
                    }

                }
                try {
                    Thread.sleep(920);
                    this.TP ++;
                } catch (InterruptedException ex) {
                    Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }  
    }
    public void ActualizarInicio(){
        Calendar auxTime = TiempoActual;
        for (int i = 0; i<cont_proc_enMem; i++){
                if (i==0){
                    try {
                        Thread.sleep(proc_enMem[0].getTL()*1000);
                        ProcList.setValueAt(jLabel7.getText(), 0, 1);
                        ProcList.setValueAt(jLabel7.getText(), 0, 1);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else{
                    try {
                        Thread.sleep(proc_enMem[0].getTL()*1000);
                        ProcList.setValueAt(jLabel7.getText(), 0, 1);
                        ProcList.setValueAt(jLabel7.getText(), 0, 1);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        DescripTable = new javax.swing.JTable();
        BtnInit = new javax.swing.JButton();
        btnReiniciar = new javax.swing.JButton();
        btnMasP = new javax.swing.JButton();
        btnMenosP = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        PMemoryList = new javax.swing.JList<>();
        jPanel3 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        TextPlanificador = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        ProcList = new javax.swing.JTable();
        TextPc = new javax.swing.JTextField();
        TextB = new javax.swing.JTextField();
        TextH = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        DireccionesList = new javax.swing.JList<>();

        jLabel3.setText("Memoria Principal");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 0, 0));

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));

        DescripTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(DescripTable);

        BtnInit.setText("Iniciar");
        BtnInit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnInitActionPerformed(evt);
            }
        });

        btnReiniciar.setText("Reiniciar");
        btnReiniciar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReiniciarActionPerformed(evt);
            }
        });

        btnMasP.setText("+");
        btnMasP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMasPActionPerformed(evt);
            }
        });

        btnMenosP.setText("-");
        btnMenosP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMenosPActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnMasP, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(btnMenosP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(82, 82, 82)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(BtnInit, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
                            .addComponent(btnReiniciar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(157, 157, 157)
                        .addComponent(btnMasP)
                        .addGap(18, 18, 18)
                        .addComponent(btnMenosP)))
                .addGap(18, 18, 18)
                .addComponent(BtnInit, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnReiniciar, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));

        PMemoryList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6", "Item 7", "Item 8", "Item 9", "Item 10", "Item 11", "Item 12", "Item 13", "Item 14", "Item 15", "Item 16" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(PMemoryList);

        jPanel3.setBackground(new java.awt.Color(153, 153, 153));
        jPanel3.setForeground(new java.awt.Color(153, 153, 153));

        jLabel8.setText("Planificador");

        TextPlanificador.setEditable(false);
        TextPlanificador.setText("jTextField1");

        jLabel11.setText("Contador de programa");

        ProcList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Proceso", "H. Inicio", "H. Final"
            }
        ));
        jScrollPane3.setViewportView(ProcList);

        TextPc.setEditable(false);
        TextPc.setText("jTextField2");

        TextB.setEditable(false);
        TextB.setText("jTextField2");

        TextH.setEditable(false);
        TextH.setText("jTextField2");

        jLabel12.setText("b");

        jLabel13.setText("h");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(TextPlanificador, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addGap(138, 138, 138))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addGap(105, 105, 105))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(TextPc, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addGap(2, 2, 2)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(TextH, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(TextB, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(135, 135, 135))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 368, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TextPlanificador, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TextPc, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TextB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TextH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel1.setText("CPU");

        jLabel2.setText("Memoria Principal");

        jLabel6.setText("Hora del Sistema");

        jLabel7.setText("00:00:00 hrs");

        DireccionesList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-", "-" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane4.setViewportView(DireccionesList);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(226, 226, 226)
                        .addComponent(jLabel1)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(138, 138, 138))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(152, 152, 152))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 401, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addGap(23, 23, 23))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnInitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnInitActionPerformed
        // TODO add your handling code here:
        btnReiniciar.setEnabled(true);
        BtnInit.setEnabled(false);
        TiempoActual = Calendar.getInstance();
        memory_fill();
        rr = new RoundRobin(TiempoActual);
        rr.start();
    }//GEN-LAST:event_BtnInitActionPerformed

    private void btnReiniciarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReiniciarActionPerformed
        btnReiniciar.setEnabled(false);
        BtnInit.setEnabled(true);
        Procesos = new String[15];
        String aux[] = {" "," "," "," "," "," "," "," "," "," "," "," "," ","Activador","SO","SO"};
        Mem_array = aux.clone();
        proc = new Process[10];
        proc_enMem = new Process[10];
        cont_proc_enMem = 0;
        clearElements();
    }//GEN-LAST:event_btnReiniciarActionPerformed

    private void btnMasPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMasPActionPerformed
        // TODO add your handling code here:
        proc = Arrays.copyOf(proc, proc.length + 1);
        createProcess();
        if (proc.length > 1){
            btnMenosP.setEnabled(true);
        }
    }//GEN-LAST:event_btnMasPActionPerformed

    private void btnMenosPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMenosPActionPerformed
        // TODO add your handling code here:
        DefaultTableModel modelo = (DefaultTableModel)DescripTable.getModel();
        modelo.removeRow(DescripTable.getRowCount()-1);
        proc = Arrays.copyOf(proc, proc.length - 1);
        createProcess();
        if (proc.length <= 1){
            btnMenosP.setEnabled(false);
        }
    }//GEN-LAST:event_btnMenosPActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Procesos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Procesos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Procesos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Procesos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Procesos().setVisible(true);
            }
        });
    }
    
    public String CalenToStr(Calendar calendario){
        String horaSistema = "";
        if (calendario.get(Calendar.HOUR_OF_DAY)<10)
            horaSistema += String.valueOf("0"+calendario.get(Calendar.HOUR_OF_DAY)) + ":";
        else
            horaSistema += String.valueOf(calendario.get(Calendar.HOUR_OF_DAY)) + ":";
        if (calendario.get(Calendar.MINUTE)<10)
            horaSistema += String.valueOf("0"+calendario.get(Calendar.MINUTE)) + ":";
        else
            horaSistema += String.valueOf(calendario.get(Calendar.MINUTE)) + ":";
        if (calendario.get(Calendar.SECOND)<10)
            horaSistema += String.valueOf("0"+calendario.get(Calendar.SECOND)) + ":";
        else
            horaSistema += String.valueOf(calendario.get(Calendar.SECOND)) + ":";
        horaSistema += String.valueOf(calendario.get(Calendar.MILLISECOND)) + " hrs";
        return horaSistema;
    }
    
    public class Reloj extends Thread {
        Calendar calendario;
        @Override
        public void run() {
            while (true) {
                calendario = Calendar.getInstance();
                jLabel7.setText(CalenToStr(calendario));
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BtnInit;
    private javax.swing.JTable DescripTable;
    private javax.swing.JList<String> DireccionesList;
    private javax.swing.JList<String> PMemoryList;
    private javax.swing.JTable ProcList;
    private javax.swing.JTextField TextB;
    private javax.swing.JTextField TextH;
    private javax.swing.JTextField TextPc;
    private javax.swing.JTextField TextPlanificador;
    private javax.swing.JButton btnMasP;
    private javax.swing.JButton btnMenosP;
    private javax.swing.JButton btnReiniciar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    // End of variables declaration//GEN-END:variables
}
