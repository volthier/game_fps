package com.gamefps;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBEasyFont;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public final class HudRenderer {
    private static final int MAX_QUADS = 1024;
    private static final int VERTEX_STRIDE_BYTES = 16;

    private final ByteBuffer vertexBuffer = BufferUtils.createByteBuffer(MAX_QUADS * 4 * VERTEX_STRIDE_BYTES);
    private Shader shader;
    private int vaoId;
    private int vboId;
    private int eboId;

    public void init() {
        shader = new Shader(
                ResourceLoader.loadResource("/shaders/hud.vert"),
                ResourceLoader.loadResource("/shaders/hud.frag")
        );

        vaoId = glGenVertexArrays();
        vboId = glGenBuffers();
        eboId = glGenBuffers();

        glBindVertexArray(vaoId);
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer.capacity(), GL_DYNAMIC_DRAW);

        glVertexAttribPointer(0, 3, GL_FLOAT, false, VERTEX_STRIDE_BYTES, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, 4, GL_UNSIGNED_BYTE, true, VERTEX_STRIDE_BYTES, 12);
        glEnableVertexAttribArray(1);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buildIndexBuffer(), GL_STATIC_DRAW);

        glBindVertexArray(0);
    }

    public void render(String text, int width, int height) {
        int quadCount = updateBuffer(text);
        if (quadCount == 0) {
            return;
        }

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_DEPTH_TEST);

        shader.bind();
        shader.setMatrix4f("projection", new Matrix4f().ortho2D(0f, width, height, 0f));

        glBindVertexArray(vaoId);
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertexBuffer);
        glDrawElements(GL_TRIANGLES, quadCount * 6, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);

        shader.unbind();
        glEnable(GL_DEPTH_TEST);
        glDisable(GL_BLEND);
    }

    private int updateBuffer(String text) {
        vertexBuffer.clear();
        int quadCount = STBEasyFont.stb_easy_font_print(16f, 24f, text, null, vertexBuffer);
        vertexBuffer.flip();
        return Math.min(quadCount, MAX_QUADS);
    }

    private IntBuffer buildIndexBuffer() {
        IntBuffer indices = BufferUtils.createIntBuffer(MAX_QUADS * 6);
        for (int i = 0; i < MAX_QUADS; i++) {
            int base = i * 4;
            indices.put(base);
            indices.put(base + 1);
            indices.put(base + 2);
            indices.put(base);
            indices.put(base + 2);
            indices.put(base + 3);
        }
        indices.flip();
        return indices;
    }

    public void cleanup() {
        glDeleteBuffers(vboId);
        glDeleteBuffers(eboId);
        glDeleteVertexArrays(vaoId);
        shader.cleanup();
    }
}
