package com.gamefps;

import org.joml.Vector3f;

public final class Enemy {
    private final Vector3f position;
    private float health = 50f;
    private float attackCooldown;

    public Enemy(Vector3f position) {
        this.position = position;
    }

    public void update(Player player, float delta) {
        Vector3f direction = new Vector3f(player.getCamera().getPosition()).sub(position);
        float distance = direction.length();
        if (distance > 1.2f) {
            direction.normalize().mul(delta * 1.5f);
            position.add(direction);
        } else {
            attackCooldown -= delta;
            if (attackCooldown <= 0f) {
                player.damage(5f);
                attackCooldown = 1.2f;
            }
        }
    }

    public boolean isAlive() {
        return health > 0f;
    }

    public void damage(float amount) {
        health = Math.max(0f, health - amount);
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getHealth() {
        return health;
    }
}
