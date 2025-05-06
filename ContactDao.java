import java.sql.*; // pour  la connexion a une base de donne
import java.util.ArrayList;//manipuler les listes des donnes
import java.util.List;

public class ContactDao {
    private final String url = "jdbc:mysql://localhost:3306/java"; // ✅ base de données "java"
    private final String user = "root";
    private final String password = "abrar123";

    private Connection getConnection() throws SQLException  {
        return DriverManager.getConnection(url, user, password); // drive manger fourni par jdbc pour etablir une connexion avec une base
    }

    public List<Contact> loadContacts() { //
        List<Contact> contacts = new ArrayList<>(); // inialise la liste vide des contact
        String sql = "SELECT nom, prenom, telephone, email FROM contact"; // preparer la requet sql

        try (Connection conn = getConnection();//  try pour genere auto la fermeture des ressources *con getcnx appeler pour etablir cnx a la base
             Statement stmt = conn.createStatement(); // a partir la cnx con on cree objet statement execute les requete sql simple
             ResultSet rs = stmt.executeQuery(sql)) { // execute via stmt.execut  et renvoi la rslt dnas un objet rs

            while (rs.next()) { // rs.next deplace ceurseur ver la ligne suivants
                contacts.add(new Contact( // parcour les resultat et cree objet contact pour chaque ligne
                        rs.getString("nom"),
                        rs.getString("prenom"), // les objet sont ajouter a la liste
                        rs.getString("telephone"),
                        rs.getString("email")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // en cas d'erreur sql affiche partie exception
        }

        return contacts; // retourne la liste des contact
    }

    public void addContact(Contact contact) {
        String sql = "INSERT INTO contact (nom, prenom, telephone, email) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection(); //  ouvre une connection
             PreparedStatement stmt = conn.prepareStatement(sql)) { // utilise pour eviter les injection sql et faciliter insertion

            stmt.setString(1, contact.getNom()); //  remplir1
            stmt.setString(2, contact.getPrenom());
            stmt.setString(3, contact.getTelephone());
            stmt.setString(4, contact.getEmail());

            stmt.executeUpdate(); // executer la requete sql dans la base et ajout reeellement dans a base
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteContact(Contact contact) {
        String sql = "DELETE FROM contact WHERE nom = ? AND prenom = ? AND telephone = ? AND email = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, contact.getNom());
            stmt.setString(2, contact.getPrenom());
            stmt.setString(3, contact.getTelephone());
            stmt.setString(4, contact.getEmail());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Contact> searchContacts(String keyword) {
        List<Contact> results = new ArrayList<>();
        String sql = "SELECT nom, prenom, telephone, email FROM contact WHERE nom LIKE ? OR prenom LIKE ? OR telephone LIKE ? OR email LIKE ?";

        try (Connection conn = getConnection(); // cree la connexion
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String like = "%" + keyword + "%"; // chercher le  mot clee partout dans le champs
            for (int i = 1; i <= 4; i++) { // on donne mot clee pour les 4 champs
                stmt.setString(i, like); //
            }

            ResultSet rs = stmt.executeQuery(); // excute la requete et renvoie les rslt
            while (rs.next()) { // commnce la boucle pou lire chaque ligne trouvee
                results.add(new Contact(
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("telephone"),
                        rs.getString("email")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }
}