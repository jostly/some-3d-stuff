#version 330

in vec4 interpolatedColour;
in vec3 interpolatedNormal;
out vec4 outputColor;

uniform vec3 lightDirection = normalize(vec3(0, -1, -0.2));
uniform vec4 ambientLight = vec4(0.2, 0.2, 0.2, 1);

void main() {
    outputColor = interpolatedColour * clamp(-dot(normalize(interpolatedNormal), lightDirection), 0, 1) + interpolatedColour * ambientLight;
}