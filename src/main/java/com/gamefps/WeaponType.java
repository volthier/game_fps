package com.gamefps;

public enum WeaponType {
    RIFLE("Rifle", 30, 0.12f, 2.0f, 8f),
    SHOTGUN("Shotgun", 8, 0.8f, 2.5f, 18f);

    private final String displayName;
    private final int magazineSize;
    private final float fireRate;
    private final float reloadTime;
    private final float damage;

    WeaponType(String displayName, int magazineSize, float fireRate, float reloadTime, float damage) {
        this.displayName = displayName;
        this.magazineSize = magazineSize;
        this.fireRate = fireRate;
        this.reloadTime = reloadTime;
        this.damage = damage;
    }

    public String displayName() {
        return displayName;
    }

    public int magazineSize() {
        return magazineSize;
    }

    public float fireRate() {
        return fireRate;
    }

    public float reloadTime() {
        return reloadTime;
    }

    public float damage() {
        return damage;
    }
}
