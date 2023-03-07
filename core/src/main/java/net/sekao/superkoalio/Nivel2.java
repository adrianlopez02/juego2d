/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.sekao.superkoalio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Timer;

/**
 *
 * @author pokea
 */
public class Nivel2 implements Screen {
    Stage stage2;
    TiledMap map;
    OrthogonalTiledMapRenderer renderer;
    OrthographicCamera camera;
    Music musica_nivel2;
    Music musica_muerte;
    Music musica_moneda;
    Music musica_todas_las_monedas;
    PersonajePrincipal personaje;
    Enemigo enemigo, enemigo_2, enemigo_3;
    Enemigo puerta;
    Koalio koalio;
    Dragon dragon, dragon_2, dragon_3;
    boolean modo_dios = false;

    Moneda moneda1, moneda2, moneda3;
    Moneda game;
    
    int monedas_recogidas = 0;
    
    public Label monedasLabel;
    public BitmapFont font = new BitmapFont();
    public SpriteBatch batch = new SpriteBatch();
    
    boolean moneda_recogida_1 = false;
    boolean moneda_recogida_2 = false;
    boolean moneda_recogida_3 = false;

    MyGdxGame cambiarfondo;
    int vidas;
    
    public Nivel2(MyGdxGame game, int vidas, boolean modo_dios) {
        this.cambiarfondo = game;
        this.vidas = vidas;
        this.modo_dios = modo_dios;
        font.setColor(Color.GREEN);
        font.getData().setScale(2);
        musica_nivel2 = Gdx.audio.newMusic(Gdx.files.internal("castillo.mp3"));
        musica_nivel2.setLooping(true);
        
        musica_muerte = Gdx.audio.newMusic(Gdx.files.internal("muerte.mp3"));
        musica_moneda = Gdx.audio.newMusic(Gdx.files.internal("moneda.mp3"));
        musica_todas_las_monedas = Gdx.audio.newMusic(Gdx.files.internal("lookatme.mp3"));

    }
        
    public void show() {
        musica_nivel2.play();
        
        map = new TmxMapLoader().load("level2.tmx");
        final float pixelsPerTile = 16;
        renderer = new OrthogonalTiledMapRenderer(map, 1 / pixelsPerTile);
        camera = new OrthographicCamera();

        stage2 = new Stage();
        stage2.getViewport().setCamera(camera);
        
        personaje = new PersonajePrincipal();

        personaje.xVelocity = 10;
        personaje.layer = (TiledMapTileLayer) map.getLayers().get("walls");
        personaje.setPosition(14, 10);
        stage2.addActor(personaje);    
        
        koalio = new Koalio();
       
        koalio.xVelocity = 10;
        koalio.layer = (TiledMapTileLayer) map.getLayers().get("walls");
        koalio.setPosition(17, 10);
        stage2.addActor(koalio);   
        
        dragon = new Dragon();
 
        dragon.xVelocity = 10;
        dragon.layer = (TiledMapTileLayer) map.getLayers().get("walls");
        dragon.setPosition(26, 4);
        stage2.addActor(dragon);   
        
        dragon_2 = new Dragon();
 
        dragon_2.xVelocity = 12;
        dragon_2.layer = (TiledMapTileLayer) map.getLayers().get("walls");
        dragon_2.setPosition(43, 10);
        stage2.addActor(dragon_2); 
        
        dragon_3 = new Dragon();
 
        dragon_3.xVelocity = 20;
        dragon_3.layer = (TiledMapTileLayer) map.getLayers().get("walls");
        dragon_3.setPosition(70, 6);
        stage2.addActor(dragon_3); 
        
        puerta = new Enemigo();
        puerta.xVelocity = 12;
        puerta.layer = (TiledMapTileLayer) map.getLayers().get("walls");
        puerta.setPosition(70, 2);
        puerta.setVisible(false);
        stage2.addActor(puerta); 
    }

