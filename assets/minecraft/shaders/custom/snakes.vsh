#extension GL_OES_standard_derivatives : enable

precision highp float;

uniform float time;
uniform vec2 mouse;
uniform vec2 resolution;
uniform float scale;
uniform float alpha, dist;
uniform vec4 c1, c2;
uniform vec2 position;

float sin_(float value){
	return (sin(value)+1.)/2.;
}
float cos_(float value){
	return (cos(value)+1.)/2.;
}
	
void main( void ) {
	vec2 pos = (vec2(gl_FragCoord.x - position.x, gl_FragCoord.y + position.y) / resolution.xy )*scale;
	vec4 color = mix(c1, c2, sin_(pos.x+time*3.)/2.);

	gl_FragColor = vec4( vec3( color.r, color.g, color.b), (sin_(pos.y-scale + cos(pos.x*1.3+time)) / (distance(vec2(pos.x, pos.y), vec2(0.5*scale))/4.)/dist)*alpha);

}