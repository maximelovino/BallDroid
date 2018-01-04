package ch.hepia.lovino.balldroid.models;


import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

public class Game {
    private Ball ball;
    private ArrayList<Object> objects;
    private ArrayList<Platform> platforms;
    private final int screenWidth, screenHeight;
    private final static int ARRIVAL_ZONE_VERT_SPACE = 200;
    private final static int START_ZONE_VERT_SPACE = 200;
    private final static int HORIZONTAL_GAP = 100;
    private final static int VERTICAL_GAP = 300;
    private final static int PLATFORM_HEIGHT = 20;
    private final static int WALL_WIDTH = PLATFORM_HEIGHT;
    private final static int WALL_HEIGHT = VERTICAL_GAP - PLATFORM_HEIGHT - 30;
    private final static int MAX_VERTICAL_WALLS = 3;

    public Game(Ball ball, int screenWidth, int screenHeight) {
        this.ball = ball;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.objects = new ArrayList<>();
        this.platforms = new ArrayList<>();
        this.objects.add(ball);
        generatePlatforms();
    }

    private void generatePlatforms() {
        //TODO we need to add vertical walls as well
        Random rnd = new Random();
        for (int i = START_ZONE_VERT_SPACE; i < screenHeight - ARRIVAL_ZONE_VERT_SPACE; i += VERTICAL_GAP) {
            int startOfGap = rnd.nextInt(screenWidth - HORIZONTAL_GAP);
            int endOfGap = startOfGap + HORIZONTAL_GAP;
            Platform platform = new Platform(0, i, startOfGap, PLATFORM_HEIGHT);
            this.objects.add(platform);
            this.platforms.add(platform);
            platform = new Platform(endOfGap, i, screenWidth, PLATFORM_HEIGHT);
            this.objects.add(platform);
            this.platforms.add(platform);

            //Now at each level of this, we want to add a random number of vertical walls, not using all the space
            int wallCount = rnd.nextInt(MAX_VERTICAL_WALLS);
            for (int j = 0; j < wallCount; j++) {
                int horizontalStart = rnd.nextInt(screenWidth - WALL_WIDTH);
                platform = new Platform(horizontalStart, i, WALL_WIDTH, WALL_HEIGHT);
                this.objects.add(platform);
                this.platforms.add(platform);
            }
        }
    }

    public ArrayList<Object> getObjects() {
        return objects;
    }

    public ArrayList<Platform> getPlatforms() {
        return platforms;
    }
}
