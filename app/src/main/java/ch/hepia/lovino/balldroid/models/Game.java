package ch.hepia.lovino.balldroid.models;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Game {
    private Ball ball;
    private Score score;
    private Time time;
    //We use a linkedlist so we can addFirst everything but the ball, so the ball is always the last thing rendered
    private LinkedList<Drawable> objects;
    private ArrayList<Platform> platforms;
    private ArrayList<PointArea> pointsAreas;
    private Random rnd;
    private final int screenWidth, screenHeight;
    private final static int ARRIVAL_ZONE_VERT_SPACE = 200;
    private final static int START_ZONE_VERT_SPACE = 200;
    private final static int HORIZONTAL_GAP = 100;
    private final static int VERTICAL_GAP = 300;
    private final static int PLATFORM_HEIGHT = 20;
    private final static int WALL_WIDTH = PLATFORM_HEIGHT;
    private final static int WALL_HEIGHT = VERTICAL_GAP - PLATFORM_HEIGHT - 30;
    private final static int MAX_VERTICAL_WALLS = 3;
    private final static int POINTS_AREA_COUNT = 8;
    private final static int POINTS_SEPARATORS_COUNT = POINTS_AREA_COUNT - 1;
    private final static int MAX_POINTS = 99;

    public Game(Ball ball, Score score, Time time, int screenWidth, int screenHeight) {
        this.ball = ball;
        this.score = score;
        this.time = time;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.objects = new LinkedList<>();
        this.platforms = new ArrayList<>();
        this.pointsAreas = new ArrayList<>();
        this.objects.add(ball);
        this.objects.addFirst(score);
        this.objects.addFirst(time);
        this.rnd = new Random();
        generatePlatforms();
        generatePointsArea();
    }

    private void generatePointsArea() {
        float areaWidth = (screenWidth - POINTS_SEPARATORS_COUNT * WALL_WIDTH) / (float) POINTS_AREA_COUNT;
        for (int i = 0; i < POINTS_AREA_COUNT; i++) {
            float areaStart = i * areaWidth + (i * WALL_WIDTH);
            PointArea point = new PointArea(areaStart, screenHeight - 100, areaWidth, 100, rnd.nextInt(MAX_POINTS));
            this.pointsAreas.add(point);
            this.objects.addFirst(point);
            if (i != POINTS_AREA_COUNT - 1) {
                //add wall after
                Platform platform = new Platform(areaStart + areaWidth, screenHeight - 100, WALL_WIDTH, 100);
                this.platforms.add(platform);
                this.objects.addFirst(platform);
            }
        }
    }

    private void generatePlatforms() {
        for (int i = START_ZONE_VERT_SPACE; i < screenHeight - ARRIVAL_ZONE_VERT_SPACE; i += VERTICAL_GAP) {
            int startOfGap = rnd.nextInt(screenWidth - HORIZONTAL_GAP);
            int endOfGap = startOfGap + HORIZONTAL_GAP;
            Platform platform = new Platform(0, i, startOfGap, PLATFORM_HEIGHT);
            this.objects.addFirst(platform);
            this.platforms.add(platform);
            platform = new Platform(endOfGap, i, screenWidth, PLATFORM_HEIGHT);
            this.objects.addFirst(platform);
            this.platforms.add(platform);

            //Now at each level of this, we want to add a random number of vertical walls, not using all the space
            int wallCount = rnd.nextInt(MAX_VERTICAL_WALLS);
            for (int j = 0; j < wallCount; j++) {
                int horizontalStart = rnd.nextInt(screenWidth - WALL_WIDTH);
                platform = new Platform(horizontalStart, i, WALL_WIDTH, WALL_HEIGHT);
                this.objects.addFirst(platform);
                this.platforms.add(platform);
            }
        }
    }

    public List<Drawable> getObjects() {
        return objects;
    }

    public List<Platform> getPlatforms() {
        return platforms;
    }

    public List<PointArea> getPointsAreas() {
        return pointsAreas;
    }
}
