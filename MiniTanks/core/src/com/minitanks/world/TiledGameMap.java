package com.minitanks.world;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;


public class TiledGameMap extends GameMap {

    TiledMap tiledMap;
    OrthogonalTiledMapRenderer tiledMapRenderer;


    public TiledGameMap(){
        tiledMap = new TmxMapLoader().load("Map2.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
    }

//    @Override
//    public void render(OrthographicCamera camera, ModelBatch batch) {
//        tiledMapRenderer.setView(camera);
//        tiledMapRenderer.render();
//        batch.begin(camera);
//        super.render(camera, batch);
//        batch.end();
//
//    }

    @Override
    public void update(float delta) {
        super.update(delta);
    }

    @Override
    public void dispose() {
        tiledMap.dispose();

    }


    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }


}
