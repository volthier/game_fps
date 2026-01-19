package com.gamefps;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class Camera {
    private final Vector3f position;
    private float yaw;
    private float pitch;

    public Camera(Vector3f position) {
        this.position = position;
        this.yaw = -90f;
    }

    public Matrix4f getViewMatrix() {
        Vector3f front = getFront();
        Vector3f target = new Vector3f(position).add(front);
        return new Matrix4f().lookAt(position, target, new Vector3f(0f, 1f, 0f));
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getFront() {
        Vector3f front = new Vector3f();
        front.x = (float) (Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
        front.y = (float) Math.sin(Math.toRadians(pitch));
        front.z = (float) (Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
        return front.normalize();
    }

    public float getYaw() {
        return yaw;
    }

    public void addYaw(float delta) {
        yaw += delta;
    }

    public void addPitch(float delta) {
        pitch += delta;
        pitch = Math.max(-89f, Math.min(89f, pitch));
    }
}
