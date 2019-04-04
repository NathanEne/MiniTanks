package com.minitanks.game.managers;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;

import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
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

    public ModelInstance initializeModel(String path){
        Model m = this.modelLoader.loadModel(Gdx.files.getFileHandle(path, Files.FileType.Internal));
        return new ModelInstance(m);

    }

    public ModelInstance createFloorModel(final float width, final float height, final Material material) {
        modelBuilder = new ModelBuilder();
        Model m = modelBuilder.createBox(1000000,1,1000000,new Material(ColorAttribute.createDiffuse(new Color((float)253/255, (float)227/255, (float)167/255, 1f))),VertexAttributes.Usage.Position|VertexAttributes.Usage.Normal);
        return new ModelInstance(m,0,-1,0);
    }
    public ModelInstance createWallModel(final float width, final float height, final Material material) {
        modelBuilder = new ModelBuilder();
        Model m = modelBuilder.createBox(width,4000,height,new Material(ColorAttribute.createDiffuse(new Color((float)232/255, (float)232/255, (float)232/255, 1f))),VertexAttributes.Usage.Position|VertexAttributes.Usage.Normal);
        return new ModelInstance(m,100,0,100);
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
