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
package net.raphimc.thingl.resource.vertexarray;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.raphimc.thingl.ThinGL;
import net.raphimc.thingl.drawbuilder.DrawMode;
import net.raphimc.thingl.drawbuilder.vertex.VertexDataLayout;
import net.raphimc.thingl.drawbuilder.vertex.VertexDataLayoutElement;
import net.raphimc.thingl.resource.GLContainerObject;
import net.raphimc.thingl.resource.buffer.Buffer;
import org.lwjgl.opengl.*;

public class VertexArray extends GLContainerObject {

    private final Int2ObjectMap<Buffer> vertexBuffers = new Int2ObjectOpenHashMap<>();
    private int indexType;
    private Buffer indexBuffer;

    public VertexArray() {
        super(de.florianmichael.thingl.GlCommands.get().glCreateVertexArrays()); // FlorianMichael - add macOS support
    }

    protected VertexArray(final int glId) {
        super(glId);
    }

    public static VertexArray fromGlId(final int glId) {
        if (!GL30C.glIsVertexArray(glId)) {
            throw new IllegalArgumentException("Not a vertex array object");
        }
        return fromGlIdUnsafe(glId);
    }

    public static VertexArray fromGlIdUnsafe(final int glId) {
        return new VertexArray(glId);
    }

    public void setVertexBuffer(final int bindingIndex, final Buffer buffer, final long offset, final int stride) {
        if (buffer != null) {
            this.vertexBuffers.put(bindingIndex, buffer);
            de.florianmichael.thingl.GlCommands.get().glVertexArrayVertexBuffer(this.getGlId(), bindingIndex, buffer.getGlId(), offset, stride); // FlorianMichael - add macOS support
        } else {
            this.vertexBuffers.remove(bindingIndex);
            de.florianmichael.thingl.GlCommands.get().glVertexArrayVertexBuffer(this.getGlId(), bindingIndex, 0, 0, 0); // FlorianMichael - add macOS support
        }
    }

    public void setIndexBuffer(final int indexType, final Buffer buffer) {
        this.indexType = indexType;
        this.indexBuffer = buffer;
        if (buffer != null) {
            de.florianmichael.thingl.GlCommands.get().glVertexArrayElementBuffer(this.getGlId(), buffer.getGlId()); // FlorianMichael - add macOS support
        } else {
            if (!ThinGL.workarounds().isDsaVertexArrayElementBufferUnbindBroken()) {
                de.florianmichael.thingl.GlCommands.get().glVertexArrayElementBuffer(this.getGlId(), 0); // FlorianMichael - add macOS support
            } else {
                this.bind();
                GL15C.glBindBuffer(GL15C.GL_ELEMENT_ARRAY_BUFFER, 0);
                this.unbind();
            }
        }
    }

    public void configureVertexDataLayout(final int bindingIndex, final int attribOffset, final VertexDataLayout vertexDataLayout, final int divisor) {
        int relativeOffset = 0;
        for (int i = 0; i < vertexDataLayout.getElements().length; i++) {
            final VertexDataLayoutElement element = vertexDataLayout.getElements()[i];
            switch (element.targetDataType()) {
                case INT -> de.florianmichael.thingl.GlCommands.get().glVertexArrayAttribIFormat(this.getGlId(), i + attribOffset, element.count(), element.dataType().getGlType(), relativeOffset);
                case FLOAT -> de.florianmichael.thingl.GlCommands.get().glVertexArrayAttribFormat(this.getGlId(), i + attribOffset, element.count(), element.dataType().getGlType(), false, relativeOffset);
                case FLOAT_NORMALIZED -> de.florianmichael.thingl.GlCommands.get().glVertexArrayAttribFormat(this.getGlId(), i + attribOffset, element.count(), element.dataType().getGlType(), true, relativeOffset);
                case DOUBLE -> de.florianmichael.thingl.GlCommands.get().glVertexArrayAttribLFormat(this.getGlId(), i + attribOffset, element.count(), element.dataType().getGlType(), relativeOffset);
            }
            de.florianmichael.thingl.GlCommands.get().glVertexArrayAttribBinding(this.getGlId(), i + attribOffset, bindingIndex);
            de.florianmichael.thingl.GlCommands.get().glEnableVertexArrayAttrib(this.getGlId(), i + attribOffset);
            relativeOffset += element.count() * element.dataType().getSize() + element.padding();
        }
        de.florianmichael.thingl.GlCommands.get().glVertexArrayBindingDivisor(this.getGlId(), bindingIndex, divisor);
    }

