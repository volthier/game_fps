package com.gamefps;

import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class World {
    private final List<Enemy> enemies = new ArrayList<>();
    private final Random random = new Random();

    public void spawnEnemies() {
        for (int i = 0; i < 8; i++) {
            enemies.add(new Enemy(new Vector3f(random.nextFloat() * 20f - 10f, 0f, random.nextFloat() * -20f)));
        }
    }

    public void update(Player player, float delta) {
        enemies.removeIf(enemy -> !enemy.isAlive());
        for (Enemy enemy : enemies) {
            enemy.update(player, delta);
        }
    }

    public void handleShot(Vector3f origin, Vector3f direction, float damage) {
        Enemy hit = null;
        float closest = Float.MAX_VALUE;
        for (Enemy enemy : enemies) {
            float distance = raycastEnemy(origin, direction, enemy);
            if (distance >= 0f && distance < closest) {
                closest = distance;
                hit = enemy;
            }
        }
        if (hit != null) {
            hit.damage(damage);
        }
    }

    private float raycastEnemy(Vector3f origin, Vector3f direction, Enemy enemy) {
        Vector3f center = new Vector3f(enemy.getPosition()).add(0f, 0.9f, 0f);
        float radius = 0.6f;
        Vector3f toCenter = new Vector3f(center).sub(origin);
        float t = toCenter.dot(direction);
        if (t < 0f) {
            return -1f;
        }
        Vector3f closestPoint = new Vector3f(origin).add(new Vector3f(direction).mul(t));
        float distSq = closestPoint.distanceSquared(center);
        if (distSq <= radius * radius) {
            return t;
        }
        return -1f;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }
}
