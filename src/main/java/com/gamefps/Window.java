package com.gamefps;

import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public final class Window {
    private final int width;
    private final int height;
    private final String title;
    private long handle;

    public Window(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;
    }

    public void init() {
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        handle = glfwCreateWindow(width, height, title, 0, 0);
        if (handle == 0) {
            throw new IllegalStateException("Failed to create window");
        }

        glfwMakeContextCurrent(handle);
        glfwSwapInterval(1);
        GL.createCapabilities();

        glEnable(GL_DEPTH_TEST);
        glClearColor(0.08f, 0.08f, 0.1f, 1f);
        glfwSetInputMode(handle, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
    }

    public void swapBuffers() {
        glfwSwapBuffers(handle);
    }

    public void setTitle(String title) {
        glfwSetWindowTitle(handle, title);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(handle);
    }

    public double getTime() {
        return glfwGetTime();
    }

    public long getHandle() {
        return handle;
    }

    public Matrix4f getProjection() {
        return new Matrix4f().perspective((float) Math.toRadians(70.0), (float) width / height, 0.1f, 200f);
    }

    public void cleanup() {
        glfwDestroyWindow(handle);
        glfwTerminate();
    }
}
