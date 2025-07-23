/*
 * This file is part of ThinGL - https://github.com/RaphiMC/ThinGL
 * Copyright (C) 2024-2025 RK_01/RaphiMC and contributors
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.florianmichael.thingl.encoder;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.raphimc.thingl.drawbuilder.DrawMode;
import net.raphimc.thingl.drawbuilder.builder.BuiltBuffer;
import net.raphimc.thingl.drawbuilder.builder.command.DrawArraysCommand;
import net.raphimc.thingl.drawbuilder.builder.command.DrawCommand;
import net.raphimc.thingl.drawbuilder.builder.command.DrawElementsCommand;
import net.raphimc.thingl.resource.vertexarray.VertexArray;
import org.lwjgl.opengl.GL41C;
import org.lwjgl.opengl.GL43C;

import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class AppleCommandEncoder implements GlCommandEncoder {

    private static final Int2IntMap TEXTURE_QUERY_TARGETS = new Int2IntOpenHashMap();
    private static final Int2IntMap FORMATS = new Int2IntOpenHashMap();
    private static final Int2IntMap TYPES = new Int2IntOpenHashMap();

    static {
        TEXTURE_QUERY_TARGETS.put(GL41C.GL_TEXTURE_1D, GL41C.GL_TEXTURE_BINDING_1D);
        TEXTURE_QUERY_TARGETS.put(GL41C.GL_TEXTURE_2D, GL41C.GL_TEXTURE_BINDING_2D);
        TEXTURE_QUERY_TARGETS.put(GL41C.GL_TEXTURE_2D_MULTISAMPLE, GL41C.GL_TEXTURE_BINDING_2D_MULTISAMPLE);
        TEXTURE_QUERY_TARGETS.put(GL41C.GL_TEXTURE_CUBE_MAP, GL41C.GL_TEXTURE_BINDING_CUBE_MAP);
        TEXTURE_QUERY_TARGETS.put(GL41C.GL_TEXTURE_3D, GL41C.GL_TEXTURE_BINDING_3D);

        FORMATS.put(GL41C.GL_RGBA8, GL41C.GL_RGBA);
        FORMATS.put(GL41C.GL_RGB8, GL41C.GL_RGB);
        FORMATS.put(GL41C.GL_RG8, GL41C.GL_RG);
        FORMATS.put(GL41C.GL_R8, GL41C.GL_RED);
        FORMATS.put(GL41C.GL_DEPTH_COMPONENT32F, GL41C.GL_DEPTH_COMPONENT);
        FORMATS.put(GL41C.GL_DEPTH_COMPONENT32, GL41C.GL_DEPTH_COMPONENT);
        FORMATS.put(GL41C.GL_DEPTH_COMPONENT24, GL41C.GL_DEPTH_COMPONENT);
        FORMATS.put(GL41C.GL_DEPTH_COMPONENT16, GL41C.GL_DEPTH_COMPONENT);
        FORMATS.put(GL41C.GL_DEPTH32F_STENCIL8, GL41C.GL_DEPTH_STENCIL);
        FORMATS.put(GL41C.GL_DEPTH24_STENCIL8, GL41C.GL_DEPTH_STENCIL);

        TYPES.put(GL41C.GL_RGBA8, GL41C.GL_UNSIGNED_BYTE);
        TYPES.put(GL41C.GL_RGB8, GL41C.GL_UNSIGNED_BYTE);
        TYPES.put(GL41C.GL_RG8, GL41C.GL_UNSIGNED_BYTE);
        TYPES.put(GL41C.GL_R8, GL41C.GL_UNSIGNED_BYTE);
        TYPES.put(GL41C.GL_DEPTH_COMPONENT32F, GL41C.GL_FLOAT);
        TYPES.put(GL41C.GL_DEPTH_COMPONENT32, GL41C.GL_UNSIGNED_INT);
        TYPES.put(GL41C.GL_DEPTH_COMPONENT24, GL41C.GL_UNSIGNED_INT);
        TYPES.put(GL41C.GL_DEPTH_COMPONENT16, GL41C.GL_UNSIGNED_INT);
        TYPES.put(GL41C.GL_DEPTH32F_STENCIL8, GL41C.GL_UNSIGNED_INT_24_8);
        TYPES.put(GL41C.GL_DEPTH24_STENCIL8, GL41C.GL_UNSIGNED_INT_24_8);
    }

    private final Int2IntMap textureTargets = new Int2IntOpenHashMap();
    private final Int2ObjectMap<VAOState> vertexArrayStates = new Int2ObjectOpenHashMap<>();

    @Override
    public int glCreateBuffers() {
        return GL41C.glGenBuffers();
    }

    @Override
    public int glCreateFramebuffers() {
        return GL41C.glGenFramebuffers();
    }

    @Override
    public int glCreateVertexArrays() {
        final int vertexArrayObject = GL41C.glGenVertexArrays();
        vertexArrayStates.put(vertexArrayObject, new VAOState());
        return vertexArrayObject;
    }

    @Override
    public int glCreateRenderbuffers() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int glCreateQueries(int target) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void glDeleteVertexArrays(int array) {
        GL41C.glDeleteVertexArrays(array);
        vertexArrayStates.remove(array);
    }

    @Override
    public int glCreateTextures(int target) {
        final int texture = GL41C.glGenTextures();
        textureTargets.put(texture, target);
        return texture;
    }

    @Override
    public void glDeleteTextures(int texture) {
        GL41C.glDeleteTextures(texture);
        textureTargets.remove(texture);
    }

    @Override
    public boolean glUnmapNamedBuffer(int buffer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void glNamedBufferStorage(int buffer, long size, int flags) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void glNamedBufferStorage(int buffer, ByteBuffer data, int flags) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void glNamedRenderbufferStorageMultisample(int renderbuffer, int samples, int internalformat, int width, int height) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void glNamedRenderbufferStorage(int renderbuffer, int internalformat, int width, int height) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void glNamedBufferSubData(int buffer, long offset, ByteBuffer data) {
        final int prevBuffer = GL41C.glGetInteger(GL41C.GL_COPY_WRITE_BUFFER);
        GL41C.glBindBuffer(GL41C.GL_COPY_WRITE_BUFFER, buffer);
        GL41C.glBufferSubData(GL41C.GL_COPY_WRITE_BUFFER, offset, data);
        GL41C.glBindBuffer(GL41C.GL_COPY_WRITE_BUFFER, prevBuffer);
    }

    @Override
    public void glNamedBufferData(int buffer, long size, int usage) {
        final int prevBuffer = GL41C.glGetInteger(GL41C.GL_COPY_WRITE_BUFFER);
        GL41C.glBindBuffer(GL41C.GL_COPY_WRITE_BUFFER, buffer);
        GL41C.glBufferData(GL41C.GL_COPY_WRITE_BUFFER, size, usage);
        GL41C.glBindBuffer(GL41C.GL_COPY_WRITE_BUFFER, prevBuffer);
    }

    @Override
    public void glNamedBufferData(int buffer, ByteBuffer data, int usage) {
        final int prevBuffer = GL41C.glGetInteger(GL41C.GL_COPY_WRITE_BUFFER);
        GL41C.glBindBuffer(GL41C.GL_COPY_WRITE_BUFFER, buffer);
        GL41C.glBufferData(GL41C.GL_COPY_WRITE_BUFFER, data, usage);
        GL41C.glBindBuffer(GL41C.GL_COPY_WRITE_BUFFER, prevBuffer);
    }

    @Override
    public void glGetNamedBufferSubData(int buffer, long offset, ByteBuffer data) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int glGetNamedBufferParameteri(int buffer, int pname) {
        final int prevBuffer = GL41C.glGetInteger(GL41C.GL_COPY_READ_BUFFER);
        GL41C.glBindBuffer(GL41C.GL_COPY_READ_BUFFER, buffer);
        final int[] params = new int[1];
        GL41C.glGetBufferParameteriv(GL41C.GL_COPY_READ_BUFFER, pname, params);
        GL41C.glBindBuffer(GL41C.GL_COPY_READ_BUFFER, prevBuffer);
        return params[0];
    }

    @Override
    public ByteBuffer glMapNamedBuffer(int buffer, int access) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ByteBuffer glMapNamedBufferRange(int buffer, long offset, long length, int access) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void glFlushMappedNamedBufferRange(int buffer, long offset, long length) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long glGetNamedBufferParameteri64(int buffer, int pname) {
        final int prevBuffer = GL41C.glGetInteger(GL41C.GL_COPY_READ_BUFFER);
        GL41C.glBindBuffer(GL41C.GL_COPY_READ_BUFFER, buffer);
        final long[] params = new long[1];
        GL41C.glGetBufferParameteri64v(GL41C.GL_COPY_READ_BUFFER, pname, params);
        GL41C.glBindBuffer(GL41C.GL_COPY_READ_BUFFER, prevBuffer);
        return params[0];
    }

    @Override
    public void glCopyNamedBufferSubData(int readBuffer, int writeBuffer, long readOffset, long writeOffset, long size) {
        final int prevReadBuffer = GL41C.glGetInteger(GL41C.GL_COPY_READ_BUFFER);
        final int prevWriteBuffer = GL41C.glGetInteger(GL41C.GL_COPY_WRITE_BUFFER);
        GL41C.glBindBuffer(GL41C.GL_COPY_READ_BUFFER, readBuffer);
        GL41C.glBindBuffer(GL41C.GL_COPY_WRITE_BUFFER, writeBuffer);
        GL41C.glCopyBufferSubData(GL41C.GL_COPY_READ_BUFFER, GL41C.GL_COPY_WRITE_BUFFER, readOffset, writeOffset, size);
        GL41C.glBindBuffer(GL41C.GL_COPY_READ_BUFFER, prevReadBuffer);
        GL41C.glBindBuffer(GL41C.GL_COPY_WRITE_BUFFER, prevWriteBuffer);
    }

    @Override
    public int glCheckNamedFramebufferStatus(int framebuffer, int target) {
        final int prevFramebuffer = GL41C.glGetInteger(GL41C.GL_DRAW_FRAMEBUFFER_BINDING);
        GL41C.glBindFramebuffer(target, framebuffer);
        final int status = GL41C.glCheckFramebufferStatus(target);
        GL41C.glBindFramebuffer(target, prevFramebuffer);
        return status;
    }

    @Override
    public void glClearNamedFramebufferfv(int framebuffer, int buffer, int drawbuffer, float[] value) {
        final int prevFramebuffer = GL41C.glGetInteger(GL41C.GL_DRAW_FRAMEBUFFER_BINDING);
        GL41C.glBindFramebuffer(GL41C.GL_FRAMEBUFFER, framebuffer);
        GL41C.glClearBufferfv(buffer, drawbuffer, value);
        GL41C.glBindFramebuffer(GL41C.GL_FRAMEBUFFER, prevFramebuffer);
    }

    @Override
    public void glClearNamedFramebufferfi(int framebuffer, int buffer, int drawbuffer, float depth, int stencil) {
        final int prevFramebuffer = GL41C.glGetInteger(GL41C.GL_DRAW_FRAMEBUFFER_BINDING);
        GL41C.glBindFramebuffer(GL41C.GL_FRAMEBUFFER, framebuffer);
        GL41C.glClearBufferfi(buffer, drawbuffer, depth, stencil);
        GL41C.glBindFramebuffer(GL41C.GL_FRAMEBUFFER, prevFramebuffer);
    }

    @Override
    public void glClearNamedFramebufferiv(int framebuffer, int buffer, int drawbuffer, int[] value) {
        final int prevFramebuffer = GL41C.glGetInteger(GL41C.GL_DRAW_FRAMEBUFFER_BINDING);
        GL41C.glBindFramebuffer(GL41C.GL_FRAMEBUFFER, framebuffer);
        GL41C.glClearBufferiv(buffer, drawbuffer, value);
        GL41C.glBindFramebuffer(GL41C.GL_FRAMEBUFFER, prevFramebuffer);
    }

    @Override
    public void glBlitNamedFramebuffer(int readFramebuffer, int drawFramebuffer, int srcX0, int srcY0, int srcX1, int srcY1, int dstX0, int dstY0, int dstX1, int dstY1, int mask, int filter) {
        final int prevReadFramebuffer = GL41C.glGetInteger(GL41C.GL_READ_FRAMEBUFFER_BINDING);
        final int prevDrawFramebuffer = GL41C.glGetInteger(GL41C.GL_DRAW_FRAMEBUFFER_BINDING);
        GL41C.glBindFramebuffer(GL41C.GL_READ_FRAMEBUFFER, readFramebuffer);
        GL41C.glBindFramebuffer(GL41C.GL_DRAW_FRAMEBUFFER, drawFramebuffer);
        GL41C.glBlitFramebuffer(srcX0, srcY0, srcX1, srcY1, dstX0, dstY0, dstX1, dstY1, mask, filter);
        GL41C.glBindFramebuffer(GL41C.GL_READ_FRAMEBUFFER, prevReadFramebuffer);
        GL41C.glBindFramebuffer(GL41C.GL_DRAW_FRAMEBUFFER, prevDrawFramebuffer);
    }

    @Override
    public void glNamedFramebufferTexture(int framebuffer, int attachment, int texture, int level) {
        final int prevFramebuffer = GL41C.glGetInteger(GL41C.GL_DRAW_FRAMEBUFFER_BINDING);
        GL41C.glBindFramebuffer(GL41C.GL_FRAMEBUFFER, framebuffer);
        GL41C.glFramebufferTexture2D(GL41C.GL_FRAMEBUFFER, attachment, getTextureTarget(texture), texture, level);
        GL41C.glBindFramebuffer(GL41C.GL_FRAMEBUFFER, prevFramebuffer);
    }

    @Override
    public void glInvalidateNamedFramebufferData(int framebuffer, int[] attachments) {
        // No-op, not relevant beside optimization
    }

    @Override
    public int glGetNamedFramebufferAttachmentParameteri(int framebuffer, int attachment, int pname) {
        final int prevFramebuffer = GL41C.glGetInteger(GL41C.GL_DRAW_FRAMEBUFFER_BINDING);
        GL41C.glBindFramebuffer(GL41C.GL_FRAMEBUFFER, framebuffer);
        final int param = GL41C.glGetFramebufferAttachmentParameteri(GL41C.GL_FRAMEBUFFER, attachment, pname);
        GL41C.glBindFramebuffer(GL41C.GL_FRAMEBUFFER, prevFramebuffer);
        return param;
    }

    @Override
    public int glGetNamedRenderbufferParameteri(int renderbuffer, int pname) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void glNamedFramebufferRenderbuffer(int framebuffer, int attachment, int renderbuffertarget, int renderbuffer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void glGenerateTextureMipmap(int texture) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void glBindTextureUnit(int unit, int texture) {
        final int prevTexture = GL41C.glGetInteger(GL41C.GL_ACTIVE_TEXTURE);
        GL41C.glActiveTexture(GL41C.GL_TEXTURE0 + unit);
        if (texture == 0) {
            unbindAllTextureTargets();
        } else {
            GL41C.glBindTexture(getTextureTarget(texture), texture);
        }
        GL41C.glActiveTexture(prevTexture);
    }

    @Override
    public void glBindTextures(int first, int[] textures) {
        final int prevTexture = GL41C.glGetInteger(GL41C.GL_ACTIVE_TEXTURE);
        for (int i = 0; i < textures.length; i++) {
            GL41C.glActiveTexture(GL41C.GL_TEXTURE0 + first + i);
            if (textures[i] == 0) {
                unbindAllTextureTargets();
            } else {
                GL41C.glBindTexture(getTextureTarget(textures[i]), textures[i]);
            }
        }
        GL41C.glActiveTexture(prevTexture);
    }

    @Override
    public void glTextureStorage2DMultisample(int texture, int samples, int internalformat, int width, int height, boolean fixedsamplelocations) {
        final int target = getOrThrowTextureTarget(texture);
        final int prevTexture = GL41C.glGetInteger(TEXTURE_QUERY_TARGETS.get(target));
        GL41C.glBindTexture(target, texture);
        GL41C.glTexImage2DMultisample(target, samples, internalformat, width, height, fixedsamplelocations);
        GL41C.glBindTexture(target, prevTexture);
    }

    @Override
    public void glTextureStorage2D(int texture, int levels, int internalformat, int width, int height) {
        final int target = getOrThrowTextureTarget(texture);
        final int prevTexture = GL41C.glGetInteger(TEXTURE_QUERY_TARGETS.get(target));
        GL41C.glBindTexture(target, texture);
        GL41C.glTexImage2D(target, 0, internalformat, width, height, 0, FORMATS.get(internalformat), TYPES.get(internalformat), (ByteBuffer) null);
        GL41C.glBindTexture(target, prevTexture);
    }

    @Override
    public void glTextureSubImage2D(int texture, int level, int xoffset, int yoffset, int width, int height, int format, int type, ByteBuffer pixels) {
        final int target = getOrThrowTextureTarget(texture);
        final int prevTexture = GL41C.glGetInteger(TEXTURE_QUERY_TARGETS.get(target));
        GL41C.glBindTexture(target, texture);
        GL41C.glTexSubImage2D(target, level, xoffset, yoffset, width, height, format, type, pixels);
        GL41C.glBindTexture(target, prevTexture);
    }

    @Override
    public void glTextureSubImage3D(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, ByteBuffer pixels) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void glTextureSubImage1D(int texture, int level, int xoffset, int width, int format, int type, ByteBuffer pixels) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void glGetTextureSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, ByteBuffer pixels) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void glClearTexImage(int texture, int level, int format, int type, float[] data) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void glClearTexImage(int texture, int level, int format, int type, ByteBuffer data) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void glClearTexSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, float[] data) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void glClearTexSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, ByteBuffer data) {
        throw new UnsupportedOperationException();
    }

    @Override
    public float glGetTextureParameterf(int texture, int pname) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void glTextureParameterf(int texture, int pname, float param) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void glGetTextureParameteriv(int texture, int pname, int[] params) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void glTextureParameteriv(int texture, int pname, int[] params) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void glGetTextureParameterfv(int texture, int pname, float[] params) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void glTextureParameterfv(int texture, int pname, float[] params) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void glTextureParameteri(int texture, int pname, int param) {
        final int target = getOrThrowTextureTarget(texture);
        final int prevTexture = GL41C.glGetInteger(TEXTURE_QUERY_TARGETS.get(target));
        GL41C.glBindTexture(target, texture);
        GL41C.glTexParameteri(target, pname, param);
        GL41C.glBindTexture(target, prevTexture);
    }

    @Override
    public int glGetTextureLevelParameteri(int texture, int level, int pname) {
        final int target = getOrThrowTextureTarget(texture);
        final int prevTexture = GL41C.glGetInteger(TEXTURE_QUERY_TARGETS.get(target));
        GL41C.glBindTexture(target, texture);
        final int param = GL41C.glGetTexLevelParameteri(target, level, pname);
        GL41C.glBindTexture(target, prevTexture);
        return param;
    }

    @Override
    public int glGetTextureParameteri(int texture, int pname) {
        if (pname == GL43C.GL_TEXTURE_IMMUTABLE_LEVELS) {
            return 1; // OpenGL 4.1 does not support immutable textures, so we return 1 as a default value.
        }

        final int target = getOrThrowTextureTarget(texture);
        final int prevTexture = GL41C.glGetInteger(TEXTURE_QUERY_TARGETS.get(target));
        GL41C.glBindTexture(target, texture);
        final int param = GL41C.glGetTexParameteri(target, pname);
        GL41C.glBindTexture(target, prevTexture);
        return param;
    }

    @Override
    public void glTextureBuffer(int texture, int internalformat, int buffer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void glTextureStorage3D(int texture, int levels, int internalformat, int width, int height, int depth) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void glTextureStorage3DMultisample(int texture, int samples, int internalformat, int width, int height, int depth, boolean fixedsamplelocations) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void glTextureStorage1D(int texture, int levels, int internalformat, int width) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void glVertexArrayVertexBuffer(int vaobj, int bindingindex, int buffer, long offset, int stride) {
        VAOState state = vertexArrayStates.get(vaobj);
        state.vertexBindings.put(bindingindex, new VertexBinding(buffer, offset, stride));
        final int prevVertexArray = GL41C.glGetInteger(GL41C.GL_VERTEX_ARRAY_BINDING);
        bindVAO(vaobj);

        for (Map.Entry<Integer, VertexAttrib> entry : state.attributes.entrySet()) {
            if (entry.getValue().bindingIndex == bindingindex) {
                tryApplyAttribPointer(vaobj, entry.getKey());
            }
        }
        GL41C.glBindVertexArray(prevVertexArray);
    }

    @Override
    public void glVertexArrayElementBuffer(int vaobj, int buffer) {
        final int prevVertexArray = GL41C.glGetInteger(GL41C.GL_VERTEX_ARRAY_BINDING);
        bindVAO(vaobj);
        vertexArrayStates.get(vaobj).elementArrayBuffer = buffer;
        GL41C.glBindVertexArray(vaobj);
        GL41C.glBindBuffer(GL41C.GL_ELEMENT_ARRAY_BUFFER, buffer);
        GL41C.glBindVertexArray(prevVertexArray);
    }

    @Override
    public void glVertexArrayAttribFormat(int vaobj, int attribindex, int size, int type, boolean normalized, int relativeoffset) {
        VertexAttrib attrib = vertexArrayStates.get(vaobj).getAttrib(attribindex);
        attrib.format = new AttribFormat(size, type, normalized, relativeoffset);
        attrib.iformat = null;
        attrib.lformat = null;
        tryApplyAttribPointer(vaobj, attribindex);
    }

    @Override
    public void glVertexArrayAttribIFormat(int vaobj, int attribindex, int size, int type, int relativeoffset) {
        VertexAttrib attrib = vertexArrayStates.get(vaobj).getAttrib(attribindex);
        attrib.format = null;
        attrib.iformat = new AttribIFormat(size, type, relativeoffset);
        attrib.lformat = null;
        tryApplyAttribPointer(vaobj, attribindex);
    }

    @Override
    public void glVertexArrayAttribLFormat(int vaobj, int attribindex, int size, int type, int relativeoffset) {
        VertexAttrib attrib = vertexArrayStates.get(vaobj).getAttrib(attribindex);
        attrib.format = null;
        attrib.iformat = null;
        attrib.lformat = new AttribLFormat(size, type, relativeoffset);
        tryApplyAttribPointer(vaobj, attribindex);
    }

    @Override
    public void glVertexArrayAttribBinding(int vaobj, int attribindex, int bindingindex) {
        vertexArrayStates.get(vaobj).getAttrib(attribindex).bindingIndex = bindingindex;
        tryApplyAttribPointer(vaobj, attribindex);
    }

    @Override
    public void glEnableVertexArrayAttrib(int vaobj, int index) {
        vertexArrayStates.get(vaobj).enabledAttributes.add(index);
        final int prevVertexArray = GL41C.glGetInteger(GL41C.GL_VERTEX_ARRAY_BINDING);
        bindVAO(vaobj);
        GL41C.glEnableVertexAttribArray(index);
        GL41C.glBindVertexArray(prevVertexArray);
    }

    @Override
    public void glVertexArrayBindingDivisor(int vaobj, int bindingindex, int divisor) {
        vertexArrayStates.get(vaobj).divisors.put(bindingindex, divisor);
        final int prevVertexArray = GL41C.glGetInteger(GL41C.GL_VERTEX_ARRAY_BINDING);
        bindVAO(vaobj);
        for (Map.Entry<Integer, VertexAttrib> entry : vertexArrayStates.get(vaobj).attributes.entrySet()) {
            if (entry.getValue().bindingIndex == bindingindex) {
                GL41C.glVertexAttribDivisor(entry.getKey(), divisor);
            }
        }
        GL41C.glBindVertexArray(prevVertexArray);
    }

    @Override
    public void glDrawArraysInstancedBaseInstance(int mode, int first, int count, int primcount, int baseinstance) {
        if (baseinstance != 0) {
            throw new UnsupportedOperationException("Base instance is not supported in OpenGL 4.1");
        }

        GL41C.glDrawArraysInstanced(mode, first, count, primcount);
    }

    @Override
    public void glDrawElementsInstancedBaseVertexBaseInstance(int mode, int count, int type, long indices, int primcount, int basevertex, int baseinstance) {
        GL41C.glDrawElementsInstancedBaseVertex(mode, count, type, indices, primcount, basevertex);
    }

    @Override
    public void glMultiDrawArraysIndirect(int mode, long indirect, int drawcount, int stride) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void glMultiDrawElementsIndirect(int mode, int type, long indirect, int drawcount, int stride) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void glCopyImageSubData(int srcName, int srcTarget, int srcLevel, int srcX, int srcY, int srcZ, int dstName, int dstTarget, int dstLevel, int dstX, int dstY, int dstZ, int srcWidth, int srcHeight, int srcDepth) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int glGetProgramResourceIndex(int program, int programInterface, CharSequence name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void glBindImageTexture(int unit, int texture, int level, boolean layered, int layer, int access, int format) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void glShaderStorageBlockBinding(int program, int storageBlockIndex, int storageBlockBinding) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String glGetObjectLabel(int identifier, int name) {
        return "";
    }

    @Override
    public void glObjectLabel(int identifier, int name, CharSequence label) {
    }

    public void drawBuiltBuffer(BuiltBuffer builtBuffer) {
        final DrawMode drawMode = builtBuffer.drawBatch().drawMode();
        final VertexArray vertexArray = builtBuffer.vertexArray();
        final List<DrawCommand> drawCommands = builtBuffer.drawCommands();

        for (DrawCommand drawCommand : drawCommands) {
            if (drawCommand instanceof DrawElementsCommand drawElementsCommand) {
                vertexArray.drawElements(drawMode, drawElementsCommand.vertexCount(), drawElementsCommand.firstIndex(), drawElementsCommand.instanceCount(), drawElementsCommand.baseVertex(), drawElementsCommand.baseInstance());
            } else if (drawCommand instanceof DrawArraysCommand drawArraysCommand) {
                vertexArray.drawArrays(drawMode, drawArraysCommand.vertexCount(), drawArraysCommand.firstVertex(), drawArraysCommand.instanceCount(), drawArraysCommand.baseInstance());
            }
        }
    }

    private void bindVAO(int vao) {
        GL41C.glBindVertexArray(vao);
        int elementBuffer = vertexArrayStates.get(vao).elementArrayBuffer;
        if (elementBuffer != 0) {
            GL41C.glBindBuffer(GL41C.GL_ELEMENT_ARRAY_BUFFER, elementBuffer);
        }
    }

    private void tryApplyAttribPointer(int vaobj, int attribindex) {
        VAOState state = vertexArrayStates.get(vaobj);
        VertexAttrib attrib = state.getAttrib(attribindex);
        VertexBinding binding = state.vertexBindings.get(attrib.bindingIndex);

        if (binding == null) return; // No buffer bound
        if (!attrib.isFullyDefined()) return; // Missing format or binding

        final int prevVertexArray = GL41C.glGetInteger(GL41C.GL_VERTEX_ARRAY_BINDING);
        bindVAO(vaobj);
        GL41C.glBindBuffer(GL41C.GL_ARRAY_BUFFER, binding.buffer);

        if (attrib.format != null) {
            GL41C.glVertexAttribPointer(
                    attribindex,
                    attrib.format.size,
                    attrib.format.type,
                    attrib.format.normalized,
                    binding.stride,
                    binding.offset + attrib.format.relativeoffset
            );
        } else if (attrib.iformat != null) {
            GL41C.glVertexAttribIPointer(
                    attribindex,
                    attrib.iformat.size,
                    attrib.iformat.type,
                    binding.stride,
                    binding.offset + attrib.iformat.relativeoffset
            );
        } else if (attrib.lformat != null) {
            GL41C.glVertexAttribLPointer(
                    attribindex,
                    attrib.lformat.size,
                    attrib.lformat.type,
                    binding.stride,
                    binding.offset + attrib.lformat.relativeoffset
            );
        }

        if (state.enabledAttributes.contains(attribindex)) {
            GL41C.glEnableVertexAttribArray(attribindex);
        }
        GL41C.glBindVertexArray(prevVertexArray);
    }

    private void unbindAllTextureTargets() {
        for (final Integer textureTarget : TEXTURE_QUERY_TARGETS.keySet()) {
            GL41C.glBindTexture(textureTarget, 0);
        }
    }

    private int getTextureTarget(final int texture) {
        final int target = textureTargets.get(texture);
        if (target == 0) {
            return GL41C.GL_TEXTURE_2D;
        }
        return target;
    }

    private int getOrThrowTextureTarget(final int texture) {
        final int target = textureTargets.getOrDefault(texture, -1);
        if (target == -1) {
            throw new IllegalArgumentException("Texture not found: " + texture);
        }
        return target;
    }

    public Int2IntMap getTextureTargets() {
        return textureTargets;
    }

    private static class VAOState {

        Int2ObjectMap<VertexBinding> vertexBindings = new Int2ObjectOpenHashMap<>();
        Int2ObjectMap<VertexAttrib> attributes = new Int2ObjectOpenHashMap<>();
        Int2IntMap divisors = new Int2IntOpenHashMap();

        int elementArrayBuffer = 0;
        Set<Integer> enabledAttributes = new HashSet<>();

        VertexAttrib getAttrib(int index) {
            return attributes.computeIfAbsent(index, k -> new VertexAttrib());
        }
    }

    private static class VertexBinding {
        int buffer;
        long offset;
        int stride;

        VertexBinding(int buffer, long offset, int stride) {
            this.buffer = buffer;
            this.offset = offset;
            this.stride = stride;
        }
    }

    private static class VertexAttrib {
        int bindingIndex = 0;
        AttribFormat format = null;
        AttribIFormat iformat = null;
        AttribLFormat lformat = null;

        boolean isFullyDefined() {
            return (format != null || iformat != null || lformat != null);
        }
    }

    private static class AttribFormat {
        int size;
        int type;
        boolean normalized;
        int relativeoffset;

        AttribFormat(int size, int type, boolean normalized, int relativeoffset) {
            this.size = size;
            this.type = type;
            this.normalized = normalized;
            this.relativeoffset = relativeoffset;
        }
    }

    private static class AttribIFormat {
        int size;
        int type;
        int relativeoffset;

        AttribIFormat(int size, int type, int relativeoffset) {
            this.size = size;
            this.type = type;
            this.relativeoffset = relativeoffset;
        }
    }

    private static class AttribLFormat {
        int size;
        int type;
        int relativeoffset;

        AttribLFormat(int size, int type, int relativeoffset) {
            this.size = size;
            this.type = type;
            this.relativeoffset = relativeoffset;
        }
    }

}
