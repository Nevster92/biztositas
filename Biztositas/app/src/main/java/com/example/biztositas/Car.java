package com.example.biztositas;

public class Car {

    private String licenceNumber;
    private String trafficLicenceNumber;
    private String chassisNumber;
    private String carType;
    private String licenceExpire;
    private int bonus;
    private String owner;
    private String insuranceId = null;

    public Car() {
    }

    public Car(String licenceNumber, String trafficLicenceNumber, String chassisNumber, String carType, String licenceExpire, int bonus,String owner) {
        this.licenceNumber = licenceNumber;
        this.trafficLicenceNumber = trafficLicenceNumber;
        this.chassisNumber = chassisNumber;
        this.carType = carType;
        this.licenceExpire = licenceExpire;
        this. bonus = bonus;
        this.owner = owner;
    }

    public String getInsuranceId() {
        return insuranceId;
    }

    public void setInsuranceId(String insuranceId) {
        this.insuranceId = insuranceId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getLicenceExpire() {
        return licenceExpire;
    }

    public void setLicenceExpire(String licenceExpire) {
        this.licenceExpire = licenceExpire;
    }

    public int getBonus() {
        return bonus;
    }


    public void setBonus(int bonus) {
        this.bonus = bonus;
    }

    public String getLicenceNumber() {
        return licenceNumber;
    }

    public void setLicenceNumber(String licenceNumber) {
        this.licenceNumber = licenceNumber;
    }

    public String getTrafficLicenceNumber() {
        return trafficLicenceNumber;
    }

    public void setTrafficLicenceNumber(String trafficLicenceNumber) {
        this.trafficLicenceNumber = trafficLicenceNumber;
    }

    public String getChassisNumber() {
        return chassisNumber;
    }

    public void setChassisNumber(String chassisNumber) {
        this.chassisNumber = chassisNumber;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }
}
