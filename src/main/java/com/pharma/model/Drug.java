package com.pharma.model;

public class Drug {
    private String name;
    private String description;
    private String usage;
    private String safety;
    private String manufacturer;
    private String expiry;
    private String category;

    public Drug(String name, String description, String usage, String safety,
                String manufacturer, String expiry, String category) {
        this.name = name;
        this.description = description;
        this.usage = usage;
        this.safety = safety;
        this.manufacturer = manufacturer;
        this.expiry = expiry;
        this.category = category;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUsage() {
        return usage;
    }

    public String getSafety() {
        return safety;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getExpiry() {
        return expiry;
    }

    public String getCategory() {
        return category;
    }
}
