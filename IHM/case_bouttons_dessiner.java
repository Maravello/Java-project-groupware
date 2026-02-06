//Il s'agit de modifier votre programme, solution de la question 3.
//En effet les bouttons serviront a representer des ordres d'un dessin 2D pre existant,
//de saisie de texte et de dessin de primitives 2D parametrables.
//Vous pouvez constater le resultat attendu en executant le fichier "
//case_bouttons_dessiner.class ".

//N.B. : Etudier notamment les classes :
//JMenuBar,JMenu,JMenuItem,JCheckBoxMenuItem,JPopupMenu,couleurSurg,JToolBar
 //et ActionCouleur.

import javax.swing.* ;
import java.awt.* ;
import java.awt.event.* ;
class FenBoutonsDyn extends JFrame implements ActionListener
{ final int NBOUTONS=3 ;
  public FenBoutonsDyn ()
  { setTitle ("Hierarchie de menus") ;
    setSize (350, 100) ;
    Container contenu = getContentPane() ;
    contenu.setLayout (new FlowLayout()) ;

  // Creation des boutons
    tabBout = new JButton[NBOUTONS] ;
    tabBout[0] = new JButton ("DESSIN") ;
    tabBout[1] = new JButton ("DESSINER") ;
    tabBout[2] = new JButton ("ECRIRE") ;


    for (int i=0 ; i<NBOUTONS ; i++)
    {
      contenu.add(tabBout[i]) ;
      tabBout[i].addActionListener (this);
     }

  // Creation de la case a cocher
    coche = new JCheckBox ("case") ;
    contenu.add(coche) ;
    coche.addActionListener (this) ;


  }
  public void actionPerformed (ActionEvent ev)
  {
    Object source0 = ev.getSource();
    if (source0 == coche)
      {System.out.println ("action case avec etat = "+ coche.isSelected()) ;
      if(coche.isSelected())
      for (int i=0 ; i<NBOUTONS ; i++)
          tabBout[i].setEnabled(true);
      }
    else
    {
    System.out.print ("ETAT BOUTONS = ") ;
    for (int i=0 ; i<NBOUTONS ; i++)
      System.out.print (tabBout[i].isEnabled() + " ") ;
    System.out.println() ;
    JButton source = (JButton) ev.getSource() ;



    if (source == tabBout[0]){System.out.println ("Afficher dessin");
     Fenetre f = new Fenetre() ;
     f.setVisible(true) ;}
    if (source == tabBout[1]){System.out.println ("Dessiner");
    FenMenu fen = new FenMenu() ;
    fen.setVisible(true) ;}
    if (source == tabBout[2]){System.out.println ("Rediger Texte");
    FenText fen = new FenText() ;
     fen.setVisible(true) ;}

      System.out.println("on desactive le bouton : "
                         + source.getActionCommand());
      source.setEnabled(false);

    }
}
  private JButton tabBout[] ;
  private JCheckBox coche;
}
//    Dessin 2D pre existant
class Fenetre extends JFrame
{ Fenetre ()
  { setTitle ("Dessins ...");
    setBounds(10,100,250, 150) ;
    p = new Panneau() ;
    getContentPane().add(p) ;
  }
  private JPanel p ;
}
class Panneau extends JPanel
{ public void paintComponent(Graphics g)
  { super.paintComponent(g) ;
    g.drawLine  (40, 80, 80, 20) ;  g.drawRect (30,  90,  70, 10) ;
    g.setColor(Color.GREEN);
    g.fillOval (100,  20, 50, 30) ; g.fillOval (10,  20, 50, 30) ;
  }
}

