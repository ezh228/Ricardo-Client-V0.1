#version 150
in vec2 textureCoords;
out vec4 outColor;
uniform sampler2D colorTexture;

void main( void ) {
	vec4 color = texture2D(colorTexture, textureCoords);	
	float brightness = color.r * .7 + color.g * .7 + color.b * .7;
	outColor = color * brightness;
}