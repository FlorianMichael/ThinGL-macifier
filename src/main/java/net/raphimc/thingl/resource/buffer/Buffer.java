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
package net.raphimc.thingl.resource.buffer;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.raphimc.thingl.resource.GLObject;
import org.lwjgl.opengl.GL11C;
import org.lwjgl.opengl.GL15C;
import org.lwjgl.opengl.GL43C;
import org.lwjgl.opengl.GL45C;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;

public abstract class Buffer extends GLObject {

    protected final Int2ObjectMap<Object> parameters = new Int2ObjectOpenHashMap<>();

    public Buffer() {
        super(de.florianmichael.thingl.GlCommands.get().glCreateBuffers());
    }

    protected Buffer(final int glId) {
        super(glId);
    }

    public static Buffer fromGlId(final int glId) {
        if (!GL15C.glIsBuffer(glId)) {
            throw new IllegalArgumentException("Not a buffer object");
        }
        final boolean immutable = de.florianmichael.thingl.GlCommands.get().glGetNamedBufferParameteri(glId, GL45C.GL_BUFFER_IMMUTABLE_STORAGE) != GL11C.GL_FALSE;
        if (immutable) {
            return new ImmutableBuffer(glId);
        } else {
            return new MutableBuffer(glId);
        }
    }

    public void upload(final ByteBuffer dataBuffer) {
        this.upload(0L, dataBuffer);
    }

    public void upload(final long offset, final ByteBuffer dataBuffer) {
        if (!dataBuffer.isDirect()) {
            throw new IllegalArgumentException("Data buffer must be a direct ByteBuffer");
        }

        de.florianmichael.thingl.GlCommands.get().glNamedBufferSubData(this.getGlId(), offset, dataBuffer);
    }

    public ByteBuffer download() {
        return this.download(0L, this.getSize());
    }

    public ByteBuffer download(final long offset, final long length) {
        final ByteBuffer dataBuffer = MemoryUtil.memAlloc((int) length);
        de.florianmichael.thingl.GlCommands.get().glGetNamedBufferSubData(this.getGlId(), offset, dataBuffer);
        return dataBuffer;
    }

    public void copyTo(final Buffer target, final long readOffset, final long writeOffset, final long length) {
        de.florianmichael.thingl.GlCommands.get().glCopyNamedBufferSubData(this.getGlId(), target.getGlId(), readOffset, writeOffset, length);
    }

    public ByteBuffer map(final int access) {
        this.parameters.clear();
        return de.florianmichael.thingl.GlCommands.get().glMapNamedBuffer(this.getGlId(), access);
    }

    public ByteBuffer mapFullRange(final int accessFlags) {
        return this.mapRange(0L, this.getSize(), accessFlags);
    }

    public ByteBuffer mapRange(final long offset, final long length, final int accessFlags) {
        this.parameters.clear();
        return de.florianmichael.thingl.GlCommands.get().glMapNamedBufferRange(this.getGlId(), offset, length, accessFlags);
    }

    public void flush(final long offset, final long length) {
        de.florianmichael.thingl.GlCommands.get().glFlushMappedNamedBufferRange(this.getGlId(), offset, length);
    }

    public void unmap() {
        this.parameters.clear();
        de.florianmichael.thingl.GlCommands.get().glUnmapNamedBuffer(this.getGlId());
    }

    @Override
    protected void free0() {
        GL15C.glDeleteBuffers(this.getGlId());
    }

    @Override
    public final int getGlType() {
        return GL43C.GL_BUFFER;
    }

    public int getParameterInt(final int parameter) {
        Object value = this.parameters.get(parameter);
        if (!(value instanceof Integer)) {
            value = de.florianmichael.thingl.GlCommands.get().glGetNamedBufferParameteri(this.getGlId(), parameter);
            this.parameters.put(parameter, value);
        }
        return (int) value;
    }

    public long getParameterLong(final int parameter) {
        Object value = this.parameters.get(parameter);
        if (!(value instanceof Long)) {
            value = de.florianmichael.thingl.GlCommands.get().glGetNamedBufferParameteri64(this.getGlId(), parameter);
            this.parameters.put(parameter, value);
        }
        return (long) value;
    }

    public long getSize() {
        return this.getParameterLong(GL15C.GL_BUFFER_SIZE);
    }

}
