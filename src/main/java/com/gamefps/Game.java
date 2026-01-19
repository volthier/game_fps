package com.gamefps;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class Game {
    private final Window window;
    private final Input input;
    private final Renderer renderer;
    private final World world;
    private final Player player;

    public Game() {
        window = new Window(1280, 720, "FPS Java 25");
        input = new Input(window);
        renderer = new Renderer();
        world = new World();
        player = new Player(new Vector3f(0f, 1.6f, 5f));
    }

    public void run() {
        window.init();
        renderer.init();
        input.init();
        world.spawnEnemies();

        double lastTime = window.getTime();
        while (!window.shouldClose()) {
            double now = window.getTime();
            float delta = (float) (now - lastTime);
            lastTime = now;

            input.poll();
            player.update(input, delta, world);
            world.update(player, delta);

            Matrix4f projection = window.getProjection();
            renderer.render(world, player, projection, window.getWidth(), window.getHeight());
            window.setTitle(\"FPS Java 25 - \" + player.getWeaponLabel() + \" | HP \" + Math.round(player.getHealth()));

            window.swapBuffers();
        }

        renderer.cleanup();
        window.cleanup();
    }

    public static void main(String[] args) {
        new Game().run();
    }
}
