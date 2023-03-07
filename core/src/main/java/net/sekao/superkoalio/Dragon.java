/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.sekao.superkoalio;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 *
 * @author pokea
 */
public class Dragon extends Image{
    TextureRegion stand;
    Animation walk;
    float time = 0;
    float xVelocity = 5; // velocidad constante
    float yVelocity = 0;
    boolean isFacingRight = true;
    TiledMapTileLayer layer;
    Rectangle hitbox;
        
    final float GRAVITY = -2.5f;
    final float MAX_VELOCITY = 10f;
    final float DAMPING = 0.87f;
    
    public Dragon() {
        final int width = 21; // cambiar a int
        final int height = 19; // cambiar a int
        this.setSize(1.2f, 1.2f * height / width);

        Texture koalaTexture = new Texture("dino_enemigo.png");
        TextureRegion[][] grid = TextureRegion.split(koalaTexture, width, height); // cambiar a int

        stand = grid[0][0];
        walk = new Animation(0.15f, grid[0][7], grid[0][8], grid[0][9]);
        walk.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        hitbox = new Rectangle();
    }

    public Rectangle getBounds() {
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public void act(float delta) {
        time += delta;

        //yVelocity = yVelocity + GRAVITY;
        yVelocity = Math.max(yVelocity, -MAX_VELOCITY);

        float x = this.getX();
        float y = this.getY();
        float xChange = xVelocity * delta;
        float yChange = yVelocity * delta;

        if (canMoveTo(x, y + yChange, false)) {
            this.setPosition(x, y + yChange);
        } else {
            yVelocity = 0;
        }

        if (canMoveTo(x + xChange, y, false) == false) { // si choca con un objeto
            xVelocity = -xVelocity; // invertir dirección de movimiento
            isFacingRight = !isFacingRight; // invertir la orientación
        }else{
                    
        }

        this.setPosition(this.getX() + xVelocity * delta, this.getY());

        hitbox.setPosition(this.getX(), this.getY());
        hitbox.setSize(this.getWidth(), this.getHeight());
    }
    

    public void draw(Batch batch, float parentAlpha) {
        TextureRegion frame = (TextureRegion) walk.getKeyFrame(time);

        if (isFacingRight) {
            batch.draw(frame, this.getX(), this.getY(), this.getWidth(), this.getHeight());
        } else {
            batch.draw(frame, this.getX() + this.getWidth(), this.getY(), -1 * this.getWidth(), this.getHeight());
        }
    }

    private boolean canMoveTo(float startX, float startY, boolean shouldDestroy) {
        float endX = startX + this.getWidth();
        float endY = startY + this.getHeight();

        int x = (int) startX;
        while (x < endX) {

            int y = (int) startY;
            while (y < endY) {
                if (layer.getCell(x, y) != null) {
                    if (shouldDestroy) {
                        layer.setCell(x, y, null);
                    }
                    return false;
                }
                y = y + 1;
            }
            x = x + 1;
        }

        return true;
    }
}