    public void render(float delta) {
        Gdx.gl.glClearColor(0.5f, 0.5f, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.position.x = personaje.getX();
        camera.update();
        
        renderer.setView(camera);
        renderer.render();

        stage2.act(delta);
        stage2.draw();
       
        Rectangle personajeHitbox = personaje.getBounds();
        Rectangle hitboxKoala = koalio.getBounds();
        Rectangle hitboxDragon = dragon.getBounds();
        Rectangle hitboxDragon2 = dragon_2.getBounds();
        Rectangle hitboxDragon3 = dragon_3.getBounds();

        Rectangle hitboxPuerta = puerta.getBounds();

        
    if (personajeHitbox.overlaps(hitboxKoala) && modo_dios == false && vidas == 0) {
        if (musica_nivel2.isPlaying()) {
            musica_nivel2.stop();
        }
        musica_muerte.play();
        personaje.remove();
        if (musica_todas_las_monedas.isPlaying()) {
            musica_todas_las_monedas.stop();
        }
        musica_todas_las_monedas.stop(); // se agrega aquí
        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                cambiarfondo.setScreen(new MainMenuScreen(cambiarfondo));
            }
        }, 3); // Se agrega un retraso de 3 segundos antes de ejecutar la tarea
    }else if (personajeHitbox.overlaps(hitboxKoala) && modo_dios == true){
        koalio.remove();
    }else if(personajeHitbox.overlaps(hitboxKoala) && modo_dios == false && vidas > 0){
        if (musica_nivel2.isPlaying()) {
            musica_nivel2.stop();
        }
        musica_muerte.play();
        personaje.remove();
        if (musica_todas_las_monedas.isPlaying()) {
            musica_todas_las_monedas.stop();
        }
        musica_todas_las_monedas.stop();
        vidas--;
        personaje.remove();
        personaje = new PersonajePrincipal();

        personaje.xVelocity = 10;
        personaje.layer = (TiledMapTileLayer) map.getLayers().get("walls");
        personaje.setPosition(14, 10);
        stage2.addActor(personaje);
    }
    
    if (personajeHitbox.overlaps(hitboxDragon) && modo_dios == false && vidas == 0) {
        if (musica_nivel2.isPlaying()) {
            musica_nivel2.stop();
        }
        musica_muerte.play();
        personaje.remove();
        if (musica_todas_las_monedas.isPlaying()) {
            musica_todas_las_monedas.stop();
        }
        musica_todas_las_monedas.stop(); // se agrega aquí
        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                cambiarfondo.setScreen(new MainMenuScreen(cambiarfondo));
            }
        }, 3); // Se agrega un retraso de 3 segundos antes de ejecutar la tarea
    }else if (personajeHitbox.overlaps(hitboxDragon) && modo_dios == true){
        dragon.remove();
    }else if(personajeHitbox.overlaps(hitboxDragon) && modo_dios == false && vidas > 0){
        if (musica_nivel2.isPlaying()) {
            musica_nivel2.stop();
        }
        musica_muerte.play();
        personaje.remove();
        if (musica_todas_las_monedas.isPlaying()) {
            musica_todas_las_monedas.stop();
        }
        musica_todas_las_monedas.stop();
        vidas--;
        personaje.remove();
        personaje = new PersonajePrincipal();

        personaje.xVelocity = 10;
        personaje.layer = (TiledMapTileLayer) map.getLayers().get("walls");
        personaje.setPosition(14, 10);
        stage2.addActor(personaje);
    }
    
    if (personajeHitbox.overlaps(hitboxDragon2) && modo_dios == false && vidas == 0) {
        if (musica_nivel2.isPlaying()) {
            musica_nivel2.stop();
        }
        musica_muerte.play();
        personaje.remove();
        if (musica_todas_las_monedas.isPlaying()) {
            musica_todas_las_monedas.stop();
        }
        musica_todas_las_monedas.stop(); // se agrega aquí
        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                cambiarfondo.setScreen(new MainMenuScreen(cambiarfondo));
            }
        }, 3); // Se agrega un retraso de 3 segundos antes de ejecutar la tarea
    }else if (personajeHitbox.overlaps(hitboxDragon2) && modo_dios == true){
        dragon_2.remove();
    }else if(personajeHitbox.overlaps(hitboxDragon2) && modo_dios == false && vidas > 0){
        if (musica_nivel2.isPlaying()) {
            musica_nivel2.stop();
        }
        musica_muerte.play();
        personaje.remove();
        if (musica_todas_las_monedas.isPlaying()) {
            musica_todas_las_monedas.stop();
        }
        musica_todas_las_monedas.stop();
        vidas--;
        personaje.remove();
        personaje = new PersonajePrincipal();

        personaje.xVelocity = 10;
        personaje.layer = (TiledMapTileLayer) map.getLayers().get("walls");
        personaje.setPosition(14, 10);
        stage2.addActor(personaje);
    }
    
    if (personajeHitbox.overlaps(hitboxDragon) && modo_dios == false && vidas == 0) {
        if (musica_nivel2.isPlaying()) {
            musica_nivel2.stop();
        }
        musica_muerte.play();
        personaje.remove();
        if (musica_todas_las_monedas.isPlaying()) {
            musica_todas_las_monedas.stop();
        }
        musica_todas_las_monedas.stop(); // se agrega aquí
        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                cambiarfondo.setScreen(new MainMenuScreen(cambiarfondo));
            }
        }, 3); // Se agrega un retraso de 3 segundos antes de ejecutar la tarea
    }else if (personajeHitbox.overlaps(hitboxDragon) && modo_dios == true){
        dragon.remove();
    }else if(personajeHitbox.overlaps(hitboxDragon) && modo_dios == false && vidas > 0){
        if (musica_nivel2.isPlaying()) {
            musica_nivel2.stop();
        }
        musica_muerte.play();
        personaje.remove();
        if (musica_todas_las_monedas.isPlaying()) {
            musica_todas_las_monedas.stop();
        }
        musica_todas_las_monedas.stop();
        vidas--;
        personaje.remove();
        personaje = new PersonajePrincipal();

        personaje.xVelocity = 10;
        personaje.layer = (TiledMapTileLayer) map.getLayers().get("walls");
        personaje.setPosition(14, 10);
        stage2.addActor(personaje);
    }
    
    if (personajeHitbox.overlaps(hitboxDragon3) && modo_dios == false && vidas == 0) {
        if (musica_nivel2.isPlaying()) {
            musica_nivel2.stop();
        }
        musica_muerte.play();
        personaje.remove();
        if (musica_todas_las_monedas.isPlaying()) {
            musica_todas_las_monedas.stop();
        }
        musica_todas_las_monedas.stop(); // se agrega aquí
        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                cambiarfondo.setScreen(new MainMenuScreen(cambiarfondo));
            }
        }, 3); // Se agrega un retraso de 3 segundos antes de ejecutar la tarea
    }else if (personajeHitbox.overlaps(hitboxDragon3) && modo_dios == true){
        dragon_3.remove();
    }else if(personajeHitbox.overlaps(hitboxDragon3) && modo_dios == false && vidas > 0){
        if (musica_nivel2.isPlaying()) {
            musica_nivel2.stop();
        }
        musica_muerte.play();
        personaje.remove();
        if (musica_todas_las_monedas.isPlaying()) {
            musica_todas_las_monedas.stop();
        }
        musica_todas_las_monedas.stop();
        vidas--;
        personaje.remove();
        personaje = new PersonajePrincipal();

        personaje.xVelocity = 10;
        personaje.layer = (TiledMapTileLayer) map.getLayers().get("walls");
        personaje.setPosition(14, 10);
        stage2.addActor(personaje);
    }
    
    if (personajeHitbox.overlaps(hitboxPuerta)) {
        if (musica_nivel2.isPlaying()) {
            musica_nivel2.stop();
        }
        musica_muerte.play();
        personaje.remove();
        if (musica_todas_las_monedas.isPlaying() == true) {
            musica_todas_las_monedas.stop();
        }
        musica_todas_las_monedas.stop(); // se agrega aquí
        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                cambiarfondo.setScreen(new Final(cambiarfondo));
            }
        }, 3); // Se agrega un retraso de 3 segundos antes de ejecutar la tarea
    }
        
        if(personaje.getY() < 0 && modo_dios == false){
            personaje.remove();
            if (musica_nivel2.isPlaying()) {
                musica_nivel2.stop();
            }
            cambiarfondo.setScreen(new MainMenuScreen(this.cambiarfondo));
        }else if(personaje.getY() < 0 && modo_dios == true){
            personaje.remove();
            personaje = new PersonajePrincipal();

            personaje.xVelocity = 10;
            personaje.layer = (TiledMapTileLayer) map.getLayers().get("walls");
            personaje.setPosition(14, 10);
            stage2.addActor(personaje);            
        }
            
         

        batch.begin();
        font.draw(batch, "Vidas: " + vidas, 6, 460);
        font.draw(batch, "Nivel: 2 ", 6, 430);
        batch.end();        

    }

    public void dispose() {
    }

    public void hide() {
    }

    public void pause() {
    }

    public void resize(int width, int height) {
        camera.setToOrtho(false, 20 * width / height, 20);
    }

    public void resume() {
    }
}

