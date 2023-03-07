package net.sekao.superkoalio;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Timer;
import java.util.ArrayList;

public class MainScreen implements Screen {
    Stage stage;
    TiledMap map;
    OrthogonalTiledMapRenderer renderer;
    OrthographicCamera camera;
    Music musica_nivel1;
    Music musica_muerte;
    Music musica_moneda;
    Music musica_todas_las_monedas;
    PersonajePrincipal personaje;
    Enemigo enemigo, enemigo_2;
    Enemigo puerta;
    Koalio koalio;
    XXXTentacion x;
    boolean modo_dios = false;
    int vidas = 3;

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

    TextureRegion stand, jump;
    Animation walk;
    
    public MainScreen(MyGdxGame game) {
        this.cambiarfondo = game;
        font.setColor(Color.GREEN);
        font.getData().setScale(2);
        musica_nivel1 = Gdx.audio.newMusic(Gdx.files.internal("nivel1.mp3"));
        musica_nivel1.setLooping(true);
        
        musica_muerte = Gdx.audio.newMusic(Gdx.files.internal("muerte.mp3"));
        musica_moneda = Gdx.audio.newMusic(Gdx.files.internal("moneda.mp3"));
        musica_todas_las_monedas = Gdx.audio.newMusic(Gdx.files.internal("lookatme2.mp3"));

    }
        
    public void show() {
        musica_nivel1.play();
        
        map = new TmxMapLoader().load("level1.tmx");
        final float pixelsPerTile = 16;
        renderer = new OrthogonalTiledMapRenderer(map, 1 / pixelsPerTile);
        camera = new OrthographicCamera();

        stage = new Stage();
        stage.getViewport().setCamera(camera);
        
        personaje = new PersonajePrincipal();

        personaje.xVelocity = 10;
        personaje.layer = (TiledMapTileLayer) map.getLayers().get("walls");
        personaje.setPosition(23, 10);
        stage.addActor(personaje);
        
        
        enemigo = new Enemigo();
       
        enemigo.layer = (TiledMapTileLayer) map.getLayers().get("walls");
        enemigo.setPosition(37, 10);
        stage.addActor(enemigo);    
        
        enemigo_2 = new Enemigo();
       
        enemigo_2.layer = (TiledMapTileLayer) map.getLayers().get("walls");
        enemigo_2.setPosition(45, 10);
        stage.addActor(enemigo_2);  
        
        koalio = new Koalio();
       
        koalio.xVelocity = 10;
        koalio.layer = (TiledMapTileLayer) map.getLayers().get("walls");
        koalio.setPosition(57, 10);
        stage.addActor(koalio);   
                
        moneda1 = new Moneda();
        
        moneda1.layer = (TiledMapTileLayer) map.getLayers().get("walls");
        moneda1.setPosition(44, 10);
        stage.addActor(moneda1);
        
        moneda2 = new Moneda();
        
        moneda2.layer = (TiledMapTileLayer) map.getLayers().get("walls");
        moneda2.setPosition(60, 11);
        stage.addActor(moneda2);
                
        moneda3 = new Moneda();
        
        moneda3.layer = (TiledMapTileLayer) map.getLayers().get("walls");
        moneda3.setPosition(96, 18);
        stage.addActor(moneda3);
        
        puerta = new Enemigo();
       
        puerta.layer = (TiledMapTileLayer) map.getLayers().get("walls");
        puerta.setPosition(97, 4);
        puerta.setVisible(false);
        stage.addActor(puerta); 
        
        x = new XXXTentacion();

        x.layer = (TiledMapTileLayer) map.getLayers().get("walls");
        x.setPosition(91, 5);
        x.setVisible(false);
        stage.addActor(x); 
    }

