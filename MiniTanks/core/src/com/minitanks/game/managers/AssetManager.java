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
        System.out.println(path);
        Model m = this.modelLoader.loadModel(Gdx.files.getFileHandle(path, Files.FileType.Internal));
        return new ModelInstance(m);

    }

    public ModelInstance createFloorModel(final float width, final float height, final Material material) {
        modelBuilder = new ModelBuilder();
        Model m =modelBuilder.createBox(10000,1,10000,new Material(ColorAttribute.createDiffuse(Color.GRAY)),VertexAttributes.Usage.Position|VertexAttributes.Usage.Normal);
        return new ModelInstance(m,0,-1,0);
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
