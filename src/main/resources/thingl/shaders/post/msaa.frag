#version 410 core

uniform sampler2DMS u_Input;

in vec2 v_VpTexCoord;
out vec4 o_Color;

void main() {
    ivec2 texPos = ivec2(v_VpTexCoord * textureSize(u_Input));
    // int samples = textureSamples(u_Input); // FlorianMichael - macOS support

    vec4 colorSum = vec4(0);
    for (int i = 0; i < samples; i++) {
        colorSum += texelFetch(u_Input, texPos, i);
    }

    o_Color = colorSum / samples;
    if (o_Color.a == 0) {
        discard;
    }
}
