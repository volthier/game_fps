package com.gamefps;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.opengl.GL11.*;

public final class Renderer {
    private Shader shader;
    private Mesh cube;
    private Mesh crosshair;
    private HudRenderer hudRenderer;

    public void init() {
        shader = new Shader(
                ResourceLoader.loadResource("/shaders/basic.vert"),
                ResourceLoader.loadResource("/shaders/basic.frag")
        );
        cube = new Mesh(createCubeVertices());
        crosshair = new Mesh(new float[] {
                -0.02f, 0f, 0f,
                0.02f, 0f, 0f,
                0f, -0.02f, 0f,
                0f, 0.02f, 0f
        }, GL_LINES);
        hudRenderer = new HudRenderer();
        hudRenderer.init();
    }

    public void render(World world, Player player, Matrix4f projection, int width, int height) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        shader.bind();
        shader.setMatrix4f("projection", projection);
        shader.setMatrix4f("view", player.getCamera().getViewMatrix());

        renderFloor();
        renderPlayerWeapon(player);
        renderEnemies(world);
        shader.unbind();

        renderCrosshair();
        renderHud(player, width, height);
    }

    private void renderFloor() {
        Matrix4f model = new Matrix4f().translate(0f, -0.5f, 0f).scale(50f, 1f, 50f);
        shader.setMatrix4f("model", model);
        shader.setVector3f("objectColor", 0.2f, 0.2f, 0.25f);
        cube.render();
    }

    private void renderPlayerWeapon(Player player) {
        Matrix4f model = new Matrix4f().translate(player.getCamera().getPosition()).translate(0.5f, -0.5f, -1f)
                .rotateY((float) Math.toRadians(player.getCamera().getYaw()))
                .scale(0.3f, 0.2f, 0.6f);
        shader.setMatrix4f("model", model);
        Weapon weapon = player.getWeapon();
        if (weapon.getType() == WeaponType.RIFLE) {
            shader.setVector3f("objectColor", 0.3f, 0.7f, 0.9f);
        } else {
            shader.setVector3f("objectColor", 0.9f, 0.6f, 0.3f);
        }
        cube.render();
    }

    private void renderEnemies(World world) {
        for (Enemy enemy : world.getEnemies()) {
            Vector3f pos = enemy.getPosition();
            Matrix4f model = new Matrix4f().translate(pos.x, 0f, pos.z).scale(0.9f, 1.8f, 0.9f);
            shader.setMatrix4f("model", model);
            float health = enemy.getHealth();
            shader.setVector3f("objectColor", 1f - health / 50f, 0.2f, 0.2f);
            cube.render();
        }
    }

    private void renderCrosshair() {
        glDisable(GL_DEPTH_TEST);
        shader.bind();
        shader.setMatrix4f("projection", new Matrix4f());
        shader.setMatrix4f("view", new Matrix4f());
        shader.setMatrix4f("model", new Matrix4f());
        shader.setVector3f("objectColor", 0.9f, 0.9f, 0.9f);
        crosshair.render();
        shader.unbind();
        glEnable(GL_DEPTH_TEST);
    }

    private void renderHud(Player player, int width, int height) {
        Weapon weapon = player.getWeapon();
        StringBuilder hud = new StringBuilder();
        hud.append("HP: ").append(Math.round(player.getHealth())).append('\n');
        hud.append("Weapon: ").append(weapon.getType().displayName()).append('\n');
        hud.append("Ammo: ").append(weapon.getAmmo()).append('/').append(weapon.getType().magazineSize());
        if (weapon.isReloading()) {
            hud.append("  Reloading ").append(Math.round(weapon.getReloadProgress() * 100f)).append('%');
        }
        hud.append('\n');
        hud.append("WASD move | Mouse look | LMB shoot | R reload | 1/2 swap");
        hudRenderer.render(hud.toString(), width, height);
    }

    public void cleanup() {
        cube.cleanup();
        crosshair.cleanup();
        shader.cleanup();
        hudRenderer.cleanup();
    }

    private float[] createCubeVertices() {
        return new float[] {
                -0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, 0.5f, -0.5f,
                0.5f, 0.5f, -0.5f,
                -0.5f, 0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,

                -0.5f, -0.5f, 0.5f,
                0.5f, -0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,
                -0.5f, 0.5f, 0.5f,
                -0.5f, -0.5f, 0.5f,

                -0.5f, 0.5f, 0.5f,
                -0.5f, 0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,
                -0.5f, -0.5f, 0.5f,
                -0.5f, 0.5f, 0.5f,

                0.5f, 0.5f, 0.5f,
                0.5f, 0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,

                -0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, 0.5f,
                0.5f, -0.5f, 0.5f,
                -0.5f, -0.5f, 0.5f,
                -0.5f, -0.5f, -0.5f,

                -0.5f, 0.5f, -0.5f,
                0.5f, 0.5f, -0.5f,
                0.5f, 0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,
                -0.5f, 0.5f, 0.5f,
                -0.5f, 0.5f, -0.5f
        };
    }
}
