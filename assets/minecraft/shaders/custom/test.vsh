// sun options
#define glow		( 1./16. )
#define move		0
#define col             vec3( 1.15, .77, 0.99 )

// gfx options
#define AA		1.
#define dithering	1

// code 
#define POS gl_FragCoord
#define OUT gl_FragColor
#define res resolution
precision highp float;
uniform float time;
uniform vec2 mouse, res;
uniform sampler2D bb;
vec2 p, p0, m = mouse*res;
vec3 c, c0;
float sphere( vec3 p, float r ) { return length( p ) - r ; }

float map( vec3 p )
	{ vec3 trans = vec3( 0 );
	#if ( move == 1 )
	trans += 8.*vec3( sin( time), 0., cos( time ) ); p -= trans;
	#endif
	float d = sphere( p, .5 );
	return d; }

void mainImage( vec2 p )
	{ vec3 camPos = vec3( -9.*m.x, -9.*m.y, -9. );
	vec3 rayDir = normalize( vec3( p, 1. ) );
	float depth = 0.;
	for ( int i = 0; i < 56; i++ )
		{ vec3 rayPos = camPos + rayDir * depth;
		float dist = map( rayPos );
		c += vec3( glow )/( dist );
		if ( dist < 0.001 ) { c = vec3( 2. ); break; }
		depth += dist; }
	c *= col; } 

void main( void )
	{ p0 = POS.xy/res; 
 	for ( float kk = 0.; kk < AA; kk++ ) for ( float kkk = 0.; kkk < AA; kkk++ ) // nested AA loop
    		{ vec2 k = ( vec2( kk, kkk ) - .5 )/vec2( AA ); c = vec3( 0. );
		if ( kk + kkk == 0. ) { m = (   m.xy - k - res/2. )/res.y; }
		                        p = ( POS.xy - k - res/2. )/res.y;  
		mainImage( p );
		c0 += c; } // acc the color    
	c0 /= AA*AA;
	#if ( dithering == 1 )
	c0 += fract( sin( POS.x*vec3( 13, 1, 11 )+POS.y*vec3( 1, 7, 2 ) )*30.0391832 )/255.;
	#endif
	OUT = vec4( c0, .3 ); } //ändrom3da