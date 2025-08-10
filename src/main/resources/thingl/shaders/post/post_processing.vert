#version 330 core

uniform mat4 u_ProjectionMatrix;
uniform vec2 u_Viewport;
uniform vec4 u_Quad;

out vec2 v_RelPixelSize;
out vec4 v_RelCoord;
out vec2 v_RelTexCoord;
out vec2 v_VpPixelSize;
out vec4 v_VpCoord;
out vec2 v_VpTexCoord;

vec2 quadValues[6] = vec2[](u_Quad.xy, u_Quad.xw, u_Quad.zy, u_Quad.zy, u_Quad.xw, u_Quad.zw);

void main() {
    vec3 i_Position = vec3(quadValues[gl_VertexID], 0);
    gl_Position = u_ProjectionMatrix * vec4(i_Position, 1);

    vec2 quadSize = u_Quad.zw - u_Quad.xy;

    vec2 relative = i_Position.xy - u_Quad.xy;
    vec4 relativeCoord = u_ProjectionMatrix * vec4(relative / quadSize * u_Viewport, i_Position.z, 1);

    v_RelPixelSize = 1 / quadSize;
    v_RelCoord = vec4(relative.x, quadSize.y - relative.y, i_Position.z, 1);
    v_RelTexCoord = (relativeCoord.xy + 1) / 2;

    v_VpPixelSize = 1 / u_Viewport;
    v_VpCoord = vec4(i_Position.x, u_Viewport.y - i_Position.y, i_Position.z, 1);
    v_VpTexCoord = (gl_Position.xy + 1) / 2;
}
