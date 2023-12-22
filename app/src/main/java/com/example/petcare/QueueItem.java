package com.example.petcare;

public class QueueItem {
    private String animalName;
    private String animalType;
    private String ownerName;
    private String ownerContact;
    private String symptomDescription;
    private String namaDokter;

    public QueueItem() {
    }

    public QueueItem(String animalName, String animalType, String ownerName, String ownerContact, String symptomDescription, String namaDokter) {
        this.animalName = animalName;
        this.animalType = animalType;
        this.ownerName = ownerName;
        this.ownerContact = ownerContact;
        this.symptomDescription = symptomDescription;
        this.namaDokter = namaDokter;
    }
    public String getAnimalName() {
        return animalName;
    }

    public void setAnimalName(String animalName) {
        this.animalName = animalName;
    }

    public String getAnimalType() {
        return animalType;
    }

    public void setAnimalType(String animalType) {
        this.animalType = animalType;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerContact() {
        return ownerContact;
    }

    public void setOwnerContact(String ownerContact) {
        this.ownerContact = ownerContact;
    }

    public String getSymptomDescription() {
        return symptomDescription;
    }

    public void setSymptomDescription(String symptomDescription) {
        this.symptomDescription = symptomDescription;
    }

    public String getNamaDokter() {
        return namaDokter;
    }

    public void setNamaDokter(String namaDokter) {
        this.namaDokter = namaDokter;
    }
}
