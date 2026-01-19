package com.gamefps;

import static org.lwjgl.glfw.GLFW.*;

public final class Input {
    private final Window window;
    private final boolean[] keys = new boolean[GLFW_KEY_LAST];
    private final boolean[] keysPressed = new boolean[GLFW_KEY_LAST];
    private boolean leftMouseDown;
    private boolean leftMouseClicked;
    private double lastMouseX;
    private double lastMouseY;
    private float deltaX;
    private float deltaY;
    private boolean firstMouse = true;

    public Input(Window window) {
        this.window = window;
    }

    public void init() {
        long handle = window.getHandle();
        glfwSetKeyCallback(handle, (windowHandle, key, scancode, action, mods) -> {
            if (key < 0) {
                return;
            }
            if (action == GLFW_PRESS) {
                keys[key] = true;
                keysPressed[key] = true;
            } else if (action == GLFW_RELEASE) {
                keys[key] = false;
            }
        });
        glfwSetMouseButtonCallback(handle, (windowHandle, button, action, mods) -> {
            if (button == GLFW_MOUSE_BUTTON_LEFT) {
                if (action == GLFW_PRESS) {
                    leftMouseDown = true;
                    leftMouseClicked = true;
                } else if (action == GLFW_RELEASE) {
                    leftMouseDown = false;
                }
            }
        });
        glfwSetCursorPosCallback(handle, (windowHandle, xpos, ypos) -> {
            if (firstMouse) {
                lastMouseX = xpos;
                lastMouseY = ypos;
                firstMouse = false;
            }
            deltaX = (float) (xpos - lastMouseX);
            deltaY = (float) (lastMouseY - ypos);
            lastMouseX = xpos;
            lastMouseY = ypos;
        });
    }

    public void poll() {
        deltaX = 0f;
        deltaY = 0f;
        leftMouseClicked = false;
        for (int i = 0; i < keysPressed.length; i++) {
            keysPressed[i] = false;
        }
        glfwPollEvents();
    }

    public boolean isKeyDown(int key) {
        return keys[key];
    }

    public boolean wasKeyPressed(int key) {
        return keysPressed[key];
    }

    public boolean isLeftMouseDown() {
        return leftMouseDown;
    }

    public boolean wasLeftMouseClicked() {
        return leftMouseClicked;
    }

    public float getDeltaX() {
        return deltaX;
    }

    public float getDeltaY() {
        return deltaY;
    }
}
