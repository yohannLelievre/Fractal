/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Fractale;


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
        g.drawImage(imageFractale, 0, 0, this.getWidth(), this.getHeight(), this);
        
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
     * Calculates the value of the Minkowski–Bouligand dimension
     * 
     * @param Nepsi a parameter requiered to calculate the Minkowski–Bouligand dimension
     * 
     * @return the value of the Minkowski–Bouligand dimension
     * 
    */
    private double calculDimension(int Nepsi) {
        double dimensionF;
        
        dimensionF = java.lang.Math.log10( (double)Nepsi ) / java.lang.Math.log10( (double)tailleFractale );
        
        return dimensionF;
        
    }

    
     /**
     * methode of MyFractalPanel
     * constructs the fractal image with the DNA file.
     * This construction is based on the number of time where a new coordinate is targeted.
     * Each time a new coordinate is targeted a black point is drawn
     * 
     * @param fractale the fractal object which contains the DNA buffer as well as the size of the DNA file and the last calculated Minkowski–Bouligand dimension
     * @param startBase the start position in the DNA file
     * @param stopBase the stop position in the DNA file
     * 
     * @return the number of DNA bases included in the FastA file
     * 
     */
    private void calculFractaleNB(ObjetFractale fractale, int startBase, int stopBase) {
        // initialisation des coordonnées fractales et de la matrice de comptage
        int matrice[][] = new int[tailleFractale][tailleFractale];  // initialise la matrice à zéro
        int Nepsilon=0;                                             // variable de calcul de la dimension fractal
        int x = tailleFractale/2;
        int y = tailleFractale/2;
        
        // initialisation des couleurs RGB
        int r = 0;
        int g = 0;
        int b = 0;
        int col = (r << 16) | (g << 8) | b;

        
        fractale.getFastaBuff().rewind();
        char base=0;                                    // caractère lu dans le buffeur
        int nombreBase = 1;                             // pointeur des bases ADN lues dans le buffeur
        this.reInit();
            
        // On passe les 'startBase-1' premières bases du fichier FastA
        while(nombreBase < startBase){
            base = (char)fractale.getFastaBuff().get();
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
            // pour chaque nouveau point on incrémente Nepsilon de tel sorte que Nepsilon représente la surface du motif fractal
            if(matrice[x][y]==1) Nepsilon++;
            
            // On ajoute un point noir au niveau de la coordonnée sélectionnée
            imageFractale.setRGB(x, y, col);

            base = (char)fractale.getFastaBuff().get();
            nombreBase++;
        }

        // calcul de la dimension fractale
        fractale.setDimensionFractale( calculDimension(Nepsilon) );

    }
    
    
     /**
     * methode of MyFractalPanel
     * constructs the fractal image with the DNA file.
     * This construction is based on the number of time where a coordinate is targeted.
     * Each time a counter is incremented and finally a logarithmic corresponding shade of grey is drawn
     * 
     * @param fractale the fractal object which contains the DNA buffer as well as the size of the DNA file and the last calculated Minkowski–Bouligand dimension
     * @param startBase the start position in the DNA file
     * @param stopBase the stop position in the DNA file
     * 
     * 
     */
    private void calculFractaleGris(ObjetFractale fractale, int startBase, int stopBase) {
        // initialisation des coordonnées fractales et de la matrice de comptage
        int matrice[][] = new int[tailleFractale][tailleFractale];  // initialise la matrice à zéro
        double occurenceMax;                                        // correspond à la valeur max dans la matrice
        int Nepsilon=0;                                         // variable de calcul de la dimension fractal
        int x = tailleFractale/2;
        int y = tailleFractale/2;
        
        // initialisation des couleurs RGB
        int r;
        int g;
        int b;
        int col;
        
        fractale.getFastaBuff().rewind();
        char base=0;                                    // caractère lu dans le buffeur
        int nombreBase = 1;                             // pointeur des bases ADN lues dans le buffeur
        this.reInit();

        // On passe les 'startBase-1' premières bases du fichier FastA
        while(nombreBase < startBase){
            base = (char)fractale.getFastaBuff().get();
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
            // pour chaque nouveau point on incrémente Nepsilon de tel sorte que Nepsilon représente la surface du motif fractal
            if(matrice[x][y]==1) Nepsilon++;

            base = (char)fractale.getFastaBuff().get();
            nombreBase++;
        }

        // calcul de la dimension fractale
        fractale.setDimensionFractale( calculDimension(Nepsilon) );
        
        // On identifie la valeur max dans la matrice et on en calcule le logarithme néperien
        occurenceMax = Math.log( (double)maxMatrice(matrice) );

        // Un point gris est ajouté à l'image fractale pour chaque élément de la matrice en fonction du rapport au max
        if(occurenceMax != 0)
            for(int i=0; i<tailleFractale; i++)
                for(int j=0; j<tailleFractale; j++){
                    r = 255 - (int)(255 * ( Math.log( (double)matrice[i][j]) / occurenceMax ) );
                    g = r;
                    b = r;
                    col = (r << 16) | (g << 8) | b;
                    imageFractale.setRGB(i, j, col);
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
     * @param fractale the fractal object which contains the DNA buffer as well as the size of the DNA file and the last calculated Minkowski–Bouligand dimension
     * @param startBase the start position in the DNA file
     * @param stopBase the stop position in the DNA file
     * 
     * @return the number of DNA bases included in the FastA file
     * 
     */
    private void calculFractaleCouleur(ObjetFractale fractale, int startBase, int stopBase) {
        // initialisation des coordonnées fractales et de la matrice de comptage
        int matrice[][] = new int[tailleFractale][tailleFractale];  // initialise la matrice à zéro
        int Nepsilon=0;                                             // variable de calcul de la dimension fractal
        int differenceMax;                                          // correspond à la valeur max dans la matrice soustraite
        int differenceMin;                                          // correspond à la valeur min dans la matrice soustraite
        int x = tailleFractale/2;
        int y = tailleFractale/2;
        
        // initialisation des couleurs RGB
        int r;
        int g;
        int b;
        int col;
        
        fractale.getFastaBuff().rewind();
        char base=0;                                    // caractère lu dans le buffeur
        int nombreBase = 1;                             // pointeur des bases ADN lues dans le buffeur
        this.reInit();

        // On passe les 'startBase-1' premières bases du fichier FastA
        while(nombreBase < startBase){
            base = (char)fractale.getFastaBuff().get();
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
            // pour chaque nouveau point on incrémente Nepsilon de tel sorte que Nepsilon représente la surface du motif fractal
            if(matrice[x][y]==1) Nepsilon++;

            base = (char)fractale.getFastaBuff().get();
            nombreBase++;
        }

        // calcul de la dimension fractale
        fractale.setDimensionFractale( calculDimension(Nepsilon) );
        
        // On soustrait la matrice précédente à la matrice nouvellement calculée
        matrice = soustractionMatricePrecedente(matrice);

        // On identifie la valeur max et min dans la matrice soustraite
        differenceMax = maxMatrice(matrice);
        differenceMin = minMatrice(matrice);

        // Un point de couleur est ajouté à l'image fractale pour chaque élément de la matrice en fonction du rapport au max pour les valeurs positives et au min pour les valeurs négatives
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
        else if((differenceMax == 0) && (differenceMin != 0))
            for(int i=0; i<tailleFractale; i++)
                for(int j=0; j<tailleFractale; j++){
                    r = 0;
                    g = 0;
                    b = (255 * matrice[i][j]) / differenceMin;
                    col = (r << 16) | (g << 8) | b;
                    imageFractale.setRGB(i, j, col);
                }
        else if((differenceMin == 0) && (differenceMax != 0))
            for(int i=0; i<tailleFractale; i++)
                for(int j=0; j<tailleFractale; j++){
                    r = (255 * matrice[i][j]) / differenceMax;
                    g = 0;
                    b = 0;
                    col = (r << 16) | (g << 8) | b;
                    imageFractale.setRGB(i, j, col);
                }

    }

    
     /**
     * methode of MyFractalPanel
     * constructs the fractal image with the DNA file depending of the type of representation desired
     * 
     * @param fractale the fractal object which contains the DNA buffer as well as the size of the DNA file and the last calculated Minkowski–Bouligand dimension
     * @param startBase the start position in the DNA file
     * @param stopBase the stop position in the DNA file
     * @param typeCalcul the kind of fractal representation. type=1 : Black and White ; type=2 : shade of Grey
     * 
     */
    public void calculFractale(ObjetFractale fractale, int startBase, int stopBase, int typeCalcul) {
        switch(typeCalcul){
            case 1:
                calculFractaleNB( fractale, startBase, stopBase);
                break;
            case 2:
                calculFractaleGris( fractale, startBase, stopBase);
                break;
            case 3:
                calculFractaleCouleur( fractale, startBase, stopBase);
                break;
            default:
                break;
        }
    }

    
    private final static int tailleFractale = 512;                                  // dimension de la représentation fractale
    private int matricePrecedente[][] = new int[tailleFractale][tailleFractale];    // contient la matrice précédemment calculé (initialisé à zéro=

    private java.awt.image.BufferedImage imageFractale;
    
}

