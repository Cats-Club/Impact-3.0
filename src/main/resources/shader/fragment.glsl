#version 120

uniform sampler2D DiffuseSamper;
uniform vec2 TexelSize;
uniform int SampleRadius;

void main()
{
    vec4 centerCol = texture2D(DiffuseSamper, gl_TexCoord[0].st);

    if(centerCol.a != 0.0F)
    {
        gl_FragColor = vec4(0, 0, 0, 0);
        return;
    }
    float closest = SampleRadius * 1.0F;
    for(int xo = -SampleRadius; xo <= SampleRadius; xo++)
    {
        for(int yo = -SampleRadius; yo <= SampleRadius; yo++)
        {
            vec4 currCol = texture2D(DiffuseSamper, gl_TexCoord[0].st + vec2(xo * TexelSize.x, yo * TexelSize.y));
            if(currCol.r != 0.0F || currCol.g != 0.0F || currCol.b != 0.0F || currCol.a != 0.0F)
            {
                float currentDist = sqrt(xo * xo + yo * yo);
                if(currentDist < closest)
                {
                    closest = currentDist;
                }
            }
        }
    }
    float m = 2.0;
    float fade = max(0, ((SampleRadius * 1.0F) - (closest - 1)) / (SampleRadius * 1.0F));
    gl_FragColor = vec4(m - fade, m - fade, m - fade, fade);
}