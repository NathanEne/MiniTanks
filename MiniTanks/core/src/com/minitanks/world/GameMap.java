package com.minitanks.world;

import com.minitanks.game.entities.Entity;

import java.util.ArrayList;

public abstract class GameMap {

    private ArrayList<Entity> entities;
    private ArrayList<Entity> entitiesToAdd;
    private ArrayList<Entity> entitiesToRemove;
    public GameMap() {
        this.entities = new ArrayList<Entity>();
        this.entitiesToAdd = new ArrayList<Entity>();
        this.entitiesToRemove = new ArrayList<Entity>();

    }

//    public void render(OrthographicCamera camera, ModelBatch batch) {
//
//        for (Entity entity : getEntities()) {
//            entity.render(batch);
//        }
//    }

    public void update(float delta) {

        for (Entity entity : getEntities()) {
            entity.update(delta);
                entity.getBody().setWorldTransform(entity.getModelInstance().transform);
        }
        this.entities.removeAll(entitiesToRemove);
        this.entitiesToRemove.clear();
        this.entities.addAll(entitiesToAdd);
        this.entitiesToAdd.clear();
    }

    public abstract void dispose();

    public abstract int getWidth();

    public abstract int getHeight();

    //public abstract int getLayers();

    /**
     * @return the entities
     */
    public ArrayList<Entity> getEntities() {
        return entities;
    }

    /**
     * @param entities the entities to set
     */
    public void setEntities(ArrayList<Entity> entities) {
        this.entities = entities;
    }

    public void addEntities(Entity entities) {
        this.entities.add(entities);
    }
    public void addEntitiesLater(Entity entities) {
        this.entitiesToAdd.add(entities);
    }

    public void removeEntitiesLater(ArrayList<Entity> entities) {
        this.entitiesToRemove.addAll(entities);
    }
}
