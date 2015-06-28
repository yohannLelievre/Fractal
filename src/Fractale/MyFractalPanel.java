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
        g.setColor(java.awt.Color.WHITE);
        g.fillRect(0, 0, imageFractale.getWidth(), imageFractale.getHeight());
        g.dispose();
    }


     /**
     * methode of MyFractalPanel
     * find the maximal integer in a 2D matrix
     * 
     * @param laMatrice the matrix in which we are looking for the max value
     * 
     * @return the maximal integer in a 2D matrix
    */
    private int maxMatrice(int laMatrice[][]) {
        int valeurMax = 0;
        
        for(int i=0; i<tailleFractale; i++)
            for(int j=0; j<tailleFractale; j++)
                if(laMatrice[i][j]>valeurMax) valeurMax = laMatrice[i][j];
        
        return valeurMax;
        
    }

    
     /**
     * methode of MyFractalPanel
     * find the minimal integer in a 2D matrix
     * 
     * @param laMatrice the matrix in which we are looking for the min value
     * 
     * @return the minimal integer in a 2D matrix
    */
    private int minMatrice(int laMatrice[][]) {
        int valeurMin = laMatrice[0][0];
        
        for(int i=0; i<tailleFractale; i++)
            for(int j=0; j<tailleFractale; j++)
                if(laMatrice[i][j]<valeurMin) valeurMin = laMatrice[i][j];
        
        return valeurMin;
        
    }

    
     /**
     * methode of MyFractalPanel
     * Substracts the matrix matricePrecedente to the matrix in parameter.
     * Returns the result in matricePrecedente.
     * Reinitializes matricePrecedente with the values of laMatrice.
     * 
     * @param laMatrice the matrix which is substrated by matricePrecedente
     * 
     * @return the substracted matrix
     * 
    */
    private int[][] soustractionMatricePrecedente(int laMatrice[][]) {
        int matriceSoustraite[][] = new int[tailleFractale][tailleFractale];
        
        for(int i=0; i<tailleFractale; i++)
            for(int j=0; j<tailleFractale; j++){
                matriceSoustraite[i][j] = laMatrice[i][j] - matricePrecedente[i][j];
                matricePrecedente[i][j] = laMatrice[i][j];
            }
        
        return matriceSoustraite;
        
    }

    
     /**
     * methode of MyFractalPanel
     * constructs the fractal image with the DNA file.
     * This construction is based on the number of time where a coordinate is targeted.
     * Each time a counter is incremented and finally a corresponding shade of grey is drawn
     * 
     * @param fichierADN the DNA file which is read
     * @param startBase the start position in the DNA file
     * @param stopBase the stop position in the DNA file
     * 
     * @return the number of DNA bases included in the FastA file
     * 
     */
    private int calculFractaleNB(String fichierADN, int startBase, int stopBase) {
        // initialisation des coordonnées fractales
        int x = tailleFractale/2;
        int y = tailleFractale/2;
        
        // initialisation des couleurs RGB
        int r = 0;
        int g = 0;
        int b = 0;
        int col = (r << 16) | (g << 8) | b;

        
        // ouverture du fichier contenant l'ADN
        try ( FileInputStream fis = new FileInputStream(new File(fichierADN)); FileChannel fc = fis.getChannel() ){
            int size = (int)fc.size();                      // taille du fichier
            int entete=1;                                   // pointeur de l'entête
            ByteBuffer bBuff = ByteBuffer.allocate(size);   // buffeur de lecture du fichier FastA
            fc.read(bBuff);
            bBuff.rewind();
            char base=0;                                    // caractère lu dans le buffeur
            int nombreBase = 1;                             // pointeur des bases ADN lues dans le buffeur
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
                
                // On ajoute un point blanc au niveau de la coordonnée sélectionnée
                imageFractale.setRGB(x, y, col);

                base = (char)bBuff.get();
                nombreBase++;
            }

                
            //Debug
            //System.out.println("Le nombre de base : " + (size-entete)+" et la taille de l'entete : "+entete);
            return (size-entete);
            
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
        
    }
    
    
     /**
     * methode of MyFractalPanel
     * constructs the fractal image with the DNA file.
     * This construction is based on the number of time where a coordinate is targeted.
     * Each time a counter is incremented and finally a logarithmic corresponding shade of grey is drawn
     * 
     * @param fichierADN the DNA file which is read
     * @param startBase the start position in the DNA file
     * @param stopBase the stop position in the DNA file
     * 
     * @return the number of DNA bases included in the FastA file
     * 
     */
    private int calculFractaleGris(String fichierADN, int startBase, int stopBase) {
        // initialisation des coordonnées fractales et de la matrice de comptage
        int matrice[][] = new int[tailleFractale][tailleFractale];  // initialise la matrice à zéro
        double occurenceMax;                                           // correspond à la valeur max dans la matrice
        int x = tailleFractale/2;
        int y = tailleFractale/2;
        
        // initialisation des couleurs RGB
        int r;
        int g;
        int b;
        int col;
        
        // ouverture du fichier contenant l'ADN
        try ( FileInputStream fis = new FileInputStream(new File(fichierADN)); FileChannel fc = fis.getChannel() ){
            int size = (int)fc.size();                      // taille du fichier
            int entete=1;                                   // pointeur de l'entête
            ByteBuffer bBuff = ByteBuffer.allocate(size);   // buffeur de lecture du fichier FastA
            fc.read(bBuff);
            bBuff.rewind();
            char base=0;                                    // caractère lu dans le buffeur
            int nombreBase = 1;                             // pointeur des bases ADN lues dans le buffeur
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
                
                // On incrémente le compteur au niveau de la coordonnée sélectionnée
                matrice[x][y]++;

                base = (char)bBuff.get();
                nombreBase++;
            }

            // On identifie la valeur max dans la matrice
            occurenceMax = Math.log( (double)maxMatrice(matrice) );
            
            // Un point gris est ajouté à l'image fractale pour chaque élément de la matrice en fonction rapport au max
            if(occurenceMax != 0)
                for(int i=0; i<tailleFractale; i++)
                    for(int j=0; j<tailleFractale; j++){
                        r = 255 - (int)(255 * ( Math.log( (double)matrice[i][j]) / occurenceMax ) );
                        g = r;
                        b = r;
                        col = (r << 16) | (g << 8) | b;
                        imageFractale.setRGB(i, j, col);
                    }
                
            //Debug
            //System.out.println("Le nombre de base : " + (size-entete)+" et la taille de l'entete : "+entete);
            return (size-entete);
            
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
        
    }

    
     /**
     * methode of MyFractalPanel
     * constructs the fractal image with the DNA file.
     * This construction is based on the number of time where a coordinate is targeted.
     * Each time a counter is incremented. Then the difference is done with the previous calculated matrix.
     * And finally a corresponding shade of red or blue is drawn.
     * The more red it is, the more the frequence of a DNA word increases
     * The more blue it is, the more the frequence of a DNA word decreases
     * 
     * @param fichierADN the DNA file which is read
     * @param startBase the start position in the DNA file
     * @param stopBase the stop position in the DNA file
     * 
     * @return the number of DNA bases included in the FastA file
     * 
     */
    private int calculFractaleCouleur(String fichierADN, int startBase, int stopBase) {
        // initialisation des coordonnées fractales et de la matrice de comptage
        int matrice[][] = new int[tailleFractale][tailleFractale];  // initialise la matrice à zéro
        int differenceMax;                                          // correspond à la valeur max dans la matrice soustraite
        int differenceMin;                                          // correspond à la valeur min dans la matrice soustraite
        int x = tailleFractale/2;
        int y = tailleFractale/2;
        
        // initialisation des couleurs RGB
        int r;
        int g;
        int b;
        int col;
        
        // ouverture du fichier contenant l'ADN
        try ( FileInputStream fis = new FileInputStream(new File(fichierADN)); FileChannel fc = fis.getChannel() ){
            int size = (int)fc.size();                      // taille du fichier
            int entete=1;                                   // pointeur de l'entête
            ByteBuffer bBuff = ByteBuffer.allocate(size);   // buffeur de lecture du fichier FastA
            fc.read(bBuff);
            bBuff.rewind();
            char base=0;                                    // caractère lu dans le buffeur
            int nombreBase = 1;                             // pointeur des bases ADN lues dans le buffeur
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
                
                // On incrémente le compteur au niveau de la coordonnée sélectionnée
                matrice[x][y]++;

                base = (char)bBuff.get();
                nombreBase++;
            }

            // On soustrait la matrice précédente à la matrice nouvellement calculée
            matrice = soustractionMatricePrecedente(matrice);
            
            // On identifie la valeur max et min dans la matrice soustraite
            differenceMax = maxMatrice(matrice);
            differenceMin = minMatrice(matrice);
            
            // Un point gris est ajouté à l'image fractale pour chaque élément de la matrice en fonction du rapport au max pour les valeurs positives et au min pour les valeurs négatives
            if((differenceMax != 0) && (differenceMin != 0))
                for(int i=0; i<tailleFractale; i++)
                    for(int j=0; j<tailleFractale; j++){
                        if(matrice[i][j]>=0){
                            r = (255 * matrice[i][j]) / differenceMax;
                            g = 0;
                            b = 0;
                        } else {
                            r = 0;
                            g = 0;
                            b = (255 * matrice[i][j]) / differenceMin;
                        }
                        col = (r << 16) | (g << 8) | b;
                        imageFractale.setRGB(i, j, col);
                    }
            else if((differenceMax == 0) && (differenceMin == 0))
                return (size-entete);
            else if(differenceMax == 0)
                for(int i=0; i<tailleFractale; i++)
                    for(int j=0; j<tailleFractale; j++){
                        r = 0;
                        g = 0;
                        b = (255 * matrice[i][j]) / differenceMin;
                        col = (r << 16) | (g << 8) | b;
                        imageFractale.setRGB(i, j, col);
                    }
            else if(differenceMin == 0)
                for(int i=0; i<tailleFractale; i++)
                    for(int j=0; j<tailleFractale; j++){
                        r = (255 * matrice[i][j]) / differenceMax;
                        g = 0;
                        b = 0;
                        col = (r << 16) | (g << 8) | b;
                        imageFractale.setRGB(i, j, col);
                    }
                
            //Debug
            System.out.println("Le max : " + differenceMax +" et le min : "+differenceMin);
            return (size-entete);
            
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
        
    }

    
     /**
     * methode of MyFractalPanel
     * constructs the fractal image with the DNA file depending of the type of representation desired
     * 
     * @param fichierADN the DNA file which is read
     * @param startBase the start position in the DNA file
     * @param stopBase the stop position in the DNA file
     * @param typeCalcul the kind of fractal representation. type=1 : Black and White ; type=2 : shade of Grey
     * 
     * @return the number of DNA bases included in the FastA file
     * 
     */
    public int calculFractale(String fichierADN, int startBase, int stopBase, int typeCalcul) {
        switch(typeCalcul){
            case 1:
                return calculFractaleNB( fichierADN, startBase, stopBase);
            case 2:
                return calculFractaleGris( fichierADN, startBase, stopBase);
            case 3:
                return calculFractaleCouleur( fichierADN, startBase, stopBase);
            default:
                return 0;
        }
    }

    
    private final static int tailleFractale = 512;                                  // dimension de la représentation fractale
    private int matricePrecedente[][] = new int[tailleFractale][tailleFractale];    // contient la matrice précédemment calculé (initialisé à zéro=

    private java.awt.image.BufferedImage imageFractale;
    
}

