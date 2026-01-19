package com.gamefps;

public final class Weapon {
    private final WeaponType type;
    private int ammo;
    private float fireCooldown;
    private float reloadTimer;
    private boolean reloading;

    public Weapon(WeaponType type) {
        this.type = type;
        this.ammo = type.magazineSize();
    }

    public void update(float delta) {
        fireCooldown = Math.max(0f, fireCooldown - delta);
        if (reloading) {
            reloadTimer -= delta;
            if (reloadTimer <= 0f) {
                ammo = type.magazineSize();
                reloading = false;
            }
        }
    }

    public boolean canShoot() {
        return !reloading && ammo > 0 && fireCooldown <= 0f;
    }

    public void shoot() {
        if (!canShoot()) {
            return;
        }
        ammo--;
        fireCooldown = type.fireRate();
    }

    public void reload() {
        if (reloading || ammo == type.magazineSize()) {
            return;
        }
        reloading = true;
        reloadTimer = type.reloadTime();
    }

    public WeaponType getType() {
        return type;
    }

    public int getAmmo() {
        return ammo;
    }

    public boolean isReloading() {
        return reloading;
    }

    public float getReloadProgress() {
        if (!reloading) {
            return 0f;
        }
        return 1f - (reloadTimer / type.reloadTime());
    }
}
