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
public class MyFractalPanel extends javax.swing.JPanel {
    
    
     /**
     * Constructor of MyFractalPanel 
     * 
     */
    public MyFractalPanel() {
        // initialisation de l'image
        imageFractale = new java.awt.image.BufferedImage(tailleFractale,tailleFractale,java.awt.image.BufferedImage.TYPE_INT_RGB);
        //java.awt.Graphics2D g = imageFractale.createGraphics();
    }


     /**
     * methode of MyFractalPanel
     * draw the fractal image obtain with the DNA file in the frame of a JPanel object
     * 
     * @param g the graphics that will be drawn
     * 
     */
    @Override
    public void paintComponent(java.awt.Graphics g) {
        //java.awt.Graphics scratchGraphics = (g == null) ? null : g.create();
        //g.drawImage(imageFractale, this.getX(), this.getY(), this.getWidth(), this.getHeight(), this);
        g.drawImage(imageFractale, 0, 0, this.getWidth(), this.getHeight(), this);
        //g.dispose();
        
    }


     /**
     * methode of MyFractalPanel
     * clear the BufferedImage
     * 
     */
    public void reInit() {
        java.awt.Graphics g = imageFractale.createGraphics();
        g.setColor(java.awt.Color.BLACK);
        g.fillRect(0, 0, imageFractale.getWidth(), imageFractale.getHeight());
        g.dispose();
    }


     /**
     * methode of MyFractalPanel
     * constructs the fractal image with the DNA file
     * 
     * @param fichierADN the DNA file which is read
     * @param startBase the start position in the DNA file
     * @param stopBase the stop position in the DNA file
     * 
     * @return the number of DNA bases included in the FastA file
     * 
     */
    public int calculFractale(String fichierADN, int startBase, int stopBase) {
        // initialisation des coordonnées fractales
        int x = tailleFractale/2;
        int y = tailleFractale/2;
        
        // initialisation des couleurs RGB
        int r = 255;
        int g = 255;
        int b = 255;
        int col;
        
        // ouverture du fichier contenant l'ADN
        try ( FileInputStream fis = new FileInputStream(new File(fichierADN)); FileChannel fc = fis.getChannel() ){
            int size = (int)fc.size();
            int entete=1;
            ByteBuffer bBuff = ByteBuffer.allocate(size);
            fc.read(bBuff);
            bBuff.rewind();
            char base=0;
            int nombreBase = 1;
            this.reInit();
            
            // On passe la première ligne descriptive du fichier FastA
            while(bBuff.get() != '\n'){
                entete++;
            }
            // On passe les 'startBase-1' premières bases du fichier FastA
            while(nombreBase < startBase){
                base = (char)bBuff.get();
                nombreBase++;
            }
            // On lit chaque base d'ADN du fichier jusqu'à la 'stopBase' ième base
            while((base != -1) && (nombreBase <= stopBase)){
                
                // Jeu du chaos
                switch(base){
                    case 'C':
                        x /= 2;
                        y /= 2;
                        break;
                    case 'G':
                        x = (x + tailleFractale)/2;
                        y /= 2;
                        break;
                    case 'A':
                        x /= 2;
                        y = (y + tailleFractale)/2;
                        break;
                    case 'T':
                        x = (x + tailleFractale)/2;
                        y = (y + tailleFractale)/2;
                        break;
                    default:
                }
                
                // Un point est ajouté à l'image fractale
                col = (r << 16) | (g << 8) | b;
                imageFractale.setRGB(x, y, col);

                base = (char)bBuff.get();
                nombreBase++;
            }
            
            System.out.println("Le nombre de base : " + (size-entete)+" et la taille de l'entete : "+entete);
            return (size-entete);
            
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
        
    }

    private final static int tailleFractale = 512;
    
    private java.awt.image.BufferedImage imageFractale;
    
}