//Dessiner
class FenMenu extends JFrame implements ActionListener
{ static public final String[] nomCouleurs =
                         {"jaune",    "vert",      "cyan"} ;
  static public final Color[] couleurs =
                         {Color.yellow,   Color.green, Color.cyan} ;
  static public final String[] nomIcones  =
                         {"jaune.gif", "vert.gif",  "cyan.gif"} ;
  public FenMenu ()
  { setTitle ("Dessin de primitives 2D ") ;
    setBounds(10,250,500, 300) ;
    Container contenu = getContentPane() ;
       /* creation paneau pour les dessins */
    pan = new Paneau() ;
    contenu.add(pan) ;
    pan.setBackground(Color.cyan) ;

    int nbCouleurs = nomCouleurs.length ;
       /* creation des actions */
    actions = new ActionCouleur [nbCouleurs] ;
    for (int i=0 ; i<nbCouleurs ; i++)
    { actions[i] = new ActionCouleur (nomCouleurs[i], couleurs[i],
                                      nomIcones[i], pan) ;
    }
       /* creation barre des menus */
    barreMenus = new JMenuBar() ;
    setJMenuBar(barreMenus) ;
       /* creation menu Couleur et ses options */
    couleur = new JMenu ("Fond") ; couleur.setMnemonic('C') ;
    barreMenus.add(couleur) ;
    for (int i=0 ; i<nomCouleurs.length ; i++)
      couleur.add(actions[i]) ;
       /* creation menu surgissant Couleur et ses options */
    couleurSurg = new JPopupMenu () ;
    for (int i=0 ; i<nomCouleurs.length ; i++)
      couleurSurg.add(actions[i]) ;
       /* creation menu formes et ses options rectangle et ovale */
    formes = new JMenu ("Primitive") ; formes.setMnemonic('F') ;
    barreMenus.add(formes) ;
    rectangle = new JCheckBoxMenuItem ("Rectangle") ;
    formes.add(rectangle) ;
    rectangle.addActionListener (this) ;
    ovale = new JCheckBoxMenuItem ("Ovale") ;
    formes.add(ovale) ;
    ovale.addActionListener (this) ;
       /* affichage menu surgissant sur clic dans fenetre */
    addMouseListener (new MouseAdapter ()
        { public void mouseReleased (MouseEvent e)
          { if (e.isPopupTrigger())
              couleurSurg.show (e.getComponent(), e.getX(), e.getY()) ;
          }
        }) ;
       /* creation menu Dimensions et ses options Hauteur et Largeur */
    dimensions = new JMenu ("Parametres") ; dimensions.setMnemonic('D') ;
    barreMenus.add(dimensions) ;
    largeur = new JMenuItem ("Largeur") ;
    dimensions.add(largeur) ;
    largeur.addActionListener (this) ;
    hauteur = new JMenuItem ("Hauteur") ;
    dimensions.add(hauteur) ;
    hauteur.addActionListener (this) ;
      /*             creation barre d'outils couleurs                */
      /* (avec suppression textes associes et ajout de bulles d'aide */
    barreCouleurs = new JToolBar () ;
    for (int i=0 ; i<nomCouleurs.length ; i++)
    { JButton boutonCourant = barreCouleurs.add(actions[i]) ;
      boutonCourant.setText(null) ;
      boutonCourant.setToolTipText
         ((String)actions[i].getValue(Action.SHORT_DESCRIPTION)) ;
    }
    contenu.add(barreCouleurs, "North") ;
  }
  public void actionPerformed (ActionEvent e)
  { Object source = e.getSource() ;
    if (source == largeur)
    { String ch = JOptionPane.showInputDialog (this, "Largeur") ;
      pan.setLargeur (Integer.parseInt(ch)) ;
    }
    if (source == hauteur)
    { String ch = JOptionPane.showInputDialog (this, "Hauteur") ;
      pan.setHauteur (Integer.parseInt(ch)) ;
    }
    if (source == ovale)     pan.setOvale(ovale.isSelected()) ;
    if (source == rectangle) pan.setRectangle(rectangle.isSelected()) ;
    pan.repaint() ;
  }
  private JMenuBar barreMenus ;
  private JMenu couleur, dimensions, formes ;
  private JMenuItem [] itemCouleurs ;
  private JMenuItem largeur, hauteur ;
  private JCheckBoxMenuItem rectangle, ovale ;
  private JPopupMenu couleurSurg ;
  private ActionCouleur [] actions ;
  private JToolBar barreCouleurs ;
  private Paneau pan ;
}
class Paneau extends JPanel
{ public void paintComponent(Graphics g)
  { super.paintComponent(g) ;
    if (ovale)    g.drawOval (200, 10, 10+largeur, 10+hauteur) ;
    if (rectangle) g.drawRect (10, 10, 10+largeur, 10+hauteur) ;
  }
  public void setRectangle(boolean trace) {rectangle = trace ; }
  public void setOvale(boolean trace)     {ovale = trace ;  }
  public void setLargeur (int l) { largeur = l ; }
  public void setHauteur (int h) { hauteur = h ; }
  public void setCouleur (Color c) { setBackground (c) ; }
  private boolean rectangle = false, ovale = false ;
  private int largeur=50, hauteur=50 ;
}
class ActionCouleur extends AbstractAction
{ public ActionCouleur (String nom, Color couleur, String nomIcone, Paneau pan)
  { putValue (Action.NAME, nom) ;
    putValue (Action.SMALL_ICON, new ImageIcon(nomIcone) ) ;
    putValue (Action.SHORT_DESCRIPTION, "Fond "+nom) ;
    this.couleur = couleur ;
    this.pan = pan ;
  }
  public void actionPerformed (ActionEvent e)
  { pan.setCouleur (couleur) ;
    pan.repaint() ;
    setEnabled(false) ;
    if (actionInactive != null) actionInactive.setEnabled(true) ;
    actionInactive = this ;
  }
  private Color couleur ;
  private Paneau pan ;
  static ActionCouleur actionInactive ;   // ne pas oublier static
}


// Saisie de texte
class FenText extends JFrame implements ActionListener, FocusListener
{ public FenText ()
  { setTitle ("Saisie de texte") ;
    setBounds(10,600,700, 100) ;
    Container contenu = getContentPane() ;
    contenu.setLayout (new FlowLayout() ) ;

    saisie = new JTextField (20) ;
    contenu.add(saisie) ;
    saisie.addActionListener(this) ;
    saisie.addFocusListener(this) ;

    copie = new JTextField (50) ;
    copie.setEditable(false);
    contenu.add(copie) ;
  }
  public void actionPerformed (ActionEvent e)
  { System.out.println ("validation saisie") ;
    String texte = saisie.getText() ;
    copie.setText(texte) ;
  }
  public void focusLost (FocusEvent e)
  { System.out.println ("perte focus saisie") ;
    String texte = saisie.getText() ;
    copie.setText(texte) ;
  }
  public void focusGained (FocusEvent e)
  { System.out.println ("focus sur saisie") ;
  }
  private JTextField saisie, copie ;
  private JButton bouton ;
}

// Methode principale

public class case_bouttons_dessiner
{ public static void main (String args[])
  { FenBoutonsDyn fen = new FenBoutonsDyn () ;
    fen.setVisible (true) ;

  }
}
