
public class Contact {
    private String nom;
    private String prenom;
    private String telephone;
    private String email;


    public Contact(String nom, String prenom, String telephone, String email) { // contructeur appeller quand on cree un nv contct
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.email = email;
    }

    // Getters nécessaires pour la sérialisation GSON
    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getEmail() {
        return email;
    }

    // Setters pour la désérialisation GSON
    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Méthode pour afficher les informations du contact sous forme de chaîne
     * @return Une représentation textuelle du contact
     */
    @Override
    public String toString() {
        return prenom + " " + nom + " - Tel: " + telephone + " - Email: " + email;
    }
}