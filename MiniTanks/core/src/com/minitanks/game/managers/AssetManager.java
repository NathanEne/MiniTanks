package com.minitanks.game.managers;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.g3d.utils.RenderableSorter;
import com.badlogic.gdx.graphics.g3d.utils.ShaderProvider;
import com.badlogic.gdx.utils.UBJsonReader;
import com.minitanks.game.entities.Entity;

import java.util.ArrayList;

public class AssetManager {
    private ModelBatch batch;
    private UBJsonReader jsonReader;
    private G3dModelLoader modelLoader;
    private ModelBuilder modelBuilder;


    public AssetManager(){
        this.jsonReader = new UBJsonReader();
        this.modelLoader = new G3dModelLoader(jsonReader);
        this.batch = new ModelBatch();

    }
    public AssetManager(final RenderContext context, final ShaderProvider shaderProvider, final RenderableSorter sorter){
        this.jsonReader = new UBJsonReader();
        this.modelLoader = new G3dModelLoader(jsonReader);
        this.batch = new ModelBatch(context, shaderProvider, sorter);
    }


    public ModelInstance initializeModel(String path){
        Model m = this.modelLoader.loadModel(Gdx.files.getFileHandle(path, Files.FileType.Internal));
        return new ModelInstance(m);

    }

    public ModelInstance createFloorModel(final float width, final float height, final Material material) {
        modelBuilder = new ModelBuilder();
        Model m = modelBuilder.createBox(1000000,1,1000000,new Material(ColorAttribute.createDiffuse(new Color((float)79/255, (float)102/255, (float)106/255, 1f))),VertexAttributes.Usage.Position|VertexAttributes.Usage.Normal);
        return new ModelInstance(m,0,-1,0);
    }
    public ModelInstance createWallModel(final float width, final float height, float x, float z) {
        modelBuilder = new ModelBuilder();
        Model m = modelBuilder.createBox(width,4000,height,new Material(ColorAttribute.createDiffuse(new Color((float)232/255, (float)232/255, (float)232/255, 1f))),VertexAttributes.Usage.Position|VertexAttributes.Usage.Normal);
        return new ModelInstance(m,x,0,z);
    }
    public ModelInstance createBulletModel(float x, float z,float y) {
        modelBuilder = new ModelBuilder();
        Model m = modelBuilder.createSphere(100,100,100,20,20,new Material(ColorAttribute.createDiffuse(new Color((float)205/255, (float)127/255, (float)50/255, 1f))),VertexAttributes.Usage.Position|VertexAttributes.Usage.Normal);
        return new ModelInstance(m,x,y,z);
    }


    public void render(PerspectiveCamera cam, Environment environment, ArrayList<Entity> a){
        this.batch.begin(cam);
        for(int i = 0; i<a.size(); i++)
            this.batch.render(a.get(i).getModelInstance(), environment);
        this.batch.end();
    }


    public void render(OrthographicCamera cam, Environment environment, ArrayList<Entity> a){
        this.batch.begin(cam);
        for(int i = 0; i<a.size(); i++)
            this.batch.render(a.get(i).getModelInstance(), environment);
        this.batch.end();
    }


}
