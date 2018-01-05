package ch.hepia.lovino.balldroid.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import ch.hepia.lovino.balldroid.controllers.GameController;
import ch.hepia.lovino.balldroid.models.Drawable;


public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder holder;
    private DrawingThread thread;
    private Paint paint;
    private GameController controller;
    private boolean started = false;

    public GameSurfaceView(Context context, GameController controller) {
        super(context);
        this.controller = controller;
        holder = getHolder();
        holder.addCallback(this);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        paint.setStyle(Paint.Style.FILL);

    }

    public int getSurfaceWidth() {
        return this.holder.getSurfaceFrame().width();
    }

    public int getSurfaceHeight() {
        return this.holder.getSurfaceFrame().height();
    }

    public float getDPI() {
        return getResources().getDisplayMetrics().densityDpi;
    }

    /**
     * Implement this to do your drawing.
     *
     * @param canvas the canvas on which the background will be drawn
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        controller.update();
        canvas.drawColor(Color.WHITE);
        for (Drawable obj : controller.getGame().getObjects()) {
            obj.draw(canvas, paint);
        }
    }

    /**
     * This is called immediately after the surface is first created.
     * Implementations of this should start up whatever rendering code
     * they desire.  Note that only one thread can ever draw into
     * a {@link Surface}, so you should not draw into the Surface here
     * if your normal rendering will be in another thread.
     *
     * @param holder The SurfaceHolder whose surface is being created.
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!started) {
            controller.start();
            started = true;
        }
        thread = new DrawingThread();
        thread.keepDrawing = true;
        thread.start();
    }

    /**
     * This is called immediately after any structural changes (format or
     * size) have been made to the surface.  You should at this point update
     * the imagery in the surface.  This method is always called at least
     * once, after {@link #surfaceCreated}.
     *
     * @param holder The SurfaceHolder whose surface has changed.
     * @param format The new PixelFormat of the surface.
     * @param width  The new width of the surface.
     * @param height The new height of the surface.
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    /**
     * This is called immediately before a surface is being destroyed. After
     * returning from this call, you should no longer try to access this
     * surface.  If you have a rendering thread that directly accesses
     * the surface, you must ensure that thread is no longer touching the
     * Surface before returning from this function.
     *
     * @param holder The SurfaceHolder whose surface is being destroyed.
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        controller.pauseGame();
        thread.keepDrawing = false;
        boolean joined = false;
        while (!joined) {
            try {
                thread.join();
                joined = true;
            } catch (InterruptedException e) {
            }
        }
    }

    @SuppressLint("WrongCall")
    private class DrawingThread extends Thread {
        private boolean keepDrawing = true;

        @Override
        public void run() {
            while (keepDrawing) {
                Canvas canvas = null;
                try {
                    canvas = holder.lockCanvas();
                    synchronized (holder) {
                        try {
                            onDraw(canvas);
                        } catch (Exception e) {
                            Log.e("Drawing thread", "Couldn't draw");
                        }
                    }
                } finally {
                    if (canvas != null) {
                        holder.unlockCanvasAndPost(canvas);
                    }
                }
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    Log.w("Drawing thread", "Interrupted sleep");
                }
            }
        }
    }
}
