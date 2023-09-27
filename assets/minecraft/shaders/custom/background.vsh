precision highp float;

uniform float time;
uniform vec2 mouse;
uniform vec2 resolution;
uniform vec2 pos;
uniform float step, colorSpeed, animSpeed, size;
uniform vec4 color1, color2;

void main( void ) {
	vec2 position = (gl_FragCoord.xy / resolution.xy*size)-vec2(pos.x, -pos.y);
	vec4 color = mix(color1, color2, sin(position.y*step+time*colorSpeed));
	gl_FragColor = vec4(color.r, color.g, color.b, (distance(vec2(position.x+3.*size,(sin(position.y*cos(position.x*.5-time/animSpeed)*20.)+2.+6.7*size)/2.), position*7.) >= 4.5 ? 0. : color.a));	
}