package com.gamefps;

import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public final class Player {
    private final Camera camera;
    private final Weapon[] weapons;
    private int currentWeaponIndex;
    private float health = 100f;

    public Player(Vector3f startPosition) {
        this.camera = new Camera(startPosition);
        this.weapons = new Weapon[] {
                new Weapon(WeaponType.RIFLE),
                new Weapon(WeaponType.SHOTGUN)
        };
        this.currentWeaponIndex = 0;
    }

    public void update(Input input, float delta, World world) {
        float speed = input.isKeyDown(GLFW_KEY_LEFT_SHIFT) ? 6f : 3.5f;
        Vector3f movement = new Vector3f();
        Vector3f forward = camera.getFront();
        Vector3f right = new Vector3f(forward.z, 0f, -forward.x).normalize();

        if (input.isKeyDown(GLFW_KEY_W)) {
            movement.add(forward.x, 0f, forward.z);
        }
        if (input.isKeyDown(GLFW_KEY_S)) {
            movement.sub(forward.x, 0f, forward.z);
        }
        if (input.isKeyDown(GLFW_KEY_A)) {
            movement.sub(right);
        }
        if (input.isKeyDown(GLFW_KEY_D)) {
            movement.add(right);
        }

        if (movement.lengthSquared() > 0f) {
            movement.normalize().mul(speed * delta);
            camera.getPosition().add(movement);
        }

        camera.addYaw(input.getDeltaX() * 0.12f);
        camera.addPitch(input.getDeltaY() * 0.12f);

        if (input.wasKeyPressed(GLFW_KEY_1)) {
            currentWeaponIndex = 0;
        }
        if (input.wasKeyPressed(GLFW_KEY_2)) {
            currentWeaponIndex = 1;
        }
        if (input.wasKeyPressed(GLFW_KEY_R)) {
            getWeapon().reload();
        }

        getWeapon().update(delta);

        if (input.isLeftMouseDown()) {
            if (getWeapon().canShoot()) {
                getWeapon().shoot();
                world.handleShot(camera.getPosition(), camera.getFront(), getWeapon().getType().damage());
            }
        }

        if (input.wasKeyPressed(GLFW_KEY_ESCAPE)) {
            glfwSetWindowShouldClose(glfwGetCurrentContext(), true);
        }
    }

    public Camera getCamera() {
        return camera;
    }

    public Weapon getWeapon() {
        return weapons[currentWeaponIndex];
    }

    public String getWeaponLabel() {
        Weapon weapon = getWeapon();
        return weapon.getType().displayName() + " [" + weapon.getAmmo() + "/" + weapon.getType().magazineSize() + "]";
    }

    public float getHealth() {
        return health;
    }

    public void damage(float amount) {
        health = Math.max(0f, health - amount);
    }
}
