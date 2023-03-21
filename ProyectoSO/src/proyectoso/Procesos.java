/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyectoso;

import java.util.Calendar;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author JAz
 */
public class Procesos extends javax.swing.JFrame{
    //Procesos de referencia en consola para verificar el funcionamiento del programa
    

    //variables generales
    private String[] Procesos = new String[15];
    private String Mem_array[] = {" "," "," "," "," "," "," "," "," "," "," "," "," "," ","SO","SO"}; // variable que se usa para representar la matriz
    public Process[] proc = new Process[10];
    private Reloj HoraActual=new Reloj();
    private RoundRobin CPU=new RoundRobin();
    private int mC=0; 
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
    }
    
    //asignacion de procesos, TL y TC
    public void createProcess(){
        char array[] = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K' };
        //Crear procesos
        for(int i = 0; i < 10; i++){
            proc[i] = new Process(array[i]+"");
            
        }
        //Modelo de Tabla
        DefaultTableModel modelDT = new DefaultTableModel();
        modelDT.addColumn("P");
        modelDT.addColumn("TL");
        modelDT.addColumn("TC");
        //Ciclo de llenado de tabla
        for(int i = 0; i < 10; i++){
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
        for(int i=0;i<10;i++){
          
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
        modelT.addRow(new Object[]{proc[0].getName(),"Ejecucion",this.jLabel7.getText(),""});
        this.ProcList.setModel(modelT);
        this.TextPlanificador.setText(proc[0].getName());
        
    }
    //llenado de lista de memoria principal
    public void memory_fill(){
        processVerify();
        boolean bandera = false;
        DefaultListModel listmodel = new DefaultListModel();
        Process[] TLOrder = this.proc;
        //Ordenamiento Burbuja para tiempos de llegada
        for (int i = 0; i < 10 - 1; i++) {
            for (int j = 0; j < 10 - i - 1; j++) {
                if (TLOrder[j].getTL() > TLOrder[j + 1].getTL()) {
                    Process temp = TLOrder[j];
                    TLOrder[j] = TLOrder[j + 1];
                    TLOrder[j + 1] = temp;
                }
            }
        }
        //Escritura en la lista de memoria
        for (int i = 0; i < 10; i++) {
           // System.out.println("Proceso " + TLOrder[i].getName());
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
                        for (int j = 0; j < TLOrder[i].getTC(); j++) {
                            Mem_array[rnd-1+j] = TLOrder[i].getName();
                        }
                        break;
                    }  
                }
                if(k == 9) spacemem = false;
               
                
            }
            if(spacemem == false){
                    //System.out.println("Memoria insuficiente para proceso "+ TLOrder[i].getName());
            }else {
                this.mC++;
                proc[i].setMem(true);
                proc[i].setEstado(0);
            }
        }
        
        //insercion de los procesos a la lista de la memoria
        for(int i = 0; i < 16; i++){
            listmodel.addElement(Mem_array[i]);
        }
        PMemoryList.setModel(listmodel);
        for(int i = 0; i < 10; i++){
            System.out.println(proc[i].getName()+" TL"+proc[i].getTL()+" TC"+proc[i].getTC()+" MEM"+proc[i].getMem());
        }
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
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(82, 82, 82)
                        .addComponent(BtnInit, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(44, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(BtnInit, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(204, 204, 204));

        PMemoryList.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
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
                "Title 1", "Title 2", "Title 3", "Title 4"
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 368, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );

        jLabel1.setText("CPU");

        jLabel2.setText("Memoria Principal");

        jLabel4.setText("0x00h");

        jLabel5.setText("0x00h");

        jLabel6.setText("Hora del Sistema");

        jLabel7.setText("00:00:00 hrs");

        jLabel9.setText("b");

        jLabel10.setText("h");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addComponent(jLabel2)
                .addGap(226, 226, 226)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel10)
                            .addComponent(jLabel9)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(136, 136, 136))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGap(353, 353, 353)
                        .addComponent(jLabel6)))
                .addGap(465, 465, 465))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(136, 136, 136)
                        .addComponent(jLabel10)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 118, Short.MAX_VALUE)
                        .addComponent(jLabel4)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addGap(17, 17, 17))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 597, javax.swing.GroupLayout.PREFERRED_SIZE))
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
        memory_fill();
        this.CPU.start();
    }//GEN-LAST:event_BtnInitActionPerformed

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
    public class RoundRobin extends Thread{
        private String ex="";
        private Process[] aux = new Process[10];
        private int c=0;
        private int auxc=0;
        private int tres=0;
        private int quantum=3;
        @Override
        public void run(){
            while(true){
               //aux=procesos que estan en memoria 
               for(int i=0;i<10;i++){
                   if(proc[i].getMem()){
                        aux[c]=proc[i];
                       c++;  
                   }
               }
               
               //modificar estados              
               aux[auxc].setEstado(3);
               for(int i=0;i<c;i++){
                    if(aux[i]!=aux[auxc]){
                        aux[i].setEstado(0);
                    }
               }
               tres=aux[auxc].getTC()-aux[auxc].getTP()-this.quantum;
               if(tres>0){
                    try {
                     Thread.sleep(3000);
                    } catch (InterruptedException ex) {
                     Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    aux[auxc].setTP(aux[auxc].getTP()+this.quantum);
               }else if(aux[auxc].getTC()>aux[auxc].getTP()){
                    int millis=(aux[auxc].getTC()-aux[auxc].getTP())*1000;
                    System.out.println( millis);
                    try {
                     Thread.sleep(millis);
                    } catch (InterruptedException ex) {
                     Logger.getLogger(Procesos.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    proc[auxc].setMem(false);
                    aux[auxc].setTP(aux[auxc].getTP()+(millis/1000));
                    c--;
                    
               }else{
                   break;
                }
               System.out.println("NAME"+ aux[auxc].getName()+" TL"+ aux[auxc].getTL()+" TC"+ aux[auxc].getTC()+
                       " MEM"+ aux[auxc].getMem()+" Tiempo procesado:"+aux[auxc].getTP());
               if(auxc<c-1){
                    auxc++;
               }else if(auxc==c-1){
                   auxc=0;
               }              
               c=0;
            }
        }
    }
    public class Reloj extends Thread {
        Calendar calendario;
        
        @Override
        public void run() {
            while (true) {
                String horaSistema = "";
                calendario = Calendar.getInstance();
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
                jLabel7.setText(horaSistema);
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
    private javax.swing.JList<String> PMemoryList;
    private javax.swing.JTable ProcList;
    private javax.swing.JTextField TextB;
    private javax.swing.JTextField TextH;
    private javax.swing.JTextField TextPc;
    private javax.swing.JTextField TextPlanificador;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    // End of variables declaration//GEN-END:variables
}
