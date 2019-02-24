package com.minitanks.game.managers;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.utils.UBJsonReader;
import com.minitanks.game.entities.Entity;

import java.util.ArrayList;

public class AssetManager {
    private ModelBatch batch;
    private UBJsonReader jsonReader;
    private G3dModelLoader modelLoader;
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
    public void render(PerspectiveCamera cam, Environment environment, ArrayList<Entity> a){
        this.batch.begin(cam);
        for(int i = 0; i<a.size(); i++)
            this.batch.render(a.get(i).getModelInstance(), environment);
        this.batch.end();
    }


}