    public void drawArrays(final DrawMode drawMode, final int count, final int offset) {
        this.bind();
        GL11C.glDrawArrays(drawMode.getGlMode(), offset, count);
        this.unbind();
    }

    public void drawArrays(final DrawMode drawMode, final int count, final int offset, final int instanceCount, final int baseInstance) {
        this.bind();
        de.florianmichael.thingl.GlCommands.get().glDrawArraysInstancedBaseInstance(drawMode.getGlMode(), offset, count, instanceCount, baseInstance); // FlorianMichael - add macOS support
        this.unbind();
    }

    public void drawArraysIndirect(final DrawMode drawMode, final Buffer indirectCommandBuffer, final long offset, final int count) {
        this.bind();
        final int prevIndirectCommandBuffer = GL11C.glGetInteger(GL40C.GL_DRAW_INDIRECT_BUFFER_BINDING);
        GL15C.glBindBuffer(GL40C.GL_DRAW_INDIRECT_BUFFER, indirectCommandBuffer.getGlId());
        if (count == 1) {
            GL40C.glDrawArraysIndirect(drawMode.getGlMode(), offset);
        } else {
            de.florianmichael.thingl.GlCommands.get().glMultiDrawArraysIndirect(drawMode.getGlMode(), offset, count, 0);
        }
        GL15C.glBindBuffer(GL40C.GL_DRAW_INDIRECT_BUFFER, prevIndirectCommandBuffer);
        this.unbind();
    }

    public void drawElements(final DrawMode drawMode, final int count, final int offset) {
        this.bind();
        GL11C.glDrawElements(drawMode.getGlMode(), count, this.indexType, offset);
        this.unbind();
    }

    public void drawElements(final DrawMode drawMode, final int count, final int offset, final int instanceCount, final int baseVertex, final int baseInstance) {
        this.bind();
        de.florianmichael.thingl.GlCommands.get().glDrawElementsInstancedBaseVertexBaseInstance(drawMode.getGlMode(), count, this.indexType, offset, instanceCount, baseVertex, baseInstance); // FlorianMichael - add macOS support
        this.unbind();
    }

    public void drawElementsIndirect(final DrawMode drawMode, final Buffer indirectCommandBuffer, final long offset, final int count) {
        this.bind();
        final int prevIndirectCommandBuffer = GL11C.glGetInteger(GL40C.GL_DRAW_INDIRECT_BUFFER_BINDING);
        GL15C.glBindBuffer(GL40C.GL_DRAW_INDIRECT_BUFFER, indirectCommandBuffer.getGlId());
        if (count == 1) {
            GL40C.glDrawElementsIndirect(drawMode.getGlMode(), this.indexType, offset);
        } else {
            de.florianmichael.thingl.GlCommands.get().glMultiDrawElementsIndirect(drawMode.getGlMode(), this.indexType, offset, count, 0);
        }
        GL15C.glBindBuffer(GL40C.GL_DRAW_INDIRECT_BUFFER, prevIndirectCommandBuffer);
        this.unbind();
    }

    @Override
    protected void free0() {
        de.florianmichael.thingl.GlCommands.get().glDeleteVertexArrays(this.getGlId()); // FlorianMichael - add macOS support
    }

    @Override
    protected void freeContainingObjects() {
        for (Buffer buffer : this.vertexBuffers.values()) {
            buffer.free();
        }
        this.vertexBuffers.clear();
        if (this.indexBuffer != null && this.indexBuffer != ThinGL.quadIndexBuffer().getSharedBuffer()) {
            this.indexBuffer.free();
        }
        this.indexBuffer = null;
    }

    @Override
    public final int getGlType() {
        return GL11C.GL_VERTEX_ARRAY;
    }

    public Int2ObjectMap<Buffer> getVertexBuffers() {
        return this.vertexBuffers;
    }

    public int getIndexType() {
        return this.indexType;
    }

    public Buffer getIndexBuffer() {
        return this.indexBuffer;
    }

    private void bind() {
        if (ThinGL.config().restoreVertexArrayBinding()) {
            ThinGL.glStateStack().pushVertexArray();
        }
        ThinGL.glStateManager().setVertexArray(this.getGlId());
    }

    private void unbind() {
        if (ThinGL.config().restoreVertexArrayBinding()) {
            ThinGL.glStateStack().popVertexArray();
        }
    }

}
