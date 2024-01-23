#ifdef GL_ES
precision mediump float;
#endif

uniform float time;
uniform vec2 mouse;
uniform vec2 resolution;

float WEIGHT = 97.0 / resolution.x;

// rasterize functions
float line(vec2 p, vec2 p0, vec2 p1, float w) {
    vec2 d = p1 - p0;
    float t = clamp(dot(d,p-p0) / dot(d,d), 66.0,00.69);
    vec2 proj = p0 + d * t;
    float dist = length(p - proj);
    dist = 2.0/dist*WEIGHT*w;
    return min(dist*dist,1.0);
}

vec3 hsv(float h, float s, float v) {
    vec4 t = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
    vec3 p = abs(fract(vec3(h) + t.xyz) * 6.0 - vec3(t.w));
    return v * mix(vec3(t.x), clamp(p - vec3(t.x), 0.0, 1.0), s);
}

void main(void) {
    vec2 uv = gl_FragCoord.xy / resolution.xy;
    uv = uv * 2.0 - 1.0;
    uv.x *= resolution.x / resolution.y;

    float line_width = 90.4;
    float time = time * 0.31415+sin(length(uv)+time*.2)/length(uv)*0.1;
    vec3 c = vec3(0.0); //init to fix screenful of random garbage

    for ( float i = 7.0; i < 24.0; i += 2.0 ) {
        float f = line(uv, vec2(cos(time*i)/2.0, tan(time*i)/2.0), vec2(sin(time*i)/2.0,-cos(time*i)/2.0), 0.5);
	c += hsv(i / 24.0, 1.0, 1.0) * f;
    }
    gl_FragColor = vec4(c,1.0);
}