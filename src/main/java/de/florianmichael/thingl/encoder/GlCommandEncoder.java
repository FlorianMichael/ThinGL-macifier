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

    default int glCreateRenderbuffers() {
        return GL45C.glCreateRenderbuffers();
    }

    default int glCreateQueries(int target) {
        return GL45C.glCreateQueries(target);
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

    default boolean glUnmapNamedBuffer(int buffer) {
        return GL45C.glUnmapNamedBuffer(buffer);
    }

    default void glNamedBufferStorage(int buffer, long size, int flags) {
        GL45C.glNamedBufferStorage(buffer, size, flags);
    }

    default void glNamedBufferStorage(int buffer, ByteBuffer data, int flags) {
        GL45C.glNamedBufferStorage(buffer, data, flags);
    }

    default void glNamedRenderbufferStorageMultisample(int renderbuffer, int samples, int internalformat, int width, int height) {
        GL45C.glNamedRenderbufferStorageMultisample(renderbuffer, samples, internalformat, width, height);
    }

    default void glNamedRenderbufferStorage(int renderbuffer, int internalformat, int width, int height) {
        GL45C.glNamedRenderbufferStorage(renderbuffer, internalformat, width, height);
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

    default void glGetNamedBufferSubData(int buffer, long offset, ByteBuffer data) {
        GL45C.glGetNamedBufferSubData(buffer, offset, data);
    }

    default int glGetNamedBufferParameteri(int buffer, int pname) {
        return GL45C.glGetNamedBufferParameteri(buffer, pname);
    }

    default ByteBuffer glMapNamedBuffer(int buffer, int access) {
        return GL45C.glMapNamedBuffer(buffer, access);
    }

    default ByteBuffer glMapNamedBufferRange(int buffer, long offset, long length, int access) {
        return GL45C.glMapNamedBufferRange(buffer, offset, length, access);
    }

    default void glFlushMappedNamedBufferRange(int buffer, long offset, long length) {
        GL45C.glFlushMappedNamedBufferRange(buffer, offset, length);
    }

    default long glGetNamedBufferParameteri64(int buffer, int pname) {
        return GL45C.glGetNamedBufferParameteri64(buffer, pname);
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

    default int glGetNamedRenderbufferParameteri(int renderbuffer, int pname) {
        return GL45C.glGetNamedRenderbufferParameteri(renderbuffer, pname);
    }

    default void glNamedFramebufferRenderbuffer(int framebuffer, int attachment, int renderbuffertarget, int renderbuffer) {
        GL45C.glNamedFramebufferRenderbuffer(framebuffer, attachment, renderbuffertarget, renderbuffer);
    }

    default void glGenerateTextureMipmap(int texture) {
        GL45C.glGenerateTextureMipmap(texture);
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

    default void glTextureSubImage3D(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, ByteBuffer pixels) {
        GL45C.glTextureSubImage3D(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels);
    }

    default void glTextureSubImage1D(int texture, int level, int xoffset, int width, int format, int type, ByteBuffer pixels) {
        GL45C.glTextureSubImage1D(texture, level, xoffset, width, format, type, pixels);
    }

    default void glGetTextureSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, ByteBuffer pixels) {
        GL45C.glGetTextureSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, pixels);
    }

    default void glClearTexImage(int texture, int level, int format, int type, float[] data) {
        GL45C.glClearTexImage(texture, level, format, type, data);
    }

    default void glClearTexImage(int texture, int level, int format, int type, ByteBuffer data) {
        GL45C.glClearTexImage(texture, level, format, type, data);
    }

    default void glClearTexSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, float[] data) {
        GL45C.glClearTexSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, data);
    }

    default void glClearTexSubImage(int texture, int level, int xoffset, int yoffset, int zoffset, int width, int height, int depth, int format, int type, ByteBuffer data) {
        GL45C.glClearTexSubImage(texture, level, xoffset, yoffset, zoffset, width, height, depth, format, type, data);
    }

    default float glGetTextureParameterf(int texture, int pname) {
        return GL45C.glGetTextureParameterf(texture, pname);
    }

    default void glTextureParameterf(int texture, int pname, float param) {
        GL45C.glTextureParameterf(texture, pname, param);
    }

    default void glGetTextureParameteriv(int texture, int pname, int[] params) {
        GL45C.glGetTextureParameteriv(texture, pname, params);
    }

    default void glTextureParameteriv(int texture, int pname, int[] params) {
        GL45C.glTextureParameteriv(texture, pname, params);
    }

    default void glGetTextureParameterfv(int texture, int pname, float[] params) {
        GL45C.glGetTextureParameterfv(texture, pname, params);
    }

    default void glTextureParameterfv(int texture, int pname, float[] params) {
        GL45C.glTextureParameterfv(texture, pname, params);
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

    default void glTextureBuffer(int texture, int internalformat, int buffer) {
        GL45C.glTextureBuffer(texture, internalformat, buffer);
    }

    default void glTextureStorage3D(int texture, int levels, int internalformat, int width, int height, int depth) {
        GL45C.glTextureStorage3D(texture, levels, internalformat, width, height, depth);
    }

    default void glTextureStorage3DMultisample(int texture, int samples, int internalformat, int width, int height, int depth, boolean fixedsamplelocations) {
        GL45C.glTextureStorage3DMultisample(texture, samples, internalformat, width, height, depth, fixedsamplelocations);
    }

    default void glTextureStorage1D(int texture, int levels, int internalformat, int width) {
        GL45C.glTextureStorage1D(texture, levels, internalformat, width);
    }

    default void glVertexArrayVertexBuffer(int vaobj, int bindingindex, int buffer, long offset, int stride) {
        GL45C.glVertexArrayVertexBuffer(vaobj, bindingindex, buffer, offset, stride);
    }

    default void glVertexArrayElementBuffer(int vaobj, int buffer) {
        GL45C.glVertexArrayElementBuffer(vaobj, buffer);
    }

    default void glVertexArrayAttribFormat(int vaobj, int attribindex, int size, int type, boolean normalized, int relativeoffset) {
        GL45C.glVertexArrayAttribFormat(vaobj, attribindex, size, type, normalized, relativeoffset);
    }

    default void glVertexArrayAttribIFormat(int vaobj, int attribindex, int size, int type, int relativeoffset) {
        GL45C.glVertexArrayAttribIFormat(vaobj, attribindex, size, type, relativeoffset);
    }

    default void glVertexArrayAttribLFormat(int vaobj, int attribindex, int size, int type, int relativeoffset) {
        GL45C.glVertexArrayAttribLFormat(vaobj, attribindex, size, type, relativeoffset);
    }

    default void glVertexArrayAttribBinding(int vaobj, int attribindex, int bindingindex) {
        GL45C.glVertexArrayAttribBinding(vaobj, attribindex, bindingindex);
    }

    default void glEnableVertexArrayAttrib(int vaobj, int index) {
        GL45C.glEnableVertexArrayAttrib(vaobj, index);
    }

    default void glVertexArrayBindingDivisor(int vaobj, int bindingindex, int divisor) {
        GL45C.glVertexArrayBindingDivisor(vaobj, bindingindex, divisor);
    }

    default void glDrawArraysInstancedBaseInstance(int mode, int first, int count, int primcount, int baseinstance) {
        GL45C.glDrawArraysInstancedBaseInstance(mode, first, count, primcount, baseinstance);
    }

    default void glDrawElementsInstancedBaseVertexBaseInstance(int mode, int count, int type, long indices, int primcount,int basevertex, int baseinstance) {
        GL45C.glDrawElementsInstancedBaseVertexBaseInstance(mode, count, type, indices, primcount, basevertex, baseinstance);
    }

    default void glMultiDrawArraysIndirect(int mode, long indirect, int drawcount, int stride) {
        GL45C.glMultiDrawArraysIndirect(mode, indirect, drawcount, stride);
    }

    default void glMultiDrawElementsIndirect(int mode, int type, long indirect, int drawcount,  int stride) {
        GL45C.glMultiDrawElementsIndirect(mode, type, indirect, drawcount, stride);
    }

    default void glCopyImageSubData(int srcName, int srcTarget, int srcLevel, int srcX, int srcY, int srcZ, int dstName, int dstTarget, int dstLevel, int dstX, int dstY, int dstZ, int srcWidth, int srcHeight, int srcDepth) {
        GL45C.glCopyImageSubData(srcName, srcTarget, srcLevel, srcX, srcY, srcZ, dstName, dstTarget, dstLevel, dstX, dstY, dstZ, srcWidth, srcHeight, srcDepth);
    }

    default int glGetProgramResourceIndex(int program, int programInterface, CharSequence name) {
        return GL45C.glGetProgramResourceIndex(program, programInterface, name);
    }

    default void glBindImageTexture(int unit, int texture, int level, boolean layered, int layer, int access, int format) {
        GL45C.glBindImageTexture(unit, texture, level, layered, layer, access, format);
    }

    default void glShaderStorageBlockBinding(int program, int storageBlockIndex, int storageBlockBinding) {
        GL45C.glShaderStorageBlockBinding(program, storageBlockIndex, storageBlockBinding);
    }

    default String glGetObjectLabel(int identifier, int name) {
        return GL45C.glGetObjectLabel(identifier, name);
    }

    default void glObjectLabel(int identifier, int name, CharSequence label) {
        GL45C.glObjectLabel(identifier, name, label);
    }

}