    public void render(float delta) {
        Gdx.gl.glClearColor(0.5f, 0.5f, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.position.x = personaje.getX();
        camera.update();
        
        renderer.setView(camera);
        renderer.render();

        stage.act(delta);
        stage.draw();
       
        Rectangle personajeHitbox = personaje.getBounds();
        Rectangle hitboxEnemigo = enemigo.getBounds();
        Rectangle hitboxEnemigo2 = enemigo_2.getBounds();
        Rectangle hitboxPuerta = puerta.getBounds();
        Rectangle hitboxX = x.getBounds();

        Rectangle hitboxMoneda1 = moneda1.getBounds();
        Rectangle hitboxMoneda2 = moneda2.getBounds();
        Rectangle hitboxMoneda3 = moneda3.getBounds();
        
        Rectangle hitboxKoala = koalio.getBounds();

    if (personajeHitbox.overlaps(hitboxEnemigo) && modo_dios == false && vidas == 0) {
        if (musica_nivel1.isPlaying()) {
            musica_nivel1.stop();
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
    }else if (personajeHitbox.overlaps(hitboxEnemigo) && modo_dios == true){
        enemigo.remove();
    }else if(personajeHitbox.overlaps(hitboxEnemigo) && modo_dios == false && vidas > 0){
        if (musica_nivel1.isPlaying()) {
            musica_nivel1.stop();
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
        personaje.setPosition(23, 10);
        stage.addActor(personaje);
    }
    
    if (personajeHitbox.overlaps(hitboxEnemigo2) && modo_dios == false && vidas == 0) {
        if (musica_nivel1.isPlaying()) {
            musica_nivel1.stop();
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
    }else if (personajeHitbox.overlaps(hitboxEnemigo2) && modo_dios == true){
        enemigo_2.remove();
    }else if(personajeHitbox.overlaps(hitboxEnemigo2) && modo_dios == false && vidas > 0){
        if (musica_nivel1.isPlaying()) {
            musica_nivel1.stop();
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
        personaje.setPosition(23, 10);
        stage.addActor(personaje);
    }
    
    if (personajeHitbox.overlaps(hitboxKoala) && modo_dios == false && vidas == 0) {
        if (musica_nivel1.isPlaying()) {
            musica_nivel1.stop();
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
        if (musica_nivel1.isPlaying()) {
            musica_nivel1.stop();
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
        personaje.setPosition(23, 10);
        stage.addActor(personaje);
    }

        
        if(personaje.getY() < 0){
            personaje.remove();
            cambiarfondo.setScreen(new MainMenuScreen(this.cambiarfondo));
        }
        
        if(moneda_recogida_1 == false){
            if (personajeHitbox.overlaps(hitboxMoneda1)) {
                moneda1.remove();
                musica_moneda.play();
                moneda_recogida_1 = true;
            }  
        }
        
        if(moneda_recogida_2 == false){
            if (personajeHitbox.overlaps(hitboxMoneda2)) {
                moneda2.remove();
                musica_moneda.play();
                moneda_recogida_2 = true;
            }  
        }
        
        if(moneda_recogida_3 == false){
            if (personajeHitbox.overlaps(hitboxMoneda3)) {
                moneda3.remove();
                musica_moneda.play();
                moneda_recogida_3 = true;
            }  
        } 
             
        if(moneda_recogida_1 == true && moneda_recogida_2 == true && moneda_recogida_3 == true){           
            musica_nivel1.stop();
            musica_todas_las_monedas.play();
            moneda_recogida_1 = false;
            moneda_recogida_2 = false;
            moneda_recogida_3 = false;
            modo_dios = true;
            personaje.GRAVITY = -1.5f;
            
            final int width = 21; // cambiar a int
            final int height = 19; // cambiar a int
            //this.setSize(1.2f, 1.2f * height / width);
        
            Texture koalaTexture = new Texture("dino_amarillo.png");
            TextureRegion[][] grid = TextureRegion.split(koalaTexture, width, height); // cambiar a int

            personaje.stand = grid[0][0];
            personaje.jump = grid[0][9];
            personaje.walk = new Animation(0.15f, grid[0][7], grid[0][8], grid[0][9]);
            personaje.walk.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
            
            x.setVisible(true);

        }
        
        if (personajeHitbox.overlaps(hitboxPuerta)) {
            if (musica_nivel1.isPlaying()) {
                musica_nivel1.stop();
                
            }else if(musica_todas_las_monedas.isPlaying()){
                musica_todas_las_monedas.stop();
            }
            
            cambiarfondo.setScreen(new Nivel2(cambiarfondo, vidas, modo_dios));
        }
        
        batch.begin();
        font.draw(batch, "Vidas: " + vidas, 6, 460);
        font.draw(batch, "Nivel: 1 ", 6, 430);

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
