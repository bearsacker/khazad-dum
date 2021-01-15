#version 130

uniform sampler2D texture;
uniform sampler2D overlay;
uniform sampler2D layer;
uniform vec2 dimension;
uniform vec2 position;
uniform vec2 resolution;

vec4 blur5(vec4 color, sampler2D image, vec2 uv, vec2 resolution, vec2 direction, float glow) {
	vec2 off1 = vec2(1.3333333333333333) * direction;
	color += texture2D(image, uv) * glow * 0.29411764705882354;
	color += texture2D(image, uv + (off1 / resolution)) * 0.35294117647058826;
	color += texture2D(image, uv - (off1 / resolution)) * 0.35294117647058826;
	return color; 
}

vec4 blur9(vec4 color, sampler2D image, vec2 uv, vec2 resolution, vec2 direction) {
	vec2 off1 = vec2(1.3846153846) * direction;
	vec2 off2 = vec2(3.2307692308) * direction;
	color += texture2D(image, uv) * 0.2270270270;
	color += texture2D(image, uv + (off1 / resolution)) * 0.3162162162;
	color += texture2D(image, uv - (off1 / resolution)) * 0.3162162162;
	color += texture2D(image, uv + (off2 / resolution)) * 0.0702702703;
	color += texture2D(image, uv - (off2 / resolution)) * 0.0702702703;
	return color;
}

vec4 blur13(vec4 color, sampler2D image, vec2 uv, vec2 resolution, vec2 direction) {
	vec2 off1 = vec2(1.411764705882353) * direction;
	vec2 off2 = vec2(3.2941176470588234) * direction;
	vec2 off3 = vec2(5.176470588235294) * direction;
	color += texture2D(image, uv) * 0.1964825501511404;
	color += texture2D(image, uv + (off1 / resolution)) * 0.2969069646728344;
	color += texture2D(image, uv - (off1 / resolution)) * 0.2969069646728344;
	color += texture2D(image, uv + (off2 / resolution)) * 0.09447039785044732;
	color += texture2D(image, uv - (off2 / resolution)) * 0.09447039785044732;
	color += texture2D(image, uv + (off3 / resolution)) * 0.010381362401148057;
	color += texture2D(image, uv - (off3 / resolution)) * 0.010381362401148057;
	return color;
}

bool isMagical(vec4 color) {
	return (color.r == 0.0 && color.g == 1.0 && color.b == 1.0) || (color.r == 1.0 && color.g == 1.0 && color.b == 0.0);
}

bool isMagicalNear(sampler2D image, vec2 uv, int size) {
	for (int i = -size; i <= size; i++) {
		for (int j = -size; j <= size; j++) {
			if (isMagical(texture2D(image, vec2(uv.s + i / resolution.x, uv.t + j / resolution.y)))) {
				return true;
			}
		}
	}
	
	return false;
}

bool isLayered(vec4 color) {
	return color.r > 0.0 || color.g > 0.0 || color.b > 0.0;
}

bool isLayeredNear(sampler2D image, vec2 uv, int size) {
	for (int i = -size; i <= size; i++) {
		for (int j = -size; j <= size; j++) {
			if (!((i == -size && j == -size) || (i == -size && j == size) || (i == size && j == -size) || (i == size && j == size))) {
				if (isLayered(texture2D(image, vec2(uv.s + i / resolution.x, uv.t + j / resolution.y)))) {
					return true;
				}
			}
		}
	}
	
	return false;
}

void main(void)
{
	vec2 positionIntoOverlay = ivec2(gl_TexCoord[0].st * resolution + position) % ivec2(dimension);
	
	vec4 overlayColor = texture2D(overlay, positionIntoOverlay / dimension);
	vec4 color = texture2D(texture, gl_TexCoord[0].st);
	vec4 layerColor = texture2D(layer, gl_TexCoord[0].st);
	
	if (isLayered(layerColor)) {
		color = blur5(vec4(0.0), texture, gl_TexCoord[0].st, resolution, vec2(1, 0), (1 + layerColor.r));
		color = blur5(vec4(0.0), texture, gl_TexCoord[0].st, resolution, vec2(0, 1), (1 + layerColor.r));
	} else if (isMagicalNear(texture, gl_TexCoord[0].st, 1)) {
		color = vec4(color.rgb + overlayColor.rgb, color.a);
		
		if (overlayColor.r > 0.75) {
			color = blur13(color, texture, gl_TexCoord[0].st, resolution, vec2(1, 0));
			color = blur13(color, texture, gl_TexCoord[0].st, resolution, vec2(0, 1));
		}
	}

	gl_FragColor = color;
}