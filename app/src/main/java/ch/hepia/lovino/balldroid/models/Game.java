package ch.hepia.lovino.balldroid.models;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Game {
    private final Ball ball;
    private final Score score;
    private final Time time;
    //We use a linkedlist so we can addFirst everything but the ball, so the ball is always the last thing rendered
    private final LinkedList<Drawable> objects;
    private final ArrayList<Platform> platforms;
    private final ArrayList<PointArea> pointsAreas;
    private final ArrayList<Bonus> bonuses;
    private final Random rnd;
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
    private final static int MAX_POINTS = 100;

    private final static int BONUS_SIZE = 30;
    private final static int BONUS_MAX_SECONDS = 10;
    private final static int BONUS_COUNT = 5;
    private final static int INFOS_DISTANCE_FROM_RIGHT = 400;

    public Game(DifficultyLevel difficulty, int initialScore, int initialTime, int screenWidth, int screenHeight) {
        this.ball = new Ball(difficulty);
        this.score = new Score(screenWidth - INFOS_DISTANCE_FROM_RIGHT, initialScore);
        this.time = new Time(screenWidth - INFOS_DISTANCE_FROM_RIGHT, initialTime);
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.objects = new LinkedList<>();
        this.platforms = new ArrayList<>();
        this.pointsAreas = new ArrayList<>();
        this.bonuses = new ArrayList<>();
        this.objects.add(ball);
        this.objects.addFirst(score);
        this.objects.addFirst(time);
        this.rnd = new Random();
        generatePlatforms();
        generatePointsArea();
        generateBonuses();
    }

    public Ball getBall() {
        return ball;
    }

    public Score getScore() {
        return score;
    }

    public Time getTime() {
        return time;
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

    private void generateBonuses() {
        for (int i = 0; i < BONUS_COUNT; i++) {
            int x = rnd.nextInt(screenWidth - BONUS_SIZE);
            int y = rnd.nextInt(screenHeight - START_ZONE_VERT_SPACE - ARRIVAL_ZONE_VERT_SPACE - BONUS_SIZE) + START_ZONE_VERT_SPACE;
            int seconds = rnd.nextInt(BONUS_MAX_SECONDS) + 1; //we go from 1 to 10;
            boolean negative = rnd.nextBoolean();
            seconds = negative ? -seconds : seconds;
            Bonus bonus = new Bonus(x, y, BONUS_SIZE, BONUS_SIZE, seconds);
            bonuses.add(bonus);
            objects.addFirst(bonus);
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

    public ArrayList<Bonus> getBonuses() {
        return bonuses;
    }

    public void removeBonus(Bonus bonus) {
        this.bonuses.remove(bonus);
        this.objects.remove(bonus);
    }
}
