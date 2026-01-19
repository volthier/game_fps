package com.gamefps;

import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;

public final class Shader {
    private final int programId;

    public Shader(String vertexSource, String fragmentSource) {
        int vertexId = createShader(vertexSource, GL_VERTEX_SHADER);
        int fragmentId = createShader(fragmentSource, GL_FRAGMENT_SHADER);
        programId = glCreateProgram();
        glAttachShader(programId, vertexId);
        glAttachShader(programId, fragmentId);
        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            throw new IllegalStateException("Shader link failed: " + glGetProgramInfoLog(programId));
        }
        glDeleteShader(vertexId);
        glDeleteShader(fragmentId);
    }

    private int createShader(String source, int type) {
        int shaderId = glCreateShader(type);
        glShaderSource(shaderId, source);
        glCompileShader(shaderId);
        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw new IllegalStateException("Shader compile failed: " + glGetShaderInfoLog(shaderId));
        }
        return shaderId;
    }

    public void bind() {
        glUseProgram(programId);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void cleanup() {
        glDeleteProgram(programId);
    }

    public void setMatrix4f(String name, Matrix4f matrix) {
        int location = glGetUniformLocation(programId, name);
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(16);
            matrix.get(buffer);
            glUniformMatrix4fv(location, false, buffer);
        }
    }

    public void setVector3f(String name, float x, float y, float z) {
        int location = glGetUniformLocation(programId, name);
        glUniform3f(location, x, y, z);
    }
}
