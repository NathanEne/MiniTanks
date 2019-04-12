package com.minitanks.game.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PolygonBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ScreenUtils;

public class DecalHelper {
    //class variables

    public Texture texture = null;//create this
    public Array<Disposable> disposables = new Array<Disposable>();
    public Pixmap            pm          = null;
    public BitmapFont font;
    //---------------------------

   public DecalHelper(){

       font = new BitmapFont(Gdx.files.internal("default.fnt"),
               Gdx.files.internal("default.png"), false);
    }
    //---------------------------

    //---------------------------
    public Decal render(Color fg_color, Color bg_color,String str)
    {
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();

        SpriteBatch spriteBatch = new SpriteBatch();

        FrameBuffer m_fbo = new FrameBuffer(Pixmap.Format.RGB565, (int) (width), (int) (height), false);
        m_fbo.begin();
        Gdx.gl.glClearColor(bg_color.r, bg_color.g, bg_color.b, bg_color.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Matrix4 normalProjection = new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(),  Gdx.graphics.getHeight());
        spriteBatch.setProjectionMatrix(normalProjection);

        spriteBatch.begin();
        spriteBatch.setColor(fg_color);
        //do some drawing ***here's where you draw your dynamic texture***

        font.draw(spriteBatch, str,  width/4, height - 20);//multi-line draw

        spriteBatch.end();//finish write to buffer
        pm = ScreenUtils.getFrameBufferPixmap(0, 0, (int) width, (int) height);//write frame buffer to Pixmap
        m_fbo.end();
        m_fbo.dispose();
        m_fbo = null;
        spriteBatch.dispose();
        texture = new Texture(pm);
        TextureRegion tr = new TextureRegion(texture);
        Decal d = Decal.newDecal(tr);
        return d ;
    }
    //---------------------------
}