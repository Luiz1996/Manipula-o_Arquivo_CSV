/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import view.TelaView;

/**
 *
 * @author luiz.pereira
 */
public class TelaController {
    TelaView tV;
    ActionListener actionListener;
    JFileChooser jfc;
    
    public TelaController(TelaView tV){
        this.tV = tV; 
        importarArquivo();
    }
    
    private void importarArquivo() {
        actionListener = (ActionEvent ae) -> {
            jfc = new JFileChooser();
            jfc.setFileFilter(new FileNameExtensionFilter("CSV (*.csv)", "csv"));
            jfc.showDialog(jfc, "Importar");
            try{
                tV.getTxtArquivoSelecionado().setText(jfc.getSelectedFile().getPath());
                if (JOptionPane.showConfirmDialog(null, "Tem certeza que deseja alterar o arquivo selecionado?", "Confirmação de alteração", JOptionPane.YES_NO_OPTION) == 0) {
                    editarArquivo(jfc.getSelectedFile().getPath());
                }   
            }catch (Exception e){
                tV.getTxtArquivoSelecionado().setText("");
            }
            System.gc();
        };
        tV.getBtnImportar().addActionListener(actionListener); 
    }

    private void editarArquivo(String nomeArq) throws FileNotFoundException, IOException {
        try{
            StringBuilder linhaB = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader(nomeArq));
            
            String linha = br.readLine();
            
            String[] newName = nomeArq.split(".csv");
            String newArq = newName[0].concat("_New.csv");
            
            try (FileWriter fwTemp = new FileWriter(newArq)) {
                try (BufferedWriter bw = new BufferedWriter(fwTemp)) {
                    String resultado = null;
                    for(int i = 0; linha != null; i++){
                        //obtendo dados da linha e removendo coordenadas geográficas
                        if((i % 2) == 0){       
                            linhaB.append(linha);
                            int count = 0;
                            
                            for(int j = 0; j < linha.length() && count < 13; j++){
                                if(linha.charAt(j) == ';'){
                                    count++;
                                }
                                
                                if((count == 11 || count == 12) && linha.charAt(j) != ';'){
                                    linhaB.setCharAt(j, ' ');
                                }
                            }
                            resultado = linhaB.toString().concat("\n");
                        }else{
                            resultado = linha.concat("\n");
                        }
                        
                        //escrevendo arquivo temp
                        bw.write(resultado.trim());
                        bw.newLine();
                        linhaB = new StringBuilder();
                        linha = br.readLine();
                    }
                    br.close();
                    bw.close();  
                } 
                fwTemp.close();
                
                //reescrevendo em cima do arquivo criado temporariamente
                br = new BufferedReader(new FileReader(newArq));
                try (FileWriter fwOriginal = new FileWriter(nomeArq); 
                    BufferedWriter bw = new BufferedWriter(fwOriginal)) {
                    
                    //escrevendo arquivo
                    linha = br.readLine();
                    while(linha != null){
                        bw.write(linha);
                        bw.newLine();
                        linha = br.readLine();
                    }
                    
                    //fechando apontamentos
                    br.close();
                    bw.close();
                    fwOriginal.close();
                }   
            }
            
            //deletando arquivo temporário
            File file = new File(newArq); 
            file.delete();
        }catch(FileNotFoundException e){
            JOptionPane.showMessageDialog(null, "Ocorreu uma falha durante a edição do arquivo!\nErro: ".concat(e.getMessage().trim()), "Falha ao editar arquivo", JOptionPane.ERROR_MESSAGE);
            System.gc();
            return;
        }
        System.gc();   
        JOptionPane.showMessageDialog(null, "Sucesso na edição do arquivo.", "Sucesso ao editar arquivo", JOptionPane.INFORMATION_MESSAGE);
    }
}
