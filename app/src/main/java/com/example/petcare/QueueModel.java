package com.example.petcare;

public class QueueModel {
    private String namaDokter;
    private String animalName;
    private String animalType;

    public QueueModel(){

    }

    public QueueModel(String ownerName, String animalName, String animalType, String namaDokter) {
        this.namaDokter = namaDokter;
        this.animalName = animalName;
        this.animalType = animalType;
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

    public String getNamaDokter() {
        return namaDokter;
    }

    public void setNamaDokter(String namaDokter) {
        this.namaDokter = namaDokter;
    }
}