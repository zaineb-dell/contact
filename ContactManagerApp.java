import javax.swing.*; // pour construire l'interface
import javax.swing.table.DefaultTableModel; //modele de donnes pour remplir un tab
import java.awt.*; // pour les element d'interface graphique couleur ...
import java.util.List; // pour stocker un liste des contact



public class ContactManagerApp extends JFrame { // classe principle herite jframe
    private ContactDao contactDao; // objet pour accede aux donnes des contact
    private DefaultTableModel tableModel; //tab contenat les donnes a affichier
    private JTable contactsTable; // afficher les donnes se forme de tableau

    public ContactManagerApp() {
        contactDao = new ContactDao();
        initializeUI();
        loadContacts();
    }

    private void initializeUI() {
        setTitle("Gestionnaire de Contacts");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columnNames = {"Nom", "Prénom", "Téléphone", "Email"}; // le nom de colonnes dans le tableau
        tableModel = new DefaultTableModel(columnNames, 0) { // créer un modél de tableau vide avec 4 colonnes et commence avec 0 ligne
            @Override
            public boolean isCellEditable(int row, int column) { // permet de ne pas modifier les variables
                return false;
            } // les cellule ne sont pas modifiable
        };
        contactsTable = new JTable(tableModel); // créer une table swing à partir du table tableModel ajout scroll s'il ya bcp de ligne
        JScrollPane scrollPane = new JScrollPane(contactsTable); // quand on a beaucoup de ligne qui dépasse l'ecran ce scroll ajoute automatiquement des barres de défilement

        // Création des boutons
        JButton addButton = new JButton("Ajouter");
        JButton deleteButton = new JButton("Supprimer");
        JButton searchButton = new JButton("Rechercher");

        // Champ de text pour entrer une  recherche
        JTextField searchField = new JTextField(20);

        // Panel  cree un pannaux pour les boutons et la recherche
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(new JLabel("Rechercher:"));
        buttonPanel.add(searchField);
        buttonPanel.add(searchButton);

        //  positionnement des composants : boutons en haut tableau au centre
        setLayout(new BorderLayout());
        add(buttonPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // Gestion des événements ajouter des action auc boutons
        addButton.addActionListener(e -> showAddContactDialog());
        deleteButton.addActionListener(e -> deleteSelectedContact());
        searchButton.addActionListener(e -> searchContacts(searchField.getText()));
    }
// chargement des contact dans le tableau
    private void loadContacts() { // declaration methode prive cxx
        List<Contact> contacts = contactDao.loadContacts(); // on appel la methode load de objet contact dao pour recuperer les contact enregistre
        tableModel.setRowCount(0);// avant de remplir le tab oremet nobre des ligne a 0 pour eviter de dupliquer de donnes

        for (Contact contact : contacts) { // elle parcourt chaque  contact de la liste contact
            Object[] rowData = { // cree tableau datarow  contenant les 4 informations de contact
                    contact.getNom(),
                    contact.getPrenom(),
                    contact.getTelephone(),
                    contact.getEmail() // ces valeurs constituer une ligne du tableaux
            };
            tableModel.addRow(rowData); // on ajoute datarow au table model ce qui affiche  le contact dans la jtable de l'interface
        }
    }
// pour ajouter un contact
    private void showAddContactDialog() {
        JTextField nomField = new JTextField();
        JTextField prenomField = new JTextField();
        JTextField telephoneField = new JTextField();
        JTextField emailField = new JTextField();
// les champs saisie pour les information du contact
        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("Nom:"));
        panel.add(nomField);
        panel.add(new JLabel("Prénom:"));
        panel.add(prenomField);
        panel.add(new JLabel("Téléphone:"));
        panel.add(telephoneField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
// afficher un formilaire de 4 ligne avec les champs
        int result = JOptionPane.showConfirmDialog( // appeler methode show de la classe jooption  choix d'utilisater stocker dans la variable result
                this, panel, "Ajouter un contact",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE); // contient deux boutons ok et annuler
        // boite de dialogue  avec bouton ok ou annuler

        if (result == JOptionPane.OK_OPTION) {
            Contact contact = new Contact(
                    nomField.getText(),
                    prenomField.getText(),
                    telephoneField.getText(),
                    emailField.getText());

            contactDao.addContact(contact);
            loadContacts();
        }
    }
// quand utilisateur click sur ok un contact deja cree
    private void deleteSelectedContact() { // declaration methode priver nomer , apple quand veut supprimer un conctact de jtable
        int selectedRow = contactsTable.getSelectedRow(); // recupere l'indice de la ligne silectionner si auccun ligneretourne -1
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un contact à supprimer",
                    "Aucune sélection", JOptionPane.WARNING_MESSAGE); //affiche boite de dialoque messge d'erure
            return;
        }
        // verifier un contact est selectionnee , sinon affiche un message d'erreur

        Contact contact = new Contact( // cree nouvelle objet contact represente le contact selectionne dans le tableaux
                (String) tableModel.getValueAt(selectedRow, 0), // recuperer la valeur de colonne o nom
                (String) tableModel.getValueAt(selectedRow, 1), // acceder a la valeur dune cellule
                (String) tableModel.getValueAt(selectedRow, 2),
                (String) tableModel.getValueAt(selectedRow, 3));
        // recuperer les donnes de la ligne selectionner , supprimer le contact et recharger la table

        contactDao.deleteContact(contact); //appele la methode sur l'objet contact dao  cela permet de suppimer le contact de base de donnes
        loadContacts(); // mettre a jours tous les contact et l'affichage de la jtable
    }
// rechercher des contact
    private void searchContacts(String searchTerm) {// methode prive  sert a recherche quand les info coresspond au mot cle
        if (searchTerm.trim().isEmpty()) { // if mot cle est vide
            loadContacts(); // cela affiche tous les contact
            return;
        }
        // si le champs et vides en recharge tous les contact

        List<Contact> results = contactDao.searchContacts(searchTerm); // elle retourne la list des contact qui correspond a mot cle de recherche
        tableModel.setRowCount(0); // on vide les resulta ancien pour affichier les nouveaux


        for (Contact contact : results) { // parcourir les resultat
            Object[] rowData = { // tableau de contact affichier par une ligne
                    contact.getNom(),
                    contact.getPrenom(),
                    contact.getTelephone(),
                    contact.getEmail()
            };
            tableModel.addRow(rowData); // on ajoute cette ligne si contact trouve au tableau affichie
        }
    }
//chercher les contact puis affichier dans la table
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ContactManagerApp app = new ContactManagerApp();
            app.setVisible(true);
        });
    }
}