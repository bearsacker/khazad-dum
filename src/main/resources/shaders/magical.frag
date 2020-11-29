#version 130

uniform sampler2D texture;
uniform sampler2D overlay;
uniform vec2 dimension;
uniform vec2 position;
uniform vec2 resolution;

vec4 blur5(vec4 color, sampler2D image, vec2 uv, vec2 resolution, vec2 direction) {
  vec2 off1 = vec2(1.3333333333333333) * direction;
  color += texture2D(image, uv) * 0.29411764705882354;
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

void main(void)
{
	vec2 positionIntoOverlay = ivec2(gl_TexCoord[0].st * resolution + position) % ivec2(dimension);
	vec4 overlayColor = texture2D(overlay, positionIntoOverlay / dimension);
	
	vec4 color = texture2D(texture, gl_TexCoord[0].st);
	vec4 colorL = texture2D(texture, vec2(gl_TexCoord[0].s + 1 / resolution.x, gl_TexCoord[0].t));
	vec4 colorR = texture2D(texture, vec2(gl_TexCoord[0].s - 1 / resolution.x, gl_TexCoord[0].t));
	vec4 colorT = texture2D(texture, vec2(gl_TexCoord[0].s, gl_TexCoord[0].t + 1 / resolution.y));
	vec4 colorB = texture2D(texture, vec2(gl_TexCoord[0].s, gl_TexCoord[0].t - 1 / resolution.y));
	vec4 colorTL = texture2D(texture, vec2(gl_TexCoord[0].s + 1 / resolution.x, gl_TexCoord[0].t + 1 / resolution.y));
	vec4 colorBL = texture2D(texture, vec2(gl_TexCoord[0].s + 1 / resolution.x, gl_TexCoord[0].t - 1 / resolution.y));
	vec4 colorTR = texture2D(texture, vec2(gl_TexCoord[0].s - 1 / resolution.x, gl_TexCoord[0].t + 1 / resolution.y));
	vec4 colorBR = texture2D(texture, vec2(gl_TexCoord[0].s - 1 / resolution.x, gl_TexCoord[0].t - 1 / resolution.y));
	
	if (isMagical(color) || isMagical(colorL) || isMagical(colorR) || isMagical(colorT) || isMagical(colorB) || isMagical(colorTL) || isMagical(colorTR) || isMagical(colorBL) || isMagical(colorBR)) {
		color = vec4(color.rgb + overlayColor.rgb, color.a);
		
		if (overlayColor.r > 0.75) {
			color = blur13(color, texture, gl_TexCoord[0].st, resolution, vec2(1, 0));
			color = blur13(color, texture, gl_TexCoord[0].st, resolution, vec2(0, 1));
		}
	}

	gl_FragColor = color;
}