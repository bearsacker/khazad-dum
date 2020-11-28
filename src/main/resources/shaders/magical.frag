#version 130

uniform sampler2D texture;
uniform sampler2D overlay;
uniform vec2 dimension;
uniform vec2 position;
uniform vec2 screen_dimension;

void main(void)
{
	vec2 pixel = ivec2(gl_TexCoord[0].st * screen_dimension + position) % ivec2(dimension);

	vec4 color = texture2D(texture, gl_TexCoord[0].st);
	if ((color.r == 0.0 && color.g == 1.0 && color.b == 1.0) || (color.r == 1.0 && color.g == 1.0 && color.b == 0.0)) {
		vec4 overlayColor = texture2D(overlay, pixel / dimension);
		color = vec4(0.75 * color.rgb + overlayColor.rgb, color.a);
	}

	gl_FragColor = color;
}