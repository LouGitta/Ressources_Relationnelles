package cesi.RessourceRelationnelles.models;

public enum Visibility {
    private_visibility("Privée"),
    shared("Partagée"),
    public_visibility("Publique");

    private final String label;

    Visibility(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}