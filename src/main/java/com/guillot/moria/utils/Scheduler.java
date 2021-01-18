package com.guillot.moria.utils;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Graphics;

import com.guillot.engine.gui.Component;

public class Scheduler extends Component {

    private long currentFrame = 0;

    private List<ScheduledRunnable> pendingSchedules = new ArrayList<>();

    public void schedule(Runnable runnable, long delayInFrames) {
        this.pendingSchedules.add(new ScheduledRunnable(runnable, currentFrame + delayInFrames));
    }

    @Override
    public void update(int offsetX, int offsetY) throws Exception {
        super.update(offsetX, offsetY);

        List<ScheduledRunnable> readySchedules = pendingSchedules.stream()
                .filter(schedule -> schedule.canBeRun(currentFrame))
                .peek(schedule -> schedule.run())
                .collect(toList());
        pendingSchedules.removeAll(readySchedules);
    }

    @Override
    public void paint(Graphics g) {
        currentFrame++;
    }

    private class ScheduledRunnable {

        private long runAtFrame;

        private Runnable runnable;

        public ScheduledRunnable(Runnable runnable, long runAtFrame) {
            this.runnable = runnable;
            this.runAtFrame = runAtFrame;
        }

        public boolean canBeRun(long currentFrame) {
            return currentFrame >= runAtFrame;
        }

        public void run() {
            runnable.run();
        }

    }

}
