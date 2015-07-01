/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Fractale;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 *
 * @author yohann.lelievre
 */
public class ObjetFractale {
    
    private int nombreBase;
    private double dimensionFractale;
    public ByteBuffer fastaBuff;
    
     /**
     * Constructor of ObjetFractale 
     * 
     * @param fichierADN the name of the FastA file which contains the DNA list
     * 
     */
    public ObjetFractale(String fichierADN) {
        // ouverture du fichier contenant l'ADN
        try ( FileInputStream fis = new FileInputStream(new File(fichierADN)); FileChannel fc = fis.getChannel() ){
            int size = (int)fc.size();                      // taille du fichier
            int entete=1;                                   // pointeur de l'entête
            ByteBuffer bBuff = ByteBuffer.allocate(size);          // buffeur de lecture du fichier FastA
            fc.read(bBuff);
            bBuff.rewind();
            
            // On passe la première ligne descriptive du fichier FastA
            while(bBuff.get() != '\n'){
                entete++;
            }
            nombreBase = size-entete;
            fastaBuff = ByteBuffer.allocate(nombreBase);
            fastaBuff = bBuff.slice();
            
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
     /**
     * Setter of dimension 
     * 
     * @param dimension the value corresponding to the apparent fractal dimension
     * 
     */
    public void setDimensionFractale(double dimension){
        this.dimensionFractale = dimension;
    }
    
     /**
     * Getter of nombreBase 
     * 
     * @return the number of DNA base read in the FastA file
     * 
     */
    public int getNombreBase(){
        return this.nombreBase;
    }
    
     /**
     * Getter of dimensionFractale 
     * 
     * @return the value corresponding to the apparent fractal dimension
     * 
     */
    public double getDimensionFractale(){
        return this.dimensionFractale;
    }
    
}
