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

import org.lwjgl.opengl.GL45C;

import java.nio.ByteBuffer;

/**
 * Wrapper interface for GL 4.2+ commands that are not available on Apple software. The methods below are a 1:1 of their GL class equivalents.
 */
public interface GlCommandEncoder {

    default int glCreateBuffers() {
        return GL45C.glCreateBuffers();
    }

    default int glCreateFramebuffers() {
        return GL45C.glCreateFramebuffers();
    }

    default int glCreateVertexArrays() {
        return GL45C.glCreateVertexArrays();
    }

    default void glDeleteVertexArrays(int array) {
        GL45C.glDeleteVertexArrays(array);
    }

    default int glCreateTextures(int target) {
        return GL45C.glCreateTextures(target);
    }

    default void glDeleteTextures(int texture) {
        GL45C.glDeleteTextures(texture);
    }

    default void glNamedBufferSubData(int buffer, long offset, ByteBuffer data) {
        GL45C.glNamedBufferSubData(buffer, offset, data);
    }

    default void glNamedBufferData(int buffer, long size, int usage) {
        GL45C.glNamedBufferData(buffer, size, usage);
    }

    default void glNamedBufferData(int buffer, ByteBuffer data, int usage) {
        GL45C.glNamedBufferData(buffer, data, usage);
    }

    default void glCopyNamedBufferSubData(int readBuffer, int writeBuffer, long readOffset, long writeOffset, long size) {
        GL45C.glCopyNamedBufferSubData(readBuffer, writeBuffer, readOffset, writeOffset, size);
    }

    default int glCheckNamedFramebufferStatus(int framebuffer, int target) {
        return GL45C.glCheckNamedFramebufferStatus(framebuffer, target);
    }

    default void glClearNamedFramebufferfv(int framebuffer, int buffer, int drawbuffer, float[] value) {
        GL45C.glClearNamedFramebufferfv(framebuffer, buffer, drawbuffer, value);
    }

    default void glClearNamedFramebufferfi(int framebuffer, int buffer, int drawbuffer, float depth, int stencil) {
        GL45C.glClearNamedFramebufferfi(framebuffer, buffer, drawbuffer, depth, stencil);
    }

    default void glClearNamedFramebufferiv(int framebuffer, int buffer, int drawbuffer, int[] value) {
        GL45C.glClearNamedFramebufferiv(framebuffer, buffer, drawbuffer, value);
    }

    default void glBlitNamedFramebuffer(int readFramebuffer, int drawFramebuffer, int srcX0, int srcY0, int srcX1, int srcY1, int dstX0, int dstY0, int dstX1, int dstY1, int mask, int filter) {
        GL45C.glBlitNamedFramebuffer(readFramebuffer, drawFramebuffer, srcX0, srcY0, srcX1, srcY1, dstX0, dstY0, dstX1, dstY1, mask, filter);
    }

    default void glNamedFramebufferTexture(int framebuffer, int attachment, int texture, int level) {
        GL45C.glNamedFramebufferTexture(framebuffer, attachment, texture, level);
    }

    default void glInvalidateNamedFramebufferData(int framebuffer, int[] attachments) {
        GL45C.glInvalidateNamedFramebufferData(framebuffer, attachments);
    }

    default int glGetNamedFramebufferAttachmentParameteri(int framebuffer, int attachment, int pname) {
        return GL45C.glGetNamedFramebufferAttachmentParameteri(framebuffer, attachment, pname);
    }

    default void glBindTextureUnit(int unit, int texture) {
        GL45C.glBindTextureUnit(unit, texture);
    }

    default void glBindTextures(int first, int[] textures) {
        GL45C.glBindTextures(first, textures);
    }

    default void glTextureStorage2DMultisample(int texture, int samples, int internalformat, int width, int height, boolean fixedsamplelocations) {
        GL45C.glTextureStorage2DMultisample(texture, samples, internalformat, width, height, fixedsamplelocations);
    }

    default void glTextureStorage2D(int texture, int levels, int internalformat, int width, int height) {
        GL45C.glTextureStorage2D(texture, levels, internalformat, width, height);
    }

    default void glTextureSubImage2D(int texture, int level, int xoffset, int yoffset, int width, int height, int format, int type, ByteBuffer pixels) {
        GL45C.glTextureSubImage2D(texture, level, xoffset, yoffset, width, height, format, type, pixels);
    }

    default void glTextureParameteri(int texture, int pname, int param) {
        GL45C.glTextureParameteri(texture, pname, param);
    }

    default int glGetTextureLevelParameteri(int texture, int level, int pname) {
        return GL45C.glGetTextureLevelParameteri(texture, level, pname);
    }

    default int glGetTextureParameteri(int texture, int pname) {
        return GL45C.glGetTextureParameteri(texture, pname);
    }

    default void glVertexArrayVertexBuffer(int vaobj, int bindingindex, int buffer, long offset, int stride) {
        GL45C.glVertexArrayVertexBuffer(vaobj, bindingindex, buffer, offset, stride);
    }

    default void glVertexArrayElementBuffer(int vaobj, int buffer) {
        GL45C.glVertexArrayElementBuffer(vaobj, buffer);
    }

    default void glDrawArraysInstancedBaseInstance(int mode, int first, int count, int primcount, int baseinstance) {
        GL45C.glDrawArraysInstancedBaseInstance(mode, first, count, primcount, baseinstance);
    }

    default void glDrawElementsInstancedBaseVertexBaseInstance(int mode, int count, int type, long indices, int primcount,int basevertex, int baseinstance) {
        GL45C.glDrawElementsInstancedBaseVertexBaseInstance(mode, count, type, indices, primcount, basevertex, baseinstance);
    }

}